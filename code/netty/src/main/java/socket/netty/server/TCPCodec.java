package socket.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.ByteBuffer;
import java.util.List;

import com.hdsx.taxi.cq.transprotocol.v0305.core.AbsMsg;

/**
 * 
 * ClassName: TCPCodec 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2014年3月30日 上午10:23:41 
 *
 * @author sid
 */
public class TCPCodec extends ByteToMessageCodec<AbsMsg> {

	/* 解码 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
			List<Object> out){

		if (logger_received.isDebugEnabled())
			logger_received.debug("收到消息:" );

		try {
			if (!searchHead(buffer))
				return;
			int buffLen = buffer.readableBytes();
			int index = buffer.readerIndex();
			if (buffLen < ConfigContent.TCP_HEADER_LENGTH)
				return;

			byte[] lenBytes = new byte[2];
			buffer.getBytes(index + 1, lenBytes);
			int len = Converter.bytes2UnSigned16Int(lenBytes, 0);//获取剩余消息长度
//		int dataLen = len + TCPConstants.TCP_HEADER_LENGTH;//加上消息头长度后数据总长度
			int dataLen = len;//后修改的整个包的大小应该包含消息头的长度
			if (buffLen < dataLen)
				return;

			byte[] msgbytes = new byte[dataLen];
			buffer.readBytes(msgbytes);
			AbsMsg m = this.genMsg(msgbytes);

			if (logger_received.isDebugEnabled()&&m!=null)
				logger_received.debug("收到消息:" + m.toString());
			if(m!=null)out.add(m);
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

	}

	/* 封装 */
	@Override
	protected void encode(ChannelHandlerContext ctx, AbsMsg msg, ByteBuf out){
		try {
			if (logger_send.isDebugEnabled()) {
				logger_send.debug("开始发送消息：" + msg.toString());
			}
			byte[] bt = msg.toBytes();
			logger_send.debug("消息长度:" + bt.length);
			if (logger_send.isDebugEnabled()) {
				StringBuilder sbuiler = new StringBuilder();
				for (byte bb : bt) {
					sbuiler.append("[" + Integer.toHexString(bb)+"]" );
				}
				logger_send.debug("encode——发送消息:" + msg.toString() + "  |data:" + sbuiler.toString());
			}

			out.writeBytes(bt);
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

	}

	/**
	 * 查询消息头
	 * 
	 * @param buffer
	 * @return
	 */
	private boolean searchHead(ByteBuf buffer) {
		int i = 0;
		int buffLen = buffer.readableBytes();
		int readIndex = buffer.readerIndex();
		int startFlat = 0;
		while (i < buffLen) {
			startFlat = buffer.getByte(readIndex + i);
			if (startFlat==(byte)ConfigContent.TCP_HEADER_STARTFLAG) {
				if (i > 0)
					buffer.skipBytes(i);
				return true;
			}
			i++;
		}
		return false;
	}

	private AbsMsg genMsg(byte[] msgbyte) {
		if (logger_received.isDebugEnabled()) {
			logger_received.debug("收到消息-" + getBytesHexString(msgbyte));
		}
		AbsMsg msg = null;
		ByteBuffer buffer = ByteBuffer.wrap(msgbyte);
		int tmp = buffer.get(6)&0xff;
		short msgid = (short)tmp;
		msg = MsgFacotry.genMsg(msgid);

		try {
			if (msg != null) {
				msg.fromBytes(msgbyte);
			}
		} catch (Exception e) {
			logger_received.error("生成消息异常", e);
		}
		return msg;

	}

	static String getBytesHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (Byte b : bytes) {
			sb.append("[" + Integer.toHexString(b) + "]");
		}
		return sb.toString();
	}

}
