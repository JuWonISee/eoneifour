package com.eoneifour.shopadmin.product.model;

public class Product {
	private int product_id;
	private String name;
	private String brand_name;
	private int price;
	private int stock_quantity;
	private int status;
	private String detail;
	private SubCategory sub_category; //ERD상에서는 자식이 부모의 pk숫자를 보유하지만 , java에서는 부모를 객체형으로 보유해야함.
	private TopCategory top_category;
	
	public Product() {}
	
	public Product(int productId, String name, String brand_name, int price, String detail, int stock_quantity, int status, SubCategory sub_category) {
		this.product_id = productId;
		this.name = name;
		this.brand_name = brand_name;
		this.price = price;
		this.detail = detail;
		this.stock_quantity = stock_quantity;
		this.status = status;
		this.sub_category = sub_category;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getStock_quantity() {
		return stock_quantity;
	}
	public void setStock_quantity(int stock_quantity) {
		this.stock_quantity = stock_quantity;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public SubCategory getSub_category() {
		return sub_category;
	}
	public void setSub_category(SubCategory sub_category) {
		this.sub_category = sub_category;
	}
	public TopCategory getTop_category() {
		return top_category;
	}
	public void setTop_category(TopCategory top_category) {
		this.top_category = top_category;
	}

	
}
