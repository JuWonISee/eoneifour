package com.eoneifour.shopadmin.user.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import com.eoneifour.common.util.SessionUtil;
import com.eoneifour.common.util.TableUtil;
import com.eoneifour.shopadmin.product.model.Product;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.user.repository.UserDAO;
import com.eoneifour.shopadmin.view.ShopAdminMainFrame;

public class UserListPage extends AbstractTablePage implements Refreshable {
	private ShopAdminMainFrame mainFrame;
	private UserRegistPage userRegistPage;
	
	private JTextField searchField;
	
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
        
		//검색 영역
		searchField = new JTextField("회원명 또는 이메일을 입력하세요");
		searchField.setForeground(Color.GRAY);
		searchField.setPreferredSize(new Dimension(250, 30));
		JButton searchBtn = ButtonUtil.createDefaultButton("검색", 14, 100, 30);
		
		placeholder();
		
		//검색 기능
		searchBtn.addActionListener(e->{
			String keyword = searchField.getText().trim();
			List<User> searchResults;
			
			if (!keyword.isEmpty() || keyword == "회원명 또는 이메일을 입력하세요") {
				searchResults = userDAO.serchByKeyword(keyword);
				searchField.setText(null);
				placeholder();
			} else {
				// keyword가 비어있을 경우 전체 목록 다시 조회
				searchResults = userDAO.getUserList();
				searchField.setText(null);
				placeholder();
			}

			if (searchResults.isEmpty()) {
				JOptionPane.showMessageDialog(null, "해당 제품이 없습니다.", "Info", JOptionPane.INFORMATION_MESSAGE);
				searchResults = userDAO.getUserList();
				searchField.setText(null);
				placeholder();
				searchField.setForeground(Color.BLACK);
			}
			
			model.setDataVector(toTableData(searchResults), cols);
			applyStyle();
		});

		
		// 검색 영역 엔터 이벤트 (검색버튼 클릭과 동일한 효과)
		searchField.addActionListener(e -> {
			searchBtn.doClick(); //
		});
		
        // 등록 버튼
        JButton registBtn = ButtonUtil.createPrimaryButton("회원 등록", 14, 120, 40);
        registBtn.addActionListener(e -> {
        	userRegistPage.prepare();
			mainFrame.showContent("USER_REGIST");
        });
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        rightPanel.setOpaque(false);
		rightPanel.add(searchField);
		rightPanel.add(searchBtn);
		rightPanel.add(Box.createHorizontalStrut(40));
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
        TableUtil.applyDefaultTableStyle(table);
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
                	mainFrame.userUpdatePage.prepare(userId);
                	mainFrame.showContent("USER_UPDATE");
                } else if (col == table.getColumn("삭제").getModelIndex()) {
                	if(userId == SessionUtil.getLoginUser().getUserId()) {
                		JOptionPane.showMessageDialog(null, "현재 로그인된 계정은 삭제할 수 없습니다.");
                	} else {
                		int confirm = JOptionPane.showConfirmDialog(null, "정말 " + userName + "님을 삭제하시겠습니까?", "회원 삭제", JOptionPane.WARNING_MESSAGE ,JOptionPane.YES_NO_OPTION);
                		if (confirm == JOptionPane.YES_OPTION) {
                			try {
                				userDAO.deleteUser(userId);
                				refresh();
                				JOptionPane.showMessageDialog(null, userName + "님이 삭제 처리되었습니다.");
                				
                			} catch (UserException e2) {
                				JOptionPane.showMessageDialog(null, e2.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                			}
                			
                		}
                	}
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
	
	private void applyStyle() {
		TableUtil.applyDefaultTableStyle(table);
		TableUtil.applyColorTextRenderer(table, "수정", new Color(25, 118, 210));
		TableUtil.applyColorTextRenderer(table, "삭제", new Color(211, 47, 47));
	}
	
	// 테이블 데이터 새로고침
	public void refresh() {
		userList = userDAO.getUserList();
		model.setDataVector(toTableData(userList), cols);
		applyStyle();
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
	
	//검색 TextField에 placeholder 효과 주기 (forcus 이벤트 활용)
	public void placeholder() {
		searchField.addFocusListener(new FocusAdapter() {
		    public void focusGained(FocusEvent e) {
		        if (searchField.getText().equals("회원명 또는 이메일을 입력하세요")) {
		            searchField.setText("");
		            searchField.setForeground(Color.BLACK);
		        }
		    }

		    public void focusLost(FocusEvent e) {
		        if (searchField.getText().isEmpty()) {
		            searchField.setForeground(Color.GRAY);
		            searchField.setText("회원명 또는 이메일을 입력하세요");
		        }
		    }
		});
	}
}
