package com.eoneifour.shopadmin.product.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.shopadmin.common.util.DBManager;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.product.model.ProductImg;
import com.eoneifour.shopadmin.product.model.SubCategory;
import com.eoneifour.shopadmin.product.model.TopCategory;
import com.eoneifour.shopadmin.product.repository.ProductDAO;
import com.eoneifour.shopadmin.product.repository.ProductImgDAO;
import com.eoneifour.shopadmin.product.repository.SubCategoryDAO;
import com.eoneifour.shopadmin.product.repository.TopCategoryDAO;
import com.eoneifour.shopadmin.view.frame.ShopAdminMainFrame;

public class ProductDetailPage extends JPanel {
	JComboBox cb_topcategory;
	JComboBox cb_subcategory;
	TopCategoryDAO topCategoryDAO;
	SubCategoryDAO subCategoryDAO;
	JTextField t_brand;
	JTextField t_productName;
	JTextField t_price;
	JTextField t_detail;
	JTextField t_stockQuantity;
	JFileChooser chooser;
	File[] files;
	JTextField imageField;
	JButton editBtn;
	DBManager dbManager = DBManager.getInstance();
	ProductDAO productDAO = new ProductDAO();
	ProductImgDAO productImgDAO = new ProductImgDAO();
	int selectedId;
	ShopAdminMainFrame mainFrame;
	
	
	public ProductDetailPage(ShopAdminMainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(new Color(245, 247, 250));

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBackground(Color.WHITE);
		formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
		formPanel.setMaximumSize(new Dimension(500, 610));
		formPanel.setMinimumSize(new Dimension(500, 610));
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel title = new JLabel("상품 상세");
		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(title);
		formPanel.add(Box.createVerticalStrut(20));

		// 상위 카테고리 콤보박스
		cb_topcategory = new JComboBox();
		formPanel.add(FieldUtil.createComboField("상위 카테고리", cb_topcategory));
		formPanel.add(Box.createVerticalStrut(18));

		// 하위 카테고리 콤보박스
		cb_subcategory = new JComboBox();
		formPanel.add(FieldUtil.createComboField("하위 카테고리", cb_subcategory));
		formPanel.add(Box.createVerticalStrut(18));

		// 상위 카테고리 불러오기
		topCategoryDAO = new TopCategoryDAO();
		subCategoryDAO = new SubCategoryDAO();
		getTopCategory();

		// 상위 카테고리 콤보박스에 이벤트 연결 및 하위 카테고리 불러오기
		cb_topcategory.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// 내가 선택한 아이템의 pk를 출력해 보기. 신발 = 3
					TopCategory topCategory = (TopCategory) cb_topcategory.getSelectedItem();
					getSubCategory(topCategory);
				}
			}
		});

		// 기본 필드
		formPanel.add(FieldUtil.createField("브랜드", t_brand = new JTextField(16)));
		formPanel.add(Box.createVerticalStrut(12));
		formPanel.add(FieldUtil.createField("상품명", t_productName = new JTextField(16)));
		formPanel.add(Box.createVerticalStrut(12));
		formPanel.add(FieldUtil.createField("가격", t_price = new JTextField(16)));
		formPanel.add(Box.createVerticalStrut(12));
		formPanel.add(FieldUtil.createField("상품설명", t_detail = new JTextField(16)));
		formPanel.add(Box.createVerticalStrut(12));
		formPanel.add(FieldUtil.createField("수량", t_stockQuantity = new JTextField(16)));
		formPanel.add(Box.createVerticalStrut(18));

		// 이미지 필드 + 이미지 업로드 버튼 (imageField는 편집 불가)
		imageField = new JTextField(16);
		imageField.setEditable(false);
		JButton imgBtn = ButtonUtil.createDefaultButton("업로드", 13, 90, 36);
		formPanel.add(FieldUtil.createFieldWithButton("이미지", imageField, imgBtn));
		formPanel.add(Box.createVerticalStrut(18));

		// 이미지 업로드 버튼 이벤트 연결 및 함수 호출
		chooser = new JFileChooser("C:/");
		chooser.setMultiSelectionEnabled(true); // 다중 선택 가능하도록 설정
		imgBtn.addActionListener(e -> {
			selectImg();
		});

		// 등록 , 목록으로 돌아가기 버튼 생성
		editBtn = ButtonUtil.createPrimaryButton("수정", 15, 120, 40);
		JButton listBtn = ButtonUtil.createDefaultButton("목록", 15, 120, 40);
		// 버튼 이벤트 연결
		editBtn.addActionListener(e -> {
			edit();
			mainFrame.productListPage.refresh(); // refresh 기능 구현 추가 필요
		});

		listBtn.addActionListener(e -> {
			mainFrame.showContent("PRODUCT_LIST");
		});
		// 버튼 패널
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.add(editBtn);
		buttonPanel.add(listBtn);
		buttonPanel.setMaximumSize(new Dimension(300, 50));
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(buttonPanel);

		add(Box.createVerticalGlue());
		add(formPanel);
		add(Box.createVerticalGlue());

	}

	public void getTopCategory() {
		List<TopCategory> topList = topCategoryDAO.selectAll();

		for (int i = 0; i < topList.size(); i++) {
			TopCategory topcategory = topList.get(i);
			cb_topcategory.addItem(topcategory);
		}
		
	}

	public void getSubCategory(TopCategory topCategory) {

		List<SubCategory> subList = subCategoryDAO.selectByTop(topCategory);

		cb_subcategory.removeAllItems();

		// 서브 카테고리 수만큼 반복하면서 , 두번째 콤보박스에 SubCategory 모델을 채워넣기.
		for (int i = 0; i < subList.size(); i++) {
			SubCategory subCategory = subList.get(i); // i번째 요소 꺼내기
			cb_subcategory.addItem(subCategory);
		}
	}

	public void selectImg() {
		chooser.showOpenDialog(ProductDetailPage.this);
		files = chooser.getSelectedFiles();
		if (files.length > 0) {
			String fileNames = Arrays.stream(files).map(File::getName).collect(Collectors.joining(", "));
			imageField.setText(fileNames);
		}
	}



	// 지정한 상품에 대한 정보 출력
	public void setProduct(int productId) {
		Product product = productDAO.getProduct(productId);
		List<ProductImg> imgList = productImgDAO.getProductImgs(productId);

		// 카테고리 콤보박스에 들어가있는 것은 객체이므로
		// 콤보박스에 들어간 item의 index와 topcategory의 id가 동일 한 것을 선택

		TopCategory selectedTopCategory = product.getSub_category().getTop_category();

		int topIndex = -1;
		for (int i = 0; i < cb_topcategory.getItemCount(); i++) {
			TopCategory item = (TopCategory) cb_topcategory.getItemAt(i);
			if (item != null && item.getTop_category_id() == selectedTopCategory.getTop_category_id()) {
				topIndex = i;
				break;
			}
		}

		if (topIndex != -1)
			cb_topcategory.setSelectedIndex(topIndex);

		getSubCategory(selectedTopCategory);

		SubCategory selectedSubCategory = product.getSub_category();
	    int subIndex = -1;
	    for (int i = 0; i < cb_subcategory.getItemCount(); i++) {
	        SubCategory item = (SubCategory) cb_subcategory.getItemAt(i);
	        if (item != null && item.getSub_category_id() == selectedSubCategory.getSub_category_id()) {
	            subIndex = i;
	            break;
	        }
	    }

	    if (subIndex != -1) cb_subcategory.setSelectedIndex(subIndex);

		t_brand.setText(product.getBrand_name());
		t_productName.setText(product.getName());
		t_price.setText(Integer.toString(product.getPrice()));
		t_detail.setText(product.getDetail());
		t_stockQuantity.setText(Integer.toString(product.getStock_quantity()));
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < imgList.size(); i++) {
		    sb.append(imgList.get(i).getFilename());
		    if (i < imgList.size() - 1) {
		        sb.append(", "); // 마지막엔 쉼표 안 붙이기
		    }
		}
		
		imageField.setText(sb.toString());
	}

	public void edit() {
		// 가격에 숫자(정수)를 입력하였는지 검사 , 유효성에서 정수가 아니면 반환
		String price = t_price.getText();
		boolean b_price = price.matches("\\d+");

		// 재고에 숫자(정수)를 입력하였는지 검사 , 유효성에서 정수가 아니면 반환
		String quantity = t_stockQuantity.getText();
		boolean b_quantity = quantity.matches("\\d+");
		
        Product product = new Product();
        product.setProduct_id(selectedId); // ← 중요: 수정 대상 ID 설정
        product.setBrand_name(t_brand.getText());
        product.setName(t_productName.getText());
        product.setPrice(Integer.parseInt(t_price.getText()));
        product.setDetail(t_detail.getText());
        product.setStock_quantity(Integer.parseInt(t_stockQuantity .getText()));
        product.setSub_category((SubCategory) cb_subcategory.getSelectedItem());
	}
	
	
}

