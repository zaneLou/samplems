package com.phn.embryo;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

import com.corundumstudio.socketio.Configuration;
import io.netty.util.internal.PlatformDependent;

public class EmbryoNamespacesHub {

	private final ConcurrentMap<String, EmbryoNamespace> namespaces = PlatformDependent.newConcurrentHashMap();
	private final Configuration configuration;

	public EmbryoNamespacesHub(Configuration configuration) {
		this.configuration = configuration;
	}

	public EmbryoNamespace create(String name) {
		EmbryoNamespace namespace = namespaces.get(name);
		if (namespace == null) {
			namespace = new EmbryoNamespace(name, configuration);
			EmbryoNamespace oldNamespace = namespaces.putIfAbsent(name, namespace);
			if (oldNamespace != null) {
				namespace = oldNamespace;
			}
		}
		return namespace;
	}
	
	//TODO
//	public Iterable<SocketIOClient> getRoomClients(String room) {
//		List<Iterable<SocketIOClient>> allClients = new ArrayList<Iterable<SocketIOClient>>();
//		for (EmbryoNamespace namespace : namespaces.values()) {
//			Iterable<SocketIOClient> clients = ((Namespace) namespace).getRoomClients(room);
//			allClients.add(clients);
//		}
//		return new CompositeIterable<SocketIOClient>(allClients);
//	}

	public EmbryoNamespace get(String name) {
		return namespaces.get(name);
	}

	//TODO
//	public void remove(String name) {
//		EmbryoNamespace namespace = namespaces.remove(name);
//		if (namespace != null) {
//			namespace.getBroadcastOperations().disconnect();
//		}
//	}

	public Collection<EmbryoNamespace> getAllNamespaces() {
		return namespaces.values();
	}
}
