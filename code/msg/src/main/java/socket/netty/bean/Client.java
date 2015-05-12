package socket.netty.bean;

import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * 客户端实体
 * 
 * @author sid
 *
 */
public class Client {

	private ChannelHandlerContext channel;
	private Date lastUpTime = new Date();
	private boolean login = false;
	private String jrm;

	public String getJrm() {
		return jrm;
	}

	public void setJrm(String jrm) {
		this.jrm = jrm;
	}

	public ChannelHandlerContext getChannel() {
		return channel;
	}

	public void setChannel(ChannelHandlerContext channel) {
		this.channel = channel;
	}

	public Date getLastUpTime() {
		return lastUpTime;
	}

	public void setLastUpTime(Date lastUpTime) {
		this.lastUpTime = lastUpTime;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

}
