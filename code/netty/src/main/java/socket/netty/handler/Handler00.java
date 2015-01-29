package socket.netty.handler;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.msg.AbsMsg;
import socket.netty.msg.Msg00;

/**
 * 登陆应答handler
 * @author sid
 *
 */
public class Handler00 implements IHandler {

	Logger logger = LoggerFactory.getLogger(Handler00.class);

	@Override
	public void doHandle(AbsMsg m, ChannelHandlerContext ctx) {
		if (m instanceof Msg00) {
		} else {
			logger.error("链路登陆应答2002消息强转失败");
		}
	}

}
