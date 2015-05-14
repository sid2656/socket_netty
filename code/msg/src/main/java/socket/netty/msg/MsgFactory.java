package socket.netty.msg;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.soket.msg.Constants;
import utils.soket.msg.Converter;

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
		byte xor = 0;
		 // 计算 校验位
		for (int i = 0; i < msgbytes.length-1; i++) {//在codec中已经去掉首尾标示了
			xor ^= msgbytes[i];
		}
		// 判断 校验位
		if (xor != msgbytes[msgbytes.length - 1])
			return null;
		
		ByteBuffer bf = ByteBuffer.wrap(msgbytes);
		logger.info("处理消息："+Converter.bytes2HexsSpace(msgbytes));
		int bodylen = msgbytes.length-Constants.HEAD_LENGTH-Constants.SIGN_LENGTH;//去掉消息头和校验码长度
		byte[] body = new byte[bodylen];
		bf.position(Constants.HEAD_LENGTH+Constants.SIGN_STAR_LENGTH-1);//下标从0开始
		bf.get(body);
		logger.info(bodylen+"处理消息体："+Converter.bytes2HexsSpace(body));
		AbsMsg m = null;
		int msg_id = head.getMsgid();
		logger.info("处理消息id："+msg_id);
		switch (msg_id) {
			case MessageID.MSG_0x0001:
				m = new MSG_0x0001();
				break;
			case MessageID.MSG_0x0002:
				m = new MSG_0x0002();
				break;
			case MessageID.MSG_0x0003:
				m = new MSG_0x0003();
				break;
			case MessageID.MSG_0x1001:
				m = new MSG_0x1001();
				break;
			case MessageID.MSG_0x2001:
				m = new MSG_0x2001();
				break;
			case MessageID.MSG_0x3003:
				m = new MSG_0x3003();
				break;
			default:
				m = new MSG_0x3003();
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
