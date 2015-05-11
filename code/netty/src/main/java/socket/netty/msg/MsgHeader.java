package socket.netty.msg;

import java.io.Serializable;

import socket.netty.Converter;
import utils.utils.LogUtil;

public class MsgHeader implements Serializable {
	private static final long serialVersionUID = 1L;
	public int HEAD_LENGTH = 13;
	private int msgid; //消息ID
	private long seq; //从 0 开始累加
	private String mac; //接入平台标识
	private int length; //剩余消息总长度


	public byte[] tobytes() {

		byte[] data = new byte[getLength()];
		try {
			int offset = 0;
			System.arraycopy(Converter.toByteArray(this.msgid), 0, data, offset, 2);
			offset+=2;
			System.arraycopy(Converter.toByteArray32Long(this.seq), 0, data, offset, 4);
			offset+=4;
//			this.mac = Converter.fillDataPrefix(this.mac, 6, "0");
			System.arraycopy(Converter.getBytes(this.mac), 0, data, offset, 6);
			offset+=6;
			System.arraycopy(Converter.toByteArray(this.length), 0, data, offset, 2);
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MSG_0x0001.class).error("消息头toBytes转换异常",e);
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public boolean frombytes(byte[] data) {
		boolean resultState = false;
		int offset = 0;
		try {
			this.msgid = Converter.bytes2UnSigned16Int(data, offset);
			offset+=2;
			this.seq = Converter.bytes2Unsigned32Long(data, offset);
			offset+=4;
			this.mac = Converter.toGBKString(data, offset, offset + 6);
			offset += 6;
			this.length = Converter.bigBytes2Unsigned16Int(data, offset);
			offset+=2;
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
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}

}
