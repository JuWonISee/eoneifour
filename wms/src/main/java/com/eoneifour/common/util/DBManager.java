package com.eoneifour.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.eoneifour.common.config.PrivateConfig;

public class DBManager {
	private static DBManager instance;
	private Connection con;
	
    private DBManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(PrivateConfig.URL, PrivateConfig.USER, PrivateConfig.PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }
    
    public Connection getConnection() {
		return con;
	}
    
    public void release(Connection con) {
        try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public void release(PreparedStatement pstmt) {
        try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public void release(PreparedStatement pstmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public void release(Connection con, PreparedStatement pstmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
    
    /**
     * DB 접속 상태를 반환하는 메서드.
     * @author 재환
     */
    public boolean isConnected() {
    	try {
			return con != null && !con.isClosed();
		} catch (SQLException e) {
			return false;
		}
    }
}
