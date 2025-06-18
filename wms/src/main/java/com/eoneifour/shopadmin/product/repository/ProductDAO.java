package com.eoneifour.shopadmin.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.shopadmin.common.exception.ProductException;
import com.eoneifour.shopadmin.common.util.DBManager;
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
			// e.printStackTrace();처리만 해버리면 바깥쪽 즉 유저가 사용하는 프로그램에서는
			// 에러의 원인을 알 수 없으므로 , 신뢰성이 떨어짐. 따라서 에러가 발생한다면 , 이영엑서만 처리를
			// 국한 시키지 말고 , 외부 영역까지 에러 원인을 전달
			e.printStackTrace();
			throw new ProductException("등록에 실패하였습니다.", e);
		} finally {
			dbManager.release(pstmt);
		}
	}

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

			while (rs.next()) {
				Product product = new Product();
				product.setProduct_id(rs.getInt("product_id"));

				SubCategory subCategory = new SubCategory();
				TopCategory topCategory = new TopCategory();
				topCategory.setName(rs.getString("t.name"));
				subCategory.setTop_category(topCategory);
				product.setSub_category(subCategory);

				product.setBrand_name(rs.getString("brand_name"));
				product.setName(rs.getString("p.name"));
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
}