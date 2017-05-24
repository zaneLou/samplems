package org.jupport.repository;

import java.util.List;

import org.jupport.model.Entitied;


public interface ReadRepository<T extends Entitied> {
	public T getOneByUid(Object uid);
	public Long getCount(final String queryString);
	public Long getCountByNamedParam(final String queryString, final String paramName, Object value);
	public Long getCountByNamedParams(final String queryString, final String[] paramNames, final Object[] values);
	public List findByNamedParam(final String queryString, final String paramName, final Object value);
	public List findByNamedParam(final String queryString, final String paramName, final Object value, int firstResult, int maxResult);
	public List findByNamedParams(final String queryString, final String[] paramNames, final Object[] values);
	public List findByNamedParams(final String queryString, final String[] paramNames, final Object[] values, int firstResult, int maxResult);
}
