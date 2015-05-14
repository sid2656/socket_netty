package socket.netty.msg;

import org.slf4j.Logger;

import utils.soket.msg.Converter;
import utils.utils.LogUtil;



/**
 * 
 * ClassName: Msg00 
 * Reason: 服务器端通用应答
 * date: 2014年10月15日 下午2:41:47 
 *
 * @author sid
 */
public class MSG_0x3003 extends AbsMsg {
	
	Logger logger = LogUtil.getInstance().getLogger(MSG_0x3003.class);

	private static final long serialVersionUID = 1L;
	
	private int msgid;
	private byte state;
	private String errormsg="";

	@Override
	public String toString() {
		return "MSG_0x3003 [logger=" + logger + ", msgid=" + msgid + ", state="
				+ state + ", errormsg=" + errormsg + ", head=" + head + "]";
	}

	@Override
	protected int getMsgID() {
		return MessageID.MSG_0x3003;
	}

	@Override
	protected int getBodylen() {
		return 2+1+Converter.getBytes(errormsg).length;
	}

	@Override
	protected byte[] bodytoBytes() {
		byte[] data = new byte[getBodylen()];
		try {
			int offset = 0;
			System.arraycopy(Converter.toByteArray16Int(this.msgid), 0, data, offset, 2);
			offset+=2;
			data[offset]=state;
			offset+=1;
			System.arraycopy(Converter.getBytes(errormsg), 0, data, offset, Converter.getBytes(errormsg).length);
		} catch (Exception e) {
			logger.error("服务器通用应答toBytes转换异常",e);
			e.printStackTrace();
		}
		return data;
	}

	@Override
	protected boolean bodyfrombytes(byte[] data) {
		boolean resultState = false;
		int offset = 0;
		try {
			this.msgid = Converter.bytes2UnSigned16Int(data, offset);
			offset+=2;
			this.state = data[offset];
			offset += 1;
			if(this.state==0){
				this.errormsg = Converter.toGBKString(data, offset, data.length-offset);
			}
			resultState = true;
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MSG_0x0001.class).error("登录消息fromBytes转换异常",e);
			e.printStackTrace();
		}
		return resultState;
	}

	public int getMsgid() {
		return msgid;
	}

	public void setMsgid(int msgid) {
		this.msgid = msgid;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
}
