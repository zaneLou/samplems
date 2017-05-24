package com.phn.socketio.client.listener;

import com.phn.base.component.PhnAuthenticator;
import com.phn.socketio.client.ImClient;
import com.phn.socketio.manager.AbstractManager;

public abstract class AbstractListener extends AbstractManager {

	final ImClient client;
	final PhnAuthenticator authenticator;
	public AbstractListener(ImClient client, PhnAuthenticator authenticator){
		super();
		this.client = client;
		this.authenticator = authenticator;
	}

	@Override
	public PhnAuthenticator getAuthenticator() {
		return authenticator;
	}
	
}
