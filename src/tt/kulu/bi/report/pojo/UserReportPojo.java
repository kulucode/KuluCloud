package tt.kulu.bi.report.pojo;

import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: TruckReportPojo
 * </p>
 * <p>
 * 功能描述: 车辆报表Pojo类
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
public class UserReportPojo {
	private UserPojo user = new UserPojo();
	private long step = 0;// 步数
	private float distance = 0;// 路程
	private long abs = 0;// 缺勤次数
	private long late = 0;// 迟到次数
	private String formDate = "";// 开始时间
	private String toDate = "";// 结束时间
	private String workTime = "0";

	public UserPojo getUser() {
		return user;
	}

	public void setUser(UserPojo user) {
		this.user = user;
	}

	public long getStep() {
		return step;
	}

	public void setStep(long step) {
		this.step = step;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public long getAbs() {
		return abs;
	}

	public void setAbs(long abs) {
		this.abs = abs;
	}

	public long getLate() {
		return late;
	}

	public void setLate(long late) {
		this.late = late;
	}

	public String getFormDate() {
		return formDate;
	}

	public void setFormDate(String formDate) {
		this.formDate = formDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

}
