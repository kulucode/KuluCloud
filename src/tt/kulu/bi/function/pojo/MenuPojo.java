package tt.kulu.bi.function.pojo;

/**
 * <p>
 * 标题: MenuPojo
 * </p>
 * <p>
 * 功能描述: 功能菜单Pojo T_MENU
 * </p>
 * <p>
 * 创建人：梁浩
 * </p>
 * <p>
 * 创建日期: 2011-05-01
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 */
public class MenuPojo {
	public final static String[] STATE_NAME = { "无效", "有效" };
	public final static String[] STYLE_NAME = { "后台", "WEB端" };
	public final static String[] CLASS_NAME = { "功能组", "普通功能", "页面菜单" };
	public final static String[] ISOPEN_NAME = { "关闭", "打开" };
	public final static String[] TOPFLAG_NAME = { "系统功能", "非系统功能" };
	private String menuId = "";// 菜单ID

	private String menuName = "";// 菜单项名称

	private String menuDes = "";// 菜单项描述

	private int menuClass = 1;// 菜单组

	private int menuStyle = 0;

	private int menuState = 1;// 状态

	private String jsfun = "";// JS方法

	private String imgName = "";// 图标路径

	private int isOpen = 1;// 是否打开

	private String pmenuId = "";// 上级菜单id

	private String pmenuName = "无";// 上级菜单名称

	private int chlMenuNum = -1;// 下级菜单数量

	private String toPage = "";// 去向页面

	private int topFlag = 0;// 是否可放置在top菜单里；0：不可；1：可以

	private int seq = 0;

	private String dtIndex = "2";// 桌面索引

	private int count = 0;// 下级数量

	public MenuPojo() {

	}

	public MenuPojo(String id) {
		this.menuId = id;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuDes() {
		return menuDes;
	}

	public void setMenuDes(String menuDes) {
		this.menuDes = menuDes;
	}

	public String getJsfun() {
		return jsfun;
	}

	public void setJsfun(String jsfun) {
		this.jsfun = jsfun;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public String getPmenuId() {
		return pmenuId;
	}

	public void setPmenuId(String pmenuId) {
		this.pmenuId = pmenuId;
	}

	public String getPmenuName() {
		return pmenuName;
	}

	public void setPmenuName(String pmenuName) {
		this.pmenuName = pmenuName;
	}

	public int getChlMenuNum() {
		return chlMenuNum;
	}

	public void setChlMenuNum(int chlMenuNum) {
		this.chlMenuNum = chlMenuNum;
	}

	public String getToPage() {
		return toPage;
	}

	public void setToPage(String toPage) {
		this.toPage = toPage;
	}

	public int getTopFlag() {
		return topFlag;
	}

	public void setTopFlag(int topFlag) {
		this.topFlag = topFlag;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getMenuClass() {
		return menuClass;
	}

	public void setMenuClass(int menuClass) {
		this.menuClass = menuClass;
	}

	public String getDtIndex() {
		return dtIndex;
	}

	public void setDtIndex(String dtIndex) {
		this.dtIndex = dtIndex;
	}

	public int getMenuStyle() {
		return menuStyle;
	}

	public void setMenuStyle(int menuStyle) {
		this.menuStyle = menuStyle;
	}

	public int getMenuState() {
		return menuState;
	}

	public void setMenuState(int menuState) {
		this.menuState = menuState;
	}

}