package utils.soket.msg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.msg.AbsMsg;

/**
 * 
 * 编解码
 * @author sid
 *
 */
public class TCPCodec extends ByteToMessageCodec<AbsMsg> {
	private static final byte HeadFlag = 0x5b;
	private static final byte EndFlag = 0x5d;
	private static final Logger logger = LoggerFactory.getLogger(TCPCodec.class);
	private ByteBuffer bf = ByteBuffer.allocate(1024);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
			List<Object> out) throws Exception {
		logger.info("开始解码：");
		try {
			while (buffer.readableBytes() > 0) {
				byte b = buffer.readByte();
				bf.put(b);
				if(b==EndFlag){
					byte[] bytes = new byte[bf.position()-Constants.SIGN_STAR_LENGTH-Constants.SIGN_END_LENGTH];
					if(bf.get(0)==HeadFlag){//找到头尾后去除
						bf.position(1);
						bf.get(bytes);
						out.add(bytes);
						bytes = null;
					}
					bf.clear();
				}
			}
		} catch (Exception e) {
			logger.error("解码异常:"+e.toString());
		}

	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, AbsMsg msg, ByteBuf out)
			throws Exception {
		byte[] bt = msg.toBytes();
		logger.info("发送消息："+Converter.bytes2HexsSpace(bt));
		out.writeBytes(bt);
	}
}
