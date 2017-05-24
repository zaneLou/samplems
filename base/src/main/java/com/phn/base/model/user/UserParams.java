package com.phn.base.model.user;

import org.jupport.model.BaseParams;
import org.springframework.util.StringUtils;

public class UserParams extends BaseParams{

	protected String keyword;
	protected String role;
	
	//Params
	public String getParams()
	{
		String q = "";
		if(keyword!=null)
			q += "keyword="+keyword;
		if(StringUtils.hasLength(role))
			q += (StringUtils.hasLength(q)?"&":"")+"role="+role;
		return q;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}
