package com.eoneifour.shopadmin.user.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.user.model.User;

public class UserDAO {
	DBManager db = DBManager.getInstance();
		
	public List<User> getUserList() {
		List<User> list = new ArrayList<>();
		String sql = "select * from user order by user_id desc";
		
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
	
	public int insertUser(User user) {
		int result = 0;
		String sql = "insert into user(email, password, name, address, address_detail, role) values(?,?,?,?,?,?)";
		
		Connection con = db.getConnetion();
		PreparedStatement pstmt = null; 
		
		try {
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, user.getEmail());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getAddress());
			pstmt.setString(5, user.getAddressDetail());
			pstmt.setInt(6, user.getRole());
			
			result = pstmt.executeUpdate();
			if(result == 0) throw new UserException("사용자 등록에 실패했습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("사용자 등록에 실패했습니다.");
		} finally {
			db.release(pstmt);
		}
		
		return result;
	}
	
	public boolean existByEmail(String email) {
		boolean isExist = false;
		String sql = "select * from user where email like ?";
		
		Connection con = db.getConnetion();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			isExist = rs.next()? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isExist;
	}
}
