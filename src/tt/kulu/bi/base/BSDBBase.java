package tt.kulu.bi.base;

import net.sf.json.JSONObject;
import tt.kulu.bi.user.pojo.LoginUserPojo;
import tt.kulu.bi.user.pojo.UserPojo;

import com.tt4j2ee.BSDateEx;
import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BSDBBase
 * </p>
 * <p>
 * 功能描述: 数据库操作基类
 * </p>
 * <p>
 * 作者: 梁浩
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * 
 * <p>
 * 创建日期: 2006-8-24
 * </p>
 */
public class BSDBBase {
	protected SqlExecute sqlHelper = null;
	protected BSObject m_bs = null;
	protected LoginUserPojo loginUser = null;
	protected BSDateEx bsDate = new BSDateEx();

	public BSDBBase() {

	}

	public BSDBBase(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		this.setSqlHelper(sqlHelper);

		this.setM_bs(m_bs);
		if (m_bs != null && m_bs.getCheckLogin() != null) {
			String loginUserJSON = m_bs.getPublicMap().get(
					m_bs.getCheckLogin().getLogName());
			this.loginUser = (LoginUserPojo) JSONObject.toBean(
					JSONObject.fromObject(loginUserJSON), LoginUserPojo.class);
		}
		// this.powerDB = new BSPowerDBMang(sqlHelper, m_bs);
	}

	public SqlExecute getSqlHelper() {
		return sqlHelper;
	}

	public void setSqlHelper(SqlExecute sqlHelper) throws Exception {
		this.sqlHelper = sqlHelper;
		if (this.sqlHelper != null) {
			// this.staticDB = new StaticDBMang(this.sqlHelper);
		}
	}

	public BSObject getM_bs() {
		return this.m_bs;
	}

	public void setM_bs(BSObject m_bs) {
		this.m_bs = m_bs;
	}

}
