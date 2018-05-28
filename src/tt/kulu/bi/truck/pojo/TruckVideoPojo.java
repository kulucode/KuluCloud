package tt.kulu.bi.truck.pojo;

/**
 * <p>
 * 标题: TruckVideoPojo
 * </p>
 * <p>
 * 功能描述: 车辆视频实例Pojo类
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
public class TruckVideoPojo {
	private String id = "";
	private TruckPojo truck = new TruckPojo();
	private String ip = "";
	private int port = 0;
	private String user = "";
	private String password = "";
	private String url = "";
	private String eqpNo = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TruckPojo getTruck() {
		return truck;
	}

	public void setTruck(TruckPojo truck) {
		this.truck = truck;
	}

	public String getEqpNo() {
		return eqpNo;
	}

	public void setEqpNo(String eqpNo) {
		this.eqpNo = eqpNo;
	}

}
