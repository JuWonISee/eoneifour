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

		JButton userDtlBtn = new JButton("회원 정보");
		ButtonUtil.styleMenuButton(userDtlBtn);
		userDtlBtn.addActionListener(e -> mainFrame.showPage("MY_USER_DTL", "MYPAGE_MENU"));

		JButton orderListBtn = new JButton("주문내역");
		ButtonUtil.styleMenuButton(orderListBtn);
		orderListBtn.addActionListener(e -> mainFrame.showPage("MY_ORDER_LIST", "MYPAGE_MENU"));
		
		JButton userDelBtn = new JButton("회원 탈퇴");
		ButtonUtil.styleMenuButton(userDelBtn);
		userDelBtn.addActionListener(e -> mainFrame.showPage("MY_USER_DEL", "MYPAGE_MENU"));

		add(userDtlBtn);
		add(orderListBtn);
		add(userDelBtn);
	}
}

