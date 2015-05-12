package socket.netty.msg;

/**
 * 
 * 消息体接口
 * @author sid
 *
 */
public interface IMsg {
	/**
	 * 消息转换为字节数组
	 * 
	 * @return
	 */
	byte[] toBytes();

	/**
	 * 字节数组转换为消息
	 * 
	 * @param b
	 * @return
	 */
	boolean fromBytes(byte[] b);
}
