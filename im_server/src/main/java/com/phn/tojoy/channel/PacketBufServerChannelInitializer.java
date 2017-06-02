package com.phn.tojoy.channel;

import com.google.protobuf.MessageLite;
import com.phn.embryo.AbstractEmbryoChannelInitializer;
import com.phn.embryo.EmbryoNamespacesHub;
import com.phn.embryo.sample.PacketBufServerHandler;
import com.phn.proto.PhnNetBuf.PacketBuf;

import io.netty.channel.ChannelInboundHandlerAdapter;

public class PacketBufServerChannelInitializer extends AbstractEmbryoChannelInitializer{

	public PacketBufServerChannelInitializer(EmbryoNamespacesHub namespacesHub) {
        super(namespacesHub);
    }

    @Override
	public MessageLite getPrototype() {
		return PacketBuf.getDefaultInstance();
	}

	@Override
	public ChannelInboundHandlerAdapter getInPacketHandler() {
		return new PacketBufServerHandler();
	}

}
