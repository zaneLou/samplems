package org.jupport.sample.model;

import org.jupport.model.BaseEntity;

public class FileInfo extends BaseEntity<Integer>{
    private static final long serialVersionUID = -6784958377467124469L;
    private Integer uid;
    protected int category;
	protected int targetId;
	protected String path;
	protected String otherPaths;
	protected int order;
	
	public FileInfo(){ }
	
	public FileInfo(int category, int targetId, String path, int order){
		this.category = category;
		this.targetId = targetId;
		this.path = path;
		this.order = order;
	}
	
	//File Category
	public static int Image_Product = 0;
	public static int Image_ProductMain = 1;
	public static int Image_ProductDesc = 2;
	
	public static int Image_Store_Logo = 10;
	public static int Image_Store_Qualification = 11;
	public static int Image_Store_Scene = 12;
	
	public static int Image_ActivityBanner = 20;
	public static int Image_ActivityItem = 21;
	
	public static int Image_Kind_Icon = 30;
	
	public static int Image_Category_Icon = 40;
	/*
	 * Properties
	 */
	
	public int getCategory() {
		return category;
	}
	@Override
	public Integer getUid() {
		return uid;
	}

	@Override
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public void setCategory(int category) {
		this.category = category;
	}
	public int getTargetId() {
		return targetId;
	}
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

	public String getOtherPaths() {
		return otherPaths;
	}

	public void setOtherPaths(String otherPaths) {
		this.otherPaths = otherPaths;
	}
	
}

