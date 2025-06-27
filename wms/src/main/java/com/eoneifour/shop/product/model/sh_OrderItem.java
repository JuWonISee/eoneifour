package com.eoneifour.shop.product.model;

public class sh_OrderItem {
	private int order_item_id;
	private int quantity;
	private int price;
	private int orders_id;
	private int product_id;  
	private sh_Product product;
	private sh_Orders order;
	
	public int getOrder_item_id() {
		return order_item_id;
	}
	public void setOrder_item_id(int order_item_id) {
		this.order_item_id = order_item_id;
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
	public int getOrders_id() {
		return orders_id;
	}
	public void setOrders_id(int orders_id) {
		this.orders_id = orders_id;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public sh_Product getProduct() {
		return product;
	}
	public void setProduct(sh_Product product) {
		this.product = product;
	}
	public sh_Orders getOrder() {
		return order;
	}
	public void setOrder(sh_Orders order) {
		this.order = order;
	}
}
