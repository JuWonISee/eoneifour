package com.eoneifour.shopadmin.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.shopadmin.common.util.DBManager;
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
		sql.append("select *from top_category");
		pstmt = con.prepareStatement(sql.toString());
		rs=pstmt.executeQuery();
		list = new ArrayList();
		//만일 배열로 RS의 데이터를 담게되면 , 배열의 크기를 명시해야 하는 이유로 , rs.last() , getRow()
		//rs.before() 
		//TopCategory[] list = new TopCategory[];
		//Collection framework ? 객체를 모아서 처리하는데 , 유용한 Api 지원하는 패키지
		//모여진 모습은 총 2가지. 
		//순서 있는 모습 (List, Queue) or 순서 없는 모습 (Set, Map)
		
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
