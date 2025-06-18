package com.eoneifour.shopadmin.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.shopadmin.common.util.DBManager;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.product.model.SubCategory;
import com.eoneifour.shopadmin.product.model.TopCategory;

public class ProductDAO {
	DBManager dbManager = DBManager.getInstance();
	
	public List getProductList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<Product> list = new ArrayList();
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select product_id, t.name, brand_name, p.name, price, stock_quantity ");
		sql.append(" from top_category t , sub_category s , product p");
		sql.append(" where t.top_category_id = s.top_category_id and");
		sql.append(" s.sub_category_id = p.sub_category_id");

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Product product = new Product();
				product.setProduct_id(rs.getInt("product_id"));
				
				SubCategory subCategory = new SubCategory();
				TopCategory topCategory = new TopCategory();
				topCategory.setName(rs.getString("t.name"));
				subCategory.setTopcategory(topCategory);
				product.setSubCategory(subCategory);
				
				product.setBrand_name(rs.getString("brand_name"));
				product.setName(rs.getString("p.name"));
				product.setPrice(rs.getInt("price"));
				product.setStock_quantity(rs.getInt("stock_quantity"));

				list.add(product);
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt,rs);
		}
	
		return list;
	}
}
