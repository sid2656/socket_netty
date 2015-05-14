package socket.netty.msg;

import org.slf4j.Logger;

import utils.soket.msg.Converter;
import utils.utils.LogUtil;



/**
 * 
 * ClassName: MSG_0x1001 
 * Reason: 位置请求消息
 * date: 2015年5月11日 下午4:18:08 
 *
 * @author sid
 */
public class MSG_0x1001 extends AbsMsg {
	
	Logger logger = LogUtil.getInstance().getLogger(MSG_0x1001.class);

	private static final long serialVersionUID = 1L;
	
	private String id;

	@Override
	public String toString() {
		return "MSG_0x1001 [logger=" + logger + ", id=" + id + ", head=" + head
				+ "]";
	}

	@Override
	protected int getMsgID() {
		return MessageID.MSG_0x1001;
	}

	@Override
	protected int getBodylen() {
		return 18;
	}

	@Override
	protected byte[] bodytoBytes() {
		byte[] data = new byte[getBodylen()];
		try {
			int offset = 0;
			System.arraycopy(Converter.getBytes(id), 0, data, offset, 18);
		} catch (Exception e) {
			logger.error("位置消息toBytes转换异常",e);
			e.printStackTrace();
		}
		return data;
	}

	@Override
	protected boolean bodyfrombytes(byte[] data) {
		boolean resultState = false;
		int offset = 0;
		try {
			this.id = Converter.toGBKString(data, offset, 18);
			resultState = true;
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MSG_0x0001.class).error("获取位置消息fromBytes转换异常",e);
			e.printStackTrace();
		}
		return resultState;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
