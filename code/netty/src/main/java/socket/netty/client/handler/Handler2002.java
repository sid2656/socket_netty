package com.hdsx.taxi.dcs.dcsserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.dcs.dcsserver.socket.UpaClient;
import com.hdsx.taxi.dcs.nettyutil.msg.IMsg;
import com.hdsx.taxi.dcs.nettyutil.msghandler.IHandler;
import com.hdsx.taxi.dcs.upamsg.msg.UP_CONNECT_RSP;

/**
 * 链路登陆应答handler
 * 
 * @author cuipengfei
 *
 */
public class Handler2002 implements IHandler {

	Logger logger = LoggerFactory.getLogger(Handler2002.class);

	// 0x00：成功
	// 0x01：IP地址不正确
	// 0x02：接入码不正确
	// 0x03：用户没有注册
	// 0x04：密码错误
	// 0x05：资源紧张，稍后再连接（已经占用）
	// 0x06：其他

	@Override
	public void doHandle(IMsg m) {
		if (m instanceof UP_CONNECT_RSP) {
			UP_CONNECT_RSP msg = (UP_CONNECT_RSP) m;
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
