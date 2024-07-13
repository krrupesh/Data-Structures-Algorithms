package com.onmobile.apps.ivm.daemons.missedcalls;

import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.common.debug.DebugManager;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisFactory {

	private static final String _class = "JedisFactory";
	public static final String _module = "MCA";
	private JedisPool pool;
	private static JedisFactory factory = null;
	private int maxActiveConnections = 50;

	private JedisFactory(String redisServer, int redisServerPort) {
		maxActiveConnections = ConfigDTO.getCommonDTO().getCommonJedisMaxActiveConnections();
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(maxActiveConnections);
		// poolConfig.setMaxIdle(60000); // maximum idle connections

		if (redisServer == null)
			redisServer = "localhost";

		pool = new JedisPool(poolConfig, redisServer, redisServerPort, 2000);
		DebugManager.status(_module, _class, "JedisFactory", "created Jedis factory object!",
				Thread.currentThread().getName(), null);
	}

	public static JedisFactory getFactory() {
		if (factory == null) {
			synchronized (JedisFactory.class) {
				if (factory == null) {
					factory = new JedisFactory("localhost", 6379);
				}
			}
		}
		return factory;
	}

	public Jedis getConnection() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			DebugManager.status(_module, _class, "getConnection", "getting connection! ",
					Thread.currentThread().getName(), null);
		} catch (Exception e) {
			DebugManager.exception(_class, _module, "getConnection", e, Thread.currentThread().getName(), null);
		}
		return jedis;
	}

	public void returnConnection(Jedis connection) {
		DebugManager.status(_module, _class, "returnConnection", "release connection!",
				Thread.currentThread().getName(), null);
		try {
			pool.returnResource(connection);
		} catch (Exception e) {
			DebugManager.exception(_class, _module, "returnConnection", e, Thread.currentThread().getName(), null);
		}
	}

}