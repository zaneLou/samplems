package com.phn.socketio.client.listener;

import com.phn.base.component.PhnAuthenticator;
import com.phn.socketio.client.ImClient;

import io.socket.emitter.Emitter.Listener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DisConnectListener extends AbstractListener implements Listener{

	public DisConnectListener(ImClient client, PhnAuthenticator authenticator) {
		super(client, authenticator);
	}

	@Override
	public void call(Object... args) {
		log.info("client disconnected args {}", args);
	}

}
