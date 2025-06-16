package com.eoneifour.wms.view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.eoneifour.wms.view.frame.MainFrame;

public class AdminPage extends JPanel{
	
	private JFrame mainFrame;
	
	public AdminPage(MainFrame frame) {
		this.mainFrame = frame;
		setBackground(Color.BLACK);
	}
}
