package tt.kulu.bi.power.pojo;

import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: RoleUserPojo
 * </p>
 * <p>
 * 功能描述: 用户角色关系
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
public class RoleUserPojo {
	private UserPojo user = new UserPojo();// 用户
	private RolePojo role = new RolePojo();// 角色
	private int type = 0;// 类别

	public RoleUserPojo(String userInstId, String roleId) {
		this.user.setInstId(userInstId);
		this.role.setId(roleId);
	}

	public RoleUserPojo() {

	}

	public UserPojo getUser() {
		return user;
	}

	public void setUser(UserPojo user) {
		this.user = user;
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

}
