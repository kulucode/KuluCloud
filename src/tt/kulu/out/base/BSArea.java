package tt.kulu.out.base;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.area.pojo.AreaPojo;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.fance.pojo.FancePojo;
import tt.kulu.out.call.BIArea;
import tt.kulu.out.call.BIDic;

import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSArea
 * </p>
 * <p>
 * 功能描述: 行政区域Web接口类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2014-10-23
 * </p>
 */
public class BSArea {
	/**
	 * <p>
	 * 方法名：do_AreaIni
	 * </p>
	 * <p>
	 * 方法描述：
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_AreaIni(BSObject m_bs) throws Exception {
		// 数据准备
		JSONObject fretObj = new JSONObject();
		BIDic dicBI = new BIDic(m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("dic", "AREA_CLASS");
		fretObj.put("areaclass", dicBI.getDicItemList(_paras));

		_paras.put("dic", "AREA_TYPE");
		fretObj.put("areatype", dicBI.getDicItemList(_paras));

		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchAreaLookUp
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
	public BSObject do_searchAreaLookUp(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");

		BIArea areaBI = new BIArea(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		ArrayList<AreaPojo> list = areaBI.getAreaList(_paras, 0, 50);
		for (AreaPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("value", onePojo.getId());
			oneObj.put("label", onePojo.getAllName().replaceAll(",", "-"));
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
	 * 方法名：do_searchAreaList
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
	public BSObject do_searchAreaList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		String sText = m_bs.getPrivateMap().get("pg_text");
		String areaType = m_bs.getPrivateMap().get("pg_type");
		String areaClass = m_bs.getPrivateMap().get("pg_class");
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
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
		BIArea areaBI = new BIArea(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (areaType != null) {
			_paras.put("type", areaType);
		}
		if (areaClass != null) {
			_paras.put("class", areaClass);
		}

		ArrayList<AreaPojo> list = areaBI.getAreaList(_paras, startNum,
				startNum + pageSize - 1);
		for (AreaPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("sname", onePojo.getShortName());
			oneObj.put("areatype", onePojo.getAreaType().getName());
			oneObj.put("areaclass", onePojo.getAreaClass().getName());
			oneObj.put("allname", onePojo.getAllName().replaceAll(",", "-"));
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
	 * 方法名：do_getOneAreaById
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
	public BSObject do_getOneAreaById(BSObject m_bs) throws Exception {
		JSONObject retObj = new JSONObject();
		String areaId = m_bs.getPrivateMap().get("areaid");
		String type = m_bs.getPrivateMap().get("in_type");
		AreaPojo onePojo = new AreaPojo();
		if (type.equals("edit")) {
			BIArea areaBI = new BIArea(null, m_bs);
			onePojo = areaBI.getOneAreaById(areaId);
		}
		// 基本信息
		retObj.put("id", onePojo.getId());
		retObj.put("name", onePojo.getName());
		retObj.put("sname", onePojo.getShortName());
		retObj.put("pid", onePojo.getpId());
		retObj.put("pname", onePojo.getpName().replaceAll(",", "-"));
		retObj.put("areatype", onePojo.getAreaType().getId());
		retObj.put("areaclass", onePojo.getAreaClass().getId());
		retObj.put("allname", onePojo.getAllName().replaceAll(",", "-"));

		retObj.put("r", 0);
		retObj.put("error", URLlImplBase.ErrorMap.get(retObj.getInt("r")));
		m_bs.setRetrunObj(retObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateUser
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

	public BSObject do_updateArea(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		String type = m_bs.getPrivateMap().get("in_type");

		AreaPojo onePojo = new AreaPojo();
		onePojo.setId(m_bs.getPrivateMap().get("t_areaid"));
		onePojo.setName(m_bs.getPrivateMap().get("t_areaname"));
		onePojo.setShortName(m_bs.getPrivateMap().get("t_areasname"));
		onePojo.getAreaClass().setId(m_bs.getPrivateMap().get("s_areaclass"));
		onePojo.getAreaType().setId(m_bs.getPrivateMap().get("s_areatype"));
		onePojo.setpId(m_bs.getPrivateMap().get("t_areapid_v"));
		int count = 0;
		BIArea areaBI = new BIArea(null, m_bs);
		if ("new".equals(type)) {
			count = areaBI.insertArea(onePojo);
		} else if ("edit".equals(type)) {
			count = areaBI.updateArea(onePojo);
		}
		if (count >= 0) {
			fretObj.put("r", 0);
		}
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}
}
