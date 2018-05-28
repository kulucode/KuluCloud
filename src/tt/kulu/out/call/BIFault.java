package tt.kulu.out.call;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.fault.dbclass.FaultDBMang;
import tt.kulu.bi.fault.pojo.FaultReportPojo;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BIFault
 * </p>
 * <p>
 * 功能描述: 故障报警业务接口类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2007-5-15
 * </p>
 */
public class BIFault extends BSDBBase {
	public BIFault(BSObject m_bs) throws Exception {
		super(null, m_bs);
	}

	public BIFault(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getFaultReportList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品实例列表。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输入参数描述: long f：输入的开始位置。
	 * </p>
	 * <p>
	 * 输入参数描述: long t：输入的结束位置。
	 * </p>
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<FaultReportPojo> getFaultReportList(JSONObject paras,
			long f, long t) throws Exception {
		ArrayList<FaultReportPojo> list = new ArrayList<FaultReportPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getFaultReportList(sqlHelper, paras, f, t);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getFaultReportList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品实例列表。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输入参数描述: long f：输入的开始位置。
	 * </p>
	 * <p>
	 * 输入参数描述: long t：输入的结束位置。
	 * </p>
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<FaultReportPojo> getFaultReportList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.FR_HAPPENTIME desc";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t.FR_REPORTID=? OR t.FR_NAME like ? OR t.FR_CONTENT like ? OR frt.PLATE_NUM like ? OR fru.USER_NAME like ? OR fru.MPHONE like ?)";
					vList.add(v);
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("state")) {
					// 根
					where += " and t.OP_STATE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("fault")) {
					// 根
					where += " and t.FAULT_ID=?";
					vList.add(v);
				}
				if (key.equals("truck")) {
					// 根
					where += " and t.FR_TRUCK=?";
					vList.add(v);
				}
				if (key.equals("user")) {
					// 根
					where += " and t.FR_USER=?";
					vList.add(v);
				}
				if (key.equals("type")) {
					// 根
					where += " and t.FR_TYPE in (";
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += ",?";
							vList.add(oneV);
						}
					}
					where += ((whereEx.equals("") ? "" : whereEx.substring(1)) + ")");
				}
				if (key.equals("from")) {
					// 根
					where += " and t.FR_FROM=?";
					vList.add(v);
				}
				if (key.equals("hdate")) {
					String vs[] = v.split(",");
					if (vs.length > 1 && !vs[0].equals("") && !vs[1].equals("")) {
						where += " and t.FR_HAPPENTIME BETWEEN ? AND ?";
						vList.add(Timestamp.valueOf(vs[0]));
						vList.add(Timestamp.valueOf(vs[1]));
					}
				}
				if (key.equals("opdate")) {
					String vs[] = v.split(",");
					if (vs.length > 1 && !vs[0].equals("") && !vs[1].equals("")) {
						where += " and t.OP_DATE BETWEEN ? AND ?";
						vList.add(Timestamp.valueOf(vs[0]));
						vList.add(Timestamp.valueOf(vs[1]));
					}
				}
				if (key.equals("tgroup")) {
					where += " and frt.ORG_ID in "
							+ URLlImplBase.LOGIN_GROUP_WHERE;
					vList.add("%," + v + "%");
				}
				if (key.equals("tlogin")) {
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += (whereEx.equals("") ? "" : " or ")
									+ "frt.ORG_ID in "
									+ URLlImplBase.LOGIN_GROUP_WHERE;
							vList.add("%," + oneV + "%");
						}
					}
					if (!whereEx.equals("")) {
						where += " and (" + whereEx + ")";
					}
				}
				if (key.equals("uroup")) {
					where += " and fru.ORG_ID in "
							+ URLlImplBase.LOGIN_GROUP_WHERE;
					vList.add("%," + v + "%");
				}
				if (key.equals("ulogin")) {
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += (whereEx.equals("") ? "" : " or ")
									+ "fru.ORG_ID in "
									+ URLlImplBase.LOGIN_GROUP_WHERE;
							vList.add("%," + oneV + "%");
						}
					}
					if (!whereEx.equals("")) {
						where += " and (" + whereEx + ")";
					}
				}
			}
		}
		FaultDBMang faultDB = new FaultDBMang(sqlHelper, m_bs);
		paras.put("max", faultDB.getFaultReportCount(where, vList));
		return faultDB.getFaultReportList(where, orderBy, vList, f, t);
	}

	/**
	 * <p>
	 * 方法名称: getOneFaultReportById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public FaultReportPojo getOneFaultReportById(String id) throws Exception {
		FaultReportPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneFaultReportById(sqlHelper, id);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneFaultReportById
	 * </p>
	 * <p>
	 * 方法功能描述: 根据ID得到单个物品实例。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public FaultReportPojo getOneFaultReportById(SqlExecute sqlHelper, String id)
			throws Exception {
		FaultDBMang faultDB = new FaultDBMang(sqlHelper, m_bs);
		return faultDB.getOneFaultReportById(id);
	}

	/**
	 * <p>
	 * 方法名称: insertTruck
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertFaultReport(FaultReportPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertFaultReport(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertFaultReport(SqlExecute sqlHelper, FaultReportPojo onePojo)
			throws Exception {
		FaultDBMang faultDB = new FaultDBMang(sqlHelper, m_bs);
		return faultDB.insertFaultReport(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateFaultReport
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateFaultReport(FaultReportPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateFaultReport(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertTruckDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateFaultReport(SqlExecute sqlHelper, FaultReportPojo onePojo)
			throws Exception {
		FaultDBMang faultDB = new FaultDBMang(sqlHelper, m_bs);
		return faultDB.updateFaultReport(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateFaultReport
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑单个车辆定义数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateFaultReportOP(FaultReportPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateFaultReportOP(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateFaultReportOP
	 * </p>
	 * <p>
	 * 方法功能描述: 新增单个车辆定义数据。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、菜单实体对象oneMenu
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateFaultReportOP(SqlExecute sqlHelper, FaultReportPojo onePojo)
			throws Exception {
		FaultDBMang faultDB = new FaultDBMang(sqlHelper, m_bs);
		return faultDB.updateFaultReportOP(onePojo);
	}
}
