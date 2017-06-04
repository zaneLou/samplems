package com.phn.embryo;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.corundumstudio.socketio.Configuration;
import com.google.protobuf.MessageLite;
import com.phn.embryo.listener.EmbryoClientListeners;
import com.phn.embryo.listener.EmbryoReceiverListener;
import com.phn.embryo.listener.EmbryoRegisterListener;

import io.netty.util.internal.PlatformDependent;

public class EmbryoNamespace implements EmbryoClientListeners{

	private final String name;
	private final Map<String, AbstractEmbryoTunnel> sessionId2clients = PlatformDependent.newConcurrentHashMap();

	private final Queue<EmbryoReceiverListener> receiverListeners = new ConcurrentLinkedQueue<EmbryoReceiverListener>();
    private final Queue<EmbryoRegisterListener> registerListeners = new ConcurrentLinkedQueue<EmbryoRegisterListener>();
    
    public EmbryoNamespace(String name, Configuration configuration) {
        super();
        this.name = name;
    }
    
    //Map
	public void addClient(AbstractEmbryoTunnel client) {
		sessionId2clients.put(client.getSessionId(), client);
	}

	public void removeClient(String sessionId) {
		sessionId2clients.remove(sessionId);
	}

	public AbstractEmbryoTunnel get(String sessionId) {
		return sessionId2clients.get(sessionId);
	}

	public String getName() {
		return name;
	}

	//Listener
	@Override
	public void addReceiverListener(EmbryoReceiverListener listener) {
		receiverListeners.add(listener);
	}
	
    public void onReceivePacket(AbstractEmbryoTunnel client, MessageLite packet){
    	for (EmbryoReceiverListener receiverListener : receiverListeners) {
    		receiverListener.onReceiver(client, packet);
		}
    }
    
	@Override
	public void addRegisterListener(EmbryoRegisterListener listener) {
		registerListeners.add(listener);
	}
	
	public void onRegister(AbstractEmbryoTunnel client, MessageLite packet){
		for (EmbryoRegisterListener registerListener : registerListeners) {
			registerListener.onRegister(client, packet);
		}
	}
	
    public void shutdown() {

    }

	
	
}
