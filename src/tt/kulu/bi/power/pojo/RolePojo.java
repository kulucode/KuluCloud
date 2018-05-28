package tt.kulu.bi.power.pojo;

/**
 * <p>
 * 标题: RolePojo
 * </p>
 * <p>
 * 功能描述: 角色Pojo类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2011-4-22
 * </p>
 */
public class RolePojo {
	public final static String[] STATE_NAME = { "无效", "有效" };

	private String id = "";// 角色ID

	private String name = "";// 角色名称

	private String desc = "";// 角色描述

	private int state = 1;// 状态

	private String userId = "";// 用户ID

	public RolePojo() {

	}

	public RolePojo(String id, String name, String desc) {
		this.id = id;
		this.name = name;
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
