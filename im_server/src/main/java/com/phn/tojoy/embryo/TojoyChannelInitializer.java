package com.phn.tojoy.embryo;

import com.corundumstudio.socketio.store.StoreFactory;
import com.google.protobuf.MessageLite;
import com.phn.embryo.AbstractEmbryoChannelInitializer;
import com.phn.embryo.AbstractEmbryoAckManager;
import com.phn.embryo.AbstractEmbryoInPacketHandler;
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
        AbstractEmbryoAckManager ackManager = new TojoyAckManager(scheduler);
        StoreFactory storeFactory = configuration.getStoreFactory();
        AbstractEmbryoInPacketHandler handler = new TojoyInPacketHandler(configuration, namespacesHub, storeFactory, scheduler, ackManager);
        return handler;
    }

}


