package socket.netty.handler;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.bean.TcpUser;
import socket.netty.msg.AbsMsg;
import socket.netty.msg.MSG_0x0001;
import socket.netty.msg.MSG_0x3003;
import socket.netty.server.TCPServer;
import utils.soket.msg.ClientManager;
import utils.utils.DataTypeUtil;

/**
 * 
 * 链路登陆handler
 * @author sid
 *
 */
public class Handler0x0001 implements IHandler {

	Logger logger = LoggerFactory.getLogger(Handler0x0001.class);

	// 0x00：成功
	// 0x01：IP地址不正确
	// 0x02：接入码不正确
	// 0x03：用户没有注册
	// 0x04：密码错误
	// 0x05：资源紧张，稍后再连接（已经占用）
	// 0x06：其他

	public void doHandle(AbsMsg m, ChannelHandlerContext ctx) {
		MSG_0x3003 response = new MSG_0x3003();
		logger.info("处理登录消息");
		try {
			if (m instanceof MSG_0x0001) {
				MSG_0x0001 msg = (MSG_0x0001) m;
				String mac = msg.getHead().getMac();
				response.setMsgid(msg.getHead().getMsgid());
				//TODO 用户校验工作
				TcpUser user = getTcpUserByMac(mac);
				if(DataTypeUtil.isNotEmpty(user)){
					response.setState((byte)1);
					ClientManager.addClient(ctx, user);
				}else{
					response.setState((byte)0);
					response.setErrormsg("mac校验错误");
				}
				TCPServer.getSingletonInstance().sendWithoutCache(response);
			} else {
				logger.error("登录消息强转失败:"+m.toString());
			}
		} catch (Exception e) {
			logger.error("登录消息处理失败"+e);
		}
	}

	private TcpUser getTcpUserByMac(String mac) {
		return new TcpUser();
	}

}
