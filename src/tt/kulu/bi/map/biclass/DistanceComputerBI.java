package tt.kulu.bi.map.biclass;

public class DistanceComputerBI {

	private static boolean logOn = false;

	private static void log(String msg, Object obj) {
		if (logOn) {
			System.out.println(msg + " = " + obj);
		}
	}

	// 地球半径。单位:米。
	private static double EARTH_RADIUS = 6371004;

	private static double sin(double a) {
		return Math.sin(a);
	}

	private static double cos(double a) {
		return Math.cos(a);
	}

	private static double acos(double a) {
		return Math.acos(a);
	}

	/**
	 * 转换经纬度为角度的double显示。只处理到小数点后2位(分:60进制)。
	 * */
	private static double convert2angle(double a) {
		log("a", a);
		// 转换60进制为10进制。
		double tem = (long) (a * 100) / 100;
		log("tem", tem);
		tem += (a * 100) % 100 / 60;
		log("tem", tem);
		double result = tem * Math.PI / 180.0;
		log("result", result);
		return result;
	}

	private static double abs(double a) {
		return Math.abs(a);
	}

	public static double computeDistance(double lat1, double lng1, double lat2,
			double lng2) {

		log("lat1", lat1);
		log("lng1", lng1);
		log("lat2", lat2);
		log("lng2", lng2);

		double OC = cos(convert2angle(lat1));
		log("OC", OC);

		double OD = cos(convert2angle(lat2));
		log("OD", OD);

		double AC = sin(convert2angle(lat1));
		log("AC", AC);

		double BD = sin(convert2angle(lat2));
		log("BD", BD);

		// AC=ED
		double BE = abs(BD - AC);
		log("BE", BE);

		double lngGap = convert2angle(lng1) - convert2angle(lng2);
		log("lngGap", lngGap);

		// AE=CD.
		double AE = Math.sqrt(OC * OC + OD * OD - 2 * OC * OD * cos(lngGap));

		log("AE", AE);

		double AB = Math.sqrt(AE * AE + BE * BE);
		log("AB", AB);

		double angle = acos((2 - AB * AB) / 2);
		log("angle", angle);

		double distance = EARTH_RADIUS * angle;
		log("distance", distance);

		return distance;
	}

}
