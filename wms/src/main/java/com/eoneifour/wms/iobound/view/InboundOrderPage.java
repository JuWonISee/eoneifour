package com.eoneifour.wms.iobound.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
import com.eoneifour.shopadmin.purchaseOrder.model.PurchaseOrder;
import com.eoneifour.shopadmin.purchaseOrder.repository.PurchaseOrderDAO;
import com.eoneifour.wms.home.view.MainFrame;
import com.eoneifour.wms.iobound.model.StockProduct;
import com.eoneifour.wms.iobound.repository.InBoundOrderDAO;

public class InboundOrderPage extends AbstractTablePage implements Refreshable {
	private MainFrame mainFrame;

	// 창고도착 건수
	private int watingCount;
	JLabel unloading;

	// 창고도착인 리스트
	private List<StockProduct> waitingUnloadList;

	// 입고대기(1)중인 리스트
	private List<StockProduct> waitingInboundList;

	// 발주되어있는 리스트.
	private List<PurchaseOrder> purchaseList;

	private PurchaseOrderDAO purchaseOrderDAO;
	private InBoundOrderDAO inBoundOrderDAO;
	private String[] cols = { "ID", "상품명", "입고위치", "작업" };

	public InboundOrderPage(MainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.inBoundOrderDAO = new InBoundOrderDAO();
		this.purchaseOrderDAO = new PurchaseOrderDAO();

		initTopPanel();
		initTable();
		applyTableStyle();
	}

	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		// 패널 안쪽 여백 설정 (시계반대방향)
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 0, 50));

		// 제목 라벨
		JLabel title = new JLabel("입고 대기 물품");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		topPanel.add(title, BorderLayout.WEST);

		// 하차대기 물건
		unloading = new JLabel("하차 대기 : 0건");
		unloading.setFont(new Font("맑은 고딕", Font.BOLD, 20));

		JButton unloadingBtn = ButtonUtil.createPrimaryButton("상품 하차", 20, 140, 30);

		// 검색 키워드
		JTextField searchField = new JTextField("");
		searchField.setPreferredSize(new Dimension(200, 30));

		// 등록 버튼
		JButton searchBtn = ButtonUtil.createPrimaryButton("검색", 20, 100, 30);

		searchBtn.setBorderPainted(false);
		searchBtn.addActionListener(e -> {
			String keyword = searchField.getText().trim();
			List<StockProduct> searchList;

			if (!keyword.isEmpty()) {
				searchList = inBoundOrderDAO.searchByProductName(keyword, 1);
				searchField.setText(null);
			} else {
				// keyword가 비어있을 경우 전체 목록 다시 조회
				searchList = inBoundOrderDAO.selectByStatus(1);
				searchField.setText(null);
			}

			if (searchList.isEmpty()) {
				JOptionPane.showMessageDialog(null, "해당 제품이 없습니다.", "Info", JOptionPane.INFORMATION_MESSAGE);
				refresh();
				searchField.setText(null);
			}

			model.setDataVector(toTableData(searchList), cols);
			applyStyle();
		});

		// 하차 버튼 클릭 이벤트
		unloadingBtn.addActionListener(e -> {
			inBoundOrderDAO.insertByList(waitingUnloadList);
			for(PurchaseOrder purchaseOrder : purchaseList) {
				int id = purchaseOrder.getPurchase_order_id();
				purchaseOrderDAO.updateStatus(id, "완료");
			}
			refresh();
		});

		// 엔터 이벤트
		searchField.addActionListener(e -> {
			searchBtn.doClick(); //
		});

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		rightPanel.setOpaque(false);
		rightPanel.add(unloading);
		rightPanel.add(unloadingBtn);
		rightPanel.add(searchField);
		rightPanel.add(searchBtn);
		topPanel.add(rightPanel, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);
	}

	@Override
	public void initTable() {
		waitingInboundList = inBoundOrderDAO.selectByStatus(1);

		model = new DefaultTableModel(toTableData(waitingInboundList), cols) {
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

				int id = (int) table.getValueAt(row, 0);

				// 작업 버튼 클릭시 입고 프로세스 진행
				if (col == table.getColumn("작업").getModelIndex()) {

					// stock_product 테이블의 status 변경
					inBoundOrderDAO.updateStatus(id, 1);
					System.out.println(id);

					// -문자로 구분된 SZXY를 잘라서 int[] 배열에 넣기
					String posionStr = (String) model.getValueAt(row, 2);
					int[] position = Arrays.stream(posionStr.split("-")).map(String::trim).mapToInt(Integer::parseInt)
							.toArray();

					JOptionPane.showMessageDialog(mainFrame, "입고처리가 완료되었습니다", "Success!",
							JOptionPane.INFORMATION_MESSAGE);
					refresh();
				}
			}
		});
	}

	// 테이블로 데이터로 변환
	private Object[][] toTableData(List<StockProduct> stockProducts) {
		Object[][] data = new Object[stockProducts.size()][cols.length];
		for (int i = 0; i < stockProducts.size(); i++) {
			StockProduct order = stockProducts.get(i);
			data[i] = new Object[] { 
					order.getStockprodutId(), // ID 숨김
					order.getProductName(),
					order.getS()+"-"+order.getZ()+"-"+order.getX()+"-"+order.getY(),
					"입고" };
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

	// 테이블 데이터 새로고침
	public void refresh() {
		watingCount = purchaseToStock();
		unloading.setText("하차 대기 : " + watingCount + "건");

		waitingInboundList = inBoundOrderDAO.selectByStatus(0);
		model.setDataVector(toTableData(waitingInboundList), cols);
		applyStyle();

	}

	// 발주테이블에 접근하여 전체 발주 리스트 받아오기.
	public int purchaseToStock() {
		purchaseList = new ArrayList<>();
		purchaseList = purchaseOrderDAO.searchByStatus("창고도착");
		waitingUnloadList = new ArrayList<>();
		
		int size = purchaseList.size();
		int quantity = 0;
		for (int i = 0; i < size; i++) {
			PurchaseOrder po = purchaseList.get(i);
			quantity = po.getQuantity();
			for (int j = 0; j < quantity; j++) {
				StockProduct stockProduct = new StockProduct();
				stockProduct.setProductId(po.getProduct().getProduct_id());
				stockProduct.setProductBrand(po.getProduct().getBrand_name());
				stockProduct.setProductName(po.getProduct().getName());
				stockProduct.setDetail(po.getProduct().getDetail());
				stockProduct.setS(i);
				stockProduct.setZ(i);
				stockProduct.setX(j);
				stockProduct.setY(j);
				waitingUnloadList.add(stockProduct);
			}
		}
		return size;
	}
}
