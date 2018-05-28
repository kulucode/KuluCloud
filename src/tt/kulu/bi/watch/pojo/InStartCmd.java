package tt.kulu.bi.watch.pojo;

/**
 * （cmd：2）开机通告
 * 
 * @author tangbin
 * 
 */
public class InStartCmd {
	public String electricity = "";// 电量
	private String type = "2";

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

}
