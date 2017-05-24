package com.phn.base.repository;

import java.util.List;

import org.jupport.repository.ReadRepository;
import org.jupport.repository.WriteRepository;

import com.phn.base.model.user.User;
import com.phn.base.model.user.UserParams;

public interface UserRepository<T extends User> extends ReadRepository<T>, WriteRepository<T>{
	public User findUserByLowcaseUsername(String username);
	public User findUserByWeiXinOpenId(String weiXinOpenId);
	public User findUserByTelephone(String telephone);
	public User findUserBySourceTypeAndId(int sourceType, String sourceId);
	public void setQuery(UserParams userParams);
	public long getUsersCount(UserParams userParams);
	public List getUsers(final String queryString, final String[] paramNames, final Object[] values, int firstResult, int maxResult);

}
