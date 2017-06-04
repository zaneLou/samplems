package com.phn.embryo.listener;

import com.google.protobuf.MessageLite;
import com.phn.embryo.AbstractEmbryoTunnel;

public interface EmbryoReceiverListener {
	void onReceiver(AbstractEmbryoTunnel client, MessageLite packet);
}
