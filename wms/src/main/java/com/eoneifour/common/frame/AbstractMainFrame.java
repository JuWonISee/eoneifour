package com.eoneifour.common.frame;

import javax.swing.*;
import java.awt.*;


/**
 * 모든 메인 프레임 화면 공통 기본 구조
 * - 상단, 좌측, 중앙 영역 자동 배치
 * - 자식 클래스에서 상단/좌측 패널 만들어서 넘기면 됨
 *
 * @author 혜원
 */
public abstract class AbstractMainFrame extends JFrame { 
    public JPanel topPanel;
    public JPanel leftPanel;
    public JPanel contentPanel;
    public JPanel cardTopPanel;
    public JPanel cardBottomPanel;
//    public CardLayout cardLayout; 미사용
    public CardLayout detailCardLayout;

    public AbstractMainFrame(String title) {
        // 상단 패널 (필요 없으면 null return)
        topPanel = createTopPanel();
        if(topPanel != null) add(topPanel, BorderLayout.NORTH);

        // 좌측 패널 (필요 없으면 null return)
        JPanel leftPanel = createLeftPanel();
        if (leftPanel != null) add(leftPanel, BorderLayout.WEST);
        
        // 메인 래퍼 패널
        contentPanel = new JPanel(new BorderLayout());
        
        // 위아래 패널
        cardTopPanel = new JPanel(new CardLayout());
        cardTopPanel.setPreferredSize(new Dimension(0, 50)); // 원하는 높이 지정
        
        cardBottomPanel = new JPanel(new CardLayout());
        
        contentPanel.add(cardTopPanel, BorderLayout.NORTH);
        contentPanel.add(cardBottomPanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // TODO --> 식별용 백그라운드. 추후 제거 필요
        contentPanel.setBackground(new Color(255,250,205));
        
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

    // 각 메뉴 클릭 시 화면 전환
    public void showTopPage(String key) {
        CardLayout layout = (CardLayout) cardTopPanel.getLayout();
        layout.show(cardTopPanel, key);
    }
    
    public void showBottomPage(String key) {
        CardLayout layout = (CardLayout) cardBottomPanel.getLayout();
        layout.show(cardBottomPanel, key);
    }

}
