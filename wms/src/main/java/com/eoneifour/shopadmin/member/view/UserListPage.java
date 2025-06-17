package com.eoneifour.shopadmin.member.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.frame.AbstractTablePage;
import com.eoneifour.common.util.ButtonUtil;

public class UserListPage extends AbstractTablePage {
	private AbstractMainFrame mainFrame;
	
	public UserListPage(AbstractMainFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		
		JPanel topPanel = new JPanel(new BorderLayout());
        // 패널 안쪽 여백 설정 (시계반대방향)
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        // 제목 라벨
        JLabel title = new JLabel("회원 목록");
        title.setFont(new Font("맑은 고딕",Font.BOLD,24));
        topPanel.add(title, BorderLayout.WEST);
        // 등록 버튼
        JButton addBtn = new JButton("회원 등록");
        ButtonUtil.configTableButton(addBtn, new Color(25,118,210), 100, 35);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addBtn.addActionListener(e -> mainFrame.showContent("USER_REGIST"));
        
        rightPanel.setOpaque(false);
        rightPanel.add(addBtn);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        
        initTable();
        applyTableStyle();
    }
	
	public void initTable() {
        // 테이블 헤더
        String[] cols = {"회원번호", "이름", "이메일", "가입일", "권한", "상태", "수정", "삭제"};
        // 테이블 바디 샘플 데이터
        Object[][] data = {
            {"1005", "관리자", "admin2", "25.04.05", "admin", "활성", "수정", "삭제"},
            {"1004", "박혜원", "hyeone123@naver.com", "25.04.01", "user", "활성", "수정", "삭제"},
        };

        model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);

        table.getColumn("수정").setCellRenderer((tbl, val, sel, focus, row, col) -> {
            JLabel label = new JLabel("수정", SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(new Color(25, 118, 210));
            label.setForeground(Color.WHITE);
            label.setFont(new Font("맑은 고딕", Font.BOLD, 13));
            label.setBorder(BorderFactory.createEmptyBorder(3,8,3,8));
            return label;
        });
        
        table.getColumn("삭제").setCellRenderer((tbl, val, sel, focus, row, col) -> {
            JLabel label = new JLabel("삭제", SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(new Color(211, 47, 47));
            label.setForeground(Color.WHITE);
            label.setFont(new Font("맑은 고딕", Font.BOLD, 13));
            label.setBorder(BorderFactory.createEmptyBorder(3,8,3,8));
            return label;
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == table.getColumn("수정").getModelIndex()) {
                    // 수정 버튼 클릭 처리
                    System.out.println("수정 클릭: " + row);
                } else if (col == table.getColumn("삭제").getModelIndex()) {
                    // 삭제 버튼 클릭 처리
                    System.out.println("삭제 클릭: " + row);
                }
            }
        });
	}
	
    
}
