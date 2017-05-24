package org.jupport.sample.model;

import org.jupport.model.BaseEntity;


public class Store extends BaseEntity<Integer>{
	
	//药店信息包括：药店logo、药店名称、药店地址、药店电话。
	//显示药房简介内容
	//显示药房的资质图片
	//药店实景照片
	
    private static final long serialVersionUID = -1442295427144179852L;
    private Integer uid;
    //protected User owner;
	protected String areaName;
	protected String name;
	protected FileInfo logo;
	protected String address;
	protected String phone;
	protected String introduction;
	//protected Region region;
	
	protected double longitude;
	protected double latitude;
	protected int status = 0;
	
	protected String phonesForNotification;
	//protected List<ExpenseItem> expenseItems;
	
	/*
	 * Properties
	 */
	
	
	public String getPhonesForNotification() {
		return phonesForNotification;
	}
	@Override
	public Integer getUid() {
		return uid;
	}
	@Override
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public void setPhonesForNotification(String phonesForNotification) {
		this.phonesForNotification = phonesForNotification;
	}
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FileInfo getLogo() {
		return logo;
	}
	public void setLogo(FileInfo logo) {
		this.logo = logo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
