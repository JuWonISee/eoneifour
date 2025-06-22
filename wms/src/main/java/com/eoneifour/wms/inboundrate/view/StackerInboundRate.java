package com.eoneifour.wms.inboundrate.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jfree.chart.*;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import com.eoneifour.wms.home.view.MainFrame;

public class StackerInboundRate extends JPanel {
    MainFrame mainFrame;
    private static final Font FONT_TITLE = new Font("맑은 고딕", Font.BOLD, 40);
    private static final Font FONT_RATE = new Font("맑은 고딕", Font.BOLD, 40);
    private static final Font FONT_LABEL = new Font("맑은 고딕", Font.BOLD, 20);
    private static final Font FONT_LEGEND = new Font("맑은 고딕", Font.BOLD, 22);

    public StackerInboundRate(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setBackground(Color.WHITE);
        chartsPanel.add(createChartPanel("1번 스태커", createDataset1()));
        chartsPanel.add(createChartPanel("2번 스태커", createDataset2()));

        add(chartsPanel, BorderLayout.CENTER);
        add(createLegendPanel(), BorderLayout.SOUTH);
    }

    private DefaultPieDataset createDataset1() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("입고", 25);
        dataset.setValue("공 CELL", 50);
        dataset.setValue("출고 대기", 25);
        return dataset;
    }

    private DefaultPieDataset createDataset2() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("입고", 50);
        dataset.setValue("공 CELL", 40);
        dataset.setValue("출고 대기", 10);
        return dataset;
    }

    private JPanel createChartPanel(String title, DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(null, dataset, false, false, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setSimpleLabels(true);
        plot.setLabelFont(FONT_LABEL);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}개", new DecimalFormat("0"), new DecimalFormat("0%")));

        for (Object keyObj : dataset.getKeys()) {
            Comparable key = (Comparable) keyObj;
            switch (key.toString()) {
                case "입고": plot.setSectionPaint(key, new Color(0x2196F3)); break;
                case "출고 대기": plot.setSectionPaint(key, new Color(0xFF9800)); break;
                case "공 CELL": plot.setSectionPaint(key, new Color(0x4CAF50)); break;
                default: plot.setSectionPaint(key, Color.GRAY);
            }
        }

        double total = 0;
        double inbound = 0;
        for (Object keyObj : dataset.getKeys()) {
            Comparable key = (Comparable) keyObj;
            Number value = dataset.getValue(key);
            if (value != null) {
                double val = value.doubleValue();
                total += val;
                if ("입고".equals(key)) {
                    inbound = val;
                }
            }
        }
        int inboundRate = (int) ((inbound / total) * 100);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel rateLabel = new JLabel(inboundRate + "%", SwingConstants.CENTER);
        rateLabel.setFont(FONT_RATE);
        rateLabel.setForeground(new Color(236, 0, 63));
        rateLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titleLabel);
        topPanel.add(rateLabel);

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new ChartPanel(chart), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        legendPanel.add(createLegendItem(new Color(0x2196F3), "입고"));
        legendPanel.add(createLegendItem(new Color(0x4CAF50), "공 CELL"));
        legendPanel.add(createLegendItem(new Color(0xFF9800), "출고 대기"));

        return legendPanel;
    }

    private JPanel createLegendItem(Color color, String label) {
        JPanel itemPanel = new JPanel(new BorderLayout(5, 0));
        itemPanel.setBackground(Color.WHITE);

        JLabel colorBox = new JLabel();
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(30, 30));

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(FONT_LEGEND);

        itemPanel.add(colorBox, BorderLayout.WEST);
        itemPanel.add(textLabel, BorderLayout.CENTER);

        return itemPanel;
    }

    // 전체 화면을 PNG로 저장하는 메서드
    public void saveAsPNG(String fileName) {
        int width = this.getWidth();
        int height = this.getHeight();
        if (width == 0 || height == 0) {
            this.setSize(1000, 600); // 최소 사이즈 지정 (필요 시 조정)
            this.doLayout();
            width = this.getWidth();
            height = this.getHeight();
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        this.paint(g2);
        g2.dispose();

        try {
            ImageIO.write(image, "png", new File(fileName));
            System.out.println("이미지 저장 완료: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}