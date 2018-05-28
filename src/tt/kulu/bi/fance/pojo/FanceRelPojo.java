package tt.kulu.bi.fance.pojo;

/**
 * <p>
 * 标题: FanceRelPojo
 * </p>
 * <p>
 * 功能描述: 围栏关系。
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
public class FanceRelPojo {
	public static final String TYPE_NAME[] = { "共用", "车用", "人用" };
	private String fanceId = "";
	private int type = 0;
	private String relId = "";

	public FanceRelPojo(String fanceId, String relId, int type) {
		this.fanceId = fanceId;
		this.relId = relId;
		this.type = type;
	}

	public String getFanceId() {
		return fanceId;
	}

	public void setFanceId(String fanceId) {
		this.fanceId = fanceId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRelId() {
		return relId;
	}

	public void setRelId(String relId) {
		this.relId = relId;
	}

}
