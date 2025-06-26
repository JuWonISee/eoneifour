package com.eoneifour.shopadmin.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.product.model.TopCategory;

public class TopCategoryDAO {
	
	DBManager dbManager = DBManager.getInstance();
	
	public List selectAll() {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ArrayList list = new ArrayList();
	con = dbManager.getConnection();
	try {
		StringBuffer sql = new StringBuffer();
		sql.append("select *from shop_top_category");
		pstmt = con.prepareStatement(sql.toString());
		rs=pstmt.executeQuery();
		list = new ArrayList();
		
		while(rs.next()) {
		TopCategory topcategory = new TopCategory();
		topcategory.setTop_category_id(rs.getInt("top_category_id"));
		topcategory.setName(rs.getString("name"));
		list.add(topcategory);
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally {
		dbManager.release(pstmt, rs);
		
	}
	
	return list;
	
}

}
