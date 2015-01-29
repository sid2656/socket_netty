package socket.netty.msg;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * 封装hanler类
 * 
 * @author sid
 *
 */
public class ReciPackBean {

	private byte[] msgbytes;
	private ChannelHandlerContext channel;

	public ChannelHandlerContext getChannel() {
		return channel;
	}

	public void setChannel(ChannelHandlerContext channel) {
		this.channel = channel;
	}

	public byte[] getMsgbytes() {
		return msgbytes;
	}

	public void setMsgbytes(byte[] msgbytes) {
		this.msgbytes = msgbytes;
	}

}
