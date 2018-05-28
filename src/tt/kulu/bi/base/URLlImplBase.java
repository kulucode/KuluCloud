package tt.kulu.bi.base;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tt.kulu.bi.message.pojo.WebSocketDataPojo;
import tt.kulu.bi.user.pojo.LoginUserPojo;
import net.sf.json.JSONObject;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.BSDateEx;

public class URLlImplBase {
	@SuppressWarnings("serial")
	public static boolean isLocal = false;
	public static HashMap<String, WebSocketDataPojo> webSocketMap = new HashMap<String, WebSocketDataPojo>();
	public final static int REDIS_KULUDATA = 6;
	public final static String LOGIN_GROUP_WHERE = "(select v.ORG_ID from T_ORG v where v.ORG_ALLID like ?)";
	public final static String LOGIN_USER_WHERE = "(select v2.USER_INSTID from T_ORG_USER_R v2,T_ORG v1 where v1.ORG_ID=v2.ORG_ID and v1.ORG_ALLID like ?)";
	static {
		String local = BSCommon.getConfigValue("debug_islocal");
		if (local != null && local.trim().equals("true")) {
			URLlImplBase.isLocal = true;
		}
	}
	public static HashMap<Integer, String> MQTopicM = new HashMap<Integer, String>() {
		{
			put(0, "CLOUD_TO_TRUCKSERV");// 车载终端服务到云服务
			put(1, "TRUCKSERV_TO_CLOUD");// 云服务到车载终端服务
			put(2, "CLOUD_TO_WINGSERV");// 手表终端服务到云服务
			put(3, "WINGSERV_TO_CLOUD");// 云服务到手表终端服务
		}
	};
	public static HashMap<Integer, String> ErrorMap = new HashMap<Integer, String>() {
		{
			put(0, "");
			put(1, "输入参数非法");
			put(2, "接口错误");
			put(3, "用户失效，请重新登录");
			put(4, "用户不存在");
			put(5, "用户密码错误");
			put(6, "效验码不正确");
			put(7, "用户验证码异常，属于非法访问");
			put(8, "您没有访问该功能的权限！");
			// 登录
			put(18, "该号码已被人注册，请更换号码");
			put(19, "验证时效未过期，请不要重复发送");
			put(20, "消息未发送，请稍后再试");
			put(21, "验证码错误，请查证后输入");
			put(22, "旧密码不正确");
			put(23, "头像文件不存在");
			put(24, "现有密码输入不正确！");
			put(25, "用户姓名与ID不符！");
			put(26, "短信余额不足！");
			put(27, "身份证与手机号不符！");
			put(28, "身份证与ID不符！");
			put(29, "用户不存在！");

			// 消息
			put(500, "登录发送消息");
			put(501, "上线广播消息");
			put(502, "离线广播消息");
			put(503, "推送加密消息");
			put(504, "网页明文消息");
			put(505, "得到在线列表");

			// 其他错误
			put(888, "系统初始化失败！");
			put(900, "更新失败");
			put(901, "输入参数有误");
			put(902, "报警不存在！");
			put(990, "操作失败");
			put(991, "删除失败");
			put(992, "无法得到版本信息");
			put(993, "管理员删除密码错误！");
			put(994, "登录用户密码错误！");
			put(995, "该功能暂停提供！");
			put(996, "用户超过最大数，请稍后再访问！");
			put(997, "没有提供活动号");
			put(998, "网络来源非法");
			put(999, "其他错误");
			put(1000, "系统初始化中，请稍后访问！");
			put(1001, "非法设备");

			// 导入错误
			put(1002, "导入失败！");
			put(1003, "文件不存在。");
			put(1004, "文件无法读取，请另存为xls格式。");
			put(1100, "文档格式不符合标准，或者必填字段为空！");
		}
	};
	protected LoginUserPojo user = null;

	public URLlImplBase() {

	}

	// 价格的加法，返回元宝
	public static String AllPrince(String inOne, String inOne2) {
		int oneI = Math.round(Float.parseFloat(inOne) * 100);
		int twoI = Math.round(Float.parseFloat(inOne2) * 100);
		String all = new DecimalFormat("#########.##").format((Float
				.parseFloat(String.valueOf(oneI + twoI)) / 100));
		return String.valueOf(all);
	}

	public static String AllPrinceDiv(String inOne, String count) {
		DecimalFormat df = new DecimalFormat("#########.##");
		df.setRoundingMode(RoundingMode.UP);
		return df.format((Float.parseFloat(String.valueOf(inOne)) / (Float
				.parseFloat(String.valueOf(count)))));
	}

	public static String AllPrinceDiv(String inOne, float count) {
		DecimalFormat df = new DecimalFormat("#########.##");
		df.setRoundingMode(RoundingMode.UP);
		return df.format((Float.parseFloat(String.valueOf(inOne)) / count));
	}

	public static String AllPrinceDiv(float inOne, float count) {
		DecimalFormat df = new DecimalFormat("#########.##");
		df.setRoundingMode(RoundingMode.UP);
		return df.format(inOne / count);
	}

	public static String AllPrinceMul(String inOne, String count) {
		DecimalFormat df = new DecimalFormat("#########.#");
		df.setRoundingMode(RoundingMode.UP);
		return df.format((Float.parseFloat(String.valueOf(inOne)) * (Float
				.parseFloat(String.valueOf(count)))));
	}

	public static String AllPrinceMul(String inOne, float count) {
		DecimalFormat df = new DecimalFormat("#########.#");
		df.setRoundingMode(RoundingMode.UP);
		return df.format((Float.parseFloat(String.valueOf(inOne)) * count));
	}

	public static String AllPrinceMul(float inOne, float count) {
		DecimalFormat df = new DecimalFormat("#########.#");
		df.setRoundingMode(RoundingMode.UP);
		return df.format(inOne * count);
	}

	public static int getAge(String bird) throws Exception {
		BSDateEx bsDate = new BSDateEx();
		Calendar fDate = bsDate.getStringToCalendar(bird);
		Calendar tDate = Calendar.getInstance();
		return tDate.get(Calendar.YEAR) - fDate.get(Calendar.YEAR);
	}

	// 返回参数
	protected JSONObject setReturn(String retStr) {
		JSONObject ret = new JSONObject();
		// ret.put("r", BSDes.encrypt(retStr, TTEduStatic.UrlParesKey));
		return ret;
	}

	public static String decodeUnicode(String ascii) {
		List<String> ascii_s = new ArrayList<String>();
		String zhengz = "\\\\u[0-9,a-f,A-F]{4}";
		Pattern p = Pattern.compile(zhengz);
		Matcher m = p.matcher(ascii);
		while (m.find()) {
			ascii_s.add(m.group());
		}
		for (int i = 0, j = 2; i < ascii_s.size(); i++) {
			String code = ascii_s.get(i).substring(j, j + 4);
			if (code.equals("0000")) {
				ascii = ascii.replace(ascii_s.get(i), "");
			} else {
				char ch = (char) Integer.parseInt(code, 16);
				ascii = ascii.replace(ascii_s.get(i), String.valueOf(ch));
			}
		}
		return ascii;
	}

	// 从身份证的到生日
	public static String getBirthdayFromCard(String idCard) {
		String bd = "", dataStr = "";
		if (idCard.length() == 18) {
			bd = idCard.substring(6, 14);
			dataStr = bd.substring(0, 4) + "-" + bd.substring(4, 6) + "-"
					+ bd.substring(6, 8);
		}
		return dataStr;
	}

	// 从日期中得到时间数字
	public static int getTimeNumberByTime(String timeStr) {
		int timeNum = 0;
		String[] times = timeStr.split(":");
		if (times.length >= 2) {
			timeNum = Integer.parseInt(times[0]) * 60
					+ Integer.parseInt(times[1]);
		}
		return timeNum;
	}

	// 从日期中得到时间数字
	public static int getTimeNumberByDate(String date) {
		int time = 0;
		BSDateEx bsDate = new BSDateEx();
		try {
			Calendar fDate = bsDate.getStringToCalendar(date);
			time = fDate.get(Calendar.HOUR_OF_DAY) * 60
					+ fDate.get(Calendar.MINUTE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	// 日期字符串转为对象
	public static Calendar getCalendarFromDateStr(String date, String format) {
		Calendar dateObj = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		try {
			Date time = simpleDateFormat.parse(date);
			dateObj.setTime(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateObj;
	}
}