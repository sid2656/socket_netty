package socket.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Calendar;
import java.util.Properties;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.client.thread.HeartBeatThread;
import socket.netty.client.thread.ParseMsgThreadManager;
import socket.netty.client.thread.ReConnectedThread;
import socket.netty.client.thread.ReSendMsgThread;
import socket.netty.msg.AbsMsg;
import socket.netty.msg.Converter;
import socket.netty.msg.UP_CONNECT_REQ;
import utils.utils.PropertiesUtil;

public class TcpClient extends Thread {

	private volatile static TcpClient obj;

	private volatile static Timer yssj;

	public static TcpClient getInstance() {
		if (obj == null) {
			synchronized (TcpClient.class) {
				if (obj == null) {
					obj = new TcpClient();
				}
			}
			obj = new TcpClient();
		}
		return obj;
	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(TcpClient.class);

	Channel ch;
	ChannelInboundHandlerAdapter handler;
	Bootstrap b;
	Channel channel;

	private boolean isLogined = false; // 是否登陆成功
	public ChannelHandlerContext chtx;
	public ChannelFuture cf;

	private int heartbeatdelay = Integer.parseInt(PropertiesUtil
			.getProperties().getProperty("heartbeatdelay"));// 心跳间隔
	private int reconnectdealy = Integer.parseInt(PropertiesUtil
			.getProperties().getProperty("reconnectdealy"));
	private int resendmsgdealy = Integer.parseInt(PropertiesUtil
			.getProperties().getProperty("resendmsgdealy"));
	private int connstate = 0;

	public void reconnect() {
		logger.info("数据端：断线重连线程，当前状态：" + (this.connstate==1?"连接中":"已断线"));
		if (this.connstate != 1){
			try {
				init();
			} catch (Exception e) {
				logger.error("upaclient断线重连初始化失败：", e);
				e.printStackTrace();
			}
		}

	}

	@Override
	public void run() {
		init();
		ReConnectedThread.getInstance().run(this.reconnectdealy * 1000,
				this.reconnectdealy * 1000);
	}

	private void init() {
		EventLoopGroup group = new NioEventLoopGroup();
		Properties p = PropertiesUtil.getProperties();
		try {
//			final LogLevel loglevel = LogLevel.valueOf(p.getProperty("upa_loglevel").toUpperCase());
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(new TcpCodec(),
//									new LoggingHandler(loglevel),
									new TcpClientHandler());
						}
					});

			b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
			cf = b.connect(p.getProperty("upa_hostname"),
					Integer.parseInt(p.getProperty("upa_hostport"))).sync();
			// cf.channel().closeFuture().sync();
			ParseMsgThreadManager.getInstance().run(0, 0);
		} catch (InterruptedException e) {
			logger.error("upaclient初始化失败：", e); //$NON-NLS-1$
			e.printStackTrace();
		} catch (Exception e){
			logger.error("upaclient初始化失败：", e); //$NON-NLS-1$
			e.printStackTrace();
		} finally {
			// group.shutdownGracefully();
		}
	}
	
	/**
	 * 启动定时器
	 */
	private void startTimer(){
    	//每天一点执行
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY,1);
    	cal.set(Calendar.MINUTE,0);
    	cal.set(Calendar.SECOND,0);
    	yssj = new Timer("CountYssjTask");
    	yssj.schedule(new CountYssjTask(),cal.getTime(), 24*3600*1000);
    	logger.info("启动定时更新任务--");
	}

	/**
	 * 设置登陆状态
	 * 
	 * @param b
	 */
	public void loginOK(boolean b) {

		this.isLogined = b;
		//如果是第一次登录成功
		if (this.isLogined) {
			this.connstate = 1;
			ReSendMsgThread.getInstance().run(0,this.resendmsgdealy * 1000);
			HeartBeatThread.getInstance().run(0,this.heartbeatdelay * 1000);
			logger.info("upaclient线程启动成功");
			//启动定时器
			startTimer();
		}
	}

	/**
	 * 断开连接，关闭服务
	 */
	public void stopUpaClient() {
		ReSendMsgThread.getInstance().stop();
		HeartBeatThread.getInstance().stop();
		ParseMsgThreadManager.getInstance().stop();
		chtx.close();
		this.isLogined = false;
		this.connstate = 0;
	}

	/**
	 * 发送消息
	 * 
	 * @param m
	 */
	public void send(AbsMsg m) {
		if (this.isLogined) {
			logger.debug("UPACLINET发送："+Converter.bytes2HexsSpace(m.toBytes()));
			if (chtx != null && chtx.channel().isOpen()) {
				chtx.write(m);
				chtx.flush();
			}
		}
	}

	/**
	 * 只发消息不缓存，用于心跳类消息
	 * 
	 * @param m
	 */
	public void sendWithoutCache(AbsMsg m) {
		if (isLogined) {
			logger.debug("UPACLINET发送WithoutCache："+Converter.bytes2HexsSpace(m.toBytes()));
			if (chtx != null && chtx.channel().isOpen()) {
				chtx.write(m);
				chtx.flush();
			}
		}
	}

	/**
	 * 
	 * login:(发送登陆消息).
	 * 
	 * @author sid
	 */
	public void login() {

		// 打开连接时发送登录消息
		try {
			UP_CONNECT_REQ m = new UP_CONNECT_REQ();

			if (chtx != null && chtx.channel().isOpen()) {
				chtx.write(m);
				chtx.flush();
				logger.info("-------------upa发送登录消息--------------");
			}

		} catch (Exception e) {
			logger.error("upaClient :login() - Exception",e); //$NON-NLS-1$
			e.printStackTrace();
		}
	}

	public ChannelHandlerContext getChtx() {
		return chtx;
	}

	public void setChtx(ChannelHandlerContext chtx) {
		this.chtx = chtx;
	}

	public ChannelFuture getCf() {
		return cf;
	}

	public void setCf(ChannelFuture cf) {
		this.cf = cf;
	}

	public int getConnstate() {
		return connstate;
	}

	public void setConnstate(int connstate) {
		this.connstate = connstate;
	}

	public boolean isLogined() {
		return isLogined;
	}

	public void setLogined(boolean isLogined) {
		this.isLogined = isLogined;
	}
}
