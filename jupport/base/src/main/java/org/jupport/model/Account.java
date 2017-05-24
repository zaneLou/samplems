package org.jupport.model;

import java.util.Set;

public interface Account {

	public Long getUid() ;
	public String getPassword();
	public Set<String> getRoles() ;
	public Set<String> getStringPermissions();

}
