package com.phn.tojoy.embryo;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.scheduler.CancelableScheduler;
import com.corundumstudio.socketio.store.StoreFactory;
import com.google.protobuf.MessageLite;
import com.phn.embryo.AbstractEmbryoAckManager;
import com.phn.embryo.AbstractEmbryoInPacketHandler;
import com.phn.embryo.AbstractEmbryoTunnel;
import com.phn.embryo.EmbryoNamespacesHub;
import com.phn.proto.PhnNetBuf.PacketBuf;

import io.netty.channel.Channel;

public class TojoyInPacketHandler extends AbstractEmbryoInPacketHandler {

	public TojoyInPacketHandler(Configuration configuration, EmbryoNamespacesHub namespacesHub,
			StoreFactory storeFactory, CancelableScheduler disconnectScheduler, AbstractEmbryoAckManager ackManager) {
		super(configuration, namespacesHub, storeFactory, disconnectScheduler, ackManager);
	}

	@Override
	public String getNamespace(MessageLite packet) {
		if(packet instanceof PacketBuf){
			PacketBuf packetBuf = (PacketBuf)packet;
			return packetBuf.getNamespace();
		}
		return null;
	}

	@Override
	public String getPath(MessageLite packet) {
		if(packet instanceof PacketBuf){
			PacketBuf packetBuf = (PacketBuf)packet;
			return packetBuf.getPath();
		}
		return null;
	}

	@Override
	public AbstractEmbryoTunnel newEmbryoTunnel(Channel channel, AbstractEmbryoAckManager ackManager) {
		return new TojoyTunnel(channel, ackManager);
	}

}
