package tt.kulu.bi.truck.pojo;

import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: TruckWorkParasPojo
 * </p>
 * <p>
 * 功能描述: 车辆工作参数Pojo
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
public class TruckWorkParasPojo {
	private EquipmentInstPojo eqpInst = new EquipmentInstPojo();
	private String updateDate = "";// 时间
	// 油量
	private String oil = "";
	private String oilDiff = "";
	private String speed = "";
	private String oilDate = "";

	// 位置
	private String longitude = "";// 经度
	private String latitude = "";// 纬度
	private String geoDate = "";
	private int fanceFlg = 0;

	private String weight = "";// 重量
	private UserPojo workUser = null;// 当班司机

	public EquipmentInstPojo getEqpInst() {
		return eqpInst;
	}

	public void setEqpInst(EquipmentInstPojo eqpInst) {
		this.eqpInst = eqpInst;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getOil() {
		return oil;
	}

	public void setOil(String oil) {
		this.oil = oil;
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

	public String getGeoDate() {
		return geoDate;
	}

	public void setGeoDate(String geoDate) {
		this.geoDate = geoDate;
	}

	public String getOilDiff() {
		return oilDiff;
	}

	public void setOilDiff(String oilDiff) {
		this.oilDiff = oilDiff;
	}

	public String getOilDate() {
		return oilDate;
	}

	public void setOilDate(String oilDate) {
		this.oilDate = oilDate;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public UserPojo getWorkUser() {
		return workUser;
	}

	public void setWorkUser(UserPojo workUser) {
		this.workUser = workUser;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public int getFanceFlg() {
		return fanceFlg;
	}

	public void setFanceFlg(int fanceFlg) {
		this.fanceFlg = fanceFlg;
	}

}
