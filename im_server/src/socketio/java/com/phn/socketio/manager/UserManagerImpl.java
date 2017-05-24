package com.phn.socketio.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;
import com.phn.base.component.DataStore;
import com.phn.base.component.PhnAuthenticator;
import com.phn.base.component.PhnConstants;
import com.phn.base.component.UserStore;
import com.phn.base.model.user.User;
import com.phn.proto.PhnNetBuf.PhnDataBuf;
import com.phn.proto.PhnNetBuf.PhnLoginBuf;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserManagerImpl extends AbstractManager implements UserManager {

	@Autowired
	protected UserStore userStore;
	
	@Autowired
    protected  DataStore dataStore;

	@Autowired
	protected PhnAuthenticator authenticator;

	@Override
	public PhnAuthenticator getAuthenticator() {
		return authenticator;
	}

    @Override
    public boolean hadProcessData(String sessionId, String dataId) {
        return dataStore.hadCacheSessionData(sessionId, dataId);
    }
    
    //test 1. user null 2. login fail limit 3. wrong password
	@Override
	public PhnDataBuf validate(String device, PhnLoginBuf loginBuf) {
		User user = userStore.findUserByLowcaseUsername(loginBuf.getUserName(), true);
		if (user == null) {
			log.error("Username : '" + loginBuf.getUserName() + "' does not found");
			return getPhnDataBuf(PhnConstants.SocketIO_Data_Type_LoginByUsername, PhnConstants.Response_LoginWrongPwd, null);
		} else if(userStore.hasReachLoginFailLimit(loginBuf.getUserName())){
		    log.error("Username : '" + loginBuf.getUserName() + "' reach login fail limit");
            return getPhnDataBuf(PhnConstants.SocketIO_Data_Type_LoginByUsername, PhnConstants.Response_LoginWrongPwd, null);
		} else if (!authenticator.authent(loginBuf.getPassword(), user)) {
			log.error("Username : '" + loginBuf.getUserName() + "' does not match password");
			userStore.increLoginFail(loginBuf.getUserName());
			return getPhnDataBuf(PhnConstants.SocketIO_Data_Type_LoginByUsername, PhnConstants.Response_LoginWrongPwd, null);
		}
		log.info("validate Username : {}  successfully" , loginBuf.getUserName());
		// updateUserToken
		String token = userStore.updateUserToken(user, device, true);
		return getPhnDataBuf(PhnConstants.SocketIO_Data_Type_LoginByUsername, PhnConstants.Response_Success, ByteString.copyFrom(token.getBytes()));
	}


}
