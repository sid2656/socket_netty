package socket.netty.bean;

import java.util.HashMap;

/**
 * Tcp用户实体
 * 
 * @author sid
 *
 */
public class TcpUser extends BaseBean {

	/**
	 * 接入码
	 */
	private String mac;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Override
	public HashMap<String, Object> getMapId() {
		HashMap<String, Object> mapId = new HashMap<String, Object>();
		mapId.put("mac", this.mac);
		return mapId;
	}

}
