package org.jupport.manager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.github.jedis.lock.JedisLock;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import utils.CollectionUtil;

@Slf4j
public abstract class AbstractJedisCacheContainer implements CacheContainer {

    protected boolean flushRedis;
    protected JedisPool jedisPool;
    protected Jedis subscribeJedis;
    protected JedisPubSub jedisPubSub;

    public abstract String getRedisHost();

    public void setFlushRedis(boolean flushRedis) {
        this.flushRedis = flushRedis;
    }

    @PostConstruct
    public void postConstruct() throws UnsupportedEncodingException {
        // redisson
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        config.setMaxTotal(1000);
        jedisPool = new JedisPool(config, getRedisHost());
        if (flushRedis) {
            try (Jedis jedis = jedisPool.getResource()) {
                log.warn("++++++++++CacheManagerImpl++++++++++" + "[flush]");
                jedis.flushAll();
                jedis.flushDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void preDestroy() throws UnsupportedEncodingException {
        try {
            if (subscribeJedis != null) {
                jedisPubSub.punsubscribe();
                subscribeJedis.close();
            }
            jedisPool.close();
            jedisPool.destroy();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // subscribe
    public void subscribeJedis(JedisPubSub anJedisPubSub, String prefix) {
        if (!flushRedis)
            return;

        log.info("++++do subscribeJedis++++");
        jedisPubSub = anJedisPubSub;
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {

                    subscribeJedis = new Jedis(getRedisHost());
                    // subscribeJedis.psubscribe(new KeySpaceListener(),
                    // "__key*__:*"+ConstantsManager.RedisCachePrefix+"UsersActivities:*");
                    subscribeJedis.psubscribe(jedisPubSub, "__key*__:*" + prefix + "*");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(run);
        thread.start();
    }

    // expire
    @Override
    public void expire(String key, int ttlSeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.expire(key.getBytes(), ttlSeconds);
        }
    }

    @Override
    public long getTtl(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.ttl(key);
        } catch (Exception e) {
            log.error("+++++getTtl++++[ERROR]" + key + "[Exception]" + e);
            // e.printStackTrace();
        }
        return 0;
    }

    // remove
    @Override
    public void remove(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key.getBytes());
        } catch (Exception e) {
            log.error("+++++removeRedis++++[ERROR]" + key);
            // e.printStackTrace();
        }
    }

    // piple
    public Pipeline getPipeline() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.pipelined();
        }
    }

    // get
    @Override
    public byte[] getObject(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists(key.getBytes())) {
                return jedis.get(key.getBytes());
            }
        } catch (Exception e) {
            log.error("+++++getRedisObject++++[ERROR]" + key + "[Exception]" + e);
            // e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<byte[]> getObjectValues(List<String> keys) {
        List<byte[]> list = new ArrayList<byte[]>();
        try (Jedis jedis = jedisPool.getResource()) {
            Pipeline p = jedis.pipelined();
            List<Response<byte[]>> result = new ArrayList();
            for (String key : keys) {
                Response<byte[]> bytev = p.get(key.getBytes());
                result.add(bytev);
            }
            p.sync();
            for (Response<byte[]> object : result) {
                list.add(object.get());
            }
            return list;
        } catch (Exception e) {
            log.error("+++++getObjectValues++++[ERROR]" + keys + "[Exception]" + e);
            // e.printStackTrace();
        }
        return list;
    }

    // set
    @Override
    public void setObjectValue(String key, byte[] value, int ttl) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key.getBytes(), value);
            if (ttl > 0) {
                jedis.expire(key.getBytes(), ttl);
            }
        }
    }

    @Override
    public boolean setObjectValueNx(String key, byte[] value, int ttl) {
        boolean result = false;
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.setnx(key.getBytes(), value) > 0;
            if (ttl > 0) {
                jedis.expire(key.getBytes(), ttl);
            }
        }
        return result;
    }

    @Override
    public void setObjectValues(List<String> keys, List<byte[]> values, int ttl) {
        Pipeline p = getPipeline();
        for (int i = 0; i < keys.size(); i++) {
            p.set(keys.get(i).getBytes(), values.get(i));
            if (ttl > 0) {
                p.expire(keys.get(i).getBytes(), ttl);
            }
        }
        p.sync();
    }
    
    @Override
    public long incrBy(final String key, final long integer, int ttl) {
        Long value = 0l;
        try (Jedis jedis = jedisPool.getResource()) {
             value = jedis .incrBy( key, integer);
            if (ttl > 0) {
                jedis.expire(key.getBytes(), ttl);
            }
        }
        return value;
    }

    // Lock
    private JedisLock getCacheLock(Jedis jedis, String key, int acquireTimeoutMillis) {
        JedisLock jedisLock = new JedisLock(jedis, key);
        try {
            if (jedisLock.acquire()) {
                return jedisLock;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getLock(String key, int times, int millis) {
        try (Jedis jedis = jedisPool.getResource()) {
            while (times > 0) {
                JedisLock jedisLock = getCacheLock(jedis, key, millis);
                if (jedisLock != null) {
                    return jedisLock;
                }
                times--;
            }
        } catch (Exception e) {
            log.error("+++++releaseCacheLock++++[KEY]" + key);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void releaseLock(Object lock) {
        if (lock == null)
            return;
        JedisLock jedisLock = (JedisLock) lock;
        try (Jedis jedis = jedisPool.getResource()) {
            jedisLock.release(jedis);
        } catch (Exception e) {
            log.error("+++++releaseCacheLock++++[KEY]" + jedisLock.getLockKeyPath());
            e.printStackTrace();
        }
    }

    // Map
    @Override
    public Map<String, String> getMap(String key) {
        // log.info("+++++getCacheMap++++" + "[key]"+ key);
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("+++++getCacheMap++++[ERROR]" + key + "[Exception]" + e);
        }
        return new HashMap<String, String>();
    }

    @Override
    public boolean setMapIfNotExist(String key, Map<String, String> value, boolean shadow, int ttlSeconds) {
        if (value == null)
            return false;

        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists(key.getBytes()))
                return false;
            CollectionUtil.removeNullValue(value);
            jedis.hmset(key, value);
            jedis.expire(key.getBytes(), ttlSeconds);
        }
        return true;
    }

    @Override
    public long setMapFeild(String key, String field, String value, boolean lock) {
        long result = 0;
        Pipeline pipeline = getPipeline();
        if (pipeline == null) {
            try (Jedis jedis = jedisPool.getResource()) {
                if (lock) {
                    JedisLock jedisLock = new JedisLock(jedis, key + "Lock");
                    if (jedisLock.acquire()) {
                        result = jedis.hset(key, field, value);
                        jedisLock.release();
                    }
                } else {
                    result = jedis.hset(key, field, value);
                }
            } catch (Exception e) {
                log.error("+++++setCacheMap++++[ERROR]" + key + "[Exception]" + e);
            }
        } else {
            pipeline.hset(key, field, value);
        }
        return result;
    }

    @Override
    public String getMapFeild(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, field);
        }
    }

    @Override
    public long incrMapBy(String key, String field, int integer, boolean lock) {
        long result = 0;
        Pipeline pipeline = getPipeline();
        if (pipeline == null) {
            try (Jedis jedis = jedisPool.getResource()) {
                if (lock) {
                    JedisLock jedisLock = new JedisLock(jedis, key + "Lock");
                    if (jedisLock.acquire()) {
                        result = jedis.hincrBy(key, field, integer);
                        jedisLock.release();
                    }
                } else {
                    result = jedis.hincrBy(key, field, integer);
                }
            } catch (Exception e) {
                log.error("+++++setCacheMap++++[ERROR]" + key + "[Exception]" + e);
            }
        } else {
            pipeline.hincrBy(key, field, integer);
        }
        return result;
    }

    // set
    @Override
    public Long setAdd(final String key, final String... members) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sadd(key, members);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("+++++ERROR++++[ERROR]" + key + "[Exception]" + e);
        }
        return 0l;
    }

    // list

}
