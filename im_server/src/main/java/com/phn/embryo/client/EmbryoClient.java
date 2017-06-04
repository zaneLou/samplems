/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.phn.embryo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;

import com.corundumstudio.socketio.Configuration;
import com.phn.proto.PhnNetBuf.PacketBuf;

/**
 * Sends a list of continent/city pairs to a {@link WorldClockServer} to get the
 * local times of the specified cities.
 */
@Slf4j
public final class EmbryoClient {

	private final Configuration configuration;
	protected EventLoopGroup group;
	protected Channel channel;
	protected PacketBufClientInitializer packetBufClientInitializer;
	

	public EmbryoClient(Configuration configuration) {
		this.configuration = configuration;
	}

	public void start(EmbryoClientReceiverListener receiverListener) {
		log.info("EmbryoClient will start");
		try {
			group = new NioEventLoopGroup();
			// Configure SSL.
			final SslContext sslCtx;
			sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
			Bootstrap b = new Bootstrap();
			packetBufClientInitializer = new PacketBufClientInitializer(sslCtx, configuration);
			packetBufClientInitializer.getPacketBufClientHandler().setReceiverListener(receiverListener);
			b.group(group).channel(NioSocketChannel.class).handler(packetBufClientInitializer);

			// Make a new connection.
			channel = b.connect(configuration.getHostname(), configuration.getPort()).sync().channel();
			log.info("EmbryoClient did start");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		packetBufClientInitializer.getPacketBufClientHandler().shutdown();
		try {
			channel.close().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		group.shutdownGracefully();
		log.info("EmbryoClient shutdown");
	}
	
	public void sendPacket(PacketBuf packetBuf){
		packetBufClientInitializer.getPacketBufClientHandler().sendPacket(packetBuf);
	}
	
	public boolean isOpen(){
		return packetBufClientInitializer.getPacketBufClientHandler().isOpen();
	}
}
