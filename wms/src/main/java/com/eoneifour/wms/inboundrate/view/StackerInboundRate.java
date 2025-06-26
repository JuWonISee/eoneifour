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

import com.eoneifour.common.util.Refreshable;
import com.eoneifour.wms.home.view.MainFrame;
import com.eoneifour.wms.inboundrate.repository.RackDAO;

public class StackerInboundRate extends JPanel implements Refreshable{
    MainFrame mainFrame;
    RackDAO rackDAO;
    
    private static final Font FONT_TITLE     = new Font("맑은 고딕", Font.BOLD, 40);
    private static final Font FONT_RATE   	  = new Font("맑은 고딕", Font.BOLD, 40);
    private static final Font FONT_LABEL    = new Font("맑은 고딕", Font.BOLD, 20);
    private static final Font FONT_LEGEND = new Font("맑은 고딕", Font.BOLD, 22);
    
    public StackerInboundRate(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.rackDAO = new RackDAO();
    }

    private DefaultPieDataset createDataset(int stackerNum) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        int in             = rackDAO.selectRackStatusCnt(2, stackerNum);
        int waitOut     = rackDAO.selectRackStatusCnt(3, stackerNum);
        int emptyCELL = rackDAO.selectRackStatusCnt(0, stackerNum);
        int inIng         = rackDAO.selectRackStatusCnt(1, stackerNum);
        
        if (in > 0)             { dataset.setValue("입고"     	, in); }
        if (waitOut > 0)     { dataset.setValue("출고 대기", waitOut); }
        if (emptyCELL > 0) { dataset.setValue("공 CELL"	, emptyCELL); }
        if (inIng > 0)         { dataset.setValue("입고중"   	, inIng); }
        
        return dataset;
    }

    private JPanel createChartPanel(String title, int stackerNum, DefaultPieDataset dataset) {
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
                case "입고"     	: plot.setSectionPaint(key, new Color(0x2196F3))	; break;
                case "출고 대기"	: plot.setSectionPaint(key, new Color(0xFF9800))	; break;
                case "공 CELL"	: plot.setSectionPaint(key, new Color(0x4CAF50))	; break;
                case "입고중"   	: plot.setSectionPaint(key, new Color(0xFA8EE5))	; break;
                default: plot.setSectionPaint(key, Color.GRAY);
            }
        }

        int sum  = rackDAO.selectRackStatusCnt(2 , stackerNum) + rackDAO.selectRackStatusCnt(3, stackerNum);
        int total = rackDAO.selectRackStatusCnt(-1, stackerNum);
        
        int inboundRate = (int) (((double)sum / total) * 100);

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
        legendPanel.add(createLegendItem(new Color(0xFA8EE5), "입고중"));

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

	@Override
	public void refresh() {
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setBackground(Color.WHITE);
        chartsPanel.add(createChartPanel("1번 스태커", 1, createDataset(1)));
        chartsPanel.add(createChartPanel("2번 스태커", 2, createDataset(2)));
		
		removeAll();
	    
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(chartsPanel, BorderLayout.CENTER);
        add(createLegendPanel(), BorderLayout.SOUTH);

	    revalidate(); 
	    repaint();   
	}
}