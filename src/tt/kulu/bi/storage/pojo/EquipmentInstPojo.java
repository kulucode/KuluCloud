package tt.kulu.bi.storage.pojo;

import tt.kulu.bi.truck.pojo.TruckPojo;
import tt.kulu.bi.user.pojo.OrgPojo;
import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: EquipmentInstPojo
 * </p>
 * <p>
 * 功能描述: 设备物资实例Pojo类
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
public class EquipmentInstPojo {
	public static String[] STATE_NAME = { "正常", "故障", "报废", "维修中", "待删除" };
	public static String[] ONLINE_STATE_NAME = { "下线", "上线" };
	private EquipmentDefPojo eqpDef = new EquipmentDefPojo();// 物品定义
	private String instId = "";//
	private String wyCode = "";//
	private String token = "";//
	private String name = "";// 名称
	private String qrCode = "";// 条码
	private String proDate = "";// 生产日期
	private String updateDate = "";// 状态日期
	private int state = 0;// 状态
	private int onlineState = 0;// 在线状态
	private long count = 1;// 数量
	private OrgPojo org = new OrgPojo();
	private UserPojo mangUser = new UserPojo();
	private TruckPojo truck = new TruckPojo();
	private String lastLoginDate = "";
	private String phone = "";// 关联号码
	private String thisDate = "";
	private String para1 = "";//

	public EquipmentDefPojo getEqpDef() {
		return eqpDef;
	}

	public void setEqpDef(EquipmentDefPojo eqpDef) {
		this.eqpDef = eqpDef;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getProDate() {
		return proDate;
	}

	public void setProDate(String proDate) {
		this.proDate = proDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public int getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(int onlineState) {
		this.onlineState = onlineState;
	}

	public UserPojo getMangUser() {
		return mangUser;
	}

	public void setMangUser(UserPojo mangUser) {
		this.mangUser = mangUser;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getWyCode() {
		return wyCode;
	}

	public void setWyCode(String wyCode) {
		this.wyCode = wyCode;
	}

	public OrgPojo getOrg() {
		return org;
	}

	public void setOrg(OrgPojo org) {
		this.org = org;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TruckPojo getTruck() {
		return truck;
	}

	public void setTruck(TruckPojo truck) {
		this.truck = truck;
	}

	public String getThisDate() {
		return thisDate;
	}

	public void setThisDate(String thisDate) {
		this.thisDate = thisDate;
	}

	public String getPara1() {
		return para1;
	}

	public void setPara1(String para1) {
		this.para1 = para1;
	}

}
