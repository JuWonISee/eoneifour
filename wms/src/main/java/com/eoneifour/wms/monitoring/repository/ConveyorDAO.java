package com.eoneifour.wms.monitoring.repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.monitoring.model.Conveyor;

public class ConveyorDAO {
	DBManager db = DBManager.getInstance();
	
	public List<Conveyor> selectAll(){
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<Conveyor> conveyorList = new ArrayList<>();
		
		String sql = "select * from conveyor";
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Conveyor conveyor = new Conveyor();
				
				conveyor.setConveyor_id(rs.getInt("conveyor_id"));
				conveyor.setS(rs.getInt("s"));
				conveyor.setS(rs.getInt("z"));
				conveyor.setS(rs.getInt("x"));
				conveyor.setS(rs.getInt("y"));
				conveyor.setOn_product(rs.getInt("on_product"));
				
				conveyorList.add(conveyor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(Integer.toString(e.getErrorCode()));
		} finally {
			db.release(pstmt, rs);
		}
		
		return conveyorList;
	}
	
}
