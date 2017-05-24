/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phn.base.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.jupport.model.Entitied;
import org.pac4j.core.profile.CommonProfile;


/**
 * Simple JavaBean domain object representing an person.
 *
 * @author Ken Krebs
 */
@MappedSuperclass
public class Account extends CommonProfile implements Entitied<Long>{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long uid;
    
    @Column(name = "username")
    protected String username;

    @Column(name = "password")
    protected String password;
    
    @Column(name = "roles_data")
    protected String rolesData;
    
    public static String Role_Admin = "admin";
    public static String Role_Custmer = "custmer";
    
	public String getRolesData() {
		return rolesData;
	}

	public void setRolesData(String myRoles) {
		this.rolesData = myRoles;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	//Entitied
	@Override
    public boolean isNew() {
        return (this.uid == null);
    }
    
	@Override
	public Long getUid() {
		return uid;
	}

	@Override
	public void setUid(Long uid) {
		this.uid = uid;
	}

	/*
	 * Override UserProfile
	 */
	@Override
	public String getId() {
		return this.username;
	}
    
	public void populateProfile()
	{
		 String[] roles = rolesData.split(",");
		 for (int i = 0; i < roles.length; i++) {
			String role = roles[i];
			addRole(role);
		}
	}

}
