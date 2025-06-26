package com.eoneifour.wms.inboundrate.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.inboundrate.model.Rack;

public class RackDAO {
	DBManager db = DBManager.getInstance();
	
	public List<Rack> selectAll(){
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from rack";
		
		List<Rack> rackList = new ArrayList<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Rack rack = new Rack();
				
				rack.setS(rs.getInt("s"));
				rack.setZ(rs.getInt("z"));
				rack.setX(rs.getInt("x"));
				rack.setY(rs.getInt("y"));
				rack.setRack_status(rs.getInt("rack_status"));
				
				rackList.add(rack);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(Integer.toString(e.getErrorCode()));
		} finally {
			db.release(pstmt, rs);
		}
		
		return rackList;
	}
	
	public int selectRackStatusCnt(int statusNum) {
		int cnt = 0;
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select count(*) as cnt 	");
		sql.append("	from rack               		");
		sql.append(" where 1 = 1					");
				
		try {
			if(statusNum != -1) {
				sql.append( "and rack_status = ? 	");
			}
			pstmt = conn.prepareStatement(sql.toString());
			
			if(statusNum != -1) {
				pstmt.setInt(1, statusNum);
			}
			
			rs = pstmt.executeQuery();
			rs.next();
			
			cnt = rs.getInt("cnt");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(Integer.toString(e.getErrorCode()));
		} finally {
			db.release(pstmt, rs);
		}
		return cnt;
	}
	
	public int selectRackStatusCnt(int statusNum, int s) {
		int cnt = 0;
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select count(*) as cnt 	");
		sql.append("	from rack               		");
		sql.append(" where s = ?					");
				
		try {
			if(statusNum != -1) {
				sql.append(" and rack_status = ? 	");
			}
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, s);
			
			if(statusNum != -1) {
				pstmt.setInt(2, statusNum);
			}
			
			rs = pstmt.executeQuery();
			rs.next();
			
			cnt = rs.getInt("cnt");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(Integer.toString(e.getErrorCode()));
		} finally {
			db.release(pstmt, rs);
		}	
		return cnt;
	}
	
	public int selectRackStatus(int s, int z, int x, int y) {
		int cnt = 0;
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select rack_status	");
		sql.append("	from rack           	");
		sql.append(" where s = ?          	");
		sql.append("    and z = ?          	");
		sql.append("    and x = ?          	");
		sql.append("    and y = ?          	");
		
		try {

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, s);
			pstmt.setInt(2, z);
			pstmt.setInt(3, x);
			pstmt.setInt(4, y);
			
			rs = pstmt.executeQuery();
			rs.next();
			
			cnt = rs.getInt("rack_status");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(Integer.toString(e.getErrorCode()));
		} finally {
			db.release(pstmt, rs);
		}	
		return cnt;
	}
	
}
