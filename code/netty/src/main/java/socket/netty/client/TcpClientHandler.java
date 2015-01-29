package socket.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.msg.ClientMsgQueue;


public class TcpClientHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(TcpClientHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		try {
			if (msg instanceof byte[]) {

				final byte[] msgbytes = (byte[]) msg;
				try {
					ClientMsgQueue.getRecqueue().put(msgbytes);
				} catch (InterruptedException e) {
					logger.error("upa主handler---接收消息队列存储消息失败", e);
					e.printStackTrace();
				}
			} else {
				logger.error("upa主handler---消息解码有误，请检查！！");
			}

		}catch(Exception e) {
			logger.error("upa主handler---channelRead", e);
			e.printStackTrace();
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		logger.info("-------------upa临时客户端建立连接--------------");
		TcpClient.getInstance().setChtx(ctx);
		TcpClient.getInstance().login();

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress address = (InetSocketAddress) ctx.channel()
				.remoteAddress();
		InetAddress inetAdd = address.getAddress();
		logger.info("upa客户端断开连接：" + ctx.name() + ",IP:" + inetAdd.getHostAddress()
				+ ",port:" + address.getPort());
		TcpClient.getInstance().stopUpaClient();
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		logger.error("upa主handle---rexceptionCaught异常", cause);
		TcpClient.getInstance().stopUpaClient();
		ctx.close();
	}

}
