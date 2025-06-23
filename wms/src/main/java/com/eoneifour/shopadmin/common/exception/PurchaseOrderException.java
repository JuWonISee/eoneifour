package com.eoneifour.shopadmin.common.exception;

public class PurchaseOrderException extends RuntimeException{
	public PurchaseOrderException(String msg) {
		super(msg);
	}
	//예외 객체들의 최상위 객체가 바로 Throwable 인터페이스.
	public PurchaseOrderException(Throwable e) {
		super(e);
	}
	
	public PurchaseOrderException(String msg , Throwable e) {
		super(msg , e);
	}
}
