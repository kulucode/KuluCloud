package tt.kulu.bi.truck.pojo;

import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: TruckDriverRPojo
 * </p>
 * <p>
 * 功能描述: 车辆司机关系实例Pojo类
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
public class TruckDriverRPojo {
	private String id = "";
	private String startDate = "";
	private String endDate = "";
	private TruckPojo truck = new TruckPojo();
	private UserPojo user = new UserPojo();
	private int type = 0;

	public TruckPojo getTruck() {
		return truck;
	}

	public void setTruck(TruckPojo truck) {
		this.truck = truck;
	}

	public UserPojo getUser() {
		return user;
	}

	public void setUser(UserPojo user) {
		this.user = user;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
