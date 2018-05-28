package tt.kulu.bi.user.pojo;

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
public class UserWorkDayLogsPojo {
	public static final String TYPE_NAME[] = { "离岗", "在岗" };
	public static final String LATEFLG_NAME[] = { "迟到", "按时到岗" };
	public static final String STATE_NAME[] = { "正常", "离岗", "报警" };
	private String id = "";
	private UserPojo user = new UserPojo();
	private String date = "";
	private String inDate = "";
	private String outDate = "";
	private String bjDate = "";
	private int type = 0;
	private long step = 0;// 步数
	private String distance = "0";// 路程
	private int opType = 0;// 0：围栏；1：数据
	private int late = 1;//
	private int state = 0;
	private long workTime = 0;// 工作时长

	public UserPojo getUser() {
		return user;
	}

	public void setUser(UserPojo user) {
		this.user = user;
	}

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

	public long getStep() {
		return step;
	}

	public void setStep(long step) {
		this.step = step;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getOpType() {
		return opType;
	}

	public void setOpType(int opType) {
		this.opType = opType;
	}

	public int getLate() {
		return late;
	}

	public void setLate(int late) {
		this.late = late;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getWorkTime() {
		return workTime;
	}

	public void setWorkTime(long workTime) {
		this.workTime = workTime;
	}

}
