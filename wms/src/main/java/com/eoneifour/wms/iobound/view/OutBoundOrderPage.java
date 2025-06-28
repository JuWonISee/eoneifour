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
import com.eoneifour.wms.iobound.repository.OutBoundOrderDAO;

public class OutBoundOrderPage extends AbstractTablePage implements Refreshable {

	private MainFrame mainFrame;
	private OutBoundOrderDAO outBoundOrderDAO;
	private List<selectAll> list;
	private String[] cols = { "ID", "상품명", "출고위치", "작업" };
	private JTextField searchField;
	
	
	private JLabel keywordLabel;
	private JPanel topPanel;
	private JPanel westPanel;
	private JLabel title;
	private JButton outboundAllBtn;
	private JButton searchBtn;

	public OutBoundOrderPage(MainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.outBoundOrderDAO = new OutBoundOrderDAO();
		keywordLabel = new JLabel("상품명");
		
		initTopPanel();
		initTable();
		applyTableStyle();
	}

	public void initTopPanel() {
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 0, 30)); // 상단 여백

		// 왼쪽 타이틀 + 일괄 출고 버튼
		title = new JLabel("출고 대기 물품");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		title.setPreferredSize(new Dimension(200, 30));

		outboundAllBtn = new JButton("일괄 출고");
		outboundAllBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		outboundAllBtn.setBorderPainted(false);

		JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		westPanel.setOpaque(false);
		westPanel.add(title);
		westPanel.add(outboundAllBtn);

		// 검색 라벨 + 필드 + 버튼
		keywordLabel = new JLabel("상품명");
		keywordLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		keywordLabel.setPreferredSize(new Dimension(60, 30));

		searchField = new JTextField("상품명을 입력하세요");
		searchField.setPreferredSize(new Dimension(200, 30));
		searchField.setForeground(Color.GRAY);
		searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		applySearchPlaceholder();

		searchBtn = ButtonUtil.createPrimaryButton("검색", 20, 100, 30);
		searchBtn.setBorderPainted(false);

		// 검색 패널 구성
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		rightPanel.setOpaque(false);
		rightPanel.add(keywordLabel);
		rightPanel.add(Box.createHorizontalStrut(5));
		rightPanel.add(searchField);
		rightPanel.add(Box.createHorizontalStrut(5));
		rightPanel.add(searchBtn);

		// 부착
		topPanel.add(westPanel, BorderLayout.WEST);
		topPanel.add(rightPanel, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);

		// 이벤트: 일괄 출고
		outboundAllBtn.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(
				null,
				"모든 상품을 출고 처리하시겠습니까?",
				"일괄 출고 확인",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
			);

			if (result == JOptionPane.YES_OPTION) {
				for (int i = 0; i < model.getRowCount(); i++) {
					int stockId = (int) model.getValueAt(i, 0);
					outBoundOrderDAO.releaseProductById(stockId);
				}
				JOptionPane.showMessageDialog(null, "일괄 출고가 완료되었습니다.");
				refresh();
			}
		});

		// 검색 필드에서 엔터 눌러도 버튼 실행
		searchField.addActionListener(e -> searchBtn.doClick());
	}

	@Override
	public void initTable() {
		list = outBoundOrderDAO.getOutBoundList();

		model = new DefaultTableModel(toTableData(list), cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.setRowHeight(36);
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());

				if (col == table.getColumn("작업").getModelIndex()) {
					int id = (int) table.getValueAt(row, 0);

					List<selectAll> list = new ArrayList<>();
					outBoundOrderDAO.releaseProductById(id); // 출고 완료 상태 변경

					JOptionPane.showMessageDialog(mainFrame, "출고지시가 완료되었습니다.", "Success!",
							JOptionPane.INFORMATION_MESSAGE);
					refresh();
				}
			}
		});
	}

	private Object[][] toTableData(List<selectAll> list) {
		Object[][] data = new Object[list.size()][cols.length];
		for (int i = 0; i < list.size(); i++) {
			selectAll sp = list.get(i);
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
		list = outBoundOrderDAO.getOutBoundList();
		model.setDataVector(toTableData(list), cols);
		applyStyle();
	}

	private void applySearchPlaceholder() {
		searchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchField.getText().equals("상품명을 입력하세요")) {
					searchField.setText("");
					searchField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchField.getText().isEmpty()) {
					searchField.setForeground(Color.GRAY);
					searchField.setText("상품명을 입력하세요");
				}
			}
		});
	}
}