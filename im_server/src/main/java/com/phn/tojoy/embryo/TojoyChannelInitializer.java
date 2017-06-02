package com.phn.tojoy.embryo;

import com.corundumstudio.socketio.store.StoreFactory;
import com.google.protobuf.MessageLite;
import com.phn.embryo.AbstractEmbryoChannelInitializer;
import com.phn.embryo.EmbryoAckManager;
import com.phn.embryo.EmbryoNamespacesHub;
import com.phn.proto.PhnNetBuf.PacketBuf;

import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Copyright 2017年5月31日, Easemob.inc
 * All rights reserved.
 *
 * @author liuzheng
 */
public class TojoyChannelInitializer extends AbstractEmbryoChannelInitializer{

    public TojoyChannelInitializer(EmbryoNamespacesHub namespacesHub) {
        super(namespacesHub);
    }

    @Override
    public MessageLite getPrototype() {
        return PacketBuf.getDefaultInstance();
    }

    @Override
    public ChannelInboundHandlerAdapter getInPacketHandler() {
        EmbryoAckManager ackManager = new EmbryoAckManager(scheduler);
        StoreFactory storeFactory = configuration.getStoreFactory();
        TojoyInPacketHandler handler = new TojoyInPacketHandler(configuration, namespacesHub, storeFactory, scheduler, ackManager);
        return handler;
    }

}


