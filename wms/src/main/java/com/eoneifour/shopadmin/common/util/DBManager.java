package com.eoneifour.shopadmin.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.eoneifour.common.config.PrivateConfig;

public class DBManager {
	
	private static DBManager instance;
	private Connection con;

	
	//아무도 직접 인스턴스를 생성하지 못하게 생성자의 접근제한을 막아버린다...
	private DBManager() {
		try {

			// 1) 드라이버 로드
			Class.forName("com.mysql.cj.jdbc.Driver");

			// 2) 접속
			// 서버 DB로 접속하게 변경. (상수는 대문자로 부탁드립니다.)
			con = DriverManager.getConnection(PrivateConfig.URL, PrivateConfig.USER, PrivateConfig.PASS);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public static DBManager getInstance() {
		//만일 인스턴스가 존재하지 않으면 이 메서드에서 인스턴스 생성해줌
		if(instance ==null) {
			instance = new DBManager();
		}
		return instance;
	}
	
	public Connection getConnection() {
		return con;
	}

	// 데이터 베이스 관련된 자원을 해제하는 메서드
	
	public void release(Connection con) { 

		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void release(PreparedStatement pstmt) { // DMA(insert , update , delete)

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void release(PreparedStatement pstmt, ResultSet rs) { // Select
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void release(Connection con, PreparedStatement pstmt, ResultSet rs) { // Select

		
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}