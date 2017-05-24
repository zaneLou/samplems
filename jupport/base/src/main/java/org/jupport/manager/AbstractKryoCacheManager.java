package org.jupport.manager;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.UnsafeMemoryInput;
import com.esotericsoftware.kryo.io.UnsafeMemoryOutput;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.github.jedis.lock.JedisLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import utils.CollectionUtil;

public abstract class AbstractKryoCacheManager implements CacheManagerV1{

	protected final Log logger = LogFactory.getLog(getClass());
	
	protected String redisHost;
	protected boolean flushRedis;
	protected JedisPool jedisPool;
	protected Jedis subscribeJedis;
	protected JedisPubSub jedisPubSub;
	protected KryoFactory kryoFactory;
	protected KryoPool kryoPool;
	protected BeanUtilsBean beanUtilsBean;
	
	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}
	public void setFlushRedis(boolean flushRedis) {
		this.flushRedis = flushRedis;
	}
	
	abstract void registerClass(Kryo kryo);
	
	@PostConstruct
	public void postConstruct() throws UnsupportedEncodingException 
	{
		kryoFactory = new KryoFactory() {
			@Override
			public Kryo create () {
				
				Kryo kryo = new Kryo();
				
				// Register types we know about and do not require external configuration
		        kryo.register(ArrayList.class);
		        kryo.register(LinkedList.class);
		        kryo.register(String.class, new DefaultSerializers.StringSerializer());//
		        kryo.register(HashMap.class);
		        kryo.register(TreeSet.class);
		        
		        kryo.register(Timestamp.class);
		        kryo.register(JSONArray.class);
		        kryo.register(JSONObject.class);
		        kryo.register(Date.class);
		        kryo.register(Integer.class);
		        kryo.register(Double.class);
		        kryo.register(Float.class);
		        kryo.register(Boolean.class);
		        
		        registerClass(kryo);
		        
				kryo.setRegistrationRequired(false);
				//kryo.setRegistrationRequired(true);
				kryo.setMaxDepth(20);
				
				return kryo;
			}
		};
		kryoPool = new KryoPool.Builder(kryoFactory).softReferences().build();
		
		//redisson
		JedisPoolConfig config = new JedisPoolConfig(); 
		config.setTestOnBorrow(true); 
		config.setTestOnReturn(true); 
		config.setTestWhileIdle(true); 
		config.setMaxTotal(1000);
		jedisPool = new JedisPool(config, redisHost);
		if(flushRedis)
		{
			try (Jedis jedis = jedisPool.getResource()) 
			{
				logger.error("++++++++++CacheManagerImpl++++++++++" + "[flush]");
				jedis.flushAll();
				jedis.flushDB();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//others
		DateConverter converter = new DateConverter();
		converter.setPattern("yyyy/MM/dd HH:mm:ss");
		//DateLocaleConverter converter = new DateLocaleConverter(Locale.getDefault(), "yyyy/mm/dd HH:mm:ss");
		//converter.setLenient(true);
		//ConvertUtils.register(converter, java.util.Date.class); 
		beanUtilsBean = BeanUtilsBean.getInstance();
		beanUtilsBean.getConvertUtils().register(converter, java.util.Date.class);
		beanUtilsBean.getConvertUtils().register(converter, java.sql.Timestamp.class);
		
	}
	
	@PreDestroy
	public void preDestroy() throws UnsupportedEncodingException 
	{
		try {
			if(subscribeJedis!=null)
			{
				jedisPubSub.punsubscribe();
				subscribeJedis.close();
			}
			jedisPool.close();
			jedisPool.destroy();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void subscribeJedis(JedisPubSub anJedisPubSub, String prefix)
	{
		if(!flushRedis)
			return;
		
		logger.info("++++do subscribeJedis++++");
		jedisPubSub = anJedisPubSub;
    	Runnable run = new Runnable() {
			@Override
			public void run() {
				try {
					
					subscribeJedis = new Jedis(redisHost);
					//subscribeJedis.psubscribe(new KeySpaceListener(), "__key*__:*"+ConstantsManager.RedisCachePrefix+"UsersActivities:*");
					subscribeJedis.psubscribe(jedisPubSub, "__key*__:*"+prefix+"*");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		Thread thread = new Thread(run);
		thread.start();
	}
	
	// Serialize
	@Override
	public byte[] serialize(Object object) 
	{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		Kryo kryo = kryoPool.borrow();
		Output output = new UnsafeMemoryOutput(outStream);
		kryo.writeClassAndObject(output, object);
		output.flush();
		byte[] bytes = outStream.toByteArray();
		//logger.info("[serialize]" +"[object]" + object + "[length]"+bytes.length+"[output]"+output.total());
		kryoPool.release(kryo);
		output.close();
		return bytes;
	}
	 
	@Override
	public Object deserialize(byte[] bytes) 
	{
		//logger.info("[deserialize length]"+bytes.length);
		Input input   = new UnsafeMemoryInput(bytes);
		Kryo kryo     = kryoPool.borrow();
		Object object = kryo.readClassAndObject(input);  
		kryoPool.release(kryo);
		input.close();
		return object;	
	}
	
	//redis
	@Override
	public void expire(String prefix, String key, boolean shadow, int ttlSeconds)
	{
		try (Jedis jedis = jedisPool.getResource()) 
		{
			jedis.expire(key.getBytes(), ttlSeconds);
			if(shadow)
			{
				key = prefix + key;
				jedis.expire(key.getBytes(), ttlSeconds-30);
				
				//logger.error("+++++redis expire++++[key]"+key+"[ttlSeconds]"+ttlSeconds);
			}
		}
	}
	
	public void removeRedis(String key)
	{	
		try (Jedis jedis = jedisPool.getResource()) 
		{
			jedis.del(key.getBytes());
		}
		catch (Exception e) {
			logger.error("+++++removeRedis++++[ERROR]"+key);
			//e.printStackTrace();
		}
	}
	
	@Override
	public long getTtl(String key)
	{
		try (Jedis jedis = jedisPool.getResource()) 
		{
			return jedis.ttl(key);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("+++++getTtl++++[ERROR]"+key + "[Exception]"+e);
			removeRedis(key);
		}
		return 0;
	}
	
	public Object getRedisObject(String key)
	{
		try (Jedis jedis = jedisPool.getResource()) 
		{
			if(jedis.exists(key.getBytes()))
			{
				//float  ttl    = jedis.ttl(key.getBytes());
				//jedis.expire(key.getBytes(), (int)ttl);
				Object object = deserialize(jedis.get(key.getBytes()));
				//logger.info("get object from Redis:" + key);
				return object;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("+++++getRedisObject++++[ERROR]"+key + "[Exception]"+e);
			removeRedis(key);
		}
		return null;
	}
	
	@Override
	public Pipeline getPipeline()
	{
		try (Jedis jedis = jedisPool.getResource()) 
		{
			return jedis.pipelined();
		}
	}
	
	@Override
	public List getObjectValues(List<String> keys)
	{
		List list = new ArrayList();
		try (Jedis jedis = jedisPool.getResource()) 
		{
			Pipeline p = jedis.pipelined();
			List<Response<byte[]>> result = new ArrayList();
			for (String key : keys) {
				Response<byte[]> bytev = p.get(key.getBytes());
				result.add(bytev);
			}
			p.sync();
			for (Response<byte[]> object : result)
			{
				list.add(deserialize(object.get()));
			}
			return list; 
		}
		catch (Exception e) {
			logger.error("+++++getObjectValues++++[ERROR]"+keys+"[Exception]"+e);
			logger.info("+++++getObjectValues++++[Exception]"+e);
			//e.printStackTrace();
		}
		return list;
	}
	
	public void setObjectValue(Jedis jedis, String prefix, String key, Object value, int ttl, int shadowTtl, boolean shadow)
	{
		//logger.info("set object to Redis:" + key);
		jedis.set(key.getBytes(), serialize(value));
		jedis.expire(key.getBytes(), ttl);
		if(shadow)
		{
			key = prefix + key;
			//logger.info("++shadow+++" + key);
			jedis.set(key.getBytes(), serialize(""));
			jedis.expire(key.getBytes(), shadowTtl);
		}
	}
	
	public void setObjectValues(Jedis jedis, List<String> keys, List values, int ttl)
	{
		Pipeline p = jedis.pipelined();
		for (int i = 0; i < keys.size(); i++) 
		{
			 p.set(keys.get(i).getBytes(), serialize(values.get(i)));
			 p.expire(keys.get(i).getBytes(), ttl);
		}
		p.sync(); 
	}
	
	@Override
	public void setObjectValue(String key, Object value, int ttl, boolean shadow, int shadowTtl, boolean lock)
	{
		try (Jedis jedis = jedisPool.getResource()) 
		{
			if(lock)
			{
				JedisLock jedisLock = new JedisLock(jedis, key+"Lock");
				if (jedisLock.acquire()) 
				{
					//logger.info("++setObjectValue+++" + key+"Lock");
					setObjectValue(jedis, null, key, value, ttl, shadowTtl, shadow);
					jedisLock.release();
                }
			}
			else
			{
				setObjectValue(jedis, null, key, value, ttl, shadowTtl, shadow);
			}	
		}
		catch (Exception e) {
			logger.error("+++++setObjectValue++++[ERROR]"+key+"[Exception]"+e);
			//e.printStackTrace();
		}
	}
	
	@Override
	public void setObjectValues(List<String> keys, List values, String mainKey, int ttl, boolean lock)
	{
		try (Jedis jedis = jedisPool.getResource()) 
		{
			if(lock)
			{
				JedisLock jedisLock = new JedisLock(jedis, mainKey+"Lock");
				if (jedisLock.acquire()) 
				{
					setObjectValues(jedis, keys, values, ttl);
					jedisLock.release();
                }
			}
			else
			{
				setObjectValues(jedis, keys, values, ttl);
			}	
		}
		catch (Exception e) {
			logger.error("+++++setObjectValues++++[ERROR]"+mainKey+"[Exception]"+e);
			logger.info("+++++setObjectValues++++[Exception]"+e);
			//e.printStackTrace();
		}
		//cache.put(new Element(key,value));
	}
	
	//Lock
	private JedisLock getCacheLock(Jedis jedis, String key, int acquireTimeoutMillis)
	{
		JedisLock jedisLock = new JedisLock(jedis, key);
		try {
			if (jedisLock.acquire()) 
			{
				return jedisLock;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public JedisLock getCacheLock(String key, int times, int millis)
	{
		try (Jedis jedis = jedisPool.getResource()) 
		{
			while (times>0 )
			{
				JedisLock jedisLock = getCacheLock(jedis, key, millis);
				if(jedisLock!=null)
				{
					return jedisLock;
				}
				times --;
			}
		}
		catch (Exception e) {
			logger.error("+++++releaseCacheLock++++[KEY]"+key);
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void releaseCacheLock(JedisLock jedisLock)
	{
		if(jedisLock==null)
			return;
		
		try (Jedis jedis = jedisPool.getResource()) 
		{
			jedisLock.release(jedis);
		}
		catch (Exception e) {
			logger.error("+++++releaseCacheLock++++[KEY]"+jedisLock.getLockKeyPath());
			e.printStackTrace();
		}
	}
	
	//Map
	@Override
	public Map getCacheMap(String key)
	{
		//logger.info("+++++getCacheMap++++"  + "[key]"+ key);
		try (Jedis jedis = jedisPool.getResource()) 
		{
			return jedis.hgetAll(key);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("+++++getCacheMap++++[ERROR]"+key + "[Exception]"+e);
			removeRedis(key);
		}
		return new HashMap();
	}

	public boolean setCacheMapIfNotExist(Jedis jedis,String prefix, String key, Map value, boolean shadow, int ttlSeconds)
	{
		if(value == null)
			return false;
		
		if(jedis.exists(key.getBytes()))
			return false;
		
		CollectionUtil.removeNullValue(value);
		
		jedis.hmset(key, value);
		jedis.expire(key.getBytes(), ttlSeconds);
		
		if(shadow)
		{
			//logger.info("+++++setCacheMapIfNotExist++++" + "[key]"+ key + "[ttlSeconds]" + ttlSeconds);
			key = prefix + key;
			jedis.set(key.getBytes(), serialize(""));
			jedis.expire(key.getBytes(), ttlSeconds-30);
			//logger.info("+++++setCacheMapIfNotExist++++ shadow" + "[key]"+ key + "[value]" + value.size());
		}
		return true;
	}
	
	
	@Override
	public boolean setCacheMapIfNotExist(String key, Map value, boolean shadow, boolean lock, int ttlSeconds)
	{
		boolean result = false;
		try (Jedis jedis = jedisPool.getResource()) 
		{
			if(lock)
			{
				JedisLock jedisLock = new JedisLock(jedis, key+"Lock");
				if (jedisLock.acquire()) 
				{
					result = setCacheMapIfNotExist(jedis, null, key, value, shadow, ttlSeconds);
					jedisLock.release();
                }
			}
			else
			{
				result = setCacheMapIfNotExist(jedis, null, key, value, shadow, ttlSeconds);
			}	
		}
		catch (Exception e) {
			logger.error("+++++setCacheMap++++[ERROR]"+key+"[Exception]"+e);
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public long setCacheMapFeild(String key, String field, String value, boolean lock, Pipeline pipeline)
	{
		long result = 0;
		if(pipeline==null)
		{
			try (Jedis jedis = jedisPool.getResource()) 
			{
				if(lock)
				{
					JedisLock jedisLock = new JedisLock(jedis, key+"Lock");
					if (jedisLock.acquire()) 
					{
						result = jedis.hset(key, field, value);
						jedisLock.release();
	                }
				}
				else
				{
					result = jedis.hset(key, field, value);
				}	
			}
			catch (Exception e) {
				logger.error("+++++setCacheMap++++[ERROR]"+key+"[Exception]"+e);
			}
		}
		else
		{
			pipeline.hset(key, field, value);
		}
		return result;
	}
	
	@Override
	public boolean isMapFieldTimeout(Map map, String field, Date date, long timeout)
	{
		if(map==null)
			return true;
		
		String currentString = (String)map.get(field);
		Date currentDate = currentString!=null?(Date)ConvertUtils.convert(currentString, Date.class):null;
		if( currentDate ==null || date.getTime() - currentDate.getTime() > timeout)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public long incrCacheMapBy(String key, String field, int integer, boolean lock, Pipeline pipeline)
	{
		long result = 0;
		if(pipeline==null)
		{
			try (Jedis jedis = jedisPool.getResource()) 
			{
				if(lock)
				{
					JedisLock jedisLock = new JedisLock(jedis, key+"Lock");
					if (jedisLock.acquire()) 
					{
						result = jedis.hincrBy(key, field, integer);
						jedisLock.release();
	                }
				}
				else
				{
					result = jedis.hincrBy(key, field, integer);
				}	
			}
			catch (Exception e) {
				logger.error("+++++setCacheMap++++[ERROR]"+key+"[Exception]"+e);
			}
		}
		else
		{
			pipeline.hincrBy(key, field, integer);
		}
		return result;
	}
}
