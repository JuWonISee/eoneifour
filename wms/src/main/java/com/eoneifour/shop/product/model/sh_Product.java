package com.eoneifour.shop.product.model;

public class sh_Product {
	private int product_id;
	private String name;
	private String brand_name;
	private int price;
	private int stock_quantity;
	private int status;
	private String detail;
	private sh_SubCategory sub_category; 
	private sh_TopCategory top_category;
	
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
	public sh_SubCategory getSub_category() {
		return sub_category;
	}
	public void setSub_category(sh_SubCategory sub_category) {
		this.sub_category = sub_category;
	}
	public sh_TopCategory getTop_category() {
		return top_category;
	}
	public void setTop_category(sh_TopCategory top_category) {
		this.top_category = top_category;
	}
	
	
}
