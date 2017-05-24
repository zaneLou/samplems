package org.jupport.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.jedis.lock.JedisLock;

import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;

public interface CacheManagerV1 {

	public void subscribeJedis(JedisPubSub anJedisPubSub, String prefix);
	
	public byte[] serialize(Object object);
	public Object deserialize(byte[] bytes);
	
	public void expire(String prefix, String key, boolean shadow, int ttlSeconds);
	public long getTtl(String key);
	public Pipeline getPipeline();
	public List getObjectValues(List<String> keys);
	public void setObjectValue(String key, Object value, int ttl, boolean shadow, int shadowTtl, boolean lock);
	public void setObjectValues(List<String> keys, List values, String mainKey, int ttl, boolean lock);
	
	public JedisLock getCacheLock(String key, int times, int millis);
	public void releaseCacheLock(JedisLock jedisLock);
	
	public Map getCacheMap(String key);
	public boolean setCacheMapIfNotExist(String key, Map value, boolean shadow, boolean lock, int ttlSeconds);
	public long setCacheMapFeild(String key, String field, String value, boolean lock, Pipeline pipeline);
	public boolean isMapFieldTimeout(Map map, String field, Date date, long timeout);
	public long incrCacheMapBy(String key, String field, int integer, boolean lock, Pipeline pipeline);
}
