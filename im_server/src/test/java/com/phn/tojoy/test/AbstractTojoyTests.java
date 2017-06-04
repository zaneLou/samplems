package com.phn.tojoy.test;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.MessageLite;
import com.phn.embryo.AbstractEmbryoTunnel;
import com.phn.embryo.client.EmbryoClient;
import com.phn.embryo.client.EmbryoClientReceiverListener;
import com.phn.embryo.listener.EmbryoReceiverListener;
import com.phn.proto.PhnNetBuf.PacketBuf;
import com.phn.tojoy.service.ImService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright 2017年5月25日, Easemob.inc
 * All rights reserved.
 *
 * @author liuzheng
 */
@Slf4j
public abstract class AbstractTojoyTests {

    @Autowired
    protected  ImService imService;
    
    @Before
    public void before() {
    }
 
    @After
    public void after() {
    }
    
    public EmbryoClient createEmbryoClient(){
    	EmbryoClient client = new EmbryoClient(imService.getServer().getConfiguration());
    	return client;
    }
    
    @Data
	class OnceServerReceiverListener implements  EmbryoReceiverListener  {
    	private  boolean didRecieved = false;
    	private  AbstractEmbryoTunnel client;
    	private  MessageLite packet;
    	
		@Override
		public void onReceiver(AbstractEmbryoTunnel client, MessageLite packet) {
			log.info("onReceiver server: packet {}", packet);
			didRecieved = true;
			this.client = client;
			this.packet = packet;
		}
    }
    
    @Data
	class OnceClientReceiverListener implements  EmbryoClientReceiverListener{
    	private  boolean didRecieved = false;
    	private  PacketBuf packetBuf ;
		@Override
		public void onReceiver(PacketBuf packetBuf) {
			log.info("onReceiver client: packetBuf {}", packetBuf);
			didRecieved = true;
			this.packetBuf = packetBuf;
		}
	}
}


