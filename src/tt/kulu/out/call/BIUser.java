package tt.kulu.out.call;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.tt4j2ee.BIRedis;

import tt.kulu.bi.area.pojo.AreaPojo;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.power.dbclass.BSPowerDBMang;
import tt.kulu.bi.power.pojo.RolePojo;
import tt.kulu.bi.power.pojo.RoleUserPojo;
import tt.kulu.bi.storage.dbclass.EquipmentDBMang;
import tt.kulu.bi.storage.pojo.EquipmentGeometryPojo;
import tt.kulu.bi.user.dbclass.BSUserDBMang;
import tt.kulu.bi.user.pojo.OrgPojo;
import tt.kulu.bi.user.pojo.UserOrgRPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.bi.user.pojo.UserSchedulingPojo;
import tt.kulu.bi.user.pojo.UserWorkDayLogsPojo;
import tt.kulu.bi.user.pojo.UserWorkParasPojo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BIUser
 * </p>
 * <p>
 * 功能描述: 用户接口类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2007-5-15
 * </p>
 */
public class BIUser extends BSDBBase {
	public BIUser(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	public BIUser(BSObject m_bs) throws Exception {
		super(null, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getGroupByRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 从redis得到车辆类型
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public OrgPojo getGroupByRedis(String orgId) throws Exception {
		OrgPojo onePojo = new OrgPojo();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getMapData("KGROUP_MAP", orgId,
				URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到
			onePojo = this.getOneOrgById(orgId);
			if (onePojo != null) {
				redisBI.setMapData("KGROUP_MAP", orgId,
						JSON.toJSONString(onePojo), URLlImplBase.REDIS_KULUDATA);
			}
		} else {
			onePojo = JSON.parseObject(redisS, OrgPojo.class);
		}
		if (onePojo == null) {
			onePojo = new OrgPojo();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: setGroupToRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 从redis得到车辆类型
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public void setGroupToRedis(String orgId) throws Exception {
		OrgPojo onePojo = this.getOneOrgById(orgId);
		BIRedis redisBI = new BIRedis();
		if (onePojo != null) {
			redisBI.setMapData("KGROUP_MAP", orgId, JSON.toJSONString(onePojo),
					URLlImplBase.REDIS_KULUDATA);
		}
	}

	/**
	 * <p>
	 * 方法名称: deleteGroupToRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 从redis得到车辆类型
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public void deleteGroupToRedis(String orgId) throws Exception {
		BIRedis redisBI = new BIRedis();
		redisBI.delMapData("KGROUP_MAP", orgId, URLlImplBase.REDIS_KULUDATA);
	}

	/**
	 * <p>
	 * 方法名称: getUserSchedulingRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 从redis得到车辆排班表
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public JSONArray getUserSchedulingRedis(String day, String truckId)
			throws Exception {
		JSONArray list = new JSONArray();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getStringData("KUSERDS_" + truckId + "_" + day,
				URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到
			JSONObject _paras = new JSONObject();
			_paras.put("day", day);
			_paras.put("truck", truckId);
			ArrayList<UserSchedulingPojo> newL = this
					.getUserSchedulingList(_paras);
			if (newL.size() > 0) {
				redisBI.setStringData("KUSERDS_" + truckId + "_" + day,
						JSONArray.fromObject(newL).toString(),
						URLlImplBase.REDIS_KULUDATA);
			}
		} else {
			list = JSONArray.fromObject(redisS);
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: setUserSchedulingToRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 从redis得到车辆类型
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public void setUserSchedulingToRedis(String day, String truckId)
			throws Exception {
		JSONObject _paras = new JSONObject();
		_paras.put("day", day);
		_paras.put("truck", truckId);
		ArrayList<UserSchedulingPojo> newL = this.getUserSchedulingList(_paras);
		BIRedis redisBI = new BIRedis();
		if (newL.size() > 0) {
			redisBI.setStringData("KTRUCKDS_" + truckId + "_" + day, JSONArray
					.fromObject(newL).toString(), URLlImplBase.REDIS_KULUDATA);
		}
	}

	/**
	 * <p>
	 * 方法名称: getOrgList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到员工列表。
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
	public ArrayList<OrgPojo> getOrgList(JSONObject paras, long f, long t)
			throws Exception {
		ArrayList<OrgPojo> list = new ArrayList<OrgPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getOrgList(sqlHelper, paras, f, t);
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
	 * 方法名称: getOrgList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到员工列表。
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
	public ArrayList<OrgPojo> getOrgList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.ORG_ALLNAME,t.ORG_NAME";
		String where = " and t.ORG_ID not in ('UNKNOWN_GROUP') ";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("root")) {
					// 根
					where += " and t.ORG_ID=t.ORG_PID";
				}
				if (key.equals("comp")) {
					// 根
					where += " and t.COMP_ID=?";
					vList.add(v);
				}
				if (key.equals("sub")) {
					// 根
					where += " and t.ORG_PID=? and t.ORG_PID<>t.ORG_ID";
					vList.add(v);
				}
				if (key.equals("key")) {
					// 关键字
					where += " and (t.ORG_NAME like ? or t.ORG_DESC like ?)";
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("user")) {
					// 根
					where += " and t.ORG_PID=? and t.ORG_PID<>t.ORG_ID";
					vList.add(v);
				}
				if (key.equals("type")) {
					// 列别
					where += " and t.ORG_TYPE in (";
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += ",?";
							vList.add(Integer.parseInt(oneV));
						}
					}
					where += ((whereEx.equals("") ? "" : whereEx.substring(1)) + ")");
				}
				if (key.equals("login")) {
					// 所在的机构
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += (whereEx.equals("") ? "" : " or ")
									+ "t.ORG_ID in "
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
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		paras.put("max", userDB.getOrgCount(where, vList));
		return userDB.getOrgList(where, vList, orderBy, f, t);
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个员工。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public OrgPojo getOneOrgById(String orgId) throws Exception {
		OrgPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneOrgById(sqlHelper, orgId);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个门店。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public OrgPojo getOneOrgById(SqlExecute sqlHelper, String orgId)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.getOneOrgById(orgId);
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgByUser
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个员工。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public OrgPojo getOneOrgByUser(String user) throws Exception {
		OrgPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneOrgByUser(sqlHelper, user);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgByUser
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个门店。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public OrgPojo getOneOrgByUser(SqlExecute sqlHelper, String user)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.getOneOrgByUser(user);
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgByName
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个员工。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public OrgPojo getOneOrgByName(String porgId, String orgName)
			throws Exception {
		OrgPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneOrgByName(sqlHelper, porgId, orgName);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneOrgByName
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个门店。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public OrgPojo getOneOrgByName(SqlExecute sqlHelper, String porgId,
			String orgName) throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.getOneOrgByName(porgId, orgName);
	}

	/**
	 * <p>
	 * 方法名称: insertOrg
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个员工数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertOrg(OrgPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertOrg(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setGroupToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertOrg
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个员工数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertOrg(SqlExecute sqlHelper, OrgPojo onePojo)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.insertOrg(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateOrg
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateOrg(OrgPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateOrg(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setGroupToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateOrg
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateOrg(SqlExecute sqlHelper, OrgPojo onePojo)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.updateOrg(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: deleteOneOrg
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteOneOrg(String orgId) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteOneOrg(sqlHelper, orgId);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		if (count > 0) {
			this.deleteGroupToRedis(orgId);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteOneOrg
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteOneOrg(SqlExecute sqlHelper, String orgId)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.deleteOneOrg(orgId);
	}

	/**
	 * <p>
	 * 方法名称: getUserList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量用户
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<UserPojo> getUserList(JSONObject paras, long f, long t)
			throws Exception {
		ArrayList<UserPojo> list = new ArrayList<UserPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getUserList(sqlHelper, paras, f, t);
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
	 * 方法名称: getUserList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量用户
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<UserPojo> getUserList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		ArrayList<UserPojo> list = new ArrayList<UserPojo>();
		// 参数设定
		String where = "";
		String key = "";
		String orderBy = " t.C_DATE desc,T.USER_INSTID";
		List<Object> vList = new ArrayList<Object>();
		if (paras != null) {
			Iterator<String> keys = paras.keys();
			while (keys.hasNext()) {
				key = keys.next();
				String v = paras.getString(key);
				if (!v.equals("")) {
					if (key.equals("key")) {
						// 关键字
						where += " and (t.USER_ID=? or t.USER_NAME like ? or T.MPHONE like ?)";
						vList.add(v);
						vList.add("%" + v + "%");
						vList.add("%" + v + "%");
					}
					if (key.equals("role")) {
						// 关键字
						where += " and t.user_instid in (select b.USER_INSTID from T_ROLE_USER_R b where b.ROLE_ID in (";
						String[] vs = v.split(",");
						String whereEx = "";
						for (String oneV : vs) {
							if (!oneV.equals("")) {
								whereEx += ",?";
								vList.add(oneV);
							}
						}
						where += ((whereEx.equals("") ? "" : whereEx
								.substring(1)) + "))");
					}
					if (key.equals("login")) {
						String[] vs = v.split(",");
						String whereEx = "";
						for (String oneV : vs) {
							if (!oneV.equals("")) {
								whereEx += (whereEx.equals("") ? "" : " or ")
										+ "t.ORG_ID in "
										+ URLlImplBase.LOGIN_GROUP_WHERE;
								vList.add("%," + oneV + "%");
							}
						}
						if (!whereEx.equals("")) {
							where += " and (" + whereEx + ")";
						}
					}

					if (key.equals("group")) {
						// 机构
						where += " and t.USER_INSTID in "
								+ URLlImplBase.LOGIN_USER_WHERE;
						vList.add("%," + v + "%");
					}

					if (key.equals("havephone")) {
						// 必须要有手机号
						where += " and t.MPHONE is not null";
					}

					if (key.equals("state")) {
						// 状态
						where += " and t.USER_STATE=?";
						vList.add(Integer.parseInt(v));
					}

					if (key.equals("normal")) {
						// 状态
						where += " and t.USER_INSTID not in ('UNKNOWN_USER','SUPER_ADMIN')";
					}

					if (key.equals("areaid")) {
						// 行政区域
						where += " and t.ORG_ID in (select g.ORG_ID from T_ORG g where g.A_ID=?)";
						vList.add(v);
					}
					if (key.equals("areaname")) {
						// 行政区域
						where += " and t.ORG_ID in (select g.ORG_ID from T_ORG g,t_AREA g1 where g.A_ID=g1.A_ID and g1.A_ALLNAME like ?)";
						vList.add(v);
					}
					if (key.equals("geoarea")) {
						// 矩形
						if (v.startsWith(",")) {
							v = v.substring(1);
						}
						String geos[] = v.split(",");
						if (geos.length >= 4) {
							where += " and t.USER_INSTID in (select g.eqp_muser from t_equipment_inst g,T_EQP_INST_GEOMETRY geo,t_equipment_def def where g.eqp_def = def.eqp_code and g.eqp_inst=geo.eqp_inst and ST_Contains(st_geomfromText('POLYGON(("
									+ geos[0]
									+ " "
									+ geos[1]
									+ ","
									+ geos[2]
									+ " "
									+ geos[1]
									+ ","
									+ geos[2]
									+ " "
									+ geos[3]
									+ ","
									+ geos[0]
									+ " "
									+ geos[3]
									+ ","
									+ geos[0]
									+ " "
									+ geos[1]
									+ "))',4326),geo.s_geometry)=true";
							if (geos.length >= 6) {
								where += " and geo.S_CDATE BETWEEN ? AND ?";
								vList.add(Timestamp.valueOf(geos[4]));
								vList.add(Timestamp.valueOf(geos[5]));
							}
							where += ")";
						}
					}

					if (key.equals("notfance")) {
						// 不在围栏
						where += " and t.USER_INSTID not in (select g.USER_INSTID from T_FANCE_USER_R g where g.F_ID=?)";
						vList.add(v);
					}
					if (key.equals("infance")) {
						// 在围栏
						where += " and t.USER_INSTID in (select g.USER_INSTID from T_FANCE_USER_R g where g.F_ID=?)";
						vList.add(v);
					}
					if (key.equals("nottruck")) {
						// 不在车辆
						where += " and t.USER_INSTID not in (select g.USER_INSTID from T_TRUCK_INST_USER g where g.TRUCK_ID=?)";
						vList.add(v);
					}
				}
			}
		}
		// 设置用户列表
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		paras.put("max", userDB.getUserCount(where, vList));
		return userDB.getUserList(where, orderBy, vList, f, t);
	}

	/**
	 * <p>
	 * 方法名：getOneUser
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public UserPojo getOneUser(String instid) throws Exception {
		UserPojo onePojo = new UserPojo();
		String where = " and t.user_instid=?";
		List<Object> vList = new ArrayList<Object>();
		vList.add(instid);
		SqlExecute sqlHelper = new SqlExecute();
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		try {
			onePojo = userDB.getOneUser(where, vList);
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneUserByInstId
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public UserPojo getOneUserByInstId(String id) throws Exception {
		UserPojo onePojo = new UserPojo();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneUserByInstId(sqlHelper, id);
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneUserByInstId
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public UserPojo getOneUserByInstId(SqlExecute sqlHelper, String id)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.getOneUserByInstId(id);
	}

	/**
	 * <p>
	 * 方法名：getOneUserById
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public UserPojo getOneUserById(String id) throws Exception {
		UserPojo onePojo = new UserPojo();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneUserById(sqlHelper, id);
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneUserById
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public UserPojo getOneUserById(SqlExecute sqlHelper, String id)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.getOneUserById(id);
	}

	/**
	 * <p>
	 * 方法名：getOneUserById
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public UserPojo getOneUserByIdNotState(String id) throws Exception {
		UserPojo onePojo = new UserPojo();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneUserByIdNotState(sqlHelper, id);
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneUserById
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public UserPojo getOneUserByIdNotState(SqlExecute sqlHelper, String id)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.getOneUserByIdNotState(id);
	}

	/**
	 * <p>
	 * 方法名：getOneUserByPhone
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public UserPojo getOneUserByPhone(String phone) throws Exception {
		UserPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneUserByPhone(sqlHelper, phone);
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneUserByPhone
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public UserPojo getOneUserByPhone(SqlExecute sqlHelper, String phone)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		List<Object> vList = new ArrayList<Object>();
		vList.add(phone);
		String where = " and t.MPHONE=?";
		return userDB.getOneUser(where, vList);
	}

	/**
	 * <p>
	 * 方法名：getOneUserByPhone
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public OrgPojo getOneOrgByName(String name, int type) throws Exception {
		OrgPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneOrgByName(sqlHelper, name, type);
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneOrgByName
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public OrgPojo getOneOrgByName(SqlExecute sqlHelper, String name, int type)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.getOneOrgByName(name, type);
	}

	/**
	 * <p>
	 * 方法名：getOneUserByPhone
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public OrgPojo getOneOrgByName(String pid, String name, int type)
			throws Exception {
		OrgPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneOrgByName(sqlHelper, pid, name, type);
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneOrgByName
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public OrgPojo getOneOrgByName(SqlExecute sqlHelper, String pid,
			String name, int type) throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.getOneOrgByName(pid, name, type);
	}

	/**
	 * <p>
	 * 方法名：insertUser
	 * </p>
	 * <p>
	 * 方法描述：新增用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public int insertUser(UserPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			// 新增用户
			BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
			count = userDB.insertUser(onePojo);
			// 增加角色
			if (count > 0) {
				String roleWhere = "";
				if (onePojo.getRoleList() != null) {
					BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
					for (RolePojo oneRole : onePojo.getRoleList()) {
						roleWhere += (",'" + oneRole.getId() + "'");
						roleDB.insertUserRole(new RoleUserPojo(onePojo
								.getInstId(), oneRole.getId()));
					}
				}
				if (roleWhere.length() > 0) {
					roleWhere = roleWhere.substring(1);
					onePojo.setRoleWhere(roleWhere);
				}
			}
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名：updateUser
	 * </p>
	 * <p>
	 * 方法描述：更新用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public int updateUser(UserPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
			count = userDB.updateUser(onePojo);
			// 增加角色
			String roleWhere = "";
			if (onePojo.getRoleList() != null) {
				BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
				for (RolePojo oneRole : onePojo.getRoleList()) {
					roleWhere += (",'" + oneRole.getId() + "'");
					roleDB.insertUserRole(new RoleUserPojo(onePojo.getInstId(),
							oneRole.getId()));
				}
			}
			if (roleWhere.length() > 0) {
				roleWhere = roleWhere.substring(1);
				onePojo.setRoleWhere(roleWhere);
			}
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateUserKey
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量用户
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateUserKey(SqlExecute sqlHelper, String userInstId,
			String newKey) throws Exception {

		// 设置用户列表
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.updateUserKey(userInstId, newKey);
	}

	/**
	 * <p>
	 * 方法名称: updateUserKey
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量用户
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateUserKey(String userInstId, String newKey) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			count = this.updateUserKey(sqlHelper, userInstId, newKey);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}

		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateOneUserState
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateOneUserState(String userId, int state) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateOneUserState(sqlHelper, userId, state);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateOneUserState
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateOneUserState(SqlExecute sqlHelper, String userId, int state)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.updateOneUserState(userId, state);
	}

	/**
	 * <p>
	 * 方法名称: deleteOneUser
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteOneUser(String userId) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteOneUser(sqlHelper, userId);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteOneUser
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteOneUser(SqlExecute sqlHelper, String userId)
			throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.deleteOneUser(userId);
	}

	/**
	 * <p>
	 * 方法名称: getUserOrgList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到员工列表。
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
	public ArrayList<UserOrgRPojo> getUserOrgList(JSONObject paras)
			throws Exception {
		ArrayList<UserOrgRPojo> list = new ArrayList<UserOrgRPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getUserOrgList(sqlHelper, paras);
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
	 * 方法名称: getUserOrgList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到员工列表。
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
	public ArrayList<UserOrgRPojo> getUserOrgList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.R_TYPE";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("user")) {
					// 根
					where += " and t.USER_INSTID=?";
					vList.add(v);
				}
				if (key.equals("org")) {
					// 根
					where += " and t.ORG_ID=?";
					vList.add(v);
				}
				if (key.equals("type")) {
					// 根
					where += " and t.R_TYPE=?";
					vList.add(v);
				}
			}
		}
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.getUserOrgList(where, vList, orderBy);
	}

	/**
	 * <p>
	 * 方法名称: selectUserOrg
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个员工数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int selectUserOrg(UserOrgRPojo onePojo, int type) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.selectUserOrg(sqlHelper, onePojo, type);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setGroupToRedis(onePojo.getOrg().getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: selectUserOrg
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个员工数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int selectUserOrg(SqlExecute sqlHelper, UserOrgRPojo onePojo,
			int type) throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.updateUserOrgR(onePojo, type);
	}

	/**
	 * <p>
	 * 方法名称: getUserWordParasList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量用户
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<UserWorkParasPojo> getUserWordParasList(JSONObject paras,
			long f, long t) throws Exception {
		ArrayList<UserWorkParasPojo> list = new ArrayList<UserWorkParasPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getUserWordParasList(sqlHelper, paras, f, t);
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
	 * 方法名称: getUserWordParasList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量用户
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<UserWorkParasPojo> getUserWordParasList(
			SqlExecute sqlHelper, JSONObject paras, long f, long t)
			throws Exception {
		// 参数设定
		String where = "";
		String key = "";
		String orderBy = " t.user_name";
		List<Object> vList = new ArrayList<Object>();
		if (paras != null) {
			Iterator<String> keys = paras.keys();
			while (keys.hasNext()) {
				key = keys.next();
				String v = paras.getString(key);
				if (!v.equals("")) {
					if (key.equals("key")) {
						// 关键字
						where += " and t.eqp_muser in (select v.USER_INSTID from T_USER v where v.USER_INSTID is not null and (v.user_id=? or v.USER_NAME like ? or v.MPHONE like ?))";
						vList.add(v);
						vList.add("%" + v + "%");
						vList.add("%" + v + "%");
					}
					if (key.equals("group")) {
						// 关键字
						where += " and t.eqp_muser in "
								+ URLlImplBase.LOGIN_USER_WHERE;
						vList.add("%," + v + "%");
					}
					if (key.equals("user")) {
						// 关键字
						where += " and t.eqp_muser=?";
						vList.add(v);
					}
					if (key.equals("state")) {
						// 关键字
						where += " and t.EQP_STATE=?";
						vList.add(Integer.parseInt(v));
					}
					if (key.equals("usstate")) {
						// 关键字
						where += " and us.USER_STATE=?";
						vList.add(Integer.parseInt(v));
					}
					if (key.equals("login")) {
						String[] vs = v.split(",");
						String whereEx = "";
						for (String oneV : vs) {
							if (!oneV.equals("")) {
								whereEx += (whereEx.equals("") ? "" : " or ")
										+ "us.ORG_ID in "
										+ URLlImplBase.LOGIN_GROUP_WHERE;
								vList.add("%," + oneV + "%");
							}
						}
						if (!whereEx.equals("")) {
							where += " and (" + whereEx + ")";
						}
					}
					if (key.equals("role")) {
						// 角色
						where += " and us.USER_INSTID in (select r.USER_INSTID from T_ROLE_USER_R r where r.ROLE_ID in(";
						String[] vs = v.split(",");
						String whereEx = "";
						for (String oneV : vs) {
							if (!oneV.equals("")) {
								whereEx += ",?";
								vList.add(oneV);
							}
						}
						where += ((whereEx.equals("") ? "" : whereEx
								.substring(1)) + "))");
					}

					if (key.equals("hasgeo")) {
						// 关键字
						where += " and (t.geo_id <> '' and t.geo_id is not null)";
					}
					if (key.equals("geoarea")) {
						// 关键字
						if (v.startsWith(",")) {
							v = v.substring(1);
						}
						String geos[] = v.split(",");
						if (geos.length >= 4) {
							where += " and ST_Contains(st_geomfromText('POLYGON(("
									+ geos[0]
									+ " "
									+ geos[1]
									+ ","
									+ geos[2]
									+ " "
									+ geos[1]
									+ ","
									+ geos[2]
									+ " "
									+ geos[3]
									+ ","
									+ geos[0]
									+ " "
									+ geos[3]
									+ ","
									+ geos[0]
									+ " "
									+ geos[1]
									+ "))',4326),t.geo_id)=true";
						}
					}
				}
			}
		}
		// 设置用户列表
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		paras.put("max", userDB.getUserWordParasCount(where, vList));
		return userDB.getUserWordParasList(where, orderBy, vList, f, t);
	}

	/**
	 * <p>
	 * 方法名称: getUserWorkDayLogsList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到员工列表。
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
	public ArrayList<UserWorkDayLogsPojo> getUserWorkDayLogsList(
			JSONObject paras, long f, long t) throws Exception {
		ArrayList<UserWorkDayLogsPojo> list = new ArrayList<UserWorkDayLogsPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getUserWorkDayLogsList(sqlHelper, paras, f, t);
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
	 * 方法名称: getUserWorkDayLogsList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到员工列表。
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
	public ArrayList<UserWorkDayLogsPojo> getUserWorkDayLogsList(
			SqlExecute sqlHelper, JSONObject paras, long f, long t)
			throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vLogList = new ArrayList<Object>();
		List<Object> vUserList = new ArrayList<Object>();
		String orderBy = " t1.USER_NAME";
		String logWhere = "";
		String userWhere = "";
		JSONObject where = new JSONObject();
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 根
					userWhere += " and (t1.USER_ID=? OR t1.MPHONE=? OR t1.USER_NAME like ? OR t1.ORG_ID in (select v.ORG_ID from T_ORG v where v.ORG_NAME like ?))";
					vUserList.add(v);
					vUserList.add(v);
					vUserList.add("%" + v + "%");
					vUserList.add("%" + v + "%");
				}
				if (key.equals("user")) {
					// 根
					userWhere += " and t1.USER_INSTID=?";
					vUserList.add(v);
				}
				if (key.equals("role")) {
					// 关键字
					userWhere += " and t1.USER_INSTID in (select b.USER_INSTID from T_ROLE_USER_R b where b.ROLE_ID in (";
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += ",?";
							vUserList.add(oneV);
						}
					}
					userWhere += ((whereEx.equals("") ? "" : whereEx
							.substring(1)) + "))");
				}
				if (key.equals("state")) {
					// 根
					userWhere += " and t1.USER_STATE=?";
					vUserList.add(Integer.parseInt(v));
				}
				if (key.equals("date")) {
					logWhere += " and t.LOG_DATE BETWEEN ? AND ?";
					vLogList.add(Timestamp.valueOf(v.substring(0, 10)
							+ " 00:00:00"));
					vLogList.add(Timestamp.valueOf(v.substring(0, 10)
							+ " 23:58:59"));
				}
				if (key.equals("org")) {
					// 根
					userWhere += " and t1.ORG_ID=?";
					vUserList.add(v);
				}
				if (key.equals("login")) {
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += (whereEx.equals("") ? "" : " or ")
									+ " t1.ORG_ID in  "
									+ URLlImplBase.LOGIN_GROUP_WHERE;
							vUserList.add("%," + oneV + "%");
						}
					}
					if (!whereEx.equals("")) {
						userWhere += " and (" + whereEx + ")";
					}
				}
			}
		}
		where.put("logs", logWhere);
		where.put("user", userWhere);
		vLogList.addAll(vUserList);
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		paras.put("max", userDB.getUserWorkDayLogsCount(where, vLogList));
		return userDB.getUserWorkDayLogsList(where, vLogList, orderBy, f, t);
	}

	/**
	 * <p>
	 * 方法名：getOneUserWorkDayLogsId
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public UserWorkDayLogsPojo getOneUserWorkDayLogsId(String id)
			throws Exception {
		UserWorkDayLogsPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneUserWorkDayLogsId(sqlHelper, id);
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneUserWorkDayLogsId
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public UserWorkDayLogsPojo getOneUserWorkDayLogsId(SqlExecute sqlHelper,
			String id) throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.getOneUserWorkDayLogsId(id);
	}

	/**
	 * <p>
	 * 方法名称: updateUserWorkDayLogs
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateUserWorkDayLogs(UserWorkDayLogsPojo onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateUserWorkDayLogs(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateUserWorkDayLogs
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateUserWorkDayLogs(SqlExecute sqlHelper,
			UserWorkDayLogsPojo onePojo) throws Exception {
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.updateUserWorkDayLogs(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateUserWorkDayLogsDis
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateUserWorkDayLogsDis(EquipmentGeometryPojo onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateUserWorkDayLogsDis(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateUserWorkDayLogsDis
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个机构数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateUserWorkDayLogsDis(SqlExecute sqlHelper,
			EquipmentGeometryPojo oneEqpGeo) throws Exception {
		UserWorkDayLogsPojo oneWork = new UserWorkDayLogsPojo();
		oneWork.setId(oneEqpGeo.getEqpInst().getMangUser().getInstId() + "_"
				+ oneEqpGeo.getCreateDate().substring(0, 10));
		oneWork.setOpType(1);
		oneWork.setDate(oneEqpGeo.getCreateDate());
		oneWork.setUser(oneEqpGeo.getEqpInst().getMangUser());
		// 得到距离
		EquipmentDBMang eqpDB = new EquipmentDBMang(sqlHelper, m_bs);
		oneWork.setDistance(eqpDB.getGeometryLastDisdance(oneEqpGeo, 1));

		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.updateUserWorkDayLogs(oneWork);
	}

	/**
	 * <p>
	 * 方法名称: getUserSchedulingList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆当值司机列表。
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
	public ArrayList<UserSchedulingPojo> getUserSchedulingList(JSONObject paras)
			throws Exception {
		ArrayList<UserSchedulingPojo> list = new ArrayList<UserSchedulingPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getUserSchedulingList(sqlHelper, paras);
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
	 * 方法名称: getUserSchedulingList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆当值司机列表。
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
	public ArrayList<UserSchedulingPojo> getUserSchedulingList(
			SqlExecute sqlHelper, JSONObject paras) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("user")) {
					// 关键字
					where += " and t.USER_INSTID=?";
					vList.add(v);
				}
				if (key.equals("truck")) {
					// 关键字
					where += " and t.TRUCK_ID=?";
					vList.add(v);
				}
				if (key.equals("day")) {
					// 关键字
					Calendar day = this.bsDate.getStringToCalendar(v);
					day.add(Calendar.DATE, -1);
					String sdate = this.bsDate.getCalendarToString(day)
							+ " 00:00:00";
					day.add(Calendar.DATE, 2);
					String edate = this.bsDate.getCalendarToString(day)
							+ " 23:59:59";
					where += " and t.F_DATE BETWEEN ? AND ?";
					vList.add(Timestamp.valueOf(sdate));
					vList.add(Timestamp.valueOf(edate));
				}
			}
		}
		BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
		return userDB.getUserSchedulingList(where, vList);
	}

	/**
	 * <p>
	 * 方法名称: getUserSchedulingRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 从redis得到车辆排班表
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public UserPojo getUserSchedulingByTruckAndDay(String sDate, String truckId)
			throws Exception {
		UserPojo userPojo = null;
		JSONArray list = this.getUserSchedulingRedis(sDate.substring(0, 10),
				truckId);
		if (list != null && list.size() > 0) {
			// 判断起点时间在某个排班表内
			for (int i = 0, size = list.size(); i < size; i++) {
				JSONObject oneDS = list.getJSONObject(i);
				String drSdate = oneDS.getString("startDate");
				String drEdate = oneDS.getString("endDate");
				if (this.bsDate.getDateMillCount(drSdate, sDate) >= 0
						&& this.bsDate.getDateMillCount(sDate, drEdate) >= 0) {
					JSONObject oneDSUser = oneDS.getJSONObject("user");
					userPojo = new UserPojo();
					userPojo.setInstId(oneDSUser.getString("instId"));
					userPojo.setId(oneDSUser.getString("id"));
					userPojo.setName(oneDSUser.getString("name"));
					userPojo.setmPhone(oneDSUser.getString("mPhone"));
					if (oneDSUser.containsKey("org")) {
						JSONObject oneDSOrg = oneDSUser.getJSONObject("org");
						userPojo.setOrg(new OrgPojo());
						userPojo.getOrg().setId(oneDSOrg.getString("id"));
						userPojo.getOrg().setName(oneDSOrg.getString("name"));
						userPojo.getOrg().setAllName(
								oneDSOrg.getString("allName"));
					}
					userPojo.setOrg(null);
					break;
				}

			}
		}
		return userPojo;
	}
}
