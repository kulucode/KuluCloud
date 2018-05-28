package tt.kulu.bi.watch.pojo;

import tt.kulu.bi.storage.pojo.EquipmentInstPojo;

/**
 * （cmd：13）计步周期上传
 * 
 * @author tangbin
 * 
 */
public class InStepCmd {
	private EquipmentInstPojo eqpInst = new EquipmentInstPojo();
	private String step = "";// 计步周期
	private String type = "13";
	private String createDate = "";

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
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
