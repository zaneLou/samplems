package com.phn.socketio.client.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phn.base.component.PhnAuthenticator;
import com.phn.proto.PhnNetBuf.PhnDataBuf;
import com.phn.socketio.client.ImClient;

import io.socket.client.Ack;
import io.socket.emitter.Emitter.Listener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataListener extends AbstractListener implements Listener{
	
	public DataListener(ImClient client, PhnAuthenticator authenticator) {
		super(client, authenticator);
	}

	@Override
	public void call(Object... args) {
		for (Object object : args) {
			log.info("cllient ondata object {} ", object);
		}
	    byte[] bytes = (byte[]) args[0];
	    log.info("cllient ondata object1 {} ", args[1].getClass() );
		PhnDataBuf dataBuf = null;
        try {
            dataBuf = PhnDataBuf.parseFrom(bytes);
            client.recieve(dataBuf);
            log.info("cllient ondata client {} PhnDataBuf {}", client.getSocket().id(), dataBuf);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(dataBuf!=null){
            Ack ack = (Ack) args[args.length - 1];
            ack.call(dataBuf.getDataId());
        }

	}

}
