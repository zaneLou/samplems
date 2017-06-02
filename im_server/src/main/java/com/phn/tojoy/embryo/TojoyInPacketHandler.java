package com.phn.tojoy.embryo;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.scheduler.CancelableScheduler;
import com.corundumstudio.socketio.store.StoreFactory;
import com.phn.embryo.AbstractEmbryoInPacketHandler;
import com.phn.embryo.EmbryoAckManager;
import com.phn.embryo.EmbryoNamespacesHub;
import com.phn.embryo.EmbryoPacket;
import com.phn.embryo.EmbryoTunnel;

/**
 * Copyright 2017年5月31日, Easemob.inc
 * All rights reserved.
 *
 * @author liuzheng
 */
public class TojoyInPacketHandler extends AbstractEmbryoInPacketHandler{

    public TojoyInPacketHandler(Configuration configuration, EmbryoNamespacesHub namespacesHub, StoreFactory storeFactory,
            CancelableScheduler disconnectScheduler, EmbryoAckManager ackManager) {
        super(configuration, namespacesHub, storeFactory, disconnectScheduler, ackManager);
    }

    @Override
    public void receivePacket(EmbryoTunnel client, EmbryoPacket packet) {
        // TODO Auto-generated method stub

    }

}


