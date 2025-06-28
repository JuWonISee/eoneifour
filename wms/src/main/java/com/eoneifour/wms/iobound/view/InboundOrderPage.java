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
import com.eoneifour.shopadmin.purchaseOrder.model.PurchaseOrder;
import com.eoneifour.shopadmin.purchaseOrder.repository.PurchaseOrderDAO;
import com.eoneifour.wms.home.view.MainFrame;
import com.eoneifour.wms.inboundrate.model.Rack;
import com.eoneifour.wms.inboundrate.repository.RackDAO;
import com.eoneifour.wms.iobound.model.selectAll;
import com.eoneifour.wms.iobound.repository.InBoundOrderDAO;

public class InboundOrderPage extends AbstractTablePage implements Refreshable {
	private MainFrame mainFrame;

	// 창고도착 건수
	private int arrviedOrderCnt;
	private JLabel unloading;

	// 발주데이터를 창고 데이터로 파싱하기 위함
	private List<PurchaseOrder> purchaseList;
	public List<selectAll> waitingUnloadList;

	// 입고대기중인 리스트
	private List<selectAll> stockOrderWaitList;

	// 발주되어있는 리스트.

	private PurchaseOrderDAO purchaseOrderDAO;
	private InBoundOrderDAO inBoundOrderDAO;
	private RackDAO rackDAO;

	private String[] cols = { "ID", "상품명", "입고위치", "작업" };

	private JTextField searchField;
	private JLabel keywordLabel;
	public InboundOrderPage(MainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.inBoundOrderDAO = new InBoundOrderDAO();
		this.purchaseOrderDAO = new PurchaseOrderDAO();
		this.rackDAO = new RackDAO();
		keywordLabel = new JLabel("상품명");

		initTopPanel();
		initTable();
		applyTableStyle();
	}

	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 0, 30)); //  여백

		// ▶ 서쪽(왼쪽): 제목 + 일괄 입고 버튼
		JLabel title = new JLabel("입고 대기 물품");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		title.setPreferredSize(new Dimension(200, 30));

		JButton inboundAllBtn = new JButton("일괄 입고");
		inboundAllBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		inboundAllBtn.setBorderPainted(false);

		JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		westPanel.setOpaque(false);
		westPanel.add(title);
		westPanel.add(inboundAllBtn);

		// 하차 대기 수 + 하차 버튼 + 검색창
		unloading = new JLabel("하차 대기 : 0건");
		unloading.setFont(new Font("맑은 고딕", Font.BOLD, 15));

		JButton unloadingBtn = ButtonUtil.createPrimaryButton("상품 하차", 20, 135, 30);
		unloadingBtn.setBorderPainted(false);

		keywordLabel = new JLabel("상품명");
		keywordLabel.setPreferredSize(new Dimension(60, 30));
		keywordLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

		searchField = new JTextField("상품명을 입력하세요");
		searchField.setPreferredSize(new Dimension(200, 30));
		searchField.setForeground(Color.GRAY);
		searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		applySearchPlaceholder();

		JButton searchBtn = ButtonUtil.createPrimaryButton("검색", 20, 100, 30);
		searchBtn.setBorderPainted(false);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		rightPanel.setOpaque(false);
		rightPanel.add(unloading);
		rightPanel.add(Box.createHorizontalStrut(10));
		rightPanel.add(unloadingBtn);
		rightPanel.add(Box.createHorizontalStrut(15));
		rightPanel.add(keywordLabel);
		rightPanel.add(searchField);
		rightPanel.add(searchBtn);

		// ▶ 상단 부착
		topPanel.add(westPanel, BorderLayout.WEST);
		topPanel.add(rightPanel, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);

		// ▶ 이벤트 처리
		inboundAllBtn.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(null, "모든 상품을 입고 처리하시겠습니까?", "일괄 입고 확인",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				for (int i = 0; i < model.getRowCount(); i++) {
					int stockId = (int) model.getValueAt(i, 0);
					String posStr = (String) model.getValueAt(i, 2); // "s-z-x-y"
					int[] pos = Arrays.stream(posStr.split("-")).mapToInt(Integer::parseInt).toArray();
					inBoundOrderDAO.updateStatusWithPosition(stockId, 1, pos[0], pos[1], pos[2], pos[3]);
					rackDAO.updateRackStatus(pos[0], pos[1], pos[2], pos[3], 1);
				}
				JOptionPane.showMessageDialog(null, "일괄 입고가 완료되었습니다.");
				refresh();
			}
		});

		unloadingBtn.addActionListener(e -> {
			inBoundOrderDAO.insertByList(waitingUnloadList);
			for (PurchaseOrder po : purchaseList) {
				purchaseOrderDAO.updateStatus(po.getPurchase_order_id(), "입고완료");
			}
			refresh();
		});

		searchBtn.addActionListener(e -> {
			String keyword = searchField.getText().trim();
			if (keyword.isEmpty() || keyword.equals("상품명을 입력하세요")) {
				stockOrderWaitList = inBoundOrderDAO.selectByStatus(0);
			} else {
				stockOrderWaitList = inBoundOrderDAO.searchByProductName(keyword, 0);
			}
			searchField.setText(null);
			model.setDataVector(toTableData(stockOrderWaitList), cols);
			applyStyle();
			JOptionPane.showMessageDialog(this,
				"총 " + stockOrderWaitList.size() + "건의 검색 결과가 있습니다.",
				"검색 완료", JOptionPane.INFORMATION_MESSAGE);
		});
		searchField.addActionListener(e -> searchBtn.doClick());
	}

	@Override
	public void initTable() {
		stockOrderWaitList = inBoundOrderDAO.selectByStatus(0);

		model = new DefaultTableModel(toTableData(stockOrderWaitList), cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.setRowHeight(36); // cell 높이 설정
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

	// 테이블 데이터로 변환
	private Object[][] toTableData(List<selectAll> stockProducts) {
		Object[][] data = new Object[stockProducts.size()][cols.length];

		for (int i = 0; i < stockProducts.size(); i++) {
			selectAll stock = stockProducts.get(i);
			String pos = stock.getS() + "-" + stock.getZ() + "-" + stock.getX() + "-" + stock.getY();
			data[i] = new Object[] { stock.getStockProductId(), stock.getProductName(), pos, "입고" };
		}

		return data;
	}

	// 새로고침시 적용할 스타일
	private void applyStyle() {
		TableUtil.applyDefaultTableStyle(table);
		TableUtil.applyColorTextRenderer(table, "작업", new Color(25, 118, 210));
		table.getColumn("ID").setMinWidth(0);
		table.getColumn("ID").setMaxWidth(0);
		table.getColumn("ID").setPreferredWidth(0);
	}

	// 테이블 데이터 새로고침
	public void refresh() {
		arrviedOrderCnt = purchaseToStock();
		unloading.setText("하차 대기 : " + arrviedOrderCnt + "건");

		stockOrderWaitList = inBoundOrderDAO.selectByStatus(0);
		model.setDataVector(toTableData(stockOrderWaitList), cols);
		applyStyle();

	}

	// 발주 목록을 stock_prdouct 형식에 맞추어 파싱
	public int purchaseToStock() {
		purchaseList = purchaseOrderDAO.searchByStatus("창고도착");
		waitingUnloadList = new ArrayList<>();

		List<selectAll> stockList = new ArrayList<>();
		int totalQuantity = 0;

		for (PurchaseOrder po : purchaseList) {
			for (int j = 0; j < po.getQuantity(); j++) {
				selectAll stock = new selectAll();
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
			selectAll stock = stockList.get(i);
			stock.setS(Integer.parseInt(pos[0]));
			stock.setZ(Integer.parseInt(pos[1]));
			stock.setX(Integer.parseInt(pos[2]));
			stock.setY(Integer.parseInt(pos[3]));
		}
		waitingUnloadList.addAll(stockList);

		return totalQuantity;
	}

	// 창고 입고 위치 계산 로직
	public List<String> calcPosition(List<selectAll> waitingUnloadList) {
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
