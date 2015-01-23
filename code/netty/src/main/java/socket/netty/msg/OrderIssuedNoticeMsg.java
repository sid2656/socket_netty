package socket.netty.msg;

import java.nio.ByteBuffer;


/**
 * ***************************************************************************** <br/>
 * <b>类名:OrderIssuedNoticeMsg</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月3日<br/>
 * 功能：5.2.2.7 预约订单下发通知（0x94）<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class OrderIssuedNoticeMsg extends AbsMsg {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int uint32OrderId; // 订单ID
	private byte type; // 业务类型 ： 0：即时召车；1：预约召车；2：车辆指派
	private String bcdtime; // 用车时间
	private String getOnPosition;//乘客上车地点
	private int uint32ClientPointLng ; // 乘客位置经度
	private int uint32ClientPointLat ; // 乘客位置纬度
	private String aimPosition;//目的地点
	private int uint32AimPointLng ; // 目的地位置经度
	private int uint32AimPointLat ; // 目的地位置纬度
	private String bcd2ServiceCharge; // 电召服务费
	private String desc; // 业务描述

	@Override
	protected int getMsgID() {
		return MessageID.orderIssuedNoticeMsg;
	}

	@Override
	protected int getBodylen() {
		int strl = Converter.getBytes(desc).length+1
					+Converter.getBytes(getOnPosition).length+1
					+Converter.getBytes(aimPosition).length+1;
		return 4 + 1 + 7+4+4+4+4 + 2 + strl;
	}

	@Override
	protected byte[] bodytoBytes() {
		
		ByteBuffer b = ByteBuffer.allocate(this.getBodylen());
		b.put(Converter.unSigned32LongToBigBytes(uint32OrderId));
		b.put(type);
		b.put(Converter.str2BCD(bcdtime));
		b.put(Converter.getBytes(getOnPosition));
		b.put((byte) 0x00);
		b.put(Converter.unSigned32LongToBigBytes(uint32ClientPointLng));
		b.put(Converter.unSigned32LongToBigBytes(uint32ClientPointLat));
		b.put(Converter.getBytes(aimPosition));
		b.put((byte) 0x00);
		b.put(Converter.unSigned32LongToBigBytes(uint32AimPointLng));
		b.put(Converter.unSigned32LongToBigBytes(uint32AimPointLat));
		b.put(Converter.str2BCD(bcd2ServiceCharge));
		b.put(Converter.getBytes(desc));
		b.put((byte) 0x00);
		return b.array();
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {

		try {

			ByteBuffer f = ByteBuffer.wrap(b);
			int offset = this.head.getBodylen();

			uint32OrderId = f.getInt(offset);
			offset += 4;
			type = f.get(offset);
			offset += 1;
			bcdtime = Converter.bcd2Str(b, offset, 7);
			offset += 7;

			int stringEndIdx= FindEndFlag.getFirstStringEndFlag(b, offset);
			getOnPosition = Converter.toGBKString(b, offset,stringEndIdx-offset);
			offset = stringEndIdx ;
			offset += 1 ;
			
			uint32ClientPointLng = Converter.toUInt32(b, offset);
			offset += 4 ;
			
			uint32ClientPointLat = Converter.toUInt32(b, offset);
			offset += 4 ;
			
			stringEndIdx= FindEndFlag.getFirstStringEndFlag(b, offset);
			aimPosition = Converter.toGBKString(b, offset,stringEndIdx-offset);
			offset = stringEndIdx ;
			offset += 1 ;
			
			uint32AimPointLng = Converter.toUInt32(b, offset);
			offset += 4 ;
			
			uint32AimPointLat = Converter.toUInt32(b, offset);
			offset += 4 ;
			
			bcd2ServiceCharge = Converter.bcd2Str(b, offset, 2);
			offset += 2;
			
			stringEndIdx = FindEndFlag.getFirstStringEndFlag(b, offset);
			desc = Converter.toGBKString(b, offset, stringEndIdx - offset);
			return true;
		} catch (Exception ex) {

		}
		return false;

	}

	public int getUint32OrderId() {
		return uint32OrderId;
	}

	public void setUint32OrderId(int uint32OrderId) {
		this.uint32OrderId = uint32OrderId;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getBcdtime() {
		return bcdtime;
	}

	public void setBcdtime(String bcdtime) {
		this.bcdtime = bcdtime;
	}

	public String getBcd2ServiceCharge() {
		return bcd2ServiceCharge;
	}

	public void setBcd2ServiceCharge(String bcd2ServiceCharge) {
		this.bcd2ServiceCharge = bcd2ServiceCharge;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getGetOnPosition() {
		return getOnPosition;
	}

	public void setGetOnPosition(String getOnPosition) {
		this.getOnPosition = getOnPosition;
	}

	public int getUint32ClientPointLng() {
		return uint32ClientPointLng;
	}

	public void setUint32ClientPointLng(int uint32ClientPointLng) {
		this.uint32ClientPointLng = uint32ClientPointLng;
	}

	public int getUint32ClientPointLat() {
		return uint32ClientPointLat;
	}

	public void setUint32ClientPointLat(int uint32ClientPointLat) {
		this.uint32ClientPointLat = uint32ClientPointLat;
	}

	public String getAimPosition() {
		return aimPosition;
	}

	public void setAimPosition(String aimPosition) {
		this.aimPosition = aimPosition;
	}

	public int getUint32AimPointLng() {
		return uint32AimPointLng;
	}

	public void setUint32AimPointLng(int uint32AimPointLng) {
		this.uint32AimPointLng = uint32AimPointLng;
	}

	public int getUint32AimPointLat() {
		return uint32AimPointLat;
	}

	public void setUint32AimPointLat(int uint32AimPointLat) {
		this.uint32AimPointLat = uint32AimPointLat;
	}

}
