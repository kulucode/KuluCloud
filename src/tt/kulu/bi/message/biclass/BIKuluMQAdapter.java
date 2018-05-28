package tt.kulu.bi.message.biclass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.tt4j2ee.BSDateEx;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.fault.pojo.FaultReportPojo;
import tt.kulu.bi.map.biclass.CoordTransformBI;
import tt.kulu.bi.map.pojo.Gps;
import tt.kulu.bi.storage.pojo.EquipmentGeometryPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstPojo;
import tt.kulu.bi.storage.pojo.EquipmentInstWorkLogPojo;
import tt.kulu.bi.truck.pojo.PacketLocationReport;
import tt.kulu.bi.truck.pojo.TruckWorkDayLogsPojo;
import tt.kulu.bi.watch.pojo.InBloodPressureOkCmd;
import tt.kulu.bi.watch.pojo.InHeartRateCmd;
import tt.kulu.bi.watch.pojo.InStepCmd;
import tt.kulu.out.call.BIDic;
import tt.kulu.out.call.BIEquipment;
import tt.kulu.out.call.BIFance;
import tt.kulu.out.call.BIFault;
import tt.kulu.out.call.BIRedis;
import tt.kulu.out.call.BITruck;
import tt.kulu.out.call.BIUser;
import tt.kulu.out.call.BIWatch;

/**
 * <p>
 * 标题: BIKuluMQAdapter
 * </p>
 * <p>
 * 功能描述: 酷路跨服务适配类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2011-1-25
 * </p>
 */
public class BIKuluMQAdapter {
	private BSDateEx bsDate = new BSDateEx();

	/**
	 * <p>
	 * 方法名称: VehicleGeoToRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 车辆报文缓冲处理
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public void VehicleGeoToRedis(String msg) throws Exception {
		JSONObject _oldData = JSONObject.fromObject(URLlImplBase
				.decodeUnicode(msg));
		JSONObject bodyData = _oldData.getJSONObject("databody");
		if (_oldData.getString("tag").equals("data")) {
			EquipmentInstPojo oneEqp = new BIEquipment(null, null)
					.getOneEquipmentInstByToken(_oldData.getString("eqpid"));
			if (oneEqp != null) {
				_oldData.put("token", _oldData.getString("eqpid"));
				_oldData.put("eqpid", oneEqp.getInstId());
				BIDic dicBI = new BIDic(null, null);
				BIRedis redisBI = new BIRedis();
				// 配置缓冲条数
				DicItemPojo vrItem = dicBI
						.getDicItemByRedis("SYS_BASE_CONF_VEHICLE_REDISCOUNT");
				long count = 10;
				if (vrItem != null && !vrItem.getValue().equals("")) {
					count = Long.parseLong(vrItem.getValue());
				}
				if (count > 0) {
					long time = bodyData.getJSONObject("location").getLong(
							"time");
					redisBI.setSortedSet(
							"KVEHICLE_BUFFER_" + oneEqp.getInstId(), time,
							_oldData.toString(), URLlImplBase.REDIS_KULUDATA);
					//
					if (redisBI.getSortedSetCount(
							"KVEHICLE_BUFFER_" + oneEqp.getInstId(),
							URLlImplBase.REDIS_KULUDATA) > count) {
						// 取出最后一条入库
						ArrayList<String> values = redisBI.getSortedSetValue(
								"KVEHICLE_BUFFER_" + oneEqp.getInstId(), 0, 0,
								0, URLlImplBase.REDIS_KULUDATA);
						for (String oneMsg : values) {
							if (!oneMsg.equals("")) {
								BIKuluMQAdapter doMsg = new BIKuluMQAdapter();
								doMsg.doTRUCKSERV_TO_CLOUD_MQ(oneMsg);
							}
						}
						redisBI.removeSortedSet(
								"KVEHICLE_BUFFER_" + oneEqp.getInstId(), 0, 0,
								URLlImplBase.REDIS_KULUDATA);
					}
				} else {
					BIKuluMQAdapter doMsg = new BIKuluMQAdapter();
					doMsg.doTRUCKSERV_TO_CLOUD_MQ(msg);
				}
			}

		} else {
			BIKuluMQAdapter doMsg = new BIKuluMQAdapter();
			doMsg.doTRUCKSERV_TO_CLOUD_MQ(msg);
		}
		return;
	}

	/**
	 * <p>
	 * 方法名称: clearVehicleGeoToFromRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 清理缓冲报文
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public void clearVehicleGeoToFromRedis(String eqpId) throws Exception {
		BIRedis redisBI = new BIRedis();
		long count = redisBI.getSortedSetCount("KVEHICLE_BUFFER_" + eqpId,
				URLlImplBase.REDIS_KULUDATA);
		if (count > 0) {
			ArrayList<String> values = redisBI.getSortedSetValue(
					"KVEHICLE_BUFFER_" + eqpId, 0, 0, count - 1,
					URLlImplBase.REDIS_KULUDATA);
			for (String oneMsg : values) {
				if (!oneMsg.equals("")) {
					BIKuluMQAdapter doMsg = new BIKuluMQAdapter();
					doMsg.doTRUCKSERV_TO_CLOUD_MQ(oneMsg);
				}
			}
		}
		redisBI.delData("KVEHICLE_BUFFER_" + eqpId, URLlImplBase.REDIS_KULUDATA);

		return;
	}

	// 手环来的消息
	public void doWINGSERV_TO_CLOUD_MQ(String inMessage) throws Exception {
		// 手环终端服务到云服务
		try {
			JSONObject _oldData = JSONObject.fromObject(inMessage);
			JSONObject bodyData = _oldData.getJSONObject("databody");
			String time = _oldData.getString("date");// 消息体里的时间
			JSONObject _sendMQ = new JSONObject();
			JSONObject _sendBodyMQ = new JSONObject();
			BIEquipment eqpBI = new BIEquipment(null, null);
			BIDic dicBI = new BIDic(null, null);
			BIUser userBI = new BIUser(null, null);
			BIFault faultBI = new BIFault(null, null);
			BIFance fanceBI = new BIFance(null, null);
			BIWatch watchBI = new BIWatch(null, null);
			EquipmentInstPojo oneInst = new EquipmentInstPojo();
			EquipmentGeometryPojo oneEqpGeo = new EquipmentGeometryPojo();
			switch (_oldData.getString("tag")) {
			case "login":
				// 判断是否存在，不存在则更新数据库
				oneInst.setLastLoginDate(time);
				oneInst.setName("手表");
				oneInst.getEqpDef().setId("WATCH_01");
				oneInst.setWyCode(_oldData.getString("eqpid"));
				oneInst.setQrCode(oneInst.getWyCode());
				oneInst.setState(0);
				oneInst.setOnlineState(1);
				oneInst.setThisDate(time);
				oneInst.setUpdateDate(this.bsDate.getThisDate(0, 0));
				oneInst.setProDate(oneInst.getUpdateDate());

				if (eqpBI.equipmentInstLogin(oneInst) > 0) {
					// 登录
					// 得到手表设备参数
					_sendMQ.clear();
					_sendBodyMQ.clear();
					JSONArray wp = dicBI.getDicItemListByRedis("WATCH_PARAS");
					for (int i = 0; i < wp.size(); i++) {
						JSONObject oneWP = wp.getJSONObject(i);
						_sendBodyMQ.put(oneWP.getString("value"),
								oneWP.getString("value2"));
					}
					_sendMQ.put("eqpid", _oldData.getString("eqpid"));
					_sendMQ.put("tag", "login_back");
					_sendMQ.put("date", this.bsDate.getThisDate(0, 0));
					_sendBodyMQ.put("r", 0);
					_sendMQ.put("databody", _sendBodyMQ);
					_sendMQ.put("indata", _oldData.getJSONObject("indata"));
					MQSender mqSend = new MQSender(
							URLlImplBase.MQTopicM.get(2), _sendMQ.toString());
					mqSend.run();
				}
				break;
			case "login_out":
				// 判断是否存在，不存在则更新数据库
				oneInst = eqpBI.getOneEquipmentInstById(_oldData
						.getString("eqpid"));
				if (oneInst != null) {
					oneInst.setLastLoginDate(time);
					oneInst.setThisDate(time);
					oneInst.setWyCode(_oldData.getString("eqpid"));
					if (eqpBI.equipmentInstLoginOut(oneInst) > 0) {
						// 往页面推送位置信息
						bodyData.clear();
						bodyData.put("opfun", "loginout");
						bodyData.put("r", 0);
						bodyData.put("eqpid", oneInst.getInstId());
						BITTWebSocketSend webSend = new BITTWebSocketSend(
								bodyData.toString(), "02");
						webSend.run();
					}
				}
				break;
			case "data":
				// 数据
				switch (bodyData.getString("type")) {
				case "6":// GPS
					// 写入位置表
					oneEqpGeo.getEqpInst().setWyCode(
							_oldData.getString("eqpid"));
					// oneEqpGeo.getEqpInst().getEqpDef().setId("WATCH_01");
					oneEqpGeo.setCreateDate(time);
					oneEqpGeo.setSysDate(this.bsDate.getThisDate(0, 0));
					String geos[] = bodyData.getString("location").split(";");
					oneEqpGeo.setLatitude(geos[0]);
					oneEqpGeo.setLongitude(geos[1]);
					oneEqpGeo.getEqpInst().setThisDate(time);
					boolean geoOk = false;
					if ((int) (Float.parseFloat(geos[0])) - 0 > 0
							&& (int) (Float.parseFloat(geos[1])) - 0 > 0) {
						Gps bdGPS = null;
						try {
							if (!oneEqpGeo.getLatitude().equals("")
									&& !oneEqpGeo.getLongitude().equals("")) {
								bdGPS = CoordTransformBI
										.Gps84_To_bd09(Float.valueOf(oneEqpGeo
												.getLatitude()), Float
												.valueOf(oneEqpGeo
														.getLongitude()));
								geoOk = true;
							}
						} catch (Exception ex) {
							geoOk = false;
							bdGPS = null;
						}
						if (geoOk
								&& eqpBI.insertEquipmentGeometry(oneEqpGeo) > 0) {
							// 判断是否超出围栏
							fanceBI.doCheckPoineInFanceByUser(oneEqpGeo);
							// 更新员工工作日志中的路程
							userBI.updateUserWorkDayLogsDis(oneEqpGeo);
							// 往页面推送位置信息
							bodyData.clear();
							bodyData.put("opfun", "gps");
							bodyData.put("r", 0);
							bodyData.put("eqpid", oneEqpGeo.getEqpInst()
									.getInstId());
							bodyData.put("lat", oneEqpGeo.getLatitude());
							bodyData.put("lon", oneEqpGeo.getLongitude());
							if (bdGPS != null) {
								bodyData.put("bdlat", bdGPS.getWgLat());
								bodyData.put("bdlon", bdGPS.getWgLon());
							} else {
								bodyData.put("bdlat", "");
								bodyData.put("bdlon", "");
							}
							bodyData.put("fanceflg", oneEqpGeo.getFanceFlg());
							bodyData.put("geodate", time);
							BITTWebSocketSend webSend = new BITTWebSocketSend(
									bodyData.toString(), "02");
							webSend.run();
						}
					}
					break;
				case "13":// 记步
					InStepCmd step = new InStepCmd();
					step.setStep(bodyData.getString("step"));
					step.getEqpInst().setWyCode(_oldData.getString("eqpid"));
					step.setCreateDate(time);
					if (watchBI.insertWatchStep(step) > 0) {
						// 往页面推送位置信息
						bodyData.clear();
						bodyData.put("opfun", "step");
						bodyData.put("r", 0);
						bodyData.put("eqpid", oneEqpGeo.getEqpInst()
								.getInstId());
						bodyData.put("step", step.getStep());
						bodyData.put("stepdate", time);
						BITTWebSocketSend webSend = new BITTWebSocketSend(
								bodyData.toString(), "02");
						webSend.run();
					}
					break;
				case "110":// 血压正常
					InBloodPressureOkCmd bpo = new InBloodPressureOkCmd();
					bpo.setHigh(bodyData.getString("high"));
					bpo.setLow(bodyData.getString("low"));
					bpo.getEqpInst().setWyCode(_oldData.getString("eqpid"));
					bpo.setCreateDate(time);
					if (watchBI.insertWatchBloodPressureOK(bpo) > 0) {
						// 往页面推送位置信息
						bodyData.clear();
						bodyData.put("opfun", "bpo");
						bodyData.put("r", 0);
						bodyData.put("eqpid", oneEqpGeo.getEqpInst()
								.getInstId());
						bodyData.put("bpoh", bpo.getHigh());
						bodyData.put("bpol", bpo.getLow());
						bodyData.put("bpodate", time);
						BITTWebSocketSend webSend = new BITTWebSocketSend(
								bodyData.toString(), "02");
						webSend.run();
					}
					break;
				case "14":// 心率正常
					InHeartRateCmd hr = new InHeartRateCmd();
					hr.setHeartRate(bodyData.getString("heartRate"));
					hr.setElectricity(bodyData.getString("electricity"));
					hr.getEqpInst().setWyCode(_oldData.getString("eqpid"));
					hr.setCreateDate(time);
					if (watchBI.insertWatchInHeartRate(hr) > 0) {
						// 往页面推送位置信息
						bodyData.clear();
						bodyData.put("opfun", "hr");
						bodyData.put("r", 0);
						bodyData.put("eqpid", hr.getEqpInst().getInstId());
						bodyData.put("hr", hr.getHeartRate());
						bodyData.put("hrdate", time);
						BITTWebSocketSend webSend = new BITTWebSocketSend(
								bodyData.toString(), "02");
						webSend.run();
					}
					break;
				case "99":// 心率报警
					hr = new InHeartRateCmd();
					hr.setHeartRate(bodyData.getString("value"));
					// hr.setElectricity(bodyData.getString("electricity"));
					hr.getEqpInst().setWyCode(_oldData.getString("eqpid"));
					hr.setCreateDate(time);
					if (watchBI.insertWatchInHeartRateError(hr) > 0) {
						// 报警故障
						FaultReportPojo frPojo = new FaultReportPojo();
						frPojo.setEqpInst(hr.getEqpInst());
						frPojo.setName("心率异常报警");
						frPojo.getFaultFrom().setId(
								"8E0642C7E4C9C13FE6F5C55EC1555E7E");
						frPojo.getFaultType().setId(
								"2B27A3287D25F3EA18C8FDAFA1EEB25F");
						frPojo.getFaultCode().setId(
								"31131AB6D99083E17B3E422D9DC7C0A6");
						frPojo.setContent("心率为" + hr.getHeartRate());
						frPojo.setFrUser(hr.getEqpInst().getMangUser());
						frPojo.setHappenDate(time);
						frPojo.setCreateUser(hr.getEqpInst().getMangUser());
						faultBI.insertFaultReport(frPojo);
						// 往页面推送位置信息
						bodyData.clear();
						bodyData.put("opfun", "hrerr");
						bodyData.put("r", 0);
						bodyData.put("eqpid", hr.getEqpInst().getInstId());
						bodyData.put("hr", hr.getHeartRate());
						bodyData.put("hrdate", time);
						BITTWebSocketSend webSend = new BITTWebSocketSend(
								bodyData.toString(), "02");
						webSend.run();
					}
					break;
				case "113":// 血压报警
					bpo = new InBloodPressureOkCmd();
					bpo.setHigh(bodyData.getString("high"));
					bpo.setLow(bodyData.getString("low"));
					bpo.getEqpInst().setWyCode(_oldData.getString("eqpid"));
					bpo.setCreateDate(time);
					if (watchBI.insertWatchBloodPressureError(bpo) > 0) {
						// 往页面推送位置信息
						bodyData.clear();
						bodyData.put("opfun", "bpoerr");
						bodyData.put("r", 0);
						bodyData.put("eqpid", oneEqpGeo.getEqpInst()
								.getInstId());
						bodyData.put("bpoh", bpo.getHigh());
						bodyData.put("bpol", bpo.getLow());
						bodyData.put("bpodate", time);
						// webSend.sendData(bodyData.toString(), "02");
					}
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		} catch (Exception ep) {
			ep.printStackTrace();
		}
	}

	// 车载来的消息
	public void doTRUCKSERV_TO_CLOUD_MQ(String inMessage) {
		// 车载终端服务到云服务
		String timesID = this.bsDate.getSeqDate();
		System.out.println("");
		System.out.println("[" + timesID + "]---start [TRUCKSERV_TO_CLOUD]:"
				+ inMessage);
		try {
			EquipmentInstPojo oneInst = new EquipmentInstPojo();
			BIDic dicBI = new BIDic(null, null);
			BIEquipment eqpBI = new BIEquipment(null, null);
			BITruck truckBI = new BITruck(null, null);
			JSONObject _oldData = JSONObject.fromObject(URLlImplBase
					.decodeUnicode(inMessage));
			JSONObject bodyData = _oldData.getJSONObject("databody");
			EquipmentGeometryPojo oneEqpGeo = new EquipmentGeometryPojo();
			TruckWorkDayLogsPojo oneWork = new TruckWorkDayLogsPojo();
			String time = "";// 消息体里的时间
			if (bodyData.containsKey("time")) {
				time = this.bsDate.getCalendarToStringAll(URLlImplBase
						.getCalendarFromDateStr(
								"20" + bodyData.getString("time"),
								"yyyyMMddhhmmss"));
			} else {
				time = _oldData.getString("date");
			}
			JSONObject _sendMQ = new JSONObject();
			JSONObject _sendBodyMQ = new JSONObject();
			switch (_oldData.getString("tag")) {
			case "reg":
				// 判断是否存在，不存在则更新数据库
				oneInst.setLastLoginDate(time);
				oneInst.setName("车载");
				if (_oldData.containsKey("eqpdef")) {
					oneInst.getEqpDef().setId(_oldData.getString("eqpdef"));
				} else {
					oneInst.getEqpDef().setId("TRUCK_01");
				}
				// oneInst.setWyCode(BSGuid.getRandomGUID());

				oneInst.setState(0);
				oneInst.setOnlineState(1);
				oneInst.setUpdateDate(this.bsDate.getThisDate(0, 0));
				oneInst.setProDate(oneInst.getUpdateDate());
				oneInst.setThisDate(time);
				if (bodyData.containsKey("termType")) {
					oneInst.getEqpDef().setNo(bodyData.getString("termType"));
				}
				if (bodyData.containsKey("plateNumber")) {
					oneInst.getTruck().setPlateNum(
							bodyData.getString("plateNumber"));
				}
				if (bodyData.containsKey("carVIN")) {
					oneInst.getTruck().setCjNo(bodyData.getString("carVIN"));
				}
				oneInst.setWyCode(bodyData.getString("termType") + "|"
						+ bodyData.getString("producerId") + "|"
						+ _oldData.getString("eqpid"));
				oneInst.setQrCode(oneInst.getWyCode());
				_sendMQ.clear();
				_sendBodyMQ.clear();
				if (!oneInst.getEqpDef().getNo().equals("")
						&& truckBI.vehicleReg(oneInst) > 0) {
					// 必须要有型号
					JSONArray wp = dicBI.getDicItemListByRedis("VEHICLE_PARAS");
					for (int i = 0; i < wp.size(); i++) {
						JSONObject oneWP = wp.getJSONObject(i);
						_sendBodyMQ.put(oneWP.getString("value"),
								oneWP.getInt("value2"));
					}
					_sendBodyMQ.put("r", 0);
					_sendBodyMQ.put("token", oneInst.getToken());
				} else {
					_sendBodyMQ.put("token", "");
					_sendBodyMQ.put("r", 1001);
					_sendBodyMQ.put("error",
							URLlImplBase.ErrorMap.get(_sendBodyMQ.getInt("r")));
				}
				_sendMQ.put("eqpid", _oldData.getString("eqpid"));
				_sendMQ.put("tag", "reg_back");
				_sendMQ.put("date", this.bsDate.getThisDate(0, 0));
				_sendMQ.put("databody", _sendBodyMQ);
				_sendMQ.put("indata", _oldData.getJSONObject("indata"));
				MQSender mqSend = new MQSender(URLlImplBase.MQTopicM.get(0),
						_sendMQ.toString());
				mqSend.run();
				break;
			case "login":
				// 登录
				_sendMQ.clear();
				_sendBodyMQ.clear();
				_sendMQ.put("eqpid", _oldData.getString("eqpid"));
				_sendMQ.put("tag", "login_back");
				_sendMQ.put("date", time);
				oneInst = eqpBI.getOneEquipmentInstByToken(bodyData
						.getString("token"));
				if (oneInst != null) {
					_sendBodyMQ.put("r", 0);
					// 得到手表设备参数
					JSONArray wp = dicBI.getDicItemListByRedis("VEHICLE_PARAS");
					for (int i = 0; i < wp.size(); i++) {
						JSONObject oneWP = wp.getJSONObject(i);
						_sendBodyMQ.put(oneWP.getString("value"),
								oneWP.getInt("value2"));
					}
					oneInst.setThisDate(time);
					if (eqpBI.equipmentInstLogin(oneInst) > 0) {
						// 写入车辆日志 （开机时间）
						oneWork.setTruck(oneInst.getTruck());
						oneWork.setDate(time);
						oneWork.setType(0);
						oneWork.setOpType(1);
						oneWork.setWorkSDate(time);
						truckBI.updateTruckWorkDayLogs(oneWork);
					}
				} else {
					_sendBodyMQ.put("r", 1001);
					_sendBodyMQ.put("error",
							URLlImplBase.ErrorMap.get(_sendBodyMQ.getInt("r")));
				}
				_sendMQ.put("databody", _sendBodyMQ);
				_sendMQ.put("indata", _oldData.getJSONObject("indata"));
				mqSend = new MQSender(URLlImplBase.MQTopicM.get(0),
						_sendMQ.toString());
				mqSend.run();
				break;
			case "authKeyLogin":
				// 重连
				_sendMQ.clear();
				_sendBodyMQ.clear();
				_sendMQ.put("eqpid", _oldData.getString("eqpid"));
				_sendMQ.put("tag", "authKeyLogin_back");
				_sendMQ.put("date", this.bsDate.getThisDate(0, 0));
				oneInst = eqpBI.getOneEquipmentInstByToken(bodyData
						.getString("token"));
				if (oneInst != null) {
					clearVehicleGeoToFromRedis(oneInst.getInstId());
					_sendBodyMQ.put("r", 0);
					oneInst.setThisDate(time);
					if (eqpBI.equipmentInstLogin(oneInst) > 0) {
						// 写入车辆日志 （开机时间）
						oneWork.setTruck(oneInst.getTruck());
						oneWork.setDate(time);
						oneWork.setType(0);
						oneWork.setOpType(1);
						oneWork.setWorkSDate(time);
						truckBI.updateTruckWorkDayLogs(oneWork);
						// 发送配置信息
						JSONArray wp = dicBI
								.getDicItemListByRedis("VEHICLE_PARAS");
						for (int i = 0; i < wp.size(); i++) {
							JSONObject oneWP = wp.getJSONObject(i);
							_sendBodyMQ.put(oneWP.getString("value"),
									oneWP.getInt("value2"));
						}

					}
				} else {
					_sendBodyMQ.put("r", 4);
					_sendBodyMQ.put("error",
							URLlImplBase.ErrorMap.get(_sendBodyMQ.getInt("r")));
				}
				_sendMQ.put("databody", _sendBodyMQ);
				_sendMQ.put("indata", _oldData.getJSONObject("indata"));
				mqSend = new MQSender(URLlImplBase.MQTopicM.get(0),
						_sendMQ.toString());
				mqSend.run();
				break;
			case "login_out":
				// 判断是否存在，不存在则更新数据库
				oneInst = eqpBI.getOneEquipmentInstByToken(bodyData
						.getString("token"));
				if (oneInst != null) {
					clearVehicleGeoToFromRedis(oneInst.getInstId());
					oneInst.setLastLoginDate(time);
					oneInst.setThisDate(time);
					if (eqpBI.equipmentInstLoginOut(oneInst) > 0) {
						// 车辆工作时长录入
						if (oneInst.getTruck() != null
								&& !oneInst.getTruck().getId().equals("")) {
							// 得到上次开机时间
							EquipmentInstWorkLogPojo openLog = eqpBI
									.getLastEquipmentInstWorkLog(
											oneInst.getInstId(), -1, 1);
							if (openLog != null
									&& openLog.getState() == 1
									&& this.bsDate.getDateMillCount(
											openLog.getDate(),
											oneInst.getThisDate()) > 0) {
								truckBI.updateTruckWorkTime(oneInst.getTruck()
										.getId(), openLog.getDate(), oneInst
										.getThisDate());
							}
						}
						// 往页面推送位置信息
						bodyData.clear();
						bodyData.put("opfun", "loginout");
						bodyData.put("r", 0);
						bodyData.put("eqpid", oneInst.getInstId());
						BITTWebSocketSend webSend = new BITTWebSocketSend(
								bodyData.toString(), "01");
						webSend.run();
					}
				}
				break;
			case "data":
				// 数据
				_sendMQ.clear();
				_sendBodyMQ.clear();
				switch (bodyData.getString("type")) {
				case "0":// 数据
					if (bodyData.containsKey("location")) {
						PacketLocationReport oneD = (PacketLocationReport) JSONObject
								.toBean(bodyData.getJSONObject("location"),
										PacketLocationReport.class);
						time = this.bsDate.getCalendarToStringAll(URLlImplBase
								.getCalendarFromDateStr("20" + oneD.getTime(),
										"yyyyMMddhhmmss"));
						// 写入位置表
						oneEqpGeo.setEqpInst(eqpBI
								.getOneEquipmentInstByToken(_oldData
										.getString("eqpid")));
						if (oneEqpGeo.getEqpInst() != null) {
							// oneEqpGeo.getEqpInst().getEqpDef().setId("WATCH_01");
							// Calendar calendar = Calendar.getInstance();
							// calendar.setTimeInMillis(Long.valueOf(oneD
							// .getTimeStamp()));
							oneEqpGeo.setCreateDate(time);
							oneEqpGeo.setLatitude(String.valueOf(oneD
									.getLatitude()));
							oneEqpGeo.setLongitude(String.valueOf(oneD
									.getLongitude()));
							boolean geoOk = false;
							if (Math.abs(Float.valueOf(oneEqpGeo.getLatitude()) - 0) > 0
									&& Math.abs(Float.valueOf(oneEqpGeo
											.getLongitude()) - 0) > 0) {
								Gps bdGPS = null;
								try {
									if (!oneEqpGeo.getLatitude().equals("")
											&& !oneEqpGeo.getLongitude()
													.equals("")) {
										bdGPS = CoordTransformBI.Gps84_To_bd09(
												Float.valueOf(oneEqpGeo
														.getLatitude()),
												Float.valueOf(oneEqpGeo
														.getLongitude()));
										geoOk = true;
									}
								} catch (Exception ex) {
									geoOk = false;
									bdGPS = null;
								}
								if (geoOk
										&& truckBI.insertVehicleData(oneD,
												oneEqpGeo) > 0) {
									// 往页面推送位置信息
									bodyData.clear();
									bodyData.put("opfun", "gps");
									bodyData.put("r", 0);
									bodyData.put("eqpid", oneEqpGeo
											.getEqpInst().getInstId());
									bodyData.put("lat", oneEqpGeo.getLatitude());
									bodyData.put("lon",
											oneEqpGeo.getLongitude());
									if (bdGPS != null) {
										bodyData.put("bdlat", bdGPS.getWgLat());
										bodyData.put("bdlon", bdGPS.getWgLon());
									} else {
										bodyData.put("bdlat", "");
										bodyData.put("bdlon", "");
									}
									bodyData.put("oil", oneD.getOilLevel());
									bodyData.put("oildiff", oneD.getOilDeff());
									bodyData.put("speed", oneD.getSpeed());
									bodyData.put("fanceflg",
											oneEqpGeo.getFanceFlg());
									bodyData.put("geodate", time);
									BITTWebSocketSend webSend = new BITTWebSocketSend(
											bodyData.toString(), "01");
									webSend.run();
								}
							}
						} else {
							System.out.println("车载koken不存在:"
									+ _oldData.getString("eqpid"));
						}
					}

					break;
				default:
					break;
				}
			default:
				break;
			}
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			System.out.println("[" + timesID + "]---end over!!!");

		}
	}
}
