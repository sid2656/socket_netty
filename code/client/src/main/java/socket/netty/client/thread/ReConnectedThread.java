package socket.netty.client.thread;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socket.netty.bean.AbsThread;
import socket.netty.client.TcpClient;

public class ReConnectedThread extends AbsThread {

	static ReConnectedThread obj;
	private static final Logger logger = LoggerFactory
			.getLogger(ReConnectedThread.class);

	public static ReConnectedThread getInstance() {
		if (obj == null) {
			synchronized (ReConnectedThread.class) {
				if (obj == null) {
					obj = new ReConnectedThread();
				}
			}
			obj = new ReConnectedThread();
		}
		return obj;
	}

	private Timer timer = new Timer();

	/**
	 * 重新连接
	 * 
	 * @param dalay
	 * @param period
	 */

	@Override
	public void runThread(long delay, long period) {
		timer = new Timer();
		try {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					TcpClient.getInstance().reconnect();
				}
			}, delay, period);
		} catch (Exception e) {
			logger.info("重连异常：",e);
			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		isRun = false;
		timer.cancel();
	}
}
