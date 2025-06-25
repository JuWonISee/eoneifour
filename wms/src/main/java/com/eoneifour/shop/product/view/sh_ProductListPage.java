package com.eoneifour.shop.product.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.eoneifour.shop.product.model.sh_Product;
import com.eoneifour.shop.product.repository.sh_ProductDAO;
import com.eoneifour.shop.product.repository.sh_ProductImgDAO;
import com.eoneifour.shop.view.ShopMainFrame;

public class sh_ProductListPage extends JPanel{
	private ShopMainFrame mainFrame;
	private sh_ProductDAO sh_productDAO;
	private sh_ProductImgDAO sh_productImgDAO;
	private int productId;
	
	public sh_ProductListPage(ShopMainFrame mainFrame){
		this.mainFrame = mainFrame;
		sh_productDAO = new sh_ProductDAO();
		sh_productImgDAO = new sh_ProductImgDAO();
		
        setLayout(new BorderLayout());
        

        // 상품 전체 패널 (GridLayout 사용: 4칸씩)
        JPanel productContainer = new JPanel();
        productContainer.setLayout(new GridLayout(0, 4, 20, 30)); // 열 4개, 여백 설정

        // DB에서 상품 리스트 받아오기
        List<sh_Product> productList = sh_productDAO.getProductList();

        for (sh_Product product : productList) {
            JPanel productPanel = createProductPanel(product);
            productContainer.add(productPanel);
        }

        // 스크롤 가능하게 설정
        JScrollPane scrollPane = new JScrollPane(productContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createProductPanel(sh_Product product) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(280, 300));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.setBackground(Color.WHITE);

        // 상품 이미지
        JLabel imgLabel = new JLabel();
        
        ImageIcon icon = new ImageIcon(sh_productImgDAO.getProductImg(productId).toString());
        Image scaledImg = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
        imgLabel.setIcon(new ImageIcon(scaledImg));
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        imgLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        // 상품 이름
        JLabel nameLabel = new JLabel(product.getName(), JLabel.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        // 가격
        JLabel priceLabel = new JLabel(product.getPrice() + " 원", JLabel.CENTER);
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        priceLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        // 텍스트 영역 구성
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(nameLabel);
        textPanel.add(priceLabel);

        panel.add(imgLabel, BorderLayout.NORTH);
        panel.add(textPanel, BorderLayout.CENTER);

        return panel;
    }
	

}
