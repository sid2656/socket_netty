package com.hdsx.taxi.dcs.dcsserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.dcs.nettyutil.msghandler.IHandler;
import com.hdsx.taxi.dcs.upamsg.AbsMsg;
import com.hdsx.taxi.dcs.upamsg.MessageID;

/**
 * handler工厂
 * 
 * @author cuipengfei
 *
 */
public class HandlerFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(HandlerFactory.class);

	public static IHandler getHandler(AbsMsg m) {
		int msgid = m.getHead().getMsg_id();
		if (logger.isDebugEnabled()) {
			logger.debug("handler工厂产生消息:" + Integer.toHexString(msgid)); //$NON-NLS-1$
		}
		IHandler h = null;
		switch (msgid) {
			/** 链路类 **/
			case MessageID.UP_CONNECT_RSP:
				h = new Handler2002();
				break;
			case MessageID.UP_DISCONNECT_RSP:
				h = new Handler2004();
				break;
			case MessageID.UP_LINKTEST_RSP:
				h = new Handler2006();
				break;
			case MessageID.UP_CLOSELINK_INFORM:
				h = new Handler9008();
				break;
			default:
				break;
		}
		return h;
	}
}
