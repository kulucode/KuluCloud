package tt.kulu.bi.logs.pojo;

import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: SysLogsPojo
 * </p>
 * <p>
 * 功能描述: 系统日志类。
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
public class MsgLogsPojo {
	public final static String[] TYPE_NAME = { "注册", "登录", "下线", "重连", "数据" };
	public final static String[] EQPTYPE_NAME = { "云环", "云盒", "其他" };
	private String id = "";//
	private String name = "";//
	private String createDate = "";//
	private String msgDate = "";//
	private String eqpName = "";//
	private int type = 0;//
	private int eqpType = 0;//
	private String content = "";//
	private String inMsg = "";//
	private String outMsg = "";//

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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getEqpName() {
		return eqpName;
	}

	public void setEqpName(String eqpName) {
		this.eqpName = eqpName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getEqpType() {
		return eqpType;
	}

	public void setEqpType(int eqpType) {
		this.eqpType = eqpType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getInMsg() {
		return inMsg;
	}

	public void setInMsg(String inMsg) {
		this.inMsg = inMsg;
	}

	public String getOutMsg() {
		return outMsg;
	}

	public void setOutMsg(String outMsg) {
		this.outMsg = outMsg;
	}

	public String getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}

}
