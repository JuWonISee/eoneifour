package com.eoneifour.wms.auth.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.auth.model.Admin;

public class AdminDAO {
    // DBManager 인스턴스 생성 (UserDAO 스타일)
    DBManager db = DBManager.getInstance();

    // 회원가입
    public int insert(Admin admin) {
        int result = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO admin(email, password, name) VALUES (?, ?, ?)";
        System.out.println(admin.getEmail()+","+admin.getPassword()+","+admin.getName());

        try {
            con = db.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, admin.getEmail());
            pstmt.setString(2, admin.getPassword());
            pstmt.setString(3, admin.getName());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.release(pstmt);
        }

        return result;
    }

    // 이메일 중복 확인
    public boolean existByEmail(String email) {
        boolean exists = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT 1 FROM admin WHERE email = ?";

        try {
            con = db.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            exists = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.release(pstmt, rs);
        }

        return exists;
    }

    // 로그인 admib@ 1234
    public Admin login(String email, String password) {
        Admin admin = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM admin WHERE email = ? AND password = ?";

        try {
            con = db.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setEmail(rs.getString("email"));
                admin.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.release(pstmt, rs);
        }

        return admin;
    }

  //완 관리자 정보 수정
    public int update(Admin admin) {
        int result = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE admin SET password = ?, name = ? WHERE email = ?";

        try {
            con = db.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, admin.getPassword());
            pstmt.setString(2, admin.getName());
            pstmt.setString(3, admin.getEmail());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.release(pstmt);
        }

        return result;
        
  //완 로그인 DAO부분 추가 ??? 
    }
    public void insertDefaultAdmin() {
        String defaultEmail = "admin@wms.com";
        String defaultPassword = "1234";
        String defaultName = "최고관리자";

        if (!existByEmail(defaultEmail)) {
            Admin admin = new Admin();
            admin.setEmail(defaultEmail);
            admin.setPassword(defaultPassword);
            admin.setName(defaultName);
            insert(admin);
        }
    }
}
