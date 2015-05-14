package socket.netty.msg;

import java.util.Arrays;

import org.slf4j.Logger;

import utils.soket.msg.Converter;
import utils.utils.LogUtil;



/**
 * 
 * ClassName: MSG_0x2001 
 * Reason: 位置消息 
 * date: 2015年5月11日 下午4:25:01 
 *
 * @author sid
 */
public class MSG_0x2001 extends AbsMsg {
	
	Logger logger = LogUtil.getInstance().getLogger(MSG_0x2001.class);

	private static final long serialVersionUID = 1L;
	
	private String cphm;
	private String sjhm;
	private String jdkh;
	private String jdsj;
	private long cljd;
	private long clwd;

	@Override
	public String toString() {
		return "MSG_0x2001 [logger=" + logger + ", cphm=" + cphm + ", sjhm="
				+ sjhm + ", jdkh=" + jdkh + ", jdsj=" + jdsj + ", cljd=" + cljd
				+ ", clwd=" + clwd + ", head=" + head + "]";
	}

	@Override
	protected int getMsgID() {
		return MessageID.MSG_0x2001;
	}

	@Override
	protected int getBodylen() {
		return 8+11+19+7+32+32;
	}

	@Override
	protected byte[] bodytoBytes() {
		byte[] data = new byte[getBodylen()];
		try {
			int offset = 0;
			System.arraycopy(Converter.getBytes(this.cphm), 0, data, offset, 8);
			offset+=8;
			System.arraycopy(Converter.getBytes(this.sjhm), 0, data, offset, 11);
			offset+=11;
			System.arraycopy(Converter.getBytes(this.jdkh), 0, data, offset, 18);
			offset+=18;
			System.arraycopy(Converter.strToBCD(this.jdsj), 0, data, offset, 7);
			offset+=7;
			System.arraycopy(Converter.toByteArray32Long(this.cljd), 0, data, offset, 4);
			offset+=4;
			System.arraycopy(Converter.toByteArray32Long(this.clwd), 0, data, offset, 4);
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
			this.cphm = Converter.toGBKString(data, offset, offset+8);
			offset+=8;
			this.sjhm = Converter.toGBKString(data, offset, offset+11);
			offset+=11;
			this.jdkh = Converter.toGBKString(data, offset, offset+18);
			offset+=18;
			this.jdsj = Converter.BCDToStr(Arrays.copyOfRange(data, offset, offset+7));
			offset+=7;
			this.cljd = Converter.bytes2Unsigned32Long(data, offset);
			offset+=32;
			this.clwd = Converter.bytes2Unsigned32Long(data, offset);
			resultState = true;
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MSG_0x2001.class).error("位置消息fromBytes转换异常",e);
			e.printStackTrace();
		}
		return resultState;
	}

	public String getCphm() {
		return cphm;
	}

	public void setCphm(String cphm) {
		this.cphm = cphm;
	}

	public String getSjhm() {
		return sjhm;
	}

	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}

	public String getJdkh() {
		return jdkh;
	}

	public void setJdkh(String jdkh) {
		this.jdkh = jdkh;
	}

	public String getJdsj() {
		return jdsj;
	}

	public void setJdsj(String jdsj) {
		this.jdsj = jdsj;
	}

	public long getCljd() {
		return cljd;
	}

	public void setCljd(long cljd) {
		this.cljd = cljd;
	}

	public long getClwd() {
		return clwd;
	}

	public void setClwd(long clwd) {
		this.clwd = clwd;
	}
}
