package tt.kulu.bi.report.pojo;

import tt.kulu.bi.truck.pojo.TruckPojo;

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
public class TruckReportPojo {
	private TruckPojo truck = new TruckPojo();
	private float oil = 0;// 油量
	private float distance = 0;// 路程
	private long workTimes = 0;// 工作时长
	private float useEff = 0;// 使用率
	private String formDate = "";// 开始时间
	private String toDate = "";// 结束时间
	private float speed = 0;// 平均时速

	public TruckPojo getTruck() {
		return truck;
	}

	public void setTruck(TruckPojo truck) {
		this.truck = truck;
	}

	public float getOil() {
		return oil;
	}

	public void setOil(float oil) {
		this.oil = oil;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public long getWorkTimes() {
		return workTimes;
	}

	public void setWorkTimes(long workTimes) {
		this.workTimes = workTimes;
	}

	public float getUseEff() {
		return useEff;
	}

	public void setUseEff(float useEff) {
		this.useEff = useEff;
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

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
