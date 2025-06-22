package com.eoneifour.wms.iobound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.wms.iobound.model.InBoundOrder;

public class InBoundOrderDAO {
	DBManager db = DBManager.getInstance();

	public List<InBoundOrder> getOrderList() {
		String sql = "select * from purchase_order where status = 0";

		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			List<InBoundOrder> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				InBoundOrder inBoundOrder = new InBoundOrder();

//				inBoundOrder.setPurchase_order_id(rs.getInt("purchase_order_id"));
//				inBoundOrder.setQuantity(rs.getInt("quantity"));
//				inBoundOrder.setStatus(rs.getInt("status"));
//				inBoundOrder.setRequest_date(rs.getDate("requst_date"));
//				inBoundOrder.setComplete_date(rs.getDate("complete_date"));
//				inBoundOrder.setRequested_by(rs.getInt("requested_by"));
				inBoundOrder.setProduct_id(rs.getInt("product_id"));

				list.add(inBoundOrder);
			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("입고 목록 조회 중 오류 발생", e);
		} finally {
			db.release(pstmt, rs);
		}
	}
	
	public User getStockProduct(String keyWord) {
		
		String sql = "select * stock_product user where product_id = ?";
		
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			List<String > list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, keyWord);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("product_id"));
				
				return user;
			} else {
	            throw new UserException("해당 상품이 없습니다");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("조회 중 오류가 발생하였습니다..", e);
		} finally {
		    db.release(pstmt, rs);
		}
	}
	
	
}
