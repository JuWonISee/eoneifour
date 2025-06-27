package com.eoneifour.shopadmin.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.SessionUtil;
import com.eoneifour.common.view.LoginPage;
import com.eoneifour.shopadmin.order.view.OrderDetailPage;
import com.eoneifour.shopadmin.order.view.OrderListPage;
import com.eoneifour.shopadmin.order.view.OrderUpdatePage;
import com.eoneifour.shopadmin.product.view.ProductDetailPage;
import com.eoneifour.shopadmin.product.view.ProductListPage;
import com.eoneifour.shopadmin.product.view.ProductRegistPage;
import com.eoneifour.shopadmin.product.view.ProductUpdatePage;
import com.eoneifour.shopadmin.purchaseOrder.view.PurchaseOrderDetailPage;
import com.eoneifour.shopadmin.purchaseOrder.view.PurchaseOrderListPage;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.user.view.UserDetailPage;
import com.eoneifour.shopadmin.user.view.UserListPage;
import com.eoneifour.shopadmin.user.view.UserRegistPage;
import com.eoneifour.shopadmin.user.view.UserUpdatePage;

/**
 * 쇼핑몰 관리자 메인 프레임 - 상속받은 상단바,좌측바 만들어서 넘기면 됨 - 각 메뉴별 페이지 등록
 * 
 * @author 혜원
 *
 *         병합 중, productPage들의 접근지정자를 default에서 public으로 수정하였습니다.
 * @author JH
 */

public class ShopAdminMainFrame extends AbstractMainFrame {
	public UserRegistPage userRegistPage;
	public UserDetailPage userDetailPage;
	public UserUpdatePage userUpdatePage;
	public UserListPage userListPage;

	public ProductRegistPage productRegistPage;
	public ProductDetailPage productDetailPage;
	public ProductUpdatePage productUpdatePage;
	public ProductListPage productListPage;

	public PurchaseOrderListPage purchaseOrderListPage;
	public PurchaseOrderDetailPage purchaseOrderDetailPage;

	public OrderListPage orderListPage;
	public OrderDetailPage orderDetailPage;
	public OrderUpdatePage orderUpdatePage;
	
	List<JButton> menuButtons = new ArrayList<>();

    public ShopAdminMainFrame() {
        super("쇼핑몰 메인 (관리자)"); // 타이틀 설정
        
        userRegistPage = new UserRegistPage(this);
        userDetailPage = new UserDetailPage(this);
        userUpdatePage = new UserUpdatePage(this);
        userListPage = new UserListPage(this);

        productRegistPage = new ProductRegistPage(this);
        productDetailPage = new ProductDetailPage(this);
        productUpdatePage = new ProductUpdatePage(this);
    	productListPage = new ProductListPage(this);
    	
    	purchaseOrderListPage = new PurchaseOrderListPage(this);
    	purchaseOrderDetailPage = new PurchaseOrderDetailPage(this);
    	
    	orderListPage = new OrderListPage(this);
    	orderDetailPage = new OrderDetailPage(this);
    	orderUpdatePage = new OrderUpdatePage(this);
    	
        initPages();
    }

    // 페이지 등록
    private void initPages() {
    	// 회원관리
    	contentCardPanel.add(userListPage, "USER_LIST"); 		// 회원관리 페이지
    	contentCardPanel.add(userRegistPage, "USER_REGIST"); 	// 회원등록
    	contentCardPanel.add(userDetailPage, "USER_DETAIL"); 	// 회원상세
    	contentCardPanel.add(userUpdatePage, "USER_UPDATE"); 	// 회원수정
    	
    	// 상품관리
        contentCardPanel.add(productListPage, "PRODUCT_LIST"); // 상품관리 페이지
        contentCardPanel.add(productRegistPage, "PRODUCT_REGIST"); // 상품등록 페이지
        contentCardPanel.add(productUpdatePage, "PRODUCT_UPDATE"); // 상품수정 페이지
        contentCardPanel.add(productDetailPage, "PRODUCT_DETAIL"); // 상품상세 페이지
        
        //발주관리
        contentCardPanel.add(purchaseOrderListPage, "PURCHASE_LIST"); // 발주관리 페이지
        contentCardPanel.add(purchaseOrderDetailPage, "PURCHASE_DETAIL"); // 발주상세 페이지
        // 주문관리
        contentCardPanel.add(orderListPage, "ORDER_LIST"); // 주문관리 페이지
        contentCardPanel.add(orderDetailPage, "ORDER_DETAIL"); // 주문상세 페이지
        contentCardPanel.add(orderUpdatePage, "ORDER_UPDATE"); // 주문수정 페이지

		// 회원 목록을 초기화면으로 (active 효과)
        ButtonUtil.applyMenuActiveStyle(menuButtons, menuButtons.get(0));
		showContent("USER_LIST");
	}

	// 상단 정보 바 + 메뉴 바 구성
	@Override
	public JPanel createTopPanel() {
		JPanel infoBar = createInfoBar();
		JPanel menuBar = createMenuBar();

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(infoBar, BorderLayout.NORTH);
		topPanel.add(menuBar, BorderLayout.SOUTH);

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

		// 오른쪽 정렬 + 좌우 15pt,위아래 10px 여백을 위한 Panel
		JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		rightWrapper.setOpaque(false);
		rightWrapper.add(logoutButton);
		infoBar.add(rightWrapper, BorderLayout.EAST);

		return infoBar;
	}

	private JPanel createMenuBar() {
		JPanel menuBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
		menuBar.setBackground(Color.WHITE);

		JButton userBtn = createMenuButton("회원관리", "USER_LIST", menuButtons);
		JButton productBtn = createMenuButton("상품관리", "PRODUCT_LIST", menuButtons);
		JButton orderBtn = createMenuButton("주문관리", "ORDER_LIST", menuButtons);
		JButton purchaseBtn = createMenuButton("발주관리", "PURCHASE_LIST", menuButtons);
		
		menuBar.add(userBtn);
		menuBar.add(productBtn);
		menuBar.add(orderBtn);
		menuBar.add(purchaseBtn);

		return menuBar;
	}
	
	// 메뉴에 들어가는 버튼들 공통 로직
	private JButton createMenuButton(String title, String pageKey, List<JButton> menuButtons) {
	    JButton btn = new JButton(title);
	    ButtonUtil.styleMenuButton(btn);
	    btn.addActionListener(e -> {
	        showContent(pageKey);
	        ButtonUtil.applyMenuActiveStyle(menuButtons, btn);
	    });
	    menuButtons.add(btn);
	    return btn;
	}

	// 쇼핑몰은 좌측패널 없음
	@Override
	public JPanel createLeftPanel() {
		return null;
	}

	public static void main(String[] args) {
		// 세션 없으면 로그인 창으로 강제 리디렉션
		if (SessionUtil.getLoginUser() == null) {
			new LoginPage().setVisible(true);
		} else {
			new ShopAdminMainFrame();
		}

	}
}