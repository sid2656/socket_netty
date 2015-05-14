package socket.netty.handler;

import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.msg.AbsMsg;
import socket.netty.msg.MSG_0x1001;
import socket.netty.msg.MSG_0x2001;
import socket.netty.msg.MSG_0x3003;
import socket.netty.server.TCPServer;
import utils.utils.DateUtil;

/**
 * 
 * 链路登陆handler
 * @author sid
 *
 */
public class Handler0x1001 implements IHandler {

	Logger logger = LoggerFactory.getLogger(Handler0x1001.class);

	public void doHandle(AbsMsg m, ChannelHandlerContext ctx) {
		try {
			if (m instanceof MSG_0x1001) {
				MSG_0x1001 msg = (MSG_0x1001) m;
				logger.info("位置请求:"+msg.toString());
				//通用应答
				MSG_0x3003 response = new MSG_0x3003();
				response.setMsgid(m.getHead().getMsgid());
				response.setState((byte)1);
				response.getHead().setSeq(m.getHead().getSeq());
				TCPServer.getSingletonInstance().sendWithoutCache(response);
				//获取对应信息并处理
				MSG_0x2001 request = new MSG_0x2001();
//				double[] wgs84ToBD09 = EvilTransform.WGS84ToBD09(12345615, 125645891);
				request.setCljd(12323l);
				request.setClwd(45645l);
				request.setCphm("京B12345");
				request.setJdkh("221548198811125454");
				request.setJdsj(DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss"));
				request.setSjhm("12345678901");
				TCPServer.getSingletonInstance().send(request);
			} else {
				logger.error("登录消息强转失败:"+m.toString());
			}
		} catch (Exception e) {
			logger.error("登录消息处理失败"+e);
		}
	}

}
