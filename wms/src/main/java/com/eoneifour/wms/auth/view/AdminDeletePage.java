package com.eoneifour.wms.auth.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;

/**
 * 관리자 탈퇴 페이지
 * - 관리자 계정을 삭제하는 화면 구성
 * - 비밀번호 입력 후 확인 버튼을 통해 탈퇴 수행
 * - 돌아가기 버튼을 통해 수정 페이지로 이동
 */
public class AdminDeletePage extends JPanel {

    private AbstractMainFrame mainFrame;

    public AdminDeletePage(AbstractMainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // 전체 레이아웃 설정
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        // 중앙 박스 패널 생성 (모든 내용 포함)
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(245, 247, 250));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),                      // 테두리
            BorderFactory.createEmptyBorder(30, 40, 30, 40)          // 안쪽 여백
        ));
        box.setMaximumSize(new Dimension(480, 300));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 타이틀 라벨
        JLabel title = new JLabel("관리자 탈퇴");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 안내 문구 (HTML로 줄바꿈 표현)
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

        JLabel pwLabel = new JLabel("Password");             // 라벨
        JPasswordField pwField = new JPasswordField();       // 비밀번호 입력창

        pwLabel.setPreferredSize(new Dimension(100, 36));
        pwField.setPreferredSize(new Dimension(250, 36));

        pwPanel.add(pwLabel);
        pwPanel.add(Box.createHorizontalStrut(10));
        pwPanel.add(pwField);

        // 버튼 패널 생성 (탈퇴 / 돌아가기)
        JButton deleteBtn = ButtonUtil.createWarningButton("탈퇴하기", 14, 120, 38);   // 빨간 버튼
        JButton backBtn = ButtonUtil.createDefaultButton("돌아가기", 14, 120, 38);    // 기본 회색 버튼

        // 탈퇴 버튼 클릭 이벤트
        deleteBtn.addActionListener((ActionEvent e) -> {
            // TODO: 비밀번호 확인 및 실제 탈퇴 처리 로직 필요
            JOptionPane.showMessageDialog(this, "탈퇴되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
        });

        // 돌아가기 버튼 클릭 이벤트
        backBtn.addActionListener((ActionEvent e) -> {
            mainFrame.showContent("ADMIN_EDIT"); // 이전 페이지(관리자 수정)로 이동
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(deleteBtn);
        btnPanel.add(Box.createHorizontalStrut(10));
        btnPanel.add(backBtn);

        // 최종 UI 조립
        box.add(title);
        box.add(Box.createVerticalStrut(15));
        box.add(noticePanel);
        box.add(Box.createVerticalStrut(20));
        box.add(pwPanel);
        box.add(Box.createVerticalStrut(20));
        box.add(btnPanel);

        add(Box.createVerticalGlue()); // 위쪽 여백
        add(box);                      // 중앙 박스 삽입
        add(Box.createVerticalGlue()); // 아래쪽 여백
    }
}
