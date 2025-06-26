package com.eoneifour.wms.iobound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.wms.iobound.model.InBoundOrder;

public class OutBoundOrderDAO {
	DBManager db = DBManager.getInstance();
	Connection conn = db.getConnection();

	public List<InBoundOrder> getOrderList() {
		String sql = "SELECT po.purchase_order_id, p.name " + "FROM shop_product p "
				+ "JOIN shop_purchase_order po ON p.product_id = po.product_id " + "WHERE po.status = ?";

		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			List<InBoundOrder> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "창고도착");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setName(rs.getString("name"));

				InBoundOrder inBoundOrder = new InBoundOrder();
				inBoundOrder.setPurchase_order_id(rs.getInt("purchase_order_id"));

				inBoundOrder.setProduct(product);

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

	public List<InBoundOrder> searchByProductName(String keyword) {
		String sql = "SELECT p.name FROM shop_product p "
				+ "JOIN shop_purchase_order po ON p.product_id = po.product_id "
				+ "WHERE po.status = ? AND p.name LIKE ?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			List<InBoundOrder> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "창고도착");
			pstmt.setString(2, "%" + keyword + "%"); // 와일드카드 검색

			rs = pstmt.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setName(rs.getString("name"));

				InBoundOrder inBoundOrder = new InBoundOrder();
				inBoundOrder.setProduct(product);

				list.add(inBoundOrder);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("상품 검색 중 오류 발생", e);
		} finally {
			db.release(pstmt, rs);
		}
	}

	public int inBound(int orderId) {
		int result = 0;

		String sql = "UPDATE shop_purchase_order SET status = ?, complete_date = ? WHERE purchase_order_id = ?";
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "입고완료");
			pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			pstmt.setInt(3, orderId);

			result = pstmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("입고 처리 중 오류 발생", e);
		} finally {
			db.release(pstmt);
		}

	}
}
