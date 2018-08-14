package tt.kulu.out.base;

import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.dic.pojo.DicPojo;
import tt.kulu.bi.logs.biclass.SysLogsBIMang;
import tt.kulu.bi.logs.pojo.SysLogsPojo;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BILogin;
import net.sf.json.JSONObject;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSDic
 * </p>
 * <p>
 * 功能描述: 数据字典Web接口类
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

public class BSDic {
	/**
	 * <p>
	 * 方法名：do_searchDicList
	 * </p>
	 * <p>
	 * 方法描述：得到数据字典列表
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchDicList(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		String sText = (String) m_bs.getPrivateMap().get("pg_text");
		BIDic dicBI = new BIDic(m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		fretObj.put("list", dicBI.getDicList(_paras));
		fretObj.put("r", 0);

		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getDicById
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
	public BSObject do_getDicById(BSObject m_bs) throws Exception {
		String type = (String) m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		DicPojo onePojo = new DicPojo();
		if (type.equals("new")) {
			/** 新增 */
			onePojo.setId(BSGuid.getRandomGUID());
		} else if (type.equals("edit")) {
			/** 编辑 */
			onePojo = new BIDic(m_bs).getOneDicById((String) m_bs
					.getPrivateMap().get("dicid"));
		}
		fretObj.put("id", onePojo.getId());
		fretObj.put("name", onePojo.getName());
		fretObj.put("desc", onePojo.getDesc());
		fretObj.put("pid", onePojo.getPid());
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateDic
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
	public BSObject do_updateDic(BSObject m_bs) throws Exception {
		String type = (String) m_bs.getPrivateMap().get("in_type");
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
		DicPojo onePojo = new DicPojo();
		onePojo.setId((String) m_bs.getPrivateMap().get("t_dicid"));
		onePojo.setName((String) m_bs.getPrivateMap().get("t_dicname"));
		onePojo.setDesc((String) m_bs.getPrivateMap().get("t_dicdesc"));
		onePojo.setPid((String) m_bs.getPrivateMap().get("s_pdic"));
		BIDic dicBI = new BIDic(m_bs);
		int count = 0;
		if (type.equals("new")) {
			count = dicBI.insertDic(onePojo);
			oneLogs.setName("新增数据字典");
		} else if (type.equals("edit")) {
			count = dicBI.updateDic(onePojo);
			oneLogs.setName("编辑数据字典");
		}
		JSONObject fretObj = new JSONObject();
		fretObj.put("id", onePojo.getId());
		if (count > 0) {
			fretObj.put("r", 0);
			// 写日志
			oneLogs.setType(1);
			oneLogs.setContent("操作:" + oneLogs.getName() + "；影响数据字典："
					+ onePojo.getName() + "[" + onePojo.getId() + "]");

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
	 * 方法名：do_searchDicItemList
	 * </p>
	 * <p>
	 * 方法描述：得到数据字典项目列表
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchDicItemList(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		BIDic dicBI = new BIDic(m_bs);
		JSONObject _paras = new JSONObject();
		if (m_bs.getPrivateMap().get("dicid") != null) {
			_paras.put("dic", (String) m_bs.getPrivateMap().get("dicid"));
		}
		if (m_bs.getPrivateMap().get("pitemid") != null) {
			_paras.put("pitemid", (String) m_bs.getPrivateMap().get("pitemid"));
		}
		fretObj.put("list", dicBI.getDicItemList(_paras));
		// 得到联动下拉框
		_paras.clear();
		_paras.put("pdic", (String) m_bs.getPrivateMap().get("dicid"));
		fretObj.put("pitemlist", dicBI.getDicItemList(_paras));
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getDicItemById
	 * </p>
	 * <p>
	 * 方法描述：根据数据字典项目ID得到一个数据字典项目
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getDicItemById(BSObject m_bs) throws Exception {
		String type = (String) m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		DicItemPojo onePojo = new DicItemPojo();
		if (type.equals("new")) {
			/** 新增 */
			onePojo.setId(BSGuid.getRandomGUID());
		} else if (type.equals("edit")) {
			/** 编辑 */
			onePojo = new BIDic(m_bs).getOneDicItemById((String) m_bs
					.getPrivateMap().get("dicitemid"));
		}
		fretObj.put("dicid", onePojo.getDic().getId());
		fretObj.put("id", onePojo.getId());
		fretObj.put("name", onePojo.getName());
		fretObj.put("value", onePojo.getValue());
		fretObj.put("value2", onePojo.getValue2());
		fretObj.put("index", onePojo.getIndex());
		fretObj.put("pitemid", onePojo.getPitemid());
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateDicItem
	 * </p>
	 * <p>
	 * 方法描述：编辑数据字典项目
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateDicItem(BSObject m_bs) throws Exception {
		String type = (String) m_bs.getPrivateMap().get("in_type");
		SysLogsPojo oneLogs = new SysLogsPojo();
		oneLogs.setCreateUser(BILogin.getLoginUser(m_bs));
		DicItemPojo onePojo = new DicItemPojo();
		onePojo.getDic().setId((String) m_bs.getPrivateMap().get("t_dicid"));
		onePojo.setId((String) m_bs.getPrivateMap().get("t_dicitemid"));
		onePojo.setName((String) m_bs.getPrivateMap().get("t_dicitemname"));
		onePojo.setValue((String) m_bs.getPrivateMap().get("t_dicitemvalue"));
		onePojo.setValue2((String) m_bs.getPrivateMap().get("t_dicitemvalue2"));
		onePojo.setIndex(Integer.parseInt((String) m_bs.getPrivateMap().get(
				"t_dicitemindex")));
		onePojo.setPitemid((String) m_bs.getPrivateMap().get("s_pdicitem"));
		BIDic dicBI = new BIDic(m_bs);
		int count = 0;
		if (type.equals("new")) {
			count = dicBI.insertDicItem(onePojo);
			oneLogs.setName("新增数据字典项目");
		} else if (type.equals("edit")) {
			count = dicBI.updateDicItem(onePojo);
			oneLogs.setName("编辑数据字典项目");
		}
		JSONObject fretObj = new JSONObject();
		fretObj.put("id", onePojo.getId());
		if (count > 0) {
			fretObj.put("r", 0);
			// 写日志
			oneLogs.setType(1);
			oneLogs.setContent("操作:" + oneLogs.getName() + "；影响数据字典项目："
					+ onePojo.getName() + "[" + onePojo.getDic().getId() + ","
					+ onePojo.getId() + "]");

			SysLogsBIMang slbi = new SysLogsBIMang(oneLogs, m_bs);
			slbi.start();
		} else {
			fretObj.put("r", 990);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}
}
