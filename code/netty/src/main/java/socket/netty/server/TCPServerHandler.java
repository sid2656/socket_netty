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

import java.util.Timer;

import org.slf4j.Logger;

import socket.netty.msg.ReciPackBean;
import socket.netty.msg.ServerMsgQueue;
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

	// (1)

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		logger.info("消息来了");

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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext chtx) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("TCPServerHandler:channelActive(ChannelHandlerContext) - start"); //$NON-NLS-1$
		}

		super.channelActive(chtx);
		TCPServer.setChtx(chtx);
//		SendLocationThread.getInstance().run();
		if (logger.isDebugEnabled()) {
			logger.debug("TCPServerHandler:channelActive(ChannelHandlerContext) - end"); //$NON-NLS-1$
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("TCPServerHandler:channelInactive(ChannelHandlerContext) - start"); //$NON-NLS-1$
		}

		super.channelInactive(ctx);
		TCPServer.getChtx().close();
		TCPServerHandler.isrunning=false;
		TCPServerHandler.timer.cancel();

		if (logger.isDebugEnabled()) {
			logger.debug("TCPServerHandler:channelInactive(ChannelHandlerContext) - end"); //$NON-NLS-1$
		}
	}

}
