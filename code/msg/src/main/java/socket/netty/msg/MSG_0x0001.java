package socket.netty.msg;

import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;

import utils.soket.msg.Converter;
import utils.utils.DateUtil;
import utils.utils.LogUtil;


/**
 * 
 * ClassName: MSG_0x0001 
 * 登录消息 
 * date: 2015年5月11日 下午4:01:24 
 *
 * @author sid
 */
public class MSG_0x0001 extends AbsMsg {
	
	Logger logger = LogUtil.getInstance().getLogger(MSG_0x0001.class);

	private static final long serialVersionUID = 1L;
	private String connecttime;
	private String md5;


	@Override
	public String toString() {
		return "MSG_0x0001 [connecttime=" + connecttime + ", md5=" + md5
				+ ", head=" + head + "]";
	}


	@Override
	protected int getMsgID() {
		return MessageID.MSG_0x0001;
	}


	@Override
	protected int getBodylen() {
		return 7+32;
	}

	@Override
	public byte[] bodytoBytes() {
		byte[] data = new byte[getBodylen()];
		try {
			this.connecttime = DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss");
			this.md5 = Converter.MD5UTF8(this.connecttime+this.head.getMac());
			int offset = 0;
			System.arraycopy(Converter.strToBCD(this.connecttime), 0, data, offset, 7);
			offset+=7;
			System.arraycopy(Converter.getBytes(this.md5), 0, data, offset, 32);
		} catch (Exception e) {
			logger.error("登录消息toBytes转换异常",e);
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public boolean bodyfrombytes(byte[] b) {
		boolean resultState = false;
		int offset = 0;
		try {
			this.connecttime = Converter.BCDToStr(Arrays.copyOfRange(b, offset, offset+7));
			offset+=7;
			this.md5 = Converter.toGBKString(Arrays.copyOfRange(b, offset, offset+32));
			resultState = true;
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MSG_0x0001.class).error("登录消息fromBytes转换异常",e);
			e.printStackTrace();
		}
		return resultState;
	}

	public String getConnecttime() {
		return connecttime;
	}

	public void setConnecttime(String connecttime) {
		this.connecttime = connecttime;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

}
