package com.phn.tojoy.embryo;

import com.google.protobuf.MessageLite;
import com.phn.embryo.AbstractEmbryoAckManager;
import com.phn.embryo.AbstractEmbryoTunnel;
import com.phn.proto.PhnNetBuf.PacketBuf;

import io.netty.channel.Channel;

public class TojoyTunnel extends AbstractEmbryoTunnel{

	public TojoyTunnel(Channel channel, AbstractEmbryoAckManager ackManager) {
		super(channel, ackManager);
	}

	@Override
	public MessageLite setAckId(MessageLite packet, long index) {
		if(packet instanceof PacketBuf){
			PacketBuf packetBuf = (PacketBuf)packet;
			return packetBuf.toBuilder().setAckId(index).build();
		}
		return null;
	}

}
