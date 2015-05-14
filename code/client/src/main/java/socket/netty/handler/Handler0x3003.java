package socket.netty.handler;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.cache.MsgCache;
import socket.netty.client.TcpClient;
import socket.netty.msg.AbsMsg;
import socket.netty.msg.MSG_0x3003;
import socket.netty.msg.MessageID;

/**
 * 
 * 链路登陆handler
 * @author sid
 *
 */
public class Handler0x3003 implements IHandler {

	Logger logger = LoggerFactory.getLogger(Handler0x3003.class);

	public void doHandle(AbsMsg m, ChannelHandlerContext ctx) {
		try {
			if (m instanceof MSG_0x3003) {
				MSG_0x3003 msg = (MSG_0x3003)m;
				//去除消息缓存
				MsgCache.getInstance().remove(msg.getMsgid()+";"+msg.getHead().getSeq());
				if(msg.getMsgid()==MessageID.MSG_0x0001&&msg.getState()==1)
					TcpClient.getInstance().loginOK(true);
			} else {
				logger.error("通用应答消息强转失败:"+m.toString());
			}
		} catch (Exception e) {
			logger.error("通用应答处理失败"+e);
		}
	}

}
