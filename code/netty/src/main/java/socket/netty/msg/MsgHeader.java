package socket.netty.msg;

import java.io.Serializable;

import utils.utils.LogUtil;

public class MsgHeader implements Serializable {
	private static final long serialVersionUID = 1L;
	private byte uint8start = '@'; // 起始位
	private short uint16Length; // 包大小
	private byte uint8Type = 0x01; // 包类型
	private byte uint8version = 0x00; // 协议版本
	private byte uint8business = 0x12; // 业务类开
	private byte uint8mid; // 消息ID
	private String persist; // 保留字段 18
	private int uint32seq; // 从 0 开始累加


	public byte[] tobytes() {

		byte[] data = new byte[getBodylen()];
		try {
			int offset = 0;
			data[offset] = uint8start;
			offset++;
			System.arraycopy(Converter.toByteArray(this.uint16Length), 0, data, offset, 2);
			offset+=2;
			data[offset] = uint8Type;
			offset++;
			data[offset] = uint8version;
			offset++;
			data[offset] = uint8business;
			offset++;
			data[offset] = uint8mid;
			offset++;
			this.persist = Converter.fillDataPrefix(this.persist, 18, "0");
			System.arraycopy(Converter.getBytes(this.persist), 0, data, offset, 18);
			offset+=18;
			System.arraycopy(Converter.toByteArray16Int(uint32seq), 0, data, offset, 4);
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(Msg01.class).error("登录消息toBytes转换异常",e);
			e.printStackTrace();
		}
		return data;
//		ByteBuffer b = ByteBuffer.allocate(getBodylen());
//
//		b.put(uint8start);
//		b.put(Converter.unSigned16Int2Bytes(uint16Length));
//		b.put(uint8Type);
//		b.put(uint8version);
//		b.put(uint8business);
//		b.put(uint8mid);
//
//		ByteBuffer b_persist = ByteBuffer.allocate(18);
//
//		b.put(b_persist.put(persist.getBytes()).array());
//
//		b.put(Converter.unSigned32LongToBigBytes(uint32seq));
//
//		return b.array();
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
			uint8start = data[offset];
			offset++;
			this.uint16Length = (short) Converter.bigBytes2Unsigned16Int(data, offset);
			offset+=2;
			uint8Type = data[offset];
			offset++;
			uint8version = data[offset];
			offset++;
			uint8business = data[offset];
			offset++;
			uint8mid = data[offset];
			offset++;
			resultState = true;
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(Msg01.class).error("登录消息fromBytes转换异常",e);
			e.printStackTrace();
		}
		return resultState;
//		try {
//			ByteBuffer bf = ByteBuffer.wrap(b);
//			int offset = 0;
//			this.uint8start = bf.get(offset);
//			offset += 1;
//			this.uint16Length = Converter.toUInt16(b, offset);
//			offset += 2;
//			this.uint8Type = bf.get(offset);
//			offset += 1;
//			this.uint8version = bf.get(offset);
//			offset += 1;
//			this.uint8business = bf.get(offset);
//			offset += 1;
//			this.uint8mid = bf.get(offset);
//			offset += 1;
//			this.persist = Converter.toGBKString(b, offset, 18);
//			offset += 18;
//			this.uint32seq = Converter.toUInt32(b, offset);
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
	}

	public int getBodylen() {
		return 1 + 2 + 1 + 1 + 1 + 1 + 18 + 4;
	}

	public byte getUint8start() {
		return uint8start;
	}

	public void setUint8start(byte uint8start) {
		this.uint8start = uint8start;
	}

	public short getUint16Length() {
		return uint16Length;
	}

	public void setUint16Length(short uint16Length) {
		this.uint16Length = uint16Length;
	}

	public byte getUint8Type() {
		return uint8Type;
	}

	public void setUint8Type(byte uint8Type) {
		this.uint8Type = uint8Type;
	}

	public byte getUint8version() {
		return uint8version;
	}

	public void setUint8version(byte uint8version) {
		this.uint8version = uint8version;
	}

	public byte getUint8business() {
		return uint8business;
	}

	public void setUint8business(byte uint8business) {
		this.uint8business = uint8business;
	}

	public short getUint8mid() {
		int tmp = uint8mid & 0xff;
		short result = (short) tmp;
		return result;
		// return uint8mid;
	}

	public void setUint8mid(byte uint8mid) {
		this.uint8mid = uint8mid;
	}

	public String getPersist() {
		return persist;
	}

	public void setPersist(String persist) {
		this.persist = persist;
	}

	public int getUint32seq() {
		return uint32seq;
	}

	public void setUint32seq(int uint32seq) {
		this.uint32seq = uint32seq;
	}

}
