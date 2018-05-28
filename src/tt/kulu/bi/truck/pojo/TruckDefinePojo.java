package tt.kulu.bi.truck.pojo;

/**
 * <p>
 * 标题: TruckDefinePojo
 * </p>
 * <p>
 * 功能描述: 车型定义实例Pojo类
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
public class TruckDefinePojo {
	private String id = "";
	private String name = "";
	private String no = "";
	private String company = "";
	private String saleDate = "";
	private String linkMan = "";
	private long instCount = 0;
	private String oilMJ = "0";// 邮箱面积mm (宽*厚*高)
	private String oilDe = "0";// 百公里油耗
	private String brand = "";// 品牌
	private int workTime = 8;// 日规定时长

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public long getInstCount() {
		return instCount;
	}

	public void setInstCount(long instCount) {
		this.instCount = instCount;
	}

	public String getOilMJ() {
		return oilMJ;
	}

	public void setOilMJ(String oilMJ) {
		this.oilMJ = oilMJ;
	}

	public String getOilDe() {
		return oilDe;
	}

	public void setOilDe(String oilDe) {
		this.oilDe = oilDe;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getWorkTime() {
		return workTime;
	}

	public void setWorkTime(int workTime) {
		this.workTime = workTime;
	}

}
