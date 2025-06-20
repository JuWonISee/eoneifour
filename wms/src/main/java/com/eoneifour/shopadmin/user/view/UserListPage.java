package com.eoneifour.shopadmin.user.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.TableUtil;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.user.repository.UserDAO;
import com.eoneifour.shopadmin.view.frame.ShopAdminMainFrame;

public class UserListPage extends AbstractTablePage {
	private ShopAdminMainFrame mainFrame;
	private UserRegistPage userRegistPage;
	
	private List<User> userList;
	private UserDAO userDAO;
	private String[] cols = {"회원번호", "이름", "이메일", "가입일", "권한", "상태", "수정", "삭제"};
	
	public UserListPage(ShopAdminMainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.userRegistPage = mainFrame.userRegistPage;
		this.userDAO = new UserDAO();
		
		initTopPanel();
        initTable();
        applyTableStyle();
    }
	
	// 상단 패널 UI 구성 (제목 + 등록 버튼)
	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
        // 패널 안쪽 여백 설정 (시계반대방향)
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));
        // 제목 라벨
        JLabel title = new JLabel("회원 목록");
        title.setFont(new Font("맑은 고딕",Font.BOLD,24));
        topPanel.add(title, BorderLayout.WEST);
        // 등록 버튼
        JButton registBtn = ButtonUtil.createPrimaryButton("회원 등록", 14, 120, 40);
        registBtn.addActionListener(e -> {
        	userRegistPage.prepare();
			mainFrame.showContent("USER_REGIST");
        });
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        rightPanel.setOpaque(false);
        rightPanel.add(registBtn);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
	}
	
	// 테이블 초기화 및 클릭 이벤트 연결
	public void initTable() {
		userList = userDAO.getUserList();
        
        model = new DefaultTableModel(toTableData(userList), cols) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);
        table.setRowHeight(36); // cell 높이 설정
        // 테이블 컬럼 스타일 적용 (수정: 파랑, 삭제: 빨강)
        TableUtil.applyColorTextRenderer(table, "수정", new Color(25, 118, 210));  // 파랑
        TableUtil.applyColorTextRenderer(table, "삭제", new Color(211, 47, 47));  // 빨강

        // 상세, 수정, 삭제 이벤트
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                
                int userId = (int)model.getValueAt(row, 0);
                String userName = (String)model.getValueAt(row, 1);
                
                // 회원 수정
                if (col == table.getColumn("수정").getModelIndex()) {
                	// 회원 수정 로직
                } else if (col == table.getColumn("삭제").getModelIndex()) {
                	// 회원 삭제 로직
                } else { // 회원 상세
                	mainFrame.userDetailPage.setUser(userId);
                	mainFrame.showContent("USER_DETAIL");
                }
            }
        });
        
        // 마우스가 수정/삭제 셀 위에 있을 때 손 모양 커서로 변경
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                String columnName = table.getColumnName(col);
                if ("수정".equals(columnName) || "삭제".equals(columnName)) {
                    table.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
	}
	
	// 테이블 데이터 새로고침
	public void refresh() {
		userList = userDAO.getUserList();
		model.setDataVector(toTableData(userList), cols);

		TableUtil.applyDefaultTableStyle(table);
		
		TableUtil.applyColorTextRenderer(table, "수정", new Color(25, 118, 210));
		TableUtil.applyColorTextRenderer(table, "삭제", new Color(211, 47, 47));
	}
	
	// 테이블용 데이터로 변환
	private Object[][] toTableData(List<User> userList) {
		Object[][] data = new Object[userList.size()][cols.length];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			data[i] = new Object[] {
				user.getUserId(), user.getName(), user.getEmail(),
				sdf.format(user.getCreatedAt()),
				(user.getRole() == 0) ? "user" : "admin",
				(user.getStatus() == 0) ? "활성" : "비활성",
				"수정", "삭제"
			};
		}
		
		return data;
	}
}
