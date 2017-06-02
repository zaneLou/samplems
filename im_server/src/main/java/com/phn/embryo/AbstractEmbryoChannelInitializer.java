package com.phn.embryo;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOChannelInitializer;
import com.corundumstudio.socketio.namespace.NamespacesHub;
import com.corundumstudio.socketio.scheduler.CancelableScheduler;
import com.corundumstudio.socketio.scheduler.HashedWheelTimeoutScheduler;
import com.corundumstudio.socketio.store.StoreFactory;
import com.google.protobuf.MessageLite;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractEmbryoChannelInitializer extends SocketIOChannelInitializer {

    protected CancelableScheduler scheduler = new HashedWheelTimeoutScheduler();
    protected SSLContext sslContext;
    protected Configuration configuration;
    protected final EmbryoNamespacesHub namespacesHub;
    
    //need implements
    public abstract MessageLite getPrototype();
    public abstract ChannelInboundHandlerAdapter getInPacketHandler();
    
    public AbstractEmbryoChannelInitializer(EmbryoNamespacesHub namespacesHub){
        super();
        this.namespacesHub = namespacesHub;
    }
    
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        scheduler.update(ctx);
    }
    
    @Override
	public void start(Configuration configuration, NamespacesHub namespacesHub) {
        this.configuration = configuration;
        boolean isSsl = configuration.getKeyStore() != null;
        if (isSsl) {
            try {
                sslContext = createSSLContext(configuration);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
    
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        addSslHandler(pipeline);
        addSocketioHandlers(pipeline);
    }

    /**
     * Adds the ssl handler
     *
     * @return
     */
    @Override
	protected void addSslHandler(ChannelPipeline pipeline) {
        if (sslContext != null) {
            SSLEngine engine = sslContext.createSSLEngine();
            engine.setUseClientMode(false);
            pipeline.addLast(SSL_HANDLER, new SslHandler(engine));
        }
    }
    
    /**
     * Adds the socketio channel handlers
     *
     * @param pipeline
     */
    @Override
	protected void addSocketioHandlers(ChannelPipeline p) {

        p.addLast(new ProtobufVarint32FrameDecoder());
        p.addLast(new ProtobufDecoder(getPrototype()));

        p.addLast(new ProtobufVarint32LengthFieldPrepender());
        p.addLast(new ProtobufEncoder());

        p.addLast(getInPacketHandler());
    }

    private SSLContext createSSLContext(Configuration configuration) throws Exception {
        TrustManager[] managers = null;
        if (configuration.getTrustStore() != null) {
            KeyStore ts = KeyStore.getInstance(configuration.getTrustStoreFormat());
            ts.load(configuration.getTrustStore(), configuration.getTrustStorePassword().toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);
            managers = tmf.getTrustManagers();
        }

        KeyStore ks = KeyStore.getInstance(configuration.getKeyStoreFormat());
        ks.load(configuration.getKeyStore(), configuration.getKeyStorePassword().toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, configuration.getKeyStorePassword().toCharArray());

        SSLContext serverContext = SSLContext.getInstance(configuration.getSSLProtocol());
        serverContext.init(kmf.getKeyManagers(), managers, null);
        return serverContext;
    }
    
    @Override
	public void stop() {
        StoreFactory factory = configuration.getStoreFactory();
        factory.shutdown();
        scheduler.shutdown();
        namespacesHub.shutdown();
    }
}
