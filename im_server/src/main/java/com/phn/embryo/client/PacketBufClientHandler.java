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

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import com.phn.proto.PhnNetBuf.PacketBuf;

@Slf4j
public class PacketBufClientHandler extends SimpleChannelInboundHandler<PacketBuf> {

	private EmbryoClientReceiverListener receiverListener;
	
	// Stateful properties
	private volatile Channel channel;
	private ChannelFuture lastWriteFuture ;
	
	public PacketBufClientHandler() {
		super(false);
	}

	public boolean isActive(){
		return channel.isActive();
	}
	
	public boolean isOpen(){
		return channel.isOpen();
	}
	
	public void sendPacket(PacketBuf packetBuf){
		log.info("PacketBufClientHandler sendPacket");
		lastWriteFuture = channel.writeAndFlush(packetBuf);
		lastWriteFuture.addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                	log.error("client write failed: ");
                    future.cause().printStackTrace(System.err);
                }
            }
        });
	}
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		channel = ctx.channel();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, PacketBuf packetBuf) throws Exception {
		log.info("read packetBuf:{}from server", packetBuf);
		if(receiverListener!=null)
			receiverListener.onReceiver(packetBuf);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	public void setReceiverListener(EmbryoClientReceiverListener receiverListener) {
		this.receiverListener = receiverListener;
	}
	
	public void shutdown() {
		 // Wait until all messages are flushed before closing the channel.
        if (lastWriteFuture != null) {
            try {
				lastWriteFuture.sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}
	
}
