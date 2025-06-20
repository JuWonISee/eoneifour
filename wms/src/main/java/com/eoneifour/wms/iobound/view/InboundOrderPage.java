package com.eoneifour.wms.iobound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.TableUtil;

public class InboundOrderPage extends AbstractTablePage {
	private AbstractMainFrame mainFrame;

	public InboundOrderPage(AbstractMainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		
		initTopPanel();
		initTable();
		applyTableStyle();
	}
	
	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
        // 패널 안쪽 여백 설정 (시계반대방향)
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 0, 50));
		
        // 제목 라벨
        JLabel title = new JLabel("제품 목록");
        title.setFont(new Font("맑은 고딕",Font.BOLD,20));
        topPanel.add(title, BorderLayout.WEST);
        
        // 검색 키워드
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        
        // 등록 버튼
        JButton searchBtn = ButtonUtil.createPrimaryButton("검색", 20, 100, 30);

        searchBtn.setBorderPainted(false);
        searchBtn.addActionListener(e -> {
//        	userRegistPage.prepare();
//			mainFrame.showContent("USER_REGIST");
        });
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        rightPanel.setOpaque(false);	
        rightPanel.add(searchField);
        rightPanel.add(searchBtn);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
	}

	@Override
	public void initTable() {
		String[] cols = { "제품명", "입고위치", "입고" };
		Object[][] data = { { "DRINK_1", "1-1-1-1", "입고" }, { "DRINK_1", "2-2-2-2", "입고하기" } };

		model = new DefaultTableModel(data, cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);    
		table.setRowHeight(20);
		
		TableUtil.applyDefaultTableStyle(table);
		TableUtil.applyColorTextRenderer(table, "입고" , new Color(25, 118, 210));
	}
}
