package com.eoneifour.wms.iobound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.iobound.model.selectAll;

public class OutBoundOrderDAO {
	DBManager db = DBManager.getInstance();

	public List<selectAll> selectAll() {
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM stock_prdouct WHERE stock_status = 3";
		List<selectAll> list = new ArrayList<>();

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				selectAll stockProduct = new selectAll();
				stockProduct.setStockProductId(0);
				stockProduct.setProductId(0);
				stockProduct.setProductName(sql);
				stockProduct.setProductBrand(sql);
				stockProduct.setS(0);
				stockProduct.setZ(0);
				stockProduct.setX(0);
				stockProduct.setY(0);
				stockProduct.setStockStatus(0);
				stockProduct.setDetail(sql);
				list.add(stockProduct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release(pstmt);
		}
		return list;
	}

	public List<selectAll> getOutBoundList() {
		List<selectAll> list = new ArrayList<>();
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			StringBuffer sql = new StringBuffer();
			sql.append("WITH Filtered AS (")
					.append(" SELECT stock_product_id, product_name, s, z, x, y, stock_time, stock_status")
					.append(" FROM stock_product WHERE stock_status = 3), ").append("Ranked AS (")
					.append(" SELECT *, ROW_NUMBER() OVER (PARTITION BY product_name ORDER BY stock_time ASC) AS rn,")
					.append(" SUM(1) OVER (PARTITION BY product_name) AS stock_3_count FROM Filtered) ")
					.append("SELECT stock_product_id, product_name, s, z, x, y, stock_time FROM Ranked ")
					.append("WHERE stock_3_count > 4 AND rn > 4 ").append("ORDER BY product_name, stock_time;");

			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				selectAll sp = new selectAll();
				sp.setStockProductId(rs.getInt("stock_product_id"));
				sp.setProductName(rs.getString("product_name"));
				sp.setS(rs.getInt("s"));
				sp.setZ(rs.getInt("z"));
				sp.setX(rs.getInt("x"));
				sp.setY(rs.getInt("y"));
				sp.setStock_time(rs.getTimestamp("stock_time"));
				list.add(sp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("출고 목록 조회 실패", e);
		} finally {
			db.release(pstmt, rs);
		}

		return list;
	}
	
	public List<selectAll> searchByProductName(String keyword) {
		List<selectAll> list = new ArrayList<>();
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			StringBuffer sql = new StringBuffer();
			sql.append("WITH Filtered AS (")
			   .append(" SELECT stock_product_id, product_name, s, z, x, y, stock_time, stock_status")
			   .append(" FROM stock_product WHERE stock_status = 3), ")
			   .append("Ranked AS (")
			   .append(" SELECT *, ROW_NUMBER() OVER (PARTITION BY product_name ORDER BY stock_time ASC) AS rn,")
			   .append(" SUM(1) OVER (PARTITION BY product_name) AS stock_3_count FROM Filtered) ")
			   .append("SELECT stock_product_id, product_name, s, z, x, y, stock_time FROM Ranked ")
			   .append("WHERE stock_3_count > 4 AND rn > 4 AND product_name LIKE ? ")
			   .append("ORDER BY product_name, stock_time;");

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, "%" + keyword + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				selectAll sp = new selectAll();
				sp.setStockProductId(rs.getInt("stock_product_id"));
				sp.setProductName(rs.getString("product_name"));
				sp.setS(rs.getInt("s"));
				sp.setZ(rs.getInt("z"));
				sp.setX(rs.getInt("x"));
				sp.setY(rs.getInt("y"));
				sp.setStock_time(rs.getTimestamp("stock_time"));
				list.add(sp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("출고 목록 조회 실패", e);
		} finally {
			db.release(pstmt, rs);
		}

		return list;
	}

	public void releaseProductById(int id) {
		Connection conn = db.getConnection();
		PreparedStatement selectStmt = null;
		PreparedStatement insertStmt = null;
		PreparedStatement deleteStmt = null;
		PreparedStatement logStmt = null;
		PreparedStatement quantityStmt = null;
		ResultSet rs = null;

		try {
			conn.setAutoCommit(false); // 

			String selectSql = "SELECT product_id, product_name, product_brand, s, z, x, y, detail FROM stock_product WHERE stock_product_id = ?";
			selectStmt = conn.prepareStatement(selectSql);
			selectStmt.setInt(1, id);
			rs = selectStmt.executeQuery();

			if (rs.next()) {
				String insertSql = "INSERT INTO release_product (product_id, product_name, product_brand, s, z, x, y, release_status, detail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(insertSql);
				insertStmt.setInt(1, rs.getInt("product_id"));
				insertStmt.setString(2, rs.getString("product_name"));
				insertStmt.setString(3, rs.getString("product_brand"));
				insertStmt.setInt(4, rs.getInt("s"));
				insertStmt.setInt(5, rs.getInt("z"));
				insertStmt.setInt(6, rs.getInt("x"));
				insertStmt.setInt(7, rs.getInt("y"));
				insertStmt.setInt(8, 4);
				insertStmt.setString(9, rs.getString("detail"));
				insertStmt.executeUpdate();

				String deleteSql = "DELETE FROM stock_product WHERE stock_product_id = ?";
				deleteStmt = conn.prepareStatement(deleteSql);
				deleteStmt.setInt(1, id);
				deleteStmt.executeUpdate();

				String logSql = "INSERT INTO release_log (product_id, product_name, product_brand, s, z, x, y, release_date, detail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				logStmt = conn.prepareStatement(logSql);
				logStmt.setInt(1, rs.getInt("product_id"));
				logStmt.setString(2, rs.getString("product_name"));
				logStmt.setString(3, rs.getString("product_brand"));
				logStmt.setInt(4, rs.getInt("s"));
				logStmt.setInt(5, rs.getInt("z"));
				logStmt.setInt(6, rs.getInt("x"));
				logStmt.setInt(7, rs.getInt("y"));
				logStmt.setObject(8, java.time.LocalDateTime.now());
				logStmt.setString(9, rs.getString("detail"));
				logStmt.executeUpdate();

				String updateQuantitySql = "UPDATE shop_product SET stock_quantity = stock_quantity + 30 WHERE product_id = ?";
				quantityStmt = conn.prepareStatement(updateQuantitySql);
				quantityStmt.setInt(1, rs.getInt("product_id"));
				quantityStmt.executeUpdate();
			}

			conn.commit(); // ✅ 성공 시 커밋
		} catch (SQLException e) {
			try {
				conn.rollback(); // ❗ 예외 발생 시 롤백
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			e.printStackTrace();
			throw new UserException("출고 처리 중 오류 발생", e);
		} finally {
			db.release(selectStmt, rs);
			db.release(insertStmt);
			db.release(deleteStmt);
			db.release(logStmt);
			db.release(quantityStmt);
			try {
				conn.setAutoCommit(true); // 🧼 커넥션 자동 복구
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void outbound(int id) {
		String sql = "DELETE FROM stock_product WHERE stock_product_id = ?";

		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, id);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}