package com.eoneifour.shopadmin.product.view;

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

public class NoticeAlert extends AbstractModalDialog{
	private JLabel label;
	
    public NoticeAlert(JFrame parent , String Msg , String Msg2) {
        super(parent, Msg2); //parent는 나중에 mainframe으로 지정.
        setSize(400, 170); 
        initComponents();
        initAlert(Msg);
    }
    
    protected void initComponents() {
    }
    
    protected void initAlert(String Msg) {
    	
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 라벨 생성
        label = new JLabel("<html>" + Msg.replace("\n", "<br>") + "</html>");
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

    	buttonPanel.add(confirmBtn);
    }
    
    
}