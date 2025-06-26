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
import com.eoneifour.wms.iobound.model.OutBoundOrder;
import com.eoneifour.wms.iobound.model.StockProduct;

public class OutBoundOrderDAO {
	DBManager db = DBManager.getInstance();

	// 발주 리스트 조회
	public List<OutBoundOrder> getOrderList() {
		String sql = "SELECT * FROM stock_product WHERE product_id"
				+ " IN (SELECT product_id FROM stock_product GROUP BY product_id HAVING COUNT(*) > 4)"
				+ " ORDER BY stock_product_id ASC" ;   

		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			List<OutBoundOrder> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				OutBoundOrder outBoundOrder = new OutBoundOrder();
				
				outBoundOrder.setStock_product_id(rs.getInt("stock_product_id"));
				outBoundOrder.setProduct_id(rs.getInt("product_id"));
				outBoundOrder.setProduct_name(rs.getString("product_name"));
				outBoundOrder.setProduct_brand(rs.getString("product_brand"));
				outBoundOrder.setS(rs.getInt("s"));
				outBoundOrder.setZ(rs.getInt("z"));
				outBoundOrder.setX(rs.getInt("x"));
				outBoundOrder.setY(rs.getInt("y"));
				outBoundOrder.setStock_status(rs.getInt("stock_status"));
				outBoundOrder.setDetail(rs.getString("detail"));
				
				list.add(outBoundOrder);
			}

			System.out.println(list);
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(Integer.toString(e.getErrorCode()));
		} finally {
			db.release(pstmt, rs);
		}
	}

	// 발주 리스트 중 키워드 검색
	public List<OutBoundOrder> searchByProductName(String keyword) {
		String sql = "SELECT p.name FROM shop_product p "
				+ "JOIN shop_purchase_order po ON p.product_id = po.product_id "
				+ "WHERE po.status = ? AND p.name LIKE ?";

		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			List<OutBoundOrder> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "창고도착");
			pstmt.setString(2, "%" + keyword + "%"); // 와일드카드 검색

			rs = pstmt.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setName(rs.getString("name"));

				OutBoundOrder outBoundOrder = new OutBoundOrder();

				list.add(outBoundOrder);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(Integer.toString(e.getErrorCode()));
		} finally {
			db.release(pstmt, rs);
		}
	}

	//
	public int processOutBound(int orderId) throws UserException {
		Connection conn = db.getConnection();

		PreparedStatement pstmtUpdate = null;
		PreparedStatement pstmtSelect = null;
		PreparedStatement pstmtInsert = null;

		ResultSet rs = null;

		try {
			// 1. 트랙잭션 시작
			conn.setAutoCommit(false);

			// 2. 상품정보 조회 Query
			String selectSql = "SELECT p.product_id, p.name, p.brand_name, p.detail, po.quantity";
			selectSql += " FROM shop_product p JOIN shop_purchase_order po";
			selectSql += " ON p.product_id = po.product_id WHERE po.purchase_order_id = ?";

			pstmtSelect = conn.prepareStatement(selectSql);
			pstmtSelect.setInt(1, orderId);
			rs = pstmtSelect.executeQuery();

			// 3. 입고 테이블에 INSERT Query
			String insertSql = "INSERT INTO stock_product(product_id, product_name, product_brand, detail, s, z, x, y, stock_status)";
			insertSql += " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmtInsert = conn.prepareStatement(insertSql);

			// 4. 모델 객체에 set 및 INSERT Query에 바인딩
			while (rs.next()) {
				StockProduct stockProduct = new StockProduct();
				stockProduct.setProductId(rs.getInt("product_id"));
				stockProduct.setProductName(rs.getString("name"));
				stockProduct.setProductBrand(rs.getString("brand_name"));
				stockProduct.setDetail(rs.getString("detail"));
				stockProduct.setS(1);
				stockProduct.setZ(2);
				stockProduct.setX(3);
				stockProduct.setY(4);
				stockProduct.setStockStatus(0);

				pstmtInsert.setInt(1, stockProduct.getProductId());
				pstmtInsert.setString(2, stockProduct.getProductName());
				pstmtInsert.setString(3, stockProduct.getProductBrand());
				pstmtInsert.setString(4, stockProduct.getDetail());
				pstmtInsert.setInt(5, stockProduct.getS());
				pstmtInsert.setInt(6, stockProduct.getZ());
				pstmtInsert.setInt(7, stockProduct.getX());
				pstmtInsert.setInt(8, stockProduct.getY());
				pstmtInsert.setInt(9, stockProduct.getStockStatus());

				pstmtInsert.executeUpdate();
			}

			// 5 . 발주 테이블 UPDATE (창고도착 -> 입고완료)
			String updateSql = "UPDATE shop_purchase_order SET status = ?, complete_date = ? WHERE purchase_order_id = ?";
			pstmtUpdate = conn.prepareStatement(updateSql);
			pstmtUpdate.setString(1, "입고중");
			pstmtUpdate.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			pstmtUpdate.setInt(3, orderId);

			int updateResult = pstmtUpdate.executeUpdate();

			// 모든 작업이 성공하면 커밋
			conn.commit();

			return updateResult; // 업데이트된 행 수 반환

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
			// 자동 커밋 다시 활성화
			try {
				if (conn != null)
					conn.setAutoCommit(true);
				db.release(pstmtSelect, rs);
				db.release(pstmtInsert);
				db.release(pstmtUpdate);
			} catch (SQLException e) {
				throw new UserException(Integer.toString(e.getErrorCode()));
			}
			db.release(pstmtUpdate);
			db.release(pstmtSelect);
			db.release(pstmtInsert);
		}
	}
}
