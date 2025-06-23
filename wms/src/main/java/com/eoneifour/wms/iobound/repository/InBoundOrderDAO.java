package com.eoneifour.wms.iobound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.iobound.model.InBoundOrder;

public class InBoundOrderDAO {
	DBManager db = DBManager.getInstance();
	
	public List<InBoundOrder> getOrderList(){
		String sql = "select * from purchase_order where status = 1";
		
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			List<InBoundOrder> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				InBoundOrder inBoundOrder = new InBoundOrder();
				
				inBoundOrder.setPurchase_order_id(0);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
