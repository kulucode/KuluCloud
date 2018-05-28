package tt.kulu.bi.file.pojo;

import tt.kulu.bi.user.pojo.UserPojo;

/**
 * <p>
 * 标题: BFSFilePojo
 * </p>
 * <p>
 * 功能描述: 文件类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2010-1-22
 * </p>
 */
public class BFSFilePojo {
	public static String[] TYPE_NAME = { "图片", "文件", "语音", "压缩包" };
	public final static String[] STATE_NAME = { "公开", "仅自己看", "分享全部好友",
			"仅分享选定好友" };
	private String oldId = "";
	private String hashID = "";// 哈希效验
	private String instId = "";
	private String bissId = "";// 业务
	private String opType = "";
	private UserPojo user = null;// 创建者
	private String title = "";// 名称
	private int type = 0;// 类别
	private String fileName = "";// 文件夹名
	private String fileUrl = "";
	private String fileEx = "";// 后缀
	private String filePath = "";// 物理路径
	private String size = "";// 大小
	private String updateDate = "";// 上传时间
	private String editDate = "";// 更新时间
	private String desc = "";// 描述
	private int h = 0;// 高
	private int w = 0;// 宽
	private String password = "";// 阅读密码
	private int state = 0; // 状态
	private String otherId1 = "";
	private String otherId2 = "";
	private int index = 0;// 索引
	private long readNum = 0;

	public BFSFilePojo() {
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getEditDate() {
		return editDate;
	}

	public void setEditDate(String editDate) {
		this.editDate = editDate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getFileEx() {
		return fileEx;
	}

	public void setFileEx(String fileEx) {
		this.fileEx = fileEx;
		if (this.fileEx.toLowerCase().endsWith("png")
				|| this.fileEx.toLowerCase().endsWith("jpg")
				|| this.fileEx.toLowerCase().endsWith("gif")
				|| this.fileEx.toLowerCase().endsWith("bmp")) {
			this.type = 0;
		} else {
			this.type = 1;
		}
	}

	public String getBissId() {
		return bissId;
	}

	public void setBissId(String bissId) {
		this.bissId = bissId;
	}

	public UserPojo getUser() {
		return user;
	}

	public void setUser(UserPojo user) {
		this.user = user;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}

	public String getOtherId1() {
		return otherId1;
	}

	public void setOtherId1(String otherId1) {
		this.otherId1 = otherId1;
	}

	public String getOtherId2() {
		return otherId2;
	}

	public void setOtherId2(String otherId2) {
		this.otherId2 = otherId2;
	}

	public String getHashID() {
		return hashID;
	}

	public void setHashID(String hashID) {
		this.hashID = hashID;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getReadNum() {
		return readNum;
	}

	public void setReadNum(long readNum) {
		this.readNum = readNum;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

}
