package com.eoneifour.wms.auth.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.wms.auth.model.Admin;
import com.eoneifour.wms.auth.repository.AdminDAO;

/**
 * 관리자 회원가입 화면 클래스
 * - 이메일, 비밀번호, 이름 입력 후 가입 처리
 * - 이메일 중복 체크 기능 포함
 */
public class AdminRegistPage extends JPanel {

    private AbstractMainFrame mainFrame; // 메인 프레임 참조

    // 입력 필드들
    private JTextField emailField, nameField;
    private JPasswordField passwordField, confirmField;

    // 버튼
    private JButton checkBtn, registBtn;

    // 중복 확인 여부 플래그
    private boolean emailChecked = false;

    // DB 접근용 DAO
    private AdminDAO adminDAO;

    // 생성자
    public AdminRegistPage(AbstractMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.adminDAO = new AdminDAO();

        // 전체 패널 레이아웃 설정
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        // 중앙 입력 박스 설정
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(245, 247, 250));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        box.setMaximumSize(new Dimension(480, 360));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 타이틀
        JLabel title = new JLabel("관리자 가입");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 공통 사이즈 설정
        Dimension labelSize = new Dimension(100, 36);
        Dimension fieldSize = new Dimension(250, 36);

        // 이메일 입력 및 중복확인 버튼
        JPanel emailPanel = new JPanel();
        emailPanel.setOpaque(false);
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.X_AXIS));
        JLabel emailLabel = new JLabel("이메일");
        emailField = new JTextField();
        checkBtn = ButtonUtil.createDefaultButton("중복확인", 13, 100, 36);
        checkBtn.addActionListener(e -> checkEmail()); // 중복확인 버튼 이벤트 등록
        emailLabel.setPreferredSize(labelSize);
        emailField.setPreferredSize(fieldSize);
        emailPanel.add(emailLabel);
        emailPanel.add(Box.createHorizontalStrut(28));
        emailPanel.add(emailField);
        emailPanel.add(Box.createHorizontalStrut(10));
        emailPanel.add(checkBtn);

        // 비밀번호 입력
        JPanel pwPanel = new JPanel();
        pwPanel.setOpaque(false);
        pwPanel.setLayout(new BoxLayout(pwPanel, BoxLayout.X_AXIS));
        JLabel pwLabel = new JLabel("비밀번호");
        passwordField = new JPasswordField();
        pwLabel.setPreferredSize(labelSize);
        passwordField.setPreferredSize(fieldSize);
        pwPanel.add(pwLabel);
        pwPanel.add(Box.createHorizontalStrut(10));
        pwPanel.add(passwordField);

        // 비밀번호 확인 입력
        JPanel pwConfirmPanel = new JPanel();
        pwConfirmPanel.setOpaque(false);
        pwConfirmPanel.setLayout(new BoxLayout(pwConfirmPanel, BoxLayout.X_AXIS));
        JLabel confirmLabel = new JLabel("비밀번호 확인");
        confirmField = new JPasswordField();
        confirmLabel.setPreferredSize(labelSize);
        confirmField.setPreferredSize(fieldSize);
        pwConfirmPanel.add(confirmLabel);
        pwConfirmPanel.add(Box.createHorizontalStrut(10));
        pwConfirmPanel.add(confirmField);

        // 이름 입력
        JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        JLabel nameLabel = new JLabel("이름");
        nameField = new JTextField();
        nameLabel.setPreferredSize(labelSize);
        nameField.setPreferredSize(fieldSize);
        namePanel.add(nameLabel);
        namePanel.add(Box.createHorizontalStrut(10));
        namePanel.add(nameField);

        // 가입 버튼 설정
        registBtn = ButtonUtil.createPrimaryButton("관리자 가입", 14, 160, 38);
        registBtn.addActionListener(e -> register()); // 가입 버튼 이벤트 등록
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(registBtn);

        // 컴포넌트 조립
        box.add(title);
        box.add(Box.createVerticalStrut(20));
        box.add(emailPanel);
        box.add(Box.createVerticalStrut(10));
        box.add(pwPanel);
        box.add(Box.createVerticalStrut(10));
        box.add(pwConfirmPanel);
        box.add(Box.createVerticalStrut(10));
        box.add(namePanel);
        box.add(Box.createVerticalStrut(20));
        box.add(btnPanel);

        // 중앙 정렬
        add(Box.createVerticalGlue());
        add(box);
        add(Box.createVerticalGlue());
        
     
    }

    /**
     * 이메일 중복 확인 메서드
     * - 이메일 미입력시 경고
     * - DB에 존재하면 불가 메시지, 존재하지 않으면 사용 가능
     */
    private void checkEmail() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이메일을 입력하세요.");
            return;
        }

        if (adminDAO.existByEmail(email)) {
            JOptionPane.showMessageDialog(this, "이미 사용 중인 이메일입니다.");
            emailChecked = false;
        } else {
            JOptionPane.showMessageDialog(this, "사용 가능한 이메일입니다.");
            emailChecked = true;
        }
    }

    /**
     * 관리자 가입 처리 메서드
     * - 중복확인 여부 확인
     * - 비밀번호 일치 확인
     * - Admin 객체 생성 후 DB insert 시도
     */
    private void register() {
        if (!emailChecked) {
            JOptionPane.showMessageDialog(this, "이메일 중복확인을 해주세요.");
            return;
        }

        String pw = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (!pw.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
            return;
        }

        // 입력된 정보로 Admin 객체 생성
        Admin admin = new Admin();
        admin.setEmail(emailField.getText().trim());
        admin.setPassword(pw);
        admin.setName(nameField.getText().trim());

        // DB에 저장 시도
        int result = adminDAO.insert(admin);
        
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "관리자 가입이 완료되었습니다.");
            
            emailField.setText("");
            nameField.setText("");
            passwordField.setText("");
            confirmField.setText("");
            emailChecked = false;
            
            if (mainFrame != null) {
                mainFrame.showContent("HOME"); // 메인 페이지로 이동
            }
        } else {
            JOptionPane.showMessageDialog(this, "가입에 실패했습니다.");
        }
    }
}
