package tt.kulu.out.biss;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.dic.pojo.DicPojo;
import tt.kulu.out.call.BICompany;
import tt.kulu.out.call.BIDic;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSCompany
 * </p>
 * <p>
 * 功能描述: 企业Web接口类
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
public class BSCompany {
	/**
	 * <p>
	 * 方法名：do_getThisCompany
	 * </p>
	 * <p>
	 * 方法描述：根据当前运营商
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getThisCompany(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		BICompany compBI = new BICompany(m_bs);
		CompanyPojo onePojo = compBI.getOneCompanyByType(1);
		if (onePojo == null) {
			onePojo = new CompanyPojo();
			onePojo.setId(BSGuid.getRandomGUID());
		}

		fretObj.put("id", onePojo.getId());
		if (onePojo.getArea() != null) {
			fretObj.put("areaid", onePojo.getArea().getId());
			fretObj.put("areaname",
					onePojo.getArea().getAllName().replaceAll(",", "-"));
		} else {
			fretObj.put("areaid", "");
			fretObj.put("areaname", "");
		}
		fretObj.put("lat", onePojo.getLatitude());
		fretObj.put("lon", onePojo.getLongitude());
		fretObj.put("name", onePojo.getName());
		fretObj.put("desc", onePojo.getDesc());
		fretObj.put("linkman", onePojo.getLinkMan());
		fretObj.put("linkphone", onePojo.getLinkPhone());
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateThisCompany
	 * </p>
	 * <p>
	 * 方法描述：更新企业信息
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateThisCompany(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		BICompany compBI = new BICompany(m_bs);
		CompanyPojo onePojo = compBI.getOneCompanyById(m_bs.getPrivateMap()
				.get("t_compid"));
		boolean isNew = false;
		if (onePojo == null) {
			onePojo = new CompanyPojo();
			isNew = true;
			onePojo.setId(m_bs.getPrivateMap().get("t_compid"));
		}
		onePojo.setType(1);
		onePojo.getArea().setId(m_bs.getPrivateMap().get("t_areaid_v"));
		onePojo.setName(m_bs.getPrivateMap().get("t_compname"));
		onePojo.setDesc(m_bs.getPrivateMap().get("t_compdesc"));
		onePojo.setLinkMan(m_bs.getPrivateMap().get("t_complink"));
		onePojo.setLinkPhone(m_bs.getPrivateMap().get("t_compphone"));
		onePojo.setLatitude(m_bs.getPrivateMap().get("t_complat"));
		onePojo.setLongitude(m_bs.getPrivateMap().get("t_complon"));
		int count = 0;
		if (isNew) {
			count += compBI.insertCompany(onePojo);
		} else {
			count += compBI.updateCompany(onePojo);
		}
		if (count > 0) {
			fretObj.put("r", 0);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}
}
