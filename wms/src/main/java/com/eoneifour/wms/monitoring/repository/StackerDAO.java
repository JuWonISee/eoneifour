package com.eoneifour.wms.monitoring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.monitoring.model.Stacker;

public class StackerDAO {
	DBManager db = DBManager.getInstance();

	public List<Stacker> selectAll() {
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Stacker> stackerList = new ArrayList<>();

		String sql = "select * from stacker";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Stacker stacker = new Stacker();
				
				stacker.setStacker_id(rs.getInt("stacker_id"));
				stacker.setZ(rs.getInt("z"));
				stacker.setX(rs.getInt("x"));
				stacker.setY(rs.getInt("y"));
				stacker.setStatus(rs.getInt("status"));
				stacker.setPre_fork(rs.getInt("pre_fork"));
				stacker.setOn_product(rs.getInt("on_product"));
				stacker.setCurrent_z(rs.getInt("current_z"));
				stacker.setCurrent_x(rs.getInt("current_x"));

				stackerList.add(stacker);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release(pstmt, rs);
		}

		return stackerList;
	}
}
