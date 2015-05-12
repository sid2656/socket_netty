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
			System.arraycopy(Converter.getBytes(this.jdkh), 0, data, offset, 19);
			offset+=19;
			System.arraycopy(Converter.strToBCD(this.jdsj), 0, data, offset, 7);
			offset+=7;
			System.arraycopy(Converter.toByteArray(this.cljd), 0, data, offset, 32);
			offset+=32;
			System.arraycopy(Converter.toByteArray(this.clwd), 0, data, offset, 32);
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
			this.cphm = Converter.toGBKString(data, offset, 8);
			offset+=8;
			this.sjhm = Converter.toGBKString(data, offset, 11);
			offset+=11;
			this.jdkh = Converter.toGBKString(data, offset, 19);
			offset+=19;
			this.jdkh = Converter.BCDToStr(Arrays.copyOfRange(data, offset, 7));
			offset+=7;
			this.cljd = Converter.bytes2Unsigned32Long(data, offset);
			offset+=32;
			this.clwd = Converter.bytes2Unsigned32Long(data, offset);
			resultState = true;
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MSG_0x0001.class).error("登录消息fromBytes转换异常",e);
			e.printStackTrace();
		}
		return resultState;
	}
}
