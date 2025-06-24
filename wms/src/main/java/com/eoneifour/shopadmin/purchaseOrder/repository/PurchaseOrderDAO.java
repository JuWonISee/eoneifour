package com.eoneifour.shopadmin.purchaseOrder.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.purchaseOrder.model.PurchaseOrder;

public class PurchaseOrderDAO {
	DBManager dbManager = DBManager.getInstance();
	
	public void insertOrder(int productId, int quantity) throws UserException {
	    Connection con = null;
	    PreparedStatement pstmt = null;

	    try {
	        con = dbManager.getConnection();

	        StringBuffer sql = new StringBuffer();
	        sql.append("INSERT INTO shop_purchase_order(quantity, status, complete_date, requested_by, product_id) ");
	        sql.append("VALUES (?, ?, ?, ?, ?)");

	        PurchaseOrder purchaseOrder = new PurchaseOrder();
	        purchaseOrder.setQuantity(quantity);
	        purchaseOrder.setStatus("창고도착"); // 발주 상태는 "창고도착" 하드코딩 (발주완료 , 창고도착 , 입고완료)
	        purchaseOrder.setComplete_date(new java.sql.Date(System.currentTimeMillis()));
	        purchaseOrder.setRequested_by(1); //주문자는 로그인 정보에서 얻어올 로직 필요. 현재 1로 하드코딩
	        purchaseOrder.setProduct_id(productId);

	        pstmt = con.prepareStatement(sql.toString());
	        pstmt.setInt(1, purchaseOrder.getQuantity());
	        pstmt.setString(2, purchaseOrder.getStatus());
	        pstmt.setDate(3, purchaseOrder.getComplete_date());
	        pstmt.setInt(4, purchaseOrder.getRequested_by());
	        pstmt.setInt(5, purchaseOrder.getProduct_id());

	        int result = pstmt.executeUpdate();
	        if (result == 0) {
	            throw new UserException("발주 신청에 실패했습니다");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new UserException("발주 신청 중 오류가 발생하였습니다.", e);
	    } finally {
	        dbManager.release(pstmt);
	    }
	}
	
	
	
	
}
