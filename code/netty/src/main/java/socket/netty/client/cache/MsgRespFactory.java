package socket.netty.client.cache;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.Converter;
import socket.netty.msg.AbsMsg;
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
		byte[] body = new byte[msgbytes.length - head.getHeadLen() - 2];
		bf.position(head.getHeadLen());
		bf.get(body);
		if (head.getEncrypt_flag() == 1) {
			body = Encrypt.encryptUtil(head.getEncrypt_key(), body,
					body.length, head.getM1(), head.getIA1(), head.getIC1());
		}
		AbsMsg m = null;
		int msg_id = head.getMsg_id();
		logger.debug("收到消息id：{}",msg_id);
		logger.debug("收到消息body：{}",Converter.bytes2HexsSpace(body));
		if (msg_id != MessageID.UP_EXG_MSG&&msg_id != MessageID.UP_SUM_MSG
				&& msg_id == MessageID.UP_BASE_MSG) {
		}
		/**
		 * 如果有子业务类型的把子业务类型放进消息头里面方便handle使用
		 */
		head.setMsg_id(msg_id);
		switch (msg_id) {
			/** 链路类 **/
			case MessageID.UP_CONNECT_RSP:
				m = new UP_CONNECT_RSP();
				break;
			case MessageID.UP_DISCONNECT_RSP:
				m = new UP_DISCONNECT_RSP();
				break;
			case MessageID.UP_LINKTEST_RSP:
				m = new UP_LINKTEST_RSP();
				break;
			case MessageID.UP_CLOSELINK_INFORM:
				m = new UP_CLOSELINK_INFORM();
				break;
			default:
				m = new CommonAnswerMsg();
				break;
		}
		if (m != null) {
			m.setHead(head);
		}
		m.bodyfromBytes(body);
		return m;
	}
}
