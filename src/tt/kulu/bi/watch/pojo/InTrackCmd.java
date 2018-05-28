package tt.kulu.bi.watch.pojo;

import java.util.ArrayList;

/**
 * （cmd：43）轨迹报文（WIFI+LBS）
 * 
 * @author tangbin
 * 
 */
public class InTrackCmd {
	private int baseStationNum = 0;
	private ArrayList<LbsData> lbsData = null;
	private int wifiNum = 0;
	private ArrayList<WifiData> wifiData = null;
	private String type = "43";

	public int getBaseStationNum() {
		return baseStationNum;
	}

	public void setBaseStationNum(int baseStationNum) {
		this.baseStationNum = baseStationNum;
	}

	public ArrayList<LbsData> getLbsData() {
		return lbsData;
	}

	public void setLbsData(ArrayList<LbsData> lbsData) {
		this.lbsData = lbsData;
	}

	public int getWifiNum() {
		return wifiNum;
	}

	public void setWifiNum(int wifiNum) {
		this.wifiNum = wifiNum;
	}

	public ArrayList<WifiData> getWifiData() {
		return wifiData;
	}

	public void setWifiData(ArrayList<WifiData> wifiData) {
		this.wifiData = wifiData;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
