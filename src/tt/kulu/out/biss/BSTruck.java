package tt.kulu.out.biss;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.file.pojo.BFSFilePojo;
import tt.kulu.bi.logs.biclass.SysLogsBIMang;
import tt.kulu.bi.logs.pojo.SysLogsPojo;
import tt.kulu.bi.truck.pojo.TruckDefinePojo;
import tt.kulu.bi.truck.pojo.TruckDriverRPojo;
import tt.kulu.bi.truck.pojo.TruckFixLogsPojo;
import tt.kulu.bi.truck.pojo.TruckPojo;
import tt.kulu.bi.truck.pojo.TruckVideoPojo;
import tt.kulu.bi.user.pojo.OrgPojo;
import tt.kulu.out.call.BICompany;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BIFile;
import tt.kulu.out.call.BILogin;
import tt.kulu.out.call.BITruck;
import tt.kulu.out.call.BIUser;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSTruck
 * </p>
 * <p>
 * 功能描述: 车辆Web接口类
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
public class BSTruck {
	/**
	 * <p>
	 * 方法名：do_searchTruckDefineList
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
	public BSObject do_searchTruckDefineList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");
		BITruck truckBI = new BITruck(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		ArrayList<TruckDefinePojo> list = truckBI.getTruckDefList(_paras);
		for (TruckDefinePojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("sdate", onePojo.getSaleDate());
			oneObj.put("no", onePojo.getNo());
			oneObj.put("company", onePojo.getCompany());
			oneObj.put("oilmj", onePojo.getOilMJ());
			oneObj.put("oilde", onePojo.getOilDe());
			oneObj.put("brand", onePojo.getBrand());
			oneObj.put("worktime", onePojo.getWorkTime());
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
	 * 方法名：do_getTruckDefineById
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
	public BSObject do_getTruckDefineById(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		TruckDefinePojo onePojo = new TruckDefinePojo();
		if (type.equals("new")) {
			/** 新增 */
			onePojo.setId(BSGuid.getRandomGUID());
		} else if (type.equals("edit")) {
			/** 编辑 */
			onePojo = new BITruck(m_bs).getOneTruckDefById((String) m_bs
					.getPrivateMap().get("defid"));
		}
		fretObj.put("id", onePojo.getId());
		fretObj.put("name", onePojo.getName());
		fretObj.put("sdate", onePojo.getSaleDate());
		fretObj.put("no", onePojo.getNo());
		fretObj.put("company", onePojo.getCompany());
		fretObj.put("oilmj", onePojo.getOilMJ());
		fretObj.put("oilde", onePojo.getOilDe());
		fretObj.put("brand", onePojo.getBrand());
		fretObj.put("worktime", onePojo.getWorkTime());
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateTruckDefine
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
	public BSObject do_updateTruckDefine(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
		TruckDefinePojo onePojo = new TruckDefinePojo();
		onePojo.setId(m_bs.getPrivateMap().get("t_defid"));
		onePojo.setName(m_bs.getPrivateMap().get("t_defname"));
		onePojo.setNo(m_bs.getPrivateMap().get("t_defno"));
		onePojo.setCompany(m_bs.getPrivateMap().get("t_defcomp"));
		onePojo.setSaleDate(m_bs.getPrivateMap().get("t_defsdate"));
		onePojo.setWorkTime(Integer.parseInt(m_bs.getPrivateMap().get(
				"t_defworktime")));
		if (m_bs.getPrivateMap().get("t_defoilmj") != null) {
			onePojo.setOilMJ(m_bs.getPrivateMap().get("t_defoilmj"));
		}
		if (m_bs.getPrivateMap().get("t_defoilde") != null) {
			onePojo.setOilDe(m_bs.getPrivateMap().get("t_defoilde"));
		}
		if (m_bs.getPrivateMap().get("t_defbrand") != null) {
			onePojo.setBrand(m_bs.getPrivateMap().get("t_defbrand"));
		}
		BITruck truckBI = new BITruck(m_bs);
		int count = 0;
		if (type.equals("new")) {
			count = truckBI.insertTruckDef(onePojo);
			oneLogs.setName("新增车型");
		} else if (type.equals("edit")) {
			count = truckBI.updateTruckDef(onePojo);
			oneLogs.setName("编辑车型");
		}
		JSONObject fretObj = new JSONObject();
		fretObj.put("id", onePojo.getId());
		if (count > 0) {
			fretObj.put("r", 0);
			// 写日志
			oneLogs.setType(1);
			oneLogs.setContent("操作:" + oneLogs.getName() + "；影响车型："
					+ onePojo.getName() + "[" + onePojo.getNo() + "]");
			SysLogsBIMang slbi = new SysLogsBIMang(oneLogs, m_bs);
			slbi.start();
		} else {
			fretObj.put("r", 990);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_TruckIni
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
	public BSObject do_TruckIni(BSObject m_bs) throws Exception {
		// 数据准备
		JSONObject fretObj = new JSONObject();
		// 车型
		JSONArray retObj = new JSONArray();
		JSONObject _paras = new JSONObject();
		BITruck truckBI = new BITruck(m_bs);
		ArrayList<TruckDefinePojo> list = truckBI.getTruckDefList(_paras);
		for (TruckDefinePojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			retObj.add(oneObj);
		}
		fretObj.put("truckdef", retObj);
		// 车牌颜色
		fretObj.put("paltecolor", TruckPojo.COLOR_NAME);
		// 当前运营商
		BICompany compBI = new BICompany(m_bs);
		CompanyPojo onePojo = compBI.getOneCompanyByType(1);
		JSONObject compJ = new JSONObject();
		if (onePojo != null) {
			compJ.put("id", onePojo.getId());
			compJ.put("name", onePojo.getName());
		} else {
			onePojo = new CompanyPojo();
			compJ.put("id", "");
			compJ.put("name", "未设定运营商");
		}
		fretObj.put("comp", onePojo);
		// 得到项目组
		BIUser userBI = new BIUser(m_bs);
		_paras.clear();
		_paras.put("type", 1);
		ArrayList<OrgPojo> orgList = userBI.getOrgList(_paras, 0, 200);
		JSONArray orgObj = new JSONArray();
		for (OrgPojo oneOrg : orgList) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", oneOrg.getId());
			oneObj.put("name", oneOrg.getAllName().replaceAll(",", "-"));
			orgObj.add(oneObj);
		}
		fretObj.put("groups", orgObj);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchTruckLookUp
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
	public BSObject do_searchTruckLookUp(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");

		BITruck truckBI = new BITruck(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);

		ArrayList<TruckPojo> list = truckBI.getTruckList(_paras, 0, 50);
		for (TruckPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("value", onePojo.getId());
			oneObj.put("label", onePojo.getName() + "[" + onePojo.getPlateNum()
					+ "]");
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
	public BSObject do_searchTruckList(BSObject m_bs) throws Exception {
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
		BITruck truckBI = new BITruck(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (org != null) {
			_paras.put("group", org);
		}
		if (state != null) {
			_paras.put("state", state);
		}

		ArrayList<TruckPojo> list = truckBI.getTruckList(_paras, startNum,
				startNum + pageSize - 1);
		for (TruckPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("no", onePojo.getNo());
			oneObj.put("name", onePojo.getName());
			oneObj.put("def", onePojo.getDefine().getName());
			oneObj.put("indate", onePojo.getInDate());
			oneObj.put("pdate", onePojo.getProDate());
			oneObj.put("state", onePojo.getState());
			oneObj.put("statename", TruckPojo.STATE_NAME[onePojo.getState()]);
			oneObj.put("platenum", onePojo.getPlateNum());
			oneObj.put("platecolor",
					TruckPojo.COLOR_NAME[onePojo.getPlateColor()]);
			oneObj.put("cjno", onePojo.getCjNo());
			oneObj.put("upno", onePojo.getUpNo());
			oneObj.put("uodate", onePojo.getUpDate());
			oneObj.put("area", onePojo.getArea());
			oneObj.put("oildef", onePojo.getOilDef().toString());
			oneObj.put("muser", onePojo.getMangUser().getInstId());
			oneObj.put("muserid", onePojo.getMangUser().getId());
			oneObj.put("musername", onePojo.getMangUser().getName());
			oneObj.put("muserphone", onePojo.getMangUser().getmPhone());
			if (onePojo.getOrg() != null) {
				oneObj.put("morg", onePojo.getOrg().getId());
				oneObj.put("morgname", onePojo.getOrg().getAllName()
						.replaceAll(",", "-"));
			} else {
				oneObj.put("morg", "");
				oneObj.put("morgname", "");
			}
			oneObj.put("vnum", onePojo.getVideoNum());
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
	public BSObject do_searchTruckTreeList(BSObject m_bs) throws Exception {
		String pid = m_bs.getPrivateMap().get("in_pid");
		// 数据准备
		JSONObject fretObj = new JSONObject();
		JSONArray listObj = new JSONArray();
		// 得到一级单位
		BIUser userBI = new BIUser(null, m_bs);
		BICompany compBI = new BICompany(null, m_bs);
		BITruck truckBI = new BITruck(null, m_bs);
		CompanyPojo comp = compBI.getThisCompanyByRedis();
		// 参数
		JSONObject paras = new JSONObject();
		paras.put("type", "1,2");
		paras.put("comp", comp.getId());
		paras.put("sub", pid);
		paras.put("root", "root");
		JSONObject truckParas = new JSONObject();
		truckParas.put("state", 0);
		ArrayList<TruckPojo> truckList = new ArrayList<TruckPojo>();
		ArrayList<OrgPojo> list = userBI.getOrgList(paras, 0, 100);
		for (OrgPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			// oneObj.put("code", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("cnum", onePojo.getSubOrgNum());
			// 得到车辆
			truckParas.put("onlygroup", onePojo.getId());
			truckList = truckBI.getTruckList(truckParas, 0, 500);
			JSONArray trL = new JSONArray();
			for (TruckPojo oneTruck : truckList) {
				JSONObject oneT = new JSONObject();
				oneT.put("id", oneTruck.getId());
				oneT.put("name", oneTruck.getPlateNum());
				trL.add(oneT);
			}
			oneObj.put("struck", trL.size());
			oneObj.put("trucks", trL);
			oneObj.put("snum", onePojo.getUserNum());
			if (onePojo.getSubOrgNum() > 0) {
				paras.put("root", "");
				paras.put("sub", onePojo.getId());
				oneObj.put("children", this._getChildOrgList(userBI, paras,
						truckParas, truckBI));
			}
			listObj.add(oneObj);
		}
		fretObj.put("list", listObj);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getTruckById
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
	public BSObject do_getTruckById(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		TruckPojo onePojo = new TruckPojo();
		if (type.equals("new")) {
			/** 新增 */
			onePojo.setId(BSGuid.getRandomGUID());
		} else if (type.equals("edit")) {
			/** 编辑 */
			onePojo = new BITruck(m_bs).getOneTruckById((String) m_bs
					.getPrivateMap().get("truckid"));
		}
		fretObj.put("id", onePojo.getId());
		fretObj.put("no", onePojo.getNo());
		fretObj.put("cjno", onePojo.getCjNo());
		fretObj.put("name", onePojo.getName());
		fretObj.put("inname", onePojo.getInName());
		fretObj.put("def", onePojo.getDefine().getId());
		fretObj.put("indate", onePojo.getInDate());
		fretObj.put("pdate", onePojo.getProDate());
		fretObj.put("style", TruckPojo.STATE_NAME[onePojo.getState()]);
		fretObj.put("platenum", onePojo.getPlateNum());
		fretObj.put("platecolor", onePojo.getPlateColor());
		fretObj.put("upno", onePojo.getUpNo());
		fretObj.put("uodate", onePojo.getUpDate());
		fretObj.put("area", onePojo.getArea());
		fretObj.put("oildef", onePojo.getOilDef().toString());
		fretObj.put("muser", onePojo.getMangUser().getInstId());
		fretObj.put("muserid", onePojo.getMangUser().getId());
		fretObj.put("musername", onePojo.getMangUser().getName());
		fretObj.put("muserphone", onePojo.getMangUser().getmPhone());
		fretObj.put("morg", onePojo.getOrg().getId());
		fretObj.put("morgname",
				(onePojo.getOrg().getAllName()).replaceAll(",", "-"));
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateTruck
	 * </p>
	 * <p>
	 * 方法描述：编辑车辆
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateTruck(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));

		TruckPojo onePojo = new TruckPojo();
		onePojo.setId(m_bs.getPrivateMap().get("t_truckid"));
		onePojo.setName(m_bs.getPrivateMap().get("t_truckname"));
		onePojo.setInName(m_bs.getPrivateMap().get("t_truckinname"));
		onePojo.setNo(m_bs.getPrivateMap().get("t_truckno"));
		onePojo.setCjNo(m_bs.getPrivateMap().get("t_truckcjno"));
		onePojo.getDefine().setId(m_bs.getPrivateMap().get("s_truckdef"));
		onePojo.setInDate(m_bs.getPrivateMap().get("t_truckindate"));
		onePojo.setProDate(m_bs.getPrivateMap().get("t_truckpdate"));
		onePojo.getMangUser().setInstId(
				m_bs.getPrivateMap().get("t_truckmuser_v"));
		onePojo.getOrg().setId(m_bs.getPrivateMap().get("t_truckorg_v"));
		onePojo.setPlateNum(m_bs.getPrivateMap().get("t_truckpnum"));
		onePojo.setPlateColor(Integer.parseInt(m_bs.getPrivateMap().get(
				"s_truckpcolor")));
		if (m_bs.getPrivateMap().get("t_truckupno") != null) {
			onePojo.setUpNo(m_bs.getPrivateMap().get("t_truckupno"));
		}
		if (m_bs.getPrivateMap().get("t_truckupdate") != null) {
			onePojo.setUpDate(m_bs.getPrivateMap().get("t_truckupdate"));
		}
		if (m_bs.getPrivateMap().get("t_truckoildef") != null) {
			if (!m_bs.getPrivateMap().get("t_truckoildef").equals("")) {
				onePojo.setOilDef(JSONObject.fromObject(m_bs.getPrivateMap()
						.get("t_truckoildef")));
			}
		}
		onePojo.getArea().setId(m_bs.getPrivateMap().get("t_truckarea"));
		BITruck truckBI = new BITruck(m_bs);
		int count = 0;
		if (type.equals("new")) {
			count = truckBI.insertTruck(onePojo);
			oneLogs.setName("新增车辆");
		} else if (type.equals("edit")) {
			count = truckBI.updateTruck(onePojo);
			oneLogs.setName("编辑车辆");
		}
		JSONObject fretObj = new JSONObject();
		fretObj.put("id", onePojo.getId());
		if (count > 0) {
			fretObj.put("r", 0);
			// 写日志
			oneLogs.setType(1);
			oneLogs.setContent("操作:" + oneLogs.getName() + "；影响车辆："
					+ onePojo.getName() + "；车牌：" + onePojo.getPlateNum()
					+ "；车架号：" + onePojo.getCjNo());
			SysLogsBIMang slbi = new SysLogsBIMang(oneLogs, m_bs);
			slbi.start();
		} else {
			fretObj.put("r", 990);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_deleteOneTruck
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
	public BSObject do_deleteOneTruck(BSObject m_bs) throws Exception {
		JSONObject retObj = new JSONObject();
		retObj.put("r", 0);
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
		String instid = m_bs.getPrivateMap().get("pg_inst");
		String type = m_bs.getPrivateMap().get("pg_type");
		int count = 0;
		BITruck truckBI = new BITruck(null, m_bs);
		TruckPojo onePojo = truckBI.getOneTruckById(instid);
		if (onePojo != null) {
			if (type.equals("delete")) {
				// 物理删除
				count = truckBI.deleteOneTruck(instid);
				oneLogs.setName("删除车辆");
			} else if (type.equals("state")) {
				// 逻辑删除
				count = truckBI.updateOneTruckState(instid, 4);
				oneLogs.setName("设置车辆无效");
			} else if (type.equals("reset")) {
				// 还原
				count = truckBI.updateOneTruckState(instid, 0);
				oneLogs.setName("还原无效车辆");
			}
			if (count > 0) {
				retObj.put("r", 0);
				// 写日志
				oneLogs.setType(1);
				oneLogs.setContent("操作:" + oneLogs.getName() + "；影响车辆："
						+ onePojo.getName() + "；车牌：" + onePojo.getPlateNum()
						+ "；车架号：" + onePojo.getCjNo());
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
	 * 方法名：do_searchDriverList
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
	public BSObject do_searchDriverList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String turck = m_bs.getPrivateMap().get("truckid");
		BITruck truckBI = new BITruck(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("turck", turck);

		ArrayList<TruckDriverRPojo> list = truckBI.getTruckDriverList(_paras);
		for (TruckDriverRPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("truckid", onePojo.getTruck().getId());
			oneObj.put("userinstid", onePojo.getUser().getInstId());
			oneObj.put("userid", onePojo.getUser().getId());
			oneObj.put("username", onePojo.getUser().getName());
			oneObj.put("userphone", onePojo.getUser().getmPhone());
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
	 * 方法名：do_addTruckDriver
	 * </p>
	 * <p>
	 * 方法描述：添加车辆司机
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_addTruckDriver(BSObject m_bs) throws Exception {
		TruckDriverRPojo onePojo = new TruckDriverRPojo();
		onePojo.getUser().setInstId(m_bs.getPrivateMap().get("t_userid"));
		onePojo.getTruck().setId(m_bs.getPrivateMap().get("t_truckid"));

		BITruck truckBI = new BITruck(m_bs);
		JSONObject fretObj = new JSONObject();
		if (truckBI.addOneTruckDriver(onePojo) > 0) {
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
	 * 方法名：do_deleteTruckDriver
	 * </p>
	 * <p>
	 * 方法描述：删除车辆司机
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_deleteTruckDriver(BSObject m_bs) throws Exception {
		BITruck truckBI = new BITruck(m_bs);
		JSONObject fretObj = new JSONObject();
		if (truckBI.deleteOneTruckDriver(m_bs.getPrivateMap().get("t_truckid"),
				m_bs.getPrivateMap().get("t_userid")) > 0) {
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
	 * 方法名：do_searchTruckVideoList
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
	public BSObject do_searchTruckVideoList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String truckId = m_bs.getPrivateMap().get("truckid");
		BITruck truckBI = new BITruck(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("truck", truckId);
		ArrayList<TruckVideoPojo> list = truckBI.getTruckVideoList(_paras);
		for (TruckVideoPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("ip", onePojo.getIp());
			oneObj.put("port", onePojo.getPort());
			oneObj.put("user", onePojo.getUser());
			oneObj.put("key", onePojo.getPassword());
			oneObj.put("url", onePojo.getUrl());
			oneObj.put("eqpno", onePojo.getEqpNo());
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
	 * 方法名：do_getTruckDefineById
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
	public BSObject do_getTruckVideoById(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		TruckVideoPojo onePojo = new TruckVideoPojo();
		if (type.equals("new")) {
			/** 新增 */
			onePojo.setId(BSGuid.getRandomGUID());
		} else if (type.equals("edit")) {
			/** 编辑 */
			onePojo = new BITruck(m_bs).getOneTruckVideoById((String) m_bs
					.getPrivateMap().get("videoid"));
		}
		fretObj.put("id", onePojo.getId());
		fretObj.put("ip", onePojo.getIp());
		fretObj.put("port", onePojo.getPort());
		fretObj.put("user", onePojo.getUser());
		fretObj.put("key", onePojo.getPassword());
		fretObj.put("url", onePojo.getUrl());
		fretObj.put("eqpno", onePojo.getEqpNo());
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateTruckVideo
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
	public BSObject do_updateTruckVideo(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		TruckVideoPojo onePojo = new TruckVideoPojo();
		onePojo.setId(m_bs.getPrivateMap().get("t_videoid"));
		onePojo.getTruck().setId(m_bs.getPrivateMap().get("t_truckid"));
		onePojo.setIp(m_bs.getPrivateMap().get("t_videoip"));
		onePojo.setPort(Integer.parseInt(m_bs.getPrivateMap()
				.get("t_videoport")));
		onePojo.setUser(m_bs.getPrivateMap().get("t_videouser"));
		onePojo.setPassword(m_bs.getPrivateMap().get("t_videokey"));
		onePojo.setUrl(m_bs.getPrivateMap().get("t_videourl"));

		BITruck truckBI = new BITruck(m_bs);
		int count = 0;
		if (type.equals("new")) {
			count = truckBI.insertTruckVideo(onePojo);
		} else if (type.equals("edit")) {
			count = truckBI.updateTruckVideo(onePojo);
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
	 * 方法名：do_updateTruckVideo
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
	public BSObject do_updatePLTruckVideo(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		TruckVideoPojo onePojo = new TruckVideoPojo();
		onePojo.getTruck().setId(m_bs.getPrivateMap().get("t_truckid"));
		onePojo.setId(m_bs.getPrivateMap().get("t_videoid"));
		onePojo.setIp(m_bs.getPrivateMap().get("t_videoip"));
		onePojo.setPort(Integer.parseInt(m_bs.getPrivateMap()
				.get("t_videoport")));
		onePojo.setUser(m_bs.getPrivateMap().get("t_videouser"));
		onePojo.setPassword(m_bs.getPrivateMap().get("t_videokey"));
		onePojo.setUrl(m_bs.getPrivateMap().get("t_videourl"));

		BITruck truckBI = new BITruck(m_bs);
		int count = 0;
		String trucks[] = onePojo.getTruck().getId().split(",");
		for (String oneT : trucks) {
			if (!oneT.equals("")) {
				truckBI.deleteTruckVideoByTruck(oneT);
				onePojo.setId(BSGuid.getRandomGUID());
				onePojo.getTruck().setId(oneT);
				count += truckBI.updateTruckVideo(onePojo);
			}
		}
		if (count > 0) {
			fretObj.put("r", 0);
			SysLogsPojo oneLogs = new SysLogsPojo();
			oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
			oneLogs.setName("批量配置车载视频");
			// 写日志
			oneLogs.setType(1);
			oneLogs.setContent("操作:" + oneLogs.getName());
			SysLogsBIMang slbi = new SysLogsBIMang(oneLogs, m_bs);
			slbi.start();
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_deleteTruckVideo
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
	public BSObject do_deleteTruckVideo(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		if ((new BITruck(null, null)).deleteTruckVideo(m_bs.getPrivateMap()
				.get("videoid")) > 0) {
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
	 * 方法名：do_searchTruckFixLogsList
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
	public BSObject do_searchTruckFixLogsList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		String sText = m_bs.getPrivateMap().get("pg_text");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String type = m_bs.getPrivateMap().get("pg_type");
		String org = m_bs.getPrivateMap().get("pg_group");
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
		BITruck truckBI = new BITruck(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (org != null) {
			_paras.put("group", org);
		}
		if (type != null) {
			_paras.put("type", type);
		}

		ArrayList<TruckFixLogsPojo> list = truckBI.getTruckFixLogsList(_paras,
				startNum, startNum + pageSize - 1);
		for (TruckFixLogsPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("date", onePojo.getLogDate());
			oneObj.put("content", onePojo.getContent());
			oneObj.put("type", onePojo.getType().getName());
			oneObj.put("money", onePojo.getMoney());
			// 车辆
			oneObj.put("truckid", onePojo.getTruck().getId());
			oneObj.put("truckno", onePojo.getTruck().getNo());
			oneObj.put("truckname", onePojo.getTruck().getName());
			oneObj.put("truckdef", onePojo.getTruck().getDefine().getName());
			oneObj.put("platenum", onePojo.getTruck().getPlateNum());
			// 操作人
			oneObj.put("muser", onePojo.getUser().getInstId());
			oneObj.put("muserid", onePojo.getUser().getId());
			oneObj.put("musername", onePojo.getUser().getName());
			oneObj.put("muserphone", onePojo.getUser().getmPhone());
			if (onePojo.getUser().getOrg() != null) {
				oneObj.put("morg", onePojo.getUser().getOrg().getId());
				oneObj.put("morgname", onePojo.getUser().getOrg().getAllName()
						.replaceAll(",", "-"));
			} else {
				oneObj.put("morg", "");
				oneObj.put("morgname", "");
			}
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
	 * 方法名：do_getTruckFixLogsById
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
	public BSObject do_getTruckFixLogsById(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		TruckFixLogsPojo onePojo = new TruckFixLogsPojo();
		if (type.equals("new")) {
			/** 新增 */
			onePojo.setId(BSGuid.getRandomGUID());
		} else if (type.equals("edit")) {
			/** 编辑 */
			onePojo = new BITruck(m_bs).getOneTruckFixLogsById((String) m_bs
					.getPrivateMap().get("fixlogsid"));
		}
		fretObj.put("id", onePojo.getId());
		fretObj.put("name", onePojo.getName());
		fretObj.put("date", onePojo.getLogDate());
		fretObj.put("type", onePojo.getType().getId());
		fretObj.put("content", onePojo.getContent());
		fretObj.put("money", onePojo.getMoney());
		if (!onePojo.getTruck().getId().equals("")) {
			fretObj.put("truckid", onePojo.getTruck().getId());
			fretObj.put("truckname", onePojo.getTruck().getName() + "["
					+ onePojo.getTruck().getPlateNum() + "]");
		} else {
			fretObj.put("truckid", "");
			fretObj.put("truckname", "");
		}
		if (!onePojo.getUser().getInstId().equals("")) {
			fretObj.put("userinst", onePojo.getUser().getInstId());
			fretObj.put("user", onePojo.getUser().getName() + "["
					+ onePojo.getUser().getName() + "] "
					+ onePojo.getUser().getmPhone());
		} else {
			fretObj.put("user", "");
			fretObj.put("truckname", "");
		}
		fretObj.put("TYPE_LIST",
				(new BIDic(null)).getDicItemListByRedis("TRUCK_FIXTYPE"));
		// 文件
		JSONArray fileJSON = new JSONArray();
		for (BFSFilePojo oneFile : onePojo.getFiles()) {
			JSONObject oneFileJSON = new JSONObject();
			oneFileJSON.put("type", oneFile.getType());
			oneFileJSON.put("fid", oneFile.getInstId());
			oneFileJSON.put("name", oneFile.getFileName());
			oneFileJSON.put("url", BIFile.GetImgURL(oneFile));
			fileJSON.add(oneFileJSON);
		}
		fretObj.put("files", fileJSON);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateTruckVideo
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
	public BSObject do_updateTruckFixlogs(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		TruckFixLogsPojo onePojo = new TruckFixLogsPojo();
		onePojo.setId(m_bs.getPrivateMap().get("t_flid"));
		onePojo.setName(m_bs.getPrivateMap().get("t_flname"));
		onePojo.setContent(m_bs.getPrivateMap().get("t_fltext"));
		onePojo.setMoney(m_bs.getPrivateMap().get("t_flmoney"));
		onePojo.setLogDate(m_bs.getPrivateMap().get("t_fldate"));
		onePojo.getType().setId(m_bs.getPrivateMap().get("s_fltype"));
		onePojo.getTruck().setId(m_bs.getPrivateMap().get("t_fltruck_v"));
		onePojo.getUser().setInstId(m_bs.getPrivateMap().get("t_fluser_v"));

		BITruck truckBI = new BITruck(m_bs);
		int count = 0;
		if (type.equals("new")) {
			count = truckBI.insertTruckFixLogs(onePojo);
		} else if (type.equals("edit")) {
			count = truckBI.updateTruckFixLogs(onePojo);
		}
		JSONObject fretObj = new JSONObject();
		fretObj.put("id", onePojo.getId());
		if (count > 0) {
			fretObj.put("r", 0);
			// 上传图片
			BIFile fileBI = new BIFile(null, m_bs);
			if (m_bs.getFileList().size() > 0) {
				fileBI.deleteFiles(onePojo.getId() + "_FILES", true);
				BFSFilePojo file = new BFSFilePojo();
				file.setUser(onePojo.getUser());
				file.setInstId("");
				file.setOldId("");
				file.setBissId(onePojo.getId() + "_FILES");
				file.setType(1);
				file.setUpdateDate(m_bs.getDateEx().getThisDate(0, 0));
				file.setEditDate(file.getEditDate());
				file.setFileUrl("/fixlogs/" + onePojo.getId());
				file.setFilePath(file.getFileUrl());
				fileBI.saveUploadUserFile(m_bs, file, BIFile.F_FIXLOGS,
						m_bs.getFileList());
			}
		} else {
			fretObj.put("r", 990);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	private JSONArray _getChildOrgList(BIUser userBI, JSONObject paras,
			JSONObject truckParas, BITruck truckBI) throws Exception {
		JSONArray orgs = new JSONArray();
		ArrayList<TruckPojo> truckList = new ArrayList<TruckPojo>();
		ArrayList<OrgPojo> list = userBI.getOrgList(paras, 0, 100);
		for (OrgPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			// oneObj.put("code", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("cnum", onePojo.getSubOrgNum());
			// 得到车辆
			truckParas.put("onlygroup", onePojo.getId());
			truckList = truckBI.getTruckList(truckParas, 0, 500);
			JSONArray trL = new JSONArray();
			for (TruckPojo oneTruck : truckList) {
				JSONObject oneT = new JSONObject();
				oneT.put("id", oneTruck.getId());
				oneT.put("name", oneTruck.getPlateNum());
				trL.add(oneT);
			}
			oneObj.put("struck", trL.size());
			oneObj.put("trucks", trL);
			oneObj.put("snum", onePojo.getUserNum());
			if (onePojo.getSubOrgNum() > 0) {
				paras.put("root", "");
				paras.put("sub", onePojo.getId());
				oneObj.put("children", this._getChildOrgList(userBI, paras,
						truckParas, truckBI));
			}
			orgs.add(oneObj);
		}
		return orgs;
	}
}
