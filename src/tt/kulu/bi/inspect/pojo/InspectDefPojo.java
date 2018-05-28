package tt.kulu.bi.inspect.pojo;

import tt.kulu.bi.truck.pojo.TruckDefinePojo;

/**
 * <p>
 * 标题: InspectDefPojo
 * </p>
 * <p>
 * 功能描述: 巡检规程定义类。
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
public class InspectDefPojo {
	public static String[] CLASS_NAME = { "一级", "二级", "三级" };
	public static String[] STATE_NAME = { "无效", "有效" };
	private String id = "";//
	private String name = "";// 名称
	private TruckDefinePojo turckDef = new TruckDefinePojo();// 列车
	private String desc = "";// 描述
	private String mileage = "0";// 里程数
	private int defClass = 2;// 级别
	private int cycle = 0;// 周期
	private int state = 1;// 状态
	//
	private int subCount = 0;// 下级保养次数
	private InspectDefPojo subDefId = null;// 下级规程

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

	public TruckDefinePojo getTurckDef() {
		return turckDef;
	}

	public void setTurckDef(TruckDefinePojo turckDef) {
		this.turckDef = turckDef;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public int getSubCount() {
		return subCount;
	}

	public void setSubCount(int subCount) {
		this.subCount = subCount;
	}

	public InspectDefPojo getSubDefId() {
		return subDefId;
	}

	public void setSubDefId(InspectDefPojo subDefId) {
		this.subDefId = subDefId;
	}

	public int getDefClass() {
		return defClass;
	}

	public void setDefClass(int defClass) {
		this.defClass = defClass;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
