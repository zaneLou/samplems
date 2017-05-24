package com.phn.base.component;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.phn.base.model.user.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BcryptAuthenticator implements PhnAuthenticator{

	private String passwordPrefix = "0:";

	@Override
	public void updatehashPassword(User user, String plain){
		String salt = BCrypt.gensalt();
		String hashed = BCrypt.hashpw(plain, salt);
		user.setPassword(passwordPrefix+hashed);
	}
	
	@Override
	public boolean authent(String plain, User user){
		if(!StringUtils.hasLength(plain) ||  !StringUtils.hasLength(user.getPassword()))
			return false;
		String password = user.getPassword().substring(passwordPrefix.length());
		return BCrypt.checkpw(plain, password);
	}
	
	public static void main(String[] args) {
		String plain = "111222";
		User user = new User();
		BcryptAuthenticator authenticator = new BcryptAuthenticator();
		authenticator.updatehashPassword( user,  plain);
		log.info("authent {}", authenticator.authent(plain, user));
		log.info("password {}", user.getPassword());
	}
	
	@Override
	public String hash(String plain){
		String salt = BCrypt.gensalt();
		return BCrypt.hashpw(plain, salt);
	}

}
