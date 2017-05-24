package com.phn.embryo;

import java.util.concurrent.TimeUnit;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.scheduler.CancelableScheduler;
import com.corundumstudio.socketio.scheduler.SchedulerKey;
import com.corundumstudio.socketio.scheduler.SchedulerKey.Type;
import com.corundumstudio.socketio.store.StoreFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
@Slf4j
public abstract class AbstractEmbryoInPacketHandler extends ChannelInboundHandlerAdapter{

	private final Configuration configuration;
    private final EmbryoNamespacesHub namespacesHub;
    private final StoreFactory storeFactory;
    private final CancelableScheduler disconnectScheduler;
    private final EmbryoAckManager ackManager;
    
    //register login logout connect ack message 
	public AbstractEmbryoInPacketHandler(Configuration configuration, EmbryoNamespacesHub namespacesHub,
			StoreFactory storeFactory, CancelableScheduler disconnectScheduler,
			EmbryoAckManager ackManager) {
		this.configuration = configuration;
		this.namespacesHub = namespacesHub;
		this.storeFactory = storeFactory;
		this.disconnectScheduler = disconnectScheduler;
		this.ackManager = ackManager;
	}

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        SchedulerKey key = new SchedulerKey(Type.PING_TIMEOUT, ctx.channel());
        disconnectScheduler.schedule(key, new Runnable() {
            @Override
            public void run() {
                ctx.channel().close();
                log.debug("Client with ip {} opened channel but doesn't send any data! Channel closed!", ctx.channel().remoteAddress());
            }
        }, configuration.getFirstDataTimeout(), TimeUnit.MILLISECONDS);
        super.channelActive(ctx);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SchedulerKey key = new SchedulerKey(Type.PING_TIMEOUT, ctx.channel());
        disconnectScheduler.cancel(key);
        if (msg instanceof EmbryoPacket) {
        	EmbryoPacket packet = (EmbryoPacket) msg;
            Channel channel = ctx.channel();
           	//Get Client
            EmbryoNamespace namespace = namespacesHub.get(packet.getNamespace());
            if(namespace==null){
            	log.error("Not found namespace {} ", packet.getNamespace());
            	return;
            }
            EmbryoTunnel client = namespace.get(channel);
            if(client == null){
            	client = new EmbryoTunnel(channel, ackManager);
            	namespace.add(channel, client);
            	namespace.addClient(client);
            }
            receivePacket(client, packet);
        }
    }
    
    public abstract void receivePacket(EmbryoTunnel client, EmbryoPacket packet);
    
    public void processPacket(EmbryoTunnel client, EmbryoPacket packet){
    	EmbryoNamespace namespace = namespacesHub.get(packet.getNamespace());
    	if(packet.getPath().equals(EmbryoPacket.PATH_REGISTER)){
    		namespace.onRegister(client, packet);
    	}
    }
    
}
