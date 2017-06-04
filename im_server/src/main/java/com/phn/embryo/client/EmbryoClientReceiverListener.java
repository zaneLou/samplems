package com.phn.embryo.client;

import com.phn.proto.PhnNetBuf.PacketBuf;

public interface EmbryoClientReceiverListener {

	void onReceiver(PacketBuf packetBuf);
}
