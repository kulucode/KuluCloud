package tt.kulu.bi.storage.dbclass;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.storage.pojo.EquipmentDefPojo;
import tt.kulu.bi.storage.pojo.EquipmentGeometryPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstWorkLogPojo;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BIEquipment;
import tt.kulu.out.call.BITruck;
import tt.kulu.out.call.BIUser;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: EquipmentDBMang
 * </p>
 * <p>
 * 功能描述: 物品数据库操作类
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
public class EquipmentDBMang extends BSDBBase {
	private BIDic dicBI = null;
	private BIEquipment eqpBI = null;
	private BIUser userBI = null;
	private BITruck truckBI = null;

	public EquipmentDBMang(SqlExecute sqlHelper, BSObject m_bs)
			throws Exception {
		super(sqlHelper, m_bs);
		this.dicBI = new BIDic(m_bs);
		this.eqpBI = new BIEquipment(m_bs);
		this.truckBI = new BITruck(m_bs);
		this.userBI = new BIUser(m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getEquipmentDefList
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
	public ArrayList<EquipmentDefPojo> getEquipmentDefList(String where,
			String orderBy, List<Object> vList) throws Exception {
		ArrayList<EquipmentDefPojo> list = new ArrayList<EquipmentDefPojo>();
		ResultSet rs = this.sqlHelper.queryBySql(
				_getEquipmentDefSelectSQL(where, orderBy).toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneEquipmentDefPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentDefById
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
	public EquipmentDefPojo getOneEquipmentDefById(String id) throws Exception {
		EquipmentDefPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		ResultSet rs = this.sqlHelper.queryBySql(this
				._getEquipmentDefSelectSQL(" and t.EQP_CODE=?", "").toString(),
				vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneEquipmentDefPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentDefById
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
	public EquipmentDefPojo getOneEquipmentDefByEqpNo(String typeId, String no)
			throws Exception {
		EquipmentDefPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(typeId);
		vList.add(no);
		ResultSet rs = this.sqlHelper.queryBySql(
				this._getEquipmentDefSelectSQL(
						" and t.EQP_TYPE=? and t.EQP_NO=?", "").toString(),
				vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneEquipmentDefPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertEquipmentDef
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
	public int insertEquipmentDef(EquipmentDefPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo.getId() == null || onePojo.getId().trim().equals("")) {
			if (onePojo.getNo() != null || !onePojo.getNo().equals("")) {
				onePojo.setId(onePojo.getEqpType().getId() + "_"
						+ onePojo.getNo());
			} else {
				onePojo.setId(BSGuid.getRandomGUID());
			}
		}
		StringBuffer strSQL = new StringBuffer("insert into T_EQUIPMENT_DEF (");
		strSQL.append("EQP_CODE");
		strSQL.append(",EQP_NAME");
		strSQL.append(",EQP_MANUFACTURER");
		strSQL.append(",EQP_BRAND");
		strSQL.append(",EQP_TYPE");
		strSQL.append(",EQP_STYLE");
		strSQL.append(",EQP_NO");
		strSQL.append(",EQP_PARA1");
		strSQL.append(",EQP_PARA2");
		strSQL.append(",EQP_PARA3");
		strSQL.append(") values (?,?,?,?,?,?,?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		vList.add(onePojo.getName());
		vList.add(onePojo.getManufacturer());
		vList.add(onePojo.getBrand());
		vList.add(onePojo.getEqpType().getId());
		vList.add(onePojo.getStyle());
		vList.add(onePojo.getNo());
		vList.add(onePojo.getPara1());
		vList.add(onePojo.getPara2());
		vList.add(onePojo.getPara3());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);

		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentDef
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
	public int updateEquipmentDef(EquipmentDefPojo onePojo) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		if ((onePojo.getId() == null || onePojo.getId().trim().equals(""))
				|| this.sqlHelper
						.queryIntBySql(
								"select count(EQP_CODE) from T_EQUIPMENT_DEF where EQP_CODE=?",
								vList) <= 0) {
			// 新增
			count += this.insertEquipmentDef(onePojo);
		} else {
			StringBuffer strSQL = new StringBuffer(
					"update T_EQUIPMENT_DEF set ");
			strSQL.append("EQP_NAME=?");
			strSQL.append(",EQP_MANUFACTURER=?");
			strSQL.append(",EQP_BRAND=?");
			strSQL.append(",EQP_TYPE=?");
			strSQL.append(",EQP_STYLE=?");
			strSQL.append(",EQP_NO=?");
			strSQL.append(",EQP_PARA1=?");
			strSQL.append(",EQP_PARA2=?");
			strSQL.append(",EQP_PARA3=?");
			strSQL.append(" where EQP_CODE=?");
			vList.clear();
			vList.add(onePojo.getName());
			vList.add(onePojo.getManufacturer());
			vList.add(onePojo.getBrand());
			vList.add(onePojo.getEqpType().getId());
			vList.add(onePojo.getStyle());
			vList.add(onePojo.getNo());
			vList.add(onePojo.getPara1());
			vList.add(onePojo.getPara2());
			vList.add(onePojo.getPara3());
			vList.add(onePojo.getId());
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getEquipmentInstList
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
	public ArrayList<EquipmentInstPojo> getEquipmentInstList(String where,
			String orderBy, long f, long t, List<Object> vList)
			throws Exception {
		ArrayList<EquipmentInstPojo> list = new ArrayList<EquipmentInstPojo>();
		StringBuffer strSQL = _getEquipmentInstSelectSQL(where, orderBy);
		if (f >= 0 && t > 0) {
			strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneEquipmentInstPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getEquipmentInstList
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
	public long getEquipmentInstCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.EQP_INST) as OBJ_COUNT");
		strSQL.append(" from T_EQUIPMENT_INST t left outer join T_USER tu on t.EQP_MUSER = tu.USER_INSTID");
		strSQL.append(" left outer join T_ORG torg on t.ORG_ID = torg.ORG_ID");
		strSQL.append(" where t.EQP_INST is not null ");

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
	 * 方法名称: getOneEquipmentInstById
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
	public EquipmentInstPojo getOneEquipmentInstById(String id)
			throws Exception {
		EquipmentInstPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		vList.add(id);
		vList.add(id);
		vList.add(id);
		ResultSet rs = this.sqlHelper
				.queryBySql(
						this._getEquipmentInstSelectSQL(
								" and (t.EQP_INST=? OR t.EQP_QR=? OR t.EQP_WYCODE=? OR t.EQP_TOKEN=?)",
								"").toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneEquipmentInstPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentInstByWhere
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
	public EquipmentInstPojo getOneEquipmentInstByWhere(String where,
			String orderBy, List<Object> vList) throws Exception {
		EquipmentInstPojo onePojo = null;
		ResultSet rs = this.sqlHelper.queryBySql(this
				._getEquipmentInstSelectSQL(where, orderBy).toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneEquipmentInstPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentInstById
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
	public EquipmentInstPojo getOneEquipmentInstByToken(String tokey)
			throws Exception {
		EquipmentInstPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(tokey);
		vList.add(tokey);
		ResultSet rs = this.sqlHelper.queryBySql(
				this._getEquipmentInstSelectSQL(
						" and (t.EQP_TOKEN=? OR t.EQP_INST=?)", "").toString(),
				vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneEquipmentInstPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOnevehicleInstByTruck
	 * </p>
	 * <p>
	 * 方法功能描述: 根据车辆的到车载设备实例。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public EquipmentInstPojo getOnevehicleInstByTruck(String truckid)
			throws Exception {
		EquipmentInstPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(truckid);
		ResultSet rs = this.sqlHelper
				.queryBySql(
						this._getEquipmentInstSelectSQL(
								" and t.EQP_TRUCK=? and t.EQP_DEF in (select v.EQP_CODE from T_EQUIPMENT_DEF v where v.EQP_TYPE='EQUIPMENT_DEFTYPE_0')",
								"").toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneEquipmentInstPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentInstById
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
	public EquipmentInstPojo getOneEquipmentInstByWyCode(String wyCode)
			throws Exception {
		EquipmentInstPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(wyCode);
		vList.add(wyCode);
		ResultSet rs = this.sqlHelper.queryBySql(
				this._getEquipmentInstSelectSQL(
						" and (t.EQP_WYCODE=? OR t.EQP_TOKEN=?)", "")
						.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneEquipmentInstPojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentInstByWYCode
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
	public String getOneEquipmentInstByWYCode(String wyCode, String eqpDef)
			throws Exception {
		String eqpInstId = "";
		List<Object> vList = new ArrayList<Object>();
		vList.add(wyCode);
		vList.add(eqpDef);
		ResultSet rs = this.sqlHelper
				.queryBySql(
						"select t.EQP_INST from T_EQUIPMENT_INST t where t.EQP_WYCODE=? and t.EQP_DEF=?",
						vList);
		if (rs != null) {
			if (rs.next()) {
				eqpInstId = rs.getString("EQP_INST");
			}
			rs.close();
		}

		return eqpInstId;
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentMUserByWYCode
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
	public String getOneEquipmentMUserByWYCode(String wyCode, String eqpDef)
			throws Exception {
		String eqpInstId = "";
		List<Object> vList = new ArrayList<Object>();
		vList.add(wyCode);
		vList.add(eqpDef);
		ResultSet rs = this.sqlHelper
				.queryBySql(
						"select t.EQP_MUSER from T_EQUIPMENT_INST t where t.EQP_WYCODE=? and t.EQP_DEF=?",
						vList);
		if (rs != null) {
			if (rs.next()) {
				eqpInstId = rs.getString("EQP_MUSER");
			}
			rs.close();
		}

		return eqpInstId;
	}

	/**
	 * <p>
	 * 方法名称: insertEquipmentInst
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个物品实例。
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
	public int insertEquipmentInst(EquipmentInstPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo.getInstId() == null
				|| onePojo.getInstId().trim().equals("")) {
			onePojo.setInstId(BSGuid.getRandomGUID());
		}
		StringBuffer strSQL = new StringBuffer("insert into T_EQUIPMENT_INST (");
		strSQL.append("EQP_INST");
		strSQL.append(",EQP_WYCODE");
		strSQL.append(",EQP_DEF");
		strSQL.append(",EQP_NAME");
		strSQL.append(",EQP_QR");
		strSQL.append(",EQP_PDATE");
		strSQL.append(",EQP_UDATE");
		strSQL.append(",EQP_STATE");
		strSQL.append(",EQP_ONLINE");
		strSQL.append(",EQP_PHONE");
		strSQL.append(",EQP_MUSER");
		strSQL.append(",ORG_ID");
		strSQL.append(",EQP_TRUCK");
		strSQL.append(",EQP_TOKEN");
		strSQL.append(",EQP_PARA1");
		strSQL.append(",EQP_PARA2");
		strSQL.append(",EQP_DEFGEO");
		strSQL.append(",EQP_INSTALL");
		strSQL.append(",EQP_TONNAGE");
		strSQL.append(") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getInstId());
		vList.add(onePojo.getWyCode());
		vList.add(onePojo.getEqpDef().getId());
		vList.add(onePojo.getName());
		vList.add(onePojo.getQrCode());
		vList.add(Timestamp.valueOf(onePojo.getProDate()));
		vList.add(Timestamp.valueOf(onePojo.getUpdateDate()));
		vList.add(onePojo.getState());
		vList.add(onePojo.getOnlineState());
		vList.add(onePojo.getPhone());
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
		if (onePojo.getTruck() != null) {
			vList.add(onePojo.getTruck().getId());
		} else {
			vList.add("");
		}
		vList.add(onePojo.getToken());
		vList.add(onePojo.getPara1());
		vList.add(onePojo.getPara2());
		if (!onePojo.getDefLongitude().equals("")
				&& !onePojo.getDefLatitude().equals("")) {
			vList.add(onePojo.getDefLongitude() + ","
					+ onePojo.getDefLatitude());
		} else {
			vList.add("");
		}
		vList.add(onePojo.getInstall());
		vList.add(onePojo.getTonnage());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);

		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInst
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个物品实例。
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
	public int updateEquipmentInst(EquipmentInstPojo onePojo) throws Exception {
		int count = 0;
		StringBuffer strSQL = new StringBuffer("update T_EQUIPMENT_INST set ");
		strSQL.append("EQP_DEF=?");
		strSQL.append(",EQP_NAME=?");
		strSQL.append(",EQP_QR=?");
		strSQL.append(",EQP_UDATE=?");
		strSQL.append(",EQP_STATE=?");
		// strSQL.append(",EQP_ONLINE=?");
		strSQL.append(",EQP_LDATE=?");
		strSQL.append(",EQP_PHONE=?");
		strSQL.append(",EQP_MUSER=?");
		strSQL.append(",ORG_ID=?");
		strSQL.append(",EQP_TRUCK=?");
		strSQL.append(",EQP_WYCODE=?");
		strSQL.append(",EQP_TOKEN=?");
		strSQL.append(",EQP_PARA1=?");
		strSQL.append(",EQP_PARA2=?");
		strSQL.append(",EQP_DEFGEO=?");
		strSQL.append(",EQP_INSTALL=?");
		strSQL.append(",EQP_TONNAGE=?");
		strSQL.append(" where EQP_INST=?");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getEqpDef().getId());
		vList.add(onePojo.getName());
		vList.add(onePojo.getQrCode());
		vList.add(Timestamp.valueOf(onePojo.getUpdateDate()));
		vList.add(onePojo.getState());
		// vList.add(onePojo.getOnlineState());
		if (onePojo.getLastLoginDate().equals("")) {
			vList.add(null);
		} else {
			vList.add(Timestamp.valueOf(onePojo.getLastLoginDate()));
		}
		vList.add(onePojo.getPhone());
		vList.add(onePojo.getMangUser().getInstId());
		vList.add(onePojo.getOrg().getId());
		vList.add(onePojo.getTruck().getId());
		vList.add(onePojo.getWyCode());
		vList.add(onePojo.getToken());
		vList.add(onePojo.getPara1());
		vList.add(onePojo.getPara2());
		if (!onePojo.getDefLongitude().equals("")
				&& !onePojo.getDefLatitude().equals("")) {
			vList.add(onePojo.getDefLongitude() + ","
					+ onePojo.getDefLatitude());
		} else {
			vList.add("");
		}
		vList.add(onePojo.getInstall());
		vList.add(onePojo.getTonnage());
		vList.add(onePojo.getInstId());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		// 关联设备更换车辆
		strSQL = new StringBuffer("update T_EQUIPMENT_INST set ");
		strSQL.append("EQP_MUSER=?");
		strSQL.append(",ORG_ID=?");
		strSQL.append(",EQP_TRUCK=?");
		strSQL.append(" where EQP_INST in (select v.EQP_INST from T_EQUIPMENT_INST_R v where v.P_EQP_INST=?)");
		vList.clear();
		vList.add(onePojo.getMangUser().getInstId());
		vList.add(onePojo.getOrg().getId());
		vList.add(onePojo.getTruck().getId());
		vList.add(onePojo.getInstId());
		count += this.sqlHelper.updateBySql(strSQL.toString(), vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInstMUser
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个物品实例。
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
	public int updateEquipmentInstMUser(EquipmentInstPojo onePojo)
			throws Exception {
		int count = 0;
		StringBuffer strSQL = new StringBuffer("update T_EQUIPMENT_INST set ");
		strSQL.append("EQP_MUSER=?");
		strSQL.append(",ORG_ID=?");
		strSQL.append(" where EQP_INST=?");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getMangUser().getInstId());
		vList.add(onePojo.getOrg().getId());
		vList.add(onePojo.getInstId());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInstTruck
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个物品实例。
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
	public int updateEquipmentInstTruck(EquipmentInstPojo onePojo)
			throws Exception {
		int count = 0;
		StringBuffer strSQL = new StringBuffer("update T_EQUIPMENT_INST set ");
		strSQL.append("EQP_TRUCK=?");
		strSQL.append(",ORG_ID=?");
		strSQL.append(",EQP_MUSER=?");
		strSQL.append(" where EQP_INST=?");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getTruck().getId());
		if (onePojo.getOrg() != null) {
			vList.add(onePojo.getOrg().getId());
		} else {
			vList.add("");
		}
		if (onePojo.getMangUser() != null) {
			vList.add(onePojo.getMangUser().getId());
		} else {
			vList.add("");
		}
		vList.add(onePojo.getInstId());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInst
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个物品实例。
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
	public int updateEquipmentInstLast(EquipmentInstPojo onePojo)
			throws Exception {
		int count = 0;
		StringBuffer strSQL = new StringBuffer("update T_EQUIPMENT_INST set ");
		strSQL.append("EQP_ONLINE=?");
		strSQL.append(",EQP_LDATE=?");
		strSQL.append(" where EQP_INST=?");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getOnlineState());
		if (onePojo.getLastLoginDate().equals("")) {
			vList.add(null);
		} else {
			vList.add(Timestamp.valueOf(onePojo.getLastLoginDate()));
		}
		vList.add(onePojo.getInstId());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInstRel
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个实例关系。
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
	public int updateEquipmentInstRel(String pEqp, String relEqp)
			throws Exception {
		int count = 0;
		// 得到父亲设备
		EquipmentInstPojo pEqpPojo = this.getOneEquipmentInstById(pEqp);
		if (pEqpPojo != null) {
			// 得到关联设备
			EquipmentInstPojo relEqpPojo = this.getOneEquipmentInstById(relEqp);
			if (relEqpPojo != null) {
				// 更新关系设备
				relEqpPojo.setMangUser(pEqpPojo.getMangUser());
				relEqpPojo.setOrg(pEqpPojo.getOrg());
				relEqpPojo.setPhone(pEqpPojo.getPhone());
				relEqpPojo.setOnlineState(1);
				relEqpPojo.setTruck(pEqpPojo.getTruck());
				count += this.updateEquipmentInst(relEqpPojo);
				// 添加关系
				List<Object> vList = new ArrayList<Object>();
				vList.add(relEqp);
				// 删除关系
				count += this.sqlHelper.updateBySql(
						"delete from T_EQUIPMENT_INST_R where EQP_INST=?",
						vList);
				// 新增关系
				vList.add(pEqp);
				count += this.sqlHelper
						.updateBySql(
								"insert into T_EQUIPMENT_INST_R (EQP_INST,P_EQP_INST) values (?,?)",
								vList);
			}
		}

		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInstRel
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个实例关系。
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
	public int deleteEquipmentInstRel(String pEqp, String relEqp)
			throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(relEqp);
		vList.add(pEqp);
		// 删除关系
		count += this.sqlHelper
				.updateBySql(
						"delete from T_EQUIPMENT_INST_R where EQP_INST=? and P_EQP_INST=?",
						vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: checkEquipmentQRCode
	 * </p>
	 * <p>
	 * 方法功能描述: 判断是有重复条码。
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
	public int checkEquipmentQRCode(String code) throws Exception {
		List<Object> vList = new ArrayList<Object>();
		vList.add(code);
		return this.sqlHelper
				.queryIntBySql(
						"select count(t.EQP_INST) from T_EQUIPMENT_INST t where t.EQP_QR=?",
						vList);

	}

	/**
	 * <p>
	 * 方法名称: checkEquipmentQRCode
	 * </p>
	 * <p>
	 * 方法功能描述: 判断是有重复设备。
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
	public int checkEquipmentWYCode(String wyCode) throws Exception {
		List<Object> vList = new ArrayList<Object>();
		vList.add(wyCode);
		return this.sqlHelper
				.queryIntBySql(
						"select count(t.EQP_INST) from T_EQUIPMENT_INST t where t.EQP_WYCODE=?",
						vList);

	}

	/**
	 * <p>
	 * 方法名称: insertEquipmentGeometry
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
	public int insertEquipmentGeometry(EquipmentGeometryPojo onePojo)
			throws Exception {
		int count = 0;
		StringBuffer sql = new StringBuffer(
				"insert into T_EQP_INST_GEOMETRY(LOG_ID,EQP_INST,C_DATE,S_CDATE,S_LAT,S_LON,S_SPEED,S_FDID,S_LSDATE,S_GEOMETRY) values (?,?,?,?,?,?,?,?,?,ST_GeomFromText('POINT("
						+ onePojo.getLongitude()
						+ " "
						+ onePojo.getLatitude()
						+ ")',4326))");
		List<Object> vList = new ArrayList<Object>();
		onePojo.setId(BSGuid.getRandomGUID());
		vList.add(onePojo.getId());
		vList.add(onePojo.getEqpInst().getInstId());
		vList.add(Timestamp.valueOf(onePojo.getSysDate()));
		vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
		vList.add(onePojo.getLatitude());
		vList.add(onePojo.getLongitude());
		vList.add(onePojo.getSpeed());
		vList.add(this.eqpBI.getEqpInstFDIdByRedis(onePojo.getEqpInst()
				.getInstId()));
		if (onePojo.getLastStopDate().equals("")) {
			vList.add(null);
		} else {
			vList.add(Timestamp.valueOf(onePojo.getLastStopDate()));
		}
		count = this.sqlHelper.updateBySql(sql.toString(), vList);
		// 更新设备最新记录
		if (count > 0) {
			vList.clear();
			vList.add(onePojo.getId());
			vList.add(onePojo.getEqpInst().getInstId());
			this.sqlHelper
					.updateBySql(
							"update T_EQUIPMENT_INST set EQP_GEO_ID=? where EQP_INST=?",
							vList);
		}

		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertEquipmentGeometry
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
	public int updateEquipmentGeometryFance(EquipmentGeometryPojo onePojo)
			throws Exception {
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getFanceFlg());
		vList.add(onePojo.getId());
		return this.sqlHelper
				.updateBySql(
						"update T_EQP_INST_GEOMETRY t set S_FANCEFLG=? where t.LOG_ID=?",
						vList);
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInst
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个物品实例。
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
	public String getGeometryLastDisdance(EquipmentGeometryPojo oneEqpGeo,
			int offset) throws Exception {
		String dis = "0";
		// 得到最后一条记录
		StringBuffer sql = new StringBuffer(
				"select ST_distance_sphere(ST_GeomFromText('POINT("
						+ oneEqpGeo.getLongitude()
						+ " "
						+ oneEqpGeo.getLatitude()
						+ ")',4326),t.S_GEOMETRY) as GEO_DIS from T_EQP_INST_GEOMETRY t where t.EQP_INST=? and t.C_DATE BETWEEN ? AND ? order by t.C_DATE desc  LIMIT 1 OFFSET "
						+ offset);
		List<Object> vList = new ArrayList<Object>();
		vList.add(oneEqpGeo.getEqpInst().getInstId());
		vList.add(Timestamp.valueOf(oneEqpGeo.getSysDate().substring(0, 10)
				+ " 00:00:00"));
		vList.add(Timestamp.valueOf(oneEqpGeo.getSysDate().substring(0, 10)
				+ " 23:59:59"));
		ResultSet rs = this.sqlHelper.queryBySql(sql.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				dis = rs.getString("GEO_DIS");
			}
			rs.close();
		}
		return dis;
	}

	/**
	 * <p>
	 * 方法名称: getEquipmentInstList
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
	public ArrayList<EquipmentGeometryPojo> getEqpGeometryList(String where,
			String orderBy, long f, long t, List<Object> vList)
			throws Exception {
		ArrayList<EquipmentGeometryPojo> list = new ArrayList<EquipmentGeometryPojo>();
		StringBuffer strSQL = _getEqpGeometrySelectSQL(where, orderBy);
		if (f >= 0 && t > 0) {
			strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneEqpGeometryPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getEquipmentInstList
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
	public long getEqpGeometryCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.LOG_ID) as OBJ_COUNT");
		strSQL.append(" from t_eqp_inst_geometry t,T_EQUIPMENT_INST t1 left outer join T_USER tu on t1.EQP_MUSER = tu.USER_INSTID");
		strSQL.append(" left outer join T_ORG torg on t1.ORG_ID = torg.ORG_ID");
		strSQL.append(" left outer join T_TRUCK_INST tru on t1.EQP_TRUCK = tru.TRUCK_ID");
		strSQL.append(" where t.EQP_INST=t1.EQP_INST ");

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
	public EquipmentGeometryPojo getLastEquipmentGeoPojo(String eqpId)
			throws Exception {
		EquipmentGeometryPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(eqpId);
		StringBuffer strSQL = _getEqpGeometrySelectSQL(
				" and t.EQP_INST=? and t.C_DATE is not null",
				" t.C_DATE desc,t.S_CDATE desc");
		strSQL.append(" LIMIT 1 OFFSET 0");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = this._setOneEqpGeometryPojo(rs);
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentGeoFDId
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
	public String getOneEquipmentGeoFDId(String eqpId) throws Exception {
		String fdId = BSGuid.getRandomGUID();
		List<Object> vList = new ArrayList<Object>();
		vList.add(eqpId);
		ResultSet rs = this.sqlHelper
				.queryBySql(
						"select t.S_FDID from T_EQP_INST_GEOMETRY t where t.EQP_INST=? order by t.S_CDATE desc LIMIT 1 OFFSET 0",
						vList);
		if (rs != null) {
			if (rs.next()) {
				if (rs.getString("S_FDID") != null
						&& !rs.getString("S_FDID").equals("")) {
					fdId = rs.getString("S_FDID");
				}
			}
			rs.close();
		}

		return fdId;
	}

	/**
	 * <p>
	 * 方法名称: getLastEquipmentInstWorkLog
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个设备实例开关机日志。
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
	public EquipmentInstWorkLogPojo getLastEquipmentInstWorkLog(String instId,
			int state, int offset) throws Exception {
		EquipmentInstWorkLogPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		if (state >= 0) {
			vList.add(state);
		}
		vList.add(instId);
		StringBuffer strSQL = this
				._getEquipmentInstWorkLogSelectSQL(
						(state >= 0 ? " and t.WORK_STATE=?" : "")
								+ " and t.EQP_INST=?", "");
		strSQL.append(" LIMIT 1 OFFSET " + offset);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneEquipmentInstWorkLogPojo(rs));
			}
			rs.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertEquipmentInstWorkLog
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个设备实例开关机日志。
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
	public int insertEquipmentInstWorkLog(EquipmentInstWorkLogPojo onePojo)
			throws Exception {
		int count = 0;
		if (onePojo.getEqpInst() == null
				|| !onePojo.getEqpInst().getInstId().trim().equals("")) {
			onePojo.setId(BSGuid.getRandomGUID());
			StringBuffer strSQL = new StringBuffer(
					"insert into T_EQUIPMENT_WORKLOG (");
			strSQL.append("LOG_ID");
			strSQL.append(",EQP_INST");
			strSQL.append(",S_CDATE");
			strSQL.append(",WORK_STATE");
			strSQL.append(") values (?,?,?,?)");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getId());
			vList.add(onePojo.getEqpInst().getInstId());
			vList.add(Timestamp.valueOf(onePojo.getDate()));
			vList.add(onePojo.getState());
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertEquipmentInstWorkLog
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个设备实例开关机日志。
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
	public int updateEquipmentInstWorkLog(EquipmentInstWorkLogPojo onePojo)
			throws Exception {
		int count = 0;
		if (onePojo.getEqpInst() == null
				|| !onePojo.getEqpInst().getInstId().trim().equals("")) {
			StringBuffer strSQL = new StringBuffer(
					"update T_EQUIPMENT_WORKLOG set ");
			strSQL.append("S_CDATE=?");
			strSQL.append(",WORK_STATE=?");
			strSQL.append(" where LOG_ID=?");
			List<Object> vList = new ArrayList<Object>();
			vList.add(Timestamp.valueOf(onePojo.getDate()));
			vList.add(onePojo.getState());
			vList.add(onePojo.getId());
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getEquipmentDefSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.EQP_CODE");
		strSQL.append(",t.EQP_NAME");
		strSQL.append(",t.EQP_MANUFACTURER");
		strSQL.append(",t.EQP_BRAND");
		strSQL.append(",t.EQP_STYLE");
		strSQL.append(",t.EQP_TYPE");
		strSQL.append(",t.EQP_NO");
		strSQL.append(",t.EQP_PARA1");
		strSQL.append(",t.EQP_PARA2");
		strSQL.append(",t.EQP_PARA3");
		strSQL.append(",(select count(v1.EQP_INST) from T_EQUIPMENT_INST v1 where v1.eqp_def=t.EQP_CODE) as INST_COUNT");// 实例数量
		strSQL.append(" from T_EQUIPMENT_DEF t");
		strSQL.append(" where t.EQP_CODE is not null ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.EQP_NAME");
		}
		return strSQL;
	}

	// 物品定义对象
	private EquipmentDefPojo _setOneEquipmentDefPojo(ResultSet rs)
			throws Exception {
		EquipmentDefPojo onePojo = new EquipmentDefPojo();
		onePojo.setId(rs.getString("EQP_CODE"));
		onePojo.setName(rs.getString("EQP_NAME"));
		onePojo.setStyle(rs.getInt("EQP_STYLE"));
		if (rs.getString("EQP_NO") != null) {
			onePojo.setNo(rs.getString("EQP_NO"));
		}
		if (rs.getString("EQP_MANUFACTURER") != null) {
			onePojo.setManufacturer(rs.getString("EQP_MANUFACTURER"));
		}
		if (rs.getString("EQP_BRAND") != null) {
			onePojo.setBrand(rs.getString("EQP_BRAND"));
		}
		if (rs.getString("EQP_PARA1") != null) {
			onePojo.setPara1(rs.getString("EQP_PARA1"));
		}
		if (rs.getString("EQP_PARA2") != null) {
			onePojo.setPara2(rs.getString("EQP_PARA2"));
		}
		if (rs.getString("EQP_PARA3") != null) {
			onePojo.setPara3(rs.getString("EQP_PARA3"));
		}
		onePojo.setEqpType(this.dicBI.getDicItemByRedis(rs
				.getString("EQP_TYPE")));
		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getEquipmentInstSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.EQP_INST");
		strSQL.append(",t.EQP_WYCODE");
		strSQL.append(",t.EQP_TOKEN");
		strSQL.append(",t.EQP_NAME");
		strSQL.append(",t.EQP_QR");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.EQP_PDATE")
				+ " as EQP_PDATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.EQP_UDATE")
				+ " as EQP_UDATE");
		strSQL.append(",t.EQP_STATE");
		strSQL.append(",t.EQP_ONLINE");
		strSQL.append(",t.EQP_TRUCK");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.EQP_LDATE")
				+ " as EQP_LDATE");
		strSQL.append(",t.EQP_PHONE");
		strSQL.append(",t.EQP_PARA1");
		strSQL.append(",t.EQP_PARA2");
		strSQL.append(",t.EQP_DEFGEO");
		strSQL.append(",t.EQP_INSTALL");
		strSQL.append(",t.EQP_TONNAGE");
		// 定义
		strSQL.append(",t.EQP_DEF");
		// 管理用户
		strSQL.append(",t.EQP_MUSER");
		strSQL.append(",tu.USER_ID");
		strSQL.append(",tu.USER_NAME");
		strSQL.append(",tu.MPHONE");
		// 机构
		strSQL.append(",t.ORG_ID");
		// 车辆
		strSQL.append(",t.EQP_TRUCK");
		strSQL.append(",tru.TD_ID");
		strSQL.append(",tru.TRUCK_NAME");
		strSQL.append(",tru.TRUCK_NO");
		strSQL.append(",tru.PLATE_NUM");
		strSQL.append(",tru.PLATE_COLOR");
		strSQL.append(",tru.TRUCK_OILDEF");
		strSQL.append(",tru.ORG_ID as TRUCK_ORG");

		strSQL.append(" from T_EQUIPMENT_INST t left outer join T_USER tu on t.EQP_MUSER = tu.USER_INSTID");
		strSQL.append(" left outer join T_TRUCK_INST tru on t.EQP_TRUCK = tru.TRUCK_ID");
		strSQL.append(" where t.EQP_INST is not null ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.EQP_UDATE desc");
		}
		return strSQL;
	}

	// 物品定义对象
	private EquipmentInstPojo _setOneEquipmentInstPojo(ResultSet rs)
			throws Exception {
		EquipmentInstPojo onePojo = new EquipmentInstPojo();
		onePojo.setInstId(rs.getString("EQP_INST"));
		onePojo.setWyCode(rs.getString("EQP_WYCODE"));
		onePojo.setName(rs.getString("EQP_NAME"));
		onePojo.setProDate(rs.getString("EQP_PDATE"));
		onePojo.setUpdateDate(rs.getString("EQP_UDATE"));
		onePojo.setState(rs.getInt("EQP_STATE"));
		onePojo.setOnlineState(rs.getInt("EQP_ONLINE"));
		if (rs.getString("EQP_TOKEN") != null) {
			onePojo.setToken(rs.getString("EQP_TOKEN"));
		}
		if (rs.getString("EQP_PARA1") != null) {
			onePojo.setPara1(rs.getString("EQP_PARA1"));
		}
		if (rs.getString("EQP_QR") != null) {
			onePojo.setQrCode(rs.getString("EQP_QR"));
		}

		if (rs.getString("EQP_TRUCK") != null) {
			// onePojo.setQrCode(rs.getString("EQP_TRUCK"));
		}
		if (rs.getString("EQP_LDATE") != null) {
			onePojo.setLastLoginDate(rs.getString("EQP_LDATE"));
		}
		if (rs.getString("EQP_PHONE") != null) {
			onePojo.setPhone(rs.getString("EQP_PHONE"));
		}
		if (rs.getString("EQP_PARA2") != null) {
			onePojo.setPara2(rs.getString("EQP_PARA2"));
		}
		if (rs.getString("EQP_INSTALL") != null) {
			onePojo.setInstall(rs.getString("EQP_INSTALL"));
		}
		if (rs.getString("EQP_TONNAGE") != null) {
			onePojo.setTonnage(rs.getString("EQP_TONNAGE"));
		}
		if (rs.getString("EQP_DEFGEO") != null
				&& !rs.getString("EQP_DEFGEO").equals("")) {
			String[] defG = rs.getString("EQP_DEFGEO").split(",");
			if (defG.length == 2) {
				onePojo.setDefLongitude(defG[0]);
				onePojo.setDefLatitude(defG[1]);
			}
		}

		// 定义
		onePojo.setEqpDef(this.eqpBI.getEqpDefByRedis(rs.getString("EQP_DEF")));

		// 管理员
		if (rs.getString("EQP_MUSER") != null) {
			onePojo.getMangUser().setInstId(rs.getString("EQP_MUSER"));
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
		if (rs.getString("ORG_ID") != null) {
			onePojo.setOrg(this.userBI.getGroupByRedis(rs.getString("ORG_ID")));
		}
		// 车辆
		if (rs.getString("EQP_TRUCK") != null) {
			onePojo.getTruck().setId(rs.getString("EQP_TRUCK"));
			onePojo.getTruck().setDefine(
					this.truckBI.getTruckDefByRedis(rs.getString("TD_ID")));
			if (rs.getString("TRUCK_NAME") != null) {
				onePojo.getTruck().setName(rs.getString("TRUCK_NAME"));
			}
			if (rs.getString("TRUCK_NO") != null) {
				onePojo.getTruck().setNo(rs.getString("TRUCK_NO"));
			}
			if (rs.getString("PLATE_NUM") != null) {
				onePojo.getTruck().setPlateNum(rs.getString("PLATE_NUM"));
			}
			if (rs.getString("PLATE_COLOR") != null) {
				onePojo.getTruck().setPlateColor(rs.getInt("PLATE_COLOR"));
			}
			if (rs.getString("TRUCK_ORG") != null
					&& !rs.getString("TRUCK_ORG").equals("")) {
				onePojo.getTruck().setOrg(
						this.userBI.getGroupByRedis(rs.getString("TRUCK_ORG")));
			}
			// 邮箱
			if (rs.getString("TRUCK_OILDEF") != null
					&& !rs.getString("TRUCK_OILDEF").equals("")) {
				onePojo.getTruck().setOilDef(
						JSONObject.fromObject(rs.getString("TRUCK_OILDEF")));
			}
		}

		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getEqpGeometrySelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.LOG_ID");
		strSQL.append(",t.EQP_INST");
		strSQL.append(",t.S_SPEED");
		strSQL.append(",t.S_FDID");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.S_LSDATE")
				+ " as S_LSDATE");
		strSQL.append(",t.S_FANCEFLG");
		strSQL.append(",t1.EQP_WYCODE");
		strSQL.append(",t1.EQP_NAME");
		strSQL.append(",t1.EQP_QR");
		strSQL.append(",t1.EQP_TRUCK");
		strSQL.append("," + SqlExecute.getDateToCharSql("t1.EQP_LDATE")
				+ " as EQP_LDATE");
		// 定义
		strSQL.append(",t1.EQP_DEF");
		// 管理用户
		strSQL.append(",t1.EQP_MUSER");
		strSQL.append(",tu.USER_ID");
		strSQL.append(",tu.USER_NAME");
		strSQL.append(",tu.MPHONE");
		// 机构
		strSQL.append(",t1.ORG_ID");
		// 车辆
		strSQL.append(",t1.EQP_TRUCK");
		strSQL.append(",tru.TD_ID");
		strSQL.append(",tru.TRUCK_NAME");
		strSQL.append(",tru.TRUCK_NO");
		strSQL.append(",tru.PLATE_NUM");
		strSQL.append(",tru.PLATE_COLOR");
		strSQL.append(",tru.ORG_ID as TRUCK_ORG");
		// 位置
		strSQL.append(",t.S_LAT");
		strSQL.append(",t.S_LON");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.s_cdate")
				+ " as s_cdate");
		strSQL.append(" from t_eqp_inst_geometry t,T_EQUIPMENT_INST t1 left outer join T_USER tu on t1.EQP_MUSER = tu.USER_INSTID");
		strSQL.append(" left outer join T_TRUCK_INST tru on t1.EQP_TRUCK = tru.TRUCK_ID");
		strSQL.append(" where t.EQP_INST=t1.EQP_INST ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.EQP_INST");
		}
		return strSQL;
	}

	// 物品定义对象
	private EquipmentGeometryPojo _setOneEqpGeometryPojo(ResultSet rs)
			throws Exception {
		EquipmentGeometryPojo onePojo = new EquipmentGeometryPojo();
		onePojo.setId(rs.getString("LOG_ID"));
		if (rs.getString("S_SPEED") != null) {
			onePojo.setSpeed(rs.getString("S_SPEED"));
		}
		if (rs.getString("S_FDID") != null) {
			onePojo.setFdId(rs.getString("S_FDID"));
		}
		if (rs.getString("S_LSDATE") != null) {
			onePojo.setLastStopDate(rs.getString("S_LSDATE"));
		}
		if (rs.getString("EQP_QR") != null) {
			onePojo.getEqpInst().setQrCode(rs.getString("EQP_QR"));
		}
		onePojo.setFanceFlg(rs.getInt("S_FANCEFLG"));
		onePojo.getEqpInst().setInstId(rs.getString("EQP_INST"));
		onePojo.getEqpInst().setWyCode(rs.getString("EQP_WYCODE"));
		onePojo.getEqpInst().setName(rs.getString("EQP_NAME"));
		// onePojo.getEqpInst().setState(rs.getInt("EQP_STATE"));
		// onePojo.getEqpInst().setOnlineState(rs.getInt("EQP_ONLINE"));
		if (rs.getString("EQP_QR") != null) {
			onePojo.getEqpInst().setQrCode(rs.getString("EQP_QR"));
		}
		// 管理员
		if (rs.getString("EQP_MUSER") != null) {
			onePojo.getEqpInst().getMangUser()
					.setInstId(rs.getString("EQP_MUSER"));
			if (rs.getString("USER_ID") != null) {
				onePojo.getEqpInst().getMangUser()
						.setId(rs.getString("USER_ID"));
			}
			if (rs.getString("USER_NAME") != null) {
				onePojo.getEqpInst().getMangUser()
						.setName(rs.getString("USER_NAME"));
			}
			if (rs.getString("MPHONE") != null) {
				onePojo.getEqpInst().getMangUser()
						.setmPhone(rs.getString("MPHONE"));
			}
		}

		// 机构
		if (rs.getString("ORG_ID") != null
				&& !rs.getString("ORG_ID").equals("")) {
			onePojo.getEqpInst()
					.getTruck()
					.setOrg(this.userBI.getGroupByRedis(rs.getString("ORG_ID")));
		}
		// 车辆
		if (rs.getString("EQP_TRUCK") != null) {
			onePojo.getEqpInst().getTruck().setId(rs.getString("EQP_TRUCK"));
			onePojo.getEqpInst()
					.getTruck()
					.setDefine(
							this.truckBI.getTruckDefByRedis(rs
									.getString("TD_ID")));
			if (rs.getString("TRUCK_NAME") != null) {
				onePojo.getEqpInst().getTruck()
						.setName(rs.getString("TRUCK_NAME"));
			}
			if (rs.getString("TRUCK_NO") != null) {
				onePojo.getEqpInst().getTruck().setNo(rs.getString("TRUCK_NO"));
			}
			if (rs.getString("PLATE_NUM") != null) {
				onePojo.getEqpInst().getTruck()
						.setPlateNum(rs.getString("PLATE_NUM"));
			}
			if (rs.getString("PLATE_COLOR") != null) {
				onePojo.getEqpInst().getTruck()
						.setPlateColor(rs.getInt("PLATE_COLOR"));
			}
			if (rs.getString("TRUCK_ORG") != null
					&& !rs.getString("TRUCK_ORG").equals("")) {
				onePojo.getEqpInst()
						.getTruck()
						.setOrg(this.userBI.getGroupByRedis(rs
								.getString("TRUCK_ORG")));
			}
		}
		if (rs.getString("EQP_LDATE") != null) {
			onePojo.getEqpInst().setLastLoginDate(rs.getString("EQP_LDATE"));
		}

		// 定义
		onePojo.getEqpInst().setEqpDef(
				this.eqpBI.getEqpDefByRedis(rs.getString("EQP_DEF")));
		// 位置
		onePojo.setLatitude(rs.getString("S_LAT"));
		onePojo.setLongitude(rs.getString("S_LON"));
		onePojo.setCreateDate(rs.getString("S_CDATE"));
		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getEquipmentInstWorkLogSelectSQL(String where,
			String orderBy) throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.LOG_ID");
		strSQL.append(",t.EQP_INST");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.S_CDATE")
				+ " as S_CDATE");
		strSQL.append(",t.WORK_STATE");
		strSQL.append(" from T_EQUIPMENT_WORKLOG t");
		strSQL.append(" where t.LOG_ID is not null ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.S_CDATE desc,t.EQP_INST");
		}
		return strSQL;
	}

	// 物品定义对象
	private EquipmentInstWorkLogPojo _setOneEquipmentInstWorkLogPojo(
			ResultSet rs) throws Exception {
		EquipmentInstWorkLogPojo onePojo = new EquipmentInstWorkLogPojo();
		onePojo.setId(rs.getString("LOG_ID"));
		onePojo.getEqpInst().setInstId(rs.getString("EQP_INST"));
		onePojo.setState(rs.getInt("WORK_STATE"));
		onePojo.setDate(rs.getString("S_CDATE"));
		return onePojo;
	}
}
