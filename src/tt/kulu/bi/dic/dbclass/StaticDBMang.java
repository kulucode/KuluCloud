package tt.kulu.bi.dic.dbclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.dom4j.tree.*;

import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.dic.pojo.DicPojo;
import tt.kulu.bi.dic.pojo.StaticPojo;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.BSGuid;
import com.tt4j2ee.XMLReader;
import com.tt4j2ee.db.SqlExecute;

/**
 * <p>
 * 标题: StaticDBMang
 * </p>
 * <p>
 * 功能描述: 字典数据库操作类
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
public class StaticDBMang {
	private SqlExecute sqlHelper = null;

	public StaticDBMang() throws Exception {
		this.sqlHelper = new SqlExecute();
	}

	public StaticDBMang(SqlExecute sqlHelper) throws Exception {
		this.sqlHelper = sqlHelper;
	}

	public StaticDBMang(String dbName) throws Exception {
		this.sqlHelper = new SqlExecute(dbName);
	}

	/**
	 * <p>
	 * 方法名称: getDicList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到字典。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public ArrayList<DicPojo> getDicList(String where, String orderBy,
			List<Object> vList) throws Exception {
		ArrayList<DicPojo> list = new ArrayList<DicPojo>();
		StringBuffer strSQL = this._getDicSelectSQL(where, orderBy);
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString(),
				vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneDicPojo(rs));
			}
		}
		return list;
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
	public DicPojo getOneDicById(String id) throws Exception {
		DicPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		StringBuffer strSQL = this._getDicSelectSQL(" and t.DIC_ID=?", "");
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString(),
				vList);
		if (rs != null && rs.next()) {
			onePojo = (this._setOneDicPojo(rs));

		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertDic
	 * </p>
	 * <p>
	 * 方法功能描述: 添加字典信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo onePojo：字典信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int insertDic(DicPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			if (onePojo.getId().equals("")) {
				onePojo.setId(BSGuid.getRandomGUID());
			}
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("insert into TB_DIC ");
			strSQL.append("(DIC_ID");
			strSQL.append(",DIC_NAME");
			strSQL.append(",DIC_DESC");
			strSQL.append(",DIC_PID");
			strSQL.append(") values (?,?,?,?)");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getId());
			vList.add(onePojo.getName());
			vList.add(onePojo.getDesc());
			vList.add(onePojo.getPid());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateDic
	 * </p>
	 * <p>
	 * 方法功能描述: 更新字典信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo onePojo：字典信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int updateDic(DicPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("update TB_DIC set ");
			strSQL.append("DIC_NAME=?");
			strSQL.append(",DIC_DESC=?");
			strSQL.append(",DIC_PID=?");
			strSQL.append(" where DIC_ID=? ");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getName());
			vList.add(onePojo.getDesc());
			vList.add(onePojo.getPid());
			vList.add(onePojo.getId());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getDicItemList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到字典。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public ArrayList<DicItemPojo> getDicItemList(String where, String orderBy,
			List<Object> vList) throws Exception {
		ArrayList<DicItemPojo> list = new ArrayList<DicItemPojo>();
		StringBuffer strSQL = this._getDicItemSelectSQL(where, orderBy);
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString(),
				vList);
		if (rs != null) {
			while (rs.next()) {
				list.add(this._setOneDicItemPojo(rs));
			}
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getDicItemList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到字典。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public HashMap<String, DicItemPojo> getDicItemMap(String where,
			String orderBy, List<Object> vList) throws Exception {
		HashMap<String, DicItemPojo> map = new HashMap<String, DicItemPojo>();
		StringBuffer strSQL = this._getDicItemSelectSQL(where, orderBy);
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString(),
				vList);
		if (rs != null) {
			while (rs.next()) {
				map.put(rs.getString("ITEM_ID").trim(),
						this._setOneDicItemPojo(rs));
			}
		}
		return map;
	}

	/**
	 * <p>
	 * 方法名称: getOneDicItemById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个字典项目。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public DicItemPojo getOneDicItemById(String id) throws Exception {
		DicItemPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(id);
		StringBuffer strSQL = this
				._getDicItemSelectSQL(" and t1.ITEM_ID=?", "");
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString(),
				vList);
		if (rs != null && rs.next()) {
			onePojo = (this._setOneDicItemPojo(rs));

		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneDicItemById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个字典项目。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public DicItemPojo getOneDicItemByName(String name) throws Exception {
		DicItemPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(name);
		StringBuffer strSQL = this._getDicItemSelectSQL(" and t1.ITEM_NAME=?",
				"");
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString(),
				vList);
		if (rs != null && rs.next()) {
			onePojo = (this._setOneDicItemPojo(rs));

		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneDicItemById
	 * </p>
	 * <p>
	 * 方法功能描述: 得到单个字典项目。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList
	 * </p>
	 */
	public DicItemPojo getOneDicItemByName(String dic, String name)
			throws Exception {
		DicItemPojo onePojo = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(dic);
		vList.add(name);
		StringBuffer strSQL = this._getDicItemSelectSQL(
				" and t.DIC_ID=? and t1.ITEM_NAME=?", "");
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString(),
				vList);
		if (rs != null && rs.next()) {
			onePojo = (this._setOneDicItemPojo(rs));

		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: insertDic
	 * </p>
	 * <p>
	 * 方法功能描述: 添加字典信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo onePojo：字典信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int insertDicItem(DicItemPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			if (onePojo.getId().equals("")) {
				onePojo.setId(BSGuid.getRandomGUID());
			}
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("insert into TB_DIC_ITEM ");
			strSQL.append("(DIC_ID");
			strSQL.append(",ITEM_ID");
			strSQL.append(",ITEM_NAME");
			strSQL.append(",ITEM_VALUE");
			strSQL.append(",ITEM_VALUE2");
			strSQL.append(",ITEM_INDEX");
			strSQL.append(",DIC_PITEMID");
			strSQL.append(") values (?,?,?,?,?,?,?)");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getDic().getId());
			vList.add(onePojo.getId());
			vList.add(onePojo.getName());
			vList.add(onePojo.getValue());
			vList.add(onePojo.getValue2());
			vList.add(onePojo.getIndex());
			vList.add(onePojo.getPitemid());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateDic
	 * </p>
	 * <p>
	 * 方法功能描述: 更新字典信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo onePojo：字典信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int updateDicItem(DicItemPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("update TB_DIC_ITEM set ");
			strSQL.append("ITEM_NAME=?");
			strSQL.append(",ITEM_VALUE=?");
			strSQL.append(",ITEM_VALUE2=?");
			strSQL.append(",ITEM_INDEX=?");
			strSQL.append(",DIC_PITEMID=?");
			strSQL.append(" where ITEM_ID=? ");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getName());
			vList.add(onePojo.getValue());
			vList.add(onePojo.getValue2());
			vList.add(onePojo.getIndex());
			vList.add(onePojo.getPitemid());
			vList.add(onePojo.getId());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateDic
	 * </p>
	 * <p>
	 * 方法功能描述: 更新字典信息。
	 * </p>
	 * <p>
	 * 输入参数描述: RolePojo onePojo：字典信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 */
	public int updateDicItemByName(DicItemPojo onePojo) throws Exception {
		int count = 0;
		if (onePojo != null) {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("update TB_DIC_ITEM set ");
			strSQL.append("ITEM_VALUE=?");
			strSQL.append(",ITEM_VALUE2=?");
			strSQL.append(",ITEM_INDEX=?");
			strSQL.append(",DIC_PITEMID=?");
			strSQL.append(" where ITEM_NAME=? ");
			List<Object> vList = new ArrayList<Object>();
			vList.add(onePojo.getValue());
			vList.add(onePojo.getValue2());
			vList.add(onePojo.getIndex());
			vList.add(onePojo.getPitemid());
			vList.add(onePojo.getName());
			count = sqlHelper.updateBySql(strSQL.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertStatic
	 * </p>
	 * <p>
	 * 方法功能描述: 插入一条静态参数。
	 * </p>
	 * <p>
	 * 输入参数描述: StaticPojo dbName：输入的数据库ID。 StaticPojo oneStatic：输入的静态参数Pojo。
	 * </p>
	 * <p>
	 * 输出参数描述: String：返回ID
	 * </p>
	 */
	public int insertStatic(StaticPojo oneStatic) throws Exception {
		int count = 0;
		if (oneStatic != null) {
			if (oneStatic.getStaticId() == null
					|| oneStatic.getStaticId().equals("")) {
				oneStatic.setStaticId(BSGuid.getRandomGUID(""));
			}
			StringBuffer strBufSql = new StringBuffer();
			strBufSql.append("insert into PUB_STATIC (");
			strBufSql.append("STATIC_ID,");
			strBufSql.append("STATIC_NAME,");
			strBufSql.append("TABLE_NAME,");
			strBufSql.append("COL_NAME,");
			strBufSql.append("COL_VALUE,");
			strBufSql.append("COL_VALUE_NAME,");
			strBufSql.append("OBJECT_ID,");
			strBufSql.append("TYPE,");
			strBufSql.append("JSFUN,");
			strBufSql.append("VALUE_DESC)");
			strBufSql.append(" values (?,?,?,?,?,?,?,?,?,?)");
			List<Object> vList = new ArrayList<Object>();
			vList.add(oneStatic.getStaticId());
			vList.add(oneStatic.getName());
			vList.add(oneStatic.getTable().toUpperCase());
			vList.add(oneStatic.getCol().toUpperCase());
			vList.add(oneStatic.getValue());
			vList.add(oneStatic.getValueName());
			vList.add(oneStatic.getObjectId());
			vList.add(oneStatic.getType());
			vList.add(oneStatic.getJsFun());
			vList.add(oneStatic.getDesc());

			count = this.sqlHelper.updateBySql(strBufSql.toString(), vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertStatic
	 * </p>
	 * <p>
	 * 方法功能描述: 删除静态参数。
	 * </p>
	 * <p>
	 * 输入参数描述: StaticPojo dbName：输入的数据库ID。 StaticPojo oneStatic：输入的静态参数Pojo。
	 * </p>
	 * <p>
	 * 输出参数描述: String：返回ID
	 * </p>
	 */
	public int deleteStatic(String dbName, String where) throws Exception {
		int count = 0;
		StringBuffer strBufSql = new StringBuffer();
		strBufSql.append("delete from PUB_STATIC ");
		if (where != null && !where.trim().equals("")) {
			strBufSql.append("where " + where);
		}
		count = this.sqlHelper.updateBySql(dbName, strBufSql.toString());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertStatic
	 * </p>
	 * <p>
	 * 方法功能描述: 插入一条静态参数。
	 * </p>
	 * <p>
	 * 输入参数描述: StaticPojo dbName：输入的数据库ID。 StaticPojo oneStatic：输入的静态参数Pojo。
	 * </p>
	 * <p>
	 * 输出参数描述: String：返回ID
	 * </p>
	 */
	public int updateStatic(String dbName, StaticPojo oneStatic)
			throws Exception {
		int count = 0;
		if (oneStatic != null) {
			StringBuffer strBufSql = new StringBuffer();
			strBufSql.append("update PUB_STATIC set ");
			strBufSql.append("STATIC_NAME=?");
			strBufSql.append(",TABLE_NAME=?");
			strBufSql.append(",COL_NAME=?");
			strBufSql.append(",COL_VALUE=?");
			strBufSql.append(",COL_VALUE_NAME=?");
			strBufSql.append(",OBJECT_ID=?");
			strBufSql.append(",TYPE=?");
			strBufSql.append(",JSFUN=?");
			strBufSql.append(",VALUE_DESC=?");
			strBufSql.append(" where STATIC_ID=?");
			List<Object> vList = new ArrayList<Object>();
			vList.add(oneStatic.getName());
			vList.add(oneStatic.getTable().toUpperCase());
			vList.add(oneStatic.getCol().toUpperCase());
			vList.add(oneStatic.getValue());
			vList.add(oneStatic.getValueName());
			vList.add(oneStatic.getObjectId());
			vList.add(oneStatic.getType());
			vList.add(oneStatic.getJsFun());
			vList.add(oneStatic.getDesc());
			vList.add(oneStatic.getStaticId());
			count += this.sqlHelper.updateBySql(dbName, strBufSql.toString(),
					vList);
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getStaticList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到项目树。
	 * </p>
	 * <p>
	 * 输入参数描述: StaticPojo dbName：输入的数据库ID。 String id：输入的ID。 String table：输入的表名。
	 * String col：输入的列名。 String value：输入的值。 String obj：输入的。 String type：输入的。
	 * String where：输入的。 String order：输入的。
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList：结果集
	 * </p>
	 */
	public ArrayList<StaticPojo> getStaticList(String id, String table,
			String col, String value, String obj, String type, String sel,
			String where, String order) throws Exception {
		ArrayList<StaticPojo> staticList = null;
		StringBuffer strBufSql = new StringBuffer();
		strBufSql.append("select ");
		if (!sel.trim().equals("")) {
			strBufSql.append(sel + " ");
		}
		strBufSql.append("t.STATIC_ID,");
		strBufSql.append("t.TABLE_NAME,");
		strBufSql.append("t.COL_NAME,");
		strBufSql.append("t.COL_VALUE,");
		strBufSql.append("t.COL_VALUE_NAME,");
		strBufSql.append("t.OBJECT_ID,");
		strBufSql.append("t.TYPE,");
		strBufSql.append("t.JSFUN,");
		strBufSql.append("t.VALUE_DESC ");
		strBufSql.append("from PUB_STATIC t ");
		strBufSql.append("where t.STATIC_ID is not null");
		if (!id.trim().equals("")) {
			strBufSql.append(" and t.STATIC_ID='" + id + "'");
		}
		if (!table.trim().equals("")) {
			strBufSql.append(" and t.TABLE_NAME='" + table.toUpperCase() + "'");
		}
		if (!col.trim().equals("")) {
			strBufSql.append(" and t.COL_NAME='" + col.toUpperCase() + "'");
		}
		if (!value.trim().equals("")) {
			strBufSql.append(" and t.COL_VALUE='" + value + "'");
		}
		if (!obj.trim().equals("")) {
			strBufSql.append(" and t.OBJECT_ID='" + obj + "'");
		}
		if (!type.trim().equals("")) {
			strBufSql.append(" and t.TYPE='" + type + "'");
		}
		if (!where.trim().equals("")) {
			strBufSql.append(" and " + where);
		}
		// strSql += " group by t.STATIC_ID, t.TABLE_NAME, t.COL_NAME,
		// t.COL_VALUE, t.COL_VALUE_NAME, t.OBJECT_ID, t.TYPE, t.JSFUN ";
		if (!order.trim().equals("")) {
			strBufSql.append(" order by " + order);
		}

		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strBufSql.toString());
		if (rs != null) {
			staticList = new ArrayList<StaticPojo>();
			while (rs.next()) {
				StaticPojo oneStatic = new StaticPojo();
				oneStatic.setStaticId(rs.getString("STATIC_ID"));
				oneStatic.setTable(rs.getString("TABLE_NAME"));
				oneStatic.setCol(rs.getString("COL_NAME"));
				oneStatic.setValue(rs.getString("COL_VALUE"));
				oneStatic.setValueName(rs.getString("COL_VALUE_NAME"));
				oneStatic.setJsFun(rs.getString("JSFUN"));
				if (rs.getString("OBJECT_ID") != null) {
					oneStatic.setObjectId(rs.getString("OBJECT_ID"));
				}
				if (rs.getString("TYPE") != null) {
					oneStatic.setType(rs.getString("TYPE"));
				}
				if (rs.getString("VALUE_DESC") != null) {
					oneStatic.setDesc(rs.getString("VALUE_DESC"));
				}
				staticList.add(oneStatic);
			}
			rs.close();
			// this.sqlHelper.close(rs);
		}
		return staticList;
	}

	/**
	 * <p>
	 * 方法名称: getOneStatic
	 * </p>
	 * <p>
	 * 方法功能描述: 得到项目树。
	 * </p>
	 * <p>
	 * 输入参数描述: StaticPojo dbName：输入的数据库ID。 String table：输入的表名。 String col：输入的列名。
	 * String value：输入的值。
	 * </p>
	 * <p>
	 * 输出参数描述: StaticPojo：结果集
	 * </p>
	 */
	public StaticPojo getOneStatic(String dbName, String table, String col,
			String value) throws Exception {
		StaticPojo oneStatic = new StaticPojo();
		oneStatic.setTable(table);
		oneStatic.setCol(col);
		oneStatic.setStaticId(value);
		oneStatic.setValue(value);

		StringBuffer strBufSql = new StringBuffer();
		strBufSql.append("select ");
		strBufSql.append("t.STATIC_ID,");
		strBufSql.append("t.TABLE_NAME,");
		strBufSql.append("t.COL_NAME,");
		strBufSql.append("t.COL_VALUE,");
		strBufSql.append("t.COL_VALUE_NAME,");
		strBufSql.append("t.OBJECT_ID,");
		strBufSql.append("t.TYPE,");
		strBufSql.append("t.JSFUN,");
		strBufSql.append("t.VALUE_DESC ");
		strBufSql.append("from PUB_STATIC t ");
		strBufSql.append("where ");
		strBufSql.append("t.TABLE_NAME='" + table + "' ");
		strBufSql.append("and t.COL_NAME='" + col + "' ");
		strBufSql.append(" and t.COL_VALUE='" + value + "'");
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(dbName,
				strBufSql.toString());
		if (rs != null && rs.next()) {
			oneStatic.setStaticId(rs.getString("STATIC_ID"));
			oneStatic.setTable(rs.getString("TABLE_NAME"));
			oneStatic.setCol(rs.getString("COL_NAME"));
			oneStatic.setValue(rs.getString("COL_VALUE"));
			oneStatic.setValueName(rs.getString("COL_VALUE_NAME"));

			oneStatic.setJsFun(rs.getString("JSFUN"));
			if (rs.getString("OBJECT_ID") != null) {
				oneStatic.setObjectId(rs.getString("OBJECT_ID"));
			}
			if (rs.getString("TYPE") != null) {
				oneStatic.setType(rs.getString("TYPE"));
			}
			if (rs.getString("VALUE_DESC") != null) {
				oneStatic.setDesc(rs.getString("VALUE_DESC"));
			}
		}
		rs.close();
		return oneStatic;
	}

	/**
	 * <p>
	 * 方法名称: getOneStatic
	 * </p>
	 * <p>
	 * 方法功能描述: 得到项目树。
	 * </p>
	 * <p>
	 * 输入参数描述: StaticPojo dbName：输入的数据库ID。 String staticId：输入的ID。
	 * </p>
	 * <p>
	 * 输出参数描述: StaticPojo：结果集
	 * </p>
	 */
	public StaticPojo getOneStatic(String dbName, String staticId)
			throws Exception {
		StaticPojo oneStatic = new StaticPojo();
		oneStatic.setStaticId(staticId);

		StringBuffer strBufSql = new StringBuffer();
		strBufSql.append("select ");
		strBufSql.append("t.STATIC_ID,");
		strBufSql.append("t.TABLE_NAME,");
		strBufSql.append("t.COL_NAME,");
		strBufSql.append("t.COL_VALUE,");
		strBufSql.append("t.COL_VALUE_NAME,");
		strBufSql.append("t.OBJECT_ID,");
		strBufSql.append("t.TYPE,");
		strBufSql.append("t.JSFUN,");
		strBufSql.append("t.VALUE_DESC ");
		strBufSql.append("from PUB_STATIC t ");
		strBufSql.append("where ");
		strBufSql.append("t.STATIC_ID='" + staticId + "'");
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(dbName,
				strBufSql.toString());
		if (rs != null && rs.next()) {
			oneStatic.setStaticId(rs.getString("STATIC_ID"));
			oneStatic.setTable(rs.getString("TABLE_NAME"));
			oneStatic.setCol(rs.getString("COL_NAME"));
			oneStatic.setValue(rs.getString("COL_VALUE"));
			oneStatic.setValueName(rs.getString("COL_VALUE_NAME"));
			oneStatic.setJsFun(rs.getString("JSFUN"));
			if (rs.getString("OBJECT_ID") != null) {
				oneStatic.setObjectId(rs.getString("OBJECT_ID"));
			}
			if (rs.getString("TYPE") != null) {
				oneStatic.setType(rs.getString("TYPE"));
			}
			if (rs.getString("VALUE_DESC") != null) {
				oneStatic.setDesc(rs.getString("VALUE_DESC"));
			}
		}
		rs.close();
		// this.sqlHelper.close(rs);
		return oneStatic;
	}

	/**
	 * <p>
	 * 方法名称: getOneStatic
	 * </p>
	 * <p>
	 * 方法功能描述: 得到项目树。
	 * </p>
	 * <p>
	 * 输入参数描述: StaticPojo dbName：输入的数据库ID。 String staticId：输入的ID。
	 * </p>
	 * <p>
	 * 输出参数描述: StaticPojo：结果集
	 * </p>
	 */
	public StaticPojo getOneStaticByWhere(String dbName, String where)
			throws Exception {
		StaticPojo oneStatic = new StaticPojo();

		StringBuffer strBufSql = new StringBuffer();
		strBufSql.append("select ");
		strBufSql.append("t.STATIC_ID,");
		strBufSql.append("t.TABLE_NAME,");
		strBufSql.append("t.COL_NAME,");
		strBufSql.append("t.COL_VALUE,");
		strBufSql.append("t.COL_VALUE_NAME,");
		strBufSql.append("t.OBJECT_ID,");
		strBufSql.append("t.TYPE,");
		strBufSql.append("t.JSFUN,");
		strBufSql.append("t.VALUE_DESC ");
		strBufSql.append("from PUB_STATIC t ");
		strBufSql.append("where ");
		strBufSql.append("t.STATIC_ID is not null " + where);
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(dbName,
				strBufSql.toString());
		if (rs != null && rs.next()) {
			oneStatic.setStaticId(rs.getString("STATIC_ID"));
			oneStatic.setTable(rs.getString("TABLE_NAME"));
			oneStatic.setCol(rs.getString("COL_NAME"));
			oneStatic.setValue(rs.getString("COL_VALUE"));
			oneStatic.setValueName(rs.getString("COL_VALUE_NAME"));
			oneStatic.setJsFun(rs.getString("JSFUN"));
			if (rs.getString("OBJECT_ID") != null) {
				oneStatic.setObjectId(rs.getString("OBJECT_ID"));
			}
			if (rs.getString("TYPE") != null) {
				oneStatic.setType(rs.getString("TYPE"));
			}
			if (rs.getString("VALUE_DESC") != null) {
				oneStatic.setDesc(rs.getString("VALUE_DESC"));
			}
		}
		rs.close();
		// this.sqlHelper.close(rs);
		return oneStatic;
	}

	/**
	 * <p>
	 * 方法名称: setStaticIni
	 * </p>
	 * <p>
	 * 方法功能描述: 初始化静态参数表。
	 * </p>
	 * <p>
	 * 输入参数描述: StaticPojo dbName：输入的数据库ID。
	 * </p>
	 * <p>
	 * 输出参数描述: StaticPojo：结果集
	 * </p>
	 */
	public void setStaticIni(String dbName) throws Exception {
		// 从系统配置文件中得到静态参数的配置文件目录
		String path = System.getProperty("user.dir");
		String filePath = BSCommon.getConfigValue("userconfig_static_path");
		if (filePath != null && !filePath.trim().equals("")) {
			path += ("\\" + filePath);
		}
		// 读取目录下所有文件。
		XMLReader xml = new XMLReader(path + "\\PUB_STATIC.xml");
		if (xml.isRealFile()) {
			// 处理配置项
			String tableName = xml.getDocument().getRootElement().getName()
					.toUpperCase();
			this.sqlHelper.updateBySql(dbName, "delete from " + tableName);
			StringBuffer sql = new StringBuffer();
			sql.append("insert into " + tableName);
			sql.append(" (");
			// 得到列
			String colStr = xml.getAttributeByName("//PUB_STATIC/COLLIST",
					"COL").trim();
			String keyCol = xml.getAttributeByName("//PUB_STATIC/COLLIST",
					"KEY").toUpperCase();
			if (colStr.substring(colStr.length() - 1).equals(",")) {
				colStr = colStr.substring(colStr.length() - 2);
			}
			sql.append(colStr);
			sql.append(") values");
			String[] colLlist = colStr.split(",");
			// 得到实际数据
			ArrayList rows = xml.getAllNodeByNodeName("//PUB_STATIC/ROWS");
			for (int j = 0; j < rows.size(); j++) {
				String oneSql = sql.toString();
				DefaultElement de = (DefaultElement) rows.get(j);
				oneSql += " (";
				String tempValue = "";
				for (int k = 0; k < colLlist.length; k++) {
					if (!colLlist[k].trim().equals("")) {
						tempValue = (de.attribute(colLlist[k].trim()))
								.getValue();
						if (colLlist[k].trim().equals(keyCol)
								&& tempValue.trim().equals("")) {
							tempValue = "'"
									+ BSGuid.getRandomGUID("").toUpperCase()
									+ "'";
						}
						oneSql += tempValue;
						if (k < colLlist.length - 1) {
							oneSql += ",";
						}
					}
				}
				oneSql += ")";
				// 插入数据库
				System.out.println(oneSql);
				this.sqlHelper.updateBySql(dbName, oneSql);
			}
		}
		xml.close();
	}

	public SqlExecute getSqlHelper() {
		return sqlHelper;
	}

	public void setSqlHelper(SqlExecute sqlHelper) {
		this.sqlHelper = sqlHelper;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getDicSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.DIC_ID");
		strSQL.append(",t.DIC_NAME");
		strSQL.append(",t.DIC_DESC");
		strSQL.append(",(select count(v.ITEM_ID) from TB_DIC_ITEM v where v.DIC_ID=t.DIC_ID) as ITEM_COUNT");
		strSQL.append(",t.DIC_PID");
		strSQL.append(" from TB_DIC t");
		strSQL.append(" where t.DIC_ID is not null ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by" + orderBy);
		}
		return strSQL;
	}

	// 填充字典信息
	private DicPojo _setOneDicPojo(CachedRowSet rs) throws Exception {
		DicPojo onePojo = new DicPojo();
		// ID
		if (rs.getString("DIC_ID") != null) {
			onePojo.setId(rs.getString("DIC_ID").trim());
		}
		// 名称
		if (rs.getString("DIC_NAME") != null) {
			onePojo.setName(rs.getString("DIC_NAME").trim());
		}
		// 描述
		if (rs.getString("DIC_DESC") != null) {
			onePojo.setDesc(rs.getString("DIC_DESC").trim());
		}
		// 联动上级
		if (rs.getString("DIC_PID") != null) {
			onePojo.setPid(rs.getString("DIC_PID"));
		}
		onePojo.setItemCount(rs.getInt("ITEM_COUNT"));

		return onePojo;
	}

	// 得到资源实例查询的SQL语句
	private StringBuffer _getDicItemSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer("select ");
		strSQL.append("t.DIC_ID");
		strSQL.append(",t.DIC_NAME");
		strSQL.append(",t.DIC_DESC");
		strSQL.append(",0 as ITEM_COUNT");
		strSQL.append(",t.DIC_PID");
		//
		strSQL.append(",t1.ITEM_ID");
		strSQL.append(",t1.ITEM_NAME");
		strSQL.append(",t1.ITEM_VALUE");
		strSQL.append(",t1.ITEM_VALUE2");
		strSQL.append(",t1.ITEM_INDEX");
		strSQL.append(",t1.DIC_PITEMID");
		strSQL.append(" from TB_DIC t,TB_DIC_ITEM t1");
		strSQL.append(" where t.DIC_ID=t1.DIC_ID ");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		if (orderBy != null && !orderBy.trim().equals("")) {
			strSQL.append(" order by" + orderBy);
		} else {
			strSQL.append(" order by t1.ITEM_INDEX,t1.ITEM_VALUE");
		}
		return strSQL;
	}

	// 填充字典信息
	private DicItemPojo _setOneDicItemPojo(CachedRowSet rs) throws Exception {
		DicItemPojo onePojo = new DicItemPojo();
		onePojo.setDic(this._setOneDicPojo(rs));
		// ID
		onePojo.setId(rs.getString("ITEM_ID").trim());
		// 名称
		onePojo.setName(rs.getString("ITEM_NAME").trim());
		// 描述
		onePojo.setValue(rs.getString("ITEM_VALUE").trim());
		if (rs.getString("ITEM_VALUE2") != null) {
			onePojo.setValue2(rs.getString("ITEM_VALUE2").trim());
		}
		onePojo.setIndex(rs.getInt("ITEM_INDEX"));
		if (rs.getString("DIC_PITEMID") != null) {
			onePojo.setPitemid(rs.getString("DIC_PITEMID").trim());
		}

		return onePojo;
	}
}
