package org.jupport.repository;

import org.jupport.model.Entitied;

public interface WriteRepository <T extends Entitied> {
	public void saveOrUpdate(T entity);

}
