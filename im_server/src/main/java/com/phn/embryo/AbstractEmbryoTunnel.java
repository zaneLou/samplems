package com.phn.embryo;

import java.util.concurrent.atomic.AtomicBoolean;

import com.corundumstudio.socketio.AckCallback;
import com.google.protobuf.MessageLite;

import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class AbstractEmbryoTunnel {

	private final AtomicBoolean disconnected = new AtomicBoolean();
	protected Channel channel;
	protected AbstractEmbryoAckManager ackManager;
	protected String spacename;
	
	public AbstractEmbryoTunnel(Channel channel, AbstractEmbryoAckManager ackManager){
		super();
		this.channel = channel;
		this.ackManager = ackManager;
	}
	
	public abstract MessageLite setAckId(MessageLite packet, long index);
	
	public void sendPatche(MessageLite packet, AckCallback<?> ackCallback) {
		if (!isConnected()) {
			if (ackCallback != null) {
				ackCallback.onTimeout();
			}
			return;
		}
		long index = ackManager.registerAck(getSessionId(), ackCallback);
		packet = setAckId(packet, index);
		if(packet==null){
			log.error("AbstractEmbryoTunnel setAckId return null");
			return;
		}
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
