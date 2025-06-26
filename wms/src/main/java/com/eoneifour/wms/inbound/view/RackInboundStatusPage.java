package com.eoneifour.wms.inbound.view;

import javax.swing.*;
import com.eoneifour.wms.home.view.MainFrame;
import com.eoneifour.wms.inbound.repository.StockProductDAO;
import com.eoneifour.wms.inboundrate.model.Rack;
import com.eoneifour.wms.inboundrate.repository.RackDAO;
import com.eoneifour.wms.iobound.model.StockProduct;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RackInboundStatusPage extends JPanel {
    private String currentStacker = "스태커1";
    private int s = 1;
    private JTextArea cellInfoArea;
    private JPanel gridPanel;

    private JButton stacker1Btn;
    private JButton stacker2Btn;

    private MainFrame mainFrame;
    private RackDAO rackDAO;
    private StockProductDAO stockProductDAO;
    private List<Rack> rackList = new ArrayList<>();
    private Timer refreshTimer; // ✅ 타이머 필드

    public RackInboundStatusPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.rackDAO = new RackDAO();
        this.stockProductDAO = new StockProductDAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // 🔝 상단: 스태커 선택 버튼 + 범례
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        topPanel.setBackground(Color.WHITE);

        stacker1Btn = new JButton("스태커1");
        stacker2Btn = new JButton("스태커2");

        stacker1Btn.addActionListener(e -> {
            currentStacker = "스태커1";
            s = 1;
            refreshGrid();
            refreshButtonHighlight();
        });

        stacker2Btn.addActionListener(e -> {
            currentStacker = "스태커2";
            s = 2;
            refreshGrid();
            refreshButtonHighlight();
        });

        topPanel.add(new JLabel("스태커 선택:"));
        topPanel.add(stacker1Btn);
        topPanel.add(stacker2Btn);
        topPanel.add(Box.createHorizontalStrut(40));
        topPanel.add(createLegendPanel());

        add(topPanel, BorderLayout.NORTH);

        gridPanel = new JPanel();
        gridPanel.setBackground(Color.WHITE);
        add(gridPanel, BorderLayout.CENTER);

        cellInfoArea = new JTextArea(6, 20);
        cellInfoArea.setEditable(false);
        cellInfoArea.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        cellInfoArea.setBorder(BorderFactory.createTitledBorder("셀 정보"));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(cellInfoArea), BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // 초기 표시 + 타이머 시작
        refreshGrid();
        refreshButtonHighlight();

        // 🔁 5초마다 자동 새로고침
        refreshTimer = new Timer(5000, e -> refreshGrid());
        refreshTimer.start();
    }

    private void refreshGrid() {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(2, 1, 20, 20));
        gridPanel.add(createColumnPanel("1열", s, 1));
        gridPanel.add(createColumnPanel("2열", s, 2));
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createColumnPanel(String columnName, int s, int z) {
        JPanel columnPanel = new JPanel(new BorderLayout(0, 5));
        JLabel label = new JLabel(columnName, SwingConstants.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        JPanel cells = new JPanel(new GridLayout(7, 7, 5, 5));
        rackList = rackDAO.selectAll();

        for (int col = 7; col >= 1; col--) {
            for (int row = 1; row <= 7; row++) {
                JButton btn = new JButton();
                setbtnBackground(s, z, row, col, btn);
                btn.setForeground(btn.getBackground());

                final int x = row;
                final int y = col;

                btn.addActionListener(e -> setCellInfoAreaText(s, z, x, y));
                cells.add(btn);
            }
        }

        columnPanel.add(label, BorderLayout.NORTH);
        columnPanel.add(cells, BorderLayout.CENTER);
        return columnPanel;
    }

    private void setCellInfoAreaText(int s, int z, int x, int y) {
        StockProduct stockProduct = stockProductDAO.selectInfo(s, z, x, y);

        if (stockProduct != null) {
            cellInfoArea.setText(
                "\n상품 번호 : " + stockProduct.getProductId() +
                "\n\n브랜드 : " + stockProduct.getProductBrand() +
                "\n\n상품명 : " + stockProduct.getProductName() +
                "\n\n위치 : " + stockProduct.getS() + " - " + stockProduct.getZ() + " - " + stockProduct.getX() + " - " + stockProduct.getY() +
                "\n\n상세사항 : " + stockProduct.getDetail()
            );
        } else {
            cellInfoArea.setText("\n상품 번호 : " + "\n\n브랜드 : " + "\n\n상품명 : " + "\n\n위치 : " + "\n\n상세사항 : ");
        }
    }

    private void setbtnBackground(int s, int z, int row, int col, JButton btn) {
        for (Rack rack : rackList) {
            if (rack.getS() == s && rack.getZ() == z && rack.getX() == row && rack.getY() == col) {
                switch (rack.getRack_status()) {
                    case 1 -> btn.setBackground(new Color(0xFA8EE5));
                    case 2 -> btn.setBackground(new Color(0x2196F3));
                    case 3 -> btn.setBackground(new Color(0xFF9800));
                    default -> btn.setBackground(Color.LIGHT_GRAY);
                }
                return;
            }
        }
        btn.setBackground(Color.LIGHT_GRAY);
    }

    private void refreshButtonHighlight() {
        stacker1Btn.setBackground(currentStacker.equals("스태커1") ? Color.YELLOW : null);
        stacker2Btn.setBackground(currentStacker.equals("스태커2") ? Color.YELLOW : null);
    }

    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        legendPanel.setBackground(Color.WHITE);

        legendPanel.add(createLegendItem(Color.LIGHT_GRAY, "공 CELL"));
        legendPanel.add(createLegendItem(new Color(0xFA8EE5), "입고중"));
        legendPanel.add(createLegendItem(new Color(0x2196F3), "입고완료"));
        legendPanel.add(createLegendItem(new Color(0xFF9800), "출고대기"));

        return legendPanel;
    }

    private JPanel createLegendItem(Color color, String labelText) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setBackground(Color.WHITE);

        JLabel colorBox = new JLabel();
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(20, 20));

        JLabel textLabel = new JLabel(labelText);
        textLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        item.add(colorBox);
        item.add(textLabel);
        return item;
    }
}
