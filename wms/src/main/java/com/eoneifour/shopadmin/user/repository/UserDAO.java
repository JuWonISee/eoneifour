package com.eoneifour.shopadmin.user.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.user.model.User;

public class UserDAO {
	DBManager db = DBManager.getInstance();
	
	public List<User> getUserList() {
		List<User> list = new ArrayList<>();
		String sql = "select * from user";
		
		Connection con = db.getConnetion();
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				User user = new User();
				
				user.setUserId(rs.getInt("user_id"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setAddress(rs.getString("address"));
				user.setAddressDetail(rs.getString("address_detail"));
				user.setRole(rs.getInt("role"));
				user.setStatus(rs.getInt("status"));
				user.setCreatedAt(rs.getDate("created_at"));
				
				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release(pstmt, rs);
		}
		 
		return list;
	}
}
