package com.eoneifour.wms.iobound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.iobound.model.StockProduct;

public class InBoundOrderDAO {
	DBManager db = DBManager.getInstance();

	// 페이지 새로고침시 '입고대기'인 제품 출력 (status 1 줘야함  );
	public List<StockProduct> selectByStatus(int status) {
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT stock_product_id, product_name FROM stock_product WHERE stock_status = ?";
		List<StockProduct> list = new ArrayList<>();

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, status);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				StockProduct stockProduct = new StockProduct();
				stockProduct.setStockprodutId(rs.getInt("stock_product_id"));
				stockProduct.setProductName(rs.getString("product_name"));

				list.add(stockProduct);
			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(Integer.toString(e.getErrorCode()));
		} finally {
			db.release(pstmt, rs);
		}
	}

	// 입고물품 키워드 검색
	public List<StockProduct> searchByProductName(String keyword, int status) {
		String sql = "SELECT * FROM stock_product WHERE stock_status = ? AND product_name LIKE ?";

		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			List<StockProduct> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, status);
			pstmt.setString(2, "%" + keyword + "%"); // 와일드카드 검색

			rs = pstmt.executeQuery();

			while (rs.next()) {
				StockProduct stockProduct = new StockProduct();
				stockProduct.setStockprodutId(rs.getInt("stock_product_id"));
				stockProduct.setProductName(rs.getString("product_name"));

				list.add(stockProduct);
			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(Integer.toString(e.getErrorCode()));
		} finally {
			db.release(pstmt, rs);
		}
	}

	// 하차 버튼 클릭시 insert
	public void insertByList(List<StockProduct> stockProduct) throws UserException {
		Connection conn = db.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append(
				"INSERT INTO stock_product(product_id, product_name, product_brand, s, z, x, y, stock_status, detail)");
		sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");

		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (StockProduct sp : stockProduct) {
				pstmt.setInt(1, sp.getProductId());
				pstmt.setString(2, sp.getProductName());
				pstmt.setString(3, sp.getProductBrand());
				pstmt.setInt(4, sp.getS());
				pstmt.setInt(5, sp.getZ());
				pstmt.setInt(6, sp.getX());
				pstmt.setInt(7, sp.getY());
				pstmt.setInt(8, 0);
				pstmt.setString(9, sp.getDetail());
				
				// 모든 쿼리문을 메모리에 저장해두었다가 한번에 보내기
				pstmt.addBatch();
			}
			// 저장된 쿼리 실행
			pstmt.executeBatch(); 
			conn.setAutoCommit(true);

		} catch (SQLException e) {
			throw new UserException("insert 실패", e);
		} finally {
			db.release(pstmt);
		}
	}
	
	// 입고 위치 업데이트
	public void updateStatusWithPosition(int id, int statusNum, int s, int z, int x, int y) {
	    Connection conn = db.getConnection();
	    PreparedStatement pstmt = null;

	    try {
	        String sql = "UPDATE stock_product SET stock_status = ?, s = ?, z = ?, x = ?, y = ? WHERE stock_product_id = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, statusNum);
	        pstmt.setInt(2, s);
	        pstmt.setInt(3, z);
	        pstmt.setInt(4, x);
	        pstmt.setInt(5, y);
	        pstmt.setInt(6, id);

	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new UserException("입고 위치 업데이트 실패", e);
	    } finally {
	        db.release(pstmt);
	    }
	}

}
