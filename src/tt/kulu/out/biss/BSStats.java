package tt.kulu.out.biss;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.report.pojo.TruckReportPojo;
import tt.kulu.bi.report.pojo.UserReportPojo;
import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.out.call.BICompany;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BILogin;
import tt.kulu.out.call.BIStats;

import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSStats
 * </p>
 * <p>
 * 功能描述: 统计Web接口类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2014-10-29
 * </p>
 */
public class BSStats {
	/**
	 * <p>
	 * 方法名：do_searchSysBaseStats
	 * </p>
	 * <p>
	 * 方法描述：得到基础统计信息
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchSysBaseStats(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject fretObj = new JSONObject();
		String group = m_bs.getPrivateMap().get("pg_group");
		BIStats statsBI = new BIStats(null, m_bs);
		if (group == null) {
			group = "";
		}
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") >= 0
				|| user.getRoleWhere().indexOf("'ADMIN'") >= 0) {
			fretObj = statsBI.searchSysBaseStats();
			fretObj.put("r", 0);
		} else {
			fretObj = statsBI.searchMyBaseStats(user.getUserInst(), group);
			fretObj.put("r", 0);
		}
		fretObj.put("thisdate", m_bs.getDateEx().getThisDate(0, 0));
		// 当前运营商
		BICompany compBI = new BICompany(m_bs);
		CompanyPojo onePojo = compBI.getOneCompanyByType(1);
		JSONObject compJ = new JSONObject();
		if (onePojo != null) {
			compJ.put("id", onePojo.getId());
			compJ.put("name", onePojo.getName());
			compJ.put("link", onePojo.getLinkMan());
			compJ.put("linkphone", onePojo.getLinkPhone());
			compJ.put("lat", onePojo.getLatitude());
			compJ.put("lon", onePojo.getLongitude());
		}
		fretObj.put("company", compJ);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_ReportIni
	 * </p>
	 * <p>
	 * 方法描述：得到车辆初始化信息页面：www/admin/role/index.html
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_ReportIni(BSObject m_bs) throws Exception {
		// 数据准备
		JSONObject fretObj = new JSONObject();

		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchTruckReportList
	 * </p>
	 * <p>
	 * 方法描述：搜索车辆工作报告情况
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchTruckReportList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		long startNum = 0;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");
		String group = m_bs.getPrivateMap().get("pg_group");
		String truck = m_bs.getPrivateMap().get("pg_truck");
		String sdate = m_bs.getPrivateMap().get("pg_sdate");
		String edate = m_bs.getPrivateMap().get("pg_edate");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		long days = 0;
		JSONObject paras = new JSONObject();
		if (sText != null) {
			paras.put("key", sText);
		}
		if (sdate != null && !sdate.equals("") && edate != null
				&& !edate.equals("")) {
			paras.put("date", sdate + "," + edate);
			days = m_bs.getDateEx().getDateCount(sdate, edate);
		}
		paras.put("state", 0);
		if (group != null) {
			paras.put("group", group);
		}
		if (truck != null) {
			paras.put("truck", truck);
		}
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			paras.put("login", user.getGroupId());
		}
		if (pageNumStr != null) {
			pageNum = Integer.parseInt(pageNumStr);
		} else {
			pageNum = 0;
		}
		if (pageNum < 0) {
			pageNum = 0;
		}
		if (pageSizeStr != null) {
			pageSize = Integer.parseInt(pageSizeStr);
		} else {
			pageSize = 200;
		}
		startNum = pageSize * pageNum;
		// 调用BI
		BIStats statsBI = new BIStats(null, m_bs);
		// 返回数据
		JSONArray retlist = new JSONArray();
		ArrayList<TruckReportPojo> list = statsBI.getTruckReportList(paras,
				startNum, startNum + pageSize - 1);
		if (days == 0 && paras.containsKey("days")) {
			days = paras.getLong("days");
		}
		for (TruckReportPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("truckid", onePojo.getTruck().getId());
			oneObj.put("truckno", onePojo.getTruck().getNo());
			if (onePojo.getTruck().getOrg() != null) {
				oneObj.put("truckorg", onePojo.getTruck().getOrg().getAllName()
						.replaceAll(",", "-"));
			} else {
				oneObj.put("truckorg", "");
			}
			oneObj.put("truckname", onePojo.getTruck().getName());
			oneObj.put("platenum", onePojo.getTruck().getPlateNum());
			oneObj.put("truckbrand", onePojo.getTruck().getDefine().getBrand());
			oneObj.put("trucktype", onePojo.getTruck().getDefine().getName());
			oneObj.put("userid", onePojo.getTruck().getMangUser().getId());
			oneObj.put("username", onePojo.getTruck().getMangUser().getName());
			oneObj.put("userphone", onePojo.getTruck().getMangUser()
					.getmPhone());
			oneObj.put("oil", URLlImplBase.AllPrinceDiv(onePojo.getOil(), 1000));
			oneObj.put("distance",
					URLlImplBase.AllPrinceDiv(onePojo.getDistance(), 1000));
			oneObj.put("worktimes",
					URLlImplBase.AllPrinceDiv(onePojo.getWorkTimes(), 3600));
			oneObj.put(
					"useff",
					URLlImplBase.AllPrinceMul(
							URLlImplBase.AllPrinceDiv(
									oneObj.getString("worktimes"), days
											* onePojo.getTruck().getDefine()
													.getWorkTime()), 100)
							+ "%");
			oneObj.put("speed", onePojo.getSpeed());
			oneObj.put("fdate", onePojo.getFormDate());
			oneObj.put("tdate", onePojo.getToDate());
			retlist.add(oneObj);
		}
		retJSON.put("data", retlist);
		retJSON.put("max", paras.getLong("max"));
		retJSON.put("r", 0);
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchTruckReportList
	 * </p>
	 * <p>
	 * 方法描述：搜索车辆工作报告情况
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchUserReportList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		long startNum = 0;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");
		String group = m_bs.getPrivateMap().get("pg_group");
		String userInst = m_bs.getPrivateMap().get("pg_user");
		String sdate = m_bs.getPrivateMap().get("pg_sdate");
		String edate = m_bs.getPrivateMap().get("pg_edate");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		JSONObject paras = new JSONObject();
		long days = 0;
		paras.put("role", "OUTDOORS_STAFF");
		if (sText != null) {
			paras.put("key", sText);
		}
		if (sdate != null && !sdate.equals("") && edate != null
				&& !edate.equals("")) {
			paras.put(
					"date",
					sdate.substring(0, 10) + " 00:00:00,"
							+ edate.substring(0, 10) + " 23:59:59");
			days = m_bs.getDateEx().getDateCount(sdate, edate);
		}
		if (group != null) {
			paras.put("group", group);
		}
		if (userInst != null) {
			paras.put("user", userInst);
		}
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			paras.put("login", user.getGroupId());
		}
		if (pageNumStr != null) {
			pageNum = Integer.parseInt(pageNumStr);
		} else {
			pageNum = 0;
		}
		if (pageNum < 0) {
			pageNum = 0;
		}
		if (pageSizeStr != null) {
			pageSize = Integer.parseInt(pageSizeStr);
		} else {
			pageSize = 50;
		}
		startNum = pageSize * pageNum;
		// 调用BI
		BIStats statsBI = new BIStats(null, m_bs);
		// 返回数据
		JSONArray retlist = new JSONArray();
		ArrayList<UserReportPojo> list = statsBI.getUserReportList(paras,
				startNum, startNum + pageSize - 1);
		if (days == 0 && paras.containsKey("days")) {
			days = paras.getLong("days");
		}
		for (UserReportPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("instid", onePojo.getUser().getInstId());
			oneObj.put("id", onePojo.getUser().getId());
			oneObj.put("name", onePojo.getUser().getName());
			oneObj.put("phone", onePojo.getUser().getmPhone());
			if (onePojo.getUser().getOrg() != null) {
				oneObj.put("org", onePojo.getUser().getOrg().getAllName()
						.replaceAll(",", "-"));
			} else {
				oneObj.put("org", "");
			}
			oneObj.put("sbflg", UserPojo.SBFLG_NAME[onePojo.getUser()
					.getSbFlg()]);
			oneObj.put("step", onePojo.getStep());
			oneObj.put("distance",
					URLlImplBase.AllPrinceDiv(onePojo.getDistance(), 1000));
			oneObj.put("late", onePojo.getLate());
			oneObj.put("abs", days - onePojo.getAbs());
			if (!onePojo.getUser().getBirthday().equals("")) {
				oneObj.put("age",
						URLlImplBase.getAge(onePojo.getUser().getBirthday()));
			} else {
				oneObj.put("age", 0);
			}

			retlist.add(oneObj);
		}
		retJSON.put("data", retlist);
		retJSON.put("max", paras.getLong("max"));
		retJSON.put("r", 0);
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}
}
