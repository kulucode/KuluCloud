package tt.kulu.bi.stats.dbclass;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.out.call.BIDic;

/**
 * <p>
 * 标题: StatsDBMang
 * </p>
 * <p>
 * 功能描述: 统计数据库操作类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2011-05-05
 * </p>
 */
public class StatsDBMang extends BSDBBase {

	public StatsDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: searchBaseStats
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品定义列表。
	 * </p>
	 * <p>
	 * 输入参数描述:
	 * </p>
	 * <p>
	 * 输出参数描述:
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchSysBaseStats() throws Exception {
		JSONObject retObj = new JSONObject();
		ResultSet rs = this.sqlHelper
				.queryBySql("select t.USER_COUNT,t.TRUCK_COUNT,t.FAULT_COUNT,t.ORDER_COUNT from V_BASESTATS t");
		if (rs != null && rs.next()) {
			retObj.put("user_count", rs.getString("USER_COUNT"));
			retObj.put("truck_count", rs.getString("TRUCK_COUNT"));
			retObj.put("fault_count", rs.getString("FAULT_COUNT"));
			retObj.put("order_count", rs.getString("ORDER_COUNT"));
			rs.close();
		}
		return retObj;
	}

	/**
	 * <p>
	 * 方法名称: searchMyBaseStats
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品定义列表。
	 * </p>
	 * <p>
	 * 输入参数描述:
	 * </p>
	 * <p>
	 * 输出参数描述:
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchMyBaseStats(String userInstId, String groupId)
			throws Exception {
		JSONObject retObj = new JSONObject();
		List<Object> vList = new ArrayList<Object>();
		String groupW = "";
		// 员工
		if (groupId != null && !groupId.equals("")) {
			String[] vs = groupId.split(",");
			String whereEx = "";
			for (String oneV : vs) {
				if (!oneV.equals("")) {
					whereEx += (whereEx.equals("") ? "" : " or ")
							+ "t.ORG_ID in " + URLlImplBase.LOGIN_GROUP_WHERE;
					vList.add("%," + oneV + "%");
				}
			}
			if (!whereEx.equals("")) {
				groupW += " and (" + whereEx + ")";
			}
		}
		String sql = "select count(t.USER_INSTID) as DATA_COUNT from T_ORG_USER_R t where t.ORG_ID is not null "
				+ groupW
				+ " and t.USER_INSTID in (select v.USER_INSTID from T_USER v where v.USER_STATE=1) and t.USER_INSTID in (select r.USER_INSTID from T_ROLE_USER_R r where r.ROLE_ID in('OUTDOORS_STAFF'))";
		ResultSet rs = this.sqlHelper.queryBySql(sql, vList);
		retObj.put("user_count", 0);
		if (rs != null && rs.next()) {
			retObj.put("user_count", rs.getLong("DATA_COUNT"));
			rs.close();
		}
		// 车辆
		sql = "select count(t.TRUCK_ID) as DATA_COUNT from T_TRUCK_INST t where t.ORG_ID is not null "
				+ groupW + " and t.TRUCK_STATE not in (2,4)";
		rs = this.sqlHelper.queryBySql(sql, vList);
		retObj.put("truck_count", 0);
		if (rs != null && rs.next()) {
			retObj.put("truck_count", rs.getLong("DATA_COUNT"));
			rs.close();
		}
		// 故障报警
		vList.clear();
		groupW = "";
		String groupW2 = "";
		if (groupId != null && !groupId.equals("")) {
			String[] vs = groupId.split(",");
			String whereEx = "";
			for (String oneV : vs) {
				if (!oneV.equals("")) {
					whereEx += (whereEx.equals("") ? "" : " or ")
							+ " t1.ORG_ID in " + URLlImplBase.LOGIN_GROUP_WHERE;
					vList.add("%," + oneV + "%");
				}
			}
			if (!whereEx.equals("")) {
				groupW += " and (" + whereEx + ")";
			}
			whereEx = "";
			for (String oneV : vs) {
				if (!oneV.equals("")) {
					whereEx += (whereEx.equals("") ? "" : " or ")
							+ " t2.ORG_ID in " + URLlImplBase.LOGIN_GROUP_WHERE;
					vList.add("%," + oneV + "%");
				}
			}
			if (!whereEx.equals("")) {
				groupW2 += " and (" + whereEx + ")";
			}
		}
		sql = "select count(t.FR_REPORTID) as DATA_COUNT from T_FAULTREPORT t where (t.FR_TRUCK in (select t1.TRUCK_ID from T_TRUCK_INST t1 where t1.ORG_ID is not null "
				+ groupW
				+ " OR t.FR_USER in (select t2.USER_INSTID from T_ORG_USER_R t2 where t2.ORG_ID is not null "
				+ groupW2 + ")))";
		rs = this.sqlHelper.queryBySql(sql, vList);
		retObj.put("fault_count", 0);
		if (rs != null && rs.next()) {
			retObj.put("fault_count", rs.getLong("DATA_COUNT"));
			rs.close();
		}
		// 未完成工单
		vList.clear();
		groupW = "";
		if (groupId != null && !groupId.equals("")) {
			String[] vs = groupId.split(",");
			String whereEx = "";
			for (String oneV : vs) {
				if (!oneV.equals("")) {
					whereEx += (whereEx.equals("") ? "" : " or ")
							+ "t1.ORG_ID in " + URLlImplBase.LOGIN_GROUP_WHERE;
					vList.add("%," + oneV + "%");
				}
			}
			if (!whereEx.equals("")) {
				groupW += " and (" + whereEx + ")";
			}
		}
		sql = "select count(t.PLAN_ID) as DATA_COUNT from T_INSPECT_PLAN t,T_TRUCK_INST t1 where t.TRUCK_ID=t1.TRUCK_ID and t.PLAN_STATE <> 1 and current_timestamp > t.PLAN_DATE "
				+ groupW;
		rs = this.sqlHelper.queryBySql(sql, vList);
		retObj.put("order_count", 0);
		if (rs != null && rs.next()) {
			retObj.put("order_count", rs.getLong("DATA_COUNT"));
			rs.close();
		}

		return retObj;
	}

	/**
	 * <p>
	 * 方法名称: searchFaultStats
	 * </p>
	 * <p>
	 * 方法功能描述: 得到报警信息的统计。
	 * </p>
	 * <p>
	 * 输入参数描述:
	 * </p>
	 * <p>
	 * 输出参数描述:
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchFaultStats(String where, List<Object> vList)
			throws Exception {
		JSONObject retList = new JSONObject();
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("distinct t.fault_id");
		strSQL.append("," + SqlExecute.getDateToCharSql("t1.falut_date")
				+ " as falut_date");
		strSQL.append(",t1.fault_count");
		strSQL.append(" from V_FAULT_STAT t1,T_FAULTREPORT t");
		strSQL.append(" where t.fault_id=t1.fault_id ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		strSQL.append(" order by falut_date,t.fault_id");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			DicItemPojo oneDicItem = null;
			BIDic dicBI = new BIDic(sqlHelper, m_bs);
			String tempDay = "";
			JSONObject fList = new JSONObject();
			JSONObject oneDay = new JSONObject();
			JSONObject oneFault = new JSONObject();
			while (rs.next()) {
				// 日期循环
				if (!tempDay
						.equals(rs.getString("falut_date").substring(0, 10))) {
					if (fList.size() > 0) {
						oneDay.put("faults", fList);
						retList.put(tempDay, oneDay);
						fList.clear();
						oneFault = new JSONObject();
						oneDay = new JSONObject();
					}
					tempDay = rs.getString("falut_date").substring(0, 10);
				}
				oneDay.put("date", tempDay);
				oneDicItem = dicBI.getDicItemByRedis(rs.getString("fault_id"));
				oneFault.put("faultid", rs.getString("fault_id"));
				if (oneDicItem != null) {
					oneFault.put("fault", oneDicItem.getName());
				}
				oneFault.put("count", rs.getLong("fault_count"));
				fList.put(rs.getString("fault_id"), oneFault);
			}
			rs.close();
			if (oneFault != null && fList.size() > 0) {
				oneDay.put("faults", fList);
				retList.put(tempDay, oneDay);
			}
		}
		return retList;
	}

	/**
	 * <p>
	 * 方法名称: searchFaultScore
	 * </p>
	 * <p>
	 * 方法功能描述: 得到报警信息的统计。
	 * </p>
	 * <p>
	 * 输入参数描述:
	 * </p>
	 * <p>
	 * 输出参数描述:
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int searchFaultScore(String where, List<Object> vList)
			throws Exception {
		int score = 100;
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append(" count(t.fault_id) as OBJ_COUNT");
		strSQL.append(" from T_FAULTREPORT t");
		strSQL.append(" where t.fault_id is not null and t.OP_STATE<>1 ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				score = rs.getInt("OBJ_COUNT");
			}
			rs.close();
		}
		return score;
	}

	/**
	 * <p>
	 * 方法名称: searchInspectScore
	 * </p>
	 * <p>
	 * 方法功能描述: 得到报警信息的统计。
	 * </p>
	 * <p>
	 * 输入参数描述:
	 * </p>
	 * <p>
	 * 输出参数描述:
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int searchInspectScore(String where, List<Object> vList)
			throws Exception {
		int score = 0;
		BIDic dicBI = new BIDic(null, m_bs);
		int[] iScore = { 60, 1, 2, 3 };
		DicItemPojo inspect01 = dicBI
				.getDicItemByRedis("SCORE_ITEM_INSPECT_01");
		DicItemPojo inspect02 = dicBI
				.getDicItemByRedis("SCORE_ITEM_INSPECT_02");
		DicItemPojo inspect03 = dicBI
				.getDicItemByRedis("SCORE_ITEM_INSPECT_03");
		DicItemPojo inspect = dicBI.getDicItemByRedis("SCORE_ITEM_INSPECT");
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
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append(" t1.DEF_CLASS");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.PLAN_DATE")
				+ " as PLAN_DATE");
		strSQL.append(" from T_INSPECT_PLAN t,T_INSPECT_DEF t1");
		strSQL.append(" where t.DEF_ID=t1.DEF_ID and t.PLAN_ID is not null and (t.PLAN_STATE=0 OR t.PLAN_OPUSER is not null) ");
		strSQL.append(" and t.PLAN_STATE=0");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		long outDay = 0;
		boolean scoreOver = false;
		if (rs != null) {
			if (rs.next()) {// 得到逾期天数
				if (!scoreOver) {
					outDay = m_bs.getDateEx().getDateCount(
							rs.getString("PLAN_DATE").substring(0, 10)
									+ " 00:00:00",
							m_bs.getDateEx().getThisDate(0, 0).substring(0, 10)
									+ " 23:59:59") - 1;
					if (outDay > 0) {
						score += outDay * iScore[rs.getInt("DEF_CLASS") - 1];
						if (score > 100 - iScore[0]) {
							score = 40;
							scoreOver = true;
						}
					}
				}
			}
			rs.close();
		}
		return score;
	}

	/**
	 * <p>
	 * 方法名称: searchTruckOilStats
	 * </p>
	 * <p>
	 * 方法功能描述: 得到报警信息的统计。
	 * </p>
	 * <p>
	 * 输入参数描述:
	 * </p>
	 * <p>
	 * 输出参数描述:
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONObject searchTruckOilStats(String where, List<Object> vList)
			throws Exception {
		JSONObject retObj = new JSONObject();
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("to_timestamp(to_char(t.s_cdate,'YYYY-MM-DD'),'YYYY-MM-DD') as oil_date");
		strSQL.append(",sum(to_number(t.OIL_VALUME, '99999D99' )) as OIL_VALUME");
		strSQL.append(" from T_VEHICLE_DATA t,T_EQUIPMENT_INST t1,T_EQUIPMENT_DEF t3");
		strSQL.append(" where t.eqp_inst=t1.eqp_inst");
		strSQL.append(" and t1.eqp_def = t3.EQP_CODE");
		strSQL.append(" and t3.eqp_type = 'EQUIPMENT_DEFTYPE_0'");
		strSQL.append(" and (t.OIL_VALUME is not null or t.OIL_VALUME <> '')");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		strSQL.append(" group by oil_date");
		strSQL.append(" order by oil_date");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				JSONObject oneFault = new JSONObject();
				oneFault.put("date", rs.getString("oil_date").substring(0, 10));
				oneFault.put("count", rs.getString("OIL_VALUME"));
				retObj.put(oneFault.getString("date"), oneFault);
			}
			rs.close();
		}
		return retObj;
	}

	/**
	 * <p>
	 * 方法名称: getTruckWorkTimeByWhere
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆统计报告列表。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public JSONObject getTruckWorkTimeByWhere(String where, List<Object> vList)
			throws Exception {
		JSONObject paras = new JSONObject();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select sum(to_number(t.WORK_TIME, '9999999999999999999.99')) as ALL_TIME ");
		strSQL.append(" from T_TRUCK_WORK_DAY_LOGS t,T_TRUCK_INST t1 ");
		strSQL.append(" where  t.TRUCK_ID=t1.TRUCK_ID");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				paras.put("alltime", rs.getString("ALL_TIME"));
			}
			rs.close();
		}
		return paras;
	}
}
