package com.eoneifour.shopadmin.user.view;

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
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.user.repository.UserDAO;
import com.eoneifour.shopadmin.view.frame.ShopAdminMainFrame;

public class UserDetailPage extends JPanel {
	private ShopAdminMainFrame mainFrame;
	
	private JPanel formPanel;
	private JTextField emailField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField addressDetailField;
    private JTextField roleField;
    private JTextField createdAtField;
    
    private JButton updateBtn;
    private JButton listBtn;
    
    private int userId;
    
	public UserDetailPage(ShopAdminMainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
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
        JLabel title = new JLabel("회원 상세");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));
        
        // 필드 생성
        emailField = new JTextField(16);
        formPanel.add(FieldUtil.createField("이메일", emailField));
        formPanel.add(Box.createVerticalStrut(12));
        nameField = new JTextField(16);
        formPanel.add(FieldUtil.createField("이름", nameField));
        formPanel.add(Box.createVerticalStrut(12));
        addressField = new JTextField(16);
        formPanel.add(FieldUtil.createField("도로명 주소", addressField));
        formPanel.add(Box.createVerticalStrut(12));
        addressDetailField = new JTextField(16);
        formPanel.add(FieldUtil.createField("상세 주소", addressDetailField));
        formPanel.add(Box.createVerticalStrut(12));
        roleField = new JTextField(16);
        formPanel.add(FieldUtil.createField("권한", roleField));
        formPanel.add(Box.createVerticalStrut(12));
        createdAtField = new JTextField(16);
        formPanel.add(FieldUtil.createField("가입일", createdAtField));
        formPanel.add(Box.createVerticalStrut(32));
        
        // 필드 비활성화
        emailField.setEditable(false);
        nameField.setEditable(false);
        addressField.setEditable(false);
        addressDetailField.setEditable(false);
        roleField.setEditable(false);
        createdAtField.setEditable(false);
        
        // 버튼 패널 붙이기
        formPanel.add(createButtonPanel());
        
        return formPanel;
	}
	
	// 하단 버튼 패널 초기화
	private JPanel createButtonPanel() {
		// 버튼 생성
        updateBtn = ButtonUtil.createPrimaryButton("수정", 15, 120, 40);
        listBtn = ButtonUtil.createDefaultButton("목록", 15, 120, 40);
        
        // 수정 버튼 이벤트
        updateBtn.addActionListener(e -> {
        	// 수정 화면 이동 로직
        });
        // 목록 버튼 이벤트
        listBtn.addActionListener(e->{
        	mainFrame.userListPage.refresh();
    		mainFrame.showContent("USER_LIST");
        });
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(updateBtn);
        buttonPanel.add(listBtn);
        buttonPanel.setMaximumSize(new Dimension(300, 50));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	return buttonPanel;
	}
	
	// 화면 진입 전에 호출 필요. userId로 상세 정보 설정
	public void setUser(int userId) {
		this.userId = userId;
		loadUser();
	}
	
	// userId로 사용자 정보 조회 후 필드에 표시
	private void loadUser() {
		User user = new UserDAO().getUserById(userId);
		
		emailField.setText(user.getEmail());
		nameField.setText(user.getName());
		addressField.setText(user.getAddress());
		addressDetailField.setText(user.getAddressDetail());
		roleField.setText(user.getRole() == 0 ? "user" : "admin");
		createdAtField.setText(user.getCreatedAt().toString());
	}
}
