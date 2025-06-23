package com.eoneifour.common.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.util.Refreshable;
 
/**
 * 공통 테이블 페이지
 * - initTable() : 자식 클래스에서 상단패널/테이블/모델 설정
 * - applyTableStyle : 테이블 스타일 고정 (자식 클래스에서 반드시 호출)
 * @author 혜원
 */
public abstract class AbstractTablePage extends JPanel{
    public JTable table;
    public DefaultTableModel model;

    public AbstractTablePage(AbstractMainFrame mainFrame) {
        setLayout(new BorderLayout());
    }

    // 테이블 및 모델 생성
    public abstract void initTable();

    // 테이블 스타일
    public void applyTableStyle() {
    	table.setRowHeight(40);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setIntercellSpacing(new Dimension(0, 5));
        
        

        // 테이블 래퍼 및 여백
        JScrollPane scroll = new JScrollPane(table);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        wrapper.add(scroll, BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);
    }
}
