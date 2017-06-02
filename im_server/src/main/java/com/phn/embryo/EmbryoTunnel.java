package com.phn.embryo;

import java.util.concurrent.atomic.AtomicBoolean;

import com.corundumstudio.socketio.AckCallback;
import io.netty.channel.Channel;
import lombok.Data;

@Data
public class EmbryoTunnel {

	private final AtomicBoolean disconnected = new AtomicBoolean();
	protected Channel channel;
	protected EmbryoAckManager ackManager;
	protected String spacename;
	
	public EmbryoTunnel(Channel channel, EmbryoAckManager ackManager){
		super();
		this.channel = channel;
		this.ackManager = ackManager;
	}
	
	public void sendPatche(EmbryoPacket packet, AckCallback<?> ackCallback) {
		if (!isConnected()) {
			if (ackCallback != null) {
				ackCallback.onTimeout();
			}
			return;
		}
		long index = ackManager.registerAck(getSessionId(), ackCallback);
		packet.setAckId(index);
		channel.writeAndFlush(packet);
	}
	
	public String getSessionId(){
		return channel.id().asLongText();
	}
	
	private boolean isConnected() {
		return !disconnected.get();
	}

	//Channel
	public boolean isChannelOpen() {
		return channel.isActive();
	}

}
