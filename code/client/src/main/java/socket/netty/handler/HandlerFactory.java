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
		int msgid = m.getHead().getMsgid();
		if (logger.isDebugEnabled()) {
			logger.debug("handler工厂产生消息:" + Integer.toHexString(msgid)); //$NON-NLS-1$
		}
		IHandler h = null;
		switch (msgid) {
			case MessageID.MSG_0x2001:
				h = new Handler0x2001();
				break;
			case MessageID.MSG_0x3003:
				h = new Handler0x3003();
				break;
			default:
				break;
		}
		return h;
	}
}
