package com.phn.base.component;

import org.springframework.dao.DataAccessException;

import com.phn.base.model.user.User;
import com.phn.base.repository.UserRepository;

public interface UserStore {

	public UserRepository<User> getUserRepository() ;
	public void saveUser(User user, boolean cached) throws DataAccessException ;
	public User getUserAndCreateIfNotExists(String openId, String nickname, User referee);
	public User findUserByLowcaseUsername(String username, boolean cached);
	public void increLoginFail(String username);
	public boolean hasReachLoginFailLimit(String username);
	public String getUserToken(long userId,  String device);
	public String updateUserToken(User user, String device, boolean cached) ;
}
