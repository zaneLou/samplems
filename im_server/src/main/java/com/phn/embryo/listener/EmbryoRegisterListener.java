package com.phn.embryo.listener;

import com.phn.embryo.AbstractEmbryoTunnel;
import com.google.protobuf.MessageLite;

public interface EmbryoRegisterListener {

    void onRegister(AbstractEmbryoTunnel client, MessageLite packet);
}
