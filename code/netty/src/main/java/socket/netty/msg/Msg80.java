package socket.netty.msg;

import utils.utils.LogUtil;


/**
 * 
 * ClassName: Msg80 
 * Reason: 通用应答
 * date: 2014年10月15日 下午2:41:47 
 *
 * @author sid
 */
public class Msg80 extends AbsMsg {

	private static final long serialVersionUID = 1L;
	private short answerid; // 对应的驾服消息的ID
	private byte result;// 结果 0：成功/确认；1：失败；2：消息有误

	@Override
	protected int getMsgID() {
		return MessageID.ID_0x80;
	}


	@Override
	public byte[] bodytoBytes() {
		byte[] data = new byte[1];
		try {
			int offset = 0;
			System.arraycopy(Converter.toByteArray(this.answerid), 0, data, offset, 2);
			offset++;
			data[offset] = this.result;
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(Msg80.class).error("登录消息toBytes转换异常",e);
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public boolean bodyfrombytes(byte[] b) {
		boolean resultState = false;
		try {
			int offset = this.head.getBodylen();
			this.answerid = Converter.bytes2Unsigned8Short(b);
			offset += 2;
			this.result = b[offset];
			resultState = true;
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(Msg80.class).error("登录消息fromBytes转换异常",e);
			e.printStackTrace();
		}
		return resultState;
	}

	@Override
	protected int getBodylen() {
		return 4 + 1 + 1;
	}

	public byte getAnswerid() {
		return answerid;
	}
	public void setAnswerid(byte answerid) {
		this.answerid = answerid;
	}
	public byte getResult() {
		return result;
	}
	public void setResult(byte result) {
		this.result = result;
	}
}
