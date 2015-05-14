package socket.netty.server.thread;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.soket.msg.ClientManager;

/**
 * 检查长时间未活跃的客户端
 * 
 * @author cuipengfei
 *
 */
public class CheckClientThread extends AbsThread {

	private static final Logger logger = LoggerFactory
			.getLogger(CheckClientThread.class);

	static CheckClientThread obj;

	public static CheckClientThread getInstance() {
		if (obj == null)
			obj = new CheckClientThread();
		return obj;
	}

	private Timer timer = new Timer();

	@Override
	public void runThread(long delay, long period) {
		timer = new Timer();
		try {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					try{
						ClientManager.checkClient();
					}catch(Exception e){
						logger.error("检查登陆客户端异常:",e);
					}
				}
			}, delay * 1000, period * 1000);
		} catch (Exception e) {
			logger.info("检查客户端线程异常：",e);
			e.printStackTrace();
			
		}

	}

}
