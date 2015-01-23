package socket.netty.msg;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class MsgHeader implements Serializable {
	private static final long serialVersionUID = 1L;
	private byte uint8start = MessageID.uint8start; // 起始位
	private short uint16Length; // 包大小
	private byte uint8Type = MessageID.uint8Type; // 包类型
	private byte uint8version = MessageID.uint8version; // 协议版本
	private byte uint8business = MessageID.uint8business; // 业务类开
	private byte uint8mid; // 消息ID
	private String persist; // 保留字段 18
	private int uint32seq; // 从 0 开始累加

	// 0x7e

	/**
	 * 
	 * 方法名：tobytes <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年3月10日<br/>
	 * 功能描述：<br/>
	 * <b>把头部对象转化字节</b>
	 * 
	 * @return
	 */
	public byte[] tobytes() {
		ByteBuffer b = ByteBuffer.allocate(getBodylen());

		b.put(uint8start);
		b.put(Converter.unSigned16Int2Bytes(uint16Length));
		b.put(uint8Type);
		b.put(uint8version);
		b.put(uint8business);
		b.put(uint8mid);

		ByteBuffer b_persist = ByteBuffer.allocate(18);

		b.put(b_persist.put(persist.getBytes()).array());

		b.put(Converter.unSigned32LongToBigBytes(uint32seq));

		return b.array();

	}

	/**
	 * 
	 * 方法名：frombytes <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年3月10日<br/>
	 * 功能描述：<br/>
	 * <b>把字节流转化成对象</b>
	 * 
	 * @param b
	 */
	public boolean frombytes(byte[] b) {
		try {
			ByteBuffer bf = ByteBuffer.wrap(b);
			int offset = 0;
			this.uint8start = bf.get(offset);
			offset += 1;
			this.uint16Length = Converter.toUInt16(b, offset);
			offset += 2;
			this.uint8Type = bf.get(offset);
			offset += 1;
			this.uint8version = bf.get(offset);
			offset += 1;
			this.uint8business = bf.get(offset);
			offset += 1;
			this.uint8mid = bf.get(offset);
			offset += 1;
			this.persist = Converter.toGBKString(b, offset, 18);
			offset += 18;
			this.uint32seq = Converter.toUInt32(b, offset);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * 方法名：getBodylen <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年3月10日<br/>
	 * 功能描述：<br/>
	 * <b>头部长度</b>
	 * 
	 * @return
	 */
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
