package com.eoneifour.shopadmin.user.model;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class User {
	private int userId;
    private String email;
    private String password;
    private String name;
    private String address;
    private String addressDetail;
    private int role;
    private int status;
    private Date createdAt;
    
    public User() {}
    
    public User(int userId, String email, String password, String name, String address, String addressDetail, int role, int status, Date createdAt) {
	    this.userId = userId;
	    this.email = email;
	    this.password = password;
	    this.name = name;
	    this.address = address;
	    this.addressDetail = addressDetail;
	    this.role = role;
	    this.status = status;
	    this.createdAt = createdAt;
	}
    
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddressDetail() {
		return addressDetail;
	}
	
	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}
	
	public int getRole() {
		return role;
	}
	
	public void setRole(int role) {
		this.role = role;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
