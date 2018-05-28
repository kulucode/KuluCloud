package tt.kulu.bi.fance.pojo;

import net.sf.json.JSONArray;
import tt.kulu.bi.area.pojo.AreaPojo;
import tt.kulu.bi.user.pojo.OrgPojo;

/**
 * <p>
 * 标题: FancePojo
 * </p>
 * <p>
 * 功能描述: 围栏。
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
public class FancePojo {
	public static final String TYPE_NAME[] = { "共用", "车用", "人用" };
	private String id = "";// ID
	private String name = "";// 名称
	private int type = 0;// 类型
	private OrgPojo org = new OrgPojo();// 所属机构
	private AreaPojo area = new AreaPojo();
	private JSONArray geo = new JSONArray();// 围栏点列表
	private String users = "";
	private String trucks = "";
	private String center[] = new String[2];// 中心点位置，展示用

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

	public OrgPojo getOrg() {
		return org;
	}

	public void setOrg(OrgPojo org) {
		this.org = org;
	}

	public AreaPojo getArea() {
		return area;
	}

	public void setArea(AreaPojo area) {
		this.area = area;
	}

	public JSONArray getGeo() {
		return geo;
	}

	public void setGeo(JSONArray geo) {
		this.geo = geo;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getTrucks() {
		return trucks;
	}

	public void setTrucks(String trucks) {
		this.trucks = trucks;
	}

	public String[] getCenter() {
		return center;
	}

	public void setCenter(String[] center) {
		this.center = center;
	}

}
