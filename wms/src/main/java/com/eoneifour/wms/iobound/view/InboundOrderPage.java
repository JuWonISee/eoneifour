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
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.eoneifour.wms.inboundrate.model.Rack;
import com.eoneifour.wms.inboundrate.repository.RackDAO;
import com.eoneifour.wms.iobound.model.StockProduct;
import com.eoneifour.wms.iobound.repository.InBoundOrderDAO;

public class InboundOrderPage extends AbstractTablePage implements Refreshable {
	private MainFrame mainFrame;

	// 창고도착 건수
	private int waitingCount;
	JLabel unloading;

	// 창고도착인 리스트
	private List<StockProduct> waitingUnloadList;

	// 입고대기(1)중인 리스트
	private List<StockProduct> waitingInboundList;

	// 발주되어있는 리스트.
	private List<PurchaseOrder> purchaseList;

	private PurchaseOrderDAO purchaseOrderDAO;
	private InBoundOrderDAO inBoundOrderDAO;
	private RackDAO rackDAO;

	private String[] cols = { "ID", "상품명", "입고위치", "작업" };

	JTextField searchField;

	public InboundOrderPage(MainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.inBoundOrderDAO = new InBoundOrderDAO();
		this.purchaseOrderDAO = new PurchaseOrderDAO();
		this.rackDAO = new RackDAO();

		initTopPanel();
		initTable();
		applyTableStyle();
	}

	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		westPanel.setOpaque(false);

		// 제목 라벨
		JLabel title = new JLabel("입고 대기 물품");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		westPanel.add(title);

		// 제목 라벨
		JButton inboundAllBtn = new JButton("일괄 입고");
		inboundAllBtn.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		inboundAllBtn.setBorderPainted(false);
		inboundAllBtn.addActionListener(e -> {

			int result = JOptionPane.showConfirmDialog(null, "모든 상품을 입고 처리하시겠습니까?", "일괄 입고 확인",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (result == JOptionPane.YES_OPTION) {
				for (int i = 0; i < model.getRowCount(); i++) {
					int stockId = (int) model.getValueAt(i, 0);
					String posStr = (String) model.getValueAt(i, 2); // "s-z-x-y"
					int[] pos = Arrays.stream(posStr.split("-")).mapToInt(Integer::parseInt).toArray();

					int s = pos[0];
					int z = pos[1];
					int x = pos[2];
					int y = pos[3];

					inBoundOrderDAO.updateStatusWithPosition(stockId, 1, s, z, x, y);
					rackDAO.updateRackStatus(s, z, x, y, 1); // 입고되었으므로 랙 사용 처리
				}

				JOptionPane.showMessageDialog(null, "일괄 입고가 완료되었습니다.");
				refresh();
			}

		});

		westPanel.add(inboundAllBtn);

		topPanel.add(westPanel, BorderLayout.WEST);

		// 하차대기 물건
		unloading = new JLabel("하차 대기 : 0건");
		unloading.setFont(new Font("맑은 고딕", Font.BOLD, 20));

		JButton unloadingBtn = ButtonUtil.createPrimaryButton("상품 하차", 20, 140, 30);

		// 검색 키워드
		searchField = new JTextField("상품명을 입력하세요(공백 : 전체검색)");
		searchField.setPreferredSize(new Dimension(200, 30));
		placeholder();

		// 등록 버튼
		JButton searchBtn = ButtonUtil.createPrimaryButton("검색", 20, 100, 30);

		searchBtn.setBorderPainted(false);
		searchBtn.addActionListener(e -> {
			String keyword = searchField.getText().trim();
			List<StockProduct> searchList;

			if (!keyword.isEmpty() || keyword == "카테고리명 또는 상품명을 입력하세요") {
				searchList = inBoundOrderDAO.searchByProductName(keyword, 1);
				searchField.setText(null);
				placeholder();
			} else {
				// keyword가 비어있을 경우 전체 목록 다시 조회
				searchList = inBoundOrderDAO.selectByStatus(1);
				searchField.setText(null);
				placeholder();
			}

			if (searchList.isEmpty()) {
				JOptionPane.showMessageDialog(null, "해당 제품이 없습니다.", "Info", JOptionPane.INFORMATION_MESSAGE);
				searchField.setText(null);
				placeholder();
				refresh();
			}

			model.setDataVector(toTableData(searchList), cols);
			applyStyle();
		});

		// 하차 버튼 클릭 이벤트
		unloadingBtn.addActionListener(e -> {
			inBoundOrderDAO.insertByList(waitingUnloadList);
			for (PurchaseOrder purchaseOrder : purchaseList) {
				int id = purchaseOrder.getPurchase_order_id();
				purchaseOrderDAO.updateStatus(id, "입고완료");
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

					// 테이블에서 위치값 받아 온후 파싱하기
					String posStr = (String) model.getValueAt(row, 2);
					int[] position = Arrays.stream(posStr.split("-")).mapToInt(Integer::parseInt).toArray();

					// 파싱한 위치값 입력하기
					int s = position[0];
					int z = position[1];
					int x = position[2];
					int y = position[3];

					inBoundOrderDAO.updateStatusWithPosition(id, 1, s, z, x, y);
					rackDAO.updateRackStatus(s, z, x, y, 1); // status = 1로 사용중 처리
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
			StockProduct stock = stockProducts.get(i);
			String pos = stock.getS() + "-" + stock.getZ() + "-" + stock.getX() + "-" + stock.getY();
			data[i] = new Object[] { stock.getStockprodutId(), stock.getProductName(), pos, "입고" };
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

	// 검색 TextField에 placeholder 효과 주기 (forcus 이벤트 활용)
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

	// 테이블 데이터 새로고침
	public void refresh() {
		waitingCount = purchaseToStock();
		unloading.setText("하차 대기 : " + waitingCount + "건");

		waitingInboundList = inBoundOrderDAO.selectByStatus(0);
		model.setDataVector(toTableData(waitingInboundList), cols);
		applyStyle();

	}

	public int purchaseToStock() {
		purchaseList = purchaseOrderDAO.searchByStatus("창고도착");
		waitingUnloadList = new ArrayList<>();

		List<StockProduct> stockList = new ArrayList<>();
		int totalQuantity = 0;

		for (PurchaseOrder po : purchaseList) {
			for (int j = 0; j < po.getQuantity(); j++) {
				StockProduct stock = new StockProduct();
				stock.setProductId(po.getProduct().getProduct_id());
				stock.setProductBrand(po.getProduct().getBrand_name());
				stock.setProductName(po.getProduct().getName());
				stock.setDetail(po.getProduct().getDetail());
				stockList.add(stock);
			}
		}

		totalQuantity = stockList.size();

		// 위치 할당: 상품 수 만큼 랙 좌표 받아오기
		List<String> positions = calcPosition(stockList);
		if (positions.size() == 1 && positions.get(0).equals("-1")) {
			JOptionPane.showMessageDialog(null, "랙 공간이 부족합니다. 관리자에게 문의하세요.");
			return 0;
		}

		for (int i = 0; i < totalQuantity; i++) {
			String[] pos = positions.get(i).split("-");
			StockProduct stock = stockList.get(i);
			stock.setS(Integer.parseInt(pos[0]));
			stock.setZ(Integer.parseInt(pos[1]));
			stock.setX(Integer.parseInt(pos[2]));
			stock.setY(Integer.parseInt(pos[3]));
		}
		waitingUnloadList.addAll(stockList);

		return totalQuantity;
	}

	public List<String> calcPosition(List<StockProduct> waitingUnloadList) {
		List<Rack> emptyRacks = rackDAO.selectByRackStatus(0);
		int needRack = waitingUnloadList.size();

		if (emptyRacks.size() < needRack)
			return List.of("-1");

		// 모든 랙 위치 문자열: S-Z-X-Y
		List<String> allPositions = emptyRacks.stream()
				.map(r -> r.getS() + "-" + r.getZ() + "-" + r.getX() + "-" + r.getY()).collect(Collectors.toList());

		// 정렬 기준: S-Z-X-Y
		allPositions.sort(Comparator.comparing((String pos) -> Integer.parseInt(pos.split("-")[1])) // Z 먼저
				.thenComparing(pos -> Integer.parseInt(pos.split("-")[0])) // S 교차
				.thenComparing(pos -> Integer.parseInt(pos.split("-")[2])) // X
				.thenComparing(pos -> Integer.parseInt(pos.split("-")[3]))); // Y

		// 가능한 Z 값 추출
		Set<String> zSet = emptyRacks.stream().map(r -> String.valueOf(r.getZ()))
				.collect(Collectors.toCollection(LinkedHashSet::new));
		List<String> zList = new ArrayList<>(zSet);

		int zIndex = 0;
		int sToggle = 0; // 0 -> S=1, 1 -> S=2

		List<String> assigned = new ArrayList<>();

		while (assigned.size() < needRack && !allPositions.isEmpty()) {
			String currentZ = zList.get(zIndex);
			String currentS = sToggle == 0 ? "1" : "2";

			Optional<String> match = allPositions.stream().filter(pos -> {
				String[] parts = pos.split("-");
				return parts[0].equals(currentS) && parts[1].equals(currentZ);
			}).findFirst();

			if (match.isPresent()) {
				assigned.add(match.get());
				allPositions.remove(match.get());
			}

			// 다음: S toggle → 다음 Z
			sToggle = (sToggle + 1) % 2;
			if (sToggle == 0) {
				zIndex = (zIndex + 1) % zList.size();
			}
		}

		if (assigned.size() < needRack) {
			return List.of("-1");
		}

		return assigned;
	}

}
