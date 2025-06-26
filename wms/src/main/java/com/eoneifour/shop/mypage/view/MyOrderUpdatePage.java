package com.eoneifour.shop.mypage.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.shop.view.ShopMainFrame;
import com.eoneifour.shopadmin.order.model.Order;
import com.eoneifour.shopadmin.order.repository.OrderDAO;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;

public class MyOrderUpdatePage extends JPanel {
	private ShopMainFrame mainFrame;
	
    private JTextField prodNameField;
    private JTextField quantityField;
    private JTextField totalPriceField;
    private JTextField statusField;
    private JTextField addressField;
    private JTextField addressDetailField;
    
    private JButton updateBtn;
    private JButton listBtn;
    
    private int orderId;
    
    private OrderDAO orderDAO;
    
	public MyOrderUpdatePage(ShopMainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.orderDAO = new OrderDAO();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 247, 250));
        
    	JPanel formPanel = initFormPanel();
        
        add(Box.createVerticalGlue());
    	add(formPanel);
    	add(Box.createVerticalGlue());
	}
	
	// 폼 전체 초기화
	private JPanel initFormPanel() {
		JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setMaximumSize(new Dimension(500, 610));
        formPanel.setMinimumSize(new Dimension(500, 610));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 타이틀 생성
        JLabel title = new JLabel("주문 수정");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));
        
        // 필드 생성
        prodNameField = new JTextField(16);
        formPanel.add(FieldUtil.createField("상품명", prodNameField));
        formPanel.add(Box.createVerticalStrut(12));
        quantityField = new JTextField(16);
        formPanel.add(FieldUtil.createField("수량", quantityField));
        formPanel.add(Box.createVerticalStrut(12));
        totalPriceField = new JTextField(16);
        formPanel.add(FieldUtil.createField("총 액", totalPriceField));
        formPanel.add(Box.createVerticalStrut(12));
        statusField = new JTextField(16);
        formPanel.add(FieldUtil.createField("배송 상태", statusField));
        formPanel.add(Box.createVerticalStrut(12));
        addressField = new JTextField(16);
        formPanel.add(FieldUtil.createField("도로명 주소", addressField));
        formPanel.add(Box.createVerticalStrut(12));
        addressDetailField = new JTextField(16);
        formPanel.add(FieldUtil.createField("상세 주소", addressDetailField));
        formPanel.add(Box.createVerticalStrut(12));
        
        // 필드 비활성화
        prodNameField.setEditable(false);
        quantityField.setEditable(false);
        totalPriceField.setEditable(false);
        statusField.setEditable(false);
        
        // 버튼 패널 붙이기
        formPanel.add(createButtonPanel());
        
        return formPanel;
	}
	
	// 하단 버튼 패널 초기화
	private JPanel createButtonPanel() {
		// 버튼 생성
        updateBtn = ButtonUtil.createWarningButton("수정", 15, 120, 40);
        listBtn = ButtonUtil.createDefaultButton("목록", 15, 120, 40);
        // 수정 버튼 이벤트
        updateBtn.addActionListener(e-> {
        	if(validateForm()) {
        		updateOrder();
        		JOptionPane.showMessageDialog(this, "수정이 완료되었습니다.");
        		mainFrame.showPage("MY_ORDER_LIST", "MYPAGE_MENU");
        	}
        });
        // 목록 버튼 이벤트
        listBtn.addActionListener(e-> mainFrame.showPage("MY_ORDER_LIST", "MYPAGE_MENU"));
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(updateBtn);
        buttonPanel.add(listBtn);
        buttonPanel.setMaximumSize(new Dimension(300, 50));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	return buttonPanel;
	}
	
	// 수정 요청 처리
    public void updateOrder() {
    	try {
    		String address = addressField.getText();
			String addressDetail = addressDetailField.getText();
			
			orderDAO.updateOrder(orderId, address, addressDetail);
    	} catch (UserException e) {
    		JOptionPane.showMessageDialog(this, e.getMessage());
    		e.printStackTrace();
		}
    }
	
	// 폼 유효성 검사
    public boolean validateForm() {
        if (addressField.getText().trim().isEmpty()) return showErrorMessage("도로명 주소를 입력해주세요.");
        if (addressDetailField.getText().trim().isEmpty()) return showErrorMessage("상세 주소를 입력해주세요.");
        
        return true;
    }
    
    // 오류 메시지 출력
    private boolean showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
        return false;
    }
    
    // orderId 설정
 	public void setOrder(int orderId) {
 		this.orderId = orderId;
 		loadOrder();
 	}
	
	private void loadOrder() {
		Order order = new OrderDAO().getOrderById(orderId);
		
        prodNameField.setText(order.getProductName());
        quantityField.setText(String.valueOf(order.getQuantity()));
        totalPriceField.setText(String.valueOf(order.getTotalPrice()));
        statusField.setText(order.getStatusName());
        addressField.setText(order.getDeliveryAddress());
        addressDetailField.setText(order.getDeliveryAddressDetail());
	}
	
	public void prepare(int userId) {
 		setOrder(userId);
 	}
}