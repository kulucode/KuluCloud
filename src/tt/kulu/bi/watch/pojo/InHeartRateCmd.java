package tt.kulu.bi.watch.pojo;

import tt.kulu.bi.storage.pojo.EquipmentInstPojo;

/**
 * （cmd：14）心率周期上传（中间包含了实时电量）
 * 
 * @author tangbin
 * 
 */
public class InHeartRateCmd {
	private EquipmentInstPojo eqpInst = new EquipmentInstPojo();
	private String createDate = "";
	private String heartRate = "";
	private String electricity = "";// 电量
	private String type = "14";

	public String getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(String heartRate) {
		this.heartRate = heartRate;
	}

	public String getElectricity() {
		return electricity;
	}

	public void setElectricity(String electricity) {
		this.electricity = electricity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public EquipmentInstPojo getEqpInst() {
		return eqpInst;
	}

	public void setEqpInst(EquipmentInstPojo eqpInst) {
		this.eqpInst = eqpInst;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
