package com.phn.base.component;

import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;

import org.jupport.manager.AbstractJedisCacheContainer;
import org.jupport.manager.CacheContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PhnCacheContainer extends AbstractJedisCacheContainer implements CacheContainer {

	@Value("${phn.redis.host:127.0.0.1}")
	protected String redisHost;

	@Override
	public String getRedisHost() {
		return redisHost;
	}
	
	@Override
	@PostConstruct
	public void postConstruct() throws UnsupportedEncodingException {
		super.postConstruct();
		log.info("PhnCacheStore redisHost {}", redisHost);
	}

}
