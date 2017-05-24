package com.phn.base.component;

import com.phn.base.model.user.User;

public interface PhnAuthenticator {

	public void updatehashPassword(User user, String plain);
	public boolean authent(String plain, User user);
	public String hash(String plain);
}
