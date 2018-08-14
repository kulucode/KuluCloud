package tt.kulu.out.biss;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.logs.biclass.SysLogsBIMang;
import tt.kulu.bi.logs.pojo.SysLogsPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.bi.truck.pojo.TruckWorkParasPojo;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BIEquipment;
import tt.kulu.out.call.BILogin;
import tt.kulu.out.call.BITruck;
import tt.kulu.out.call.BIWatch;

import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSVehicle
 * </p>
 * <p>
 * 功能描述: 车载Web接口类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2014-10-29
 * </p>
 */
public class BSVehicle {
	/**
	 * <p>
	 * 方法名：do_searchVehicleWordParasList
	 * </p>
	 * <p>
	 * 方法描述：搜索用户工作情况
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchVehicleWordParasList(BSObject m_bs)
			throws Exception {
		long startNum = 0;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String geoArea = m_bs.getPrivateMap().get("pg_geoarea");
		JSONObject paras = new JSONObject();
		if (sText != null) {
			paras.put("key", sText);
		}
		if (geoArea != null) {
			paras.put("geoarea", geoArea);
		}
		if (pageNumStr != null) {
			pageNum = Integer.parseInt(pageNumStr);
		} else {
			pageNum = 0;
		}
		if (pageNum < 0) {
			pageNum = 0;
		}
		if (pageSizeStr != null) {
			pageSize = Integer.parseInt(pageSizeStr);
		} else {
			pageSize = 20;
		}
		startNum = pageSize * pageNum;
		// 调用BI
		BITruck truckBI = new BITruck(null, m_bs);
		// 返回数据
		JSONArray trucklist = new JSONArray();
		ArrayList<TruckWorkParasPojo> list = truckBI.getVehicleWordParasList(
				paras, startNum, startNum + pageSize - 1);
		for (TruckWorkParasPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("eqpid", onePojo.getEqpInst().getInstId());
			oneObj.put("eqpname", onePojo.getEqpInst().getName());
			oneObj.put("eqpqr", onePojo.getEqpInst().getQrCode());
			oneObj.put("onlinev", onePojo.getEqpInst().getOnlineState());
			oneObj.put("online", EquipmentInstPojo.ONLINE_STATE_NAME[onePojo
					.getEqpInst().getOnlineState()]);
			oneObj.put("truckid", onePojo.getEqpInst().getTruck().getId());
			oneObj.put("trucknno", onePojo.getEqpInst().getTruck().getNo());
			oneObj.put("truckname", onePojo.getEqpInst().getTruck().getName());
			oneObj.put("trucktype", onePojo.getEqpInst().getTruck().getDefine()
					.getName());
			oneObj.put("truckorg", onePojo.getEqpInst().getTruck().getOrg()
					.getAllName().replaceAll(",", "-"));
			oneObj.put("paltenum", onePojo.getEqpInst().getTruck()
					.getPlateNum());
			// 司机
			if (onePojo.getWorkUser() != null) {
				oneObj.put("userinst", onePojo.getWorkUser().getInstId());
				oneObj.put("userid", onePojo.getWorkUser().getId());
				oneObj.put("username", onePojo.getWorkUser().getName());
				oneObj.put("usermphone", onePojo.getWorkUser().getmPhone());
			}

			if (onePojo.getThisOilV().equals("")) {
				oneObj.put(
						"oil",
						(!onePojo.getOil().equals("") ? URLlImplBase
								.AllPrinceDiv(onePojo.getOil(), 100) : "0")
								+ "毫米");
			} else {
				oneObj.put(
						"oil",
						(URLlImplBase.AllPrinceDiv(onePojo.getThisOilV(), 1000))
								+ "升");
			}
			oneObj.put("oildiff", onePojo.getOilDiff());
			oneObj.put(
					"speed",
					!onePojo.getSpeed().equals("") ? URLlImplBase.AllPrinceDiv(
							onePojo.getSpeed(), 10) : "0");
			oneObj.put("oildate", onePojo.getOilDate());
			oneObj.put("weight", onePojo.getWeight());
			oneObj.put("lat", onePojo.getLatitude());
			oneObj.put("lon", onePojo.getLongitude());
			oneObj.put("geodate", onePojo.getGeoDate());
			oneObj.put("date", m_bs.getDateEx().getThisDate(0, 0));
			oneObj.put("fanceflg", onePojo.getFanceFlg());
			trucklist.add(oneObj);
		}
		retJSON.put("list", trucklist);
		retJSON.put("max", paras.getLong("max"));
		retJSON.put("r", 0);
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getVehicleParas
	 * </p>
	 * <p>
	 * 方法描述：搜索车载参数
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getVehicleParas(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		String dicid = m_bs.getPrivateMap().get("vehickeid");
		// 调用BI
		BIDic dicBI = new BIDic(null, m_bs);
		// 返回数据
		JSONObject _paras = new JSONObject();
		_paras.put("dic", dicid);
		ArrayList<DicItemPojo> items = dicBI.getDicItemList(_paras);
		for (DicItemPojo onePojo : items) {
			retJSON.put(onePojo.getValue(), onePojo.getValue2());
		}
		retJSON.put("r", 0);
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateVehicleParas
	 * </p>
	 * <p>
	 * 方法描述：搜索车载参数
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateVehicleParas(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		String dicid = m_bs.getPrivateMap().get("t_dicid");
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
		oneLogs.setName("编辑云盒配置参数");
		// 调用BI
		BIDic dicBI = new BIDic(null, m_bs);
		// 返回数据
		DicItemPojo oneItem = new DicItemPojo();
		// 位置上传间隔
		// oneItem = dicBI.getDicItemByRedis("VEHICLE_PARAS_0");
		// if (oneItem == null) {
		// oneItem = new DicItemPojo();
		// oneItem.getDic().setId(dicid);
		// oneItem.setName("位置上传间隔");
		// oneItem.setIndex(0);
		// oneItem.setValue("gps_date");
		// oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_0"));
		// dicBI.insertDicItem(oneItem);
		// } else {
		// oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_0"));
		// dicBI.updateDicItem(oneItem);
		// }
		// // 油量时间间隔
		// oneItem = dicBI.getDicItemByRedis("VEHICLE_PARAS_1");
		// if (oneItem == null) {
		// oneItem = new DicItemPojo();
		// oneItem.getDic().setId(dicid);
		// oneItem.setName("油量时间间隔");
		// oneItem.setIndex(0);
		// oneItem.setValue("oil_date");
		// oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_1"));
		// dicBI.insertDicItem(oneItem);
		// } else {
		// oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_1"));
		// dicBI.updateDicItem(oneItem);
		// }
		// 停车时间阈值
		oneItem = dicBI.getDicItemByRedis("VEHICLE_PARAS_2");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.setId("VEHICLE_PARAS_2");
			oneItem.getDic().setId(dicid);
			oneItem.setName("停车时间阈值");
			oneItem.setIndex(0);
			oneItem.setValue("stop_date");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_2"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_2"));
			dicBI.updateDicItem(oneItem);
		}
		// 油量差额阈值
		oneItem = dicBI.getDicItemByRedis("VEHICLE_PARAS_3");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.setId("VEHICLE_PARAS_3");
			oneItem.getDic().setId(dicid);
			oneItem.setName("油量差额阈值");
			oneItem.setIndex(0);
			oneItem.setValue("oil_diff");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_3"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.getDic().setId(dicid);
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_3"));
			dicBI.updateDicItem(oneItem);
		}
		retJSON.put("r", 0);
		// 写日志
		oneLogs.setType(1);
		oneLogs.setContent("操作:" + oneLogs.getName());
		SysLogsBIMang slbi = new SysLogsBIMang(oneLogs, m_bs);
		slbi.start();
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_deleteOneVehicle
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户初始化信息
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
	public BSObject do_deleteOneVehicle(BSObject m_bs) throws Exception {
		JSONObject retObj = new JSONObject();
		retObj.put("r", 0);
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
		String instid = m_bs.getPrivateMap().get("pg_inst");
		String type = m_bs.getPrivateMap().get("pg_type");
		int count = 0;
		BITruck truckBI = new BITruck(null, m_bs);
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		EquipmentInstPojo oneEqp = eqpBI.getOneEquipmentInstById(instid);
		if (oneEqp != null) {
			if (type.equals("delete")) {
				// 物理删除
				count = truckBI.deleteOneVehicle(instid);
				oneLogs.setName("删除云盒");
			} else if (type.equals("state")) {
				// 逻辑删除
				count = truckBI.updateOneVehicleState(instid, 4);
				oneLogs.setName("设置云盒无效");
			} else if (type.equals("reset")) {
				// 还原
				count = truckBI.updateOneVehicleState(instid, 0);
				oneLogs.setName("还原无效云盒");
			}
			if (count > 0) {
				retObj.put("r", 0);
				// 写日志
				oneLogs.setType(1);
				oneLogs.setContent("操作:" + oneLogs.getName() + "；影响云环："
						+ oneEqp.getName() + "；Token:" + oneEqp.getToken()
						+ "；唯一标识:" + oneEqp.getWyCode() + "；物联网号码:"
						+ oneEqp.getPhone() + "；ICCID号:" + oneEqp.getPara1()
						+ "；关联用户:" + oneEqp.getMangUser().getName());
				SysLogsBIMang slbi = new SysLogsBIMang(oneLogs, m_bs);
				slbi.start();
			}
		}
		retObj.put("error", URLlImplBase.ErrorMap.get(retObj.getInt("r")));
		m_bs.setRetrunObj(retObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_do_updateAllVehicle
	 * </p>
	 * <p>
	 * 方法描述：得到一个用户初始化信息
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
	public BSObject do_updateAllVehicle(BSObject m_bs) throws Exception {
		JSONObject retObj = new JSONObject();
		retObj.put("r", 0);
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
		String state = m_bs.getPrivateMap().get("pg_allstate");
		BITruck truckBI = new BITruck(null, m_bs);
		if (truckBI.updateAllVehicle(Integer.parseInt(state)) > 0) {
			retObj.put("r", 0);
			if (Integer.parseInt(state) == 0) {
				oneLogs.setName("批量还原无效云盒");
			} else if (Integer.parseInt(state) > 0) {
				oneLogs.setName("批量设置云盒无效");
			} else {
				oneLogs.setName("批量删除云盒");
			}
			oneLogs.setType(1);
			oneLogs.setContent("操作:" + oneLogs.getName());
			SysLogsBIMang slbi = new SysLogsBIMang(oneLogs, m_bs);
			slbi.start();
		}
		retObj.put("error", URLlImplBase.ErrorMap.get(retObj.getInt("r")));
		m_bs.setRetrunObj(retObj);
		return m_bs;
	}

}
