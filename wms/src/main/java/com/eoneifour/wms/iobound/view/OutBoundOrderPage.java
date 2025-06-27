package com.eoneifour.wms.iobound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
import com.eoneifour.wms.iobound.model.StockProduct;
import com.eoneifour.wms.iobound.repository.OutBoundOrderDAO;

public class OutBoundOrderPage extends AbstractTablePage implements Refreshable {

	private MainFrame mainFrame;
	private OutBoundOrderDAO outBoundOrderDAO;
	private List<StockProduct> outboundList;
	private String[] cols = { "ID", "상품명", "출고위치", "작업" };
	private JTextField searchField;

	JPanel topPanel;
	JPanel westPanel;
	JLabel title;
	JButton outboundAllBtn;
	JButton searchBtn;

	public OutBoundOrderPage(MainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.outBoundOrderDAO = new OutBoundOrderDAO();

		initTopPanel();
		initTable();
		applyTableStyle();
	}

	public void initTopPanel() {
		// 생성 영역
		topPanel = new JPanel(new BorderLayout());
		westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		title = new JLabel("출고 대기 물품");
		outboundAllBtn = new JButton("일괄 출고");
		searchField = new JTextField("상품명을 입력하세요(공백 : 전체검색)");
		searchBtn = ButtonUtil.createPrimaryButton("검색", 20, 100, 30);

		// 디자인 영역
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		outboundAllBtn.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		outboundAllBtn.setBorderPainted(false);
		searchField.setPreferredSize(new Dimension(200, 30));
		searchField.setForeground(Color.GRAY);
		searchBtn.setBorderPainted(false);
		westPanel.setOpaque(false);
		westPanel.add(title);

		// 부착 영역
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.setOpaque(false);
		rightPanel.add(searchField);
		rightPanel.add(searchBtn);
		westPanel.add(outboundAllBtn);
		topPanel.add(westPanel, BorderLayout.WEST);
		topPanel.add(rightPanel, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);

		// 이벤트 영역
		outboundAllBtn.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(null, "모든 상품을 출고 처리하시겠습니까?", "일괄 출고 확인",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (result == JOptionPane.YES_OPTION) {
				for (int i = 0; i < model.getRowCount(); i++) {
					int stockId = (int) model.getValueAt(i, 0);
					outBoundOrderDAO.releaseProductById(stockId);
				}
				JOptionPane.showMessageDialog(null, "일괄 출고가 완료되었습니다.");
				refresh();
			}
		});

		placeholder(); // placeholder 이벤트 설정

		searchBtn.addActionListener(e -> {
			String keyword = searchField.getText().trim();
			List<StockProduct> searchList;

			if (!keyword.isEmpty() && !keyword.equals("상품명을 입력하세요(공백 : 전체검색)")) {
				searchList = outBoundOrderDAO.searchByProductName(keyword, 3);
				searchField.setText(null);
				placeholder();
			} else {
				// keyword가 비어있을 경우 전체 목록 다시 조회
				searchList = outBoundOrderDAO.getOutBoundList();
				searchField.setText(null);
				placeholder();
			}

			if (searchList.isEmpty()) {
				JOptionPane.showMessageDialog(null, "해당 제품이 없습니다.", "Info", JOptionPane.INFORMATION_MESSAGE);
				searchField.setText(null);
				placeholder();
			}

			model.setDataVector(toTableData(searchList), cols);
			applyStyle();
		});

		searchField.addActionListener(e -> searchBtn.doClick());
	}

	@Override
	public void initTable() {
		outboundList = outBoundOrderDAO.getOutBoundList();

		model = new DefaultTableModel(toTableData(outboundList), cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.setRowHeight(36);
		table.getColumn("ID").setMinWidth(0);
		table.getColumn("ID").setMaxWidth(0);
		table.getColumn("ID").setPreferredWidth(0);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());

				if (col == table.getColumn("작업").getModelIndex()) {
					int id = (int) table.getValueAt(row, 0);

					List<StockProduct> list = new ArrayList<>();
					outBoundOrderDAO.releaseProductById(id); // 출고 완료 상태 변경

					JOptionPane.showMessageDialog(mainFrame, "출고지시가 완료되었습니다.", "Success!",
							JOptionPane.INFORMATION_MESSAGE);
					refresh();
				}
			}
		});
	}

	private Object[][] toTableData(List<StockProduct> list) {
		Object[][] data = new Object[list.size()][cols.length];
		for (int i = 0; i < list.size(); i++) {
			StockProduct sp = list.get(i);
			String position = sp.getS() + "-" + sp.getZ() + "-" + sp.getX() + "-" + sp.getY();

			data[i] = new Object[] { sp.getStockProductId(), sp.getProductName(), position, // 출고위치 표시
					"출고" };
		}
		return data;
	}

	private void applyStyle() {
		TableUtil.applyDefaultTableStyle(table);
		TableUtil.applyColorTextRenderer(table, "작업", new Color(25, 118, 210));
		table.getColumn("ID").setMinWidth(0);
		table.getColumn("ID").setMaxWidth(0);
		table.getColumn("ID").setPreferredWidth(0);
	}

	public void refresh() {
		outboundList = outBoundOrderDAO.getOutBoundList();
		model.setDataVector(toTableData(outboundList), cols);
		applyStyle();
	}

	public void placeholder() {
		searchField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (searchField.getText().equals("상품명을 입력하세요(공백 : 전체검색)")) {
					searchField.setText("");
					searchField.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) {
				if (searchField.getText().isEmpty()) {
					searchField.setForeground(Color.GRAY);
					searchField.setText("상품명을 입력하세요(공백 : 전체검색)");
				}
			}
		});
	}
}