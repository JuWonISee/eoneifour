package com.eoneifour.wms.iobound.view;

import java.awt.Color;
import java.awt.event.MouseAdapter;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.TableUtil;

public class InboundOrder extends AbstractTablePage {
	private AbstractMainFrame mainFrame;

	public InboundOrder(AbstractMainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		
		initTable();
		applyTableStyle();
	}

	@Override
	public void initTable() {
		String[] cols = { "제품명", "입고위치", "" };
		Object[][] data = { { "product_1", "1-2-12-8", "입고하기" }, { "product_2", "1-3-10-3", "입고하기" } };

		model = new DefaultTableModel(data, cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
	}
}
