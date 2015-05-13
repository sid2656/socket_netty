package socket.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.slf4j.Logger;

import socket.netty.cache.MsgCache;
import socket.netty.msg.AbsMsg;
import socket.netty.server.thread.CheckClientThread;
import socket.netty.server.thread.CheckTemClientThread;
import socket.netty.server.thread.ParseMsgThreadManager;
import utils.soket.msg.Converter;
import utils.soket.msg.TCPCodec;
import utils.utils.LogUtil;
import utils.utils.PropertiesUtil;


/**
 * 
 * ClassName: TCPServer 
 * date: 2015年1月29日 下午4:11:19 
 *
 * @author sid
 */
public class TCPServer extends Thread {

	private volatile static TCPServer instance = null;

	public static TCPServer getSingletonInstance() {
		if (instance == null) {
			synchronized (TCPServer.class) {
				if (instance == null) {
					instance = new TCPServer();
				}
			}
			instance = new TCPServer();
		}
		return instance;
	}

	private TCPServer() {}
	
	public static ChannelHandlerContext chtx;
	private ChannelFuture cf;
	private Logger logger = LogUtil.getInstance().getLogger(TCPServer.class);

	@Override
	public void run() {
		if (logger.isDebugEnabled()) {
			logger.debug("run() - start"); //$NON-NLS-1$
		}

		init();


		// 启动临时客户端管理(秒)
		CheckTemClientThread.getInstance().run(
				Integer.parseInt(PropertiesUtil.getProperties().getProperty(
						"tcp.temclientdelay")),
				Integer.parseInt(PropertiesUtil.getProperties().getProperty(
						"tcp.temclientdelay")));

		//启动客户端检查线程
		CheckClientThread.getInstance().run(
				Integer.parseInt(PropertiesUtil.getProperties().getProperty(
						"tcp.clientdelay")),
				Integer.parseInt(PropertiesUtil.getProperties().getProperty(
						"tcp.clientdelay")));
		
		// 启动消息处理
		ParseMsgThreadManager.getInstance().run(0,0);

		if (logger.isDebugEnabled()) {
			logger.debug("run() - end"); //$NON-NLS-1$
		}
	}

	private void init() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class) // (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
								@Override
								public void initChannel(SocketChannel ch)
										throws Exception {
									 ch.pipeline().addLast(new TCPCodec(),new
									 TCPServerHandler());
								}
							}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			cf = b.bind(Integer.parseInt(PropertiesUtil.getProperties().getProperty("serverport"))).sync(); // (7)
		} catch (InterruptedException e) {
			logger.error("run()", e); //$NON-NLS-1$
			e.printStackTrace();
		} finally {
			// workerGroup.shutdownGracefully();
			// bossGroup.shutdownGracefully();
		}
	}

	/**
	 * 
	 * closeConnect
	 *
	 * @author sid
	 */
	public void closeConnect() {
		try {
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * send:(发送消息).
	 *
	 * @author sid
	 * @param m
	 */
	public void send(AbsMsg m) {
		if (chtx != null && chtx.channel().isOpen()) {
			MsgCache.getInstance().put(m);
			chtx.channel().write(m);
			chtx.flush();
		}
	}

	/**
	 * 只发消息不缓存，用于心跳类消息
	 * 
	 * @param m
	 */
	public void sendWithoutCache(AbsMsg m) {
		logger.debug("CLINET发送WithoutCache："+Converter.bytes2HexsSpace(m.toBytes()));
		if (chtx != null && chtx.channel().isOpen()) {
			chtx.write(m);
			chtx.flush();
		}
	}

	public static ChannelHandlerContext getChtx() {
		return chtx;
	}

	public static void setChtx(ChannelHandlerContext chtx) {
		TCPServer.chtx = chtx;
	}

	public ChannelFuture getCf() {
		return cf;
	}

	public void setCf(ChannelFuture cf) {
		this.cf = cf;
	}
}
