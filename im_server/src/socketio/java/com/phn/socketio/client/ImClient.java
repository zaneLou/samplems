package com.phn.socketio.client;

import com.phn.proto.PhnNetBuf.PhnDataBuf;

import io.socket.client.Socket;

public interface ImClient {

	public Socket getSocket();
	public boolean isLogined();
	public void recieve(PhnDataBuf dataBuf) ;
	public void didConnect() ;
	public void didLogin() ;
}
