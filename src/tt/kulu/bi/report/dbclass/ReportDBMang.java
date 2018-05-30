package tt.kulu.bi.report.dbclass;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.report.pojo.TruckReportPojo;
import tt.kulu.bi.report.pojo.UserReportPojo;
import tt.kulu.out.call.BIArea;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BITruck;
import tt.kulu.out.call.BIUser;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 类名：ReportDBMang
 * </p>
 * <p>
 * 功能描述：工作报告的数据权限读取操作类
 * </p>
 * <p>
 * 创建人：梁浩
 * </p>
 * <p>
 * 创建时间：2018-03-28
 * </p>
 * <p>
 * 版本：0.1
 * </p>
 * 
 */
public class ReportDBMang extends BSDBBase {
	private BITruck truckBI = null;
	private BIArea areaBI = null;
	private BIUser userBI = null;

	public ReportDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
		this.truckBI = new BITruck(sqlHelper, m_bs);
		this.areaBI = new BIArea(sqlHelper, m_bs);
		this.userBI = new BIUser(m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getTruckReportList
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
	public ArrayList<TruckReportPojo> getTruckReportList(JSONObject where,
			String orderBy, List<Object> vList) throws Exception {
		ArrayList<TruckReportPojo> list = new ArrayList<TruckReportPojo>();
		StringBuffer strSQL = this._getTruckReportSelectSQL(where, orderBy);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneTruckReportPojo(rs));
			}
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getTruckReportList
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
	public ArrayList<UserReportPojo> getUserReportList(JSONObject where,
			String orderBy, List<Object> vList) throws Exception {
		ArrayList<UserReportPojo> list = new ArrayList<UserReportPojo>();
		StringBuffer strSQL = this._getUserReportSelectSQL(where, orderBy);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneUserReportPojo(rs));
			}
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getTruckReportList
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
	public JSONObject getUserReportDate(JSONObject where, List<Object> vList)
			throws Exception {
		JSONObject paras = new JSONObject();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select min(t1.LOG_DATE) as MIN_DATE,max(t1.LOG_DATE) as MAX_DATE");
		strSQL.append(" from T_USER t");
		strSQL.append(" left outer join T_USER_WORK_DAY_LOGS t1 on (t1.USER_INSTID = t.USER_INSTID "
				+ (where.containsKey("work") ? where.getString("work") : "")
				+ ")");
		strSQL.append(" where t.USER_INSTID is not null ");
		if (where.containsKey("user")) {
			strSQL.append(" " + where.getString("user"));
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				paras.put("mindate", rs.getString("MIN_DATE"));
				paras.put("maxdate", rs.getString("MAX_DATE"));
			}
			rs.close();
		}
		return paras;
	}

	/**
	 * <p>
	 * 方法名称: getTruckReportList
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
	public JSONObject getTruckReportDate(JSONObject where, List<Object> vList)
			throws Exception {
		JSONObject paras = new JSONObject();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select min(t1.LOG_DATE) as MIN_DATE,max(t1.LOG_DATE) as MAX_DATE");
		strSQL.append(" from T_TRUCK_INST t left outer join T_USER tu on t.TRUCK_MANGUSER = tu.USER_INSTID");
		strSQL.append(" left outer join T_TRUCK_WORK_DAY_LOGS t1 on (t1.TRUCK_ID = t.TRUCK_ID "
				+ (where.containsKey("work") ? where.getString("work") : "")
				+ ")");
		strSQL.append(" where t.TRUCK_ID is not null ");
		if (where.containsKey("truck")) {
			strSQL.append(" " + where.getString("truck"));
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				paras.put("mindate", rs.getString("MIN_DATE"));
				paras.put("maxdate", rs.getString("MAX_DATE"));
			}
			rs.close();
		}
		return paras;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getTruckReportSelectSQL(JSONObject where,
			String orderBy) throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.TRUCK_ID");
		strSQL.append(",t.TRUCK_NAME");
		strSQL.append(",t.TRUCK_NO");
		strSQL.append(",t.TD_ID");
		strSQL.append(",t.A_ID");
		strSQL.append(",t.PLATE_NUM");
		strSQL.append(",t.PLATE_COLOR");
		// 管理用户
		strSQL.append(",t.TRUCK_MANGUSER");
		strSQL.append(",tu.USER_ID");
		strSQL.append(",tu.USER_NAME");
		strSQL.append(",tu.MPHONE");
		// 机构
		strSQL.append(",t.ORG_ID");
		// 报表
		strSQL.append(",sum( to_number( t1.log_distance, '9999999999999999999.99' )) as ALL_DIS");
		strSQL.append(",sum( to_number( t1.log_oil, '9999999999999999999.99' )) as ALL_OIL");// 0.1mm
		strSQL.append(",sum( to_number( t1.WORK_TIME, '9999999999999999999.99' )) as ALL_TIME");// 秒

		strSQL.append(" from T_TRUCK_INST t left outer join T_USER tu on t.TRUCK_MANGUSER = tu.USER_INSTID");
		strSQL.append(" left outer join T_TRUCK_WORK_DAY_LOGS t1 on (t1.TRUCK_ID = t.TRUCK_ID "
				+ (where.containsKey("work") ? where.getString("work") : "")
				+ ")");
		strSQL.append(" where t.TRUCK_ID is not null ");
		if (where.containsKey("truck")) {
			strSQL.append(" " + where.getString("truck"));
		}
		strSQL.append(" group by t.TRUCK_ID");
		strSQL.append(",t.TRUCK_NAME");
		strSQL.append(",t.TRUCK_NO");
		strSQL.append(",t.TD_ID");
		strSQL.append(",t.A_ID");
		strSQL.append(",t.PLATE_NUM");
		strSQL.append(",t.PLATE_COLOR");
		// 管理用户
		strSQL.append(",t.TRUCK_MANGUSER");
		strSQL.append(",tu.USER_ID");
		strSQL.append(",tu.USER_NAME");
		strSQL.append(",tu.MPHONE");
		// 机构
		strSQL.append(",t.ORG_ID");
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.TRUCK_NO");
		}
		return strSQL;
	}

	// 物品定义对象
	private TruckReportPojo _setOneTruckReportPojo(ResultSet rs)
			throws Exception {
		TruckReportPojo onePojo = new TruckReportPojo();
		onePojo.getTruck().setId(rs.getString("TRUCK_ID"));
		onePojo.getTruck().setName(rs.getString("TRUCK_NAME"));
		if (rs.getString("TRUCK_NO") != null) {
			onePojo.getTruck().setNo(rs.getString("TRUCK_NO"));
		}
		onePojo.getTruck().setDefine(
				this.truckBI.getTruckDefByRedis(rs.getString("TD_ID")));
		onePojo.getTruck().setArea(
				this.areaBI.getAreaByRedis(rs.getString("A_ID")));
		if (rs.getString("PLATE_NUM") != null) {
			onePojo.getTruck().setPlateNum(rs.getString("PLATE_NUM"));
		}
		if (rs.getString("PLATE_COLOR") != null) {
			onePojo.getTruck().setPlateColor(rs.getInt("PLATE_COLOR"));
		}

		// 管理员
		if (rs.getString("TRUCK_MANGUSER") != null) {
			onePojo.getTruck().getMangUser()
					.setInstId(rs.getString("TRUCK_MANGUSER"));
			if (rs.getString("USER_ID") != null) {
				onePojo.getTruck().getMangUser().setId(rs.getString("USER_ID"));
			}
			if (rs.getString("USER_NAME") != null) {
				onePojo.getTruck().getMangUser()
						.setName(rs.getString("USER_NAME"));
			}
			if (rs.getString("MPHONE") != null) {
				onePojo.getTruck().getMangUser()
						.setmPhone(rs.getString("MPHONE"));
			}
		}

		// 机构
		if (rs.getString("ORG_ID") != null
				&& !rs.getString("ORG_ID").equals("")) {
			onePojo.getTruck().setOrg(
					this.userBI.getGroupByRedis(rs.getString("ORG_ID")));
		}
		// 统计
		onePojo.setOil(rs.getFloat("ALL_OIL"));
		onePojo.setDistance(rs.getFloat("ALL_DIS"));
		onePojo.setWorkTimes(rs.getLong("ALL_TIME"));
		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getUserReportSelectSQL(JSONObject where,
			String orderBy) throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.USER_INSTID");
		strSQL.append(",t.USER_ID");
		strSQL.append(",t.USER_NAME");
		strSQL.append(",t.MPHONE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.USER_BIRTHDAY")
				+ " as USER_BIRTHDAY");
		strSQL.append(",t.USER_SBFLG");
		// 机构
		strSQL.append(",t.ORG_ID");
		// 报表
		strSQL.append(",sum( to_number( t1.log_distance, '9999999999999999999.99' )) as ALL_DIS");
		strSQL.append(",sum(t1.LOG_STEP) as ALL_STEP");
		strSQL.append(",sum(extract(epoch from(t1.out_date - t1.in_date))) as WORK_TIME");
		strSQL.append(",count(t1.LOG_ID) as ALL_COUNT");// 到岗次数
		strSQL.append(",(select count(v.USER_INSTID) from T_USER_WORK_DAY_LOGS v where v.USER_INSTID=t.USER_INSTID and v.LOG_LATEFLG=0) as ALL_LATE");// 迟到

		strSQL.append(" from T_USER t");
		strSQL.append(" left outer join T_USER_WORK_DAY_LOGS t1 on (t1.USER_INSTID = t.USER_INSTID "
				+ (where.containsKey("work") ? where.getString("work") : "")
				+ ")");
		strSQL.append(" where t.USER_INSTID is not null ");
		if (where.containsKey("user")) {
			strSQL.append(" " + where.getString("user"));
		}
		strSQL.append(" group by t.USER_INSTID");
		strSQL.append(",t.USER_ID");
		strSQL.append(",t.USER_NAME");
		strSQL.append(",t.MPHONE");
		strSQL.append(",t.USER_BIRTHDAY");
		strSQL.append(",t.USER_SBFLG");
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.USER_ID");
		}
		return strSQL;
	}

	// 物品定义对象
	private UserReportPojo _setOneUserReportPojo(ResultSet rs) throws Exception {
		UserReportPojo onePojo = new UserReportPojo();
		onePojo.getUser().setInstId(rs.getString("USER_INSTID"));
		onePojo.getUser().setId(rs.getString("USER_ID"));
		onePojo.getUser().setName(rs.getString("USER_NAME"));
		onePojo.getUser().setSbFlg(rs.getInt("USER_SBFLG"));
		if (rs.getString("MPHONE") != null) {
			onePojo.getUser().setmPhone(rs.getString("MPHONE"));
		}
		if (rs.getString("USER_BIRTHDAY") != null) {
			onePojo.getUser().setBirthday(rs.getString("USER_BIRTHDAY"));
		}
		// 机构
		if (rs.getString("ORG_ID") != null
				&& !rs.getString("ORG_ID").equals("")) {
			onePojo.getUser().setOrg(
					this.userBI.getGroupByRedis(rs.getString("ORG_ID")));
		}
		// 统计
		onePojo.setStep(rs.getLong("ALL_STEP"));
		onePojo.setDistance(rs.getFloat("ALL_DIS"));
		onePojo.setAbs(rs.getLong("ALL_COUNT"));
		onePojo.setLate(rs.getLong("ALL_LATE"));
		if (rs.getString("WORK_TIME") != null) {
			onePojo.setWorkTime(rs.getString("WORK_TIME"));
		}
		return onePojo;
	}

}
