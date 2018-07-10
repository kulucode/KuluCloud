package tt.kulu.bi.fance.dbclass;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.fance.pojo.FancePojo;
import tt.kulu.bi.fance.pojo.FanceRelPojo;
import tt.kulu.bi.truck.pojo.TruckPojo;
import tt.kulu.bi.user.pojo.UserPojo;
import tt.kulu.out.call.BITruck;

import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: FanceDBMang
 * </p>
 * <p>
 * 功能描述: 围栏数据库操作类
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
public class FanceDBMang extends BSDBBase {
	private BITruck truckBI = null;

	public FanceDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
		this.truckBI = new BITruck(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getFanceList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品定义列表。
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
	public ArrayList<FancePojo> getFanceList(String where, String orderBy,
			List<Object> vList) throws Exception {
		ArrayList<FancePojo> list = new ArrayList<FancePojo>();
		ResultSet rs = this.sqlHelper.queryBySql(
				_getFanceSelectSQL(where, orderBy).toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneFancePojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getFanceList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品定义列表。
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
	public long getFanceUserCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.F_ID) as OBJ_COUNT");
		strSQL.append(" from T_FANCE_USER_R t");
		strSQL.append(" where t.F_ID is not null ");

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
	 * 方法名称: getFanceList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品定义列表。
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
	public long getFanceTruckCount(String where, List<Object> vList)
			throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.F_ID) as OBJ_COUNT");
		strSQL.append(" from T_FANCE_TRUCK_R t");
		strSQL.append(" where t.F_ID is not null ");

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
	 * 方法名称: getOneFanceById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个物品定义。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public FancePojo getOneFanceById(String id) throws Exception {
		FancePojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		ResultSet rs = this.sqlHelper.queryBySql(
				this._getFanceSelectSQL(" and t.F_ID=?", "").toString(), vList);
		if (rs != null) {
			if (rs.next()) {
				onePojo = (this._setOneFancePojo(rs));
			}
			rs.close();
		}

		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertEquipmentDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个物品定义。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int insertFance(FancePojo onePojo) throws Exception {
		int count = 0;
		if (onePojo.getId() == null || onePojo.getId().trim().equals("")) {
			onePojo.setId(BSGuid.getRandomGUID());
		}
		StringBuffer strSQL = new StringBuffer("insert into T_FANCE (");
		strSQL.append("F_ID");
		strSQL.append(",F_NAME");
		strSQL.append(",F_TYPE");
		strSQL.append(",ORG_ID");
		strSQL.append(",A_ID");
		strSQL.append(",F_POINT_LIST");
		strSQL.append(",F_GEOMETRY");
		strSQL.append(") values (?,?,?,?,?,?,");
		if (onePojo.getGeo().size() > 0) {
			strSQL.append("st_geomfromText('POLYGON((");
			for (int i = 0, size = onePojo.getGeo().size(); i < size; i++) {
				JSONObject oneP = onePojo.getGeo().getJSONObject(i);
				if (i > 0) {
					strSQL.append(",");
				}
				strSQL.append(oneP.getString("lon") + " "
						+ oneP.getString("lat"));
			}
			strSQL.append("))',4326))");
		} else {
			strSQL.append("null");
		}

		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getId());
		vList.add(onePojo.getName());
		vList.add(onePojo.getType());
		vList.add(onePojo.getOrg().getId());
		vList.add(onePojo.getArea().getId());
		vList.add(onePojo.getGeo().toString());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		// 写入关系表
		this.deletFanceRel(onePojo.getId(), 0);
		if (!onePojo.getUsers().equals("")) {
			String rels[] = onePojo.getUsers().split(",");
			for (String oneR : rels) {
				if (!oneR.equals("")) {
					count += this.insertFanceRel(new FanceRelPojo(onePojo
							.getId(), oneR, 2));
				}

			}
		}
		if (!onePojo.getTrucks().equals("")) {
			String rels[] = onePojo.getTrucks().split(",");
			for (String oneR : rels) {
				if (!oneR.equals("")) {
					count += this.insertFanceRel(new FanceRelPojo(onePojo
							.getId(), oneR, 1));
				}
			}
		}

		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateFance
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个物品定义。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateFance(FancePojo onePojo) throws Exception {
		int count = 0;
		StringBuffer strSQL = new StringBuffer("update T_FANCE set ");
		strSQL.append("F_NAME=?");
		strSQL.append(",F_TYPE=?");
		strSQL.append(",ORG_ID=?");
		strSQL.append(",A_ID=?");
		strSQL.append(",F_POINT_LIST=?");
		strSQL.append(",F_GEOMETRY=");
		if (onePojo.getGeo().size() > 0) {
			strSQL.append("st_geomfromText('POLYGON((");
			for (int i = 0, size = onePojo.getGeo().size(); i < size; i++) {
				JSONObject oneP = onePojo.getGeo().getJSONObject(i);
				if (i > 0) {
					strSQL.append(",");
				}
				strSQL.append(oneP.getString("lon") + " "
						+ oneP.getString("lat"));
			}
			strSQL.append("))',4326)");
		} else {
			strSQL.append("null");
		}
		strSQL.append(" where F_ID=?");
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getName());
		vList.add(onePojo.getType());
		vList.add(onePojo.getOrg().getId());
		vList.add(onePojo.getArea().getId());
		vList.add(onePojo.getGeo().toString());
		vList.add(onePojo.getId());
		count = this.sqlHelper.updateBySql(strSQL.toString(), vList);
		// 写入关系表
		this.deletFanceRel(onePojo.getId(), 0);
		if (!onePojo.getUsers().equals("")) {
			String rels[] = onePojo.getUsers().split(",");
			for (String oneR : rels) {
				if (!oneR.equals("")) {
					count += this.insertFanceRel(new FanceRelPojo(onePojo
							.getId(), oneR, 2));
				}

			}
		}
		if (!onePojo.getTrucks().equals("")) {
			String rels[] = onePojo.getTrucks().split(",");
			for (String oneR : rels) {
				if (!oneR.equals("")) {
					count += this.insertFanceRel(new FanceRelPojo(onePojo
							.getId(), oneR, 1));
				}
			}
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getFanceList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品定义列表。
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
	public ArrayList<UserPojo> getFanceUserRList(String where,
			List<Object> vList) throws Exception {
		ArrayList<UserPojo> list = new ArrayList<UserPojo>();
		ResultSet rs = this.sqlHelper.queryBySql(_getFanceUserRSelectSQL(where)
				.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneFanceUserRPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getFanceList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到物品定义列表。
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
	public ArrayList<TruckPojo> getFanceTruckRList(String where,
			List<Object> vList) throws Exception {
		ArrayList<TruckPojo> list = new ArrayList<TruckPojo>();
		ResultSet rs = this.sqlHelper.queryBySql(
				_getFanceTruckRSelectSQL(where).toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneFanceTruckRPojo(rs));
			}
			rs.close();
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: insertFanceRel
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个物品定义。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int insertFanceRel(FanceRelPojo onePojo) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(onePojo.getFanceId());
		vList.add(onePojo.getRelId());
		if (onePojo.getType() == 0 || onePojo.getType() == 1) {
			count += this.sqlHelper.updateBySql(
					"insert into T_FANCE_TRUCK_R (F_ID,TRUCK_ID) values (?,?)",
					vList);
		}
		if (onePojo.getType() == 0 || onePojo.getType() == 2) {
			count += this.sqlHelper
					.updateBySql(
							"insert into T_FANCE_USER_R (F_ID,USER_INSTID) values (?,?)",
							vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertEquipmentDef
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个物品定义。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int deletFanceRel(String fanceId, int type) throws Exception {
		int count = 0;
		List<Object> vList = new ArrayList<Object>();
		vList.add(fanceId);
		if (type == 0 || type == 1) {
			count += this.sqlHelper.updateBySql(
					"delete from T_FANCE_TRUCK_R where F_ID=?", vList);
		}
		if (type == 0 || type == 2) {
			count += this.sqlHelper.updateBySql(
					"delete from T_FANCE_USER_R where F_ID=?", vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: deleteFanceById
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个物品定义。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int deleteFanceById(String id) throws Exception {
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		int count = this.deletFanceRel(id, 0);
		count += this.sqlHelper.updateBySql("delete from T_FANCE where F_ID=?",
				vList);
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateFance
	 * </p>
	 * <p>
	 * 方法功能描述: 新增一个物品定义。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 * 
	 * @throws Exception
	 */
	public boolean checkPoineInFance(String where, JSONObject point,
			List<Object> vList) throws Exception {
		boolean isIn = true;
		StringBuffer strSQL = new StringBuffer("select ST_Contains(");
		strSQL.append("t.f_geometry");
		strSQL.append(",st_geometryfromtext('POINT(" + point.getString("lon")
				+ " " + point.getString("lat") + ")',4326)");
		strSQL.append(") as IN_FLG");
		strSQL.append(" from T_FANCE t where t.f_id is not null ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			boolean t = false;
			while (rs.next()) {
				if (rs.getBoolean("IN_FLG")) {
					t = true;
					break;
				}
			}
			isIn = t;
			rs.close();
		}
		return isIn;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getFanceSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.F_ID");
		strSQL.append(",t.F_NAME");
		strSQL.append(",t.F_TYPE");
		strSQL.append(",t.ORG_ID");
		strSQL.append(",t.F_POINT_LIST");
		strSQL.append(",t.A_ID");
		strSQL.append(",ST_X(ST_Centroid(t.F_GEOMETRY)) as P_CENETR_LON");
		strSQL.append(",ST_Y(ST_Centroid(t.F_GEOMETRY)) as P_CENETR_LAT");
		strSQL.append(" from T_FANCE t");
		strSQL.append(" where t.F_ID is not null ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		} else {
			strSQL.append(" order by t.F_TYPE");
		}
		return strSQL;
	}

	// 物品定义对象
	private FancePojo _setOneFancePojo(ResultSet rs) throws Exception {
		FancePojo onePojo = new FancePojo();
		onePojo.setId(rs.getString("F_ID"));
		onePojo.setName(rs.getString("F_NAME"));
		onePojo.setType(rs.getInt("F_TYPE"));
		if (rs.getString("ORG_ID") != null) {
			onePojo.getOrg().setId(rs.getString("ORG_ID"));
		}
		if (rs.getString("F_POINT_LIST") != null) {
			onePojo.setGeo(JSONArray.fromObject(rs.getString("F_POINT_LIST")));
		}
		if (rs.getString("A_ID") != null) {
			onePojo.getArea().setId(rs.getString("A_ID"));
		}
		if (rs.getString("P_CENETR_LON") != null) {
			onePojo.getCenter()[0] = rs.getString("P_CENETR_LON");
			onePojo.getCenter()[1] = rs.getString("P_CENETR_LAT");
		}
		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getFanceUserRSelectSQL(String where) throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.USER_INSTID");
		strSQL.append(",t1.USER_ID");
		strSQL.append(",t1.USER_NAME");
		strSQL.append(",t1.MPHONE");
		strSQL.append(" from T_FANCE_USER_R t, T_USER t1");
		strSQL.append(" where t.USER_INSTID=t1.USER_INSTID ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		strSQL.append(" order by t1.USER_ID");
		return strSQL;
	}

	// 物品定义对象
	private UserPojo _setOneFanceUserRPojo(ResultSet rs) throws Exception {
		UserPojo onePojo = new UserPojo();
		onePojo.setInstId(rs.getString("USER_INSTID"));
		onePojo.setId(rs.getString("USER_ID"));
		onePojo.setName(rs.getString("USER_NAME"));
		if (rs.getString("MPHONE") != null) {
			onePojo.setmPhone(rs.getString("MPHONE"));
		}
		return onePojo;
	}

	// 得到物品定义查询的SQL语句
	private StringBuffer _getFanceTruckRSelectSQL(String where)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.TRUCK_ID");
		strSQL.append(",t1.TRUCK_NAME");
		strSQL.append(",t1.PLATE_NUM");
		strSQL.append(",t1.TD_ID");
		strSQL.append(" from T_FANCE_TRUCK_R t, T_TRUCK_INST t1");
		strSQL.append(" where t.TRUCK_ID=t1.TRUCK_ID ");

		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		strSQL.append(" order by t1.TRUCK_NAME");
		return strSQL;
	}

	// 物品定义对象
	private TruckPojo _setOneFanceTruckRPojo(ResultSet rs) throws Exception {
		TruckPojo onePojo = new TruckPojo();
		onePojo.setId(rs.getString("TRUCK_ID"));
		onePojo.setName(rs.getString("TRUCK_NAME"));
		if (rs.getString("PLATE_NUM") != null) {
			onePojo.setPlateNum(rs.getString("PLATE_NUM"));
		}
		onePojo.setDefine(this.truckBI.getTruckDefByRedis(rs.getString("TD_ID")));
		return onePojo;
	}
}
