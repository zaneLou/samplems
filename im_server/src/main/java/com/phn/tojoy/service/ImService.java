package com.phn.tojoy.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.phn.tojoy.embryo.TojoyNamespacesHub;

public interface ImService {

	public SocketIOServer getServer();
	public TojoyNamespacesHub getNamespacesHub();
	
}
