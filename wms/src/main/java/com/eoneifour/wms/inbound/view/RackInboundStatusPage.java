package com.eoneifour.wms.inbound.view;

import javax.swing.*;

import com.eoneifour.wms.home.view.MainFrame;

import java.awt.*;

public class RackInboundStatusPage extends JPanel {
    private String currentStacker = "스태커1";
    private JTextArea cellInfoArea;
    private JPanel gridPanel;

    // 버튼 참조 저장
    private JButton stacker1Btn;
    private JButton stacker2Btn;
    
    private MainFrame mainFrame;

    public RackInboundStatusPage(MainFrame mainFrame) {
    	this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // 🔝 스태커 선택 버튼
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stacker1Btn = new JButton("스태커1");
        stacker2Btn = new JButton("스태커2");

        stacker1Btn.addActionListener(e -> {
            currentStacker = "스태커1";
            refreshGrid();
            refreshButtonHighlight();
        });

        stacker2Btn.addActionListener(e -> {
            currentStacker = "스태커2";
            refreshGrid();
            refreshButtonHighlight();
        });

        topPanel.add(new JLabel("스태커 선택:"));
        topPanel.add(stacker1Btn);
        topPanel.add(stacker2Btn);
        add(topPanel, BorderLayout.NORTH);

        // 중앙: 열(1,2)을 상하로 배치
        gridPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        gridPanel.setBackground(Color.WHITE);
        add(gridPanel, BorderLayout.CENTER);

        // 우측: 셀 정보 영역
        cellInfoArea = new JTextArea(6, 20);
        cellInfoArea.setEditable(false);
        cellInfoArea.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        cellInfoArea.setBorder(BorderFactory.createTitledBorder("셀 정보"));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(cellInfoArea), BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // 초기 표시
        refreshGrid();
        refreshButtonHighlight();
    }

    private void refreshGrid() {
        gridPanel.removeAll();

        gridPanel.add(createColumnPanel("1열"));
        gridPanel.add(createColumnPanel("2열"));

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createColumnPanel(String columnName) {
        JPanel columnPanel = new JPanel(new BorderLayout(0, 5));

        JLabel label = new JLabel(columnName, SwingConstants.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        JPanel cells = new JPanel(new GridLayout(7, 7, 5, 5));
        for (int i = 1; i <= 49; i++) {
            int row = 7 - ((i - 1) / 7);      // 행 번호 역순
            int col = ((i - 1) % 7) + 1;      // 열 번호
            
            JButton btn = new JButton("R" + row + "C" + col);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setForeground(btn.getBackground());
            
            
            btn.addActionListener(e -> {
                JButton source = (JButton) e.getSource();
                cellInfoArea.setText(
                    "\n" + currentStacker +
                    "\n\n열: " + columnName +
                    "\n\n위치: " + source.getText() +
                    "\n\n입고상태: 입고완료"
                );
            });
            cells.add(btn);
        }

        columnPanel.add(label, BorderLayout.NORTH);
        columnPanel.add(cells, BorderLayout.CENTER);
        return columnPanel;
    }

    private void refreshButtonHighlight() {
        stacker1Btn.setBackground(currentStacker.equals("스태커1") ? Color.YELLOW : null);
        stacker2Btn.setBackground(currentStacker.equals("스태커2") ? Color.YELLOW : null);
    }
}