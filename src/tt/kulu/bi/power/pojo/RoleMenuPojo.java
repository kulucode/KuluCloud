package tt.kulu.bi.power.pojo;

import tt.kulu.bi.function.pojo.MenuPojo;

/**
 * <p>
 * 标题: RoleMenuPojo
 * </p>
 * <p>
 * 功能描述: 角色权限POJO
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2011-1-22
 * </p>
 */

public class RoleMenuPojo {
	public static final String TYPE_NAME[] = { "不可用", "完全可用", "可编辑", "只读" };

	private RolePojo role = new RolePojo();// 角色

	private MenuPojo menu = new MenuPojo();// 功能

	private int type = 0;// 类别

	private String desc = "";// 描述

	private int mainFlg = 0;// 首页标志

	public RoleMenuPojo() {

	}

	public RoleMenuPojo(String roleId, String menuId, int type) {
		this.role.setId(roleId);
		this.menu.setMenuId(menuId);
		this.setType(type);
	}

	public RolePojo getRole() {
		return role;
	}

	public void setRole(RolePojo role) {
		this.role = role;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public MenuPojo getMenu() {
		return menu;
	}

	public void setMenu(MenuPojo menu) {
		this.menu = menu;
	}

	public int getMainFlg() {
		return mainFlg;
	}

	public void setMainFlg(int mainFlg) {
		this.mainFlg = mainFlg;
	}
}