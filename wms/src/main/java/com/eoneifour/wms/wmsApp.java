package com.eoneifour.wms;

import com.eoneifour.wms.auth.repository.AdminDAO;
import com.eoneifour.wms.home.view.MainFrame;

public class wmsApp {
	public static void main(String[] args) {
		// 이게 맞나...?
		new AdminDAO().insertDefaultAdmin();
		
		new MainFrame();
	}
}
	