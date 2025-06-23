package com.eoneifour.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.user.model.User;

public class LoginDAO {
	DBManager db = DBManager.getInstance();
	
	public User findByEmailAndPwd(String email, String password) {
		String sql = "select * from shop_user where email = ? and password = ? AND status = 0";
		
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setAddress(rs.getString("address"));
				user.setAddressDetail(rs.getString("address_detail"));
				user.setRole(rs.getInt("role"));
				user.setStatus(rs.getInt("status"));
				user.setCreatedAt(rs.getDate("created_at"));
				
				return user;
			} else {
	            throw new UserException("해당 회원이 존재하지 않습니다.");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("로그인 시도 중 오류 발생했습니다.", e);
		} finally {
			db.release(pstmt, rs);
		}
	}
}
