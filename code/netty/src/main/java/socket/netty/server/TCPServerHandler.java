/**
 * Project Name:main
 * File Name:TCPServerHandler.java
 * Package Name:com.hdsx.taxi.driver.cq.tcp
 * Date:2014年4月10日上午10:49:15
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
 */

package main.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.hdsx.taxi.cq.transprotocol.v0305.CallAnswerMsg;
import com.hdsx.taxi.cq.transprotocol.v0305.CarPasitionMsg;
import com.hdsx.taxi.cq.transprotocol.v0305.core.AbsMsg;
import com.hdsx.taxi.cq.transprotocol.v0305.core.MessageID;
import com.hdsx.taxi.woxing.cqmsg.handler.HandlerFactory;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

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
	private static final Logger logger = Logger.getLogger(TCPServerHandler.class);
	public static boolean isrunning = false;
	public static Timer timer = new Timer();

	// (1)

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		// if (logger.isDebugEnabled()) {
		//			logger.debug("TCPServerHandler:channelRead(ChannelHandlerContext, Object) - start"); //$NON-NLS-1$
		// }
		// (2)
		// ((ByteBuf) msg).release(); // (3)
		logger.info("消息来了");

		// 发送应答
		if (msg instanceof AbsMsg) {
			if (logger.isDebugEnabled()) {
				logger.debug("TCPServerHandler:channelRead(ChannelHandlerContext, Object) - start com.hdsx.taxi.cq.transprotocol.v0305.core.AbsMsg"); //$NON-NLS-1$
			}

			AbsMsg message = (AbsMsg) msg;
			String json = JsonUtils.getSingletonInstance().obj2Json(message);
			if (logger.isDebugEnabled()) {
				logger.debug("TCPServerHandler:channelRead(ChannelHandlerContext, Object) - json:" + json); //$NON-NLS-1$
			}
			short uint8mid = message.getHeader().getUint8mid();
			int uint32seq = message.getHeader().getUint32seq();
			byte result = 0x00;

			if (uint8mid==MessageID.loginMsg) {
				CallAnswerMsg m = new CallAnswerMsg();
				m.setUint32seq(uint32seq);
				m.setUint8Answerid((byte) uint8mid);
				m.setUint8Result(result);
	
				TCPServer.getSingletonInstance().send(m);
			}
			
			if (!isrunning) {
				timer.cancel();
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						CarPasitionMsg cm = new CarPasitionMsg();
						cm.setCarNumber("京A88888");
						cm.setUint32Lat(456789);
						cm.setUint32Lng(123456);
						cm.setUint8Type((byte)0);
						TCPServer.getSingletonInstance().send(cm);
					}
				}, 1000, 5000);
				isrunning = true;
			}

		} else if (msg instanceof com.hdsx.taxi.woxing.cqmsg.AbsMsg) {
			logger.info("主hangdler接受到消息");
			try {
				if (msg instanceof com.hdsx.taxi.woxing.cqmsg.AbsMsg) {

					com.hdsx.taxi.woxing.cqmsg.AbsMsg m = (com.hdsx.taxi.woxing.cqmsg.AbsMsg) msg;
					logger.info("消息工厂");
					IHandler handler = HandlerFactory.getHandler(m);
					if (handler != null) {
						logger.info("处理消息+"+handler.getClass());
						handler.doHandle(m);
					} else {
						TCPServer.getSingletonInstance().sendWoxingAnsworMsg(m);
						;
					}
				}
			} finally {
				ReferenceCountUtil.release(msg);
			}

			// com.hdsx.taxi.woxing.cqmsg.AbsMsg message =
			// (com.hdsx.taxi.woxing.cqmsg.AbsMsg) msg;
			// String json = JsonUtils.getSingletonInstance().obj2Json(message);
			// if (logger.isDebugEnabled()) {
			//				logger.debug("TCPServerHandler:channelRead(ChannelHandlerContext, Object) - message:"+message.toString()); //$NON-NLS-1$
			// }
			// short uint8mid = message.getHeader().getMsgid();
			// int uint32seq = message.getHeader().getSn();
			// byte result = 0x00;
			//
			// com.hdsx.taxi.woxing.cqmsg.msg.Msg3003 m = new
			// com.hdsx.taxi.woxing.cqmsg.msg.Msg3003();
			// m.getHeader().setSn(uint32seq);
			// m.setMsgid(uint8mid);
			// m.setError(result);
			//
			// TCPServer.getSingletonInstance().sendWoxing(m);

		}
		{
			if (logger.isDebugEnabled()) {
				logger.debug("TCPServerHandler:channelRead(ChannelHandlerContext, Object) - start"); //$NON-NLS-1$
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("TCPServerHandler:channelRead(ChannelHandlerContext, Object) - end"); //$NON-NLS-1$
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
