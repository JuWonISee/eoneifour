package com.eoneifour.shopadmin.order.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
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

	private List<Order> orderList;
	private OrderDAO orderDAO;
	private String[] cols = {"주문번호", "주문일시", "고객명", "상품명", "수량", "가격", "총 결제금액", "배송상태", "수정", "취소"};
	
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
		JTextField searchField = new JTextField("");
		searchField.setPreferredSize(new Dimension(200, 30));
		JButton searchBtn = ButtonUtil.createPrimaryButton("검색", 20, 100, 30);
		searchBtn.setBorderPainted(false);

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
        TableUtil.applyColorTextRenderer(table, "수정", new Color(25, 118, 210));
        TableUtil.applyColorTextRenderer(table, "취소", new Color(211, 47, 47));
        
        // 상세, 수정, 취소 이벤트
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                
                int orderId = (int)model.getValueAt(row, 0);
                String userName = (String)model.getValueAt(row, 2);
                String orderStatus = (String)model.getValueAt(row, 7);
                // 주문 수정
                if (col == table.getColumn("수정").getModelIndex()) {
                	if(!orderStatus.equals("배송준비")) JOptionPane.showMessageDialog(null,"배송 준비가 아닌 주문은 수정할 수 없습니다.");
                	else {
                		mainFrame.orderUpdatePage.prepare(orderId);
                    	mainFrame.showContent("ORDER_UPDATE");
                	}
                } else if (col == table.getColumn("취소").getModelIndex()) { // 주문 취소
                	if(!orderStatus.equals("배송준비")) JOptionPane.showMessageDialog(null,"배송 준비가 아닌 주문은 취소할 수 없습니다.");
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
                } else { // 주문 상세
                	mainFrame.orderDetailPage.setUser(orderId);
                	mainFrame.showContent("ORDER_DETAIL");
                }
            }
        });
        
        // 마우스가 수정/취소 셀 위에 있을 때 손 모양 커서로 변경
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
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
		orderList = orderDAO.getOrderList();
		model.setDataVector(toTableData(orderList), cols);

		TableUtil.applyDefaultTableStyle(table);
		
		TableUtil.applyColorTextRenderer(table, "수정", new Color(25, 118, 210));
		TableUtil.applyColorTextRenderer(table, "취소", new Color(211, 47, 47));
	}
	
	// 테이블용 데이터로 변환
	private Object[][] toTableData(List<Order> orderList) {
		Object[][] data = new Object[orderList.size()][cols.length];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < orderList.size(); i++) {
			Order order = orderList.get(i);
			data[i] = new Object[] {
				order.getOrderId(), sdf.format(order.getOrderDate()), order.getUserName(), order.getProductName(), order.getQuantity(),
				order.getPrice(), order.getTotalPrice(), order.getStatusName(), "수정", "취소"
			};
		}
		
		return data;
	}
}