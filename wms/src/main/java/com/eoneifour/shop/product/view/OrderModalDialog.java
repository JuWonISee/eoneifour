package com.eoneifour.shop.product.view;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.eoneifour.common.frame.AbstractModalDialog;
import com.eoneifour.common.util.ButtonUtil;

public class OrderModalDialog extends AbstractModalDialog{
	private JLabel label;
    public OrderModalDialog(JFrame parent, String msg) {
        super(parent, "주문 확인"); //parent는 나중에 mainframe으로 지정.
        setSize(400, 180); // 여기서 크기 재조절
        initComponents();
        alert(msg);
    }
    
    protected void initComponents() {     
    }
    
    protected void alert(String Msg) {
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 라벨 생성
        label = new JLabel("<html>" + Msg.replace("\n", "<br>") + "</html>"); //\n이 잘 작동하도록 포맷변경
        label.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 라벨을 감싸는 패널 생성 (중앙 정렬용)
        JPanel centerPanel = new JPanel(new GridBagLayout()); //중앙정렬
        centerPanel.setOpaque(false); // 배경 투명하게
        centerPanel.add(label);

        // 가운데에 붙이기
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(Box.createVerticalGlue()); // 위 여백
        formPanel.add(centerPanel);              // 가운데
        formPanel.add(Box.createVerticalGlue()); // 아래 여백
        
        confirmBtn.addActionListener(e -> {
            confirmed = true;
            dispose();      
        });
        
    }
    @Override
    protected void createButtons() {
    	confirmBtn = ButtonUtil.createPrimaryButton("확인", 14, 100, 35); // 파란색 버튼
    	cancelBtn = ButtonUtil.createDefaultButton("취소", 14, 100, 35);     // 회색 버튼

    	cancelBtn.addActionListener(e -> dispose());

    	buttonPanel.add(confirmBtn);
    	buttonPanel.add(cancelBtn);
    }
    
    
}