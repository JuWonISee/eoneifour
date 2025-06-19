package com.eoneifour.shopadmin.user.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.TableUtil;
import com.eoneifour.shopadmin.user.model.User;
import com.eoneifour.shopadmin.user.repository.UserDAO;

public class UserListPage extends AbstractTablePage {
	private AbstractMainFrame mainFrame;
	
	public UserListPage(AbstractMainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		
		initTopPanel();
        initTable();
        applyTableStyle();
    }
	
	public void initTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
        // 패널 안쪽 여백 설정 (시계반대방향)
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));
        // 제목 라벨
        JLabel title = new JLabel("회원 목록");
        title.setFont(new Font("맑은 고딕",Font.BOLD,24));
        topPanel.add(title, BorderLayout.WEST);
        // 등록 버튼
        JButton addBtn = ButtonUtil.createPrimaryButton("회원 등록", 14, 120, 40);
        addBtn.addActionListener(e -> mainFrame.showContent("USER_REGIST"));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addBtn.addActionListener(e -> mainFrame.showContent("USER_REGIST"));
        
        rightPanel.setOpaque(false);
        rightPanel.add(addBtn);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
	}
	
	public void initTable() {
		UserDAO userDAO = new UserDAO();
		List<User> userList = userDAO.getUserList();
		// System.out.println("사용자 수: " + userList.size());
		
        String[] cols = {"회원번호", "이름", "이메일", "가입일", "권한", "상태", "수정", "삭제"};
        Object[][] data = new Object[userList.size()][cols.length];

        for(int i=0;i<userList.size();i++) {
        	User user = userList.get(i);
        	data[i][0] = user.getUserId();
        	data[i][1] = user.getName();
        	data[i][2] = user.getEmail();
        	data[i][3] = new SimpleDateFormat("yyyy-MM-dd").format(user.getCreatedAt());
        	data[i][4] = (user.getRole() == 0) ? "user" : "admin";
        	data[i][5] = (user.getStatus() == 0) ? "활성" : "비활성";
        	data[i][6] = "수정";
        	data[i][7] = "삭제";
        }
        
        model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);

        TableUtil.applyColoredLabelRenderer(table, "수정", new Color(25, 118, 210));
        TableUtil.applyColoredLabelRenderer(table, "삭제", new Color(211, 47, 47));

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == table.getColumn("수정").getModelIndex()) {
                    System.out.println("수정 클릭: " + row);
                } else if (col == table.getColumn("삭제").getModelIndex()) {
                    System.out.println("삭제 클릭: " + row);
                }
            }
        });
	}
}
