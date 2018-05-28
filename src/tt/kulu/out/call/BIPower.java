package tt.kulu.out.call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.power.dbclass.BSPowerDBMang;
import tt.kulu.bi.power.pojo.RoleMenuPojo;
import tt.kulu.bi.power.pojo.RolePojo;
import tt.kulu.bi.power.pojo.RoleUserPojo;
import tt.kulu.bi.user.dbclass.BSUserDBMang;
import tt.kulu.bi.user.pojo.UserPojo;
import net.sf.json.JSONObject;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;
import com.tt4j2ee.m.BSReturnPojo;

/**
 * <p>
 * 标题: BIRole
 * </p>
 * <p>
 * 功能描述: 角色接口类
 * </p>
 * <p>
 * 作者: 马维
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2014-10-23
 * </p>
 */
public class BIPower extends BSDBBase {
	public BIPower(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getRoleList
	 * </p>
	 * <p>
	 * 方法功能描述:得到角色列表，带条件
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<RolePojo> getRoleList(JSONObject paras) throws Exception {
		ArrayList<RolePojo> list = new ArrayList<RolePojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getRoleList(sqlHelper, paras);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getRoleList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量角色
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<RolePojo> getRoleList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		// 参数设定
		Iterator<String> keys = paras.keys();
		String where = "";
		String key = "";
		String orderBy = " t.ROLE_NAME";
		List<Object> vList = new ArrayList<Object>();
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("user")) {
					// 用户
					where += " and t.ROLE_ID in (select v.ROLE_ID from T_ROLE_USER_R v where v.USER_INSTID=?)";
					vList.add(v);
				}
				if (key.equals("key")) {
					// 关键字
					where += " and t.ROLE_ID=? and (t.ROLE_NAME like ? or t.ROLE_DESC like ?)";
					vList.add(v);
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("state")) {
					// 根据类别
					where += " and t.ROLE_STATE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("fortea")) {
					// 根据类别
					where += " and t.ROLE_ID not in ('DATA_ADMIN','WD_STUDENT')";
				}
			}
		}
		// 设置角色列表
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		ArrayList<RolePojo> list = roleDB.getRoles(where, orderBy, vList);
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getRoleById
	 * </p>
	 * <p>
	 * 方法功能描述:得到单个角色
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public RolePojo getRoleById(String roleId) throws Exception {
		SqlExecute sqlHelper = new SqlExecute();
		RolePojo oneRole = null;
		try {
			oneRole = this.getRoleById(sqlHelper, roleId);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return oneRole;
	}

	/**
	 * <p>
	 * 方法名称: getRoleById
	 * </p>
	 * <p>
	 * 方法功能描述:得到单个角色
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public RolePojo getRoleById(SqlExecute sqlHelper, String roleId)
			throws Exception {
		RolePojo oneRole = new BSPowerDBMang(sqlHelper, m_bs)
				.getOneRole(roleId);

		return oneRole;
	}

	/**
	 * <p>
	 * 方法名称: getRoleById
	 * </p>
	 * <p>
	 * 方法功能描述:得到单个角色
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public RolePojo getRoleByName(String roleName) throws Exception {
		SqlExecute sqlHelper = new SqlExecute();
		RolePojo oneRole = null;
		try {
			oneRole = this.getRoleByName(sqlHelper, roleName);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return oneRole;
	}

	/**
	 * <p>
	 * 方法名称: getRoleById
	 * </p>
	 * <p>
	 * 方法功能描述:得到单个角色
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public RolePojo getRoleByName(SqlExecute sqlHelper, String roleName)
			throws Exception {
		RolePojo oneRole = new BSPowerDBMang(sqlHelper, m_bs)
				.getOneRole("and t.ROLE_NAME='" + roleName + "'");

		return oneRole;
	}

	/**
	 * <p>
	 * 方法名称: insertIntoRole
	 * </p>
	 * <p>
	 * 方法功能描述: 插入角色数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertRole(SqlExecute sqlHelper, RolePojo oneRole)
			throws Exception {
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		return roleDB.insertRole(oneRole);
	}

	/**
	 * <p>
	 * 方法名称: insertIntoRole
	 * </p>
	 * <p>
	 * 方法功能描述: 插入角色数据。
	 * </p>
	 * <p>
	 * 输入参数描述:角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public BSReturnPojo insertRole(RolePojo oneRole) throws Exception {
		BSReturnPojo retObj = new BSReturnPojo(-999, "");
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			if (this.insertRole(sqlHelper, oneRole) > 0) {
				retObj.setErrorNo(0);
			}
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			retObj.setErrorString(ep.getMessage());
			ep.printStackTrace();
			throw ep;
		}
		return retObj;
	}

	/**
	 * <p>
	 * 方法名称: updateUserRoles
	 * </p>
	 * <p>
	 * 方法功能描述: 更新用户角色数据。
	 * </p>
	 * <p>
	 * 输入参数描述:角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public BSReturnPojo updateUserRoles(String userInstId, String[] roles)
			throws Exception {
		BSReturnPojo ret = new BSReturnPojo(-999, "");
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
			BSUserDBMang userDB = new BSUserDBMang(sqlHelper, m_bs);
			// 删除现有角色关系
			List<Object> vList = new ArrayList<Object>();
			vList.add(userInstId);
			roleDB.deleteUserRole(" and t.USER_INSTID=?", vList);
			// 添加新的角色关系
			RoleUserPojo oneUserRole = new RoleUserPojo(userInstId, "");
			String roleWhere = "";
			for (String oneRole : roles) {
				if (!oneRole.equals("")) {
					roleWhere += (",'" + oneRole + "'");
					oneUserRole.getRole().setId(oneRole);
					roleDB.insertUserRole(oneUserRole);
				}
			}
			if (roleWhere.length() > 0) {
				roleWhere = roleWhere.substring(1);
			}
			UserPojo oneUser = userDB.getOneUserByInstId(userInstId);
			if (oneUser != null) {
				BIRedis redisBI = new BIRedis();
				oneUser.setRoleWhere(roleWhere);
				redisBI.setMapData("USERMAP", oneUser.getId(), JSONObject
						.fromObject(oneUser).toString(), 0);
			}

			sqlHelper.commit();
			ret.setErrorNo(0);
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			ret.setErrorString("更新用户角色失败：" + ep.getMessage());
			throw ep;
		}

		return ret;
	}

	/**
	 * <p>
	 * 方法名称: getRoleMenuMap
	 * </p>
	 * <p>
	 * 方法功能描述: 得到角色功能map。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public HashMap<String, RoleMenuPojo> getRoleMenuMap(String roleId)
			throws Exception {
		HashMap<String, RoleMenuPojo> maps = new HashMap<String, RoleMenuPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
			String where = " and t.ROLE_ID in (";
			String[] vs = roleId.split(",");
			String whereEx = "";
			List<Object> vList = new ArrayList<Object>();
			for (String oneV : vs) {
				if (!oneV.equals("")) {
					whereEx += ",?";
					vList.add(oneV);
				}
			}
			where += ((whereEx.equals("") ? "" : whereEx.substring(1)) + ")");
			ArrayList<RoleMenuPojo> list = roleDB
					.getMenuPowerList(where, vList);
			for (RoleMenuPojo onePojo : list) {
				maps.put(onePojo.getMenu().getMenuId(), onePojo);
			}
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}

		return maps;
	}

	/**
	 * <p>
	 * 方法名称: insertRoleMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 插入角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertRoleMenu(SqlExecute sqlHelper, RoleMenuPojo oneRoleFunc)
			throws Exception {
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		return roleDB.insertRoleMenu(oneRoleFunc);
	}

	/**
	 * <p>
	 * 方法名称: insertRoleMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 插入角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertRoleMenu(RoleMenuPojo oneRole) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertRoleMenu(sqlHelper, oneRole);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateRoleFunc
	 * </p>
	 * <p>
	 * 方法功能描述: 插入角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public BSReturnPojo updateOneRoleFunc(RoleMenuPojo onePojo)
			throws Exception {
		BSReturnPojo ret = new BSReturnPojo(-999, "未更新");
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getRole().getId());
			vList.add(onePojo.getMenu().getMenuId());
			if (roleDB
					.deleteRoleFunc(" and t.ROLE_ID=? and t.MENU_ID=?", vList) >= 0) {
				if (onePojo.getType() == 1) {
					if (roleDB.insertRoleMenu(onePojo) > 0) {
						ret.setErrorNo(0);
					}
				} else {
					ret.setErrorNo(0);
				}

			}
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ret.setRetObj(ep.getMessage());
			ep.printStackTrace();
			throw ep;
		}
		return ret;
	}

	/**
	 * <p>
	 * 方法名称: deleteRoleMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 插入角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteRoleMenu(SqlExecute sqlHelper, ArrayList<RoleMenuPojo> list)
			throws Exception {
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		int count = 0;
		for (RoleMenuPojo onePojo : list) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getRole().getId());
			vList.add(onePojo.getMenu().getMenuId());
			count = roleDB.deleteRoleFunc(" and t.ROLE_ID=? and t.MENU_ID=?",
					vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertRoleMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 插入角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteRoleMenu(ArrayList<RoleMenuPojo> list) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteRoleMenu(sqlHelper, list);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteRoleUser
	 * </p>
	 * <p>
	 * 方法功能描述: 删除角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteRoleUser(ArrayList<RoleUserPojo> list) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteRoleUser(sqlHelper, list);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteRoleUser
	 * </p>
	 * <p>
	 * 方法功能描述: 删除角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteRoleUser(SqlExecute sqlHelper, ArrayList<RoleUserPojo> list)
			throws Exception {
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		int count = 0;
		for (RoleUserPojo onePojo : list) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getRole().getId());
			vList.add(onePojo.getUser().getInstId());
			count = roleDB.deleteUserRole(
					" and t.ROLE_ID=? and t.USER_INSTID=?", vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteRoleUser
	 * </p>
	 * <p>
	 * 方法功能描述: 删除角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteUserRole(String where, List<Object> vList)
			throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteUserRole(sqlHelper, where, vList);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteRoleUser
	 * </p>
	 * <p>
	 * 方法功能描述: 删除角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteUserRole(SqlExecute sqlHelper, String where,
			List<Object> vList) throws Exception {
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		return roleDB.deleteUserRole(where, vList);
	}

	/**
	 * <p>
	 * 方法名称: insertUserRole
	 * </p>
	 * <p>
	 * 方法功能描述: 插入角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertUserRole(RoleUserPojo oneRole) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertUserRole(sqlHelper, oneRole);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertRoleMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 插入角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertUserRole(SqlExecute sqlHelper, RoleUserPojo oneRoleFunc)
			throws Exception {
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		return roleDB.insertUserRole(oneRoleFunc);
	}

	/**
	 * <p>
	 * 方法名称: getMenuListByRole
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量角色
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<RoleMenuPojo> getRoleMenuList(SqlExecute sqlHelper,
			String roleId, JSONObject paras) throws Exception {
		String where = "";
		String key = "";
		String orderBy = " t.ROLE_NAME desc";
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("sub")) {
					// 下级
					where += " and t.MENU_ID in (select v.MENU_ID from T_MENU_R v where v.P_MENU_ID=?)";
					vList.add(v);
				}
				if (key.equals("user")) {
					// 根据类别
					where += " and t.ROLE_ID in (select v.MENU_ID from T_ROLE_USER_R v where v.USER_INSTID=?)";
					vList.add(v);
				}
				if (key.equals("key")) {
					// 关键字
					where += "  and (t1.MENU_DESC like ? or t1.MENU_NAME like ?)";
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("class")) {
					// 根据类别
					where += " and t1.MENU_CLASS=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("style")) {
					// 根据类别
					where += " and t1.MENU_STYLE=?";
					vList.add(Integer.parseInt(v));
				}
			}
		}
		if (roleId != null && !roleId.equals("")) {
			where = " and t.ROLE_ID in (";
			String[] vs = roleId.split(",");
			String whereEx = "";
			for (String oneV : vs) {
				if (!oneV.equals("")) {
					whereEx += ",?";
					vList.add(oneV);
				}
			}
			where += ((whereEx.equals("") ? "" : whereEx.substring(1)) + ")");
		}
		ArrayList<RoleMenuPojo> list = new ArrayList<RoleMenuPojo>();
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		list = roleDB.getMenuPowerList(where, vList);
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getMenuListByRole
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量角色
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<RoleMenuPojo> getRoleMenuList(String roleId,
			JSONObject paras) throws Exception {
		ArrayList<RoleMenuPojo> list = new ArrayList<RoleMenuPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getRoleMenuList(sqlHelper, roleId, paras);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: updateRole
	 * </p>
	 * <p>
	 * 方法功能描述: 修改角色数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateRole(SqlExecute sqlHelper, RolePojo oneRole)
			throws Exception {
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		return roleDB.updateRole(oneRole);
	}

	/**
	 * <p>
	 * 方法名称: updateRole
	 * </p>
	 * <p>
	 * 方法功能描述: 修改角色数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public BSReturnPojo updateRole(RolePojo oneRole) throws Exception {
		BSReturnPojo retObj = new BSReturnPojo(-999, "");
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			if (this.updateRole(sqlHelper, oneRole) > 0) {
				retObj.setErrorNo(0);
			}
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			retObj.setErrorString(ep.getMessage());
			ep.printStackTrace();
			throw ep;
		}
		return retObj;
	}

	/**
	 * <p>
	 * 方法名称: updateRoleFunc
	 * </p>
	 * <p>
	 * 方法功能描述: 插入角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateFunHome(RoleMenuPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateFunHome(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateFunHome
	 * </p>
	 * <p>
	 * 方法功能描述: 插入角色权限数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateFunHome(SqlExecute sqlHelper, RoleMenuPojo onePojo)
			throws Exception {
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		return roleDB.updateFunHome(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: getRootWinApps
	 * </p>
	 * <p>
	 * 方法功能描述: 得到桌面APP
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<RoleMenuPojo> getRootWinApps(JSONObject paras)
			throws Exception {
		ArrayList<RoleMenuPojo> list = new ArrayList<RoleMenuPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getRootWinApps(sqlHelper, paras);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getRootWinApps
	 * </p>
	 * <p>
	 * 方法功能描述: 得到桌面APP
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<RoleMenuPojo> getRootWinApps(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		// 参数设定
		Iterator<String> keys = paras.keys();
		String where = "";
		String key = "";
		List<Object> vList = new ArrayList<Object>();
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("state")) {
					// 用户
					where += " and t3.MENU_STATE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("class")) {
					// 用户
					where += " and t3.MENU_CLASS=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("style")) {
					// 用户
					where += " and t3.MENU_STYLE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("onerole")) {
					// 用户
					where += " and t.ROLE_ID=?";
					vList.add(v);
				}
				if (key.equals("pmenu")) {
					// 用户
					where += " and t2.P_MENU_ID=?";
					vList.add(v);
				}
				if (key.equals("role")) {
					// 根据类别
					where += " and t.ROLE_ID in (";
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += ",?";
							vList.add(oneV);
						}
					}
					where += ((whereEx.equals("") ? "" : whereEx.substring(1)) + ")");
				}
			}
		}
		// 设置角色列表
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		ArrayList<RoleMenuPojo> rootMenuList = roleDB.getRootMenuRoleList(
				where, vList);
		return rootMenuList;
	}

	/**
	 * <p>
	 * 方法名称: getRootWinApps
	 * </p>
	 * <p>
	 * 方法功能描述: 得到桌面APP
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<RoleMenuPojo> getWinApps(JSONObject paras)
			throws Exception {
		ArrayList<RoleMenuPojo> list = new ArrayList<RoleMenuPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getWinApps(sqlHelper, paras);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getRootWinApps
	 * </p>
	 * <p>
	 * 方法功能描述: 得到桌面APP
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<RoleMenuPojo> getWinApps(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		// 参数设定
		Iterator<String> keys = paras.keys();
		String where = "";
		String key = "";
		List<Object> vList = new ArrayList<Object>();
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("state")) {
					// 用户
					where += " and t3.MENU_STATE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("class")) {
					// 用户
					where += " and t3.MENU_CLASS=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("onerole")) {
					// 用户
					where += " and t.ROLE_ID=?";
					vList.add(v);
				}
				if (key.equals("parent")) {
					// 用户
					where += " and t2.P_MENU_ID=?";
					vList.add(v);
				}
				if (key.equals("role")) {
					// 根据类别
					where += " and t.ROLE_ID in (";
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += ",?";
							vList.add(oneV);
						}
					}
					where += ((whereEx.equals("") ? "" : whereEx.substring(1)) + ")");
				}
			}
		}
		// 设置角色列表
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		ArrayList<RoleMenuPojo> rootMenuList = roleDB.getMenuRoleList(where,
				vList);
		return rootMenuList;
	}

	/**
	 * <p>
	 * 方法名称: updateRole
	 * </p>
	 * <p>
	 * 方法功能描述: 修改角色数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteRole(String roleId) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.deleteRole(sqlHelper, roleId);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteRole
	 * </p>
	 * <p>
	 * 方法功能描述: 修改角色数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、角色实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int deleteRole(SqlExecute sqlHelper, String roleId) throws Exception {
		BSPowerDBMang roleDB = new BSPowerDBMang(sqlHelper, m_bs);
		return roleDB.deleteRole(roleId);
	}

}