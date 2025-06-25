package com.eoneifour.shop.view;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.eoneifour.common.util.ButtonUtil;

public class ProductMenuPanel extends JPanel {
	public ProductMenuPanel(ShopMainFrame mainFrame) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
		setBackground(Color.WHITE);

		JButton productListBtn = new JButton("전체");
		ButtonUtil.styleMenuButton(productListBtn);
		productListBtn.addActionListener(e -> mainFrame.showPage("PRODUCT_LIST", "PRODUCT_MENU"));
		
		JButton foodBtn = new JButton("식품");
		ButtonUtil.styleMenuButton(foodBtn);
		foodBtn.addActionListener(e -> mainFrame.showPage("PRODUCT_FOOD", "PRODUCT_MENU"));
		
		JButton dailyBtn = new JButton("생활");
		ButtonUtil.styleMenuButton(dailyBtn);
		dailyBtn.addActionListener(e -> mainFrame.showPage("PRODUCT_DAILY", "PRODUCT_MENU"));
		
		JButton sportsBtn = new JButton("스포츠");
		ButtonUtil.styleMenuButton(sportsBtn);
		sportsBtn.addActionListener(e -> mainFrame.showPage("PRODUCT_SPORTS", "PRODUCT_MENU"));
		
		JButton petBtn = new JButton("반려동물");
		ButtonUtil.styleMenuButton(petBtn);
		petBtn.addActionListener(e -> mainFrame.showPage("PRODUCT_PET", "PRODUCT_MENU"));

		add(productListBtn);
		add(foodBtn);
		add(dailyBtn);
		add(sportsBtn);
		add(petBtn);
	}
}