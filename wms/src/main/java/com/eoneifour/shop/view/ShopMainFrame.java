package com.eoneifour.shop.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import com.eoneifour.shop.mypage.view.MyOrderListPage;
import com.eoneifour.shop.product.view.sh_ProductListPage;
import com.eoneifour.shopadmin.user.model.User;

/**
 * 쇼핑몰 메인 프레임
 * 
 * @author 혜원
 */

public class ShopMainFrame extends AbstractMainFrame {
	public MyOrderListPage myOrderListPage;
	public sh_ProductListPage sh_productListPage;

    public ShopMainFrame() {
        super("쇼핑몰 메인");
        // 페이지 생성
        myOrderListPage = new MyOrderListPage(this);
        sh_productListPage = new sh_ProductListPage(this);
        initPages();
    }

    // 메뉴/페이지 등록
    private void initPages() {
    	// 페이지 등록
    	contentCardPanel.add(myOrderListPage, "MY_ORDER_LIST"); // 마이페이지 주문내역
    	contentCardPanel.add(sh_productListPage, "SH_PRODUCT_LISTPAGE"); // 마이페이지 주문내역
    	// 메뉴 등록
    	menuCardPanel.add(new MypageMenuPanel(this), "MYPAGE_MENU");
    	menuCardPanel.add(new ProductMenuPanel(this), "PRODUCT_MENU");
    	// 초기 화면
    	showPage("SH_PRODUCT_LISTPAGE", "PRODUCT_MENU");
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
	
	// 메뉴와 콘텐츠를 동시에 전환하는 메서드
    public void showPage(String contentKey, String menuKey) {
        showContent(contentKey);
        ((CardLayout) menuCardPanel.getLayout()).show(menuCardPanel, menuKey);
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