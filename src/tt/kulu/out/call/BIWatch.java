package tt.kulu.out.call;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.storage.dbclass.EquipmentDBMang;
import tt.kulu.bi.user.pojo.UserWorkDayLogsPojo;
import tt.kulu.bi.user.pojo.UserWorkParasMinPojo;
import tt.kulu.bi.user.pojo.UserWorkParasPojo;
import tt.kulu.bi.watch.dbclass.WatchDBMang;
import tt.kulu.bi.watch.pojo.InBloodPressureOkCmd;
import tt.kulu.bi.watch.pojo.InHeartRateCmd;
import tt.kulu.bi.watch.pojo.InStepCmd;

/**
 * <p>
 * 标题: BIWatch
 * </p>
 * <p>
 * 功能描述: 手表业务接口类
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
public class BIWatch extends BSDBBase {
	public BIWatch(BSObject m_bs) throws Exception {
		super(null, m_bs);
	}

	public BIWatch(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
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
	public UserWorkParasMinPojo getWatchLastDataByRedis(String instid)
			throws Exception {
		UserWorkParasMinPojo onePojo = new UserWorkParasMinPojo();
		if (!instid.equals("")) {
			BIRedis redisBI = new BIRedis();
			String redisS = redisBI.getMapData("WATCHLASTDATA_MAP", instid,
					URLlImplBase.REDIS_KULUDATA);
			if (redisS == null || redisS.trim().equals("")) {
				// 从数据库的到
				onePojo = this.getOneWatchWordParasMinByInstId(instid);
				if (onePojo != null) {
					redisBI.setMapData("WATCHLASTDATA_MAP", instid,
							JSON.toJSONString(onePojo),
							URLlImplBase.REDIS_KULUDATA);
				}
			} else {
				onePojo = JSON.parseObject(redisS, UserWorkParasMinPojo.class);
			}
		}
		return onePojo;
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
	public void setWatchLastDataToRedis(UserWorkParasMinPojo onePojo)
			throws Exception {
		BIRedis redisBI = new BIRedis();
		redisBI.setMapData("WATCHLASTDATA_MAP", onePojo.getEqpInst(),
				JSON.toJSONString(onePojo), URLlImplBase.REDIS_KULUDATA);
		return;
	}

	/**
	 * <p>
	 * 方法名称: insertWatchStep
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
	public int insertWatchStep(InStepCmd onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertWatchStep(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		if (count > 0) {
			// 写入员工工作日志
			if (onePojo.getEqpInst().getMangUser() != null
					&& !onePojo.getEqpInst().getMangUser().equals("")) {
				BIUser userBI = new BIUser(m_bs);
				UserWorkDayLogsPojo oneWork = new UserWorkDayLogsPojo();
				oneWork.setId(onePojo.getEqpInst().getMangUser().getInstId()
						+ "_" + onePojo.getCreateDate().substring(0, 10));
				oneWork.setOpType(1);
				oneWork.setDate(onePojo.getCreateDate());
				oneWork.setUser(onePojo.getEqpInst().getMangUser());
				oneWork.setStep(Integer.parseInt(onePojo.getStep()));
				userBI.updateUserWorkDayLogs(oneWork);
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertWatchStep
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
	public int insertWatchStep(SqlExecute sqlHelper, InStepCmd onePojo)
			throws Exception {
		int count = 0;
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		onePojo.setEqpInst(quipmentDB.getOneEquipmentInstByWyCode(onePojo
				.getEqpInst().getWyCode()));
		if (onePojo.getEqpInst() != null) {
			WatchDBMang watchDB = new WatchDBMang(sqlHelper, m_bs);
			count = watchDB.insertWatchStep(onePojo);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertWatchBloodPressureOK
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
	public int insertWatchBloodPressureOK(InBloodPressureOkCmd onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertWatchBloodPressureOK(sqlHelper, onePojo);
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
	 * 方法名称: insertWatchBloodPressureOK
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
	public int insertWatchBloodPressureOK(SqlExecute sqlHelper,
			InBloodPressureOkCmd onePojo) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		onePojo.setEqpInst(quipmentDB.getOneEquipmentInstByWyCode(onePojo
				.getEqpInst().getWyCode()));
		WatchDBMang watchDB = new WatchDBMang(sqlHelper, m_bs);
		return watchDB.insertWatchBloodPressureOK(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: insertWatchBloodPressureOK
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
	public int insertWatchBloodPressureError(InBloodPressureOkCmd onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertWatchBloodPressureError(sqlHelper, onePojo);
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
	 * 方法名称: insertWatchBloodPressureOK
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
	public int insertWatchBloodPressureError(SqlExecute sqlHelper,
			InBloodPressureOkCmd onePojo) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		onePojo.setEqpInst(quipmentDB.getOneEquipmentInstByWyCode(onePojo
				.getEqpInst().getWyCode()));
		WatchDBMang watchDB = new WatchDBMang(sqlHelper, m_bs);
		return watchDB.insertWatchBloodPressureError(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: insertWatchInHeartRate
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
	public int insertWatchInHeartRate(InHeartRateCmd onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertWatchInHeartRate(sqlHelper, onePojo);
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
	 * 方法名称: insertWatchInHeartRate
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
	public int insertWatchInHeartRate(SqlExecute sqlHelper,
			InHeartRateCmd onePojo) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		onePojo.setEqpInst(quipmentDB.getOneEquipmentInstByWyCode(onePojo
				.getEqpInst().getWyCode()));
		int count = 0;
		if (onePojo.getEqpInst() != null) {
			WatchDBMang watchDB = new WatchDBMang(sqlHelper, m_bs);
			count = watchDB.insertWatchInHeartRate(onePojo);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertWatchInHeartRateError
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
	public int insertWatchInHeartRateError(InHeartRateCmd onePojo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertWatchInHeartRateError(sqlHelper, onePojo);
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
	 * 方法名称: insertWatchInHeartRateError
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
	public int insertWatchInHeartRateError(SqlExecute sqlHelper,
			InHeartRateCmd onePojo) throws Exception {
		EquipmentDBMang quipmentDB = new EquipmentDBMang(sqlHelper, m_bs);
		onePojo.setEqpInst(quipmentDB.getOneEquipmentInstByWyCode(onePojo
				.getEqpInst().getWyCode()));
		int count = 0;
		if (onePojo.getEqpInst() != null) {
			WatchDBMang watchDB = new WatchDBMang(sqlHelper, m_bs);
			count = watchDB.insertWatchInHeartRateError(onePojo);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getWatchWordParasList
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
	public ArrayList<UserWorkParasPojo> getWatchWordParasList(JSONObject paras,
			long f, long t) throws Exception {
		ArrayList<UserWorkParasPojo> list = new ArrayList<UserWorkParasPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getWatchWordParasList(sqlHelper, paras, f, t);
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
	 * 方法名称: getWatchWordParasList
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
	public ArrayList<UserWorkParasPojo> getWatchWordParasList(
			SqlExecute sqlHelper, JSONObject paras, long f, long t)
			throws Exception {
		// 参数设定
		String where = "";
		String key = "";
		String orderBy = " t1.EQP_WYCODE";
		List<Object> vList = new ArrayList<Object>();
		if (paras != null) {
			Iterator<String> keys = paras.keys();
			while (keys.hasNext()) {
				key = keys.next();
				String v = paras.getString(key);
				if (!v.equals("")) {
					if (key.equals("key")) {
						// 关键字
						where += " and (tu.user_id=? OR t1.EQP_PHONE=? OR t1.EQP_WYCODE like ? or tu.USER_NAME like ? or tu.MPHONE like ? OR t1.EQP_NAME like ? OR t1.eqp_qr like ?)";
						vList.add(v);
						vList.add(v);
						vList.add("%" + v + "%");
						vList.add("%" + v + "%");
						vList.add("%" + v + "%");
						vList.add("%" + v + "%");
						vList.add("%" + v + "%");
					}
					if (key.equals("ele")) {
						String[] vs = v.split(",");
						if (!(Integer.parseInt(vs[0]) == 0 && Integer
								.parseInt(vs[1]) == 100)) {
							where += " and t.eqp_inst in (select v.EQP_INST from (select twh.eqp_inst ,to_number(twh.bbe_ele, '999') as bbe_ele from t_watch_hr twh where twh.eqp_inst=t.eqp_inst order by twh.s_cdate desc limit 1) v where v.bbe_ele<=? and v.bbe_ele>=?)";
							vList.add(Integer.parseInt(vs[1]));
							vList.add(Integer.parseInt(vs[0]));
						}
					}
					if (key.equals("group")) {
						where += " and tu.ORG_ID in "
								+ URLlImplBase.LOGIN_GROUP_WHERE;
						vList.add("%," + v + "%");
					}
					if (key.equals("user")) {
						// 关键字
						where += " and t1.eqp_muser=?";
						vList.add(v);
					}
					if (key.equals("eqpstate")) {
						// 关键字
						where += " and (t.EQP_STATE=? OR t1.EQP_STATE=?)";
						vList.add(Integer.parseInt(v));
						vList.add(Integer.parseInt(v));
					}
					if (key.equals("userstate")) {
						// 关键字
						where += " and t1.USER_STATE=?";
						vList.add(Integer.parseInt(v));
					}
					if (key.equals("login")) {
						String[] vs = v.split(",");
						String whereEx = "";
						for (String oneV : vs) {
							if (!oneV.equals("")) {
								whereEx += (whereEx.equals("") ? "" : " or ")
										+ "tu.ORG_ID in "
										+ URLlImplBase.LOGIN_GROUP_WHERE;
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
		WatchDBMang watchDB = new WatchDBMang(sqlHelper, m_bs);
		paras.put("max", watchDB.getWatchWordParasCount(where, vList));
		return watchDB.getWatchWordParasList(where, orderBy, vList, f, t);
	}

	/**
	 * <p>
	 * 方法名称: getOneWatchWordParasMinByInstId
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个云环最新数据。
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
	public UserWorkParasMinPojo getOneWatchWordParasMinByInstId(
			String equipmentId) throws Exception {
		UserWorkParasMinPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneWatchWordParasMinByInstId(sqlHelper,
					equipmentId);
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
	 * 方法名称: getOneWatchWordParasMinByInstId
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个云环最新数据。
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
	public UserWorkParasMinPojo getOneWatchWordParasMinByInstId(
			SqlExecute sqlHelper, String equipmentId) throws Exception {
		WatchDBMang watchDB = new WatchDBMang(sqlHelper, m_bs);
		return watchDB.getOneWatchWordParasMinByInstId(equipmentId);
	}

	/**
	 * <p>
	 * 方法名称: updateOneWatchState
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
	public int updateOneWatchState(String userId, int state) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateOneWatchState(sqlHelper, userId, state);
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
	 * 方法名称: updateOneWatchState
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
	public int updateOneWatchState(SqlExecute sqlHelper, String userId,
			int state) throws Exception {
		WatchDBMang watchDB = new WatchDBMang(sqlHelper, m_bs);
		return watchDB.updateOneWatchState(userId, state);
	}

	/**
	 * <p>
	 * 方法名称: deleteOneWatch
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
	public int deleteOneWatch(String userId) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteOneWatch(sqlHelper, userId);
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
	 * 方法名称: deleteOneWatch
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
	public int deleteOneWatch(SqlExecute sqlHelper, String instId)
			throws Exception {
		WatchDBMang watchDB = new WatchDBMang(sqlHelper, m_bs);
		return watchDB.deleteOneWatch(instId);
	}
}
