package tt.kulu.bi.power.dbclass;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.power.pojo.RoleMenuPojo;
import tt.kulu.bi.power.pojo.RolePojo;
import tt.kulu.bi.power.pojo.RoleUserPojo;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSRoleDBMang
 * </p>
 * <p>
 * 功能描述: 角色实体数据库操作类
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
public class BSPowerDBMang extends BSDBBase {

	public BSPowerDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getOneRole
	 * </p>
	 * <p>
	 * 方法功能描述: 得到一个角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public RolePojo getOneRole(String id) throws Exception {
		RolePojo oneRole = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		StringBuffer strSQL = this._getRoleSelectSQL(" and t.ROLE_ID=?", "");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			oneRole = this._setOneRolePojo(rs);
		}
		return oneRole;
	}

	/**
	 * <p>
	 * 方法名称: getOneRoleByName
	 * </p>
	 * <p>
	 * 方法功能描述: 得到一个角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public RolePojo getOneRoleByName(String name) throws Exception {
		RolePojo oneRole = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(name);
		StringBuffer strSQL = this._getRoleSelectSQL(" and t.ROLE_NAME=?", "");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			oneRole = this._setOneRolePojo(rs);
		}
		return oneRole;
	}

	/**
	 * <p>
	 * 方法名称: getRoles
	 * </p>
	 * <p>
	 * 方法功能描述: 得到角色。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public ArrayList<RolePojo> getRoles(String where, String orderBy,
			List<Object> vList) throws Exception {
		ArrayList<RolePojo> roleList = null;
		StringBuffer strSQL = this._getRoleSelectSQL(where, orderBy);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			roleList = new ArrayList<RolePojo>();
			while (rs.next()) {
				roleList.add(this._setOneRolePojo(rs));
			}
		}
		return roleList;
	}

	/**
	 * <p>
	 * 方法名称: getRoles
	 * </p>
	 * <p>
	 * 方法功能描述: 得到用户角色。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public String getRoles(ArrayList<RolePojo> list, String where,
			List<Object> vList) throws Exception {
		String rolesStr = "";
		StringBuffer strSQL = this._getRoleSelectSQL(where, "");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			RolePojo onePojo = null;
			while (rs.next()) {
				// 得到角色信息
				onePojo = this._setOneRolePojo(rs);
				list.add(onePojo);
				rolesStr += ",'" + onePojo.getId() + "'";
			}
		}
		if (!rolesStr.trim().equals("")) {
			rolesStr = rolesStr.substring(1);
		}
		return rolesStr;
	}

	/**
	 * <p>
	 * 方法名称: insertRole
	 * </p>
	 * <p>
	 * 方法功能描述: 添加角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo rolePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int insertRole(RolePojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("insert into t_role ");
			strSQL.append("(ROLE_ID");
			strSQL.append(",ROLE_NAME");
			strSQL.append(",ROLE_DESC");
			strSQL.append(",ROLE_STATE");
			strSQL.append(") values (?,?,?,?)");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getId());
			vList.add(onePojo.getName());
			vList.add(onePojo.getDesc());
			vList.add(onePojo.getState());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertRoleMenu
	 * </p>
	 * <p>
	 * 方法功能描述: 添加角色权限信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo rolePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int insertRoleMenu(RoleMenuPojo rolePojo) throws Exception {
		int count = 0;
		// 删除
		List<Object> vList = new ArrayList<Object>();
		vList.add(rolePojo.getRole().getId());
		vList.add(rolePojo.getMenu().getMenuId());
		this.deleteRoleFunc(" and ROLE_ID=? and MENU_ID=?", vList);
		// 添加
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("insert into T_ROLE_MENU_R ");
		strSQL.append("( ROLE_ID");
		strSQL.append(",MENU_ID");
		strSQL.append(",RELA_TYPE");
		strSQL.append(",RELA_DESC");
		strSQL.append(") values (?,?,?,?)");
		vList.add(rolePojo.getType());
		vList.add(rolePojo.getDesc());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertUserRole
	 * </p>
	 * <p>
	 * 方法功能描述: 添加用户角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo rolePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int insertUserRole(RoleUserPojo roleUserR) throws Exception {
		int count = 0;
		if (roleUserR != null) {
			// 删除
			List<Object> vList = new ArrayList<Object>();
			vList.add(roleUserR.getRole().getId());
			vList.add(roleUserR.getUser().getInstId());
			this.deleteUserRole(" and ROLE_ID=? and USER_INSTID=?", vList);
			// 添加
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("insert into T_ROLE_USER_R ");
			strSQL.append("( ROLE_ID");
			strSQL.append(",USER_INSTID");
			strSQL.append(",RELA_TYPE)");
			strSQL.append(" values (?,?,?)");
			vList.add(roleUserR.getType());
			count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteUserRole
	 * </p>
	 * <p>
	 * 方法功能描述: 删除用户角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo rolePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int deleteUserRole(String where, List<Object> vList)
			throws Exception {
		int count = 0;
		if (where != null && !where.trim().equals("")) {
			count = this.sqlHelper.updateBySql(
					"delete from T_ROLE_USER_R t where t.USER_INSTID is not null "
							+ where, vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateRole
	 * </p>
	 * <p>
	 * 方法功能描述: 修改角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo rolePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int updateRole(RolePojo rolePojo) throws Exception {
		int count = 0;
		if (rolePojo != null) {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("update T_ROLE set ");
			strSQL.append(" ROLE_NAME=?");
			strSQL.append(", ROLE_DESC=?");
			strSQL.append(", ROLE_STATE=?");
			strSQL.append(" where ROLE_ID=?");
			List<Object> vList = new ArrayList<Object>();
			vList.add(rolePojo.getName());
			vList.add(rolePojo.getDesc());
			vList.add(rolePojo.getState());
			vList.add(rolePojo.getId());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteRoleFunc
	 * </p>
	 * <p>
	 * 方法功能描述: 删除用户角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo rolePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int deleteRoleFunc(String where, List<Object> vList)
			throws Exception {
		int count = 0;
		if (where != null && !where.trim().equals("")) {
			count = this.sqlHelper.updateBySql(
					"delete from T_ROLE_MENU_R t where t.ROLE_ID is not null "
							+ where, vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteRole
	 * </p>
	 * <p>
	 * 方法功能描述: 删除角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：删除条件。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int deleteRole(String where, List<Object> vList) throws Exception {
		int count = 0;
		if (!where.equals("")) {
			where = " where " + where;
		}
		count += sqlHelper.updateBySql("delete from T_ROLE_MENU_R " + where,
				vList);
		// strSQL = "delete TSM_ORG_ROLE_REAL ";
		// count += sqlHelper.updateBySql(strSQL + where);
		count += sqlHelper.updateBySql("delete from T_ROLE_USER_R " + where,
				vList);
		// strSQL = "delete T_ROLE_RESINST_R ";
		// count += sqlHelper.updateBySql(strSQL + where);
		// strSQL = "delete T_ROLE_RESTEMP_R ";
		// count += sqlHelper.updateBySql(strSQL + where);
		// strSQL = "delete T_ROLE_RESITEM_R ";
		// count += sqlHelper.updateBySql(strSQL + where);
		count += sqlHelper.updateBySql("delete from T_ROLE " + where, vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getRootMenuRoleList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到跟功能菜单。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 */
	public ArrayList<RoleMenuPojo> getRootMenuRoleList(String where,
			List<Object> vList) throws Exception {
		ArrayList<RoleMenuPojo> menuList = new ArrayList<RoleMenuPojo>();
		StringBuffer strSQL = _getRootMenuRoleSelectSQL(where);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				menuList.add(this._setOneRootMenuRolePojo(rs));
			}
			rs.close();
		}
		return menuList;
	}

	/**
	 * <p>
	 * 方法名称: getMenuRoleList
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
	public ArrayList<RoleMenuPojo> getMenuRoleList(String where,
			List<Object> vList) throws Exception {
		ArrayList<RoleMenuPojo> menuList = new ArrayList<RoleMenuPojo>();
		StringBuffer strSQL = _getMenuRoleSelectSQL(where);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				menuList.add(this._setOneMenuRolePojo(rs));
			}
			rs.close();
		}
		return menuList;
	}

	/**
	 * <p>
	 * 方法名称: getMenuPowerList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到角色功能菜单。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 */
	public ArrayList<RoleMenuPojo> getMenuPowerList(String where,
			List<Object> vList) throws Exception {
		ArrayList<RoleMenuPojo> menuList = new ArrayList<RoleMenuPojo>();
		StringBuffer strSQL = _getMenuPowerSelectSQL(where);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				menuList.add(this._setOneMenuPowerPojo(rs));
			}
			rs.close();
		}
		return menuList;
	}

	/**
	 * <p>
	 * 方法名称: deleteRole
	 * </p>
	 * <p>
	 * 方法功能描述: 添加角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo rolePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int deleteRole(String roleId) throws Exception {
		int count = 0;
		if (roleId != null && !roleId.equals("")) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(roleId);
			// 删除功能菜单关系
			count = sqlHelper.updateBySql(
					"delete from T_ROLE_MENU_R where ROLE_ID=?", vList);
			// 删除用户关系
			count += sqlHelper.updateBySql(
					"delete from T_ROLE_USER_R where ROLE_ID=?", vList);
			// 删除角色
			count += sqlHelper.updateBySql(
					"delete from T_ROLE where ROLE_ID=?", vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateRole
	 * </p>
	 * <p>
	 * 方法功能描述: 修改角色信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo rolePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int updateFunHome(RoleMenuPojo rolePojo) throws Exception {
		int count = 0;
		if (rolePojo != null) {
			List<Object> vList = new ArrayList<Object>();
			vList.add(rolePojo.getRole().getId());
			count = sqlHelper.updateBySql(
					"update T_ROLE_MENU_R set MAIN_FLG=0 where ROLE_ID =?",
					vList);
			vList.add(rolePojo.getMenu().getMenuId());
			count = sqlHelper
					.updateBySql(
							"update T_ROLE_MENU_R set MAIN_FLG=1 where ROLE_ID=? and MENU_ID=?",
							vList);
		}
		return count;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getRoleSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.ROLE_ID");
		strSQL.append(",t.ROLE_NAME");
		strSQL.append(",t.ROLE_DESC");
		strSQL.append(",t.ROLE_STATE");
		strSQL.append(" from T_ROLE t");
		strSQL.append(" where t.ROLE_ID is not null ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by" + orderBy);
		}
		return strSQL;
	}

	// 填充角色信息
	private RolePojo _setOneRolePojo(ResultSet rs) throws Exception {
		RolePojo oneRole = new RolePojo();
		// 角色ID
		if (rs.getString("ROLE_ID") != null) {
			oneRole.setId(rs.getString("ROLE_ID").trim());
		}
		// 角色名称
		if (rs.getString("ROLE_NAME") != null) {
			oneRole.setName(rs.getString("ROLE_NAME").trim());
		}
		// 角色描述
		if (rs.getString("ROLE_DESC") != null) {
			oneRole.setDesc(rs.getString("ROLE_DESC").trim());
		}
		// 状态
		if (rs.getString("ROLE_STATE") != null) {
			oneRole.setState(rs.getInt("ROLE_STATE"));
		}

		return oneRole;
	}

	// 取得应用信息的SQL语句
	private StringBuffer _getRootMenuRoleSelectSQL(String where) {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select distinct ");
		strSQL.append("t.MENU_ID");
		strSQL.append(",t3.MENU_NAME");
		strSQL.append(",t3.MENU_DESC");
		strSQL.append(",t3.TOPAGE");
		strSQL.append(",t3.JSFUN");
		strSQL.append(",t3.IMGNAME");
		strSQL.append(",t3.SEQ");
		strSQL.append(",(select count(*) from T_MENU_R tt where tt.P_MENU_ID = t.MENU_ID) as CHLD_NUM ");
		strSQL.append(" from T_ROLE_MENU_R t, T_MENU t3 ");
		strSQL.append(" where t.MENU_ID=t3.MENU_ID");
		if (!where.trim().equals("")) {
			strSQL.append(where);
		}
		strSQL.append(" order by t3.SEQ");
		return strSQL;
	}

	// 设置一个角色功能
	private RoleMenuPojo _setOneRootMenuRolePojo(ResultSet rs) throws Exception {
		RoleMenuPojo onePojo = new RoleMenuPojo();
		if (rs.getString("MENU_ID") != null) {
			onePojo.getMenu().setMenuId(rs.getString("MENU_ID"));
		}
		if (rs.getString("MENU_NAME") != null) {
			onePojo.getMenu().setMenuName(rs.getString("MENU_NAME"));
		}
		if (rs.getString("MENU_DESC") != null) {
			onePojo.getMenu().setMenuDes(rs.getString("MENU_DESC"));
		}
		if (rs.getString("JSFUN") != null) {
			onePojo.getMenu().setJsfun(rs.getString("JSFUN"));
		}
		if (rs.getString("IMGNAME") != null) {
			onePojo.getMenu().setImgName(rs.getString("IMGNAME"));
		}
		if (rs.getString("SEQ") != null) {
			onePojo.getMenu().setSeq(rs.getInt("SEQ"));
		}
		if (rs.getString("TOPAGE") != null) {
			onePojo.getMenu().setToPage(rs.getString("TOPAGE"));
		}
		if (rs.getString("CHLD_NUM") != null) {
			onePojo.getMenu().setCount(rs.getInt("CHLD_NUM"));
		}
		return onePojo;
	}

	// 取得应用信息的SQL语句
	private StringBuffer _getMenuRoleSelectSQL(String where) {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select distinct ");
		strSQL.append("t.MENU_ID");
		strSQL.append(",t.MAIN_FLG");
		strSQL.append(",t3.MENU_NAME");
		strSQL.append(",t3.MENU_DESC");
		strSQL.append(",t3.JSFUN");
		strSQL.append(",t3.TOPAGE");
		strSQL.append(",t3.IMGNAME");
		strSQL.append(",t3.SEQ");
		strSQL.append(",t2.SEQ as R_SEQ");
		strSQL.append(",t2.MENU_ID as P_MENU_ID");
		strSQL.append(",t4.SEQ as P_SEQ");
		strSQL.append(" from T_ROLE_MENU_R t, T_MENU_R t2, T_MENU t3, T_MENU t4 ");
		strSQL.append(" where t.MENU_ID=t3.MENU_ID");
		strSQL.append(" and t2.MENU_ID=t3.MENU_ID");
		strSQL.append(" and t2.P_MENU_ID=t4.MENU_ID");
		if (!where.trim().equals("")) {
			strSQL.append(where);
		}
		strSQL.append(" order by t2.SEQ,t4.SEQ,t3.SEQ");
		return strSQL;
	}

	// 取得应用信息的SQL语句
	private StringBuffer _getMenuPowerSelectSQL(String where) {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select distinct ");
		strSQL.append("t.MENU_ID");
		strSQL.append(",t.MAIN_FLG");
		strSQL.append(",t1.MENU_NAME");
		strSQL.append(",t1.MENU_DESC");
		strSQL.append(",t1.JSFUN");
		strSQL.append(",t1.TOPAGE");
		strSQL.append(",t1.IMGNAME");
		strSQL.append(",t1.SEQ");
		strSQL.append(" from T_ROLE_MENU_R t, T_MENU t1 ");
		strSQL.append(" where t.MENU_ID=t1.MENU_ID");
		if (!where.trim().equals("")) {
			strSQL.append(where);
		}
		strSQL.append(" order by t1.SEQ");
		return strSQL;
	}

	// 设置一个角色功能
	private RoleMenuPojo _setOneMenuRolePojo(ResultSet rs) throws Exception {
		RoleMenuPojo onePojo = new RoleMenuPojo();
		if (rs.getString("MENU_ID") != null) {
			onePojo.getMenu().setMenuId(rs.getString("MENU_ID"));
		}
		if (rs.getString("MAIN_FLG") != null) {
			onePojo.setMainFlg(rs.getInt("MAIN_FLG"));
		}
		if (rs.getString("MENU_NAME") != null) {
			onePojo.getMenu().setMenuName(rs.getString("MENU_NAME"));
		}
		if (rs.getString("MENU_DESC") != null) {
			onePojo.getMenu().setMenuDes(rs.getString("MENU_DESC"));
		}
		if (rs.getString("JSFUN") != null) {
			onePojo.getMenu().setJsfun(rs.getString("JSFUN"));
		}
		if (rs.getString("TOPAGE") != null) {
			onePojo.getMenu().setToPage(rs.getString("TOPAGE"));
		}
		if (rs.getString("IMGNAME") != null) {
			onePojo.getMenu().setImgName(rs.getString("IMGNAME"));
		}
		if (rs.getString("SEQ") != null) {
			onePojo.getMenu().setSeq(rs.getInt("SEQ"));
		}
		if (rs.getString("P_MENU_ID") != null) {
			// onePojo.getpMenuid().setMenuId(rs.getString("P_MENU_ID"));
		}
		return onePojo;
	}

	// 设置一个角色功能
	private RoleMenuPojo _setOneMenuPowerPojo(ResultSet rs) throws Exception {
		RoleMenuPojo onePojo = new RoleMenuPojo();
		if (rs.getString("MENU_ID") != null) {
			onePojo.getMenu().setMenuId(rs.getString("MENU_ID"));
		}
		if (rs.getString("MAIN_FLG") != null) {
			onePojo.setMainFlg(rs.getInt("MAIN_FLG"));
		}
		if (rs.getString("MENU_NAME") != null) {
			onePojo.getMenu().setMenuName(rs.getString("MENU_NAME"));
		}
		if (rs.getString("MENU_DESC") != null) {
			onePojo.getMenu().setMenuDes(rs.getString("MENU_DESC"));
		}
		if (rs.getString("JSFUN") != null) {
			onePojo.getMenu().setJsfun(rs.getString("JSFUN"));
		}
		if (rs.getString("TOPAGE") != null) {
			onePojo.getMenu().setToPage(rs.getString("TOPAGE"));
		}
		if (rs.getString("IMGNAME") != null) {
			onePojo.getMenu().setImgName(rs.getString("IMGNAME"));
		}
		if (rs.getString("SEQ") != null) {
			onePojo.getMenu().setSeq(rs.getInt("SEQ"));
		}
		return onePojo;
	}
}
