package tt.kulu.bi.dic.pojo;

/**
 * <p>
 * 标题: StaticPojo
 * </p>
 * <p>
 * 功能描述: 静态配置类。
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
public class StaticPojo {
	private String staticId = "";
	private String name = "";
	private String table = "";
	private String col = "";
	private String value = "";
	private String valueName = "";
	private String objectId = "";
	private String type = "";
	private String jsFun = "";
	private String desc = "";

	public StaticPojo() {
	}

	public String getCol() {
		return col;
	}

	public String getObjectId() {
		return objectId;
	}

	public String getStaticId() {
		return staticId;
	}

	public String getTable() {
		return table;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public String getValueName() {
		return valueName;
	}

	public String getJsFun() {
		return jsFun;
	}

	public String getDesc() {
		return desc;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setStaticId(String staticId) {
		this.staticId = staticId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public void setJsFun(String jsFun) {
		this.jsFun = jsFun;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
