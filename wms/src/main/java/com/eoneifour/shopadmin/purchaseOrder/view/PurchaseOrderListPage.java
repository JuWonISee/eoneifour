package com.eoneifour.shopadmin.purchaseOrder.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.Refreshable;
import com.eoneifour.common.util.TableUtil;
import com.eoneifour.shopadmin.purchaseOrder.model.PurchaseOrder;
import com.eoneifour.shopadmin.purchaseOrder.repository.PurchaseOrderDAO;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;

public class PurchaseOrderListPage extends AbstractTablePage implements Refreshable {
	private ShopAdminMainFrame mainFrame;
	private PurchaseOrderDetailPage purchaseOrderDetailPage;
	
	private JTextField searchField;

	private PurchaseOrderDAO purchaseOrderDAO;
	private List<PurchaseOrder> purchaseOrderList;
	private String[] cols = { "발주번호", "상품명", "요청수량", "요청일자", "요청자", "처리상태 ", "처리일자"};

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
		
		//검색 영역
		searchField = new JTextField("발주번호 또는 상품명을 입력하세요");
		searchField.setForeground(Color.GRAY);
		searchField.setPreferredSize(new Dimension(250, 30));
		JButton searchBtn = ButtonUtil.createPrimaryButton("검색", 15, 100, 30);
		searchBtn.setBorderPainted(false);
		
		placeholder();
		
		//검색 기능
		
		searchBtn.addActionListener(e->{
			String keyword = searchField.getText().trim();
			List<PurchaseOrder> searchResults;
			
			if (!keyword.isEmpty() || keyword == "회원명 또는 이메일을 입력하세요") {
				//searchResults = productDAO.serchByKeyword(keyword);
				searchField.setText(null);
				placeholder();
			} else {
				// keyword가 비어있을 경우 전체 목록 다시 조회
				//searchResults = productDAO.getProductList();
				searchField.setText(null);
				placeholder();
			}

//			if (searchResults.isEmpty()) {
//				JOptionPane.showMessageDialog(null, "해당 제품이 없습니다.", "Info", JOptionPane.INFORMATION_MESSAGE);
//				//searchResults = productDAO.getProductList();
//				searchField.setText(null);
//				placeholder();
//				searchField.setForeground(Color.BLACK);
//			}
		});

		// 검색 영역 엔터 이벤트 (검색버튼 클릭과 동일한 효과)
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
					purchaseOrder.getStatus(), purchaseOrder.getComplete_date()};
		}

		return data;
	}
	
	//검색 TextField에 placeholder 효과 주기 (forcus 이벤트 활용)
	public void placeholder() {
		searchField.addFocusListener(new FocusAdapter() {
		    public void focusGained(FocusEvent e) {
		        if (searchField.getText().equals("발주번호 또는 상품명을 입력하세요")) {
		            searchField.setText("");
		            searchField.setForeground(Color.BLACK);
		        }
		    }

		    public void focusLost(FocusEvent e) {
		        if (searchField.getText().isEmpty()) {
		            searchField.setForeground(Color.GRAY);
		            searchField.setText("발주번호 또는 상품명을 입력하세요");
		        }
		    }
		});
	}
	
}
