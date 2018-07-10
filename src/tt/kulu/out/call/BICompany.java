package tt.kulu.out.call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.tt4j2ee.BIRedis;

import net.sf.json.JSONObject;
import tt.kulu.bi.area.pojo.AreaPojo;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.company.dbclass.CompanyDBMang;
import tt.kulu.bi.company.pojo.CompanyPojo;
import tt.kulu.bi.dic.pojo.DicItemPojo;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BICompany
 * </p>
 * <p>
 * 功能描述: 企业接口类
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
public class BICompany extends BSDBBase {
	public BICompany(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	public BICompany(BSObject m_bs) throws Exception {
		super(null, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getDicItemByRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 从redis得到数据字典项目
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public CompanyPojo getCompanyByRedis(String id) throws Exception {
		CompanyPojo onePojo = new CompanyPojo();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getStringData("KCOMP_" + id,
				URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到
			onePojo = this.getOneCompanyById(id);
			if (onePojo != null) {
				redisBI.setStringData("KCOMP_" + id,
						JSONObject.fromObject(onePojo).toString(),
						URLlImplBase.REDIS_KULUDATA);
			}
		} else {
			Map config = new HashMap();
			config.put("area", AreaPojo.class);
			config.put("areaClass", DicItemPojo.class);
			config.put("areaType", DicItemPojo.class);
			onePojo = (CompanyPojo) JSONObject.toBean(
					JSONObject.fromObject(redisS), CompanyPojo.class);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getThisCompanyByRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 从redis得到数据字典项目
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public CompanyPojo getThisCompanyByRedis() throws Exception {
		CompanyPojo onePojo = new CompanyPojo();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getStringData("KCOMP_THIS",
				URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到
			onePojo = this.getOneCompanyByType(1);
			if (onePojo != null) {
				redisBI.setStringData("KCOMP_THIS",
						JSONObject.fromObject(onePojo).toString(),
						URLlImplBase.REDIS_KULUDATA);
			}
		} else {
			Map config = new HashMap();
			config.put("area", AreaPojo.class);
			config.put("areaClass", DicItemPojo.class);
			config.put("areaType", DicItemPojo.class);
			onePojo = (CompanyPojo) JSONObject.toBean(
					JSONObject.fromObject(redisS), CompanyPojo.class);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: setCompanyToRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 将企业放入redis
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public void setCompanyToRedis(String id) throws Exception {
		CompanyPojo onePojo = this.getOneCompanyById(id);
		BIRedis redisBI = new BIRedis();
		if (onePojo != null) {
			redisBI.setStringData("KCOMP_" + id, JSONObject.fromObject(onePojo)
					.toString(), URLlImplBase.REDIS_KULUDATA);
			if (onePojo.getType() == 1) {
				redisBI.setStringData("KCOMP_THIS",
						JSONObject.fromObject(onePojo).toString(),
						URLlImplBase.REDIS_KULUDATA);
			}
		}
	}

	/**
	 * <p>
	 * 方法名称: getCompanyList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量企业
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<CompanyPojo> getCompanyList(JSONObject paras, long f,
			long t) throws Exception {
		ArrayList<CompanyPojo> list = new ArrayList<CompanyPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getCompanyList(sqlHelper, paras, f, t);
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
	 * 方法名称: getCompanyList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量企业
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: BSObject m_bs:BinaryStar框架参数集。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<CompanyPojo> getCompanyList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		ArrayList<CompanyPojo> list = new ArrayList<CompanyPojo>();
		// 参数设定
		String where = "";
		String key = "";
		String orderBy = " t.C_DATE desc,T.USER_INSTID";
		List<Object> vList = new ArrayList<Object>();
		if (paras != null) {
			Iterator<String> keys = paras.keys();
			while (keys.hasNext()) {
				key = keys.next();
				String v = paras.getString(key);
				if (!v.equals("")) {
					if (key.equals("key")) {
						// 关键字
						where += " and (t.COMP_LINKPHONE=? OR t.COMP_NAME like ? or T.COMP_DESC like ? or T.COMP_LINK like ?)";
						vList.add(v);
						vList.add("%" + v + "%");
						vList.add("%" + v + "%");
						vList.add("%" + v + "%");
					}
				}
			}
		}
		// 设置企业列表
		CompanyDBMang compDB = new CompanyDBMang(sqlHelper, m_bs);
		paras.put("max", compDB.getCompanyCount(where, vList));
		return compDB.getCompanyList(where, orderBy, vList, f, t);
	}

	/**
	 * <p>
	 * 方法名：getOneCompanyById
	 * </p>
	 * <p>
	 * 方法描述：得到一个企业
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public CompanyPojo getOneCompanyByType(int id) throws Exception {
		CompanyPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneCompanyByType(sqlHelper, id);
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneCompanyById
	 * </p>
	 * <p>
	 * 方法描述：得到一个企业
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public CompanyPojo getOneCompanyByType(SqlExecute sqlHelper, int type)
			throws Exception {
		CompanyPojo onePojo = null;
		CompanyDBMang compDB = new CompanyDBMang(sqlHelper, m_bs);
		List<Object> vList = new ArrayList<Object>();
		vList.add(type);
		ArrayList<CompanyPojo> list = compDB.getCompanyList(
				" and t.COMP_TYPE=?", "", vList, 0, 1);
		if (list.size() > 0) {
			onePojo = list.get(0);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneCompanyById
	 * </p>
	 * <p>
	 * 方法描述：得到一个企业
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public CompanyPojo getOneCompanyById(String id) throws Exception {
		CompanyPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneCompanyById(sqlHelper, id);
		} catch (Exception ep) {
			ep.printStackTrace();
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名：getOneCompanyById
	 * </p>
	 * <p>
	 * 方法描述：得到一个企业
	 * </p>
	 * <p>
	 * 输入参数：BSObject m_bs：BS框架业务对象
	 * </p>
	 * <p>
	 * 作者:曹祺
	 * </p>
	 * <p>
	 * 输出参数：BSObject：BS框架业务对象
	 * </p>
	 */
	public CompanyPojo getOneCompanyById(SqlExecute sqlHelper, String id)
			throws Exception {
		CompanyDBMang compDB = new CompanyDBMang(sqlHelper, m_bs);
		return compDB.getOneCompanyById(id);
	}

	/**
	 * <p>
	 * 方法名称: insertCompany
	 * </p>
	 * <p>
	 * 方法功能描述: 插入企业。
	 * </p>
	 * <p>
	 * 输入参数描述:数据字典实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertCompany(CompanyPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertCompany(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setCompanyToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertCompany
	 * </p>
	 * <p>
	 * 方法功能描述: 插入企业。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、数据字典实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int insertCompany(SqlExecute sqlHelper, CompanyPojo onePojo)
			throws Exception {
		CompanyDBMang compDB = new CompanyDBMang(sqlHelper, m_bs);
		return compDB.insertCompany(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateCompany
	 * </p>
	 * <p>
	 * 方法功能描述: 更新用户数据字典。
	 * </p>
	 * <p>
	 * 输入参数描述:数据字典实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateCompany(CompanyPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateCompany(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setCompanyToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateCompany
	 * </p>
	 * <p>
	 * 方法功能描述: 更新企业。
	 * </p>
	 * <p>
	 * 输入参数描述:数据库操作类对象sqlHelper、数据字典实体对象oneRole
	 * </p>
	 * <p>
	 * 输出参数描述:java.lang.Integer 整型
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 */
	public int updateCompany(SqlExecute sqlHelper, CompanyPojo onePojo)
			throws Exception {
		CompanyDBMang compDB = new CompanyDBMang(sqlHelper, m_bs);
		return compDB.updateCompany(onePojo);
	}

}
