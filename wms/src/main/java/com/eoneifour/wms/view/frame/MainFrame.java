package com.eoneifour.wms.view.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;

/**
 *  - WMS 관리자 메인 프레임
 *  - 메인 프레임, 사이드바 구현
 * @author JH재환
 * @since 2025. 6. 15.
 * @version 0.1
 */
public class MainFrame extends AbstractMainFrame{

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
	
	private JPanel creatInfoBar() {
		JPanel infoBar = new JPanel(new BorderLayout());
		infoBar.setBackground(Color.BLACK);
		infoBar.setPreferredSize(new Dimension(1280, 50));
		
        // Left Panel: Logo 이미지 버튼 area
		// TODO => 아이콘 이쁜걸로 만드기
		ImageIcon icon = new ImageIcon("C:/lecture_workspace/proect_workspace/eoneifour/wms/src/main/resources/images/wmsLogo.png");
		JButton btn = new JButton(icon);
		btn.setPreferredSize(new Dimension(30, 30));
        // 왼쪽 정렬 + 좌우 15pt,위아래 10px 여백을 위한 Panel
        JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftWrapper.setOpaque(false);
        leftWrapper.add(btn);
        infoBar.add(leftWrapper, BorderLayout.WEST);
		
		// Right Panel: 관리자 이름 포함한 인삿말
		// TODO  --> 추후, 계정 연동 필요
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
	
    private JPanel createMenuBar() {
        JPanel menuBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        menuBar.setBackground(Color.WHITE);

        JButton adminBtn = new JButton("관리자");
        ButtonUtil.styleMenuButton(adminBtn);
        adminBtn.addActionListener(e->showPage("ADMIN"));

        JButton ioBtn = new JButton("입/출고");
        ButtonUtil.styleMenuButton(ioBtn);
        ioBtn.addActionListener(e->showPage("PRODUCT"));

        JButton inputBtn = new JButton("입/출고기록");
        ButtonUtil.styleMenuButton(inputBtn);
        inputBtn.addActionListener(e->showPage("ORDER"));

        JButton stockModifyBtn = new JButton("재고 수정");
        ButtonUtil.styleMenuButton(stockModifyBtn);
        stockModifyBtn.addActionListener(e->showPage("PURCHASE"));

        JButton recevingRateBtn= new JButton("입고율 조회");
        ButtonUtil.styleMenuButton(recevingRateBtn);
        recevingRateBtn.addActionListener(e->showPage("SETTING"));

        JButton recevingBtn = new JButton("입고 조회");
        ButtonUtil.styleMenuButton(recevingBtn);
        v.addActionListener(e->showPage("SETTING"));
        
        JButton shippingBtn = new JButton("출고 조회");
        ButtonUtil.styleMenuButton(shippingBtn);
        shippingBtn.addActionListener(e->showPage("SETTING"));
        
        JButton monitoringBtn = new JButton("모니터링");
        ButtonUtil.styleMenuButton(monitoringBtn);
        monitoringBtn.addActionListener(e->showPage("SETTING"));
        
        menuBar.add(adminBtn);
        menuBar.add(ioBtn);
        menuBar.add(ioBtn);
        menuBar.add(stockModifyBtn);
        menuBar.add(recevingRateBtn);
        menuBar.add(recevingBtn);
        menuBar.add(shippingBtn);
        menuBar.add(monitoringBtn);

        return menuBar;
    }

	@Override
	public JPanel createLeftPanel() {
		JPanel menuBar = createMenuBar();
		
		return menuBar;
	}

	public static void main(String[] args) {
		new MainFrame();
	}
}
