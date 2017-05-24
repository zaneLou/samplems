package com.phn.socketio.manager;

import com.phn.proto.PhnNetBuf.PhnDataBuf;
import com.phn.proto.PhnNetBuf.PhnLoginBuf;

public interface UserManager {

    public boolean hadProcessData(String sessionId, String dataId);
	public PhnDataBuf validate(String device, PhnLoginBuf loginBuf);
}
