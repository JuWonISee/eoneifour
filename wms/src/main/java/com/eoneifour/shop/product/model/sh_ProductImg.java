package com.eoneifour.shop.product.model;

public class sh_ProductImg {
	private int product_img_id;
	private String filename;
	private sh_Product product;
	public int getProduct_img_id() {
		return product_img_id;
	}
	public void setProduct_img_id(int product_img_id) {
		this.product_img_id = product_img_id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public sh_Product getProduct() {
		return product;
	}
	public void setProduct(sh_Product product) {
		this.product = product;
	}
}
