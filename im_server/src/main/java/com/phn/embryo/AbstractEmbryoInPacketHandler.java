package com.phn.embryo;

import java.util.concurrent.TimeUnit;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.scheduler.CancelableScheduler;
import com.corundumstudio.socketio.scheduler.SchedulerKey;
import com.corundumstudio.socketio.scheduler.SchedulerKey.Type;
import com.corundumstudio.socketio.store.StoreFactory;
import com.google.protobuf.MessageLite;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

@Sharable
@Slf4j
public abstract class AbstractEmbryoInPacketHandler extends ChannelInboundHandlerAdapter implements DisconnectableHub{

    protected final Configuration configuration;
	protected final EmbryoNamespacesHub namespacesHub;
    protected final StoreFactory storeFactory;
    protected final CancelableScheduler disconnectScheduler;
    protected final AbstractEmbryoAckManager ackManager;
    
    protected AttributeKey<Boolean> hasTunnel = AttributeKey.newInstance("hasTunnel");
    
    //register login logout connect ack message 
	public AbstractEmbryoInPacketHandler(Configuration configuration, EmbryoNamespacesHub namespacesHub,
			StoreFactory storeFactory, CancelableScheduler disconnectScheduler,
			AbstractEmbryoAckManager ackManager) {
		this.configuration = configuration;
		this.namespacesHub = namespacesHub;
		this.storeFactory = storeFactory;
		this.disconnectScheduler = disconnectScheduler;
		this.ackManager = ackManager;
	}
	
    public abstract String getNamespace(MessageLite packet);
    public abstract String getPath(MessageLite packet);
    public abstract AbstractEmbryoTunnel newEmbryoTunnel(Channel channel, AbstractEmbryoAckManager ackManager);
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    	log.info("EmbryoInPacketHandler channelActive {} ", ctx.channel().id().asLongText());
        SchedulerKey key = new SchedulerKey(Type.PING_TIMEOUT, ctx.channel());
        disconnectScheduler.schedule(key, new Runnable() {
            @Override
            public void run() {
                log.info("Client with ip {} opened channel but doesn't send any data! Channel closed!", ctx.channel().remoteAddress());
                ctx.channel().close();
            }
        }, configuration.getFirstDataTimeout(), TimeUnit.MILLISECONDS);
        super.channelActive(ctx);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	log.info("EmbryoInPacketHandler channelRead {} msg {}", ctx.channel().id().asLongText(), msg);
        SchedulerKey key = new SchedulerKey(Type.PING_TIMEOUT, ctx.channel());
        disconnectScheduler.cancel(key);
        if (msg instanceof MessageLite) {
        	MessageLite packet = (MessageLite) msg;
            Channel channel = ctx.channel();
            //Get Client
            AbstractEmbryoTunnel client = getClient(getNamespace(packet), channel, true);
            receivePacket(client, packet);
            
            //Add Timeout
            disconnectScheduler.schedule(key, new Runnable() {
                @Override
                public void run() {
                    ctx.channel().close();
                    disconnect(getNamespace(packet), ctx.channel());
                    log.info("Client with sessionId {} opened channel but ping timeout!", channel.id().asLongText());
                }
            }, configuration.getPingTimeout(), TimeUnit.MILLISECONDS);
        }
    }
    
    public void disconnect(String spacename, Channel channel) {
        AbstractEmbryoTunnel client = getClient(spacename, channel, false);
        if(client==null){
            log.error("onDisconnect not found  {} ", channel.id().asLongText());
            return;
        }
        onDisconnect(client);
    }
    
    public AbstractEmbryoTunnel getClient(String spacename, Channel channel, boolean createIfNoExist) {
        AbstractEmbryoTunnel client = null;
        EmbryoNamespace namespace = namespacesHub.get(spacename);
        if (namespace == null) {
            log.warn("Not found namespace {} ", spacename);
            return client;
        }
        Attribute<Boolean> value = channel.attr(hasTunnel);
        if (value != null && value.get() != null && !value.get()) {
            client = namespace.get(channel.id().asLongText());
        }
        
        if(client==null && createIfNoExist){
            client = newEmbryoTunnel(channel, ackManager);
            client.setSpacename(spacename);
            namespace.addClient(client);
        }
        return client;
    }
    
    @Override
    public void onDisconnect(AbstractEmbryoTunnel client) {
        ackManager.onDisconnect(client);
        EmbryoNamespace namespace = namespacesHub.get(client.getSpacename());
        if(namespace==null){
            log.error("onDisconnect namespace {} ", client.getSpacename());
        }else{
            namespace.removeClient(client.getSessionId());
        }
    }
    
    public void receivePacket(AbstractEmbryoTunnel client, MessageLite packet){
    	log.info("EmbryoInPacketHandler receivePacket {} msg {}", client, packet);
    	EmbryoNamespace namespace = namespacesHub.get(getNamespace(packet));
    	namespace.onReceivePacket(client, packet);
    }
    
    public void processPacket(AbstractEmbryoTunnel client, MessageLite packet){
    	EmbryoNamespace namespace = namespacesHub.get(getNamespace(packet));
    	switch (getPath(packet)) {
            case EmbryoConstants.PATH_REGISTER:
                namespace.onRegister(client, packet);
                break;

            default:
                break;
        }
    }
    
}
