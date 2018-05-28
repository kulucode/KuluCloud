package tt.kulu.bi.user.pojo;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import tt.kulu.bi.area.pojo.AreaPojo;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.dic.pojo.DicItemPojo;

/**
 * <p>
 * 标题: 组织机构
 * </p>
 * <p>
 * 功能描述:组织机构Pojo
 * </p>
 * <p>
 * 创建人：梁浩
 * </p>
 * <p>
 * 创建日期: 2017-03-01
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 */
public class OrgPojo {
	public static final String TYPE_NAME[] = { "组织机构", "项目组", "项目组公司" };
	private String id = "";
	private String code = "";// 群组编码
	private String name = "";// 群组名称
	private UserPojo createStaff = new UserPojo();// 创建人
	private String createDate = "";// 创建时间
	private String desc = "";// 群组描述
	private CompanyPojo company = new CompanyPojo();// 企业
	private AreaPojo area = new AreaPojo();
	private int type = 1;
	// 位置
	private String longitude = "";// 经度
	private String latitude = "";// 纬度

	// 层级
	private JSONArray mangStaff = new JSONArray();// 管理人列表
	private long userNum = 0;// 员工数量
	private long subOrgNum = 0;// 下级机构数量
	private String porgId = "";// 上级ID
	private String porgName = "无";
	private String allOrgId = "";// 机构ID全路径
	private String allName = "";// 群组全称

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPorgId() {
		return porgId;
	}

	public void setPorgId(String porgId) {
		this.porgId = porgId;
	}

	public String getPorgName() {
		return porgName;
	}

	public void setPorgName(String porgName) {
		this.porgName = porgName;
	}

	public String getAllName() {
		return allName;
	}

	public void setAllName(String allName) {
		this.allName = allName;
	}

	public long getSubOrgNum() {
		return subOrgNum;
	}

	public void setSubOrgNum(long subOrgNum) {
		this.subOrgNum = subOrgNum;
	}

	public String getAllOrgId() {
		return allOrgId;
	}

	public void setAllOrgId(String allOrgId) {
		this.allOrgId = allOrgId;
	}

	public UserPojo getCreateStaff() {
		return createStaff;
	}

	public void setCreateStaff(UserPojo createStaff) {
		this.createStaff = createStaff;
	}

	public long getUserNum() {
		return userNum;
	}

	public void setUserNum(long userNum) {
		this.userNum = userNum;
	}

	public CompanyPojo getCompany() {
		return company;
	}

	public void setCompany(CompanyPojo company) {
		this.company = company;
	}

	public AreaPojo getArea() {
		return area;
	}

	public void setArea(AreaPojo area) {
		this.area = area;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static String[] getTypeName() {
		return TYPE_NAME;
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

	public JSONArray getMangStaff() {
		return mangStaff;
	}

	public void setMangStaff(JSONArray mangStaff) {
		this.mangStaff = mangStaff;
	}

}
