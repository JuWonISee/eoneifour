package com.eoneifour.shop.product.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.shop.product.model.sh_Product;
import com.eoneifour.shop.product.model.sh_ProductImg;
import com.eoneifour.shop.product.repository.sh_ProductDAO;
import com.eoneifour.shop.product.repository.sh_ProductImgDAO;
import com.eoneifour.shop.view.ShopMainFrame;

public class sh_ProductListPage extends JPanel{
	private ShopMainFrame mainFrame;
	private sh_ProductDAO sh_productDAO;
	private sh_ProductImgDAO sh_productImgDAO;
	private sh_ProductDetailPage sh_productDetailPage;
	private JPanel productContainer;
	
	public sh_ProductListPage(ShopMainFrame mainFrame){
		this.mainFrame = mainFrame;
		this.sh_productDetailPage = mainFrame.sh_productDetailPage;
		
		sh_productDAO = new sh_ProductDAO();
		sh_productImgDAO = new sh_ProductImgDAO();
		
        setLayout(new BorderLayout());
        

        // 상품 전체 패널 (GridLayout 사용: 4칸씩)
        productContainer = new JPanel();
        productContainer.setLayout(new GridLayout(0, 4, 40, 50)); // 열 4개, 여백 설정
        productContainer.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // 패널 안쪽 여백 설정

        //기본적으로 전체 상품 리스트 출력
        showAllProducts();
        
        // 스크롤 가능하게 설정
        JScrollPane scrollPane = new JScrollPane(productContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createProductPanel(sh_Product product) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(230, 300));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.setBackground(Color.WHITE);

        // 상품 이미지
        JLabel imgLabel = new JLabel();

        sh_ProductImg productImg;
        
		try {
			productImg = sh_productImgDAO.getProductImg(product.getProduct_id());
			
	        //productImg 객체 및 productimg의 filename이 존재할 경우와 존재하지 않을 경우 맵핑
	        String filename = (productImg != null && productImg.getFilename() != null) ? productImg.getFilename() : "";

	        //이미지가 로드됐는지 확인하기 위한 변수
	        boolean imageLoaded = false;

	        //filename이 비어있지 않을 경우 해당 이미지 로딩
	        if (!filename.isEmpty()) {
	            URL imgUrl = getClass().getClassLoader().getResource(filename);
	            if (imgUrl != null) {
	                ImageIcon icon = new ImageIcon(imgUrl);
	                Image img = icon.getImage();
	                if (img != null && img.getWidth(null) > 0) {
	                    Image scaledImg = img.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
	                    imgLabel.setIcon(new ImageIcon(scaledImg));
	                    imageLoaded = true;
	                }
	            }
	        }

	        //image가 로드되지 않았을 경우 , No image 부착
	        if (!imageLoaded) {
	            imgLabel.setPreferredSize(new Dimension(140, 190));
	            imgLabel.setOpaque(true);
	            imgLabel.setBackground(Color.LIGHT_GRAY);
	            imgLabel.setText("No Image");
	            imgLabel.setHorizontalAlignment(JLabel.CENTER);
	            imgLabel.setVerticalAlignment(JLabel.CENTER);
	            imgLabel.setForeground(Color.DARK_GRAY);
	            imgLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
	        }

	        imgLabel.setHorizontalAlignment(JLabel.CENTER);
	        imgLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 50, 0));
			
		} catch (UserException e) {
			JOptionPane.showMessageDialog(this, "이미지 불러오기 중 오류발생 " + e.getMessage());
		}
        

        // 상품 이름
        JLabel nameLabel = new JLabel(product.getName(), JLabel.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        // 가격
        JLabel priceLabel = new JLabel(FieldUtil.commaFormat(product.getPrice()) + " 원", JLabel.CENTER);
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        priceLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        // 텍스트 영역 구성
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(8)); // 간격
        textPanel.add(priceLabel);
        
        panel.add(imgLabel, BorderLayout.NORTH);
        panel.add(textPanel, BorderLayout.CENTER);

        return panel;
    }
    
    private void refreshProductList(List<sh_Product> productList) {
        productContainer.removeAll(); // 기존 패널 모두 제거
        productContainer.revalidate(); // 레이아웃 다시 계산
        productContainer.repaint();    // UI 다시 그리기

        // 상품갯수만큼 Panel 생성 및 클릭 이벤트
        for (sh_Product product : productList) {
            JPanel productPanel = createProductPanel(product);

            final int productId = product.getProduct_id(); // 상품 ID 저장
            //패널별 클릭 이벤트
            productPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    mainFrame.sh_productDetailPage.setProduct(productId);
                    mainFrame.showPage("SH_PRODUCT_DETAIL", "PRODUCT_MENU");
                }
            });

            productContainer.add(productPanel);
        }
    }
	
    //상품 전체 리스트 보여주기
    public void showAllProducts() {
        List<sh_Product> productList;
		try {
			productList = sh_productDAO.getProductList();
			refreshProductList(productList);
		} catch (UserException e) {
			JOptionPane.showMessageDialog(this, "전체 카테고리 상품 불러오기 중 오류 발생 " + e.getMessage());
		}
        
    }
    
    //카테고리별 리스트 보여주기
    public void showProductsByCategory(int topCategoryId) {
        try {
			List<sh_Product> productList = sh_productDAO.getProductsByTopCategory(topCategoryId);
			refreshProductList(productList);
		} catch (UserException e) {
			JOptionPane.showMessageDialog(this, "카테고리 별 상품 불러오기 중 오류 발생 " + e.getMessage());
		}
    }
    
}
