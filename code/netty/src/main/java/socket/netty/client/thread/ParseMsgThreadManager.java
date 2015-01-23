package com.hdsx.taxi.dcs.dcsserver.socket.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.dcs.dcsserver.util.PropertiesUtil;
import com.hdsx.taxi.dcs.utils.MsgQueue;

/**
 * 线程池（处理消息）管理
 * 
 * @author cuipengfei
 *
 */
public class ParseMsgThreadManager extends AbsThread {

	private static final Logger logger = LoggerFactory
			.getLogger(ParseMsgThreadManager.class);

	static ParseMsgThreadManager obj;

	public static ParseMsgThreadManager getInstance() {
		if (obj == null)
			obj = new ParseMsgThreadManager();
		return obj;
	}

	private ThreadPoolExecutor threadPool;

	private boolean isStart=true;
	
	public ParseMsgThreadManager() {
		int corePoolSize = Integer.parseInt(PropertiesUtil.getProperties()
				.getProperty("ParseCorePoolSize"));
		int maximunPoolSize = Integer.parseInt(PropertiesUtil.getProperties()
				.getProperty("ParseMaximumPoolSize"));
		int keepAliveTime = Integer.parseInt(PropertiesUtil.getProperties()
				.getProperty("ParseKeepAliveTime"));
		threadPool = new ThreadPoolExecutor(corePoolSize, maximunPoolSize,
				keepAliveTime, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(corePoolSize),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	@Override
	protected void runThread(long delay, long period) {

		new Thread(new ParseThreadManage()).start();
		logger.info("服务器消息处理线程启动完成");

	}

	class ParseThreadManage implements Runnable {

		@Override
		public void run() {
			while (isStart) {
				byte[] rpb = null;
				try {
					rpb = MsgQueue.getRecqueue().take();
					threadPool.execute(new ParseMsgThread(rpb));
				} catch (Exception e) {
					logger.error("消息解析管理线程运行异常", e);
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public void stop() {
		isRun = false;
		isStart=false;
	}

}
