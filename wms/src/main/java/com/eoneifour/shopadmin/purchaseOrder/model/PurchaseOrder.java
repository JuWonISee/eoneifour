package com.eoneifour.shopadmin.purchaseOrder.model;

import java.sql.Date;

import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.user.model.User;

public class PurchaseOrder {
	private int purchase_order_id;
	private int quantity;
	private String status;
	private Date request_date;
	private Date complete_date;
	private int requested_by;
	private int product_id;
	private int reflect;
	private Product product;
	private User user;
	private String detail;

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getPurchase_order_id() {
		return purchase_order_id;
	}

	public void setPurchase_order_id(int purchase_order_id) {
		this.purchase_order_id = purchase_order_id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getRequest_date() {
		return request_date;
	}

	public void setRequest_date(Date request_date) {
		this.request_date = request_date;
	}

	public Date getComplete_date() {
		return complete_date;
	}

	public void setComplete_date(Date complete_date) {
		this.complete_date = complete_date;
	}

	public int getRequested_by() {
		return requested_by;
	}

	public void setRequested_by(int requested_by) {
		this.requested_by = requested_by;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getReflect() {
		return reflect;
	}

	public void setReflect(int reflect) {
		this.reflect = reflect;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
