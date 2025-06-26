package com.eoneifour.shopadmin.purchaseOrder.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.product.model.ProductImg;
import com.eoneifour.shopadmin.product.repository.ProductDAO;
import com.eoneifour.shopadmin.product.repository.ProductImgDAO;
import com.eoneifour.shopadmin.purchaseOrder.model.PurchaseOrder;
import com.eoneifour.shopadmin.purchaseOrder.repository.PurchaseOrderDAO;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;

public class PurchaseOrderDetailPage extends JPanel{

	private ShopAdminMainFrame mainFrame;
	
	private JPanel formPanel;
	private JTextField productNameField;
	private JTextField quantityField;
	private JTextField requesteDateField;
    private JTextField userNameField;
    private JTextField statusField;
    private JTextField completeDateField;
	
    private JButton listBtn;
    
    private int purchaseId;
    
	public PurchaseOrderDetailPage(ShopAdminMainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 247, 250));
        
    	JPanel formPanel = initFormPanel();
        
        add(Box.createVerticalGlue());
    	add(formPanel);
    	add(Box.createVerticalGlue());
		
	}
	
	//폼 전체 초기화
	private JPanel initFormPanel() {
		JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setMaximumSize(new Dimension(500, 610));
        formPanel.setMinimumSize(new Dimension(500, 610));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 타이틀 생성
        JLabel title = new JLabel("발주 상세");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));
        
        // 필드 생성
        productNameField = new JTextField(16);
        formPanel.add(FieldUtil.createField("상품명", productNameField));
        formPanel.add(Box.createVerticalStrut(12));
        quantityField = new JTextField(16);
        formPanel.add(FieldUtil.createField("요청수량", quantityField));
        formPanel.add(Box.createVerticalStrut(12));
        requesteDateField = new JTextField(16);
        formPanel.add(FieldUtil.createField("요청일자", requesteDateField));
        formPanel.add(Box.createVerticalStrut(12));
        userNameField = new JTextField(16);
        formPanel.add(FieldUtil.createField("요청자", userNameField));
        formPanel.add(Box.createVerticalStrut(12));
        statusField = new JTextField(16);
        formPanel.add(FieldUtil.createField("처리상태", statusField));
        formPanel.add(Box.createVerticalStrut(12));
        completeDateField = new JTextField(16);
        formPanel.add(FieldUtil.createField("처리일자", completeDateField));
        formPanel.add(Box.createVerticalStrut(32));
        
        // 필드 비활성화
        productNameField.setEditable(false);
        quantityField.setEditable(false);
        requesteDateField.setEditable(false);
        userNameField.setEditable(false);
        statusField.setEditable(false);
        completeDateField.setEditable(false);
        
        // 버튼 패널 붙이기
        formPanel.add(createButtonPanel());
        
        return formPanel;
	}
	
	// 하단 버튼 패널 초기화
	private JPanel createButtonPanel() {
		// 버튼 생성
        listBtn = ButtonUtil.createDefaultButton("목록", 15, 120, 40);
        
        // 목록 버튼 이벤트
        listBtn.addActionListener(e->{
    		mainFrame.showContent("PURCHASE_LIST");
        });
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(listBtn);
        buttonPanel.setMaximumSize(new Dimension(300, 50));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	return buttonPanel;
	}
	
	// 화면 진입 전에 호출 필요. purchaseId로 상세 정보 설정
	public void setPurchase(int purchaseId) {
		this.purchaseId = purchaseId;
		loadPurchase();
	}
	
	// purchaseId로 사용자 정보 조회 후 필드에 표시
	private void loadPurchase() {
		PurchaseOrder purchaseOrder = new PurchaseOrderDAO().getPurchase(purchaseId);
		productNameField.setText(purchaseOrder.getProduct().getName());
		quantityField.setText(Integer.toString(purchaseOrder.getQuantity()));
		requesteDateField.setText(purchaseOrder.getRequest_date().toString());
		userNameField.setText(purchaseOrder.getUser().getName());
		statusField.setText(purchaseOrder.getStatus());
		completeDateField.setText(purchaseOrder.getComplete_date().toString());
		
	}
	
}
