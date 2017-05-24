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
package com.phn.embryo.sample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

import com.phn.proto.PhnNetBuf.PacketBuf;

@Slf4j
public class PacketBufClientHandler extends SimpleChannelInboundHandler<PacketBuf> {

	// Stateful properties
	private volatile Channel channel;

	public static AtomicInteger onRecieveCount = new AtomicInteger(0);
	
	public PacketBufClientHandler() {
		super(false);
	}

	public void sendPacket(int index){
		log.info("PacketBufClientHandler sendPacket");
		PacketBuf.Builder builer = PacketBuf.newBuilder();
		channel.writeAndFlush(builer.setPacketId(index + "").setPath("paht").build());
	}
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		channel = ctx.channel();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, PacketBuf packetBuf) throws Exception {
		log.info("read packetBuf:{} from server, onRecieveCount {}", packetBuf, onRecieveCount.incrementAndGet());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
