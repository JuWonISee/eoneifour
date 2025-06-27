package com.eoneifour.wms.iobound.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.iobound.model.StockProduct;

public class OutBoundOrderDAO {

    DBManager db = DBManager.getInstance();

    // 출고대기(4) 상태 상품 조회
    public List<StockProduct> selectByStatus(int status) {
        List<StockProduct> list = new ArrayList<>();	
        String sql = "SELECT stock_product_id, product_name, s, z, x, y FROM stock_product WHERE stock_status = ? ORDER BY time ASC";

        Connection conn = db.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                StockProduct sp = new StockProduct();
                sp.setStockprodutId(rs.getInt("stock_product_id"));
                sp.setProductName(rs.getString("product_name"));
                sp.setS(rs.getInt("s"));
                sp.setZ(rs.getInt("z"));
                sp.setX(rs.getInt("x"));
                sp.setY(rs.getInt("y"));

                list.add(sp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("출고 리스트 조회 실패", e);
        } finally {
            db.release(pstmt, rs);
        }

        return list;
    }

    // 상품명 + 출고대기 조건으로 검색
    public List<StockProduct> searchByProductName(String keyword, int status) {
        List<StockProduct> list = new ArrayList<>();
        String sql = "SELECT stock_product_id, product_name, s, z, x, y FROM stock_product WHERE stock_status = ? AND product_name LIKE ?";

        Connection conn = db.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setString(2, "%" + keyword + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                StockProduct sp = new StockProduct();
                sp.setStockprodutId(rs.getInt("stock_product_id"));
                sp.setProductName(rs.getString("product_name"));
                sp.setS(rs.getInt("s"));
                sp.setZ(rs.getInt("z"));
                sp.setX(rs.getInt("x"));
                sp.setY(rs.getInt("y"));

                list.add(sp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("출고 검색 실패", e);
        } finally {
            db.release(pstmt, rs);
        }

        return list;
    }

    // 출고 상태 업데이트 (보통 5 = 출고 중 or 6 = 출고 완료)
    public void updateStatus(int id, int statusNum) {
        Connection conn = db.getConnection();
        PreparedStatement pstmt = null;

        try {
            String sql = "UPDATE stock_product SET stock_status = ? WHERE stock_product_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, statusNum);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("출고 상태 업데이트 실패", e);
        } finally {
            db.release(pstmt);
        }
    }
}