package com.eoneifour.shopadmin.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.product.model.SubCategory;
import com.eoneifour.shopadmin.product.model.TopCategory;

public class ProductDAO {
	DBManager dbManager = DBManager.getInstance();

	// 전체 상품 리스트 조회
	public List getProductList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<Product> list = new ArrayList<>();

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("select product_id, t.name AS top_category_name, brand_name, p.name AS product_name, price, p.status, stock_quantity ");
		sql.append(" from shop_top_category t , shop_sub_category s , shop_product p");
		sql.append(" where t.top_category_id = s.top_category_id and");
		sql.append(" s.sub_category_id = p.sub_category_id ");
		sql.append(" order by product_id desc");

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
				product.setStatus(rs.getInt("status"));
				product.setStock_quantity(rs.getInt("stock_quantity"));

				list.add(product);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("상품 목록 조회 중 오류 발생", e);
		} finally {
			dbManager.release(pstmt, rs);
		}

	}

	// Product_id 기준으로 상품 1건 조회
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
		sql.append(" from shop_product p");
		sql.append(" join shop_sub_category s on p.sub_category_id = s.sub_category_id");
		sql.append(" join shop_top_category t on s.top_category_id = t.top_category_id");
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

				return product;
			} else {
				throw new UserException("해당 상품이 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("상품 조회 중 오류가 발생했습니다.", e);
		} finally {
			dbManager.release(pstmt, rs);
		}

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

	// 1건 등록
	public void insertProduct(Product product) throws UserException {

		Connection con = null;
		PreparedStatement pstmt = null;

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("insert into shop_product(name, brand_name, price,detail, stock_quantity, sub_category_id)");
		sql.append(" values(?,?,?,?,?,?)");

		try {
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setString(1, product.getName());
			pstmt.setString(2, product.getBrand_name());
			pstmt.setInt(3, product.getPrice());
			pstmt.setString(4, product.getDetail());
			pstmt.setInt(5, product.getStock_quantity());
			pstmt.setInt(6, product.getSub_category().getSub_category_id());

			int result = pstmt.executeUpdate();
			if (result == 0) {
				throw new UserException("상품 등록에 실패했습니다");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("상품 등록 중 오류가 발생하였하였습니다.", e);
		} finally {
			dbManager.release(pstmt);
		}
	}

	// 상품 1건 수정
	public void updateProduct(Product product) throws UserException {
		Connection con = null;
		PreparedStatement pstmt = null;

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE shop_product ");
		sql.append("SET name = ?, brand_name = ?, price = ?, detail = ?, sub_category_id = ? ");
		sql.append("WHERE product_id = ?");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, product.getName());
			pstmt.setString(2, product.getBrand_name());
			pstmt.setInt(3, product.getPrice());
			pstmt.setString(4, product.getDetail());
			pstmt.setInt(5, product.getSub_category().getSub_category_id());
			pstmt.setInt(6, product.getProduct_id());

			int result = pstmt.executeUpdate();
			if (result == 0) {
				throw new UserException("상품 수정에 실패했습니다");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("상품 수정 중 오류가 발생했습니다.", e);
		} finally {
			dbManager.release(pstmt);
		}
	}

	// 상품 등록시 상품명 중복 여부 확인
	public boolean existByProductName(String productName) {
		String sql = "select 1 from shop_product where name = ?";

		Connection conn = dbManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productName);
			rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("상품명 중복 확인 중 오류 발생했습니다.", e);
		} finally {
			dbManager.release(pstmt, rs);
		}
	}

	// 상품 수정 시 , 현재 조회된 상품을 제외한 상품들중 상품명 중복 여부 확인

	public boolean existByProductNameExceptCurrent(String name, int productId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean exists = false;

		try {
			con = dbManager.getConnection();
			String sql = "SELECT COUNT(*) FROM shop_product WHERE name = ? AND product_id != ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, productId);
			rs = pstmt.executeQuery();

			if (rs.next() && rs.getInt(1) > 0) {
				exists = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return exists;
	}

	// 상품 상태 Togle (상품 상태가1이라면 0으로 , 0이라면 1로)
	public void switchProductStatus(int productId) throws UserException {
		Product product = new Product();
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE shop_product ");
		sql.append("SET status = 1 - status ");
		sql.append(" WHERE product_id = ?");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, productId);

			result = pstmt.executeUpdate();
			if (result == 0) {
				throw new UserException("상품이 삭제 되지 않았습니다");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("상품 삭제 중 오류가 발생했습니다.", e);
		} finally {
			dbManager.release(pstmt);
		}
	}
	
	public void updateStock_quantity(Product product , int quantity) throws UserException{
		Connection con = null;
		PreparedStatement pstmt = null;

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE shop_product ");
		sql.append("SET stock_quantity = ? ");
		sql.append(" WHERE product_id = ? ");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, product.getStock_quantity() + quantity);
			pstmt.setInt(2, product.getProduct_id());

			int result = pstmt.executeUpdate();
			if (result == 0) {
				throw new UserException("재고 수정에 실패했습니다");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("재고 수정 중 오류가 발생했습니다.", e);
		} finally {
			dbManager.release(pstmt);
		}
	}
	
	public List<Product> serchByKeyword(String keyword){
		Connection con = null;
		PreparedStatement pstmt = null;
		List<Product> list = new ArrayList<>();
		ResultSet rs = null;
		
		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT product_id, t.name AS top_category_name, brand_name, ");
		sql.append("p.name AS product_name, price, p.status, stock_quantity ");
		sql.append("FROM shop_product p ");
		sql.append("JOIN shop_sub_category s ON p.sub_category_id = s.sub_category_id ");
		sql.append("JOIN shop_top_category t ON s.top_category_id = t.top_category_id ");
		sql.append("WHERE (t.name LIKE ? OR p.name LIKE ?)");
		
		try {
			pstmt = con.prepareStatement(sql.toString());

			
	        String likeKeyword = "%" + keyword + "%";
	        pstmt.setString(1, likeKeyword);
	        pstmt.setString(2, likeKeyword);
	        
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
				product.setStatus(rs.getInt("status"));
				product.setStock_quantity(rs.getInt("stock_quantity"));

				list.add(product);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("상품 목록 조회 중 오류 발생", e);
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		
	}
	
}