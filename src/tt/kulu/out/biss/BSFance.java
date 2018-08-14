package tt.kulu.out.biss;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.fance.pojo.FancePojo;
import tt.kulu.out.call.BICompany;
import tt.kulu.out.call.BIFance;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSFance
 * </p>
 * <p>
 * 功能描述: 围栏Web接口类
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
public class BSFance {
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
	public BSObject do_FanceIni(BSObject m_bs) throws Exception {
		// 数据准备
		JSONObject fretObj = new JSONObject();
		fretObj.put("type", FancePojo.TYPE_NAME);
		// 运营商信息
		BICompany compBI = new BICompany(m_bs);
		CompanyPojo oneComp = compBI.getThisCompanyByRedis();
		JSONObject compJ = new JSONObject();
		if (oneComp != null) {
			compJ.put("id", oneComp.getId());
			compJ.put("name", oneComp.getName());
			compJ.put("link", oneComp.getLinkMan());
			compJ.put("linkphone", oneComp.getLinkPhone());
			compJ.put("lat", oneComp.getLatitude());
			compJ.put("lon", oneComp.getLongitude());
		}
		fretObj.put("company", compJ);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchFanceList
	 * </p>
	 * <p>
	 * 方法描述：得到围栏信息页面：www/admin/role/index.html
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchFanceList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");
		BIFance fanceBI = new BIFance(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		ArrayList<FancePojo> list = fanceBI.getFanceList(_paras);
		for (FancePojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("org", onePojo.getOrg().getAllName());
			oneObj.put("type", FancePojo.TYPE_NAME[onePojo.getType()]);
			oneObj.put("points", onePojo.getGeo());

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
	 * 方法名：do_getFanceById
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
	public BSObject do_getFanceById(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		FancePojo onePojo = new FancePojo();
		BIFance fanceBI = new BIFance(m_bs);
		if (type.equals("new")) {
			/** 新增 */
			onePojo.setId(BSGuid.getRandomGUID());
		} else if (type.equals("edit")) {
			/** 编辑 */
			onePojo = fanceBI.getOneFanceById((String) m_bs.getPrivateMap()
					.get("fanceid"));
		}
		fretObj.put("id", onePojo.getId());
		fretObj.put("name", onePojo.getName());
		fretObj.put("org", onePojo.getOrg().getId());
		fretObj.put("orgname", onePojo.getOrg().getAllName());
		fretObj.put("type", onePojo.getType());
		fretObj.put("points", onePojo.getGeo());
		if (onePojo.getCenter() != null && onePojo.getCenter()[0] != null
				&& !onePojo.getCenter()[0].equals("")) {
			fretObj.put("cenlon", onePojo.getCenter()[0]);
			fretObj.put("cenlat", onePojo.getCenter()[1]);
		}
		if (type.equals("edit")) {
			JSONObject _paras = new JSONObject();
			_paras.put("fanceid", onePojo.getId());
			if (onePojo.getType() == 0 || onePojo.getType() == 1) {
				// 车
				fretObj.put("trucks", fanceBI.getFanceTruckRList(_paras));
			}
			if (onePojo.getType() == 0 || onePojo.getType() == 2) {
				// 车
				fretObj.put("users", fanceBI.getFanceUserRList(_paras));
			}
		}
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateFance
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
	public BSObject do_updateFance(BSObject m_bs) throws Exception {
		String type = m_bs.getPrivateMap().get("in_type");
		FancePojo onePojo = new FancePojo();
		onePojo.setId(m_bs.getPrivateMap().get("t_fanceid"));
		onePojo.setName(m_bs.getPrivateMap().get("t_fancename"));
		onePojo.setType(Integer.parseInt(m_bs.getPrivateMap()
				.get("s_fancetype")));
		onePojo.setGeo(JSONArray.fromObject(m_bs.getPrivateMap().get(
				"t_fancepoints")));
		onePojo.getArea().setId(m_bs.getPrivateMap().get("t_area"));
		onePojo.setUsers(m_bs.getPrivateMap().get("t_fanceusers"));
		onePojo.setTrucks(m_bs.getPrivateMap().get("t_fancetrucks"));
		BIFance fanceBI = new BIFance(m_bs);
		int count = 0;
		if (type.equals("new")) {
			count = fanceBI.insertFance(onePojo);
		} else if (type.equals("edit")) {
			count = fanceBI.updateFance(onePojo);
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
	 * 方法名：do_deleteFance
	 * </p>
	 * <p>
	 * 方法描述：删除围栏
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_deleteFance(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		String fid = m_bs.getPrivateMap().get("pg_id");
		BIFance fanceBI = new BIFance(m_bs);
		if (fanceBI.deleteFanceById(fid) > 0) {
			fretObj.put("r", 0);
		} else {
			fretObj.put("r", 990);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}
}
