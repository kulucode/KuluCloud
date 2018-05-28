package tt.kulu.out.biss;

import java.util.ArrayList;
import java.util.Calendar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.inspect.pojo.InspectDefPojo;
import tt.kulu.bi.inspect.pojo.InspectPlanPojo;
import tt.kulu.bi.truck.pojo.TruckPojo;
import tt.kulu.out.call.BIInspect;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSInspect
 * </p>
 * <p>
 * 功能描述: 车辆保养Web接口类
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
public class BSInspect {
	/**
	 * <p>
	 * 方法名：do_searchTruckList
	 * </p>
	 * <p>
	 * 方法描述：得到车辆初始化信息页面
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchInspectDefList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");
		String truckDef = m_bs.getPrivateMap().get("pg_truckdef");
		String defClass = m_bs.getPrivateMap().get("pg_defclass");
		String subClass = m_bs.getPrivateMap().get("pg_subclass");
		BIInspect inspectBI = new BIInspect(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (truckDef != null) {
			_paras.put("define", truckDef);
		}
		if (truckDef != null) {
			_paras.put("subclass", subClass);
		}
		if (defClass != null) {
			_paras.put("defclass", defClass);
		}
		ArrayList<InspectDefPojo> list = inspectBI.getInspectDefList(_paras);
		for (InspectDefPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("statev", onePojo.getState());
			oneObj.put("state", InspectDefPojo.STATE_NAME[onePojo.getState()]);
			oneObj.put("defclassv", onePojo.getDefClass());
			oneObj.put("defclass",
					InspectDefPojo.CLASS_NAME[onePojo.getDefClass()]);
			oneObj.put("truckdef", onePojo.getTurckDef().getName());
			oneObj.put("cycle", onePojo.getCycle());
			oneObj.put("mileage", onePojo.getMileage());
			if (onePojo.getSubDefId() != null) {
				oneObj.put("subcount", onePojo.getSubCount());
				oneObj.put("subdef", onePojo.getSubDefId().getName());
			} else {
				oneObj.put("subcount", 0);
				oneObj.put("subdef", "");
			}
			retObj.add(oneObj);
		}
		fretObj.put("list", retObj);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getInspectDefineById
	 * </p>
	 * <p>
	 * 方法描述：根据数据字典ID得到一个数据字典
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getInspectDefineById(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		BIInspect inspectBI = new BIInspect(m_bs);
		InspectDefPojo onePojo = new InspectDefPojo();
		if (type.equals("new")) {
			/** 新增 */
			onePojo.setId(BSGuid.getRandomGUID());
			onePojo.getTurckDef().setId(m_bs.getPrivateMap().get("idefid"));
		} else if (type.equals("edit")) {
			/** 编辑 */
			onePojo = inspectBI.getOneInspectDefById(m_bs.getPrivateMap().get(
					"idefid"));
		}
		fretObj.put("id", onePojo.getId());
		fretObj.put("name", onePojo.getName());
		fretObj.put("state", onePojo.getState());
		fretObj.put("desc", onePojo.getDesc());
		fretObj.put("defclass", onePojo.getDefClass());
		fretObj.put("truckdefid", onePojo.getTurckDef().getId());
		fretObj.put("truckdef", onePojo.getTurckDef().getName());
		fretObj.put("cycle", onePojo.getCycle());
		fretObj.put("mileage", onePojo.getMileage());
		if (onePojo.getSubDefId() != null) {
			fretObj.put("subcount", onePojo.getSubCount());
			fretObj.put("subdefid", onePojo.getSubDefId().getId());
		} else {
			fretObj.put("subcount", 0);
			fretObj.put("subdefid", "");
		}
		fretObj.put("CLASS_NAME", InspectDefPojo.CLASS_NAME);
		fretObj.put("STATE_NAME", InspectDefPojo.STATE_NAME);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateInspectDefine
	 * </p>
	 * <p>
	 * 方法描述：编辑数据字典
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateInspectDefine(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		InspectDefPojo onePojo = new InspectDefPojo();
		onePojo.setId(m_bs.getPrivateMap().get("t_idefid"));
		onePojo.setName(m_bs.getPrivateMap().get("t_idefname"));
		onePojo.getTurckDef().setId(m_bs.getPrivateMap().get("t_itruckdef"));
		onePojo.setState(Integer.parseInt(m_bs.getPrivateMap().get(
				"s_idefstate")));
		onePojo.setDefClass(Integer.parseInt(m_bs.getPrivateMap().get(
				"s_idefclass")));
		onePojo.setCycle(Integer.parseInt(m_bs.getPrivateMap().get(
				"t_idefcycle")));
		onePojo.setSubCount(Integer.parseInt(m_bs.getPrivateMap().get(
				"t_idefsubcount")));
		onePojo.setMileage(m_bs.getPrivateMap().get("t_idefmil"));
		if (m_bs.getPrivateMap().get("t_idefdesc") != null) {
			onePojo.setDesc(m_bs.getPrivateMap().get("t_idefdesc"));
		}
		onePojo.setSubDefId(new InspectDefPojo());
		onePojo.getSubDefId().setId(m_bs.getPrivateMap().get("s_idefsubdef"));

		BIInspect inspectBI = new BIInspect(m_bs);
		int count = 0;
		if (type.equals("new")) {
			count = inspectBI.insertInspectDef(onePojo);
		} else if (type.equals("edit")) {
			count = inspectBI.updateInspectDef(onePojo);
		}
		JSONObject fretObj = new JSONObject();
		fretObj.put("id", onePojo.getId());
		if (count > 0) {
			fretObj.put("r", 0);
		} else {
			fretObj.put("r", 990);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_deleteInspectDefine
	 * </p>
	 * <p>
	 * 方法描述：编辑数据字典
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_deleteInspectDefine(BSObject m_bs) throws Exception {
		BIInspect inspectBI = new BIInspect(m_bs);
		int count = inspectBI.deleteInspectDef(m_bs.getPrivateMap().get(
				"idefid"));
		JSONObject fretObj = new JSONObject();
		if (count > 0) {
			fretObj.put("r", 0);
		} else {
			fretObj.put("r", 990);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_iniInspectPlanFromDef
	 * </p>
	 * <p>
	 * 方法描述：得到车辆初始化信息页面
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_iniInspectPlanFromDef(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		String truckDef = m_bs.getPrivateMap().get("pg_truckdef");
		BIInspect inspectBI = new BIInspect(null, m_bs);
		inspectBI.iniInspectPlanFromDef(truckDef);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchTruckList
	 * </p>
	 * <p>
	 * 方法描述：得到车辆初始化信息页面：www/admin/role/index.html
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchInspectPlanList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		String sText = m_bs.getPrivateMap().get("pg_text");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String org = m_bs.getPrivateMap().get("pg_group");
		String state = m_bs.getPrivateMap().get("pg_state");
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
		BIInspect inspectBI = new BIInspect(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (state != null) {
			_paras.put("state", state);
		}
		if (org != null) {
			_paras.put("group", org);
		}

		_paras.put("key", sText);
		ArrayList<InspectPlanPojo> list = inspectBI.getInspectPlanList(_paras,
				startNum, startNum + pageSize - 1);
		for (InspectPlanPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("cdate", onePojo.getCreateDate());
			oneObj.put("plandate", onePojo.getPlanDate().substring(0, 10));
			// 车辆
			oneObj.put("truckid", onePojo.getTruck().getId());
			oneObj.put("truckno", onePojo.getTruck().getNo());
			oneObj.put("truckname", onePojo.getTruck().getName());
			oneObj.put("truckdef", onePojo.getTruck().getDefine().getName());
			oneObj.put("platenum", onePojo.getTruck().getPlateNum());
			oneObj.put("platecolor", TruckPojo.COLOR_NAME[onePojo.getTruck()
					.getPlateColor()]);
			// 规程
			oneObj.put("defid", onePojo.getInspectDef().getId());
			oneObj.put("defname", onePojo.getInspectDef().getName());
			oneObj.put("worktime", onePojo.getInspectDef().getTurckDef()
					.getWorkTime());
			retObj.add(oneObj);
		}
		fretObj.put("list", retObj);
		fretObj.put("max", _paras.getLong("max"));
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_resetInspectPlan
	 * </p>
	 * <p>
	 * 方法描述：编辑数据字典
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_resetInspectPlan(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		BIInspect inspectBI = new BIInspect(m_bs);
		InspectPlanPojo onePlan = inspectBI.getOneInspectPlanById(m_bs
				.getPrivateMap().get("t_planid"));
		if (onePlan != null) {
			onePlan.setCreateDate(m_bs.getPrivateMap().get("t_planopdate"));
			if (inspectBI.updateInspectPlan(onePlan) > 0) {
				fretObj.put("r", 0);
			}
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateInspectPlanOP
	 * </p>
	 * <p>
	 * 方法描述：编辑数据字典
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateInspectPlanOP(BSObject m_bs) throws Exception {
		BIInspect inspectBI = new BIInspect(m_bs);
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		InspectPlanPojo onePlan = inspectBI.getOneInspectPlanById(m_bs
				.getPrivateMap().get("t_planid"));
		if (onePlan != null) {
			onePlan.setState(1);
			onePlan.getOpUser().setInstId(
					m_bs.getPrivateMap().get("t_planopuser_v"));
			onePlan.setPlanDate(m_bs.getPrivateMap().get("t_planopdate"));
		}
		if (inspectBI.updateInspectPlanOP(onePlan) > 0) {
			fretObj.put("r", 0);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}
}
