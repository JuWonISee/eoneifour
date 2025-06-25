package com.eoneifour.wms.iobound.model;

import java.sql.Date;

import com.eoneifour.shopadmin.product.model.Product;

/*
발주 테이블
테이블명 : purchase_order
컬럼종류 : 수량, 상태, 요청일, 완료일, 발주상태?, 상품 ID
*/ 

public class OutBoundOrder {
	
	private int stock_product_id;
	private int product_id;
	private String product_name;
	private String product_brand;
	private int s;
	private int z;
	private int x;
	private int y;
	private int stock_status;
	private String detail;
	public int getStock_product_id() {
		return stock_product_id;
	}
	public void setStock_product_id(int stock_product_id) {
		this.stock_product_id = stock_product_id;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_brand() {
		return product_brand;
	}
	public void setProduct_brand(String product_brand) {
		this.product_brand = product_brand;
	}
	public int getS() {
		return s;
	}
	public void setS(int s) {
		this.s = s;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getStock_status() {
		return stock_status;
	}
	public void setStock_status(int stock_status) {
		this.stock_status = stock_status;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}

