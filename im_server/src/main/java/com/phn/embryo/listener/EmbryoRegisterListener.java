package com.phn.embryo.listener;

import com.phn.embryo.EmbryoTunnel;
import com.phn.embryo.EmbryoPacket;

public interface EmbryoRegisterListener {

    void onRegister(EmbryoTunnel client, EmbryoPacket packet);
}
