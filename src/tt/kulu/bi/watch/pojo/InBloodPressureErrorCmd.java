package tt.kulu.bi.watch.pojo;

/**
 * 血压异常
 * 
 * @author tangbin
 * 
 */
public class InBloodPressureErrorCmd {
	public String high = "";
	public String low = "";
	private String type = "113";

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

}
