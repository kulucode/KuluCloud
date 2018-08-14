package tt.kulu.out.base;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.logs.pojo.MsgLogsPojo;
import tt.kulu.bi.logs.pojo.SysLogsPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.out.call.BICompany;
import tt.kulu.out.call.BILogs;
import tt.kulu.out.call.BIUser;

import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSRole
 * </p>
 * <p>
 * 功能描述: 角色Web接口类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2014-10-23
 * </p>
 */
public class BSLogs {
	/**
	 * <p>
	 * 方法名：do_LogsIni
	 * </p>
	 * <p>
	 * 方法描述：搜索用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_LogsIni(BSObject m_bs) throws Exception {
		JSONObject fretObj = new JSONObject();
		fretObj.put("edate", m_bs.getDateEx().getThisDate(0, 0)
				.substring(0, 10)
				+ " 23:59:59");
		fretObj.put("sdate", m_bs.getDateEx().getThisDate(0, -7));
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchUser
	 * </p>
	 * <p>
	 * 方法描述：搜索用户
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_searchLogsList(BSObject m_bs) throws Exception {
		// 数据准备
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject paras = new JSONObject();
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String sText = m_bs.getPrivateMap().get("pg_text");
		String type = m_bs.getPrivateMap().get("pg_type");
		String sdate = m_bs.getPrivateMap().get("pg_sdate");
		String edate = m_bs.getPrivateMap().get("pg_edate");
		paras.put("key", sText);
		if (type == null || type.equals("")) {
			type = "0";
		}
		paras.put("type", type);
		paras.put("date", sdate + "," + edate);

		if (sdate == null || sdate.equals("")) {
			sdate = m_bs.getDateEx().getThisDate(0, -7);
		}
		if (edate == null || edate.equals("")) {
			edate = m_bs.getDateEx().getThisDate(0, 0);
		}
		paras.put("date", sdate + "," + edate);

		if (pageNumStr != null) {
			pageNum = Integer.parseInt(pageNumStr);
		} else {
			pageNum = 0;
		}
		if (pageNum < 0) {
			pageNum = 0;
		}
		if (pageSizeStr != null) {
			pageSize = Integer.parseInt(pageSizeStr);
		} else {
			pageSize = 20;
		}
		startNum = pageSize * pageNum;
		BILogs logsBI = new BILogs(null, m_bs);
		ArrayList<SysLogsPojo> list = logsBI.getSysLogsList(paras, startNum,
				startNum + pageSize - 1);
		for (SysLogsPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("desc", onePojo.getContent().replaceAll("；", "<br/>"));
			oneObj.put("date", onePojo.getCreateDate());
			oneObj.put("netip", onePojo.getFromNetIP());
			oneObj.put("localip", onePojo.getFromLocalIP());
			oneObj.put("cuinst", onePojo.getCreateUser().getUserInst());
			oneObj.put("cuid", onePojo.getCreateUser().getUserId());
			oneObj.put("cuname", onePojo.getCreateUser().getUserName());
			oneObj.put("cuphone", onePojo.getCreateUser().getPhone());
			oneObj.put("cugroup", onePojo.getCreateUser().getGroupAllName()
					.replaceAll(",", "-"));
			retObj.add(oneObj);
		}
		fretObj.put("list", retObj);
		fretObj.put("max", paras.getLong("max"));
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchEqpMsgLogsList
	 * </p>
	 * <p>
	 * 方法描述：搜索设备报文日志
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_searchEqpMsgLogsList(BSObject m_bs) throws Exception {
		// 数据准备
		JSONArray retObj = new JSONArray();
		JSONObject fretObj = new JSONObject();
		long startNum = 1;
		int pageNum = 0;
		int pageSize = 0;
		JSONObject paras = new JSONObject();
		String pageNumStr = m_bs.getPrivateMap().get("pg_num");
		String pageSizeStr = m_bs.getPrivateMap().get("pg_size");
		String sText = m_bs.getPrivateMap().get("pg_text");
		String type = m_bs.getPrivateMap().get("pg_logtype");
		String sdate = m_bs.getPrivateMap().get("pg_sdate");
		String edate = m_bs.getPrivateMap().get("pg_edate");
		paras.put("key", sText);
		if (type == null) {
			type = "";
		}
		paras.put("type", type);
		paras.put("date", sdate + "," + edate);

		if (sdate == null || sdate.equals("")) {
			sdate = m_bs.getDateEx().getThisDate(0, -7);
		}
		if (edate == null || edate.equals("")) {
			edate = m_bs.getDateEx().getThisDate(0, 0);
		}
		paras.put("date", sdate + "," + edate);

		if (pageNumStr != null) {
			pageNum = Integer.parseInt(pageNumStr);
		} else {
			pageNum = 0;
		}
		if (pageNum < 0) {
			pageNum = 0;
		}
		if (pageSizeStr != null) {
			pageSize = Integer.parseInt(pageSizeStr);
		} else {
			pageSize = 20;
		}
		startNum = pageSize * pageNum;
		BILogs logsBI = new BILogs(null, m_bs);
		ArrayList<MsgLogsPojo> list = logsBI.getMsgLogsList(paras, startNum,
				startNum + pageSize - 1);
		for (MsgLogsPojo onePojo : list) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("eqpname", onePojo.getEqpName());
			oneObj.put("eqptype",
					MsgLogsPojo.EQPTYPE_NAME[onePojo.getEqpType()]);
			oneObj.put("desc", onePojo.getContent());
			oneObj.put("date", onePojo.getCreateDate());
			oneObj.put("msgdate", onePojo.getMsgDate());
			retObj.add(oneObj);
		}
		fretObj.put("list", retObj);
		fretObj.put("max", paras.getLong("max"));
		fretObj.put("r", 0);
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}

	/**
	 * <p>
	 * 方法名：do_searchEqpMsgLogsList
	 * </p>
	 * <p>
	 * 方法描述：搜索设备报文日志
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */

	public BSObject do_getOneEqpMsgLogs(BSObject m_bs) throws Exception {
		// 数据准备
		JSONObject fretObj = new JSONObject();
		fretObj.put("r", 999);

		BILogs logsBI = new BILogs(null, m_bs);
		MsgLogsPojo onePojo = logsBI.getOneMsgLogs(m_bs.getPrivateMap().get(
				"pg_logsid"));
		if (onePojo != null) {
			JSONObject oneObj = new JSONObject();
			oneObj.put("id", onePojo.getId());
			oneObj.put("name", onePojo.getName());
			oneObj.put("eqpname", onePojo.getEqpName());
			oneObj.put("inmsg", onePojo.getInMsg());
			oneObj.put("outmsg", onePojo.getOutMsg());
			fretObj.put("data", oneObj);
			fretObj.put("r", 0);
		}
		fretObj.put("error", URLlImplBase.ErrorMap.get(fretObj.getInt("r")));
		m_bs.setRetrunObj(fretObj);
		return m_bs;
	}
}
