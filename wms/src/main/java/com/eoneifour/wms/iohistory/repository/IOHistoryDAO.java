package com.eoneifour.wms.iohistory.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.iohistory.model.IoHistory;

public class IOHistoryDAO {
	DBManager db = DBManager.getInstance();

	// ────────────── [입고 로그: stock_log] ──────────────

	public List<IoHistory> selectAll() {
		List<IoHistory> list = new ArrayList<>();
		String sql = "SELECT * FROM stock_log";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IoHistory h = new IoHistory();
				h.setStock_log_id(rs.getInt("stock_log_id"));
				h.setProduct_id(rs.getInt("product_id"));
				h.setProduct_name(rs.getString("product_name"));
				h.setProduct_brand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setStock_date(rs.getDate("stock_date"));
				h.setDetail(rs.getString("detail"));
				list.add(h);
			}

			return list;
		} catch (SQLException e) {
			throw new UserException("입고 전체 조회 오류", e);
		} finally {
			db.release(pstmt, rs);
		}
	}

	public List<IoHistory> searchByCondition(String keyword) {
		List<IoHistory> list = new ArrayList<>();
		String sql = "SELECT * FROM stock_log WHERE product_name LIKE ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IoHistory h = new IoHistory();
				h.setStock_log_id(rs.getInt("stock_log_id"));
				h.setProduct_id(rs.getInt("product_id"));
				h.setProduct_name(rs.getString("product_name"));
				h.setProduct_brand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setStock_date(rs.getDate("stock_date"));
				h.setDetail(rs.getString("detail"));
				list.add(h);
			}

			return list;
		} catch (SQLException e) {
			throw new UserException("입고 키워드 검색 오류", e);
		} finally {
			db.release(pstmt, rs);
		}
	}

	public List<IoHistory> searchByCondition(Date start, Date end) {
		List<IoHistory> list = new ArrayList<>();
		String sql = "SELECT * FROM stock_log WHERE stock_date BETWEEN ? AND ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, start);
			pstmt.setDate(2, end);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IoHistory h = new IoHistory();
				h.setStock_log_id(rs.getInt("stock_log_id"));
				h.setProduct_id(rs.getInt("product_id"));
				h.setProduct_name(rs.getString("product_name"));
				h.setProduct_brand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setStock_date(rs.getDate("stock_date"));
				h.setDetail(rs.getString("detail"));
				list.add(h);
			}

			return list;
		} catch (SQLException e) {
			throw new UserException("입고 날짜 검색 오류", e);
		} finally {
			db.release(pstmt, rs);
		}
	}

	public List<IoHistory> searchByCondition(String keyword, Date start, Date end) {
		List<IoHistory> list = new ArrayList<>();
		String sql = "SELECT * FROM stock_log WHERE product_name LIKE ? AND stock_date BETWEEN ? AND ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setDate(2, start);
			pstmt.setDate(3, end);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IoHistory h = new IoHistory();
				h.setStock_log_id(rs.getInt("stock_log_id"));
				h.setProduct_id(rs.getInt("product_id"));
				h.setProduct_name(rs.getString("product_name"));
				h.setProduct_brand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setStock_date(rs.getDate("stock_date"));
				h.setDetail(rs.getString("detail"));
				list.add(h);
			}

			return list;
		} catch (SQLException e) {
			throw new UserException("입고 복합 검색 오류", e);
		} finally {
			db.release(pstmt, rs);
		}
	}

	// ────────────── [출고 로그: release_log] ──────────────

	public List<IoHistory> selectOutBoundLogs() {
		List<IoHistory> list = new ArrayList<>();
		String sql = "SELECT * FROM release_log";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IoHistory h = new IoHistory();
				h.setStock_log_id(rs.getInt("release_log_id"));
				h.setProduct_id(rs.getInt("product_id"));
				h.setProduct_name(rs.getString("product_name"));
				h.setProduct_brand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setRelease_date(rs.getDate("release_date"));
				h.setDetail(rs.getString("detail"));
				list.add(h);
			}

			return list;
		} catch (SQLException e) {
			throw new UserException("출고 전체 조회 오류", e);
		} finally {
			db.release(pstmt, rs);
		}
	}

	public List<IoHistory> searchOutBoundByCondition(String keyword) {
		List<IoHistory> list = new ArrayList<>();
		String sql = "SELECT * FROM release_log WHERE product_name LIKE ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IoHistory h = new IoHistory();
				h.setStock_log_id(rs.getInt("release_log_id"));
				h.setProduct_id(rs.getInt("product_id"));
				h.setProduct_name(rs.getString("product_name"));
				h.setProduct_brand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setRelease_date(rs.getDate("release_date"));
				h.setDetail(rs.getString("detail"));
				list.add(h);
			}

			return list;
		} catch (SQLException e) {
			throw new UserException("출고 키워드 검색 오류", e);
		} finally {
			db.release(pstmt, rs);
		}
	}

	public List<IoHistory> searchOutBoundByCondition(Date start, Date end) {
		List<IoHistory> list = new ArrayList<>();
		String sql = "SELECT * FROM release_log WHERE release_date BETWEEN ? AND ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, start);
			pstmt.setDate(2, end);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IoHistory h = new IoHistory();
				h.setStock_log_id(rs.getInt("release_log_id"));
				h.setProduct_id(rs.getInt("product_id"));
				h.setProduct_name(rs.getString("product_name"));
				h.setProduct_brand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setRelease_date(rs.getDate("release_date"));
				h.setDetail(rs.getString("detail"));
				list.add(h);
			}

			return list;
		} catch (SQLException e) {
			throw new UserException("출고 복합 검색 오류", e);
		} finally {
			db.release(pstmt, rs);
		}
	}

	public List<IoHistory> searchOutBoundByCondition(String keyword, Date start, Date end) {
		List<IoHistory> list = new ArrayList<>();
		String sql = "SELECT * FROM release_log WHERE product_name LIKE ? AND release BETWEEN ? AND ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setDate(2, start);
			pstmt.setDate(3, end);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IoHistory h = new IoHistory();
				h.setStock_log_id(rs.getInt("release_log_id"));
				h.setProduct_id(rs.getInt("product_id"));
				h.setProduct_name(rs.getString("product_name"));
				h.setProduct_brand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setRelease_date(rs.getDate("release_date"));
				h.setDetail(rs.getString("detail"));
				list.add(h);
			}

			return list;
		} catch (SQLException e) {
			throw new UserException("출고 복합 검색 오류", e);
		} finally {
			db.release(pstmt, rs);
		}
	}
}
