package com.eoneifour.shop.product.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.common.util.SessionUtil;
import com.eoneifour.shop.product.model.sh_OrderItem;
import com.eoneifour.shop.product.model.sh_Orders;
import com.eoneifour.shop.product.model.sh_Product;
import com.eoneifour.shop.product.model.sh_ProductImg;
import com.eoneifour.shop.product.repository.sh_OrderItemDAO;
import com.eoneifour.shop.product.repository.sh_OrdersDAO;
import com.eoneifour.shop.product.repository.sh_ProductDAO;
import com.eoneifour.shop.product.repository.sh_ProductImgDAO;
import com.eoneifour.shop.view.ShopMainFrame;
import com.eoneifour.shopadmin.product.view.NoticeAlert;

public class sh_ProductDetailPage extends JPanel {
	private ShopMainFrame mainFrame;
	
	//좌측 패널
	private JLabel categoryLabel;
	private JLabel imgLabel;
	//우측 패널
	private JLabel brandLabel;
	private JLabel nameLabel;
	private JLabel priceLabel;
	private JTextField qtyValue;
	private int currentQty;
	private JButton minusBtn;
	private JButton plusBtn;
	private JLabel totalLabel;
	
	//
	private OrderModalDialog o_dialog;
	//현재 상품 정보
	private int productId;
	private sh_Product currentProduct;
	private sh_ProductImg currentImg;
	
	private sh_OrdersDAO sh_ordersDAO;
	private sh_OrderItemDAO sh_orderItemDAO;
	
	public sh_ProductDetailPage(ShopMainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		this.sh_ordersDAO = new sh_OrdersDAO();
		this.sh_orderItemDAO = new sh_OrderItemDAO();

		setLayout(new GridLayout(1, 2)); // 좌우로 두 개의 영역 나눔
		setLeftPanel();
		setRightPanel();
	}

	//상위카테고리 > 하위카테고리 / 이미지 가 출력되는 왼쪽 패널
	public void setLeftPanel() {
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(248, 248, 248)); // 배경색 지정 , 연한 회색
		leftPanel.setPreferredSize(new Dimension(630, 680));
		leftPanel.setLayout(new BorderLayout());
		
		// 카테고리 라벨 초기화
		categoryLabel = new JLabel("");
		categoryLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		categoryLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0)); // 상좌하우 여백

		// 이미지 영역 초기화
		imgLabel = new JLabel();
		imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// 추가
		leftPanel.add(categoryLabel, BorderLayout.NORTH);
		leftPanel.add(imgLabel, BorderLayout.CENTER);
		add(leftPanel);
	}

	//상품 옵션 및 주문 , 돌아가기 버튼이 출력되는 오른쪽 패널
	public void setRightPanel() {
				
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(new Color(248, 248, 248)); // 배경색 지정 , 연한 회색
		rightPanel.setPreferredSize(new Dimension(630, 680));
		rightPanel.setLayout(null); // 자유 배치
		
		// 브랜드
		brandLabel = new JLabel("");
		brandLabel.setBounds(80, 140, 300, 30);
		brandLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));

		// 상품명
		nameLabel = new JLabel("");
		nameLabel.setBounds(80, 220, 300, 40);
		nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));

		// 가격
		priceLabel = new JLabel("");
		priceLabel.setBounds(80, 290, 300, 30);
		priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 22));

		// 수량 선택
		JLabel qtyLabel = new JLabel("수량 선택");
		qtyLabel.setBounds(80, 360, 100, 30);
		qtyLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		
		qtyValue = new JTextField("1"); //Default 값은 1로 고정.
		qtyValue.setHorizontalAlignment(JTextField.CENTER);
		qtyValue.setBounds(230, 360, 60, 30);
		qtyValue.setFont(new Font("맑은 고딕", Font.PLAIN, 18));

		minusBtn = new JButton("-");
		minusBtn.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		minusBtn.setBounds(180, 360, 45, 30);
		minusBtn.setBackground(Color.WHITE);

		plusBtn = new JButton("+");
		plusBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		plusBtn.setBounds(295, 360, 45, 30);
		plusBtn.setBackground(Color.WHITE);
		
		//버튼 이벤트 추가 (-버튼 : 1미만 내려가지 않게) (+버튼 : 재고이상 올라가지 않게) (현재 주문수량 기준 총 금액 셋팅)
		minusBtn.addActionListener(e -> {
		    int currentQty = Integer.parseInt(qtyValue.getText());
		    if (currentQty > 1) {
		        int newQty = currentQty - 1;
		        qtyValue.setText(String.valueOf(newQty));
		        totalLabel.setText("총 금액 " + FieldUtil.commaFormat(newQty * currentProduct.getPrice()) + "원");
		    }
		});
		plusBtn.addActionListener(e -> {
		    int currentQty = Integer.parseInt(qtyValue.getText());
		    if (currentQty < currentProduct.getStock_quantity()) {
		        int newQty = currentQty + 1;
		        qtyValue.setText(String.valueOf(newQty));
		        totalLabel.setText("총 금액 " + FieldUtil.commaFormat(newQty * currentProduct.getPrice()) + "원");
		    }
		});
		
		//수량 입력 TextField에 키보드 이벤트(고객이 입력한 수량을 감지) 추가
		qtyValue.getDocument().addDocumentListener(new DocumentListener() {
		    public void insertUpdate(DocumentEvent e) {
		        updateTotalPrice();
		    }
		    public void removeUpdate(DocumentEvent e) {
		        updateTotalPrice();
		    }
		    public void changedUpdate(DocumentEvent e) {
		        updateTotalPrice();
		    }
		    
		    private void updateTotalPrice() {
		        String qtyText = qtyValue.getText().trim();

		        if (!qtyText.matches("\\d+") || qtyText.equals("0")) {
		            totalLabel.setText("총 금액");
		            return;
		        }

		        int qty = Integer.parseInt(qtyText);
		        // 정상적인 경우 총금액 업데이트
		        totalLabel.setText("총 금액 " + FieldUtil.commaFormat(qty * currentProduct.getPrice()) + "원");
		    }
		});

		// 총 금액
		totalLabel = new JLabel("총 금액 ");
		totalLabel.setBounds(80, 430, 500, 30);
		totalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

		// 주문 버튼 생성 및 클릭이벤트 연결
		JButton orderBtn = new JButton("주문");
		orderBtn.setBounds(80, 500, 120, 40);
		orderBtn.setBackground(new Color(0, 120, 215));
		orderBtn.setForeground(Color.WHITE);
		orderBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		// 주문 버튼 이벤트 연결 (등록 이벤트 중복 방지 , 수량 유효성 검사)
        if (orderBtn.getActionListeners().length == 0) {
        	orderBtn.addActionListener(e->{
        		if (validateqty()) {
        	        String errorMsg = "정말 이 주소로 주문하시겠습니까?\n"
        	        		  + SessionUtil.getLoginUser().getAddress() + " "
        	        		  + SessionUtil.getLoginUser().getAddressDetail();
        	        		  
        		    o_dialog = new OrderModalDialog(mainFrame, errorMsg);
        		    o_dialog.setVisible(true);

        		    if (o_dialog.isConfirmed()) {
        		        try {
							insertOrder();
							insertOrderItem();
							mainFrame.showPage("SH_ORDER_COMPLETE", "PRODUCT_MENU");
						} catch (UserException e1) {
							JOptionPane.showMessageDialog(this, "상품 주문 중 오류 발생" + e1.getMessage());
							e1.printStackTrace();
						}
        		    }
        		}
	        });
        }
		

		// 목록 버튼 생성 및 클릭 이벤트 연결
		JButton backBtn = new JButton("목록"); 
		backBtn.setBounds(210, 500, 120, 40);
		backBtn.setBackground(new Color(220, 220, 220));
		backBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		backBtn.addActionListener(e-> mainFrame.showPage("SH_PRODUCT_LIST", "PRODUCT_MENU"));
		
		
		//컴포넌트 추가
        rightPanel.add(brandLabel);
        rightPanel.add(nameLabel);
        rightPanel.add(priceLabel);
        rightPanel.add(qtyLabel);
        rightPanel.add(minusBtn);
        rightPanel.add(qtyValue);
        rightPanel.add(plusBtn);
        rightPanel.add(totalLabel);
        rightPanel.add(orderBtn);
        rightPanel.add(backBtn);
        
        add(rightPanel);

	}

	public void setProduct(int productId) {
		this.productId = productId;	
		loadProduct();
	}
	
	public void loadProduct() {
		
		try {
			//좌측 패널 현재 product 이미지 출력
			currentImg = new sh_ProductImgDAO().getProductImg(productId);
			//현 상품의 이미지 출력 전 , 전 상품의 이미지가 들어있던 라벨 초기화 
			imgLabel.setIcon(null);
			imgLabel.setText(""); 
			imgLabel.setOpaque(false); // 기본 배경 제거
			
			//파일 객체 및 제대로 된 경로가 있는지 없는지 확인.
			String filename = (currentImg != null && currentImg.getFilename() != null) ? currentImg.getFilename() : "";	
			//이미지가 로드됐는지 확인하기 위한 변수
			boolean imageLoaded = false;
			
			//filename이 비어있지 않을 경우 해당 이미지 로딩
			if (!filename.isEmpty()) {
			    URL imgUrl = getClass().getClassLoader().getResource(filename);
			    if (imgUrl != null) {
			        ImageIcon icon = new ImageIcon(imgUrl);
			        Image img = icon.getImage();
			        if (img != null && img.getWidth(null) > 0) {
			            Image scaledImg = img.getScaledInstance(450, 450, Image.SCALE_SMOOTH);
			            imgLabel.setIcon(new ImageIcon(scaledImg));
			            imageLoaded = true;
			        }
			    }
			}

			//image가 로드되지 않았을 경우 , No image 부착
			if (!imageLoaded) {
			    imgLabel.setPreferredSize(new Dimension(450, 450));
			    imgLabel.setOpaque(true);
			    imgLabel.setBackground(Color.LIGHT_GRAY);
			    imgLabel.setText("No Image");
			    imgLabel.setHorizontalAlignment(JLabel.CENTER);
			    imgLabel.setVerticalAlignment(JLabel.CENTER);
			    imgLabel.setForeground(Color.DARK_GRAY);
			    imgLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
			}
		} catch (UserException e) {
			JOptionPane.showMessageDialog(this, "이미지 불러오기 중 오류 발생 " + e.getMessage());
		}
		
		//우측 패널 현재 product 값 입력
		try {
			currentProduct = new sh_ProductDAO().getProduct(productId);
			categoryLabel.setText(currentProduct.getTop_category().getName() + " > " + currentProduct.getSub_category().getName());
			
			brandLabel.setText(currentProduct.getBrand_name());
			nameLabel.setText(currentProduct.getName());
			priceLabel.setText(FieldUtil.commaFormat(currentProduct.getPrice()));
		} catch (UserException e) {
			JOptionPane.showMessageDialog(this, "상품 불러오기 중 오류 발생 " + e.getMessage());
		}
		
		currentQty = 1;
		qtyValue.setText("1");
		totalLabel.setText("총 금액 " + FieldUtil.commaFormat(Integer.parseInt(qtyValue.getText()) * currentProduct.getPrice()));
	}
	
	//수량 유효성 검사 (1. 입력값이 음수나 문자가 아닌지 and 2. 주문수량이 현재 남아있는 재고보다 작은지)
	public boolean validateqty() {
	    String qtyText = qtyValue.getText().trim();

	    // 숫자 형식 확인
	    if (!qtyText.matches("\\d+")) {
	    	new NoticeAlert(mainFrame, "주문수량은 숫자만 입력 가능합니다.", "요청 실패").setVisible(true);
	    	return false;
	    }

	    int qty = Integer.parseInt(qtyText);

	    // 0 이상 확인
	    if (qty <= 0) {
	    	new NoticeAlert(mainFrame, "주문수량은 1개 이상이어야 합니다." , "요청 실패").setVisible(true);
	    	return false;
	    }

	    // 재고 수량 초과 여부
	    if (qty > currentProduct.getStock_quantity()) {
	    	
	        String errorMsg = "현재 보유 재고보다 주문 수량이 많습니다.\n" +
	                "최대 주문 가능 수량: " + currentProduct.getStock_quantity() + "개";
	        
	        new NoticeAlert(mainFrame, errorMsg, "요청 실패").setVisible(true);
	        return false;
	    }

	    return true;
	}
	
	public void insertOrder() {
		try {
			sh_Orders orders = new sh_Orders();
			if (!validateqty()) return; 
			int qty = Integer.parseInt(qtyValue.getText());
			int userId = SessionUtil.getLoginUser().getUserId();
			String address = SessionUtil.getLoginUser().getAddress();
			String detailAddress = SessionUtil.getLoginUser().getAddressDetail();
			
			orders.setTotal_price(currentProduct.getPrice() * qty);
			orders.setUser_id(userId);
			orders.setDelivery_address(address);
			orders.setDelivery_address_detail(detailAddress);

			sh_ordersDAO.insertOrder(orders);
			
		} catch (UserException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void insertOrderItem() {
		try {
			sh_OrderItem orderItem = new sh_OrderItem();
			if (!validateqty()) return; 
			int quantity = Integer.parseInt(qtyValue.getText());
			int price = currentProduct.getPrice();
			int orders_id = sh_ordersDAO.selectRecentPk();
			int product_id = currentProduct.getProduct_id();
			
			orderItem.setQuantity(quantity);
			orderItem.setPrice(price);
			orderItem.setOrders_id(orders_id);
			orderItem.setProduct_id(product_id);
			
			sh_orderItemDAO.insertOrderItem(orderItem);
			
		} catch (UserException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
		
	}

}
