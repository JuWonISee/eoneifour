package com.eoneifour.shopadmin.product.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.eoneifour.common.frame.AbstractModalDialog;
import com.eoneifour.common.util.ButtonUtil;

public class UpdateModalDialog extends AbstractModalDialog{
	private JLabel label;
    public UpdateModalDialog(JFrame parent) {
        super(parent, "상품 수정"); //parent는 나중에 mainframe으로 지정.
        setSize(400, 180); // 여기서 높이 줄이기
        initComponents();
    }
    
    protected void initComponents() {
    	label = new JLabel("정말 수정하시겠습니까?");
    	label.setFont(new Font("맑은 고딕", Font.BOLD, 14));
    	label.setPreferredSize(new Dimension(300, 50));
    	formPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        formPanel.add(label);

        confirmBtn.addActionListener(e -> {
            confirmed = true;
            dispose();      
        });
        
    }
    @Override
    protected void createButtons() {
    	confirmBtn = ButtonUtil.createPrimaryButton("예", 14, 100, 35); // 파란색 버튼
    	cancelBtn = ButtonUtil.createDefaultButton("아니오", 14, 100, 35);     // 빨간색 버튼

    	cancelBtn.addActionListener(e -> dispose());

    	buttonPanel.add(confirmBtn);
    	buttonPanel.add(cancelBtn);
    }
    
    
}