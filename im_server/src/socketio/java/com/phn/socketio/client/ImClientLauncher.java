package com.phn.socketio.client;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phn.base.component.BcryptAuthenticator;
import com.phn.base.component.PhnAuthenticator;
import com.phn.base.component.PhnConstants;
import com.phn.proto.PhnNetBuf.PhnDataBuf;
import com.phn.proto.PhnNetBuf.PhnResponseBuf;
import com.phn.socketio.client.listener.ConnectListener;
import com.phn.socketio.client.listener.DataListener;
import com.phn.socketio.client.listener.DisConnectListener;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImClientLauncher implements ImClient{

	protected String host = "localhost";
	protected int port = 8088;
	protected Socket socket;
	private volatile boolean logined;
	protected String token;

	public static AtomicInteger connectCount = new AtomicInteger(0);
	public static AtomicInteger loginCount = new AtomicInteger(0);
	
	public static void main(String[] args) {
		for (int i = 0; i < 200; i++) {
			ImClientLauncher launcher = new ImClientLauncher();
			launcher.startClient(PhnConstants.TestUserName1, PhnConstants.TestPassword);
		}
		//launcher.startClient(PhnConstants.TestUserName1 + "😜", PhnConstants.TestPassword);
	}
	@Override
	public void didConnect(){
		log.info("cllient ondata connectCount {} ", connectCount.addAndGet(1));
	}
	@Override
	public void didLogin(){
		log.info("cllient ondata loginXount {} ", loginCount.addAndGet(1));
	}

	public void startClient(String username, String password) {
		log.info("start client ");
		try {
			PhnAuthenticator authenticator = new BcryptAuthenticator();;
			socket = IO.socket("https://" + host + ":"  + port + PhnConstants.SocketIO_Namespace, createOptions());
			ConnectListener connectListener = new ConnectListener(this, authenticator);
			connectListener.setUsername(username);
			connectListener.setPassword(password);
			DataListener dataListener = new DataListener(this, authenticator);
			DisConnectListener disConnectListener = new DisConnectListener(this, authenticator);
			socket.on(Socket.EVENT_CONNECT, connectListener)
					.on(PhnConstants.SocketIO_Data_Event, dataListener)
					.on(Socket.EVENT_DISCONNECT, disConnectListener);
			socket.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopClient() {
		if(socket!=null && socket.connected())
			socket.disconnect();
	}
	
	@Override
	public Socket getSocket() {
		return socket;
	}
	
	@Override
	public boolean isLogined() {
		return logined;
	}
	
	@Override
	public void recieve(PhnDataBuf dataBuf){
		log.info("recieve DataType {} dataBuf {} ", dataBuf.getDataType(), dataBuf);
		switch (dataBuf.getDataType()) {
		case PhnConstants.SocketIO_Data_Type_LoginByUsername:
			try {
				PhnResponseBuf responseBuf = PhnResponseBuf.parseFrom(dataBuf.getDataObj());
				if(responseBuf.getCode() == PhnConstants.Response_Success){
					logined = true;
					token = responseBuf.getDataObj().toString();
					log.info("Login Success, token: {} ", token);
				}
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
		
	}

	protected IO.Options createOptions() throws GeneralSecurityException, IOException {
		// default settings for all sockets
		// IO.setDefaultSSLContext(mySSLContext);
		// IO.setDefaultHostnameVerifier(myHostnameVerifier);
		IO.Options opts = new IO.Options();
		opts.hostname = host;
		opts.forceNew = true;
		opts.port = port;

		opts.secure = true;
		opts.sslContext = createSSLContext();
        opts.hostnameVerifier = hostnameVerifier;
		return opts;
	}
	
	protected HostnameVerifier hostnameVerifier = new javax.net.ssl.HostnameVerifier(){
        @Override
		public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
            return hostname.equals("localhost");
        }
    };
    
    protected SSLContext createSSLContext() throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance("JKS");
        InputStream stream = getClass().getResourceAsStream("/phnimclient.jks");
        //File file = new File("src/test/resources/keystore.jks");
        String password = "details513implemented";
        ks.load(stream, password.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return sslContext;
    }
    


}
