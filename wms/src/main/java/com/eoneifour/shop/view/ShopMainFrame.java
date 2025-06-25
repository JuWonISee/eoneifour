package com.eoneifour.shop.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.SessionUtil;
import com.eoneifour.common.view.LoginPage;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.user.view.UserRegistPage;

/**
 * 쇼핑몰 메인 프레임
 * 
 * @author 혜원
 */

public class ShopMainFrame extends AbstractMainFrame {
	public UserRegistPage userRegistPage;

    public ShopMainFrame() {
        super("쇼핑몰 메인");
        initPages();
    }

    // 페이지 등록
    private void initPages() {
		// 회원 목록을 초기화면으로
		showContent("ORDER_LIST");
	}

	// 상단 정보 바 + 메뉴 바 구성
	@Override
	public JPanel createTopPanel() {
		JPanel infoBar = createInfoBar();

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(infoBar, BorderLayout.NORTH);

		return topPanel;
	}

	private JPanel createInfoBar() {
		JPanel infoBar = new JPanel(new BorderLayout());
		infoBar.setBackground(Color.BLACK);
		infoBar.setPreferredSize(new Dimension(1280, 50));
		// 로그인 사용자
		 User loginUser = SessionUtil.getLoginUser();
		// Left Panel: 사용자 이름 포함한 인삿말
		 JLabel userInfoLabel = new JLabel(loginUser.getName() +"님, 안녕하세요.");
		 userInfoLabel.setForeground(Color.WHITE);
		// 왼쪽 정렬 + 좌우 15pt,위아래 10px 여백을 위한 Panel
		JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		leftWrapper.setOpaque(false);
		leftWrapper.add(userInfoLabel);
		infoBar.add(leftWrapper, BorderLayout.WEST);

		
		// Right Panel: 버튼 area
		JButton homeBtn = new JButton("HOME");
		ButtonUtil.styleHeaderButton(homeBtn);
		JButton logoutButton = new JButton("로그아웃");
		ButtonUtil.styleHeaderButton(logoutButton);
		homeBtn.addActionListener(e -> {
			// 상품리스트로 이동
		});
		
		logoutButton.addActionListener(e -> {
			SessionUtil.clear();
			dispose();
			new LoginPage().setVisible(true);
		});

		// 오른쪽 정렬 + 좌우 15pt,위아래 10px 여백을 위한 Panel
		JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		rightWrapper.setOpaque(false);
		rightWrapper.add(homeBtn);
		rightWrapper.add(logoutButton);
		infoBar.add(rightWrapper, BorderLayout.EAST);

		return infoBar;
	}

	@Override
	public JPanel createLeftPanel() {
		return null;
	}

	public static void main(String[] args) {
		if (SessionUtil.getLoginUser() == null) new LoginPage().setVisible(true);
		else new ShopMainFrame();
	}
}