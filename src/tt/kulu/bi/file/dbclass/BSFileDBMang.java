package tt.kulu.bi.file.dbclass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.file.pojo.BFSFilePojo;
import tt.kulu.bi.user.pojo.UserPojo;

import com.tt4j2ee.BSCommon;
import com.tt4j2ee.BSGuid;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSFileDBMang
 * </p>
 * <p>
 * 功能描述: 文件管理数据库操作类
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
public class BSFileDBMang extends BSDBBase {
	public BSFileDBMang(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getMenuList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到功能菜单。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 */
	public ArrayList<BFSFilePojo> getFileList(String where, String orderBy,
			List<Object> vList) throws Exception {
		ArrayList<BFSFilePojo> fileList = new ArrayList<BFSFilePojo>();
		StringBuffer strSQL = _getFileSelectSQL(where, orderBy);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				fileList.add(this._setOneFilePojo(rs));
			}
			rs.close();
		}
		return fileList;
	}

	/**
	 * <p>
	 * 方法名称: getMenuList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到功能菜单。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 */
	public ArrayList<BFSFilePojo> getFileList(String where, String orderBy,
			List<Object> vList, long from, long to) throws Exception {
		ArrayList<BFSFilePojo> fileList = new ArrayList<BFSFilePojo>();
		// 翻页代码
		StringBuffer strSQL = this._getFileSelectSQL(where, orderBy);
		strSQL.append(" LIMIT " + (to - from + 1) + " OFFSET " + from);
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null) {
			while (rs.next()) {
				fileList.add(this._setOneFilePojo(rs));
			}
			rs.close();
		}
		return fileList;
	}

	/**
	 * <p>
	 * 方法名称: getMenuList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到功能菜单。
	 * </p>
	 * <p>
	 * 输入参数描述: String where：输入的查询条件。
	 * </p>
	 * <p>
	 * 输出参数描述: boolean
	 * </p>
	 */
	public long getFileCount(String where, List<Object> vList) throws Exception {
		long count = 0;
		StringBuffer strSQL = new StringBuffer(
				"select count(t.F_ID) as FILE_COUNT");
		strSQL.append(" from T_FILE t ");
		strSQL.append(" where t.F_ID is not null");
		if (where != null && !where.trim().equals("")) {
			strSQL.append(" " + where);
		}
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			count = rs.getLong("FILE_COUNT");
		}
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getOneFile
	 * </p>
	 * <p>
	 * 方法功能描述: 得到二维码图片。
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
	public BFSFilePojo getOneFile(String fId) throws Exception {
		BFSFilePojo oneFile = null;
		List<Object> vList = new ArrayList<Object>();
		vList.add(fId);
		StringBuffer strSQL = _getFileSelectSQL(" and t.F_ID=?", "");
		ResultSet rs = this.sqlHelper.queryBySql(strSQL.toString(), vList);
		if (rs != null && rs.next()) {
			oneFile = (this._setOneFilePojo(rs));

			rs.close();
		}

		return oneFile;
	}

	/**
	 * <p>
	 * 方法名称: getOneFile
	 * </p>
	 * <p>
	 * 方法功能描述: 得到二维码图片。
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
	public BFSFilePojo getOneFileByWhere(String bId, int index)
			throws Exception {
		BFSFilePojo oneFile = null;
		StringBuffer strSQL = _getFileSelectSQL(" and t.B_ID='" + bId
				+ "' and t.F_INDEX=" + index, "");
		CachedRowSet rs = this.sqlHelper.queryCachedBySql(strSQL.toString());
		if (rs != null && rs.next()) {
			oneFile = (this._setOneFilePojo(rs));

			rs.close();
		}
		if (oneFile == null) {
			oneFile = new BFSFilePojo();
			oneFile.setInstId(BSGuid.getRandomGUID());
		}
		return oneFile;
	}

	/**
	 * <p>
	 * 方法名称: updateFile
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑广告图片。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：用户信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateFile(BFSFilePojo file, int size, InputStream in)
			throws Exception {
		int count = 0;
		// 删除文件
		if (!file.getOldId().equals("")) {
			count = this.sqlHelper
					.updateBySql("delete from T_FILE t where t.F_ID='"
							+ file.getOldId() + "'");
		}
		//
		// 入库
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("insert into T_FILE (F_ID,B_ID,U_USER,F_TITLE,F_NAME,F_NAME_EX,F_SIZE,U_DATE,F_TYPE,F_STATE,F_FILE");
		strSQL.append(") values (");
		strSQL.append("'" + file.getInstId() + "'");
		strSQL.append(",'" + file.getBissId() + "'");
		strSQL.append(",'" + file.getUser().getInstId() + "'");
		strSQL.append(",'" + file.getTitle() + "'");
		strSQL.append(",'" + file.getFileName() + "'");
		strSQL.append(",'" + file.getFileEx() + "'");
		strSQL.append(",'" + file.getSize() + "'");
		strSQL.append("," + SqlExecute.getCharToDateSql(file.getUpdateDate()));
		strSQL.append("," + file.getType());
		strSQL.append("," + file.getState());
		strSQL.append(",?");
		strSQL.append(")");
		count += this.sqlHelper.updateBySql(strSQL.toString(), in,
				in.available());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateFile
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑广告图片。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：用户信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int updateFile(BFSFilePojo file) throws Exception {
		int count = 0;
		// 删除文件
		if (!file.getInstId().equals("")) {
			count = this.sqlHelper
					.updateBySql("delete from T_FILE t where t.F_ID='"
							+ file.getInstId() + "'");
		}
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("insert into T_FILE (F_ID,B_ID,H_ID,F_INDEX,U_USER,F_PATH,F_URL,F_TITLE,F_NAME,F_NAME_EX,F_SIZE,U_DATE,F_TYPE,F_STATE");
		strSQL.append(") values (");
		strSQL.append("'" + file.getInstId() + "'");
		strSQL.append(",'" + file.getBissId() + "'");
		strSQL.append(",'" + file.getHashID() + "'");
		strSQL.append("," + file.getIndex());
		strSQL.append(",'" + file.getUser().getInstId() + "'");
		strSQL.append(",'" + file.getFilePath() + "'");
		strSQL.append(",'" + file.getFileUrl() + "'");
		strSQL.append(",'" + file.getTitle() + "'");
		strSQL.append(",'" + file.getFileName() + "'");
		strSQL.append(",'" + file.getFileEx() + "'");
		strSQL.append(",'" + file.getSize() + "'");
		strSQL.append("," + SqlExecute.getCharToDateSql(file.getUpdateDate()));
		strSQL.append("," + file.getType());
		strSQL.append("," + file.getState());
		strSQL.append(")");

		count += this.sqlHelper.updateBySql(strSQL.toString());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateFile
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑广告图片。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：用户信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int onlyUpdateFile(BFSFilePojo file, String oid) throws Exception {
		int count = 0;
		// 删除文件
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("update T_FILE");
		strSQL.append(" set ");
		strSQL.append("F_ID='" + file.getInstId() + "'");
		strSQL.append(",F_INDEX=" + file.getIndex());
		strSQL.append(",F_PATH='" + file.getFilePath() + "'");
		strSQL.append(",F_URL='" + file.getFileUrl() + "'");
		strSQL.append(",F_TITLE='" + file.getTitle() + "'");
		strSQL.append(",F_NAME='" + file.getFileName() + "'");
		strSQL.append(",F_NAME_EX='" + file.getFileEx() + "'");
		strSQL.append(",F_SIZE='" + file.getSize() + "'");
		strSQL.append(",U_DATE="
				+ SqlExecute.getCharToDateSql(file.getUpdateDate()));
		strSQL.append(",F_TYPE=" + file.getType());
		strSQL.append(" where F_ID='" + oid + "'");

		count += this.sqlHelper.updateBySql(strSQL.toString());
		return count;
	}

	public InputStream getFileInputStreamById(String fId) throws Exception {
		InputStream in = null;
		CachedRowSet rs = this.sqlHelper
				.queryCachedBySql("select t.F_FILE from T_FILE t where t.F_ID='"
						+ fId + "' and t.F_TYPE=0");
		if (rs != null && rs.next()) {
			in = rs.getBlob(1).getBinaryStream();
		}
		return in;
	}

	/**
	 * <p>
	 * 方法名称: updateFile
	 * </p>
	 * <p>
	 * 方法功能描述: 编辑广告图片。
	 * </p>
	 * <p>
	 * 输入参数描述: GroupPojo onePojo：用户信息对象。
	 * </p>
	 * <p>
	 * 输出参数描述: int count：影响行数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public int deleteFile(String where, List<Object> vList) throws Exception {
		int count = 0;
		// 删除文件
		count = this.sqlHelper
				.updateBySql("delete from T_FILE t where t.F_ID is not null "
						+ where, vList);
		//
		return count;
	}

	/**
	 * <p>
	 * 方法名称: getOneQRImg
	 * </p>
	 * <p>
	 * 方法功能描述: 得到二维码图片。
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
	public CachedRowSet getOneQRImg(String qrId) throws Exception {
		// 得到图片
		return this.sqlHelper
				.queryCachedBySql("select t.QR_IMG from T_QR t where t.QR_ID='"
						+ qrId + "'");
	}

	// 设置一个菜单实体
	private BFSFilePojo _setOneFilePojo(ResultSet rs) throws Exception {
		BFSFilePojo onePojo = new BFSFilePojo();
		onePojo.setUser(new UserPojo());
		onePojo.setInstId(rs.getString("F_ID"));
		onePojo.setBissId(rs.getString("B_ID"));
		onePojo.setSize(rs.getString("F_SIZE"));
		onePojo.setIndex(rs.getInt("F_INDEX"));
		onePojo.setType(rs.getInt("F_TYPE"));
		onePojo.setTitle(rs.getString("F_TITLE"));
		onePojo.setFileName(rs.getString("F_NAME"));
		onePojo.setReadNum(rs.getLong("R_NUM"));
		if (rs.getString("F_PATH") != null) {
			onePojo.setFilePath(rs.getString("F_PATH"));
		}
		if (rs.getString("F_URL") != null) {
			onePojo.setFileUrl(rs.getString("F_URL"));
		}
		onePojo.setFileEx(rs.getString("F_NAME_EX"));
		onePojo.setUpdateDate(rs.getString("U_DATE"));
		onePojo.setHashID(rs.getString("H_ID"));
		return onePojo;
	}

	// 取得应用信息的SQL语句
	private StringBuffer _getFileSelectSQL(String where, String orderBy)
			throws Exception {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select ");
		strSQL.append("t.F_ID,");
		strSQL.append("t.B_ID,");
		strSQL.append("t.U_USER,");
		strSQL.append("t.F_INDEX,");
		strSQL.append("t.F_TITLE,");
		strSQL.append("t.F_NAME,");
		strSQL.append("t.F_NAME_EX,");
		strSQL.append("t.F_PATH,");
		strSQL.append("t.F_URL,");
		strSQL.append("t.F_SIZE,");
		strSQL.append("t.F_TYPE,");
		strSQL.append("t.F_STATE,");
		strSQL.append("t.H_ID,");
		strSQL.append("t.F_PASSWORD,");
		strSQL.append("t.R_NUM,");
		strSQL.append(SqlExecute.getDateToCharSql("t.U_DATE") + " as U_DATE");
		strSQL.append(" from T_FILE t ");
		strSQL.append(" where t.F_ID is not null");
		if (!where.trim().equals("")) {
			strSQL.append(where);
		}
		if (!orderBy.trim().equals("")) {
			strSQL.append(" order by " + orderBy);
		}
		return strSQL;
	}

	// 保存文件到目录
	public File saveFile(InputStream in, String fileName) {
		File file = new File(BSCommon.getConfigValue("upload_path") + "//f_"
				+ fileName);
		(new File(file.getParent())).mkdir();
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((in.read(buffer)) != -1) {
				os.write(buffer);
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

}
