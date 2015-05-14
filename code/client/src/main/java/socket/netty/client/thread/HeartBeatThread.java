package socket.netty.client.thread;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.bean.AbsThread;
import socket.netty.client.TcpClient;
import socket.netty.msg.MSG_0x0002;


public class HeartBeatThread extends AbsThread {
	private static final Logger logger = LoggerFactory
			.getLogger(HeartBeatThread.class);
	static HeartBeatThread obj;
	public static Date lastTime;

	public static HeartBeatThread getInstance() {
		if (obj == null) {
			synchronized (HeartBeatThread.class) {
				if (obj == null) {
					obj = new HeartBeatThread();
				}
			}
			obj = new HeartBeatThread();
		}
		return obj;
	}

	private Timer timer = new Timer();

	/**
	 * 重新发送消息
	 * 
	 * @param dalay
	 * @param period
	 */

	@Override
	protected void runThread(long delay, long period) {
		timer = new Timer();
		try {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					logger.debug("给服务发送心跳");
					MSG_0x0002 m = new MSG_0x0002();
					TcpClient.getInstance().sendWithoutCache(m);
				}
			}, delay, period);
		} catch (Exception e) {
			logger.info("发送心跳消息异常：",e);
			e.printStackTrace();
		}

	}
	public void stop(){
		isRun = false;
		timer.cancel();
	}
}
