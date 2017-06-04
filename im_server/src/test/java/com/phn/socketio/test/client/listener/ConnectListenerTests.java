package com.phn.socketio.test.client.listener;

import org.junit.Assert;
import org.junit.Test;
import com.phn.base.component.PhnConstants;
import com.phn.socketio.test.client.AbstractImClientTests;

//@RunWith(SpringJUnit4ClassRunner.class)
public class ConnectListenerTests extends AbstractImClientTests{

    @Test
    public void shouldFindSingleOwnerWithPet() {
    	launcher.startClient( PhnConstants.TestUserName1, PhnConstants.TestPassword);
    	try {
			Thread.sleep(5000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	Assert.assertTrue(launcher.isLogined());
    }

}
