package tt.kulu.bi.storage.pojo;

/**
 * <p>
 * 标题: EquipmentInstWorkLogPojo
 * </p>
 * <p>
 * 功能描述: 设备物资实例工作日志Pojo类
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
public class EquipmentInstWorkLogPojo {
	public static final String STATE_NAME[] = { "关机", "开机" };
	private String id = "";
	private EquipmentInstPojo eqpInst = new EquipmentInstPojo();
	private String date = "";
	private int state = 1;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public EquipmentInstPojo getEqpInst() {
		return eqpInst;
	}

	public void setEqpInst(EquipmentInstPojo eqpInst) {
		this.eqpInst = eqpInst;
	}

}
