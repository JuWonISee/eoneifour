package com.eoneifour.wms.iohistory.model;

import java.sql.Date;

public class IoHistory {
	private int stock_log_id;
	private int product_id;
	private String product_name;
	private String product_brand;
	private int s;
	private int z;
	private int x;
	private int y;
	private Date stock_date;
	private Date release_date;

	public Date getRelease_date() {
		return release_date;
	}

	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}

	public int getStock_log_id() {
		return stock_log_id;
	}

	public void setStock_log_id(int stock_log_id) {
		this.stock_log_id = stock_log_id;
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

	public Date getStock_date() {
		return stock_date;
	}

	public void setStock_date(Date stock_date) {
		this.stock_date = stock_date;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	private String detail;

}
