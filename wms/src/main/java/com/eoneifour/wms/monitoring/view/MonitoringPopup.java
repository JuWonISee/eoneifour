package com.eoneifour.wms.monitoring.view;

import com.eoneifour.wms.home.view.MainFrame;
import javax.swing.*;

public class MonitoringPopup extends JFrame {
    private static MonitoringPopup instance;

    private MonitoringPopup(MainFrame mainFrame) {
        setTitle("📦 창고 모니터링");
        setSize(1330, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new MonitoringPage(mainFrame));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                instance = null; // 창 닫히면 다시 열 수 있도록 해줌
            }
        });

        setVisible(true);
    }

    public static void showPopup(MainFrame mainFrame) {
        if (instance == null) {
            instance = new MonitoringPopup(mainFrame);
        } else {
            instance.toFront();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // mainFrame은 실제 앱에서는 연동되지만 테스트 목적이라면 null 또는 더미로
            new MonitoringPopup(null); 
        });
    }
}
