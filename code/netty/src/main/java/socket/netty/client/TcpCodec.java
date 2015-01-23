package socket.netty.client;

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
 * @author sid
 *
 */
public class TcpCodec extends ByteToMessageCodec<AbsMsg> {

	private static final byte HeadFlag = 0x5b;
	private static final byte EndFlag = 0x5d;
	private static final Logger logger = LoggerFactory
			.getLogger(TcpCodec.class);

	ByteBuffer bf = ByteBuffer.allocate(4096);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
			List<Object> out) throws Exception {
		try {
			while (buffer.readableBytes() > 0) {
				byte b = buffer.readByte();
				bf.put(b);
				if(b==EndFlag){
					byte[] bytes = new byte[bf.position()-2];
					if(bf.get(0)==HeadFlag){
						bf.position(1);
						bf.get(bytes);
						out.add(bytes);
					}
					bf.clear();
				}
			}
		} catch (Exception e) {
			logger.error("解码异常:",e);
			e.printStackTrace();
		}

	}
	
	
	@Override
	protected void encode(ChannelHandlerContext ctx, AbsMsg msg, ByteBuf out)
			throws Exception {
		byte[] bt = msg.toBytes();
		out.writeBytes(bt);

	}

}
