package com.eoneifour.wms.monitoring.model;

public class Stacker {
	private int stacker_id;
	private int z;
	private int x;
	private int y;
	private int status;
	private int pre_fork;
	private int on_product;
	private int current_z;
	private int current_x;
	
	public int getStacker_id() {
		return stacker_id;
	}
	public void setStacker_id(int stacker_id) {
		this.stacker_id = stacker_id;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPre_fork() {
		return pre_fork;
	}
	public void setPre_fork(int pre_fork) {
		this.pre_fork = pre_fork;
	}
	public int getOn_product() {
		return on_product;
	}
	public void setOn_product(int on_product) {
		this.on_product = on_product;
	}
	public int getCurrent_z() {
		return current_z;
	}
	public void setCurrent_z(int current_z) {
		this.current_z = current_z;
	}
	public int getCurrent_x() {
		return current_x;
	}
	public void setCurrent_x(int current_x) {
		this.current_x = current_x;
	}
	
	
	}
