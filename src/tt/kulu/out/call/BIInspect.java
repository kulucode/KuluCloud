package tt.kulu.out.call;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import com.tt4j2ee.BIRedis;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.inspect.dbclass.InspectDBMang;
import tt.kulu.bi.inspect.pojo.InspectDefPojo;
import tt.kulu.bi.inspect.pojo.InspectPlanPojo;
import tt.kulu.bi.truck.pojo.TruckPojo;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BIInspect
 * </p>
 * <p>
 * 功能描述: 故障报警业务接口类
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
public class BIInspect extends BSDBBase {
	public BIInspect(BSObject m_bs) throws Exception {
		super(null, m_bs);
	}

	public BIInspect(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getInspectDefByRedis
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
	public InspectDefPojo getInspectDefByRedis(String defId) throws Exception {
		InspectDefPojo onePojo = new InspectDefPojo();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getStringData("KINSPECTDEF_" + defId,
				URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到
			onePojo = this.getOneInspectDefById(defId);
			if (onePojo != null) {
				redisBI.setStringData("KINSPECTDEF_" + defId, JSONObject
						.fromObject(onePojo).toString(),
						URLlImplBase.REDIS_KULUDATA);
			}
		} else {
			onePojo = (InspectDefPojo) JSONObject.toBean(
					JSONObject.fromObject(redisS), InspectDefPojo.class);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: setInspectDefToRedis
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
	public void setInspectDefToRedis(String defId) throws Exception {
		InspectDefPojo onePojo = this.getOneInspectDefById(defId);
		BIRedis redisBI = new BIRedis();
		if (onePojo != null) {
			redisBI.setStringData("KINSPECTDEF_" + defId, JSONObject
					.fromObject(onePojo).toString(),
					URLlImplBase.REDIS_KULUDATA);
		}
	}

	/**
	 * <p>
	 * 方法名称: setInspectDefToRedis
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
	public void setInspectDefListToRedis(String truckDefId) throws Exception {
		JSONObject paras = new JSONObject();
		paras.put("define", truckDefId);
		ArrayList<InspectDefPojo> list = this.getInspectDefList(paras);
		BIRedis redisBI = new BIRedis();
		for (InspectDefPojo onePojo : list) {
			redisBI.setStringData("KINSPECTDEF_" + onePojo.getId(), JSONObject
					.fromObject(onePojo).toString(),
					URLlImplBase.REDIS_KULUDATA);
		}
	}

	/**
	 * <p>
	 * 方法名称: getInspectDefList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到保养定义列表。
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
	public ArrayList<InspectDefPojo> getInspectDefList(JSONObject paras)
			throws Exception {
		ArrayList<InspectDefPojo> list = new ArrayList<InspectDefPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getInspectDefList(sqlHelper, paras);
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
	 * 方法名称: getInspectDefList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到保养定义列表。
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
	public ArrayList<InspectDefPojo> getInspectDefList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.DEF_CLASS";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and t.DEF_NAME like ? OR t.DEF_DESC like ?";
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("define")) {
					// 关键字
					where += " and t.TD_ID=?";
					vList.add(v);
				}
				if (key.equals("defclass")) {
					// 关键字
					where += " and t.DEF_CLASS=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("subclass")) {
					// 关键字
					where += " and t.DEF_CLASS=?";
					vList.add(Integer.parseInt(v) + 1);
				}
			}
		}
		InspectDBMang inspectDB = new InspectDBMang(sqlHelper, m_bs);
		return inspectDB.getInspectDefList(where, orderBy, vList);
	}

	/**
	 * <p>
	 * 方法名称: getOneInspectDefById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个保养定义。
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
	public InspectDefPojo getOneInspectDefById(String id) throws Exception {
		InspectDefPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneInspectDefById(sqlHelper, id);
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
	 * 方法名称: getOneInspectDefById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个保养定义。
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
	public InspectDefPojo getOneInspectDefById(SqlExecute sqlHelper, String id)
			throws Exception {
		InspectDBMang inspectDB = new InspectDBMang(sqlHelper, m_bs);
		return inspectDB.getOneInspectDefById(id);
	}

	/**
	 * <p>
	 * 方法名称: insertInspectDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个保养定义数据。
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
	public int insertInspectDef(InspectDefPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertInspectDef(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setInspectDefToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertInspectDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个保养定义数据。
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
	public int insertInspectDef(SqlExecute sqlHelper, InspectDefPojo onePojo)
			throws Exception {
		InspectDBMang inspectDB = new InspectDBMang(sqlHelper, m_bs);
		return inspectDB.insertInspectDef(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateInspectDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个保养定义数据。
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
	public int updateInspectDef(InspectDefPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateInspectDef(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setInspectDefToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateInspectDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个保养定义数据。
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
	public int updateInspectDef(SqlExecute sqlHelper, InspectDefPojo onePojo)
			throws Exception {
		InspectDBMang inspectDB = new InspectDBMang(sqlHelper, m_bs);
		return inspectDB.updateInspectDef(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: deleteInspectDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个保养定义数据。
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
	public int deleteInspectDef(String defId) throws Exception {
		int count = 0;
		InspectDefPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			onePojo = this.getInspectDefByRedis(defId);
			count = this.deleteInspectDef(sqlHelper, defId);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		if (onePojo != null && count > 0) {
			this.setInspectDefListToRedis(onePojo.getTurckDef().getId());
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteInspectDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个保养定义数据。
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
	public int deleteInspectDef(SqlExecute sqlHelper, String defId)
			throws Exception {
		InspectDBMang inspectDB = new InspectDBMang(sqlHelper, m_bs);
		return inspectDB.deleteInspectDef(defId);
	}

	/**
	 * <p>
	 * 方法名称: getInspectPlanList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到保养计划列表。
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
	public ArrayList<InspectPlanPojo> getInspectPlanList(JSONObject paras,
			long f, long t) throws Exception {
		ArrayList<InspectPlanPojo> list = new ArrayList<InspectPlanPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getInspectPlanList(sqlHelper, paras, f, t);
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
	 * 方法名称: getInspectPlanList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到保养计划列表。
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
	public ArrayList<InspectPlanPojo> getInspectPlanList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.TRUCK_ID,t.PLAN_DATE";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t1.TRUCK_NAME like ? OR t1.PLATE_NUM like ?)";
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("define")) {
					// 关键字
					where += " and t.DEF_ID=?";
					vList.add(v);
				}
				if (key.equals("truck")) {
					// 关键字
					where += " and t.TRUCK_ID=?";
					vList.add(v);
				}
				if (key.equals("group")) {
					where += " and t1.ORG_ID in "
							+ URLlImplBase.LOGIN_GROUP_WHERE;
					vList.add("%," + v + "%");
				}
				if (key.equals("login")) {
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += (whereEx.equals("") ? "" : " or ")
									+ "t1.ORG_ID in "
									+ URLlImplBase.LOGIN_GROUP_WHERE;
							vList.add("%," + oneV + "%");
						}
					}
					if (!whereEx.equals("")) {
						where += " and (" + whereEx + ")";
					}
				}
				if (key.equals("truckdef")) {
					// 关键字
					where += " and t1.TD_ID=?";
					vList.add(v);
				}
				if (key.equals("state")) {
					// 关键字
					where += " and t.PLAN_STATE=?";
					vList.add(Integer.parseInt(v));
				}
			}
		}
		InspectDBMang inspectDB = new InspectDBMang(sqlHelper, m_bs);
		paras.put("max", inspectDB.getInspectPlanCount(where, vList));
		return inspectDB.getInspectPlanList(where, orderBy, f, t, vList);
	}

	/**
	 * <p>
	 * 方法名称: getOneInspectPlanById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个保养计划。
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
	public InspectPlanPojo getOneInspectPlanById(String id) throws Exception {
		InspectPlanPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneInspectPlanById(sqlHelper, id);
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
	 * 方法名称: getOneInspectPlanById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个保养计划。
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
	public InspectPlanPojo getOneInspectPlanById(SqlExecute sqlHelper, String id)
			throws Exception {
		InspectDBMang inspectDB = new InspectDBMang(sqlHelper, m_bs);
		return inspectDB.getOneInspectPlanById(id);
	}

	/**
	 * <p>
	 * 方法名称: insertInspectPlan
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个保养定义数据。
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
	public int insertInspectPlan(InspectPlanPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertInspectPlan(sqlHelper, onePojo);
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
	 * 方法名称: insertInspectPlan
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个保养定义数据。
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
	public int insertInspectPlan(SqlExecute sqlHelper, InspectPlanPojo onePojo)
			throws Exception {
		InspectDBMang inspectDB = new InspectDBMang(sqlHelper, m_bs);
		return inspectDB.insertInspectPlan(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateInspectPlan
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个保养定义数据。
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
	public int updateInspectPlan(InspectPlanPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateInspectPlan(sqlHelper, onePojo);
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
	 * 方法名称: updateInspectPlan
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个保养定义数据。
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
	public int updateInspectPlan(SqlExecute sqlHelper, InspectPlanPojo onePojo)
			throws Exception {
		InspectDBMang inspectDB = new InspectDBMang(sqlHelper, m_bs);
		return inspectDB.updateInspectPlan(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: iniInspectPlanFromDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个保养定义数据。
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
	public int iniInspectPlanFromDef(String truckDefId) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.iniInspectPlanFromDef(sqlHelper, truckDefId);
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
	 * 方法名称: iniInspectPlanFromDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个保养定义数据。
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
	public int iniInspectPlanFromDef(SqlExecute sqlHelper, String truckDefId)
			throws Exception {
		int count = 0;
		InspectDBMang inspectDB = new InspectDBMang(sqlHelper, m_bs);
		// 得到该车型所有的保养计划
		JSONObject paras = new JSONObject();
		paras.put("define", truckDefId);
		ArrayList<InspectDefPojo> list = this.getInspectDefList(paras);
		BITruck truckBI = new BITruck(this.sqlHelper, this.m_bs);
		for (InspectDefPojo onePojo : list) {
			// 处理未完成的已有计划，计划时间重新计算
			paras.put("define", onePojo.getId());
			paras.put("state", 0);
			ArrayList<InspectPlanPojo> planList = this.getInspectPlanList(
					sqlHelper, paras, 0, 5000);
			for (InspectPlanPojo onePlan : planList) {
				onePlan.setState(0);
				count += inspectDB.updateInspectPlan(onePlan);
			}
			// 处理未生成保养记录的车辆列表
			paras.remove("state");
			paras.put("define", truckDefId);
			paras.put("notinspect", onePojo.getId());
			ArrayList<TruckPojo> truckList = truckBI.getTruckList(paras, 0,
					5000);
			for (TruckPojo oneTruck : truckList) {
				InspectPlanPojo onePlan = new InspectPlanPojo();
				onePlan.setTruck(oneTruck);
				onePlan.setInspectDef(onePojo);
				onePlan.setCreateDate(this.bsDate.getThisDate(0, 0));
				count += inspectDB.insertInspectPlan(onePlan);
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateInspectPlanOP
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个保养定义数据。
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
	public int updateInspectPlanOP(InspectPlanPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateInspectPlanOP(sqlHelper, onePojo);
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
	 * 方法名称: updateInspectPlanOP
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个保养定义数据。
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
	public int updateInspectPlanOP(SqlExecute sqlHelper, InspectPlanPojo onePojo)
			throws Exception {
		InspectDBMang inspectDB = new InspectDBMang(sqlHelper, m_bs);
		return inspectDB.updateInspectPlanOP(onePojo);
	}
}
