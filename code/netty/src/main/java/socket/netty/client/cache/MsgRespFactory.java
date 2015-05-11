package socket.netty.client.cache;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.Converter;
import socket.netty.msg.AbsMsg;
import socket.netty.msg.MSG_0x0001;
import socket.netty.msg.MSG_0x0002;
import socket.netty.msg.MSG_0x0003;
import socket.netty.msg.MSG_0x1001;
import socket.netty.msg.MSG_0x2001;
import socket.netty.msg.MSG_0x3003;
import socket.netty.msg.MessageID;
import socket.netty.msg.MsgHeader;

/**
 * 消息工厂
 * 
 * @author sid
 *
 */
public class MsgRespFactory {

	private static final Logger logger = LoggerFactory.getLogger(MsgRespFactory.class);

	/**
	 * 根据二进制文件生成消息对象
	 * 
	 * @param bytes
	 * @return
	 */
	public static AbsMsg genMsg(MsgHeader head, byte[] msgbytes) {
		logger.debug("收到消息内容：{}",Converter.bytes2HexsSpace(msgbytes));
		ByteBuffer bf = ByteBuffer.wrap(msgbytes);
		byte[] body = new byte[msgbytes.length - head.HEAD_LENGTH - 2];
		bf.position(head.HEAD_LENGTH);
		bf.get(body);
		AbsMsg m = null;
		int msg_id = head.getMsgid();
		logger.debug("收到消息id：{}",msg_id);
		logger.debug("收到消息body：{}",Converter.bytes2HexsSpace(body));
		/**
		 * 如果有子业务类型的把子业务类型放进消息头里面方便handle使用
		 */
		head.setMsgid(msg_id);
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
			m.setHead(head);
		}
		m.fromBytes(body);
		return m;
	}
}
