package tt.kulu.bi.area.dbclass;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import tt.kulu.bi.area.pojo.AreaPojo;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.out.call.BIDic;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: AreaDBMang
 * </p>
 * <p>
 * 功能描述: 行政区域数据库操作类
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
public class AreaDBMang extends BSDBBase {
	private BIDic dicBI = null;

	public AreaDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
		dicBI = new BIDic(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getAreaList  
	 * </p>
	 * <p>
	 * 方法功能描述: 得到行政区域。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public ArrayList<AreaPojo> getAreaList(String where, String orderBy,
			List<Object> vList, long f, long t) throws Exception {
		ArrayList<AreaPojo> list = new ArrayList<AreaPojo>();
		StringBuffer strSQL = this._getAreaSelectSQL(where, orderBy);
		strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneAreaPojo(rs));
			}
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getEquipmentInstList
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
	public long getAreaCount(String where, List<Object> vList) throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.A_ID) as OBJ_COUNT");
		strSQL.append(" from T_AREA t");
		strSQL.append(" where t.A_ID is not null ");
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
	 * 方法名称: getOneDicById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个字典。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public AreaPojo getOneAreaById(String id) throws Exception {
		AreaPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		StringBuffer strSQL = this._getAreaSelectSQL(" and t.A_ID=?", "");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = (this._setOneAreaPojo(rs));

		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertArea
	 * </p>
	 * <p>
	 * 方法功能描述: 添加新政地域。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：新政地域对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int insertArea(AreaPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null && !onePojo.getId().equals("")) {
			// 得到上级
			if (onePojo.getpId().equals("") || onePojo.getpId().equals("root")) {
				onePojo.setpId("");
				onePojo.setAllId("," + onePojo.getId());
				onePojo.setAllName(onePojo.getName());
			} else {
				AreaPojo pArea = this.getOneAreaById(onePojo.getpId());
				if (pArea != null) {
					onePojo.setAllId(pArea.getAllId() + "," + onePojo.getId());
					onePojo.setAllName(pArea.getAllName() + ","
							+ onePojo.getName());
				}
			}
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("insert into T_AREA (");
			strSQL.append("A_ID");
			strSQL.append(",A_PID");
			strSQL.append(",A_NAME");
			strSQL.append(",A_SNAME");
			strSQL.append(",A_CLASS");
			strSQL.append(",A_TYPE");
			strSQL.append(",A_ALLID");
			strSQL.append(",A_ALLNAME");
			strSQL.append(") values (?,?,?,?,?,?,?,?)");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getId());
			vList.add(onePojo.getpId());
			vList.add(onePojo.getName());
			vList.add(onePojo.getShortName());
			vList.add(onePojo.getAreaClass().getId());
			vList.add(onePojo.getAreaType().getId());
			vList.add(onePojo.getAllId());
			vList.add(onePojo.getAllName());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateArea
	 * </p>
	 * <p>
	 * 方法功能描述: 保存新政地域。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：新政地域对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateArea(AreaPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null && !onePojo.getId().equals("")) {
			// 得到上级
			if (onePojo.getpId().equals("") || onePojo.getpId().equals("root")) {
				onePojo.setpId("");
				onePojo.setAllId("," + onePojo.getId());
				onePojo.setAllName(onePojo.getName());
			} else {
				AreaPojo pArea = this.getOneAreaById(onePojo.getpId());
				if (pArea != null) {
					onePojo.setAllId(pArea.getAllId() + "," + onePojo.getId());
					onePojo.setAllName(pArea.getAllName() + ","
							+ onePojo.getName());
				}
			}
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("update T_AREA set ");
			strSQL.append("A_PID=?");
			strSQL.append(",A_NAME=?");
			strSQL.append(",A_SNAME=?");
			strSQL.append(",A_CLASS=?");
			strSQL.append(",A_TYPE=?");
			strSQL.append(",A_ALLID=?");
			strSQL.append(",A_ALLNAME=?");
			strSQL.append(" where A_ID=?");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getpId());
			vList.add(onePojo.getName());
			vList.add(onePojo.getShortName());
			vList.add(onePojo.getAreaClass().getId());
			vList.add(onePojo.getAreaType().getId());
			vList.add(onePojo.getAllId());
			vList.add(onePojo.getAllName());
			vList.add(onePojo.getId());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getAreaSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.A_ID");
		strSQL.append(",t.A_PID");
		strSQL.append(",t.A_NAME");
		strSQL.append(",t.A_SNAME");
		strSQL.append(",t.A_CLASS");
		strSQL.append(",t.A_TYPE");
		strSQL.append(",t.A_ALLID");
		strSQL.append(",t.A_ALLNAME");
		strSQL.append(" from T_AREA t");
		strSQL.append(" where t.A_ID is not null ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by" + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private AreaPojo _setOneAreaPojo(ResultSet rs) throws Exception {
		AreaPojo onePojo = new AreaPojo();
		onePojo.setId(rs.getString("A_ID"));
		onePojo.setName(rs.getString("A_NAME"));
		if (rs.getString("A_SNAME") != null) {
			onePojo.setShortName(rs.getString("A_SNAME"));
		}
		onePojo.setAreaClass(this.dicBI.getDicItemByRedis(rs
				.getString("A_CLASS")));
		onePojo.setAreaType(this.dicBI.getDicItemByRedis(rs.getString("A_TYPE")));
		if (rs.getString("A_PID") != null) {
			onePojo.setpId(rs.getString("A_PID"));
		}
		onePojo.setAllId(rs.getString("A_ALLID"));
		onePojo.setAllName(rs.getString("A_ALLNAME"));
		// 上级名称
		if (!onePojo.getpId().equals("")) {
			int s = onePojo.getAllName().lastIndexOf(",");
			if (s < 0) {
				onePojo.setpName(onePojo.getAllName());
			} else {
				onePojo.setpName(onePojo.getAllName().substring(0, s));
			}
		}

		return onePojo;
	}
}
