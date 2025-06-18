package com.eoneifour.shopadmin.product.model;

public class SubCategory {
	private int sub_category_id;
	private String name;
	private TopCategory top_category;
	public int getSub_category_id() {
		return sub_category_id;
	}
	public void setSub_category_id(int sub_category_id) {
		this.sub_category_id = sub_category_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TopCategory getTop_category() {
		return top_category;
	}
	public void setTop_category(TopCategory top_category) {
		this.top_category = top_category;
	}
	

}
