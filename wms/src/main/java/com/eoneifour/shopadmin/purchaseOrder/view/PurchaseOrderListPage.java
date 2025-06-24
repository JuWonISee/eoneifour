package com.eoneifour.shopadmin.purchaseOrder.view;

import java.awt.BorderLayout;
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
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.Refreshable;
import com.eoneifour.common.util.TableUtil;
import com.eoneifour.shopadmin.purchaseOrder.model.PurchaseOrder;
import com.eoneifour.shopadmin.purchaseOrder.repository.PurchaseOrderDAO;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;

public class PurchaseOrderListPage extends AbstractTablePage implements Refreshable {
	private ShopAdminMainFrame mainFrame;
	private int purchaseId = 0; // purchase 상세 보기를 위해 purchase Id를 담기 위한 변수
	private PurchaseOrderDetailPage purchaseOrderDetailPage;

	private PurchaseOrderDAO purchaseOrderDAO;
	private List<PurchaseOrder> purchaseOrderList;
	private String[] cols = { "발주번호", "상품명", "요청수량", "요청일자", "요청자", "처리상태 ", "처리일자", "재고반영여부"};

	public PurchaseOrderListPage(ShopAdminMainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.purchaseOrderDetailPage = mainFrame.purchaseOrderDetailPage;
		this.purchaseOrderDAO = new PurchaseOrderDAO();

		initTopPanel();
		initTable();
		applyTableStyle();

	}

	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		// 패널 안쪽 여백 설정 (시계반대방향)
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));
		// 제목 라벨
		JLabel title = new JLabel("발주 목록");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		topPanel.add(title, BorderLayout.WEST);

		// 재고 최신화 버튼
		JButton reflectBtn = ButtonUtil.createPrimaryButton("재고 최신화", 14, 120, 40);
		reflectBtn.addActionListener(e -> {
			reflectAll();
		});
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		rightPanel.setOpaque(false);
		rightPanel.add(reflectBtn);
		topPanel.add(rightPanel, BorderLayout.EAST);
		
		add(topPanel, BorderLayout.NORTH);
	}

	// 테이블 초기화 및 클릭 이벤트 연결
	public void initTable() {
		purchaseOrderList = purchaseOrderDAO.getPurchaseList();

		model = new DefaultTableModel(toTableData(purchaseOrderList), cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.setRowHeight(36); // cell 높이 설정

		// 상세 , 수정 , 삭제 이벤트 연결
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());

				int purchaseId = (int) model.getValueAt(row, 0);

				mainFrame.purchaseOrderDetailPage.setPurchase(purchaseId);
				mainFrame.showContent("PURCHASE_DETAIL");

			}
		});
	}

	// 테이블 데이터 새로고침
	public void refresh() {
		purchaseOrderList = purchaseOrderDAO.getPurchaseList();
		model.setDataVector(toTableData(purchaseOrderList), cols);

		TableUtil.applyDefaultTableStyle(table);
	}

	// 테이블용 데이터로 변환
	private Object[][] toTableData(List<PurchaseOrder> purchaseOrderList) {
		Object[][] data = new Object[purchaseOrderList.size()][cols.length];

		for (int i = 0; i < purchaseOrderList.size(); i++) {
			PurchaseOrder purchaseOrder = purchaseOrderList.get(i);
			data[i] = new Object[] { purchaseOrder.getPurchase_order_id(), purchaseOrder.getProduct().getName(),
					purchaseOrder.getQuantity(), purchaseOrder.getRequest_date(), purchaseOrder.getUser().getName(),
					purchaseOrder.getStatus(), purchaseOrder.getComplete_date(),
					(purchaseOrder.getReflect()) == 0 ? "미반영" : "반영됨"};
		}

		return data;
	}
	
	public void reflectAll() {
	    try {
	        purchaseOrderDAO.reflectAll();
	        JOptionPane.showMessageDialog(this, "재고가 반영되었습니다.");
	        refresh(); // 테이블 갱신
	    } catch (UserException e) {
	        JOptionPane.showMessageDialog(this, "재고 반영 중 오류 발생: " + e.getMessage());
	    }
	}
	
	
}
