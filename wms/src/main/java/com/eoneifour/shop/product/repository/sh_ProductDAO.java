package com.eoneifour.shop.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.shop.product.model.sh_Product;
import com.eoneifour.shop.product.model.sh_SubCategory;
import com.eoneifour.shop.product.model.sh_TopCategory;

public class sh_ProductDAO {
    DBManager dbManager = DBManager.getInstance();

    // 전체 상품 리스트 조회
    public List<sh_Product> getProductList() {
        List<sh_Product> productList = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    p.product_id, p.name, p.price, p.status, ");
        sql.append("    (SELECT filename FROM shop_product_img i ");
        sql.append("     WHERE i.product_id = p.product_id LIMIT 1) AS filename ");
        sql.append("FROM shop_product p ");
        sql.append("ORDER BY p.product_id DESC");

        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                sh_Product product = new sh_Product();
                product.setProduct_id(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getInt("price"));
                product.setStatus(rs.getInt("status"));
                product.setFilename(rs.getString("filename")); // 이미지 파일명 포함

                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("상품 리스트 조회 중 오류 발생", e);
        } finally {
            dbManager.release(pstmt, rs);
        }

        return productList;
    }

    // Product_id 기준으로 상품 1건 조회
    public sh_Product getProduct(int productId) {
        sh_Product product = new sh_Product();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        con = dbManager.getConnection();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    p.product_id, t.top_category_id, t.name AS top_category_name, ");
        sql.append("    s.sub_category_id, s.name AS sub_category_name, ");
        sql.append("    p.brand_name, p.name AS product_name, p.price, p.detail, p.stock_quantity, ");
        sql.append("    (SELECT filename FROM shop_product_img i WHERE i.product_id = p.product_id LIMIT 1) AS filename ");
        sql.append("FROM shop_product p ");
        sql.append("JOIN shop_sub_category s ON p.sub_category_id = s.sub_category_id ");
        sql.append("JOIN shop_top_category t ON s.top_category_id = t.top_category_id ");
        sql.append("WHERE p.product_id = ?");

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
                product.setFilename(rs.getString("filename")); // 이미지 파일명 추가

                sh_TopCategory topCategory = new sh_TopCategory();
                topCategory.setTop_category_id(rs.getInt("top_category_id"));
                topCategory.setName(rs.getString("top_category_name"));

                sh_SubCategory subCategory = new sh_SubCategory();
                subCategory.setSub_category_id(rs.getInt("sub_category_id"));
                subCategory.setName(rs.getString("sub_category_name"));

                subCategory.setTop_category(topCategory);
                product.setSub_category(subCategory);
                product.setTop_category(topCategory);

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

    // 카테고리별 상품 리스트 조회 (상위 카테고리 기준)
    public List<sh_Product> getProductsByTopCategory(int topCategoryId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<sh_Product> list = new ArrayList<>();

        con = dbManager.getConnection();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    p.product_id, p.name AS product_name, p.brand_name, p.price, p.status, p.stock_quantity, ");
        sql.append("    t.name AS top_category_name, ");
        sql.append("    (SELECT filename FROM shop_product_img i WHERE i.product_id = p.product_id LIMIT 1) AS filename ");
        sql.append("FROM shop_product p ");
        sql.append("JOIN shop_sub_category s ON p.sub_category_id = s.sub_category_id ");
        sql.append("JOIN shop_top_category t ON s.top_category_id = t.top_category_id ");
        sql.append("WHERE t.top_category_id = ? ");
        sql.append("ORDER BY p.product_id DESC");

        try {
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, topCategoryId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                sh_Product product = new sh_Product();
                product.setProduct_id(rs.getInt("product_id"));
                product.setName(rs.getString("product_name"));
                product.setBrand_name(rs.getString("brand_name"));
                product.setPrice(rs.getInt("price"));
                product.setStatus(rs.getInt("status"));
                product.setStock_quantity(rs.getInt("stock_quantity"));
                product.setFilename(rs.getString("filename")); // 이미지 파일명 추가

                // 카테고리 정보
                sh_TopCategory topCategory = new sh_TopCategory();
                topCategory.setName(rs.getString("top_category_name"));
                sh_SubCategory subCategory = new sh_SubCategory();
                subCategory.setTop_category(topCategory);
                product.setSub_category(subCategory);
                product.setTop_category(topCategory);

                list.add(product);
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("카테고리 상품 조회 중 오류 발생", e);
        } finally {
            dbManager.release(pstmt, rs);
        }
    }
}
