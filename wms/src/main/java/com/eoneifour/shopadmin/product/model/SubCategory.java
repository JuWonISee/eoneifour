package com.eoneifour.shopadmin.product.model;

public class SubCategory {
	private int sub_category_id;
	private String name;
	private TopCategory topcategory;
	
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
	public TopCategory getTopcategory() {
		return topcategory;
	}
	public void setTopcategory(TopCategory topcategory) {
		this.topcategory = topcategory;
	}
	public String toString() {
		return name;
	}
}
