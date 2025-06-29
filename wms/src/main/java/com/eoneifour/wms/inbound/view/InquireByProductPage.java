package com.eoneifour.wms.inbound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.Refreshable;
import com.eoneifour.common.util.TableUtil;
import com.eoneifour.wms.home.view.MainFrame;
import com.eoneifour.wms.inbound.repository.StockProductDAO;
import com.eoneifour.wms.iobound.model.StockProduct;
import com.eoneifour.wms.iohistory.model.IoHistory;

public class InquireByProductPage extends AbstractTablePage implements Refreshable {
	private MainFrame mainFrame;
	private StockProductDAO stockProductDAO;
	List<StockProduct> list;
	private String[] cols = { "ID", "제품명", "입고일", "입고위치" };

	private JTextField searchField;
	private JLabel keywordLabel;

	public InquireByProductPage(MainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.stockProductDAO = new StockProductDAO();

		keywordLabel = new JLabel("제품명");

		initTopPanel();
		initTable();
		applyTableStyle();
		applyPlaceholderEvents();
	}

	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 0, 30));

		// ▶ 서쪽: 타이틀 추가
		JLabel title = new JLabel("제품명별 입고상태");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		title.setPreferredSize(new Dimension(200, 30));

		JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		westPanel.setOpaque(false);
		westPanel.add(title);

		// ▶
		searchField = new JTextField("제품명을 입력하세요");
		searchField.setPreferredSize(new Dimension(160, 30));
		searchField.setForeground(Color.GRAY);

		keywordLabel.setPreferredSize(new Dimension(60, 30));

		keywordLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

		JButton searchBtn = ButtonUtil.createPrimaryButton("검색", 20, 100, 30);
		searchBtn.setBorderPainted(false);
		searchBtn.addActionListener(e -> performSearch());
		searchField.addActionListener(e -> performSearch());

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		rightPanel.setOpaque(false);
		rightPanel.add(keywordLabel);
		rightPanel.add(searchField);
		rightPanel.add(searchBtn);

		// 부착
		topPanel.add(westPanel, BorderLayout.WEST);
		topPanel.add(rightPanel, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);
	}

	public void initTable() {
		list = stockProductDAO.selectAll(true);
		model = new DefaultTableModel(toTableData(list), cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.setRowHeight(36);
	}

	private void performSearch() {
		String keyword = searchField.getText().trim();

		Date startDate = null, endDate = null;

		if (keyword.equals("제품명을 입력하세요"))
			keyword = "";

		if (!keyword.isBlank()) {
			list = stockProductDAO.searchByCondition(keyword);
		} else {
			list = stockProductDAO.selectAll(true);
		}

		if (list.isEmpty()) {
			JOptionPane.showMessageDialog(this, "조건에 맞는 제품이 없습니다.\n입력값을 다시 확인해주세요!", "검색 결과 없음",
					JOptionPane.INFORMATION_MESSAGE);
			model.setRowCount(0); // 테이블을 비워줘요
			return;
		}

		model.setDataVector(toTableData(list), cols);
		applyStyle();
		JOptionPane.showMessageDialog(this, "총 " + list.size() + "건의 검색 결과가 있습니다.", "검색 완료",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private Object[][] toTableData(List<StockProduct> list) {
		if (list == null || list.isEmpty())
			return new Object[0][cols.length];
		Object[][] data = new Object[list.size()][cols.length];
		for (int i = 0; i < list.size(); i++) {
			StockProduct h = list.get(i);
			String pos = h.getS() + "-" + h.getZ() + "-" + h.getX() + "-" + h.getY();
			data[i] = new Object[] { h.getStockProductId(),h.getProductName(), h.getStock_time(), pos };
		}
		return data;
	}

	private void applyStyle() {
		TableUtil.applyDefaultTableStyle(table);
		table.getColumn("ID").setMinWidth(0);
		table.getColumn("ID").setMaxWidth(0);
		table.getColumn("ID").setPreferredWidth(0);
	}

	@Override
	public void refresh() {
		list = stockProductDAO.selectAll(true);
		model.setDataVector(toTableData(list), cols);
		applyStyle();
	}

	private void applyPlaceholderEvents() {
		// 제품명
		searchField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (searchField.getText().equals("제품명을 입력하세요")) {
					searchField.setText("");
					searchField.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) {
				if (searchField.getText().isEmpty()) {
					searchField.setForeground(Color.GRAY);
					searchField.setText("제품명을 입력하세요");
				}
			}
		});

	}
}