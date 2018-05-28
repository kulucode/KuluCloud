package tt.kulu.out.call;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import tt.kulu.bi.redis.biclass.JedisPoolUtils;

import com.tt4j2ee.BSCommon;

/**
 * <p>
 * 标题: BIRedis
 * </p>
 * <p>
 * 功能描述:数据库缓存接口类；0：基础信息；1：session;2：其他
 * </p>
 * <p>
 * 作者: 马维
 * </p>
 * <p>
 * 版本: 0.1
 * </p>
 * <p>
 * 创建日期: 2014-10-23
 * </p>
 */
public class BIRedis {
	private String pre = BSCommon.getConfigValue("redis_pre");

	public long clearAll(int index) {
		int count = 0;
		Jedis jedis = JedisPoolUtils.getJedis(index);
		Set s = jedis.keys("*");
		Iterator<String> it = s.iterator();
		while (it.hasNext()) {
			String keyStr = it.next();
			jedis.del(keyStr);
		}
		JedisPoolUtils.close(jedis);
		return count;
	}

	public long getKeyCount(int index) {
		int count = 0;
		Jedis jedis = JedisPoolUtils.getJedis(index);
		Set s = jedis.keys("*");
		count = s.size();
		JedisPoolUtils.close(jedis);
		return count;
	}

	public boolean exists(String key, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		boolean ret = jedis.exists(pre + key);
		JedisPoolUtils.close(jedis);
		return ret;
	}

	public void setStringData(String key, String value, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		jedis.set(pre + key, value);
		JedisPoolUtils.close(jedis);
	}

	public void setStringData(String key, String value, int seconds, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		jedis.set(pre + key, value);
		jedis.expire(pre + key, seconds);
		JedisPoolUtils.close(jedis);
	}

	public String getStringData(String key, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		String retStr = jedis.get(pre + key);
		if (retStr == null) {
			retStr = "";
		}
		JedisPoolUtils.close(jedis);
		return retStr;
	}

	public long setListData(String key, String value, int index) {
		long count = 0;
		Jedis jedis = JedisPoolUtils.getJedis(index);
		count = jedis.lpush(pre + key, value);
		JedisPoolUtils.close(jedis);
		return count;
	}

	public List<String> getListData(String key, long f, long t, int index) {
		List<String> list = null;
		Jedis jedis = JedisPoolUtils.getJedis(index);
		list = jedis.lrange(pre + key, f, t);
		JedisPoolUtils.close(jedis);
		return list;
	}

	public void setMapData(String key, Map<String, String> map, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		jedis.hmset(pre + key, map);
		JedisPoolUtils.close(jedis);
	}

	public void setMapData(String key, String fields, String value, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		Map<String, String> hgetAll = jedis.hgetAll(pre + key);
		hgetAll.put(fields, value);
		jedis.hmset(pre + key, hgetAll);
		JedisPoolUtils.close(jedis);
	}

	public Map<String, String> getMapData(String key, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		Map<String, String> hgetAll = jedis.hgetAll(pre + key);
		JedisPoolUtils.close(jedis);
		return hgetAll;
	}

	public String getMapData(String key, String fields, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		List<String> rsmap = jedis.hmget(pre + key, fields);
		String retStr = "";
		if (rsmap != null && rsmap.get(0) != null) {
			retStr = rsmap.get(0);
		}
		JedisPoolUtils.close(jedis);
		return retStr;
	}

	public void delData(String key, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		jedis.del(pre + key);
		JedisPoolUtils.close(jedis);
	}

	public void delMapData(String key, String fields, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		jedis.hdel(pre + key, fields);
		JedisPoolUtils.close(jedis);
	}

	public void setSortedSet(String key, double number, String value, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		jedis.zadd(pre + key, number, value);
		JedisPoolUtils.close(jedis);
	}

	public long getSortedSetCount(String key, int index) {
		long count = 0;
		Jedis jedis = JedisPoolUtils.getJedis(index);
		count = jedis.zcard(pre + key);
		JedisPoolUtils.close(jedis);
		return count;
	}

	public double getSortedSetScore(String key, String value, int index) {
		double count = 0;
		Jedis jedis = JedisPoolUtils.getJedis(index);
		count = jedis.zscore(pre + key, value);
		JedisPoolUtils.close(jedis);
		return count;
	}

	public long getSortedSetCount(String key, double min, double max, int index) {
		long count = 0;
		Jedis jedis = JedisPoolUtils.getJedis(index);
		count = jedis.zcount(pre + key, min, max);
		JedisPoolUtils.close(jedis);
		return count;
	}

	public long deleteOneSortedSet(String key, double min, double max, int index) {
		long count = 0;
		Jedis jedis = JedisPoolUtils.getJedis(index);
		count = jedis.zcount(pre + key, min, max);
		JedisPoolUtils.close(jedis);
		return count;
	}

	public ArrayList<String> getSortedSetValue(String key, int desc, long mix,
			long max, int index) {
		ArrayList<String> list = new ArrayList<String>();
		Jedis jedis = JedisPoolUtils.getJedis(index);
		if (desc == 0) {
			// 从小到大
			list = new ArrayList<String>(jedis.zrange(pre + key, mix, max));
		} else {
			// 从大到小
			list = new ArrayList<String>(jedis.zrevrange(pre + key, mix, max));
		}
		JedisPoolUtils.close(jedis);
		return list;
	}

	public void removeSortedSet(String key, long min, long max, int index) {
		Jedis jedis = JedisPoolUtils.getJedis(index);
		jedis.zremrangeByRank(pre + key, min, max);
		JedisPoolUtils.close(jedis);
	}
}
