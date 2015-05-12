package socket.netty.msg;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息队列
 * 
 */
public class ClientMsgQueue {

	/**
	 * 接收数据队列
	 */
	private static LinkedBlockingQueue<ReciPackBean> rec_queue = new LinkedBlockingQueue<ReciPackBean>();

	public static LinkedBlockingQueue<ReciPackBean> getRecqueue() {
		return rec_queue;
	}
}
