package socket.netty.msg;

import java.io.Serializable;
import java.nio.ByteBuffer;


public abstract class AbsMsg implements Serializable {
	private static final long serialVersionUID = 1L;

	protected MsgHeader head;

	static int uint32seq = Integer.MIN_VALUE;

	public AbsMsg() {
		this.head = new MsgHeader();
		this.head.setUint8mid((byte) getMsgID());
		byte[] fs = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		String persist = new String(fs);
		this.head.setPersist(persist);
		this.head.setUint32seq(uint32seq++);

	}

	public byte[] toBytes() {
		// 消息长度
		ByteBuffer bb = ByteBuffer.allocate(1024);

		// 消息内容
		this.head.setUint16Length((short) (getBodylen() + this.head
				.getBodylen()+1));
		byte[] head = this.head.tobytes();
		byte[] body = bodytoBytes();

		// 计算 校验位
		byte xor = 0;
		for (byte bt : head) {
			xor ^= bt;
		}
		for (byte bt : body) {
			xor ^= bt;
		}

		// 保存消息头
		bb.put(head);
		// 消息体
		bb.put(body);
		// 校验位
		bb.put(xor);

		byte[] b = new byte[bb.position()];
		bb.position(0);
		bb.get(b);

		return b;

	}

	public boolean fromBytes(byte[] bs) {

		byte xor = 0;
		 // 计算 校验位
		for (int i = 0; i < bs.length  -1 ; i++) {
			xor ^= bs[i];
		}

		// 判断 校验位
		if (xor != bs[bs.length - 1])
			return false;

		// 解析消息
		this.head = new MsgHeader();
		if (!this.head.frombytes(bs))
			return false;
		if (!bodyfrombytes(bs))
			return false;
		return true;
	}

	protected abstract int getMsgID();

	protected abstract int getBodylen();

	protected abstract byte[] bodytoBytes();

	protected abstract boolean bodyfrombytes(byte[] b);

	public MsgHeader getHeader() {
		return head;
	}

}
