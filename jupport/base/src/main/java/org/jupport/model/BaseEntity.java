package org.jupport.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity<T>  implements Serializable, Entitied<T> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public boolean isNew() 
	{
		return (getUid() == null);
	}
	
	/* Overriden Methods */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BaseEntity) {
			return ((BaseEntity) obj).getUid().equals(this.getUid());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getUid()!=null?getUid().hashCode():super.hashCode();
	}

}
