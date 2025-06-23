package com.eoneifour.wms.iobound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import com.eoneifour.wms.iobound.model.InBoundOrder;
import com.eoneifour.wms.iobound.repository.InBoundOrderDAO;

public class InboundOrderPage extends AbstractTablePage implements Refreshable {
	private MainFrame mainFrame;

	private List<InBoundOrder> orderList;
	private InBoundOrderDAO inBoundOrderDAO;
	private String[] cols = { "ID", "상품명", "입고위치", "작업" };

	public InboundOrderPage(MainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.inBoundOrderDAO = new InBoundOrderDAO();

		initTopPanel();
		initTable();
		applyTableStyle();
	}

	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		// 패널 안쪽 여백 설정 (시계반대방향)
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 0, 50));

		// 제목 라벨
		JLabel title = new JLabel("제품 목록");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		topPanel.add(title, BorderLayout.WEST);

		// 검색 키워드
		JTextField searchField = new JTextField("");
		searchField.setPreferredSize(new Dimension(200, 30));

		// 등록 버튼
		JButton searchBtn = ButtonUtil.createPrimaryButton("검색", 20, 100, 30);

		searchBtn.setBorderPainted(false);
		searchBtn.addActionListener(e -> {
			String keyword = searchField.getText().trim();
			List<InBoundOrder> searchResults;

			if (!keyword.isEmpty()) {
				searchResults = inBoundOrderDAO.searchByProductName(keyword);
				searchField.setText(null);
			} else {
				// keyword가 비어있을 경우 전체 목록 다시 조회
				searchResults = inBoundOrderDAO.getOrderList();
				searchField.setText(null);
			}

			if (searchResults.isEmpty()) {
				JOptionPane.showMessageDialog(null, "해당 제품이 없습니다.", "Info", JOptionPane.INFORMATION_MESSAGE);
				searchResults = inBoundOrderDAO.getOrderList();
				searchField.setText(null);
			}

			model.setDataVector(toTableData(searchResults), cols);
			applyStyle();
		});

		// 엔터 이벤트
		searchField.addActionListener(e -> {
			searchBtn.doClick(); //
		});

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		rightPanel.setOpaque(false);
		rightPanel.add(searchField);
		rightPanel.add(searchBtn);
		topPanel.add(rightPanel, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);
	}

	@Override
	public void initTable() {
		orderList = inBoundOrderDAO.getOrderList();

		model = new DefaultTableModel(toTableData(orderList), cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.setRowHeight(36); // cell 높이 설정
		
		// ID는 숨김
		table.getColumn("ID").setMinWidth(0);
		table.getColumn("ID").setMaxWidth(0);
		table.getColumn("ID").setPreferredWidth(0);


		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());
				int orderId = (int) model.getValueAt(row, 0);

				if (col == table.getColumn("작업").getModelIndex()) {
					// 입고 처리 로직
					inBoundOrderDAO.inBound(orderId);
					String msg = "입고처리가 완료되었습니다.";
					JOptionPane.showMessageDialog(null, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
					refresh();
				}
			}
		});
	}

	// 테이블로 데이터로 변환
	private Object[][] toTableData(List<InBoundOrder> orderList) {
		Object[][] data = new Object[orderList.size()][cols.length];
		for (int i = 0; i < orderList.size(); i++) {
			InBoundOrder order = orderList.get(i);
			data[i] = new Object[] { 
				order.getPurchase_order_id(), //ID 숨김
				order.getProduct().getName(), "입고 위치 로직은 구현중" + i, "입고" };
		}
		return data;
	}

	private void applyStyle() {
		TableUtil.applyDefaultTableStyle(table);
		TableUtil.applyColorTextRenderer(table, "작업", new Color(25, 118, 210));
	}

	// 테이블 데이터 새로고침
	public void refresh() {
		orderList = inBoundOrderDAO.getOrderList();
		model.setDataVector(toTableData(orderList), cols);
		applyStyle();
	}
}
