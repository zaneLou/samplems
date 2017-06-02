package com.phn.embryo.test.server;

import java.io.InputStream;

import org.junit.After;
import org.junit.Before;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.phn.socketio.client.ImClientLauncher;
import com.phn.tojoy.channel.PacketBufServerChannelInitializer;
import com.phn.tojoy.embryo.TojoyNamespacesHub;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright 2017年5月25日, Easemob.inc
 * All rights reserved.
 *
 * @author liuzheng
 */
@Slf4j
public abstract class AbstractImServerTests {

    protected SocketIOServer server;
    
    @Before
    public void before() {
        startServer();
    }
    
    protected void startServer() {
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(8088);
        config.setKeyStorePassword("chatto362implemented");
        config.getSocketConfig().setTcpNoDelay(true);
        InputStream stream = getClass().getResourceAsStream("/phnimserver.jks");
        config.setKeyStore(stream);
        server = new SocketIOServer(config);
        TojoyNamespacesHub namespacesHub = new TojoyNamespacesHub(config);
        PacketBufServerChannelInitializer channelInitializer = new PacketBufServerChannelInitializer(namespacesHub);
        server.setPipelineFactory(channelInitializer);

        server.start();
        log.info("SocketIOServer start...");
    }
    
    @After
    public void after() {
    }
    
    
}


