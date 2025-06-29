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
import com.eoneifour.shop.common.util.ImgUtil;
import com.eoneifour.shop.product.model.sh_Product;
import com.eoneifour.shop.product.repository.sh_ProductDAO;
import com.eoneifour.shop.view.ShopMainFrame;

public class sh_ProductListPage extends JPanel {
    private ShopMainFrame mainFrame;
    private sh_ProductDAO sh_productDAO;
    private sh_ProductDetailPage sh_productDetailPage;
    private JPanel productContainer;
    public JScrollPane scrollPane;

    public sh_ProductListPage(ShopMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.sh_productDetailPage = mainFrame.sh_productDetailPage;

        sh_productDAO = new sh_ProductDAO();

        setLayout(new BorderLayout());

        // 상품 전체 패널 (GridLayout 사용: 4칸씩)
        productContainer = new JPanel();
        productContainer.setLayout(new GridLayout(0, 4, 40, 50));
        productContainer.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // 기본적으로 전체 상품 리스트 출력
        showAllProducts();

        // 스크롤 가능하게 설정
        this.scrollPane = new JScrollPane(productContainer);
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

        JLabel imgLabel = new JLabel();

        try {
            String filename = product.getFilename();
            ImageIcon scaledIcon = ImgUtil.getScaledImageIcon(filename, 140, 140);

            if (scaledIcon != null) {
                imgLabel.setIcon(scaledIcon);
            } else {
                // 이미지 없음 처리
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

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "이미지 불러오기 중 오류 발생: " + e.getMessage());
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
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(priceLabel);

        panel.add(imgLabel, BorderLayout.NORTH);
        panel.add(textPanel, BorderLayout.CENTER);

        return panel;
    }

    private void refreshProductList(List<sh_Product> productList) {
        productContainer.removeAll();
        productContainer.revalidate();
        productContainer.repaint();

        for (sh_Product product : productList) {
            JPanel productPanel = createProductPanel(product);

            final int productId = product.getProduct_id();
            productPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    mainFrame.sh_productDetailPage.setProduct(productId);
                    mainFrame.showPage("SH_PRODUCT_DETAIL", "PRODUCT_MENU");
                }
            });

            productContainer.add(productPanel);
        }

        if (scrollPane != null) {
            scrollPane.getVerticalScrollBar().setValue(0);
        }
    }

    public void showAllProducts() {
        try {
            List<sh_Product> productList = sh_productDAO.getProductList();
            refreshProductList(productList);
        } catch (UserException e) {
            JOptionPane.showMessageDialog(this, "전체 카테고리 상품 불러오기 중 오류 발생 " + e.getMessage());
        }
    }

    public void showProductsByCategory(int topCategoryId) {
        try {
            List<sh_Product> productList = sh_productDAO.getProductsByTopCategory(topCategoryId);
            refreshProductList(productList);
        } catch (UserException e) {
            JOptionPane.showMessageDialog(this, "카테고리 별 상품 불러오기 중 오류 발생 " + e.getMessage());
        }
    }
}
