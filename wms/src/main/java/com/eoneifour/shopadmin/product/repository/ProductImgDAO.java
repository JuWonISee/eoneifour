package com.eoneifour.shopadmin.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.shopadmin.common.exception.ProductException;
import com.eoneifour.shopadmin.common.exception.ProductImgException;
import com.eoneifour.shopadmin.common.util.DBManager;
import com.eoneifour.shopadmin.product.model.ProductImg;

public class ProductImgDAO {
	DBManager dbManager = DBManager.getInstance();
	
	public void insertProductImg(ProductImg productImg) {
		Connection con=null;
		PreparedStatement pstmt=null;
		
		con=dbManager.getConnection();
		
		StringBuffer sql=new StringBuffer();
		sql.append("insert into product_img(filename, product_id) values(?,?)");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, productImg.getFilename());
			pstmt.setInt(2, productImg.getProduct().getProduct_id());
			int result =pstmt.executeUpdate();
			if(result <1) {
				throw new ProductImgException("상품 이미지가 등록되지 않았습니다");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ProductException("상품 이미지가 등록되지 않았습니다", e);
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
		sql.append("select filename from product_img ");
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
	
	
	public void updateProductImg(ProductImg productImg) throws ProductException{
		Connection con = null;
		PreparedStatement pstmt = null;
		
	    con = dbManager.getConnection();

	    StringBuffer sql = new StringBuffer();
	    sql.append("UPDATE product_img ");
	    sql.append(" SET filename = ?");
	    sql.append(" WHERE product_id = ?");
		
        try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, productImg.getFilename());
			pstmt.setInt(2, productImg.getProduct_img_id()); 
			
			int result = pstmt.executeUpdate();
			
			if (result == 0) {
				throw new ProductImgException("상품 이미지 수정에 실패했습니다");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ProductImgException("상품이미지 수정 중 오류가 발생했습니다", e);
		} finally {
			dbManager.release(pstmt);
		}

		
	}
	

	
}
