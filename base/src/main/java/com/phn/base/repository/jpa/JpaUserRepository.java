package com.phn.base.repository.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import org.jupport.repository.jpa.JpaBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.phn.base.model.user.User;
import com.phn.base.model.user.UserParams;
import com.phn.base.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class JpaUserRepository extends JpaBaseRepository implements UserRepository{
	
	@PostConstruct
	public void postConstruct() {
		entityClass = com.phn.base.model.user.User.class;
		log.info("JpaUserRepository  postConstruct {}", getOneByUid(1l));
	}
	
//    @PersistenceContext(name="phnEntityManagerFactory")
//    private EntityManager phnEntityManager;

    @Autowired
	@Qualifier("phnEntityManager")
    private EntityManager phnEntityManager;
   
    @Override
    public EntityManager getEntityManager(){
    	return phnEntityManager;
    }
    
	@Override
	@Transactional(readOnly = true)
	public User findUserByLowcaseUsername(String username) {
		List<User> result = findByNamedParam("from User where lower(username) = :username", "username", username.toLowerCase());
		if(result.size()>0)
			return result.get(0);
		else
			return null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public User findUserByWeiXinOpenId(String wxOpenId) {
		List<User> result = findByNamedParam("from User where wxOpenId = :wxOpenId", "wxOpenId", wxOpenId);
		if(result.size()>0)
			return result.get(0);
		else
			return null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public User findUserByTelephone(String telephone) {
		List<User> result = findByNamedParam("from User where telephone = :telephone", "telephone", telephone);
		if(result.size()>0)
			return result.get(0);
		else
			return null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public User findUserBySourceTypeAndId(int sourceType, String sourceId) {
		return (User) getFirst(findByNamedParams("from User where sourceType = :sourceType and sourceId = :sourceId",
				new String[] { "sourceType", "sourceId" }, new Object[] { sourceType, sourceId }));
	}
	
	// Object getFirst(List result)
	 
	@Override
	public void setQuery(UserParams userParams)
	{
		String query = "";
		List<String> paramNames = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		
		boolean has = false;
		query = " from User u ";
		if(StringUtils.hasLength(userParams.getRole()))
		{
			query += (has?" and ":" where ") + " u.rolesData = :role ";
			paramNames.add("role");
			
			values.add(userParams.getRole());
			has = true;
		}
		if(StringUtils.hasLength(userParams.getKeyword()))
		{
			query += (has?" and ":" where ") + " u.realname like :realname ";
			paramNames.add("realname");
			
			values.add("%"+userParams.getKeyword()+"%");
			has = true;
		}
	
		query += " order by u.createDate desc ";
		
		userParams.setParamNames(paramNames.toArray(new String[paramNames.size()]));
		userParams.setValues(values.toArray());
		userParams.setQueryString(query);
	}
	
	@Override
	@Transactional(readOnly = true)
	public long getUsersCount(UserParams userParams)
	{
		String queryString = "select count(*) " + userParams.getQueryString();
		return getCountByNamedParams(queryString, userParams.getParamNames(), userParams.getValues()).longValue();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List getUsers(final String queryString, final String[] paramNames, final Object[] values, int firstResult, int maxResult)
	{
		return findByNamedParams(queryString, paramNames, values, firstResult, maxResult);
	}
	
	
}
