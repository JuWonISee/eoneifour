package com.eoneifour.wms.iobound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.wms.iobound.model.InBoundOrder;

public class InBoundOrderDAO {
	DBManager db = DBManager.getInstance();
	Connection conn = db.getConnection();

	public List<InBoundOrder> getOrderList() {
		String sql = "SELECT p.name " + "FROM product p " + "JOIN purchase_order po ON p.product_id = po.product_id "
				+ "WHERE po.status = 0";

		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			List<InBoundOrder> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql);
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
			throw new UserException("입고 목록 조회 중 오류 발생", e);
		} finally {
			db.release(pstmt, rs);
		}
	}

	public List<InBoundOrder> searchByProductName(String keyword) {
		String sql = "SELECT p.name FROM product p " + "JOIN purchase_order po ON p.product_id = po.product_id "
				+ "WHERE po.status = 0 AND p.name LIKE ?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			List<InBoundOrder> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%"); // 와일드카드 검색

			rs = pstmt.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setName(rs.getString("name"));

				InBoundOrder inBoundOrder = new InBoundOrder();
				inBoundOrder.setProduct(product);

				list.add(inBoundOrder);
			}
			return list;
		} catch (

		SQLException e) {
			e.printStackTrace();
			throw new UserException("상품 검색 중 오류 발생", e);

		}

	}
}
