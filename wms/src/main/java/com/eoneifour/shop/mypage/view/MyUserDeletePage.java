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
import javax.swing.JPasswordField;

import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.common.util.SessionUtil;
import com.eoneifour.common.view.LoginPage;
import com.eoneifour.shop.view.ShopMainFrame;
import com.eoneifour.shopadmin.user.repository.UserDAO;

public class MyUserDeletePage extends JPanel {
	private ShopMainFrame mainFrame;
	private JPasswordField passwordField;
    private JButton delBtn;
    private JButton backBtn;
    
	public MyUserDeletePage(ShopMainFrame mainFrame) {
		this.mainFrame = mainFrame;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 247, 250));
        
    	JPanel formPanel = initFormPanel();
        
        add(Box.createVerticalGlue());
    	add(formPanel);
    	add(Box.createVerticalGlue());
	}
	
	private JPanel initFormPanel() {
		JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setMaximumSize(new Dimension(400, 350));
        formPanel.setMinimumSize(new Dimension(400, 350));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel title = new JLabel("회원 탈퇴");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));
        
        // 안내문 생성
        JLabel guide1 = new JLabel("탈퇴 후 복구가 불가능합니다.");
        JLabel guide2 = new JLabel("정말 탈퇴하시겠습니까?");
        JLabel guide3 = new JLabel("탈퇴하시려면 비밀번호를 입력해주세요.");
        for (JLabel label : new JLabel[]{guide1, guide2, guide3}) {
            label.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        
        formPanel.add(guide1);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(guide2);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(guide3);
        formPanel.add(Box.createVerticalStrut(20));
        
        passwordField = new JPasswordField(16);
        formPanel.add(FieldUtil.createField("비밀번호", passwordField));
        formPanel.add(Box.createVerticalStrut(30));
        
        formPanel.add(createButtonPanel());
        
        return formPanel;
	}
	
	private JPanel createButtonPanel() {
        delBtn = ButtonUtil.createWarningButton("탈퇴", 15, 120, 40);
        backBtn = ButtonUtil.createDefaultButton("돌아가기", 15, 120, 40);
        
        delBtn.addActionListener(e-> deleteUser());
        backBtn.addActionListener(e-> mainFrame.showPage("MY_USER_DTL", "MYPAGE_MENU"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(delBtn);
        buttonPanel.add(backBtn);
        buttonPanel.setMaximumSize(new Dimension(300, 50));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	return buttonPanel;
	}
	
	private void deleteUser() {
		String password = new String(passwordField.getPassword());
		if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요.");
            return;
        }
		
		UserDAO userDAO = new UserDAO();
        boolean success = userDAO.validatePassword(mainFrame.userId, password);
        if (!success) {
            JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "정말 탈퇴하시겠습니까?", "회원 탈퇴", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
        	userDAO.deleteUser(mainFrame.userId); // 탈퇴 처리
            JOptionPane.showMessageDialog(this, "탈퇴가 완료되었습니다.");
            SessionUtil.clear();
            mainFrame.dispose();
            new LoginPage().setVisible(true);
        }
	}
}
