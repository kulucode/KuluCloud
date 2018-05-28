package tt.kulu.bi.user.pojo;

/**
 * <p>
 * 标题: URLPublicJSONPojo
 * </p>
 * <p>
 * 功能描述: 网络接口共用参数Pojo
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
public class LoginUserPojo {
	private String userInst = "";// 用户实例ID
	private String userId = "";// 用户ID
	private String userName = "";// 用户名
	private String password = "";// 密码
	private String loginDate = "";// 时间戳
	private String phone = "";
	private String email = "";
	private String groupId = "";
	private String groupAllId = "";
	private String groupName = "";
	private String groupAllName = "";
	private String idCard = "";
	private int sex = 0;// 性别
	private String client = "";// 终端标识
	private int clientType = 0;// 终端类型0:iphone;1:android;2:wp
	private String roleWhere = "";// 角色列表
	private String dataWhere = "";// 数据权限列表

	public LoginUserPojo() {

	}

	public String getUserInst() {
		return userInst;
	}

	public void setUserInst(String userInst) {
		this.userInst = userInst;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public int getClientType() {
		return clientType;
	}

	public void setClientType(int clientType) {
		this.clientType = clientType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDataWhere() {
		return dataWhere;
	}

	public void setDataWhere(String dataWhere) {
		this.dataWhere = dataWhere;
	}

	public String getRoleWhere() {
		return roleWhere;
	}

	public void setRoleWhere(String roleWhere) {
		this.roleWhere = roleWhere;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupAllName() {
		return groupAllName;
	}

	public void setGroupAllName(String groupAllName) {
		this.groupAllName = groupAllName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupAllId() {
		return groupAllId;
	}

	public void setGroupAllId(String groupAllId) {
		this.groupAllId = groupAllId;
	}
}
