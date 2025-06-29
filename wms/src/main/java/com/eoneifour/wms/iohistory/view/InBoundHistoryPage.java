package com.eoneifour.wms.iohistory.view;

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
import com.eoneifour.wms.iohistory.model.IoHistory;
import com.eoneifour.wms.iohistory.repository.IOHistoryDAO;

public class InBoundHistoryPage extends AbstractTablePage implements Refreshable {
	private MainFrame mainFrame;
	private IOHistoryDAO ioHistoryDAO;

	private List<IoHistory> list;
	private String[] cols = { "ID", "입고일", "제품명", "입고위치" };

	private JTextField searchField;
	private JTextField startDateField;
	private JTextField endDateField;
	private JLabel keywordLabel;
	private JLabel startLabel;
	private JLabel endLabel;

	public InBoundHistoryPage(MainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.ioHistoryDAO = new IOHistoryDAO();

		keywordLabel = new JLabel("제품명");
		startLabel = new JLabel("날짜");
		endLabel = new JLabel("~");

		initTopPanel();
		initTable();
		applyTableStyle();
		applyPlaceholderEvents();
	}

	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 0, 30));

		// ▶ 서쪽: 타이틀 추가
		JLabel title = new JLabel("입고 로그");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		title.setPreferredSize(new Dimension(200, 30));

		JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		westPanel.setOpaque(false);
		westPanel.add(title);

		// ▶
		searchField = new JTextField("제품명을 입력하세요");
		searchField.setPreferredSize(new Dimension(160, 30));
		searchField.setForeground(Color.GRAY);

		startDateField = new JTextField("2000-01-01");
		startDateField.setPreferredSize(new Dimension(130, 30));
		startDateField.setForeground(Color.GRAY);

		endDateField = new JTextField("2099-12-31");
		endDateField.setPreferredSize(new Dimension(130, 30));
		endDateField.setForeground(Color.GRAY);

		keywordLabel.setPreferredSize(new Dimension(60, 30));
		startLabel.setPreferredSize(new Dimension(40, 30));
		endLabel.setPreferredSize(new Dimension(15, 30));

		keywordLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		startLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		endLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

		JButton searchBtn = ButtonUtil.createPrimaryButton("검색", 20, 100, 30);
		searchBtn.setBorderPainted(false);
		searchBtn.addActionListener(e -> performSearch());
		searchField.addActionListener(e -> performSearch());

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		rightPanel.setOpaque(false);
		rightPanel.add(keywordLabel);
		rightPanel.add(searchField);
		rightPanel.add(startLabel);
		rightPanel.add(startDateField);
		rightPanel.add(endLabel);
		rightPanel.add(endDateField);
		rightPanel.add(searchBtn);

		// 부착
		topPanel.add(westPanel, BorderLayout.WEST);
		topPanel.add(rightPanel, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);
	}

	public void initTable() {
		list = ioHistoryDAO.selectAll();
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
		String startStr = startDateField.getText().trim();
		String endStr = endDateField.getText().trim();

		Date startDate = null, endDate = null;

		if (startStr.isEmpty() || startStr.equals("시작일(YYYY-MM-DD)"))
			startStr = "2000-01-01";
		if (endStr.isEmpty() || endStr.equals("종료일(YYYY-MM-DD)"))
			endStr = "2099-12-31";

		try {
			startDate = Date.valueOf(startStr);
			endDate = Date.valueOf(endStr);
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, "날짜 형식을 정확히 입력해주세요 (yyyy-MM-dd)", "날짜 오류", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (keyword.equals("상품명을 입력하세요"))
			keyword = "";

		if (!keyword.isBlank() && startDate != null && endDate != null) {
			list = ioHistoryDAO.searchByCondition(keyword, startDate, endDate);
		} else if (!keyword.isBlank()) {
			list = ioHistoryDAO.searchByCondition(keyword);
		} else if (startDate != null && endDate != null) {
			list = ioHistoryDAO.searchByCondition(startDate, endDate);
		} else {
			list = ioHistoryDAO.selectAll();
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

	private Object[][] toTableData(List<IoHistory> historyList) {
		if (historyList == null || historyList.isEmpty())
			return new Object[0][cols.length];
		Object[][] data = new Object[historyList.size()][cols.length];
		for (int i = 0; i < historyList.size(); i++) {
			IoHistory h = historyList.get(i);
			String pos = h.getS() + "-" + h.getZ() + "-" + h.getX() + "-" + h.getY();
			data[i] = new Object[] { h.getStock_log_id(), h.getStock_date(), h.getProduct_name(), pos };
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
		list = ioHistoryDAO.selectAll();
		model.setDataVector(toTableData(list), cols);
		applyStyle();
	}

	private void applyPlaceholderEvents() {
		// 상품명
		searchField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (searchField.getText().equals("상품명을 입력하세요")) {
					searchField.setText("");
					searchField.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) {
				if (searchField.getText().isEmpty()) {
					searchField.setForeground(Color.GRAY);
					searchField.setText("상품명을 입력하세요");
				}
			}
		});

		// 시작일
		startDateField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (startDateField.getText().equals("2000-01-01")) {
					startDateField.setText("");
					startDateField.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) {
				if (startDateField.getText().isEmpty()) {
					startDateField.setForeground(Color.GRAY);
					startDateField.setText("2000-01-01");
				}
			}
		});

		// 종료일
		endDateField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (endDateField.getText().equals("2099-12-31")) {
					endDateField.setText("");
					endDateField.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) {
				if (endDateField.getText().isEmpty()) {
					endDateField.setForeground(Color.GRAY);
					endDateField.setText("2099-12-31");
				}
			}
		});
	}
}