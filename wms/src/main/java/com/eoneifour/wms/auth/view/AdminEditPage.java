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
 * 관리자 정보 수정 페이지
 * - 이메일, 비밀번호, 이름 수정 가능
 * - 수정 버튼 클릭 시 DB에 반영
 */
public class AdminEditPage extends JPanel {

    private MainFrame mainFrame; // 메인 프레임 참조
    private JTextField emailField, nameField; // 이메일, 이름 필드
    private JPasswordField passwordField, confirmField; // 비밀번호, 비밀번호 확인 필드
    private JButton editBtn; // 수정 버튼

    private AdminDAO adminDAO; // DB 연동 DAO

    public AdminEditPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.adminDAO = new AdminDAO();

        // 전체 레이아웃 설정
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        // 중앙 패널(box) 생성
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(245, 247, 250));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        box.setMaximumSize(new Dimension(480, 360));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 제목
        JLabel title = new JLabel("관리자 정보 수정");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 라벨 및 입력 필드 크기 설정
        Dimension labelSize = new Dimension(100, 36);
        Dimension fieldSize = new Dimension(250, 36);

        // 이메일 패널
        JPanel emailPanel = new JPanel();
        emailPanel.setOpaque(false);
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.X_AXIS));
        JLabel emailLabel = new JLabel("이메일");
        emailField = new JTextField("admin@naver.com"); // 기본값 설정
        emailLabel.setPreferredSize(labelSize);
        emailField.setPreferredSize(fieldSize);
        emailPanel.add(emailLabel);
        emailPanel.add(Box.createHorizontalStrut(10));
        emailPanel.add(emailField);

        // 비밀번호 패널
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

        // 비밀번호 확인 패널
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

        // 이름 패널
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

        // 수정 버튼 생성 및 이벤트 등록
        editBtn = ButtonUtil.createPrimaryButton("수정하기", 14, 160, 38);
        editBtn.addActionListener(e -> edit()); // 클릭 시 edit() 실행

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(editBtn);

        // box에 요소 추가
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

        // 화면 중앙 정렬
        add(Box.createVerticalGlue());
        add(box);
        add(Box.createVerticalGlue());
        
        MainFrame mf=(MainFrame)mainFrame;
        if(mf.admin !=null) { //로그인 한 경우만...
        	getAdminInfo();
        }
    }

    public void getAdminInfo(){
//    	MainFrame mf=(MainFrame)mainFrame;
    	
    	emailField.setText(mainFrame.admin.getEmail());
    	nameField.setText(mainFrame.admin.getName());
    }
    
    
    /**
     * 수정 버튼 클릭 시 호출되는 메서드
     * - 비밀번호 일치 여부 확인
     * - Admin 객체 생성 후 DB 업데이트 요청
     */
    private void edit() {
        String pw = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        String name = nameField.getText().trim();

        if (!pw.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
            return;
        }

        Admin admin = new Admin();
        admin.setEmail(emailField.getText().trim());
        admin.setPassword(pw);
        admin.setName(name);

        int result = adminDAO.update(admin);  // ← AdminDAO에 update 메서드 있어야 함
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "관리자 정보가 수정되었습니다.");
            
            passwordField.setText("");
            confirmField.setText("");
            
            mainFrame.admin.setName(name);          
            mainFrame.setAdminInfo(name);           
            
            mainFrame.showContent("HOME"); // 수정 후 홈으로 이동
        } else {
            JOptionPane.showMessageDialog(this, "수정에 실패했습니다.");
        }
    }
}
