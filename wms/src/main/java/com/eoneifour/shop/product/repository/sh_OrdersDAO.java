package com.eoneifour.shop.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shop.product.model.sh_Orders;

public class sh_OrdersDAO {
	
	DBManager dbManager = DBManager.getInstance();
	
	// 1건 등록
	public void insertOrder(sh_Orders order) throws UserException {

		Connection con = null;
		PreparedStatement pstmt = null;

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("insert into shop_orders(total_price, user_id, delivery_address, delivery_address_detail)");
		sql.append(" values(?,?,?,?)");

		try {
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setInt(1, order.getTotal_price());
			pstmt.setInt(2, order.getUser_id());
			pstmt.setString(3, order.getDelivery_address());
			pstmt.setString(4, order.getDelivery_address_detail());
			
			int result = pstmt.executeUpdate();
			if (result == 0) {
				throw new UserException("주문에 실패했습니다");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("주문 중 오류가 발생하였하였습니다.", e);
		} finally {
			dbManager.release(pstmt);
		}
	}
	
	//orderItem table에서 참조할 orders_id 값을 넣기 위한 함수
	public int selectRecentPk() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int pk = 0;

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("select last_insert_id() as orders_id");

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				pk = rs.getInt("orders_id");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return pk;
	}
	
}
