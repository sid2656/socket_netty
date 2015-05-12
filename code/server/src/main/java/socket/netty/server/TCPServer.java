package socket.netty.server;

import org.slf4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import socket.netty.msg.AbsMsg;
import utils.utils.LogUtil;


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
	
	private int port = 20104;

	public static ChannelHandlerContext chtx;
	private ChannelFuture cf;
	private Logger logger = LogUtil.getInstance().getLogger(TCPServer.class);

	@Override
	public void run() {
		if (logger.isDebugEnabled()) {
			logger.debug("run() - start"); //$NON-NLS-1$
		}

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
			cf = b.bind(port).sync(); // (7)
		} catch (InterruptedException e) {
			logger.error("run()", e); //$NON-NLS-1$
			e.printStackTrace();
		} finally {
			// workerGroup.shutdownGracefully();
			// bossGroup.shutdownGracefully();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("run() - end"); //$NON-NLS-1$
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
			chtx.channel().write(m);
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
