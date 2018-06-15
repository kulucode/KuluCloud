package tt.kulu.bi.truck.dbclass;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.storage.dbclass.EquipmentDBMang;
import tt.kulu.bi.storage.pojo.EquipmentGeometryPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.bi.truck.pojo.DriverSchedulingPojo;
import tt.kulu.bi.truck.pojo.PacketLocationReport;
import tt.kulu.bi.truck.pojo.TruckDefinePojo;
import tt.kulu.bi.truck.pojo.TruckDriverRPojo;
import tt.kulu.bi.truck.pojo.TruckFixLogsPojo;
import tt.kulu.bi.truck.pojo.TruckPojo;
import tt.kulu.bi.truck.pojo.TruckVideoPojo;
import tt.kulu.bi.truck.pojo.TruckWorkDayLogsPojo;
import tt.kulu.bi.truck.pojo.TruckWorkParasPojo;
import tt.kulu.bi.truck.pojo.TruckWorkStatsPojo;
import tt.kulu.out.call.BIArea;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BIEquipment;
import tt.kulu.out.call.BIFile;
import tt.kulu.out.call.BITruck;
import tt.kulu.out.call.BIUser;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: TruckDBMang
 * </p>
 * <p>
 * 功能描述: 车辆管理数据库操作类
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
public class TruckDBMang extends BSDBBase {
	private BITruck truckBI = null;
	private BIArea areaBI = null;
	private BIUser userBI = null;

	public TruckDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
		this.truckBI = new BITruck(sqlHelper, m_bs);
		this.areaBI = new BIArea(sqlHelper, m_bs);
		this.userBI = new BIUser(m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getTruckDefList
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
	public ArrayList<TruckDefinePojo> getTruckDefList(String where,
			String orderBy, List<Object> vList) throws Exception {
		ArrayList<TruckDefinePojo> list = new ArrayList<TruckDefinePojo>();
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(
				_getTruckDefSelectSQL(where, orderBy).toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneTruckDefinePojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getOneTruckDefById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个物品定义。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public TruckDefinePojo getOneTruckDefById(String id) throws Exception {
		TruckDefinePojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(this
				._getTruckDefSelectSQL(" and t.TD_ID=?", "").toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneTruckDefinePojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertTruckDef
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
	public int insertTruckDef(TruckDefinePojo onePojo) throws Exception {
		int count = 0;
		if (onePojo.getId() == null || onePojo.getId().trim().equals("")) {
			onePojo.setId(BSGuid.getRandomGUID());
		}
		StringBuffer strSQL = new StringBuffer("insert into T_TRUCK_DEF (");
		strSQL.append("TD_ID");
		strSQL.append(",TD_NAME");
		strSQL.append(",TD_NO");
		strSQL.append(",TD_COMPANY");
		strSQL.append(",TD_SALEDATE");
		strSQL.append(",TD_LINKMAN");
		strSQL.append(",TD_OILMJ");
		strSQL.append(",TD_OILDE");
		strSQL.append(",TD_BRAND");
		strSQL.append(",TD_WORKTIME");
		strSQL.append(") values (?,?,?,?,?,?,?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		vList.add(onePojo.getName());
		vList.add(onePojo.getNo());
		vList.add(onePojo.getCompany());
		vList.add(Timestamp.valueOf(onePojo.getSaleDate()));
		vList.add(onePojo.getLinkMan());
		vList.add(onePojo.getOilMJ());
		vList.add(onePojo.getOilDe());
		vList.add(onePojo.getBrand());
		vList.add(onePojo.getWorkTime());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);

		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个物品定义。
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
	public int updateTruckDef(TruckDefinePojo onePojo) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		if ((onePojo.getId() == null || onePojo.getId().trim().equals(""))
				|| this.sqlHelper.queryIntBySql(
						"select count(TD_ID) from T_TRUCK_DEF where TD_ID=?",
						vList) <= 0) {
			// 新增
			count += this.insertTruckDef(onePojo);
		} else {
			StringBuffer strSQL = new StringBuffer("update T_TRUCK_DEF set ");
			strSQL.append("TD_NAME=?");
			strSQL.append(",TD_NO=?");
			strSQL.append(",TD_COMPANY=?");
			strSQL.append(",TD_SALEDATE=?");
			strSQL.append(",TD_LINKMAN=?");
			strSQL.append(",TD_OILMJ=?");
			strSQL.append(",TD_OILDE=?");
			strSQL.append(",TD_BRAND=?");
			strSQL.append(",TD_WORKTIME=?");
			strSQL.append(" where TD_ID=?");
			vList.clear();
			vList.add(onePojo.getName());
			vList.add(onePojo.getNo());
			vList.add(onePojo.getCompany());
			vList.add(Timestamp.valueOf(onePojo.getSaleDate()));
			vList.add(onePojo.getLinkMan());
			vList.add(onePojo.getOilMJ());
			vList.add(onePojo.getOilDe());
			vList.add(onePojo.getBrand());
			vList.add(onePojo.getWorkTime());
			vList.add(onePojo.getId());
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getTruckList
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
	public ArrayList<TruckPojo> getTruckList(String where, String orderBy,
			long f, long t, List<Object> vList) throws Exception {
		ArrayList<TruckPojo> list = new ArrayList<TruckPojo>();
		StringBuffer strSQL = _getTruckSelectSQL(where, orderBy);
		if (f >= 0 && t > 0) {
			strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneTruckPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getTruckList
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
	public long getTruckCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.TRUCK_ID) as OBJ_COUNT");
		strSQL.append(" from T_TRUCK_INST t left outer join T_USER tu on t.TRUCK_MANGUSER = tu.USER_INSTID");
		strSQL.append(" left outer join T_ORG torg on t.ORG_ID = torg.ORG_ID");
		strSQL.append(" where t.TRUCK_ID is not null ");

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
	 * 方法名称: getOneTruckById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个物品实例。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public TruckPojo getOneTruckById(String id) throws Exception {
		TruckPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		vList.add(id);
		vList.add(id);
		vList.add(id);
		ResultSet rs = this.sqlHelper
				.queryBySql(
						this._getTruckSelectSQL(
								" and t.TRUCK_ID=? OR t.TRUCK_NO=? OR t.PLATE_NUM=? OR t.TRUCK_CJNO=?",
								"").toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneTruckPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneTruckByPlateNum
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个物品实例。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public TruckPojo getOneTruckByPlateNum(String id) throws Exception {
		TruckPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		ResultSet rs = this.sqlHelper.queryBySql(
				this._getTruckSelectSQL(" and t.PLATE_NUM=?", "").toString(),
				vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneTruckPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertTruck
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
	public int insertTruck(TruckPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo.getId() == null || onePojo.getId().trim().equals("")) {
			onePojo.setId(BSGuid.getRandomGUID());
		}
		StringBuffer strSQL = new StringBuffer("insert into T_TRUCK_INST (");
		strSQL.append("TRUCK_ID");
		strSQL.append(",TRUCK_NAME");
		strSQL.append(",TRUCK_NO");
		strSQL.append(",TRUCK_CJNO");
		strSQL.append(",TD_ID");
		strSQL.append(",TRUCK_IDATE");
		strSQL.append(",TRUCK_CDATE");
		strSQL.append(",TRUCK_MANGUSER");
		strSQL.append(",ORG_ID");
		strSQL.append(",A_ID");
		strSQL.append(",PLATE_NUM");
		strSQL.append(",PLATE_COLOR");
		strSQL.append(",TRUCK_INNAME");
		strSQL.append(",TRUCK_UNO");
		strSQL.append(",TRUCK_UDATE");
		strSQL.append(") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		vList.add(onePojo.getName());
		vList.add(onePojo.getNo());
		vList.add(onePojo.getCjNo());
		vList.add(onePojo.getDefine().getId());
		vList.add(Timestamp.valueOf(onePojo.getInDate()));
		vList.add(Timestamp.valueOf(onePojo.getProDate()));
		if (onePojo.getMangUser() != null) {
			vList.add(onePojo.getMangUser().getInstId());
		} else {
			vList.add("");
		}
		if (onePojo.getOrg() != null) {
			vList.add(onePojo.getOrg().getId());
		} else {
			vList.add("");
		}
		if (onePojo.getArea() != null && !onePojo.getArea().equals("")) {
			vList.add(onePojo.getArea().getId());
		} else {
			vList.add("430000");
		}
		vList.add(onePojo.getPlateNum());
		vList.add(onePojo.getPlateColor());
		vList.add(onePojo.getInName());
		vList.add(onePojo.getUpNo());
		if (!onePojo.getUpDate().equals("")) {
			vList.add(Timestamp.valueOf(onePojo.getUpDate()));
		} else {
			vList.add(null);
		}

		count += this.sqlHelper.updateBySql(strSQL.toString(), vList);//
		// 新增统计表
		TruckWorkStatsPojo oneStats = new TruckWorkStatsPojo();
		oneStats.setTruck(onePojo);
		oneStats.setStartDate(onePojo.getInDate());
		count += this.insertTruckWorkStats(oneStats);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个物品定义。
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
	public int updateTruck(TruckPojo onePojo) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		if ((onePojo.getId() == null || onePojo.getId().trim().equals(""))
				|| this.sqlHelper
						.queryIntBySql(
								"select count(TRUCK_ID) from T_TRUCK_INST where TRUCK_ID=?",
								vList) <= 0) {
			// 新增
			count += this.insertTruck(onePojo);
		} else {
			StringBuffer strSQL = new StringBuffer("update T_TRUCK_INST set ");
			strSQL.append("TRUCK_NAME=?");
			strSQL.append(",TRUCK_NO=?");
			strSQL.append(",TRUCK_CJNO=?");
			strSQL.append(",TD_ID=?");
			strSQL.append(",TRUCK_IDATE=?");
			strSQL.append(",TRUCK_CDATE=?");
			strSQL.append(",TRUCK_MANGUSER=?");
			strSQL.append(",ORG_ID=?");
			strSQL.append(",A_ID=?");
			strSQL.append(",PLATE_NUM=?");
			strSQL.append(",PLATE_COLOR=?");
			strSQL.append(",TRUCK_INNAME=?");
			strSQL.append(",TRUCK_UNO=?");
			strSQL.append(",TRUCK_UDATE=?");
			strSQL.append(" where TRUCK_ID=?");
			vList.clear();
			vList.add(onePojo.getName());
			vList.add(onePojo.getNo());
			vList.add(onePojo.getCjNo());
			vList.add(onePojo.getDefine().getId());
			vList.add(Timestamp.valueOf(onePojo.getInDate()));
			vList.add(Timestamp.valueOf(onePojo.getProDate()));
			if (onePojo.getMangUser() != null) {
				vList.add(onePojo.getMangUser().getInstId());
			} else {
				vList.add("");
			}
			if (onePojo.getOrg() != null) {
				vList.add(onePojo.getOrg().getId());
			} else {
				vList.add("");
			}
			if (onePojo.getArea() != null && !onePojo.getArea().equals("")) {
				vList.add(onePojo.getArea().getId());
			} else {
				vList.add("430000");
			}
			vList.add(onePojo.getPlateNum());
			vList.add(onePojo.getPlateColor());
			vList.add(onePojo.getInName());
			vList.add(onePojo.getUpNo());
			if (!onePojo.getUpDate().equals("")) {
				vList.add(Timestamp.valueOf(onePojo.getUpDate()));
			} else {
				vList.add(null);
			}
			vList.add(onePojo.getId());
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateOneTruckState
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
	public int updateOneTruckState(String instId, int state) throws Exception {
		int count = 0;
		if (instId != null && !instId.trim().equals("")) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(state);
			vList.add(instId);
			count += sqlHelper.updateBySql(
					"update T_TRUCK_INST set TRUCK_STATE=? where TRUCK_ID=?",
					vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteOneUser
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
	public int deleteOneTruck(String instId) throws Exception {
		int count = 0;
		if (instId != null && !instId.trim().equals("")) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(instId);
			count += sqlHelper
					.updateBySql(
							"update T_EQUIPMENT_INST set EQP_TRUCK='' where EQP_TRUCK=?",
							vList);
			count += sqlHelper
					.updateBySql(
							"delete from T_TRUCK_WORK_DAY_LOGS where TRUCK_ID=?",
							vList);
			count += sqlHelper.updateBySql(
					"delete from T_TRUCK_WORK_STATS where TRUCK_ID=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_TRUCK_FIX_LOGS where TRUCK_ID=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_INSPECT_PLAN where TRUCK_ID=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_FANCE_TRUCK_R where TRUCK_ID=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_TRUCK_INST_USER where TRUCK_ID=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_TRUCK_INST_VIDEO where TRUCK_ID=?", vList);
			count += sqlHelper.updateBySql(
					"delete from T_TRUCK_INST where  TRUCK_ID=?", vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getVehicleWordParasList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车载列表。
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
	public ArrayList<TruckWorkParasPojo> getVehicleWordParasList(String where,
			String orderBy, List<Object> vList, long f, long t)
			throws Exception {
		// 翻页代码
		ArrayList<TruckWorkParasPojo> list = new ArrayList<TruckWorkParasPojo>();
		StringBuffer strSQL = new StringBuffer(
				this._getVehicleWordParasSelectSQL(where, orderBy));
		if (f >= 0 && t > 0) {
			strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneVehicleWordParasPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getVehicleWordParasCount
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
	public long getVehicleWordParasCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.eqp_inst) as TAB_COUNT");
		strSQL.append(" from V_VEHICLE_WORK_PARAS t");
		strSQL.append(" where t.eqp_inst is not null");
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

	public int insertVehicleData(PacketLocationReport onePojo,
			EquipmentGeometryPojo oneEqpGeo) throws Exception {
		int count = 0;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		// 写入数据
		StringBuffer strSQL = new StringBuffer("insert into T_VEHICLE_DATA (");
		strSQL.append("LOG_ID");
		strSQL.append(",EQP_INST");
		strSQL.append(",C_DATE");
		strSQL.append(",S_CDATE");
		strSQL.append(",OIL_LEVEL");
		strSQL.append(",EQP_SPEED");
		strSQL.append(",OIL_DIFF");
		strSQL.append(",OIL_VALUME");
		strSQL.append(") values (?,?,?,?,?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(BSGuid.getRandomGUID());
		vList.add(oneEqpGeo.getEqpInst().getInstId());
		vList.add(Timestamp.valueOf(oneEqpGeo.getSysDate()));
		vList.add(Timestamp.valueOf(oneEqpGeo.getCreateDate()));
		vList.add(String.valueOf(onePojo.getOilLevel()));
		vList.add(String.valueOf(onePojo.getSpeed()));
		vList.add(String.valueOf(onePojo.getOilDeff()));
		vList.add(decimalFormat.format(onePojo.getValume()));
		count += this.sqlHelper.updateBySql(strSQL.toString(), vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getOneTruckWorkDayLogsId
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个群组。
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
	 * @throws Exception
	 * 
	 */
	public TruckWorkDayLogsPojo getOneTruckWorkDayLogsId(String id)
			throws Exception {
		TruckWorkDayLogsPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(this
				._getTruckWorkDayLogsSelectSQL(" and t.LOG_ID=?", "")
				.toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = this._setOneTruckWorkDayLogsPojo(rs);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: updateTruckWorkDayLogs
	 * </p>
	 * <p>
	 * 方法功能描述: 修改员工日工作日志。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int updateTruckWorkDayLogs(TruckWorkDayLogsPojo onePojo)
			throws Exception {
		int count = 0;
		if (onePojo != null && onePojo.getTruck() != null
				&& !onePojo.getTruck().getId().equals("")) {
			StringBuffer strSQL = new StringBuffer();
			List<Object> vList = new ArrayList<Object>();
			onePojo.setId(onePojo.getTruck().getId() + "_"
					+ onePojo.getDate().substring(0, 10));
			TruckWorkDayLogsPojo oneOldPojo = this
					.getOneTruckWorkDayLogsId(onePojo.getId());
			if (oneOldPojo == null) {
				// 新增
				strSQL.append("insert into T_TRUCK_WORK_DAY_LOGS (LOG_ID,TRUCK_ID,LOG_DATE,LOG_STATE");
				strSQL.append(") values (?,?,?,?)");
				vList.clear();
				vList.add(onePojo.getTruck().getId() + "_"
						+ onePojo.getDate().substring(0, 10));
				vList.add(onePojo.getTruck().getId());
				vList.add(Timestamp.valueOf(onePojo.getDate()));
				vList.add(onePojo.getType());
				count += this.sqlHelper.updateBySql(strSQL.toString(), vList);
				oneOldPojo = onePojo;
			}
			vList.clear();
			strSQL = new StringBuffer();
			if (onePojo.getOpType() == 0) {
				// 围栏判断
				onePojo.setInDate(oneOldPojo.getInDate());
				onePojo.setOutDate(oneOldPojo.getOutDate());
				if (onePojo.getType() == 0) {
					// 在围栏外
					onePojo.setOutDate(onePojo.getDate());
					if (onePojo.getBjDate().equals("")) {
						onePojo.setBjDate(oneOldPojo.getBjDate());
					}
					if (onePojo.getBjDate().equals("")) {
						onePojo.setBjDate(onePojo.getDate());
					}
				} else {
					if (oneOldPojo.getInDate().equals("")) {
						// 进入围栏的时间距离0点秒数小于车载间隔，怎设为0点
						onePojo.setInDate(onePojo.getDate());
					}
				}
				vList.add(onePojo.getType());
				vList.add(Timestamp.valueOf(onePojo.getDate()));
				if (!onePojo.getInDate().equals("")) {
					vList.add(Timestamp.valueOf(onePojo.getInDate()));
				} else {
					vList.add(null);
				}
				if (!onePojo.getOutDate().equals("")) {
					vList.add(Timestamp.valueOf(onePojo.getOutDate()));
				} else {
					vList.add(null);
				}
				if (!onePojo.getBjDate().equals("")) {
					vList.add(Timestamp.valueOf(onePojo.getBjDate()));
				} else {
					vList.add(null);
				}
				// 油量
				onePojo.setOil(URLlImplBase.AllPrince(onePojo.getOil(),
						oneOldPojo.getOil()));
				vList.add(onePojo.getOil());
				// 里程
				vList.add(URLlImplBase.AllPrince(onePojo.getDistance(),
						oneOldPojo.getDistance()));
				vList.add(onePojo.getId());
				strSQL.append("update T_TRUCK_WORK_DAY_LOGS set LOG_STATE=?,LOG_DATE=?,IN_DATE=?,OUT_DATE=?,LOG_BJDATE=?,LOG_OIL=?,LOG_DISTANCE=? where LOG_ID=?");
				count += this.sqlHelper.updateBySql(strSQL.toString(), vList);
			} else {
				// 工作时长
				String todayT = "0";
				if (!onePojo.getWorkSDate().equals("")
						&& !onePojo.getWorkEDate().equals("")) {
					if (!(onePojo.getWorkSDate().substring(0, 10))
							.equals(onePojo.getDate().substring(0, 10))) {
						// 开始时间不在当天，当天工作时长为结束时间减去当日开机时间点
						if (!onePojo.getInDate().equals("")) {
							todayT = String.valueOf((this.bsDate
									.getDateMillCount(onePojo.getInDate(),
											onePojo.getWorkEDate())) / 1000);
						}
						onePojo.setWorkTime(todayT);
						// 昨天时长补全。
					} else {
						todayT = URLlImplBase.AllPrince(oneOldPojo
								.getWorkTime(), String.valueOf((this.bsDate
								.getDateMillCount(onePojo.getWorkSDate(),
										onePojo.getWorkEDate())) / 1000));
					}
					vList.clear();
					vList.add(todayT);
					vList.add(onePojo.getId());
					strSQL.append("update T_TRUCK_WORK_DAY_LOGS set WORK_TIME=? where LOG_ID=?");
					count += this.sqlHelper.updateBySql(strSQL.toString(),
							vList);
				}
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getTruckWordParasCount
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
	public long getTruckWordParasCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.eqp_inst) as TAB_COUNT");
		strSQL.append(" from V_TRUCK_WORK_PARAS t");
		strSQL.append(" where t.eqp_inst is not null");
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
	 * 方法名称: getUserWordParasList
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
	public ArrayList<TruckWorkParasPojo> getTruckWordParasList(String where,
			String orderBy, List<Object> vList, long f, long t)
			throws Exception {
		// 翻页代码
		ArrayList<TruckWorkParasPojo> list = new ArrayList<TruckWorkParasPojo>();
		StringBuffer strSQL = new StringBuffer(
				this._getTruckWordParasSelectSQL(where, orderBy));
		if (f >= 0 && t > 0) {
			strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneTruckWordParasPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getTruckDriverList
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
	public ArrayList<TruckDriverRPojo> getTruckDriverList(String where,
			String orderBy, List<Object> vList) throws Exception {
		ArrayList<TruckDriverRPojo> list = new ArrayList<TruckDriverRPojo>();
		ResultSet rs = this.sqlHelper.queryBySql(
				_getTruckDriverSelectSQL(where, orderBy).toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneTruckDriverPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getTruckDriverList
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
	public int addOneTruckDriver(TruckDriverRPojo onePojo) throws Exception {
		int count = this.deleteOneTruckDriver(onePojo.getTruck().getId(),
				onePojo.getUser().getInstId());
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getTruck().getId());
		vList.add(onePojo.getUser().getInstId());
		vList.add(0);
		count = this.sqlHelper
				.updateBySql(
						"insert into T_TRUCK_INST_USER (TRUCK_ID,USER_INSTID,R_TYPE) values (?,?,?)",
						vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getTruckDriverList
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
	public int deleteOneTruckDriver(String truckId, String userInstId)
			throws Exception {
		List<Object> vList = new ArrayList<Object>();
		vList.add(truckId);
		vList.add(userInstId);

		return this.sqlHelper
				.updateBySql(
						"delete from T_TRUCK_INST_USER where TRUCK_ID=? and USER_INSTID=?",
						vList);
	}

	/**
	 * <p>
	 * 方法名称: getDriverSchedulingList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到司机排班表定义列表。
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
	public ArrayList<DriverSchedulingPojo> getDriverSchedulingList(
			String where, List<Object> vList) throws Exception {
		ArrayList<DriverSchedulingPojo> list = new ArrayList<DriverSchedulingPojo>();
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(
				_getDriverSchedulingSelectSQL(where).toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneDriverSchedulingPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getOneTruckByPlateNum
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个物品实例。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public TruckWorkStatsPojo getOneTruckWorkStats(String truckId)
			throws Exception {
		TruckWorkStatsPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(truckId);
		ResultSet rs = this.sqlHelper.queryBySql(this
				._getTruckWorkStatsSelectSQL(" and t.TRUCK_ID=?", "")
				.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneTruckWorkStatsPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertTruckWorkStats
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
	public int insertTruckWorkStats(TruckWorkStatsPojo onePojo)
			throws Exception {
		if (onePojo.getStartDate().equals("")) {
			onePojo.setStartDate(this.bsDate.getThisDate(0, 0));
		}
		StringBuffer strSQL = new StringBuffer(
				"insert into T_TRUCK_WORK_STATS (");
		strSQL.append("TRUCK_ID");
		strSQL.append(",F_DATE");
		strSQL.append(",T_DATE");
		strSQL.append(",WORK_TIME");
		strSQL.append(",OIL_VALUME");
		strSQL.append(",OIL_DIFF");
		strSQL.append(",LOG_DISTANCE");
		strSQL.append(") values (?,?,?,?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getTruck().getId());
		vList.add(Timestamp.valueOf(onePojo.getStartDate()));
		vList.add(Timestamp.valueOf(onePojo.getStartDate()));
		vList.add(onePojo.getWorkTime());
		vList.add(onePojo.getOil());
		vList.add(onePojo.getOilDiff());
		vList.add(onePojo.getDistance());
		// 新增统计表
		return this.sqlHelper.updateBySql(strSQL.toString(), vList);
	}

	/**
	 * <p>
	 * 方法名称: updateTruckWorkStats
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑一个物品定义。
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
	public int updateTruckWorkStatsData(TruckWorkStatsPojo onePojo)
			throws Exception {
		int count = 0;
		// 得到当前的数据
		TruckWorkStatsPojo oneOldPojo = this.getOneTruckWorkStats(onePojo
				.getTruck().getId());
		if (oneOldPojo == null) {
			count += this.insertTruckWorkStats(onePojo);
		} else {
			oneOldPojo.setEndDate(onePojo.getEndDate());
			oneOldPojo.setDistance(URLlImplBase.AllPrince(
					oneOldPojo.getDistance(), onePojo.getDistance()));
			oneOldPojo.setOil(URLlImplBase.AllPrince(oneOldPojo.getOil(),
					onePojo.getOil()));
			oneOldPojo.setOilDiff(URLlImplBase.AllPrince(
					oneOldPojo.getOilDiff(), onePojo.getOilDiff()));
			StringBuffer strSQL = new StringBuffer(
					"update T_TRUCK_WORK_STATS set ");
			strSQL.append("T_DATE=?");
			strSQL.append(",OIL_VALUME=?");
			strSQL.append(",OIL_DIFF=?");
			strSQL.append(",LOG_DISTANCE=?");
			strSQL.append(" where TRUCK_ID=?");
			List<Object> vList = new ArrayList<Object>();
			vList.add(Timestamp.valueOf(onePojo.getEndDate()));
			vList.add(oneOldPojo.getOil());
			vList.add(oneOldPojo.getOilDiff());
			vList.add(oneOldPojo.getDistance());
			vList.add(oneOldPojo.getTruck().getId());
			count += this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateTruckWorkStatsWorkTime
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑一个物品定义。
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
	public int updateTruckWorkStatsWorkTime(TruckWorkStatsPojo onePojo)
			throws Exception {
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getTruck().getId());
		String oldT = (String) this.sqlHelper
				.queryObjectBySql(
						"select t.WORK_TIME from T_TRUCK_WORK_STATS t where t.TRUCK_ID=?",
						vList);
		if (oldT == null || oldT.trim().equals("")) {
			oldT = onePojo.getWorkTime();
		} else {
			oldT = URLlImplBase.AllPrince(oldT, onePojo.getWorkTime());
		}
		StringBuffer strSQL = new StringBuffer("update T_TRUCK_WORK_STATS set ");
		strSQL.append("T_DATE=?");
		strSQL.append(",WORK_TIME=?");
		strSQL.append(" where TRUCK_ID=?");
		vList.clear();
		vList.add(Timestamp.valueOf(onePojo.getEndDate()));
		vList.add(oldT);
		vList.add(onePojo.getTruck().getId());
		return this.sqlHelper.updateBySql(strSQL.toString(), vList);
	}

	/**
	 * <p>
	 * 方法名称: getTruckVideoList
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
	public ArrayList<TruckVideoPojo> getTruckVideoList(String where,
			List<Object> vList) throws Exception {
		ArrayList<TruckVideoPojo> list = new ArrayList<TruckVideoPojo>();
		ResultSet rs = this.sqlHelper.queryBySql(_getTruckVideoSelectSQL(where)
				.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneTruckVideoPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getOneTruckVideoById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个物品定义。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public TruckVideoPojo getOneTruckVideoById(String id) throws Exception {
		TruckVideoPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		ResultSet rs = this.sqlHelper
				.queryBySql(this._getTruckVideoSelectSQL(" and t.V_ID=?")
						.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneTruckVideoPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertTruckVideo
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
	public int insertTruckVideo(TruckVideoPojo onePojo) throws Exception {
		if (onePojo.getId() == null || onePojo.getId().trim().equals("")) {
			onePojo.setId(BSGuid.getRandomGUID());
		}
		// 根据车辆得到设备
		BIEquipment eqpBI = new BIEquipment(null, null);
		EquipmentInstPojo oneEqp = eqpBI.getOnevehicleInstByTruck(sqlHelper,
				onePojo.getTruck().getId());
		if (oneEqp != null) {
			String[] code = oneEqp.getWyCode().split("\\|");
			if (code.length == 3) {
				onePojo.setEqpNo(code[2]);
			}
		}
		StringBuffer strSQL = new StringBuffer(
				"insert into T_TRUCK_INST_VIDEO (");
		strSQL.append("V_ID");
		strSQL.append(",TRUCK_ID");
		strSQL.append(",V_IP");
		strSQL.append(",V_PORT");
		strSQL.append(",V_USER");
		strSQL.append(",V_PASSWORD");
		strSQL.append(",V_URL");
		strSQL.append(",V_EQPNO");
		strSQL.append(") values (?,?,?,?,?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		vList.add(onePojo.getTruck().getId());
		vList.add(onePojo.getIp());
		vList.add(onePojo.getPort());
		vList.add(onePojo.getUser());
		vList.add(onePojo.getPassword());
		vList.add(onePojo.getUrl());
		vList.add(onePojo.getEqpNo());
		return this.sqlHelper.updateBySql(strSQL.toString(), vList);
	}

	/**
	 * <p>
	 * 方法名称: updateTruckVideo
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个物品定义。
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
	public int updateTruckVideo(TruckVideoPojo onePojo) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		if ((onePojo.getId() == null || onePojo.getId().trim().equals(""))
				|| this.sqlHelper
						.queryIntBySql(
								"select count(V_ID) from T_TRUCK_INST_VIDEO where V_ID=?",
								vList) <= 0) {
			// 新增
			count += this.insertTruckVideo(onePojo);
		} else {
			// 根据车辆得到设备
			BIEquipment eqpBI = new BIEquipment(null, null);
			EquipmentInstPojo oneEqp = eqpBI.getOnevehicleInstByTruck(
					sqlHelper, onePojo.getTruck().getId());
			if (oneEqp != null) {
				String[] code = oneEqp.getWyCode().split("\\|");
				if (code.length == 3) {
					onePojo.setEqpNo(code[2]);
				}
			}
			StringBuffer strSQL = new StringBuffer(
					"update T_TRUCK_INST_VIDEO set ");
			strSQL.append("V_IP=?");
			strSQL.append(",V_PORT=?");
			strSQL.append(",V_USER=?");
			strSQL.append(",V_PASSWORD=?");
			strSQL.append(",V_URL=?");
			strSQL.append(",V_EQPNO=?");
			strSQL.append(" where V_ID=?");
			vList.clear();
			vList.add(onePojo.getIp());
			vList.add(onePojo.getPort());
			vList.add(onePojo.getUser());
			vList.add(onePojo.getPassword());
			vList.add(onePojo.getUrl());
			vList.add(onePojo.getEqpNo());
			vList.add(onePojo.getId());
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteTruckVideo
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
	public int deleteTruckVideo(String id) throws Exception {
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		return this.sqlHelper.updateBySql(
				"delete from T_TRUCK_INST_VIDEO where V_ID=?", vList);
	}

	/**
	 * <p>
	 * 方法名称: deleteTruckVideo
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
	public int deleteTruckVideoByTruck(String id) throws Exception {
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		return this.sqlHelper.updateBySql(
				"delete from T_TRUCK_INST_VIDEO where TRUCK_ID=?", vList);
	}

	/**
	 * <p>
	 * 方法名称: getTruckFixLogsList
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
	public ArrayList<TruckFixLogsPojo> getTruckFixLogsList(String where,
			String orderBy, long f, long t, List<Object> vList)
			throws Exception {
		ArrayList<TruckFixLogsPojo> list = new ArrayList<TruckFixLogsPojo>();
		StringBuffer strSQL = _getTruckFixLogsSelectSQL(where, orderBy);
		if (f >= 0 && t > 0) {
			strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneTruckFixLogsPojo(rs, false));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getTruckFixLogsCount
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
	public long getTruckFixLogsCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.LOG_ID) as OBJ_COUNT");
		strSQL.append(" from T_TRUCK_FIX_LOGS t,T_TRUCK_INST t1,T_USER tu");
		strSQL.append(" where t.TRUCK_ID=t1.TRUCK_ID");
		strSQL.append(" and t.LOG_USER=tu.USER_INSTID");

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
	 * 方法名称: getOneTruckFixLogsById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个维修日志。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public TruckFixLogsPojo getOneTruckFixLogsById(String id) throws Exception {
		TruckFixLogsPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		ResultSet rs = this.sqlHelper.queryBySql(this
				._getTruckFixLogsSelectSQL(" and t.LOG_ID=?", "").toString(),
				vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneTruckFixLogsPojo(rs, true));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertTruckFixLogs
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
	public int insertTruckFixLogs(TruckFixLogsPojo onePojo) throws Exception {
		if (onePojo.getId() == null || onePojo.getId().trim().equals("")) {
			onePojo.setId(BSGuid.getRandomGUID());
		}
		StringBuffer strSQL = new StringBuffer("insert into T_TRUCK_FIX_LOGS (");
		strSQL.append("LOG_ID");
		strSQL.append(",TRUCK_ID");
		strSQL.append(",LOG_NAME");
		strSQL.append(",C_DATE");
		strSQL.append(",LOG_TYPE");
		strSQL.append(",LOG_USER");
		strSQL.append(",LOG_CONTENT");
		strSQL.append(",LOG_MONEY");
		strSQL.append(") values (?,?,?,?,?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		vList.add(onePojo.getTruck().getId());
		vList.add(onePojo.getName());
		vList.add(Timestamp.valueOf(onePojo.getLogDate()));
		vList.add(onePojo.getType().getId());
		vList.add(onePojo.getUser().getInstId());
		vList.add(onePojo.getContent());
		vList.add(onePojo.getMoney());
		return this.sqlHelper.updateBySql(strSQL.toString(), vList);
	}

	/**
	 * <p>
	 * 方法名称: updateTruckFixLogs
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个物品定义。
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
	public int updateTruckFixLogs(TruckFixLogsPojo onePojo) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		if ((onePojo.getId() == null || onePojo.getId().trim().equals(""))
				|| this.sqlHelper
						.queryIntBySql(
								"select count(LOG_ID) from T_TRUCK_FIX_LOGS where LOG_ID=?",
								vList) <= 0) {
			// 新增
			count += this.insertTruckFixLogs(onePojo);
		} else {
			StringBuffer strSQL = new StringBuffer(
					"update T_TRUCK_FIX_LOGS set ");
			strSQL.append("TRUCK_ID=?");
			strSQL.append(",LOG_NAME=?");
			strSQL.append(",LOG_TYPE=?");
			strSQL.append(",LOG_USER=?");
			strSQL.append(",LOG_CONTENT=?");
			strSQL.append(",LOG_MONEY=?");
			strSQL.append(" where LOG_ID=?");
			vList.clear();
			vList.add(onePojo.getTruck().getId());
			vList.add(onePojo.getName());
			vList.add(onePojo.getType().getId());
			vList.add(onePojo.getUser().getInstId());
			vList.add(onePojo.getContent());
			vList.add(onePojo.getMoney());
			vList.add(onePojo.getId());
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateOneTruckState
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
	public int updateOneVehicleState(String instId, int state) throws Exception {
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
	 * 方法名称: deleteOneVehicle
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
	public int deleteOneVehicle(String instId) throws Exception {
		int count = 0;
		if (instId != null && !instId.trim().equals("")) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(instId);
			count += sqlHelper.updateBySql(
					"delete from T_VEHICLE_DATA where EQP_INST=?", vList);
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
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteOneVehicle
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
	public JSONObject getVehicleNewestByInst(String instId) throws Exception {
		JSONObject onePojo = new JSONObject();
		if (instId != null && !instId.trim().equals("")) {
			EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
			EquipmentGeometryPojo lastGeo = quipmentDB
					.getLastEquipmentGeoPojo(instId);
			if (lastGeo != null) {
				onePojo.put("instid", instId);
				onePojo.put("longitude", lastGeo.getLongitude());
				onePojo.put("latitude", lastGeo.getLatitude());// 纬度
				onePojo.put("sysDate", lastGeo.getSysDate());// 时间
				onePojo.put("createDate", lastGeo.getCreateDate());// 时间
				onePojo.put("dir", lastGeo.getDir());// 方向
				onePojo.put("speed", lastGeo.getSpeed());
				onePojo.put("fdId", lastGeo.getFdId());// 分段ID
				onePojo.put("lastStopDate", lastGeo.getLastStopDate());// 上次停车时间
				onePojo.put("fanceFlg", lastGeo.getFanceFlg());
			}
			//
			TruckWorkParasPojo oneV = this.getLasVehiclePojo(instId);
			if (oneV != null) {
				onePojo.put("oil", oneV.getOil());
				onePojo.put("oildiff", oneV.getOilDiff());
				onePojo.put("speed", oneV.getSpeed());
			}
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getLastEquipmentGeoPojo
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个物品实例。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public TruckWorkParasPojo getLasVehiclePojo(String instId) throws Exception {
		TruckWorkParasPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(instId);
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.LOG_ID");
		strSQL.append(",t.EQP_INST");
		strSQL.append(",t.C_DATE");
		strSQL.append(",t.S_CDATE");
		strSQL.append(",t.OIL_LEVEL");
		strSQL.append(",t.EQP_SPEED");
		strSQL.append(",t.OIL_DIFF");
		strSQL.append(",t.OIL_VALUME");
		strSQL.append(",t.EQP_WEIGHT");
		strSQL.append(" from T_VEHICLE_DATA t");
		strSQL.append(" where t.EQP_INST=? and t.C_DATE is not null");
		strSQL.append(" order by t.C_DATE desc,t.S_CDATE desc");
		strSQL.append(" LIMIT 1");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = new TruckWorkParasPojo();
				onePojo.getEqpInst().setInstId(rs.getString("EQP_INST"));
				onePojo.setOil(rs.getString("OIL_LEVEL"));
				onePojo.setOilDiff(rs.getString("OIL_DIFF"));
				onePojo.setSpeed(rs.getString("EQP_SPEED"));
				onePojo.setGeoDate(rs.getString("S_CDATE"));
				onePojo.setUpdateDate(rs.getString("C_DATE"));
			}
			rs.close();
		}

		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getTruckDefSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.TD_ID");
		strSQL.append(",t.TD_NAME");
		strSQL.append(",t.TD_NO");
		strSQL.append(",t.TD_COMPANY");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.TD_SALEDATE")
				+ " as TD_SALEDATE");
		strSQL.append(",t.TD_LINKMAN");
		strSQL.append(",t.TD_OILMJ");
		strSQL.append(",t.TD_OILDE");
		strSQL.append(",t.TD_BRAND");
		strSQL.append(",t.TD_WORKTIME");
		strSQL.append(",(select count(v1.TRUCK_ID) from T_TRUCK_INST v1 where v1.TD_ID=t.TD_ID) as INST_COUNT");// 实例数量
		strSQL.append(" from T_TRUCK_DEF t");
		strSQL.append(" where t.TD_ID is not null ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.TD_NAME");
		}
		return strSQL;
	}

	// 物品定义对象
	private TruckDefinePojo _setOneTruckDefinePojo(CachedRowSet rs)
			throws Exception {
		TruckDefinePojo onePojo = new TruckDefinePojo();
		onePojo.setId(rs.getString("TD_ID"));
		onePojo.setName(rs.getString("TD_NAME"));
		if (rs.getString("TD_NO") != null) {
			onePojo.setNo(rs.getString("TD_NO"));
		}
		if (rs.getString("TD_NO") != null) {
			onePojo.setNo(rs.getString("TD_NO"));
		}
		if (rs.getString("TD_COMPANY") != null) {
			onePojo.setCompany(rs.getString("TD_COMPANY"));
		}
		if (rs.getString("TD_SALEDATE") != null) {
			onePojo.setSaleDate(rs.getString("TD_SALEDATE"));
		}
		if (rs.getString("TD_LINKMAN") != null) {
			onePojo.setLinkMan(rs.getString("TD_LINKMAN"));
		}
		if (rs.getString("TD_OILMJ") != null) {
			onePojo.setOilMJ(rs.getString("TD_OILMJ"));
		}
		if (rs.getString("TD_OILDE") != null) {
			onePojo.setOilDe(rs.getString("TD_OILDE"));
		}
		if (rs.getString("TD_BRAND") != null) {
			onePojo.setBrand(rs.getString("TD_BRAND"));
		}
		onePojo.setInstCount(rs.getLong("INST_COUNT"));
		onePojo.setWorkTime(rs.getInt("TD_WORKTIME"));

		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getTruckSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.TRUCK_ID");
		strSQL.append(",t.TRUCK_NAME");
		strSQL.append(",t.TRUCK_INNAME");
		strSQL.append(",t.TRUCK_NO");
		strSQL.append(",t.TRUCK_CJNO");
		strSQL.append(",t.TD_ID");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.TRUCK_IDATE")
				+ " as TRUCK_IDATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.TRUCK_CDATE")
				+ " as TRUCK_CDATE");
		strSQL.append(",t.A_ID");
		strSQL.append(",t.PLATE_NUM");
		strSQL.append(",t.PLATE_COLOR");
		strSQL.append(",t.TRUCK_STATE");
		strSQL.append(",t.TRUCK_UNO");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.TRUCK_UDATE")
				+ " as TRUCK_UDATE");
		// 管理用户
		strSQL.append(",t.TRUCK_MANGUSER");
		strSQL.append(",tu.USER_ID");
		strSQL.append(",tu.USER_NAME");
		strSQL.append(",tu.MPHONE");
		// 机构
		strSQL.append(",t.ORG_ID");
		strSQL.append(",(select count(v1.V_ID) from T_TRUCK_INST_VIDEO v1 where v1.TRUCK_ID=t.TRUCK_ID) as VIDEO_COUNT");// 实例数量
		strSQL.append(" from T_TRUCK_INST t left outer join T_USER tu on t.TRUCK_MANGUSER = tu.USER_INSTID");
		strSQL.append(" where t.TRUCK_ID is not null ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.TRUCK_IDATE desc");
		}
		return strSQL;
	}

	// 物品定义对象
	private TruckPojo _setOneTruckPojo(ResultSet rs) throws Exception {
		TruckPojo onePojo = new TruckPojo();
		onePojo.setId(rs.getString("TRUCK_ID"));
		onePojo.setName(rs.getString("TRUCK_NAME"));
		onePojo.setState(rs.getInt("TRUCK_STATE"));
		if (rs.getString("TRUCK_INNAME") != null) {
			onePojo.setInName(rs.getString("TRUCK_INNAME"));
		}
		if (rs.getString("TRUCK_NO") != null) {
			onePojo.setNo(rs.getString("TRUCK_NO"));
		}
		if (rs.getString("TRUCK_CJNO") != null) {
			onePojo.setCjNo(rs.getString("TRUCK_CJNO"));
		}
		onePojo.setDefine(this.truckBI.getTruckDefByRedis(rs.getString("TD_ID")));
		if (rs.getString("TRUCK_IDATE") != null) {
			onePojo.setInDate(rs.getString("TRUCK_IDATE"));
		}
		if (rs.getString("TRUCK_CDATE") != null) {
			onePojo.setProDate(rs.getString("TRUCK_CDATE"));
		}
		onePojo.setArea(this.areaBI.getAreaByRedis(rs.getString("A_ID")));
		if (rs.getString("PLATE_NUM") != null) {
			onePojo.setPlateNum(rs.getString("PLATE_NUM"));
		}
		if (rs.getString("PLATE_COLOR") != null) {
			onePojo.setPlateColor(rs.getInt("PLATE_COLOR"));
		}
		if (rs.getString("TRUCK_UNO") != null) {
			onePojo.setUpNo(rs.getString("TRUCK_UNO"));
		}
		if (rs.getString("TRUCK_UDATE") != null) {
			onePojo.setUpDate(rs.getString("TRUCK_UDATE"));
		}
		onePojo.setVideoNum(rs.getInt("VIDEO_COUNT"));

		// 管理员
		if (rs.getString("TRUCK_MANGUSER") != null) {
			onePojo.getMangUser().setInstId(rs.getString("TRUCK_MANGUSER"));
			if (rs.getString("USER_ID") != null) {
				onePojo.getMangUser().setId(rs.getString("USER_ID"));
			}
			if (rs.getString("USER_NAME") != null) {
				onePojo.getMangUser().setName(rs.getString("USER_NAME"));
			}
			if (rs.getString("MPHONE") != null) {
				onePojo.getMangUser().setmPhone(rs.getString("MPHONE"));
			}
		}

		// 机构
		if (rs.getString("ORG_ID") != null
				&& !rs.getString("ORG_ID").equals("")) {
			onePojo.setOrg(this.userBI.getGroupByRedis(rs.getString("ORG_ID")));
		}

		return onePojo;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getTruckWorkDayLogsSelectSQL(String where,
			String orderBy) throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.LOG_ID");
		strSQL.append(",t.LOG_STATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.LOG_DATE")
				+ " as LOG_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.IN_DATE")
				+ " as IN_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.OUT_DATE")
				+ " as OUT_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.LOG_BJDATE")
				+ " as LOG_BJDATE");
		strSQL.append(",t.LOG_DISTANCE");
		strSQL.append(",t.LOG_OIL");
		strSQL.append(",t.WORK_TIME");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.S_DATE")
				+ " as S_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.E_DATE")
				+ " as E_DATE");
		// 创建者
		strSQL.append(",t.TRUCK_ID");
		strSQL.append(",t1.TRUCK_NAME");
		strSQL.append(",t1.TRUCK_NO");
		strSQL.append(",t1.PLATE_NUM");
		strSQL.append(",t1.TD_ID");
		// 管理用户
		strSQL.append(",t1.TRUCK_MANGUSER");
		strSQL.append(",tu.USER_ID");
		strSQL.append(",tu.USER_NAME");
		strSQL.append(",tu.MPHONE");
		// 机构
		strSQL.append(",t1.ORG_ID");
		strSQL.append(" from T_TRUCK_WORK_DAY_LOGS t,T_TRUCK_INST t1");
		strSQL.append(" left outer join T_USER tu on t1.TRUCK_MANGUSER = tu.USER_INSTID");
		strSQL.append(" where t.TRUCK_ID=t1.TRUCK_ID");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private TruckWorkDayLogsPojo _setOneTruckWorkDayLogsPojo(CachedRowSet rs)
			throws Exception {
		TruckWorkDayLogsPojo onePojo = new TruckWorkDayLogsPojo();
		// 设置群组信息
		onePojo.setId(rs.getString("LOG_ID"));
		onePojo.setType(rs.getInt("LOG_STATE"));
		onePojo.setDate(rs.getString("LOG_DATE"));
		onePojo.setOil(rs.getString("LOG_OIL"));
		onePojo.setDistance(rs.getString("LOG_DISTANCE"));
		if (rs.getString("WORK_TIME") != null) {
			onePojo.setWorkTime(rs.getString("WORK_TIME"));
		}
		if (rs.getString("IN_DATE") != null) {
			onePojo.setInDate(rs.getString("IN_DATE"));
		}
		if (rs.getString("OUT_DATE") != null) {
			onePojo.setOutDate(rs.getString("OUT_DATE"));
		}
		if (rs.getString("LOG_BJDATE") != null) {
			onePojo.setBjDate(rs.getString("LOG_BJDATE"));
		}
		if (rs.getString("IN_DATE") != null) {
			onePojo.setWorkSDate(rs.getString("IN_DATE"));
		}
		if (rs.getString("OUT_DATE") != null) {
			onePojo.setWorkSDate(rs.getString("OUT_DATE"));
		}

		// 车
		onePojo.getTruck().setId(rs.getString("TRUCK_ID"));
		onePojo.getTruck().setNo(rs.getString("TRUCK_NO"));
		onePojo.getTruck().setName(rs.getString("TRUCK_NAME"));
		if (rs.getString("PLATE_NUM") != null) {
			onePojo.getTruck().setPlateNum(rs.getString("PLATE_NUM"));
		}
		onePojo.getTruck().setDefine(
				this.truckBI.getTruckDefByRedis(rs.getString("TD_ID")));
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
		return onePojo;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getTruckWordParasSelectSQL(String where,
			String orderBy) throws Exception {
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.eqp_inst");
		strSQL.append(",t.eqp_wycode");
		strSQL.append(",t.eqp_qr");
		strSQL.append(",t.eqp_online");
		strSQL.append(",t.eqp_muser");
		strSQL.append(",t.truck_id");
		strSQL.append(",t.org_id");
		strSQL.append(",t.td_id");
		strSQL.append(",t.truck_name");
		strSQL.append(",t.truck_no");
		strSQL.append(",t.plate_num");
		strSQL.append(" from V_TRUCK_WORK_PARAS t");
		strSQL.append(" where t.eqp_inst is not null");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by" + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private TruckWorkParasPojo _setOneTruckWordParasPojo(ResultSet rs)
			throws Exception {
		TruckWorkParasPojo onePojo = new TruckWorkParasPojo();
		onePojo.getEqpInst().setInstId(rs.getString("eqp_inst"));
		onePojo.getEqpInst().setWyCode(rs.getString("eqp_wycode"));
		onePojo.getEqpInst().setQrCode(rs.getString("eqp_qr"));
		onePojo.getEqpInst().setOnlineState(rs.getInt("eqp_online"));
		onePojo.getEqpInst().getTruck().setId(rs.getString("truck_id"));
		onePojo.getEqpInst().getTruck().setNo(rs.getString("truck_no"));
		onePojo.getEqpInst().getTruck().setName(rs.getString("truck_name"));
		onePojo.getEqpInst().getTruck().setPlateNum(rs.getString("plate_num"));
		if (rs.getString("org_id") != null
				&& !rs.getString("org_id").equals("")) {
			onePojo.getEqpInst()
					.getTruck()
					.setOrg(this.userBI.getGroupByRedis(rs.getString("org_id")));
		}
		if (rs.getString("TD_ID") != null) {
			onePojo.getEqpInst()
					.getTruck()
					.setDefine(
							this.truckBI.getTruckDefByRedis(rs
									.getString("TD_ID")));
		}
		JSONObject oneNDate = (new BITruck(null, null))
				.getVehicleNewestByRedis(onePojo.getEqpInst().getInstId());
		if (oneNDate != null) {
			if (oneNDate.containsKey("oil")) {
				onePojo.setOil(oneNDate.getString("oil"));
			}
			if (oneNDate.containsKey("speed")) {
				onePojo.setSpeed(oneNDate.getString("speed"));
			}
			if (oneNDate.containsKey("oildiff")) {
				onePojo.setOilDiff(oneNDate.getString("oildiff"));
			}
			if (oneNDate.containsKey("createDate")) {
				onePojo.setOilDate(oneNDate.getString("createDate"));
			}
			if (oneNDate.containsKey("longitude")) {
				onePojo.setLongitude(oneNDate.getString("longitude"));
			}
			if (oneNDate.containsKey("latitude")) {
				onePojo.setLatitude(oneNDate.getString("latitude"));
			}
			if (oneNDate.containsKey("createDate")) {
				onePojo.setGeoDate(oneNDate.getString("createDate"));
			}
			if (oneNDate.containsKey("fanceFlg")) {
				onePojo.setFanceFlg(oneNDate.getInt("fanceFlg"));
			}
		}
		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getTruckDriverSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.TRUCK_ID");
		strSQL.append(",t.USER_INSTID");
		strSQL.append(",t.R_TYPE");
		strSQL.append(",t1.USER_ID");
		strSQL.append(",t1.USER_NAME");
		strSQL.append(",t1.MPHONE");
		strSQL.append(" from T_TRUCK_INST_USER t,T_USER t1");
		strSQL.append(" where t.USER_INSTID=t1.USER_INSTID ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t1.USER_NAME");
		}
		return strSQL;
	}

	// 物品定义对象
	private TruckDriverRPojo _setOneTruckDriverPojo(ResultSet rs)
			throws Exception {
		TruckDriverRPojo onePojo = new TruckDriverRPojo();
		onePojo.setType(rs.getInt("R_TYPE"));
		onePojo.getTruck().setId(rs.getString("TRUCK_ID"));
		onePojo.getUser().setId(rs.getString("USER_ID"));
		onePojo.getUser().setInstId(rs.getString("USER_INSTID"));
		onePojo.getUser().setName(rs.getString("USER_NAME"));
		if (rs.getString("MPHONE") != null) {
			onePojo.getUser().setmPhone(rs.getString("MPHONE"));
		}
		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getDriverSchedulingSelectSQL(String where)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.S_ID");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.F_DATE")
				+ " as F_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.T_DATE")
				+ " as T_DATE");
		// 司机
		strSQL.append(",t1.USER_INSTID");
		strSQL.append(",t1.USER_ID");
		strSQL.append(",t1.USER_NAME");
		strSQL.append(",t1.MPHONE");
		strSQL.append(",t1.ORG_ID");
		// 车辆
		strSQL.append(",t2.TRUCK_ID");
		strSQL.append(",t2.TRUCK_NAME");
		strSQL.append(",t2.TD_ID");
		strSQL.append(",t2.PLATE_NUM");
		strSQL.append(" from T_DRIVER_SCHEDULING t,T_USER t1,T_TRUCK_INST t2");
		strSQL.append(" where t.USER_INSTID=t1.USER_INSTID ");
		strSQL.append(" and t.TRUCK_ID=t2.TRUCK_ID ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		strSQL.append(" order by t.F_DATE");
		return strSQL;
	}

	// 物品定义对象
	private DriverSchedulingPojo _setOneDriverSchedulingPojo(ResultSet rs)
			throws Exception {
		DriverSchedulingPojo onePojo = new DriverSchedulingPojo();
		onePojo.setId(rs.getString("S_ID"));
		onePojo.setStartDate(rs.getString("F_DATE"));
		onePojo.setEndDate(rs.getString("T_DATE"));
		// 司机
		onePojo.getUser().setInstId(rs.getString("USER_INSTID"));
		onePojo.getUser().setId(rs.getString("USER_ID"));
		onePojo.getUser().setName(rs.getString("USER_NAME"));
		if (rs.getString("MPHONE") != null) {
			onePojo.getUser().setmPhone(rs.getString("MPHONE"));
		}
		if (rs.getString("ORG_ID") != null
				&& !rs.getString("ORG_ID").equals("")) {
			onePojo.getUser().setOrg(
					this.userBI.getGroupByRedis(rs.getString("ORG_ID")));
		}
		// 车辆
		onePojo.getTruck().setId(rs.getString("TRUCK_ID"));
		onePojo.getTruck().setName(rs.getString("TRUCK_NAME"));
		onePojo.getTruck().setDefine(
				this.truckBI.getTruckDefByRedis(rs.getString("TD_ID")));
		if (rs.getString("PLATE_NUM") != null) {
			onePojo.getTruck().setPlateNum(rs.getString("PLATE_NUM"));
		}
		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getTruckWorkStatsSelectSQL(String where,
			String orderBy) throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.TRUCK_ID");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.F_DATE")
				+ " as F_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.T_DATE")
				+ " as T_DATE");
		//
		strSQL.append(",t.WORK_TIME");
		strSQL.append(",t.OIL_VALUME");
		strSQL.append(",t.OIL_DIFF");
		strSQL.append(",t.LOG_DISTANCE");
		// 车辆
		strSQL.append(",t1.TRUCK_NAME");
		strSQL.append(",t1.TRUCK_NO");
		strSQL.append(",t1.PLATE_NUM");
		strSQL.append(",t1.TD_ID");
		strSQL.append(" from T_TRUCK_WORK_STATS t,T_TRUCK_INST t1");
		strSQL.append(" where t.TRUCK_ID=t1.TRUCK_ID");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t1.TRUCK_NAME");
		}
		return strSQL;
	}

	// 物品定义对象
	private TruckWorkStatsPojo _setOneTruckWorkStatsPojo(ResultSet rs)
			throws Exception {
		TruckWorkStatsPojo onePojo = new TruckWorkStatsPojo();
		onePojo.setStartDate(rs.getString("F_DATE"));
		onePojo.setEndDate(rs.getString("T_DATE"));
		onePojo.setWorkTime(rs.getString("T_DATE"));
		onePojo.setOil(rs.getString("OIL_VALUME"));
		onePojo.setOilDiff(rs.getString("OIL_DIFF"));
		onePojo.setDistance(rs.getString("LOG_DISTANCE"));
		//
		onePojo.getTruck().setId(rs.getString("TRUCK_ID"));
		onePojo.getTruck().setName(rs.getString("TRUCK_NAME"));
		onePojo.getTruck().setNo(rs.getString("TRUCK_NO"));
		if (rs.getString("PLATE_NUM") != null) {
			onePojo.getTruck().setPlateNum(rs.getString("PLATE_NUM"));
		}
		onePojo.getTruck().setDefine(
				this.truckBI.getTruckDefByRedis(rs.getString("TD_ID")));
		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getTruckVideoSelectSQL(String where) throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.V_ID");
		strSQL.append(",t.TRUCK_ID");
		strSQL.append(",t.V_IP");
		strSQL.append(",t.V_PORT");
		strSQL.append(",t.V_USER");
		strSQL.append(",t.V_PASSWORD");
		strSQL.append(",t.V_URL");
		strSQL.append(",t.V_EQPNO");
		// 车辆
		strSQL.append(",t1.TRUCK_NAME");
		strSQL.append(",t1.TRUCK_NO");
		strSQL.append(",t1.PLATE_NUM");
		strSQL.append(",t1.TD_ID");
		strSQL.append(" from T_TRUCK_INST_VIDEO t,T_TRUCK_INST t1");
		strSQL.append(" where t.TRUCK_ID=t1.TRUCK_ID");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		strSQL.append(" order by t.V_IP");
		return strSQL;
	}

	// 物品定义对象
	private TruckVideoPojo _setOneTruckVideoPojo(ResultSet rs) throws Exception {
		TruckVideoPojo onePojo = new TruckVideoPojo();
		onePojo.setId(rs.getString("V_ID"));
		if (rs.getString("V_IP") != null) {
			onePojo.setIp(rs.getString("V_IP"));
		}
		if (rs.getString("V_PORT") != null) {
			onePojo.setPort(rs.getInt("V_PORT"));
		}
		if (rs.getString("V_USER") != null) {
			onePojo.setUser(rs.getString("V_USER"));
		}
		if (rs.getString("V_PASSWORD") != null) {
			onePojo.setPassword(rs.getString("V_PASSWORD"));
		}
		if (rs.getString("V_URL") != null) {
			onePojo.setUrl(rs.getString("V_URL"));
		}
		if (rs.getString("V_EQPNO") != null) {
			onePojo.setEqpNo(rs.getString("V_EQPNO"));
		}

		//
		onePojo.getTruck().setId(rs.getString("TRUCK_ID"));
		onePojo.getTruck().setName(rs.getString("TRUCK_NAME"));
		onePojo.getTruck().setNo(rs.getString("TRUCK_NO"));
		if (rs.getString("PLATE_NUM") != null) {
			onePojo.getTruck().setPlateNum(rs.getString("PLATE_NUM"));
		}
		onePojo.getTruck().setDefine(
				this.truckBI.getTruckDefByRedis(rs.getString("TD_ID")));

		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getTruckFixLogsSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.LOG_ID");
		strSQL.append(",t.LOG_NAME");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.C_DATE")
				+ " as C_DATE");
		strSQL.append(",t.LOG_TYPE");
		strSQL.append(",t.LOG_CONTENT");
		strSQL.append(",t.LOG_MONEY");
		// 车辆
		strSQL.append(",t.TRUCK_ID");
		strSQL.append(",t1.TRUCK_NAME");
		strSQL.append(",t1.TRUCK_NO");
		strSQL.append(",t1.PLATE_NUM");
		strSQL.append(",t1.TD_ID");
		// 操作人
		strSQL.append(",t.LOG_USER");
		strSQL.append(",tu.USER_ID");
		strSQL.append(",tu.USER_NAME");
		strSQL.append(",tu.MPHONE");
		strSQL.append(",tu.ORG_ID");
		strSQL.append(" from T_TRUCK_FIX_LOGS t,T_TRUCK_INST t1,T_USER tu");
		strSQL.append(" where t.TRUCK_ID=t1.TRUCK_ID");
		strSQL.append(" and t.LOG_USER=tu.USER_INSTID");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.C_DATE desc");
		}
		return strSQL;
	}

	// 物品定义对象
	private TruckFixLogsPojo _setOneTruckFixLogsPojo(ResultSet rs, boolean isAll)
			throws Exception {
		TruckFixLogsPojo onePojo = new TruckFixLogsPojo();
		onePojo.setId(rs.getString("LOG_ID"));
		onePojo.setName(rs.getString("LOG_NAME"));
		onePojo.setLogDate(rs.getString("C_DATE"));
		onePojo.setType((new BIDic(null)).getDicItemByRedis(rs
				.getString("LOG_TYPE")));
		if (rs.getString("LOG_CONTENT") != null) {
			onePojo.setContent(rs.getString("LOG_CONTENT"));
		}
		if (rs.getString("LOG_MONEY") != null) {
			onePojo.setMoney(rs.getString("LOG_MONEY"));
		}
		// 操作员
		onePojo.getUser().setInstId(rs.getString("LOG_USER"));
		onePojo.getUser().setId(rs.getString("USER_ID"));
		onePojo.getUser().setName(rs.getString("USER_NAME"));
		if (rs.getString("MPHONE") != null) {
			onePojo.getUser().setmPhone(rs.getString("MPHONE"));
		}
		onePojo.getUser().setOrg(
				this.userBI.getGroupByRedis(rs.getString("ORG_ID")));

		//
		onePojo.getTruck().setId(rs.getString("TRUCK_ID"));
		onePojo.getTruck().setName(rs.getString("TRUCK_NAME"));
		onePojo.getTruck().setNo(rs.getString("TRUCK_NO"));
		if (rs.getString("PLATE_NUM") != null) {
			onePojo.getTruck().setPlateNum(rs.getString("PLATE_NUM"));
		}
		onePojo.getTruck().setDefine(
				this.truckBI.getTruckDefByRedis(rs.getString("TD_ID")));
		// 文件
		// 文件
		if (isAll) {
			JSONObject _paras = new JSONObject();
			_paras.put("bid", onePojo.getId() + "_FILES");
			onePojo.setFiles((new BIFile(null, null)).getFileList(_paras, 0,
					100));
		}
		return onePojo;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getVehicleWordParasSelectSQL(String where,
			String orderBy) throws Exception {
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.eqp_inst");
		strSQL.append(",t.eqp_name");
		strSQL.append(",t.eqp_wycode");
		strSQL.append(",t.eqp_qr");
		strSQL.append(",t.eqp_online");
		strSQL.append(",t.eqp_muser");
		strSQL.append(",t.truck_id");
		strSQL.append(",t.org_id");
		strSQL.append(",t.td_id");
		strSQL.append(",t.truck_name");
		strSQL.append(",t.truck_no");
		strSQL.append(",t.plate_num");
		strSQL.append(" from V_VEHICLE_WORK_PARAS t");
		strSQL.append(" where t.eqp_inst is not null");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by" + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private TruckWorkParasPojo _setOneVehicleWordParasPojo(ResultSet rs)
			throws Exception {
		TruckWorkParasPojo onePojo = new TruckWorkParasPojo();
		onePojo.getEqpInst().setInstId(rs.getString("eqp_inst"));
		onePojo.getEqpInst().setName(rs.getString("eqp_name"));
		onePojo.getEqpInst().setWyCode(rs.getString("eqp_wycode"));
		onePojo.getEqpInst().setQrCode(rs.getString("eqp_qr"));
		onePojo.getEqpInst().setOnlineState(rs.getInt("eqp_online"));
		if (rs.getString("truck_id") != null) {
			onePojo.getEqpInst().getTruck().setId(rs.getString("truck_id"));
			onePojo.getEqpInst().getTruck().setNo(rs.getString("truck_no"));
			onePojo.getEqpInst().getTruck().setName(rs.getString("truck_name"));
			onePojo.getEqpInst().getTruck()
					.setPlateNum(rs.getString("plate_num"));
		}
		if (rs.getString("org_id") != null
				&& !rs.getString("org_id").equals("")) {
			onePojo.getEqpInst()
					.getTruck()
					.setOrg(this.userBI.getGroupByRedis(rs.getString("org_id")));
		}
		if (rs.getString("TD_ID") != null) {
			onePojo.getEqpInst()
					.getTruck()
					.setDefine(
							this.truckBI.getTruckDefByRedis(rs
									.getString("TD_ID")));
		}
		JSONObject oneNDate = (new BITruck(null, null))
				.getVehicleNewestByRedis(onePojo.getEqpInst().getInstId());
		if (oneNDate != null) {
			if (oneNDate.containsKey("oil")) {
				onePojo.setOil(oneNDate.getString("oil"));
			}
			if (oneNDate.containsKey("speed")) {
				onePojo.setSpeed(oneNDate.getString("speed"));
			}
			if (oneNDate.containsKey("oildiff")) {
				onePojo.setOilDiff(oneNDate.getString("oildiff"));
			}
			if (oneNDate.containsKey("createDate")) {
				onePojo.setOilDate(oneNDate.getString("createDate"));
			}
			if (oneNDate.containsKey("longitude")) {
				onePojo.setLongitude(oneNDate.getString("longitude"));
			}
			if (oneNDate.containsKey("latitude")) {
				onePojo.setLatitude(oneNDate.getString("latitude"));
			}
			if (oneNDate.containsKey("createDate")) {
				onePojo.setGeoDate(oneNDate.getString("createDate"));
			}
			if (oneNDate.containsKey("fanceFlg")) {
				onePojo.setFanceFlg(oneNDate.getInt("fanceFlg"));
			}
		}
		return onePojo;
	}
}
