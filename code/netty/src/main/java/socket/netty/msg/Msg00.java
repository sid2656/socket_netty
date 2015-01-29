package socket.netty.msg;



/**
 * 
 * ClassName: Msg00 
 * Reason: 心跳消息 
 * date: 2014年10月15日 下午2:41:47 
 *
 * @author sid
 */
public class Msg00 extends AbsMsg {

	private static final long serialVersionUID = 1L;

	@Override
	protected int getMsgID() {
		return MessageID.ID_0x00;
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
