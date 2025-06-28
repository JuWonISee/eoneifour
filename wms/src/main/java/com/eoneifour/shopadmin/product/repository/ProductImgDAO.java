package com.eoneifour.shopadmin.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.product.model.ProductImg;

public class ProductImgDAO {
	DBManager dbManager = DBManager.getInstance();
	
	public void insertProductImg(ProductImg productImg) {
		Connection con=null;
		PreparedStatement pstmt=null;
		
		con=dbManager.getConnection();
		
		StringBuffer sql=new StringBuffer();
		sql.append("insert into shop_product_img(filename, product_id) values(?,?)");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, productImg.getFilename());
			pstmt.setInt(2, productImg.getProduct().getProduct_id());
			int result =pstmt.executeUpdate();
			if(result <1) {
				throw new UserException("상품 이미지가 등록되지 않았습니다");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("상품 이미지가 등록되지 않았습니다", e);
		}finally {
			dbManager.release(pstmt);
		}
	}
	
	public List<ProductImg> getProductImgs(int productId) {
		List<ProductImg> imgList = new ArrayList<>();
		ProductImg productImg = new ProductImg();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select filename from shop_product_img ");
		sql.append(" where product_id = ?;");

		try {
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setInt(1, productId);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				productImg.setFilename(rs.getString("filename"));
				imgList.add(productImg);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return imgList;
	}
	
	
	public void updateProductImg(ProductImg productImg) throws UserException {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        con = dbManager.getConnection();

	        // 먼저, 해당 상품의 이미지가 존재하는지 확인
	        String checkSql = "SELECT COUNT(*) FROM shop_product_img WHERE product_id = ?";
	        pstmt = con.prepareStatement(checkSql);
	        pstmt.setInt(1, productImg.getProduct().getProduct_id());
	        rs = pstmt.executeQuery();

	        int count = 0;
	        if (rs.next()) {
	            count = rs.getInt(1);
	        }

	        // 리소스 해제
	        dbManager.release(pstmt, rs);

	        if (count == 0) {
	            // 이미지가 없으면 insert
	            insertProductImg(productImg);

	        } else {
	            // 이미지가 있으면 update
	            String updateSql = "UPDATE shop_product_img SET filename = ? WHERE product_id = ?";
	            pstmt = con.prepareStatement(updateSql);
	            pstmt.setString(1, productImg.getFilename());
	            pstmt.setInt(2, productImg.getProduct().getProduct_id());

	            int result = pstmt.executeUpdate();

	            if (result == 0) {
	                throw new UserException("상품 이미지 수정에 실패했습니다");
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new UserException("상품이미지 수정 중 오류가 발생했습니다", e);
	    } finally {
	        dbManager.release(pstmt, rs);
	    }
	}
	

	
}
