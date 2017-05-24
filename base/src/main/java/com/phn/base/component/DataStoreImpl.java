package com.phn.base.component;

import org.jupport.manager.CacheContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


/**
 * Copyright 2017年4月27日, Easemob.inc
 * All rights reserved.
 *
 * @author liuzheng
 */
@Slf4j
@Component
public class DataStoreImpl implements DataStore{

    public static int middleTimeout = 60*60*6;
    
    public static String userDataIdsPrefix = "phn:user:dataIds:";
    
    @Autowired
    protected CacheContainer cacheContainer;

    @Override
    public boolean hadCacheSessionData(String sessionId, String dataId) {
        return !cacheContainer.setObjectValueNx(userDataIdsPrefix + sessionId + ":" + dataId, "".getBytes(), middleTimeout);
    }
}


