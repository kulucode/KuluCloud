package tt.kulu.bi.function.dbclass;

import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.function.pojo.MenuPojo;
import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 类名：BSMenuDBMang
 * </p>
 * <p>
 * 功能描述：角色的数据权限读取操作类
 * </p>
 * <p>
 * 创建人：梁浩
 * </p>
 * <p>
 * 创建时间：2018-03-28
 * </p>
 * <p>
 * 版本：0.1
 * </p>
 * 
 */

public class BSMenuDBMang extends BSDBBase {
	public BSMenuDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getMenuList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到功能菜单。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 */
	public ArrayList<MenuPojo> getMenuList(String where, String orderBy,
			List<Object> vList) throws Exception {
		ArrayList<MenuPojo> menuList = new ArrayList<MenuPojo>();
		StringBuffer strSQL = _getMenuSelectSQL(where, orderBy);
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString(),
				vList);
		if (rs != null) {
			while (rs.next()) {
				menuList.add(this._setOneMenuPojo(rs));
			}
			rs.close();
		}
		return menuList;
	}

	/**
	 * <p>
	 * 方法名称: getOneMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 得到一个菜单。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 */
	public MenuPojo getOneMenuById(String id) throws Exception {
		MenuPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		StringBuffer strSQL = _getMenuSelectSQL(" and t.MENU_ID=?", "");
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString(),
				vList);
		if (rs != null && rs.next()) {
			onePojo = (this._setOneMenuPojo(rs));
			rs.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个菜单。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int insertMenu(MenuPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo.getMenuId() == null
				|| onePojo.getMenuId().trim().equals("")) {
			onePojo.setMenuId(BSGuid.getRandomGUID());
		}
		StringBuffer strSQL = new StringBuffer(
				"insert into T_MENU (MENU_ID,MENU_NAME,MENU_DESC,MENU_CLASS,MENU_STYLE,MENU_STATE,JSFUN,IMGNAME,ISOPEN,TOPAGE,TOP_FLAG,SEQ)");
		strSQL.append(" values (?,?,?,?,?,?,?,?,?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getMenuId());
		vList.add(onePojo.getMenuName());
		vList.add(onePojo.getMenuDes());
		vList.add(onePojo.getMenuClass());
		vList.add(onePojo.getMenuStyle());
		vList.add(onePojo.getMenuState());
		vList.add(onePojo.getJsfun().replaceAll("'", "''"));
		vList.add(onePojo.getImgName());
		vList.add(onePojo.getIsOpen());
		vList.add(onePojo.getToPage());
		vList.add(onePojo.getTopFlag());
		vList.add(onePojo.getSeq());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		// 添加关系
		if (!onePojo.getPmenuId().trim().equals("")) {
			vList.clear();
			vList.add(onePojo.getPmenuId());
			vList.add(onePojo.getMenuId());
			// 删除该菜单所有关系
			this.deleteMenuRel(" and P_MENU_ID=? and MENU_ID=?", vList);
			// 添加新关系
			count += this.insertMenuRel(onePojo.getMenuId(),
					onePojo.getPmenuId(), onePojo.getSeq());
		}

		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 更新一个菜单。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateMenu(MenuPojo onePojo) throws Exception {
		StringBuffer strSQL = new StringBuffer("update T_MENU set MENU_NAME=?");
		strSQL.append(",MENU_DESC=?");
		strSQL.append(",MENU_CLASS=?");
		strSQL.append(",MENU_STATE=?");
		strSQL.append(",MENU_STYLE=?");
		strSQL.append(",SEQ=?");
		strSQL.append(",JSFUN=?");
		strSQL.append(",IMGNAME=?");
		strSQL.append(",ISOPEN=?");
		strSQL.append(",TOPAGE=?");
		strSQL.append(",TOP_FLAG=?");
		strSQL.append(" where MENU_ID=?");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getMenuName());
		vList.add(onePojo.getMenuDes());
		vList.add(onePojo.getMenuClass());
		vList.add(onePojo.getMenuState());
		vList.add(onePojo.getMenuStyle());
		vList.add(onePojo.getSeq());
		vList.add(onePojo.getJsfun().replaceAll("'", "''"));
		vList.add(onePojo.getImgName());
		vList.add(onePojo.getIsOpen());
		vList.add(onePojo.getToPage());
		vList.add(onePojo.getTopFlag());
		vList.add(onePojo.getMenuId());
		return this.sqlHelper.updateBySql(strSQL.toString(), vList);
	}

	/**
	 * <p>
	 * 方法名称: insertMenuRel
	 * </p>
	 * <p>
	 * 方法功能描述: 插入一个功能菜单关系。
	 * </p>
	 */
	public int insertMenuRel(String menuid, String pmenuid, int seq)
			throws Exception {
		int count = 0;
		if (!pmenuid.trim().equals("root") && !pmenuid.trim().equals("")) {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("insert into T_MENU_R (");
			strSQL.append("MENU_ID,");
			strSQL.append("P_MENU_ID,");
			strSQL.append("SEQ");
			strSQL.append(") values (?,?,?)");
			List<Object> vList = new ArrayList<Object>();
			vList.add(menuid);
			vList.add(pmenuid);
			vList.add(seq);
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteMenuRel
	 * </p>
	 * <p>
	 * 方法功能描述: 删除一个功能菜单关系。
	 * </p>
	 */
	public int deleteMenuRel(String where, List<Object> vList) throws Exception {
		int count = 0;
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("delete from T_MENU_R t");
		if (!where.trim().equals("")) {
			strSQL.append(" where t.P_MENU_ID is not null " + where);
		}
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertMenuRel
	 * </p>
	 * <p>
	 * 方法功能描述: 插入一个功能菜单关系。
	 * </p>
	 */
	public int addOneTopMenu(MenuPojo oneMenu, UserPojo user, String dtIndex)
			throws Exception {
		int count = 0;
		// 得到当前的快捷菜单的记录
		int seq = this._resetTopMenu(user.getInstId());
		// 添加
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("insert into T_user_TOP_REL (");
		strSQL.append("MENU_ID,");
		strSQL.append("USER_INSTID,");
		strSQL.append("DESKTOP_ID,");
		strSQL.append("SEQ");
		strSQL.append(") values (?,?,?,?)");
		List<Object> vList = new ArrayList<Object>();
		vList.add(oneMenu.getMenuId());
		vList.add(user.getInstId());
		vList.add(dtIndex);
		vList.add(seq);
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteTopMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 删除快捷菜单关系。
	 * </p>
	 */
	public int deleteTopMenu(String menuId, UserPojo user) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(menuId);
		vList.add(user.getInstId());
		count = this.sqlHelper
				.updateBySql(
						"delete from T_user_TOP_REL t where t.MENU_ID in (?) and t.USER_INSTID=?",
						vList);
		// 得到当前的快捷菜单的记录
		count += this._resetTopMenu(user.getInstId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: checkMenuIsMain
	 * </p>
	 * <p>
	 * 方法功能描述: 删除快捷菜单关系。
	 * </p>
	 */
	public int checkMenuIsMain(String userInstId, String menuId)
			throws Exception {
		int isOK = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(userInstId);
		vList.add(menuId);
		StringBuffer strSQL = new StringBuffer(
				"select t.ROLE_ID, t.MAIN_FLG from T_ROLE_MENU_R t,T_ROLE_USER_R t1 where t.ROLE_ID=t1.ROLE_ID and t1.USER_INSTID=? and t.MENU_ID=?");
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString(),
				vList);
		if (rs != null) {
			while (rs.next()) {
				if (rs.getString("ROLE_ID").equals("SUPER_ADMIN")
						|| rs.getInt("MAIN_FLG") == 1) {
					isOK = 1;
					break;
				}
			}
			rs.close();
		}
		return isOK;
	}

	// 取得应用信息的SQL语句
	private StringBuffer _getMenuSelectSQL(String where, String orderBy) {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.MENU_ID,");
		strSQL.append("t.MENU_NAME,");
		strSQL.append("t.MENU_DESC,");
		strSQL.append("t.MENU_CLASS,");
		strSQL.append("t.MENU_STYLE,");
		strSQL.append("t.MENU_STATE,");
		strSQL.append("t.JSFUN,");
		strSQL.append("t.IMGNAME,");
		strSQL.append("t.TOPAGE,");
		strSQL.append("t.ISOPEN,");
		strSQL.append("t.TOP_FLAG,");
		strSQL.append("t.SEQ as THIS_SEQ,");
		strSQL.append("t1.SEQ,");
		strSQL.append("t2.MENU_ID AS P_MENU_ID,");
		strSQL.append("t2.MENU_NAME AS P_MENU_NAME,");
		strSQL.append("(select count(*) from T_MENU_R tt where tt.P_MENU_ID = t.MENU_ID) as CHLD_NUM ");
		strSQL.append(" from T_MENU_R t1 right outer join T_MENU t on t1.MENU_ID = t.MENU_ID left outer join T_MENU t2 on t2.MENU_ID = t1.P_MENU_ID");
		strSQL.append(" where t.MENU_ID is not null ");
		if (!where.trim().equals("")) {
			strSQL.append(where);
		}
		if (!orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		}
		return strSQL;
	}

	// 设置一个菜单实体
	private MenuPojo _setOneMenuPojo(CachedRowSet rs) throws Exception {
		MenuPojo oneMenu = new MenuPojo();
		if (rs.getString("MENU_ID") != null) {
			oneMenu.setMenuId(rs.getString("MENU_ID"));
		}
		if (rs.getString("MENU_NAME") != null) {
			oneMenu.setMenuName(rs.getString("MENU_NAME"));
		}
		if (rs.getString("MENU_DESC") != null) {
			oneMenu.setMenuDes(rs.getString("MENU_DESC"));
		}
		if (rs.getString("MENU_CLASS") != null) {
			oneMenu.setMenuClass(rs.getInt("MENU_CLASS"));
		}
		if (rs.getString("MENU_STATE") != null) {
			oneMenu.setMenuState(rs.getInt("MENU_STATE"));
		}
		oneMenu.setMenuStyle(rs.getInt("MENU_STYLE"));
		if (rs.getString("JSFUN") != null) {
			oneMenu.setJsfun(rs.getString("JSFUN").replaceAll("&acute;", "'"));
		}
		if (rs.getString("IMGNAME") != null) {
			oneMenu.setImgName(rs.getString("IMGNAME"));
		}
		if (rs.getString("ISOPEN") != null) {
			oneMenu.setIsOpen(rs.getInt("ISOPEN"));
		}
		if (rs.getString("TOP_FLAG") != null) {
			oneMenu.setTopFlag(rs.getInt("TOP_FLAG"));
		}
		if (rs.getString("SEQ") != null) {
			oneMenu.setSeq(rs.getInt("SEQ"));
		} else {
			oneMenu.setSeq(rs.getInt("THIS_SEQ"));
		}
		// 上级ID
		if (rs.getString("P_MENU_ID") != null) {
			oneMenu.setPmenuId(rs.getString("P_MENU_ID"));
		}
		if (rs.getString("P_MENU_NAME") != null) {
			oneMenu.setPmenuName(rs.getString("P_MENU_NAME"));
		}
		if (rs.getString("TOPAGE") != null) {
			oneMenu.setToPage(rs.getString("TOPAGE"));
		}
		if (rs.getString("CHLD_NUM") != null) {
			oneMenu.setCount(rs.getInt("CHLD_NUM"));
		}

		oneMenu.setChlMenuNum(rs.getInt("CHLD_NUM"));
		return oneMenu;
	}

	private int _resetTopMenu(String userInstid) throws Exception {
		int count = 0;
		// 得到当前的快捷菜单的记录
		List<Object> vList = new ArrayList<Object>();
		vList.add(userInstid);
		CachedRowSet rs = this.sqlHelper
				.queryCachedBySql(
						"select MENU_ID from T_user_TOP_REL where USER_INSTID=? order by SEQ",
						vList);
		if (rs != null) {
			while (rs.next()) {
				vList.clear();
				vList.add(count);
				vList.add(rs.getString("MENU_ID"));
				this.sqlHelper.updateBySql("update T_user_TOP_REL set SEQ="
						+ count + " where USER_INSTID='" + userInstid
						+ "' and MENU_ID='" + rs.getString("MENU_ID") + "'",
						vList);
				count++;
			}
			rs.close();
		}
		return count;
	}
}
