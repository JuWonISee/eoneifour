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

public class OrderDAO {
	DBManager db = DBManager.getInstance();
	
	// 전체 조회
	public List<Order> getOrderList() {
		StringBuffer sql = new StringBuffer();
		sql.append("select o.orders_id, o.order_date, o.total_price, o.status, u.name as user_name, p.brand_name, p.name as product_name, oi.quantity, oi.price");
		sql.append(" from shop_orders o join shop_user u on o.user_id = u.user_id join shop_order_item oi on o.orders_id = oi.orders_id join shop_product p on oi.product_id = p.product_id ");
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
				order.setBrand(rs.getString("brand_name"));
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
	
	// userId 기준으로 주문 리스트 조회
	public List<Order> getOrderListByUserId(int userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select o.orders_id, o.order_date, o.total_price, o.status, o.user_id, u.name as user_name, p.brand_name, p.name as product_name, oi.quantity, oi.price");
		sql.append(" from shop_orders o join shop_user u on o.user_id = u.user_id join shop_order_item oi on o.orders_id = oi.orders_id join shop_product p on oi.product_id = p.product_id");
		sql.append(" where u.user_id = ?");
		sql.append(" order by o.orders_id desc");

		Connection conn = db.getConnection();
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			List<Order> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				Order order = new Order();
				order.setOrderId(rs.getInt("orders_id"));
				order.setOrderDate(rs.getDate("order_date"));
				order.setUserName(rs.getString("user_name"));
				order.setBrand(rs.getString("brand_name"));
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
		    "u.name AS user_name, p.brand_name, p.name AS product_name, oi.quantity " +
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
				order.setBrand(rs.getString("brand_name"));
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
	        pstmt.close(); rs.close();
			
	        // 주문 상태 변경(취소)
	        String updateSql = "update shop_orders set status = 3 where orders_id = ?";
			pstmt = conn.prepareStatement(updateSql);
			pstmt.setInt(1, orderId);
			int updated = pstmt.executeUpdate();
			pstmt.close();
			if(updated == 0) throw new UserException("주문 취소 변경 실패했습니다.");			
		} catch (SQLException e) {
			e.printStackTrace();
	        throw new UserException("주문 취소 중 오류가 발생했습니다.", e);
		} finally {
	        db.release(pstmt, rs);
	    }
	}
	
	// 주문 관리에서 주문 확인/배송 준비 처리 로직 (0: 주문확인중 / 1: 배송준비)
	public void changeStatus(int orderId, int status, int orderQuantity) {
		String str = (status == 0) ? "배송 준비" : "배송 완료";  
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
	        int prdQuantity = rs.getInt("quantity");
	        pstmt.close(); rs.close();
	        
	        // 재고 확인
	        if (prdQuantity < orderQuantity) throw new UserException("재고가 부족합니다.");
			
	        // 주문 상태 변경
	        String updateSql = (status == 0) ? "update shop_orders set status = 1 where orders_id = ?" 
											: "update shop_orders set status = 2 where orders_id = ?";
			pstmt = conn.prepareStatement(updateSql);
			pstmt.setInt(1, orderId);
			int updated = pstmt.executeUpdate();
			pstmt.close();
			if(updated == 0) throw new UserException(str+ " 처리 실패했습니다.");
			
			// 배송준비 완료 후 상품 재고 차감
			if(status != 0) {
				String updateStockSql = "update shop_product set stock_quantity = stock_quantity - ? WHERE product_id = ?";
				pstmt = conn.prepareStatement(updateStockSql);
				pstmt.setInt(1, orderQuantity);
				pstmt.setInt(2, productId);
				int stockUpdated = pstmt.executeUpdate();
				pstmt.close();
				if(stockUpdated == 0) throw new UserException(str +" 요청 실패했습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(str + " 요청 중 오류 발생했습니다.", e);
		} finally {
			db.release(pstmt, rs);
		}
	}
	
	public List<Order> searchByKeyword(String keyword) {
		StringBuffer sql = new StringBuffer();
		sql.append("select o.orders_id, o.order_date, o.total_price, o.status, u.name as user_name, p.name as product_name, oi.quantity, oi.price ");
		sql.append("from shop_orders o join shop_user u on o.user_id = u.user_id join shop_order_item oi on o.orders_id = oi.orders_id join shop_product p on oi.product_id = p.product_id ");
		sql.append(" where (u.name like ? or p.name like ?) ");

		Connection conn = db.getConnection();
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			List<Order> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql.toString());
			
	        String likeKeyword = "%" + keyword + "%";
	        pstmt.setString(1, likeKeyword);
	        pstmt.setString(2, likeKeyword);
	        
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
	
	
}