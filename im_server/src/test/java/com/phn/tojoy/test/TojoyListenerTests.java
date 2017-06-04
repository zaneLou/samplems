package com.phn.tojoy.test;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;
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
public class TojoyListenerTests extends AbstractTojoyTests{

    @Test
    public void testReceiverListener() throws InterruptedException {
        log.info("testReceiverListener start");
        Date now = new Date();
        PacketBuf.Builder builer = PacketBuf.newBuilder();
        PacketBuf packetBuf = builer.setPacketId(now.getTime() + "").setNamespace(PhnConstants.IM_Namespace).setPath("path").build();
        EmbryoNamespace namespace = imService.getNamespacesHub().get(PhnConstants.IM_Namespace);
        OnceServerReceiverListener onceServerReceiverListener = new OnceServerReceiverListener();
        namespace.addReceiverListener(onceServerReceiverListener);
        EmbryoClient client = createEmbryoClient();
        client.start(new OnceClientReceiverListener());
      	client.sendPacket(packetBuf);
      	while (!onceServerReceiverListener.isDidRecieved()) {
			Thread.sleep(300l);
		}
      	PacketBuf serverPacketBuf = (PacketBuf)onceServerReceiverListener.getPacket();
      	assertThat(serverPacketBuf.getPacketId()).isEqualTo(packetBuf.getPacketId());
      	client.shutdown();
      	log.info("testReceiverListener end");
    }
}
