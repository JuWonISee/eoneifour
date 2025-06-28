package com.eoneifour.shop.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shop.product.model.sh_Orders;

public class sh_OrdersDAO {
	
	DBManager dbManager = DBManager.getInstance();
	
	// 1건 등록
	public void insertOrder(sh_Orders order, int orderQuantity) throws UserException {
	    Connection con = dbManager.getConnection();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        // 상품 상태 및 재고 확인
	        String checkSql = "select status, stock_quantity from shop_product where product_id = ?";
	        pstmt = con.prepareStatement(checkSql);
	        pstmt.setInt(1, order.getProduct_id());
	        rs = pstmt.executeQuery();

	        if (!rs.next()) throw new UserException("상품 정보가 존재하지 않습니다.");

	        int prdstatus = rs.getInt("status");
	        int prdQuantity = rs.getInt("stock_quantity");
	        pstmt.close(); rs.close();

	        //상품 상태 확인
	        if (prdstatus == 1) throw new UserException("상품이 비활성화 상태입니다.");
	        //상품 재고 확인
	        if (prdQuantity < orderQuantity) throw new UserException("요청 수량이 재고보다 많습니다.");

	        // 주문 테이블 insert + 생성된 orders_id 반환 (return_generated_keys)
	        StringBuffer sql = new StringBuffer();
	        sql.append("insert into shop_orders(total_price, user_id, delivery_address, delivery_address_detail) ");
	        sql.append("values(?,?,?,?)");
	        pstmt = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
	        pstmt.setInt(1, order.getTotal_price());
	        pstmt.setInt(2, order.getUser_id());
	        pstmt.setString(3, order.getDelivery_address());
	        pstmt.setString(4, order.getDelivery_address_detail());

	        int orderResult = pstmt.executeUpdate();
	        if (orderResult == 0) throw new UserException("상품 주문에 실패했습니다.");

	        rs = pstmt.getGeneratedKeys();
	        int generatedOrderId = -1;
	        if (rs.next()) {
	            generatedOrderId = rs.getInt(1);
	        } else {
	            throw new UserException("주문 ID 생성에 실패했습니다.");
	        }
	        pstmt.close(); rs.close();

	        // 주문 아이템 insert
	        StringBuffer sql2 = new StringBuffer();
	        sql2.append("insert into shop_order_item(quantity, price, orders_id, product_id) ");
	        sql2.append("values(?,?,?,?)");

	        pstmt = con.prepareStatement(sql2.toString());
	        pstmt.setInt(1, order.getQuantity());
	        pstmt.setInt(2, order.getPrice());
	        pstmt.setInt(3, generatedOrderId);  // 실제 생성된 orders_id 사용
	        pstmt.setInt(4, order.getProduct_id());

	        int orderItemResult = pstmt.executeUpdate();
	        pstmt.close();

	        if (orderItemResult == 0) throw new UserException("상품 아이템 주문에 실패했습니다.");

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new UserException("상품 주문 중 오류가 발생하였습니다.", e);
	    } finally {
	        dbManager.release(pstmt, rs);
	    }
	}
	
}
