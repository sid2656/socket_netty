package socket.netty.msg;

import java.util.Arrays;

import utils.utils.LogUtil;


/**
 * 
 * ClassName: LoginMsg 
 * Reason: 登录消息 
 * date: 2014年10月15日 下午2:41:47 
 *
 * @author sid
 */
public class Msg01 extends AbsMsg {

	private static final long serialVersionUID = 1L;
	private long userid;
	private String connecttime;
	private String mac;

	@Override
	public String toString() {
		return "UP_CONNECT_REQ [userid=" + userid + ", connecttime="
				+ connecttime + ", mac=" + mac + ", getMsgID()=" + getMsgID()
				+ "]";
	}

	@Override
	protected int getMsgID() {
		return MessageID.ID_0x01;
	}


	@Override
	public byte[] bodytoBytes() {
		byte[] data = new byte[4+7+32];
		try {
			int offset = 0;
			System.arraycopy(Converter.toByteArray32Long(this.userid), 0, data, offset, 4);
			offset+=4;
			System.arraycopy(Converter.strToBCD(this.connecttime), 0, data, offset, 7);
			offset+=7;
			System.arraycopy(Converter.getBytes(this.mac), 0, data, offset, Converter.getBytes(this.mac).length);
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(Msg01.class).error("登录消息toBytes转换异常",e);
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public boolean bodyfrombytes(byte[] b) {
		boolean resultState = false;
		int offset = 0;
		try {
			this.userid = Converter.bigBytes2Unsigned32Long(b, offset);
			offset+=4;
			this.connecttime = Converter.BCDToStr(Arrays.copyOfRange(b, offset, offset+7));
			offset+=7;
			this.mac = new String(Arrays.copyOfRange(b, offset, offset+32));
			offset+=32;
			resultState = true;
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(Msg01.class).error("登录消息fromBytes转换异常",e);
			e.printStackTrace();
		}
		return resultState;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getConnecttime() {
		return connecttime;
	}

	public void setConnecttime(String connecttime) {
		this.connecttime = connecttime;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Override
	protected int getBodylen() {
		return 0;
	}

}
