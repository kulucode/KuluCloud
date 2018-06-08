package tt.kulu.out.call;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.dbclass.StaticDBMang;
import tt.kulu.bi.dic.pojo.DicPojo;
import tt.kulu.bi.report.dbclass.ReportDBMang;
import tt.kulu.bi.report.pojo.TruckReportPojo;
import tt.kulu.bi.report.pojo.UserReportPojo;
import tt.kulu.bi.stats.dbclass.StatsDBMang;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BIArea
 * </p>
 * <p>
 * 功能描述: 地域接口类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2011-1-25
 * </p>
 */
public class BIStats extends BSDBBase {
	public BIStats(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	public BIStats(BSObject m_bs) throws Exception {
		super(null, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getDicList
	 * </p>
	 * <p>
	 * 方法功能描述:得到数据字典列表，带条件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchSysBaseStats() throws Exception {
		JSONObject retObj = new JSONObject();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			retObj = this.searchSysBaseStats(sqlHelper);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return retObj;
	}

	/**
	 * <p>
	 * 方法名称: searchSysBaseStats
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchSysBaseStats(SqlExecute sqlHelper) throws Exception {
		StatsDBMang statsDB = new StatsDBMang(sqlHelper, m_bs);
		return statsDB.searchSysBaseStats();
	}

	/**
	 * <p>
	 * 方法名称: getDicList
	 * </p>
	 * <p>
	 * 方法功能描述:得到数据字典列表，带条件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchMyBaseStats(String userInstId, String groupId)
			throws Exception {
		JSONObject retObj = new JSONObject();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			retObj = this.searchMyBaseStats(sqlHelper, userInstId, groupId);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return retObj;
	}

	/**
	 * <p>
	 * 方法名称: searchMyBaseStats
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchMyBaseStats(SqlExecute sqlHelper,
			String userInstId, String groupId) throws Exception {
		StatsDBMang statsDB = new StatsDBMang(sqlHelper, m_bs);
		return statsDB.searchMyBaseStats(userInstId, groupId);
	}

	/**
	 * <p>
	 * 方法名称: getTruckReportList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆定义列表。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输入参数描述: long f：输入的开始位置。
	 * </p>
	 * <p>
	 * 输入参数描述: long t：输入的结束位置。
	 * </p>
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<TruckReportPojo> getTruckReportList(JSONObject paras,
			long f, long t) throws Exception {
		ArrayList<TruckReportPojo> list = new ArrayList<TruckReportPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getTruckReportList(sqlHelper, paras, f, t);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getTruckReportList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆定义列表。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输入参数描述: long f：输入的开始位置。
	 * </p>
	 * <p>
	 * 输入参数描述: long t：输入的结束位置。
	 * </p>
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<TruckReportPojo> getTruckReportList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vWorkList = new ArrayList<Object>();
		List<Object> vUserList = new ArrayList<Object>();
		String orderBy = " t.TRUCK_NO";
		JSONObject where = new JSONObject();
		String truckWhere = "";
		String workWhere = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					truckWhere += " and (t.TRUCK_NAME like ? OR t.PLATE_NUM like ?)";
					vUserList.add("%" + v + "%");
					vUserList.add("%" + v + "%");
				}
				if (key.equals("date")) {
					// 日期
					String vs[] = v.split(",");
					if (vs.length > 1 && !vs[0].equals("") && !vs[1].equals("")) {
						workWhere += " and t1.log_date BETWEEN ? AND ?";
						vWorkList.add(Timestamp.valueOf(vs[0].substring(0, 10)
								+ " 00:00:00"));
						vWorkList.add(Timestamp.valueOf(vs[1].substring(0, 10)
								+ " 23:59:59"));
					}
				}
				if (key.equals("group")) {
					// 机构
					truckWhere += " and t.ORG_ID in "
							+ URLlImplBase.LOGIN_GROUP_WHERE;
					vUserList.add("%," + v + "%");
				}
				if (key.equals("login")) {
					// 登录用户
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += (whereEx.equals("") ? "" : " or ")
									+ "t.ORG_ID in "
									+ URLlImplBase.LOGIN_GROUP_WHERE;
							vUserList.add("%," + oneV + "%");
						}
					}
					if (!whereEx.equals("")) {
						truckWhere += " and (" + whereEx + ")";
					}
				}
				if (key.equals("truck")) {
					// 车辆ID
					truckWhere += " and t.TRUCK_ID=?";
					vUserList.add(v);
				}
				if (key.equals("truck")) {
					// 车辆ID
					truckWhere += " and t.TRUCK_STATE=?";
					vUserList.add(Integer.parseInt(v));
				}
			}
		}
		where.put("work", workWhere);
		where.put("truck", truckWhere);
		vWorkList.addAll(vUserList);
		ReportDBMang reportDB = new ReportDBMang(sqlHelper, m_bs);
		if (!paras.containsKey("date")) {
			// 得到数据集合早晚时间
			JSONObject dp = reportDB.getTruckReportDate(where, vWorkList);
			if (dp.containsKey("mindate") && dp.containsKey("maxdate")) {
				paras.put(
						"days",
						m_bs.getDateEx().getDateCount(dp.getString("mindate"),
								dp.getString("maxdate")));
			}
		}
		paras.put("max", reportDB.getTruckReportCount(where, vWorkList));
		return reportDB.getTruckReportList(where, orderBy, vWorkList, f, t);
	}

	/**
	 * <p>
	 * 方法名称: getUserReportList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆定义列表。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输入参数描述: long f：输入的开始位置。
	 * </p>
	 * <p>
	 * 输入参数描述: long t：输入的结束位置。
	 * </p>
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<UserReportPojo> getUserReportList(JSONObject paras,
			long f, long t) throws Exception {
		ArrayList<UserReportPojo> list = new ArrayList<UserReportPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getUserReportList(sqlHelper, paras, f, t);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getTruckReportList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆定义列表。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输入参数描述: long f：输入的开始位置。
	 * </p>
	 * <p>
	 * 输入参数描述: long t：输入的结束位置。
	 * </p>
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<UserReportPojo> getUserReportList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vWorkList = new ArrayList<Object>();
		List<Object> vUserList = new ArrayList<Object>();
		String orderBy = " t.USER_ID";
		JSONObject where = new JSONObject();
		String userWhere = "";
		String workWhere = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					userWhere += " and (t.USER_ID=? OR t.USER_NAME like ? OR t.MPHONE like ?)";
					vUserList.add(v);
					vUserList.add("%" + v + "%");
					vUserList.add("%" + v + "%");
				}
				if (key.equals("date")) {
					// 日期
					String vs[] = v.split(",");
					if (vs.length > 1 && !vs[0].equals("") && !vs[1].equals("")) {
						workWhere += " and t1.log_date BETWEEN ? AND ?";
						vWorkList.add(Timestamp.valueOf(vs[0]));
						vWorkList.add(Timestamp.valueOf(vs[1]));
					}
				}
				if (key.equals("group")) {
					// 机构
					userWhere += " and t.ORG_ID in "
							+ URLlImplBase.LOGIN_GROUP_WHERE;
					vUserList.add("%," + v + "%");
				}
				if (key.equals("login")) {
					// 登录用户
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += (whereEx.equals("") ? "" : " or ")
									+ "t.ORG_ID in "
									+ URLlImplBase.LOGIN_GROUP_WHERE;
							vUserList.add("%," + oneV + "%");
						}
					}
					if (!whereEx.equals("")) {
						userWhere += " and (" + whereEx + ")";
					}
				}
				if (key.equals("user")) {
					//
					userWhere += " and t.USER_INSTID=?";
					vUserList.add(v);
				}
				if (key.equals("role")) {
					// 角色
					userWhere += " and t.USER_INSTID in (select v.USER_INSTID from T_ROLE_USER_R v where v.ROLE_ID in (";
					String[] vs = v.split(",");
					String inW = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							inW += ",?";
							vUserList.add(oneV);
						}
					}
					userWhere += inW.substring(1) + "))";
				}
			}
		}
		where.put("work", workWhere);
		where.put("user", userWhere);
		vWorkList.addAll(vUserList);
		ReportDBMang reportDB = new ReportDBMang(sqlHelper, m_bs);
		if (!paras.containsKey("date")) {
			// 得到数据集合早晚时间
			JSONObject dp = reportDB.getUserReportDate(where, vWorkList);
			if (dp.containsKey("mindate") && dp.containsKey("maxdate")) {
				paras.put(
						"days",
						m_bs.getDateEx().getDateCount(dp.getString("mindate"),
								dp.getString("maxdate")));
			}
		}
		paras.put("max", reportDB.getUserReportCount(where, vWorkList));
		return reportDB.getUserReportList(where, orderBy, vWorkList, f, t);
	}

	/**
	 * <p>
	 * 方法名称: searchFaultStats
	 * </p>
	 * <p>
	 * 方法功能描述:得到数据字典列表，带条件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchFaultStats(JSONObject paras) throws Exception {
		JSONObject retObj = new JSONObject();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			retObj = this.searchFaultStats(sqlHelper, paras);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return retObj;
	}

	/**
	 * <p>
	 * 方法名称: searchSysBaseStats
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchFaultStats(SqlExecute sqlHelper, JSONObject paras)
			throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.TRUCK_NO";
		String where = new String();
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("fault")) {
					where += " and t.FAULT_ID in (";
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += ",?";
							vList.add(oneV);
						}
					}
					where += ((whereEx.equals("") ? "" : whereEx.substring(1)) + ")");
				}
				if (key.equals("type")) {
					where += " and t.FR_TYPE in (";
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += ",?";
							vList.add(oneV);
						}
					}
					where += ((whereEx.equals("") ? "" : whereEx.substring(1)) + ")");
				}
				if (key.equals("from")) {
					where += " and t.FR_FROM in (";
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += ",?";
							vList.add(oneV);
						}
					}
					where += ((whereEx.equals("") ? "" : whereEx.substring(1)) + ")");
				}
				if (key.equals("truck")) {
					// 根
					where += " and t.FR_TRUCK=?";
					vList.add(v);
				}
				if (key.equals("user")) {
					// 根
					where += " and t.FR_USER=?";
					vList.add(v);
				}
				if (key.equals("login")) {
					// 关键字
					where += " and t.FR_USER in (select t2.USER_INSTID from T_ORG_USER_R t2 where t2.ORG_ID in (select v.ORG_ID from T_ORG_USER_R v where v.USER_INSTID=?))";
					vList.add(v);
				}
				if (key.equals("date")) {
					// 日期
					String vs[] = v.split(",");
					if (vs.length > 1 && !vs[0].equals("") && !vs[1].equals("")) {
						where += " and t1.falut_date BETWEEN ? AND ?";
						vList.add(Timestamp.valueOf(vs[0].substring(0, 10)
								+ " 00:00:00"));
						vList.add(Timestamp.valueOf(vs[1].substring(0, 10)
								+ " 23:59:59"));
					}
				}
			}
		}
		StatsDBMang statsDB = new StatsDBMang(sqlHelper, m_bs);
		return statsDB.searchFaultStats(where, vList);
	}

	/**
	 * <p>
	 * 方法名称: searchTruckOilStats
	 * </p>
	 * <p>
	 * 方法功能描述:得到数据字典列表，带条件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchTruckOilStats(JSONObject paras) throws Exception {
		JSONObject retObj = new JSONObject();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			retObj = this.searchTruckOilStats(sqlHelper, paras);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return retObj;
	}

	/**
	 * <p>
	 * 方法名称: searchSysBaseStats
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchTruckOilStats(SqlExecute sqlHelper, JSONObject paras)
			throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String where = new String();
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("truck")) {
					where += " and t1.eqp_truck=?";
					vList.add(v);
				}
				if (key.equals("date")) {
					// 日期
					String vs[] = v.split(",");
					if (vs.length > 1 && !vs[0].equals("") && !vs[1].equals("")) {
						where += " and t.s_cdate BETWEEN ? AND ?";
						vList.add(Timestamp.valueOf(vs[0].substring(0, 10)
								+ " 00:00:00"));
						vList.add(Timestamp.valueOf(vs[1].substring(0, 10)
								+ " 23:59:59"));
					}
				}
				if (key.equals("group")) {
					// 机构
					where += " and t1.org_id in "
							+ URLlImplBase.LOGIN_GROUP_WHERE;
					vList.add("%," + v + "%");
				}
				if (key.equals("login")) {
					// 登录用户
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += (whereEx.equals("") ? "" : " or ")
									+ "t1.org_id in "
									+ URLlImplBase.LOGIN_GROUP_WHERE;
							vList.add("%," + oneV + "%");
						}
					}
					if (!whereEx.equals("")) {
						where += " and (" + whereEx + ")";
					}
				}
			}
		}
		StatsDBMang statsDB = new StatsDBMang(sqlHelper, m_bs);
		return statsDB.searchTruckOilStats(where, vList);
	}

	/**
	 * <p>
	 * 方法名称: searchFaultStats
	 * </p>
	 * <p>
	 * 方法功能描述:得到数据字典列表，带条件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject getTruckWorkTimeByWhere(JSONObject paras)
			throws Exception {
		JSONObject retObj = new JSONObject();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			retObj = this.getTruckWorkTimeByWhere(sqlHelper, paras);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return retObj;
	}

	/**
	 * <p>
	 * 方法名称: getTruckWorkTimeByWhere
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject getTruckWorkTimeByWhere(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.TRUCK_NO";
		String where = new String();
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("truck")) {
					// 根
					where += " and t.TRUCK_ID=?";
					vList.add(v);
				}
				if (key.equals("login")) {
					// 关键字
					where += " and t1.ORG_ID in (select v.ORG_ID from T_ORG_USER_R v where v.USER_INSTID=?)";
					vList.add(v);
				}
				if (key.equals("date")) {
					// 日期
					String vs[] = v.split(",");
					if (vs.length > 1 && !vs[0].equals("") && !vs[1].equals("")) {
						where += " and t.LOG_DATE BETWEEN ? AND ?";
						vList.add(Timestamp.valueOf(vs[0].substring(0, 10)
								+ " 00:00:00"));
						vList.add(Timestamp.valueOf(vs[1].substring(0, 10)
								+ " 23:59:59"));
					}
				}
			}
		}
		StatsDBMang statsDB = new StatsDBMang(sqlHelper, m_bs);
		return statsDB.getTruckWorkTimeByWhere(where, vList);
	}

	/**
	 * <p>
	 * 方法名称: searchTruckStatsScore
	 * </p>
	 * <p>
	 * 方法功能描述:得到数据字典列表，带条件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int searchTruckStatsScore(JSONObject paras) throws Exception {
		int score = 100;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			score = this.searchTruckStatsScore(sqlHelper, paras);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return score;
	}

	/**
	 * <p>
	 * 方法名称: searchTruckStatsScore
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int searchTruckStatsScore(SqlExecute sqlHelper, JSONObject paras)
			throws Exception {
		int score = 100;
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		List<Object> vList2 = new ArrayList<Object>();
		String where = new String();
		String where2 = new String();
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("truck")) {
					where += " and t.FR_TRUCK=?";
					vList.add(v);
					where2 += " and t.TRUCK_ID=?";
					vList2.add(v);
				}
				if (key.equals("date")) {
					// 日期
					String vs[] = v.split(",");
					if (vs.length > 1 && !vs[0].equals("") && !vs[1].equals("")) {
						where += " and t.s_cdate BETWEEN ? AND ?";
						where2 += " and t.PLAN_DATE BETWEEN ? AND ?";
						vList.add(Timestamp.valueOf(vs[0].substring(0, 10)
								+ " 00:00:00"));
						vList.add(Timestamp.valueOf(vs[1].substring(0, 10)
								+ " 23:59:59"));
						vList2.add(Timestamp.valueOf(vs[0].substring(0, 10)
								+ " 00:00:00"));
						vList2.add(Timestamp.valueOf(vs[1].substring(0, 10)
								+ " 23:59:59"));
					}
				}
				if (key.equals("state")) {
					// 状态
					where += " and t.OP_STATE=?";
					vList.add(Integer.parseInt(v));
					where2 += " and t.PLAN_STATE=?";
					vList2.add(Integer.parseInt(v));
				}
				if (key.equals("outtime")) {
					where2 += " and ?>t.PLAN_DATE";
					vList2.add(Timestamp.valueOf(this.bsDate.getThisDate(0, 0)));
				}
			}
		}
		where += " and t.FR_TYPE in ('1AA40EFE45EE2BA75E1C4ADE20F796C2','12D24119E79CFA25144A19EDDEA53A94')";
		StatsDBMang statsDB = new StatsDBMang(sqlHelper, m_bs);
		int count = statsDB.searchFaultScore(where, vList);
		int minFP = Integer.parseInt((new BIDic(null)
				.getDicItemByRedis("SCORE_ITEM_MIN").getValue()));
		int oneFP = Integer.parseInt((new BIDic(null)
				.getDicItemByRedis("SCORE_ITEM_FAULT").getValue()));
		score -= oneFP * count;
		if (score > minFP) {
			// 保养统计
			score -= statsDB.searchInspectScore(where2, vList2);
		}
		if (score < minFP) {
			score = minFP;
		}
		return score;
	}

	/**
	 * <p>
	 * 方法名称: searchUserStatsScore
	 * </p>
	 * <p>
	 * 方法功能描述:得到数据字典列表，带条件
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int searchUserStatsScore(JSONObject paras) throws Exception {
		int score = 100;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			score = this.searchUserStatsScore(sqlHelper, paras);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return score;
	}

	/**
	 * <p>
	 * 方法名称: searchUserStatsScore
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: 查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int searchUserStatsScore(SqlExecute sqlHelper, JSONObject paras)
			throws Exception {
		int score = 100;
		int minFP = Integer.parseInt((new BIDic(null)
				.getDicItemByRedis("SCORE_ITEM_MIN").getValue()));
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String where = new String();
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("user")) {
					where += " and t.FR_USER=?";
					vList.add(v);
				}
				if (key.equals("date")) {
					// 日期
					String vs[] = v.split(",");
					if (vs.length > 1 && !vs[0].equals("") && !vs[1].equals("")) {
						where += " and t.s_cdate BETWEEN ? AND ?";
						vList.add(Timestamp.valueOf(vs[0].substring(0, 10)
								+ " 00:00:00"));
						vList.add(Timestamp.valueOf(vs[1].substring(0, 10)
								+ " 23:59:59"));
					}
				}
				if (key.equals("state")) {
					// 状态
					where += " and t.OP_STATE=?";
					vList.add(Integer.parseInt(v));
				}
			}
		}
		where += " and t.FR_TYPE in ('2B27A3287D25F3EA18C8FDAFA1EEB25F','A93012F52325FBCA228CB09A89FF408E')";
		StatsDBMang statsDB = new StatsDBMang(sqlHelper, m_bs);
		int count = statsDB.searchFaultScore(where, vList);
		int oneFP = Integer.parseInt((new BIDic(null)
				.getDicItemByRedis("SCORE_ITEM_FAULT").getValue()));
		score -= oneFP * count;

		if (score < minFP) {
			score = minFP;
		}
		return score;
	}
}
