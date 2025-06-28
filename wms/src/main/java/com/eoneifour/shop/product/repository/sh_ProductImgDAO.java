package com.eoneifour.shop.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shop.product.model.sh_ProductImg;


public class sh_ProductImgDAO {
	DBManager dbManager = DBManager.getInstance();
	
	public sh_ProductImg getProductImg(int productId) {
		sh_ProductImg productImg = new sh_ProductImg();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select filename from shop_product_img ");
		sql.append(" where product_id = ?");
		sql.append(" limit 1");

		try {
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setInt(1, productId);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				productImg.setFilename(rs.getString("filename"));
			}
//			else {
//				throw new UserException("해당 상품은 사진이 존재하지 않습니다.");
//			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("사진 조회 중 오류 발생가 발생했습니다.", e);
		} finally {
			dbManager.release(pstmt, rs);
		}
		return productImg;
	}
}
