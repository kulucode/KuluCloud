package tt.kulu.bi.logs.dbclass;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.logs.pojo.MsgLogsPojo;
import tt.kulu.bi.user.pojo.LoginUserPojo;

import com.alibaba.fastjson.JSON;
import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: MsgLogsDBMang
 * </p>
 * <p>
 * 功能描述: 系统日志数据库操作类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2011-05-05
 * </p>
 */
public class MsgLogsDBMang extends BSDBBase {
	public MsgLogsDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getMsgLogsList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到放行条件列表。
	 * </p>
	 * <p>
	 * 输入参数描述:
	 * </p>
	 * <p>
	 * 输出参数描述:
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<MsgLogsPojo> getMsgLogsList(String where, long f, long t,
			List<Object> vList) throws Exception {
		ArrayList<MsgLogsPojo> list = new ArrayList<MsgLogsPojo>();
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.LOGS_ID");
		strSQL.append(",t.LOGS_NAME");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.MSG_DATE")
				+ " as MSG_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.C_DATE")
				+ " as C_DATE");
		strSQL.append(",t.EQP_NAME");
		strSQL.append(",t.LOGS_CONTENT");
		strSQL.append(",t.LOGS_TYPE");
		strSQL.append(",t.EQP_TYPE");
		strSQL.append(",t.LOGS_INMSG::text");
		strSQL.append(",t.LOGS_OUTMSG::text");
		strSQL.append(" from T_EQP_MSG_LOGS t");
		strSQL.append(" where t.LOGS_ID is not null ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		strSQL.append(" order by t.C_DATE desc");
		strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				MsgLogsPojo onePojo = new MsgLogsPojo();
				onePojo.setId(rs.getString("LOGS_ID"));
				onePojo.setName(rs.getString("LOGS_NAME"));
				onePojo.setMsgDate(rs.getString("MSG_DATE"));
				onePojo.setCreateDate(rs.getString("C_DATE"));
				onePojo.setEqpName(rs.getString("EQP_NAME"));
				onePojo.setType(rs.getInt("LOGS_TYPE"));
				onePojo.setEqpType(rs.getInt("EQP_TYPE"));
				onePojo.setContent(rs.getString("LOGS_CONTENT"));
				list.add(onePojo);
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getMsgLogsList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到放行条件列表。
	 * </p>
	 * <p>
	 * 输入参数描述:
	 * </p>
	 * <p>
	 * 输出参数描述:
	 * </p>
	 * 
	 * @throws Exception
	 */
	public MsgLogsPojo getOneMsgLogs(String logsId) throws Exception {
		List<Object> vList = new ArrayList<Object>();
		vList.add(logsId);
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.LOGS_ID");
		strSQL.append(",t.LOGS_NAME");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.C_DATE")
				+ " as C_DATE");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.MSG_DATE")
				+ " as MSG_DATE");
		strSQL.append(",t.EQP_NAME");
		strSQL.append(",t.LOGS_CONTENT");
		strSQL.append(",t.LOGS_TYPE");
		strSQL.append(",t.EQP_TYPE");
		strSQL.append(",t.LOGS_INMSG");
		strSQL.append(",t.LOGS_OUTMSG");
		strSQL.append(" from T_EQP_MSG_LOGS t");
		strSQL.append(" where t.LOGS_ID=?");
		MsgLogsPojo onePojo = null;
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = new MsgLogsPojo();
				onePojo.setId(rs.getString("LOGS_ID"));
				onePojo.setName(rs.getString("LOGS_NAME"));
				onePojo.setCreateDate(rs.getString("C_DATE"));
				onePojo.setMsgDate(rs.getString("MSG_DATE"));
				onePojo.setEqpName(rs.getString("EQP_NAME"));
				onePojo.setType(rs.getInt("LOGS_TYPE"));
				onePojo.setEqpType(rs.getInt("EQP_TYPE"));
				if (rs.getString("LOGS_INMSG") != null) {
					onePojo.setInMsg(rs.getString("LOGS_INMSG"));
				}
				if (rs.getString("LOGS_OUTMSG") != null) {
					onePojo.setOutMsg(rs.getString("LOGS_OUTMSG"));
				}
				onePojo.setContent(rs.getString("LOGS_CONTENT"));
			}
			rs.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getMsgLogsCount
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品实例列表。
	 * </p>
	 * <p>
	 * 输入参数描述:
	 * </p>
	 * <p>
	 * 输出参数描述:
	 * </p>
	 * 
	 * @throws Exception
	 */
	public long getMsgLogsCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.LOGS_ID) as OBJ_COUNT");
		strSQL.append(" from T_EQP_MSG_LOGS t");
		strSQL.append(" where t.LOGS_ID is not null");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			count = rs.getLong("OBJ_COUNT");
		}
		return count;

	}

	/**
	 * <p>
	 * 方法名称: insertMsgLogs
	 * </p>
	 * <p>
	 * 方法功能描述: 新增接口信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo onePojo：角色信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int insertMsgLogs(MsgLogsPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			if (onePojo.getId() == null || onePojo.getId().trim().equals("")) {
				onePojo.setId(BSGuid.getRandomGUID());
			}
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("insert into T_EQP_MSG_LOGS (");
			strSQL.append("LOGS_ID");
			strSQL.append(",LOGS_NAME");
			strSQL.append(",EQP_NAME");
			strSQL.append(",MSG_DATE");
			strSQL.append(",C_DATE");
			strSQL.append(",LOGS_CONTENT");
			strSQL.append(",LOGS_TYPE");
			strSQL.append(",EQP_TYPE");
			strSQL.append(",LOGS_INMSG");
			strSQL.append(",LOGS_OUTMSG");
			strSQL.append(") values (?,?,?,?,?,?,?,?,to_json(?::jsonb),to_json(?::jsonb)) ");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getId());
			vList.add(onePojo.getName());
			vList.add(onePojo.getEqpName());
			if (onePojo.getMsgDate().equals("")) {
				onePojo.setMsgDate(onePojo.getCreateDate());
			}
			vList.add(Timestamp.valueOf(onePojo.getMsgDate()));
			vList.add(Timestamp.valueOf(onePojo.getCreateDate()));
			vList.add(onePojo.getContent());
			vList.add(onePojo.getType());
			vList.add(onePojo.getEqpType());
			vList.add(onePojo.getInMsg());
			vList.add(onePojo.getOutMsg());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}
}
