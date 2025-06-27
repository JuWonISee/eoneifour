package com.eoneifour.shop.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shop.product.model.sh_OrderItem;

public class sh_OrderItemDAO {
	
	DBManager dbManager = DBManager.getInstance();
	
	// 1건 등록
	public void insertOrderItem(sh_OrderItem orderItem) throws UserException {

		Connection con = null;
		PreparedStatement pstmt = null;

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("insert into shop_order_item(quantity, price, orders_id, product_id)");
		sql.append(" values(?,?,?,?)");

		try {
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setInt(1, orderItem.getQuantity());
			pstmt.setInt(2, orderItem.getPrice());
			pstmt.setInt(3, orderItem.getOrders_id());
			pstmt.setInt(4, orderItem.getProduct_id());
			
			int result = pstmt.executeUpdate();
			if (result == 0) {
				throw new UserException("주문 등록에 실패했습니다");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("주문 등록 중 오류가 발생하였하였습니다.", e);
		} finally {
			dbManager.release(pstmt);
		}
	}
}
