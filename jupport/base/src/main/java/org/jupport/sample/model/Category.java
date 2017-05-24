package org.jupport.sample.model;

import org.jupport.model.BaseEntity;

public class Category extends BaseEntity<Integer>{
    private static final long serialVersionUID = -6388827678144290288L;
	private Integer uid;
	protected String name;
	protected String description;
	protected String iconUrl;
	
	/*
	 * Properties
	 */
    @Override
	public Integer getUid() {
		return uid;
	}

	@Override
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	
}
