package com.eoneifour.shopadmin.member.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;


public class UserRegistPage extends JPanel {
    public UserRegistPage(AbstractMainFrame mainFrame) {
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
        JTextField emailField = new JTextField(16);
        JButton checkBtn = ButtonUtil.createDefaultButton("중복확인", 13, 90, 36);
        formPanel.add(FieldUtil.createFieldWithButton("이메일", emailField, checkBtn));
        formPanel.add(Box.createVerticalStrut(18));

        // 기본 필드
        formPanel.add(FieldUtil.createField("비밀번호", new JPasswordField(16)));
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(FieldUtil.createField("비밀번호 확인", new JPasswordField(16)));
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(FieldUtil.createField("이름", new JTextField(16)));
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(FieldUtil.createField("도로명 주소", new JTextField(16)));
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(FieldUtil.createField("상세 주소", new JTextField(16)));
        formPanel.add(Box.createVerticalStrut(18));
        
        // 권한 필드 + 콤보박스
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"user", "admin"});
        formPanel.add(FieldUtil.createComboField("권한", roleCombo));
        formPanel.add(Box.createVerticalStrut(32));
        
        // 버튼 생성
        JButton registBtn = ButtonUtil.createPrimaryButton("저장", 15, 120, 40);
        JButton listBtn = ButtonUtil.createDefaultButton("목록", 15, 120, 40);

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
}