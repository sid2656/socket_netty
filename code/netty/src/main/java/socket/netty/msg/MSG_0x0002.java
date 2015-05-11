package socket.netty.msg;

import socket.netty.msg.AbsMsg;
import socket.netty.msg.MessageID;



/**
 * 
 * ClassName: Msg00 
 * Reason: 心跳消息 
 * date: 2014年10月15日 下午2:41:47 
 *
 * @author sid
 */
public class MSG_0x0002 extends AbsMsg {

	private static final long serialVersionUID = 1L;

	@Override
	protected int getMsgID() {
		return MessageID.MSG_0x0002;
	}

	@Override
	protected int getBodylen() {
		return 0;
	}

	@Override
	protected byte[] bodytoBytes() {
		return null;
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {
		return false;
	}
}
