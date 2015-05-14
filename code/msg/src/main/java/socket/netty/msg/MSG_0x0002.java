package socket.netty.msg;

import socket.netty.msg.AbsMsg;
import socket.netty.msg.MessageID;



/**
 * 
 * ClassName: MSG_0x0002 
 * Reason: 心跳消息 
 * date: 2015年5月11日 下午4:01:48 
 *
 * @author sid
 */
public class MSG_0x0002 extends AbsMsg {

	private static final long serialVersionUID = 1L;

	
	
	@Override
	public String toString() {
		return "MSG_0x0002 [head=" + head + "]";
	}

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
		return new byte[0];
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {
		return false;
	}
}
