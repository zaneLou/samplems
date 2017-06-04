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

import com.corundumstudio.socketio.Configuration;
import com.phn.proto.PhnNetBuf.PacketBuf;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PacketBufClientInitializer extends ChannelInitializer<SocketChannel> {

	private final Configuration configuration;
    private final SslContext sslCtx;
    protected PacketBufClientHandler packetBufClientHandler;
    
    public PacketBufClientInitializer(SslContext sslCtx, Configuration configuration) {
        this.sslCtx = sslCtx;
        this.configuration = configuration;
        packetBufClientHandler = new PacketBufClientHandler();
    }

    @Override
    public void initChannel(SocketChannel ch) {
    	log.info("PacketBufClientInitializer initChannel");
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc(), configuration.getHostname(), configuration.getPort()));
        }

        p.addLast(new ProtobufVarint32FrameDecoder());
        p.addLast(new ProtobufDecoder(PacketBuf.getDefaultInstance()));

        p.addLast(new ProtobufVarint32LengthFieldPrepender());
        p.addLast(new ProtobufEncoder());

        p.addLast(packetBufClientHandler);
    }

	public PacketBufClientHandler getPacketBufClientHandler() {
		return packetBufClientHandler;
	}
    
}
