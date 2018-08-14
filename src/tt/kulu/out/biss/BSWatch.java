package tt.kulu.out.biss;

import java.util.ArrayList;
import java.util.Calendar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.logs.biclass.SysLogsBIMang;
import tt.kulu.bi.logs.pojo.SysLogsPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.bi.user.pojo.UserWorkParasPojo;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BIEquipment;
import tt.kulu.out.call.BILogin;
import tt.kulu.out.call.BITruck;
import tt.kulu.out.call.BIUser;
import tt.kulu.out.call.BIWatch;

import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSWatch
 * </p>
 * <p>
 * 功能描述: 手表Web接口类
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
public class BSWatch {
	/**
	 * <p>
	 * 方法名：do_searchUserWorkParas
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
	public BSObject do_searchWatchWordParasList(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		long startNum = 0;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject retJSON = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");
		String group = m_bs.getPrivateMap().get("pg_group");
		String elemin = m_bs.getPrivateMap().get("pg_elemin");
		String elemax = m_bs.getPrivateMap().get("pg_elemax");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String geoArea = m_bs.getPrivateMap().get("pg_geoarea");
		String state = m_bs.getPrivateMap().get("pg_state");
		JSONObject paras = new JSONObject();
		if (sText != null) {
			paras.put("key", sText);
		}
		if (geoArea != null) {
			paras.put("geoarea", geoArea);
		}
		if (group != null) {
			paras.put("group", group);
		}
		if (state != null) {
			paras.put("eqpstate", state);
		}
		if (elemin == null) {
			elemin = "0";
		}
		if (elemax == null) {
			elemax = "100";
		}
		paras.put("ele", elemin + "," + elemax);
		if (user.getRoleWhere().indexOf("'SUPER_ADMIN'") < 0
				&& user.getRoleWhere().indexOf("'ADMIN'") < 0) {
			paras.put("login", user.getGroupId());
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
			pageSize = 50;
		}
		startNum = pageSize * pageNum;
		// 调用BI
		BIWatch watchBI = new BIWatch(null, m_bs);
		// 返回数据
		JSONArray userlist = new JSONArray();
		ArrayList<UserWorkParasPojo> list = watchBI.getWatchWordParasList(
				paras, startNum, startNum + pageSize - 1);
		for (UserWorkParasPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("eqpid", onePojo.getEqpInst().getInstId());
			oneObj.put("pdate", onePojo.getEqpInst().getProDate());
			oneObj.put("wycode", onePojo.getEqpInst().getWyCode());
			oneObj.put("token", onePojo.getEqpInst().getToken());
			oneObj.put("name", onePojo.getEqpInst().getName());
			oneObj.put("def", onePojo.getEqpInst().getEqpDef().getName());
			oneObj.put("qr", onePojo.getEqpInst().getQrCode());
			oneObj.put("phone", onePojo.getEqpInst().getPhone());
			oneObj.put("step", onePojo.getStep());
			oneObj.put("stepdate", onePojo.getStepDate());
			oneObj.put("hr", onePojo.getHeartRate());
			oneObj.put("ele", onePojo.getEleValue());
			oneObj.put("hrdate", onePojo.getHrDate());
			// oneObj.put("bpoh", onePojo.getBroHigh());
			// oneObj.put("bpol", onePojo.getBroLow());
			// oneObj.put("bpodate", onePojo.getBroDate());
			oneObj.put("lat", onePojo.getLatitude());
			oneObj.put("lon", onePojo.getLongitude());
			oneObj.put("geodate", onePojo.getGeoDate());
			//
			oneObj.put("state", onePojo.getEqpInst().getState());
			oneObj.put("statename", EquipmentInstPojo.STATE_NAME[onePojo
					.getEqpInst().getState()]);
			oneObj.put("online", EquipmentInstPojo.ONLINE_STATE_NAME[onePojo
					.getEqpInst().getOnlineState()]);
			//
			oneObj.put("muser", onePojo.getEqpInst().getMangUser().getInstId());
			oneObj.put("muserid", onePojo.getEqpInst().getMangUser().getId());
			oneObj.put("musername", onePojo.getEqpInst().getMangUser()
					.getName());
			oneObj.put("muserphone", onePojo.getEqpInst().getMangUser()
					.getmPhone());
			if (onePojo.getEqpInst().getOrg() != null) {
				oneObj.put("morg", onePojo.getEqpInst().getOrg().getId());
				oneObj.put("morgname", onePojo.getEqpInst().getOrg()
						.getAllName().replaceAll(",", "-"));
			} else {
				oneObj.put("morg", "");
				oneObj.put("morgname", "");
			}

			oneObj.put("fanceflg", onePojo.getFanceFlg());
			oneObj.put("date", m_bs.getDateEx().getThisDate(0, 0));
			userlist.add(oneObj);
		}
		retJSON.put("list", userlist);
		retJSON.put("max", paras.getLong("max"));
		retJSON.put("r", 0);
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getWatchParas
	 * </p>
	 * <p>
	 * 方法描述：搜索手表参数
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getWatchParas(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		String dicid = m_bs.getPrivateMap().get("dicid");
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
	 * 方法名：do_updateWatchParas
	 * </p>
	 * <p>
	 * 方法描述：搜索手表参数
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateWatchParas(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		String dicid = m_bs.getPrivateMap().get("dicid");
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setName("编辑云环配置参数");
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
		// 调用BI
		BIDic dicBI = new BIDic(null, m_bs);
		// 返回数据
		DicItemPojo oneItem = new DicItemPojo();
		// 心率高位报警
		oneItem = dicBI.getDicItemByRedis("WATCH_PARAS_0");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.getDic().setId(dicid);
			oneItem.setName("心率高位报警");
			oneItem.setIndex(0);
			oneItem.setValue("hr_max");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_0"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_0"));
			dicBI.updateDicItem(oneItem);
		}
		// 心率低位报警
		oneItem = dicBI.getDicItemByRedis("WATCH_PARAS_1");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.getDic().setId(dicid);
			oneItem.setName("心率低位报警");
			oneItem.setIndex(0);
			oneItem.setValue("hr_min");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_1"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_1"));
			dicBI.updateDicItem(oneItem);
		}
		// 心率上传间隔
		oneItem = dicBI.getDicItemByRedis("WATCH_PARAS_2");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.getDic().setId(dicid);
			oneItem.setName("心率上传间隔");
			oneItem.setIndex(0);
			oneItem.setValue("hr_date");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_2"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_2"));
			dicBI.updateDicItem(oneItem);
		}
		// 血压高压报警
		oneItem = dicBI.getDicItemByRedis("WATCH_PARAS_3");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.getDic().setId(dicid);
			oneItem.setName("血压高压报警");
			oneItem.setIndex(0);
			oneItem.setValue("bpo_h");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_3"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_3"));
			dicBI.updateDicItem(oneItem);
		}
		// 血压低压报警
		oneItem = dicBI.getDicItemByRedis("WATCH_PARAS_4");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.getDic().setId(dicid);
			oneItem.setName("血压低压报警");
			oneItem.setIndex(0);
			oneItem.setValue("bpo_l");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_4"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_4"));
			dicBI.updateDicItem(oneItem);
		}
		// 血压上传间隔
		oneItem = dicBI.getDicItemByRedis("WATCH_PARAS_6");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.getDic().setId(dicid);
			oneItem.setName("血压上传间隔");
			oneItem.setIndex(0);
			oneItem.setValue("bpo_date");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_6"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_6"));
			dicBI.updateDicItem(oneItem);
		}
		// 记步上传间隔
		oneItem = dicBI.getDicItemByRedis("WATCH_PARAS_7");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.getDic().setId(dicid);
			oneItem.setName("记步上传间隔");
			oneItem.setIndex(0);
			oneItem.setValue("step_date");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_7"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_7"));
			dicBI.updateDicItem(oneItem);
		}
		// 位置上传间隔
		oneItem = dicBI.getDicItemByRedis("WATCH_PARAS_8");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.getDic().setId(dicid);
			oneItem.setName("位置上传间隔");
			oneItem.setIndex(0);
			oneItem.setValue("gps_date");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_8"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_8"));
			dicBI.updateDicItem(oneItem);
		}
		// 上班时间
		oneItem = dicBI.getDicItemByRedis("WATCH_PARAS_9");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.getDic().setId(dicid);
			oneItem.setName("上班时间");
			oneItem.setIndex(0);
			oneItem.setValue("gps_workondate");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_9"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_9"));
			dicBI.updateDicItem(oneItem);
		}
		// 下班时间
		oneItem = dicBI.getDicItemByRedis("WATCH_PARAS_10");
		if (oneItem == null) {
			oneItem = new DicItemPojo();
			oneItem.getDic().setId(dicid);
			oneItem.setName("下班时间");
			oneItem.setIndex(0);
			oneItem.setValue("gps_workoutdate");
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_10"));
			dicBI.insertDicItem(oneItem);
		} else {
			oneItem.setValue2(m_bs.getPrivateMap().get("t_dicitem_10"));
			dicBI.updateDicItem(oneItem);
		}
		// 写日志
		oneLogs.setType(1);
		oneLogs.setContent("操作:" + oneLogs.getName());
		SysLogsBIMang slbi = new SysLogsBIMang(oneLogs, m_bs);
		slbi.start();
		retJSON.put("r", 0);
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_deleteOneWatch
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
	public BSObject do_deleteOneWatch(BSObject m_bs) throws Exception {
		JSONObject retObj = new JSONObject();
		retObj.put("r", 0);
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
		String instid = m_bs.getPrivateMap().get("pg_inst");
		String type = m_bs.getPrivateMap().get("pg_type");
		int count = 0;
		BIWatch watchBI = new BIWatch(null, m_bs);
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		EquipmentInstPojo oneEqp = eqpBI.getOneEquipmentInstById(instid);
		if (oneEqp != null) {
			if (type.equals("delete")) {
				// 物理删除
				count = watchBI.deleteOneWatch(instid);
				oneLogs.setName("删除云环");
			} else if (type.equals("state")) {
				// 逻辑删除
				count = watchBI.updateOneWatchState(instid, 4);
				oneLogs.setName("设置云环无效");
			} else if (type.equals("reset")) {
				// 还原
				count = watchBI.updateOneWatchState(instid, 0);
				oneLogs.setName("还原无效云环");
			}
			if (count > 0) {
				retObj.put("r", 0);
				// 写日志
				oneLogs.setType(1);
				oneLogs.setContent("操作:" + oneLogs.getName() + "；影响云环："
						+ oneEqp.getName() + "；Token:" + oneEqp.getToken()
						+ "；唯一标识:" + oneEqp.getWyCode() + "；物联网号码:"
						+ oneEqp.getPhone() + "；关联用户:"
						+ oneEqp.getMangUser().getName());
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
	 * 方法名：do_updateAllWatch
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
	public BSObject do_updateAllWatch(BSObject m_bs) throws Exception {
		JSONObject retObj = new JSONObject();
		retObj.put("r", 0);
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
		String state = m_bs.getPrivateMap().get("pg_allstate");
		BIWatch watchBI = new BIWatch(null, m_bs);
		if (watchBI.updateAllWatch(Integer.parseInt(state)) > 0) {
			retObj.put("r", 0);
			if (Integer.parseInt(state) == 0) {
				oneLogs.setName("批量还原无效云环");
			} else if (Integer.parseInt(state) > 0) {
				oneLogs.setName("批量设置云环无效");
			} else {
				oneLogs.setName("批量删除云环");
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
