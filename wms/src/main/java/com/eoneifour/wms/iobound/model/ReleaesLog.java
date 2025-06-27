package com.eoneifour.wms.iobound.model;

import java.time.LocalDateTime;

public class ReleaesLog {
	private int release_log_id;
	private int product_id;
	private String product_name;
	private String product_brand;
	private int s;
	private int z;
	private int x;
	private int y;
	private LocalDateTime release_date;
	private String detail;
	
	public int getRelease_log_id() {
		return release_log_id;
	}
	public void setRelease_log_id(int release_log_id) {
		this.release_log_id = release_log_id;
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
	public LocalDateTime getRelease_date() {
		return release_date;
	}
	public void setRelease_date(LocalDateTime release_date) {
		this.release_date = release_date;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}
