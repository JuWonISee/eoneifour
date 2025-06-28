package com.eoneifour.wms.iobound.model;

import java.sql.Timestamp;

public class StockProduct {
	private int stockProductId;
	private int productId;
	private String productName;
	private String productBrand;
	private int s;
	private int z;
	private int x;
	private int y;
	private int stockStatus;
	private String detail;
	private Timestamp stock_time;

	public int getStockProductId() {
	    return stockProductId;
	}

	public void setStockProductId(int stockProductId) {
	    this.stockProductId = stockProductId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
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

	public int getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(int stockStatus) {
		this.stockStatus = stockStatus;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Timestamp getStock_time() {
		return stock_time;
	}

	public void setStock_time(Timestamp stock_time) {
		this.stock_time = stock_time;
	}
}
