package com.eoneifour.shop.product.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.shop.view.ShopMainFrame;

public class sh_OrderCompletePage extends JPanel{
	private ShopMainFrame mainFrame;
	
	JPanel orderBox; //전체 박스
	JLabel titleLabel; //"주문완료" 제목 영역
	JTextArea messageLabel;// 안내메시지 영역
	JPanel buttonPanel; //주문내역 , 홈으로 버튼 영역
	
	public sh_OrderCompletePage(ShopMainFrame mainFrame) {
		this.mainFrame = mainFrame;
        setBackground(Color.WHITE);
        setLayout(null); // 수동 배치
		initPanel();
	}
	
	public void initPanel(){
        // 주문 완료 박스
        orderBox = new JPanel();
        orderBox.setLayout(new BoxLayout(orderBox, BoxLayout.Y_AXIS));
        orderBox.setBackground(new Color(0xf2f2f2));
        orderBox.setBounds(340, 50, 600, 500); // 대략 중앙에 위치
        orderBox.setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));

        // "주문 완료" 제목
        titleLabel = new JLabel("주문 완료");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));

        // 안내 메시지 (줄바꿈 처리)
        messageLabel = new JTextArea("주문이 완료되었습니다.\n마이페이지에서 주문내역을 확인할 수 있습니다.");
        messageLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        messageLabel.setEditable(false);
        messageLabel.setFocusable(false);
        messageLabel.setOpaque(false);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setMaximumSize(new Dimension(500, 100));
        messageLabel.setLineWrap(true);
        messageLabel.setWrapStyleWord(true);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 130, 0));

        // 버튼 영역
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton orderListBtn = ButtonUtil.createPrimaryButton("주문내역 보기", 14, 160, 40);
        JButton homeBtn = ButtonUtil.createDefaultButton("홈으로", 14, 120, 40);
        
        orderListBtn.addActionListener(e->mainFrame.showPage("MY_ORDER_LIST", "PRODUCT_MENU"));
        homeBtn.addActionListener(e->mainFrame.showPage("SH_PRODUCT_LIST", "PRODUCT_MENU"));

        buttonPanel.add(orderListBtn);
        buttonPanel.add(homeBtn);

        // 박스에 요소 추가
        orderBox.add(titleLabel);
        orderBox.add(messageLabel);
        orderBox.add(buttonPanel);

        // 패널에 주문 박스 추가
        add(orderBox);
	}
}
