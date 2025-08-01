package com.eoneifour.common.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

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
        centerTableCells(table);
    }
    
    /**
     * 테이블 전체 가운데 정렬
     * @param table
     */
    public static void centerTableCells(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * 특정 컬럼만 원하는 색상으로 폰트 색상 변경
     * @param table
     * @param columnName
     * @param fontColor
     */
    public static void applyColorTextRenderer(JTable table, String columnName, Color fontColor) {
        table.getColumn(columnName).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );

                // 컬러 강조만 변경, 나머지 스타일은 기본 유지
                label.setForeground(fontColor);
                label.setFont(new Font("맑은 고딕", Font.BOLD, 13));
                label.setHorizontalAlignment(SwingConstants.CENTER);

                return label;
            }
        });
    }
    
    /**
     * 특정 컬럼에서 특정 셀 값에만 원하는 색상 적용
     * @param table JTable 객체
     * @param columnName 적용할 컬럼명
     * @param targetValue 특정 셀 값
     * @param fontColor 해당 값일 때 적용할 폰트 색상
     */
    public static void applyConditionalTextRenderer(JTable table, String columnName, String targetValue, Color fontColor) {
        table.getColumn(columnName).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );

                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("맑은 고딕", Font.BOLD, 13));

                // 값이 일치하면 지정된 색상 적용, 아니면 기본색
                if (targetValue.equals(value)) {
                    label.setForeground(fontColor);
                } else {
                    label.setForeground(Color.BLACK); // 기본 색상
                }

                return label;
            }
        });
    }

}
