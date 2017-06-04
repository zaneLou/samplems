package com.phn.tojoy.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phn.base.component.PhnConstants;
import com.phn.embryo.EmbryoNamespace;
import com.phn.embryo.client.EmbryoClient;
import com.phn.proto.PhnNetBuf.PacketBuf;
import com.phn.tojoy.laucher.config.ImBusinessConfig;
import lombok.extern.slf4j.Slf4j;

@ContextConfiguration(classes = ImBusinessConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("jpa")
@Slf4j
public class TimeoutTests extends AbstractTojoyTests {

	@Test
	public void testActiveTimeout() throws InterruptedException {
		log.info("testActiveTimeout start");
		EmbryoNamespace namespace = imService.getNamespacesHub().get(PhnConstants.IM_Namespace);
		OnceServerReceiverListener onceServerReceiverListener = new OnceServerReceiverListener();
		namespace.addReceiverListener(onceServerReceiverListener);
		EmbryoClient client = createEmbryoClient();
		client.start(new OnceClientReceiverListener());
		Thread.sleep(imService.getServer().getConfiguration().getFirstDataTimeout() + 1000l);
		assertThat(client.isOpen()).isEqualTo(false);
		client.shutdown();
		log.info("testActiveTimeout end");
	}
	
	@Test
	public void testPingTimeout() throws InterruptedException {
		log.info("testActiveTimeout start");
		Date now = new Date();
        PacketBuf.Builder builer = PacketBuf.newBuilder();
        PacketBuf packetBuf = builer.setPacketId(now.getTime() + "").setNamespace(PhnConstants.IM_Namespace).setPath("path").build();
        
		EmbryoNamespace namespace = imService.getNamespacesHub().get(PhnConstants.IM_Namespace);
		OnceServerReceiverListener onceServerReceiverListener = new OnceServerReceiverListener();
		namespace.addReceiverListener(onceServerReceiverListener);
		EmbryoClient client = createEmbryoClient();
		client.start(new OnceClientReceiverListener());
		client.sendPacket(packetBuf);
		Thread.sleep(imService.getServer().getConfiguration().getPingTimeout() + 1000l);
		assertThat(client.isOpen()).isEqualTo(false);
		client.shutdown();
		log.info("testActiveTimeout end");
	}
}
