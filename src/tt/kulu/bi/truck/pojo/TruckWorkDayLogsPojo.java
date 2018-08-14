package tt.kulu.bi.truck.pojo;

/**
 * <p>
 * 标题: UserWorkDayLogsPojo
 * </p>
 * <p>
 * 功能描述: 员工日工作日志Pojo
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
public class TruckWorkDayLogsPojo {
	public static final String TYPE_NAME[] = { "离岗", "在岗" };
	private String id = "";
	private TruckPojo truck = new TruckPojo();
	private String date = "";
	private String inDate = "";
	private String outDate = "";
	private String bjDate = "";
	private int type = 0;
	private String oil = "0";
	private String oilV = "0";
	private String distance = "0";// 路程
	private String workSDate = "";
	private String workEDate = "";

	private String workTime = "0";

	private int opType = 0;// 0：围栏；1：数据

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
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

	public String getBjDate() {
		return bjDate;
	}

	public void setBjDate(String bjDate) {
		this.bjDate = bjDate;
	}

	public TruckPojo getTruck() {
		return truck;
	}

	public void setTruck(TruckPojo truck) {
		this.truck = truck;
	}

	public String getOil() {
		return oil;
	}

	public void setOil(String oil) {
		this.oil = oil;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getWorkSDate() {
		return workSDate;
	}

	public void setWorkSDate(String workSDate) {
		this.workSDate = workSDate;
	}

	public String getWorkEDate() {
		return workEDate;
	}

	public void setWorkEDate(String workEDate) {
		this.workEDate = workEDate;
	}

	public int getOpType() {
		return opType;
	}

	public void setOpType(int opType) {
		this.opType = opType;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public String getOilV() {
		return oilV;
	}

	public void setOilV(String oilV) {
		this.oilV = oilV;
	}

}
