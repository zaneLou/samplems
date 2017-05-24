package com.phn.netty.example.echo;

import java.util.concurrent.atomic.AtomicInteger;

public class EchoClients {
	
	public static AtomicInteger sendCount = new AtomicInteger(0);
	public static void main(String[] args) throws Exception {
		System.out.println("EchoClients start");
		for (int i = 0; i < 500; i++) {
			EchoClient echoClient = new EchoClient();
			echoClient.start();
			//System.out.println("EchoClient :" + i);
		}
		System.out.println("EchoClients end");
	}
}
