package com.eoneifour.wms.home.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.wms.admin.view.UserListPage;
import com.eoneifour.wms.common.config.Config;

/**
 * - WMS 관리자 메인 프레임 - 메인 프레임, 사이드바 구현
 * 
 * @author 재환
 * @since 2025. 6. 15.
 * @version 0.1
 */
public class MainFrame extends AbstractMainFrame {

	public MainFrame() {
		super("WMS 메인(관리자)"); // 타이틀 설정
		menuCardPanel.setPreferredSize(new Dimension(0, 50));
		initPages();
		showContent("HOME");
	}

	/***
	 * Content(세부 메뉴별 Panel) 클래스 생성 후 아래 메서드에서 new 해야함. 2번째 매개변수(String)는
	 * Config.java를 확인 후 대응되는 KEYS값을 넣어주면 됨.
	 * 
	 * @TODO 각 메뉴별 기능 구현이 완료되면 맵핑이나.. 다른 방식 활용해서 리팩토링 예정
	 * @author 재환
	 */
	private void initPages() {
		createSubMenu();

		// 홈 버튼 연결
		contentCardPanel.add(new HomePage(this), "HOME");

		// ex) 혜원님이 만들어놓은 패널
		contentCardPanel.add(new UserListPage(this), "ADMIN_REGISTER");

		// 초기 화면을 홈 화면으로 고정하기 위한 메서드.
		contentCardPanel.revalidate();
		contentCardPanel.repaint();

		// inforBar 이벤트 연결
	}

	// 상단패널
	@Override
	public JPanel createTopPanel() {
		JPanel infoBar = creatInfoBar();

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(infoBar, BorderLayout.NORTH);

		return topPanel;
	}

	// 상단패널에 부착할 InfoBar
	private JPanel creatInfoBar() {

		JPanel infoBar = new JPanel(new BorderLayout());
		infoBar.setPreferredSize(new Dimension(1280, 30));
		infoBar.setBackground(Color.BLACK);

		// 로고
		JButton homeBtn = new JButton("HOME");
		ButtonUtil.styleHeaderButton(homeBtn);
		homeBtn.setContentAreaFilled(false);
		homeBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		homeBtn.addActionListener(e -> showContent("HOME"));

		// 왼쪽 정렬 + 좌우 15pt,위아래 10px 여백을 위한 Panel
		JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		leftWrapper.setOpaque(false);
		leftWrapper.add(homeBtn);
		infoBar.add(leftWrapper, BorderLayout.WEST);

		// Right Panel: 관리자 이름 포함한 인삿말
		// TODO --> 추후, 계정 연동 필요
		String adminName = "운영자";
		JLabel adminInfoLabel = new JLabel(adminName + "님, 안녕하세요");
		adminInfoLabel.setForeground(Color.WHITE);
		// 오른쪽 정렬 + 좌우 15pt,위아래 10px 여백을 위한 Panel
		JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		rightWrapper.setOpaque(false); // 컴포넌트의 투명여부 설정
		rightWrapper.add(adminInfoLabel);
		infoBar.add(rightWrapper, BorderLayout.EAST);

		return infoBar;
	};

	@Override
	public JPanel createLeftPanel() {
		JPanel menuBar = createMainMenu();

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setPreferredSize(new Dimension(150, 0)); // 사이드바 폭 설정
		leftPanel.add(menuBar, BorderLayout.CENTER);

		return leftPanel;
	};

	private JPanel createMainMenu() {
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
		menuPanel.setBackground(Color.WHITE);

		for (int i = 0; i < Config.MENUNAME.length; i++) {
			int index = i;
			JButton button = new JButton(Config.MENUNAME[index]);
			ButtonUtil.styleMenuButton(button);
			button.addActionListener(e -> showTopMenu(Config.MENUKYES[index]));
			button.setAlignmentX(JButton.CENTER_ALIGNMENT);
			button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // 폭 자동 확장

			menuPanel.add(Box.createVerticalStrut(20)); // 간격 추가
			menuPanel.add(button);
		}
		// 홈메뉴 등록
		menuPanel.add(Box.createVerticalGlue()); // 아래 공간 채우기
		return menuPanel;
	};

	private void createSubMenu() {
		for (int i = 0; i < Config.PAGENAME.length; i++) {
			String groupKey = Config.MENUKYES[i];

			JPanel groupPanel = new JPanel();
			groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.X_AXIS));
			groupPanel.setBackground(Color.LIGHT_GRAY);

			for (int j = 0; j < Config.PAGENAME[i].length; j++) {
				groupPanel.add(Box.createHorizontalStrut(20));

				JButton button = new JButton(Config.PAGENAME[i][j]);
				ButtonUtil.styleMenuButton(button);
				final String PAGEKEY = Config.PAGEKEYS[i][j];
				button.addActionListener(e -> showContent(PAGEKEY));
				button.setAlignmentX(JButton.CENTER_ALIGNMENT);
				groupPanel.add(button);
			}
			menuCardPanel.add(groupPanel, groupKey);
		}
	}

	public void showTopMenu(String key) {
		CardLayout layout = (CardLayout) menuCardPanel.getLayout();
		layout.show(menuCardPanel, key);
	}
}
