package com.phn.socketio.laucher;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExitThread extends Thread {
	@Override
	public void run() {
		log.info("press ENTER to call System.exit(0) ");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
