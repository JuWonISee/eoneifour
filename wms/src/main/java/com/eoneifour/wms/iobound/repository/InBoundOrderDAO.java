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
import com.eoneifour.wms.iobound.model.StockProduct;

public class InBoundOrderDAO {
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

	
	public int processInbound(int orderId) throws UserException {
		PreparedStatement pstmtUpdate = null;
		PreparedStatement pstmtSelect = null;
		PreparedStatement pstmtInsert = null;
		ResultSet rs = null;

		try {
			// 트랜잭션 시작 - 자동 커밋 비활성화
			conn.setAutoCommit(false);
			
			String updateSql = "UPDATE shop_purchase_order SET status = ?, complete_date = ? WHERE purchase_order_id = ?";
			pstmtUpdate = conn.prepareStatement(updateSql);
			pstmtUpdate.setString(1, "입고완료");
			pstmtUpdate.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			pstmtUpdate.setInt(3, orderId);
			
			int updateResult = pstmtUpdate.executeUpdate();
			if (updateResult == 0) {
				throw new UserException("발주 주문 상태 업데이트에 실패했습니다");
			}

			// 2단계: 상품 정보 조회
			String selectSql = "SELECT p.product_id, p.name, p.brand_name, p.detail, po.quantity";
			selectSql += " FROM shop_product p JOIN shop_purchase_order po";
			selectSql += " ON p.product_id = po.product_id WHERE po.purchase_order_id = ?";
			
			pstmtSelect = conn.prepareStatement(selectSql);
			pstmtSelect.setInt(1, orderId);
			rs = pstmtSelect.executeQuery();

			// 3단계: stock_product 테이블에 삽입
			String insertSql = "INSERT INTO stock_product(product_id, product_name, product_brand, detail, s, z, x, y, stock_status)";
			insertSql += " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmtInsert = conn.prepareStatement(insertSql);
                              
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

				int insertResult = pstmtInsert.executeUpdate();
				if (insertResult == 0) {
					throw new UserException("재고 상품 등록에 실패했습니다");
				}
			}

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
			throw new UserException("입고 처리 중 오류가 발생했습니다", e);
		} finally {
			// 자동 커밋 다시 활성화
			try {
				if (conn != null) {
					conn.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			db.release(pstmtUpdate);
			db.release(pstmtSelect);
			db.release(pstmtInsert);
		}
	}
}
