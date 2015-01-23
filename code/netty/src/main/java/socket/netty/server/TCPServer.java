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

import com.hdsx.taxi.cq.transprotocol.v0305.core.AbsMsg;

/**
 * 
 * ClassName: TCPServer Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). date: 2014年4月10日 上午10:47:13
 * 
 * @author sid
 */
public class TCPServer extends Thread {

	private int port = 20104;

	public static ChannelHandlerContext chtx;
	private ChannelFuture cf;

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

	private TCPServer() {

	}

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
									if (logger.isDebugEnabled()) {
										logger.debug("$ChannelInitializer<SocketChannel>.initChannel(SocketChannel) - start"); //$NON-NLS-1$
									}
//									ch.pipeline().addLast(new CQTcpCodec(),
//											new TCPServerHandler());
									 ch.pipeline().addLast(new TCPCodec(),new
									 TCPServerHandler());

									if (logger.isDebugEnabled()) {
										logger.debug("$ChannelInitializer<SocketChannel>.initChannel(SocketChannel) - end"); //$NON-NLS-1$
									}
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
		if (logger.isInfoEnabled()) {
			logger.info("send(AbsMsg) - ChannelHandlerContext chtx=" + chtx); //$NON-NLS-1$
		}
		if (chtx != null && chtx.channel().isOpen()) {

			chtx.channel().write(m);
			chtx.flush();

			if (logger.isInfoEnabled()) {
				logger.info("send(AbsMsg) - ChannelHandlerContext chtx.channel().isOpen()=" + chtx.channel().isOpen()); //$NON-NLS-1$
			}
		}
	}

	public void sendWoxing(com.hdsx.taxi.woxing.cqmsg.AbsMsg m) {
//		if (logger.isInfoEnabled()) {
//			logger.info("send(AbsMsg) - ChannelHandlerContext chtx=" + chtx); //$NON-NLS-1$
//		}
//		if (chtx != null && chtx.channel().isOpen()) {
//
//			ChannelFuture cfout = chtx.channel().write(m);
//
//			chtx.flush();
//			if (logger.isInfoEnabled()) {
//				logger.info("sendWoxing: isDone=" + cfout.isDone() + "  isSucess=" + cfout.isSuccess()); //$NON-NLS-1$
//			}
//
//		}

	}

	public void sendWoxingAnsworMsg(com.hdsx.taxi.woxing.cqmsg.AbsMsg message) {
		short uint8mid = message.getHeader().getMsgid();
		int uint32seq = message.getHeader().getSn();
		long newOrderId=message.getHeader().getOrderid();
		logger.info("new oderid:"+newOrderId);
		byte result = 0x00;

		com.hdsx.taxi.woxing.cqmsg.msg.Msg3003 m = new com.hdsx.taxi.woxing.cqmsg.msg.Msg3003();
		m.getHeader().setSn(uint32seq);
		m.setMsgid(uint8mid);
		m.getHeader().setOrderid(newOrderId);
		m.setError(result);
		if (logger.isInfoEnabled()) {
			logger.info("通用应答"); //$NON-NLS-1$
		}
		sendWoxing(m);

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

//	public static void main(String[] args) {
//		TCPServer.getSingletonInstance().start();
//	}

}
