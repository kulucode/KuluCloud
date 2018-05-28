package tt.kulu.out.base;

import java.util.ArrayList;
import java.util.HashMap;

import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.function.pojo.MenuPojo;
import tt.kulu.bi.power.pojo.RoleMenuPojo;
import tt.kulu.bi.power.pojo.RolePojo;
import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.out.call.BILogin;
import tt.kulu.out.call.BIMenu;
import tt.kulu.out.call.BIPower;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.m.BSObject;
import com.tt4j2ee.m.BSReturnPojo;

/**
 * <p>
 * 标题: BSRole
 * </p>
 * <p>
 * 功能描述: 角色Web接口类
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

public class BSRole {
	/**
	 * <p>
	 * 方法名：do_searchRoleList
	 * </p>
	 * <p>
	 * 方法描述：得到角色管理初始化信息页面：www/admin/role/index.html
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_searchRoleList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String sText = m_bs.getPrivateMap().get("pg_text");
		String state = m_bs.getPrivateMap().get("pg_state");
		BIPower powerBI = new BIPower(null, m_bs);
		JSONObject _paras = new JSONObject();
		_paras.put("key", sText);
		if (state != null) {
			_paras.put("state", state);
		}
		ArrayList<RolePojo> list = powerBI.getRoleList(_paras);
		for (RolePojo oneRole : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", oneRole.getId());
			oneObj.put("name", oneRole.getName());
			oneObj.put("desc", oneRole.getDesc());
			oneObj.put("state", RolePojo.STATE_NAME[oneRole.getState()]);
			oneObj.put("statev", oneRole.getState());
			retObj.add(oneObj);
		}
		fretObj.put("data", retObj);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getUserRoleList
	 * </p>
	 * <p>
	 * 方法描述：得到用户角色关系：www/admin/role/index.html
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getUserRoleList(BSObject m_bs) throws Exception {
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		String inUserStr = (String) m_bs.getPrivateMap().get("in_roleuser");
		String forTea = (String) m_bs.getPrivateMap().get("fortea");
		BIPower powerBI = new BIPower(null, m_bs);
		JSONObject _paras = new JSONObject();
		// 得到用户角色，转化为Map
		_paras.put("user", inUserStr);
		if (forTea != null) {
			_paras.put("fortea", "1");
		}
		_paras.put("state", "1");
		ArrayList<RolePojo> rolelist = powerBI.getRoleList(_paras);
		HashMap<String, String> userRoleMap = new HashMap<String, String>();
		for (RolePojo oneRole : rolelist) {
			userRoleMap.put(oneRole.getId(), oneRole.getId());
		}
		// 得到所有的角色
		_paras.remove("user");
		rolelist = powerBI.getRoleList(_paras);
		for (RolePojo oneRole : rolelist) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", oneRole.getId());
			oneObj.put("name", oneRole.getName());
			oneObj.put("desc", oneRole.getDesc());
			oneObj.put("state", RolePojo.STATE_NAME[oneRole.getState()]);
			// 角色选择状态
			if (userRoleMap.get(oneRole.getId()) != null
					&& userRoleMap.get(oneRole.getId()).equals(oneRole.getId())) {
				oneObj.put("sel", 1);
			} else {
				oneObj.put("sel", 0);
			}
			retObj.add(oneObj);
		}
		fretObj.put("data", retObj);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateUserRole
	 * </p>
	 * <p>
	 * 方法描述：更新用户角色关系
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateUserRole(BSObject m_bs) throws Exception {
		LoginUserPojo user = BILogin.getLoginUser(m_bs);
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", -999);
		String inRolesStr = (String) m_bs.getPrivateMap().get("t_roleids");
		String inUserStr = (String) m_bs.getPrivateMap().get("t_roleuserid");
		String roles[] = inRolesStr.split(",");
		// 调用接口
		BIPower powerBI = new BIPower(null, m_bs);
		BSReturnPojo ret = powerBI.updateUserRoles(inUserStr, roles);
		// 设置返回值
		fretObj.put("r", ret.getErrorNo());
		fretObj.put("error", ret.getErrorString());
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_rolePermIni
	 * </p>
	 * <p>
	 * 方法描述：在角色管理的权限分配对话框中加载对应角色的功能权限
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 * <p>
	 * 
	 * @throws Exception
	 *             </p>
	 */
	public BSObject do_rolePermIni(BSObject m_bs) throws Exception {
		String roleId = (String) m_bs.getPrivateMap().get("roleId");
		JSONObject fretObj = new JSONObject();
		JSONArray retObj = new JSONArray();
		ArrayList<RoleMenuPojo> oneRoleMenuList = new BIPower(null, m_bs)
				.getRoleMenuList(roleId, null);
		for (RoleMenuPojo roleMenu : oneRoleMenuList) {
			retObj.add(roleMenu.getMenu().getMenuId());
			retObj.add(roleMenu.getMenu().getMenuName());
		}
		fretObj.put("menuIdsAndNames", retObj);

		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getRoleById
	 * </p>
	 * <p>
	 * 方法描述：根据角色ID得到一个角色实体：cdoom/system/role/index.jsp
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_getRoleById(BSObject m_bs) throws Exception {
		String type = (String) m_bs.getPrivateMap().get("in_type");
		JSONObject fretObj = new JSONObject();
		RolePojo oneRole = new RolePojo();
		if (type.equals("new")) {
			/** 新增 */
			oneRole.setId(BSGuid.getRandomGUID());
		} else if (type.equals("edit")) {
			/** 编辑 */
			String roleId = (String) m_bs.getPrivateMap().get("roleid");
			oneRole = new BIPower(null, m_bs).getRoleById(roleId);
		}
		fretObj.put("id", oneRole.getId());
		fretObj.put("name", oneRole.getName());
		fretObj.put("desc", oneRole.getDesc());
		fretObj.put("state", oneRole.getState());
		fretObj.put("stateobj", RolePojo.STATE_NAME);
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateRole
	 * </p>
	 * <p>
	 * 方法描述：设置新增、编辑及权限分配页面的提交方法：cdoom/system/role/edit.jsp
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateRole(BSObject m_bs) throws Exception {
		String type = (String) m_bs.getPrivateMap().get("in_type");
		String retStr = "";
		RolePojo oneRole = this._getRoleFromWeb(type, m_bs);
		BIPower powerBI = new BIPower(null, m_bs);
		BSReturnPojo retObj = new BSReturnPojo(-999, "");
		if (type.equals("new")) {
			retObj = powerBI.insertRole(oneRole);
		} else if (type.equals("edit")) {
			retObj = powerBI.updateRole(oneRole);
		}
		JSONObject fretObj = new JSONObject();
		fretObj.put("data", retStr);
		fretObj.put("r", retObj.getErrorNo());
		fretObj.put("error", retObj.getErrorString());
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_deleteRole
	 * </p>
	 * <p>
	 * 方法描述：设置新增、编辑及权限分配页面的提交方法：cdoom/system/role/edit.jsp
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_deleteRole(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 991);
		String roleId = (String) m_bs.getPrivateMap().get("roleid");
		if (new BIPower(null, m_bs).deleteRole(roleId) > 0) {
			fretObj.put("r", 0);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_getRoleFunTree
	 * </p>
	 * <p>
	 * 方法描述：得到角色功能
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 * <p>
	 * 
	 * @throws Exception
	 *             </p>
	 */
	public BSObject do_getRoleFunTree(BSObject m_bs) throws Exception {
		JSONObject retJSON = new JSONObject();
		String roleId = (String) m_bs.getPrivateMap().get("roleid");
		String style = (String) m_bs.getPrivateMap().get("pg_style");
		new JSONArray();
		// 菜单
		BIPower powerBI = new BIPower(null, m_bs);
		HashMap<String, RoleMenuPojo> roleFunMap = powerBI
				.getRoleMenuMap(roleId);
		BIMenu menuBI = new BIMenu(null, m_bs);
		// 递归得到功能树
		JSONArray menuList = this._setTreeNodes(m_bs, menuBI, "", style,
				roleFunMap);

		retJSON.put("data", menuList);
		retJSON.put("r", 0);
		retJSON.put("error", URLlImplBase.ErrorMap.get(retJSON.getInt("r")));
		m_bs.setRetrunObj(retJSON);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateRoleFunc
	 * </p>
	 * <p>
	 * 方法描述：角色赋权
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateRoleFunc(BSObject m_bs) throws Exception {
		String type = (String) m_bs.getPrivateMap().get("in_type");
		String roleId = (String) m_bs.getPrivateMap().get("roleid");
		String funcId = (String) m_bs.getPrivateMap().get("funcid");
		JSONObject fretObj = new JSONObject();
		// 设置关系对象
		RoleMenuPojo oneRoleFunc = new RoleMenuPojo(roleId, funcId,
				Integer.parseInt(type));
		BIPower powerBI = new BIPower(null, m_bs);
		BSReturnPojo ret = powerBI.updateOneRoleFunc(oneRoleFunc);
		// 返回到页面
		fretObj.put("r", ret.getErrorNo());
		fretObj.put("type", type);
		fretObj.put("error", ret.getErrorString());
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_updateFunHome
	 * </p>
	 * <p>
	 * 方法描述：设置首页
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public BSObject do_updateFunHome(BSObject m_bs) throws Exception {
		String roleId = (String) m_bs.getPrivateMap().get("roleid");
		String funcId = (String) m_bs.getPrivateMap().get("funcid");
		JSONObject fretObj = new JSONObject();
		// 设置关系对象
		RoleMenuPojo oneRoleFunc = new RoleMenuPojo(roleId, funcId, 0);
		BIPower powerBI = new BIPower(null, m_bs);
		if (powerBI.updateFunHome(oneRoleFunc) > 0) {
			fretObj.put("r", 0);
		} else {
			fretObj.put("r", 999);
		}
		// 返回到页面

		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/* 得到下级树 (html静态页面) */
	private JSONArray _setTreeNodes(BSObject m_bs, BIMenu menuBI, String pMenu,
			String style, HashMap<String, RoleMenuPojo> roleFunMap)
			throws Exception {
		JSONObject oneObj = null;
		JSONArray retObj = new JSONArray();
		JSONObject paras = new JSONObject();
		ArrayList<MenuPojo> thisList = new ArrayList<MenuPojo>();
		if (style != null) {
			paras.put("style", style);
		}
		if (pMenu == null || pMenu.equals("")) {
			// 根
			paras.put("root", "root");
			thisList = menuBI.getMenuList(paras);
		} else {
			// 下级
			paras.put("sub", pMenu);
			thisList = menuBI.getMenuList(paras);
		}
		// 处理本级
		RoleMenuPojo ontRoleMenu = null;
		for (MenuPojo oneMenu : thisList) {
			oneObj = new JSONObject();
			oneObj.put("id", oneMenu.getMenuId());
			oneObj.put("text", oneMenu.getMenuName());
			oneObj.put("title", oneMenu.getMenuDes());
			oneObj.put("value", oneMenu.getMenuName());
			oneObj.put("mclass", oneMenu.getMenuClass());
			oneObj.put("ismain", 0);
			// 是否选中
			ontRoleMenu = roleFunMap.get(oneMenu.getMenuId());
			if (ontRoleMenu != null) {
				oneObj.put("sel", 1);
				if (ontRoleMenu.getMainFlg() == 1) {
					oneObj.put("ismain", 1);
				}
			} else {
				oneObj.put("sel", 0);
			}
			// 下级
			if (oneMenu.getCount() > 0) {
				oneObj.put("sub", this._setTreeNodes(m_bs, menuBI,
						oneMenu.getMenuId(), style, roleFunMap));
			}
			retObj.add(oneObj);
		}

		return retObj;
	}

	/** 从前端页面得到元素设置一个角色实体 */
	private RolePojo _getRoleFromWeb(String type, BSObject m_bs)
			throws Exception {
		RolePojo onePojo = new RolePojo();
		onePojo.setId((String) m_bs.getPrivateMap().get("t_roleid"));
		/** 角色名称 */
		onePojo.setName((String) m_bs.getPrivateMap().get("t_rolename"));

		/** 角色描述 */
		onePojo.setDesc((String) m_bs.getPrivateMap().get("t_roledesc"));

		/** 角色状态 */
		onePojo.setState(Integer.parseInt(m_bs.getPrivateMap().get(
				"t_rolestate")));
		return onePojo;
	}
}