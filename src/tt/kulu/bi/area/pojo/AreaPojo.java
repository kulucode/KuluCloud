package tt.kulu.bi.area.pojo;

import tt.kulu.bi.dic.pojo.DicItemPojo;

/**
 * <p>
 * 标题: AreaPojo
 * </p>
 * <p>
 * 功能描述: 区域实体类。
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
public class AreaPojo {
	private String id = "";
	private String name = "";
	private String shortName = "";
	private DicItemPojo areaClass = new DicItemPojo();
	private DicItemPojo areaType = new DicItemPojo();
	private String pId = "";// 上级ID
	private String pName = "无";
	private String allId = "";// 机构ID全路径
	private String allName = "";// 群组全称

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

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getAllId() {
		return allId;
	}

	public void setAllId(String allId) {
		this.allId = allId;
	}

	public String getAllName() {
		return allName;
	}

	public void setAllName(String allName) {
		this.allName = allName;
	}

	public DicItemPojo getAreaClass() {
		return areaClass;
	}

	public void setAreaClass(DicItemPojo areaClass) {
		this.areaClass = areaClass;
	}

	public DicItemPojo getAreaType() {
		return areaType;
	}

	public void setAreaType(DicItemPojo areaType) {
		this.areaType = areaType;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
