package com.eoneifour.shop.view;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.eoneifour.common.util.ButtonUtil;

public class MypageMenuPanel extends JPanel {
	public MypageMenuPanel(ShopMainFrame mainFrame) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
		setBackground(Color.WHITE);

		JButton orderListBtn = new JButton("주문내역");
		ButtonUtil.styleMenuButton(orderListBtn);
		orderListBtn.addActionListener(e -> mainFrame.showPage("MY_ORDER_LIST", "MYPAGE_MENU"));
		
		JButton updateBtn = new JButton("회원 정보 수정");
		ButtonUtil.styleMenuButton(updateBtn);
		updateBtn.addActionListener(e -> mainFrame.showPage("ORDER_HISTORY", "MYPAGE_MENU"));
		
		JButton delBtn = new JButton("회원 탈퇴");
		ButtonUtil.styleMenuButton(delBtn);
		delBtn.addActionListener(e -> mainFrame.showPage("ORDER_HISTORY", "MYPAGE_MENU"));

		add(orderListBtn);
		add(updateBtn);
		add(delBtn);
	}
}

