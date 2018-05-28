package tt.kulu.bi.fault.pojo;

import java.util.ArrayList;

import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.file.pojo.BFSFilePojo;
import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: FaultReportPojo
 * </p>
 * <p>
 * 功能描述: 故障报告类。
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
public class FaultReportPojo {
	public static String[] STATE_NAME = { "未处理", "已处理", "无法处理" };
	private String id = "";
	private String name = "";// 名称
	private String content = "";// 故障描述
	private String happenDate = "";// 发生时间
	private String endDate = "";// 结束时间
	private EquipmentInstPojo eqpInst = new EquipmentInstPojo();
	private UserPojo frUser = new UserPojo();
	private DicItemPojo faultCode = new DicItemPojo();// 故障代码
	private DicItemPojo faultType = new DicItemPojo();// 故障类别
	private DicItemPojo faultFrom = new DicItemPojo();// 故障来源

	private UserPojo createUser = new UserPojo();

	private UserPojo opUser = new UserPojo();//
	private String opDate = "";// 故障处理结束时间
	private String opDesc = "";//
	private int opState = 0;//

	private ArrayList<BFSFilePojo> faultFiles = new ArrayList<BFSFilePojo>();// 附件
	private ArrayList<BFSFilePojo> opFiles = new ArrayList<BFSFilePojo>();// 附件

	// private String
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHappenDate() {
		return happenDate;
	}

	public void setHappenDate(String happenDate) {
		this.happenDate = happenDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public EquipmentInstPojo getEqpInst() {
		return eqpInst;
	}

	public void setEqpInst(EquipmentInstPojo eqpInst) {
		this.eqpInst = eqpInst;
	}

	public UserPojo getFrUser() {
		return frUser;
	}

	public void setFrUser(UserPojo frUser) {
		this.frUser = frUser;
	}

	public UserPojo getCreateUser() {
		return createUser;
	}

	public void setCreateUser(UserPojo createUser) {
		this.createUser = createUser;
	}

	public String getOpDate() {
		return opDate;
	}

	public void setOpDate(String opDate) {
		this.opDate = opDate;
	}

	public String getOpDesc() {
		return opDesc;
	}

	public void setOpDesc(String opDesc) {
		this.opDesc = opDesc;
	}

	public int getOpState() {
		return opState;
	}

	public void setOpState(int opState) {
		this.opState = opState;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DicItemPojo getFaultCode() {
		return faultCode;
	}

	public void setFaultCode(DicItemPojo faultCode) {
		this.faultCode = faultCode;
	}

	public DicItemPojo getFaultType() {
		return faultType;
	}

	public void setFaultType(DicItemPojo faultType) {
		this.faultType = faultType;
	}

	public DicItemPojo getFaultFrom() {
		return faultFrom;
	}

	public void setFaultFrom(DicItemPojo faultFrom) {
		this.faultFrom = faultFrom;
	}

	public UserPojo getOpUser() {
		return opUser;
	}

	public void setOpUser(UserPojo opUser) {
		this.opUser = opUser;
	}

	public ArrayList<BFSFilePojo> getFaultFiles() {
		return faultFiles;
	}

	public void setFaultFiles(ArrayList<BFSFilePojo> faultFiles) {
		this.faultFiles = faultFiles;
	}

	public ArrayList<BFSFilePojo> getOpFiles() {
		return opFiles;
	}

	public void setOpFiles(ArrayList<BFSFilePojo> opFiles) {
		this.opFiles = opFiles;
	}

}
