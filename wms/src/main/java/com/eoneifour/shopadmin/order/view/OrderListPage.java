package com.eoneifour.shopadmin.order.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.Refreshable;
import com.eoneifour.common.util.TableUtil;
import com.eoneifour.shopadmin.order.model.Order;
import com.eoneifour.shopadmin.order.repository.OrderDAO;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;

public class OrderListPage extends AbstractTablePage implements Refreshable {
	private ShopAdminMainFrame mainFrame;
	
	private JTextField searchField;

	private List<Order> orderList;
	private OrderDAO orderDAO;
	private String[] cols = {"주문번호", "주문일시", "고객명", "상품명", "수량", "총 결제금액", "주문상태", "수정", "취소"};
	
	public OrderListPage(ShopAdminMainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.orderDAO = new OrderDAO();
		
		initTopPanel();
        initTable();
        applyTableStyle();
    }
	
	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));
        JLabel title = new JLabel("주문 목록");
        title.setFont(new Font("맑은 고딕",Font.BOLD,24));
        topPanel.add(title, BorderLayout.WEST);
        
		//검색 영역
		searchField = new JTextField("고객명을 입력하세요");
		searchField.setForeground(Color.GRAY);
		searchField.setPreferredSize(new Dimension(250, 30));
		JButton searchBtn = ButtonUtil.createPrimaryButton("검색", 15, 100, 30);
		searchBtn.setBorderPainted(false);

		placeholder();
		
		//검색 기능
		searchBtn.addActionListener(e->{
			String keyword = searchField.getText().trim();
			List<Order> searchResults;
			
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
		orderList = orderDAO.getOrderList();
        
        model = new DefaultTableModel(toTableData(orderList), cols) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);
        table.setRowHeight(36);
        TableUtil.applyDefaultTableStyle(table);
        applyOrderStatusRenderer(table);
        TableUtil.applyConditionalTextRenderer(table, "주문상태", "주문확인중", new Color(194, 192, 72));
		TableUtil.applyConditionalTextRenderer(table, "주문상태", "배송준비", new Color(129, 193, 71));
        
        // 상세, 수정, 취소 이벤트
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                
                int orderId = (int)model.getValueAt(row, 0);
                String userName = (String)model.getValueAt(row, 2);
                int quantity = (int)model.getValueAt(row, 4);
                String orderStatus = (String)model.getValueAt(row, 6);
                
                if (col == table.getColumn("수정").getModelIndex()) { // 주문 수정
                	if(!orderStatus.equals("주문확인중")) JOptionPane.showMessageDialog(null,"주문확인중 상태가 아닌 주문은 수정할 수 없습니다.");
                	else {
                		mainFrame.orderUpdatePage.prepare(orderId);
                    	mainFrame.showContent("ORDER_UPDATE");
                	}
                } else if (col == table.getColumn("취소").getModelIndex()) { // 주문 취소
                	if(!orderStatus.equals("주문확인중")) JOptionPane.showMessageDialog(null,"주문확인중 상태가 아닌 주문은 취소할 수 없습니다.");
                	else {
                		int confirm = JOptionPane.showConfirmDialog(null, "정말 " + userName + "님의 주문번호 " + orderId + "를 취소하시겠습니까?", "주문 취소", JOptionPane.WARNING_MESSAGE ,JOptionPane.YES_NO_OPTION);
                		if (confirm == JOptionPane.YES_OPTION) {
                			try {
                				orderDAO.cancelOrder(orderId);
                				JOptionPane.showMessageDialog(null, orderId + " 주문 취소되었습니다.");
                				refresh();
                			} catch (UserException e2) {
                				JOptionPane.showMessageDialog(null, e2.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                			}
                		}
                	}
                } else if (col == table.getColumn("주문상태").getModelIndex() && (orderStatus == "주문확인중" || orderStatus == "배송준비")) { // 상태변경
            		int confirm = JOptionPane.showConfirmDialog(null, orderId + "번 " + orderStatus + " 처리 하시겠습니까?", orderStatus, JOptionPane.YES_NO_OPTION);
            		if (confirm == JOptionPane.YES_OPTION) {
            			try {
            				if(orderStatus=="주문확인중") orderDAO.changeStatus(orderId, 0, 0);
            				else orderDAO.changeStatus(orderId, 1, quantity);
            				JOptionPane.showMessageDialog(null, orderStatus + "처리 완료되었습니다.");
            				refresh();
            			} catch (UserException e2) {
            				JOptionPane.showMessageDialog(null, e2.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            			}
            		}
            	} else { // 주문 상세
                	mainFrame.orderDetailPage.setUser(orderId);
                	mainFrame.showContent("ORDER_DETAIL");
                }
            }
        });
        
        // 마우스가 수정/취소 셀 위에 있을 때 손 모양 커서로 변경
        table.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                String columnName = table.getColumnName(col);
                if ("수정".equals(columnName) || "주문상태".equals(columnName) || "취소".equals(columnName)) {
                    table.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
	}
	
	// 테이블 데이터 새로고침
	public void refresh() {
		orderList = orderDAO.getOrderList();
		model.setDataVector(toTableData(orderList), cols);

		TableUtil.applyDefaultTableStyle(table);
		
		applyOrderStatusRenderer(table);
		TableUtil.applyColorTextRenderer(table, "수정", new Color(25, 118, 210));
		TableUtil.applyColorTextRenderer(table, "취소", new Color(211, 47, 47));
	}
	
	// 주문 상태에 맞는 컬럼 색상 출력
	private void applyOrderStatusRenderer(JTable table) {
	    table.getColumn("주문상태").setCellRenderer(new DefaultTableCellRenderer() {
	        @Override
	        public Component getTableCellRendererComponent(
	            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	            
	            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	            label.setFont(new Font("맑은 고딕", Font.BOLD, 13));
	            label.setHorizontalAlignment(SwingConstants.CENTER);

	            String val = value == null ? "" : value.toString();
	            switch (val) {
	                case "주문확인중":
	                    label.setForeground(new Color(194, 192, 72)); break;
	                case "배송준비":
	                    label.setForeground(new Color(129, 193, 71)); break;
	                case "배송완료":
	                    label.setForeground(new Color(95, 146, 243)); break;
	                case "주문취소":
	                    label.setForeground(new Color(160, 160, 160)); break;
	                default:
	                    label.setForeground(Color.BLACK);
	            }

	            return label;
	        }
	    });
	}

	
	// 테이블용 데이터로 변환
	private Object[][] toTableData(List<Order> orderList) {
		Object[][] data = new Object[orderList.size()][cols.length];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < orderList.size(); i++) {
			Order order = orderList.get(i);
			data[i] = new Object[] {
				order.getOrderId(), sdf.format(order.getOrderDate()), order.getUserName(), order.getProductName(), order.getQuantity(),
				order.getTotalPrice(), order.getStatusName(), "수정", "취소"
			};
		}
		
		return data;
	}
	
	//검색 TextField에 placeholder 효과 주기 (forcus 이벤트 활용)
	public void placeholder() {
		searchField.addFocusListener(new FocusAdapter() {
		    public void focusGained(FocusEvent e) {
		        if (searchField.getText().equals("고객명을 입력하세요")) {
		            searchField.setText("");
		            searchField.setForeground(Color.BLACK);
		        }
		    }

		    public void focusLost(FocusEvent e) {
		        if (searchField.getText().isEmpty()) {
		            searchField.setForeground(Color.GRAY);
		            searchField.setText("고객명을 입력하세요");
		        }
		    }
		});
	}
}