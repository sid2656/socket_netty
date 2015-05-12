package socket.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import socket.netty.msg.AbsMsg;

public interface IHandler {

	void doHandle(AbsMsg m, ChannelHandlerContext ctx);

}
