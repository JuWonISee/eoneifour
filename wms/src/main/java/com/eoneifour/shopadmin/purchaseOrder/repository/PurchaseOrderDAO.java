package com.eoneifour.shopadmin.purchaseOrder.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.product.repository.ProductDAO;
import com.eoneifour.shopadmin.purchaseOrder.model.PurchaseOrder;
import com.eoneifour.shopadmin.user.model.User;

public class PurchaseOrderDAO {
	DBManager dbManager = DBManager.getInstance();
	
	//전체 발주 리스트 조회
	public List<PurchaseOrder> getPurchaseList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<PurchaseOrder> list = new ArrayList<>();

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("select po.purchase_order_id, p.name AS product_name, po.quantity, ");
		sql.append(" po.request_date, u.name AS requested_by_name, po.status, ");
		sql.append(" po.complete_date, po.requested_by, po.product_id ");
		sql.append(" FROM shop_purchase_order po ");
		sql.append(" JOIN shop_user u ON po.requested_by = u.user_id ");
		sql.append(" JOIN shop_product p ON po.product_id = p.product_id");
		sql.append(" order by purchase_order_id desc");

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				PurchaseOrder purchaseOrder = new PurchaseOrder();
				User user = new User();
				Product product = new Product();
				
				purchaseOrder.setPurchase_order_id(rs.getInt("purchase_order_id"));
				product.setName(rs.getString("product_name"));
				purchaseOrder.setQuantity(rs.getInt("quantity"));
				purchaseOrder.setRequest_date(rs.getDate("request_date"));
				user.setName(rs.getString("requested_by_name"));
				purchaseOrder.setStatus(rs.getString("status"));
				purchaseOrder.setComplete_date(rs.getDate("complete_date"));
				purchaseOrder.setProduct(product);
				purchaseOrder.setUser(user);
				
				list.add(purchaseOrder);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("발주 목록 조회 중 오류 발생", e);
		} finally {
			dbManager.release(pstmt, rs);
		}

	}
	
	// 발주 ID 기준으로 발주 1건 조회
	public PurchaseOrder getPurchase(int purchaseOrderId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		PurchaseOrder purchaseOrder = new PurchaseOrder();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT po.purchase_order_id, p.name AS product_name, po.quantity, ");
		sql.append(" po.request_date, u.name AS requested_by_name, po.status, ");
		sql.append(" po.complete_date ");
		sql.append(" FROM shop_purchase_order po ");
		sql.append(" JOIN shop_user u ON po.requested_by = u.user_id ");
		sql.append(" JOIN shop_product p ON po.product_id = p.product_id ");
		sql.append(" WHERE po.purchase_order_id = ?");

		try {
			con = dbManager.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, purchaseOrderId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				Product product = new Product();
				User user = new User();

				purchaseOrder.setPurchase_order_id(purchaseOrderId);

				product.setName(rs.getString("product_name"));
				purchaseOrder.setProduct(product);

				purchaseOrder.setQuantity(rs.getInt("quantity"));
				purchaseOrder.setRequest_date(rs.getDate("request_date"));

				user.setName(rs.getString("requested_by_name"));
				purchaseOrder.setUser(user);

				purchaseOrder.setStatus(rs.getString("status"));
				purchaseOrder.setComplete_date(rs.getDate("complete_date"));

				return purchaseOrder;
			} else {
				throw new UserException("해당 발주 정보가 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("발주 상세 조회 중 오류가 발생했습니다.", e);
		} finally {
			dbManager.release(pstmt, rs);
		}
	}

	//발주 1건 추가
	public void insertPurchase(int productId, int quantity, int userId) throws UserException {
	    Connection con = null;
	    PreparedStatement pstmt = null;

	    try {
	        con = dbManager.getConnection();

	        StringBuffer sql = new StringBuffer();
	        sql.append("INSERT INTO shop_purchase_order(quantity, status, complete_date, requested_by, product_id) ");
	        sql.append("VALUES (?, ?, ?, ?, ?)");

	        PurchaseOrder purchaseOrder = new PurchaseOrder();
	        purchaseOrder.setQuantity(quantity);
	        purchaseOrder.setStatus("창고도착"); // 발주 상태는 창고도착 하드코딩 (발주완료 , 창고도착 , 입고완료)
	        purchaseOrder.setComplete_date(null);
	        purchaseOrder.setRequested_by(userId); //주문자는 로그인 정보에서 얻어오는 로직.
	        purchaseOrder.setProduct_id(productId);

	        pstmt = con.prepareStatement(sql.toString());
	        pstmt.setInt(1, purchaseOrder.getQuantity());
	        pstmt.setString(2, purchaseOrder.getStatus());
	        pstmt.setNull(3, java.sql.Types.DATE);
	        pstmt.setInt(4, purchaseOrder.getRequested_by());
	        pstmt.setInt(5, purchaseOrder.getProduct_id());

	        int result = pstmt.executeUpdate();
	        if (result == 0) {
	            throw new UserException("발주 신청에 실패했습니다");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new UserException("발주 신청 중 오류가 발생하였습니다.", e);
	    } finally {
	        dbManager.release(pstmt);
	    }
	}
	
	public List<PurchaseOrder> searchByKeyword(String keyword){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<PurchaseOrder> list = new ArrayList<>();

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("select po.purchase_order_id, p.name AS product_name, po.quantity, ");
		sql.append(" po.request_date, u.name AS requested_by_name, po.status, ");
		sql.append(" po.complete_date, po.requested_by, po.product_id ");
		sql.append(" FROM shop_purchase_order po ");
		sql.append(" JOIN shop_user u ON po.requested_by = u.user_id ");
		sql.append(" JOIN shop_product p ON po.product_id = p.product_id");
		sql.append(" WHERE (po.purchase_order_id LIKE ? or p.name LIKE ?)");

		try {
			pstmt = con.prepareStatement(sql.toString());
			
	        String likeKeyword = "%" + keyword + "%";
	        pstmt.setString(1, likeKeyword);
	        pstmt.setString(2, likeKeyword);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				PurchaseOrder purchaseOrder = new PurchaseOrder();
				User user = new User();
				Product product = new Product();
				
				purchaseOrder.setPurchase_order_id(rs.getInt("purchase_order_id"));
				product.setName(rs.getString("product_name"));
				purchaseOrder.setQuantity(rs.getInt("quantity"));
				purchaseOrder.setRequest_date(rs.getDate("request_date"));
				user.setName(rs.getString("requested_by_name"));
				purchaseOrder.setStatus(rs.getString("status"));
				purchaseOrder.setComplete_date(rs.getDate("complete_date"));
				purchaseOrder.setProduct(product);
				purchaseOrder.setUser(user);
				
				list.add(purchaseOrder);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("발주 목록 조회 중 오류 발생", e);
		} finally {
			dbManager.release(pstmt, rs);
		}
	}
	
}
