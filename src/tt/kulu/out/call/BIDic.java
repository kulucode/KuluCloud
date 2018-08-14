package tt.kulu.out.call;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.tt4j2ee.BIRedis;

import tt.kulu.bi.base.BSDBBase;
import tt.kulu.bi.base.URLlImplBase;
import tt.kulu.bi.dic.dbclass.StaticDBMang;
import tt.kulu.bi.dic.pojo.DicItemPojo;
import tt.kulu.bi.dic.pojo.DicPojo;
import tt.kulu.bi.dic.pojo.StaticPojo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tt4j2ee.db.SqlExecute;
import com.tt4j2ee.m.BSObject;

/**
 * <p>
 * 标题: BIDic
 * </p>
 * <p>
 * 功能描述: 数据字典接口类
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
public class BIDic extends BSDBBase {
	public BIDic(SqlExecute sqlHelper, BSObject m_bs) throws Exception {
		super(sqlHelper, m_bs);
	}

	public BIDic(BSObject m_bs) throws Exception {
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
	public DicItemPojo getDicItemByRedis(String itemId) throws Exception {
		DicItemPojo onePojo = new DicItemPojo();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getMapData("KDICITEM_MAP", itemId,
				URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到
			onePojo = this.getOneDicItemById(itemId);
			if (onePojo != null) {
				redisBI.setMapData("KDICITEM_MAP", itemId,
						JSON.toJSONString(onePojo), URLlImplBase.REDIS_KULUDATA);
			}
		} else {
			onePojo = (DicItemPojo) JSON.parseObject(redisS, DicItemPojo.class);
		}
		return onePojo;
	}

	/**
	 * <p>
	 * 方法名称: setDicItemToRedis
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
	public void setDicItemToRedis(String itemId) throws Exception {
		DicItemPojo onePojo = this.getOneDicItemById(itemId);
		BIRedis redisBI = new BIRedis();
		if (onePojo != null) {
			redisBI.setMapData("KDICITEM_MAP", itemId,
					JSON.toJSONString(onePojo), URLlImplBase.REDIS_KULUDATA);
		}
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
	public JSONArray getDicItemListByRedis(String dicId) throws Exception {
		JSONArray list = new JSONArray();
		BIRedis redisBI = new BIRedis();
		String redisS = redisBI.getStringData("KDIC_" + dicId,
				URLlImplBase.REDIS_KULUDATA);
		if (redisS == null || redisS.trim().equals("")) {
			// 从数据库的到
			JSONObject _paras = new JSONObject();
			_paras.put("dic", dicId);
			ArrayList<DicItemPojo> items = this.getDicItemList(_paras);
			list = JSONArray.fromObject(items);
			redisBI.setStringData("KDIC_" + dicId, list.toString(),
					URLlImplBase.REDIS_KULUDATA);
		} else {
			list = JSONArray.fromObject(redisS);
		}
		return list;
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
	public void setDicItemListToRedis(String dicId) throws Exception {
		JSONObject _paras = new JSONObject();
		_paras.put("dic", dicId);
		ArrayList<DicItemPojo> items = this.getDicItemList(_paras);
		BIRedis redisBI = new BIRedis();
		redisBI.setStringData("KDIC_" + dicId,
				(JSONArray.fromObject(items)).toString(),
				URLlImplBase.REDIS_KULUDATA);
	}

	/**
	 * <p>
	 * 方法名称: getDicList
	 * </p>
	 * <p>
	 * 方法功能描述:得到数据字典列表，带条件
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
	public ArrayList<DicPojo> getDicList(JSONObject paras) throws Exception {
		ArrayList<DicPojo> list = new ArrayList<DicPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getDicList(sqlHelper, paras);
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
	 * 方法名称: getDicList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典
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
	public ArrayList<DicPojo> getDicList(SqlExecute sqlHelper, JSONObject paras)
			throws Exception {
		// 参数设定
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String where = "";
		String key = "";
		String orderBy = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("key")) {
					// 关键字
					where += " and t.DIC_ID=? and (t.DIC_NAME like ? or t.DIC_DESC like ?)";
					vList.add(v);
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
			}
		}
		// 设置数据字典列表
		StaticDBMang dicDB = new StaticDBMang(sqlHelper);
		ArrayList<DicPojo> list = dicDB.getDicList(where, orderBy, vList);
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getDicList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典
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
	public DicPojo getOneDicById(String id) throws Exception {
		DicPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneDicById(sqlHelper, id);
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
	 * 方法名称: getOneDicById
	 * </p>
	 * <p>
	 * 方法功能描述:得到单个数据字典
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
	public DicPojo getOneDicById(SqlExecute sqlHelper, String id)
			throws Exception {
		// 设置数据字典列表
		StaticDBMang dicDB = new StaticDBMang(sqlHelper);
		return dicDB.getOneDicById(id);
	}

	/**
	 * <p>
	 * 方法名称: insertDic
	 * </p>
	 * <p>
	 * 方法功能描述: 插入数据字典数据。
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
	public int insertDic(DicPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertDic(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setDicItemListToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertDic
	 * </p>
	 * <p>
	 * 方法功能描述: 插入数据字典数据。
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
	public int insertDic(SqlExecute sqlHelper, DicPojo onePojo)
			throws Exception {
		StaticDBMang dicDB = new StaticDBMang(sqlHelper);
		return dicDB.insertDic(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: updateDic
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
	public int updateDic(DicPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateDic(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setDicItemListToRedis(onePojo.getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateDic
	 * </p>
	 * <p>
	 * 方法功能描述: 更新数据字典数据。
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
	public int updateDic(SqlExecute sqlHelper, DicPojo onePojo)
			throws Exception {
		StaticDBMang dicDB = new StaticDBMang(sqlHelper);
		return dicDB.updateDic(onePojo);
	}

	/**
	 * <p>
	 * 方法名称: getDicItemList
	 * </p>
	 * <p>
	 * 方法功能描述:得到数据字典项目列表，带条件
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
	public ArrayList<DicItemPojo> getDicItemList(JSONObject paras)
			throws Exception {
		ArrayList<DicItemPojo> list = new ArrayList<DicItemPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getDicItemList(sqlHelper, paras);
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
	 * 方法名称: getDicItemList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典项目
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
	public ArrayList<DicItemPojo> getDicItemList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		// 参数设定
		Iterator<String> keys = paras.keys();
		List<Object> vList = new ArrayList<Object>();
		String where = "";
		String key = "";
		String orderBy = "";
		while (keys.hasNext()) {
			key = keys.next();
			String v = paras.getString(key);
			if (!v.equals("")) {
				if (key.equals("dic")) {
					// 关键字
					where += " and t.DIC_ID=?";
					vList.add(v);
				}
				if (key.equals("pdic")) {
					// 关键字
					where += " and t.DIC_ID in (select v.DIC_PID from TB_DIC v where v.DIC_ID=?)";
					vList.add(v);
				}
				if (key.equals("pitemid")) {
					// 关键字
					where += " and t1.DIC_PITEMID=?";
					vList.add(v);
				}
				if (key.equals("pitemids")) {
					where += " and t1.DIC_PITEMID in (";
					String[] vs = v.split(",");
					String whereEx = "";
					for (String oneV : vs) {
						if (!oneV.equals("")) {
							whereEx += ",?";
							vList.add(oneV);
						}
					}
					where += ((whereEx.equals("") ? "" : whereEx.substring(1)) + ")");
				}
				if (key.equals("key")) {
					// 关键字
					where += " and t.ITEM_ID=? and (t.ITEM_NAME like ? or t.ITEM_VALUE like ?)";
					vList.add(v);
					vList.add("%" + v + "%");
					vList.add("%" + v + "%");
				}
			}
		}
		// 设置数据字典列表
		StaticDBMang dicDB = new StaticDBMang(sqlHelper);
		ArrayList<DicItemPojo> list = dicDB.getDicItemList(where, orderBy,
				vList);
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getOneDicItemById
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典项目
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
	public DicItemPojo getOneDicItemById(String id) throws Exception {
		DicItemPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneDicItemById(sqlHelper, id);
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
	 * 方法名称: getOneDicItemById
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典项目
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
	public DicItemPojo getOneDicItemById(SqlExecute sqlHelper, String id)
			throws Exception {
		// 设置数据字典列表
		StaticDBMang dicDB = new StaticDBMang(sqlHelper);
		return dicDB.getOneDicItemById(id);
	}

	/**
	 * <p>
	 * 方法名称: getOneDicItemByName
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典项目
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
	public DicItemPojo getOneDicItemByName(String dic, String name)
			throws Exception {
		DicItemPojo onePojo = null;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			onePojo = this.getOneDicItemByName(sqlHelper, dic, name);
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
	 * 方法名称: getOneDicItemByName
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量数据字典项目
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
	public DicItemPojo getOneDicItemByName(SqlExecute sqlHelper, String dic,
			String name) throws Exception {
		// 设置数据字典列表
		StaticDBMang dicDB = new StaticDBMang(sqlHelper);
		return dicDB.getOneDicItemByName(dic, name);
	}

	/**
	 * <p>
	 * 方法名称: insertDicItem
	 * </p>
	 * <p>
	 * 方法功能描述: 插入数据字典数据。
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
	public int insertDicItem(DicItemPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.insertDicItem(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setDicItemToRedis(onePojo.getId());
		this.setDicItemListToRedis(onePojo.getDic().getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: insertDicItem
	 * </p>
	 * <p>
	 * 方法功能描述: 插入数据字典数据。
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
	public int insertDicItem(SqlExecute sqlHelper, DicItemPojo onePojo)
			throws Exception {
		StaticDBMang dicDB = new StaticDBMang(sqlHelper);
		int count = dicDB.insertDicItem(onePojo);

		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateDicItem
	 * </p>
	 * <p>
	 * 方法功能描述: 更新用户数据字典项目。
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
	public int updateDicItem(DicItemPojo onePojo) throws Exception {
		int count = 0;
		SqlExecute sqlHelper = new SqlExecute();
		try {
			sqlHelper.setAutoCommit(false);
			count = this.updateDicItem(sqlHelper, onePojo);
			sqlHelper.commit();
		} catch (Exception ep) {
			sqlHelper.rollback();
			ep.printStackTrace();
			throw ep;
		}
		this.setDicItemToRedis(onePojo.getId());
		this.setDicItemListToRedis(onePojo.getDic().getId());
		return count;
	}

	/**
	 * <p>
	 * 方法名称: updateDicItem
	 * </p>
	 * <p>
	 * 方法功能描述: 更新数据字典数据项目。
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
	public int updateDicItem(SqlExecute sqlHelper, DicItemPojo onePojo)
			throws Exception {
		StaticDBMang dicDB = new StaticDBMang(sqlHelper);
		int count = dicDB.updateDicItem(onePojo);

		return count;
	}

	/**
	 * <p>
	 * 方法名称: getStaticLookUpList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量员工
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
	public JSONArray getStaticLookUpList(JSONObject paras) throws Exception {
		JSONArray list = new JSONArray();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getStaticLookUpList(sqlHelper, paras);
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
	 * 方法名称: getStaticLookUpList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量员工
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
	public JSONArray getStaticLookUpList(SqlExecute sqlHelper, JSONObject paras)
			throws Exception {
		String where = "";
		String key = "";
		String orderBy = " t.COL_VALUE,t.COL_NAME";
		String id = "";
		String table = "";
		String col = "";
		String value = "";

		if (paras != null) {
			Iterator<String> keys = paras.keys();
			while (keys.hasNext()) {
				key = keys.next();
				String v = paras.getString(key);
				if (!v.equals("")) {
					if (key.equals("id")) {
						id = v;
					}
					if (key.equals("table")) {
						table = v;
					}
					if (key.equals("col")) {
						col = v;
					}
				}
			}
		}
		// 设置用户列表
		StaticDBMang dicDB = new StaticDBMang(sqlHelper);
		JSONArray staticList = new JSONArray();
		ArrayList<StaticPojo> list = dicDB.getStaticList(id, table, col, value,
				"", "", "", where, orderBy);
		for (StaticPojo onePojo : list) {
			JSONObject obj = new JSONObject();
			obj.put("name", onePojo.getValueName());
			obj.put("value", onePojo.getStaticId());
			staticList.add(obj);
		}
		return staticList;
	}

	/**
	 * <p>
	 * 方法名称: getStaticLookUpList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量员工
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
	public ArrayList<StaticPojo> getStaticList(JSONObject paras)
			throws Exception {
		ArrayList<StaticPojo> list = new ArrayList<StaticPojo>();
		SqlExecute sqlHelper = new SqlExecute();
		try {
			list = this.getStaticList(sqlHelper, paras);
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
	 * 方法名称: getStaticLookUpList
	 * </p>
	 * <p>
	 * 方法功能描述:得到批量员工
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
	public ArrayList<StaticPojo> getStaticList(SqlExecute sqlHelper,
			JSONObject paras) throws Exception {
		String where = "";
		String key = "";
		String orderBy = " t.COL_VALUE,t.COL_NAME";
		String id = "";
		String table = "";
		String col = "";
		String value = "";

		if (paras != null) {
			Iterator<String> keys = paras.keys();
			while (keys.hasNext()) {
				key = keys.next();
				String v = paras.getString(key);
				if (!v.equals("")) {
					if (key.equals("id")) {
						id = v;
					}
					if (key.equals("table")) {
						table = v;
					}
					if (key.equals("col")) {
						col = v;
					}
				}
			}
		}
		// 设置用户列表
		StaticDBMang dicDB = new StaticDBMang(sqlHelper);
		return dicDB.getStaticList(id, table, col, value, "", "", "", where,
				orderBy);
	}
}
