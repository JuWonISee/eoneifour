package com.eoneifour.common.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
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

public class JoinPage extends JFrame {
	private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField addressDetailField;
    
    private JButton joinButton;
    private JButton loginButton;
    private JButton checkBtn;
    
    private UserDAO userDAO;
    
    private boolean isEmailChecked = false; // 중복체크 클릭 유무
    private boolean isEmailDuplicate = false; // 이메일 중복 유무
    
    public JoinPage() {
    	userDAO = new UserDAO();
    	
        setTitle("회원가입");
        setSize(550, 610);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        JPanel formPanel = new JPanel();
        
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("회원가입");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(40));

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

        joinButton = ButtonUtil.createPrimaryButton("회원가입", 14, 170, 40);
        loginButton = ButtonUtil.createDefaultButton("로그인", 14, 170, 40);

        joinButton.addActionListener(e-> joinUser());
        loginButton.addActionListener(e-> {
        	dispose();
        	new LoginPage().setVisible(true);
        });
        
        // 중복 확인 버튼 이벤트
        checkBtn.addActionListener(e->{
        	if (emailField.getText().trim().isEmpty()) showErrorMessage("이메일을 입력해주세요.");
        	else {
        		isEmailDuplicate = userDAO.existByEmail(emailField.getText());
        		if (isEmailDuplicate) {
        			isEmailChecked = false;
        			showErrorMessage("이메일이 중복됐습니다. 다른 이메일을 입력해주세요.");
        		} else {
        			isEmailChecked = true;
        			JOptionPane.showMessageDialog(this, "사용 가능한 이메일입니다.");
        		}
        	}
        });
               
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.add(joinButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(loginButton);

        formPanel.add(buttonPanel);
        add(formPanel);
	}
    
    // 등록 요청 처리
    private void joinUser() {
    	if(validateForm()) {
    		try {
    			User user = new User();
    			user.setEmail(emailField.getText());
    			user.setPassword(new String(passwordField.getPassword()));
    			user.setName(nameField.getText());
    			user.setAddress(addressField.getText());
    			user.setAddressDetail(addressDetailField.getText());
    			user.setRole(0); // 회원가입은 사용자 고정
    			userDAO.insertUser(user);
        	} catch (UserException e) {
        		JOptionPane.showMessageDialog(this, e.getMessage());
        		e.printStackTrace();
    		}
    		
    		JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.");
    		dispose();
    		new LoginPage().setVisible(true);
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
    
    public static void main(String[] args) {
        new JoinPage().setVisible(true);
    }
}
