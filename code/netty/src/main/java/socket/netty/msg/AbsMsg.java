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

	/**
	 * 
	 * 方法名：toBytes <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年3月7日<br/>
	 * 功能描述：<br/>
	 * <b>通过实体对象转化成 字节流</b>
	 * 
	 * @return
	 */
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

	/**
	 * 
	 * 方法名：fromBytes <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年3月7日<br/>
	 * 功能描述：<br/>
	 * <b>转化成实体对象</b>
	 * 
	 * @param b
	 */
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

	/**
	 * 
	 * 方法名：getMsgID <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年3月5日<br/>
	 * 功能描述：<br/>
	 * <b>标示</b>
	 * 
	 * @return
	 */
	protected abstract int getMsgID();

	/**
	 * 
	 * 方法名：getBodylen <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年3月5日<br/>
	 * 功能描述：<br/>
	 * <b>消息体长度</b>
	 * 
	 * @return
	 */
	protected abstract int getBodylen();

	/**
	 * 
	 * 方法名：bodytoBytes <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年3月5日<br/>
	 * 功能描述：<br/>
	 * <b>消息体转化成 bytes</b>
	 * 
	 * @return
	 */
	protected abstract byte[] bodytoBytes();

	/**
	 * 
	 * 方法名：bodyfrombytes <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年3月5日<br/>
	 * 功能描述：<br/>
	 * <b>bytes 转化成 object</b>
	 * 
	 * @param b
	 */
	protected abstract boolean bodyfrombytes(byte[] b);

	/**
	 * 
	 * 方法名：getHeader <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年4月9日<br/>
	 * 功能描述：<br/>
	 * <b>获取消息头方法</b>
	 * 
	 * @return
	 */
	public MsgHeader getHeader() {
		return head;
	}

}
