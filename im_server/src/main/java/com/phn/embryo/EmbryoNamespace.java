package com.phn.embryo;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.corundumstudio.socketio.Configuration;
import com.phn.embryo.listener.EmbryoClientListeners;
import com.phn.embryo.listener.EmbryoRegisterListener;

import io.netty.util.internal.PlatformDependent;

public class EmbryoNamespace implements EmbryoClientListeners{

	private final String name;
	private final Map<String, EmbryoTunnel> sessionId2clients = PlatformDependent.newConcurrentHashMap();

    private final Queue<EmbryoRegisterListener> registerListeners = new ConcurrentLinkedQueue<EmbryoRegisterListener>();
    
    public EmbryoNamespace(String name, Configuration configuration) {
        super();
        this.name = name;
    }
    
    //Map
	public void addClient(EmbryoTunnel client) {
		sessionId2clients.put(client.getSessionId(), client);
	}

	public void removeClient(String sessionId) {
		sessionId2clients.remove(sessionId);
	}

	public EmbryoTunnel get(String sessionId) {
		return sessionId2clients.get(sessionId);
	}

	public String getName() {
		return name;
	}

	//Lister
	@Override
	public void addRegisterListener(EmbryoRegisterListener listener) {
		registerListeners.add(listener);
	}
	
	public void onRegister(EmbryoTunnel client, EmbryoPacket packet){
		for (EmbryoRegisterListener registerListener : registerListeners) {
			registerListener.onRegister(client, packet);
		}
	}
	
    public void shutdown() {

    }
	
}
