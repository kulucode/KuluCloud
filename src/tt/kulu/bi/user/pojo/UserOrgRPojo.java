package tt.kulu.bi.user.pojo;

/**
 * <p>
 * 标题: UserOrgRPojo
 * </p>
 * <p>
 * 功能描述: 用户机构关系Pojo
 * </p>
 * <p>
 * 创建人：梁浩
 * </p>
 * <p>
 * 创建日期: 2011-05-01
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 */
public class UserOrgRPojo {
	public static final String[] TYPE_NAME = { "所属", "管理", "所有" };
	private UserPojo user = new UserPojo();
	private OrgPojo org = new OrgPojo();
	private int type = 0;

	public UserOrgRPojo() {

	}

	public UserOrgRPojo(String userInstId, String orgId, int type) {
		this.user.setInstId(userInstId);
		this.org.setId(orgId);
		this.type = type;
	}

	public UserPojo getUser() {
		return user;
	}

	public void setUser(UserPojo user) {
		this.user = user;
	}

	public OrgPojo getOrg() {
		return org;
	}

	public void setOrg(OrgPojo org) {
		this.org = org;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
