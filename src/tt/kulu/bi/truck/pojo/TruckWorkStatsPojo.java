package tt.kulu.bi.truck.pojo;

/**
 * <p>
 * 标题: TruckWorkStatsPojo
 * </p>
 * <p>
 * 功能描述: 车辆统计表实例Pojo类
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
public class TruckWorkStatsPojo {
	private TruckPojo truck = new TruckPojo();
	private String startDate = "";
	private String endDate = "";
	private String workTime = "0";
	private String oil = "0";
	private String oilDiff = "0";
	private String oilDiffV = "0";
	private String distance = "0";// 路程

	public TruckPojo getTruck() {
		return truck;
	}

	public void setTruck(TruckPojo truck) {
		this.truck = truck;
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

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public String getOil() {
		return oil;
	}

	public void setOil(String oil) {
		this.oil = oil;
	}

	public String getOilDiff() {
		return oilDiff;
	}

	public void setOilDiff(String oilDiff) {
		this.oilDiff = oilDiff;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getOilDiffV() {
		return oilDiffV;
	}

	public void setOilDiffV(String oilDiffV) {
		this.oilDiffV = oilDiffV;
	}
}
