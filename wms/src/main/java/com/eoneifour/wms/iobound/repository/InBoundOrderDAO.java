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
import com.eoneifour.shopadmin.purchaseOrder.model.PurchaseOrder;
import com.eoneifour.wms.iobound.model.ShopPurchaseOrder;
import com.eoneifour.wms.iobound.model.StockProduct;

public class InBoundOrderDAO {
	DBManager db = DBManager.getInstance();
	// 페이지 새로고침시 '입고대기'인 제품 출력
	public List<StockProduct> getInboundWaitingList() {
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT stock_product_id, product_NAME FROM stock_product WHERE stock_status = 0";
		List<StockProduct> list = new ArrayList<>();

		try {
			pstmt = conn.prepareStatement(sql);
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

	// 하차 대기중인 건수 조회
	public int getUnloadList() {
		String sql = "SELECT count(purchase_order_id) FROM shop_purchase_order WHERE status='창고도착'";
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1); // 첫 번째 컬럼(count 값) 가져오기
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release(pstmt, rs);
		}

		return count;

	}

	// 입고대기중 키워드 검색
	public List<StockProduct> searchByProductName(String keyword) {
		String sql = "SELECT * FROM stock_product WHERE stock_[status = ? AND product_name LIKE ?";

		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			List<StockProduct> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, 0);
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

	// 하차 버튼 클릭시 update / insert
	public int unlodingProcess() throws UserException {
		Connection conn = db.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmtUpdate = null;
		PreparedStatement pstmtInsert = null;
		PreparedStatement pstmtSelect = null;
		int result = 0;

		try {
			conn.setAutoCommit(false);

			// 1. 상품정보 조회 Query
			String selectSql = "SELECT p.product_id, p.name, p.brand_name, p.detail, po.quantity"
					+ " FROM shop_product p JOIN shop_purchase_order po"
					+ " ON p.product_id = po.product_id WHERE po.status = ?";

			pstmtSelect = conn.prepareStatement(selectSql);

			pstmtSelect.setString(1, "창고도착");
			rs = pstmtSelect.executeQuery();

			// 2. 조회된 결과로 INSERT
			while (rs.next()) {
				String insertSql = "INSERT INTO stock_product(product_id, product_name, product_brand, s, z, x, y, stock_status, detail)"
						+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				pstmtInsert = conn.prepareStatement(insertSql);

				pstmtInsert.setInt(1, rs.getInt("product_id"));
				pstmtInsert.setString(2, rs.getString("name"));
				pstmtInsert.setString(3, rs.getString("brand_name"));
				pstmtInsert.setInt(4, 1);
				pstmtInsert.setInt(5, 1);
				pstmtInsert.setInt(6, 2);
				pstmtInsert.setInt(7, 2);
				pstmtInsert.setInt(8, 0); // 0--> 하차완료 1-->입고중 2-->입고완료
				pstmtInsert.setString(9, rs.getString("detail"));

				result += pstmtInsert.executeUpdate();
				// 3 . 발주 테이블 UPDATE (창고도착 -> 입고대기)
				String updateSql = "UPDATE shop_purchase_order SET status = ?, complete_date  = ? WHERE status = ?";
				pstmtUpdate = conn.prepareStatement(updateSql);
				pstmtUpdate.setString(1, "완료");
				pstmtUpdate.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				pstmtUpdate.setString(3, "창고도착");
				pstmtUpdate.executeUpdate();
			}
			
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// 오류 발생 시 롤백
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			e.printStackTrace();
			throw new UserException(Integer.toString(e.getErrorCode()));
		} finally {
			db.release(pstmtUpdate);
			db.release(pstmtSelect, rs);
			db.release(pstmtInsert);
		}
		return result;
	}
	
	public void setStatus(int id, int statusNum) {
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			String sql = "UPDATE stock_product SET stock_status = ? WHERE stock_product_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,statusNum);
			pstmt.setInt(2,id);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release(pstmt);
		}
	}
}
