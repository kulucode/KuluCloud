package tt.kulu.bi.logs.dbclass;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.logs.pojo.SysLogsPojo;
import tt.kulu.bi.user.pojo.LoginUserPojo;

import com.alibaba.fastjson.JSON;
import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: SysLogsDBMang
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
public class SysLogsDBMang extends BSDBBase {
	public SysLogsDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getRunMangConList
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
	public ArrayList<SysLogsPojo> getSysLogsList(String where, long f, long t,
			List<Object> vList) throws Exception {
		ArrayList<SysLogsPojo> list = new ArrayList<SysLogsPojo>();
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.LOGS_ID");
		strSQL.append(",t.LOG_NAME");
		strSQL.append("," + SqlExecute.getDateToCharSql("t.C_DATE")
				+ " as C_DATE");
		strSQL.append(",t.C_USER");
		strSQL.append(",t.C_USER_MSG");
		strSQL.append(",t.FROM_L_IP");
		strSQL.append(",t.FROM_N_IP");
		strSQL.append(",t.LOG_CONTENT");
		strSQL.append(",t.LOG_TYPE");
		strSQL.append(" from T_SYS_LOGS t");
		strSQL.append(" where t.LOGS_ID is not null ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		strSQL.append(" order by t.C_DATE desc,t.LOG_TYPE");
		strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				SysLogsPojo onePojo = new SysLogsPojo();
				onePojo.setId(rs.getString("LOGS_ID"));
				onePojo.setName(rs.getString("LOG_NAME"));
				onePojo.setCreateDate(rs.getString("C_DATE"));
				if (rs.getString("FROM_L_IP") != null) {
					onePojo.setFromLocalIP(rs.getString("FROM_L_IP"));
				}
				if (rs.getString("FROM_N_IP") != null) {
					onePojo.setFromNetIP(rs.getString("FROM_N_IP"));
				}
				onePojo.setContent(rs.getString("LOG_CONTENT"));
				onePojo.setType(rs.getInt("LOG_TYPE"));
				//
				onePojo.setCreateUser(JSON.parseObject(
						rs.getString("C_USER_MSG"), LoginUserPojo.class));
				list.add(onePojo);
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getSysLogsCount
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
	public long getSysLogsCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.LOGS_ID) as OBJ_COUNT");
		strSQL.append(" from T_SYS_LOGS t");
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
	 * 方法名称: insertRole
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
	public int insertSysLogs(SysLogsPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			if (onePojo.getId() == null || onePojo.getId().trim().equals("")) {
				onePojo.setId(BSGuid.getRandomGUID());
			}
			if (onePojo.getFromLocalIP() == null
					|| onePojo.getFromLocalIP().equals("")) {
				onePojo.setFromLocalIP("未知");
			}
			if (onePojo.getFromNetIP() == null
					|| onePojo.getFromNetIP().equals("")) {
				onePojo.setFromNetIP("未知");
			}
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("insert into T_SYS_LOGS (");
			strSQL.append("LOGS_ID");
			strSQL.append(",LOG_NAME");
			strSQL.append(",C_DATE");
			strSQL.append(",C_USER");
			strSQL.append(",C_USER_MSG");
			strSQL.append(",FROM_L_IP");
			strSQL.append(",FROM_N_IP");
			strSQL.append(",LOG_CONTENT");
			strSQL.append(",LOG_TYPE");
			strSQL.append(") values (?,?,?,?,to_json(?::jsonb),?,?,?,?) ");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getId());
			vList.add(onePojo.getName());
			vList.add(Timestamp.valueOf(this.bsDate.getThisDate(0, 0)));
			vList.add(onePojo.getCreateUser().getUserInst());
			vList.add(JSON.toJSONString(onePojo.getCreateUser()));
			vList.add(onePojo.getFromLocalIP());
			vList.add(onePojo.getFromNetIP());
			vList.add(onePojo.getContent());
			vList.add(onePojo.getType());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}
}
