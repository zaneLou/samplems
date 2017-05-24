package org.jupport.model;

public class BaseParams {

	protected boolean hasQuery;
	protected String queryString;
	protected String[] paramNames;
	protected Object[] values;
	
	/*
	 * Properties
	 */
	
	public String getQueryString() {
		return queryString;
	}
	public boolean isHasQuery() {
		return hasQuery;
	}
	public void setHasQuery(boolean hasQuery) {
		this.hasQuery = hasQuery;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public String[] getParamNames() {
		return paramNames;
	}
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	public Object[] getValues() {
		return values;
	}
	public void setValues(Object[] values) {
		this.values = values;
	}
}
