package socket.netty.msg;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 消息工厂
 * @author sid
 *
 */
public class MsgFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(MsgFactory.class);

	/**
	 * 根据二进制文件生成消息对象
	 * 
	 * @param bytes
	 * @return
	 */
	public static AbsMsg genMsg(MsgHeader head, byte[] msgbytes) {
		ByteBuffer bf = ByteBuffer.wrap(msgbytes);
		int bodylen = msgbytes.length-head.getLength()-2;//去掉末尾
		byte[] body = new byte[bodylen];
		bf.position(bodylen);
		bf.get(body);
		AbsMsg m = null;
		int msg_id = head.getMsgid();
		switch (msg_id) {
			case MessageID.MSG_0x0001:
				m = new MSG_0x0001();
				break;
			default:
				break;
		}
		if (m != null) {
			m.head = head;
		}
		m.bodyfrombytes(body);
		logger.debug("接收到数据解码后："+m.toString());

		return m;
	}
}
