package socket.netty.server.thread;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.soket.msg.ClientManager;

/**
 * 检查未登陆的客户端
 * 
 * @author cuipengfei
 *
 */
public class CheckTemClientThread extends AbsThread {

	private static final Logger logger = LoggerFactory
			.getLogger(CheckTemClientThread.class);

	static CheckTemClientThread obj;

	public static CheckTemClientThread getInstance() {
		if (obj == null)
			obj = new CheckTemClientThread();
		return obj;
	}

	private Timer timer = new Timer();

	@Override
	public void runThread(long delay, long period) {
		timer = new Timer();
		try{
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					ClientManager.checkTempClient();
				}
			}, delay * 1000, period * 1000);
		}catch(Exception e){
			logger.error("检查临时客户端异常：",e);
		}

	}

}
