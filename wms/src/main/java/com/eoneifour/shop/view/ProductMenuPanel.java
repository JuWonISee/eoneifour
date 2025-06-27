package com.eoneifour.shop.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.shop.common.config.Config;
import com.eoneifour.shop.product.view.sh_ProductListPage;

public class ProductMenuPanel extends JPanel {
	private ShopMainFrame mainFrame;
	private sh_ProductListPage sh_productListpage;
	public List<JButton> menuButtons;
	public JButton allBtn;
	
	public ProductMenuPanel(ShopMainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.menuButtons = mainFrame.menuButtons;
		sh_productListpage = mainFrame.sh_productListPage;
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
		setBackground(Color.WHITE);
		
		for(int i =0; i < Config.CATEGORYNAME.length; i++) {
			
			String category = Config.CATEGORYNAME[i]; //Config에서 카테고리명 받아오기
			String categoryKey = Config.CATEGORYKEY[i];
			JButton button = createMenuButton(category, categoryKey, menuButtons);
			
			if ("전체".equals(category)) {
				allBtn = button; //  "전체" 버튼 저장
			}
			
			button.addActionListener(e -> { //카테고리별 클릭이벤트 생성
				ButtonUtil.applyMenuActiveStyle(menuButtons, button);
				
                switch (category) {
                case "전체":
                	allBtn = button;
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
		
		if (allBtn != null) {
			SwingUtilities.invokeLater(() -> ButtonUtil.applyMenuActiveStyle(menuButtons, allBtn));
		}
	}
	
	public JButton createMenuButton(String title, String pageKey, List<JButton> menuButtons) {
	    JButton btn = new JButton(title);
	    ButtonUtil.styleMenuButton(btn);
	    menuButtons.add(btn);
	    return btn;
	}
	

    public JButton getAllButton() {
        return allBtn;
    }
	
	
}