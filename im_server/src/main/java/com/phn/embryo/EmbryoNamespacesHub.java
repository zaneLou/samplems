package com.phn.embryo;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

import com.corundumstudio.socketio.Configuration;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	public void shutdown() {
	    for (EmbryoNamespace namespace : namespaces.values()) {
	        namespace.shutdown();
        }
	}
	
	/*
	 * EmbryoNamespace
	 */
	public EmbryoNamespace get(String name) {
		return namespaces.get(name);
	}

	public Collection<EmbryoNamespace> getAllNamespaces() {
        return namespaces.values();
    }
	
	public void remove(String name) {
		EmbryoNamespace namespace = namespaces.remove(name);
		log.info("remove namespace {}", namespace);
	}

	
}
