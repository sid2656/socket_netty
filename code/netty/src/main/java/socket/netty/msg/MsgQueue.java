package socket.netty.msg;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息队列
 * 
 */
public class MsgQueue {

	/**
	 * 接收数据队列
	 */
	private static LinkedBlockingQueue<byte[]> rec_queue = new LinkedBlockingQueue<byte[]>();

	public static LinkedBlockingQueue<byte[]> getRecqueue() {
		return rec_queue;
	}
	/**
	 * 接收数据队列
	 */
	private static LinkedBlockingQueue<AbsMsg> base_queue = new LinkedBlockingQueue<AbsMsg>();

	public static LinkedBlockingQueue<AbsMsg> getBasequeue() {
		return base_queue;
	}
}
