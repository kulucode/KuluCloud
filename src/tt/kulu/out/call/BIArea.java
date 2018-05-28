package tt.kulu.out.call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import tt.kulu.bi.area.dbclass.AreaDBMang;
import tt.kulu.bi.area.pojo.AreaPojo;
import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.pojo.DicItemPojo;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BIArea
 * </p>
 * <p>
 * 功能描述: 地域接口类
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
public class BIArea extends BSDBBase {
	public BIArea(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	public BIArea(BSObject m_bs) throws Exception {
		super(null, m_bs);
	}

	/**
	 * <p>
	 * 方法名称: getAreaByRedis
	 * </p>
	 * <p>
	 * 方法功能描述: 从redis得到行政区域项目
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
	public AreaPojo getAreaByRedis(String itemId) throws Exception {
		AreaPojo onePojo = new AreaPojo();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getStringData("KAREA_" + itemId,
				URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到
			onePojo = this.getOneAreaById(itemId);
			if (onePojo != null) {
				redisBI.setStringData("KAREA_" + itemId,
						JSONObject.fromObject(onePojo).toString(),
						URLlImplBase.REDIS_KULUDATA);
			}
		} else {
			Map config = new HashMap();
			config.put("areaClass", DicItemPojo.class);
			config.put("areaType", DicItemPojo.class);
			onePojo = (AreaPojo) JSONObject.toBean(
					JSONObject.fromObject(redisS), AreaPojo.class, config);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: setAreaToRedis
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
	public void setAreaToRedis(String id) throws Exception {
		AreaPojo onePojo = this.getOneAreaById(id);
		BIRedis redisBI = new BIRedis();
		if (onePojo != null) {
			redisBI.setStringData("KAREA_" + id, JSONObject.fromObject(onePojo)
					.toString(), URLlImplBase.REDIS_KULUDATA);
		}
	}

	/**
	 * <p>
	 * 方法名称: getAreaList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到行政区域列表。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输入参数描述: long f：输入的开始位置。
	 * </p>
	 * <p>
	 * 输入参数描述: long t：输入的结束位置。
	 * </p>
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<AreaPojo> getAreaList(JSONObject paras, long f, long t)
			throws Exception {
		ArrayList<AreaPojo> list = new ArrayList<AreaPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getAreaList(sqlHelper, paras, f, t);
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
	 * 方法名称: getAreaList
	 * </p>
	 * <p>
	 * 方法功能描述: 得到行政区域列表。
	 * </p>
	 * <p>
	 * 创建人: 梁浩
	 * </p>
	 * <p>
	 * 输入参数描述: JSONObject paras：输入的条件。
	 * </p>
	 * <p>
	 * 输入参数描述: long f：输入的开始位置。
	 * </p>
	 * <p>
	 * 输入参数描述: long t：输入的结束位置。
	 * </p>
	 * </p>
	 * <p>
	 * 输出参数描述: ArrayList<ActivityPojo> list :活动列表。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public ArrayList<AreaPojo> getAreaList(SqlExecute sqlHelper,
			JSONObject paras, long f, long t) throws Exception {
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String orderBy = " t.A_ID";
		String where = "";
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and (t.A_ID=? OR t.A_SNAME like ? OR t.A_ALLNAME like ?)";
					vList.add(v);
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
				if (key.equals("class")) {
					// 根
					where += " and t1.A_CLASS=?";
					vList.add(v);
				}
				if (key.equals("type")) {
					// 根
					where += " and t.A_TYPE=?";
					vList.add(v);
				}
			}
		}
		AreaDBMang areaDB = new AreaDBMang(sqlHelper, m_bs);
		paras.put("max", areaDB.getAreaCount(where, vList));
		return areaDB.getAreaList(where, orderBy, vList, f, t);
	}

	/**
	 * <p>
	 * 方法名称: getOneAreaById
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量行政区域项目
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 数据库操作类对象sqlHelper、查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public AreaPojo getOneAreaById(String id) throws Exception {
		AreaPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneAreaById(sqlHelper, id);
		} catch (Exception ep) {
			ep.printStackTrace();
			throw ep;
		} finally {
			sqlHelper.close();
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: getOneAreaById
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量行政区域项目
	 * </p>
	 * <p>
	 * 创建人: 马维
	 * </p>
	 * <p>
	 * 输入参数描述: 查询条件sTest。
	 * </p>
	 * <p>
	 * 输出参数描述: BSObject：BinaryStar框架参数集。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public AreaPojo getOneAreaById(SqlExecute sqlHelper, String id)
			throws Exception {
		// 设置行政区域列表
		AreaDBMang areaDB = new AreaDBMang(sqlHelper, m_bs);
		return areaDB.getOneAreaById(id);
	}

	/**
	 * <p>
	 * 方法名称: insertArea
	 * </p>
	 * <p>
	 * 方法功能描述: 插入新政地域。
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
	public int insertArea(AreaPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertArea(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setAreaToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertArea
	 * </p>
	 * <p>
	 * 方法功能描述: 插入新政地域。
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
	public int insertArea(SqlExecute sqlHelper, AreaPojo onePojo)
			throws Exception {
		AreaDBMang areaDB = new AreaDBMang(sqlHelper, m_bs);
		return areaDB.insertArea(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateArea
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
	public int updateArea(AreaPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateArea(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setAreaToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateArea
	 * </p>
	 * <p>
	 * 方法功能描述: 更新新政地域。
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
	public int updateArea(SqlExecute sqlHelper, AreaPojo onePojo)
			throws Exception {
		AreaDBMang areaDB = new AreaDBMang(sqlHelper, m_bs);
		return areaDB.updateArea(onePojo);
	}

}
