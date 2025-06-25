package com.eoneifour.shopadmin.product.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.product.model.ProductImg;
import com.eoneifour.shopadmin.product.repository.ProductDAO;
import com.eoneifour.shopadmin.product.repository.ProductImgDAO;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;

public class ProductDetailPage extends JPanel {
	private ShopAdminMainFrame mainFrame;
	
	private JPanel formPanel;
	private JTextField topcategoryField;
	private JTextField subcategoryField;
	private JTextField productNameField;
    private JTextField brandField;
    private JTextField priceField;
    private JTextField detailField;
    private JTextField stockQuantityField;
    private JTextField imageField;
    
    private JButton updateBtn;
    private JButton listBtn;
    
    private int productId;
	
	public ProductDetailPage(ShopAdminMainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 247, 250));
        
    	JPanel formPanel = initFormPanel();
        
        add(Box.createVerticalGlue());
    	add(formPanel);
    	add(Box.createVerticalGlue());
		
	}
	
	// 폼 전체 초기화
		private JPanel initFormPanel() {
			JPanel formPanel = new JPanel();
	        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
	        formPanel.setBackground(Color.WHITE);
	        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
	        formPanel.setMaximumSize(new Dimension(500, 610));
	        formPanel.setMinimumSize(new Dimension(500, 610));
	        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        
	        // 타이틀 생성
	        JLabel title = new JLabel("상품 상세");
	        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
	        title.setAlignmentX(Component.CENTER_ALIGNMENT);
	        formPanel.add(title);
	        formPanel.add(Box.createVerticalStrut(20));
	        
	        // 필드 생성
	        topcategoryField = new JTextField(16);
	        formPanel.add(FieldUtil.createField("상위카테고리", topcategoryField));
	        formPanel.add(Box.createVerticalStrut(12));
	        subcategoryField = new JTextField(16);
	        formPanel.add(FieldUtil.createField("하위카테고리", subcategoryField));
	        formPanel.add(Box.createVerticalStrut(12));
	        productNameField = new JTextField(16);
	        formPanel.add(FieldUtil.createField("상품명", productNameField));
	        formPanel.add(Box.createVerticalStrut(12));
	        brandField = new JTextField(16);
	        formPanel.add(FieldUtil.createField("브랜드", brandField));
	        formPanel.add(Box.createVerticalStrut(12));
	        priceField = new JTextField(16);
	        formPanel.add(FieldUtil.createField("가격", priceField));
	        formPanel.add(Box.createVerticalStrut(12));
	        detailField = new JTextField(16);
	        formPanel.add(FieldUtil.createField("상품 설명", detailField));
	        formPanel.add(Box.createVerticalStrut(12));
	        stockQuantityField = new JTextField(16);
	        formPanel.add(FieldUtil.createField("재고 수량", stockQuantityField));
	        formPanel.add(Box.createVerticalStrut(12));
	        imageField = new JTextField(16);
	        formPanel.add(FieldUtil.createField("이미지 파일", imageField));
	        formPanel.add(Box.createVerticalStrut(32));
	        
	        // 필드 비활성화
	        topcategoryField.setEditable(false);
	        subcategoryField.setEditable(false);
	        productNameField.setEditable(false);
	        brandField.setEditable(false);
	        priceField.setEditable(false);
	        detailField.setEditable(false);
	        stockQuantityField.setEditable(false);
	        imageField.setEditable(false);
	        
	        // 버튼 패널 붙이기
	        formPanel.add(createButtonPanel());
	        
	        return formPanel;
		}
		
		// 하단 버튼 패널 초기화
		private JPanel createButtonPanel() {
			// 버튼 생성
	        updateBtn = ButtonUtil.createPrimaryButton("수정", 15, 120, 40);
	        listBtn = ButtonUtil.createDefaultButton("목록", 15, 120, 40);
	        
	        // 수정 버튼 이벤트
	        updateBtn.addActionListener(e -> {
			    mainFrame.productUpdatePage.setProduct(productId);
			    mainFrame.showContent("PRODUCT_UPDATE"); 
	        });
	        // 목록 버튼 이벤트
	        listBtn.addActionListener(e->{
	    		mainFrame.showContent("PRODUCT_LIST");
	        });
	        
	        // 버튼 패널
	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
	        buttonPanel.setOpaque(false);
	        buttonPanel.add(updateBtn);
	        buttonPanel.add(listBtn);
	        buttonPanel.setMaximumSize(new Dimension(300, 50));
	        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    	
	    	return buttonPanel;
		}
		
		// 화면 진입 전에 호출 필요. userId로 상세 정보 설정
		public void setProduct(int productId) {
			this.productId = productId;
			loadProduct();
		}
		
		// productId로 사용자 정보 조회 후 필드에 표시
		private void loadProduct() {
			Product product = new ProductDAO().getProduct(productId);
			ProductImgDAO productImgDAO = new ProductImgDAO();
			List<ProductImg> imgList = productImgDAO.getProductImgs(productId);
			
			topcategoryField.setText(product.getSub_category().getTop_category().getName());
			subcategoryField.setText(product.getSub_category().getName());
			productNameField.setText(product.getName());
			brandField.setText(product.getBrand_name());
			priceField.setText(Integer.toString(product.getPrice()));
			detailField.setText(product.getDetail());
			stockQuantityField.setText(Integer.toString(product.getStock_quantity()));
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < imgList.size(); i++) {
				sb.append(imgList.get(i).getFilename());
				if (i < imgList.size() - 1) {
					sb.append(", "); // 마지막엔 쉼표 안 붙이기
				}
			}

			imageField.setText(sb.toString());
		}
}