package com.hdsx.taxi.dcs.dcsserver.socket.thread;

import java.nio.ByteBuffer;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.dcs.dcsserver.socket.MsgCache;
import com.hdsx.taxi.dcs.dcsserver.socket.MsgRespFactory;
import com.hdsx.taxi.dcs.dcsserver.socket.hanlder.HandlerFactory;
import com.hdsx.taxi.dcs.dcsserver.util.PropertiesUtil;
import com.hdsx.taxi.dcs.nettyutil.msghandler.IHandler;
import com.hdsx.taxi.dcs.upamsg.AbsMsg;
import com.hdsx.taxi.dcs.upamsg.MsgHeader;
import com.hdsx.taxi.dcs.upamsg.util.CRC16_CUI;
import com.hdsx.taxi.dcs.upamsg.util.Converter;

/**
 * 处理消息线程
 * 
 * @author cuipengfei
 *
 */
public class ParseMsgThread extends Thread {

	private static final Logger logger = LoggerFactory
			.getLogger(ParseMsgThread.class);

	private byte[] rpb;

	public ParseMsgThread(byte[] rpb) {
		this.rpb = rpb;
	}

	@Override
	public void run() {
		try {
			HeartBeatThread.lastTime=new Date();
			
			// 转码
			rpb = decode(rpb);

			// 消息头解析
			MsgHeader head = headFromBytes(rpb);
			if (head == null) {
				return;
			}
			//去除消息缓存
			MsgCache.getInstance().remove(MsgCache.getMsgKey(head));

			// 登陆消息处理
			if (head.getEncrypt_flag() == 1) {
				head.setIA1(Long.parseLong(PropertiesUtil.getProperties()
						.getProperty("IA1")));
				head.setIC1(Long.parseLong(PropertiesUtil.getProperties()
						.getProperty("IC1")));
				head.setM1(Long.parseLong(PropertiesUtil.getProperties()
						.getProperty("M1")));
			}
			// 生成消息后产生handler
			AbsMsg msg = MsgRespFactory.genMsg(head, rpb);
			if (msg == null) {
				logger.error(Integer.toHexString(head.getMsg_id()) + "消息不存在");
				return;
			}
			// 交给对应handler处理
			IHandler handler = HandlerFactory.getHandler(msg);
			if (handler != null) {
				handler.doHandle(msg);
			}
		} catch (Exception e) {
			logger.error("接受消息队列处理数据错误", e);
			e.printStackTrace();
		}
	}

	/**
	 * 消息头解析
	 * 
	 * @return
	 */
	private MsgHeader headFromBytes(byte[] b) {
		ByteBuffer buffer1 = ByteBuffer.wrap(b);
		byte[] head_body = new byte[b.length - 2];
		buffer1.position(0);
		buffer1.get(head_body);

		byte[] crc = new byte[2];
		buffer1.position(b.length - 2);
		buffer1.get(crc);

		byte[] crc_check = CRC16_CUI.getCRCCRC16_CCITT(head_body);

		if (Converter.bigBytes2Unsigned16Int(crc, 0) != Converter
				.bigBytes2Unsigned16Int(crc_check, 0)) {
			logger.info(Converter.bigBytes2Unsigned16Int(crc, 0) + "  "
					+ Converter.bigBytes2Unsigned16Int(crc_check, 0));
			logger.error("消息crc校验码不正确");
			return null;// crc校验码有误
		}

		MsgHeader head = new MsgHeader();
		if (!head.frombytes(head_body))
			return null;// 消息头属性解析有误
		return head;
	}

	/**
	 * 解码转义
	 * 
	 * @param b
	 * @return
	 */
	private byte[] decode(byte[] b) {
		ByteBuffer buffer = ByteBuffer.allocate(10 * 1024 * 1024);
		ByteBuffer buffer1 = ByteBuffer.wrap(b);
		buffer.position(0);
		while (buffer1.remaining() > 0) {

			byte d = buffer1.get();
			if (d == 0x5a) {
				byte e = buffer1.get();
				if (e == 0x02)
					buffer.put((byte) 0x5a);
				else if (e == 0x01)
					buffer.put((byte) 0x5b);
			} else if (d == 0x5e) {
				byte e = buffer1.get();
				if (e == 0x02)
					buffer.put((byte) 0x5e);
				else if (e == 0x01)
					buffer.put((byte) 0x5d);
			} else {
				buffer.put(d);
			}
		}

		byte[] result = new byte[buffer.position()];
		buffer.position(0);
		buffer.get(result);
		return result;
	}

}
