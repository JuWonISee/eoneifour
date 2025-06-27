package com.eoneifour.common.frame;

import javax.swing.*;

import com.eoneifour.common.util.Refreshable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 모든 메인 프레임 화면 공통 기본 구조 - 상단, 좌측, 중앙 영역 자동 배치 - 자식 클래스에서 상단/좌측 패널 만들어서 넘기면 됨
 *
 * @author 혜원
 */
public abstract class AbstractMainFrame extends JFrame {
	public JPanel topPanel;
	public JPanel leftPanel;
	public JPanel centerWrapperPanel;

//    public CardLayout cardLayout; 미사용
	public JPanel menuCardPanel;

//    변수 추가
	public JPanel contentCardPanel;
	// 메뉴 버튼 리스트 변수 추가(@hw)
	public List<JButton> menuButtons = new ArrayList<>();
	public AbstractMainFrame(String title) {
		// 상단 패널 (필요 없으면 null return)
		topPanel = createTopPanel();
		if (topPanel != null)
			add(topPanel, BorderLayout.NORTH);

		// 좌측 패널 (필요 없으면 null return)
		JPanel leftPanel = createLeftPanel();
		if (leftPanel != null)
			add(leftPanel, BorderLayout.WEST);

		// 카드 레이아웃 두개를 담을 중앙 패널
		centerWrapperPanel = new JPanel(new BorderLayout());

		// 상단 메뉴바(카드 레이아웃)
		menuCardPanel = new JPanel(new CardLayout());

		contentCardPanel = new JPanel(new CardLayout());

		centerWrapperPanel.add(menuCardPanel, BorderLayout.NORTH);
		centerWrapperPanel.add(contentCardPanel, BorderLayout.CENTER);

		add(centerWrapperPanel, BorderLayout.CENTER);

		// 기본 프레임 설정
		setSize(1280, 800); // 창 크기
		setLocationRelativeTo(null); // 창 가운데 배치
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(title); // 윈도우 타이틀 설정
		setVisible(true);
	}

	// 상단 nav panel
	public abstract JPanel createTopPanel();

	// 좌측 side panel
	public abstract JPanel createLeftPanel();

	/**  v                                                                                                                      
	 * 페이지(메뉴) 선택시, Refreshable을 구현한 페이지일 경우 최신 상태로 새로고침되는 기능 추가하였습니다.
	 * @author JH
	 */
	public void showContent(String key) {
		CardLayout layout = (CardLayout) contentCardPanel.getLayout();
		layout.show(contentCardPanel, key);

		// 현재 보여지는 컴포넌트를 찾아서 refresh() 호출
		for (Component comp : contentCardPanel.getComponents()) {
			if (comp.isVisible() && comp instanceof Refreshable refreshable) {
				refreshable.refresh(); // 페이지가 보일 때 자동 갱신
			}
		}
	}

}
