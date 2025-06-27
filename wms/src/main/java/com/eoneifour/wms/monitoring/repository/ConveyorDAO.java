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

	public List<Conveyor> selectAll() {
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<Conveyor> conveyorList = new ArrayList<>();

		String sql = "select * from conveyor";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
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

	public int selectById(int id) {
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select on_product from conveyor WHERE conveyor_id = ?";
		int a = -1;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				a = rs.getInt("on_product");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(Integer.toString(e.getErrorCode()));
		} finally {
			db.release(pstmt, rs);
		}
		return a;
	}

	public void updateOnProductByIdWithPos(int s, int z, int x, int y) {
		Connection conn = db.getConnection();
		PreparedStatement pstmt = null;

		try {
			String sql = "UPDATE conveyor SET s = ?, z = ?, x = ?, y = ?, on_product = 1 WHERE conveyor_id = 301";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, s);
			pstmt.setInt(2, z);
			pstmt.setInt(3, x);
			pstmt.setInt(4, y);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("컨베이어 상태 업데이트 실패", e);
		} finally {
			db.release(pstmt);
		}
	}

}
