package com.eoneifour.shop.view;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.shop.common.config.Config;
import com.eoneifour.shop.product.view.sh_ProductListPage;

public class ProductMenuPanel extends JPanel {
	private sh_ProductListPage sh_productListpage;
	
	public ProductMenuPanel(ShopMainFrame mainFrame) {
		sh_productListpage = mainFrame.sh_productListPage;
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
		setBackground(Color.WHITE);
		
		for(int i =0; i < Config.CATEGORYNAME.length; i++) {
			
			String category = Config.CATEGORYNAME[i]; //Config에서 카테고리명 받아오기
			
			JButton button = new JButton(category); //버튼 생성
			
			ButtonUtil.styleMenuButton(button); //디자인 유틸 적용
			
			button.addActionListener(e -> { //카테고리별 클릭이벤트 생성
				
                switch (category) {
                case "전체":
                	mainFrame.showPage("SH_PRODUCT_LIST", "PRODUCT_MENU");
                	sh_productListpage.showAllProducts();
                    break;
                case "식품":
                	mainFrame.showPage("SH_PRODUCT_LIST", "PRODUCT_MENU");
                	sh_productListpage.showProductsByCategory(1);
                    break;
                case "생활":
                	mainFrame.showPage("SH_PRODUCT_LIST", "PRODUCT_MENU");
                	sh_productListpage.showProductsByCategory(2);
                    break;
                case "스포츠":
                	mainFrame.showPage("SH_PRODUCT_LIST", "PRODUCT_MENU");
                	sh_productListpage.showProductsByCategory(3);
                    break;
                case "반려동물":
                	mainFrame.showPage("SH_PRODUCT_LIST", "PRODUCT_MENU");
                	sh_productListpage.showProductsByCategory(4);
                    break;
            }
				
			});
			
			add(button);
		}
	}
}