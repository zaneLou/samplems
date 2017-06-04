package com.phn.tojoy.embryo;

import com.corundumstudio.socketio.scheduler.CancelableScheduler;
import com.google.protobuf.MessageLite;
import com.phn.embryo.AbstractEmbryoAckManager;
import com.phn.proto.PhnNetBuf.PacketBuf;

public class TojoyAckManager extends AbstractEmbryoAckManager{

	public TojoyAckManager(CancelableScheduler scheduler) {
		super(scheduler);
	}

	@Override
	public long getAckId(MessageLite packet) {
		if(packet instanceof PacketBuf){
			PacketBuf packetBuf = (PacketBuf)packet;
			return packetBuf.getAckId();
		}
		return 0;
	}

}
