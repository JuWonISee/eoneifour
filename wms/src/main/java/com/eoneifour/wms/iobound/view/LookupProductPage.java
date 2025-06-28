package com.eoneifour.wms.iobound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
import com.eoneifour.wms.iobound.model.selectAll;
import com.eoneifour.wms.iobound.repository.InBoundOrderDAO;

public class LookupProductPage extends AbstractTablePage implements Refreshable {
	private MainFrame mainFrame;
	public List<selectAll> productList;

	private InBoundOrderDAO inBoundOrderDAO;

	private String[] cols = { "ID", "상품명", "수량" };

	private JLabel keywordLabel;
	private JTextField searchField;

	public LookupProductPage(MainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.inBoundOrderDAO = new InBoundOrderDAO();
		keywordLabel = new JLabel("상품명");
		
		initTopPanel();
		initTable();
		applyTableStyle();
	}

	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 0, 30)); // 🎯 아래쪽 기준 여백

		// ▶ 서쪽(왼쪽): 제목
		JLabel title = new JLabel("상품별 입고대기 현황");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));

		JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		westPanel.setOpaque(false);
		westPanel.add(title);

		// ▶ 동쪽(오른쪽): 검색 라벨 + 필드 + 버튼
		keywordLabel = new JLabel("상품명");
		keywordLabel.setPreferredSize(new Dimension(60, 30));
		keywordLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

		searchField = new JTextField("상품명을 입력하세요");
		searchField.setPreferredSize(new Dimension(200, 30));
		searchField.setForeground(Color.GRAY);
		searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		placeholder();

		JButton searchBtn = ButtonUtil.createPrimaryButton("검색", 20, 100, 30);
		searchBtn.setBorderPainted(false);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		rightPanel.setOpaque(false);
		rightPanel.add(keywordLabel);
		rightPanel.add(searchField);
		rightPanel.add(searchBtn);

		// ▶ 부착
		topPanel.add(westPanel, BorderLayout.WEST);
		topPanel.add(rightPanel, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);

		// ▶ 검색 이벤트 처리
		searchBtn.addActionListener(e -> {
			String keyword = searchField.getText().trim();
			if (keyword.isEmpty() || keyword.equals("상품명을 입력하세요")) {
				productList = inBoundOrderDAO.selectGroupedProductCount(0);
			} else {
				productList = inBoundOrderDAO.selectGroupedProductCount(keyword, 0);
			}
			searchField.setText(null);
			model.setDataVector(toTableData(productList), cols);
			placeholder();
			applyStyle();

			JOptionPane.showMessageDialog(this,
				"총 " + productList.size() + "건의 검색 결과가 있습니다.",
				"검색 완료", JOptionPane.INFORMATION_MESSAGE);
		});

		searchField.addActionListener(e -> searchBtn.doClick());
	}


	@Override
	public void initTable() {
		productList = inBoundOrderDAO.selectGroupedProductCount(0);
		model = new DefaultTableModel(toTableData(productList), cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.setRowHeight(36); // cell 높이 설정
	}

	// 테이블 데이터로 변환
	private Object[][] toTableData(List<selectAll> stockProducts) {
		Object[][] data = new Object[stockProducts.size()][cols.length];
		for (int i = 0; i < stockProducts.size(); i++) {
			selectAll stock = stockProducts.get(i);
			data[i] = new Object[] { stock.getStockProductId(), stock.getProductName()
					,stock.getQuantity() + "box"};
		}
		return data;
	}

	// 새로고침시 적용할 스타일
	private void applyStyle() {
		TableUtil.applyDefaultTableStyle(table);
		table.getColumn("ID").setMinWidth(0);
		table.getColumn("ID").setMaxWidth(0);
		table.getColumn("ID").setPreferredWidth(0);
	}

	// 테이블 데이터 새로고침
	public void refresh() {
		productList = inBoundOrderDAO.selectGroupedProductCount(0);
		model.setDataVector(toTableData(productList), cols);
		placeholder();
		applyStyle();
	}

	// 검색 TextField에 placeholder 효과 주기 (forcus 이벤트 활용)
	public void placeholder() {
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
	}

}
