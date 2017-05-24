package com.phn.embryo;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.namespace.EventEntry;
import com.phn.embryo.listener.EmbryoClientListeners;
import com.phn.embryo.listener.EmbryoRegisterListener;

import io.netty.channel.Channel;
import io.netty.util.internal.PlatformDependent;

public class EmbryoNamespace implements EmbryoClientListeners{

	private final String name;
	private final Map<String, EmbryoTunnel> uuid2clients = PlatformDependent.newConcurrentHashMap();
	private final Map<Channel, EmbryoTunnel> channel2clients = PlatformDependent.newConcurrentHashMap();

    private final Queue<EmbryoRegisterListener> registerListeners = new ConcurrentLinkedQueue<EmbryoRegisterListener>();
    
    public EmbryoNamespace(String name, Configuration configuration) {
        super();
        this.name = name;
    }
    
    //Map
	public void addClient(EmbryoTunnel client) {
		uuid2clients.put(client.getSessionId(), client);
	}

	public void removeClient(UUID sessionId) {
		uuid2clients.remove(sessionId);
	}

	public EmbryoTunnel get(String sessionId) {
		return uuid2clients.get(sessionId);
	}

	public void add(Channel channel, EmbryoTunnel client) {
		channel2clients.put(channel, client);
	}

	public void remove(Channel channel) {
		channel2clients.remove(channel);
	}

	public EmbryoTunnel get(Channel channel) {
		return channel2clients.get(channel);
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
	
	
}
