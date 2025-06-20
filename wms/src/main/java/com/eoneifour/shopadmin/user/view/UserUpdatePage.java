package com.eoneifour.shopadmin.user.view;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.eoneifour.shopadmin.view.frame.ShopAdminMainFrame;

public class UserUpdatePage extends JPanel{
private ShopAdminMainFrame mainFrame;
	public UserUpdatePage(ShopAdminMainFrame mainFrame) {
		this.mainFrame = mainFrame;
    	
    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 247, 250)); 

        JPanel formPanel = initFormPanel();
        
        add(Box.createVerticalGlue());
    	add(formPanel);
    	add(Box.createVerticalGlue());
	}
	      
	private JPanel initFormPanel() {
		JPanel formPanel = new JPanel();
		
		return formPanel;
	}
}
