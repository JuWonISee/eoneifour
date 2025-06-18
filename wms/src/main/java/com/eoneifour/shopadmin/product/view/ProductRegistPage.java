package com.eoneifour.shopadmin.product.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
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

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.shopadmin.common.exception.ProductException;
import com.eoneifour.shopadmin.common.exception.ProductImgException;
import com.eoneifour.shopadmin.common.util.DBManager;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.product.model.ProductImg;
import com.eoneifour.shopadmin.product.model.SubCategory;
import com.eoneifour.shopadmin.product.model.TopCategory;
import com.eoneifour.shopadmin.product.repository.ProductDAO;
import com.eoneifour.shopadmin.product.repository.ProductImgDAO;
import com.eoneifour.shopadmin.product.repository.SubCategoryDAO;
import com.eoneifour.shopadmin.product.repository.TopCategoryDAO;

public class ProductRegistPage extends JPanel {
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
	DBManager dbManager = DBManager.getInstance();
	ProductDAO productDAO = new ProductDAO();
	ProductImgDAO productImgDAO = new ProductImgDAO();

	public ProductRegistPage(AbstractMainFrame mainFrame) {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(new Color(245, 247, 250));

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBackground(Color.WHITE);
		formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
		formPanel.setMaximumSize(new Dimension(500, 610));
		formPanel.setMinimumSize(new Dimension(500, 610));
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
		JButton registBtn = ButtonUtil.createPrimaryButton("저장", 15, 120, 40);
		JButton listBtn = ButtonUtil.createDefaultButton("목록", 15, 120, 40);

		// 버튼 이벤트 연결
		registBtn.addActionListener(e -> {
			regist();
			
		});

		listBtn.addActionListener(e -> {
			returnList();
			
		});

		// 버튼 패널
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.add(registBtn);
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

		TopCategory dummy = new TopCategory();
		dummy.setName("상위 카테고리를 선택하세요");
		dummy.setTop_category_id(0);
		cb_topcategory.addItem(dummy);

		for (int i = 0; i < topList.size(); i++) {
			TopCategory topcategory = topList.get(i);
			cb_topcategory.addItem(topcategory);
		}
	}

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

	public void selectImg() {
		chooser.showOpenDialog(ProductRegistPage.this);
		files = chooser.getSelectedFiles();
		if (files.length > 0) {
			String fileNames = Arrays.stream(files).map(File::getName).collect(Collectors.joining(", "));
			imageField.setText(fileNames);
		}
	}

	// 유효성 검사 후 insert
	public void regist() {
		
		//가격에 숫자(정수)를 입력하였는지 검사 , 유효성에서 정수가 아니면 반환
		String price = t_price.getText();
		boolean b_price = price.matches("\\d+");
		
		//재고에 숫자(정수)를 입력하였는지 검사 , 유효성에서 정수가 아니면 반환
		String quantity = t_stockQuantity.getText();
		boolean b_quantity = quantity.matches("\\d+");
		
		
		if (cb_topcategory.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "상위 카테고리를 선택하세요");
		} else if (cb_subcategory.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "하위 카테고리를 선택하세요");
		} else if (t_brand.getText().length() < 1) {
			JOptionPane.showMessageDialog(this, "브랜드를 입력하세요");
		} else if (t_productName.getText().length() < 1) {
			JOptionPane.showMessageDialog(this, "상품명을 입력하세요");
		} else if (t_price.getText().length() < 1) {
			JOptionPane.showMessageDialog(this, "가격을 입력하세요");
		} else if (t_detail.getText().length() < 1) {
			JOptionPane.showMessageDialog(this, "상품설명을 입력하세요");
		} else if(b_price == false){
			JOptionPane.showMessageDialog(this, "가격은 숫자만 입력하세요.");
		} else if (t_stockQuantity.getText().length() < 1) {
			JOptionPane.showMessageDialog(this, "재고를 입력하세요");
		} else if(b_quantity == false){
			JOptionPane.showMessageDialog(this, "재고는 숫자만 입력하세요.");
		} else {
			insert();
		}
		
	}

	// DB 입력
	public void insert() {
		Connection con = dbManager.getConnection();
		
		try {
			con.setAutoCommit(false); //트랜젝션 완료시 commit
			
			Product product = new Product();
			
			product.setSub_category((SubCategory) cb_subcategory.getSelectedItem());
			product.setName(t_productName.getText());
			product.setBrand_name(t_brand.getText());
			product.setPrice(Integer.parseInt(t_price.getText()));
			product.setDetail(t_detail.getText());
			product.setStock_quantity(Integer.parseInt(t_stockQuantity .getText()));
			
			productDAO.insert(product);
			

			
			//product_img 테이블에 insert (product_id 구해온 후 insert)
			int product_id = productDAO.selectRecentPk();
			product.setProduct_id(product_id);
			
			for (int i = 0; i < files.length; i++) {
				File file = files[i]; 
				ProductImg productImg = new ProductImg();
				productImg.setProduct(product); 
				productImg.setFilename(file.getName()); 
				productImgDAO.insert(productImg);
			}

			con.commit(); // 에러가 없으면 확정.
		} catch (ProductException | ProductImgException e) {
			
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage()); 
			
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 상품 목록 돌아가기
	public void returnList() {

	}

}