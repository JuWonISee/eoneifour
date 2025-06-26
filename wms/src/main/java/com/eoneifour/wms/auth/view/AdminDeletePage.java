package com.eoneifour.wms.auth.view;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.wms.auth.model.Admin;
import com.eoneifour.wms.auth.repository.AdminDAO;
import com.eoneifour.wms.home.view.MainFrame;

public class AdminDeletePage extends JPanel {

    private AbstractMainFrame mainFrame;
    private AdminDAO adminDAO = new AdminDAO();

    public AdminDeletePage(AbstractMainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(245, 247, 250));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        box.setMaximumSize(new Dimension(480, 300));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("관리자 탈퇴");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel noticePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        noticePanel.setOpaque(false);
        JLabel noticeLabel = new JLabel("<html><div style='text-align: center;'>"
                + "탈퇴 후 복구가 불가능합니다.<br>"
                + "정말 탈퇴하시겠습니까?<br>"
                + "탈퇴하시려면 비밀번호를 입력해주세요."
                + "</div></html>");
        noticeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        noticePanel.add(noticeLabel);

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

        JButton deleteBtn = ButtonUtil.createWarningButton("탈퇴하기", 14, 120, 38);
        JButton backBtn = ButtonUtil.createDefaultButton("돌아가기", 14, 120, 38);

        deleteBtn.addActionListener((ActionEvent e) -> {
            MainFrame mf = (MainFrame) mainFrame;
            Admin loginAdmin = mf.admin;

            if (loginAdmin == null) {
                JOptionPane.showMessageDialog(this, "로그인 정보가 없습니다.");
                return;
            }

            String inputPw = new String(pwField.getPassword());

            // 실제 로그인 재검증
            Admin checkAdmin = adminDAO.login(loginAdmin.getEmail(), inputPw);

            if (checkAdmin != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "정말 탈퇴하시겠습니까? 이 작업은 복구되지 않습니다.",
                        "탈퇴 확인",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    int result = adminDAO.deleteByEmail(loginAdmin.getEmail());
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "탈퇴가 완료되었습니다.");
                        pwField.setText("");
                        mf.admin = null; // 로그인 상태 해제
                        mainFrame.showContent("ADMIN_LOGIN");
                    } else {
                        JOptionPane.showMessageDialog(this, "탈퇴 실패: 관리자 정보가 존재하지 않습니다.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
            }
        });

        backBtn.addActionListener((ActionEvent e) -> {
            mainFrame.showContent("ADMIN_EDIT");
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(deleteBtn);
        btnPanel.add(Box.createHorizontalStrut(10));
        btnPanel.add(backBtn);

        box.add(title);
        box.add(Box.createVerticalStrut(15));
        box.add(noticePanel);
        box.add(Box.createVerticalStrut(20));
        box.add(pwPanel);
        box.add(Box.createVerticalStrut(20));
        box.add(btnPanel);

        add(Box.createVerticalGlue());
        add(box);
        add(Box.createVerticalGlue());
    }
}
