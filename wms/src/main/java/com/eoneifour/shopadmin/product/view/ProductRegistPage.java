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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.product.model.ProductImg;
import com.eoneifour.shopadmin.product.model.SubCategory;
import com.eoneifour.shopadmin.product.model.TopCategory;
import com.eoneifour.shopadmin.product.repository.ProductDAO;
import com.eoneifour.shopadmin.product.repository.ProductImgDAO;
import com.eoneifour.shopadmin.product.repository.SubCategoryDAO;
import com.eoneifour.shopadmin.product.repository.TopCategoryDAO;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;

public class ProductRegistPage extends JPanel {
	private ShopAdminMainFrame mainFrame;
	
	private JComboBox cb_topcategory;
	private JComboBox cb_subcategory;

	private JTextField productNameField;
	private JTextField brandField;
	private JTextField priceField;
	private JTextField detailField;
	private JTextField stockQuantityField;
	
	private JFileChooser chooser;
	private File[] files;
	private JTextField imageField;
	
	private JButton registBtn;
	private JButton checkBtn;
	private JButton imgBtn;
	private JButton listBtn;
	
	
	private TopCategoryDAO topCategoryDAO;
	private SubCategoryDAO subCategoryDAO;
	private ProductDAO productDAO;
	private ProductImgDAO productImgDAO;
	
	private boolean isProductNameChecked = false; //상품명 중복체크 클릭 여부
	private boolean isProductNameDuplicate = false; // 상품명 중복 여부

	public ProductRegistPage(ShopAdminMainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		productDAO = new ProductDAO();
		productImgDAO = new ProductImgDAO();
		topCategoryDAO = new TopCategoryDAO();
		subCategoryDAO = new SubCategoryDAO();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(new Color(245, 247, 250));

		JPanel formPanel = initFormPanel();
		
		add(Box.createVerticalGlue());
		add(formPanel);
		add(Box.createVerticalGlue());
		
	}
	
	private JPanel initFormPanel() {
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBackground(Color.WHITE);
		formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
		formPanel.setMaximumSize(new Dimension(500, 610));
		formPanel.setMinimumSize(new Dimension(500, 610));
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		//타이틀 생성
		JLabel title = new JLabel("상품 등록");
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

		//카테고리 설정
		setCategory();
		
		//상품명 필드 + 중복확인 버튼
		productNameField = new JTextField(16);
		checkBtn = ButtonUtil.createDefaultButton("중복확인", 13, 90, 36);
		formPanel.add(FieldUtil.createFieldWithButton("상품명", productNameField, checkBtn));
		formPanel.add(Box.createVerticalStrut(12));
		
		//상품명 바뀌면 중복검사 상태 초기화
		productNameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { markEmailUnchecked(); }
            public void removeUpdate(DocumentEvent e) { markEmailUnchecked(); }
            public void changedUpdate(DocumentEvent e) { markEmailUnchecked(); }

            private void markEmailUnchecked() {
            	isProductNameChecked = false;
            	isProductNameDuplicate = false;
            }
        });

		// 기본 필드
        brandField = new JTextField(16);
        formPanel.add(FieldUtil.createField("브랜드", brandField));
        formPanel.add(Box.createVerticalStrut(12));
        priceField = new JTextField(16);
        formPanel.add(FieldUtil.createField("가격", priceField));
        formPanel.add(Box.createVerticalStrut(12));
        detailField = new JTextField(16);
        formPanel.add(FieldUtil.createField("상품설명", detailField));
        formPanel.add(Box.createVerticalStrut(12));
        stockQuantityField = new JTextField(16);
        formPanel.add(FieldUtil.createField("수량", stockQuantityField));
        formPanel.add(Box.createVerticalStrut(12));


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
		
		
		formPanel.add(createButtonPanel());
		
		return formPanel;
	}
	
	private JPanel createButtonPanel() {
		
		// 등록 , 목록으로 돌아가기 버튼 생성
		registBtn = ButtonUtil.createPrimaryButton("저장", 15, 120, 40);
		listBtn = ButtonUtil.createDefaultButton("목록", 15, 120, 40);

		//중복 확인 버튼 이벤트
        checkBtn.addActionListener(e->{
        	isProductNameDuplicate  = productDAO.existByProductName(productNameField.getText());
        	if (isProductNameDuplicate ) {
        		isProductNameChecked  = false;
        		showErrorMessage("상품명이 중복됐습니다. 다른 상품명을 입력해주세요.");
        	} else {
        		isProductNameChecked  = true;
        		JOptionPane.showMessageDialog(this, "사용 가능한 상품명입니다.");
        	}
        });
		// 등록 버튼 이벤트 연결 (등록 이벤트 중복 방지)
        if (registBtn.getActionListeners().length == 0) {
	        registBtn.addActionListener(e->{
	        	if (validateForm()) {
	        		int result = JOptionPane.showConfirmDialog(
	        			this,
	        			"정말 등록하시겠습니까?",
	        			"확인",
	        			JOptionPane.YES_NO_OPTION
	        		);

	        		if (result == JOptionPane.YES_OPTION) {
	        			registerProduct();
	        			clearForm();
	        			JOptionPane.showMessageDialog(this, "등록이 완료되었습니다.");
	        			mainFrame.showContent("PRODUCT_LIST");
	        		}
	        	}
	        });
        }
		
		//목록 버튼 이벤트
		listBtn.addActionListener(e -> {
			mainFrame.showContent("PRODUCT_LIST");
		});

		// 버튼 패널
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.add(registBtn);
		buttonPanel.add(listBtn);
		buttonPanel.setMaximumSize(new Dimension(300, 50));
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		return buttonPanel;
	}

	//상위 카테고리 항목 채우기
	public void getTopCategory() {
		List<TopCategory> topList = topCategoryDAO.selectAll();

		TopCategory dummy = new TopCategory();
		dummy.setName("상위 카테고리를 선택하세요");
		dummy.setTop_category_id(0);
		cb_topcategory.addItem(dummy);

		for (int i = 0; i < topList.size(); i++) {
			TopCategory topcategory = topList.get(i);
			cb_topcategory.addItem(topcategory);
		}
	}

	//상위 카테고리 항목을 눌렀을 때 , 하위 카테고리 항목 채우기
	public void getSubCategory(TopCategory topCategory) {

		List<SubCategory> subList = subCategoryDAO.selectByTop(topCategory);

		cb_subcategory.removeAllItems();

		SubCategory dummy = new SubCategory();
		dummy.setName("하위 카테고리를 선택하세요");
		dummy.setSub_category_id(0);
		cb_subcategory.addItem(dummy);

		// 서브 카테고리 수만큼 반복하면서 , 두번째 콤보박스에 SubCategory 모델을 채워넣기.
		for (int i = 0; i < subList.size(); i++) {
			SubCategory subCategory = subList.get(i); // i번째 요소 꺼내기
			cb_subcategory.addItem(subCategory);
		}
	}
	
	//상위 , 하위 카테고리 채워넣기
	public void setCategory() {
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
	}

	//filechooser로 이미지 선택했을 때 , 파일명만 보이게 하기
	public void selectImg() {
		chooser.showOpenDialog(ProductRegistPage.this);
		files = chooser.getSelectedFiles();
		if (files.length > 0) {
			String fileNames = Arrays.stream(files).map(File::getName).collect(Collectors.joining(", "));
			imageField.setText(fileNames);
		}
	}

	// 유효성 검사 후 insert
	public boolean validateForm() {
		
		//가격에 0이상의 정수를 입력하였는지 검사
		String price = priceField.getText();
		boolean b_price = price.matches("\\d+");
		
		//재고에 0이상의 정수를 입력하였는지 검사
		String quantity = stockQuantityField.getText();
		boolean b_quantity = quantity.matches("\\d+");
		
		if (cb_topcategory.getSelectedIndex() == 0) return showErrorMessage("상위 카테고리를 선택하세요");
		if (cb_subcategory.getSelectedIndex() == 0) return showErrorMessage("하위 카테고리를 선택하세요");
		
		if(productNameField.getText().trim().isEmpty()) return showErrorMessage("상품명을 입력해주세요");
        if (!isProductNameChecked ) return showErrorMessage("상품명 중복확인을 해주세요.");
        if (isProductNameDuplicate ) return showErrorMessage("상품명이 중복됐습니다. 다른 상품명을 입력해주세요.");
        
        if(brandField.getText().trim().isEmpty()) return showErrorMessage("브랜드를 입력해주세요");
        if(priceField.getText().trim().isEmpty()) return showErrorMessage("가격을 입력해주세요");
        if(detailField.getText().trim().isEmpty()) return showErrorMessage("상품설명을 입력해주세요");
        if(stockQuantityField.getText().trim().isEmpty()) return showErrorMessage("재고를 입력해주세요");
        
        if (files == null || files.length == 0) return showErrorMessage("파일을 최소 1개 이상 선택해주세요");
        
        if(b_price == false) return showErrorMessage("가격은 0이상의 정수만 입력하세요.");
        if(b_quantity == false) return showErrorMessage("재고는 0이상의 정수만 입력하세요.");
		
        return true;
	}

	// DB 입력
	public void registerProduct() {
		try {
			Product product = new Product();
			ProductImg productImg = new ProductImg();
			
			product.setSub_category((SubCategory) cb_subcategory.getSelectedItem());
			product.setName(productNameField.getText());
			product.setBrand_name(brandField.getText());
			product.setPrice(Integer.parseInt(priceField.getText()));
			product.setDetail(detailField.getText());
			product.setStock_quantity(Integer.parseInt(stockQuantityField .getText()));
			
			productDAO.insertProduct(product);
			
			//product_img 테이블에 insert (product_id 구해온 후 insert)
			int product_id = productDAO.selectRecentPk();
			product.setProduct_id(product_id);
			
			for (int i = 0; i < files.length; i++) {
				File file = files[i]; 
				productImg.setProduct(product); 
				productImg.setFilename(file.getAbsolutePath()); 
				productImgDAO.insertProductImg(productImg);
			}

		} catch (UserException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}

	}

	//오류 메세지 출력
	private boolean showErrorMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
		return false;
	}
	
	//등록 폼 초기화
	private void clearForm() {
    	cb_topcategory.setSelectedIndex(0);
		productNameField.setText("");
		brandField.setText("");
		priceField.setText("");
		detailField.setText("");
		stockQuantityField.setText("");
    	isProductNameChecked  = false;
    	isProductNameDuplicate  = false;
		
	}
	
	// 진입 전 필수 함수 호출
	public void prepare() {
		clearForm();
	}
}