package com.eoneifour.shopadmin.user.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.SQLException;

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
import com.eoneifour.common.util.DBManager;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.user.repository.UserDAO;
import com.eoneifour.shopadmin.view.frame.ShopAdminMainFrame;


public class UserRegistPage extends JPanel {
	private ShopAdminMainFrame mainFrame;
	
	private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField addressDetailField;
    private JComboBox<String> roleCombo;
    
    private UserDAO userDAO;
    private DBManager dbManager = DBManager.getInstance();
    
    private boolean isEmailChecked = false; // 중복체크 클릭 유무
    private boolean isEmailDuplicate = false; // 이메일 중복 유무
    
    public UserRegistPage(ShopAdminMainFrame mainFrame) {
    	this.mainFrame = mainFrame;
    	userDAO = new UserDAO();
    	
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 247, 250)); 

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setMaximumSize(new Dimension(500, 610));
        formPanel.setMinimumSize(new Dimension(500, 610));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("회원 등록");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));

        
        // 이메일 필드 + 중복확인 버튼
        emailField = new JTextField(16);
        JButton checkBtn = ButtonUtil.createDefaultButton("중복확인", 13, 90, 36);
        formPanel.add(FieldUtil.createFieldWithButton("이메일", emailField, checkBtn));
        formPanel.add(Box.createVerticalStrut(18));

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
        addressDetailField = new JTextField(16);
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(FieldUtil.createField("상세 주소", addressDetailField));
        formPanel.add(Box.createVerticalStrut(18));
        
        // 권한 필드 + 콤보박스
        roleCombo = new JComboBox<>(new String[]{"user", "admin"});
        formPanel.add(FieldUtil.createComboField("권한", roleCombo));
        formPanel.add(Box.createVerticalStrut(32));
        
        // 버튼 생성
        JButton registBtn = ButtonUtil.createPrimaryButton("저장", 15, 120, 40);
        JButton listBtn = ButtonUtil.createDefaultButton("목록", 15, 120, 40);
        
        // 버튼 이벤트
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
        
        registBtn.addActionListener(e->{
        	if(validateForm()) {
        		registerUser();
        		JOptionPane.showMessageDialog(this, "등록이 완료되었습니다.");
        		mainFrame.userListPage.refresh();
        		mainFrame.showContent("USER_LIST");
        	}
        });

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(registBtn);
        buttonPanel.add(listBtn);
        buttonPanel.setMaximumSize(new Dimension(300, 50));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(buttonPanel);

        add(Box.createVerticalGlue());
        add(formPanel);
        add(Box.createVerticalGlue());
    }
    
    public void registerUser() {
    	Connection conn = dbManager.getConnetion();
    	
    	try {
    		conn.setAutoCommit(false);
			
			User user = new User();
			user.setEmail(emailField.getText());
			user.setPassword(new String(passwordField.getPassword()));
			user.setName(nameField.getText());
			user.setAddress(addressField.getText());
			user.setAddressDetail(addressDetailField.getText());
			user.setRole(roleCombo.getSelectedIndex());
			
			userDAO.insertUser(user);
			
			conn.commit();
    	} catch (UserException e) {
    		JOptionPane.showMessageDialog(this, e.getMessage());
    		
    		try {
    			conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
    		e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	
    }
    
    // 회원 등록 폼 유효성 검사
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

}