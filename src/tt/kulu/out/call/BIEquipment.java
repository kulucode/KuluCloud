package tt.kulu.out.call;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.dic.pojo.DicPojo;
import tt.kulu.bi.storage.dbclass.EquipmentDBMang;
import tt.kulu.bi.storage.pojo.EquipmentDefPojo;
import tt.kulu.bi.storage.pojo.EquipmentGeometryPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstWorkLogPojo;
import net.sf.json.JSONObject;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BIEquipment
 * </p>
 * <p>
 * 功能描述: 物品接口类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2011-1-25
 * </p>
 */
public class BIEquipment extends BSDBBase {
	public BIEquipment(BSObject m_bs) throws Exception {
		super(null, m_bs);
	}

	public BIEquipment(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getEqpDefByRedis
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
	public EquipmentDefPojo getEqpDefByRedis(String defId) throws Exception {
		EquipmentDefPojo onePojo = new EquipmentDefPojo();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getStringData("KEQPDEF_" + defId,
				URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到
			onePojo = this.getOneEquipmentDefById(defId);
			if (onePojo != null) {
				redisBI.setStringData("KEQPDEF_" + defId, JSONObject
						.fromObject(onePojo).toString(),
						URLlImplBase.REDIS_KULUDATA);
			}
		} else {
			Map config = new HashMap();
			config.put("eqpType", DicItemPojo.class);
			config.put("dic", DicPojo.class);
			onePojo = (EquipmentDefPojo) JSONObject.toBean(
					JSONObject.fromObject(redisS), EquipmentDefPojo.class,
					config);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: setEqpDefToRedis
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
	public void setEqpDefToRedis(String defId) throws Exception {
		EquipmentDefPojo onePojo = this.getOneEquipmentDefById(defId);
		BIRedis redisBI = new BIRedis();
		if (onePojo != null) {
			redisBI.setStringData("KEQPDEF_" + defId,
					JSONObject.fromObject(onePojo).toString(),
					URLlImplBase.REDIS_KULUDATA);
		}
	}

	/**
	 * <p>
	 * 方法名称: getEqpDefByRedis
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
	public String getEqpInstFDIdByRedis(String eqpId) throws Exception {
		String fdId = "";
		BIRedis redisBI = new BIRedis();
		fdId = redisBI.getStringData("KEQPINSTFDID_" + eqpId,
				URLlImplBase.REDIS_KULUDATA);
		if (fdId == null || fdId.trim().equals("")) {
			// 从数据库的到
			fdId = this.getOneEquipmentGeoFDId(eqpId);
			redisBI.setStringData("KEQPINSTFDID_" + eqpId, fdId,
					URLlImplBase.REDIS_KULUDATA);
		}
		return fdId;
	}

	/**
	 * <p>
	 * 方法名称: getEqpDefByRedis
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
	public String setEqpInstFDIdToRedis(String eqpId) throws Exception {
		String fdId = BSGuid.getRandomGUID();
		BIRedis redisBI = new BIRedis();
		redisBI.setStringData("KEQPINSTFDID_" + eqpId, fdId,
				URLlImplBase.REDIS_KULUDATA);
		return fdId;
	}

	/**
	 * <p>
	 * 方法名称: getEquipmentDefList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品定义列表。
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
	public ArrayList<EquipmentDefPojo> getEquipmentDefList(JSONObject paras)
			throws Exception {
		ArrayList<EquipmentDefPojo> list = new ArrayList<EquipmentDefPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getEquipmentDefList(sqlHelper, paras);
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
	 * 方法名称: getEquipmentDefList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品定义列表。
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
	public ArrayList<EquipmentDefPojo> getEquipmentDefList(
			SqlExecute sqlHelper, JSONObject paras) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.EQP_NAME";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t.EQP_CODE=? OR t.EQP_NAME like ? OR t.EQP_MANUFACTURER like ? OR t.EQP_BRAND like ?)";
					vList.add(v);
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("type")) {
					// 关键字
					where += " and t.EQP_TYPE=?";
					vList.add(v);
				}
				if (key.equals("style")) {
					// 根
					where += " and t.EQP_STYLE in (";
					String[] vs = v.split(",");
					String inW = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							inW += ",?";
							vList.add(Integer.parseInt(oneV));
						}
					}
					where += inW.substring(1) + ")";
				}
			}
		}
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.getEquipmentDefList(where, orderBy, vList);
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentDefById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品定义。
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
	public EquipmentDefPojo getOneEquipmentDefById(String equipmentId)
			throws Exception {
		EquipmentDefPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneEquipmentDefById(sqlHelper, equipmentId);
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
	 * 方法名称: getOneEquipmentDefById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品定义。
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
	public EquipmentDefPojo getOneEquipmentDefById(SqlExecute sqlHelper,
			String equipmentId) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.getOneEquipmentDefById(equipmentId);
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentDefById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品定义。
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
	public EquipmentDefPojo getOneEquipmentDefByEqpNo(String typeId, String no)
			throws Exception {
		EquipmentDefPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneEquipmentDefByEqpNo(sqlHelper, typeId, no);
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
	 * 方法名称: getOneEquipmentDefByEqpNo
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品定义。
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
	public EquipmentDefPojo getOneEquipmentDefByEqpNo(SqlExecute sqlHelper,
			String typeId, String no) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.getOneEquipmentDefByEqpNo(typeId, no);
	}

	/**
	 * <p>
	 * 方法名称: insertEquipmentDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个物品定义数据。
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
	public int insertEquipmentDef(EquipmentDefPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertEquipmentDef(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setEqpDefToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertEquipmentDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个物品定义数据。
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
	public int insertEquipmentDef(SqlExecute sqlHelper, EquipmentDefPojo onePojo)
			throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.insertEquipmentDef(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品定义数据。
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
	public int updateEquipmentDef(EquipmentDefPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateEquipmentDef(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setEqpDefToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品定义数据。
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
	public int updateEquipmentDef(SqlExecute sqlHelper, EquipmentDefPojo onePojo)
			throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.updateEquipmentDef(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: getEquipmentInstList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品实例列表。
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
	public ArrayList<EquipmentInstPojo> getEquipmentInstList(JSONObject paras,
			long f, long t) throws Exception {
		ArrayList<EquipmentInstPojo> list = new ArrayList<EquipmentInstPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getEquipmentInstList(sqlHelper, paras, f, t);
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
	 * 方法名称: getEquipmentInstList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品实例列表。
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
	public ArrayList<EquipmentInstPojo> getEquipmentInstList(
			SqlExecute sqlHelper, JSONObject paras, long f, long t)
			throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.EQP_NAME";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t.EQP_WYCODE=? OR t.EQP_QR like ? OR t.EQP_NAME like ?)";
					vList.add(v);
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("style")) {
					// 根
					where += " and t1.EQP_STYLE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("defid")) {
					// 根
					where += " and t.EQP_DEF=?";
					vList.add(v);
				}
				if (key.equals("eqptype")) {
					// 根
					where += " and t.EQP_DEF in (select v.EQP_CODE from T_EQUIPMENT_DEF v where v.EQP_TYPE=?)";
					vList.add(v);
				}
				if (key.equals("state")) {
					// 根
					where += " and t.EQP_STATE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("location")) {
					where += " and t.EQP_CODE in (select x.EQP_CODE from TB_LOCATION x where x.LOCATION_ID=?)";
					vList.add(v);
				}
				if (key.equals("peqp")) {
					// 关系
					where += " and t.EQP_INST in (select v.EQP_INST from T_EQUIPMENT_INST_R v where v.P_EQP_INST=?)";
					vList.add(v);
				}
				if (key.equals("group")) {
					where += " and t.ORG_ID in "
							+ URLlImplBase.LOGIN_GROUP_WHERE;
					vList.add("%," + v + "%");
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
			}
		}
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		paras.put("max", quipmentDB.getEquipmentInstCount(where, vList));
		return quipmentDB.getEquipmentInstList(where, orderBy, f, t, vList);
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentInstById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
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
	public EquipmentInstPojo getOneEquipmentInstById(String instId)
			throws Exception {
		EquipmentInstPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneEquipmentInstById(sqlHelper, instId);
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
	 * 方法名称: getOneEquipmentInstById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
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
	public EquipmentInstPojo getOneEquipmentInstById(SqlExecute sqlHelper,
			String instId) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.getOneEquipmentInstById(instId);
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentInstByToken
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
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
	public EquipmentInstPojo getOneEquipmentInstByToken(String instId)
			throws Exception {
		EquipmentInstPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneEquipmentInstByToken(sqlHelper, instId);
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
	 * 方法名称: getOneEquipmentInstByToken
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
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
	public EquipmentInstPojo getOneEquipmentInstByToken(SqlExecute sqlHelper,
			String instId) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.getOneEquipmentInstByToken(instId);
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentInstByToken
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
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
	public EquipmentInstPojo getOnevehicleInstByTruck(String truckId)
			throws Exception {
		EquipmentInstPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOnevehicleInstByTruck(sqlHelper, truckId);
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
	 * 方法名称: getOnevehicleInstByTruck
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
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
	public EquipmentInstPojo getOnevehicleInstByTruck(SqlExecute sqlHelper,
			String truckId) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.getOnevehicleInstByTruck(truckId);
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentInstByWyCode
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
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
	public EquipmentInstPojo getOneEquipmentInstByWyCode(String instId)
			throws Exception {
		EquipmentInstPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneEquipmentInstByWyCode(sqlHelper, instId);
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
	 * 方法名称: getOneEquipmentInstByToken
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
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
	public EquipmentInstPojo getOneEquipmentInstByWyCode(SqlExecute sqlHelper,
			String instId) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.getOneEquipmentInstByWyCode(instId);
	}

	/**
	 * <p>
	 * 方法名称: equipmentInstLogin
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个物品实例数据。
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
	public int equipmentInstLogin(EquipmentInstPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.equipmentInstLogin(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		if (count > 0 && onePojo != null && !onePojo.getInstId().equals("")) {
			this.setEqpInstFDIdToRedis(onePojo.getInstId());
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: equipmentInstLogin
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个物品实例数据。
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
	public int equipmentInstLogin(SqlExecute sqlHelper,
			EquipmentInstPojo onePojo) throws Exception {
		int count = 0;
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		// 判断该code是否存在
		EquipmentInstPojo tempPojo = quipmentDB
				.getOneEquipmentInstByWyCode(onePojo.getWyCode());
		if (tempPojo == null) {
			// count = quipmentDB.insertEquipmentInst(onePojo);
		} else {
			if (onePojo != null) {
				onePojo.setInstId(tempPojo.getInstId());
				onePojo.setOnlineState(1);
				count += quipmentDB.updateEquipmentInstLast(onePojo);
			}
		}
		if (count > 0) {
			// 写入开关机记录
			EquipmentInstWorkLogPojo oneW = new EquipmentInstWorkLogPojo();
			oneW.setEqpInst(onePojo);
			oneW.setDate(onePojo.getThisDate());
			oneW.setState(1);
			count += quipmentDB.insertEquipmentInstWorkLog(oneW);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: equipmentInstLoginOut
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个物品实例数据。
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
	public int equipmentInstLoginOut(EquipmentInstPojo onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.equipmentInstLoginOut(sqlHelper, onePojo);
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
	 * 方法名称: equipmentInstLoginOut
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个物品实例数据。
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
	public int equipmentInstLoginOut(SqlExecute sqlHelper,
			EquipmentInstPojo onePojo) throws Exception {
		int count = 0;
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		if (onePojo != null) {
			onePojo.setOnlineState(0);
			count += quipmentDB.updateEquipmentInstLast(onePojo);
		}
		// 写入开关机记录
		EquipmentInstWorkLogPojo oneW = new EquipmentInstWorkLogPojo();
		oneW.setEqpInst(onePojo);
		oneW.setDate(onePojo.getThisDate());
		oneW.setState(0);
		count += quipmentDB.insertEquipmentInstWorkLog(oneW);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: equipmentInstIni
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个物品实例数据。
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
	public int insertEquipmentGeometry(EquipmentGeometryPojo onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertEquipmentGeometry(sqlHelper, onePojo);
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
	 * 方法名称: insertEquipmentGeometry
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个物品实例数据。
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
	public int insertEquipmentGeometry(SqlExecute sqlHelper,
			EquipmentGeometryPojo onePojo) throws Exception {
		int count = 0;
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		onePojo.setEqpInst(quipmentDB.getOneEquipmentInstByWyCode(onePojo
				.getEqpInst().getWyCode()));
		if (onePojo.getEqpInst() != null) {
			count += quipmentDB.insertEquipmentGeometry(onePojo);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertEquipmentDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个物品实例数据。
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
	public int insertEquipmentInst(EquipmentInstPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertEquipmentInst(sqlHelper, onePojo);
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
	 * 方法名称: insertEquipmentDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个物品实例数据。
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
	public int insertEquipmentInst(SqlExecute sqlHelper,
			EquipmentInstPojo onePojo) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.insertEquipmentInst(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInst
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int updateEquipmentInst(EquipmentInstPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateEquipmentInst(sqlHelper, onePojo);
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
	 * 方法名称: updateEquipmentInst
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int updateEquipmentInst(SqlExecute sqlHelper,
			EquipmentInstPojo onePojo) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.updateEquipmentInst(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInstRel
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int updateEquipmentInstRel(String pEqp, String relEqp)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateEquipmentInstRel(sqlHelper, pEqp, relEqp);
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
	 * 方法名称: updateEquipmentInstRel
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int updateEquipmentInstRel(SqlExecute sqlHelper, String pEqp,
			String relEqp) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.updateEquipmentInstRel(pEqp, relEqp);
	}

	/**
	 * <p>
	 * 方法名称: deleteEquipmentInstRel
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int deleteEquipmentInstRel(String pEqp, String relEqp)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteEquipmentInstRel(sqlHelper, pEqp, relEqp);
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
	 * 方法名称: deleteEquipmentInstRel
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int deleteEquipmentInstRel(SqlExecute sqlHelper, String pEqp,
			String relEqp) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.deleteEquipmentInstRel(pEqp, relEqp);
	}

	/**
	 * <p>
	 * 方法名称: checkEquipmentQRCode
	 * </p>
	 * <p>
	 * 方法功能描述:判断是有重复条码。
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
	public int checkEquipmentQRCode(String code) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.checkEquipmentQRCode(sqlHelper, code);
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
	 * 方法名称: checkEquipmentQRCode
	 * </p>
	 * <p>
	 * 方法功能描述: 判断是有重复条码。
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
	public int checkEquipmentQRCode(SqlExecute sqlHelper, String code)
			throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.checkEquipmentQRCode(code);
	}

	/**
	 * <p>
	 * 方法名称: checkEquipmentWYCode
	 * </p>
	 * <p>
	 * 方法功能描述:判断是有重复条码。
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
	public int checkEquipmentWYCode(String code, String defId) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.checkEquipmentWYCode(sqlHelper, code);
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
	 * 方法名称: checkEquipmentQRCode
	 * </p>
	 * <p>
	 * 方法功能描述: 判断是有重复条码。
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
	public int checkEquipmentWYCode(SqlExecute sqlHelper, String code)
			throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.checkEquipmentWYCode(code);
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
	public ArrayList<EquipmentGeometryPojo> getEqpGeometryList(
			JSONObject paras, long f, long t) throws Exception {
		ArrayList<EquipmentGeometryPojo> list = new ArrayList<EquipmentGeometryPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getEqpGeometryList(sqlHelper, paras, f, t);
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
	 * 方法名称: getEqpGeometryList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量设备最后的地理位置
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
	public ArrayList<EquipmentGeometryPojo> getEqpGeometryList(
			SqlExecute sqlHelper, JSONObject paras, long f, long t)
			throws Exception {
		ArrayList<EquipmentGeometryPojo> list = new ArrayList<EquipmentGeometryPojo>();
		// 参数设定
		String where = "";
		String key = "";
		String orderBy = " t.s_cdate";
		List<Object> vList = new ArrayList<Object>();
		if (paras != null) {
			Iterator<String> keys = paras.keys();
			while (keys.hasNext()) {
				key = keys.next();
				String v = paras.getString(key);
				if (!v.equals("")) {
					if (key.equals("datatype")) {
						switch (v) {
						case "00":// 车载
							where += " and t2.EQP_STYLE=0";
							break;
						case "01":// 手环
							where += " and t2.EQP_STYLE=1";
							break;
						default:
							break;
						}
					}
					if (key.equals("eqpid")) {
						where += " and t.EQP_INST=?";
						vList.add(v);
					}
					if (key.equals("user")) {
						where += " and t1.EQP_MUSER=?";
						vList.add(v);
					}
					if (key.equals("truck")) {
						where += " and t1.EQP_TRUCK=?";
						vList.add(v);
					}
					if (key.equals("fdid")) {
						where += " and t.S_FDID=?";
						vList.add(v);
					}
					if (key.equals("date")) {
						// 时间
						String vs[] = v.split(",");
						if (vs.length > 1 && !vs[0].equals("")
								&& !vs[1].equals("")) {
							where += " and t.s_cdate BETWEEN ? AND ?";
							vList.add(Timestamp.valueOf(vs[0]));
							vList.add(Timestamp.valueOf(vs[1]));
						}
					}
				}
			}
		}
		// 设置用户列表
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		paras.put("max", quipmentDB.getEqpGeometryCount(where, vList));
		return quipmentDB.getEqpGeometryList(where, orderBy, f, t, vList);
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentDefById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品定义。
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
	public String getOneEquipmentGeoFDId(String eqpId) throws Exception {
		String fdId = "";
		SqlExecute sqlHelper = new SqlExecute();
		try {
			fdId = this.getOneEquipmentGeoFDId(sqlHelper, eqpId);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return fdId;
	}

	/**
	 * <p>
	 * 方法名称: getOneEquipmentGeoFDId
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
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
	public String getOneEquipmentGeoFDId(SqlExecute sqlHelper, String eqpId)
			throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.getOneEquipmentGeoFDId(eqpId);
	}

	/**
	 * <p>
	 * 方法名称: getLastEquipmentInstWorkLog
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
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
	public EquipmentInstWorkLogPojo getLastEquipmentInstWorkLog(String instId,
			int state, int offset) throws Exception {
		EquipmentInstWorkLogPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getLastEquipmentInstWorkLog(sqlHelper, instId,
					state, offset);
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
	 * 方法名称: getLastEquipmentInstWorkLog
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
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
	public EquipmentInstWorkLogPojo getLastEquipmentInstWorkLog(
			SqlExecute sqlHelper, String instId, int state, int offset)
			throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.getLastEquipmentInstWorkLog(instId, state, offset);
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInstWorkLog
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int updateEquipmentInstWorkLog(EquipmentInstWorkLogPojo onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateEquipmentInstWorkLog(sqlHelper, onePojo);
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
	 * 方法名称: updateEquipmentInstWorkLog
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int updateEquipmentInstWorkLog(SqlExecute sqlHelper,
			EquipmentInstWorkLogPojo onePojo) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.updateEquipmentInstWorkLog(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInstMUser
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int updateEquipmentInstMUser(EquipmentInstPojo onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateEquipmentInstMUser(sqlHelper, onePojo);
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
	 * 方法名称: updateEquipmentInstMUser
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int updateEquipmentInstMUser(SqlExecute sqlHelper,
			EquipmentInstPojo onePojo) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.updateEquipmentInstMUser(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateEquipmentInstTruck
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int updateEquipmentInstTruck(EquipmentInstPojo onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateEquipmentInstTruck(sqlHelper, onePojo);
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
	 * 方法名称: updateEquipmentInstTruck
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个物品实例数据。
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
	public int updateEquipmentInstTruck(SqlExecute sqlHelper,
			EquipmentInstPojo onePojo) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		return quipmentDB.updateEquipmentInstTruck(onePojo);
	}
}
