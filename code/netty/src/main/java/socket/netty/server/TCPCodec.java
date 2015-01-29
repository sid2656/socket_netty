package socket.netty.server;

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
	private ByteBuffer bf = ByteBuffer.allocate(10*1024*1024);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
			List<Object> out) throws Exception {
		try {
			while (buffer.readableBytes() > 0) {
				byte b = buffer.readByte();
				bf.put(b);
				if(b==EndFlag){
					byte[] bytes = new byte[bf.position()-2];
					byte[] msgbytes = new byte[bf.position()];
					if(bf.get(0)==HeadFlag){
						bf.position(1);
						bf.get(bytes);
						bf.position(0);
						bf.get(msgbytes);
						out.add(bytes);
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
		out.writeBytes(bt);
	}
}
