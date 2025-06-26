package com.eoneifour.shop.mypage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.exception.UserException;
import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.Refreshable;
import com.eoneifour.common.util.SessionUtil;
import com.eoneifour.common.util.TableUtil;
import com.eoneifour.shop.view.ShopMainFrame;
import com.eoneifour.shopadmin.order.model.Order;
import com.eoneifour.shopadmin.order.repository.OrderDAO;

public class MyOrderListPage extends AbstractTablePage implements Refreshable{
	private ShopMainFrame mainFrame;
	private OrderDAO orderDAO;
	private List<Order> orderList;
	private String[] cols = {"주문번호", "주문일시", "브랜드", "상품명", "가격", "수량", "총 결제금액", "주문상태", "수정", "취소"};
	
	public MyOrderListPage(ShopMainFrame mainFrame) {
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
        JLabel title = new JLabel(SessionUtil.getLoginUser().getName() + "님 주문 내역");
        title.setFont(new Font("맑은 고딕",Font.BOLD,24));
        topPanel.add(title, BorderLayout.WEST);
        
        add(topPanel, BorderLayout.NORTH);
	}
	
	// 테이블 초기화 및 클릭 이벤트 연결
	public void initTable() {
		orderList = orderDAO.getOrderListByUserId(SessionUtil.getLoginUser().getUserId());
        
        model = new DefaultTableModel(toTableData(orderList), cols) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);
        
        // 상세, 수정, 취소 이벤트
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                
                int orderId = (int)model.getValueAt(row, 0);
                String orderStatus = (String)model.getValueAt(row, 7);
                
                if (col == table.getColumn("수정").getModelIndex()) { // 주문 수정
                	if(!orderStatus.equals("주문확인중")) JOptionPane.showMessageDialog(null,"주문확인중 상태가 아닌 주문은 수정할 수 없습니다.");
                	else {
                		mainFrame.myOrderUpdatePage.prepare(orderId);
                		mainFrame.showPage("MY_ORDER_UPD", "MYPAGE_MENU");
                	}
                } else if (col == table.getColumn("취소").getModelIndex()) { // 주문 취소
                	if(!orderStatus.equals("주문확인중")) JOptionPane.showMessageDialog(null,"주문확인중 상태가 아닌 주문은 취소할 수 없습니다.");
                	else {
                		int confirm = JOptionPane.showConfirmDialog(null, "정말 주문번호 " + orderId + "를 취소하시겠습니까?", "주문 취소", JOptionPane.WARNING_MESSAGE ,JOptionPane.YES_NO_OPTION);
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
            	} else { // 주문 상세
                	mainFrame.myOrderDetailPage.setUser(orderId);
                	mainFrame.showPage("MY_ORDER_DTL", "MYPAGE_MENU");
                }
            }
        });
        
        // 마우스가 수정/취소 셀 위에 있을 때 손 모양 커서로 변경
        table.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                String columnName = table.getColumnName(col);
                if ("수정".equals(columnName) || "취소".equals(columnName)) {
                    table.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
	}
	
	// 테이블 데이터 새로고침
	public void refresh() {
		orderList = orderDAO.getOrderListByUserId(mainFrame.userId);
		model.setDataVector(toTableData(orderList), cols);
		applyStyle();
	}
	
	public void applyStyle() {
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
				order.getOrderId(), sdf.format(order.getOrderDate()), order.getBrand(), order.getPrice(), order.getProductName(), order.getQuantity(),
				order.getTotalPrice(), order.getStatusName(), "수정", "취소"
			};
		}
		
		return data;
	}
}

