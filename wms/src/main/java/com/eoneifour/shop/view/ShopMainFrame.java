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
import com.eoneifour.shop.mypage.view.MyOrderDetailPage;
import com.eoneifour.shop.mypage.view.MyOrderListPage;
import com.eoneifour.shop.mypage.view.MyOrderUpdatePage;
import com.eoneifour.shop.mypage.view.MyUserDeletePage;
import com.eoneifour.shop.mypage.view.MyUserDetailPage;
import com.eoneifour.shop.mypage.view.MyUserUpdatePage;
import com.eoneifour.shop.product.view.sh_OrderCompletePage;
import com.eoneifour.shop.product.view.sh_ProductDetailPage;
import com.eoneifour.shop.product.view.sh_ProductListPage;
import com.eoneifour.shopadmin.user.model.User;

/**
 * 쇼핑몰 메인 프레임
 * 
 * @author 혜원
 */

public class ShopMainFrame extends AbstractMainFrame {
	// 마이페이지
	public MyUserDetailPage myUserDetailPage;
	public MyUserUpdatePage myUserUpdatePage;
	public MyUserDeletePage myUserDeletePage;
	
	public MyOrderListPage myOrderListPage;
	public MyOrderDetailPage myOrderDetailPage;
	public MyOrderUpdatePage myOrderUpdatePage;
	
	public sh_ProductListPage sh_productListPage;
	public sh_ProductDetailPage sh_productDetailPage;
	public sh_OrderCompletePage sh_orderCompletePage;

	private JPanel rightWrapper;
	public String currentMenuKey = "PRODUCT_MENU";
	public int userId;
	
    public ShopMainFrame() {
        super("쇼핑몰 메인");
        userId = SessionUtil.getLoginUser().getUserId();
        // 페이지 생성
        myUserDetailPage = new MyUserDetailPage(this);
        myUserUpdatePage = new MyUserUpdatePage(this);
        myUserDeletePage = new MyUserDeletePage(this);

        myOrderListPage = new MyOrderListPage(this);
        myOrderDetailPage = new MyOrderDetailPage(this);
        myOrderUpdatePage = new MyOrderUpdatePage(this);
        
        sh_productListPage = new sh_ProductListPage(this);
        sh_productDetailPage = new sh_ProductDetailPage(this);
        sh_orderCompletePage = new sh_OrderCompletePage(this);
        
        initPages();
    }

    // 메뉴/페이지 등록
    private void initPages() {
    	// 페이지 등록
    	contentCardPanel.add(myUserDetailPage, "MY_USER_DTL"); // 마이페이지 회원상세
    	contentCardPanel.add(myUserUpdatePage, "MY_USER_UPD"); // 마이페이지 회원수정
    	contentCardPanel.add(myUserDeletePage, "MY_USER_DEL"); // 마이페이지 회원탈퇴
    	
    	contentCardPanel.add(myOrderListPage, "MY_ORDER_LIST"); // 마이페이지 주문내역
    	contentCardPanel.add(myOrderDetailPage, "MY_ORDER_DTL"); // 마이페이지 주문상세
    	contentCardPanel.add(myOrderUpdatePage, "MY_ORDER_UPD"); // 마이페이지 주문수정
    	
    	contentCardPanel.add(sh_productListPage, "SH_PRODUCT_LIST"); // 쇼핑몰 상품목록
    	contentCardPanel.add(sh_productDetailPage, "SH_PRODUCT_DETAIL"); // 쇼핑몰 상품상세
    	//contentCardPanel.add(sh_orderCompletePage, "SH_ORDER_COMPLETE"); // 주문 완료 Alert 페이지 
    	
    	// 메뉴 등록
    	menuCardPanel.add(new MypageMenuPanel(this), "MYPAGE_MENU");
    	menuCardPanel.add(new ProductMenuPanel(this), "PRODUCT_MENU");
    	
    	// 초기 화면
    	showPage("SH_PRODUCT_LIST", "PRODUCT_MENU");
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
		JButton logoutButton = new JButton("로그아웃");
		ButtonUtil.styleHeaderButton(logoutButton);
		logoutButton.addActionListener(e -> {
			SessionUtil.clear();
			dispose();
			new LoginPage().setVisible(true);
		});

		rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		rightWrapper.setOpaque(false);
		rightWrapper.add(createSwitchMenuButton());
		rightWrapper.add(logoutButton);
		infoBar.add(rightWrapper, BorderLayout.EAST);

		return infoBar;
	}
	
	private JButton createSwitchMenuButton() {
	    boolean isInMypage = "MYPAGE_MENU".equals(currentMenuKey);
	    JButton button = new JButton(isInMypage ? "HOME" : "마이페이지");
	    ButtonUtil.styleHeaderButton(button);

	    button.addActionListener(e -> {
	        if (isInMypage) {
	            showPage("SH_PRODUCT_LIST", "PRODUCT_MENU");
	        } else {
	            showPage("MY_USER_DTL", "MYPAGE_MENU");
	        }
	    });

	    return button;
	}
	
	public void updateHeaderButton() {
	    rightWrapper.remove(0); // 기존 버튼 제거
	    rightWrapper.add(createSwitchMenuButton(), 0); // 새 버튼 추가
	    rightWrapper.revalidate();
	    rightWrapper.repaint();
	}
	
	// 메뉴와 콘텐츠를 동시에 전환하는 메서드
    public void showPage(String contentKey, String menuKey) {
    	boolean menuChanged = !menuKey.equals(currentMenuKey);
    	currentMenuKey = menuKey;
        showContent(contentKey);
        ((CardLayout) menuCardPanel.getLayout()).show(menuCardPanel, menuKey);
        
        if (menuChanged) updateHeaderButton();
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