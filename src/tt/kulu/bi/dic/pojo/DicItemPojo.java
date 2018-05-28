package tt.kulu.bi.dic.pojo;

/**
 * <p>
 * 标题: DicItemPojo
 * </p>
 * <p>
 * 功能描述: 数据字典项目类。
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
public class DicItemPojo {
	private DicPojo dic = new DicPojo();
	private String id = "";
	private String name = "";
	private String value = "";//
	private String value2 = "";//
	private int index = 0;
	private String pitemid = "";

	public DicPojo getDic() {
		return dic;
	}

	public void setDic(DicPojo dic) {
		this.dic = dic;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getPitemid() {
		return pitemid;
	}

	public void setPitemid(String pitemid) {
		this.pitemid = pitemid;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

}
