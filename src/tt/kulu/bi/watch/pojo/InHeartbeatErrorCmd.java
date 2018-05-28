package tt.kulu.bi.watch.pojo;

/**
 * （cmd：99）心跳异常报文
 * 
 * @author tangbin
 * 
 */
public class InHeartbeatErrorCmd {
	private String value = "";// 位置
	private String type = "99";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
