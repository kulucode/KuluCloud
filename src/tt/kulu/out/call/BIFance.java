package tt.kulu.out.call;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.fance.dbclass.FanceDBMang;
import tt.kulu.bi.fance.pojo.FancePojo;
import tt.kulu.bi.fault.pojo.FaultReportPojo;
import tt.kulu.bi.storage.dbclass.EquipmentDBMang;
import tt.kulu.bi.storage.pojo.EquipmentGeometryPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstWorkLogPojo;
import tt.kulu.bi.truck.pojo.PacketLocationReport;
import tt.kulu.bi.truck.pojo.TruckPojo;
import tt.kulu.bi.truck.pojo.TruckWorkDayLogsPojo;
import tt.kulu.bi.truck.pojo.TruckWorkStatsPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.bi.user.pojo.UserWorkDayLogsPojo;

import com.tt4j2ee.BSDateEx;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BIFance
 * </p>
 * <p>
 * 功能描述: 围栏业务接口类
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
public class BIFance extends BSDBBase {

	public BIFance(BSObject m_bs) throws Exception {
		super(null, m_bs);
	}

	public BIFance(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getFanceList
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
	public ArrayList<FancePojo> getFanceList(JSONObject paras) throws Exception {
		ArrayList<FancePojo> list = new ArrayList<FancePojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getFanceList(sqlHelper, paras);
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
	 * 方法名称: getFanceList
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
	public ArrayList<FancePojo> getFanceList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.F_NAME";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t.F_NAME like ?)";
					vList.add("%" + v + "%");
				}
				if (key.equals("user")) {
					// 关键字
					where += " and t.F_ID in (select v.F_ID from T_FANCE_USER_R v where v.USER_INSTID=?)";
					vList.add(v);
				}
				if (key.equals("truck")) {
					// 关键字
					where += " and t.F_ID in (select v.F_ID from T_FANCE_TRUCK_R v where v.TRUCK_ID=?)";
					vList.add(v);
				}
				if (key.equals("type")) {
					// 根
					where += " and t.F_TYPE in (";
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
		FanceDBMang fanceDB = new FanceDBMang(sqlHelper, m_bs);
		return fanceDB.getFanceList(where, orderBy, vList);
	}

	/**
	 * <p>
	 * 方法名称: getOneFanceById
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
	public FancePojo getOneFanceById(String equipmentId) throws Exception {
		FancePojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneFanceById(sqlHelper, equipmentId);
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
	 * 方法名称: getOneFanceById
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
	public FancePojo getOneFanceById(SqlExecute sqlHelper, String equipmentId)
			throws Exception {
		FanceDBMang fanceDB = new FanceDBMang(sqlHelper, m_bs);
		return fanceDB.getOneFanceById(equipmentId);
	}

	/**
	 * <p>
	 * 方法名称: insertFance
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
	public int insertFance(FancePojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertFance(sqlHelper, onePojo);
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
	 * 方法名称: insertFance
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
	public int insertFance(SqlExecute sqlHelper, FancePojo onePojo)
			throws Exception {
		FanceDBMang fanceDB = new FanceDBMang(sqlHelper, m_bs);
		return fanceDB.insertFance(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateFance
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
	public int updateFance(FancePojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateFance(sqlHelper, onePojo);
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
	 * 方法名称: updateFance
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
	public int updateFance(SqlExecute sqlHelper, FancePojo onePojo)
			throws Exception {
		FanceDBMang fanceDB = new FanceDBMang(sqlHelper, m_bs);
		return fanceDB.updateFance(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: deleteFanceById
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
	public int deleteFanceById(String id) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteFanceById(sqlHelper, id);
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
	 * 方法名称: deleteFanceById
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
	public int deleteFanceById(SqlExecute sqlHelper, String id)
			throws Exception {
		FanceDBMang fanceDB = new FanceDBMang(sqlHelper, m_bs);
		return fanceDB.deleteFanceById(id);
	}

	/**
	 * <p>
	 * 方法名称: getFanceUserRList
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
	public ArrayList<UserPojo> getFanceUserRList(JSONObject paras)
			throws Exception {
		ArrayList<UserPojo> list = new ArrayList<UserPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getFanceUserRList(sqlHelper, paras);
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
	 * 方法名称: getFanceUserRList
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
	public ArrayList<UserPojo> getFanceUserRList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("fanceid")) {
					// 关键字
					where += " and t.F_ID=?";
					vList.add(v);
				}
			}
		}
		FanceDBMang fanceDB = new FanceDBMang(sqlHelper, m_bs);
		return fanceDB.getFanceUserRList(where, vList);
	}

	/**
	 * <p>
	 * 方法名称: getFanceTruckRList
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
	public ArrayList<TruckPojo> getFanceTruckRList(JSONObject paras)
			throws Exception {
		ArrayList<TruckPojo> list = new ArrayList<TruckPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getFanceTruckRList(sqlHelper, paras);
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
	 * 方法名称: getFanceTruckRList
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
	public ArrayList<TruckPojo> getFanceTruckRList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("fanceid")) {
					// 关键字
					where += " and t.F_ID=?";
					vList.add(v);
				}
			}
		}
		FanceDBMang fanceDB = new FanceDBMang(sqlHelper, m_bs);
		return fanceDB.getFanceTruckRList(where, vList);
	}

	/**
	 * <p>
	 * 方法名称: doCheckPoineInFanceByUser
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
	public int doCheckPoineInFanceByUser(EquipmentGeometryPojo oneEqpGeo)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.doCheckPoineInFanceByUser(sqlHelper, oneEqpGeo);
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
	 * 方法名称: deleteFanceById
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
	public int doCheckPoineInFanceByUser(SqlExecute sqlHelper,
			EquipmentGeometryPojo oneEqpGeo) throws Exception {
		int count = 0;
		EquipmentDBMang eqpDB = new EquipmentDBMang(sqlHelper, m_bs);
		FanceDBMang fanceDB = new FanceDBMang(sqlHelper, m_bs);
		BIUser userBI = new BIUser(m_bs);
		BIFault faultBI = new BIFault(m_bs);
		JSONObject point = new JSONObject();
		List<Object> vList = new ArrayList<Object>();
		BSDateEx bsDate = new BSDateEx();
		if (oneEqpGeo.getEqpInst().getEqpDef().getEqpType().getId()
				.equals("EQUIPMENT_DEFTYPE_1")) {
			// 手表，人用
			UserWorkDayLogsPojo oneUWDL = new UserWorkDayLogsPojo();
			oneUWDL.setOpType(0);
			oneUWDL.setUser(oneEqpGeo.getEqpInst().getMangUser());
			oneUWDL.setDate(oneEqpGeo.getCreateDate());
			oneUWDL.setId(oneEqpGeo.getEqpInst().getMangUser().getInstId()
					+ "_" + oneEqpGeo.getCreateDate().substring(0, 10));
			oneEqpGeo.setFanceFlg(0);
			if (!oneEqpGeo.getEqpInst().getMangUser().getInstId().equals("")) {
				// 判断是否在围栏内
				vList.clear();
				vList.add(oneEqpGeo.getEqpInst().getMangUser().getInstId());
				point.put("lon", oneEqpGeo.getLongitude());
				point.put("lat", oneEqpGeo.getLatitude());
				if (fanceDB.getFanceUserCount(" and t.USER_INSTID=?", vList) > 0
						&& !fanceDB
								.checkPoineInFance(
										" and t.F_ID in (select v.F_ID from T_FANCE_USER_R v where v.USER_INSTID=?)",
										point, vList)) {
					// 超出围栏
					oneEqpGeo.setFanceFlg(0);
					oneUWDL.setType(0);
					UserWorkDayLogsPojo oneThisUWDL = userBI
							.getOneUserWorkDayLogsId(sqlHelper, oneUWDL.getId());
					if (oneThisUWDL != null) {
						oneUWDL.setBjDate(oneThisUWDL.getBjDate());
					}
					if (oneThisUWDL == null
							|| oneThisUWDL.getType() == 1
							|| (!oneUWDL.getBjDate().equals("") && bsDate
									.getDateMillCount(oneUWDL.getBjDate(),
											bsDate.getThisDate(0, 0)) > 900000)) {
						// 根据当天的最新在岗状态，如果没有记录或者在岗或者时间超过15分钟，则报警
						oneUWDL.setOutDate(oneUWDL.getDate());
						// 得到下班时间
						DicItemPojo outTime = (new BIDic(null))
								.getDicItemByRedis("WATCH_PARAS_10");
						if (outTime != null
								&& !outTime.getValue2().equals("")
								&& (URLlImplBase.getTimeNumberByTime(outTime
										.getValue2()) >= URLlImplBase
										.getTimeNumberByDate(oneUWDL.getDate()))) {
							// 工作时间内
							oneUWDL.setBjDate(oneUWDL.getDate());
							// 报警故障
							FaultReportPojo frPojo = new FaultReportPojo();
							frPojo.setEqpInst(oneEqpGeo.getEqpInst());
							frPojo.setName("人员超出围栏报警");
							frPojo.getFaultFrom().setId(
									"8E0642C7E4C9C13FE6F5C55EC1555E7E");
							frPojo.getFaultType().setId(
									"2B27A3287D25F3EA18C8FDAFA1EEB25F");
							frPojo.getFaultCode().setId(
									"E0671FCA6A1D9312393863370D03982C");
							frPojo.setContent("在工作围栏之外：经纬度["
									+ oneEqpGeo.getLongitude() + ","
									+ oneEqpGeo.getLatitude() + "]");
							frPojo.setFrUser(oneUWDL.getUser());
							frPojo.setHappenDate(oneUWDL.getDate());
							if (oneEqpGeo.getEqpInst().getMangUser() == null
									|| oneEqpGeo.getEqpInst().getMangUser()
											.getInstId().equals("")) {
								frPojo.getCreateUser().setInstId("SUPER_ADMIN");
							} else {
								frPojo.setCreateUser(oneEqpGeo.getEqpInst()
										.getMangUser());
							}
							faultBI.insertFaultReport(sqlHelper, frPojo);
						} else {
							oneUWDL.setBjDate("");
						}
					} else {
						oneUWDL.setBjDate("");
					}
				} else {
					oneEqpGeo.setFanceFlg(1);
					oneUWDL.setType(1);
				}
			}
			// 修改当前定位消息的围栏标志
			eqpDB.updateEquipmentGeometryFance(oneEqpGeo);
			// 写入日工作日志
			userBI.updateUserWorkDayLogs(sqlHelper, oneUWDL);

		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteFanceById
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
	public int doCheckPoineInFanceByTruck(PacketLocationReport onePojo,
			EquipmentGeometryPojo oneEqpGeo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.doCheckPoineInFanceByTruck(sqlHelper, onePojo,
					oneEqpGeo);
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
	 * 方法名称: doCheckPoineInFanceByTruck
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
	public int doCheckPoineInFanceByTruck(SqlExecute sqlHelper,
			PacketLocationReport onePojo, EquipmentGeometryPojo oneEqpGeo)
			throws Exception {
		int count = 0;
		EquipmentDBMang eqpDB = new EquipmentDBMang(sqlHelper, m_bs);
		FanceDBMang fanceDB = new FanceDBMang(sqlHelper, m_bs);
		BITruck truckBI = new BITruck(m_bs);
		BIFault faultBI = new BIFault(m_bs);
		JSONObject point = new JSONObject();
		List<Object> vList = new ArrayList<Object>();
		BSDateEx bsDate = new BSDateEx();
		if (oneEqpGeo.getEqpInst().getEqpDef().getEqpType().getId()
				.equals("EQUIPMENT_DEFTYPE_0")) {
			// 车载，人用
			TruckWorkDayLogsPojo oneUWDL = new TruckWorkDayLogsPojo();
			oneUWDL.setTruck(oneEqpGeo.getEqpInst().getTruck());
			oneUWDL.setDate(oneEqpGeo.getSysDate());
			oneUWDL.setId(oneEqpGeo.getEqpInst().getTruck().getId() + "_"
					+ oneEqpGeo.getSysDate().substring(0, 10));
			oneUWDL.setOpType(0);
			oneUWDL.setOil(String.valueOf(onePojo.getOilDeff()));
			oneUWDL.setDistance(eqpDB.getGeometryLastDisdance(oneEqpGeo, 1));
			if (!oneEqpGeo.getEqpInst().getTruck().getId().equals("")) {
				// 判断是否在围栏内
				vList.clear();
				vList.add(oneEqpGeo.getEqpInst().getTruck().getId());
				point.put("lon", oneEqpGeo.getLongitude());
				point.put("lat", oneEqpGeo.getLatitude());
				if (fanceDB.getFanceTruckCount(" and t.TRUCK_ID=?", vList) > 0
						&& !fanceDB
								.checkPoineInFance(
										" and t.F_ID in (select v.F_ID from T_FANCE_TRUCK_R v where v.TRUCK_ID=?)",
										point, vList)) {
					oneEqpGeo.setFanceFlg(0);
					oneUWDL.setType(0);
					// 超出围栏
					TruckWorkDayLogsPojo oneThisUWDL = truckBI
							.getOneTruckWorkDayLogsId(sqlHelper,
									oneUWDL.getId());
					if (oneThisUWDL != null) {
						oneUWDL.setBjDate(oneThisUWDL.getBjDate());
					}
					if (oneThisUWDL == null
							|| oneThisUWDL.getType() == 1
							|| (!oneUWDL.getBjDate().equals("") && bsDate
									.getDateMillCount(oneUWDL.getBjDate(),
											bsDate.getThisDate(0, 0)) > 1800000)) {
						// 根据当天的最新在岗状态，如果没有记录或者在岗或者时间超过半小时，则报警
						// 报警故障
						FaultReportPojo frPojo = new FaultReportPojo();
						frPojo.setEqpInst(oneEqpGeo.getEqpInst());
						frPojo.setName("车辆超出围栏报警");
						frPojo.getFaultFrom().setId(
								"CD485479DD0310687865A24B6CB1FBB6");
						frPojo.getFaultType().setId(
								"1AA40EFE45EE2BA75E1C4ADE20F796C2");
						frPojo.getFaultCode().setId(
								"E848FC7E9913F0A29203EA533ADC240C");
						frPojo.setContent("在工作围栏之外：经纬度["
								+ oneEqpGeo.getLongitude() + ","
								+ oneEqpGeo.getLatitude() + "]");
						frPojo.setFrUser(oneEqpGeo.getEqpInst().getMangUser());
						frPojo.getEqpInst().setTruck(oneUWDL.getTruck());
						frPojo.setHappenDate(oneUWDL.getDate());
						if (oneEqpGeo.getEqpInst().getMangUser() == null
								|| oneEqpGeo.getEqpInst().getMangUser()
										.getInstId().equals("")) {
							frPojo.getCreateUser().setInstId("SUPER_ADMIN");
						} else {
							frPojo.setCreateUser(oneEqpGeo.getEqpInst()
									.getMangUser());
						}
						oneUWDL.setOutDate(oneUWDL.getDate());
						oneUWDL.setBjDate(oneUWDL.getDate());
						faultBI.insertFaultReport(sqlHelper, frPojo);
					} else {
						oneUWDL.setBjDate("");
					}
				} else {
					oneEqpGeo.setFanceFlg(1);
					oneUWDL.setType(1);
				}
			}
			// 修改当前定位消息的围栏标志
			eqpDB.updateEquipmentGeometryFance(oneEqpGeo);

			TruckWorkStatsPojo oneStats = new TruckWorkStatsPojo();
			oneStats.getTruck()
					.setId(oneEqpGeo.getEqpInst().getTruck().getId());
			oneStats.setEndDate(oneEqpGeo.getCreateDate());
			oneStats.setOil(oneUWDL.getOil());
			oneStats.setOilDiff(String.valueOf(onePojo.getOilDeff()));
			oneStats.setDistance(oneUWDL.getDistance());
			count += truckBI.updateTruckWorkStatsData(sqlHelper, oneStats);
			// 写入日工作日志
			count += truckBI.updateTruckWorkDayLogs(sqlHelper, oneUWDL);

		}
		return count;
	}
}
