package com.eoneifour.wms.admin.view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.eoneifour.wms.home.view.MainFrame;

public class AdminPage extends JPanel {

	private JFrame mainFrame;

	public AdminPage(MainFrame frame) {
		this.mainFrame = frame;
		setBackground(Color.BLACK);
	}
}
