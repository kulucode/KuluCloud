package tt.kulu.bi.fault.dbclass;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.fault.pojo.FaultReportPojo;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BIEquipment;
import tt.kulu.out.call.BIFile;
import tt.kulu.out.call.BITruck;
import tt.kulu.out.call.BIUser;

import com.tt4j2ee.BSDateEx;
import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: FaultDBMang
 * </p>
 * <p>
 * 功能描述: 报警故障数据库操作类
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
public class FaultDBMang extends BSDBBase {
	private BIDic dicBI = null;
	private BIFile fileBI = null;
	private BIEquipment eqpBI = null;
	private BITruck truckBI = null;
	private BIUser userBI = null;

	public FaultDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
		this.dicBI = new BIDic(m_bs);
		this.eqpBI = new BIEquipment(m_bs);
		this.truckBI = new BITruck(m_bs);
		this.userBI = new BIUser(m_bs);
		this.fileBI = new BIFile(null, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getFaultId
	 * </p>
	 * <p>
	 * 方法功能描述: 得到需求ID。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 */
	public String getFaultId() throws Exception {
		String id = "FR-" + BSGuid.getRandomGUID();
		// ResultSet rs = this.sqlHelper
		// .queryBySql("SELECT nextval('kulu_faultid') as SEQ_ID");
		// if (rs != null && rs.next()) {
		// id = String
		// .format("%06d", Integer.parseInt(rs.getString("SEQ_ID")));
		// rs.close();
		// }
		// BSDateEx date = new BSDateEx();
		// id = "FR-"
		// + (((date.getThisDate(0, 0)).substring(0, 10)).replaceAll("-",
		// "") + id);
		return id;
	}

	/**
	 * <p>
	 * 方法名称: getFaultReportList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到企业。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public ArrayList<FaultReportPojo> getFaultReportList(String where,
			String orderBy, List<Object> vList, long f, long t)
			throws Exception {
		ArrayList<FaultReportPojo> list = new ArrayList<FaultReportPojo>();
		StringBuffer strSQL = this._getFaultReportSelectSQL(where, orderBy);
		strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneFaultReportPojo(rs, false));
			}
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getFaultReportCount
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
	public long getFaultReportCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.FR_REPORTID) as TAB_COUNT");
		strSQL.append(" from T_USER CU,T_FAULTREPORT t left outer join T_USER fru on t.FR_USER = fru.USER_INSTID");
		strSQL.append(" left outer join T_USER opu on t.OP_USER = opu.USER_INSTID");
		strSQL.append(" left outer join T_TRUCK_INST frt on t.FR_TRUCK = frt.TRUCK_ID");
		strSQL.append(" left outer join T_EQUIPMENT_INST fre on t.EQP_INST = fre.EQP_INST");
		strSQL.append(" where CU.USER_INSTID=t.C_USER ");
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
	 * 方法名称: getOneFaultReportById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个企业。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public FaultReportPojo getOneFaultReportById(String id) throws Exception {
		FaultReportPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		StringBuffer strSQL = this._getFaultReportSelectSQL(
				" and t.FR_REPORTID=?", "");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = (this._setOneFaultReportPojo(rs, true));

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
	public int insertFaultReport(FaultReportPojo onePojo) throws Exception {
		onePojo.setId(this.getFaultId());
		StringBuffer strSQL = new StringBuffer("insert into T_FAULTREPORT (");
		strSQL.append("FR_REPORTID");
		strSQL.append(",FR_NAME");
		strSQL.append(",EQP_INST");
		strSQL.append(",FR_TRUCK");
		strSQL.append(",FR_USER");
		strSQL.append(",FAULT_ID");
		strSQL.append(",FR_TYPE");
		strSQL.append(",FR_FROM");
		strSQL.append(",FR_CONTENT");
		strSQL.append(",FR_HAPPENTIME");
		strSQL.append(",FR_ENDTIME");
		strSQL.append(",C_USER");
		strSQL.append(",OP_USER");
		strSQL.append(",OP_RESULT");
		strSQL.append(",OP_STATE");
		strSQL.append(",OP_DATE");
		strSQL.append(") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		vList.add(onePojo.getName());
		vList.add(onePojo.getEqpInst().getInstId());
		vList.add(onePojo.getEqpInst().getTruck().getId());
		vList.add(onePojo.getFrUser().getInstId());
		vList.add(onePojo.getFaultCode().getId());
		vList.add(onePojo.getFaultType().getId());
		vList.add(onePojo.getFaultFrom().getId());
		vList.add(onePojo.getContent());
		vList.add(Timestamp.valueOf(onePojo.getHappenDate()));
		if (!onePojo.getEndDate().equals("")) {
			vList.add(Timestamp.valueOf(onePojo.getEndDate()));
		} else {
			vList.add(null);
		}
		vList.add(onePojo.getCreateUser().getInstId());
		vList.add(onePojo.getOpUser().getInstId());
		vList.add(onePojo.getOpDesc());
		vList.add(onePojo.getOpState());
		if (!onePojo.getOpDate().equals("")) {
			vList.add(Timestamp.valueOf(onePojo.getOpDate()));
		} else {
			vList.add(null);
		}
		return this.sqlHelper.updateBySql(strSQL.toString(), vList);
	}

	/**
	 * <p>
	 * 方法名称: updateFaultReport
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
	public int updateFaultReport(FaultReportPojo onePojo) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		if ((onePojo.getId() == null || onePojo.getId().trim().equals(""))
				|| this.sqlHelper
						.queryIntBySql(
								"select count(FR_REPORTID) from T_FAULTREPORT where FR_REPORTID=?",
								vList) <= 0) {
			// 新增
			count += this.insertFaultReport(onePojo);
		} else {
			StringBuffer strSQL = new StringBuffer("update T_FAULTREPORT set ");
			strSQL.append("FR_NAME=?");
			strSQL.append(",EQP_INST=?");
			strSQL.append(",FR_TRUCK=?");
			strSQL.append(",FR_USER=?");
			strSQL.append(",FAULT_ID=?");
			strSQL.append(",FR_TYPE=?");
			strSQL.append(",FR_FROM=?");
			strSQL.append(",FR_CONTENT=?");
			strSQL.append(",FR_HAPPENTIME=?");
			strSQL.append(",FR_ENDTIME=?");
			strSQL.append(",OP_USER=?");
			strSQL.append(",OP_RESULT=?");
			strSQL.append(",OP_STATE=?");
			strSQL.append(",OP_DATE=?");
			strSQL.append(" where FR_REPORTID=?");
			vList.clear();
			vList.add(onePojo.getName());
			vList.add(onePojo.getEqpInst().getInstId());
			vList.add(onePojo.getEqpInst().getTruck().getId());
			vList.add(onePojo.getFrUser().getInstId());
			vList.add(onePojo.getFaultCode().getId());
			vList.add(onePojo.getFaultType().getId());
			vList.add(onePojo.getFaultFrom().getId());
			vList.add(onePojo.getContent());
			vList.add(Timestamp.valueOf(onePojo.getHappenDate()));
			if (!onePojo.getEndDate().equals("")) {
				vList.add(Timestamp.valueOf(onePojo.getEndDate()));
			} else {
				vList.add(null);
			}
			vList.add(onePojo.getOpUser().getInstId());
			vList.add(onePojo.getOpDesc());
			vList.add(onePojo.getOpState());
			if (!onePojo.getOpDate().equals("")) {
				vList.add(Timestamp.valueOf(onePojo.getOpDate()));
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
	 * 方法名称: updateFaultReport
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
	public int updateFaultReportOP(FaultReportPojo onePojo) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();

		StringBuffer strSQL = new StringBuffer("update T_FAULTREPORT set ");
		strSQL.append("OP_USER=?");
		strSQL.append(",OP_RESULT=?");
		strSQL.append(",OP_STATE=?");
		strSQL.append(",OP_DATE=?");
		strSQL.append(" where FR_REPORTID=?");
		vList.add(onePojo.getOpUser().getInstId());
		vList.add(onePojo.getOpDesc());
		vList.add(onePojo.getOpState());
		if (!onePojo.getOpDate().equals("")) {
			vList.add(Timestamp.valueOf(onePojo.getOpDate()));
		} else {
			vList.add(null);
		}
		vList.add(onePojo.getId());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		return count;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getFaultReportSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.FR_REPORTID");
		strSQL.append(",t.FR_NAME");
		strSQL.append(",t.FAULT_ID");
		strSQL.append(",t.FR_TYPE");
		strSQL.append(",t.FR_FROM");
		strSQL.append(",t.FR_CONTENT");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.FR_HAPPENTIME")
				+ " as FR_HAPPENTIME");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.FR_ENDTIME")
				+ " as FR_ENDTIME");
		strSQL.append(",t.OP_RESULT");
		strSQL.append(",t.OP_STATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.OP_DATE")
				+ " as OP_DATE");
		// 提交人
		strSQL.append(",t.C_USER");
		strSQL.append(",CU.USER_ID as CU_USER_ID");
		strSQL.append(",CU.USER_NAME as CU_USER_NAME");
		strSQL.append(",CU.MPHONE as CU_MPHONE");
		strSQL.append(",CU.ORG_ID as CU_ORG");

		// 处理人
		strSQL.append(",t.OP_USER");
		strSQL.append(",opu.USER_ID as OPU_USER_ID");
		strSQL.append(",opu.USER_NAME as OPU_USER_NAME");
		strSQL.append(",opu.MPHONE as OPU_MPHONE");
		strSQL.append(",opu.ORG_ID as OPU_ORG");
		// 关联员工
		strSQL.append(",t.FR_USER");
		strSQL.append(",fru.USER_ID as FRU_USER_ID");
		strSQL.append(",fru.USER_NAME as FRU_USER_NAME");
		strSQL.append(",fru.MPHONE as FRU_MPHONE");
		strSQL.append(",fru.ORG_ID as FRU_ORG");
		// 关联车辆
		strSQL.append(",t.FR_TRUCK");
		strSQL.append(",frt.TRUCK_NAME");
		strSQL.append(",frt.TRUCK_INNAME");
		strSQL.append(",frt.TRUCK_NO");
		strSQL.append(",frt.PLATE_NUM");
		strSQL.append(",frt.TD_ID");
		strSQL.append(",frt.ORG_ID as FRT_ORG");
		// 关联设备
		strSQL.append(",t.EQP_INST");
		strSQL.append(",fre.EQP_WYCODE");
		strSQL.append(",fre.EQP_NAME");
		strSQL.append(",fre.EQP_DEF");

		strSQL.append(" from T_USER CU,T_FAULTREPORT t left outer join T_USER fru on t.FR_USER = fru.USER_INSTID");
		strSQL.append(" left outer join T_USER opu on t.OP_USER = opu.USER_INSTID");
		strSQL.append(" left outer join T_TRUCK_INST frt on t.FR_TRUCK = frt.TRUCK_ID");
		strSQL.append(" left outer join T_EQUIPMENT_INST fre on t.EQP_INST = fre.EQP_INST");
		strSQL.append(" where CU.USER_INSTID=t.C_USER ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by" + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private FaultReportPojo _setOneFaultReportPojo(ResultSet rs, boolean isAll)
			throws Exception {
		FaultReportPojo onePojo = new FaultReportPojo();
		onePojo.setId(rs.getString("FR_REPORTID"));
		onePojo.setName(rs.getString("FR_NAME"));
		onePojo.setFaultCode(this.dicBI.getDicItemByRedis(rs
				.getString("FAULT_ID")));
		onePojo.setFaultType(this.dicBI.getDicItemByRedis(rs
				.getString("FR_TYPE")));
		onePojo.setFaultFrom(this.dicBI.getDicItemByRedis(rs
				.getString("FR_FROM")));
		if (rs.getString("FR_CONTENT") != null) {
			onePojo.setContent(rs.getString("FR_CONTENT"));
		}
		onePojo.setHappenDate(rs.getString("FR_HAPPENTIME"));
		if (rs.getString("FR_ENDTIME") != null) {
			onePojo.setEndDate(rs.getString("FR_ENDTIME"));
		}
		if (rs.getString("OP_RESULT") != null) {
			onePojo.setOpDesc(rs.getString("OP_RESULT"));
		}
		if (rs.getString("OP_DATE") != null) {
			onePojo.setOpDate(rs.getString("OP_DATE"));
		}
		onePojo.setOpState(rs.getInt("OP_STATE"));
		// 提交人
		if (rs.getString("C_USER") != null
				&& !rs.getString("C_USER").equals("")) {
			onePojo.getCreateUser().setInstId(rs.getString("C_USER"));
			onePojo.getCreateUser().setId(rs.getString("CU_USER_ID"));
			onePojo.getCreateUser().setName(rs.getString("CU_USER_NAME"));
			if (rs.getString("CU_MPHONE") != null
					&& !rs.getString("CU_MPHONE").equals("")) {
				onePojo.getCreateUser().setmPhone(rs.getString("CU_MPHONE"));
			}
		}
		if (rs.getString("CU_ORG") != null
				&& !rs.getString("CU_ORG").equals("")) {
			onePojo.getCreateUser().setOrg(
					this.userBI.getGroupByRedis(rs.getString("CU_ORG")));
		}
		// 处理人
		if (rs.getString("OP_USER") != null
				&& !rs.getString("OP_USER").equals("")) {
			onePojo.getOpUser().setInstId(rs.getString("OP_USER"));
			onePojo.getOpUser().setId(rs.getString("OPU_USER_ID"));
			onePojo.getOpUser().setName(rs.getString("OPU_USER_NAME"));
			if (rs.getString("OPU_MPHONE") != null) {
				onePojo.getOpUser().setmPhone(rs.getString("OPU_MPHONE"));
			}
			if (rs.getString("OPU_ORG") != null) {
				onePojo.getOpUser().setOrg(
						this.userBI.getGroupByRedis(rs.getString("OPU_ORG")));
			}
		}
		// 关联员工
		if (rs.getString("FR_USER") != null
				&& !rs.getString("FR_USER").equals("")) {
			onePojo.getFrUser().setInstId(rs.getString("FR_USER"));
			onePojo.getFrUser().setId(rs.getString("FRU_USER_ID"));
			onePojo.getFrUser().setName(rs.getString("FRU_USER_NAME"));
			if (rs.getString("FRU_USER_NAME") != null) {
				onePojo.getFrUser().setmPhone(rs.getString("FRU_MPHONE"));
			}
			if (rs.getString("FRU_ORG") != null
					&& !rs.getString("FRU_ORG").equals("")) {
				onePojo.getFrUser().setOrg(
						this.userBI.getGroupByRedis(rs.getString("FRU_ORG")));
			}
		}

		// 关联设备
		onePojo.getEqpInst().setInstId(rs.getString("EQP_INST"));
		onePojo.getEqpInst().setName(rs.getString("EQP_NAME"));
		onePojo.getEqpInst().setWyCode(rs.getString("EQP_WYCODE"));
		onePojo.getEqpInst().setEqpDef(
				this.eqpBI.getEqpDefByRedis(rs.getString("EQP_DEF")));

		// 管理车辆
		if (rs.getString("FR_TRUCK") != null
				&& !rs.getString("FR_TRUCK").equals("")) {
			onePojo.getEqpInst().getTruck().setId(rs.getString("FR_TRUCK"));
			onePojo.getEqpInst().getTruck().setName(rs.getString("TRUCK_NAME"));
			onePojo.getEqpInst().getTruck().setInName(rs.getString("TRUCK_INNAME"));
			if (rs.getString("TRUCK_NO") != null) {
				onePojo.getEqpInst().getTruck().setNo(rs.getString("TRUCK_NO"));
			}
			if (rs.getString("PLATE_NUM") != null) {
				onePojo.getEqpInst().getTruck()
						.setPlateNum(rs.getString("PLATE_NUM"));
			}
			if (rs.getString("FRT_ORG") != null) {
				onePojo.getEqpInst()
						.getTruck()
						.setOrg(this.userBI.getGroupByRedis(rs
								.getString("FRT_ORG")));
			}
			onePojo.getEqpInst()
					.getTruck()
					.setDefine(
							this.truckBI.getTruckDefByRedis(rs
									.getString("TD_ID")));
		}
		// 文件
		if (isAll) {
			JSONObject _paras = new JSONObject();
			_paras.put("bid", onePojo.getId() + "_FILES");
			onePojo.setFaultFiles(this.fileBI.getFileList(_paras, 0, 100));
			_paras.put("bid", onePojo.getId() + "_OPFILES");
			onePojo.setOpFiles(this.fileBI.getFileList(_paras, 0, 100));
		}
		return onePojo;
	}
}
