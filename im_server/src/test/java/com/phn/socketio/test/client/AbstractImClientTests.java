package com.phn.socketio.test.client;

import org.junit.After;
import org.junit.Before;

import com.phn.socketio.client.ImClientLauncher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractImClientTests {

	protected ImClientLauncher launcher = new ImClientLauncher();
	
    @Before
    public void before() {
    	launcher = new ImClientLauncher();

    }
    
    @After
    public void after() {
    	launcher.stopClient();
    }
    
	
	
}
