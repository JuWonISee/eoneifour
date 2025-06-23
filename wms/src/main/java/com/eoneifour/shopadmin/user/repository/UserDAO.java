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
	
	// 사용자 목록 전체 조회
	public List<User> getUserList() {
		String sql = "select * from shop_user where status = 0 order by user_id desc";
		
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try {
			List<User> list = new ArrayList<>();
			pstmt = conn.prepareStatement(sql);
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
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("회원 목록 조회 중 오류 발생", e);
		} finally {
			db.release(pstmt, rs);
		}
	}
	
	// userId 기준으로 사용자 1명 조회
	public User getUserById(int userId) {
		String sql = "select * from shop_user where user_id = ? and status = 0";
		
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
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
			throw new UserException("회원 조회 중 오류 발생가 발생했습니다.", e);
		} finally {
		    db.release(pstmt, rs);
		}
	}
	
	// 사용자 등록
	public void insertUser(User user) throws UserException {
		String sql = "insert into shop_user(email, password, name, address, address_detail, role) values(?,?,?,?,?,?)";
		
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null; 
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getEmail());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getAddress());
			pstmt.setString(5, user.getAddressDetail());
			pstmt.setInt(6, user.getRole());
			
			int result = pstmt.executeUpdate();
			if(result == 0) throw new UserException("사용자 등록에 실패했습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("사용자 등록 중 오류가 발생했습니다.", e);
		} finally {
			db.release(pstmt);
		}
	}
	
	// 사용자 수정
	public void updateUser(User user) throws UserException {
		StringBuffer sql = new StringBuffer();
		sql.append("update user ");
		sql.append("set password = ?, ");
		sql.append("name = ?, ");
		sql.append("address = ?, ");
		sql.append("address_detail = ?, ");
		sql.append("role = ? ");
		sql.append("where user_id = ?");
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null; 
		
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, user.getPassword());
			pstmt.setString(2, user.getName());
			pstmt.setString(3, user.getAddress());
			pstmt.setString(4, user.getAddressDetail());
			pstmt.setInt(5, user.getRole());
			pstmt.setInt(6, user.getUserId());
			
			int result = pstmt.executeUpdate();
			if(result == 0) throw new UserException("사용자 수정에 실패했습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("사용자 수정 중 오류가 발생했습니다.", e);
		} finally {
			db.release(pstmt);
		}
	}

	// userId 기준으로 사용자 상태 변경(active:0 -> delete:1)
	public void deleteUser(int userId) {
		String sql = "update shop_user set status = 1 where user_id = ? and status = 0";
		
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			int result = pstmt.executeUpdate();
			if(result == 0) throw new UserException("삭제 대상이 존재하지 않습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
	        throw new UserException("회원 삭제 중 오류가 발생했습니다.", e);
	    } finally {
	        db.release(pstmt);
	    }
	}
	
	// 이메일 중복 여부 확인
	public boolean existByEmail(String email) {
		String sql = "select 1 from shop_user where email = ? and status = 0";
		
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("이메일 중복 확인 중 오류 발생했습니다.", e);
		} finally {
			db.release(pstmt, rs);
		}
	}
}
