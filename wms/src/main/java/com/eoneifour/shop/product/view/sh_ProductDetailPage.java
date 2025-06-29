package com.eoneifour.shop.product.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.util.FieldUtil;
import com.eoneifour.common.util.SessionUtil;
import com.eoneifour.shop.common.util.ImgUtil;
import com.eoneifour.shop.product.model.sh_Orders;
import com.eoneifour.shop.product.model.sh_Product;
import com.eoneifour.shop.product.repository.sh_OrdersDAO;
import com.eoneifour.shop.product.repository.sh_ProductDAO;
import com.eoneifour.shop.view.ShopMainFrame;
import com.eoneifour.shopadmin.product.view.NoticeAlert;

public class sh_ProductDetailPage extends JPanel {
    private ShopMainFrame mainFrame;

    // 좌측 패널
    private JLabel categoryLabel;
    private JLabel imgLabel;

    // 우측 패널
    private JLabel brandLabel;
    private JLabel nameLabel;
    private JLabel priceLabel;
    private JTextField qtyValue;
    private int currentQty;
    private JButton minusBtn;
    private JButton plusBtn;
    private JLabel totalLabel;

    private OrderModalDialog o_dialog;

    private int productId;
    private sh_Product currentProduct;

    private sh_OrdersDAO sh_ordersDAO;

    public sh_ProductDetailPage(ShopMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.sh_ordersDAO = new sh_OrdersDAO();

        setLayout(new GridLayout(1, 2));
        setLeftPanel();
        setRightPanel();
    }

    public void setLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(248, 248, 248));
        leftPanel.setPreferredSize(new Dimension(630, 680));
        leftPanel.setLayout(new BorderLayout());

        categoryLabel = new JLabel("");
        categoryLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));

        imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(categoryLabel, BorderLayout.NORTH);
        leftPanel.add(imgLabel, BorderLayout.CENTER);
        add(leftPanel);
    }

    public void setRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(248, 248, 248));
        rightPanel.setPreferredSize(new Dimension(630, 680));
        rightPanel.setLayout(null);

        brandLabel = new JLabel("");
        brandLabel.setBounds(80, 140, 300, 30);
        brandLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));

        nameLabel = new JLabel("");
        nameLabel.setBounds(80, 220, 300, 40);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));

        priceLabel = new JLabel("");
        priceLabel.setBounds(80, 290, 300, 30);
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 22));

        JLabel qtyLabel = new JLabel("수량 선택");
        qtyLabel.setBounds(80, 360, 100, 30);
        qtyLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));

        qtyValue = new JTextField("1");
        qtyValue.setHorizontalAlignment(JTextField.CENTER);
        qtyValue.setBounds(230, 360, 60, 30);
        qtyValue.setFont(new Font("맑은 고딕", Font.PLAIN, 18));

        minusBtn = new JButton("-");
        minusBtn.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        minusBtn.setBounds(180, 360, 45, 30);
        minusBtn.setBackground(Color.WHITE);

        plusBtn = new JButton("+");
        plusBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        plusBtn.setBounds(295, 360, 45, 30);
        plusBtn.setBackground(Color.WHITE);

        minusBtn.addActionListener(e -> {
            int currentQty = Integer.parseInt(qtyValue.getText());
            if (currentQty > 1) {
                int newQty = currentQty - 1;
                qtyValue.setText(String.valueOf(newQty));
                totalLabel.setText("총 금액 " + FieldUtil.commaFormat(newQty * currentProduct.getPrice()) + "원");
            }
        });

        plusBtn.addActionListener(e -> {
            int currentQty = Integer.parseInt(qtyValue.getText());
            if (currentQty < currentProduct.getStock_quantity()) {
                int newQty = currentQty + 1;
                qtyValue.setText(String.valueOf(newQty));
                totalLabel.setText("총 금액 " + FieldUtil.commaFormat(newQty * currentProduct.getPrice()) + "원");
            }
        });

        qtyValue.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateTotalPrice(); }
            public void removeUpdate(DocumentEvent e) { updateTotalPrice(); }
            public void changedUpdate(DocumentEvent e) { updateTotalPrice(); }

            private void updateTotalPrice() {
                String qtyText = qtyValue.getText().trim();
                if (!qtyText.matches("\\d+") || qtyText.equals("0")) {
                    totalLabel.setText("총 금액");
                    return;
                }
                int qty = Integer.parseInt(qtyText);
                totalLabel.setText("총 금액 " + FieldUtil.commaFormat(qty * currentProduct.getPrice()) + "원");
            }
        });

        totalLabel = new JLabel("총 금액 ");
        totalLabel.setBounds(80, 430, 500, 30);
        totalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        JButton orderBtn = new JButton("주문");
        orderBtn.setBounds(80, 500, 120, 40);
        orderBtn.setBackground(new Color(0, 120, 215));
        orderBtn.setForeground(Color.WHITE);
        orderBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        if (orderBtn.getActionListeners().length == 0) {
            orderBtn.addActionListener(e -> {
                if (validateqty()) {
                    String errorMsg = "주소 : " + SessionUtil.getLoginUser().getAddress() + " " +
                                      SessionUtil.getLoginUser().getAddressDetail() +
                                      "\n정말 이 주소로 주문하시겠습니까?";
                    o_dialog = new OrderModalDialog(mainFrame, errorMsg);
                    o_dialog.setVisible(true);
                    if (o_dialog.isConfirmed()) insertOrder();
                }
            });
        }

        qtyValue.addActionListener(e -> orderBtn.doClick());

        JButton backBtn = new JButton("목록");
        backBtn.setBounds(210, 500, 120, 40);
        backBtn.setBackground(new Color(220, 220, 220));
        backBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        backBtn.addActionListener(e -> mainFrame.showPage("SH_PRODUCT_LIST", "PRODUCT_MENU"));

        rightPanel.add(brandLabel);
        rightPanel.add(nameLabel);
        rightPanel.add(priceLabel);
        rightPanel.add(qtyLabel);
        rightPanel.add(minusBtn);
        rightPanel.add(qtyValue);
        rightPanel.add(plusBtn);
        rightPanel.add(totalLabel);
        rightPanel.add(orderBtn);
        rightPanel.add(backBtn);

        add(rightPanel);
    }

    public void setProduct(int productId) {
        this.productId = productId;
        loadProduct();
    }

    public void loadProduct() {
        try {
            // 이미지 라벨 초기화
            imgLabel.setIcon(null);
            imgLabel.setText("");
            imgLabel.setOpaque(false);

            // 상품 정보 DB에서 가져오기
            currentProduct = new sh_ProductDAO().getProduct(productId);

            // 이미지 파일명 가져오기 (product DAO에서 filename 필드 추가했다고 가정)
            String filename = currentProduct.getFilename() != null ? currentProduct.getFilename() : "";

            boolean imageLoaded = false;

            if (!filename.isEmpty()) {
                // ImgUtil에서 캐싱된 이미지 가져오기
                ImageIcon icon = ImgUtil.getImageIcon(filename);

                if (icon != null) {
                    Image img = icon.getImage();
                    if (img != null && img.getWidth(null) > 0) {
                        Image scaledImg = img.getScaledInstance(450, 450, Image.SCALE_SMOOTH);
                        imgLabel.setIcon(new ImageIcon(scaledImg));
                        imageLoaded = true;
                    }
                }
            }

            if (!imageLoaded) {
                imgLabel.setPreferredSize(new Dimension(450, 450));
                imgLabel.setOpaque(true);
                imgLabel.setBackground(Color.LIGHT_GRAY);
                imgLabel.setText("No Image");
                imgLabel.setHorizontalAlignment(JLabel.CENTER);
                imgLabel.setVerticalAlignment(JLabel.CENTER);
                imgLabel.setForeground(Color.DARK_GRAY);
                imgLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            }

            // 카테고리 텍스트 설정
            categoryLabel.setText(currentProduct.getTop_category().getName() + " > " + currentProduct.getSub_category().getName());

            // 우측 패널 정보 세팅
            brandLabel.setText(currentProduct.getBrand_name());
            nameLabel.setText(currentProduct.getName());
            priceLabel.setText(FieldUtil.commaFormat(currentProduct.getPrice()));

        } catch (UserException e) {
            JOptionPane.showMessageDialog(this, "상품 불러오기 중 오류 발생 " + e.getMessage());
        }

        currentQty = 1;
        qtyValue.setText("1");
        totalLabel.setText("총 금액 " + FieldUtil.commaFormat(Integer.parseInt(qtyValue.getText()) * currentProduct.getPrice()));
    }

    public boolean validateqty() {
        String qtyText = qtyValue.getText().trim();
        if (!qtyText.matches("\\d+")) {
            new NoticeAlert(mainFrame, "주문수량은 숫자만 입력 가능합니다.", "요청 실패").setVisible(true);
            return false;
        }
        int qty = Integer.parseInt(qtyText);
        if (qty <= 0) {
            new NoticeAlert(mainFrame, "주문수량은 1개 이상이어야 합니다.", "요청 실패").setVisible(true);
            return false;
        }
        if (qty > currentProduct.getStock_quantity()) {
            String errorMsg = "현재 보유 재고보다 주문 수량이 많습니다.\n" +
                              "최대 주문 가능 수량: " + currentProduct.getStock_quantity() + "개";
            new NoticeAlert(mainFrame, errorMsg, "요청 실패").setVisible(true);
            qtyValue.setText(String.valueOf(currentProduct.getStock_quantity()));
            return false;
        }
        return true;
    }

    public void insertOrder() {
        try {
            if (!validateqty()) return;
            int qty = Integer.parseInt(qtyValue.getText());
            int userId = SessionUtil.getLoginUser().getUserId();
            String address = SessionUtil.getLoginUser().getAddress();
            String detailAddress = SessionUtil.getLoginUser().getAddressDetail();

            sh_Orders orders = new sh_Orders();
            orders.setTotal_price(currentProduct.getPrice() * qty);
            orders.setUser_id(userId);
            orders.setDelivery_address(address);
            orders.setDelivery_address_detail(detailAddress);
            orders.setProduct_id(currentProduct.getProduct_id());
            orders.setQuantity(qty);
            orders.setPrice(currentProduct.getPrice());

            sh_ordersDAO.insertOrder(orders, qty);

            if (mainFrame.sh_productListPage.scrollPane != null) {
                mainFrame.sh_productListPage.scrollPane.getVerticalScrollBar().setValue(0);
            }
            new NoticeAlert(mainFrame, "주문이 완료되었습니다.", "요청 성공").setVisible(true);
            mainFrame.showPage("SH_ORDER_COMPLETE", "PRODUCT_MENU");

        } catch (UserException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            e.printStackTrace();
        }
    }
}
