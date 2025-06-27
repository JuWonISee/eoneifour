package com.eoneifour.shop.product.model;

import java.sql.Date;

public class sh_Orders {
	private int orders_id;
	private Date order_date;
	private int total_price;
	private int status;
	private int user_id;
	private String delivery_address;
	private String delivery_address_detail;
	
	public int getOrders_id() {
		return orders_id;
	}
	public void setOrders_id(int orders_id) {
		this.orders_id = orders_id;
	}
	public Date getOrder_date() {
		return order_date;
	}
	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}
	public int getTotal_price() {
		return total_price;
	}
	public void setTotal_price(int total_price) {
		this.total_price = total_price;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getDelivery_address() {
		return delivery_address;
	}
	public void setDelivery_address(String delivery_address) {
		this.delivery_address = delivery_address;
	}
	public String getDelivery_address_detail() {
		return delivery_address_detail;
	}
	public void setDelivery_address_detail(String delivery_address_detail) {
		this.delivery_address_detail = delivery_address_detail;
	}
	
	
}
