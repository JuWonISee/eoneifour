package com.eoneifour.common.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * 버튼 공통 디자인 유틸 클래스
 * - infobar 버튼 : 빨강(기본) → 진한 빨강(호버)
 * - menubar 버튼 : 검정(기본) → 파랑(호버)
 * - 기본 버튼 (default/ primary / warning)
 * - 호버 시 마우스 커서 손모양 변경
 *
 * @author 혜원
 */
public class ButtonUtil {
	// 메뉴 버튼 색상 상수
	public static final Color MENU_NORMAL = new Color(51, 51, 51);      // #333333 기본회색
	public static final Color MENU_HOVER = new Color(13, 71, 161);      // #0D47A1 진한 파랑
	public static final Color MENU_ACTIVE = new Color(0, 87, 255);    // #1976D2 파랑

	// 헤더 버튼 색상 상수
	public static final Color HEADER_BG = new Color(231, 76, 60);  // 기본 빨간색
	public static final Color HEADER_HOVER = new Color(192, 57, 43); // hover시 진한빨강
    
    //기본 버튼 색상 상수
    public static final Color PRIMARY_COLOR = new Color(0, 104, 255);
    public static final Color WARNING_COLOR = new Color(231, 76, 60);
    public static final Color DEFAULT_COLOR = new Color(220, 220, 220);
    
    /**
     * InfoBar 버튼
     * @param btn 적용할 JButton
     */
    public static void styleHeaderButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setBackground(HEADER_BG);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(100, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // hover 효과
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(HEADER_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(HEADER_BG);
            }
        });
    }
    
    /**
     * 상단 메뉴 바 버튼
     * @param btn 적용할 JButton
     */
    public static void styleMenuButton(JButton btn) {
        // 기본 상태 설정
        btn.setFocusPainted(false); // 포커스 테두리 제거
        btn.setBorderPainted(false); // border 제거
        btn.setContentAreaFilled(false); // 배경 투명
        btn.setOpaque(false);
        btn.setForeground(MENU_NORMAL);
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 손모양 표시
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 16)); // 폰트 설정
        btn.putClientProperty("active", false); // 기본 상태는 비활성
        
        // 호버 시
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!(Boolean) btn.getClientProperty("active")) {
                    btn.setForeground(MENU_HOVER); // hover 색상
                    btn.setFont(new Font("맑은 고딕", Font.BOLD, 16)); // hover도 bold 유지
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!(Boolean) btn.getClientProperty("active")) {
                    btn.setForeground(MENU_NORMAL);
                    btn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
                    btn.setBorder(BorderFactory.createEmptyBorder());
                }
            }
        });
    }
        
    // 기본 Default 버튼
    public static JButton createDefaultButton(String text, int fontSize, int width, int height) {
        return createButton(text, fontSize, DEFAULT_COLOR, Color.BLACK, width, height);
    }

    // Primary 버튼 (파랑)
    public static JButton createPrimaryButton(String text, int fontSize, int width, int height) {
        return createButton(text, fontSize, PRIMARY_COLOR, Color.WHITE, width, height);
    }

    // Warning 버튼 (빨강)
    public static JButton createWarningButton(String text, int fontSize, int width, int height) {
        return createButton(text, fontSize, WARNING_COLOR, Color.WHITE, width, height);
    }

    // 공통 버튼 생성 메서드
    private static JButton createButton(String text, int fontSize, Color bgColor, Color fgColor, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.BOLD, fontSize));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(width, height));
        
        return button;
    }
    
    // 선택된 메뉴 active 효과
    public static void applyMenuActiveStyle(List<JButton> menuButtons, JButton activeButton) {
    	for (JButton btn : menuButtons) {
            btn.putClientProperty("active", false);
            btn.setForeground(MENU_NORMAL);
            btn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            btn.setBorder(BorderFactory.createEmptyBorder());
        }

        activeButton.putClientProperty("active", true);
        activeButton.setForeground(MENU_ACTIVE);
        activeButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
    }
}