package tt.kulu.bi.company.dbclass;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.out.call.BIArea;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;
import com.tt4j2ee.md5.MD5Imp;

/**
 * <p>
 * 标题: CompanyDBMang
 * </p>
 * <p>
 * 功能描述: 企业管理数据库操作类
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
public class CompanyDBMang extends BSDBBase {
	private BIArea areaBI = null;

	public CompanyDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
		this.areaBI = new BIArea(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getCompanyList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到企业。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public ArrayList<CompanyPojo> getCompanyList(String where, String orderBy,
			List<Object> vList, long f, long t) throws Exception {
		ArrayList<CompanyPojo> list = new ArrayList<CompanyPojo>();
		StringBuffer strSQL = this._getCompanySelectSQL(where, orderBy);
		strSQL.append(" LIMIT " + (t - f + 1) + " OFFSET " + f);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneCompanyPojo(rs));
			}
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getCompanyCount
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个资源实例。
	 * </p>
	 * <p>
	 * 输入参数描述:
	 * </p>
	 * <p>
	 * 输出参数描述:
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public long getCompanyCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.COMP_ID) as TAB_COUNT");
		strSQL.append(" from T_COMPANY t");
		strSQL.append(" where t.COMP_ID is not null ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			count = rs.getLong("TAB_COUNT");
			rs.close();
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getOneCompanyById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个企业。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public CompanyPojo getOneCompanyById(String id) throws Exception {
		CompanyPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		StringBuffer strSQL = this._getCompanySelectSQL(" and t.COMP_ID=?", "");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			onePojo = (this._setOneCompanyPojo(rs));

		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertCompany
	 * </p>
	 * <p>
	 * 方法功能描述: 添加企业。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：企业对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int insertCompany(CompanyPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			// 新增资源实例表
			if (onePojo.getId().equals("")) {
				onePojo.setId(BSGuid.getRandomGUID());
			}
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("insert into T_COMPANY (");
			strSQL.append("COMP_ID");
			strSQL.append(",COMP_NAME");
			strSQL.append(",COMP_TYPE");
			strSQL.append(",COMP_DESC");
			strSQL.append(",A_ID");
			strSQL.append(",COMP_LINK");
			strSQL.append(",COMP_PHONE");
			strSQL.append(",S_LAT");
			strSQL.append(",S_LON");
			if (onePojo.getLatitude() != "" && onePojo.getLongitude() != "") {
				// 有地理位置
				strSQL.append(",S_GEOMETRY");
			}
			strSQL.append(") values (?,?,?,?,?,?,?,?,?");
			if (onePojo.getLatitude() != "" && onePojo.getLongitude() != "") {
				// 有地理位置
				strSQL.append(",ST_GeomFromText('POINT("
						+ onePojo.getLongitude() + " " + onePojo.getLatitude()
						+ ")',4326)");
			}
			strSQL.append(")");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getId());
			vList.add(onePojo.getName());
			vList.add(onePojo.getType());
			vList.add(onePojo.getDesc());
			vList.add(onePojo.getArea().getId());
			vList.add(onePojo.getLinkMan());
			vList.add(onePojo.getLinkPhone());
			vList.add(onePojo.getLatitude());
			vList.add(onePojo.getLongitude());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateCompany
	 * </p>
	 * <p>
	 * 方法功能描述: 保存企业。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：企业对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateCompany(CompanyPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("update T_COMPANY set ");
			strSQL.append("COMP_NAME=?");
			strSQL.append(",COMP_TYPE=?");
			strSQL.append(",COMP_DESC=?");
			strSQL.append(",A_ID=?");
			strSQL.append(",COMP_LINK=?");
			strSQL.append(",COMP_PHONE=?");
			strSQL.append(",S_LAT=?");
			strSQL.append(",S_LON=?");
			if (onePojo.getLatitude() != "" && onePojo.getLongitude() != "") {
				// 有地理位置
				strSQL.append(",S_GEOMETRY=ST_GeomFromText('POINT("
						+ onePojo.getLongitude() + " " + onePojo.getLatitude()
						+ ")',4326)");
			}
			strSQL.append(" where COMP_ID=?");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getName());
			vList.add(onePojo.getType());
			vList.add(onePojo.getDesc());
			vList.add(onePojo.getArea().getId());
			vList.add(onePojo.getLinkMan());
			vList.add(onePojo.getLinkPhone());
			vList.add(onePojo.getLatitude());
			vList.add(onePojo.getLongitude());
			vList.add(onePojo.getId());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getCompanySelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.COMP_ID");
		strSQL.append(",t.COMP_NAME");
		strSQL.append(",t.COMP_TYPE");
		strSQL.append(",t.COMP_DESC");
		strSQL.append(",t.A_ID");
		strSQL.append(",t.COMP_LINK");
		strSQL.append(",t.COMP_PHONE");
		strSQL.append(",t.S_LAT");
		strSQL.append(",t.S_LON");
		strSQL.append(" from T_COMPANY t");
		strSQL.append(" where t.COMP_ID is not null ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by" + orderBy);
		}
		return strSQL;
	}

	// 加载一个资源实例对象
	private CompanyPojo _setOneCompanyPojo(ResultSet rs) throws Exception {
		CompanyPojo onePojo = new CompanyPojo();
		onePojo.setId(rs.getString("COMP_ID"));
		onePojo.setName(rs.getString("COMP_NAME"));
		onePojo.setType(rs.getInt("COMP_TYPE"));
		if (rs.getString("COMP_DESC") != null) {
			onePojo.setDesc(rs.getString("COMP_DESC"));
		}
		if (rs.getString("COMP_LINK") != null) {
			onePojo.setLinkMan(rs.getString("COMP_LINK"));
		}
		if (rs.getString("COMP_PHONE") != null) {
			onePojo.setLinkPhone(rs.getString("COMP_PHONE"));
		}
		//
		if (rs.getString("S_LAT") != null) {
			onePojo.setLatitude(rs.getString("S_LAT"));
		}
		if (rs.getString("S_LON") != null) {
			onePojo.setLongitude(rs.getString("S_LON"));
		}
		onePojo.setArea(this.areaBI.getAreaByRedis(rs.getString("A_ID")));
		return onePojo;
	}

	public BIArea getAreaBI() {
		return areaBI;
	}

	public void setAreaBI(BIArea areaBI) {
		this.areaBI = areaBI;
	}
}
