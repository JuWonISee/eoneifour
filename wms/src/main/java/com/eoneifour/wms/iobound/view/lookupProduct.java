package com.eoneifour.wms.iobound.view;

import java.awt.Color;
import java.awt.event.MouseAdapter;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.TableUtil;

public class lookupProduct extends AbstractTablePage {
	private AbstractMainFrame mainFrame;

	public lookupProduct(AbstractMainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		
		initTable();
		applyTableStyle();
	}

	@Override
	public void initTable() {
		String[] cols = {"제품명"};
		Object[][] data = {{"DRINK"},{"DEST"},{"COLLECTION"}};

		model = new DefaultTableModel(data, cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
	}
} 
