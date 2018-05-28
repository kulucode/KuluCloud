package tt.kulu.bi.watch.pojo;

public class LbsData {

	private int mcc = 0;
	private int mnc = 0;
	private int lac = 0;
	private int cell_id = 0;
	private int rssi = 0;

	public int getMcc() {
		return mcc;
	}

	public void setMcc(int mcc) {
		this.mcc = mcc;
	}

	public int getMnc() {
		return mnc;
	}

	public void setMnc(int mnc) {
		this.mnc = mnc;
	}

	public int getLac() {
		return lac;
	}

	public void setLac(int lac) {
		this.lac = lac;
	}

	public int getCell_id() {
		return cell_id;
	}

	public void setCell_id(int cell_id) {
		this.cell_id = cell_id;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
}
