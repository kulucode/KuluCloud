package tt.kulu.out.call;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import com.tt4j2ee.BIRedis;

import javax.sql.rowset.CachedRowSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.map.biclass.CoordTransformBI;
import tt.kulu.bi.map.pojo.Gps;
import tt.kulu.bi.message.biclass.BIKuluMQAdapter;
import tt.kulu.bi.message.biclass.BITTWebSocketSend;
import tt.kulu.bi.storage.dbclass.EquipmentDBMang;
import tt.kulu.bi.storage.pojo.EquipmentDefPojo;
import tt.kulu.bi.storage.pojo.EquipmentGeometryPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstWorkLogPojo;
import tt.kulu.bi.truck.dbclass.TruckDBMang;
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
import tt.kulu.bi.user.dbclass.BSUserDBMang;
import tt.kulu.bi.user.pojo.OrgPojo;
import tt.kulu.bi.user.pojo.UserPojo;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BITruck
 * </p>
 * <p>
 * 功能描述: 车辆业务接口类
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
public class BITruck extends BSDBBase {

	public BITruck(BSObject m_bs) throws Exception {
		super(null, m_bs);
	}

	public BITruck(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getTruckDefByRedis
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
	public TruckDefinePojo getTruckDefByRedis(String defId) throws Exception {
		TruckDefinePojo onePojo = new TruckDefinePojo();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getStringData("KTRUCKDEF_" + defId,
				URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到
			onePojo = this.getOneTruckDefById(defId);
			if (onePojo != null) {
				redisBI.setStringData("KTRUCKDEF_" + defId, JSONObject
						.fromObject(onePojo).toString(),
						URLlImplBase.REDIS_KULUDATA);
			}
		} else {
			onePojo = (TruckDefinePojo) JSONObject.toBean(
					JSONObject.fromObject(redisS), TruckDefinePojo.class);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: setTruckDefToRedis
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
	public void setTruckDefToRedis(String defId) throws Exception {
		TruckDefinePojo onePojo = this.getOneTruckDefById(defId);
		BIRedis redisBI = new BIRedis();
		if (onePojo != null) {
			redisBI.setStringData("KTRUCKDEF_" + defId,
					JSONObject.fromObject(onePojo).toString(),
					URLlImplBase.REDIS_KULUDATA);
		} else {
			redisBI.delData("KTRUCKDEF_" + defId, URLlImplBase.REDIS_KULUDATA);
		}
	}

	/**
	 * <p>
	 * 方法名称: getVehicleNewestByRedis
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
	public JSONObject getVehicleNewestByRedis(String id) throws Exception {
		// 得到最新的一条记录
		JSONObject onePojo = new JSONObject();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getMapData("VEHICLE_NEWEST", id,
				URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到到最新的
			onePojo = this.getVehicleNewestByInst(id);
			if (onePojo != null) {
				redisBI.setMapData("VEHICLE_NEWEST", id,
						JSONObject.fromObject(onePojo).toString(),
						URLlImplBase.REDIS_KULUDATA);
			}
		} else {
			onePojo = JSONObject.fromObject(redisS);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: setTruckDefToRedis
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
	public void setVehicleNewestToRedis(JSONObject msg) throws Exception {
		// 得到最新的一条记录
		BIRedis redisBI = new BIRedis();
		if (msg != null && msg.containsKey("instid")) {
			redisBI.setMapData("VEHICLE_NEWEST", msg.getString("instid"),
					msg.toString(), URLlImplBase.REDIS_KULUDATA);
		}
	}

	/**
	 * <p>
	 * 方法名称: setTruckDefToRedis
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
	public void deleteVehicleNewestToRedis(String instid) throws Exception {
		// 得到最新的一条记录
		BIRedis redisBI = new BIRedis();
		if (instid != null && !instid.equals("")) {
			redisBI.delMapData("VEHICLE_NEWEST", instid,
					URLlImplBase.REDIS_KULUDATA);
		}
	}

	/**
	 * <p>
	 * 方法名称: setTruckDefToRedis
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
	public void deleteAllVehicleNewestToRedis() throws Exception {
		// 得到最新的一条记录
		BIRedis redisBI = new BIRedis();
		redisBI.delData("VEHICLE_NEWEST", URLlImplBase.REDIS_KULUDATA);
	}

	/**
	 * <p>
	 * 方法名称: getDriverSchedulingRedis
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
	public JSONArray getDriverSchedulingRedis(String day, String truckId)
			throws Exception {
		JSONArray list = new JSONArray();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getStringData(
				"KTRUCKDS_" + truckId + "_" + day, URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到
			JSONObject _paras = new JSONObject();
			_paras.put("day", day);
			_paras.put("truck", truckId);
			ArrayList<DriverSchedulingPojo> newL = this
					.getDriverSchedulingList(_paras);
			if (newL.size() > 0) {
				redisBI.setStringData("KTRUCKDS_" + truckId + "_" + day,
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
	 * 方法名称: setDriverSchedulingToRedis
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
	public void setDriverSchedulingToRedis(String day, String truckId)
			throws Exception {
		JSONObject _paras = new JSONObject();
		_paras.put("day", day);
		_paras.put("truck", truckId);
		ArrayList<DriverSchedulingPojo> newL = this
				.getDriverSchedulingList(_paras);
		BIRedis redisBI = new BIRedis();
		if (newL.size() > 0) {
			redisBI.setStringData("KTRUCKDS_" + truckId + "_" + day, JSONArray
					.fromObject(newL).toString(), URLlImplBase.REDIS_KULUDATA);
		}
	}

	/**
	 * <p>
	 * 方法名称: getTruckDefList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆定义列表。
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
	public ArrayList<TruckDefinePojo> getTruckDefList(JSONObject paras)
			throws Exception {
		ArrayList<TruckDefinePojo> list = new ArrayList<TruckDefinePojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getTruckDefList(sqlHelper, paras);
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
	 * 方法名称: getTruckDefList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆定义列表。
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
	public ArrayList<TruckDefinePojo> getTruckDefList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.TD_NAME";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t.TD_ID=? OR t.TD_NAME like ? OR t.TD_NO like ? OR t.TD_COMPANY like ? OR t.TD_LINKMAN like ?)";
					vList.add(v);
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
			}
		}
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.getTruckDefList(where, orderBy, vList);
	}

	/**
	 * <p>
	 * 方法名称: getOneTruckDefById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个车辆定义。
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
	public TruckDefinePojo getOneTruckDefById(String equipmentId)
			throws Exception {
		TruckDefinePojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneTruckDefById(sqlHelper, equipmentId);
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
	 * 方法名称: getOneTruckDefById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个车辆定义。
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
	public TruckDefinePojo getOneTruckDefById(SqlExecute sqlHelper,
			String equipmentId) throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.getOneTruckDefById(equipmentId);
	}

	/**
	 * <p>
	 * 方法名称: insertTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int insertTruckDef(TruckDefinePojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertTruckDef(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setTruckDefToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int insertTruckDef(SqlExecute sqlHelper, TruckDefinePojo onePojo)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.insertTruckDef(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int updateTruckDef(TruckDefinePojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateTruckDef(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setTruckDefToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int updateTruckDef(SqlExecute sqlHelper, TruckDefinePojo onePojo)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.updateTruckDef(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: getTruckList
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
	public ArrayList<TruckPojo> getTruckList(JSONObject paras, long f, long t)
			throws Exception {
		ArrayList<TruckPojo> list = new ArrayList<TruckPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getTruckList(sqlHelper, paras, f, t);
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
	 * 方法名称: getTruckList
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
	public ArrayList<TruckPojo> getTruckList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.TRUCK_IDATE desc";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t.PLATE_NUM=? OR t.TRUCK_NO=? OR t.TRUCK_NAME like ?)";
					vList.add(v);
					vList.add(v);
					vList.add("%" + v + "%");
				}
				if (key.equals("state")) {
					// 根
					where += " and t.TRUCK_STATE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("group")) {
					where += " and t.ORG_ID in "
							+ URLlImplBase.LOGIN_GROUP_WHERE;
					vList.add("%," + v + "%");
				}
				if (key.equals("onlygroup")) {
					where += " and t.ORG_ID=?";
					vList.add(v);
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
				if (key.equals("define")) {
					// 关键字
					where += " and t.TD_ID=?";
					vList.add(v);
				}
				if (key.equals("notinspect")) {
					// 关键字
					where += " and t.TRUCK_ID not in (select v.TRUCK_ID from T_INSPECT_PLAN v where v.DEF_ID=?)";
					vList.add(v);
				}
				if (key.equals("inspect")) {
					// 关键字
					where += " and t.TRUCK_ID not in (select v.TRUCK_ID from T_INSPECT_PLAN v where v.DEF_ID=?)";
					vList.add(v);
				}
			}
		}
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		paras.put("max", truckDB.getTruckCount(where, vList));
		return truckDB.getTruckList(where, orderBy, f, t, vList);
	}

	/**
	 * <p>
	 * 方法名称: getOneTruckById
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
	public TruckPojo getOneTruckById(String instId) throws Exception {
		TruckPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneTruckById(sqlHelper, instId);
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
	 * 方法名称: getOneTruckById
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
	public TruckPojo getOneTruckById(SqlExecute sqlHelper, String instId)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.getOneTruckById(instId);
	}

	/**
	 * <p>
	 * 方法名称: insertTruck
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int insertTruck(TruckPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertTruck(sqlHelper, onePojo);
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
	 * 方法名称: insertTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int insertTruck(SqlExecute sqlHelper, TruckPojo onePojo)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.insertTruck(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int updateTruck(TruckPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateTruck(sqlHelper, onePojo);
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
	 * 方法名称: updateTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int updateTruck(SqlExecute sqlHelper, TruckPojo onePojo)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.updateTruck(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateOneTruckState
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
	public int updateOneTruckState(String userId, int state) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateOneTruckState(sqlHelper, userId, state);
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
	 * 方法名称: updateOneTruckState
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
	public int updateOneTruckState(SqlExecute sqlHelper, String userId,
			int state) throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.updateOneTruckState(userId, state);
	}

	/**
	 * <p>
	 * 方法名称: deleteOneTruck
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
	public int deleteOneTruck(String userId) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteOneTruck(sqlHelper, userId);
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
	 * 方法名称: deleteOneTruck
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
	public int deleteOneTruck(SqlExecute sqlHelper, String instId)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.deleteOneTruck(instId);
	}

	/**
	 * <p>
	 * 方法名称: insertVehicleData
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int insertVehicleData(PacketLocationReport onePojo,
			EquipmentGeometryPojo oneEqpGeo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			// oneEqpGeo.setSysDate(this.bsDate.getThisDate(0, 0));
			count = this.insertVehicleData(sqlHelper, onePojo, oneEqpGeo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		// 关联了车辆则执行如下代码
		BIEquipment eqpBI = new BIEquipment(null);
		if (oneEqpGeo.getEqpInst().getTruck() != null
				&& !oneEqpGeo.getEqpInst().getTruck().getId().equals("")) {
			// 判断围栏，并写入数据
			count += new BIFance(m_bs).doCheckPoineInFanceByTruck(onePojo,
					oneEqpGeo);
			// 修改工作时长
			// 得到上次开机时间
			EquipmentInstWorkLogPojo openLog = new EquipmentInstWorkLogPojo();
			openLog.getEqpInst().setInstId(oneEqpGeo.getEqpInst().getInstId());
			openLog.setState(1);
			String lastD = eqpBI.getEqpInstLastWorkTimeByRedis(oneEqpGeo
					.getEqpInst().getInstId());
			lastD = (lastD == null || lastD.equals("-1")) ? oneEqpGeo
					.getSysDate() : lastD;
			openLog.setDate(lastD);
			if (openLog != null
					&& openLog.getState() == 1
					&& this.bsDate.getDateMillCount(openLog.getDate(),
							oneEqpGeo.getSysDate()) > 0) {
				// 更新车辆工作时长
				this.updateTruckWorkTime(oneEqpGeo.getEqpInst().getTruck()
						.getId(), openLog.getDate(), oneEqpGeo.getSysDate());
				openLog.setDate(oneEqpGeo.getSysDate());
				// new BIEquipment(null).updateEquipmentInstWorkLog(openLog);
			}
		}
		if (count > 0) {
			// 设置最新数据如redis
			this.setVehicleNewestToRedis(this.getVehicleNewestByInst(oneEqpGeo
					.getEqpInst().getInstId()));
			eqpBI.setEqpInstLastWorkTimeByRedis(oneEqpGeo.getEqpInst()
					.getInstId(), oneEqpGeo.getSysDate());
		}

		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertVehicleData
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int insertVehicleData(SqlExecute sqlHelper,
			PacketLocationReport onePojo, EquipmentGeometryPojo oneEqpGeo)
			throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(oneEqpGeo.getEqpInst().getInstId());
		// 写入位置
		BIDic dicBI = new BIDic(null, null);
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		oneEqpGeo.setSpeed(String.valueOf(onePojo.getSpeed()));
		oneEqpGeo.setLastStopDate("");
		// 得到上一条位置信息
		JSONObject lastDate = this.getVehicleNewestByRedis(oneEqpGeo
				.getEqpInst().getInstId());
		// EquipmentGeometryPojo lastGeo = quipmentDB
		// .getLastEquipmentGeoPojo(oneEqpGeo.getEqpInst().getInstId());
		if (lastDate != null) {
			if (!lastDate.containsKey("fdId")) {
				lastDate.put("fdId", "");
			}
			// 判断上一条记录速度是否为0，同时判断上次停车时间是否超时。
			if (Math.abs(Float.valueOf(oneEqpGeo.getSpeed()) - 0) <= 0) {
				if (!lastDate.containsKey("lastStopDate")
						|| lastDate.getString("lastStopDate").equals("")) {
					oneEqpGeo.setLastStopDate(oneEqpGeo.getSysDate());
				} else {
					oneEqpGeo.setLastStopDate(lastDate
							.getString("lastStopDate"));
					// 得到停车间隔的时间
					DicItemPojo oneItem = dicBI
							.getDicItemByRedis("VEHICLE_PARAS_2");
					int stopDate = 30;
					if (oneItem != null && !oneItem.getValue2().equals("")) {
						stopDate = Integer.parseInt(oneItem.getValue2());
					}
					if (this.bsDate
							.getDateMillCount(oneEqpGeo.getLastStopDate(),
									oneEqpGeo.getSysDate()) > stopDate * 60 * 1000) {
						// 停车超时，新的段号
						new BIEquipment(m_bs).setEqpInstFDIdToRedis(oneEqpGeo
								.getEqpInst().getInstId());
						oneEqpGeo.setLastStopDate(oneEqpGeo.getSysDate());
					} else if (!lastDate.getString("fdId").equals(
							(new BIEquipment(null))
									.getEqpInstFDIdByRedis(oneEqpGeo
											.getEqpInst().getInstId()))) {
						// 如果分段ID重置了
						oneEqpGeo.setLastStopDate(oneEqpGeo.getSysDate());
					}
				}

			}
		}
		count += quipmentDB.insertEquipmentGeometry(oneEqpGeo);
        if (onePojo.getOilLevel() <= 0)
            return count;

		// 得到油量算法
		// 得到上一条记录
		// DicItemPojo oneOilItem = dicBI.getDicItemByRedis("VEHICLE_PARAS_3");
		// String oilDiffMax = "1000";
		// if (oneOilItem != null && !oneOilItem.getValue2().equals("")) {
		// oilDiffMax = oneOilItem.getValue2();
		// }
		String lastDiff = "";
		// String lastDiff = (String) sqlHelper
		// .queryObjectBySql(
		// "select OIL_LEVEL from T_VEHICLE_DATA where EQP_INST=? and C_DATE is not null order by C_DATE desc,S_CDATE desc   LIMIT 1 OFFSET 0",
		// vList);
		if (lastDate != null && lastDate.containsKey("oil")) {
			lastDiff = lastDate.getString("oil");
		}
		// 比较液面差
		if (lastDiff == null || lastDiff.equals("")) {
			lastDiff = "0";
		}
		float diff = Float.valueOf(lastDiff) - Float.valueOf(onePojo.getOilLevel());
		if (diff <= 0) {
			diff = 0;
		}
		// if (diff - Float.valueOf(oilDiffMax) >= 0) {
		// // diff = 0;
		// }
		double valume = 0;
		onePojo.setOilDeff(diff);
		if (oneEqpGeo.getEqpInst().getTruck().getOilDef() != null) {
			oneEqpGeo.getEqpInst().getTruck().getOilDef()
					.put("X", (onePojo.getOilLevel()) / 100);
			// 计算当前体积
			valume = BITruck.getOilVolume(oneEqpGeo.getEqpInst().getTruck()
					.getOilDef());
		}
		if (diff > 0) {
			// 得到页面传感器的参数
			onePojo.setThisOilV(valume);// 当前油量
			if (lastDate != null && lastDate.containsKey("thisoilv")) {
				onePojo.setValume(lastDate.getDouble("thisoilv") - valume);// 容积差
			} else {
				onePojo.setValume(valume);
			}
		} else {
			onePojo.setValume(0);
			onePojo.setThisOilV(valume);// 当前油量
		}
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		count += truckDB.insertVehicleData(onePojo, oneEqpGeo);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int vehicleReg(EquipmentInstPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.vehicleReg(sqlHelper, onePojo);
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
	 * 方法名称: insertTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int vehicleReg(SqlExecute sqlHelper, EquipmentInstPojo onePojo)
			throws Exception {
		int count = 0;
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		// 根据设备编码得到实例，设备存在则设置token，不存在则丢弃
		BIEquipment eqpBI = new BIEquipment(m_bs);
		EquipmentInstPojo oneOldPojo = eqpBI.getOneEquipmentInstByWyCode(
				sqlHelper, onePojo.getWyCode());
		if (oneOldPojo != null) {
			oneOldPojo.setOnlineState(0);
			oneOldPojo.setToken(truckDB.getNewToken());
			onePojo.setToken(oneOldPojo.getToken());
			count = eqpBI.updateEquipmentInst(sqlHelper, oneOldPojo);
		}
		// if (oneOldPojo == null) {
		// // 新增新车型
		// onePojo.getEqpDef().getEqpType().setId("EQUIPMENT_DEFTYPE_0");
		// EquipmentDefPojo eqpDef = eqpBI.getOneEquipmentDefByEqpNo(
		// sqlHelper, onePojo.getEqpDef().getEqpType().getId(),
		// onePojo.getEqpDef().getNo());
		// if (eqpDef == null) {
		// // 新增车型
		// eqpDef = new EquipmentDefPojo();
		// eqpDef.getEqpType().setId(
		// onePojo.getEqpDef().getEqpType().getId());
		// eqpDef.setNo(onePojo.getEqpDef().getNo());
		// eqpDef.setName("新车载类型");
		// eqpDef.setPara1("mm");
		// eqpBI.insertEquipmentDef(sqlHelper, eqpDef);
		// }
		// onePojo.setEqpDef(eqpDef);
		// // 判断车牌存在，存在则关联，不存在则新增
		// TruckPojo truck = truckDB.getOneTruckByPlateNum(onePojo.getTruck()
		// .getPlateNum());
		// if (truck != null) {
		// onePojo.setTruck(truck);
		// }
		// // 新建设备实例
		// onePojo.setName("新的车载终端");
		// onePojo.setOnlineState(1);
		// onePojo.setToken(BSGuid.getRandomGUID());
		// count = eqpBI.insertEquipmentInst(sqlHelper, onePojo);
		// } else {
		//
		// }
		return count;
	}

	/**
	 * <p>
	 * 方法名：getOneTruckWorkDayLogsId
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
	public TruckWorkDayLogsPojo getOneTruckWorkDayLogsId(String id)
			throws Exception {
		TruckWorkDayLogsPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneTruckWorkDayLogsId(sqlHelper, id);
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneTruckWorkDayLogsId
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
	public TruckWorkDayLogsPojo getOneTruckWorkDayLogsId(SqlExecute sqlHelper,
			String id) throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.getOneTruckWorkDayLogsId(id);
	}

	/**
	 * <p>
	 * 方法名称: updateTruckWorkDayLogs
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
	public int updateTruckWorkDayLogs(TruckWorkDayLogsPojo onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateTruckWorkDayLogs(sqlHelper, onePojo);
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
	 * 方法名称: updateTruckWorkDayLogs
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
	public int updateTruckWorkDayLogs(SqlExecute sqlHelper,
			TruckWorkDayLogsPojo onePojo) throws Exception {
		int count = 0;
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		// 更新工作日志
		count += truckDB.updateTruckWorkDayLogs(onePojo);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateTruckWorkTime
	 * </p>
	 * <p>
	 * 方法功能描述: 更新工作时长。
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
	public int updateTruckWorkTime(String truckId, String sDate, String eDate)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateTruckWorkTime(sqlHelper, truckId, sDate, eDate);
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
	 * 方法名称: updateTruckWorkTime
	 * </p>
	 * <p>
	 * 方法功能描述: 更新工作时长。
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
	public int updateTruckWorkTime(SqlExecute sqlHelper, String truckId,
			String sDate, String eDate) throws Exception {
		int count = 0;
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		// 写入车辆完整日志
		TruckWorkStatsPojo oneStats = new TruckWorkStatsPojo();
		oneStats.getTruck().setId(truckId);
		oneStats.setEndDate(eDate);
		oneStats.setWorkTime(String.valueOf(this.bsDate.getDateMillCount(sDate,
				eDate) / 1000));
		count += truckDB.updateTruckWorkStatsWorkTime(oneStats);

		// 更新车辆当天日志表的时长
		TruckWorkDayLogsPojo oneDay = new TruckWorkDayLogsPojo();
		oneDay.getTruck().setId(truckId);
		oneDay.setDate(eDate);
		oneDay.setWorkSDate(sDate);
		oneDay.setWorkEDate(eDate);
		oneDay.setOpType(1);
		count += this.updateTruckWorkDayLogs(sqlHelper, oneDay);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateTruckWorkStatsData
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int updateTruckWorkStatsData(TruckWorkStatsPojo onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateTruckWorkStatsData(sqlHelper, onePojo);
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
	 * 方法名称: updateTruckWorkStatsData
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int updateTruckWorkStatsData(SqlExecute sqlHelper,
			TruckWorkStatsPojo onePojo) throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.updateTruckWorkStatsData(onePojo);
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
	public ArrayList<TruckWorkParasPojo> getTruckWordParasList(
			JSONObject paras, long f, long t) throws Exception {
		ArrayList<TruckWorkParasPojo> list = new ArrayList<TruckWorkParasPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getTruckWordParasList(sqlHelper, paras, f, t);
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
	public ArrayList<TruckWorkParasPojo> getTruckWordParasList(
			SqlExecute sqlHelper, JSONObject paras, long f, long t)
			throws Exception {
		// 参数设定
		String where = "";
		String key = "";
		String orderBy = " t.truck_name";
		List<Object> vList = new ArrayList<Object>();
		if (paras != null) {
			Iterator<String> keys = paras.keys();
			while (keys.hasNext()) {
				key = keys.next();
				String v = paras.getString(key);
				if (!v.equals("")) {
					if (key.equals("key")) {
						// 关键字
						where += " and (t.truck_name like ? or t.plate_num like ?)";
						vList.add(v);
						vList.add("%" + v + "%");
					}
					if (key.equals("group")) {
						// 关键字
						where += " and t.eqp_muser in "
								+ URLlImplBase.LOGIN_USER_WHERE;
						vList.add("%," + v + "%");
					}
					if (key.equals("truck")) {
						// 关键字
						where += " and t.truck_id=?";
						vList.add(v);
					}
					if (key.equals("login")) {
						String[] vs = v.split(",");
						String whereEx = "";
						for (String oneV : vs) {
							if (!oneV.equals("")) {
								whereEx += (whereEx.equals("") ? "" : " or ")
										+ "t.org_id in "
										+ URLlImplBase.LOGIN_GROUP_WHERE;
								vList.add("%," + oneV + "%");
							}
						}
						if (!whereEx.equals("")) {
							where += " and (" + whereEx + ")";
						}
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
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		paras.put("max", truckDB.getTruckWordParasCount(where, vList));
		return truckDB.getTruckWordParasList(where, orderBy, vList, f, t);
	}

	/**
	 * <p>
	 * 方法名称: getVehicleWordParasList
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
	public ArrayList<TruckWorkParasPojo> getVehicleWordParasList(
			JSONObject paras, long f, long t) throws Exception {
		ArrayList<TruckWorkParasPojo> list = new ArrayList<TruckWorkParasPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getVehicleWordParasList(sqlHelper, paras, f, t);
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
	 * 方法名称: getVehicleWordParasList
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
	public ArrayList<TruckWorkParasPojo> getVehicleWordParasList(
			SqlExecute sqlHelper, JSONObject paras, long f, long t)
			throws Exception {
		// 参数设定
		String where = "";
		String key = "";
		String orderBy = " t.truck_name";
		List<Object> vList = new ArrayList<Object>();
		if (paras != null) {
			Iterator<String> keys = paras.keys();
			while (keys.hasNext()) {
				key = keys.next();
				String v = paras.getString(key);
				if (!v.equals("")) {
					if (key.equals("key")) {
						// 关键字
						where += " and (t.truck_name like ? or t.plate_num like ?)";
						vList.add(v);
						vList.add("%" + v + "%");
					}
					if (key.equals("group")) {
						// 关键字
						where += " and t.eqp_muser in "
								+ URLlImplBase.LOGIN_USER_WHERE;
						vList.add("%," + v + "%");
					}
					if (key.equals("truck")) {
						// 关键字
						where += " and t.truck_id=?";
						vList.add(v);
					}
					if (key.equals("login")) {
						String[] vs = v.split(",");
						String whereEx = "";
						for (String oneV : vs) {
							if (!oneV.equals("")) {
								whereEx += (whereEx.equals("") ? "" : " or ")
										+ "t.eqp_muser in "
										+ URLlImplBase.LOGIN_USER_WHERE;
								vList.add("%," + oneV + "%");
							}
						}
						if (!whereEx.equals("")) {
							where += " and (" + whereEx + ")";
						}
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
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		paras.put("max", truckDB.getVehicleWordParasCount(where, vList));
		return truckDB.getVehicleWordParasList(where, orderBy, vList, f, t);
	}

	/**
	 * <p>
	 * 方法名称: getTruckDriverList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆定义列表。
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
	public ArrayList<TruckDriverRPojo> getTruckDriverList(JSONObject paras)
			throws Exception {
		ArrayList<TruckDriverRPojo> list = new ArrayList<TruckDriverRPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getTruckDriverList(sqlHelper, paras);
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
	 * 方法名称: getTruckDriverList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆定义列表。
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
	public ArrayList<TruckDriverRPojo> getTruckDriverList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t1.USER_NAME";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t.USER_ID=? OR t.USER_NAME like ? OR t.MPHONE like ?)";
					vList.add(v);
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("truck")) {
					// 车辆
					where += " and t.TRUCK_ID=?";
					vList.add(v);
				}
				if (key.equals("user")) {
					// 用户
					where += " and t.USER_INSTID=?";
					vList.add(v);
				}
			}
		}
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.getTruckDriverList(where, orderBy, vList);
	}

	/**
	 * <p>
	 * 方法名称: addOneTruckDriver
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
	public int addOneTruckDriver(TruckDriverRPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.addOneTruckDriver(sqlHelper, onePojo);
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
	 * 方法名称: deleteOneTruckDriver
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
	public int addOneTruckDriver(SqlExecute sqlHelper, TruckDriverRPojo onePojo)
			throws Exception {
		int count = 0;
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		String userInst[] = onePojo.getUser().getInstId().split(",");
		for (String oneUser : userInst) {
			if (!oneUser.equals("")) {
				onePojo.getUser().setInstId(oneUser);
				count += truckDB.addOneTruckDriver(onePojo);
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteOneTruckDriver
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
	public int deleteOneTruckDriver(String truckId, String instId)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteOneTruckDriver(sqlHelper, truckId, instId);
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
	 * 方法名称: deleteOneTruckDriver
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
	public int deleteOneTruckDriver(SqlExecute sqlHelper, String truckId,
			String instId) throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.deleteOneTruckDriver(truckId, instId);
	}

	/**
	 * <p>
	 * 方法名称: getDriverSchedulingList
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
	public ArrayList<DriverSchedulingPojo> getDriverSchedulingList(
			JSONObject paras) throws Exception {
		ArrayList<DriverSchedulingPojo> list = new ArrayList<DriverSchedulingPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getDriverSchedulingList(sqlHelper, paras);
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
	 * 方法名称: getDriverSchedulingList
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
	public ArrayList<DriverSchedulingPojo> getDriverSchedulingList(
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
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.getDriverSchedulingList(where, vList);
	}

	/**
	 * <p>
	 * 方法名称: getDriverSchedulingRedis
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
	public UserPojo getDriverSchedulingByTruckAndDay(String sDate,
			String truckId) throws Exception {
		UserPojo userPojo = null;
		JSONArray list = this.getDriverSchedulingRedis(sDate.substring(0, 10),
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

	/**
	 * <p>
	 * 方法名称: getTruckDefList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆定义列表。
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
	public ArrayList<TruckVideoPojo> getTruckVideoList(JSONObject paras)
			throws Exception {
		ArrayList<TruckVideoPojo> list = new ArrayList<TruckVideoPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getTruckVideoList(sqlHelper, paras);
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
	 * 方法名称: getTruckVideoList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到车辆定义列表。
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
	public ArrayList<TruckVideoPojo> getTruckVideoList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("truck")) {
					// 关键字
					where += " and t.TRUCK_ID=?";
					vList.add(v);
				}
			}
		}
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.getTruckVideoList(where, vList);
	}

	/**
	 * <p>
	 * 方法名称: getOneTruckVideoById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个车辆定义。
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
	public TruckVideoPojo getOneTruckVideoById(String Id) throws Exception {
		TruckVideoPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneTruckVideoById(sqlHelper, Id);
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
	 * 方法名称: getOneTruckVideoById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个车辆定义。
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
	public TruckVideoPojo getOneTruckVideoById(SqlExecute sqlHelper, String Id)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.getOneTruckVideoById(Id);
	}

	/**
	 * <p>
	 * 方法名称: insertTruckVideo
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int insertTruckVideo(TruckVideoPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertTruckVideo(sqlHelper, onePojo);
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
	 * 方法名称: insertTruckVideo
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int insertTruckVideo(SqlExecute sqlHelper, TruckVideoPojo onePojo)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.insertTruckVideo(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateTruckVideo
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int updateTruckVideo(TruckVideoPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateTruckVideo(sqlHelper, onePojo);
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
	 * 方法名称: updateTruckVideo
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int updateTruckVideo(SqlExecute sqlHelper, TruckVideoPojo onePojo)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.updateTruckVideo(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: deleteTruckVideo
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int deleteTruckVideo(String id) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteTruckVideo(sqlHelper, id);
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
	 * 方法名称: deleteTruckVideo
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int deleteTruckVideo(SqlExecute sqlHelper, String id)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.deleteTruckVideo(id);
	}

	/**
	 * <p>
	 * 方法名称: deleteTruckVideoByTruck
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int deleteTruckVideoByTruck(String id) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteTruckVideoByTruck(sqlHelper, id);
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
	 * 方法名称: deleteTruckVideoByTruck
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int deleteTruckVideoByTruck(SqlExecute sqlHelper, String id)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.deleteTruckVideoByTruck(id);
	}

	/**
	 * <p>
	 * 方法名称: getTruckFixLogsList
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
	public ArrayList<TruckFixLogsPojo> getTruckFixLogsList(JSONObject paras,
			long f, long t) throws Exception {
		ArrayList<TruckFixLogsPojo> list = new ArrayList<TruckFixLogsPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getTruckFixLogsList(sqlHelper, paras, f, t);
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
	 * 方法名称: getTruckFixLogsList
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
	public ArrayList<TruckFixLogsPojo> getTruckFixLogsList(
			SqlExecute sqlHelper, JSONObject paras, long f, long t)
			throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.C_DATE desc";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t.LOG_NAME like ? OR t.LOG_CONTENT like ? OR tu.USER_ID=? OR tu.USER_NAME like ? OR tu.MPHONE like ? OR t1.TRUCK_NAME like ? OR t1.PLATE_NUM like ?)";
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
					vList.add(v);
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("truck")) {
					// 根
					where += " and t.TRUCK_ID=?";
					vList.add(v);
				}
				if (key.equals("type")) {
					// 根
					where += " and t.LOG_TYPE=?";
					vList.add(v);
				}
				if (key.equals("user")) {
					// 关键字
					where += " and t.LOG_USER=?";
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
				if (key.equals("date")) {
					String vs[] = v.split(",");
					if (vs.length > 1 && !vs[0].equals("") && !vs[1].equals("")) {
						where += " and t.C_DATE BETWEEN ? AND ?";
						vList.add(Timestamp.valueOf(vs[0]));
						vList.add(Timestamp.valueOf(vs[1]));
					}
				}
			}
		}
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		paras.put("max", truckDB.getTruckFixLogsCount(where, vList));
		return truckDB.getTruckFixLogsList(where, orderBy, f, t, vList);
	}

	/**
	 * <p>
	 * 方法名称: getOneTruckFixLogsById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个车辆定义。
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
	public TruckFixLogsPojo getOneTruckFixLogsById(String Id) throws Exception {
		TruckFixLogsPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneTruckFixLogsById(sqlHelper, Id);
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
	 * 方法名称: getOneTruckFixLogsById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个车辆定义。
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
	public TruckFixLogsPojo getOneTruckFixLogsById(SqlExecute sqlHelper,
			String Id) throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.getOneTruckFixLogsById(Id);
	}

	/**
	 * <p>
	 * 方法名称: insertTruckFixLogs
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int insertTruckFixLogs(TruckFixLogsPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertTruckFixLogs(sqlHelper, onePojo);
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
	 * 方法名称: insertTruckFixLogs
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
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
	public int insertTruckFixLogs(SqlExecute sqlHelper, TruckFixLogsPojo onePojo)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.insertTruckFixLogs(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateTruckFixLogs
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int updateTruckFixLogs(TruckFixLogsPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateTruckFixLogs(sqlHelper, onePojo);
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
	 * 方法名称: updateTruckFixLogs
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
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
	public int updateTruckFixLogs(SqlExecute sqlHelper, TruckFixLogsPojo onePojo)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.updateTruckFixLogs(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateOneVehicleState
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
	public int updateOneVehicleState(String instId, int state) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateOneVehicleState(sqlHelper, instId, state);
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
	 * 方法名称: updateOneVehicleState
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
	public int updateOneVehicleState(SqlExecute sqlHelper, String instId,
			int state) throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.updateOneVehicleState(instId, state);
	}

	/**
	 * <p>
	 * 方法名称: deleteOneVehicle
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
	public int deleteOneVehicle(String userId) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteOneVehicle(sqlHelper, userId);
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
	 * 方法名称: deleteOneVehicle
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
	public int deleteOneVehicle(SqlExecute sqlHelper, String instId)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.deleteOneVehicle(instId);
	}

	/**
	 * <p>
	 * 方法名称: deleteOneVehicle
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
	public int updateAllVehicle(int state) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateAllVehicle(sqlHelper, state);
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
	 * 方法名称: updateAllVehicle
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
	public int updateAllVehicle(SqlExecute sqlHelper, int state)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.updateAllVehicle(state);
	}

	/**
	 * <p>
	 * 方法名称: getOneTruckById
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
	public JSONObject getVehicleNewestByInst(String instId) throws Exception {
		JSONObject onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getVehicleNewestByInst(sqlHelper, instId);
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
	 * 方法名称: getOneTruckById
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
	public JSONObject getVehicleNewestByInst(SqlExecute sqlHelper, String instId)
			throws Exception {
		TruckDBMang truckDB = new TruckDBMang(sqlHelper, m_bs);
		return truckDB.getVehicleNewestByInst(instId);
	}

	// 根据公式计算邮箱体积返回ml
	public static double getOilVolume(JSONObject inParas) {
		double value = 0;
		double X = inParas.getDouble("X");
		//
		if (inParas.containsKey("L") && inParas.containsKey("W")
				&& inParas.containsKey("H") && inParas.containsKey("R")) {
			double L = inParas.getDouble("L");
			double W = inParas.getDouble("W");
			double H = inParas.getDouble("H");
			double R = inParas.getDouble("R");
			double N = 0;
			if (X <= 0) {
				value = 0;
			} else if (X > 0 && X <= R) {
				N = Math.acos((R - X) / R);
				value = (((N - 0.5 * Math.sin(2 * N)) * L * Math.pow(R, 2) + (W - 2 * R)
						* L * X)) / 1000;
			} else if (X <= (H - R) && X > R) {
				value = (0.5 * Math.PI * Math.pow(R, 2) * L + (W - 2 * R) * L
						* R + (X - R) * L * W) / 1000;

			} else if (X > (H - R) && X < H) {
				N = Math.acos((X - (H - R)) / R);
				value = ((H - 2 * R) * L * W + (W - 2 * R) * L * R + Math.PI
						* Math.pow(R, 2) * L - (N - 0.5 * Math.sin(2 * N)) * L
						* Math.pow(R, 2) + (W - 2 * R) * (X - (H - R)) * L) / 1000;
			} else if (X >= H) {
				value = ((H - 2 * R) * L * W + (W - 2 * R) * L * R + Math.PI
						* Math.pow(R, 2) * L + (W - 2 * R) * L * R) / 1000;
			}
		}
		return value;
	}
}
