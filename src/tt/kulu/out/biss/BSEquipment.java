package tt.kulu.out.biss;

import java.util.ArrayList;
import java.util.Calendar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.storage.pojo.EquipmentDefPojo;
import tt.kulu.bi.storage.pojo.EquipmentGeometryPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.out.call.BICompany;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BIEquipment;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSEquipment
 * </p>
 * <p>
 * 功能描述: 设备管理Web接口类
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
public class BSEquipment {

	/**
	 * <p>
	 * 方法名：do_EqpDefineIni
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
	public BSObject do_EqpDefineIni(BSObject m_bs) throws Exception {
		// 数据准备
		JSONObject fretObj = new JSONObject();
		BIDic dicBI = new BIDic(m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("dic", "EQUIPMENT_DEFTYPE");
		JSONArray typeList = new JSONArray();
		ArrayList<DicItemPojo> list = dicBI.getDicItemList(_paras);
		for (DicItemPojo oneDI : list) {
			JSONObject oneType = new JSONObject();
			oneType.put("id", oneDI.getId());
			oneType.put("name", oneDI.getName());
			oneType.put("value", oneDI.getValue());
			typeList.add(oneType);
		}
		fretObj.put("type", typeList);

		// 当前运营商
		CompanyPojo onePojo = (new BICompany(null)).getThisCompanyByRedis();
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

		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchEqpList
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
	public BSObject do_searchEqpDefineList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");
		String type = m_bs.getPrivateMap().get("pg_type");
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (type != null) {
			_paras.put("type", type);
		}
		ArrayList<EquipmentDefPojo> list = eqpBI.getEquipmentDefList(_paras);
		for (EquipmentDefPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("type", onePojo.getEqpType().getName());
			oneObj.put("style", EquipmentDefPojo.STYLE_NAME[onePojo.getStyle()]);
			oneObj.put("brand", onePojo.getBrand());
			oneObj.put("company", onePojo.getManufacturer());
			oneObj.put("no", onePojo.getNo());
			oneObj.put("para1", onePojo.getPara1());
			oneObj.put("para2", onePojo.getPara2());
			oneObj.put("para3", onePojo.getPara3());
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
	 * 方法名：do_getEqpDefineById
	 * </p>
	 * <p>
	 * 方法描述：根据设备定义ID得到一个设备定义
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getEqpDefineById(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		EquipmentDefPojo onePojo = new EquipmentDefPojo();
		if (type.equals("new")) {
			/** 新增 */
			onePojo.setId(BSGuid.getRandomGUID());
		} else if (type.equals("edit")) {
			/** 编辑 */
			onePojo = new BIEquipment(m_bs).getOneEquipmentDefById(m_bs
					.getPrivateMap().get("defid"));
		}
		fretObj.put("id", onePojo.getId());
		fretObj.put("name", onePojo.getName());
		fretObj.put("type", onePojo.getEqpType().getId());
		fretObj.put("style", onePojo.getStyle());
		fretObj.put("brand", onePojo.getBrand());
		fretObj.put("company", onePojo.getManufacturer());
		fretObj.put("no", onePojo.getNo());
		fretObj.put("para1", onePojo.getPara1());
		fretObj.put("para2", onePojo.getPara2());
		fretObj.put("para3", onePojo.getPara3());
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateEqpDefine
	 * </p>
	 * <p>
	 * 方法描述：编辑设备定义
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateEqpDefine(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		EquipmentDefPojo onePojo = new EquipmentDefPojo();
		onePojo.setId(m_bs.getPrivateMap().get("t_defid"));
		onePojo.setName(m_bs.getPrivateMap().get("t_defname"));
		onePojo.setBrand(m_bs.getPrivateMap().get("t_defbrand"));
		onePojo.setManufacturer(m_bs.getPrivateMap().get("t_defcomp"));
		onePojo.getEqpType().setId(m_bs.getPrivateMap().get("s_deftype"));
		onePojo.setNo(m_bs.getPrivateMap().get("t_defno"));
		if (m_bs.getPrivateMap().get("t_defpara1") != null) {
			onePojo.setPara1(m_bs.getPrivateMap().get("t_defpara1"));
		}
		if (m_bs.getPrivateMap().get("t_defpara2") != null) {
			onePojo.setPara2(m_bs.getPrivateMap().get("t_defpara2"));
		}
		if (m_bs.getPrivateMap().get("t_defpara3") != null) {
			onePojo.setPara3(m_bs.getPrivateMap().get("t_defpara3"));
		}
		onePojo.setNo(m_bs.getPrivateMap().get("t_defno"));
		onePojo.setStyle(Integer.parseInt(m_bs.getPrivateMap()
				.get("s_defstyle")));
		BIEquipment eqpBI = new BIEquipment(m_bs);
		int count = 0;
		if (type.equals("new")) {
			count = eqpBI.insertEquipmentDef(onePojo);
		} else if (type.equals("edit")) {
			count = eqpBI.updateEquipmentDef(onePojo);
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
	 * 方法名：do_searchEqpInstLookUp
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
	public BSObject do_searchEqpInstLookUp(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");

		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		ArrayList<EquipmentInstPojo> list = eqpBI.getEquipmentInstList(_paras,
				0, 50);
		for (EquipmentInstPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("value", onePojo.getInstId());
			oneObj.put("label",
					onePojo.getName() + "[条码:" + onePojo.getQrCode() + "]");
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
	 * 方法名：do_searchEqpInstList
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
	public BSObject do_searchEqpInstList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		String sText = m_bs.getPrivateMap().get("pg_text");
		String group = m_bs.getPrivateMap().get("pg_group");
		String peqpId = m_bs.getPrivateMap().get("pg_peqp");
		String eqpType = m_bs.getPrivateMap().get("pg_eqptye");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
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
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (eqpType != null) {
			_paras.put("eqptype", eqpType);
		}
		if (peqpId != null) {
			_paras.put("peqp", peqpId);
		}
		if (group != null) {
			_paras.put("group", group);
		}
		if (state != null) {
			_paras.put("state", state);
		}

		ArrayList<EquipmentInstPojo> list = eqpBI.getEquipmentInstList(_paras,
				startNum, startNum + pageSize - 1);
		for (EquipmentInstPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getInstId());
			oneObj.put("wycode", onePojo.getWyCode());
			oneObj.put("token", onePojo.getToken());
			oneObj.put("name", onePojo.getName());
			oneObj.put("def", onePojo.getEqpDef().getName());
			oneObj.put("qr", onePojo.getQrCode());
			oneObj.put("phone", onePojo.getPhone());
			oneObj.put("para1", onePojo.getPara1());
			oneObj.put("pdate", onePojo.getProDate());
			oneObj.put("state", onePojo.getState());
			oneObj.put("statename",
					EquipmentInstPojo.STATE_NAME[onePojo.getState()]);
			oneObj.put("online", EquipmentInstPojo.ONLINE_STATE_NAME[onePojo
					.getOnlineState()]);
			oneObj.put("muser", onePojo.getMangUser().getInstId());
			oneObj.put("muserid", onePojo.getMangUser().getId());
			oneObj.put("musername", onePojo.getMangUser().getName());
			oneObj.put("muserphone", onePojo.getMangUser().getmPhone());
			if (onePojo.getOrg() != null) {
				oneObj.put("morg", onePojo.getOrg().getId());
				oneObj.put("morgname", onePojo.getOrg().getName());
			} else {
				oneObj.put("morg", "");
				oneObj.put("morgname", "");
			}
			oneObj.put("mtruck", onePojo.getTruck().getId());
			oneObj.put("mtruckname", onePojo.getTruck().getName());
			oneObj.put("mtruckpnum", onePojo.getTruck().getPlateNum());
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
	 * 方法名：do_getOneEqpInst
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
	public BSObject do_getOneEqpInst(BSObject m_bs) throws Exception {
		JSONObject retObj = new JSONObject();
		String instid = m_bs.getPrivateMap().get("eqpid");
		String type = m_bs.getPrivateMap().get("in_type");
		EquipmentInstPojo onePojo = new EquipmentInstPojo();
		if (type.equals("edit")) {
			BIEquipment eqpBI = new BIEquipment(null, m_bs);
			onePojo = eqpBI.getOneEquipmentInstById(instid);
		}
		// 基本信息
		retObj.put("id", onePojo.getInstId());
		retObj.put("wycode", onePojo.getWyCode());
		retObj.put("token", onePojo.getToken());
		retObj.put("name", onePojo.getName());
		retObj.put("def", onePojo.getEqpDef().getId());
		retObj.put("defpara1", onePojo.getEqpDef().getPara1());
		retObj.put("defpara2", onePojo.getEqpDef().getPara1());
		retObj.put("defpara3", onePojo.getEqpDef().getPara1());
		retObj.put("qr", onePojo.getQrCode());
		retObj.put("phone", onePojo.getPhone());
		retObj.put("para1", onePojo.getPara1());
		retObj.put("pdate", onePojo.getProDate());
		retObj.put("muser", onePojo.getMangUser().getInstId());
		retObj.put("muserid", onePojo.getMangUser().getId());
		retObj.put("musername", onePojo.getMangUser().getName());
		if (onePojo.getOrg() != null) {
			retObj.put("morg", onePojo.getOrg().getId());
			retObj.put("morgname", onePojo.getOrg().getName());
			retObj.put("morgallname",
					onePojo.getOrg().getAllName().replaceAll(",", "-"));
		} else {
			retObj.put("morg", "");
			retObj.put("morgname", "");
			retObj.put("morgallname", "无");
		}
		retObj.put("mtruck", onePojo.getTruck().getId());
		retObj.put("mtruckname", onePojo.getTruck().getName());
		retObj.put("mtruckpnum", onePojo.getTruck().getPlateNum());

		retObj.put("r", 0);
		retObj.put("error", URLlImplBase.ErrorMap.get(retObj.getInt("r")));
		m_bs.setRetrunObj(retObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateEqpInst
	 * </p>
	 * <p>
	 * 方法描述：得到用户管理初始化信息 页面：cdoom/user/index.jsp
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_updateEqpInst(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		String type = m_bs.getPrivateMap().get("in_type");

		EquipmentInstPojo onePojo = this._getEqpInstFromWeb(m_bs);
		int count = 0;
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		onePojo.setUpdateDate(m_bs.getDateEx().getThisDate(0, 0));
		if ("new".equals(type)) {
			count = eqpBI.insertEquipmentInst(onePojo);
		} else if ("edit".equals(type)) {
			count = eqpBI.updateEquipmentInst(onePojo);
		}
		if (count >= 0) {
			fretObj.put("r", 0);
		}
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateEqpRel
	 * </p>
	 * <p>
	 * 方法描述：得到用户管理初始化信息 页面：cdoom/user/index.jsp
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_updateEqpRel(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		String pEqp = m_bs.getPrivateMap().get("t_peqpid");
		String relEqp = m_bs.getPrivateMap().get("t_releqp_v");

		EquipmentInstPojo onePojo = this._getEqpInstFromWeb(m_bs);
		int count = 0;
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		if (eqpBI.updateEquipmentInstRel(pEqp, relEqp) >= 0) {
			fretObj.put("r", 0);
		}
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateEqpRel
	 * </p>
	 * <p>
	 * 方法描述：得到用户管理初始化信息 页面：cdoom/user/index.jsp
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_deleteEqpRel(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		String pEqp = m_bs.getPrivateMap().get("t_peqpid");
		String relEqp = m_bs.getPrivateMap().get("t_releqp");
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		if (eqpBI.deleteEquipmentInstRel(pEqp, relEqp) >= 0) {
			fretObj.put("r", 0);
		}
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getEqpGeometryTraceList
	 * </p>
	 * <p>
	 * 方法描述：搜索用户手环的
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_getEqpGeometryTraceList(BSObject m_bs) throws Exception {
		// 数据准备
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject paras = new JSONObject();
		String eqpId = m_bs.getPrivateMap().get("pg_eqp");
		String sDate = m_bs.getPrivateMap().get("pg_sdate");
		String eDate = m_bs.getPrivateMap().get("pg_edate");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		paras.put("eqpid", eqpId);
		if (sDate == null || sDate.equals("")) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -1);
			sDate = m_bs.getDateEx().getCalendarToStringAll(date);
		}
		if (eDate == null || eDate.equals("")) {
			eDate = m_bs.getDateEx().getCalendarToStringAll(
					Calendar.getInstance());
		}
		paras.put("date", sDate + "," + eDate);

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
			pageSize = 500;
		}
		startNum = pageSize * pageNum;
		BIEquipment eqpBI = new BIEquipment(null, m_bs);
		ArrayList<EquipmentGeometryPojo> list = eqpBI.getEqpGeometryList(paras,
				startNum, startNum + pageSize - 1);
		for (EquipmentGeometryPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("eqpid", onePojo.getEqpInst().getInstId());
			oneObj.put("eqptype", onePojo.getEqpInst().getEqpDef().getName());
			oneObj.put("userinst", onePojo.getEqpInst().getMangUser()
					.getInstId());
			oneObj.put("userid", onePojo.getEqpInst().getMangUser().getId());
			oneObj.put("username", onePojo.getEqpInst().getMangUser().getName());
			oneObj.put("lat", onePojo.getLatitude());
			oneObj.put("lon", onePojo.getLongitude());
			oneObj.put("cdate", onePojo.getCreateDate());
			retObj.add(oneObj);
		}
		fretObj.put("list", retObj);
		// fretObj.put("max", paras.getLong("max"));
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	// 设置编辑页面
	private EquipmentInstPojo _getEqpInstFromWeb(BSObject m_bs)
			throws Exception {
		// 基本信息
		EquipmentInstPojo onePojo = new EquipmentInstPojo();
		onePojo.setInstId(m_bs.getPrivateMap().get("t_eqpid"));
		onePojo.setWyCode(m_bs.getPrivateMap().get("t_eqpwycode"));
		onePojo.setToken(m_bs.getPrivateMap().get("t_eqptoken"));
		onePojo.setName(m_bs.getPrivateMap().get("t_eqpname"));
		onePojo.setQrCode(m_bs.getPrivateMap().get("t_eqpqr"));
		onePojo.getEqpDef().setId(m_bs.getPrivateMap().get("s_eqpdef"));
		onePojo.setPhone(m_bs.getPrivateMap().get("t_eqpphone"));
		onePojo.setProDate(m_bs.getPrivateMap().get("t_eqppdate"));
		onePojo.getMangUser().setInstId(
				m_bs.getPrivateMap().get("t_eqpmuser_v"));
		onePojo.getOrg().setId(m_bs.getPrivateMap().get("t_eqpmorg_v"));
		if (m_bs.getPrivateMap().get("t_truck_v") != null) {
			onePojo.getTruck().setId(m_bs.getPrivateMap().get("t_truck_v"));
		}
		if (m_bs.getPrivateMap().get("t_eqppara1") != null) {
			onePojo.setPara1(m_bs.getPrivateMap().get("t_eqppara1"));
		}

		return onePojo;
	}
}
