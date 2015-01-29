package socket.netty.handler;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.client.TcpClient;
import socket.netty.msg.AbsMsg;
import socket.netty.msg.Msg80;

/**
 * 
 * 链路登陆应答handler
 * @author sid
 *
 */
public class Handler80 implements IHandler {

	Logger logger = LoggerFactory.getLogger(Handler80.class);

	@Override
	public void doHandle(AbsMsg m, ChannelHandlerContext ctx) {
		if (m instanceof Msg80) {
			Msg80 msg = (Msg80) m;
			byte result = msg.getResult();
			String sResult = "";
			boolean isLogin = false;
			switch (result) {
				/** 链路类 **/
				case 0x00:
					isLogin = true;
					sResult = "upaclient登陆成功";
					break;
				case 0x01:
					sResult = "Ip地址不正确";
					break;
				case 0x02:
					sResult = "接入码不正确";
					break;
				case 0x03:
					sResult = "用户没有注册";
					break;
				case 0x04:
					sResult = "密码错误";
					break;
				case 0x05:
					sResult = "资源紧张，稍候再连接";
					break;
				case 0x06:
					sResult = "其它原因";
					break;
				default:
					sResult = "没有找到对应类型：" + result;
					break;
			}
			logger.info(sResult);
			TcpClient.getInstance().loginOK(isLogin);
		} else {
			logger.error("链路登陆应答2002消息强转失败");
		}
	}

}
