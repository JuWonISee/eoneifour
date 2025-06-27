package com.eoneifour.wms.inbound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.inboundrate.model.Rack;
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
}
