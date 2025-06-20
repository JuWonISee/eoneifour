package com.eoneifour.shopadmin.product.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.TableUtil;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.product.repository.ProductDAO;
import com.eoneifour.shopadmin.view.frame.ShopAdminMainFrame;

public class ProductListPage extends AbstractTablePage {
	private ShopAdminMainFrame mainFrame;
	int productId =0; // product 상세 보기를 위해 product ID 를 담기 위한 변수

	public ProductListPage(ShopAdminMainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		initTopPanel();
		initTable();
		applyTableStyle();
	}

	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		// 패널 안쪽 여백 설정 (시계반대방향)
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));
		// 제목 라벨
		JLabel title = new JLabel("상품 목록");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		topPanel.add(title, BorderLayout.WEST);
		// 등록 버튼
		JButton addBtn = ButtonUtil.createPrimaryButton("상품 등록", 14, 120, 40);
		addBtn.addActionListener(e -> mainFrame.showContent("PRODUCT_REGIST"));
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		rightPanel.setOpaque(false);
		rightPanel.add(addBtn);
		topPanel.add(rightPanel, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);
	}

	public void initTable() {
		ProductDAO productDAO = new ProductDAO();
		List<Product> productList = productDAO.getProductList();
		// 테이블 헤더
		String[] cols = { "상품번호", "카테고리", "브랜드", "상품명", "가격", "재고수량", "품절상태", "수정", "삭제" };
		// 테이블 바디 샘플 데이터
		Object[][] data = new Object[productList.size()][9]; // 컬럼 수에 맞춰 배열 크기 설정

		for (int i = 0; i < productList.size(); i++) {
			Product product = productList.get(i);
			data[i][0] = product.getProduct_id();
			data[i][1] = product.getSub_category().getTop_category().getName();
			data[i][2] = product.getBrand_name();
			data[i][3] = product.getName();
			data[i][4] = product.getPrice();
			data[i][5] = product.getStock_quantity();

			if (product.getStock_quantity() == 0) {
				data[i][6] = "품절";
			} else {
				data[i][6] = "판매중";
			}

			data[i][7] = "수정";
			data[i][8] = "삭제";
		}

		model = new DefaultTableModel(data, cols) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);

		// 리팩토링된 Table 추상클래스 메서드명으로 수정. @JH
		TableUtil.applyColorTextRenderer(table, "품절상태", new Color(25, 118, 210));
		TableUtil.applyColorTextRenderer(table, "수정", new Color(25, 118, 210));
		TableUtil.applyColorTextRenderer(table, "삭제", new Color(211, 47, 47));

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());
				if (col == table.getColumn("수정").getModelIndex()) {
					System.out.println("수정 클릭: " + row);
				} else if (col == table.getColumn("삭제").getModelIndex()) {
					System.out.println("삭제 클릭: " + row);
				} else if(col == table.getColumn("상품번호").getModelIndex()) {
				    System.out.println("상세 클릭" + row);
				    productId = (Integer) table.getValueAt(row, 0); 
//				    mainFrame.productDetailpage.setProduct(productId);
//				    mainFrame.productDetailpage.selectedId = productId;
				    mainFrame.productDetailPage.setProduct(productId);
				    mainFrame.productDetailPage.selectedId = productId;
				    mainFrame.showContent("PRODUCT_DETAIL");             
				}
			}  
		});
	}

	//
	public void refresh() {

	}
}
