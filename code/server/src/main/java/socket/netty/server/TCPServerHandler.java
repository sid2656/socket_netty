/**
 * Project Name:main
 * File Name:TCPServerHandler.java
 * Package Name:com.hdsx.taxi.driver.cq.tcp
 * Date:2014年4月10日上午10:49:15
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
 */

package socket.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Timer;

import org.slf4j.Logger;

import socket.netty.msg.ReciPackBean;
import socket.netty.msg.ServerMsgQueue;
import utils.soket.msg.ClientManager;
import utils.utils.LogUtil;

/**
 * ClassName:TCPServerHandler Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2014年4月10日 上午10:49:15
 * 
 * @author sid
 * @see
 */
public class TCPServerHandler extends ChannelInboundHandlerAdapter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LogUtil.getInstance().getLogger(TCPServerHandler.class);
	public static boolean isrunning = false;
	public static Timer timer = new Timer();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			if (msg instanceof byte[]) {
				final byte[] msgbytes = (byte[]) msg;
				ReciPackBean rpb = new ReciPackBean();
				rpb.setChannel(ctx);
				rpb.setMsgbytes(msgbytes);
				ServerMsgQueue.getRecqueue().put(rpb);
			} else {
				logger.error("主handler---消息解码有误，请检查！！");
			}
		} catch (Exception e) {
			logger.error("主handler---接收消息失败", e);
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.info("-------------连接异常关闭--------------");
		try {
			cause.printStackTrace();
			ctx.close();
			ClientManager.removeClient(ctx);
			ClientManager.removeTemClient(ctx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext chtx) throws Exception {
		try {
			super.channelActive(chtx);
			logger.info("-------------临时客户端建立连接--------------");
			TCPServer.setChtx(chtx);
			ClientManager.addTemClient(chtx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		try {
			super.channelInactive(ctx);
			TCPServer.getChtx().close();
			TCPServerHandler.isrunning=false;
			TCPServerHandler.timer.cancel();

			InetSocketAddress address = (InetSocketAddress) ctx.channel()
					.remoteAddress();
			InetAddress inetAdd = address.getAddress();
			logger.info("客户端断开连接：" + ctx.name() 
					+ ",IP:" + inetAdd.getHostAddress()
					+ ",port:" + address.getPort());
			// 记录日志
			ClientManager.removeClient(ctx);
			ClientManager.removeTemClient(ctx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
