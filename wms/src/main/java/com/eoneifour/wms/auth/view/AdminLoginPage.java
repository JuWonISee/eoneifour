package com.eoneifour.wms.auth.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.ImgUtil;
import com.eoneifour.wms.auth.model.Admin;
import com.eoneifour.wms.auth.repository.AdminDAO;
import com.eoneifour.wms.home.view.MainFrame;

/**
 * 관리자 로그인 페이지 클래스 - 이메일과 비밀번호를 입력 받아 로그인 처리 - 로그인 성공 시 관리자 정보를 mainFrame에 저장 후
 * HOME 페이지로 이동 - 로그인 실패 시 오류 메시지 출력
 * 
 * ※ 현재 관리자 가입 버튼은 UI에서 제거됨 (joinBtn 제거됨)
 */
public class AdminLoginPage extends JPanel {

	private MainFrame mainFrame; // 메인 프레임 참조 (로그인 후 페이지 전환 및 관리자 정보 갱신에 사용)
	private Image backgroundImage;

	// 입력 필드 및 버튼
	private JTextField emailField; // 이메일 입력 필드
	private JPasswordField passwordField; // 비밀번호 입력 필드
	private JButton loginBtn; // 로그인 버튼

	private AdminDAO adminDAO; // 관리자 인증을 위한 DAO 객체

	// 생성자 - UI 구성 및 이벤트 등록
	public AdminLoginPage(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.adminDAO = new AdminDAO(); // DAO 초기화 (DB 연동용)

		// 레이아웃 설정
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		ImgUtil imgUtil = new ImgUtil();
		backgroundImage = imgUtil.getImage("images/homeImage2.png", 1280, 800);

		// 중앙 박스 UI 생성
		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
		box.setBackground(new Color(245, 247, 250));
		box.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
				BorderFactory.createEmptyBorder(30, 40, 30, 40)));
		box.setMaximumSize(new Dimension(400, 240));
		box.setAlignmentX(Component.CENTER_ALIGNMENT);

		// 타이틀
		JLabel title = new JLabel("로그인");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);

		Dimension labelSize = new Dimension(100, 36);
		Dimension fieldSize = new Dimension(200, 36);

		// 이메일 입력 UI
		JPanel emailPanel = new JPanel();
		emailPanel.setOpaque(false);
		emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.X_AXIS));
		JLabel emailLabel = new JLabel("이메일");
		emailLabel.setPreferredSize(labelSize);
		emailField = new JTextField("admin@wms.com");
		emailField.setPreferredSize(fieldSize);
		emailPanel.add(emailLabel);
		emailPanel.add(Box.createHorizontalStrut(10));
		emailPanel.add(emailField);

		// 비밀번호 입력 UI
		JPanel pwPanel = new JPanel();
		pwPanel.setOpaque(false);
		pwPanel.setLayout(new BoxLayout(pwPanel, BoxLayout.X_AXIS));
		JLabel pwLabel = new JLabel("비밀번호");
		pwLabel.setPreferredSize(labelSize);
		passwordField = new JPasswordField("1111");
		passwordField.setPreferredSize(fieldSize);
		pwPanel.add(pwLabel);
		pwPanel.add(Box.createHorizontalStrut(10));
		pwPanel.add(passwordField);

		// 로그인 버튼 설정 및 이벤트 등록
		loginBtn = ButtonUtil.createPrimaryButton("로그인", 14, 120, 38);
		loginBtn.addActionListener(e -> doLogin()); // 버튼 클릭 시 로그인 시도

		// 버튼 패널
		JPanel btnPanel = new JPanel();
		btnPanel.setOpaque(false);
		btnPanel.add(loginBtn);

		// UI 조립
		box.add(title);
		box.add(Box.createVerticalStrut(20));
		box.add(emailPanel);
		box.add(Box.createVerticalStrut(10));
		box.add(pwPanel);
		box.add(Box.createVerticalStrut(20));
		box.add(btnPanel);

		// 중앙 정렬
		add(Box.createVerticalGlue());
		add(box);
		add(Box.createVerticalGlue());
	}

	private void doLogin() {
		String email = emailField.getText().trim(); // 이메일 입력값 가져오기
		String pw = new String(passwordField.getPassword()); // 비밀번호 입력값 가져오기

		Admin admin = adminDAO.login(email, pw); // DB에서 인증 시도

		if (admin != null) {
			// 로그인 성공 시
			JOptionPane.showMessageDialog(this, "로그인 성공" + admin.getName() + "님");

			mainFrame.admin = admin; // 로그인 정보 저장
			mainFrame.leftPanel.setVisible(true);
//			mainFrame.activateAllSubMenus();
			mainFrame.setAdminInfo(admin.getName() + "님 반갑습니다."); // 상단에 관리자 이름 표시

			// 비활성화 되어있는 메뉴 버튼들 활성화
			for (JButton btn : mainFrame.menuButtons) {
				btn.setEnabled(true);
			}

			mainFrame.menuPanel.setBackground(new Color(33, 33, 33));
			mainFrame.menuPanel.setOpaque(true);
			mainFrame.menuPanel.repaint();

			mainFrame.adminEditPage.getAdminInfo(); // 수정 페이지에 관리자 정보 미리 설정

			// 입력 필드 초기화
			emailField.setText("");
			passwordField.setText("");

			mainFrame.showContent("HOME"); // HOME 페이지로 이동

		} else {
			// 로그인 실패
			JOptionPane.showMessageDialog(this, "이메일 또는 비밀번호가 틀렸습니다.");
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (backgroundImage != null) {
			// 패널 크기에 맞게 이미지 늘려 그리기
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
