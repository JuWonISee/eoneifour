package com.eoneifour.wms.auth.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.*;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.ImgUtil;
import com.eoneifour.wms.auth.model.Admin;
import com.eoneifour.wms.auth.repository.AdminDAO;
import com.eoneifour.wms.home.view.MainFrame;

/**
 * 관리자 탈퇴 페이지
 * - 비밀번호 확인 후, 본인 인증이 되면 계정 삭제
 * - 삭제 완료 시 로그인 페이지로 이동
 */
public class AdminDeletePage extends JPanel {

    private AbstractMainFrame mainFrame; // 메인 프레임 참조
    private AdminDAO adminDAO = new AdminDAO(); // DB 접근 객체

	private Image backgroundImage;
    
    public AdminDeletePage(AbstractMainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // 전체 패널 설정
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		ImgUtil imgUtil = new ImgUtil();
		backgroundImage = imgUtil.getImage("images/homeImage2.png", 1280, 800);

        // 중앙 box 설정
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(245, 247, 250));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        box.setMaximumSize(new Dimension(480, 300));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 타이틀
        JLabel title = new JLabel("관리자 탈퇴");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 안내 문구
        JPanel noticePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        noticePanel.setOpaque(false);
        JLabel noticeLabel = new JLabel("<html><div style='text-align: center;'>"
                + "탈퇴 후 복구가 불가능합니다.<br>"
                + "정말 탈퇴하시겠습니까?<br>"
                + "탈퇴하시려면 비밀번호를 입력해주세요."
                + "</div></html>");
        noticeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        noticePanel.add(noticeLabel);

        // 비밀번호 입력 패널
        JPanel pwPanel = new JPanel();
        pwPanel.setOpaque(false);
        pwPanel.setLayout(new BoxLayout(pwPanel, BoxLayout.X_AXIS));
        JLabel pwLabel = new JLabel("Password");
        JPasswordField pwField = new JPasswordField();
        pwLabel.setPreferredSize(new Dimension(100, 36));
        pwField.setPreferredSize(new Dimension(250, 36));
        pwPanel.add(pwLabel);
        pwPanel.add(Box.createHorizontalStrut(10));
        pwPanel.add(pwField);

        // 탈퇴 버튼 생성
        JButton deleteBtn = ButtonUtil.createWarningButton("탈퇴하기", 14, 120, 38);

        // 탈퇴 버튼 이벤트 처리
        deleteBtn.addActionListener((ActionEvent e) -> {
            MainFrame mf = (MainFrame) mainFrame;
            Admin loginAdmin = mf.admin;

            // 로그인 상태 체크
            if (loginAdmin == null) {
                JOptionPane.showMessageDialog(this, "패스워드가 틀렸습니다.");
                return;
            }

            // 입력된 비밀번호 값 읽기
            String inputPw = new String(pwField.getPassword());

            // 로그인 검증 - 다시 한번 비밀번호 체크
            Admin checkAdmin = adminDAO.login(loginAdmin.getEmail(), inputPw);

            if (checkAdmin != null) {
                // 비밀번호 일치 -> 탈퇴 확인 팝업
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "정말 탈퇴하시겠습니까? 이 작업은 복구되지 않습니다.",
                        "탈퇴 확인",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // 실제 DB에서 관리자 삭제
                    int result = adminDAO.deleteByEmail(loginAdmin.getEmail());
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "탈퇴가 완료되었습니다.");

                        // 입력 필드 초기화
                        pwField.setText("");

                        // 메인 프레임에서 로그아웃 처리 및 로그인 페이지로 이동
                        mf.logout(); 
                    } else {
                        JOptionPane.showMessageDialog(this, "탈퇴 실패: 관리자 정보가 존재하지 않습니다.");
                    }
                }
            } else {
                // 비밀번호 불일치
                JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
            }
        });

        // 버튼 영역 패널
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(deleteBtn);

        // box에 요소 추가
        box.add(title);
        box.add(Box.createVerticalStrut(15));
        box.add(noticePanel);
        box.add(Box.createVerticalStrut(20));
        box.add(pwPanel);
        box.add(Box.createVerticalStrut(20));
        box.add(btnPanel);

        // 화면 중앙 정렬
        add(Box.createVerticalGlue());
        add(box);
        add(Box.createVerticalGlue());
    }
    
	@Override
	protected void paintComponent(Graphics g) {
		if(backgroundImage != null) {
			// 패널 크기에 맞게 이미지 늘려 그리기
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
