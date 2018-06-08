package tt.kulu.bi.user.pojo;

/**
 * <p>
 * 标题: UserWorkParasPojo
 * </p>
 * <p>
 * 功能描述: 用户工作参数Pojo
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
public class UserWorkParasMinPojo {
	private String eqpInst = "";
	private String updateDate = "";// 时间
	// 血压
	private String broHigh = "";
	private String broLow = "";
	private String broDate = "";
	// 心率
	private String heartRate = "";
	private String hrDate = "";
	private String eleValue = "";
	// 步数
	private String step = "";// 计步周期
	private String stepDate = "";
	// 位置
	private String longitude = "";// 经度
	private String latitude = "";// 纬度
	private String geoDate = "";
	private int fanceFlg = 0;
	// 状态
	private int thisState = 0;

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(String heartRate) {
		this.heartRate = heartRate;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
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

	public String getBroDate() {
		return broDate;
	}

	public void setBroDate(String broDate) {
		this.broDate = broDate;
	}

	public String getHrDate() {
		return hrDate;
	}

	public void setHrDate(String hrDate) {
		this.hrDate = hrDate;
	}

	public String getStepDate() {
		return stepDate;
	}

	public void setStepDate(String stepDate) {
		this.stepDate = stepDate;
	}

	public String getGeoDate() {
		return geoDate;
	}

	public void setGeoDate(String geoDate) {
		this.geoDate = geoDate;
	}

	public String getBroHigh() {
		return broHigh;
	}

	public void setBroHigh(String broHigh) {
		this.broHigh = broHigh;
	}

	public String getBroLow() {
		return broLow;
	}

	public void setBroLow(String broLow) {
		this.broLow = broLow;
	}

	public String getEleValue() {
		return eleValue;
	}

	public void setEleValue(String eleValue) {
		this.eleValue = eleValue;
	}

	public int getFanceFlg() {
		return fanceFlg;
	}

	public void setFanceFlg(int fanceFlg) {
		this.fanceFlg = fanceFlg;
	}

	public int getThisState() {
		return thisState;
	}

	public void setThisState(int thisState) {
		this.thisState = thisState;
	}

	public String getEqpInst() {
		return eqpInst;
	}

	public void setEqpInst(String eqpInst) {
		this.eqpInst = eqpInst;
	}

}
