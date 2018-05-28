package tt.kulu.out.call;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.function.dbclass.BSMenuDBMang;
import tt.kulu.bi.function.pojo.MenuPojo;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BILogin
 * </p>
 * <p>
 * 功能描述: 登录类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2011-1-25
 * </p>
 */
public class BIMenu extends BSDBBase {
	public BIMenu(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getMenuList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到活动列表。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输入参数描述: long f：输入的开始位置。
	 * </p>
	 * <p>
	 * 输入参数描述: long t：输入的结束位置。
	 * </p>
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<MenuPojo> getMenuList(JSONObject paras) throws Exception {
		ArrayList<MenuPojo> list = new ArrayList<MenuPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getMenuList(sqlHelper, paras);
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
	 * 方法名称: getActivityList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到活动列表。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输入参数描述: long f：输入的开始位置。
	 * </p>
	 * <p>
	 * 输入参数描述: long t：输入的结束位置。
	 * </p>
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<MenuPojo> getMenuList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		ArrayList<MenuPojo> list = new ArrayList<MenuPojo>();
		Iterator<String> keys = paras.keys();
		String orderBy = " t.SEQ";
		String where = "";
		String key = "";
		List<Object> vList = new ArrayList<Object>();
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("root")) {
					// 根
					where += " and (t2.MENU_ID is null OR t2.MENU_ID='')";
					orderBy = " t.SEQ";
				}
				if (key.equals("user")) {
					// 根
					where += " and t.MENU_ID in (select v2.MENU_ID from T_ROLE_USER_R v1,T_ROLE_MENU_R v2 where v1.ROLE_ID=v2.ROLE_ID and v1.USER_INSTID=?)";
					vList.add(v);
				}
				if (key.equals("sub")) {
					// 根
					where += " and t2.MENU_ID=?";
					orderBy = " t1.SEQ";
					vList.add(v);
				}
				if (key.equals("key")) {
					// 关键字
					where += " and (t.MENU_NAME like ? or t.MENU_DESC like ?)";
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("class")) {
					// 菜单组
					where += " and t.MENU_CLASS=?";
					vList.add(Integer.parseInt(v));
				}

				if (key.equals("style")) {
					where += " and t.MENU_STYLE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("state")) {
					where += " and t.MENU_STATE=?";
					vList.add(Integer.parseInt(v));
				}
			}
		}
		BSMenuDBMang menuDB = new BSMenuDBMang(sqlHelper, m_bs);
		list = menuDB.getMenuList(where, orderBy, vList);
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getMenuById
	 * </p>
	 * <p>
	 * 方法功能描述:得到单个功能
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 功能ID menuId
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public MenuPojo getMenuById(String menuId) throws Exception {
		SqlExecute sqlHelper = null;
		MenuPojo oneMenu = null;
		try {
			sqlHelper = new SqlExecute();
			oneMenu = this.getMenuById(sqlHelper, menuId);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return oneMenu;
	}

	/**
	 * <p>
	 * 方法名称: getMenuById
	 * </p>
	 * <p>
	 * 方法功能描述:得到单个功能
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、功能ID menuId
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public MenuPojo getMenuById(SqlExecute sqlHelper, String menuId)
			throws Exception {
		MenuPojo oneMenu = new BSMenuDBMang(sqlHelper, m_bs)
				.getOneMenuById(menuId);
		return oneMenu;
	}

	/**
	 * <p>
	 * 方法名称: updateMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 修改菜单数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateMenu(SqlExecute sqlHelper, MenuPojo oneMenu)
			throws Exception {
		BSMenuDBMang menuDB = new BSMenuDBMang(sqlHelper, m_bs);
		int count = menuDB.updateMenu(oneMenu);
		// 添加关系
		if (!"".equals(oneMenu.getPmenuId())) {
			// 删除该菜单所有关系
			List<Object> vList = new ArrayList<Object>();
			vList.add(oneMenu.getPmenuId());
			vList.add(oneMenu.getMenuId());
			count += menuDB.deleteMenuRel(" and P_MENU_ID=? and MENU_ID = ?",
					vList);
			// 添加新关系
			count += menuDB.insertMenuRel(oneMenu.getMenuId(),
					oneMenu.getPmenuId(), oneMenu.getSeq());
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 修改菜单数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateMenu(MenuPojo oneMenu) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateMenu(sqlHelper, oneMenu);
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
	 * 方法名称: insertMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 新增菜单数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertMenu(SqlExecute sqlHelper, MenuPojo oneMenu)
			throws Exception {
		BSMenuDBMang menuDB = new BSMenuDBMang(sqlHelper, m_bs);
		int count = menuDB.insertMenu(oneMenu);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 新增菜单数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertMenu(MenuPojo oneMenu) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertMenu(sqlHelper, oneMenu);
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
	 * 方法名称: checkMenuIsMain
	 * </p>
	 * <p>
	 * 方法功能描述:得到单个功能
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 功能ID menuId
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int checkMenuIsMain(String userInstId, String menuId)
			throws Exception {
		SqlExecute sqlHelper = null;
		MenuPojo oneMenu = null;
		int isOK = 0;
		try {
			sqlHelper = new SqlExecute();
			isOK = this.checkMenuIsMain(sqlHelper, userInstId, menuId);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return isOK;
	}

	/**
	 * <p>
	 * 方法名称: checkMenuIsMain
	 * </p>
	 * <p>
	 * 方法功能描述:得到单个功能
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、功能ID menuId
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int checkMenuIsMain(SqlExecute sqlHelper, String userInstId,
			String menuId) throws Exception {
		return new BSMenuDBMang(sqlHelper, m_bs).checkMenuIsMain(userInstId,
				menuId);
	}

	/**
	 * <p>
	 * 方法名称: updatePFuncs
	 * </p>
	 * <p>
	 * 方法功能描述: 修改菜单数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updatePFuncs(MenuPojo oneMenu) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updatePFuncs(sqlHelper, oneMenu);
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
	 * 方法名称: updatePFuncs
	 * </p>
	 * <p>
	 * 方法功能描述: 修改菜单数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updatePFuncs(SqlExecute sqlHelper, MenuPojo oneMenu)
			throws Exception {
		BSMenuDBMang menuDB = new BSMenuDBMang(sqlHelper, m_bs);
		int count = 0;
		// 添加关系
		if (!"".equals(oneMenu.getPmenuId())) {
			// 删除该菜单所有关系
			List<Object> vList = new ArrayList<Object>();
			vList.add(oneMenu.getMenuId());
			count += menuDB.deleteMenuRel(" and MENU_ID=?", vList);
			// 添加新关系
			count += menuDB.insertMenuRel(oneMenu.getMenuId(),
					oneMenu.getPmenuId(), oneMenu.getSeq());
		}
		return count;
	}

}
