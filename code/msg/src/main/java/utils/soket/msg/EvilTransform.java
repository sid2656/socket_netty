package utils.soket.msg;

public class EvilTransform {
	// static final double pi=Math.PI;

	static final double pi = Math.PI;
	static final double x_pi = pi * 3000.0 / 180.0;
	//
	// Krasovsky 1940
	//
	// a = 6378245.0, 1/f = 298.3
	// b = a * (1 - f)
	// ee = (a^2 - b^2) / a^2;
	static final double a = 6378245.0;
	static final double ee = 0.00669342162296594323;
	static final double interval = 0.000001;

	//

	/**
	 * WGS84转换为GCJ-02
	 * 
	 * @param wgLon
	 * @param wgLat
	 * @return
	 */
	public static double[] WGS84ToGCJ02(double wgLon, double wgLat) {
		double mgLat = 0;
		double mgLon = 0;
		if (outOfChina(wgLat, wgLon)) {
			mgLat = wgLat;
			mgLon = wgLon;
			return new double[] { mgLon, mgLat };
		}
		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
		double radLat = wgLat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		mgLat = wgLat + dLat;
		mgLon = wgLon + dLon;
		return new double[] { mgLon, mgLat };
	}

	static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
				+ 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
				* Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
				* pi)) * 2.0 / 3.0;
		return ret;
	}

	/**
	 * GCJ-02转换为WGS84
	 * 
	 * @param clon
	 * @param clat
	 * @return
	 */
	public static double[] GCJ02ToWGS84(double clon, double clat) {
		double wlat, wlon;

		double[] wgs = WGS84ToGCJ02(clon, clat); // 输入坐标转换为Mars坐标
		// 计算坐标差值
		double dlat = wgs[1] - clat;
		double dlon = wgs[0] - clon;

		// 带入差值，模拟wgs坐标
		wlat = clat + dlat;
		wlon = clon + dlon;

		// 将计算出来的wgs坐标转换为Mars坐标，并与输入坐标校验精度
		double[] mars = WGS84ToGCJ02(wlon, wlat);

		dlat = mars[1] - clat;
		dlon = mars[0] - clon;
		double dis = Math.sqrt(dlat * dlat + dlon * dlon);

		while (dis > interval) {
			dlat = dlat / 2;
			dlon = dlon / 2;
			wlat = wlat - dlat;
			wlon = wlon - dlon;
			mars = WGS84ToGCJ02(wlon, wlat);
			double _lat = mars[1] - clat;
			double _lon = mars[0] - clon;
			dis = Math.sqrt(_lat * _lat + _lon * _lon);
			dlat = Math.abs(dlat) > Math.abs(_lat) ? dlat : _lat;
			dlon = Math.abs(dlon) > Math.abs(_lon) ? dlon : _lon;
		}
		return new double[] { wlon, wlat };
	}

	/**
	 * GCJ-02 转 BD-09
	 * 
	 * @param gg_lat
	 * @param gg_lon
	 * @return
	 */
	static double[] GCJ02ToBD09(double gg_lat, double gg_lon) {
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);

		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		return new double[] { bd_lon, bd_lat };
	}

	/**
	 * BD-09转GCJ-02
	 * 
	 * @param bd_lat
	 * @param bd_lon
	 * @return
	 */
	static double[] BD09ToGCJ02(double bd_lat, double bd_lon) {
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return new double[] { gg_lon, gg_lat };
	}

	/**
	 * BD09转WGS84
	 * 
	 * @param bd_lat
	 * @param bd_lon
	 * @return
	 */
	public static double[] BD09ToWGS84(double bd_lat, double bd_lon) {
		double[] gcj = BD09ToGCJ02(bd_lon, bd_lat);	
		return GCJ02ToWGS84(gcj[0], gcj[1]);
	}

	/**
	 * WGS84转BD09
	 * 
	 * @param wgs_lat
	 * @param wgs_lon
	 * @return
	 */
	public static double[] WGS84ToBD09(double wgs_lat, double wgs_lon) {
		double[] gcj = WGS84ToGCJ02( wgs_lon,wgs_lat);
		return GCJ02ToBD09(gcj[1], gcj[0]);
	}
}
