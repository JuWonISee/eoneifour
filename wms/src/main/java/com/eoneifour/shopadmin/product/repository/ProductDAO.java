package com.eoneifour.shopadmin.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.common.exception.ProductException;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.product.model.SubCategory;
import com.eoneifour.shopadmin.product.model.TopCategory;

public class ProductDAO {
	DBManager dbManager = DBManager.getInstance();

	// 1건 등록
	public void insert(Product product) throws ProductException {

		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0; // 쿼리 실행 성공 여부 결정짓는 변수

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("insert into product(name, brand_name, price,detail, stock_quantity, sub_category_id)");
		sql.append(" values(?,?,?,?,?,?)");

		try {
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setString(1, product.getName());
			pstmt.setString(2, product.getBrand_name());
			pstmt.setInt(3, product.getPrice());
			pstmt.setString(4, product.getDetail());
			pstmt.setInt(5, product.getStock_quantity());
			pstmt.setInt(6, product.getSub_category().getSub_category_id());

			result = pstmt.executeUpdate();
			if (result == 0) {
				throw new ProductException("등록이 되지 않았습니다");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ProductException("등록에 실패하였습니다.", e);
		} finally {
			dbManager.release(pstmt);
		}
	}

	public Product getProduct(int productId) {
		Product product = new Product();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(" p.product_id, t.top_category_id, t.name AS top_category_name");
		sql.append(" , s.sub_category_id, s.name AS sub_category_name, ");
		sql.append(" p.brand_name, p.name AS product_name, p.price, p.detail, p.stock_quantity");
		sql.append(" from product p");
		sql.append(" join sub_category s on p.sub_category_id = s.sub_category_id");
		sql.append(" join top_category t on s.top_category_id = t.top_category_id");
		sql.append(" where p.product_id = ?;");

		try {
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setInt(1, productId);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				product.setProduct_id(rs.getInt("product_id"));
				product.setBrand_name(rs.getString("brand_name"));
				product.setName(rs.getString("product_name"));
				product.setPrice(rs.getInt("price"));
				product.setDetail(rs.getString("detail"));
				product.setStock_quantity(rs.getInt("stock_quantity"));
				
				TopCategory topCategory = new TopCategory();
				topCategory.setTop_category_id(rs.getInt("top_category_id"));
				topCategory.setName(rs.getString("top_category_name"));
				
				SubCategory subCategory = new SubCategory();
				subCategory.setSub_category_id(rs.getInt("sub_category_id"));
				subCategory.setName(rs.getString("sub_category_name"));
				
				subCategory.setTop_category(topCategory);
				product.setSub_category(subCategory);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return product;
	}

	// 전체 상품 리스트 가져오기
	public List getProductList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<Product> list = new ArrayList<>();

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("select product_id, t.name AS top_category_name, brand_name, p.name AS product_name, price, stock_quantity ");
		sql.append(" from top_category t , sub_category s , product p");
		sql.append(" where t.top_category_id = s.top_category_id and");
		sql.append(" s.sub_category_id = p.sub_category_id");
		sql.append(" order by product_id asc");

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setProduct_id(rs.getInt("product_id"));

				SubCategory subCategory = new SubCategory();
				TopCategory topCategory = new TopCategory();
				topCategory.setName(rs.getString("top_category_name"));
				subCategory.setTop_category(topCategory);
				product.setSub_category(subCategory);

				product.setBrand_name(rs.getString("brand_name"));
				product.setName(rs.getString("product_name"));
				product.setPrice(rs.getInt("price"));
				product.setStock_quantity(rs.getInt("stock_quantity"));

				list.add(product);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return list;
	}

	// product_img table에서 참조할 product_id 값을 넣기 위한 함수
	public int selectRecentPk() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int pk = 0;

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("select last_insert_id() as product_id");

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				pk = rs.getInt("product_id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return pk;
	}
	
	public void editProduct(Product product) throws ProductException {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    int result = 0;

	    con = dbManager.getConnection();

	    StringBuffer sql = new StringBuffer();
	    sql.append("UPDATE product ");
	    sql.append("SET name = ?, brand_name = ?, price = ?, detail = ?, stock_quantity = ?, sub_category_id = ? ");
	    sql.append("WHERE product_id = ?");

	    try {
	        pstmt = con.prepareStatement(sql.toString());
	        pstmt.setString(1, product.getName());
	        pstmt.setString(2, product.getBrand_name());
	        pstmt.setInt(3, product.getPrice());
	        pstmt.setString(4, product.getDetail());
	        pstmt.setInt(5, product.getStock_quantity());
	        pstmt.setInt(6, product.getSub_category().getSub_category_id());
	        pstmt.setInt(7, product.getProduct_id()); 

	        result = pstmt.executeUpdate();
	        if (result == 0) {
	            throw new ProductException("수정이 되지 않았습니다");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new ProductException("수정에 실패하였습니다.", e);
	    } finally {
	        dbManager.release(pstmt);
	    }
	}
	
	
	
	
}