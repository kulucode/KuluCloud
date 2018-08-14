package tt.kulu.bi.truck.pojo;

import net.sf.json.JSONObject;
import tt.kulu.bi.area.pojo.AreaPojo;
import tt.kulu.bi.user.pojo.OrgPojo;
import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: TruckPojo
 * </p>
 * <p>
 * 功能描述: 车辆定义实例Pojo类
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
public class TruckPojo {
	public static String[] COLOR_NAME = { "未知", "蓝底白字", "白底黑字或红字", "黑底白字",
			"黄底黑字", "绿底白字" };
	public static String[] STATE_NAME = { "正常", "故障", "报废", "维修中", "待删除" };
	private TruckDefinePojo define = new TruckDefinePojo();// 车型
	private String id = "";// id
	private String name = "";// 名称
	private String inName = "";// 内部名称
	private String no = "";// 车辆资产编码
	private String cjNo = "";// 车架号
	private String proDate = "";// 生产日期
	private String inDate = "";// 入场日期
	private int state = 0;// 状态
	private String plateNum = "";// 车牌号
	private int plateColor = 0;// 车牌颜色
	private UserPojo mangUser = new UserPojo();// 管理人
	private UserPojo driverUser = new UserPojo();// 当班司机
	private OrgPojo org = new OrgPojo();// 所属机构
	private String distance = "0";// 路程
	//
	private String upNo = "";
	private String upDate = "";
	private JSONObject oilDef = new JSONObject();

	private AreaPojo area = new AreaPojo();// 行政区域

	private int videoNum = 0;

	public TruckDefinePojo getDefine() {
		return define;
	}

	public void setDefine(TruckDefinePojo define) {
		this.define = define;
	}

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

	public String getPlateNum() {
		return plateNum;
	}

	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}

	public int getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(int plateColor) {
		this.plateColor = plateColor;
	}

	public UserPojo getMangUser() {
		return mangUser;
	}

	public void setMangUser(UserPojo mangUser) {
		this.mangUser = mangUser;
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getCjNo() {
		return cjNo;
	}

	public void setCjNo(String cjNo) {
		this.cjNo = cjNo;
	}

	public UserPojo getDriverUser() {
		return driverUser;
	}

	public void setDriverUser(UserPojo driverUser) {
		this.driverUser = driverUser;
	}

	public String getInName() {
		return inName;
	}

	public void setInName(String inName) {
		this.inName = inName;
	}

	public String getUpNo() {
		return upNo;
	}

	public void setUpNo(String upNo) {
		this.upNo = upNo;
	}

	public String getUpDate() {
		return upDate;
	}

	public void setUpDate(String upDate) {
		this.upDate = upDate;
	}

	public int getVideoNum() {
		return videoNum;
	}

	public void setVideoNum(int videoNum) {
		this.videoNum = videoNum;
	}

	public JSONObject getOilDef() {
		return oilDef;
	}

	public void setOilDef(JSONObject oilDef) {
		this.oilDef = oilDef;
	}

}
