package utils.soket.msg;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.LoggerFactory;

import utils.utils.DateUtil;
import utils.utils.LogUtil;

/**
 * 字节转换工具类
 * 
 * @author sid
 * 
 */
public class Converter {

	/**
	 * 把无符号32位long转换为字节数组
	 * 
	 * @param val
	 * @return
	 */
	public static byte[] unSigned32LongToBigBytes(long val) {
		byte[] b = new byte[4];
		b[3] = (byte) (val >> 0);
		b[2] = (byte) (val >> 8);
		b[1] = (byte) (val >> 16);
		b[0] = (byte) (val >> 24);
		return b;
	}

	/**
	 * 把字节数组转换为无符号32位long
	 * 
	 * @param b
	 * @param pos
	 * @return
	 */
	public static long bigBytes2Unsigned32Long(byte[] b, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		int index = pos;
		firstByte = (0x000000FF & ((int) b[index + 3]));
		secondByte = (0x000000FF & ((int) b[index + 2]));
		thirdByte = (0x000000FF & ((int) b[index + 1]));
		fourthByte = (0x000000FF & ((int) b[index + 0]));
		index = index + 4;
		return ((long) (fourthByte << 24 | thirdByte << 16 | secondByte << 8 | firstByte)) & 0xFFFFFFFFL;
	}

	/**
	 * 把无符号16位int转换为字节数组
	 * 
	 * @param val
	 * @return
	 */
	public static byte[] unSigned16IntToBigBytes(int val) {
		byte[] b = new byte[2];
		b[1] = (byte) (val >> 0);
		b[0] = (byte) (val >> 8);
		return b;
	}

	/**
	 * 把字节数组转换为无符号16位int
	 * 
	 * @param b
	 * @param pos
	 * @return
	 */
	public static int bigBytes2Unsigned16Int(byte[] b, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int index = pos;
		firstByte = (0x00FF & ((int) b[index + 1]));
		secondByte = (0x00FF & ((int) b[index + 0]));
		index = index + 2;
		return (secondByte << 8 | firstByte) & 0xFFFF;
	}

	/**
	 * LHY
	 * 
	 * @param str
	 *            必须为2的倍数，且为纯字符串
	 * @return
	 */
	public static byte[] str2BCD(String str) {
		if (str == null)
			throw new IllegalArgumentException("输入转换参数为null!");
		int strLen = str.length();
		if (strLen % 2 != 0) {
			throw new IllegalArgumentException("参数不合法,长度必须为2的倍数!");
		}
		int len = strLen / 2;
		byte[] bcd = new byte[len];
		byte[] temp = str.getBytes();
		for (int i = temp.length; i > 0; i -= 2) {
			byte low = (byte) (temp[i - 1] - 48);
			byte high = (byte) (temp[i - 2] - 48);
			bcd[(i / 2 - 1)] = (byte) (((high << 4) & 0xff) + low);
		}
		return bcd;

	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bcd2Str(byte[] b, int from, int len) {
		byte[] bytes = new byte[len];
		System.arraycopy(b, from, bytes, 0, len);

		return bcd2Str(bytes);
	}

	public static String bcd2Str(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(byte2HexStr(bytes[i]));
		}
		return sb.toString();
	}

	/**
	 * 把字节数组抓换成十六进制字符串
	 * 
	 * @param b
	 * @param flag
	 * @param i
	 *            长度
	 * @return
	 */
	public static String byte2HexStr(byte[] b, int flag, int i) {
		byte[] temp = new byte[i];
		System.arraycopy(b, flag, temp, 0, i);
		return byte2HexStr(temp);

	}

	/**
	 * @date 2010/06/11
	 * 
	 * @author xijie
	 * 
	 * @param Input
	 *            String-contents
	 * @return Integer-array
	 */
	public static byte[] string2ASCII(String s) {// 字符串转换为ASCII码

		byte[] bytes = null;
		try {
			bytes = s.getBytes("US-ASCII");
		} catch (Exception e) {
			LoggerFactory.getLogger(Converter.class).error(e.getMessage());
		}
		return bytes;
	}

	public static String byte2HexStr(byte b) {
		String hs = "";
		String stmp = "";
		stmp = (Integer.toHexString(b & 0XFF));
		if (stmp.length() == 1)
			hs = hs + "0" + stmp;
		else
			hs = hs + stmp;
		// if (n<b.length-1) hs=hs+":";
		return hs.toUpperCase();
	}

	public static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}
	
	/**
	 * 将字节数组转换为16进制字符串如 {10,-1}-->0aFF
	 * 每个字节后面一个空格
	 * @param src
	 *            源数组
	 * @return 字符串,null表示失败
	 * @roseuid 3C15CF9502F8
	 */
	public static String bytes2HexsSpace(byte[] src) {

		StringBuffer hs = new StringBuffer();
		String stmp = "";
		hs.setLength(src.length * 3);
		int t = 0;
		try {

			for (int n = 0; n < src.length; n++) {
				stmp = (java.lang.Integer.toHexString(src[n] & 0XFF));
				if (stmp.length() == 1) {
					hs.setCharAt(t, '0');
					t++;
					hs.setCharAt(t, stmp.charAt(0));
					t++;
				} else {
					hs.setCharAt(t, stmp.charAt(0));
					t++;
					hs.setCharAt(t, stmp.charAt(1));
					t++;
				}
				hs.setCharAt(t, ' ');
				t++;
			}
		}
		// try
		catch (java.lang.NullPointerException ex) {
			return null;
		}
		String strTemp = hs.toString().toUpperCase();
		hs = null;
		return strTemp;
	}

	/**
	 * 
	 * int2Double:(经纬度从int转换成double). 
	 *
	 * @author sid
	 * @param i
	 * @return
	 */
	public static double long2Double(Long i){
		double result = i;
		if (null!=i) {
			return result/600000d;
		}
		return 0d;
	}

	/**
	 * 
	 * double2Int:(经纬度从double转换成int). 
	 *
	 * @author sid
	 * @param i
	 * @return
	 */
	public static long double2Long(Double i){
		if (null!=i) {
			return (long)(i.doubleValue()*600000);
		}
		return 0;
	}

	/**
	 * String转GBK Bytes
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] getBytes(String s) {
		if (s!=null) {
			return s.getBytes(Charset.forName("GBK"));
		}else{
			return new byte[0];
		}
	}

	/**
	 * byte[] 转String
	 * 
	 * @param b
	 * @return
	 */
	public static String toGBKString(byte[] b) {
		try {
			String s = new String(b, "GBK");
			return s.trim();
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * byte[] 转String
	 * 
	 * @param b
	 * @param startIndex
	 * @param toIndex
	 * @return
	 */
	public static String toGBKString(byte[] b, int startIndex, int toIndex) {
		return toGBKString(Arrays.copyOfRange(b, startIndex, toIndex));
	}
	

	/**
	 * 把字节数组转换成16位无符号整数
	 * 
	 * @param b
	 * @param pos
	 *            起始位置
	 * @return
	 */
	public static int bytes2UnSigned16Int(byte[] b, int pos) {
		return (b[pos + 1] & 0xff) + (b[pos] << 8 & 0xff00);
	}

	/**
	 * String 转为定长的BCD码
	 * 
	 * @param s
	 * @param BCDLength
	 * @return
	 */
	public static byte[] string2BCD(String s, int BCDLength) {
		if (s.length() / 2 != BCDLength) {
			throw new IllegalArgumentException("希望长度为:" + BCDLength + ",实际为："
					+ s.length() / 2);
		}
		return str2BCD(s);

	}

	public static int bytesToInt(byte[] b) {
		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for (int i = 0; i < b.length; i++) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}

	/**
	 * 把字节数组转换成char
	 * 
	 * @param b
	 * @param pos
	 *            起始位置
	 * @return
	 */
	public static char bytes2Char(byte[] b, int pos) {
		return (char) (b[pos] + (b[pos + 1] << 8));
	}

	/**
	 * 将char转换为小端字节数组
	 * 
	 * @param val
	 * @param b
	 * @param from
	 * @param len
	 * @return len
	 */
	public static byte[] char2bytes(char c) {
		byte[] b = new byte[2];
		b[0] = (byte) (c);
		b[1] = (byte) (c >> 8);
		return b;
	}

	/**
	 * 把无符号16位整数转换成byte数组
	 * 
	 * @param val
	 *            : int 存放16位无符号整数的
	 * @param b
	 * @param pos
	 *            起始位置
	 */
	public static void unSigned16Int2Bytes(int val, byte[] b, int pos) {
		b[pos + 1] = (byte) (val);
		b[pos] = (byte) (val >> 8);
	}

	public static void long2Bytes(long x, byte[] bb, int index) {
		bb[index + 0] = (byte) (x >> 56);
		bb[index + 1] = (byte) (x >> 48);
		bb[index + 2] = (byte) (x >> 40);
		bb[index + 3] = (byte) (x >> 32);
		bb[index + 4] = (byte) (x >> 24);
		bb[index + 5] = (byte) (x >> 16);
		bb[index + 6] = (byte) (x >> 8);
		bb[index + 7] = (byte) (x >> 0);
	}
	public static long bytes2Long(byte[] bb, int index) {
		return ((((long) bb[index + 0] & 0xff) << 56)
				| (((long) bb[index + 1] & 0xff) << 48)
				| (((long) bb[index + 2] & 0xff) << 40)
				| (((long) bb[index + 3] & 0xff) << 32)
				| (((long) bb[index + 4] & 0xff) << 24)
				| (((long) bb[index + 5] & 0xff) << 16)
				| (((long) bb[index + 6] & 0xff) << 8) | (((long) bb[index + 7] & 0xff) << 0));
	}

	/**
	 * 6位BCD码转换成 yy-mm-dd-hh-mm-ss格式字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String bcd2Date(byte[] b) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 6; i++) {
			sb.append((b[i] & 0xf0) >> 4).append((b[i] & 0x0f)).append("-");

		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 把字节数组转换成32位无符号整数
	 * 
	 * @param b
	 *            byte数组 小端
	 * @param pos
	 *            位置
	 * @return
	 */
	public static long bytes2Unsigned32Long(byte[] b, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		int index = pos;
		firstByte = (0x000000FF & ((int) b[index + 3]));
		secondByte = (0x000000FF & ((int) b[index + 2]));
		thirdByte = (0x000000FF & ((int) b[index + 1]));
		fourthByte = (0x000000FF & ((int) b[index]));
		index = index + 4;
		return ((long) (fourthByte << 24 | thirdByte << 16 | secondByte << 8 | firstByte)) & 0xFFFFFFFFL;
	}

	/**
	 * 把无符号32位无符号整数转换成字节数组
	 * 
	 * @param val
	 *            32位无符号整数
	 * @param b
	 * @param pos
	 *            位置
	 */
	public static void unSigned32LongToBytes(long val, byte[] b, int pos) {
		// b[pos] = (byte) (val >> 0);
		// b[pos + 1] = (byte) (val >> 8);
		// b[pos + 2] = (byte) (val >> 16);
		// b[pos + 3] = (byte) (val >> 24);
		b[pos + 3] = (byte) (val >> 0);
		b[pos + 2] = (byte) (val >> 8);
		b[pos + 1] = (byte) (val >> 16);
		b[pos] = (byte) (val >> 24);
	}

	/**
	 * yy-mm-dd-hh-mm-ss格式字符串转换成6位BCD码
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] date2Bcd(String str) {
		String[] arr = str.split("-");
		byte[] bytes = new byte[6];
		for (int i = 0; i < 6; i++) {
			bytes[i] = (byte) ((((byte) (Integer.parseInt(arr[i]
					.substring(0, 1)))) << 4) + ((byte) (Integer
					.parseInt(arr[i].substring(1)))));
		}

		return bytes;
	}

	public static short bytes2Unsigned8Short(byte b) {
		Byte bb = new Byte(b);
		if (bb.shortValue() < 0)
			return (short) (bb.shortValue() + 256);

		return bb.shortValue();
	}

	/**
	 * @date 2010/06/11
	 * 
	 * @author xijie
	 * @param ASCIIs
	 *            ASCII编码字符串
	 * @return
	 */
	public static String ascii2String(int size, byte[] aSCIIs, int index) {
		String aString = null;

		byte[] asciiByte = new byte[size];
		for (int i = 0; i < asciiByte.length; i++) {
			asciiByte[i] = aSCIIs[index];
			index++;
		}

		try {
			aString = new String(asciiByte, "US-ASCII");
		} catch (Exception e) {
//			LogUtil.getInstance().getLogger(Converter.class).error("ascii2String", e);
			LoggerFactory.getLogger(Converter.class).error(e.getMessage());
		}
		return aString.trim();
	}

	public static char ascii2Char(int ASCII) {
		return (char) ASCII;
	}

	/**
	 * BCD值转换为数值,如153（10011001）转换为99(10进制)；
	 * 
	 * @param b
	 * @return
	 */
	public static int bcd2Value(byte b) {
		int low = b & 0x0F;
		int high = (b & 0xF0) >> 4;
		return high * 10 + low;

	}

	/**
	 * BCD转int
	 * 
	 * @param b
	 * @return
	 */
	public static int bcd2int(byte[] b) {
		int n = 0;

		for (int i = 0; i < b.length; i++) {
			n = n * 100 + bcd2Value(b[i]);
		}
		return n;
	}

	public static int bcd2int(byte[] b, int startIndex, int length) {
		byte[] s = new byte[length];
		System.arraycopy(b, startIndex, s, 0, length);
		return bcd2int(s);
	}

	/**
	 * BCD转Float
	 * 
	 * @param b
	 *            BCD
	 * @param dotlen
	 *            小数点位数
	 * @return
	 */
	public static float bcd2Float(byte[] b, int dotlen) {

		long n = Converter.bcd2int(b);
		double fac = Math.pow(10, dotlen);
		float r = (float) (n / fac);
		return r;
	}

	public static float bcd2Float(byte[] b, int startIndex, int length,
			int dotlen) {
		byte[] s = new byte[length];
		System.arraycopy(b, startIndex, s, 0, length);
		return bcd2Float(s, dotlen);
	}
	/**
	 * 处理所有小数点BCD码 营运上传指令里交易前余额 dot 小数点位置 比如56.24 为2
	 */
	public static String bcd2DotString(byte[] bytes, int startIndex, int len,
			int dot) {

		byte[] b = new byte[len];
		System.arraycopy(bytes, startIndex, b, 0, len);
		String temp = bcd2Str(b);
		temp = temp.substring(0, temp.length() - dot) + "."
				+ temp.substring(temp.length() - dot);
		while (temp.startsWith("0")) {
			int start = temp.indexOf("0");
			temp = temp.substring(start + 1);
		}

		return temp;
	}


	/**
	 * 高位在前
	 * 
	 * @param hex
	 * @param bytes
	 * @param index
	 */
	public static void hexStr2Bytes(String hex, byte[] bytes, int index) {
		int n = hex.length() / 2;
		for (int i = 0; i < n; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
					16);
		}
	}


	/**
	 * 高位在前
	 * 
	 * @param hex
	 */
	public static byte hexStr2Byte(String hex) {
		byte b = 0;
		int n = hex.length() / 2;
		for (int i = 0; i < n; i++) {
			 b = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
					16);
		}
		return b;
	}
	
	public static byte[] bcddotStr2Bytes(String str) {
		str = str.replace(".", "");
		return str2BCD(str);
	}

	/**
	 * byte转换为BCD值 如89(十进制)转换为01001001
	 * 
	 * @param b
	 * @return
	 */
	private static byte _value2BCD(int b) {
		int m = b % 10;// 低位
		int n = b / 10;// 高位
		return (byte) (((n & 0x0F) << 4) + (m & 0x0F));
	}

	/**
	 * 整数类型转换为BCD编码
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] int2bcd(int n) {
		ArrayList<Byte> list = new ArrayList<Byte>();

		while (n > 0) {
			int v = n % 100;
			byte b = _value2BCD(v);
			list.add(new Byte(b));
			n = n / 100;
		}
		byte[] r = ArrayUtils.toPrimitive(list.toArray(new Byte[0]));
		ArrayUtils.reverse(r);
		return r;

	}
	/**
	 * Float转BCD
	 * 
	 * @param f
	 *            Float
	 * @param dotlen
	 *            小数点位数
	 * @return
	 */
	public static byte[] float2bcd(float f, int dotlen) {

		double fac = Math.pow(10, dotlen);
		int n = (int) (f * (int) fac);
		return Converter.int2bcd(n);

	}

	public static byte[] float2bcd(float f, int dotlen, int BCDLength) {
		byte[] b = float2bcd(f, dotlen);
		if (b.length == BCDLength)
			return b;
		byte[] r = new byte[BCDLength];
		for (int i = 0; i < r.length; i++) {
			r[i] = 0;
		}
		System.arraycopy(b, 0, r, BCDLength - b.length, b.length);
		return r;
	}

	/**
	 * 根据四个字节的物理存储,生成一个整数
	 * <p>
	 * 例如e0,08,00,00生成整数2272
	 * <p>
	 * wujinzhong 0610
	 * 
	 * @param bts
	 *            Description of Parameter
	 * @return The Int value
	 */
	public static int toInt(byte[] bts) {
		if (bts == null) {
			return Integer.MIN_VALUE;
		}
		int v = 0;
		int len = bts.length;
		for (int i = len - 1; i >= 0; i--) {
			v = v * 256 + ((bts[i] + 256) % 256);

		}
		return v;
	}

	/**
	 * 将对象转换为整数型
	 * 
	 * @param o
	 *            源对象
	 * @return 对应的Int值,如果出错,则返回Integer.MIN_VALUE
	 */
	public static int toInt(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("该对象为空");
		}
		String s = o.toString();
		try {
			return Integer.parseInt(s);
		} catch (Exception ex) {
			LogUtil.getInstance().getLogger(Converter.class).error(ex.getMessage(), ex);
			return Integer.MIN_VALUE;
		}
	}

	/**
	 * 将对象转换为整数型
	 * 
	 * @param o
	 *            源对象
	 * @param defaultInt
	 * @return 对应的Int值,如果出错,则返回defaultInt
	 */
	public static int toInt(Object o, int defaultInt) {
		try {
			if (o instanceof BigDecimal) {
				return ((BigDecimal) o).intValue();
			} else if (o instanceof String) {
				return new BigDecimal((String) o).setScale(0,
						BigDecimal.ROUND_HALF_UP).intValue();
			}
			return Integer.parseInt(o.toString());
		} catch (Exception ex) {
			return defaultInt;
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param foo
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static byte[] toByteArray(short foo) {
		return toByteArray(foo, new byte[2]);
	}

	/**
	 * Description of the Method
	 * 
	 * @param foo
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static byte[] toByteArray(int foo) {
		return toByteArray(foo, new byte[4]);
	}

	/**
	 * Description of the Method
	 * 
	 * @param foo
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static byte[] toByteArray(long foo) {
		return toByteArray(foo, new byte[8]);
	}

	/**
	 * Description of the Method
	 * 
	 * @param foo
	 *            Description of Parameter
	 * @param array
	 *            Description of Parameter
	 * @return Description of the Return Value
	 * @eturn Description of the Returned Value
	 */
	private static byte[] toByteArray(long foo, byte[] array) {
		int len = array.length;
		for (int iInd = 0; iInd < len; iInd++) {
			array[iInd] = (byte) ((foo >> ((len - iInd - 1) * 8)) & 0x000000FF);
		}
		return array;
	}

	/**
	 * 将字符串转换为BCD码，目前不支持汉字
	 * <p>
	 * add by wjz 040916
	 * 
	 * @param s
	 *            String
	 * @return byte[] BCD格式的数组
	 */
	public static byte[] strToBCD(String s) {
		byte[] bt = new byte[s.length()];
		// 锟斤拷始锟斤拷
		for (int i = 0; i < bt.length; i++) {
			bt[i] = (byte) 0xff;
		}
		char[] chars = s.toCharArray();
		byte c = 0;
		try {
			int len = chars.length;
			int pos = 0;
			boolean flag = true; // true-high position;false-lower position
			for (int i = 0; i < len; i++) {
				c = (byte) (chars[i]);
				if (c >= '0' && c <= '9') { // 锟斤拷锟斤拷
					if (!flag) { // 锟斤拷锟街斤拷
						bt[pos] = (byte) (bt[pos] + (c - 0x30));
						flag = true;
						pos++;
					} else { // 锟斤拷锟街斤拷
						bt[pos] = (byte) ((c - 0x30) << 4);
						flag = false;
					}
				} else { // 锟街凤拷
					if (!flag) { // 锟斤拷锟街斤拷
						bt[pos] = (byte) ((byte) (bt[pos]) + (byte) ((c + 128) >> 4));
						pos++;
						bt[pos] = (byte) (c << 4);
					} else { // 锟斤拷锟街斤拷
						bt[pos] = (byte) (c + 128);
						pos++;
					}

				}

			}
			if (!flag) {
				bt[pos] = (byte) (bt[pos] + 0x0f);
				pos++;
			}
			byte[] ret = new byte[pos];
			System.arraycopy(bt, 0, ret, 0, pos);
			return ret;
		} catch (Exception ex) {
			LogUtil.getInstance().getLogger(Converter.class).error(
					"strToBCD ERROR:" + ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * 将BCD码还原为字符串，目前不支持汉字
	 * <p>
	 * add by wjz 040916
	 * 
	 * @param bt
	 *            byte[] BCD格式的数组
	 * @return String
	 */
	public static String BCDToStr(byte[] bt) {
		String s = "";
		if (bt == null)
			return "";
		byte[] bt2 = new byte[bt.length * 2];
		byte b = 0;
		try {
			int len = bt2.length;
			for (int i = 0; i < bt.length; i++) {
				b = bt[i];
				bt2[i * 2] = (byte) (bt[i] >> 4 & 0x0f); // 锟斤拷位
				bt2[i * 2 + 1] = (byte) (bt[i] & 0x0f); // 锟斤拷位

			}
			boolean flag = true; // 一锟斤拷锟街凤拷目锟绞?
			for (int i = 0; i < len; i++) {
				b = bt2[i];
				if (flag) {
					if (b <= 9) {
						s = s + (char) (b + 0x30);
					} else {
						flag = false;
					}
				} else { // 锟斤拷锟角匡拷始锟斤拷锟斤拷前锟斤拷喜锟?
					if (b == 0x0f && (i == len - 1))
						continue;

					s = s + (char) ((bt2[i - 1] << 4) + b - 128);

					flag = true;

				}
			}
			return s;
		} catch (Exception ex) {
			LogUtil.getInstance().getLogger(Converter.class).error(
					"BCDToStr ERROR:" + ex.getMessage(), ex);
			return null;
		}
	}


//	public static int bytesToInt(byte[] b) {
//
//		int mask = 0xff;
//		int temp = 0;
//		int n = 0;
//		for (int i = 0; i < b.length; i++) {
//			n <<= 8;
//			temp = b[i] & mask;
//			n |= temp;
//		}
//		return n;
//	}
	
	
	
//	public static long bytesToLong(byte[] b) {
//
//		int mask = 0xff;
//		int temp = 0;
//		long n = 0;
//		for (int i = 0; i < b.length; i++) {
//			n <<= 8;
//			temp = b[i] & mask;
//			n |= temp;
//		}
//		return n;
//	}
	
	
	public static String longToHex(long l, int length) {
		String temp = Long.toHexString(l);
		while (temp.length()<length*2) {
			temp = "0" + temp;
		}
		return temp;
	}
	
	
	public static byte[] getDevBytes(String devid) {
		int len = devid.length();
		while (len<10) {
			devid = "0"+devid;
			len++;
		}
		return strToBCD(devid);
	}
	
	
	public static String IntToHex(int i, int length) {
		String temp = Integer.toHexString(i);
		while (temp.length()<length*2) {
			temp = "0" + temp;
		}
		return temp;
	}
	
	public static int HexToInt(String hex) {
		return Integer.parseInt(hex, 16);
	}
	
//	public static String bytesToBinary(byte[] b, int byteLength) {
//		int i = bytesToInt(b);
//		String binString = Integer.toBinaryString(i);
//		while (binString.length() < byteLength*8) {
//			binString = "0" + binString;
//		}
//		return binString;
//	}
	
	public static String intToBinary(int i, int byteLength) {
		String binString = Integer.toBinaryString(i);
		while (binString.length() < byteLength*8) {
			binString = "0" + binString;
		}
		return binString;
	}
	
	public static String binaryToInt(String binary, int intlength){
		String s = Integer.parseInt(binary,2)+"";
		while ((s+"").length()<intlength){
			s = "0"+s;
		}
		return s;
	}
	
	public static byte[] getLittleData(byte[] b) {
		int len = b.length;
		byte[] temp = new byte[len];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = b[len-1-i];
		}
		return temp;
	}
	
	public static String getSrcData(String hex) {
		return hex;
	}
	
	public static byte[] getLittleEndian(byte[] b) {
		byte[] temp = new byte[b.length];
		for (int i = 0; i < b.length; i++) {
			temp[b.length-i-1] = b[i];
		}
		return temp;
	}
	
	public static String getLittleEndian(String hex) {
		int len = hex.length();
		if (len%2==1)return null;
		String str = "";
		for (int i = len/2; i > 0; i--) {
			str += hex.substring((i-1)*2, i*2);
		}
		return str;
	}

	/**
     * 锟斤拷锟街斤拷转锟斤拷锟斤拷锟睫凤拷诺锟斤拷锟斤拷锟斤拷址锟斤拷锟绞?
     * @param src
     * @return
     */
    public static String byte2Hexs(byte src) {
        try {
        	String stmp = (java.lang.Integer.toHexString(src & 0XFF));
            if (stmp.length() == 1) {
            	stmp = "0" + stmp;
            }
            return stmp.toUpperCase();
        }
        catch (java.lang.NullPointerException ex) {
            return null;
        }
    }


	/**
	 * obj锟斤拷锟街凤拷锟绞撅拷锟绞斤拷欠锟轿拷锟斤拷址锟絥ull锟斤拷"")
	 * <p>
	 * add by wjz 040401
	 * 
	 * @param obj
	 *            目锟斤拷锟斤拷锟?
	 * @return 锟斤拷锟街凤拷锟斤拷式为null锟斤拷""锟斤拷锟斤拷为true,锟斤拷锟斤拷为false
	 */
	public static boolean isEmptyStr(Object obj) {
		if (obj == null) {
			return true;
		}
		try {
			String s = obj.toString();
			if (s == null || s.trim().length() == 0) {
				return true;
			}
			return false;
		} catch (Exception ex) {
			return false;
		}

	}
	
	public static String strPaddingFront(String src, int length, String padstr) {
		if (src==null)src="";
		while (src.length()<length) {
			src = padstr + src;
		}
		return src;
	}

	/**
	 * 锟斤拷锟街斤拷锟斤拷锟斤拷转锟斤拷为16锟斤拷锟斤拷锟街凤拷锟斤拷 {10,-1}-->0aFF
	 * 
	 * @param src
	 *            源锟斤拷锟斤拷
	 * @return 锟街凤拷,null锟斤拷示失锟斤拷
	 * @roseuid 3C15CF9502F8
	 */
	public static String bytes2Hexs(byte[] src) {

		StringBuffer hs = new StringBuffer();
		String stmp = "";
		hs.setLength(src.length * 2);
		int t = 0;
		try {

			for (int n = 0; n < src.length; n++) {
				stmp = (java.lang.Integer.toHexString(src[n] & 0XFF));
				if (stmp.length() == 1) {
					hs.setCharAt(t, '0');
					t++;
					hs.setCharAt(t, stmp.charAt(0));
					t++;
				} else {
					hs.setCharAt(t, stmp.charAt(0));
					t++;
					hs.setCharAt(t, stmp.charAt(1));
					t++;
				}
			}
		}
		// try
		catch (java.lang.NullPointerException ex) {
			return null;
		}
		String strTemp = hs.toString().toUpperCase();
		hs = null;
		return strTemp;
	}

	
	public static String MD5UTF8(String src) {
		MessageDigest md = null;
		String des = "";
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LogUtil.getInstance().getLogger(Converter.class).error("MD5 error", e);
		}
		try {
			md.update(src.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			LogUtil.getInstance().getLogger(Converter.class).error("MD5 utf-8 error", e);
			e.printStackTrace();
		}
		byte[] b = md.digest();
		des = bytes2Hexs(b);
		return des;
	}

	/**
	 * Description of the Method
	 * 
	 * @param foo
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static byte[] toByteArray16Int(int foo) {
		return toByteArray(foo, new byte[2]);
	}

	/**
	 * Description of the Method
	 * 
	 * @param foo
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static byte[] toByteArray32Long(long foo) {
		return toByteArray(foo, new byte[4]);
	}

	/**
	 * Description of the Method
	 * 
	 * @param foo
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static byte[] toByteArray64Long(long foo) {
		return toByteArray(foo, new byte[8]);
	}

	/**
	 * Description of the Method
	 * 
	 * @param src
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	public static String toHexsFormat(byte[] src) {
		if (src == null) {
			return null;
		}
		int len = src.length;
		StringBuffer sb = new StringBuffer();

		String s = "";
		sb.append(",value=");

		for (int i = 0; i < len; i++) {
			s = java.lang.Integer.toHexString(src[i] & 0XFF);
			if (s.length() == 1) {
				sb.append("0");
			}
			sb.append(s);
			sb.append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	/**
	 * 锟斤拷莅烁锟斤拷纸诘锟斤拷锟斤拷锟芥储,锟斤拷锟揭伙拷锟斤拷锟斤拷锟?
	 * @param bts
	 *            Description of Parameter
	 * @return The Long value
	 */
	public static long toLong(byte[] bts) {
		if (bts == null) {
			return Long.MIN_VALUE;
		}
		int v = 0;
		int len = bts.length;
		for (int i = len - 1; i >= 0; i--) {
			v = v * 256 + ((bts[i] + 256) % 256);

		}
		return v;
	}

	public static Date parseDate(Object o, Date defaulti) {
		if (o instanceof Date) {
			return (Date) o;
		}
		try {
			return DateUtil.strToDate(o.toString());
		} catch (Exception e) {
			return defaulti;
		}
	}

	/**
	 * "1001,1002"-->"'1001','1002'"
	 * 
	 * @param s
	 * @return
	 */
	public static String getInSQLStr(String s) {
		if (s == null || s.length() <= 0)
			return "";
		String[] ss = s.split(",");
		return getInSQLStr(ss);
	}

	public static String getInSQLStr(Object[] ss) {
		if (ss == null)
			return "";
		String retstr = "";
		for (int i = 0; i < ss.length; i++) {
			if (i != 0)
				retstr += ",";
			retstr += "'" + ss[i] + "'";
		}
		return retstr;
	}

	public synchronized static String getSysId() {
		try {
			Thread.sleep(36);
		} catch (Exception e) {

		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		Date d = getSysDateTime();
		return sf.format(d);
	}

	/**
	 * 取系统时锟戒，锟斤拷时锟斤拷锟街★拷锟斤拷
	 * 
	 * @return
	 */
	public static Date getSysDateTime() {
		return new Date();
	}
	
	
	public static BigDecimal decimalDivide(BigDecimal cs, BigDecimal bcs, int scale) {
		return cs.divide(bcs ,scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param source
	 * @param length
	 * @param sign
	 * @return
	 */
	public static byte[] fillData(byte[] source,int length,byte sign){
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			if(i<source.length){
				result[i]=source[i];
			}else{
				result[i]=sign;
			}
		}
		
		return result;
	}

	/**
	 * @param source
	 * @param length
	 * @param sign
	 * @return
	 */
	public static String fillDataPrefix(String source,int length,String sign){
		String result = "";
		if (source==null) {
			source = "";
		}
		int len = source.length();
		int num = length-len;
		for (int i = 0; i < num; i++) {
			result+=sign;
		}
		return result+source;
	}

	/**
	 * @param source
	 * @param length
	 * @param sign
	 * @return
	 */
	public static String fillDataStr(String source,int length){
		String reulst = "";
		for (int i = 0; i < length; i++) {
			if(i<source.length()){
				reulst+=source.charAt(i);
			}else{
				reulst+=0x00;
			}
		}
		
		return reulst;
	}

}
