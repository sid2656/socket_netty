package socket.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.msg.AbsMsg;
import socket.netty.msg.MessageID;

/**
 * 
 * handler工厂
 * 
 * @author sid
 *
 */
public class HandlerFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(HandlerFactory.class);

	public static IHandler getHandler(AbsMsg m) {
		int msgid = m.getHeader().getUint8mid();
		if (logger.isDebugEnabled()) {
			logger.debug("handler工厂产生消息:" + Integer.toHexString(msgid)); //$NON-NLS-1$
		}
		IHandler h = null;
		switch (msgid) {
			/** 链路类 **/
			case MessageID.ID_0x00:
				h = new Handler00();
				break;
			case MessageID.ID_0x01:
				h = new Handler01();
				break;
			case MessageID.ID_0x80:
				h = new Handler80();
				break;
			default:
				break;
		}
		return h;
	}
}
