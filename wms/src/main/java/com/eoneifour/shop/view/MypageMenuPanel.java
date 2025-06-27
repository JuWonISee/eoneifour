package com.eoneifour.shop.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.eoneifour.common.util.ButtonUtil;

public class MypageMenuPanel extends JPanel {
	private ShopMainFrame mainFrame;
	
	public MypageMenuPanel(ShopMainFrame mainFrame) {
		this.mainFrame = mainFrame;
		List<JButton> menuButtons = new ArrayList<>();
		setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
		setBackground(Color.WHITE);
		
		JButton userDtl = createMenuButton("회원 정보" , "MY_USER_DTL", menuButtons);
		JButton orderList = createMenuButton("주문 내역" , "MY_ORDER_LIST", menuButtons);
		JButton userDel = createMenuButton("회원 탈퇴" , "MY_USER_DEL", menuButtons);

		add(userDtl);
		add(orderList);
		add(userDel);
		
		ButtonUtil.applyMenuActiveStyle(menuButtons, userDtl);
	}
	
	// 메뉴에 들어가는 버튼들 공통 로직
	private JButton createMenuButton(String title, String pageKey, List<JButton> menuButtons) {
	    JButton btn = new JButton(title);
	    ButtonUtil.styleMenuButton(btn);
	    btn.addActionListener(e -> {
	    	mainFrame.showPage(pageKey, "MYPAGE_MENU");
	        ButtonUtil.applyMenuActiveStyle(menuButtons, btn);
	    });
	    menuButtons.add(btn);
	    return btn;
	}
}

