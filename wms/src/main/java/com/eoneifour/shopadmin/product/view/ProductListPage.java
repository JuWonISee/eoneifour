package com.eoneifour.shopadmin.product.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.common.util.Refreshable;
import com.eoneifour.common.util.SessionUtil;
import com.eoneifour.common.util.TableUtil;
import com.eoneifour.shopadmin.order.repository.OrderDAO;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.product.repository.ProductDAO;
import com.eoneifour.shopadmin.purchaseOrder.repository.PurchaseOrderDAO;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;

public class ProductListPage extends AbstractTablePage implements Refreshable {
	private ShopAdminMainFrame mainFrame;
	private int productId = 0; // product 상세 보기를 위해 product ID 를 담기 위한 변수
	private int userId = 0; //purchase_order 할 때 , 주문자의 userId를 담기 위한 변수
	private ProductRegistPage productRegistPage;
	private ProductDetailPage productDetailPage;
	private ProductUpdatePage productUpdatePage;
	private PurchaseModalDialog p_dialog;
	private StatusModalDialog s_dialog;
	private JTextField searchField;

	private ProductDAO productDAO;
	private PurchaseOrderDAO purchaseOrderDAO;
	private OrderDAO orderDAO;
	private List<Product> productList;
	private String[] cols = { "상품번호", "카테고리", "브랜드", "상품명", "가격", "재고수량", "품절상태", "발주요청", "수정" , "상태변경"};

	public ProductListPage(ShopAdminMainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.productRegistPage = mainFrame.productRegistPage;
		this.productDetailPage = mainFrame.productDetailPage;
		this.productUpdatePage = mainFrame.productUpdatePage;
		this.productDAO = new ProductDAO();
		this.purchaseOrderDAO = new PurchaseOrderDAO();
		this.orderDAO = new OrderDAO();
		initTopPanel();
		initTable();
		applyTableStyle();
	}

	// 사상단 패널 UI 구성 (제목 + 등록 버튼)
	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		// 패널 안쪽 여백 설s정 (시계반대방향)
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));
		// 제목 라벨
		JLabel title = new JLabel("상품 목록");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		topPanel.add(title, BorderLayout.WEST);
		
		//검색 영역
		searchField = new JTextField("카테고리명 또는 상품명을 입력하세요");
		searchField.setForeground(Color.GRAY);
		searchField.setPreferredSize(new Dimension(250, 30));
		JButton searchBtn = ButtonUtil.createDefaultButton("검색", 14, 100, 30);
		
		placeholder();
		
		//검색 기능
		searchBtn.addActionListener(e ->{
			String keyword = searchField.getText().trim();
			List<Product> searchResults;
			
			if (!keyword.isEmpty() || keyword == "카테고리명 또는 상품명을 입력하세요") {
				searchResults = productDAO.searchByKeyword(keyword);
				searchField.setText(null);
				placeholder();
			} else {
				// keyword가 비어있을 경우 전체 목록 다시 조회
				searchResults = productDAO.getProductList();
				searchField.setText(null);
				placeholder();
			}

			if (searchResults.isEmpty()) {
				new NoticeAlert(mainFrame, "해당 제품이 없습니다.", "Info").setVisible(true);
				searchResults = productDAO.getProductList();
				searchField.setText(null);
				placeholder();
				searchField.setForeground(Color.BLACK);
			}
			
			model.setDataVector(toTableData(searchResults), cols);
			applyStyle();
		});


		// 검색 영역 엔터 이벤트 (검색버튼 클릭과 동일한 효과)
		searchField.addActionListener(e -> {
			searchBtn.doClick(); //
		});
		
		// 등록 버튼
		JButton registBtn = ButtonUtil.createPrimaryButton("상품 등록", 14, 120, 40);
		registBtn.addActionListener(e -> {
			productRegistPage.prepare();
			mainFrame.showContent("PRODUCT_REGIST");
		});
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		rightPanel.setOpaque(false);
		rightPanel.add(searchField);
		rightPanel.add(searchBtn);
		rightPanel.add(Box.createHorizontalStrut(40));
		rightPanel.add(registBtn);
		topPanel.add(rightPanel, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);
	}

	// 테이블 초기화 및 클릭 이벤트 연결
	public void initTable() {
		productList = productDAO.getProductList();

		model = new DefaultTableModel(toTableData(productList), cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);

		// 테이블 컬럼 스타일 적용 (재고수량0개, 품절, 비활성 : 빨강 / 발주요청 : 오렌지 / 수정 : 파랑)
		TableUtil.applyConditionalTextRenderer(table, "재고수량", "0 개", Color.RED);
		TableUtil.applyConditionalTextRenderer(table, "품절상태", "품절", Color.RED);
		TableUtil.applyConditionalTextRenderer(table, "상태변경", "비활성", Color.RED);
		TableUtil.applyColorTextRenderer(table, "발주요청", Color.ORANGE);
		TableUtil.applyColorTextRenderer(table, "수정", new Color(25, 118, 210));

		// 발주요청, 상세 , 수정 , 활성화 이벤트 연결
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());

				int productId = (int) model.getValueAt(row, 0);

				if (col == table.getColumn("발주요청").getModelIndex()) {
					// 발주 로직
					purchaseOrder(productId);
				} else if (col == table.getColumn("수정").getModelIndex()) {
					// 상품 수정 로직
					mainFrame.productUpdatePage.setProduct(productId);
					mainFrame.showContent("PRODUCT_UPDATE");
				} else if (col == table.getColumn("상태변경").getModelIndex()) {
					// 상품 상태변경 로직 (삭제 시 , db delete가 아닌 , product 의 status 를 전환 (0 : 활성 , 1 : 비활성)
					if(orderDAO.hasUndeliveredOrders(productId)) { // 주문상품에 존재하고 , 배송완료가 아니면 
						new NoticeAlert(mainFrame, "주문진행중인 상품이 존재합니다", "상태 전환 실패").setVisible(true);
					}else {
						switchProductStatus(productId);
					}
					
				} else {
					// 상품 상세 로직
					mainFrame.productDetailPage.setProduct(productId);
					mainFrame.showContent("PRODUCT_DETAIL");
				}
			}
		});

		// 마우스가 발주/수정/삭제 셀 위에 있을 때 손 모양 커서로 변경

		table.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());

				String columnName = table.getColumnName(col);
				if ("발주요청".equals(columnName) || "상태변경".equals(columnName) || "수정".equals(columnName)) {
					table.setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else {
					table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
	}

	// 테이블 데이터 새로고침
	public void refresh() {
		productList = productDAO.getProductList();
		model.setDataVector(toTableData(productList), cols);

		applyStyle();
	}
	
	public void applyStyle() {
		TableUtil.applyDefaultTableStyle(table);
		TableUtil.applyConditionalTextRenderer(table, "재고수량", "0 개", Color.RED);
		TableUtil.applyConditionalTextRenderer(table, "품절상태", "품절", Color.RED);
		TableUtil.applyConditionalTextRenderer(table, "상태변경", "비활성", Color.RED);
		TableUtil.applyColorTextRenderer(table, "발주요청", Color.ORANGE);
		TableUtil.applyColorTextRenderer(table, "수정", new Color(25, 118, 210));
	}

	// 테이블용 데이터로 변환
	private Object[][] toTableData(List<Product> productList) {
		Object[][] data = new Object[productList.size()][cols.length];

		for (int i = 0; i < productList.size(); i++) {
			Product product = productList.get(i);

			data[i] = new Object[] { product.getProduct_id(), product.getSub_category().getTop_category().getName(),
					product.getBrand_name(), product.getName(), FieldUtil.commaFormat(product.getPrice())+" 원", FieldUtil.commaFormat(product.getStock_quantity())+" 개",
					(product.getStock_quantity()) == 0 ? "품절" : "판매중",  
					"발주요청", "수정",(product.getStatus() == 0) ? "활성" : "비활성" };
		}

		return data;
	}

	// 상품 삭제 로직
	public void switchProductStatus(int ProductId) {
		s_dialog = new StatusModalDialog(mainFrame);
		s_dialog.setVisible(true);
		if (s_dialog.isConfirmed()) {
			try {
				productDAO.switchProductStatus(ProductId);
				new NoticeAlert(mainFrame, "상품 상태가 변경되었습니다", "요청 성공").setVisible(true);
				refresh();
			} catch (UserException e) {
				JOptionPane.showMessageDialog(this, "상품 상태 변경 중 오류 발생: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	// 발주 처리 로직
	private void purchaseOrder(int productId) {
		Product product = productDAO.getProduct(productId);
		User loginUser = SessionUtil.getLoginUser();
		userId = loginUser.getUserId();
		p_dialog = new PurchaseModalDialog(mainFrame, product);
		p_dialog.setVisible(true);

		if (p_dialog.isConfirmed()) {
			int quantity = p_dialog.getQuantity();
	        try {
	            purchaseOrderDAO.insertPurchase(productId, quantity, userId );
	            new NoticeAlert(mainFrame, "발주가 요청되었습니다. 수량: " + quantity, "요청 성공").setVisible(true);
	            refresh();
	        } catch (UserException e) {
	            JOptionPane.showMessageDialog(this, "발주 요청 중 오류 발생: " + e.getMessage());
	        }
	    }

	}
	//검색 TextField에 placeholder 효과 주기 (forcus 이벤트 활용)
	public void placeholder() {
		searchField.addFocusListener(new FocusAdapter() {
		    public void focusGained(FocusEvent e) {
		        if (searchField.getText().equals("카테고리명 또는 상품명을 입력하세요")) {
		            searchField.setText("");
		            searchField.setForeground(Color.BLACK);
		        }
		    }

		    public void focusLost(FocusEvent e) {
		        if (searchField.getText().isEmpty()) {
		            searchField.setForeground(Color.GRAY);
		            searchField.setText("카테고리명 또는 상품명을 입력하세요");
		        }
		    }
		});
	}
}
