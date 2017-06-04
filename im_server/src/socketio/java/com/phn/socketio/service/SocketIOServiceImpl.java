package com.phn.socketio.service;

import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.phn.base.component.PhnConstants;
import com.phn.socketio.listener.PhnDataListener;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SocketIOServiceImpl implements SocketIOService {

	protected SocketIOServer server;

	@Autowired
	protected PhnDataListener phnDataListener;
	
	@PostConstruct
	public void postConstruct() {
		Configuration config = new Configuration();
		config.setHostname("localhost");
		config.setPort(8088);
        config.setKeyStorePassword("chatto362implemented");
        config.getSocketConfig().setTcpNoDelay(true);
        InputStream stream = getClass().getResourceAsStream("/phnimserver.jks");
        config.setKeyStore(stream);
		server = new SocketIOServer(config);
		final SocketIONamespace namespace = server.addNamespace(PhnConstants.IM_Namespace);
		//namespace.addEventListener(PhnConstants.SocketIO_Data_Event, byte[].class, phnDataListener);
		namespace.addEventListener(PhnConstants.SocketIO_Data_Event, String.class, phnDataListener);
		namespace.addConnectListener(new ConnectListener() {
			@Override
			public void onConnect(SocketIOClient client) {
				log.info("SocketIOClient conncect id {} clienctcount {} ", client.getSessionId(), server.getAllClients().size());
			}
		});
		server.start();
		
		//server.getClient(uuid);
		
		log.info("SocketIOServer start");
	}

	@PreDestroy
	public void preDestroy() {
		server.stop();
		log.info("SocketIOServer stop");
	}
}
