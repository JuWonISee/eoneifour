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

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.repository.LoginDAO;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.common.util.SessionUtil;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;

public class LoginPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;

    LoginDAO loginDAO;
    User user;
    
    public LoginPage() {
    	loginDAO = new LoginDAO();
    	
        setTitle("로그인");
        setSize(460, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("로그인");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        loginPanel.add(title);
        loginPanel.add(Box.createVerticalStrut(40));

        emailField = new JTextField();
        loginPanel.add(FieldUtil.createField("이메일", emailField));
        loginPanel.add(Box.createVerticalStrut(15));

        passwordField = new JPasswordField();
        loginPanel.add(FieldUtil.createField("비밀번호", passwordField));
        loginPanel.add(Box.createVerticalStrut(30));

        loginButton = ButtonUtil.createPrimaryButton("로그인", 14, 170, 40);
        signupButton = ButtonUtil.createDefaultButton("회원가입", 14, 170, 40);

        loginButton.addActionListener(e-> loginUser());
        
        signupButton.addActionListener(e->{
        	//회원가입 이동
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(signupButton);

        loginPanel.add(buttonPanel);
        add(loginPanel);
    }
    
    private void loginUser() {
    	String email = emailField.getText().trim();
    	String pwd = new String(passwordField.getPassword()).trim();
    	if (email.isEmpty()) showErrorMessage("이메일을 입력해주세요.");
    	else if (pwd.isEmpty()) showErrorMessage("비밀번호를 입력해주세요.");
    	else {
    		try {
    			user = loginDAO.findByEmailAndPwd(email, pwd);
    			SessionUtil.setLoginUser(user);
    			if(user.getRole() == 1) {
    				new ShopAdminMainFrame();
    				dispose(); // 로그인 창 닫기
    			} else {
    				JOptionPane.showMessageDialog(this, "유저페이지");
    			}
			} catch (UserException e) {
    			JOptionPane.showMessageDialog(this, e.getMessage());
    			e.printStackTrace();
			}
    	}
    }
    
    // 오류 메시지 출력
    private boolean showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
        return false;
    }

    public static void main(String[] args) {
        new LoginPage().setVisible(true);
    }
}
