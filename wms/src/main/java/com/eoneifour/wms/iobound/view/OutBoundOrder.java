package com.eoneifour.wms.iobound.view;

import java.awt.Color;
import java.awt.event.MouseAdapter;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.TableUtil;

public class OutBoundOrder extends AbstractTablePage {
	private AbstractMainFrame mainFrame;

	public OutBoundOrder(AbstractMainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		
		initTable();
		applyTableStyle();
	}

	@Override
	public void initTable() {
		String[] cols = { "제품명", "출고위치", "" };
		Object[][] data = { { "출고데이터_1", "5-5-5-5", "출고하기" }, { "출고데이터_2", "5-5-5-6", "출고하기" } };

		model = new DefaultTableModel(data, cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
	}
} 
