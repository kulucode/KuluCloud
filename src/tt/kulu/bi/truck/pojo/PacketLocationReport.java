package tt.kulu.bi.truck.pojo;

import java.util.HashMap;

import net.sf.json.JSONObject;

public class PacketLocationReport {
	public int alertFlag;
	public int status;
	public double latitude;
	public double longitude;
	public int altitude;// m
	public int speed;// 0.1km/h
	public int direction;// 0-359顺时针
	public String time;// BCD[6] YY-MM-DD-hh-mm-ss
	public String timeStamp;// YYYY-MM-DD hh:mm:ss
	public JSONObject extraMsgList = new JSONObject();// 位置附加信息项列表
	public float oilLevel = 0;// 单位mm
	public float oilDeff = 0;//
	public double thisOilV = 0;// 当前容积
	public int isAccOn;// AccOn表示车在工作
	public boolean validPacket;//
	private double valume = 0;// 容积差

	public int getAlertFlag() {
		return alertFlag;
	}

	public void setAlertFlag(int alertFlag) {
		this.alertFlag = alertFlag;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public float getOilLevel() {
		return oilLevel;
	}

	public void setOilLevel(float oilLevel) {
		this.oilLevel = oilLevel;
	}

	public int getIsAccOn() {
		return isAccOn;
	}

	public void setIsAccOn(int isAccOn) {
		this.isAccOn = isAccOn;
	}

	public JSONObject getExtraMsgList() {
		return extraMsgList;
	}

	public void setExtraMsgList(JSONObject extraMsgList) {
		this.extraMsgList = extraMsgList;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isValidPacket() {
		return validPacket;
	}

	public void setValidPacket(boolean validPacket) {
		this.validPacket = validPacket;
	}

	public float getOilDeff() {
		return oilDeff;
	}

	public void setOilDeff(float oilDeff) {
		this.oilDeff = oilDeff;
	}

	public double getValume() {
		return valume;
	}

	public void setValume(double valume) {
		this.valume = valume;
	}

	public double getThisOilV() {
		return thisOilV;
	}

	public void setThisOilV(double thisOilV) {
		this.thisOilV = thisOilV;
	}

}
