package com.phn.tojoy.service;

import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.phn.base.component.PhnConstants;
import com.phn.tojoy.embryo.TojoyChannelInitializer;
import com.phn.tojoy.embryo.TojoyNamespacesHub;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImServiceImpl implements ImService{

	protected SocketIOServer server;
	
	protected TojoyNamespacesHub namespacesHub;
	
	@Override
	public SocketIOServer getServer() {
		return server;
	}

	@Override
	public TojoyNamespacesHub getNamespacesHub() {
		return namespacesHub;
	}
	
	@PostConstruct
	public void postConstruct() {
		
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(8088);
        config.setKeyStorePassword("chatto362implemented");
        config.getSocketConfig().setTcpNoDelay(true);
        InputStream stream = getClass().getResourceAsStream("/phnimserver.jks");
        config.setKeyStore(stream);
        server = new SocketIOServer(config);
        
        //channelInitializer
        namespacesHub = new TojoyNamespacesHub(config);
        TojoyChannelInitializer channelInitializer = new TojoyChannelInitializer(namespacesHub);
        server.setPipelineFactory(channelInitializer);
        namespacesHub.create(PhnConstants.IM_Namespace);

        //Listener
        
        //start
        server.start();
        log.info("SocketIOServer start...");

	}

	@PreDestroy
	public void preDestroy() {
		server.stop();
		log.info("SocketIOServer stop");
	}
}
