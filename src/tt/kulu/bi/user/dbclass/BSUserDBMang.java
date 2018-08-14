package tt.kulu.bi.user.dbclass;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.user.pojo.OrgPojo;
import tt.kulu.bi.user.pojo.UserOrgRPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.bi.user.pojo.UserSchedulingPojo;
import tt.kulu.bi.user.pojo.UserWorkDayLogsPojo;
import tt.kulu.bi.user.pojo.UserWorkParasMinPojo;
import tt.kulu.bi.user.pojo.UserWorkParasPojo;
import tt.kulu.out.call.BIArea;
import tt.kulu.out.call.BICompany;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BIUser;
import tt.kulu.out.call.BIWatch;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.BSDateEx;
import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;
import com.tt4j2ee.md5.MD5Imp;

/**
 * <p>
 * 标题: BSUserDBMang
 * </p>
 * <p>
 * 功能描述: 用户数据库操作类
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
public class BSUserDBMang extends BSDBBase {
	private BICompany compBI = null;
	private BIArea areaBI = null;
	private BIDic dicBI = null;
	private BIUser userBI = null;

	public BSUserDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
		this.compBI = new BICompany(sqlHelper, m_bs);
		this.areaBI = new BIArea(sqlHelper, m_bs);
		this.dicBI = new BIDic(m_bs);
		this.userBI = new BIUser(m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getDemandId
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
	public String getUserInstId() throws Exception {
		String id = "";
		CachedRowSet rs = this.sqlHelper
				.queryCachedBySql("SELECT nextval('seq_userid') as DEM_ID");
		if (rs != null && rs.next()) {
			id = String
					.format("%06d", Integer.parseInt(rs.getString("DEM_ID")));
			rs.close();
		}
		BSDateEx date = new BSDateEx();
		id = BSCommon.getConfigValue("userconfig_loginobj_pre")
				+ "-"
				+ (((date.getThisDate(0, 0)).substring(0, 10)).replaceAll("-",
						"") + id);
		return id;
	}

	/**
	 * <p>
	 * 方法名称: getOrgList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到群组列表。
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
	public ArrayList<OrgPojo> getOrgList(String where, List<Object> vList,
			String orderBy, long f, long t) throws Exception {
		ArrayList<OrgPojo> list = new ArrayList<OrgPojo>();
		StringBuffer strSQL = _getOrgSelectSQL(where, orderBy);
		strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString(),
				vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneOrgPojo(rs, false));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgById
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
	public OrgPojo getOneOrgById(String orgId) throws Exception {
		OrgPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(orgId);
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(this
				._getOrgSelectSQL(" and t.ORG_ID=?", "").toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = this._setOneOrgPojo(rs, true);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgById
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
	public OrgPojo getOneOrgByName(String name) throws Exception {
		OrgPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(name);
		ResultSet rs = this.sqlHelper.queryBySql(
				this._getOrgSelectSQL(" and t.ORG_NAME=?", "").toString(),
				vList);
		if (rs != null && rs.next()) {
			onePojo = this._setOneOrgPojo(rs, true);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgById
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
	public OrgPojo getOneOrgByName(String name, int type) throws Exception {
		OrgPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(name);
		vList.add(type);
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(this
				._getOrgSelectSQL(" and t.ORG_NAME=? and t.ORG_TYPE=?", "")
				.toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = this._setOneOrgPojo(rs, true);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgById
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
	public OrgPojo getOneOrgByName(String pid, String name, int type)
			throws Exception {
		OrgPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(pid);
		vList.add(name);
		vList.add(type);
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(
				this._getOrgSelectSQL(
						" and t.ORG_PID=? and t.ORG_NAME=? and t.ORG_TYPE=?",
						"").toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = this._setOneOrgPojo(rs, true);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgByUser
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
	public OrgPojo getOneOrgByUser(String user) throws Exception {
		OrgPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(user);
		CachedRowSet rs = this.sqlHelper
				.queryCachedBySql(
						this._getOrgSelectSQL(
								" and t.ORG_ID in (select v.ORG_ID from T_USER v where v.USER_INSTID=?)",
								"").toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = this._setOneOrgPojo(rs, true);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgById
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
	public OrgPojo getOneOrgByName(String pid, String name) throws Exception {
		OrgPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(name);
		CachedRowSet rs = null;
		if (pid.equals("")) {
			rs = this.sqlHelper.queryCachedBySql(
					this._getOrgSelectSQL(
							" and t.ORG_NAME=? and t.ORG_PID=t.ORG_ID", "")
							.toString(), vList);
		} else {
			vList.add(pid);
			rs = this.sqlHelper.queryCachedBySql(
					this._getOrgSelectSQL(" and t.ORG_NAME=? and t.ORG_PID=?",
							"").toString(), vList);
		}

		if (rs != null && rs.next()) {
			onePojo = this._setOneOrgPojo(rs, true);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOrgCount
	 * </p>
	 * <p>
	 * 方法功能描述: 得到群组数量。
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
	public long getOrgCount(String where, List<Object> vList) throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.ORG_ID) as OBJ_COUNT");
		strSQL.append(" from T_ORG t,T_USER t3");
		strSQL.append(" where t.C_USER=t3.USER_INSTID");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(where);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			count = rs.getLong("OBJ_COUNT");
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertGroup
	 * </p>
	 * <p>
	 * 方法功能描述: 添加群组信息。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int insertOrg(OrgPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			// 新增资源实例表
			if (onePojo.getId().equals("")) {
				onePojo.setId(BSGuid.getRandomGUID());
			}
			// 得到上级
			if (onePojo.getPorgId().equals("")
					|| onePojo.getPorgId().equals("root")) {
				onePojo.setPorgId(onePojo.getId());
				onePojo.setAllOrgId("," + onePojo.getId());
				onePojo.setAllName(onePojo.getName());
			} else {
				OrgPojo pOrg = this.getOneOrgById(onePojo.getPorgId());
				if (pOrg != null) {
					onePojo.setAllOrgId(pOrg.getAllOrgId() + ","
							+ onePojo.getId());
					onePojo.setAllName(pOrg.getAllName() + ","
							+ onePojo.getName());
				}
			}
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("insert into T_ORG (");
			strSQL.append("ORG_ID");
			strSQL.append(",ORG_PID");
			strSQL.append(",ORG_NAME");
			strSQL.append(",C_USER");
			strSQL.append(",C_DATE");
			strSQL.append(",ORG_DESC");
			strSQL.append(",ORG_ALLID");
			strSQL.append(",ORG_ALLNAME");
			strSQL.append(",COMP_ID");
			strSQL.append(",A_ID");
			strSQL.append(",ORG_TYPE");
			strSQL.append(",S_LAT");
			strSQL.append(",S_LON");
			if (onePojo.getLatitude() != "" && onePojo.getLongitude() != "") {
				// 有地理位置
				strSQL.append(",S_GEOMETRY");
			}
			strSQL.append(") values (?,?,?,?,?,?,?,?,?,?,?,?,?");
			if (onePojo.getLatitude() != "" && onePojo.getLongitude() != "") {
				// 有地理位置
				strSQL.append(",ST_GeomFromText('POINT("
						+ onePojo.getLongitude() + " " + onePojo.getLatitude()
						+ ")',4326)");
			}
			strSQL.append(")");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getId());
			vList.add(onePojo.getPorgId());
			vList.add(onePojo.getName());
			vList.add(onePojo.getCreateStaff().getInstId());
			vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
			vList.add(onePojo.getDesc());
			vList.add(onePojo.getAllOrgId());
			vList.add(onePojo.getAllName());
			vList.add(onePojo.getCompany().getId());
			vList.add(onePojo.getArea().getId());
			vList.add(onePojo.getType());
			vList.add(onePojo.getLatitude());
			vList.add(onePojo.getLongitude());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateOrg
	 * </p>
	 * <p>
	 * 方法功能描述: 修改角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int updateOrg(OrgPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			// 得到上级
			if (onePojo.getPorgId() == null || onePojo.getPorgId().equals("")
					|| onePojo.getPorgId().equals(onePojo.getId())) {
				onePojo.setPorgId(onePojo.getId());
				onePojo.setAllOrgId("," + onePojo.getId());
				onePojo.setAllName(onePojo.getName());
			} else {
				OrgPojo pOrg = this.getOneOrgById(onePojo.getPorgId());
				if (pOrg != null) {
					onePojo.setAllOrgId(pOrg.getAllOrgId() + ","
							+ onePojo.getId());
					onePojo.setAllName(pOrg.getAllName() + ","
							+ onePojo.getName());
				}
			}

			StringBuffer strSQL = new StringBuffer();
			strSQL.append("update T_ORG set ");
			strSQL.append("ORG_PID=?");
			strSQL.append(",ORG_NAME=?");
			strSQL.append(",ORG_DESC=?");
			strSQL.append(",ORG_ALLID=?");
			strSQL.append(",ORG_ALLNAME=?");
			strSQL.append(",COMP_ID=?");
			strSQL.append(",A_ID=?");
			strSQL.append(",ORG_TYPE=?");
			strSQL.append(",S_LAT=?");
			strSQL.append(",S_LON=?");
			if (onePojo.getLatitude() != "" && onePojo.getLongitude() != "") {
				// 有地理位置
				strSQL.append(",S_GEOMETRY=ST_GeomFromText('POINT("
						+ onePojo.getLongitude() + " " + onePojo.getLatitude()
						+ ")',4326)");
			}
			strSQL.append(" where ORG_ID=?");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getPorgId());
			vList.add(onePojo.getName());
			vList.add(onePojo.getDesc());
			vList.add(onePojo.getAllOrgId());
			vList.add(onePojo.getAllName());
			vList.add(onePojo.getCompany().getId());
			vList.add(onePojo.getArea().getId());
			vList.add(onePojo.getType());
			vList.add(onePojo.getLatitude());
			vList.add(onePojo.getLongitude());
			vList.add(onePojo.getId());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);

		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateOrg
	 * </p>
	 * <p>
	 * 方法功能描述: 修改角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int deleteOneOrg(String orgId) throws Exception {
		int count = 0;
		if (orgId != null && !orgId.equals("")) {
			// 更新该用户到上级目录
			OrgPojo onePojo = this.getOneOrgById(orgId);
			OrgPojo hszO = this.getOneOrgById("UNKNOWN_GROUP");
			if (hszO == null) {
				// 如果没有回收站，则新增
				hszO = new OrgPojo();
				hszO.setId("UNKNOWN_GROUP");
				hszO.setName("删除机构回收站");
				hszO.setAllName("无");
				hszO.setPorgId("UNKNOWN_GROUP");
				hszO.setCompany((new BICompany(null, null))
						.getThisCompanyByRedis());
				hszO.getArea().setId("430000");
				hszO.getCreateStaff().setInstId("SUPER_ADMIN");
				hszO.setCreateDate(this.bsDate.getThisDate(0, 0));
				this.insertOrg(hszO);
			}
			if (onePojo != null) {
				List<Object> vList = new ArrayList<Object>();
				if (onePojo.getId().equals(onePojo.getPorgId())) {
					// 跟机构，员工移植到deleteorg目录
					vList.add("UNKNOWN_GROUP");
				} else {
					vList.add(onePojo.getPorgId());
				}
				vList.add(orgId);
				count = sqlHelper.updateBySql(
						"update T_ORG_USER_R set ORG_ID=? where ORG_ID=?",
						vList);
				count = sqlHelper.updateBySql(
						"update T_EQUIPMENT_INST set ORG_ID=? where ORG_ID=?",
						vList);
				count = sqlHelper.updateBySql(
						"update T_TRUCK_INST set ORG_ID=? where ORG_ID=?",
						vList);
				count = sqlHelper.updateBySql(
						"update T_FANCE set ORG_ID=? where ORG_ID=?", vList);
				count = sqlHelper.updateBySql(
						"update T_USER set ORG_ID=? where ORG_ID=?", vList);
				// 删除
				vList.clear();
				vList.add(orgId);
				count += sqlHelper.updateBySql(
						"delete from T_ORG where ORG_ID=?", vList);
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateOrg
	 * </p>
	 * <p>
	 * 方法功能描述: 修改角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int updateUserOrgR(UserOrgRPojo onePojo, int type) throws Exception {
		int count = 0;
		if (onePojo != null) {
			// 删除关系
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getOrg().getId());
			vList.add(onePojo.getUser().getInstId());
			vList.add(String.valueOf(onePojo.getType()));
			count += sqlHelper
					.updateBySql(
							"delete from T_ORG_USER_R where ORG_ID=? and USER_INSTID=? and R_TYPE=?",
							vList);
			if (type == 1) {
				// 新增关系
				count += sqlHelper
						.updateBySql(
								"insert into T_ORG_USER_R (ORG_ID,USER_INSTID,R_TYPE) values (?,?,?)",
								vList);
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getUserRS
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
	public ArrayList<UserPojo> getUserList(String where, String orderBy,
			List<Object> vList, long f, long t) throws Exception {
		// 翻页代码
		ArrayList<UserPojo> list = new ArrayList<UserPojo>();
		StringBuffer strSQL = new StringBuffer(this._getUserSelectSQL(where,
				orderBy));
		if (f >= 0 && t > 0) {
			strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneUserPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getUserCount
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
	public long getUserCount(String where, List<Object> vList) throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.USER_INSTID) as TAB_COUNT");
		strSQL.append(" from T_USER t");
		strSQL.append(" where t.USER_INSTID is not null");
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
	 * 方法名称: getOneUser
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个用户。
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
	public UserPojo getOneUser(String where, List<Object> vList)
			throws Exception {
		UserPojo onePojo = null;
		StringBuffer strSQL = this._getUserSelectSQL(where, "");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = this._setOneUserPojo(rs);
			rs.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneUserById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个用户。
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
	public UserPojo getOneUserByInstId(String instId) throws Exception {
		UserPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(instId);
		StringBuffer strSQL = this
				._getUserSelectSQL(" and t.USER_INSTID=?", "");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = this._setOneUserPojo(rs);
			rs.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneUserById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个用户。
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
	public UserPojo getOneUserById(String id) throws Exception {
		UserPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		if (id != null && !id.equals("")) {
			vList.add(id);
			vList.add(id);
			vList.add(id);
			StringBuffer strSQL = this
					._getUserSelectSQL(
							" and (t.USER_ID=? OR t.USER_IDCARD=? OR t.MPHONE=?) and t.USER_STATE=1",
							"");
			ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
			if (rs != null && rs.next()) {
				onePojo = this._setOneUserPojo(rs);
				rs.close();
			}
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneUserById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个用户。
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
	public UserPojo getOneUserByIdNotState(String id) throws Exception {
		UserPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		vList.add(id);
		vList.add(id);
		StringBuffer strSQL = this._getUserSelectSQL(
				" and (t.USER_ID=? OR t.USER_IDCARD=? OR t.MPHONE=?)", "");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = this._setOneUserPojo(rs);
			rs.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneUserById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个用户。
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
	public JSONArray getOneOrgMang(String orgId) throws Exception {
		JSONArray list = new JSONArray();
		List<Object> vList = new ArrayList<Object>();
		vList.add(orgId);
		String allOrg = (String) this.sqlHelper.queryObjectBySql(
				"select t.ORG_ALLID from T_ORG t where t.ORG_ID=?", vList);
		if (allOrg != null && !allOrg.trim().equals("")) {
			String orgs[] = allOrg.split(",");
			for (String oneSub : orgs) {
				if (!oneSub.equals("")) {
					vList.clear();
					vList.add(oneSub);
					ArrayList<UserOrgRPojo> ugList = this.getUserOrgList(
							" and t.R_TYPE='1' and t.ORG_ID=?", vList, "");
					for (UserOrgRPojo onePojo : ugList) {
						JSONObject oneUG = new JSONObject();
						oneUG.put("userinst", onePojo.getUser().getInstId());
						oneUG.put("userid", onePojo.getUser().getId());
						oneUG.put("username", onePojo.getUser().getName());
						oneUG.put("userphone", onePojo.getUser().getmPhone());
						oneUG.put("orgid", onePojo.getOrg().getId());
						oneUG.put("orgname", onePojo.getOrg().getName());
						list.add(0, oneUG);
					}
				}
			}
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: insertUser
	 * </p>
	 * <p>
	 * 方法功能描述: 添加用户信息。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：用户信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int insertUser(UserPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getId());
			if (this.sqlHelper
					.queryIntBySql(
							"select count(t.USER_INSTID) from T_USER t where t.USER_ID=?",
							vList) <= 0) {
				if (onePojo.getInstId().equals("")) {
					onePojo.setInstId(BSGuid.getRandomGUID());
				}
				if (onePojo.getCreateDate().equals("")) {
					onePojo.setCreateDate(this.bsDate.getThisDate(0, 0));
				}

				StringBuffer strSQL = new StringBuffer();
				strSQL.append("insert into T_USER (");
				strSQL.append("USER_INSTID");
				strSQL.append(",USER_ID");
				strSQL.append(",USER_STATE");
				strSQL.append(",USER_NAME");
				strSQL.append(",USER_KEY");
				strSQL.append(",USER_SEX");
				strSQL.append(",EMAIL");
				strSQL.append(",C_DATE");
				strSQL.append(",UPDATE_DATE");
				strSQL.append(",MPHONE");
				strSQL.append(",USER_IDCARD");
				strSQL.append(",USER_BIRTHDAY");
				strSQL.append(",ORG_ID");
				strSQL.append(") values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
				vList.clear();
				vList.add(onePojo.getInstId());
				vList.add(onePojo.getId());
				vList.add(onePojo.getState());
				vList.add(onePojo.getName());
				vList.add(MD5Imp.enCode(onePojo.getPassword()));
				vList.add(onePojo.getSex());
				vList.add(onePojo.getEmail());
				vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
				vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
				vList.add(onePojo.getmPhone());
				vList.add(onePojo.getIdCard());
				if (onePojo.getBirthday().equals("")) {
					vList.add(null);
				} else {
					vList.add(Timestamp.valueOf(onePojo.getBirthday()));
				}
				if (onePojo.getOrg() != null) {
					vList.add(onePojo.getOrg().getId());
				} else {
					vList.add("");
				}
				count = sqlHelper.updateBySql(strSQL.toString(), vList);
				// 增加机构关系
				if (onePojo.getOrg() != null) {
					UserOrgRPojo oneOrgPojo = new UserOrgRPojo(
							onePojo.getInstId(), onePojo.getOrg().getId(),
							onePojo.getUgType());
					this.updateUserOrgR(oneOrgPojo, 1);
				}
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateUserBase
	 * </p>
	 * <p>
	 * 方法功能描述: 添加用户信息。
	 * </p>
	 * <p>
	 * 输入参数描述: UserPojo onePojo：用户信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateUser(UserPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getId());
			vList.add(onePojo.getInstId());
			if (this.sqlHelper
					.queryIntBySql(
							"select count(t.USER_INSTID) from T_USER t where t.USER_ID=? and t.USER_INSTID<>?",
							vList) <= 0) {
				if (onePojo.getUpdateDate().equals("")) {
					onePojo.setUpdateDate(this.bsDate.getThisDate(0, 0));
				}
				// 新增资源实例表
				StringBuffer strSQL = new StringBuffer();
				strSQL.append("update T_USER set ");
				strSQL.append("USER_ID=?");
				strSQL.append(",USER_STATE=?");
				strSQL.append(",USER_NAME=?");
				strSQL.append(",USER_SEX=?");
				strSQL.append(",EMAIL=?");
				strSQL.append(",UPDATE_DATE=?");
				strSQL.append(",MPHONE=?");
				strSQL.append(",USER_IDCARD=?");
				strSQL.append(",USER_BIRTHDAY=?");
				strSQL.append(",ORG_ID=?");
				strSQL.append(" where USER_INSTID=?");
				vList.clear();
				vList.add(onePojo.getId());
				vList.add(onePojo.getState());
				vList.add(onePojo.getName());
				vList.add(onePojo.getSex());
				vList.add(onePojo.getEmail());
				vList.add(Timestamp.valueOf(onePojo.getUpdateDate()));
				vList.add(onePojo.getmPhone());
				vList.add(onePojo.getIdCard());
				if (onePojo.getBirthday().equals("")) {
					vList.add(null);
				} else {
					vList.add(Timestamp.valueOf(onePojo.getBirthday()));
				}
				if (onePojo.getOrg() != null) {
					vList.add(onePojo.getOrg().getId());
				} else {
					vList.add("");
				}
				vList.add(onePojo.getInstId());
				count = sqlHelper.updateBySql(strSQL.toString(), vList);
				// 增加机构关系
				vList.clear();
				vList.add(onePojo.getInstId());
				count += sqlHelper
						.updateBySql(
								"delete from T_ORG_USER_R where USER_INSTID=? and R_TYPE='0'",
								vList);
				vList.add(onePojo.getOrg().getId());
				count += sqlHelper
						.updateBySql(
								"delete from T_ORG_USER_R where USER_INSTID=? and ORG_ID=?",
								vList);
				if (onePojo.getOrg() != null) {
					UserOrgRPojo oneOrgPojo = new UserOrgRPojo(
							onePojo.getInstId(), onePojo.getOrg().getId(),
							onePojo.getUgType());
					this.updateUserOrgR(oneOrgPojo, 1);
				}
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateStaffKey
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
	public int updateUserKey(String userId, String key) throws Exception {
		int count = 0;
		if (userId != null && !userId.trim().equals("")) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(key);
			vList.add(Timestamp.valueOf(this.bsDate.getThisDate(0, 0)));
			vList.add(userId);
			String sql = "update T_USER set USER_KEY=?,update_date=? where USER_INSTID=?";
			count += sqlHelper.updateBySql(sql, vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateUserPhone
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
	public int updateUserPhone(String userId, String phone) throws Exception {
		int count = 0;
		if (userId != null && !userId.trim().equals("")) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(phone);
			count += sqlHelper.updateBySql(
					"update T_USER set MPHONE='' where MPHONE=?", vList);
			vList.clear();
			vList.add(phone);
			vList.add(Timestamp.valueOf(this.bsDate.getThisDate(0, 0)));
			vList.add(userId);
			String sql = "update T_USER set MPHONE=?,update_date=? where USER_INSTID=?";
			count += sqlHelper.updateBySql(sql, vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateUserPhone
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
	public int updateOneUserState(String userId, int state) throws Exception {
		int count = 0;
		if (userId != null && !userId.trim().equals("")) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(state);
			vList.add(userId);
			count += sqlHelper
					.updateBySql(
							"update T_USER set USER_STATE=? where USER_INSTID=?",
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
	public int deleteOneUser(String userId) throws Exception {
		int count = 0;

		if (userId != null && !userId.trim().equals("")) {
			UserPojo oneUser = this.getOneUserByInstId("UNKNOWN_USER");
			if (oneUser == null) {
				oneUser = new UserPojo();
				oneUser.setInstId("UNKNOWN_USER");
				oneUser.setState(0);
				oneUser.setId("UNKNOWN_USER");
				oneUser.setName("未知用户");
				oneUser.setPassword("0");
				oneUser.setGroupId("ADMIN_GROUP");
				oneUser.setCreateDate(this.bsDate.getThisDate(0, 0));
				this.insertUser(oneUser);
			}
			if (oneUser != null) {
				List<Object> vList = new ArrayList<Object>();
				vList.add(userId);
				// 设备
				count += sqlHelper
						.updateBySql(
								"update T_EQUIPMENT_INST set EQP_MUSER='UNKNOWN_USER' where EQP_MUSER=?",
								vList);
				// 工作日志
				count += sqlHelper
						.updateBySql(
								"delete from T_USER_WORK_DAY_LOGS where USER_INSTID=?",
								vList);
				count += sqlHelper.updateBySql(
						"delete from T_USER_SCHEDULING where USER_INSTID=?",
						vList);
				// 故障
				count += sqlHelper
						.updateBySql(
								"update T_FAULTREPORT set FR_USER='UNKNOWN_USER' where FR_USER=?",
								vList);
				count += sqlHelper
						.updateBySql(
								"update T_FAULTREPORT set C_USER='UNKNOWN_USER' where C_USER=?",
								vList);
				count += sqlHelper
						.updateBySql(
								"update T_FAULTREPORT set C_USER='UNKNOWN_USER' where OP_USER=?",
								vList);
				// 围栏
				count += sqlHelper
						.updateBySql(
								"delete from T_FANCE_USER_R where USER_INSTID=?",
								vList);
				// 车辆
				count += sqlHelper
						.updateBySql(
								"update T_TRUCK_INST set TRUCK_MANGUSER='UNKNOWN_USER' where TRUCK_MANGUSER=?",
								vList);
				count += sqlHelper
						.updateBySql(
								"update T_TRUCK_FIX_LOGS set LOG_USER='UNKNOWN_USER' where LOG_USER=?",
								vList);
				count += sqlHelper
						.updateBySql(
								"update T_INSPECT_PLAN set PLAN_OPUSER='UNKNOWN_USER' where PLAN_OPUSER=?",
								vList);
				// 日志
				count += sqlHelper.updateBySql(
						"delete from T_SYS_LOGS where C_USER=?", vList);

				// 用户
				count += sqlHelper
						.updateBySql(
								"update T_FILE set U_USER='UNKNOWN_USER' where U_USER=?",
								vList);
				count += sqlHelper.updateBySql(
						"delete from T_ROLE_USER_R where USER_INSTID=?", vList);
				count += sqlHelper.updateBySql(
						"delete from T_ORG_USER_R where USER_INSTID=?", vList);

				count += sqlHelper.updateBySql(
						"delete from T_USER  where USER_INSTID=?", vList);

			}
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
	public ArrayList<UserWorkParasPojo> getUserWordParasList(String where,
			String orderBy, List<Object> vList, long f, long t)
			throws Exception {
		// 翻页代码
		ArrayList<UserWorkParasPojo> list = new ArrayList<UserWorkParasPojo>();
		StringBuffer strSQL = new StringBuffer(this._getUserWordParasSelectSQL(
				where, orderBy));
		if (f >= 0 && t > 0) {
			strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneUserWordParasPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getUserWordParasCount
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
	public long getUserWordParasCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.eqp_inst) as TAB_COUNT");
		strSQL.append(" from T_USER us, V_USER_WORK_PARAS t left outer join T_USER_WORK_DAY_LOGS t1 on t.eqp_muser = t1.user_instid and t1.log_date=current_date");
		strSQL.append(" where t.eqp_muser=us.user_instid");
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
	 * 方法名称: getOrgList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到群组列表。
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
	public ArrayList<UserOrgRPojo> getUserOrgList(String where,
			List<Object> vList, String orderBy) throws Exception {
		ArrayList<UserOrgRPojo> list = new ArrayList<UserOrgRPojo>();
		StringBuffer strSQL = _getUserOrgSelectSQL(where, orderBy);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneUserOrgPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getUserWorkDayLogsList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到员工当日日志列表。
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
	public ArrayList<UserWorkDayLogsPojo> getUserWorkDayLogsList(
			JSONObject where, List<Object> vList, String orderBy, long f, long t)
			throws Exception {
		ArrayList<UserWorkDayLogsPojo> list = new ArrayList<UserWorkDayLogsPojo>();
		StringBuffer strSQL = _getUserWorkDayLogsSelectSQL(where, orderBy);
		if (f >= 0 && t > 0) {
			strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneUserWorkDayLogsPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getOrgCount
	 * </p>
	 * <p>
	 * 方法功能描述: 得到群组数量。
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
	public long getUserWorkDayLogsCount(JSONObject where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t1.user_id) as OBJ_COUNT");
		strSQL.append(" from T_USER t1 left outer join T_USER_WORK_DAY_LOGS t on t.USER_INSTID = t1.user_instid ");
		if (where != null && where.containsKey("logs")) {
			strSQL.append(where.getString("logs"));
		}
		strSQL.append(" where t1.USER_INSTID is not null ");

		if (where != null && where.containsKey("user")) {
			strSQL.append(where.getString("user"));
		}

		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			count = rs.getLong("OBJ_COUNT");
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getOneUserWorkDayLogsId
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
	public UserWorkDayLogsPojo getOneUserWorkDayLogsId(String id)
			throws Exception {
		UserWorkDayLogsPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		JSONObject where = new JSONObject();
		where.put("user", " and t.LOG_ID=?");
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(this
				._getUserWorkDayLogsSelectSQL(where, "").toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = this._setOneUserWorkDayLogsPojo(rs);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: updateUserWorkDayLogs
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
	public int updateUserWorkDayLogs(UserWorkDayLogsPojo onePojo)
			throws Exception {
		int count = 0;
		if (onePojo != null) {
			StringBuffer strSQL = new StringBuffer();
			List<Object> vList = new ArrayList<Object>();
			UserWorkDayLogsPojo oneOldPojo = this
					.getOneUserWorkDayLogsId(onePojo.getId());
			if (oneOldPojo == null || oneOldPojo.getDate() == null) {
				// 新增
				strSQL.append("insert into T_USER_WORK_DAY_LOGS (LOG_ID,USER_INSTID,LOG_DATE,LOG_STATE,LOG_LATEFLG");
				strSQL.append(") values (?,?,?,?,?)");
				vList.clear();
				vList.add(onePojo.getId());
				vList.add(onePojo.getUser().getInstId());
				vList.add(Timestamp.valueOf(onePojo.getDate()));
				vList.add(onePojo.getType());
				vList.add(onePojo.getLate());
				count += this.sqlHelper.updateBySql(strSQL.toString(), vList);
				oneOldPojo = onePojo;
			}
			vList.clear();
			strSQL = new StringBuffer();
			if (onePojo.getOpType() == 0) {
				// 围栏判断
				onePojo.setInDate(oneOldPojo.getInDate());
				onePojo.setOutDate(oneOldPojo.getOutDate());
				onePojo.setLate(oneOldPojo.getLate());
				if (onePojo.getType() == 0) {
					// 在围栏外
					onePojo.setOutDate(onePojo.getDate());
					if (onePojo.getBjDate().equals("")) {
						onePojo.setBjDate(oneOldPojo.getBjDate());
					}
				} else {
					if (oneOldPojo.getInDate().equals("")) {
						onePojo.setInDate(onePojo.getDate());
						// 判断是否迟到
						DicItemPojo item = this.dicBI
								.getDicItemByRedis("WATCH_PARAS_9");
						if (item != null) {
							if (this.bsDate.getDateMillCount(onePojo.getDate(),
									onePojo.getDate().substring(0, 10) + " "
											+ item.getValue2()) < 0) {
								onePojo.setLate(0);
							}
						}

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
				vList.add(onePojo.getId());
				strSQL.append("update T_USER_WORK_DAY_LOGS set LOG_STATE=?,LOG_DATE=?,IN_DATE=?,OUT_DATE=?,LOG_BJDATE=? where LOG_ID=?");
				count += this.sqlHelper.updateBySql(strSQL.toString(), vList);

			} else {
				// 数据录入
				vList.add(Timestamp.valueOf(onePojo.getDate()));
				if (onePojo.getStep() == 0) {
					onePojo.setStep(oneOldPojo.getStep());
				}
				vList.add(onePojo.getStep());
				vList.add(URLlImplBase.AllPrince(onePojo.getDistance(),
						oneOldPojo.getDistance()));
				vList.add(onePojo.getId());
				strSQL.append("update T_USER_WORK_DAY_LOGS set LOG_DATE=?,LOG_STEP=?,LOG_DISTANCE=? where LOG_ID=?");
				count += this.sqlHelper.updateBySql(strSQL.toString(), vList);
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getUserSchedulingList
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
	public ArrayList<UserSchedulingPojo> getUserSchedulingList(String where,
			List<Object> vList) throws Exception {
		ArrayList<UserSchedulingPojo> list = new ArrayList<UserSchedulingPojo>();
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(
				_getUserSchedulingSelectSQL(where).toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneUserSchedulingPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getUserSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.USER_INSTID");
		strSQL.append(",t.USER_ID");
		strSQL.append(",t.USER_STATE");
		strSQL.append(",t.USER_NAME");
		strSQL.append(",t.USER_KEY");
		strSQL.append(",t.USER_SEX");
		strSQL.append(",t.EMAIL");
		strSQL.append(",t.MPHONE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.C_DATE")
				+ " as C_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.UPDATE_DATE")
				+ " as UPDATE_DATE");
		strSQL.append(",t.USER_IDCARD");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.USER_BIRTHDAY")
				+ " as USER_BIRTHDAY");
		// 机构
		strSQL.append(",t.ORG_ID");
		strSQL.append(" from T_USER t");
		strSQL.append(" where t.USER_INSTID is not null ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by" + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private UserPojo _setOneUserPojo(ResultSet rs) throws Exception {
		UserPojo onePojo = new UserPojo();
		onePojo.setInstId(rs.getString("USER_INSTID"));
		onePojo.setId(rs.getString("USER_ID"));
		onePojo.setState(rs.getInt("USER_STATE"));
		onePojo.setName(rs.getString("USER_NAME"));
		onePojo.setPassword(rs.getString("USER_KEY"));
		onePojo.setSex(rs.getInt("USER_SEX"));

		onePojo.setEmail((rs.getString("EMAIL") != null) ? rs
				.getString("EMAIL") : "");

		onePojo.setmPhone((rs.getString("MPHONE") != null) ? rs
				.getString("MPHONE") : "");

		onePojo.setCreateDate((rs.getString("C_DATE") != null) ? rs
				.getString("C_DATE") : "");

		onePojo.setUpdateDate((rs.getString("UPDATE_DATE") != null) ? rs
				.getString("UPDATE_DATE") : "");

		onePojo.setIdCard(rs.getString("USER_IDCARD"));

		onePojo.setBirthday((rs.getString("USER_BIRTHDAY") != null) ? rs
				.getString("USER_BIRTHDAY") : "");

		// 机构
		if (rs.getString("ORG_ID") != null
				&& !rs.getString("ORG_ID").equals("")) {
			onePojo.setOrg(this.userBI.getGroupByRedis(rs.getString("ORG_ID")));
		}
		return onePojo;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getUserWordParasSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.eqp_inst");
		strSQL.append(",t.eqp_wycode");
		strSQL.append(",t.eqp_qr");
		strSQL.append(",t.eqp_muser");
		strSQL.append(",t.eqp_online");
		strSQL.append(",t.user_id");
		strSQL.append(",t.user_name");
		strSQL.append(",t1.log_state");
		strSQL.append(",t1.LOG_BJDATE");

		strSQL.append(",us.ORG_ID");

		strSQL.append(" from T_USER us, V_USER_WORK_PARAS t left outer join T_USER_WORK_DAY_LOGS t1 on t.eqp_muser = t1.user_instid and t1.log_date=current_date");
		strSQL.append(" where t.eqp_muser=us.user_instid");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by" + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private UserWorkParasPojo _setOneUserWordParasPojo(ResultSet rs)
			throws Exception {
		UserWorkParasPojo onePojo = new UserWorkParasPojo();
		onePojo.getEqpInst().setInstId(rs.getString("eqp_inst"));
		onePojo.getEqpInst().setWyCode(rs.getString("eqp_wycode"));
		onePojo.getEqpInst().setQrCode(rs.getString("eqp_qr"));
		onePojo.getEqpInst().setOnlineState(rs.getInt("eqp_online"));
		onePojo.getEqpInst().getMangUser().setInstId(rs.getString("eqp_muser"));
		onePojo.getEqpInst().getMangUser().setId(rs.getString("user_id"));
		onePojo.getEqpInst().getMangUser().setName(rs.getString("user_name"));
		// 机构
		if (rs.getString("ORG_ID") != null
				&& !rs.getString("ORG_ID").equals("")) {
			onePojo.getEqpInst()
					.getMangUser()
					.setOrg(this.userBI.getGroupByRedis(rs.getString("ORG_ID")));
		}
		if (rs.getString("LOG_BJDATE") != null
				&& !rs.getString("LOG_BJDATE").equals("")) {
			onePojo.setThisState(2);
		} else if (rs.getInt("log_state") == 0) {
			onePojo.setThisState(1);
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

		return onePojo;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getOrgSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.ORG_ID");
		strSQL.append(",t.ORG_PID");
		strSQL.append(",t.ORG_NAME");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.C_DATE")
				+ " as C_DATE");
		strSQL.append(",t.ORG_DESC");
		strSQL.append(",t.ORG_ALLID");
		strSQL.append(",t.ORG_ALLNAME");
		strSQL.append(",t.COMP_ID");
		strSQL.append(",t.A_ID");
		strSQL.append(",t.ORG_TYPE");
		strSQL.append(",t.S_LAT");
		strSQL.append(",t.S_LON");
		// 创建者
		strSQL.append(",t.C_USER");
		strSQL.append(",t3.USER_ID as CUSER_ID");
		strSQL.append(",t3.USER_NAME as CUSER_NAME");
		strSQL.append(",(select count(vv2.ORG_ID) from T_ORG vv2 where vv2.ORG_PID=t.ORG_ID and vv2.ORG_PID<>vv2.ORG_ID) as CG_COUNT");// 下级数量
		strSQL.append(",(select count(v.USER_INSTID) FROM T_ORG_USER_R V, T_ORG V1 where V.ORG_ID = V1.ORG_ID AND V1.ORG_ALLID LIKE '%' || T.ORG_ID || '%') as G_COUNT");
		strSQL.append(" from T_ORG t,T_USER t3");
		strSQL.append(" where t.C_USER=t3.USER_INSTID");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private OrgPojo _setOneOrgPojo(ResultSet rs, boolean isAll)
			throws Exception {
		OrgPojo onePojo = new OrgPojo();
		// 设置群组信息
		onePojo.setId(rs.getString("ORG_ID"));
		onePojo.setPorgId(rs.getString("ORG_PID"));
		onePojo.setName(rs.getString("ORG_NAME"));
		onePojo.setType(rs.getInt("ORG_TYPE"));
		onePojo.setAllOrgId(rs.getString("ORG_ALLID"));
		onePojo.setAllName(rs.getString("ORG_ALLNAME"));
		if (rs.getString("ORG_DESC") != null) {
			onePojo.setDesc(rs.getString("ORG_DESC"));
		}
		//
		if (rs.getString("S_LAT") != null) {
			onePojo.setLatitude(rs.getString("S_LAT"));
		}
		if (rs.getString("S_LON") != null) {
			onePojo.setLongitude(rs.getString("S_LON"));
		}
		// 上级名称
		int s = onePojo.getAllName().lastIndexOf(",");
		if (s < 0) {
			onePojo.setPorgName(onePojo.getAllName());
		} else {
			onePojo.setPorgName(onePojo.getAllName().substring(s + 1));
		}

		// 创建者
		onePojo.getCreateStaff().setInstId(rs.getString("C_USER"));
		onePojo.getCreateStaff().setId(rs.getString("CUSER_ID"));
		onePojo.getCreateStaff().setName(rs.getString("CUSER_NAME"));
		// 管理者

		// 统计
		onePojo.setUserNum(rs.getInt("G_COUNT"));
		onePojo.setSubOrgNum(rs.getInt("CG_COUNT"));
		// 企业
		onePojo.setCompany(this.compBI.getCompanyByRedis(rs
				.getString("COMP_ID")));
		// 行政区域
		onePojo.setArea(this.areaBI.getAreaByRedis(rs.getString("A_ID")));

		if (isAll) {
			onePojo.setMangStaff(this.getOneOrgMang(onePojo.getId()));
		}
		return onePojo;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getUserOrgSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.R_TYPE");
		// 机构
		strSQL.append(",t.ORG_ID");
		strSQL.append(",t1.ORG_NAME");
		strSQL.append(",t1.ORG_ALLID");
		strSQL.append(",t1.ORG_ALLNAME");
		// 用户
		strSQL.append(",t.USER_INSTID");
		strSQL.append(",t2.USER_ID");
		strSQL.append(",t2.USER_NAME");
		strSQL.append(",t2.MPHONE");
		strSQL.append(" from T_ORG_USER_R t,T_ORG t1,T_USER t2");
		strSQL.append(" where t.ORG_ID=t1.ORG_ID");
		strSQL.append(" and t.USER_INSTID=t2.USER_INSTID");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private UserOrgRPojo _setOneUserOrgPojo(ResultSet rs) throws Exception {
		UserOrgRPojo onePojo = new UserOrgRPojo();
		onePojo.setType(rs.getInt("R_TYPE"));
		// 机构
		onePojo.getOrg().setId(rs.getString("ORG_ID"));
		onePojo.getOrg().setName(rs.getString("ORG_NAME"));
		onePojo.getOrg().setAllOrgId(rs.getString("ORG_ALLID"));
		onePojo.getOrg().setAllName(rs.getString("ORG_ALLNAME"));
		// 用户
		onePojo.getUser().setInstId(rs.getString("USER_INSTID"));
		onePojo.getUser().setId(rs.getString("USER_ID"));
		onePojo.getUser().setName(rs.getString("USER_NAME"));
		if (rs.getString("MPHONE") != null) {
			onePojo.getUser().setmPhone(rs.getString("MPHONE"));
		}

		return onePojo;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getUserWorkDayLogsSelectSQL(JSONObject where,
			String orderBy) throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.LOG_ID");
		strSQL.append(",t.LOG_STATE");
		strSQL.append(",t.LOG_DISTANCE");
		strSQL.append(",t.LOG_STEP");
		strSQL.append(",t.LOG_LATEFLG");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.LOG_DATE")
				+ " as LOG_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.IN_DATE")
				+ " as IN_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.OUT_DATE")
				+ " as OUT_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.LOG_BJDATE")
				+ " as LOG_BJDATE");
		strSQL.append(",(extract(epoch from(t.out_date - t.in_date))) as WORK_TIME");
		// 创建者
		strSQL.append(",t1.USER_INSTID");
		strSQL.append(",t1.USER_ID as USER_ID");
		strSQL.append(",t1.USER_NAME as USER_NAME");
		strSQL.append(",t1.MPHONE as MPHONE");
		strSQL.append(",t1.ORG_ID");
		strSQL.append(" from T_USER t1 left outer join T_USER_WORK_DAY_LOGS t on t.USER_INSTID = t1.user_instid ");
		if (where != null && where.containsKey("logs")) {
			strSQL.append(where.getString("logs"));
		}
		strSQL.append(" where t1.USER_INSTID is not null ");

		if (where != null && where.containsKey("user")) {
			strSQL.append(where.getString("user"));
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private UserWorkDayLogsPojo _setOneUserWorkDayLogsPojo(ResultSet rs)
			throws Exception {
		UserWorkDayLogsPojo onePojo = new UserWorkDayLogsPojo();
		// 设置群组信息
		onePojo.setId(rs.getString("LOG_ID"));
		onePojo.setType(rs.getInt("LOG_STATE"));
		onePojo.setDate(rs.getString("LOG_DATE"));
		onePojo.setStep(rs.getLong("LOG_STEP"));
		onePojo.setLate(rs.getInt("LOG_LATEFLG"));
		onePojo.setWorkTime(rs.getLong("WORK_TIME"));
		if (rs.getString("LOG_DISTANCE") != null) {
			onePojo.setDistance(rs.getString("LOG_DISTANCE"));
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

		// 创建者
		onePojo.getUser().setInstId(rs.getString("USER_INSTID"));
		onePojo.getUser().setId(rs.getString("USER_ID"));
		onePojo.getUser().setName(rs.getString("USER_NAME"));
		if (rs.getString("MPHONE") != null) {
			onePojo.getUser().setmPhone(rs.getString("MPHONE"));
		}
		// 机构
		if (rs.getString("ORG_ID") != null
				&& !rs.getString("ORG_ID").equals("")) {
			onePojo.getUser().setOrg(
					this.userBI.getGroupByRedis(rs.getString("ORG_ID")));
		}
		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getUserSchedulingSelectSQL(String where)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.S_ID");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.F_DATE")
				+ " as F_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.T_DATE")
				+ " as T_DATE");
		// 员工
		strSQL.append(",t1.USER_INSTID");
		strSQL.append(",t1.USER_ID");
		strSQL.append(",t1.USER_NAME");
		strSQL.append(",t1.MPHONE");
		strSQL.append(",t1.ORG_ID");
		strSQL.append(" from T_USER_SCHEDULING t,T_USER t1");
		strSQL.append(" where t.USER_INSTID=t1.USER_INSTID ");
		strSQL.append(" and t.TRUCK_ID=t2.TRUCK_ID ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		strSQL.append(" order by t.F_DATE");
		return strSQL;
	}

	// 物品定义对象
	private UserSchedulingPojo _setOneUserSchedulingPojo(ResultSet rs)
			throws Exception {
		UserSchedulingPojo onePojo = new UserSchedulingPojo();
		onePojo.setId(rs.getString("S_ID"));
		onePojo.setStartDate(rs.getString("F_DATE"));
		onePojo.setEndDate(rs.getString("T_DATE"));
		// 用户
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
		return onePojo;
	}
}
