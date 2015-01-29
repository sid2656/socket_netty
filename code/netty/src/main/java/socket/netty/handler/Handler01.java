package socket.netty.handler;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.client.TcpClient;
import socket.netty.msg.AbsMsg;
import socket.netty.msg.Msg01;
import socket.netty.msg.Msg80;

/**
 * 
 * 链路登陆handler
 * @author sid
 *
 */
public class Handler01 implements IHandler {

	Logger logger = LoggerFactory.getLogger(Handler01.class);

	// 0x00：成功
	// 0x01：IP地址不正确
	// 0x02：接入码不正确
	// 0x03：用户没有注册
	// 0x04：密码错误
	// 0x05：资源紧张，稍后再连接（已经占用）
	// 0x06：其他

	@Override
	public void doHandle(AbsMsg m, ChannelHandlerContext ctx) {
		if (m instanceof Msg01) {
			Msg01 msg = (Msg01) m;
			msg.getUserid();
			msg.getMac();
			//TODO 用户校验工作
			Msg80 response = new Msg80();
			response.setAnswerid(msg.getHeader().getUint8mid());
		} else {
			logger.error("链路登陆应答2002消息强转失败");
		}
	}

}
