package com.eoneifour.wms.iobound.model;

import java.sql.Date;

import com.eoneifour.shopadmin.product.model.Product;

/*
발주 테이블
테이블명 : purchase_order
컬럼종류 : 수량, 상태, 요청일, 완료일, 발주상태?, 상품 ID
*/ 

public class InBoundOrder {
	
	private int purchase_order_id;
	private int quantity;
	private int status;
	private Date request_date;
	private Date complete_date;
	private int  requested_by;
	private int product_id;
	private Product product;
	
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
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
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}

