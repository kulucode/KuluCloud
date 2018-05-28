package tt.kulu.bi.storage.pojo;

import tt.kulu.bi.dic.pojo.DicItemPojo;

/**
 * <p>
 * 标题: EquipmentDefPojo
 * </p>
 * <p>
 * 功能描述: 设备物资定义Pojo类
 * </p>
 * <p>
 * 作者: 马维
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2014-10-29
 * </p>
 */
public class EquipmentDefPojo {
	public static String[] STYLE_NAME = { "车载用", "人用", "其他" };
	private String id = "";//
	private String name = "";// 设备名称
	private DicItemPojo eqpType = new DicItemPojo();// 类别
	private String proDate = "";// 生产日期
	private String inDate = "";// 入场日期
	private String manufacturer = "";// 制造商
	private String no = "";// 终端型号
	private String provider = "";// 供应商
	private String brand = "";// 品牌
	private String vno = "";// 版本号
	private String price = "";// 价格
	private String fixPrice = "";// 维修成本
	private int style = 0;// 属性
	private long instCount = 0;// 实例数量

	private String para1 = "";// 油量传感器的单位
	private String para2 = "";
	private String para3 = "";

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

	public String getProDate() {
		return proDate;
	}

	public void setProDate(String proDate) {
		this.proDate = proDate;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getVno() {
		return vno;
	}

	public void setVno(String vno) {
		this.vno = vno;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getFixPrice() {
		return fixPrice;
	}

	public void setFixPrice(String fixPrice) {
		this.fixPrice = fixPrice;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public long getInstCount() {
		return instCount;
	}

	public void setInstCount(long instCount) {
		this.instCount = instCount;
	}

	public DicItemPojo getEqpType() {
		return eqpType;
	}

	public void setEqpType(DicItemPojo eqpType) {
		this.eqpType = eqpType;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getPara1() {
		return para1;
	}

	public void setPara1(String para1) {
		this.para1 = para1;
	}

	public String getPara2() {
		return para2;
	}

	public void setPara2(String para2) {
		this.para2 = para2;
	}

	public String getPara3() {
		return para3;
	}

	public void setPara3(String para3) {
		this.para3 = para3;
	}

}
