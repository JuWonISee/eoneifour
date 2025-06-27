package com.eoneifour.wms.inboundrate.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jfree.chart.*;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.*;

import com.eoneifour.common.util.Refreshable;
import com.eoneifour.wms.home.view.MainFrame;
import com.eoneifour.wms.inboundrate.model.Rack;
import com.eoneifour.wms.inboundrate.repository.RackDAO;

public class AllInboundRatePage extends JPanel implements Refreshable{

	private static final Color[] BAR_COLORS = { 
			new Color(0x4CAF50), // 공Cell
			new Color(0x2196F3), // 입고
			new Color(0xFF9800), // 출고대기
			new Color(0xFA8EE5), // 입고중
			Color.GRAY              // 입고대기
	};

	private static final Font FONT_TITLE      = new Font("맑은 고딕", Font.BOLD, 28);
	private static final Font FONT_PERCENT = new Font("맑은 고딕", Font.BOLD, 60);
	private static final Font FONT_LABEL     = new Font("맑은 고딕", Font.BOLD, 40);
	private static final Font FONT_LEGEND  = new Font("맑은 고딕", Font.BOLD, 20);

	private MainFrame mainFrame;
	private JFreeChart chart;
	private CategoryPlot plot;
	private RackDAO rackDAO;
	private List<Rack> rackList = new ArrayList<>();

	public AllInboundRatePage(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.rackDAO = new RackDAO();
	}

	private CategoryDataset createDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(rackDAO.selectRackStatusCnt(0), "공Cell"	 , "");
		dataset.addValue(rackDAO.selectRackStatusCnt(3), "입고"	 , "");
		dataset.addValue(rackDAO.selectRackStatusCnt(2), "입고중"	 , "");
		dataset.addValue(rackDAO.selectRackStatusCnt(4), "출고대기", "");
		dataset.addValue(rackDAO.selectRackStatusCnt(1), "입고대기", "");
		
		return dataset;
	}

	private JFreeChart createChart(CategoryDataset dataset) {
		return ChartFactory.createBarChart(null, null, null, dataset);
	}

	private void configureLegendFont() {
		if (chart.getLegend() != null) {
			chart.getLegend().setItemFont(FONT_LEGEND);
			chart.getLegend().setPosition(RectangleEdge.TOP);
			chart.getLegend().setItemLabelPadding(new RectangleInsets(5, 20, 5, 20));  // 위, 왼쪽, 아래, 오른쪽 여백
			chart.getLegend().setFrame(new BlockBorder(Color.WHITE)); // 경계선 제거 (선택사항)
		}
	}
	
	private void customizeLegendItems() {
	    LegendItemCollection legendItems = new LegendItemCollection();

	    String[] labels = { "공Cell", "입고", "출고대기", "입고중", "입고대기" };
	    
	    for (int i = 0; i < labels.length; i++) {
	        Shape shape = new Rectangle(25, 25); // 사각형 크기 (폭, 높이)
	        LegendItem item = new LegendItem(labels[i], null, null, null, shape, BAR_COLORS[i]);
	        item.setLabelFont(FONT_LEGEND); // 텍스트 폰트 설정
	        legendItems.add(item);
	    }

	    plot.setFixedLegendItems(legendItems);
	}

	private void configurePlotStyle() {
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlineVisible(false);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);
		plot.getDomainAxis().setVisible(false);
		plot.getRangeAxis().setVisible(false);
		plot.getRangeAxis().setRange(0, 200);
	}

	private void configureRenderer(final CategoryDataset dataset) {
		BarRenderer renderer = new BarRenderer() {
			@Override
			public ItemLabelPosition getPositiveItemLabelPosition(int row, int column) {
				return new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER);
			}
		};

		renderer.setDefaultItemLabelFont(FONT_LABEL);
		renderer.setDefaultItemLabelsVisible(true);

		renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator() {
			@Override
			public String generateLabel(CategoryDataset ds, int row, int column) {
				Number value = ds.getValue(row, column);
				return value != null ? value.intValue() + "개" : "";
			}
		});

		for (int i = 0; i < BAR_COLORS.length; i++) {
			renderer.setSeriesPaint(i, BAR_COLORS[i]);
			renderer.setSeriesItemLabelPaint(i, BAR_COLORS[i]);
		}

		plot.setRenderer(renderer);
	}

	private JPanel createTopPanel(CategoryDataset dataset) {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);

		JLabel titleLabel = new JLabel("전체 입고율", SwingConstants.CENTER);
		titleLabel.setFont(FONT_TITLE);
		titleLabel.setForeground(new Color(0x333333));

	    int percentValue = (int)((double)getSumFromDataset(dataset, "입고", "출고대기") / rackDAO.selectRackStatusCnt(-1)  * 100);
		JLabel percentLabel = new JLabel(percentValue + "%", SwingConstants.CENTER);
		percentLabel.setFont(FONT_PERCENT);
		percentLabel.setForeground(new Color(236, 0, 63)); // 입고 색상과 매칭

		topPanel.add(titleLabel, BorderLayout.NORTH);
		topPanel.add(percentLabel, BorderLayout.CENTER);

		return topPanel;
	}
	
	private int getSumFromDataset(CategoryDataset dataset, String... rowKeys) {
	    int sum = 0;
	    for (String key : rowKeys) {
	        for (int i = 0; i < dataset.getRowCount(); i++) {
	            if (key.equals(dataset.getRowKey(i))) {
	                Number value = dataset.getValue(i, 0);
	                sum += (value != null) ? value.intValue() : 0;
	                break;  // 찾았으면 다음 key로
	            }
	        }
	    }
	    return sum;
	}

	private ChartPanel createChartPanel() {
		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new Dimension(400, 220));
		panel.setBackground(Color.WHITE);
		return panel;
	}

	@Override
	public void refresh() {
		if(chart != null) {
	        chart.removeLegend();
	    }

	    CategoryDataset dataset = createDataset();
	    this.chart = createChart(dataset);
	    this.plot = chart.getCategoryPlot();

	    configureLegendFont();
	    customizeLegendItems();
	    configurePlotStyle();
	    configureRenderer(dataset);

	    removeAll(); 
	    setLayout(new BorderLayout());
	    add(createTopPanel(dataset), BorderLayout.NORTH);
	    add(createChartPanel(), BorderLayout.CENTER);

	    revalidate(); 
	    repaint();    
	}
}