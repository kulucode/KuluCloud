package tt.kulu.bi.logs.pojo;

import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: SysLogsPojo
 * </p>
 * <p>
 * 功能描述: 系统日志类。
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2006-9-18
 * </p>
 */
public class SysLogsPojo {
	public static String[] TYPE_NAME = { "登录日志", "操作日志" };
	private String id = "";//
	private String name = "";//
	private String createDate = "";//
	private LoginUserPojo createUser = new LoginUserPojo();//
	private String fromNetIP = "";//
	private String fromLocalIP = "";//
	private int type = 0;//
	private String content = "";//

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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public LoginUserPojo getCreateUser() {
		return createUser;
	}

	public void setCreateUser(LoginUserPojo createUser) {
		this.createUser = createUser;
	}

	public String getFromNetIP() {
		return fromNetIP;
	}

	public void setFromNetIP(String fromNetIP) {
		this.fromNetIP = fromNetIP;
	}

	public String getFromLocalIP() {
		return fromLocalIP;
	}

	public void setFromLocalIP(String fromLocalIP) {
		this.fromLocalIP = fromLocalIP;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
