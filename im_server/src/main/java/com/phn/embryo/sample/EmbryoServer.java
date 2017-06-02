package com.phn.embryo.sample;

import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.phn.tojoy.channel.PacketBufServerChannelInitializer;
import com.phn.tojoy.embryo.TojoyNamespacesHub;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmbryoServer {

	public static void main(String[] args) {
		EmbryoServer embryoServer = new EmbryoServer();
		embryoServer.postConstruct();
	}

	protected SocketIOServer server;
	
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
		//use PacketBufChannelInitializer
		TojoyNamespacesHub namespacesHub = new TojoyNamespacesHub(config);
		PacketBufServerChannelInitializer channelInitializer = new PacketBufServerChannelInitializer(namespacesHub);
		server.setPipelineFactory(channelInitializer);
		
		server.start();
		log.info("SocketIOServer start...");
	}

	@PreDestroy
	public void preDestroy() {
		server.stop();
		log.info("SocketIOServer stop");
	}

}
