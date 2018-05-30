package tt.kulu.out.biss;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.fance.pojo.FancePojo;
import tt.kulu.bi.fault.pojo.FaultReportPojo;
import tt.kulu.bi.file.pojo.BFSFilePojo;
import tt.kulu.bi.inspect.pojo.InspectPlanPojo;
import tt.kulu.bi.map.biclass.CoordTransformBI;
import tt.kulu.bi.map.biclass.DistanceComputerBI;
import tt.kulu.bi.map.pojo.Gps;
import tt.kulu.bi.report.pojo.TruckReportPojo;
import tt.kulu.bi.report.pojo.UserReportPojo;
import tt.kulu.bi.storage.pojo.EquipmentGeometryPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.bi.truck.pojo.TruckFixLogsPojo;
import tt.kulu.bi.truck.pojo.TruckVideoPojo;
import tt.kulu.bi.truck.pojo.TruckWorkParasPojo;
import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.bi.user.pojo.OrgPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.bi.user.pojo.UserWorkDayLogsPojo;
import tt.kulu.bi.user.pojo.UserWorkParasPojo;
import tt.kulu.out.call.BICompany;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BIEquipment;
import tt.kulu.out.call.BIFance;
import tt.kulu.out.call.BIFault;
import tt.kulu.out.call.BIFile;
import tt.kulu.out.call.BIInspect;
import tt.kulu.out.call.BILogin;
import tt.kulu.out.call.BIRedis;
import tt.kulu.out.call.BIStats;
import tt.kulu.out.call.BITruck;
import tt.kulu.out.call.BIUser;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.Const;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSLogUserPojo;
import com.tt4j2ee.m.BSObject;
import com.tt4j2ee.md5.MD5Imp;

/**
 * <p>
 * 标题: BSInterface
 * </p>
 * <p>
 * 功能描述: 酷陆Web接口类
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
public class BSInterface {
	/**
	 * <p>
	 * 方法名称: do_clearUser
	 * </p>
	 * <p>
	 * 方法功能描述: 清除当前登录用户信息
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_clearUser(BSObject m_bs) throws Exception {
		m_bs.getPrivateMap().clear();
		m_bs.getPublicMap().clear();
		JSONObject retJSON = new JSONObject();
		HashMap<String, BSLogUserPojo> userList = (HashMap<String, BSLogUserPojo>) m_bs
				.getRequest().getSession().getServletContext()
				.getAttribute("BS_ONLINE_LIST");
		retJSON.put("out", BILogin.checkUserMax(m_bs));
		// retJSON.put("uc", userList.size());
		try {
			SqlExecute sqlHelper = new SqlExecute();
			try {
				retJSON.put("code", 0);
			} catch (Exception eex) {
				eex.printStackTrace();
				throw eex;
			} finally {
				sqlHelper.close();
			}

		} catch (Exception ex) {
			retJSON.put("out", true);
			retJSON.put("code", 1000);
		}
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: checkUser
	 * </p>
	 * <p>
	 * 方法功能描述: 验证用户
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_checkUser(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		retJSON.put("code", 999);
		// 验证验证码
		try {
			if (BILogin.checkUserMax(m_bs)) {
				retJSON.put("code", 996);
			} else {
				retJSON.put("code", this.checkCode(m_bs));
				if (retJSON.getInt("code") == 0) {
					// 验证用户登录信息
					UserPojo inUser = new UserPojo();
					BILogin loginBI = new BILogin(null, m_bs);
					inUser.setId(m_bs.getPrivateMap().get("username"));
					// 后台管理员登录
					String hm = m_bs.getPrivateMap().get("bs_type");
					if (hm != null && hm.equals("hm")) {
						// 设置密码与后台管理license用户一致
						inUser.setPassword(BSCommon.getConfigValue(
								"license_password").trim());
					} else {
						// 设置用户密码与用户管理员密码一致
						inUser.setPassword(MD5Imp.enCode(((String) m_bs
								.getPrivateMap().get("userkey")).trim()));
					}
					retJSON = loginBI.checkLoginUser(inUser, "ID", hm);
					if (retJSON.getInt("r") == 0) {
						// LoginUserPojo outUser = (LoginUserPojo) JSONObject
						// .toBean(retJSON.getJSONObject("u"),
						// LoginUserPojo.class);
						// 放入用户信息
						m_bs.setPublicValue(m_bs.getCheckLogin().getLogName(),
								retJSON.getString("u"));
						// m_bs.setSessionID(loginBI.setUserToSession(outUser));
						retJSON.remove("u");
						retJSON.put("code", 0);
					} else {
						retJSON.put("code", retJSON.getInt("r"));
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			retJSON.clear();
			retJSON.put("code", 2);
		} finally {
			retJSON.put("msg",
					URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
			retJSON.put("code", retJSON.getInt("code"));
		}
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: do_getDicItemMap
	 * </p>
	 * <p>
	 * 方法功能描述: 得到登录后的用户信息
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_getDicItemMap(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		BIDic dicBI = new BIDic(m_bs);
		// 报警类别
		JSONObject _paras = new JSONObject();
		_paras.put("dic", "T_FAULTREPORT_FR_TYPE");
		ArrayList<DicItemPojo> dicList = dicBI.getDicItemList(_paras);
		_paras.put("dic", "T_FAULTREPORT_FAULT_ID");
		JSONObject frtype = new JSONObject();
		for (DicItemPojo oneItem : dicList) {
			JSONObject oneI = JSONObject.fromObject(oneItem);
			_paras.put("pitemid", oneItem.getId());
			oneI.put("frfault", dicBI.getDicItemList(_paras));
			frtype.put(oneItem.getId(), oneI);
		}
		retJSON.put("fault_type", frtype);

		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: do_getLoginMsg
	 * </p>
	 * <p>
	 * 方法功能描述: 得到登录后的用户信息
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_getLoginMsg(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject retJSON = new JSONObject();
		retJSON.put("logindate", m_bs.getDateEx().getThisDate(0, 0));
		JSONObject userJSON = new JSONObject();
		userJSON.put("instid", user.getUserInst());
		userJSON.put("id", user.getUserId());
		userJSON.put("name", user.getUserName());
		userJSON.put("group", user.getGroupName());
		userJSON.put("allgroup", user.getGroupAllName());
		// userJSON.put("roles", user.getRoleWhere());
		userJSON.put("sex", user.getSex());
		userJSON.put("sexn", UserPojo.SEX_NAME[user.getSex()]);
		// 返回数据
		retJSON.put("user", userJSON);
		// 菜单
		BILogin loginBI = new BILogin(null, m_bs);
		retJSON.put("menus", loginBI.getUserMenuQD(m_bs, user, 1));
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
			compJ.put("bdlat", "");
			compJ.put("bdlon", "");
			try {
				if (!onePojo.getLatitude().equals("")
						&& !onePojo.getLongitude().equals("")) {
					Gps bdGPS = CoordTransformBI.Gps84_To_bd09(
							Double.valueOf(onePojo.getLatitude()),
							Double.valueOf(onePojo.getLongitude()));
					compJ.put("bdlat", bdGPS.getWgLat());
					compJ.put("bdlon", bdGPS.getWgLon());
				}
			} catch (Exception ex) {
			}

		}
		retJSON.put("company", compJ);
		// 机构树
		JSONObject paras = new JSONObject();
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			paras.put("login", user.getGroupId());
		}
		BIUser userBI = new BIUser(null, m_bs);
		JSONArray orgListObj = new JSONArray();
		ArrayList<OrgPojo> list = userBI.getOrgList(paras, 0, 200);
		for (OrgPojo oneOrgPojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", oneOrgPojo.getId());
			oneObj.put("name", oneOrgPojo.getName());
			oneObj.put("desc", oneOrgPojo.getDesc());
			oneObj.put("allname", oneOrgPojo.getAllName().replaceAll(",", "-"));
			oneObj.put("pid", oneOrgPojo.getPorgId());
			oneObj.put("pname", oneOrgPojo.getPorgName());
			oneObj.put("cnum", oneOrgPojo.getSubOrgNum());
			oneObj.put("snum", oneOrgPojo.getUserNum());
			if (oneOrgPojo.getSubOrgNum() > 0) {
				paras.put("root", "");
				paras.put("sub", oneOrgPojo.getId());
				oneObj.put("children", this._getChildOrgList(userBI, paras));
			}
			orgListObj.add(oneObj);
		}
		retJSON.put("orglist", orgListObj);
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: do_getOrgTree
	 * </p>
	 * <p>
	 * 方法功能描述: 得到登录用户的机构树
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 */
	public BSObject do_getOrgTree(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject retJSON = new JSONObject();
		String type = m_bs.getPrivateMap().get("pg_type");
		JSONArray listObj = new JSONArray();

		JSONObject paras = new JSONObject();
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			paras.put("login", user.getGroupId());
		}
		if (type != null) {
			paras.put("type", type);
		}
		BIUser userBI = new BIUser(null, m_bs);
		ArrayList<OrgPojo> list = userBI.getOrgList(paras, 0, 200);
		for (OrgPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("desc", onePojo.getDesc());
			oneObj.put("allname", onePojo.getAllName().replaceAll(",", "-"));
			oneObj.put("pid", onePojo.getPorgId());
			oneObj.put("pname", onePojo.getPorgName());
			oneObj.put("type", onePojo.getType());
			oneObj.put("cnum", onePojo.getSubOrgNum());
			oneObj.put("snum", onePojo.getUserNum());
			if (onePojo.getSubOrgNum() > 0) {
				paras.put("sub", onePojo.getId());
				oneObj.put("children", this._getChildOrgList(userBI, paras));
			}
			listObj.add(oneObj);
		}
		retJSON.put("list", listObj);
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

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
			fretObj.put("code", 0);
		} else {
			fretObj = statsBI.searchMyBaseStats(user.getUserInst(), group);
			fretObj.put("code", 0);
		}
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchUserList
	 * </p>
	 * <p>
	 * 方法描述：搜索用户工作情况
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchUserList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		long startNum = 0;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String group = m_bs.getPrivateMap().get("pg_group");
		String areaId = m_bs.getPrivateMap().get("pg_areaid");
		String areaName = m_bs.getPrivateMap().get("pg_areaname");
		String geoArea = m_bs.getPrivateMap().get("pg_geoarea");
		String role = m_bs.getPrivateMap().get("pg_role");
		JSONObject paras = new JSONObject();
		if (sText != null) {
			paras.put("key", sText);
		}
		if (group != null) {
			paras.put("group", group);
		}
		if (areaId != null) {
			paras.put("areaid", areaId);
		}
		if (areaName != null) {
			paras.put("areaname", areaName);
		}
		if (role != null && !role.equals("")) {
			paras.put("role", role);
		} else {
			paras.put("role", "OUTDOORS_STAFF");
		}
		paras.put("state", 1);
		if (geoArea != null) {
			// 转化坐标系
			String gps = "";
			if (geoArea.startsWith(",")) {
				geoArea = geoArea.substring(1);
			}
			String geos[] = geoArea.split(",");
			if (geos.length >= 4) {
				Gps geolu = CoordTransformBI.bd09_To_Gps84(
						Double.valueOf(geos[0]), Double.valueOf(geos[1]));
				gps = geolu.getWgLon() + "," + geolu.getWgLat();
				Gps geord = CoordTransformBI.bd09_To_Gps84(
						Double.valueOf(geos[2]), Double.valueOf(geos[3]));
				gps += "," + geord.getWgLon() + "," + geord.getWgLat();
			}
			// 时间
			if (sDate == null || sDate.equals("")) {
				Calendar date = Calendar.getInstance();
				date.add(Calendar.DATE, -1);
				sDate = m_bs.getDateEx().getCalendarToStringAll(date);
			}
			if (eDate == null || eDate.equals("")) {
				eDate = m_bs.getDateEx().getCalendarToStringAll(
						Calendar.getInstance());
			}
			if (sDate.length() <= 10) {
				sDate = sDate + " 00:00:00";
			}
			if (eDate.length() <= 10) {
				eDate = eDate + " 23:59:59";
			}
			gps += "," + sDate + "," + eDate;
			paras.put("geoarea", gps);
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
		BIUser userBI = new BIUser(null, m_bs);
		// 返回数据
		JSONArray userlist = new JSONArray();
		ArrayList<UserPojo> list = userBI.getUserList(paras, startNum, startNum
				+ pageSize - 1);
		for (UserPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", startNum + userlist.size() + 1);
			oneObj.put("userinst", onePojo.getInstId());
			oneObj.put("userid", onePojo.getId());
			if (onePojo.getOrg() != null) {
				oneObj.put("userorg",
						onePojo.getOrg().getAllName().replaceAll(",", "-"));
				oneObj.put("usergroup", onePojo.getOrg().getName());
			} else {
				oneObj.put("userorg", "");
				oneObj.put("usergroup", "");
			}
			oneObj.put("username", onePojo.getName());
			oneObj.put("userphone", onePojo.getmPhone());
			if (!onePojo.getBirthday().equals("")) {
				oneObj.put("age", URLlImplBase.getAge(onePojo.getBirthday()));
			} else {
				oneObj.put("age", 0);
			}
			userlist.add(oneObj);
		}
		retJSON.put("data", userlist);
		retJSON.put("count", paras.getLong("max"));
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getOneUserMsg
	 * </p>
	 * <p>
	 * 方法描述：得到一个员工的情况
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getOneUserMsg(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		retJSON.put("code", 29);
		String userId = m_bs.getPrivateMap().get("pg_uinst");
		BIUser userBI = new BIUser(null, m_bs);
		UserPojo onePojo = userBI.getOneUserByInstId(userId);
		if (onePojo != null) {
			JSONObject oneObj = new JSONObject();
			// 员工基本信息
			oneObj.put("userinst", onePojo.getInstId());
			oneObj.put("userid", onePojo.getId());
			if (onePojo.getOrg() != null) {
				oneObj.put("usergroup", onePojo.getOrg().getName());
				oneObj.put("userorg",
						onePojo.getOrg().getAllName().replaceAll(",", "-"));
			} else {
				oneObj.put("userorg", "");
				oneObj.put("usergroup", "");
			}
			oneObj.put("username", onePojo.getName());
			oneObj.put("userphone", onePojo.getmPhone());
			if (!onePojo.getBirthday().equals("")) {
				oneObj.put("age", URLlImplBase.getAge(onePojo.getBirthday()));
			} else {
				oneObj.put("age", 0);
			}
			// 机构管理者
			oneObj.put("manguserinst", "");
			oneObj.put("manguserid", "");
			oneObj.put("mangusername", "");
			oneObj.put("manguserphone", "");
			if (onePojo.getOrg() != null
					&& onePojo.getOrg().getMangStaff().size() > 0) {
				for (int i = 0, size = onePojo.getOrg().getMangStaff().size(); i < size; i++) {
					JSONObject onUG = onePojo.getOrg().getMangStaff()
							.getJSONObject(i);
					if (onUG.getString("orgid")
							.equals(onePojo.getOrg().getId())
							&& !onUG.getString("userinst").equals(
									onePojo.getInstId())) {
						oneObj.put("manguserinst", onUG.getString("userinst"));
						oneObj.put("manguserid", onUG.getString("userid"));
						oneObj.put("mangusername", onUG.getString("username"));
						oneObj.put("manguserphone", onUG.getString("userphone"));
						break;
					} else if (!onUG.getString("orgid").equals(
							onePojo.getOrg().getId())
							&& !onePojo.getOrg().getId()
									.equals(onePojo.getOrg().getPorgId())) {
						oneObj.put("manguserinst", onUG.getString("userinst"));
						oneObj.put("manguserid", onUG.getString("userid"));
						oneObj.put("mangusername", onUG.getString("username"));
						oneObj.put("manguserphone", onUG.getString("userphone"));
						break;
					}
				}

			} else {
			}
			// 当天工作情况
			String id = onePojo.getInstId() + "_"
					+ m_bs.getDateEx().getThisDate(0, 0).substring(0, 10);
			UserWorkDayLogsPojo oneDay = userBI.getOneUserWorkDayLogsId(id);
			if (oneDay != null) {
				oneObj.put("indate", oneDay.getInDate());
				oneObj.put("step", oneDay.getStep());
				oneObj.put("distance", oneDay.getDistance());
			} else {
				oneObj.put("indate", "");
				oneObj.put("step", 0);
				oneObj.put("distance", 0);
			}

			retJSON.put("data", oneObj);
		}
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchUserWordParasList
	 * </p>
	 * <p>
	 * 方法描述：搜索用户工作情况
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchUserWordParasList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		long startNum = 0;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String group = m_bs.getPrivateMap().get("pg_group");
		String userId = m_bs.getPrivateMap().get("pg_uinst");
		String geoArea = m_bs.getPrivateMap().get("pg_geoarea");
		String role = m_bs.getPrivateMap().get("pg_role");
		JSONObject paras = new JSONObject();
		if (sText != null) {
			paras.put("key", sText);
		}
		if (group != null) {
			paras.put("group", group);
		}
		if (userId != null) {
			paras.put("user", userId);
		}
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			paras.put("login", user.getGroupId());
		}
		paras.put("state", 0);
		if (role != null && !role.equals("")) {
			paras.put("role", role);
		} else {
			paras.put("role", "OUTDOORS_STAFF");
		}
		paras.put("hasgeo", "1");
		if (geoArea != null) {
			// 转化坐标系
			String gps = "";
			if (geoArea.startsWith(",")) {
				geoArea = geoArea.substring(1);
			}
			String geos[] = geoArea.split(",");
			if (geos.length >= 4) {
				Gps geolu = CoordTransformBI.bd09_To_Gps84(
						Double.valueOf(geos[0]), Double.valueOf(geos[1]));
				gps = geolu.getWgLon() + "," + geolu.getWgLat();
				Gps geord = CoordTransformBI.bd09_To_Gps84(
						Double.valueOf(geos[2]), Double.valueOf(geos[3]));
				gps += "," + geord.getWgLon() + "," + geord.getWgLat();
			}
			paras.put("geoarea", gps);
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
		// paras.put("today", value);
		// 调用BI
		BIUser userBI = new BIUser(null, m_bs);
		// 返回数据
		JSONArray userlist = new JSONArray();
		ArrayList<UserWorkParasPojo> list = userBI.getUserWordParasList(paras,
				startNum, startNum + pageSize - 1);
		for (UserWorkParasPojo onePojo : list) {
			if (!onePojo.getLatitude().equals("")
					&& !onePojo.getLongitude().equals("")) {
				JSONObject oneObj = new JSONObject();
				oneObj.put("tbindex", startNum + userlist.size() + 1);
				oneObj.put("bdlat", "");
				oneObj.put("bdlon", "");
				try {
					Gps bdGPS = CoordTransformBI.Gps84_To_bd09(
							Double.valueOf(onePojo.getLatitude()),
							Double.valueOf(onePojo.getLongitude()));
					oneObj.put("bdlat", bdGPS.getWgLat());
					oneObj.put("bdlon", bdGPS.getWgLon());
				} catch (Exception ex) {
				}
				oneObj.put("eqpid", onePojo.getEqpInst().getInstId());
				oneObj.put("userinst", onePojo.getEqpInst().getMangUser()
						.getInstId());
				oneObj.put("userid", onePojo.getEqpInst().getMangUser().getId());
				if (onePojo.getEqpInst().getMangUser().getOrg() != null) {
					oneObj.put("userorg", onePojo.getEqpInst().getMangUser()
							.getOrg().getAllName().replaceAll(",", "-"));
				} else {
					oneObj.put("userorg", "");
				}
				oneObj.put("username", onePojo.getEqpInst().getMangUser()
						.getName());
				oneObj.put("onlinev", onePojo.getEqpInst().getOnlineState());
				oneObj.put("online",
						EquipmentInstPojo.ONLINE_STATE_NAME[onePojo
								.getEqpInst().getOnlineState()]);
				// oneObj.put("statev", onePojo.getState());
				// oneObj.put("state",
				// UserWorkDayLogsPojo.STATE_NAME[onePojo.getState()]);
				oneObj.put("step", onePojo.getStep());
				oneObj.put("stepdate", onePojo.getStepDate());
				oneObj.put("hr", onePojo.getHeartRate());
				oneObj.put("ele", onePojo.getEleValue());
				oneObj.put("hrdate", onePojo.getHrDate());
				// oneObj.put("bpoh", onePojo.getBroHigh());
				// oneObj.put("bpol", onePojo.getBroLow());
				// oneObj.put("bpodate", onePojo.getBroDate());
				oneObj.put("fanceflg", onePojo.getFanceFlg());
				oneObj.put("geodate", onePojo.getGeoDate());
				oneObj.put("statev", onePojo.getThisState());
				oneObj.put("state",
						UserWorkDayLogsPojo.STATE_NAME[onePojo.getThisState()]);
				oneObj.put("date", m_bs.getDateEx().getThisDate(0, 0));
				userlist.add(oneObj);
			}

		}
		retJSON.put("data", userlist);
		retJSON.put("count", paras.getLong("max"));
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchUserWordDayList
	 * </p>
	 * <p>
	 * 方法描述：搜索员工当前日工作列表
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchUserWordDayList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String group = m_bs.getPrivateMap().get("pg_group");
		String userInst = m_bs.getPrivateMap().get("pg_user");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		JSONObject paras = new JSONObject();
		if (sText != null) {
			paras.put("key", sText);
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
		if (sDate == null || sDate.equals("")) {
			paras.put("date", m_bs.getDateEx().getThisDate(0, 0));
		} else {
			paras.put("date", sDate.substring(0, 10) + " 00:00:00");
		}
		paras.put("state", 1);
		// 调用BI
		BIUser userBI = new BIUser(null, m_bs);
		// 返回数据
		JSONArray userlist = new JSONArray();
		ArrayList<UserWorkDayLogsPojo> list = userBI
				.getUserWorkDayLogsList(paras);
		for (UserWorkDayLogsPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", userlist.size() + 1);
			oneObj.put("userinst", onePojo.getUser().getInstId());
			oneObj.put("userid", onePojo.getUser().getId());
			if (onePojo.getUser().getOrg() != null) {
				oneObj.put("userorg", onePojo.getUser().getOrg().getAllName()
						.replaceAll(",", "-"));
			} else {
				oneObj.put("userorg", "");
			}
			oneObj.put("username", onePojo.getUser().getName());
			if (!onePojo.getBjDate().equals("")) {
				onePojo.setState(2);
			} else if (onePojo.getType() == 0) {
				onePojo.setState(1);
			}
			oneObj.put("userphone", onePojo.getUser().getmPhone());
			oneObj.put("statev", onePojo.getState());
			oneObj.put("state",
					UserWorkDayLogsPojo.STATE_NAME[onePojo.getState()]);
			oneObj.put(
					"worktime",
					Float.parseFloat(URLlImplBase.AllPrinceDiv(
							onePojo.getWorkTime(), 3600)));
			oneObj.put("step", onePojo.getStep());
			oneObj.put("distance",
					URLlImplBase.AllPrinceDiv(onePojo.getDistance(), 1000));
			//
			oneObj.put("indate", onePojo.getInDate());
			oneObj.put("outdate", onePojo.getOutDate());
			oneObj.put("lateflgv", onePojo.getLate());
			oneObj.put("lateflg",
					UserWorkDayLogsPojo.LATEFLG_NAME[onePojo.getLate()]);
			userlist.add(oneObj);
		}
		retJSON.put("data", userlist);
		retJSON.put("count", userlist.size());
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchTruckWordParasList
	 * </p>
	 * <p>
	 * 方法描述：搜索车辆工作情况
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchTruckWordParasList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		long startNum = 0;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String group = m_bs.getPrivateMap().get("pg_group");
		String truckId = m_bs.getPrivateMap().get("pg_truck");
		String geoArea = m_bs.getPrivateMap().get("pg_geoarea");
		JSONObject paras = new JSONObject();
		if (sText != null) {
			paras.put("key", sText);
		}
		if (group != null) {
			paras.put("group", group);
		}
		if (truckId != null) {
			paras.put("truck", truckId);
		}
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			paras.put("login", user.getGroupId());
		}
		if (geoArea != null) {
			// 转化坐标系
			String gps = "";
			if (geoArea.startsWith(",")) {
				geoArea = geoArea.substring(1);
			}
			String geos[] = geoArea.split(",");
			if (geos.length >= 4) {
				Gps geolu = CoordTransformBI.bd09_To_Gps84(
						Double.valueOf(geos[0]), Double.valueOf(geos[1]));
				gps = geolu.getWgLon() + "," + geolu.getWgLat();
				Gps geord = CoordTransformBI.bd09_To_Gps84(
						Double.valueOf(geos[2]), Double.valueOf(geos[3]));
				gps += "," + geord.getWgLon() + "," + geord.getWgLat();
			}
			paras.put("geoarea", gps);
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
		BITruck truckBI = new BITruck(null, m_bs);
		// 返回数据
		JSONArray userlist = new JSONArray();
		ArrayList<TruckWorkParasPojo> list = truckBI.getTruckWordParasList(
				paras, startNum, startNum + pageSize - 1);
		for (TruckWorkParasPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", startNum + userlist.size() + 1);
			oneObj.put("eqpid", onePojo.getEqpInst().getInstId());
			oneObj.put("onlinev", onePojo.getEqpInst().getOnlineState());
			oneObj.put("online", EquipmentInstPojo.ONLINE_STATE_NAME[onePojo
					.getEqpInst().getOnlineState()]);
			oneObj.put("truckid", onePojo.getEqpInst().getTruck().getId());
			oneObj.put("trucknno", onePojo.getEqpInst().getTruck().getNo());
			oneObj.put("truckname", onePojo.getEqpInst().getTruck().getName());
			oneObj.put("trucktype", onePojo.getEqpInst().getTruck().getDefine()
					.getName());
			oneObj.put("truckbrand", onePojo.getEqpInst().getTruck()
					.getDefine().getBrand());
			if (onePojo.getEqpInst().getTruck().getOrg() != null) {
				oneObj.put("truckorg", onePojo.getEqpInst().getTruck().getOrg()
						.getAllName().replaceAll(",", "-"));
			} else {
				oneObj.put("truckorg", "");
			}

			oneObj.put("paltenum", onePojo.getEqpInst().getTruck()
					.getPlateNum());
			// 司机
			onePojo.setWorkUser(null);
			if (!onePojo.getGeoDate().equals("")) {
				onePojo.setWorkUser(truckBI.getDriverSchedulingByTruckAndDay(
						onePojo.getGeoDate(), onePojo.getEqpInst().getTruck()
								.getId()));
			}
			if (onePojo.getWorkUser() != null) {
				oneObj.put("userinst", onePojo.getWorkUser().getInstId());
				oneObj.put("userid", onePojo.getWorkUser().getId());
				oneObj.put("username", onePojo.getWorkUser().getName());
				oneObj.put("usermphone", onePojo.getWorkUser().getmPhone());
			} else {
				oneObj.put("userinst", "");
				oneObj.put("userid", "");
				oneObj.put("username", "");
				oneObj.put("usermphone", "");
			}

			oneObj.put(
					"oil",
					!onePojo.getSpeed().equals("") ? URLlImplBase.AllPrinceDiv(
							onePojo.getOil(), 10000) : "0");
			oneObj.put(
					"oildiff",
					!onePojo.getSpeed().equals("") ? URLlImplBase.AllPrinceDiv(
							onePojo.getOilDiff(), 10000) : "0");
			oneObj.put(
					"speed",
					!onePojo.getSpeed().equals("") ? URLlImplBase.AllPrinceDiv(
							onePojo.getSpeed(), 10) : "0");
			oneObj.put("oildate", onePojo.getOilDate());
			oneObj.put("weight", onePojo.getWeight());
			oneObj.put("lat", onePojo.getLatitude());
			oneObj.put("lon", onePojo.getLongitude());
			oneObj.put("bdlat", "");
			oneObj.put("bdlon", "");
			try {
				if (!onePojo.getLatitude().equals("")
						&& !onePojo.getLongitude().equals("")) {
					Gps bdGPS = CoordTransformBI.Gps84_To_bd09(
							Double.valueOf(onePojo.getLatitude()),
							Double.valueOf(onePojo.getLongitude()));
					oneObj.put("bdlat", bdGPS.getWgLat());
					oneObj.put("bdlon", bdGPS.getWgLon());
				}
			} catch (Exception ex) {
			}

			oneObj.put("fanceflg", onePojo.getFanceFlg());
			oneObj.put("geodate", onePojo.getGeoDate());
			oneObj.put("date", m_bs.getDateEx().getThisDate(0, 0));
			userlist.add(oneObj);
		}
		retJSON.put("data", userlist);
		retJSON.put("count", paras.getLong("max"));
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchTruckList
	 * </p>
	 * <p>
	 * 方法描述：搜索车辆情况
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchTruckList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		long startNum = 0;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String group = m_bs.getPrivateMap().get("pg_group");
		String truckId = m_bs.getPrivateMap().get("pg_truck");
		String geoArea = m_bs.getPrivateMap().get("pg_geoarea");
		JSONObject paras = new JSONObject();
		if (sText != null) {
			paras.put("key", sText);
		}
		if (group != null) {
			paras.put("group", group);
		}
		if (truckId != null) {
			paras.put("truck", truckId);
		}
		if (geoArea != null) {
			// 转化坐标系
			String gps = "";
			if (geoArea.startsWith(",")) {
				geoArea = geoArea.substring(1);
			}
			String geos[] = geoArea.split(",");
			if (geos.length >= 4) {
				Gps geolu = CoordTransformBI.bd09_To_Gps84(
						Double.valueOf(geos[0]), Double.valueOf(geos[1]));
				gps = geolu.getWgLon() + "," + geolu.getWgLat();
				Gps geord = CoordTransformBI.bd09_To_Gps84(
						Double.valueOf(geos[2]), Double.valueOf(geos[3]));
				gps += "," + geord.getWgLon() + "," + geord.getWgLat();
			}
			paras.put("geoarea", gps);
		}
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			paras.put("login", user.getGroupId());
		}
		paras.put("state", 0);
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
			pageSize = 20;
		}
		startNum = pageSize * pageNum;
		// 调用BI
		BITruck truckBI = new BITruck(null, m_bs);
		// 返回数据
		JSONArray userlist = new JSONArray();
		ArrayList<TruckWorkParasPojo> list = truckBI.getTruckWordParasList(
				paras, startNum, startNum + pageSize - 1);
		for (TruckWorkParasPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", startNum + userlist.size() + 1);
			oneObj.put("eqpid", onePojo.getEqpInst().getInstId());
			oneObj.put("onlinev", onePojo.getEqpInst().getOnlineState());
			oneObj.put("online", EquipmentInstPojo.ONLINE_STATE_NAME[onePojo
					.getEqpInst().getOnlineState()]);
			oneObj.put("truckid", onePojo.getEqpInst().getTruck().getId());
			oneObj.put("trucknno", onePojo.getEqpInst().getTruck().getNo());
			oneObj.put("truckname", onePojo.getEqpInst().getTruck().getName());
			oneObj.put("trucktype", onePojo.getEqpInst().getTruck().getDefine()
					.getName());
			oneObj.put("truckbrand", onePojo.getEqpInst().getTruck()
					.getDefine().getBrand());
			oneObj.put("truckorg", onePojo.getEqpInst().getTruck().getOrg()
					.getAllName().replaceAll(",", "-"));
			oneObj.put("paltenum", onePojo.getEqpInst().getTruck()
					.getPlateNum());
			// 司机
			onePojo.setWorkUser(null);
			if (!onePojo.getGeoDate().equals("")) {
				onePojo.setWorkUser(truckBI.getDriverSchedulingByTruckAndDay(
						onePojo.getGeoDate(), onePojo.getEqpInst().getTruck()
								.getId()));
			}
			if (onePojo.getWorkUser() != null) {
				oneObj.put("userinst", onePojo.getWorkUser().getInstId());
				oneObj.put("userid", onePojo.getWorkUser().getId());
				oneObj.put("username", onePojo.getWorkUser().getName());
				oneObj.put("usermphone", onePojo.getWorkUser().getmPhone());
			} else {
				oneObj.put("userinst", "");
				oneObj.put("userid", "");
				oneObj.put("username", "");
				oneObj.put("usermphone", "");
			}

			oneObj.put(
					"oil",
					!onePojo.getSpeed().equals("") ? URLlImplBase.AllPrinceDiv(
							onePojo.getOil(), 10000) : "0");
			oneObj.put(
					"oildiff",
					!onePojo.getSpeed().equals("") ? URLlImplBase.AllPrinceDiv(
							onePojo.getOilDiff(), 10000) : "0");
			oneObj.put(
					"speed",
					!onePojo.getSpeed().equals("") ? URLlImplBase.AllPrinceDiv(
							onePojo.getSpeed(), 10) : "0");
			oneObj.put("oildate", onePojo.getOilDate());
			oneObj.put("weight", onePojo.getWeight());
			oneObj.put("date", m_bs.getDateEx().getThisDate(0, 0));
			userlist.add(oneObj);
		}
		retJSON.put("data", userlist);
		retJSON.put("count", paras.getLong("max"));
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchTruckVideoList
	 * </p>
	 * <p>
	 * 方法描述：搜索车辆视频列表
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchTruckVideoList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject retJSON = new JSONObject();
		String truckId = m_bs.getPrivateMap().get("pg_truck");
		JSONObject paras = new JSONObject();
		if (truckId != null) {
			paras.put("truck", truckId);
		}
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			paras.put("login", user.getGroupId());
		}
		// 调用BI
		BITruck truckBI = new BITruck(null, m_bs);
		// 返回数据
		JSONArray userlist = new JSONArray();
		ArrayList<TruckVideoPojo> list = truckBI.getTruckVideoList(paras);
		for (TruckVideoPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", userlist.size() + 1);
			oneObj.put("videoid", onePojo.getId());
			oneObj.put("ip", onePojo.getIp());
			oneObj.put("port", onePojo.getPort());
			oneObj.put("user", onePojo.getUser());
			oneObj.put("password", onePojo.getPassword());
			oneObj.put("eqpno", onePojo.getEqpNo());
			oneObj.put("truckid", onePojo.getTruck().getId());
			oneObj.put("trucknno", onePojo.getTruck().getNo());
			oneObj.put("truckname", onePojo.getTruck().getName());
			oneObj.put("trucktype", onePojo.getTruck().getDefine().getName());
			oneObj.put("paltenum", onePojo.getTruck().getPlateNum());
			userlist.add(oneObj);
		}
		retJSON.put("data", userlist);
		retJSON.put("count", userlist.size());
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getUserTraceList
	 * </p>
	 * <p>
	 * 方法描述：搜索设备一定时间内的路径
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_getUserTraceList(BSObject m_bs) throws Exception {
		// 数据准备
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject paras = new JSONObject();
		String userId = m_bs.getPrivateMap().get("pg_uinst");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		paras.put("user", userId);
		if (sDate == null || sDate.equals("")) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -1);
			sDate = m_bs.getDateEx().getCalendarToStringAll(date);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getCalendarToStringAll(
					Calendar.getInstance());
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		paras.put("date", sDate + "," + eDate);

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
			pageSize = 2000;
		}
		startNum = pageSize * pageNum;
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		ArrayList<EquipmentGeometryPojo> list = eqpBI.getEqpGeometryList(paras,
				startNum, startNum + pageSize - 1);
		for (EquipmentGeometryPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", startNum + retObj.size() + 1);
			oneObj.put("eqpid", onePojo.getEqpInst().getInstId());
			oneObj.put("eqptype", onePojo.getEqpInst().getEqpDef().getName());
			if (onePojo.getEqpInst().getMangUser() != null) {
				oneObj.put("userinst", onePojo.getEqpInst().getMangUser()
						.getInstId());
				oneObj.put("userid", onePojo.getEqpInst().getMangUser().getId());
				oneObj.put("username", onePojo.getEqpInst().getMangUser()
						.getName());
				oneObj.put("userphone", onePojo.getEqpInst().getMangUser()
						.getmPhone());
				if (onePojo.getEqpInst().getMangUser().getOrg() != null) {
					oneObj.put("userorg", onePojo.getEqpInst().getMangUser()
							.getOrg().getAllName().replaceAll(",", "-"));
				} else {
					oneObj.put("userorg", "");
				}
			}
			oneObj.put("lat", onePojo.getLatitude());
			oneObj.put("lon", onePojo.getLongitude());
			oneObj.put("bdlat", "");
			oneObj.put("bdlon", "");
			try {
				if (!onePojo.getLatitude().equals("")
						&& !onePojo.getLongitude().equals("")) {
					Gps bdGPS = CoordTransformBI.Gps84_To_bd09(
							Double.valueOf(onePojo.getLatitude()),
							Double.valueOf(onePojo.getLongitude()));
					if (bdGPS != null) {
						oneObj.put("bdlat", bdGPS.getWgLat());
						oneObj.put("bdlon", bdGPS.getWgLon());
					}
				}
			} catch (Exception ex) {
			}

			oneObj.put("cdate", onePojo.getCreateDate());
			retObj.add(oneObj);
		}
		fretObj.put("data", retObj);
		fretObj.put("count", paras.getLong("max"));
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getTruckTracePointList
	 * </p>
	 * <p>
	 * 方法描述：搜索设备一定时间内的路径
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_getTruckTracePointList(BSObject m_bs) throws Exception {
		// 数据准备
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject paras = new JSONObject();
		String truckId = m_bs.getPrivateMap().get("pg_truck");
		String fdId = m_bs.getPrivateMap().get("pg_fdid");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		paras.put("truck", truckId);
		if (sDate == null || sDate.equals("")) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -1);
			sDate = m_bs.getDateEx().getCalendarToStringAll(date);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getCalendarToStringAll(
					Calendar.getInstance());
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		paras.put("date", sDate + "," + eDate);
		if (fdId != null) {
			paras.put("fdid", fdId);
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
			pageSize = 2000;
		}
		startNum = pageSize * pageNum;
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		// BITruck truckBI = new BITruck(null, m_bs);
		ArrayList<EquipmentGeometryPojo> list = eqpBI.getEqpGeometryList(paras,
				startNum, startNum + pageSize - 1);
		for (EquipmentGeometryPojo onePojo : list) {
			// 定位信息
			if (!onePojo.getLatitude().equals("")
					&& !onePojo.getLongitude().equals("")) {
				JSONObject oneObj = new JSONObject();
				oneObj.put("tbindex", startNum + retObj.size() + 1);
				oneObj.put("bdlat", "");
				oneObj.put("bdlon", "");
				try {
					Gps bdGPS = CoordTransformBI.Gps84_To_bd09(
							Double.valueOf(onePojo.getLatitude()),
							Double.valueOf(onePojo.getLongitude()));
					oneObj.put("bdlat", bdGPS.getWgLat());
					oneObj.put("bdlon", bdGPS.getWgLon());
				} catch (Exception ex) {
				}

				oneObj.put("eqpid", onePojo.getEqpInst().getInstId());
				oneObj.put("eqptype", onePojo.getEqpInst().getEqpDef()
						.getName());
				oneObj.put("date", onePojo.getCreateDate());
				// 司机
				/*
				 * UserPojo driverU = null; if
				 * (!onePojo.getCreateDate().equals("")) { driverU =
				 * (truckBI.getDriverSchedulingByTruckAndDay(
				 * onePojo.getCreateDate(), onePojo.getEqpInst()
				 * .getTruck().getId())); } if (driverU != null) {
				 * oneObj.put("userinst", driverU.getInstId());
				 * oneObj.put("userid", driverU.getId()); oneObj.put("username",
				 * driverU.getName()); oneObj.put("usermphone",
				 * driverU.getmPhone()); } else { oneObj.put("userinst", "");
				 * oneObj.put("userid", ""); oneObj.put("username", "");
				 * oneObj.put("usermphone", ""); }
				 */
				if (onePojo.getEqpInst().getTruck() != null) {
					oneObj.put("truckid", onePojo.getEqpInst().getTruck()
							.getId());
					oneObj.put("truckno", onePojo.getEqpInst().getTruck()
							.getNo());
					if (onePojo.getEqpInst().getTruck().getOrg() != null) {
						oneObj.put("truckorg", onePojo.getEqpInst().getTruck()
								.getOrg().getAllName().replaceAll(",", "-"));
					} else {
						oneObj.put("truckorg", "");
					}
					// oneObj.put("trucktype", onePojo.getEqpInst().getTruck()
					// .getDefine().getName());
					// oneObj.put("truckname", onePojo.getEqpInst().getTruck()
					// .getName());
					oneObj.put("platenum", onePojo.getEqpInst().getTruck()
							.getPlateNum());
				}
				// oneObj.put("lat", onePojo.getLatitude());
				// oneObj.put("lon", onePojo.getLongitude());
				oneObj.put("fanceflg", onePojo.getFanceFlg());
				oneObj.put("cdate", onePojo.getCreateDate());
				retObj.add(oneObj);
			}
		}
		fretObj.put("data", retObj);
		fretObj.put("count", paras.getLong("max"));
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getTruckTraceList
	 * </p>
	 * <p>
	 * 方法描述：搜索设备一定时间内的路径
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_getTruckTraceList(BSObject m_bs) throws Exception {
		// 数据准备
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject paras = new JSONObject();
		String truckId = m_bs.getPrivateMap().get("pg_truck");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		paras.put("truck", truckId);
		if (sDate == null || sDate.equals("")) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -1);
			sDate = m_bs.getDateEx().getCalendarToStringAll(date);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getCalendarToStringAll(
					Calendar.getInstance());
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		paras.put("date", sDate + "," + eDate);

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
			pageSize = 2000;
		}
		startNum = pageSize * pageNum;
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		BITruck truckBI = new BITruck(null, m_bs);
		ArrayList<EquipmentGeometryPojo> list = eqpBI.getEqpGeometryList(paras,
				startNum, startNum + pageSize - 1);
		JSONObject allJOSN = new JSONObject();
		allJOSN.put("distance", 0);
		allJOSN.put("name", "总里程");
		JSONObject oneFDObj = new JSONObject();
		allJOSN.put("distance", 0);
		allJOSN.put("speed", 0);
		JSONObject oldGeo = null;
		String fdId = "";
		int fdIndex = 0;
		for (int i = 0, size = list.size(); i < size; i++) {
			EquipmentGeometryPojo onePojo = list.get(i);
			// 当前信息
			JSONObject oneObj = new JSONObject();
			oneObj.put("lat", onePojo.getLatitude());
			oneObj.put("lon", onePojo.getLongitude());
			oneObj.put("date", onePojo.getCreateDate());
			oneObj.put("speed", onePojo.getSpeed());
			oneObj.put("bdlat", "");
			oneObj.put("bdlon", "");
			try {
				if (!onePojo.getLatitude().equals("")
						&& !onePojo.getLongitude().equals("")) {
					Gps bdGPS = CoordTransformBI.Gps84_To_bd09(
							Double.valueOf(onePojo.getLatitude()),
							Double.valueOf(onePojo.getLongitude()));
					oneObj.put("bdlat", bdGPS.getWgLat());
					oneObj.put("bdlon", bdGPS.getWgLon());
				}
			} catch (Exception ex) {
			}

			if (i == 0) {
				// 初始化位置信息
				allJOSN.put("fdate", onePojo.getCreateDate());
				allJOSN.put("tdate", onePojo.getCreateDate());
				allJOSN.put("eqpid", onePojo.getEqpInst().getInstId());
				allJOSN.put("eqptype", onePojo.getEqpInst().getEqpDef()
						.getName());
				// 责任人
				if (onePojo.getEqpInst().getMangUser() != null) {
					allJOSN.put("userinst", onePojo.getEqpInst().getMangUser()
							.getInstId());
					allJOSN.put("userid", onePojo.getEqpInst().getMangUser()
							.getId());
					allJOSN.put("username", onePojo.getEqpInst().getMangUser()
							.getName());
					allJOSN.put("userphone", onePojo.getEqpInst().getMangUser()
							.getmPhone());
				} else {
					allJOSN.put("userinst", "");
					allJOSN.put("userid", "");
					allJOSN.put("username", "");
					allJOSN.put("userphone", "");
				}

				if (onePojo.getEqpInst().getTruck() != null) {
					allJOSN.put("truckid", onePojo.getEqpInst().getTruck()
							.getId());
					allJOSN.put("truckno", onePojo.getEqpInst().getTruck()
							.getNo());
					if (onePojo.getEqpInst().getTruck().getOrg() != null) {
						allJOSN.put("truckorg", onePojo.getEqpInst().getTruck()
								.getOrg().getAllName().replaceAll(",", "-"));
					} else {
						allJOSN.put("truckorg", "");
					}
					allJOSN.put("trucktype", onePojo.getEqpInst().getTruck()
							.getDefine().getName());
					allJOSN.put("truckname", onePojo.getEqpInst().getTruck()
							.getName());
					allJOSN.put("platenum", onePojo.getEqpInst().getTruck()
							.getPlateNum());
				}
				// 起点
				fdIndex++;
				oneFDObj.put("分段", "分段" + (fdIndex));
				oneFDObj.put("fdid", onePojo.getFdId());
				oneFDObj.put("fdate", oneObj.getString("date"));
				oneFDObj.put("tdate", oneObj.getString("date"));
				allJOSN.put("tdate", oneObj.getString("date"));
				oneFDObj.put("flat", oneObj.getString("lat"));
				oneFDObj.put("flon", oneObj.getString("lon"));
				oneFDObj.put("bdflat", oneObj.getString("bdlat"));
				oneFDObj.put("bdflon", oneObj.getString("bdlon"));
				oneFDObj.put("distance", "0");
				oneFDObj.put("speed", "0");
				fdId = onePojo.getFdId();
			} else {
				if (!fdId.equals(onePojo.getFdId())) {
					oneFDObj.put("tbindex", retObj.size() + 1);
					retObj.add(oneFDObj);
					allJOSN.put(
							"distance",
							URLlImplBase.AllPrince(
									allJOSN.getString("distance"),
									oneFDObj.getString("distance")));
					// 平均速度
					if (allJOSN.containsKey("speed")) {
						allJOSN.put("speed", URLlImplBase.AllPrinceDiv(
								Float.parseFloat(allJOSN.getString("speed"))
										+ Float.parseFloat(oneFDObj
												.getString("speed")), 2));
					} else {
						allJOSN.put("speed", oneFDObj.getString("speed"));
					}

					// 新的分段
					fdId = onePojo.getFdId();
					fdIndex++;
					oneFDObj = new JSONObject();
					oneFDObj.put("分段", "分段" + (fdIndex));
					// 新的分段起点
					oneFDObj.put("fdid", onePojo.getFdId());
					oneFDObj.put("fdate", oneObj.getString("date"));
					allJOSN.put("tdate", oneObj.getString("date"));
					oneFDObj.put("tdate", oneObj.getString("date"));
					oneFDObj.put("flat", oneObj.getString("lat"));
					oneFDObj.put("flon", oneObj.getString("lon"));
					oneFDObj.put("bdflat", oneObj.getString("bdlat"));
					oneFDObj.put("bdflon", oneObj.getString("bdlon"));
					oneFDObj.put("distance", "0");
					oneFDObj.put("speed", "0");
				} else {
					// 分段整合
					oneFDObj.put("tdate", oneObj.getString("date"));
					allJOSN.put("tdate", oneObj.getString("date"));
					// 终点
					oneFDObj.put("tlat", oneObj.getString("lat"));
					oneFDObj.put("tlon", oneObj.getString("lon"));
					oneFDObj.put("bdtlat", oneObj.getString("bdlat"));
					oneFDObj.put("bdtlon", oneObj.getString("bdlon"));
					// 距离
					oneFDObj.put("distance", URLlImplBase.AllPrinceDiv(String
							.valueOf(DistanceComputerBI.computeDistance(
									oldGeo.getDouble("lat"),
									oldGeo.getDouble("lon"),
									oneObj.getDouble("lat"),
									oneObj.getDouble("lon"))), 1000));
					// 平均速度
					oneFDObj.put(
							"speed",
							URLlImplBase.AllPrinceDiv(
									Float.parseFloat(oldGeo.getString("speed"))
											+ Float.parseFloat(oneObj
													.getString("speed")), 2));
				}
			}
			oldGeo = oneObj;
			if (!oneFDObj.containsKey("eqpid")) {
				oneFDObj.put("eqpid", onePojo.getEqpInst().getInstId());
				oneFDObj.put("eqptype", onePojo.getEqpInst().getEqpDef()
						.getName());
				// 司机
				UserPojo driverU = null;
				if (!onePojo.getCreateDate().equals("")) {
					driverU = (truckBI.getDriverSchedulingByTruckAndDay(
							onePojo.getCreateDate(), onePojo.getEqpInst()
									.getTruck().getId()));
				}
				if (driverU != null) {
					oneFDObj.put("userinst", driverU.getInstId());
					oneFDObj.put("userid", driverU.getId());
					oneFDObj.put("username", driverU.getName());
					oneFDObj.put("usermphone", driverU.getmPhone());
				} else {
					oneFDObj.put("userinst", "");
					oneFDObj.put("userid", "");
					oneFDObj.put("username", "");
					oneFDObj.put("usermphone", "");
				}
				if (onePojo.getEqpInst().getTruck() != null) {
					oneFDObj.put("truckid", onePojo.getEqpInst().getTruck()
							.getId());
					oneFDObj.put("truckno", onePojo.getEqpInst().getTruck()
							.getNo());
					if (onePojo.getEqpInst().getTruck().getOrg() != null) {
						oneFDObj.put("truckorg",
								onePojo.getEqpInst().getTruck().getOrg()
										.getAllName().replaceAll(",", "-"));
					} else {
						oneFDObj.put("truckorg", "");
					}
					oneFDObj.put("trucktype", onePojo.getEqpInst().getTruck()
							.getDefine().getName());
					oneFDObj.put("truckname", onePojo.getEqpInst().getTruck()
							.getName());
					oneFDObj.put("platenum", onePojo.getEqpInst().getTruck()
							.getPlateNum());
				}
			}
		}
		if (list.size() > 0) {
			// 最后一个段
			oneFDObj.put("tbindex", retObj.size() + 1);
			retObj.add(oneFDObj);
			allJOSN.put("distance", URLlImplBase.AllPrince(
					allJOSN.getString("distance"),
					oneFDObj.getString("distance")));
			// 平均速度
			if (allJOSN.containsKey("speed")) {
				allJOSN.put(
						"speed",
						URLlImplBase.AllPrinceDiv(
								Float.parseFloat(allJOSN.getString("speed"))
										+ Float.parseFloat(oneFDObj
												.getString("speed")), 2));
			} else {
				allJOSN.put("speed", oneFDObj.getString("speed"));
			}
		}
		retObj.add(0, allJOSN);
		fretObj.put("data", retObj);
		fretObj.put("count", retObj.size());
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
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
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String group = m_bs.getPrivateMap().get("pg_group");
		String truck = m_bs.getPrivateMap().get("pg_truck");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		long days = 0;
		JSONObject paras = new JSONObject();
		if (sText != null) {
			paras.put("key", sText);
		}
		if (sDate == null || sDate.equals("")) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -1);
			sDate = m_bs.getDateEx().getCalendarToStringAll(date);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getCalendarToStringAll(
					Calendar.getInstance());
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		paras.put("date", sDate + "," + eDate);
		days = m_bs.getDateEx().getDateCount(sDate, eDate);
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
		// 调用BI
		BIStats statsBI = new BIStats(null, m_bs);
		// 返回数据
		JSONArray retlist = new JSONArray();
		ArrayList<TruckReportPojo> list = statsBI.getTruckReportList(paras);
		if (days == 0 && paras.containsKey("days")) {
			days = paras.getLong("days");
		}
		for (TruckReportPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", retlist.size() + 1);
			oneObj.put("truckid", onePojo.getTruck().getId());
			oneObj.put("truckno", onePojo.getTruck().getNo());
			oneObj.put("truckorg", onePojo.getTruck().getOrg().getAllName()
					.replaceAll(",", "-"));
			oneObj.put("truckname", onePojo.getTruck().getName());
			oneObj.put("platenum", onePojo.getTruck().getPlateNum());
			oneObj.put("truckbrand", onePojo.getTruck().getDefine().getBrand());
			oneObj.put("trucktype", onePojo.getTruck().getDefine().getName());
			oneObj.put("userid", onePojo.getTruck().getMangUser().getId());
			oneObj.put("username", onePojo.getTruck().getMangUser().getName());
			oneObj.put("userphone", onePojo.getTruck().getMangUser()
					.getmPhone());
			oneObj.put("oil", onePojo.getOil());
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
			// 使用率
			oneObj.put("speed", onePojo.getSpeed());

			retlist.add(oneObj);
		}
		retJSON.put("data", retlist);
		retJSON.put("count", retlist.size());
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
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
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String group = m_bs.getPrivateMap().get("pg_group");
		String userInst = m_bs.getPrivateMap().get("pg_user");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		JSONObject paras = new JSONObject();
		long days = 0;
		paras.put("role", "OUTDOORS_STAFF,USER_MANG,TRUCK_MANG,DRIVER");
		if (sText != null) {
			paras.put("key", sText);
		}
		if (sDate == null || sDate.equals("")) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -1);
			sDate = m_bs.getDateEx().getCalendarToStringAll(date);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getCalendarToStringAll(
					Calendar.getInstance());
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		paras.put("date", sDate + "," + eDate);
		days = m_bs.getDateEx().getDateCount(sDate, eDate);
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
		// 调用BI
		BIStats statsBI = new BIStats(null, m_bs);
		// 返回数据
		JSONArray retlist = new JSONArray();
		ArrayList<UserReportPojo> list = statsBI.getUserReportList(paras);
		if (days == 0 && paras.containsKey("days")) {
			days = paras.getLong("days");
		}
		for (UserReportPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", retlist.size() + 1);
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
		retJSON.put("count", retlist.size());
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchUserReportListEX
	 * </p>
	 * <p>
	 * 方法描述：搜索员工工作报告情况
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchUserReportListByWeek(BSObject m_bs)
			throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String userInst = m_bs.getPrivateMap().get("pg_uinst");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String daysStr = m_bs.getPrivateMap().get("pg_days");// 间隔时间
		JSONObject paras = new JSONObject();
		long days = 6;
		if (daysStr != null && !daysStr.equals("")) {
			days = Long.parseLong(daysStr);
		}
		if (sText != null) {
			paras.put("key", sText);
		}
		if (sDate == null || sDate.equals("")) {
			sDate = m_bs.getDateEx().getThisDate(0, -14);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getThisDate(0, 0);
		}
		if (userInst != null) {
			paras.put("user", userInst);
		}
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			paras.put("login", user.getGroupId());
		}
		// 开始时间(周一)
		Calendar sCdate = m_bs.getDateEx().getStringToCalendar(sDate);
		sCdate.setFirstDayOfWeek(Calendar.MONDAY);
		sCdate.add(Calendar.DATE,
				sCdate.getFirstDayOfWeek() - sCdate.get(Calendar.DAY_OF_WEEK));
		// 结束时间(周日)
		Calendar eCdate = m_bs.getDateEx().getStringToCalendar(eDate);
		eCdate.setFirstDayOfWeek(Calendar.MONDAY);
		if (eCdate.get(Calendar.DAY_OF_WEEK) != 1) {
			eCdate.add(
					Calendar.DATE,
					eCdate.getFirstDayOfWeek()
							- eCdate.get(Calendar.DAY_OF_WEEK) - 1);
		}
		days = (m_bs.getDateEx().getDateCount(sDate, eDate) + 1) / 7;
		// 调用BI
		BIStats statsBI = new BIStats(null, m_bs);
		// 返回数据
		JSONArray retlist = new JSONArray();
		for (int i = 0; i < days; i++) {
			sDate = m_bs.getDateEx().getCalendarToString(sCdate) + " 00:00:00";
			sCdate.add(Calendar.DATE, 6);
			eDate = m_bs.getDateEx().getCalendarToString(sCdate) + " 23:59:59";
			paras.put("date", sDate + "," + eDate);
			ArrayList<UserReportPojo> list = statsBI.getUserReportList(paras);
			for (UserReportPojo onePojo : list) {
				JSONObject oneObj = new JSONObject();
				oneObj.put("date", m_bs.getDateEx().getCalendarToString(sCdate));
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
				oneObj.put(
						"worktime",
						Float.parseFloat(URLlImplBase.AllPrinceDiv(
								onePojo.getWorkTime(), 3600)));
				oneObj.put("step", onePojo.getStep());
				oneObj.put("distance",
						URLlImplBase.AllPrinceDiv(onePojo.getDistance(), 1000));
				oneObj.put("late", onePojo.getLate());
				oneObj.put("abs", 7 - onePojo.getAbs());
				if (!onePojo.getUser().getBirthday().equals("")) {
					oneObj.put("age", URLlImplBase.getAge(onePojo.getUser()
							.getBirthday()));
				} else {
					oneObj.put("age", 0);
				}

				oneObj.put("tbindex", retlist.size() + 1);
				retlist.add(oneObj);
			}
			sCdate.add(Calendar.DATE, 1);
		}

		retJSON.put("data", retlist);
		retJSON.put("count", retlist.size());
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchTruckFaultReportList
	 * </p>
	 * <p>
	 * 方法描述：得到车辆报警列表
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchTruckFaultReportList(BSObject m_bs)
			throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String group = m_bs.getPrivateMap().get("pg_group");
		String state = m_bs.getPrivateMap().get("pg_state");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String truck = m_bs.getPrivateMap().get("pg_truck");
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
		BIFault faultBI = new BIFault(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (sDate == null || sDate.equals("")) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -1);
			sDate = m_bs.getDateEx().getCalendarToStringAll(date);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getCalendarToStringAll(
					Calendar.getInstance());
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		_paras.put("hdate", sDate + "," + eDate);
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			_paras.put("tlogin", user.getGroupId());
		}
		if (group != null) {
			_paras.put("tgroup", group);
		}
		if (truck != null) {
			_paras.put("truck", truck);
		}
		_paras.put("type",
				"1AA40EFE45EE2BA75E1C4ADE20F796C2,12D24119E79CFA25144A19EDDEA53A94");
		if (state != null) {
			_paras.put("state", state);
		}

		ArrayList<FaultReportPojo> list = faultBI.getFaultReportList(_paras,
				startNum, startNum + pageSize - 1);
		for (FaultReportPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", startNum + retObj.size() + 1);
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("faultid", onePojo.getFaultCode().getId());
			oneObj.put("faule", onePojo.getFaultCode().getName());
			oneObj.put("type", onePojo.getFaultType().getName());
			oneObj.put("from", onePojo.getFaultFrom().getName());
			oneObj.put("hdate", onePojo.getHappenDate());
			oneObj.put("edate", onePojo.getEndDate());
			//
			oneObj.put("cuserinst", onePojo.getCreateUser().getInstId());
			oneObj.put("cuserid", onePojo.getCreateUser().getId());
			oneObj.put("cusername", onePojo.getCreateUser().getName());
			oneObj.put("cusermphone", onePojo.getCreateUser().getmPhone());
			//
			oneObj.put("userinst", onePojo.getFrUser().getInstId());
			oneObj.put("userid", onePojo.getFrUser().getId());
			oneObj.put("username", onePojo.getFrUser().getName());
			oneObj.put("usermphone", onePojo.getFrUser().getmPhone());
			//
			oneObj.put("truckid", onePojo.getEqpInst().getTruck().getId());
			oneObj.put("trucknno", onePojo.getEqpInst().getTruck().getNo());
			oneObj.put("truckname", onePojo.getEqpInst().getTruck().getName());
			oneObj.put("trucktype", onePojo.getEqpInst().getTruck().getDefine()
					.getName());
			if (onePojo.getEqpInst().getTruck().getOrg() != null) {
				oneObj.put("truckorg", onePojo.getEqpInst().getTruck().getOrg()
						.getAllName().replaceAll(",", "-"));
			} else {
				oneObj.put("truckorg", "");
			}
			oneObj.put("paltenum", onePojo.getEqpInst().getTruck()
					.getPlateNum());
			oneObj.put("opuserinst", onePojo.getOpUser().getInstId());
			oneObj.put("opuserid", onePojo.getOpUser().getId());
			oneObj.put("opusername", onePojo.getOpUser().getName());
			oneObj.put("opusermphone", onePojo.getOpUser().getName());
			oneObj.put("opstatev", onePojo.getOpState());
			oneObj.put("opstate",
					FaultReportPojo.STATE_NAME[onePojo.getOpState()]);
			oneObj.put("opdate", onePojo.getOpDate());

			retObj.add(oneObj);
		}
		fretObj.put("data", retObj);
		fretObj.put("count", _paras.getLong("max"));
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchUserFaultReportList
	 * </p>
	 * <p>
	 * 方法描述：得到车辆报警列表
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchUserFaultReportList(BSObject m_bs)
			throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String group = m_bs.getPrivateMap().get("pg_group");
		String state = m_bs.getPrivateMap().get("pg_state");
		String userId = m_bs.getPrivateMap().get("pg_uinst");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
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
			pageSize = 20;
		}
		startNum = pageSize * pageNum;
		BIFault faultBI = new BIFault(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (userId != null) {
			_paras.put("user", userId);
		}
		if (sDate == null || sDate.equals("")) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -1);
			sDate = m_bs.getDateEx().getCalendarToStringAll(date);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getCalendarToStringAll(
					Calendar.getInstance());
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		_paras.put("hdate", sDate + "," + eDate);
		if (group != null) {
			_paras.put("ugroup", group);
		}
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			_paras.put("ulogin", user.getGroupId());
		}
		_paras.put("type",
				"2B27A3287D25F3EA18C8FDAFA1EEB25F,A93012F52325FBCA228CB09A89FF408E");
		if (state != null) {
			_paras.put("state", state);
		}

		ArrayList<FaultReportPojo> list = faultBI.getFaultReportList(_paras,
				startNum, startNum + pageSize - 1);
		for (FaultReportPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", startNum + retObj.size() + 1);
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("faultid", onePojo.getFaultCode().getId());
			oneObj.put("faule", onePojo.getFaultCode().getName());
			oneObj.put("type", onePojo.getFaultType().getName());
			oneObj.put("from", onePojo.getFaultFrom().getName());
			oneObj.put("hdate", onePojo.getHappenDate());
			oneObj.put("edate", onePojo.getEndDate());
			//
			oneObj.put("cuserinst", onePojo.getCreateUser().getInstId());
			oneObj.put("cuserid", onePojo.getCreateUser().getId());
			oneObj.put("cusername", onePojo.getCreateUser().getName());
			oneObj.put("cusermphone", onePojo.getCreateUser().getmPhone());
			//
			oneObj.put("userinst", onePojo.getFrUser().getInstId());
			oneObj.put("userid", onePojo.getFrUser().getId());
			oneObj.put("username", onePojo.getFrUser().getName());
			oneObj.put("usermphone", onePojo.getFrUser().getmPhone());
			if (onePojo.getFrUser().getOrg() != null) {
				oneObj.put("userorg", onePojo.getFrUser().getOrg().getAllName()
						.replaceAll(",", "-"));
			} else {
				oneObj.put("userorg", "");
			}

			oneObj.put("opuserinst", onePojo.getOpUser().getInstId());
			oneObj.put("opuserid", onePojo.getOpUser().getId());
			oneObj.put("opusername", onePojo.getOpUser().getName());
			oneObj.put("opusermphone", onePojo.getOpUser().getName());
			oneObj.put("opstatev", onePojo.getOpState());
			oneObj.put("opstate",
					FaultReportPojo.STATE_NAME[onePojo.getOpState()]);
			oneObj.put("opdate", onePojo.getOpDate());

			retObj.add(oneObj);
		}
		fretObj.put("data", retObj);
		fretObj.put("count", _paras.getLong("max"));
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getOneFaultReportById
	 * </p>
	 * <p>
	 * 方法描述：根据数据字典ID得到一个数据字典
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getOneFaultReportById(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		FaultReportPojo onePojo = new BIFault(m_bs)
				.getOneFaultReportById((String) m_bs.getPrivateMap().get(
						"pg_faultid"));
		if (onePojo != null) {
			dataObj.put("id", onePojo.getId());
			dataObj.put("name", onePojo.getName());
			dataObj.put("faule", onePojo.getFaultCode().getName());
			dataObj.put("type", onePojo.getFaultType().getName());
			dataObj.put("from", onePojo.getFaultFrom().getName());
			dataObj.put("hdate", onePojo.getHappenDate());
			dataObj.put("edate", onePojo.getEndDate());
			//
			dataObj.put("cuserinst", onePojo.getCreateUser().getInstId());
			dataObj.put("cuserid", onePojo.getCreateUser().getId());
			dataObj.put("cusername", onePojo.getCreateUser().getName());
			dataObj.put("cusermphone", onePojo.getCreateUser().getmPhone());
			// 关联用户
			if (onePojo.getFrUser() != null
					&& !onePojo.getFrUser().getInstId().equals("")) {
				//
				dataObj.put("fruserinst", onePojo.getFrUser().getInstId());
				dataObj.put("fruserid", onePojo.getFrUser().getId());
				dataObj.put("frusername", onePojo.getFrUser().getName());
				dataObj.put("frusermphone", onePojo.getFrUser().getmPhone());
				if (onePojo.getFrUser().getOrg() != null) {
					dataObj.put("fruserorg", onePojo.getFrUser().getOrg()
							.getAllName().replaceAll(",", "-"));
				} else {
					dataObj.put("fruserorg", "");
				}
			} else {
				dataObj.put("fruserinst", "");
				dataObj.put("fruserid", "");
				dataObj.put("frusername", "");
				dataObj.put("frusermphone", "");
				dataObj.put("fruserorg", "");
			}
			// 关联设备
			if (onePojo.getEqpInst() != null
					&& onePojo.getEqpInst().getTruck() != null
					&& !onePojo.getEqpInst().getTruck().getId().equals("")) {
				//
				dataObj.put("truckid", onePojo.getEqpInst().getTruck().getId());
				dataObj.put("trucknno", onePojo.getEqpInst().getTruck().getNo());
				dataObj.put("truckname", onePojo.getEqpInst().getTruck()
						.getName());
				dataObj.put("trucktype", onePojo.getEqpInst().getTruck()
						.getDefine().getName());
				if (onePojo.getEqpInst().getTruck().getOrg() != null) {
					dataObj.put("truckorg", onePojo.getEqpInst().getTruck()
							.getOrg().getAllName().replaceAll(",", "-"));
				} else {
					dataObj.put("truckorg", "");
				}
				dataObj.put("paltenum", onePojo.getEqpInst().getTruck()
						.getPlateNum());
			} else {
				dataObj.put("truckid", "");
				dataObj.put("trucknno", "");
				dataObj.put("truckname", "");
				dataObj.put("trucktype", "");
				dataObj.put("truckorg", "");
				dataObj.put("paltenum", "");
			}
			// 操作人
			if (onePojo.getOpUser() != null
					&& !onePojo.getOpUser().getInstId().equals("")) {
				dataObj.put("opuserinst", onePojo.getOpUser().getInstId());
				dataObj.put("opuserid", onePojo.getOpUser().getId());
				dataObj.put("opusername", onePojo.getOpUser().getName());
				dataObj.put("opusermphone", onePojo.getOpUser().getName());
			} else {
				dataObj.put("opuserinst", "");
				dataObj.put("opuserid", "");
				dataObj.put("opusername", "");
				dataObj.put("opusermphone", "");
			}
			dataObj.put("opstatev", onePojo.getOpState());
			dataObj.put("opstate",
					FaultReportPojo.STATE_NAME[onePojo.getOpState()]);
			dataObj.put("opdate", onePojo.getOpDate());
			dataObj.put("optext", onePojo.getOpDesc());
			// 报警文件
			JSONArray fileJSON = new JSONArray();
			for (BFSFilePojo oneFile : onePojo.getFaultFiles()) {
				JSONObject oneFileJSON = new JSONObject();
				oneFileJSON.put("name", oneFile.getFileName());
				oneFileJSON.put("url", BIFile.GetImgURL(oneFile));
				fileJSON.add(oneFileJSON);
			}
			dataObj.put("ffiles", fileJSON);
			// 处理文件
			fileJSON.clear();
			for (BFSFilePojo oneFile : onePojo.getOpFiles()) {
				JSONObject oneFileJSON = new JSONObject();
				oneFileJSON.put("name", oneFile.getFileName());
				oneFileJSON.put("url", BIFile.GetImgURL(oneFile));
				fileJSON.add(oneFileJSON);
			}
			dataObj.put("opfiles", fileJSON);
			fretObj.put("data", dataObj);
			fretObj.put("code", 0);
		} else {
			fretObj.put("code", 902);
		}
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_createTruckFaultReport
	 * </p>
	 * <p>
	 * 方法描述：创建报警
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_createTruckFaultReport(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject fretObj = new JSONObject();
		fretObj.put("code", 900);
		long count = 0;
		BIDic dicBI = new BIDic(m_bs);
		BIFault faultBI = new BIFault(null, m_bs);
		BIUser userBI = new BIUser(null, m_bs);
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		FaultReportPojo onePojo = new FaultReportPojo();
		onePojo.setFaultCode(dicBI.getDicItemByRedis(m_bs.getPrivateMap().get(
				"pg_fcode")));
		if (onePojo.getFaultCode() != null) {
			onePojo.getFaultType().setId(onePojo.getFaultCode().getPitemid());
			onePojo.getFaultFrom().setId("56292F72534CE416A551A21EE17298B7");
			onePojo.setName(m_bs.getPrivateMap().get("pg_fname"));
			onePojo.setContent(m_bs.getPrivateMap().get("pg_ftext"));
			if (m_bs.getPrivateMap().get("pg_ftruck") != null) {
				onePojo.setEqpInst(eqpBI.getOnevehicleInstByTruck(m_bs
						.getPrivateMap().get("pg_ftruck")));
			}
			if (m_bs.getPrivateMap().get("pg_fuser") != null) {
				userBI.getOneUserById(m_bs.getPrivateMap().get("pg_fuser"));
			}
			if (m_bs.getPrivateMap().get("pg_fhdate") != null) {
				onePojo.setHappenDate(m_bs.getPrivateMap().get("pg_fhdate"));
			} else {
				onePojo.setHappenDate(m_bs.getDateEx().getThisDate(0, 0));
			}
			onePojo.getCreateUser().setInstId(user.getUserInst());
			count = faultBI.insertFaultReport(onePojo);
			if (count > 0) {
				fretObj.put("code", 0);
				fretObj.put("id", onePojo.getId());
				// 上传图片
				BIFile fileBI = new BIFile(null, m_bs);
				fileBI.deleteFiles(onePojo.getId() + "_FILES", true);
				BFSFilePojo file = new BFSFilePojo();
				file.setUser(onePojo.getCreateUser());
				file.setInstId("");
				file.setOldId("");
				file.setBissId(onePojo.getId() + "_FILES");
				file.setType(1);
				file.setUpdateDate(m_bs.getDateEx().getThisDate(0, 0));
				file.setEditDate(file.getEditDate());
				file.setFileUrl("/fault/" + onePojo.getId());
				file.setFilePath(file.getFileUrl());
				fileBI.saveUploadUserFile(m_bs, file, BIFile.F_FAULT_CODE,
						m_bs.getFileList());
			}
		} else {
			fretObj.put("code", 901);

		}
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateTruckFaultReportState
	 * </p>
	 * <p>
	 * 方法描述：处理车辆报警
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateTruckFaultReportState(BSObject m_bs)
			throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject fretObj = new JSONObject();
		fretObj.put("code", 900);
		long count = 0;
		String faultId = m_bs.getPrivateMap().get("pg_faultid");
		String state = m_bs.getPrivateMap().get("pg_state");
		FaultReportPojo onePojo = new FaultReportPojo();
		if (faultId != null) {
			onePojo.setId(faultId);
			if (state != null) {
				onePojo.setOpState(Integer.parseInt(state));
			} else {
				onePojo.setOpState(1);
			}
			BIFault faultBI = new BIFault(null, m_bs);
			onePojo.setOpDate(m_bs.getDateEx().getThisDate(0, 0));
			onePojo.getOpUser().setInstId(user.getUserInst());
			count = faultBI.updateFaultReportOP(onePojo);
			if (count > 0) {
				fretObj.put("code", 0);
			}
		} else {
			fretObj.put("code", 901);

		}
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateTruckFaultReportStateEx
	 * </p>
	 * <p>
	 * 方法描述：处理车辆报警扩展接口
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateTruckFaultReportStateEx(BSObject m_bs)
			throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject fretObj = new JSONObject();
		fretObj.put("code", 900);
		long count = 0;
		String faultId = m_bs.getPrivateMap().get("pg_faultid");
		String state = m_bs.getPrivateMap().get("pg_state");
		FaultReportPojo onePojo = new FaultReportPojo();
		if (faultId != null) {
			onePojo.setId(faultId);
			if (state != null) {
				onePojo.setOpState(Integer.parseInt(state));
			} else {
				onePojo.setOpState(1);
			}
			// 处理时间
			if (m_bs.getPrivateMap().get("pg_opdate") != null) {
				onePojo.setOpDate(m_bs.getPrivateMap().get("pg_opdate"));
			} else {
				onePojo.setOpDate(m_bs.getDateEx().getThisDate(0, 0));
			}
			// 处理文字
			if (m_bs.getPrivateMap().get("pg_optext") != null) {
				onePojo.setOpDesc(m_bs.getPrivateMap().get("pg_optext"));
			}
			BIFault faultBI = new BIFault(null, m_bs);
			onePojo.getOpUser().setInstId(user.getUserInst());
			count = faultBI.updateFaultReportOP(onePojo);
			if (count > 0) {
				fretObj.put("code", 0);
				// 上传图片
				BIFile fileBI = new BIFile(null, m_bs);
				fileBI.deleteFiles(onePojo.getId() + "_OPFILES", true);
				BFSFilePojo file = new BFSFilePojo();
				file.setUser(onePojo.getCreateUser());
				file.setInstId("");
				file.setOldId("");
				file.setBissId(onePojo.getId() + "_OPFILES");
				file.setType(1);
				file.setUpdateDate(m_bs.getDateEx().getThisDate(0, 0));
				file.setEditDate(file.getEditDate());
				file.setFileUrl("/fault/" + onePojo.getId());
				file.setFilePath(file.getFileUrl());
				fileBI.saveUploadUserFile(m_bs, file, BIFile.F_FAULT_OP,
						m_bs.getFileList());
			}
		} else {
			fretObj.put("code", 901);

		}
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateUserFaultReportState
	 * </p>
	 * <p>
	 * 方法描述：处理员工报警
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateUserFaultReportState(BSObject m_bs)
			throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject fretObj = new JSONObject();
		fretObj.put("code", 900);
		long count = 0;
		String faultId = m_bs.getPrivateMap().get("pg_faultid");
		String state = m_bs.getPrivateMap().get("pg_state");
		FaultReportPojo onePojo = new FaultReportPojo();
		if (faultId != null) {
			onePojo.setId(faultId);
			if (state != null) {
				onePojo.setOpState(Integer.parseInt(state));
			} else {
				onePojo.setOpState(1);
			}
			BIFault faultBI = new BIFault(null, m_bs);
			onePojo.setOpDate(m_bs.getDateEx().getThisDate(0, 0));
			onePojo.getOpUser().setInstId(user.getUserInst());
			count = faultBI.updateFaultReportOP(onePojo);
			if (count > 0) {
				fretObj.put("code", 0);
			}
		} else {
			fretObj.put("code", 901);

		}
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateUserFaultReportStateEx
	 * </p>
	 * <p>
	 * 方法描述：处理员工报警扩展接口
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateUserFaultReportStateEx(BSObject m_bs)
			throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject fretObj = new JSONObject();
		fretObj.put("code", 900);
		long count = 0;
		String faultId = m_bs.getPrivateMap().get("pg_faultid");
		String state = m_bs.getPrivateMap().get("pg_state");
		FaultReportPojo onePojo = new FaultReportPojo();
		if (faultId != null) {
			onePojo.setId(faultId);
			if (state != null) {
				onePojo.setOpState(Integer.parseInt(state));
			} else {
				onePojo.setOpState(1);
			}
			// 处理时间
			if (m_bs.getPrivateMap().get("pg_opdate") != null) {
				onePojo.setOpDate(m_bs.getPrivateMap().get("pg_opdate"));
			} else {
				onePojo.setOpDate(m_bs.getDateEx().getThisDate(0, 0));
			}
			// 处理文字
			if (m_bs.getPrivateMap().get("pg_optext") != null) {
				onePojo.setOpDesc(m_bs.getPrivateMap().get("pg_optext"));
			}
			BIFault faultBI = new BIFault(null, m_bs);
			onePojo.getOpUser().setInstId(user.getUserInst());
			count = faultBI.updateFaultReportOP(onePojo);
			if (count > 0) {
				fretObj.put("code", 0);
				// 上传图片
				BIFile fileBI = new BIFile(null, m_bs);
				fileBI.deleteFiles(onePojo.getId() + "_OPFILES", true);
				BFSFilePojo file = new BFSFilePojo();
				file.setUser(onePojo.getCreateUser());
				file.setInstId("");
				file.setOldId("");
				file.setBissId(onePojo.getId() + "_OPFILES");
				file.setType(1);
				file.setUpdateDate(m_bs.getDateEx().getThisDate(0, 0));
				file.setEditDate(file.getEditDate());
				file.setFileUrl("/fault/" + onePojo.getId());
				file.setFilePath(file.getFileUrl());
				fileBI.saveUploadUserFile(m_bs, file, BIFile.F_FAULT_OP,
						m_bs.getFileList());
			}
		} else {
			fretObj.put("code", 901);

		}
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchTruckOilStats
	 * </p>
	 * <p>
	 * 方法描述：得到车辆油量
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchTruckOilStats(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String truck = m_bs.getPrivateMap().get("pg_truck");
		String group = m_bs.getPrivateMap().get("pg_group");
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (sDate == null || sDate.trim().equals("")) {
			sDate = m_bs.getDateEx().getThisDate(0, -6);
		}
		if (eDate == null || eDate.trim().equals("")) {
			eDate = m_bs.getDateEx().getThisDate(0, 0);
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		_paras.put("date", sDate + "," + eDate);
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			_paras.put("login", user.getGroupId());
		}
		if (truck != null) {
			_paras.put("truck", truck);
		}
		if (group != null) {
			_paras.put("group", group);
		}
		BIStats statsBI = new BIStats(null, m_bs);
		JSONObject list = statsBI.searchTruckOilStats(_paras);
		// 循环时间
		long day = m_bs.getDateEx().getDateCount(sDate, eDate);
		Calendar sc = m_bs.getDateEx().getStringToCalendar(sDate);
		for (int d = 0; d < day; d++) {
			String oneDay = m_bs.getDateEx().getCalendarToString(sc);
			JSONObject oneDate = new JSONObject();
			oneDate.put("tbindex", retObj.size() + 1);
			oneDate.put("date", oneDay);
			oneDate.put("count", 0);
			if (list.containsKey(oneDay)) {
				oneDate.put("count",
						list.getJSONObject(oneDay).getDouble("count"));
			}

			retObj.add(oneDate);
			sc.add(Calendar.DATE, 1);
		}
		fretObj.put("data", retObj);
		fretObj.put("count", retObj.size());
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchTruckFaultStats
	 * </p>
	 * <p>
	 * 方法描述：得到车辆报警列表
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchTruckFaultStats(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String state = m_bs.getPrivateMap().get("pg_state");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String truck = m_bs.getPrivateMap().get("pg_truck");
		JSONObject _paras = new JSONObject();
		JSONObject _dicParas = new JSONObject();
		_paras.put("key", sText);
		if (sDate == null || sDate.trim().equals("")) {
			sDate = m_bs.getDateEx().getThisDate(0, -6);
		}
		if (eDate == null || eDate.trim().equals("")) {
			eDate = m_bs.getDateEx().getThisDate(0, 0);
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		_paras.put("hdate", sDate + "," + eDate);
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			_paras.put("login", user.getGroupId());
		}
		if (truck != null) {
			_paras.put("truck", truck);
		}
		_paras.put("type",
				"1AA40EFE45EE2BA75E1C4ADE20F796C2,12D24119E79CFA25144A19EDDEA53A94");
		_dicParas.put("dic", "T_FAULTREPORT_FAULT_ID");
		_dicParas
				.put("pitemids",
						"1AA40EFE45EE2BA75E1C4ADE20F796C2,12D24119E79CFA25144A19EDDEA53A94");

		if (state != null) {
			_paras.put("state", state);
		}
		BIStats statsBI = new BIStats(null, m_bs);
		BIDic dicBI = new BIDic(m_bs);
		JSONObject list = statsBI.searchFaultStats(_paras);
		// rows
		JSONArray rows = new JSONArray();
		JSONObject allC = new JSONObject();
		ArrayList<DicItemPojo> faultLise = dicBI.getDicItemList(_dicParas);

		// 循环时间
		long day = m_bs.getDateEx().getDateCount(sDate, eDate);
		Calendar sc = m_bs.getDateEx().getStringToCalendar(sDate);
		JSONArray dayFList = new JSONArray();
		for (int d = 0; d < day; d++) {
			String oneDay = m_bs.getDateEx().getCalendarToString(sc)
					.substring(0, 10);
			JSONObject oneDate = new JSONObject();
			oneDate.put("date", oneDay);
			oneDate.put("count", 0);
			dayFList.clear();
			if (list.containsKey(oneDay)) {
				// 当天有数据
				for (DicItemPojo oneItem : faultLise) {
					JSONObject faultData = list.getJSONObject(oneDay);
					JSONObject oneFault = new JSONObject();
					oneFault.put("faultid", oneItem.getId());
					oneFault.put("fault", oneItem.getName());
					if (faultData.getJSONObject("faults").containsKey(
							oneItem.getId())) {
						oneFault.put("count", faultData.getJSONObject("faults")
								.getJSONObject(oneItem.getId()).getInt("count"));
						if (allC.containsKey(oneItem.getId())) {
							allC.getJSONObject(oneItem.getId()).put(
									"count",
									oneFault.getInt("count")
											+ allC.getJSONObject(
													oneItem.getId()).getInt(
													"count"));
						} else {
							allC.put(oneItem.getId(), oneFault);
						}

					} else {
						oneFault.put("count", 0);
					}
					dayFList.add(oneFault);
					oneDate.put("count",
							oneFault.getInt("count") + oneDate.getInt("count"));
				}
			} else {
				// 当天无数据
				for (DicItemPojo oneItem : faultLise) {
					JSONObject oneFault = new JSONObject();
					oneFault.put("faultid", oneItem.getId());
					oneFault.put("fault", oneItem.getName());
					oneFault.put("count", 0);
					dayFList.add(oneFault);
				}
				oneDate.put("count", 0);
			}
			oneDate.put("faults", dayFList);
			retObj.add(oneDate);
			sc.add(Calendar.DATE, 1);
		}
		fretObj.put("data", retObj);
		for (DicItemPojo oneItem : faultLise) {
			JSONObject oneRow = new JSONObject();
			oneRow.put("faultid", oneItem.getId());
			oneRow.put("fault", oneItem.getName());
			if (allC.containsKey(oneItem.getId())) {
				oneRow.put("count",
						allC.getJSONObject(oneItem.getId()).getInt("count"));
			} else {
				oneRow.put("count", 0);
			}
			rows.add(oneRow);
		}
		fretObj.put("rows", rows);

		fretObj.put("count", retObj.size());
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchUserFaultStats
	 * </p>
	 * <p>
	 * 方法描述：得到用户报警统计
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchUserFaultStats(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String state = m_bs.getPrivateMap().get("pg_state");
		String userId = m_bs.getPrivateMap().get("pg_uinst");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		JSONObject _paras = new JSONObject();
		JSONObject _dicParas = new JSONObject();
		_paras.put("key", sText);
		if (userId != null) {
			_paras.put("user", userId);
		}
		if (sDate == null || sDate.trim().equals("")) {
			sDate = m_bs.getDateEx().getThisDate(0, -6);
		}
		if (eDate == null || eDate.trim().equals("")) {
			eDate = m_bs.getDateEx().getThisDate(0, 0);
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		_paras.put("hdate", sDate + "," + eDate);
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			_paras.put("login", user.getGroupId());
		}
		_paras.put("type",
				"2B27A3287D25F3EA18C8FDAFA1EEB25F,A93012F52325FBCA228CB09A89FF408E");
		_dicParas.put("dic", "T_FAULTREPORT_FAULT_ID");
		_dicParas
				.put("pitemids",
						"2B27A3287D25F3EA18C8FDAFA1EEB25F,A93012F52325FBCA228CB09A89FF408E");

		if (state != null) {
			_paras.put("state", state);
		}
		BIStats statsBI = new BIStats(null, m_bs);
		BIDic dicBI = new BIDic(m_bs);

		JSONObject list = statsBI.searchFaultStats(_paras);
		// rows
		JSONArray rows = new JSONArray();
		JSONObject allC = new JSONObject();
		ArrayList<DicItemPojo> faultLise = dicBI.getDicItemList(_dicParas);

		// 循环时间
		long day = m_bs.getDateEx().getDateCount(sDate, eDate);
		Calendar sc = m_bs.getDateEx().getStringToCalendar(sDate);
		JSONArray dayFList = new JSONArray();
		for (int d = 0; d < day; d++) {
			String oneDay = m_bs.getDateEx().getCalendarToString(sc)
					.substring(0, 10);
			JSONObject oneDate = new JSONObject();
			oneDate.put("date", oneDay);
			oneDate.put("count", 0);
			dayFList.clear();
			if (list.containsKey(oneDay)) {
				// 当天有数据
				for (DicItemPojo oneItem : faultLise) {
					JSONObject faultData = list.getJSONObject(oneDay);
					JSONObject oneFault = new JSONObject();
					oneFault.put("faultid", oneItem.getId());
					oneFault.put("fault", oneItem.getName());
					if (faultData.getJSONObject("faults").containsKey(
							oneItem.getId())) {
						oneFault.put("count", faultData.getJSONObject("faults")
								.getJSONObject(oneItem.getId()).getInt("count"));
						if (allC.containsKey(oneItem.getId())) {
							allC.getJSONObject(oneItem.getId()).put(
									"count",
									oneFault.getInt("count")
											+ allC.getJSONObject(
													oneItem.getId()).getInt(
													"count"));
						} else {
							allC.put(oneItem.getId(), oneFault);
						}

					} else {
						oneFault.put("count", 0);
					}
					dayFList.add(oneFault);
					oneDate.put("count",
							oneFault.getInt("count") + oneDate.getInt("count"));
				}
			} else {
				// 当天无数据
				for (DicItemPojo oneItem : faultLise) {
					JSONObject oneFault = new JSONObject();
					oneFault.put("faultid", oneItem.getId());
					oneFault.put("fault", oneItem.getName());
					oneFault.put("count", 0);
					dayFList.add(oneFault);
				}
				oneDate.put("count", 0);
			}
			oneDate.put("faults", dayFList);
			retObj.add(oneDate);
			sc.add(Calendar.DATE, 1);
		}
		fretObj.put("data", retObj);
		for (DicItemPojo oneItem : faultLise) {
			JSONObject oneRow = new JSONObject();
			oneRow.put("faultid", oneItem.getId());
			oneRow.put("fault", oneItem.getName());
			if (allC.containsKey(oneItem.getId())) {
				oneRow.put("count",
						allC.getJSONObject(oneItem.getId()).getInt("count"));
			} else {
				oneRow.put("count", 0);
			}
			rows.add(oneRow);
		}
		fretObj.put("rows", rows);

		fretObj.put("count", retObj.size());
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchFanceList
	 * </p>
	 * <p>
	 * 方法描述：得到围栏信息页面：www/admin/role/index.html
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchFanceList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String group = m_bs.getPrivateMap().get("pg_group");
		String type = m_bs.getPrivateMap().get("pg_type");
		String truck = m_bs.getPrivateMap().get("pg_truck");
		String userInst = m_bs.getPrivateMap().get("pg_uinst");
		BIFance fanceBI = new BIFance(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			_paras.put("login", user.getGroupId());
		}
		if (group != null) {
			_paras.put("group", group);
		}
		if (userInst != null) {
			_paras.put("user", userInst);
		}
		if (truck != null) {
			_paras.put("truck", truck);
		}
		if (type != null) {
			type = "0," + type;
			_paras.put("type", type);
		}
		ArrayList<FancePojo> list = fanceBI.getFanceList(_paras);
		for (FancePojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("org", onePojo.getOrg().getAllName());
			oneObj.put("typev", onePojo.getType());
			oneObj.put("type", FancePojo.TYPE_NAME[onePojo.getType()]);
			for (int j = 0, size = onePojo.getGeo().size(); j < size; j++) {
				JSONObject onePoint = onePojo.getGeo().getJSONObject(j);
				oneObj.put("bdlat", "");
				oneObj.put("bdlon", "");
				try {
					Gps bdGPS = CoordTransformBI.Gps84_To_bd09(
							Double.valueOf(onePoint.getString("lat")),
							Double.valueOf(onePoint.getString("lon")));
					if (bdGPS != null) {
						onePoint.put("bdlat", bdGPS.getWgLat());
						onePoint.put("bdlon", bdGPS.getWgLon());
					}
				} catch (Exception ex) {
				}

				onePojo.getGeo().set(j, onePoint);
			}
			oneObj.put("points", onePojo.getGeo());
			JSONObject center = new JSONObject();
			if (onePojo.getCenter() != null && onePojo.getCenter().length == 2) {
				center.put("lat", onePojo.getCenter()[1]);
				center.put("lon", onePojo.getCenter()[0]);
				oneObj.put("bdlat", "");
				oneObj.put("bdlon", "");
				try {
					Gps bdGPS = CoordTransformBI.Gps84_To_bd09(
							Double.valueOf(onePojo.getCenter()[1]),
							Double.valueOf(onePojo.getCenter()[0]));
					if (bdGPS != null) {
						center.put("bdlat", bdGPS.getWgLat());
						center.put("bdlon", bdGPS.getWgLon());
					}
				} catch (Exception ex) {
				}

			}
			oneObj.put("center", center);

			retObj.add(oneObj);
		}
		fretObj.put("data", retObj);
		fretObj.put("count", retObj.size());
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchInpectPlanList
	 * </p>
	 * <p>
	 * 方法描述：得到保养计划列表：www/admin/role/index.html
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchInpectPlanList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String truck = m_bs.getPrivateMap().get("pg_truck");
		BIInspect inspectBI = new BIInspect(null, m_bs);
		BIDic dicBI = new BIDic(null, m_bs);
		BIStats statsBI = new BIStats(null, m_bs);
		JSONObject _paras = new JSONObject();
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			_paras.put("login", user.getGroupId());
		}
		if (truck != null) {
			_paras.put("truck", truck);
		}
		ArrayList<InspectPlanPojo> list = inspectBI.getInspectPlanList(_paras,
				0, 5000);
		DicItemPojo inspect01 = dicBI
				.getDicItemByRedis("SCORE_ITEM_INSPECT_01");
		DicItemPojo inspect02 = dicBI
				.getDicItemByRedis("SCORE_ITEM_INSPECT_02");
		DicItemPojo inspect03 = dicBI
				.getDicItemByRedis("SCORE_ITEM_INSPECT_03");
		DicItemPojo inspect = dicBI.getDicItemByRedis("SCORE_ITEM_INSPECT");
		for (InspectPlanPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", retObj.size() + 1);
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getInspectDef().getName());
			oneObj.put("createdate", onePojo.getCreateDate());
			oneObj.put("plandate", onePojo.getPlanDate().substring(0, 10));
			oneObj.put("alltime", onePojo.getInspectDef().getCycle());
			// 得到改时间点到现在的工作总时长
			// inspectBI.get
			_paras.put("date", onePojo.getCreateDate().substring(0, 10)
					+ " 00:00:00,"
					+ m_bs.getDateEx().getThisDate(0, 0).substring(0, 10)
					+ " 23:59:59");
			JSONObject worktime = statsBI.getTruckWorkTimeByWhere(_paras);
			if (worktime.containsKey("alltime")) {
				oneObj.put(
						"usertime",
						Float.parseFloat(URLlImplBase.AllPrinceDiv(
								worktime.getString("alltime"), 3600)));
			} else {
				oneObj.put("usertime", 0);
			}
			retObj.add(oneObj);
		}
		fretObj.put("data", retObj);
		fretObj.put("count", retObj.size());
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchInspectPlanList
	 * </p>
	 * <p>
	 * 方法描述：得到保养计划列表：www/admin/role/index.html
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchInspectPlanList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String truck = m_bs.getPrivateMap().get("pg_truck");
		BIInspect inspectBI = new BIInspect(null, m_bs);
		BIDic dicBI = new BIDic(null, m_bs);
		BIStats statsBI = new BIStats(null, m_bs);
		JSONObject _paras = new JSONObject();
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			_paras.put("login", user.getGroupId());
		}
		if (truck != null) {
			_paras.put("truck", truck);
		}
		ArrayList<InspectPlanPojo> list = inspectBI.getInspectPlanList(_paras,
				0, 5000);
		DicItemPojo inspect01 = dicBI
				.getDicItemByRedis("SCORE_ITEM_INSPECT_01");
		DicItemPojo inspect02 = dicBI
				.getDicItemByRedis("SCORE_ITEM_INSPECT_02");
		DicItemPojo inspect03 = dicBI
				.getDicItemByRedis("SCORE_ITEM_INSPECT_03");
		DicItemPojo inspect = dicBI.getDicItemByRedis("SCORE_ITEM_INSPECT");
		int[] iScore = { 60, 1, 2, 3 };

		if (inspect01 != null && !inspect01.getValue().equals("")) {
			iScore[1] = Integer.parseInt(inspect01.getValue());
		}
		if (inspect02 != null && !inspect02.getValue().equals("")) {
			iScore[2] = Integer.parseInt(inspect02.getValue());
		}
		if (inspect03 != null && !inspect03.getValue().equals("")) {
			iScore[3] = Integer.parseInt(inspect03.getValue());
		}
		if (inspect != null && !inspect.getValue().equals("")) {
			iScore[0] = Integer.parseInt(inspect.getValue());
		}
		int score = 100;
		long outDay = 0;
		boolean scoreOver = false;
		for (InspectPlanPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", retObj.size() + 1);
			oneObj.put("id", onePojo.getId());
			oneObj.put("truck", onePojo.getTruck().getId());
			oneObj.put("name", onePojo.getInspectDef().getName());
			oneObj.put("createdate", onePojo.getCreateDate());
			oneObj.put("plandate", onePojo.getPlanDate().substring(0, 10));
			oneObj.put("alltime", onePojo.getInspectDef().getCycle());
			// 得到改时间点到现在的工作总时长
			// inspectBI.get
			_paras.put("date", onePojo.getCreateDate().substring(0, 10)
					+ " 00:00:00,"
					+ m_bs.getDateEx().getThisDate(0, 0).substring(0, 10)
					+ " 23:59:59");
			JSONObject worktime = statsBI.getTruckWorkTimeByWhere(_paras);
			if (worktime.containsKey("alltime")) {
				oneObj.put(
						"usertime",
						Float.parseFloat(URLlImplBase.AllPrinceDiv(
								worktime.getString("alltime"), 3600)));
			} else {
				oneObj.put("usertime", 0);
			}
			retObj.add(oneObj);
			if (!scoreOver) {
				// 得到逾期天数
				outDay = m_bs.getDateEx().getDateCount(
						oneObj.getString("plandate").substring(0, 10)
								+ " 00:00:00",
						m_bs.getDateEx().getThisDate(0, 0).substring(0, 10)
								+ " 23:59:59") - 1;
				if (outDay > 0) {
					score -= outDay
							* iScore[onePojo.getInspectDef().getDefClass() - 1];
					if (score <= iScore[0]) {
						score = 60;
						scoreOver = true;
					}
				}
			}
		}
		fretObj.put("data", retObj);
		fretObj.put("score", score);
		fretObj.put("scoreall", 100);
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchScoreStats
	 * </p>
	 * <p>
	 * 方法描述：得到系统评分
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchScoreStats(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		JSONObject _paras = new JSONObject();
		_paras.put("state", 0);
		_paras.put("outtime", 1);
		BIStats statsBI = new BIStats(null, m_bs);
		// 车辆评分
		dataObj.put("truck", statsBI.searchTruckStatsScore(_paras));
		dataObj.put("truckall", 100);
		// 用户评分
		dataObj.put("user", statsBI.searchUserStatsScore(_paras));
		dataObj.put("userall", 100);

		fretObj.put("data", dataObj);
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchTruckFixLogsList
	 * </p>
	 * <p>
	 * 方法描述：得到车辆维护日志列表
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchTruckFixLogsList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String group = m_bs.getPrivateMap().get("pg_group");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String truck = m_bs.getPrivateMap().get("pg_truck");
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
		BITruck truckBI = new BITruck(null, m_bs);
		JSONObject _paras = new JSONObject();
		if (sText != null) {
			_paras.put("key", sText);
		}
		if (sDate == null || sDate.equals("")) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -14);
			sDate = m_bs.getDateEx().getCalendarToStringAll(date);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getCalendarToStringAll(
					Calendar.getInstance());
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		_paras.put("date", sDate + "," + eDate);
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			_paras.put("tlogin", user.getGroupId());
		}
		if (group != null) {
			_paras.put("group", group);
		}
		if (truck != null) {
			_paras.put("truck", truck);
		}

		ArrayList<TruckFixLogsPojo> list = truckBI.getTruckFixLogsList(_paras,
				startNum, startNum + pageSize - 1);
		for (TruckFixLogsPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("tbindex", startNum + retObj.size() + 1);
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("date", onePojo.getLogDate());
			oneObj.put("type", onePojo.getType().getName());
			oneObj.put("money", onePojo.getMoney());
			// 车辆
			oneObj.put("truckid", onePojo.getTruck().getId());
			oneObj.put("truckno", onePojo.getTruck().getNo());
			oneObj.put("truckname", onePojo.getTruck().getName());
			oneObj.put("truckdef", onePojo.getTruck().getDefine().getName());
			oneObj.put("platenum", onePojo.getTruck().getPlateNum());
			// 操作人
			oneObj.put("muser", onePojo.getUser().getInstId());
			oneObj.put("muserid", onePojo.getUser().getId());
			oneObj.put("musername", onePojo.getUser().getName());
			oneObj.put("muserphone", onePojo.getUser().getmPhone());
			if (onePojo.getUser().getOrg() != null) {
				oneObj.put("morg", onePojo.getUser().getOrg().getId());
				oneObj.put("morgname", onePojo.getUser().getOrg().getAllName()
						.replaceAll(",", "-"));
			} else {
				oneObj.put("morg", "");
				oneObj.put("morgname", "");
			}

			retObj.add(oneObj);
		}
		fretObj.put("data", retObj);
		fretObj.put("count", _paras.getLong("max"));
		fretObj.put("code", 0);
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getOneTruckFixLogsById
	 * </p>
	 * <p>
	 * 方法描述：根据数据字典ID得到一个数据字典
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getOneTruckFixLogsById(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		TruckFixLogsPojo onePojo = new BITruck(m_bs)
				.getOneTruckFixLogsById((String) m_bs.getPrivateMap().get(
						"pg_logsid"));
		if (onePojo != null) {
			dataObj.put("id", onePojo.getId());
			dataObj.put("name", onePojo.getName());
			dataObj.put("date", onePojo.getLogDate());
			dataObj.put("type", onePojo.getType().getId());
			dataObj.put("content", onePojo.getContent());
			dataObj.put("money", onePojo.getMoney());
			//
			dataObj.put("truckid", onePojo.getTruck().getId());
			dataObj.put("truckno", onePojo.getTruck().getNo());
			dataObj.put("truckname", onePojo.getTruck().getName());
			dataObj.put("truckdef", onePojo.getTruck().getDefine().getName());
			dataObj.put("platenum", onePojo.getTruck().getPlateNum());
			// 用户
			dataObj.put("muserid", onePojo.getUser().getId());
			dataObj.put("musername", onePojo.getUser().getName());
			dataObj.put("muserphone", onePojo.getUser().getmPhone());
			if (onePojo.getUser().getOrg() != null) {
				dataObj.put("morg", onePojo.getUser().getOrg().getId());
				dataObj.put("morgname", onePojo.getUser().getOrg().getAllName()
						.replaceAll(",", "-"));
			} else {
				dataObj.put("morg", "");
				dataObj.put("morgname", "");
			}
			// 文件
			JSONArray fileJSON = new JSONArray();
			for (BFSFilePojo oneFile : onePojo.getFiles()) {
				JSONObject oneFileJSON = new JSONObject();
				oneFileJSON.put("name", oneFile.getFileName());
				oneFileJSON.put("url", BIFile.GetImgURL(oneFile));
				fileJSON.add(oneFileJSON);
			}
			dataObj.put("files", fileJSON);
			fretObj.put("data", dataObj);
			// 下拉框
			fretObj.put("typeitems",
					(new BIDic(null)).getDicItemListByRedis("TRUCK_FIXTYPE"));
			fretObj.put("data", dataObj);
			fretObj.put("code", 0);
		} else {
			fretObj.put("code", 902);
		}
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateTruckFixLogsEx
	 * </p>
	 * <p>
	 * 方法描述：处理车辆维护日志接口
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateTruckFixLogsEx(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject fretObj = new JSONObject();
		fretObj.put("code", 900);
		long count = 0;
		String logsId = m_bs.getPrivateMap().get("pg_logsid");
		TruckFixLogsPojo onePojo = new TruckFixLogsPojo();
		if (logsId == null) {
			logsId = "";
		}
		onePojo.setId(logsId);
		onePojo.getTruck().setId(m_bs.getPrivateMap().get("pg_truck"));
		onePojo.setName(m_bs.getPrivateMap().get("pg_name"));
		onePojo.getType().setId(m_bs.getPrivateMap().get("pg_type"));
		// 操作时间
		if (m_bs.getPrivateMap().get("pg_opdate") != null) {
			onePojo.setLogDate(m_bs.getPrivateMap().get("pg_opdate"));
		} else {
			onePojo.setLogDate(m_bs.getDateEx().getThisDate(0, 0));
		}
		// 维护内容
		if (m_bs.getPrivateMap().get("pg_optext") != null) {
			onePojo.setContent(m_bs.getPrivateMap().get("pg_optext"));
		}
		// 维护金额
		if (m_bs.getPrivateMap().get("pg_money") != null) {
			onePojo.setMoney(m_bs.getPrivateMap().get("pg_money"));
		}
		onePojo.getUser().setInstId(user.getUserInst());
		BITruck truckBI = new BITruck(null, m_bs);
		count = truckBI.updateTruckFixLogs(onePojo);
		if (count > 0) {
			fretObj.put("code", 0);
			// 上传图片
			BIFile fileBI = new BIFile(null, m_bs);
			fileBI.deleteFiles(onePojo.getId() + "_FILES", true);
			BFSFilePojo file = new BFSFilePojo();
			file.setUser(onePojo.getUser());
			file.setInstId("");
			file.setOldId("");
			file.setBissId(onePojo.getId() + "_FILES");
			file.setType(1);
			file.setUpdateDate(m_bs.getDateEx().getThisDate(0, 0));
			file.setEditDate(file.getEditDate());
			file.setFileUrl("/fixlogs/" + onePojo.getId());
			file.setFilePath(file.getFileUrl());
			fileBI.saveUploadUserFile(m_bs, file, BIFile.F_FIXLOGS,
					m_bs.getFileList());
		}
		fretObj.put("msg", URLlImplBase.ErrorMap.get(fretObj.getInt("code")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名称: do_exportUserReportList
	 * </p>
	 * <p>
	 * 方法功能描述:导出用户报表统计。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public BSObject do_exportUserReportList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String group = m_bs.getPrivateMap().get("pg_group");
		String userInst = m_bs.getPrivateMap().get("pg_user");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String filepath = BSCommon.getConfigValue("upload_path") + "/report/";
		String fileName = "user_" + m_bs.getDateEx().getSeqDate()
				+ "report.xls";
		JSONObject paras = new JSONObject();
		long days = 0;
		paras.put("role", "OUTDOORS_STAFF,USER_MANG,TRUCK_MANG,DRIVER");
		if (sText != null) {
			paras.put("key", sText);
		}
		if (sDate == null || sDate.equals("")) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -1);
			sDate = m_bs.getDateEx().getCalendarToStringAll(date);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getCalendarToStringAll(
					Calendar.getInstance());
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		paras.put("date", sDate + "," + eDate);
		days = m_bs.getDateEx().getDateCount(sDate, eDate);
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
		WritableWorkbook workbook = null;
		try {
			(new File(filepath)).mkdirs();
			workbook = Workbook.createWorkbook(new File(filepath + "/"
					+ fileName));
			// 定义xls式样
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 12);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 14,
					WritableFont.BOLD);
			WritableFont BoldFont2 = new WritableFont(WritableFont.ARIAL, 13,
					WritableFont.BOLD);
			WritableCellFormat wcf_title = new WritableCellFormat(BoldFont);
			wcf_title.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_title.setAlignment(Alignment.CENTRE);
			wcf_title.setWrap(true);

			WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_center.setAlignment(Alignment.CENTRE);
			wcf_center.setWrap(true);

			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_left.setAlignment(Alignment.LEFT);
			wcf_left.setWrap(true);

			WritableCellFormat wcf_boldFont = new WritableCellFormat(BoldFont2);
			wcf_boldFont.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_boldFont.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_boldFont.setAlignment(Alignment.CENTRE);
			wcf_boldFont.setWrap(true);
			//
			WritableSheet sheet = null;
			sheet = workbook.createSheet("员工报表", 0);
			// 列行合并
			sheet.mergeCells(0, 0, 10, 0);// x,y
			sheet.mergeCells(0, 1, 10, 1);
			// 设置表头
			sheet.setRowView(0, 700);
			sheet.setRowView(1, 600);
			sheet.setRowView(2, 600);
			sheet.addCell(new Label(0, 0, "员工报表", wcf_boldFont));
			sheet.addCell(new Label(0, 1, "时间从：" + sDate + " 到：" + eDate,
					wcf_boldFont));
			sheet.setColumnView(0, 10);
			sheet.setColumnView(1, 40);
			sheet.setColumnView(2, 18);
			sheet.setColumnView(3, 18);
			sheet.setColumnView(4, 15);
			sheet.setColumnView(5, 30);
			sheet.setColumnView(6, 30);
			sheet.setColumnView(7, 20);
			sheet.setColumnView(8, 20);
			sheet.setColumnView(9, 20);
			sheet.setColumnView(10, 20);
			sheet.addCell(new Label(0, 2, "ID", wcf_title));
			sheet.addCell(new Label(1, 2, "项目组", wcf_title));
			sheet.addCell(new Label(2, 2, "员工编号", wcf_title));
			sheet.addCell(new Label(3, 2, "姓名", wcf_title));
			sheet.addCell(new Label(4, 2, "年龄", wcf_title));
			sheet.addCell(new Label(5, 2, "联系方式", wcf_title));
			sheet.addCell(new Label(6, 2, "累计行走步数", wcf_title));
			sheet.addCell(new Label(7, 2, "累计里程", wcf_title));
			sheet.addCell(new Label(8, 2, "是否参保", wcf_title));
			sheet.addCell(new Label(9, 2, "缺勤记录", wcf_title));
			sheet.addCell(new Label(10, 2, "迟到次数", wcf_title));
			// 调用BI
			BIStats statsBI = new BIStats(null, m_bs);
			// 返回数据
			JSONArray retlist = new JSONArray();
			ArrayList<UserReportPojo> list = statsBI.getUserReportList(paras);
			if (days == 0 && paras.containsKey("days")) {
				days = paras.getLong("days");
			}
			int i = 3;
			for (UserReportPojo onePojo : list) {
				int age = 0;
				if (!onePojo.getUser().getBirthday().equals("")) {
					age = URLlImplBase.getAge(onePojo.getUser().getBirthday());
				}
				String org = "";
				if (onePojo.getUser().getOrg() != null) {
					org = onePojo.getUser().getOrg().getAllName()
							.replaceAll(",", "-");
				}
				sheet.setRowView(i, 600);
				sheet.addCell(new Label(0, i,
						String.valueOf(retlist.size() + 1), wcf_center));
				sheet.addCell(new Label(1, i, org, wcf_center));
				sheet.addCell(new Label(2, i, onePojo.getUser().getId(),
						wcf_center));
				sheet.addCell(new Label(3, i, onePojo.getUser().getName(),
						wcf_center));
				sheet.addCell(new Label(4, i, String.valueOf(age), wcf_center));
				sheet.addCell(new Label(5, i, onePojo.getUser().getmPhone(),
						wcf_center));
				sheet.addCell(new Label(6, i,
						String.valueOf(onePojo.getStep()), wcf_center));
				sheet.addCell(new Label(7, i, URLlImplBase.AllPrinceDiv(
						onePojo.getDistance(), 1000), wcf_center));
				sheet.addCell(new Label(8, i, UserPojo.SBFLG_NAME[onePojo
						.getUser().getSbFlg()], wcf_center));
				sheet.addCell(new Label(9, i, String.valueOf(days
						- onePojo.getAbs()), wcf_center));
				sheet.addCell(new Label(10, i,
						String.valueOf(onePojo.getLate()), wcf_center));
				i++;
			}
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			if (workbook != null) {
				workbook.write();
				workbook.close();
			}
			if (!filepath.equals("")) {
				retJSON.put("path", "/files/report/" + fileName);
				m_bs.setPrivateValue(Const.BS_DOWNLOAD_DELETE_FLAG, "true");
				m_bs.setPrivateValue(Const.BS_DOWNLOAD_FILE, filepath + "/"
						+ fileName);
				File file = new File(filepath + fileName);
				file.setExecutable(true, false);// 设置可执行权限
				file.setReadable(true, false);// 设置可读权限
				file.setWritable(true, false);// 设置可写权限
			}
		}
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;

	}

	/**
	 * <p>
	 * 方法名称: do_exportUserReportList
	 * </p>
	 * <p>
	 * 方法功能描述:导出用户报表统计。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public BSObject do_exportTruckReportList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_keyword");
		String group = m_bs.getPrivateMap().get("pg_group");
		String truck = m_bs.getPrivateMap().get("pg_truck");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String filepath = BSCommon.getConfigValue("upload_path") + "/report/";
		String fileName = "truck_" + m_bs.getDateEx().getSeqDate()
				+ "report.xls";
		long days = 0;
		JSONObject paras = new JSONObject();
		if (sText != null) {
			paras.put("key", sText);
		}
		if (sDate == null || sDate.equals("")) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -1);
			sDate = m_bs.getDateEx().getCalendarToStringAll(date);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getCalendarToStringAll(
					Calendar.getInstance());
		}
		if (sDate.length() <= 10) {
			sDate = sDate + " 00:00:00";
		}
		if (eDate.length() <= 10) {
			eDate = eDate + " 23:59:59";
		}
		paras.put("date", sDate + "," + eDate);
		days = m_bs.getDateEx().getDateCount(sDate, eDate);
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
		WritableWorkbook workbook = null;
		try {
			(new File(filepath)).mkdirs();
			workbook = Workbook.createWorkbook(new File(filepath + "/"
					+ fileName));
			// 定义xls式样
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 12);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 14,
					WritableFont.BOLD);
			WritableFont BoldFont2 = new WritableFont(WritableFont.ARIAL, 13,
					WritableFont.BOLD);
			WritableCellFormat wcf_title = new WritableCellFormat(BoldFont);
			wcf_title.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_title.setAlignment(Alignment.CENTRE);
			wcf_title.setWrap(true);

			WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_center.setAlignment(Alignment.CENTRE);
			wcf_center.setWrap(true);

			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_left.setAlignment(Alignment.LEFT);
			wcf_left.setWrap(true);

			WritableCellFormat wcf_boldFont = new WritableCellFormat(BoldFont2);
			wcf_boldFont.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_boldFont.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_boldFont.setAlignment(Alignment.CENTRE);
			wcf_boldFont.setWrap(true);
			//
			WritableSheet sheet = null;
			sheet = workbook.createSheet("车辆报表", 0);
			// 列行合并
			sheet.mergeCells(0, 0, 11, 0);// x,y
			sheet.mergeCells(0, 1, 11, 1);
			// 设置表头
			sheet.setRowView(0, 700);
			sheet.setRowView(1, 600);
			sheet.setRowView(2, 600);
			sheet.addCell(new Label(0, 0, "车辆报表", wcf_title));
			sheet.addCell(new Label(0, 1, "时间从：" + sDate + " 到：" + eDate,
					wcf_title));
			sheet.setColumnView(0, 10);
			sheet.setColumnView(1, 40);
			sheet.setColumnView(2, 30);
			sheet.setColumnView(3, 18);
			sheet.setColumnView(4, 15);
			sheet.setColumnView(5, 30);
			sheet.setColumnView(6, 25);
			sheet.setColumnView(7, 20);
			sheet.setColumnView(8, 20);
			sheet.setColumnView(9, 20);
			sheet.setColumnView(10, 20);
			sheet.setColumnView(11, 25);
			sheet.addCell(new Label(0, 2, "ID", wcf_boldFont));
			sheet.addCell(new Label(1, 2, "项目组", wcf_boldFont));
			sheet.addCell(new Label(2, 2, "设备类型", wcf_boldFont));
			sheet.addCell(new Label(3, 2, "资产编号", wcf_boldFont));
			sheet.addCell(new Label(4, 2, "车牌号", wcf_boldFont));
			sheet.addCell(new Label(5, 2, "车辆品牌", wcf_boldFont));
			sheet.addCell(new Label(6, 2, "负责人", wcf_boldFont));
			sheet.addCell(new Label(7, 2, "联系方式", wcf_boldFont));
			sheet.addCell(new Label(8, 2, "油耗统计(L)", wcf_boldFont));
			sheet.addCell(new Label(9, 2, "工作里程(km)", wcf_boldFont));
			sheet.addCell(new Label(10, 2, "工作时长(H)", wcf_boldFont));
			sheet.addCell(new Label(11, 2, "设备使用率(%)", wcf_boldFont));
			// 调用BI
			BIStats statsBI = new BIStats(null, m_bs);
			// 返回数据
			JSONArray retlist = new JSONArray();
			ArrayList<TruckReportPojo> list = statsBI.getTruckReportList(paras);
			if (days == 0 && paras.containsKey("days")) {
				days = paras.getLong("days");
			}
			int i = 3;
			for (TruckReportPojo onePojo : list) {
				sheet.setRowView(i, 600);
				String org = "";
				if (onePojo.getTruck().getOrg() != null) {
					org = onePojo.getTruck().getOrg().getAllName()
							.replaceAll(",", "-");
				}
				String workTime = URLlImplBase.AllPrinceDiv(
						onePojo.getWorkTimes(), 3600);
				sheet.addCell(new Label(0, i, String.valueOf(i - 2), wcf_center));
				sheet.addCell(new Label(1, i, org, wcf_center));
				sheet.addCell(new Label(2, i, onePojo.getTruck().getDefine()
						.getName(), wcf_center));
				sheet.addCell(new Label(3, i, onePojo.getTruck().getNo(),
						wcf_center));
				sheet.addCell(new Label(4, i, onePojo.getTruck().getPlateNum(),
						wcf_center));
				sheet.addCell(new Label(5, i, onePojo.getTruck().getDefine()
						.getBrand(), wcf_center));
				sheet.addCell(new Label(6, i, onePojo.getTruck().getMangUser()
						.getName(), wcf_center));
				sheet.addCell(new Label(7, i, onePojo.getTruck().getMangUser()
						.getmPhone(), wcf_center));
				sheet.addCell(new Label(8, i, String.valueOf(onePojo.getOil()),
						wcf_center));
				sheet.addCell(new Label(9, i, URLlImplBase.AllPrinceDiv(
						onePojo.getDistance(), 1000), wcf_center));
				sheet.addCell(new Label(10, i, workTime, wcf_center));
				sheet.addCell(new Label(11, i,
						URLlImplBase.AllPrinceMul(
								URLlImplBase.AllPrinceDiv(workTime, days
										* onePojo.getTruck().getDefine()
												.getWorkTime()), 100)
								+ "%", wcf_center));
				i++;
			}
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			if (workbook != null) {
				workbook.write();
				workbook.close();
			}
			if (!filepath.equals("")) {
				retJSON.put("path", "/files/report/" + fileName);
				m_bs.setPrivateValue(Const.BS_DOWNLOAD_DELETE_FLAG, "true");
				m_bs.setPrivateValue(Const.BS_DOWNLOAD_FILE, filepath + "/"
						+ fileName);
				File file = new File(filepath + fileName);
				file.setExecutable(true, false);// 设置可执行权限
				file.setReadable(true, false);// 设置可读权限
				file.setWritable(true, false);// 设置可写权限
			}
		}
		retJSON.put("code", 0);
		retJSON.put("msg", URLlImplBase.ErrorMap.get(retJSON.getInt("code")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;

	}

	// 验证码效验
	private int checkCode(BSObject m_bs) throws Exception {
		int ret = 6;
		if (m_bs.getPrivateMap().get("usertype") != null
				&& (m_bs.getPrivateMap().get("usertype")).equals("0")) {
			ret = 0;
		} else {
			BIRedis redisBI = new BIRedis();
			String inCode = (String) m_bs.getPrivateMap().get("usercode");
			String sysCode = redisBI.getStringData(
					"code_" + m_bs.getSessionID(), 1);
			// 验证码效验
			if (inCode != null && sysCode != null && sysCode.equals(inCode)) {
				ret = 0;
			}
			redisBI.delData("code_" + m_bs.getSessionID(), 1);
		}
		return ret;
	}

	private JSONArray _getChildOrgList(BIUser userBI, JSONObject paras)
			throws Exception {
		JSONArray orgs = new JSONArray();
		ArrayList<OrgPojo> list = userBI.getOrgList(paras, 0, 100);
		for (OrgPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("desc", onePojo.getDesc());
			oneObj.put("allname", onePojo.getAllName().replaceAll(",", "-"));
			oneObj.put("allid", onePojo.getAllOrgId());
			oneObj.put("pid", onePojo.getPorgId());
			oneObj.put("pname", onePojo.getPorgName());
			oneObj.put("cnum", onePojo.getSubOrgNum());
			oneObj.put("snum", onePojo.getUserNum());
			if (onePojo.getSubOrgNum() > 0) {
				paras.put("root", "");
				paras.put("sub", onePojo.getId());
				oneObj.put("children", this._getChildOrgList(userBI, paras));
			}
			orgs.add(oneObj);
		}
		return orgs;
	}
}
