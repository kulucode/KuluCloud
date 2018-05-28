package tt.kulu.out.base;

import java.util.ArrayList;

import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.function.pojo.MenuPojo;
import tt.kulu.out.call.BIMenu;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSFunc
 * </p>
 * <p>
 * 功能描述: 功能Web接口类
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

public class BSFunc {

	/**
	 * <p>
	 * 方法名：do_searchFuncs
	 * </p>
	 * <p>
	 * 方法描述：得到功能管理初始化信息页面：cdoom/system/func/index.jsp
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchOnlyFuncs(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		JSONArray funcList = new JSONArray();
		String fclass = (String) m_bs.getPrivateMap().get("funclass");
		BIMenu funcBI = new BIMenu(null, m_bs);
		// 得到第一层
		JSONObject _paras = new JSONObject();
		_paras.put("class", fclass);
		ArrayList<MenuPojo> list = funcBI.getMenuList(_paras);
		for (MenuPojo oneMenu : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", oneMenu.getMenuId());
			oneObj.put("name", oneMenu.getMenuName());
			oneObj.put("desc", oneMenu.getMenuDes());
			oneObj.put("class", MenuPojo.CLASS_NAME[oneMenu.getMenuClass()]);
			oneObj.put("style", MenuPojo.STYLE_NAME[oneMenu.getMenuStyle()]);
			oneObj.put("state", MenuPojo.STATE_NAME[oneMenu.getMenuState()]);
			oneObj.put("jsfun", oneMenu.getJsfun());
			oneObj.put("img", oneMenu.getImgName());
			oneObj.put("isOpen", MenuPojo.ISOPEN_NAME[oneMenu.getIsOpen()]);
			oneObj.put("pId", oneMenu.getPmenuId());
			oneObj.put("cnum", oneMenu.getChlMenuNum());
			oneObj.put("url", oneMenu.getToPage());
			oneObj.put("topflag", MenuPojo.TOPFLAG_NAME[oneMenu.getTopFlag()]);
			oneObj.put("seq", oneMenu.getSeq());
			oneObj.put("count", oneMenu.getCount());

			funcList.add(oneObj);
		}
		retJSON.put("r", 0);
		retJSON.put("data", funcList);
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchFuncs
	 * </p>
	 * <p>
	 * 方法描述：得到功能管理初始化信息页面：cdoom/system/func/index.jsp
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchFuncs(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		String style = m_bs.getPrivateMap().get("pg_style");
		JSONArray funcList = new JSONArray();
		BIMenu funcBI = new BIMenu(null, m_bs);
		// 得到第一层
		JSONObject _paras = new JSONObject();
		JSONObject _subparas = new JSONObject();
		_paras.put("root", "true");
		if (style != null) {
			_paras.put("style", style);
			_subparas.put("style", style);
		}
		ArrayList<MenuPojo> list = funcBI.getMenuList(_paras);
		for (MenuPojo oneMenu : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", oneMenu.getMenuId());
			oneObj.put("name", oneMenu.getMenuName());
			oneObj.put("desc", oneMenu.getMenuDes());
			oneObj.put("class", MenuPojo.CLASS_NAME[oneMenu.getMenuClass()]);
			oneObj.put("stylev", oneMenu.getMenuStyle());
			oneObj.put("style", MenuPojo.STYLE_NAME[oneMenu.getMenuStyle()]);
			oneObj.put("state", MenuPojo.STATE_NAME[oneMenu.getMenuState()]);
			oneObj.put("jsfun", oneMenu.getJsfun());
			oneObj.put("img", oneMenu.getImgName());
			oneObj.put("isOpen", MenuPojo.ISOPEN_NAME[oneMenu.getIsOpen()]);
			oneObj.put("pId", oneMenu.getPmenuId());
			oneObj.put("cnum", oneMenu.getChlMenuNum());
			oneObj.put("url", oneMenu.getToPage());
			oneObj.put("topflag", MenuPojo.TOPFLAG_NAME[oneMenu.getTopFlag()]);
			oneObj.put("seq", oneMenu.getSeq());
			oneObj.put("count", oneMenu.getCount());
			// 添加孩子
			if (oneMenu.getChlMenuNum() > 0) {
				_subparas.put("sub", oneMenu.getMenuId());
				ArrayList<MenuPojo> subList = funcBI.getMenuList(_subparas);
				JSONArray subFuncList = new JSONArray();
				for (MenuPojo oneSubMenu : subList) {
					JSONObject oneSubObj = new JSONObject();
					oneSubObj.put("id", oneSubMenu.getMenuId());
					oneSubObj.put("name", oneSubMenu.getMenuName());
					oneSubObj.put("desc", oneSubMenu.getMenuDes());
					oneSubObj.put("class",
							MenuPojo.CLASS_NAME[oneSubMenu.getMenuClass()]);
					oneSubObj.put("style",
							MenuPojo.STYLE_NAME[oneSubMenu.getMenuStyle()]);
					oneSubObj.put("state",
							MenuPojo.STATE_NAME[oneSubMenu.getMenuState()]);
					oneSubObj.put("jsfun", oneSubMenu.getJsfun());
					oneSubObj.put("img", oneSubMenu.getImgName());
					oneSubObj.put("isOpen",
							MenuPojo.ISOPEN_NAME[oneSubMenu.getIsOpen()]);
					oneSubObj.put("pId", oneSubMenu.getPmenuId());
					oneSubObj.put("cnum", oneSubMenu.getChlMenuNum());
					oneSubObj.put("url", oneSubMenu.getToPage());
					oneSubObj.put("topflag",
							MenuPojo.TOPFLAG_NAME[oneSubMenu.getTopFlag()]);
					oneSubObj.put("seq", oneSubMenu.getSeq());
					oneSubObj.put("count", oneSubMenu.getCount());
					subFuncList.add(oneSubObj);
				}
				oneObj.put("sub", subFuncList);
			}
			funcList.add(oneObj);
		}
		retJSON.put("r", 0);
		retJSON.put("data", funcList);
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getOneFun
	 * </p>
	 * <p>
	 * 方法描述：功能页面初始化
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getOneFun(BSObject m_bs) throws Exception {
		String type = (String) m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 0);
		MenuPojo oneMenu = new MenuPojo();
		if (type.equals("new")) {
			/** 新增 */
			oneMenu.setMenuId(BSGuid.getRandomGUID());
		} else if (type.equals("edit")) {
			/** 编辑 */
			String menuId = (String) m_bs.getPrivateMap().get("funid");
			oneMenu = new BIMenu(null, m_bs).getMenuById(menuId);
		}
		fretObj.put("id", oneMenu.getMenuId());
		fretObj.put("name", oneMenu.getMenuName());
		fretObj.put("desc", oneMenu.getMenuDes());
		fretObj.put("fclass", oneMenu.getMenuClass());
		fretObj.put("fclasssel", MenuPojo.CLASS_NAME);
		fretObj.put("style", oneMenu.getMenuStyle());
		fretObj.put("styleobj", MenuPojo.STYLE_NAME);
		fretObj.put("state", oneMenu.getMenuState());
		fretObj.put("stateobj", MenuPojo.STATE_NAME);
		fretObj.put("jsfun", oneMenu.getJsfun());
		fretObj.put("img", oneMenu.getImgName());
		fretObj.put("pmenu", oneMenu.getPmenuId());
		fretObj.put("pmenuname", oneMenu.getPmenuName());
		fretObj.put("url", oneMenu.getToPage());
		fretObj.put("seq", oneMenu.getSeq());
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateOneFunc
	 * </p>
	 * <p>
	 * 方法描述：更新应用
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateOneFunc(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		String type = (String) m_bs.getPrivateMap().get("in_type");
		MenuPojo oneMenu = this._getFuncFromWebForEdit(m_bs);
		BIMenu menuBI = new BIMenu(null, m_bs);
		int count = 0;
		if (type.equals("new")) {
			count = menuBI.insertMenu(oneMenu);
		} else if (type.equals("edit")) {
			count = menuBI.updateMenu(oneMenu);
		}
		if (count >= 0) {
			fretObj.put("r", 0);
			JSONObject funcObj = new JSONObject();
			funcObj.put("id", oneMenu.getMenuId());
			funcObj.put("name", oneMenu.getMenuName());
			funcObj.put("fclass", oneMenu.getMenuClass());
			fretObj.put("data", funcObj);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateOneFunc
	 * </p>
	 * <p>
	 * 方法描述：更新应用
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updatePFuncs(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 990);
		// 基本信息
		MenuPojo onePojo = new MenuPojo();
		/** 功能id */
		onePojo.setMenuId((String) m_bs.getPrivateMap().get("thisfunid"));
		onePojo.setPmenuId((String) m_bs.getPrivateMap().get("pfunid"));
		BIMenu menuBI = new BIMenu(null, m_bs);
		int count = menuBI.updatePFuncs(onePojo);
		if (count >= 0) {
			fretObj.put("r", 0);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/** 设置编辑页面 */
	private MenuPojo _getFuncFromWebForEdit(BSObject m_bs) throws Exception {
		// 基本信息
		MenuPojo onePojo = new MenuPojo();
		/** 功能id */
		onePojo.setMenuId((String) m_bs.getPrivateMap().get("t_funid"));
		/** 功能名称 */
		onePojo.setMenuName((String) m_bs.getPrivateMap().get("t_funname"));
		/** 功能描述 */
		onePojo.setMenuDes((String) m_bs.getPrivateMap().get("t_fundesc"));

		/** 功能状态 */
		onePojo.setMenuState(Integer.parseInt((String) m_bs.getPrivateMap()
				.get("s_funstate")));

		/** 功能组菜单 */
		onePojo.setMenuClass(Integer.parseInt((String) m_bs.getPrivateMap()
				.get("s_funclass")));

		/** 功能类型 */
		onePojo.setMenuStyle(Integer.parseInt((String) m_bs.getPrivateMap()
				.get("s_funstyle")));

		/** 功能JS方法 */
		if (m_bs.getPrivateMap().get("t_funjs") != null) {
			onePojo.setJsfun((String) m_bs.getPrivateMap().get("t_funjs"));
		}
		/** 功能图标路径 */
		if (m_bs.getPrivateMap().get("t_funicon") != null) {
			onePojo.setImgName((String) m_bs.getPrivateMap().get("t_funicon"));
		}
		/** 功能去向页面 */
		if (m_bs.getPrivateMap().get("t_funpage") != null) {
			onePojo.setToPage((String) m_bs.getPrivateMap().get("t_funpage"));
		}
		/** 功能seq序列 */
		onePojo.setSeq(Integer.parseInt((String) m_bs.getPrivateMap().get(
				"t_funseq")));
		/** 上级菜单ID */
		if (m_bs.getPrivateMap().get("t_pfunid") != null) {
			onePojo.setPmenuId((String) m_bs.getPrivateMap().get("t_pfunid"));
			if (onePojo.getPmenuId().equals("root")
					|| onePojo.getPmenuId().equals("plateroot")
					|| onePojo.getPmenuId().equals("webroot")) {
				onePojo.setPmenuId("");
			}
		}
		return onePojo;
	}

}
