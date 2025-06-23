package com.eoneifour.shopadmin.product.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.eoneifour.common.frame.AbstractModalDialog;
import com.eoneifour.shopadmin.product.model.Product;

public class OrderModalDialog extends AbstractModalDialog{
	private JTextField productNameField;
    private JTextField stockField;
    private JTextField quantityField;
    private int quantity;
    private Product product;

    public OrderModalDialog(JFrame parent, Product product) {
        super(parent, "발주 요청");
        this.product = product;
        initComponents();
    }
    
    protected void initComponents() {
        productNameField = new JTextField(product.getName());
        productNameField.setEnabled(false);

        stockField = new JTextField(product.getStock_quantity() + " 개");
        stockField.setEnabled(false);

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
    }

    public int getQuantity() {
        return quantity;
    }
}

