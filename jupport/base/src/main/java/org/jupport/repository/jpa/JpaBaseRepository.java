package org.jupport.repository.jpa;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jupport.model.Entitied;
import org.jupport.repository.ReadRepository;
import org.jupport.repository.WriteRepository;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class JpaBaseRepository<T extends Entitied> implements ReadRepository<T>, WriteRepository<T>{

	public abstract EntityManager getEntityManager();
	
	protected Class<T> entityClass; 
	  
	/*
	 * Write
	 */
	@Override
	public synchronized void saveOrUpdate(T entity){
    	getEntityManager().getTransaction().begin();
    	if(entity.isNew()){
			getEntityManager().persist(entity);
		}else{
			getEntityManager().merge(entity);
		}
		getEntityManager().flush();
		getEntityManager().getTransaction().commit();
	}
	
	/*
	 * Read
	 */
	
	@Override
	public T getOneByUid(Object uid){
		return getEntityManager().find(entityClass, uid);
	}
	
	protected Query getQuery(final String queryString, final String[] paramNames, final Object[] values, int firstResult, int maxResult){
        Query query = getEntityManager().createQuery(queryString);
        for (int i = 0; i < values.length; i++) {
        	query.setParameter(paramNames[i], values[i]);
		}
        query.setFirstResult(firstResult);
        if(maxResult>0)
        	query.setMaxResults(maxResult);
        return query;
	}
	
	@Override
	public Long getCount(final String queryString){
		Query query = getEntityManager().createQuery(queryString);
        return (Long)query.getSingleResult();
	}
	
	@Override
	public Long getCountByNamedParam(final String queryString, final String paramName, Object value){
		Query query = getEntityManager().createQuery(queryString);
		query.setParameter(paramName, value);
        return (Long)query.getSingleResult();
	}
	
	@Override
	public Long getCountByNamedParams(final String queryString, final String[] paramNames, final Object[] values){
        if(paramNames.length!=values.length){
        	log.error("in getCount, length not equals. queryString {}", queryString);
        	return 0l;
        }
        Query query = getQuery(queryString, paramNames, values, 0, 0);
        return (Long)query.getSingleResult();
	}
	

	public Object getFirst(List result){
		if(result.size()>0)
			return result.get(0);
		else
			return null;
	}
	
	
	@Override
	public List findByNamedParam(final String queryString, final String paramName, final Object value){
		return findByNamedParam( queryString, paramName,  value, 0, -1);
	}
	
	@Override
	public List findByNamedParam(final String queryString, final String paramName, final Object value, int firstResult, int maxResult){
		Query query = getEntityManager().createQuery(queryString);
		if(StringUtils.hasLength(paramName))
			query.setParameter(paramName, value);
		//log.error("findByNamedParam, firstResult {} maxResult {}", firstResult, maxResult);
        query.setFirstResult(firstResult);
        if(maxResult>0)
        	query.setMaxResults(maxResult);
		return query.getResultList();
	}
	
	@Override
	public List findByNamedParams(final String queryString, final String[] paramNames, final Object[] values){
		return findByNamedParams(queryString, paramNames, values, 0, -1);
	}
	
	@Override
	public List findByNamedParams(final String queryString, final String[] paramNames, final Object[] values, int firstResult, int maxResult){
        if(paramNames.length!=values.length){
        	log.error("in findByNamedParam, length not equals. queryString {}", queryString);
        	return Collections.emptyList();
        }
        Query query = getQuery(queryString, paramNames, values, firstResult, maxResult);
        return query.getResultList();
	}
}
