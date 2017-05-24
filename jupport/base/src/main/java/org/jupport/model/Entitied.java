package org.jupport.model;

public interface Entitied<T> {

	public void setUid(T uid);

	public T getUid();

	public boolean isNew();
	
}