package tt.kulu.bi.user.pojo;

import java.util.ArrayList;

import tt.kulu.bi.power.pojo.RolePojo;

/**
 * <p>
 * 标题: UserPojo
 * </p>
 * <p>
 * 功能描述: 用户Pojo
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
public class UserPojo {
	public static final String[] SEX_NAME = { "女", "男", "保密" };
	public static final String[] STATE_NAME = { "无效", "有效" };
	public static final String[] SBFLG_NAME = { "否", "是" };
	private String instId = "";// id
	private String id = "";// 用户号
	private String name = "";// 姓名
	private String inName = "";// 内部名称
	private String password = "";// 密码
	private String mPhone = "";// 电话
	private int state = 1;// 状态
	private String email = "";// 邮箱（可作为登录用）
	private int sex = 2;// 性别
	private String createDate = "";// 创建时间
	private String updateDate = "";// 更新时间
	private String idCard = "";
	private String birthday = "";
	private int age = 18;
	private int sbFlg = 1;// 是否社保
	// 角色
	private ArrayList<RolePojo> roleList = null;// 角色信息
	private String roleWhere = "";// 角色列表

	// 机构
	private OrgPojo org = null;
	private int ugType = 0;

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getmPhone() {
		return mPhone;
	}

	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<RolePojo> getRoleList() {
		return roleList;
	}

	public void setRoleList(ArrayList<RolePojo> roleList) {
		this.roleList = roleList;
	}

	public String getRoleWhere() {
		return roleWhere;
	}

	public void setRoleWhere(String roleWhere) {
		this.roleWhere = roleWhere;
	}

	public int getUgType() {
		return ugType;
	}

	public void setUgType(int ugType) {
		this.ugType = ugType;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSbFlg() {
		return sbFlg;
	}

	public void setSbFlg(int sbFlg) {
		this.sbFlg = sbFlg;
	}

	public OrgPojo getOrg() {
		return org;
	}

	public void setOrg(OrgPojo org) {
		this.org = org;
	}

	public String getInName() {
		return inName;
	}

	public void setInName(String inName) {
		this.inName = inName;
	}

}