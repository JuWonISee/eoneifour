package com.eoneifour.shop.product.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.eoneifour.shop.product.repository.sh_ProductDAO;
import com.eoneifour.shop.product.repository.sh_ProductImgDAO;
import com.eoneifour.shop.view.ShopMainFrame;

public class sh_ProductDetailPage extends JPanel {
	private ShopMainFrame mainFrame;
	private sh_ProductDAO sh_productDAO;
	private sh_ProductImgDAO sh_productImgDAO;

	public sh_ProductDetailPage(ShopMainFrame mainFrame) {
		this.mainFrame = mainFrame;

		sh_productDAO = new sh_ProductDAO();
		sh_productImgDAO = new sh_ProductImgDAO();

		setLayout(new GridLayout(1, 2)); // 좌우로 두 개의 영역 나눔
		
		

		setLeftPanel();
		setRightPanel();

	}


	public void setLeftPanel() {
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(248, 248, 248)); // 배경색 지정 , 연한 회색
		leftPanel.setPreferredSize(new Dimension(630, 680));
		leftPanel.setLayout(new BorderLayout());
		
		// 카테고리 라벨
		JLabel categoryLabel = new JLabel("식품 > 인스턴트");
		categoryLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		categoryLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0)); // 상좌하우 여백

		// 이미지
		ImageIcon icon = new ImageIcon(getClass().getResource("/images/snack.PNG"));
		Image scaledImage = icon.getImage().getScaledInstance(450, 450, Image.SCALE_SMOOTH);
		JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// 추가
		leftPanel.add(categoryLabel, BorderLayout.NORTH);
		leftPanel.add(imageLabel, BorderLayout.CENTER);
		add(leftPanel);
	}

	public void setRightPanel() {
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(new Color(248, 248, 248)); // 배경색 지정 , 연한 회색
		rightPanel.setPreferredSize(new Dimension(630, 680));
		rightPanel.setLayout(null); // 자유 배치
		
		// 브랜드
		JLabel brandLabel = new JLabel("농심");
		brandLabel.setBounds(80, 140, 100, 30);
		brandLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));

		// 상품명
		JLabel nameLabel = new JLabel("육개장");
		nameLabel.setBounds(80, 220, 200, 40);
		nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));

		// 가격
		JLabel priceLabel = new JLabel("9,500원");
		priceLabel.setBounds(80, 290, 200, 30);
		priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 22));

		// 수량 선택
		JLabel qtyLabel = new JLabel("수량 선택");
		qtyLabel.setBounds(80, 360, 100, 30);
		qtyLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));

		JButton minusBtn = new JButton("-");
		minusBtn.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		minusBtn.setBounds(180, 360, 45, 30);
		minusBtn.setBackground(Color.WHITE);

		JTextField qtyValue = new JTextField("2");
		qtyValue.setHorizontalAlignment(JTextField.CENTER);
		qtyValue.setBounds(230, 360, 60, 30);
		qtyValue.setFont(new Font("맑은 고딕", Font.PLAIN, 18));

		JButton plusBtn = new JButton("+");
		plusBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		plusBtn.setBounds(295, 360, 45, 30);
		plusBtn.setBackground(Color.WHITE);

		// 총 금액
		JLabel totalLabel = new JLabel("총 금액  19,000원");
		totalLabel.setBounds(80, 430, 300, 30);
		totalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

		// 주문 버튼
		JButton orderBtn = new JButton("주문하기");
		orderBtn.setBounds(80, 500, 120, 40);
		orderBtn.setBackground(new Color(0, 120, 215));
		orderBtn.setForeground(Color.WHITE);
		orderBtn.setFont(new Font("맑은 고딕", Font.BOLD, 18));

		// 목록 버튼
		JButton backBtn = new JButton("목록으로");
		backBtn.setBounds(210, 500, 120, 40);
		backBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		
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

	}

}
