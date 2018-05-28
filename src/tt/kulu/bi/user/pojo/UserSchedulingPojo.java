package tt.kulu.bi.user.pojo;

/**
 * <p>
 * 标题: UserSchedulingPojo
 * </p>
 * <p>
 * 功能描述: 人员排班表实例Pojo类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2014-10-29
 * </p>
 */
public class UserSchedulingPojo {
	private String id = "";
	private UserPojo user = new UserPojo();
	private String startDate = "";
	private String endDate = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserPojo getUser() {
		return user;
	}

	public void setUser(UserPojo user) {
		this.user = user;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
