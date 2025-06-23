package com.eoneifour.common.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class AbstractModalDialog extends JDialog {

	protected JPanel formPanel;
	protected JPanel buttonPanel;

	protected JButton confirmBtn; // 확인 버튼
	protected JButton cancelBtn; // 취소 버튼

	protected boolean confirmed = false; // 확인 눌렀는지 알기 위한 변수

	public AbstractModalDialog(JFrame parent, String title) {
		super(parent, title, true); // 모달 설정
		setLayout(new BorderLayout());
		setSize(400, 250);
		setLocationRelativeTo(parent);

		// 폼 영역
		formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // 행은 동적으로, 2열
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
		add(formPanel, BorderLayout.CENTER);

		// 버튼 영역
		buttonPanel = new JPanel();
		createButtons(); // 버튼 생성은 별도 메서드로 위임
		add(buttonPanel, BorderLayout.SOUTH);

	}

	protected abstract void initComponents();
	protected abstract void createButtons();
	
	public boolean isConfirmed() {
		return confirmed;
	}

}
