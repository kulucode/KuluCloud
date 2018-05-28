package tt.kulu.bi.storage.pojo;

/**
 * <p>
 * 标题: EquipmentGeometryPojo
 * </p>
 * <p>
 * 功能描述: 设备地理位置定义Pojo类
 * </p>
 * <p>
 * 作者: 马维
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2014-10-29
 * </p>
 */
public class EquipmentGeometryPojo {
	private String id = "";
	private EquipmentInstPojo eqpInst = new EquipmentInstPojo();
	private String longitude = "";// 经度
	private String latitude = "";// 纬度
	private String sysDate = "";// 时间
	private String createDate = "";// 时间
	private String dir = "";// 方向
	private String speed = "0";
	private String fdId = "";// 分段ID
	private String lastStopDate = "";// 上次停车时间
	private int fanceFlg = 0;

	public EquipmentInstPojo getEqpInst() {
		return eqpInst;
	}

	public void setEqpInst(EquipmentInstPojo eqpInst) {
		this.eqpInst = eqpInst;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getLastStopDate() {
		return lastStopDate;
	}

	public void setLastStopDate(String lastStopDate) {
		this.lastStopDate = lastStopDate;
	}

	public int getFanceFlg() {
		return fanceFlg;
	}

	public void setFanceFlg(int fanceFlg) {
		this.fanceFlg = fanceFlg;
	}

	public String getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}

}
