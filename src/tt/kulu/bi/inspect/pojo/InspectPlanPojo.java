package tt.kulu.bi.inspect.pojo;

import tt.kulu.bi.truck.pojo.TruckPojo;
import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: InspectPlanPojo
 * </p>
 * <p>
 * 功能描述: 巡检序列定义类。
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
public class InspectPlanPojo {
	public static String[] STATE_NAME = { "等待完成", "已完成" };
	private String id = "";
	private InspectDefPojo inspectDef = new InspectDefPojo();
	private TruckPojo truck = new TruckPojo();
	private String createDate = "";
	private String planDate = "";
	private int state = 0;
	private UserPojo opUser = new UserPojo();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InspectDefPojo getInspectDef() {
		return inspectDef;
	}

	public void setInspectDef(InspectDefPojo inspectDef) {
		this.inspectDef = inspectDef;
	}

	public TruckPojo getTruck() {
		return truck;
	}

	public void setTruck(TruckPojo truck) {
		this.truck = truck;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getPlanDate() {
		return planDate;
	}

	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public UserPojo getOpUser() {
		return opUser;
	}

	public void setOpUser(UserPojo opUser) {
		this.opUser = opUser;
	}

}
