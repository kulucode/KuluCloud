package tt.kulu.bi.watch.pojo;

import tt.kulu.bi.storage.pojo.EquipmentInstPojo;

/**
 * （cmd：110）血压正常报文
 * 
 * @author tangbin
 * 
 */
public class InBloodPressureOkCmd {
	private EquipmentInstPojo eqpInst = new EquipmentInstPojo();
	private String high = "";
	private String low = "";
	private String type = "110";
	private String createDate = "";

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
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
