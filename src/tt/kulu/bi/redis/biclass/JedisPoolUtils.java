package tt.kulu.bi.redis.biclass;

import com.tt4j2ee.BSCommon;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtils {
	private static JedisPool jedisPool = null;
	/**
	 * 初始化Redis连接池
	 */
	static {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			// 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
			config.setBlockWhenExhausted(true);
			// 设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
			config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
			// 是否启用pool的jmx管理功能, 默认true
			config.setJmxEnabled(true);
			// 最大空闲连接数, 默认8个 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(50);
			// 最大连接数, 默认8个
			config.setMaxTotal(500);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(1000 * 100);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);

			jedisPool = new JedisPool(config,
					BSCommon.getConfigValue("redis_path"),
					Integer.parseInt(BSCommon
							.getConfigValue("redis_port")), 1000,
					BSCommon.getConfigValue("redis_password"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				// resource.auth(BSCommon
				// .getConfigValue("redis_password"));
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public synchronized static Jedis getJedis(int dbIndex) {
		try {
			if (jedisPool != null) {
				if (dbIndex <= 0) {
					dbIndex = 0;
				}
				Jedis resource = jedisPool.getResource();
				// resource.auth(BSCommon
				// .getConfigValue("redis_password"));
				resource.select(dbIndex);
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void close(final Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

}
