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
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

@Sharable
@Slf4j
public abstract class AbstractEmbryoInPacketHandler extends ChannelInboundHandlerAdapter implements DisconnectableHub{

    protected final Configuration configuration;
	protected final EmbryoNamespacesHub namespacesHub;
    protected final StoreFactory storeFactory;
    protected final CancelableScheduler disconnectScheduler;
    protected final EmbryoAckManager ackManager;
    
    protected AttributeKey<Boolean> hasTunnel = AttributeKey.newInstance("hasTunnel");
    
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
                log.debug("Client with ip {} opened channel but doesn't send any data! Channel closed!", ctx.channel().remoteAddress());
                ctx.channel().close();
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
            EmbryoTunnel client = getClient(packet.getNamespace(), channel, true);
            receivePacket(client, packet);
            
            //Add Timeout
            disconnectScheduler.schedule(key, new Runnable() {
                @Override
                public void run() {
                    ctx.channel().close();
                    disconnect(packet.getNamespace(), ctx.channel());
                    log.debug("Client with sessionId {} opened channel but ping timeout!", channel.id().asLongText());
                }
            }, configuration.getPingTimeout(), TimeUnit.MILLISECONDS);
        }
    }
    
    public abstract void receivePacket(EmbryoTunnel client, EmbryoPacket packet);
    
    public void disconnect(String spacename, Channel channel) {
        EmbryoTunnel client = getClient(spacename, channel, false);
        if(client==null){
            log.error("onDisconnect not found  {} ", channel.id().asLongText());
        }
        onDisconnect(client);
    }
    
    public EmbryoTunnel getClient(String spacename, Channel channel, boolean createIfNoExist) {
        EmbryoTunnel client = null;
        EmbryoNamespace namespace = namespacesHub.get(spacename);
        if (namespace == null) {
            log.warn("Not found namespace {} ", spacename);
            return client;
        }
        Attribute<Boolean> value = channel.attr(hasTunnel);
        if (value != null && !value.get()) {
            client = namespace.get(channel.id().asLongText());
        }
        
        if(client==null && createIfNoExist){
            client = new EmbryoTunnel(channel, ackManager);
            client.setSpacename(spacename);
            namespace.addClient(client);
        }
        return client;
    }
    
    @Override
    public void onDisconnect(EmbryoTunnel client) {
        ackManager.onDisconnect(client);
        EmbryoNamespace namespace = namespacesHub.get(client.getSpacename());
        if(namespace==null){
            log.error("onDisconnect namespace {} ", client.getSpacename());
        }else{
            namespace.removeClient(client.getSessionId());
        }
    }
    
    public void processPacket(EmbryoTunnel client, EmbryoPacket packet){
    	EmbryoNamespace namespace = namespacesHub.get(packet.getNamespace());
    	switch (packet.getPath()) {
            case EmbryoPacket.PATH_REGISTER:
                namespace.onRegister(client, packet);
                break;

            default:
                break;
        }
    }
    
}
