package socket.netty.handler;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.msg.AbsMsg;
import socket.netty.msg.MSG_0x2001;

/**
 * 
 * 链路登陆handler
 * @author sid
 *
 */
public class Handler0x2001 implements IHandler {

	Logger logger = LoggerFactory.getLogger(Handler0x2001.class);

	public void doHandle(AbsMsg m, ChannelHandlerContext ctx) {
		try {
			if (m instanceof MSG_0x2001) {
				MSG_0x2001 msg = (MSG_0x2001) m;
				logger.info("收到位置消息："+msg.toString());
			} else {
				logger.error("登录消息强转失败:"+m.toString());
			}
		} catch (Exception e) {
			logger.error("登录消息处理失败"+e);
		}
	}

}
