package com.phn.socketio.client.listener;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.phn.base.component.PhnAuthenticator;
import com.phn.base.component.PhnConstants;
import com.phn.proto.PhnNetBuf.PhnDataBuf;
import com.phn.proto.PhnNetBuf.PhnLoginBuf;
import com.phn.socketio.client.ImClient;

import io.socket.client.Ack;
import io.socket.emitter.Emitter.Listener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectListener extends AbstractListener implements Listener {

	protected String username;
	protected String password;

	public ConnectListener(ImClient client, PhnAuthenticator authenticator) {
		super(client, authenticator);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void call(Object... args) {
		log.info("client connected id {} args {}", client.getSocket().id(), args);
		client.didConnect();
//		try {
//			loginByUsername(username, password);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
	}

	// login
	public void loginByUsername(String username, String password) throws UnsupportedEncodingException {
		log.info("client loginByUsername");
		Long dataId = new Date().getTime();
		int dataType = PhnConstants.SocketIO_Data_Type_LoginByUsername;
		PhnLoginBuf loginBuf = PhnLoginBuf.newBuilder().setUserName(username).setPassword(password).build();
		PhnDataBuf dataBuf = getPhnDataBufBuiler(null, dataId.toString(), dataType, false, null,
				PhnConstants.User_Tokens_Device_Desktop, loginBuf.toByteString());
		String content = new String(dataBuf.toByteArray(), "UTF-8");
		log.info("client emit id {} content {}", client.getSocket().id(), content);
		
		//dataBuf.toByteArray()
		client.getSocket().emit(PhnConstants.SocketIO_Data_Event, content , new Ack() {
			@Override
			public void call(Object... args) {
				log.info("ack from server args {}", args);
				client.didLogin();
			}
		});
	}

}
