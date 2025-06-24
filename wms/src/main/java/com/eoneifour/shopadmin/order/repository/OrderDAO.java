package com.eoneifour.shopadmin.order.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.order.model.Order;
import com.eoneifour.shopadmin.user.model.User;

public class OrderDAO {
	DBManager db = DBManager.getInstance();
	
	public List<Order> getOrderList(boolean unreflected) {
		StringBuffer sql = new StringBuffer();
		sql.append("select o.orders_id, o.order_date, o.total_price, o.status, u.name as user_name, p.name as product_name, oi.quantity, oi.price ");
		sql.append("from shop_orders o join shop_user u on o.user_id = u.user_id join shop_order_item oi on o.orders_id = oi.orders_id join shop_product p on oi.product_id = p.product_id ");
		if (unreflected) { 
			sql.append("join shop_release_order ro on o.orders_id = ro.order_id ");
			sql.append("where ro.reflect = 0 ");
		}
		sql.append(" order by o.orders_id desc");

		Connection conn = db.getConnection();
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			List<Order> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Order order = new Order();
				order.setOrderId(rs.getInt("orders_id"));
				order.setOrderDate(rs.getDate("order_date"));
				order.setUserName(rs.getString("user_name"));
				order.setProductName(rs.getString("product_name"));
				order.setQuantity(rs.getInt("quantity"));
				order.setPrice(rs.getInt("price"));
				order.setTotalPrice(rs.getInt("total_price"));
				order.setStatus(rs.getInt("status"));
				list.add(order);
			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("주문 목록 조회 중 오류 발생", e);
		} finally {
			db.release(pstmt, rs);
		}
	}
	
	// orderId 기준으로 주문 1건 조회
	public Order getOrderById(int orderId) {
		String sql = 
		    "SELECT o.orders_id, o.total_price, o.status, o.delivery_address, o.delivery_address_detail, " +
		    "u.name AS user_name, p.name AS product_name, oi.quantity " +
		    "FROM shop_orders o " +
		    "JOIN shop_user u ON o.user_id = u.user_id " +
		    "JOIN shop_order_item oi ON o.orders_id = oi.orders_id " +
		    "JOIN shop_product p ON oi.product_id = p.product_id " +
		    "WHERE o.orders_id = ?";
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, orderId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				Order order = new Order();
				order.setOrderId(rs.getInt("orders_id"));
				order.setUserName(rs.getString("user_name"));
				order.setProductName(rs.getString("product_name"));
				order.setQuantity(rs.getInt("quantity"));
				order.setTotalPrice(rs.getInt("total_price"));
				order.setStatus(rs.getInt("status"));
				
				order.setDeliveryAddress(rs.getString("delivery_address"));
				order.setDeliveryAddressDetail(rs.getString("delivery_address_detail"));
				
				return order;
			} else {
	            throw new UserException("해당 주문이 존재하지 않습니다.");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("주문 조회 중 오류 발생가 발생했습니다.", e);
		} finally {
		    db.release(pstmt, rs);
		}
	}
	
	// 주문 수정
	public void updateOrder(int orderId, String address, String addressDetail) throws UserException {
		StringBuffer sql = new StringBuffer();
		
		sql.append("update shop_orders ");
		sql.append("set delivery_address = ?, delivery_address_detail= ? ");
		sql.append("where orders_id = ?");
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null; 
		
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, address);
			pstmt.setString(2, addressDetail);
			pstmt.setInt(3, orderId);
			
			int result = pstmt.executeUpdate();
			if(result == 0) throw new UserException("주문 수정에 실패했습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("주문 수정 중 오류가 발생했습니다.", e);
		} finally {
			db.release(pstmt);
		}
	}
	
	// orderId 기준으로 주문 취소(3) 변경 후 취소 수량만큼 재고 증가
	public void cancelOrder(int orderId) {
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 주문상품 조회
			String selectSql = "select product_id, quantity from shop_order_item where orders_id = ?";
	        pstmt = conn.prepareStatement(selectSql);
	        pstmt.setInt(1, orderId);
	        rs = pstmt.executeQuery();

	        if (!rs.next()) throw new UserException("주문상품 정보가 존재하지 않습니다.");
	        int productId = rs.getInt("product_id");
	        int quantity = rs.getInt("quantity");
	        pstmt.close(); rs.close();
			
	        // 주문 상태 변경(취소)
	        String updateSql = "update shop_orders set status = 3 where orders_id = ?";
			pstmt = conn.prepareStatement(updateSql);
			pstmt.setInt(1, orderId);
			int updated = pstmt.executeUpdate();
			pstmt.close();
			if(updated == 0) throw new UserException("주문 취소 변경 실패했습니다.");
			
			// 상품 재고 복구
	        String updateStockSql = "update shop_product set stock_quantity = stock_quantity + ? WHERE product_id = ?";
	        pstmt = conn.prepareStatement(updateStockSql);
	        pstmt.setInt(1, quantity);
	        pstmt.setInt(2, productId);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
	        throw new UserException("주문 취소 중 오류가 발생했습니다.", e);
		} finally {
	        db.release(pstmt, rs);
	    }
	}
	
	// 창고에 상품 출고 요청
	public void requestRelease(int orderId) {
		String sql = "insert into shop_release_order(order_id, product_id, quantity, requested_by) "
				+ "select o.orders_id, oi.product_id, oi.quantity, o.user_id "
				+ "from shop_orders o "
				+ "join shop_order_item oi on o.orders_id = oi.orders_id "
				+ "where o.orders_id = ?";
		
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, orderId);
			int result = pstmt.executeUpdate();
			if (result == 0) throw new UserException("출고 요청 실패했습니다.");
			
			// 주문 상태 배송중(2) 변경
			String updateSql = "update shop_orders set status = 2 where orders_id = ?";
			pstmt = conn.prepareStatement(updateSql);
			pstmt.setInt(1, orderId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("출고 요청 중 오류 발생했습니다.", e);
		} finally {
			db.release(pstmt);
		}
	}
	
}