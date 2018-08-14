package tt.kulu.out.call;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.IpUtils;
import tt.kulu.bi.logs.dbclass.MsgLogsDBMang;
import tt.kulu.bi.logs.dbclass.SysLogsDBMang;
import tt.kulu.bi.logs.pojo.MsgLogsPojo;
import tt.kulu.bi.logs.pojo.SysLogsPojo;
import tt.kulu.bi.user.pojo.OrgPojo;
import net.sf.json.JSONObject;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BISysLogsMang
 * </p>
 * <p>
 * 功能描述: 接口管理接口类
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
public class BILogs extends BSDBBase {
	public BILogs(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	public BILogs(BSObject m_bs) throws Exception {
		super(null, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getSysLogsList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到放行条件列表。
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
	public ArrayList<SysLogsPojo> getSysLogsList(JSONObject paras, long f,
			long t) throws Exception {
		ArrayList<SysLogsPojo> list = new ArrayList<SysLogsPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getSysLogsList(sqlHelper, paras, f, t);
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
	 * 方法名称: getRunMangLogList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到放行条件列表。
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
	public ArrayList<SysLogsPojo> getSysLogsList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		// 参数设定
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t.LOG_NAME like ? OR t.LOG_CONTENT like ? OR t.C_USER_MSG::text like ?)";
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("type")) {
					// 关键字
					where += " and t.LOG_TYPE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("date")) {
					// 关键字
					String dates[] = v.split(",");
					where += " and t.C_DATE BETWEEN ? and ?";
					vList.add(Timestamp.valueOf(dates[0]));
					vList.add(Timestamp.valueOf(dates[1]));
				}
			}
		}
		SysLogsDBMang logDB = new SysLogsDBMang(sqlHelper, m_bs);
		paras.put("max", logDB.getSysLogsCount(where, vList));
		return logDB.getSysLogsList(where, f, t, vList);
	}

	/**
	 * <p>
	 * 方法名称: insertIFLogs
	 * </p>
	 * <p>
	 * 方法功能描述: 写入接口数据。
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
	public int insertSysLogs(SysLogsPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertSysLogs(sqlHelper, onePojo);
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
	 * 方法名称: insertSysLogs
	 * </p>
	 * <p>
	 * 方法功能描述:写入系统日志。
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
	public int insertSysLogs(SqlExecute sqlHelper, SysLogsPojo onePojo)
			throws Exception {
		SysLogsDBMang logDB = new SysLogsDBMang(sqlHelper, m_bs);
		// 外网IP
		onePojo.setFromNetIP(this.m_bs.getRequest().getRemoteAddr());
		// 真实IP
		onePojo.setFromLocalIP(IpUtils.getClientIpAddr(this.m_bs.getRequest()));
		// 用户
		if (onePojo.getCreateUser().getGroupAllName().equals("")) {
			BIUser userBI = new BIUser(sqlHelper, m_bs);
			OrgPojo gp = userBI.getGroupByRedis(onePojo.getCreateUser()
					.getGroupId().replaceAll(",", ""));
			if (gp != null) {
				onePojo.getCreateUser().setGroupAllId(gp.getAllOrgId());
				onePojo.getCreateUser().setGroupAllName(gp.getAllName());
				onePojo.getCreateUser().setGroupName(gp.getName());
			}
		}
		return logDB.insertSysLogs(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: getSysLogsList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到放行条件列表。
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
	public ArrayList<MsgLogsPojo> getMsgLogsList(JSONObject paras, long f,
			long t) throws Exception {
		ArrayList<MsgLogsPojo> list = new ArrayList<MsgLogsPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getMsgLogsList(sqlHelper, paras, f, t);
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
	 * 方法名称: getRunMangLogList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到放行条件列表。
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
	public ArrayList<MsgLogsPojo> getMsgLogsList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		// 参数设定
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t.LOGS_NAME like ? OR t.LOGS_CONTENT like ? OR t.EQP_NAME like ? OR t.LOGS_INMSG::text like ?)";
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("type")) {
					// 关键字
					where += " and t.LOGS_TYPE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("eqptype")) {
					// 关键字
					where += " and t.EQP_TYPE=?";
					vList.add(Integer.parseInt(v));
				}
				if (key.equals("date")) {
					// 关键字
					String dates[] = v.split(",");
					where += " and t.C_DATE BETWEEN ? and ?";
					vList.add(Timestamp.valueOf(dates[0]));
					vList.add(Timestamp.valueOf(dates[1]));
				}
			}
		}
		MsgLogsDBMang logDB = new MsgLogsDBMang(sqlHelper, m_bs);
		paras.put("max", logDB.getMsgLogsCount(where, vList));
		return logDB.getMsgLogsList(where, f, t, vList);
	}

	/**
	 * <p>
	 * 方法名称: getSysLogsList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到放行条件列表。
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
	public MsgLogsPojo getOneMsgLogs(String logsId) throws Exception {
		MsgLogsPojo oneLogs = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			oneLogs = this.getOneMsgLogs(sqlHelper, logsId);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return oneLogs;
	}

	/**
	 * <p>
	 * 方法名称: getRunMangLogList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到放行条件列表。
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
	public MsgLogsPojo getOneMsgLogs(SqlExecute sqlHelper, String logsId)
			throws Exception {
		MsgLogsDBMang logDB = new MsgLogsDBMang(sqlHelper, m_bs);
		return logDB.getOneMsgLogs(logsId);
	}

	/**
	 * <p>
	 * 方法名称: insertMsgLogs
	 * </p>
	 * <p>
	 * 方法功能描述: 写入接口数据。
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
	public int insertMsgLogs(MsgLogsPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertMsgLogs(sqlHelper, onePojo);
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
	 * 方法名称: insertMsgLogs
	 * </p>
	 * <p>
	 * 方法功能描述:写入接口日志。
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
	public int insertMsgLogs(SqlExecute sqlHelper, MsgLogsPojo onePojo)
			throws Exception {
		MsgLogsDBMang logDB = new MsgLogsDBMang(sqlHelper, m_bs);
		return logDB.insertMsgLogs(onePojo);
	}
}
