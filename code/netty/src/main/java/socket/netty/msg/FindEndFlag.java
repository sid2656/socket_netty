package socket.netty.msg;


/**
 * ***************************************************************************** <br/>
 * <b>类名:FindEndFlag</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：....<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class FindEndFlag {

	/**
	 * 
	 * 方法名：findEndFlag <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2014年4月14日<br/>
	 * 功能描述：<br/>
	 * <b>从开始位找到指定位</b>
	 * 
	 * @param b
	 * @param offset
	 * @return
	 */
	public static int getFirstStringEndFlag(byte[] b, int offset) {
		for (int i = offset; i < b.length; i++) {
			if (b[i] == 0x00)
				return i;
		}
		return -1;
	}
}
