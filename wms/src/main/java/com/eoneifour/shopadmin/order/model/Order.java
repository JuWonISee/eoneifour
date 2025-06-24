package com.eoneifour.shopadmin.order.model;

import java.sql.Date;

public class Order {
	private int orderId;
	private Date orderDate;
	private int totalPrice;
	private int status;
	private String userName;
	private String productName;
	private int quantity;
	private int price;
	private String deliveryAddress;
	private String deliveryAddressDetail;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryAddressDetail() {
		return deliveryAddressDetail;
	}

	public void setDeliveryAddressDetail(String deliveryAddressDetail) {
		this.deliveryAddressDetail = deliveryAddressDetail;
	}

	public String getStatusName() {
	    switch(status) {
	        case 0: return "배송준비";
	        case 1: return "배송 중";
	        case 2: return "배송완료";
	        case 3: return "주문 취소";
	        default: return "알수없음";
	    }
	}
}