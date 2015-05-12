/**
 * Project Name:main
 * File Name:MsgObj.java
 * Package Name:com.hdsx.taxi.driver.cq.tcp.cache
 * Date:2014年4月16日上午11:16:54
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package socket.netty.cache;

import java.io.Serializable;
import java.util.Date;

import socket.netty.msg.AbsMsg;

/**
 * 
 * map中存的对象
 * ClassName: MsgObj 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2014年3月30日 上午10:19:32 
 *
 * @author sid
 */
public class MsgObj implements Serializable{
	private static final long serialVersionUID = 1L;
	Date sendtime;
	Date createtime;
	int sendedcount;
	AbsMsg msg;
	public MsgObj(AbsMsg msg) {
		this.sendtime = new Date();
		this.createtime = new Date();
		sendedcount = 1;
		this.msg = msg;
	}
	public Date getSendtime() {
		return sendtime;
	}
	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}
	public int getSendedcount() {
		return sendedcount;
	}
	public void setSendedcount(int sendedcount) {
		this.sendedcount = sendedcount;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public AbsMsg getMsg() {
		return msg;
	}
	public void setMsg(AbsMsg msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "MsgObj [sendtime=" + sendtime + ", sendedcount="
				+ sendedcount + ", msg=" + msg + "]";
	}
}