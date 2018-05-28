package tt.kulu.bi.company.pojo;

import tt.kulu.bi.area.pojo.AreaPojo;

/**
 * <p>
 * 标题: CompanyPojo
 * </p>
 * <p>
 * 功能描述: 企业管理类。
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2006-9-18
 * </p>
 */
public class CompanyPojo {
	public static String TYPE_NAME[] = { "本公司", "环卫运营商", "设备供应商" };
	private String id = "";
	private String name = "";
	private int type = 0;
	private String desc = "";
	private String linkMan = "";
	private String linkPhone = "";
	private AreaPojo area = new AreaPojo();
	// 位置
	private String longitude = "";// 经度
	private String latitude = "";// 纬度

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public AreaPojo getArea() {
		return area;
	}

	public void setArea(AreaPojo area) {
		this.area = area;
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

}
