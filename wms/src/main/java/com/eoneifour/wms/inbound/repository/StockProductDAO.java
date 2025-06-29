package com.eoneifour.wms.inbound.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.iobound.model.StockProduct;

public class StockProductDAO {
	DBManager db = DBManager.getInstance();

	public StockProduct selectInfo(int s, int z, int x, int y) {
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" select * 					");
		sql.append("	from stock_product 	");
		sql.append(" where s = ?          		");
		sql.append("    and z = ?          		");
		sql.append("    and x = ?          		");
		sql.append("    and y = ?          		");

		StockProduct stockProduct = new StockProduct();

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, s);
			pstmt.setInt(2, z);
			pstmt.setInt(3, x);
			pstmt.setInt(4, y);

			rs = pstmt.executeQuery();
			rs.next();

			stockProduct.setStockProductId(rs.getInt("stock_product_id"));
			stockProduct.setProductId(rs.getInt("product_id"));
			stockProduct.setProductName(rs.getString("product_name"));
			stockProduct.setProductBrand(rs.getString("product_brand"));
			stockProduct.setS(rs.getInt("s"));
			stockProduct.setZ(rs.getInt("z"));
			stockProduct.setX(rs.getInt("x"));
			stockProduct.setY(rs.getInt("y"));
			stockProduct.setStockStatus(rs.getInt("stock_status"));
			stockProduct.setDetail(rs.getString("detail"));

		} catch (SQLException e) {
			return null;
		} finally {
			db.release(pstmt, rs);
		}
		return stockProduct;
	}

	public List<StockProduct> selectAll(Boolean bool) {
		List<StockProduct> list = new ArrayList<>();
		String sql;
		// true이면 상품명 정렬
		if (bool) {
			sql = "SELECT * FROM stock_product WHERE stock_status = 3 ORDER BY product_name ASC";
		} else {
			sql = "SELECT * FROM stock_product WHERE stock_status = 3 ORDER BY stock_time ASC";
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				StockProduct h = new StockProduct();
				h.setStockProductId(rs.getInt("stock_product_id"));
				h.setProductId(rs.getInt("product_id"));
				h.setProductName(rs.getString("product_name"));
				h.setProductBrand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setStock_time(rs.getDate("stock_time"));
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

	public List<StockProduct> searchByCondition(String keyword) {
		List<StockProduct> list = new ArrayList<>();
		String sql = "SELECT * FROM stock_product WHERE product_name LIKE ? AND stock_status = 3";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				StockProduct h = new StockProduct();
				h.setStockProductId(rs.getInt("stock_product_id"));
				h.setProductId(rs.getInt("product_id"));
				h.setProductName(rs.getString("product_name"));
				h.setProductBrand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setStock_time(rs.getDate("stock_time"));
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

	public List<StockProduct> searchByCondition(Date start, Date end) {
		List<StockProduct> list = new ArrayList<>();
		String sql = "SELECT * FROM stock_product WHERE stock_time BETWEEN ? AND ? AND stock_status = 3";
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
				StockProduct h = new StockProduct();
				h.setStockProductId(rs.getInt("stock_product_id"));
				h.setProductId(rs.getInt("product_id"));
				h.setProductName(rs.getString("product_name"));
				h.setProductBrand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setStock_time(rs.getDate("stock_time"));
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
	
	public List<StockProduct> selectAllOut(Boolean bool) {
		List<StockProduct> list = new ArrayList<>();
		String sql;
		// true이면 상품명 정렬
		if (bool) {
			sql = "SELECT * FROM release_product WHERE release_status = 4 ORDER BY product_name ASC";
		} else {
			sql = "SELECT * FROM release_product WHERE release_status = 4 ORDER BY release_time ASC";
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				StockProduct h = new StockProduct();
				h.setStockProductId(rs.getInt("release_product_id"));
				h.setProductId(rs.getInt("product_id"));
				h.setProductName(rs.getString("product_name"));
				h.setProductBrand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setStock_time(rs.getDate("release_time"));
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

	public List<StockProduct> searchByConditionOut(String keyword) {
		List<StockProduct> list = new ArrayList<>();
		String sql = "SELECT * FROM release_product WHERE product_name LIKE ? AND release_status = 4";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				StockProduct h = new StockProduct();
				h.setStockProductId(rs.getInt("release_product_id"));
				h.setProductId(rs.getInt("product_id"));
				h.setProductName(rs.getString("product_name"));
				h.setProductBrand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setStock_time(rs.getDate("release_time"));
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

	public List<StockProduct> searchByConditionOut(Date start, Date end) {
		List<StockProduct> list = new ArrayList<>();
		String sql = "SELECT * FROM release_product WHERE stock_time BETWEEN ? AND ? AND stock_status = 4";
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
				StockProduct h = new StockProduct();
				h.setStockProductId(rs.getInt("release_product_id"));
				h.setProductId(rs.getInt("product_id"));
				h.setProductName(rs.getString("product_name"));
				h.setProductBrand(rs.getString("product_brand"));
				h.setS(rs.getInt("s"));
				h.setZ(rs.getInt("z"));
				h.setX(rs.getInt("x"));
				h.setY(rs.getInt("y"));
				h.setStock_time(rs.getDate("release_time"));
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
	
	
}
