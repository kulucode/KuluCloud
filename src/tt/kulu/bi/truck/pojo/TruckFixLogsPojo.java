package tt.kulu.bi.truck.pojo;

import java.util.ArrayList;

import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.file.pojo.BFSFilePojo;
import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: TruckFixLogsPojo
 * </p>
 * <p>
 * 功能描述: 车辆维修日志实例Pojo类
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
public class TruckFixLogsPojo {
	private String id = "";//
	private String name = "";// 维修位置
	private TruckPojo truck = new TruckPojo();// 车辆
	private String logDate = "";// 时间
	private String content = "";// 内容
	private String money = "0";// 维修金额
	private DicItemPojo type = new DicItemPojo();// 类别
	private UserPojo user = new UserPojo();// 操作人
	private ArrayList<BFSFilePojo> files = new ArrayList<BFSFilePojo>();// 附件

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TruckPojo getTruck() {
		return truck;
	}

	public void setTruck(TruckPojo truck) {
		this.truck = truck;
	}

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public DicItemPojo getType() {
		return type;
	}

	public void setType(DicItemPojo type) {
		this.type = type;
	}

	public UserPojo getUser() {
		return user;
	}

	public void setUser(UserPojo user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<BFSFilePojo> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<BFSFilePojo> files) {
		this.files = files;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

}
