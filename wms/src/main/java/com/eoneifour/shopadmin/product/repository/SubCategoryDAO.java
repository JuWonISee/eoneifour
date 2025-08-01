package com.eoneifour.shopadmin.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.product.model.SubCategory;
import com.eoneifour.shopadmin.product.model.TopCategory;

public class SubCategoryDAO {
	DBManager dbManager = DBManager.getInstance();
	
	//하위 카테고리중 , 유저가 선택한 상위 카테고리에 소속된 목록만 가져오기.
	public List selectByTop(TopCategory topcategory) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList(); //size 0
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from shop_sub_category where top_category_id = ?");
		
		con = dbManager.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, topcategory.getTop_category_id());
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				SubCategory subcategory = new SubCategory(); //empty
				subcategory.setSub_category_id(rs.getInt("sub_category_id"));
				subcategory.setName(rs.getString("name"));
				subcategory.setTop_category(topcategory);
				list.add(subcategory);
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
