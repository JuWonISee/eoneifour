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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.user.repository.UserDAO;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;

public class UserUpdatePage extends JPanel{
	private ShopAdminMainFrame mainFrame;
	
	private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField addressDetailField;
    private JComboBox<String> roleCombo;
    
    private int userId;
    
    private JButton updateBtn;
    private JButton listBtn;
    
    private UserDAO userDAO;
    
	public UserUpdatePage(ShopAdminMainFrame mainFrame) {
		this.mainFrame = mainFrame;
    	userDAO = new UserDAO();
    	
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
        JLabel title = new JLabel("회원 수정");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));

        // 이메일 필드(수정 불가)
        emailField = new JTextField(16);
        formPanel.add(FieldUtil.createField("이메일", emailField));
        formPanel.add(Box.createVerticalStrut(12));
        emailField.setEditable(false);
        // 기본 필드
        passwordField = new JPasswordField(16);
        formPanel.add(FieldUtil.createField("비밀번호", passwordField));
        formPanel.add(Box.createVerticalStrut(12));
        confirmPasswordField = new JPasswordField(16);
        formPanel.add(FieldUtil.createField("비밀번호 확인", confirmPasswordField));
        formPanel.add(Box.createVerticalStrut(12));
        nameField = new JTextField(16);
        formPanel.add(FieldUtil.createField("이름", nameField));
        formPanel.add(Box.createVerticalStrut(12));
        addressField = new JTextField(16);
        formPanel.add(FieldUtil.createField("도로명 주소", addressField));
        formPanel.add(Box.createVerticalStrut(12));
        addressDetailField = new JTextField(16);
        formPanel.add(FieldUtil.createField("상세 주소", addressDetailField));
        formPanel.add(Box.createVerticalStrut(18));
        roleCombo = new JComboBox<>(new String[]{"user", "admin"});
        formPanel.add(FieldUtil.createComboField("권한", roleCombo));
        formPanel.add(Box.createVerticalStrut(32));
        
        // 버튼 패널 붙이기
        formPanel.add(createButtonPanel());
        
        return formPanel;
    }
    
    // 하단 버튼 패널 초기화
    private JPanel createButtonPanel() {
    	// 버튼 생성
        updateBtn = ButtonUtil.createPrimaryButton("저장", 15, 120, 40);
        listBtn = ButtonUtil.createDefaultButton("목록", 15, 120, 40);
        
        // 수정 버튼 이벤트 (등록 이벤트 중복 방지)
        if (updateBtn.getActionListeners().length == 0) {
        	updateBtn.addActionListener(e->{
	        	if(validateForm()) {
	        		updateUser();
	        		JOptionPane.showMessageDialog(this, "수정이 완료되었습니다.");
	        		mainFrame.showContent("USER_LIST");
	        	}
	        });
        }
        
        // 목록 버튼 이벤트
        listBtn.addActionListener(e-> mainFrame.showContent("USER_LIST"));

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
    public void updateUser() {
    	try {
			User user = new User();
			user.setUserId(userId);
			user.setPassword(new String(passwordField.getPassword()));
			user.setName(nameField.getText());
			user.setAddress(addressField.getText());
			user.setAddressDetail(addressDetailField.getText());
			user.setRole(roleCombo.getSelectedIndex());
			
			userDAO.updateUser(user);
    	} catch (UserException e) {
    		JOptionPane.showMessageDialog(this, e.getMessage());
    		e.printStackTrace();
		}
    }
    
    // 폼 유효성 검사
    public boolean validateForm() {
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        if (password.isEmpty()) return showErrorMessage("비밀번호를 입력해주세요.");
        if (confirmPassword.isEmpty()) return showErrorMessage("비밀번호 확인을 입력해주세요.");
        if (!password.equals(confirmPassword)) return showErrorMessage("비밀번호가 일치하지 않습니다.");

        if (addressField.getText().trim().isEmpty()) return showErrorMessage("도로명 주소를 입력해주세요.");
        if (addressDetailField.getText().trim().isEmpty()) return showErrorMessage("상세 주소를 입력해주세요.");
        
        return true;
    }
    
    // 오류 메시지 출력
    private boolean showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
        return false;
    }
    
    // 화면 진입 전에 호출 필요. 수정할 userId 설정
 	private void setUser(int userId) {
 		this.userId = userId;
 		loadUser();
 	}
 	
 	// userId로 사용자 정보 조회 후 필드에 표시
 	private void loadUser() {
 		User user = new UserDAO().getUserById(userId);
 		passwordField.setText("");
 		confirmPasswordField.setText("");
 		emailField.setText(user.getEmail());
 		nameField.setText(user.getName());
 		addressField.setText(user.getAddress());
 		addressDetailField.setText(user.getAddressDetail());
 		roleCombo.setSelectedIndex(user.getRole());
 	}
 	
 	public void prepare(int userId) {
 		setUser(userId);
 	}
}
