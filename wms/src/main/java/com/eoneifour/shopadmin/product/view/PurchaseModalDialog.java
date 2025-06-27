package com.eoneifour.shopadmin.product.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.eoneifour.common.frame.AbstractModalDialog;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.shopadmin.product.model.Product;

public class PurchaseModalDialog extends AbstractModalDialog{
	private JTextField productNameField;
    private JTextField stockField;
    private JTextField quantityField;
    private int quantity;
    private Product product;

    public PurchaseModalDialog(JFrame parent, Product product) {
        super(parent, "발주 요청"); //parent는 나중에 mainframe으로 지정.
        this.product = product;
        initComponents();
    }
    
    protected void initComponents() {
    	//상품명 영역 (db에서 출력)
        productNameField = new JTextField(product.getName());
        productNameField.setEditable(false);
        productNameField.setBackground(new Color(220, 220, 220));
        productNameField.setForeground(Color.BLACK); //TextField의 글씨색
        productNameField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); //TextField의 배경색
        productNameField.setHorizontalAlignment(JTextField.CENTER);  // 가운데 정렬

        //재고 영역 (db에서 출력)
        stockField = new JTextField(product.getStock_quantity() + " 개");
        stockField.setEditable(false);
        stockField.setBackground(new Color(220, 220, 220));
        stockField.setForeground(Color.BLACK); //TextField의 글씨색
        stockField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));//TextField의 배경색
        stockField.setHorizontalAlignment(JTextField.CENTER);  // 가운데 정렬

        quantityField = new JTextField();

        formPanel.add(new JLabel("상품명"));
        formPanel.add(productNameField);

        formPanel.add(new JLabel("현재 재고"));
        formPanel.add(stockField);

        formPanel.add(new JLabel("수량 선택"));
        formPanel.add(quantityField);

        confirmBtn.setText("발주 신청");
        
        confirmBtn.addActionListener(e -> {
            String input = quantityField.getText().trim();
            if (!input.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "수량은 0 이상의 정수여야 합니다.");
                return;
            }
            quantity = Integer.parseInt(input);
            confirmed = true;
            dispose();
            
        });
        
        SwingUtilities.invokeLater(() -> {
            quantityField.requestFocusInWindow();
        });
    }
    
    @Override
    protected void createButtons() {
    	confirmBtn = ButtonUtil.createPrimaryButton("발주 신청", 14, 100, 35); // 파란색 버튼
    	cancelBtn = ButtonUtil.createDefaultButton("취소", 14, 100, 35);     // 빨간색 버튼

    	cancelBtn.addActionListener(e -> dispose());

    	buttonPanel.add(confirmBtn);
    	buttonPanel.add(cancelBtn);
    }

    public int getQuantity() {
        return quantity;
    }
    
    
}

