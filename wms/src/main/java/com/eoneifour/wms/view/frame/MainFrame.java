package com.eoneifour.wms.view.frame;

import java.awt.BorderLayout;
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

/**
 * - WMS 관리자 메인 프레임 - 메인 프레임, 사이드바 구현
 * 
 * @author JH재환
 * @since 2025. 6. 15.
 * @version 0.1
 */
public class MainFrame extends AbstractMainFrame {

	public MainFrame() {
		super("WMS 메인(관리자)"); // 타이틀 설정
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
		infoBar.setPreferredSize(new Dimension(1280, 50));
		infoBar.setBackground(Color.BLACK);

		JLabel homeLabel = new JLabel("HOME");
		homeLabel.setOpaque(false);
		homeLabel.setForeground(Color.WHITE);
		homeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));

		// 왼쪽 정렬 + 좌우 15pt,위아래 10px 여백을 위한 Panel
		JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		leftWrapper.setOpaque(false);
		leftWrapper.add(homeLabel);
		infoBar.add(leftWrapper, BorderLayout.WEST);

		// Right Panel: 관리자 이름 포함한 인삿말
		// TODO --> 추후, 계정 연동 필요
		String adminName = "운영자";
		JLabel adminInfoLabel = new JLabel(adminName + "님, 안녕하세요");
		adminInfoLabel.setForeground(Color.WHITE);
		// 오른쪽 정렬 + 좌우 15pt,위아래 10px 여백을 위한 Panel
		JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		rightWrapper.setOpaque(false); // 컴포넌트의 투명여부 설정
		rightWrapper.add(adminInfoLabel);
		infoBar.add(rightWrapper, BorderLayout.EAST);

		return infoBar;
	}

	@Override
	public JPanel createLeftPanel() {
		JPanel menuBar = createMenuBar();

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setPreferredSize(new Dimension(150, 0)); // 사이드바 폭 설정
		leftPanel.add(menuBar, BorderLayout.CENTER);

		return leftPanel;
	}

	private JPanel createMenuBar() {
		JPanel menuBar = new JPanel();
		menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS));
		menuBar.setBackground(Color.WHITE);

		String[] menuNames = { "관리자", "입/출고", "입/출고기록", "재고 수정", "입고율 조회", "입고 조회", "출고 조회", "모니터링" };
		String[] pageKeys = { "ADMIN", "RECEIV_SHIP", "RECEIV_SHIP_LOG", "STOCK_MODIFY", "RECEVING_RATE", "RECEVING",
				"SHIPPING", "MONITORING" };

		for (int i = 0; i < menuNames.length; i++) {
			JButton button = new JButton(menuNames[i]);
			ButtonUtil.styleMenuButton(button);
			final String pageKey = pageKeys[i];
			button.setAlignmentX(JButton.CENTER_ALIGNMENT);
			button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // 폭 자동 확장

			menuBar.add(Box.createVerticalStrut(20)); // 간격 추가
			menuBar.add(button);
		}

		menuBar.add(Box.createVerticalGlue()); // 아래 공간 채우기
		return menuBar;
	}

	public static void main(String[] args) {
		new MainFrame();
	}
}
