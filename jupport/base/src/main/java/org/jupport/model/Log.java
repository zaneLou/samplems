package org.jupport.model;

import java.util.Date;

public class Log {

	protected int 		type;
	protected Date 		createdDate;
	protected String	content;
	protected int 		level;
	
	/*
	 * Properties
	 */
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
