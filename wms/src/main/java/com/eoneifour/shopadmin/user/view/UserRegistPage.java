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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.user.repository.UserDAO;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;


public class UserRegistPage extends JPanel {
	private ShopAdminMainFrame mainFrame;
	
	private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField addressDetailField;
    private JComboBox<String> roleCombo;
    
    private JButton registBtn;
    private JButton listBtn;
    private JButton checkBtn;
    
    private UserDAO userDAO;
    
    private boolean isEmailChecked = false; // 중복체크 클릭 유무
    private boolean isEmailDuplicate = false; // 이메일 중복 유무
    
    public UserRegistPage(ShopAdminMainFrame mainFrame) {
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
        JLabel title = new JLabel("회원 등록");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));

        // 이메일 필드 + 중복확인 버튼
        emailField = new JTextField(16);
        checkBtn = ButtonUtil.createDefaultButton("중복확인", 13, 90, 36);
        formPanel.add(FieldUtil.createFieldWithButton("이메일", emailField, checkBtn));
        formPanel.add(Box.createVerticalStrut(18));
        
        // 이메일 입력 바뀌면 중복검사 상태 초기화
        emailField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { markEmailUnchecked(); }
            public void removeUpdate(DocumentEvent e) { markEmailUnchecked(); }
            public void changedUpdate(DocumentEvent e) { markEmailUnchecked(); }

            private void markEmailUnchecked() {
                isEmailChecked = false;
                isEmailDuplicate = false;
            }
        });

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
        
        // 권한 필드 + 콤보박스
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
        registBtn = ButtonUtil.createPrimaryButton("저장", 15, 120, 40);
        listBtn = ButtonUtil.createDefaultButton("목록", 15, 120, 40);
        
        // 중복 확인 버튼 이벤트
        checkBtn.addActionListener(e->{
        	isEmailDuplicate = userDAO.existByEmail(emailField.getText());
        	if (isEmailDuplicate) {
        		isEmailChecked = false;
        		showErrorMessage("이메일이 중복됐습니다. 다른 이메일을 입력해주세요.");
        	} else {
        		isEmailChecked = true;
        		JOptionPane.showMessageDialog(this, "사용 가능한 이메일입니다.");
        	}
        });
        
        // 등록 버튼 이벤트 (등록 이벤트 중복 방지)
        if (registBtn.getActionListeners().length == 0) {
	        registBtn.addActionListener(e->{
	        	if(validateForm()) {
	        		registerUser();
	        		clearForm();
	        		JOptionPane.showMessageDialog(this, "등록이 완료되었습니다.");
	        		mainFrame.userListPage.refresh();
	        		mainFrame.showContent("USER_LIST");
	        	}
	        });
        }
        
        // 목록 버튼 이벤트
        listBtn.addActionListener(e->{
    		mainFrame.userListPage.refresh();
    		mainFrame.showContent("USER_LIST");
        });

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(registBtn);
        buttonPanel.add(listBtn);
        buttonPanel.setMaximumSize(new Dimension(300, 50));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        return buttonPanel;
    }
    
    // 등록 요청 처리
    public void registerUser() {
    	try {
			User user = new User();
			user.setEmail(emailField.getText());
			user.setPassword(new String(passwordField.getPassword()));
			user.setName(nameField.getText());
			user.setAddress(addressField.getText());
			user.setAddressDetail(addressDetailField.getText());
			user.setRole(roleCombo.getSelectedIndex());
			
			userDAO.insertUser(user);
    	} catch (UserException e) {
    		JOptionPane.showMessageDialog(this, e.getMessage());
    		e.printStackTrace();
		}
    }
    
    // 폼 유효성 검사
    public boolean validateForm() {
        if (emailField.getText().trim().isEmpty()) return showErrorMessage("이메일을 입력해주세요.");
        if (!isEmailChecked) return showErrorMessage("이메일 중복확인을 해주세요.");
        if (isEmailDuplicate) return showErrorMessage("이메일이 중복됐습니다. 다른 이메일을 입력해주세요.");
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
    
    // 등록 폼 초기화
    private void clearForm() {
    	emailField.setText("");
    	passwordField.setText("");
    	confirmPasswordField.setText("");
    	nameField.setText("");
    	addressField.setText("");
    	addressDetailField.setText("");
    	roleCombo.setSelectedIndex(0);
    	isEmailChecked = false;
    	isEmailDuplicate = false;
    }
    
    // 진입 전 필수 함수 호출
    public void prepare() {
    	clearForm();
    }

}