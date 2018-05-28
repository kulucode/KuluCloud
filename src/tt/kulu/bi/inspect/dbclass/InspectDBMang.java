package tt.kulu.bi.inspect.dbclass;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.inspect.pojo.InspectDefPojo;
import tt.kulu.bi.inspect.pojo.InspectPlanPojo;
import tt.kulu.out.call.BIInspect;
import tt.kulu.out.call.BITruck;
import tt.kulu.out.call.BIUser;

/**
 * <p>
 * 标题: InspectDBMang
 * </p>
 * <p>
 * 功能描述: 车辆保养数据库操作类
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
public class InspectDBMang extends BSDBBase {
	public InspectDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getInspectDefList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到保养列表。
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
	public ArrayList<InspectDefPojo> getInspectDefList(String where,
			String orderBy, List<Object> vList) throws Exception {
		ArrayList<InspectDefPojo> list = new ArrayList<InspectDefPojo>();
		ResultSet rs = this.sqlHelper.queryBySql(
				_getInspectDefSelectSQL(where, orderBy).toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneInspectDefPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getOneInspectDefById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个保养。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public InspectDefPojo getOneInspectDefById(String id) throws Exception {
		InspectDefPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		ResultSet rs = this.sqlHelper.queryBySql(
				this._getInspectDefSelectSQL(" and t.DEF_ID=?", "").toString(),
				vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneInspectDefPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertInspectDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个保养。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int insertInspectDef(InspectDefPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo.getId() == null || onePojo.getId().trim().equals("")) {
			onePojo.setId(BSGuid.getRandomGUID());
		}
		if (onePojo.getSubDefId() != null
				&& !onePojo.getSubDefId().getId().equals("")
				&& onePojo.getSubCount() > 0) {
			onePojo.setSubDefId((new BIInspect(null)
					.getInspectDefByRedis(onePojo.getSubDefId().getId())));
			// 判断下级，重新设置时间和里程
			onePojo.setCycle(onePojo.getSubDefId().getCycle()
					* onePojo.getSubCount());
			onePojo.setMileage(String.valueOf(Integer.parseInt(onePojo
					.getSubDefId().getMileage()) * onePojo.getSubCount()));
		}
		StringBuffer strSQL = new StringBuffer("insert into T_INSPECT_DEF (");
		strSQL.append("DEF_ID");
		strSQL.append(",TD_ID");
		strSQL.append(",DEF_NAME");
		strSQL.append(",DEF_CLASS");
		strSQL.append(",DEF_STATE");
		strSQL.append(",IN_CYCLE");
		strSQL.append(",MILEAGE");
		strSQL.append(",SUB_COUNT");
		strSQL.append(",R_DEF_ID");
		strSQL.append(",DEF_DESC");
		strSQL.append(") values (?,?,?,?,?,?,?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		vList.add(onePojo.getTurckDef().getId());
		vList.add(onePojo.getName());
		vList.add(onePojo.getDefClass());
		vList.add(onePojo.getState());
		vList.add(onePojo.getCycle());
		vList.add(onePojo.getMileage());
		vList.add(onePojo.getSubCount());
		if (onePojo.getSubDefId() != null) {
			vList.add(onePojo.getSubDefId().getId());
		} else {
			vList.add("");
		}
		vList.add(onePojo.getDesc());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);

		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateInspectDef
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个保养。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateInspectDef(InspectDefPojo onePojo) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		if ((onePojo.getId() == null || onePojo.getId().trim().equals(""))
				|| this.sqlHelper
						.queryIntBySql(
								"select count(DEF_ID) from T_INSPECT_DEF where DEF_ID=?",
								vList) <= 0) {
			// 新增
			count += this.insertInspectDef(onePojo);
		} else {
			if (onePojo.getSubDefId() != null
					&& !onePojo.getSubDefId().getId().equals("")
					&& onePojo.getSubCount() > 0) {
				onePojo.setSubDefId((new BIInspect(null)
						.getInspectDefByRedis(onePojo.getSubDefId().getId())));
				// 判断下级，重新设置时间和里程
				onePojo.setCycle(onePojo.getSubDefId().getCycle()
						* onePojo.getSubCount());
				onePojo.setMileage(String.valueOf(Integer.parseInt(onePojo
						.getSubDefId().getMileage()) * onePojo.getSubCount()));
			}
			StringBuffer strSQL = new StringBuffer("update T_INSPECT_DEF set ");
			strSQL.append("TD_ID=?");
			strSQL.append(",DEF_NAME=?");
			strSQL.append(",DEF_CLASS=?");
			strSQL.append(",DEF_STATE=?");
			strSQL.append(",IN_CYCLE=?");
			strSQL.append(",MILEAGE=?");
			strSQL.append(",SUB_COUNT=?");
			strSQL.append(",R_DEF_ID=?");
			strSQL.append(",DEF_DESC=?");
			strSQL.append(" where DEF_ID=?");
			vList.clear();
			vList.add(onePojo.getTurckDef().getId());
			vList.add(onePojo.getName());
			vList.add(onePojo.getDefClass());
			vList.add(onePojo.getState());
			vList.add(onePojo.getCycle());
			vList.add(onePojo.getMileage());
			vList.add(onePojo.getSubCount());
			if (onePojo.getSubDefId() != null) {
				vList.add(onePojo.getSubDefId().getId());
			} else {
				vList.add("");
			}
			vList.add(onePojo.getDesc());
			vList.add(onePojo.getId());
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteInspectDef
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个保养。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int deleteInspectDef(String defId) throws Exception {
		int count = 0;
		// 删除计划
		List<Object> vList = new ArrayList<Object>();
		vList.add(defId);
		count = this.sqlHelper.updateBySql(
				"delete from T_INSPECT_PLAN where DEF_ID=?", vList);
		count += this.sqlHelper
				.updateBySql(
						"update T_INSPECT_DEF set R_DEF_ID='',SUB_COUNT=0 where R_DEF_ID=?",
						vList);
		count += this.sqlHelper.updateBySql(
				"delete from T_INSPECT_DEF where DEF_ID=?", vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getInspectPlanList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到保养计划列表。
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
	public ArrayList<InspectPlanPojo> getInspectPlanList(String where,
			String orderBy, long f, long t, List<Object> vList)
			throws Exception {
		ArrayList<InspectPlanPojo> list = new ArrayList<InspectPlanPojo>();
		StringBuffer strSQL = _getInspectPlanSelectSQL(where, orderBy);
		strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneInspectPlanPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getInspectPlanCount
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品实例列表。
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
	public long getInspectPlanCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.PLAN_ID) as OBJ_COUNT");
		strSQL.append(" from T_TRUCK_INST t1,T_INSPECT_PLAN t");
		strSQL.append(" left outer join T_USER opu on t.PLAN_OPUSER = opu.USER_INSTID");
		strSQL.append(" where t.TRUCK_ID=t1.TRUCK_ID ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			count = rs.getLong("OBJ_COUNT");
		}
		return count;

	}

	/**
	 * <p>
	 * 方法名称: getOneInspectPlanById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个保养计划。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public InspectPlanPojo getOneInspectPlanById(String id) throws Exception {
		InspectPlanPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		ResultSet rs = this.sqlHelper.queryBySql(
				this._getInspectPlanSelectSQL(" and t.PLAN_ID=?", "")
						.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneInspectPlanPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getLastInspectPlanByDef
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个保养计划。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public InspectPlanPojo getLastInspectPlanByDef(String defId)
			throws Exception {
		InspectPlanPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(defId);
		StringBuffer strSQL = this._getInspectPlanSelectSQL(" and t.DEF_ID=?",
				" t.PLAN_DATE desc");
		strSQL.append(" LIMIT 1");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneInspectPlanPojo(rs));
			}
			rs.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getLastInspectPlanByTruck
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个保养计划。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public InspectPlanPojo getLastInspectPlanByTruck(String defId,
			String truckId) throws Exception {
		InspectPlanPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(defId);
		vList.add(truckId);
		StringBuffer strSQL = this._getInspectPlanSelectSQL(
				" and t.DEF_ID=? and t.TRUCK_ID=?", " t.PLAN_DATE desc");
		strSQL.append(" LIMIT 1");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneInspectPlanPojo(rs));
			}
			rs.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getLastInspectPlanByWhere
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个保养计划。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public InspectPlanPojo getLastInspectPlanByWhere(String where,
			List<Object> vList) throws Exception {
		InspectPlanPojo onePojo = null;
		StringBuffer strSQL = this._getInspectPlanSelectSQL(where,
				" t.PLAN_DATE desc");
		strSQL.append(" LIMIT 1");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneInspectPlanPojo(rs));
			}
			rs.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertInspectPlan
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个保养。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int insertInspectPlan(InspectPlanPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo.getId() == null || onePojo.getId().trim().equals("")) {
			onePojo.setId(BSGuid.getRandomGUID());
		}
		// 设置计划时间
		Calendar pDate = this.bsDate.getStringToCalendar(onePojo
				.getCreateDate());
		int day = onePojo.getInspectDef().getCycle()
				/ onePojo.getInspectDef().getTurckDef().getWorkTime();
		if (day > 0) {
			pDate.add(Calendar.DATE, day);
			onePojo.setPlanDate(this.bsDate.getCalendarToStringAll(pDate));
			StringBuffer strSQL = new StringBuffer(
					"insert into T_INSPECT_PLAN (");
			strSQL.append("PLAN_ID");
			strSQL.append(",DEF_ID");
			strSQL.append(",TRUCK_ID");
			strSQL.append(",C_DATE");
			strSQL.append(",PLAN_DATE");
			strSQL.append(",PLAN_STATE");
			strSQL.append(",PLAN_OPUSER");
			strSQL.append(") values (?,?,?,?,?,?,?)");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getId());
			vList.add(onePojo.getInspectDef().getId());
			vList.add(onePojo.getTruck().getId());
			vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
			vList.add(Timestamp.valueOf(onePojo.getPlanDate()));
			vList.add(onePojo.getState());
			vList.add(onePojo.getOpUser().getInstId());
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateInspectPlan
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个保养。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateInspectPlan(InspectPlanPojo onePojo) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		// 设置计划时间
		Calendar pDate = this.bsDate.getStringToCalendar(onePojo
				.getCreateDate());
		int day = onePojo.getInspectDef().getCycle()
				/ onePojo.getInspectDef().getTurckDef().getWorkTime();
		if (day > 0) {
			pDate.add(Calendar.DATE, day);
			onePojo.setPlanDate(this.bsDate.getCalendarToStringAll(pDate));

			if ((onePojo.getId() == null || onePojo.getId().trim().equals(""))
					|| this.sqlHelper
							.queryIntBySql(
									"select count(DEF_ID) from T_INSPECT_PLAN where PLAN_ID=?",
									vList) <= 0) {
				// 新增
				count += this.insertInspectPlan(onePojo);
			} else {
				StringBuffer strSQL = new StringBuffer(
						"update T_INSPECT_PLAN set ");
				strSQL.append("C_DATE=?");
				strSQL.append(",PLAN_DATE=?");
				strSQL.append(",PLAN_STATE=?");
				strSQL.append(",PLAN_OPUSER=?");
				strSQL.append(" where PLAN_ID=?");
				vList.clear();
				vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
				vList.add(Timestamp.valueOf(onePojo.getPlanDate()));
				vList.add(onePojo.getState());
				vList.add(onePojo.getOpUser().getInstId());
				vList.add(onePojo.getId());
				count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateInspectPlanOP
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个保养。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateInspectPlanOP(InspectPlanPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			List<Object> vList = new ArrayList<Object>();
			StringBuffer strSQL = new StringBuffer("update T_INSPECT_PLAN set ");
			strSQL.append("PLAN_STATE=?");
			strSQL.append(",PLAN_OPUSER=?");
			strSQL.append(" where PLAN_ID=?");
			vList.clear();
			vList.add(1);
			vList.add(onePojo.getOpUser().getInstId());
			vList.add(onePojo.getId());
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
			// 创建新的计划
			InspectDefPojo onePlanDef = (new BIInspect(null)
					.getInspectDefByRedis(onePojo.getInspectDef().getId()));
			if (onePlanDef != null) {
				onePojo.setId("");
				onePojo.setCreateDate(onePojo.getPlanDate());
				onePojo.setState(0);
				Calendar pDate = this.bsDate.getStringToCalendar(onePojo
						.getCreateDate());
				pDate.add(Calendar.DATE, onePlanDef.getCycle());
				onePojo.setPlanDate(this.bsDate.getCalendarToStringAll(pDate));
				count += this.insertInspectPlan(onePojo);
			}
		}
		return count;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getInspectDefSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.DEF_ID");
		strSQL.append(",t.TD_ID");
		strSQL.append(",t.DEF_NAME");
		strSQL.append(",t.DEF_CLASS");
		strSQL.append(",t.IN_CYCLE");
		strSQL.append(",t.MILEAGE");
		strSQL.append(",t.SUB_COUNT");
		strSQL.append(",t.R_DEF_ID");
		strSQL.append(",t.DEF_DESC");
		strSQL.append(",t.DEF_STATE");
		strSQL.append(" from T_INSPECT_DEF t");
		strSQL.append(" where t.DEF_ID is not null ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.DEF_NAME");
		}
		return strSQL;
	}

	// 物品定义对象
	private InspectDefPojo _setOneInspectDefPojo(ResultSet rs) throws Exception {
		InspectDefPojo onePojo = new InspectDefPojo();
		onePojo.setId(rs.getString("DEF_ID"));
		onePojo.setName(rs.getString("DEF_NAME"));
		onePojo.setTurckDef(new BITruck(this.sqlHelper, this.m_bs)
				.getTruckDefByRedis(rs.getString("TD_ID")));
		onePojo.setState(rs.getInt("DEF_STATE"));

		onePojo.setDefClass(rs.getInt("DEF_CLASS"));
		onePojo.setCycle(rs.getInt("IN_CYCLE"));
		onePojo.setMileage(rs.getString("MILEAGE"));
		onePojo.setDefClass(rs.getInt("DEF_CLASS"));
		onePojo.setSubCount(rs.getInt("SUB_COUNT"));
		if (rs.getString("R_DEF_ID") != null
				&& !rs.getString("R_DEF_ID").trim().equals("")) {
			onePojo.setSubDefId(new BIInspect(this.sqlHelper, this.m_bs)
					.getInspectDefByRedis(rs.getString("R_DEF_ID")));
		}
		if (rs.getString("DEF_DESC") != null) {
			onePojo.setDesc(rs.getString("DEF_DESC"));
		}
		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getInspectPlanSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.PLAN_ID");
		strSQL.append(",t.DEF_ID");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.C_DATE")
				+ " as C_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.PLAN_DATE")
				+ " as PLAN_DATE");
		strSQL.append(",t.PLAN_STATE");
		strSQL.append(",t.PLAN_OPUSER");
		// 车辆
		strSQL.append(",t1.TRUCK_ID");
		strSQL.append(",t1.TRUCK_NAME");
		strSQL.append(",t1.TRUCK_NO");
		strSQL.append(",t1.PLATE_NUM");
		strSQL.append(",t1.TD_ID");
		strSQL.append(",t1.ORG_ID as FRT_ORG");
		// 处理人
		strSQL.append(",t.PLAN_OPUSER");
		strSQL.append(",opu.USER_ID as OPU_USER_ID");
		strSQL.append(",opu.USER_NAME as OPU_USER_NAME");
		strSQL.append(",opu.MPHONE as OPU_MPHONE");
		strSQL.append(",opu.ORG_ID as OPU_ORG");
		strSQL.append(" from T_TRUCK_INST t1,T_INSPECT_PLAN t");
		strSQL.append(" left outer join T_USER opu on t.PLAN_OPUSER = opu.USER_INSTID");
		strSQL.append(" where t.TRUCK_ID=t1.TRUCK_ID ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.PLAN_DATE");
		}
		return strSQL;
	}

	// 物品定义对象
	private InspectPlanPojo _setOneInspectPlanPojo(ResultSet rs)
			throws Exception {
		InspectPlanPojo onePojo = new InspectPlanPojo();
		onePojo.setId(rs.getString("PLAN_ID"));
		onePojo.setInspectDef(new BIInspect(null, null).getInspectDefByRedis(rs
				.getString("DEF_ID")));
		onePojo.setCreateDate(rs.getString("C_DATE"));
		onePojo.setPlanDate(rs.getString("PLAN_DATE"));
		onePojo.setState(rs.getInt("PLAN_STATE"));

		// 车辆
		onePojo.getTruck().setId(rs.getString("TRUCK_ID"));
		onePojo.getTruck().setName(rs.getString("TRUCK_NAME"));
		if (rs.getString("TRUCK_NO") != null) {
			onePojo.getTruck().setNo(rs.getString("TRUCK_NO"));
		}
		if (rs.getString("PLATE_NUM") != null) {
			onePojo.getTruck().setPlateNum(rs.getString("PLATE_NUM"));
		}
		if (rs.getString("FRT_ORG") != null) {
			onePojo.getTruck()
					.setOrg((new BIUser(null)).getGroupByRedis(rs
							.getString("FRT_ORG")));
		}
		onePojo.getTruck().setDefine(
				(new BITruck(null)).getTruckDefByRedis(rs.getString("TD_ID")));

		// 处理人
		if (rs.getString("PLAN_OPUSER") != null
				&& !rs.getString("PLAN_OPUSER").equals("")) {
			onePojo.getOpUser().setInstId(rs.getString("PLAN_OPUSER"));
			onePojo.getOpUser().setId(rs.getString("OPU_USER_ID"));
			onePojo.getOpUser().setName(rs.getString("OPU_USER_NAME"));
			if (rs.getString("OPU_MPHONE") != null) {
				onePojo.getOpUser().setmPhone(rs.getString("OPU_MPHONE"));
			}
			if (rs.getString("OPU_ORG") != null) {
				onePojo.getOpUser().setOrg(
						(new BIUser(null)).getGroupByRedis(rs
								.getString("OPU_ORG")));
			}
		}
		return onePojo;
	}
}
