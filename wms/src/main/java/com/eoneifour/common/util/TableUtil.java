package com.eoneifour.common.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 테이블 공통 유틸리티 클래스
 * - 컬럼 이동 제한, 전체 정렬, 컬럼 렌더러 적용 등 처리
 */
public class TableUtil {

    /**
     * 테이블 공통 스타일 적용
     * - 컬럼 이동 제한
     * - 전체 가운데 정렬
     */
    public static void applyDefaultTableStyle(JTable table) {
        // 컬럼 이동 못하게
        table.getTableHeader().setReorderingAllowed(false);

        // 전체 가운데 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * 특정 컬럼에만 컬러 박스 렌더러 적용
     * @param table 적용할 테이블
     * @param columnName 컬럼명
     * @param color 배경색
     */
    public static void applyColoredLabelRenderer(JTable table, String columnName, Color color) {
        table.getColumn(columnName).setCellRenderer((tbl, val, sel, focus, row, col) -> {
            JLabel label = new JLabel(val.toString(), SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(color);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("맑은 고딕", Font.BOLD, 13));
            label.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
            return label;
        });
    }
}
