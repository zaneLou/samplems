package org.jupport.manager;

import java.util.List;
import java.util.Map;


public interface CacheContainer {
	public void expire(String key, int ttlSeconds);
	public long getTtl(String key);
	public void remove(String key);
	public byte[] getObject(String key);
	public List<byte[]> getObjectValues(List<String> keys);
	public void setObjectValue(String key, byte[] value, int ttl);
	public boolean setObjectValueNx(String key, byte[] value, int ttl);
	public void setObjectValues(List<String> keys, List<byte[]> values, int ttl);
	public long incrBy(final String key, final long integer, int ttl) ;
	public Object getLock(String key, int times, int millis);
	public void releaseLock(Object lock);
	public Map<String, String> getMap(String key);
	public boolean setMapIfNotExist(String key, Map<String, String> value, boolean shadow, int ttlSeconds) ;
	public long setMapFeild(String key, String field, String value, boolean lock);
	public String getMapFeild(String key, String field);
	public long incrMapBy(String key, String field, int integer, boolean lock);
	public Long setAdd(final String key, final String... members);
}
