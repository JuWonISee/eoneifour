package com.eoneifour.wms.auth.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.wms.auth.model.Admin;
import com.eoneifour.wms.auth.repository.AdminDAO;
import com.eoneifour.wms.home.view.MainFrame;

/**
 * 관리자 로그인 페이지
 * - 이메일과 비밀번호 입력 후 로그인 수행
 * - 관리자 가입 페이지로 이동도 가능
 */
public class AdminLoginPage extends JPanel {

    private AbstractMainFrame mainFrame; // 메인 프레임 참조

    // 입력 필드 및 버튼
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginBtn, joinBtn;

    private AdminDAO adminDAO; // DB 접근용 DAO

    // 생성자
    public AdminLoginPage(AbstractMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.adminDAO = new AdminDAO(); // DAO 초기화

        // 전체 레이아웃 및 배경 설정
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        // 로그인 박스 UI 설정
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(245, 247, 250));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        box.setMaximumSize(new Dimension(400, 240));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 로그인 타이틀
        JLabel title = new JLabel("로그인");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension labelSize = new Dimension(100, 36);
        Dimension fieldSize = new Dimension(200, 36);

        // 이메일 입력 패널
        JPanel emailPanel = new JPanel();
        emailPanel.setOpaque(false);
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.X_AXIS));
        JLabel emailLabel = new JLabel("이메일");
        emailLabel.setPreferredSize(labelSize);
        emailField = new JTextField();
        emailField.setPreferredSize(fieldSize);
        emailPanel.add(emailLabel);
        emailPanel.add(Box.createHorizontalStrut(10));
        emailPanel.add(emailField);

        // 비밀번호 입력 패널
        JPanel pwPanel = new JPanel();
        pwPanel.setOpaque(false);
        pwPanel.setLayout(new BoxLayout(pwPanel, BoxLayout.X_AXIS));
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setPreferredSize(labelSize);
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(fieldSize);
        pwPanel.add(pwLabel);
        pwPanel.add(Box.createHorizontalStrut(10));
        pwPanel.add(passwordField);

        // 로그인 및 가입 버튼 생성
        loginBtn = ButtonUtil.createPrimaryButton("로그인", 14, 120, 38);
        joinBtn = ButtonUtil.createPrimaryButton("관리자 가입", 14, 120, 38);

        // 로그인 버튼 클릭 시 doLogin() 메서드 실행
        loginBtn.addActionListener(e -> doLogin());

        // 관리자 가입 버튼 클릭 시 등록 화면으로 이동
        joinBtn.addActionListener(e -> mainFrame.showContent("ADMIN_REGIST"));

        // 버튼 패널 설정
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(loginBtn);
        btnPanel.add(Box.createHorizontalStrut(10));
        btnPanel.add(joinBtn);

        // box 조립
        box.add(title);
        box.add(Box.createVerticalStrut(20));
        box.add(emailPanel);
        box.add(Box.createVerticalStrut(10));
        box.add(pwPanel);
        box.add(Box.createVerticalStrut(20));
        box.add(btnPanel);

        // 화면 중앙 정렬
        add(Box.createVerticalGlue());
        add(box);
        add(Box.createVerticalGlue());
    }

    /**
     * 로그인 처리 메서드
     * - 입력된 이메일/비밀번호를 기반으로 DB에서 관리자 인증
     * - 로그인 성공 시 HOME으로 이동
     */
    private void doLogin() {
        String email = emailField.getText().trim(); // 이메일 입력값
        String pw = new String(passwordField.getPassword()); // 비밀번호 입력값

        Admin admin = adminDAO.login(email, pw); // 로그인 시도
        if (admin != null) {
            // 로그인 성공
            JOptionPane.showMessageDialog(this, "로그인 성공! " + admin.getName() + "님");
            MainFrame mf=(MainFrame)mainFrame;
            mf.admin=admin;
           
            // 🔽 입력 필드 초기화 굿
            emailField.setText("");
            passwordField.setText("");
            
            mainFrame.showContent("HOME");
        } else {
            // 로그인 실패
            JOptionPane.showMessageDialog(this, "이메일 또는 비밀번호가 틀렸습니다.");
        }
    }
}
