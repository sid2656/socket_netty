package socket.netty.bean;

/**
 * 
 * 线程抽象类
 * @author sid
 *
 */
public abstract class AbsThread {

	public boolean isRun = false;

	public void run(long delay, long period) {
		if (isRun)
			return;
		isRun = true;
		runThread(delay, period);
	}

	protected abstract void runThread(long delay, long period);
	
	public abstract void stop();

}
