package tt.kulu.bi.watch.dbclass;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.storage.dbclass.EquipmentDBMang;
import tt.kulu.bi.storage.pojo.EquipmentDefPojo;
import tt.kulu.bi.storage.pojo.EquipmentGeometryPojo;
import tt.kulu.bi.user.pojo.UserWorkParasMinPojo;
import tt.kulu.bi.user.pojo.UserWorkParasPojo;
import tt.kulu.bi.watch.pojo.InBloodPressureOkCmd;
import tt.kulu.bi.watch.pojo.InHeartRateCmd;
import tt.kulu.bi.watch.pojo.InStepCmd;
import tt.kulu.out.call.BIEquipment;
import tt.kulu.out.call.BIUser;
import tt.kulu.out.call.BIWatch;

/**
 * <p>
 * 标题: WatchDBMang
 * </p>
 * <p>
 * 功能描述: 手表数据库操作类
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
public class WatchDBMang extends BSDBBase {
	private EquipmentDBMang eqpDB = null;

	public WatchDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
		this.eqpDB = new EquipmentDBMang(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: insertWatchStep
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个物品定义。
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
	public int insertWatchStep(InStepCmd onePojo) throws Exception {
		int count = 1;
		// 判断上一个步数，是否与当前相同
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getEqpInst().getInstId());
		String lastStep = (String) this.sqlHelper
				.queryObjectBySql(
						"select A_VALUE from T_WATCH_STEP where EQP_INST=? order by S_CDATE desc",
						vList);

		if (lastStep == null || !lastStep.equals(onePojo.getStep())) {
			long thisStep = Long.parseLong(onePojo.getStep());
			if (lastStep != null && !lastStep.equals("")) {
				long lastS = Long.parseLong(lastStep);
				thisStep = (thisStep - lastS) < 0 ? thisStep : thisStep - lastS;
			}
			if (thisStep > 0) {
				StringBuffer sql = new StringBuffer(
						"insert into T_WATCH_STEP(LOG_ID,EQP_INST,S_CDATE,F_VALUE,A_VALUE) values (?,?,?,?,?)");
				vList.clear();
				vList.add(BSGuid.getRandomGUID());
				vList.add(onePojo.getEqpInst().getInstId());
				vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
				vList.add(String.valueOf(thisStep));
				vList.add(onePojo.getStep());
				count = this.sqlHelper.updateBySql(sql.toString(), vList);
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertWatchBloodPressureOK
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一条血压正常记录。
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
	public int insertWatchBloodPressureOK(InBloodPressureOkCmd onePojo)
			throws Exception {
		int count = 0;
		// 判断上一个步数，是否与当前相同
		List<Object> vList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"insert into T_WATCH_BRO(LOG_ID,EQP_INST,S_CDATE,BRE_HIGH,BRE_LOW) values (?,?,?,?,?)");
		vList.add(BSGuid.getRandomGUID());
		vList.add(onePojo.getEqpInst().getInstId());
		vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
		vList.add(onePojo.getHigh());
		vList.add(onePojo.getLow());
		count = this.sqlHelper.updateBySql(sql.toString(), vList);

		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertWatchBloodPressureError
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一条血压正常记录。
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
	public int insertWatchBloodPressureError(InBloodPressureOkCmd onePojo)
			throws Exception {
		int count = 0;
		// 判断上一个步数，是否与当前相同
		List<Object> vList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"insert into T_WATCH_BRE(LOG_ID,EQP_INST,S_CDATE,BRE_HIGH,BRE_LOW) values (?,?,?,?,?)");
		vList.add(BSGuid.getRandomGUID());
		vList.add(onePojo.getEqpInst().getInstId());
		vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
		vList.add(onePojo.getHigh());
		vList.add(onePojo.getLow());
		count = this.sqlHelper.updateBySql(sql.toString(), vList);

		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertWatchInHeartRate
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一条血压正常记录。
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
	public int insertWatchInHeartRate(InHeartRateCmd onePojo) throws Exception {
		int count = 0;
		// 判断上一个步数，是否与当前相同
		List<Object> vList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"insert into T_WATCH_HR(LOG_ID,EQP_INST,S_CDATE,BBE_VALUE,BBE_ELE) values (?,?,?,?,?)");
		vList.add(BSGuid.getRandomGUID());
		vList.add(onePojo.getEqpInst().getInstId());
		vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
		vList.add(onePojo.getHeartRate());
		vList.add(onePojo.getElectricity());
		count = this.sqlHelper.updateBySql(sql.toString(), vList);

		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertWatchInHeartRate
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一条血压正常记录。
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
	public int insertWatchInHeartRateError(InHeartRateCmd onePojo)
			throws Exception {
		int count = 0;
		// 判断上一个步数，是否与当前相同
		List<Object> vList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"insert into T_WATCH_HRE(LOG_ID,EQP_INST,S_CDATE,BBE_VALUE) values (?,?,?,?)");
		vList.add(BSGuid.getRandomGUID());
		vList.add(onePojo.getEqpInst().getInstId());
		vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
		vList.add(onePojo.getHeartRate());
		count = this.sqlHelper.updateBySql(sql.toString(), vList);

		return count;
	}

	/**
	 * <p>
	 * 方法名称: getWatchWordParasList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到用户列表。
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
	public ArrayList<UserWorkParasPojo> getWatchWordParasList(String where,
			String orderBy, List<Object> vList, long f, long t)
			throws Exception {
		// 翻页代码
		ArrayList<UserWorkParasPojo> list = new ArrayList<UserWorkParasPojo>();
		StringBuffer strSQL = new StringBuffer(
				this._getWatchWordParasSelectSQL(where, orderBy));
		strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneWatchWordParasPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getWatchWordParasList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到用户列表。
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
	public UserWorkParasMinPojo getOneWatchWordParasMinByInstId(String instId)
			throws Exception {
		// 翻页代码
		UserWorkParasMinPojo onePojo = new UserWorkParasMinPojo();
		StringBuffer strSQL = new StringBuffer(
				"select t.eqp_inst,tg.S_LAT,tg.S_LON");
		strSQL.append(",(select tws.a_value || ',' || tws.s_cdate from t_watch_step tws where tws.eqp_inst = t.eqp_inst order by tws.s_cdate desc limit 1) as step");
		strSQL.append(",(select twb.bre_high || ',' || twb.bre_low || ',' || twb.s_cdate from t_watch_bro twb where twb.eqp_inst = t.eqp_inst order by twb.s_cdate desc limit 1) as bro");
		strSQL.append(",(select twh.bbe_value || ',' || twh.bbe_ele || ',' || twh.s_cdate from t_watch_hr twh where twh.eqp_inst = t.eqp_inst order by twh.s_cdate desc limit 1) as hr");
		strSQL.append(",(select twh.bbe_value || ',' || twh.bbe_ele || ',' || twh.s_cdate from t_watch_hr twh where twh.eqp_inst = t.eqp_inst order by twh.s_cdate desc limit 1) as hr");
		strSQL.append(" from t_equipment_def def,t_equipment_inst t left outer join t_eqp_inst_geometry tg on tg.log_id = t.EQP_GEO_ID");
		strSQL.append(" where t.eqp_def = def.eqp_code");
		strSQL.append(" and def.eqp_type = 'EQUIPMENT_DEFTYPE_1'");
		strSQL.append(" and t.eqp_inst=?");
		List<Object> vList = new ArrayList<Object>();
		vList.add(instId);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = new UserWorkParasMinPojo();
			onePojo.setEqpInst(rs.getString("eqp_inst"));
			if (rs.getString("S_LAT") != null) {
				onePojo.setLatitude(rs.getString("S_LAT"));
			}
			if (rs.getString("S_LON") != null) {
				onePojo.setLongitude(rs.getString("S_LON"));
			}
			String[] values = null;
			if (rs.getString("step") != null) {
				values = rs.getString("step").split(",");
				if (values.length > 0) {
					onePojo.setStep(values[0]);
					onePojo.setStepDate(values[1]);
				}
			}
			if (rs.getString("bro") != null) {
				values = rs.getString("bro").split(",");
				if (values.length > 0) {
					onePojo.setBroHigh(values[0]);
					onePojo.setBroLow(values[1]);
					onePojo.setBroDate(values[2]);
				}
			}
			if (rs.getString("hr") != null) {
				values = rs.getString("hr").split(",");
				if (values.length > 0) {
					onePojo.setHeartRate(values[0]);
					onePojo.setEleValue(values[1]);
					onePojo.setHrDate(values[2]);
				}
			}
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getWatchWordParasCount
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个资源实例。
	 * </p>
	 * <p>
	 * 输入参数描述:
	 * </p>
	 * <p>
	 * 输出参数描述:
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public long getWatchWordParasCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.eqp_inst) as TAB_COUNT");
		strSQL.append(" from T_EQUIPMENT_INST t1,V_WATCH_WORK_PARAS t");
		strSQL.append(" left outer join T_USER tu on tu.USER_INSTID = t.eqp_muser");
		strSQL.append(" where t.eqp_inst=t1.eqp_inst");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			count = rs.getLong("TAB_COUNT");
			rs.close();
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateOneWatchState
	 * </p>
	 * <p>
	 * 方法功能描述: 修改用户联系信息。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public int updateOneWatchState(String instId, int state) throws Exception {
		int count = 0;
		if (instId != null && !instId.trim().equals("")) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(state);
			vList.add(instId);
			count += sqlHelper.updateBySql(
					"update T_EQUIPMENT_INST set EQP_STATE=? where EQP_INST=?",
					vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteOneWatch
	 * </p>
	 * <p>
	 * 方法功能描述: 修改用户联系信息。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public int updateAllWatch(int state) throws Exception {
		int count = 0;
		if (state >= 0) {
			// 修改状态
			List<Object> vList = new ArrayList<Object>();
			vList.add(state);
			count += sqlHelper
					.updateBySql(
							"update T_EQUIPMENT_INST set EQP_STATE=? where EQP_INST in (select t.EQP_INST  from T_EQUIPMENT_INST t1,V_WATCH_WORK_PARAS t left outer join T_USER tu on tu.USER_INSTID = t.eqp_muser where t.eqp_inst=t1.eqp_inst)",
							vList);
			if (state > 0) {
				(new BIWatch(null)).deleteAllWatchLastDataToRedis();
			}
		} else {
			// 物理删除
			List<Object> vList = new ArrayList<Object>();
			vList.add(4);
			StringBuffer strSQL = new StringBuffer(
					this._getWatchWordParasSelectSQL(" and t.EQP_STATE=?", ""));
			ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
			if (rs != null) {
				while (rs.next()) {
					count += this.deleteOneWatch(rs.getString("eqp_inst"));
				}
				rs.close();
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteOneWatch
	 * </p>
	 * <p>
	 * 方法功能描述: 修改用户联系信息。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public int deleteOneWatch(String instId) throws Exception {
		int count = 0;
		if (instId != null && !instId.trim().equals("")) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(instId);
			count += sqlHelper.updateBySql(
					"delete from T_WATCH_STEP where EQP_INST=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_WATCH_HRE where EQP_INST=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_WATCH_STEP where EQP_INST=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_WATCH_HR where EQP_INST=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_WATCH_BRO where EQP_INST=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_EQP_INST_GEOMETRY where EQP_INST=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_EQP_INST_GEOMETRY_HOUR where EQP_INST=?",
					vList);
			count += sqlHelper.updateBySql(
					"delete from T_EQUIPMENT_WORKLOG where EQP_INST=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_EQUIPMENT_INST_R where EQP_INST=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_EQUIPMENT_INST_R where P_EQP_INST=?", vList);

			count += sqlHelper.updateBySql(
					"delete from T_EQUIPMENT_INST where EQP_INST=?", vList);
			(new BIWatch(null)).deleteWatchLastDataToRedis(instId);
		}
		return count;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getWatchWordParasSelectSQL(String where,
			String orderBy) throws Exception {
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.eqp_inst");
		strSQL.append("," + SqlExecute.getDateToCharSql("t1.EQP_PDATE")
				+ " as EQP_PDATE");
		strSQL.append(",t1.EQP_TOKEN");
		strSQL.append(",t1.EQP_NAME");
		strSQL.append(",t1.EQP_QR");
		strSQL.append(",t1.EQP_PHONE");
		strSQL.append(",t1.EQP_STATE");
		strSQL.append(",t1.eqp_wycode");
		strSQL.append(",t1.eqp_qr");
		strSQL.append(",t1.eqp_online");
		strSQL.append(",t1.EQP_STATE");
		// 定义
		strSQL.append(",t1.EQP_DEF");
		// 机构
		strSQL.append(",t1.ORG_ID");
		// 管理用户
		strSQL.append(",t.eqp_muser");
		strSQL.append(",tu.USER_ID");
		strSQL.append(",tu.USER_NAME");
		strSQL.append(",tu.MPHONE");
		strSQL.append(" from T_EQUIPMENT_INST t1,V_WATCH_WORK_PARAS t");
		strSQL.append(" left outer join T_USER tu on tu.USER_INSTID = t.eqp_muser");
		strSQL.append(" where t.eqp_inst=t1.eqp_inst");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by" + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private UserWorkParasPojo _setOneWatchWordParasPojo(ResultSet rs)
			throws Exception {
		UserWorkParasPojo onePojo = new UserWorkParasPojo();
		onePojo.getEqpInst().setInstId(rs.getString("eqp_inst"));
		onePojo.getEqpInst().setProDate(rs.getString("EQP_PDATE"));
		onePojo.getEqpInst().setName(rs.getString("EQP_NAME"));
		onePojo.getEqpInst().setState(rs.getInt("EQP_STATE"));
		if (rs.getString("EQP_TOKEN") != null) {
			onePojo.getEqpInst().setToken(rs.getString("EQP_TOKEN"));
		}
		if (rs.getString("EQP_QR") != null) {
			onePojo.getEqpInst().setQrCode(rs.getString("EQP_QR"));
		}
		if (rs.getString("EQP_PHONE") != null) {
			onePojo.getEqpInst().setPhone(rs.getString("EQP_PHONE"));
		}
		// 定义
		onePojo.getEqpInst().setEqpDef(
				(new BIEquipment(null)).getEqpDefByRedis(rs
						.getString("EQP_DEF")));
		onePojo.getEqpInst().setWyCode(rs.getString("eqp_wycode"));
		onePojo.getEqpInst().setQrCode(rs.getString("eqp_qr"));
		onePojo.getEqpInst().setOnlineState(rs.getInt("eqp_online"));

		if (rs.getString("eqp_muser") != null
				&& !rs.getString("eqp_muser").equals("")) {
			onePojo.getEqpInst().getMangUser()
					.setInstId(rs.getString("eqp_muser"));
			onePojo.getEqpInst().getMangUser().setId(rs.getString("user_id"));
			onePojo.getEqpInst().getMangUser()
					.setName(rs.getString("user_name"));
			if (rs.getString("MPHONE") != null) {
				onePojo.getEqpInst().getMangUser()
						.setmPhone(rs.getString("MPHONE"));
			}
		}
		// 数据
		UserWorkParasMinPojo oneMin = (new BIWatch(null, null))
				.getWatchLastDataByRedis(onePojo.getEqpInst().getInstId());
		if (oneMin != null) {
			onePojo.setStep(oneMin.getStep());
			onePojo.setStepDate(oneMin.getStepDate());
			onePojo.setBroHigh(oneMin.getBroHigh());
			onePojo.setBroLow(oneMin.getBroLow());
			onePojo.setBroDate(oneMin.getBroDate());
			onePojo.setHeartRate(oneMin.getHeartRate());
			onePojo.setEleValue(oneMin.getEleValue());
			onePojo.setHrDate(oneMin.getHrDate());
			onePojo.setLatitude(oneMin.getLatitude());
			onePojo.setLongitude(oneMin.getLongitude());
			onePojo.setGeoDate(oneMin.getGeoDate());
		}
		// 机构
		if (rs.getString("ORG_ID") != null) {
			onePojo.getEqpInst().setOrg(
					(new BIUser(null)).getGroupByRedis(rs.getString("ORG_ID")));
		}

		return onePojo;
	}
}
