package com.eoneifour.wms.home.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
/* javax.swing.Timer는 자바의 스윙(Swing) GUI 애플리케이션에서 주기적인 작업을 간편하게 수행할 수 있도록 도와주는 클래스. 
 * 특히 이벤트 디스패치 스레드(Event Dispatch Thread, EDT) 에서 동작하기 때문에 GUI를 안전하게 조작할 수 있다는 장점이 있음.
 */
import javax.swing.Timer;

import com.eoneifour.common.frame.AbstractMainFrame;
import com.eoneifour.common.util.ButtonUtil;
import com.eoneifour.common.util.DBManager;
import com.eoneifour.wms.auth.model.Admin;
import com.eoneifour.wms.auth.view.AdminDeletePage;
import com.eoneifour.wms.auth.view.AdminEditPage;
import com.eoneifour.wms.auth.view.AdminLoginPage;
import com.eoneifour.wms.auth.view.AdminRegistPage;
import com.eoneifour.wms.common.config.Config;
import com.eoneifour.wms.inbound.view.RackInboundStatusPage;
import com.eoneifour.wms.inboundrate.repository.RackDAO;
import com.eoneifour.wms.inboundrate.view.AllInboundRatePage;
import com.eoneifour.wms.inboundrate.view.StackerInboundRate;
import com.eoneifour.wms.iobound.repository.InBoundOrderDAO;
import com.eoneifour.wms.iobound.view.InboundOrderPage;
import com.eoneifour.wms.iobound.view.OutBoundOrderPage;
import com.eoneifour.wms.iobound.view.lookupProduct;
<<<<<<< HEAD
import com.eoneifour.wms.monitoring.view.MonitoringPopup;
=======
import com.eoneifour.wms.monitoring.repository.ConveyorDAO;
>>>>>>> 4f59b8588a43cd53df5d8dc80b005fb10f549d01

/**
 * - 사이드 메뉴바, 상단 메뉴바 구현. - 상태바에 DB 상태 표시. (추후 클래스 분리해야 함.)
 * 
 * @author 재환
 * @since 2025. 6. 19.
 */
public class MainFrame extends AbstractMainFrame {
	JLabel dbStatusLabel;
	DBManager db;
	public AdminEditPage adminEditPage;
	
	ConveyorDAO cd;
	InBoundOrderDAO io;
	JPanel leftPane;
	
	

	public Admin admin;
	JLabel adminInfoLabel;

	public MainFrame() {
		super("WMS 메인(관리자)"); // 타이틀 설정

		menuCardPanel.setPreferredSize(new Dimension(0, 50));
		cd = new ConveyorDAO();
		io = new InBoundOrderDAO();
		connectDB(); // 프로그램 가동시 DB 연결
		initPages();

		// DB연결
		updateDBstatus(dbStatusLabel);
		autoLoadingConveyor();
		// 정해진 시간 간격으로 ActionEvent(ActionListener의 actionPerformed()) 를 발생시키는 메서드.
		// 5초 간격마다 DB 연결 상태 체크.
		new Timer(5000, e -> updateDBstatus(dbStatusLabel)).start();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// 자원 정리 예시
				DBManager.getInstance().release(DBManager.getInstance().getConnection());

				// 종료
				System.exit(0);
			}
		});
	}


	/***
	 * Content(세부 메뉴별 Panel) 클래스 생성 후 아래 메서드에서 new 해야함. 2번째 매개변수(String)는
	 * Config.java를 확인 후 대응되는 KEYS값을 넣어주면 됨.
	 * 
	 * @TODO 각 메뉴별 기능 구현이 완료되면 맵핑이나.. 다른 방식 활용해서 리팩토링 예정
	 * @author 재환
	 */
	
	// 완 로그아웃 메서드
    public void logout() {
        this.admin = null;
        setAdminInfo(null); // 상단 정보 초기화
        leftPanel.setVisible(false); // 좌측 메뉴 숨김
        showContent("ADMIN_LOGIN"); // 로그인 화면으로 이동
    }
	
	private void initPages() {
		
		//완 로그인 페이지
		adminEditPage = new AdminEditPage(this);
		contentCardPanel.add(new AdminLoginPage(this), "ADMIN_LOGIN");
		contentCardPanel.add(new AdminRegistPage(this), "ADMIN_REGISTER");
		contentCardPanel.add(new AdminDeletePage(this), "ADMIN_DELETE");
		contentCardPanel.add(adminEditPage, "ADMIN_EDIT");

		// 홈 버튼 연결
		contentCardPanel.add(new HomePage(this), "HOME");

		// 초기 화면을 홈 화면으로 고정하기 위한 메서드.
		contentCardPanel.revalidate();
		contentCardPanel.repaint();

		// 세부 페이지 부착
		contentCardPanel.add(new InboundOrderPage(this), "INBOUND_ORDER");
		contentCardPanel.add(new OutBoundOrderPage(this), "OUTBOUND_ORDER");

		contentCardPanel.add(new lookupProduct(this), "PRODUCT_LOOKUP");
		contentCardPanel.add(new AllInboundRatePage(this), "ALL_INBOUND_RATE");
		contentCardPanel.add(new StackerInboundRate(this), "STACKER_INBOUND_RATE");
		contentCardPanel.add(new RackInboundStatusPage(this), "RACK_INBOUND_STATUS");

		// 완 로그인 페이지
		adminEditPage = new AdminEditPage(this);

		contentCardPanel.add(new AdminLoginPage(this), "ADMIN_LOGIN");
		contentCardPanel.add(new AdminRegistPage(this), "ADMIN_REGISTER");
		contentCardPanel.add(new AdminDeletePage(this), "ADMIN_DELETE");
		contentCardPanel.add(adminEditPage, "ADMIN_EDIT");

		createSubMenu();
		// 초기 화면을 홈 화면으로 고정하기 위한 메서드.
		contentCardPanel.revalidate();
		contentCardPanel.repaint();

	}

	// 최상단 패널
	@Override
	public JPanel createTopPanel() {
		JPanel infoBar = creatInfoBar();

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(infoBar, BorderLayout.NORTH);

		return topPanel;
	}

	// 최상달 패널에 부착할 내용
	private JPanel creatInfoBar() {

		JPanel infoBar = new JPanel(new BorderLayout());
		infoBar.setPreferredSize(new Dimension(1280, 30));
		infoBar.setBackground(Color.BLACK);

		// 홈 버튼
		JButton homeBtn = new JButton("HOME");
		ButtonUtil.styleHeaderButton(homeBtn);
		homeBtn.setPreferredSize(null);
		homeBtn.setContentAreaFilled(false);
		homeBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		homeBtn.addActionListener(e -> showContent("HOME"));

		// DB 접속상태
		dbStatusLabel = new JLabel("DB 접속중");
		dbStatusLabel.setPreferredSize(null);
		dbStatusLabel.setFont(new Font("Noto Sans CJK KR", Font.PLAIN, 15)); // Noto Sans CJK KR --> 이모지를 위한 별도 글꼴
		dbStatusLabel.setForeground(Color.YELLOW);

		// 테스트용 DB 연결해제 버튼
		JButton disconnectDB = new JButton("DB 연결해제");
		ButtonUtil.styleHeaderButton(disconnectDB);
		disconnectDB.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		disconnectDB.setPreferredSize(null);
		disconnectDB.addActionListener(e -> {
			db.release(db.getConnection());
			updateDBstatus(dbStatusLabel); // 즉시 갱신
		});

		// 왼쪽 정렬 + 좌우 15pt,위아래 10px 여백을 위한 Panel
		JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		leftWrapper.setOpaque(false);
		leftWrapper.add(homeBtn);
		leftWrapper.add(dbStatusLabel);
		leftWrapper.add(disconnectDB);
		infoBar.add(leftWrapper, BorderLayout.WEST);
		// Right Panel: 관리자 이름 포함한 인삿말
		// TODO --> 추후, 계정 연동 필요
		adminInfoLabel = new JLabel();
		adminInfoLabel.setForeground(Color.WHITE);
		
        // ✅ 마우스 클릭 시 로그아웃 팝업 메뉴 띄우기
        adminInfoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPopupMenu popup = new JPopupMenu();
                JMenuItem logoutItem = new JMenuItem("로그아웃");
                logoutItem.addActionListener(ev -> logout()); // ✅ 로그아웃 호출
                popup.add(logoutItem);
                popup.show(adminInfoLabel, e.getX(), e.getY());
            }
        });
		
		// 오른쪽 정렬 + 좌우 15pt,위아래 10px 여백을 위한 Panel
		JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		rightWrapper.setOpaque(false); // 컴포넌트의 투명여부 설정
		rightWrapper.add(adminInfoLabel);
		infoBar.add(rightWrapper, BorderLayout.EAST);

		return infoBar;
	}

	// 카테고리별 메인 메뉴를 담을 패널
	@Override
	public JPanel createLeftPanel() {
		JPanel menuBar = createMainMenu();

		leftPanel = new JPanel(new BorderLayout());
		leftPanel.setPreferredSize(new Dimension(150, 0)); // 사이드바 폭 설정
		leftPanel.add(menuBar, BorderLayout.CENTER);
		
		leftPanel.setVisible(false);

		return leftPanel;
	};

	// 카테고리별 세부 페이지
	private JPanel createMainMenu() {
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
		menuPanel.setBackground(Color.WHITE);

		// wms config에 정의된 페이지의 key값, name값을 가져와 디자인 및 이벤트 연결
		for (int i = 0; i < Config.MENUNAME.length; i++) {
			int index = i;
			JButton button = new JButton(Config.MENUNAME[index]);
			ButtonUtil.styleMenuButton(button);
			button.addActionListener(e -> showTopMenu(Config.MENUKYES[index]));
			button.setAlignmentX(JButton.CENTER_ALIGNMENT);
			button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // 폭 자동 확장

			menuPanel.add(Box.createVerticalStrut(20)); // 간격 추가
			menuPanel.add(button);

		}
		//
		menuPanel.add(Box.createVerticalGlue()); // 아래 공간 채우기
		return menuPanel;
	};

	// wms config에 정의된 페이지의 key값, name값을 가져와 디자인 및 이벤트 연결
	private void createSubMenu() {
		for (int i = 0; i < Config.PAGENAME.length; i++) {
			String groupKey = Config.MENUKYES[i];

			JPanel groupPanel = new JPanel();
			groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.X_AXIS));
			groupPanel.setBackground(Color.LIGHT_GRAY);

			for (int j = 0; j < Config.PAGENAME[i].length; j++) {
				groupPanel.add(Box.createHorizontalStrut(20));

				JButton button = new JButton(Config.PAGENAME[i][j]);
				ButtonUtil.styleMenuButton(button);
				final String PAGEKEY = Config.PAGEKEYS[i][j];

				button.addActionListener(e -> showContent(PAGEKEY));
				button.setAlignmentX(JButton.CENTER_ALIGNMENT);
				groupPanel.add(button);

				/***
				 * 팝업으로 띄울시 이곳에다가 추가
				 */

				if(PAGEKEY.equals("MONITORING")) {
			        button.addMouseListener(new MouseAdapter() {
			        	@Override
			        	public void mouseClicked(MouseEvent e) {
			        		MonitoringPopup.showPopup(MainFrame.this); // 팝업만 실행
			        	}
					});
				}
			}
			menuCardPanel.add(groupPanel, groupKey);
		}

	}

	// 메인 카테고리 버튼 클릭에 대응되는 세부 카테고리 패널
	public void showTopMenu(String key) {
		CardLayout layout = (CardLayout) menuCardPanel.getLayout();
		layout.show(menuCardPanel, key);
	}

	// DB 접속 상태를 체크하는 메서드.
	private void updateDBstatus(JLabel dbStatus) {
		boolean isConnected = DBManager.getInstance().isConnected();
		if (isConnected) {
			dbStatus.setText("📶 DB 연결됨");
			dbStatus.setForeground(Color.GREEN);
		} else {
			dbStatus.setText("🚫 DB 연결 끊김");
			dbStatus.setForeground(Color.RED);
		}
	}

	// 관리자 로그인 시, 해당관리자의 정보를 상단 영역에 출력하기 위한 메서드 정의 (by Wan)
	public void setAdminInfo(String name) {
		adminInfoLabel.setText(name);
	}

	public void autoLoadingConveyor() {
		new Thread(() -> {
			try {
				while (true) {
					Thread.sleep(5000); // 9초대기
					if (cd.selectById(301) == 1) {
						System.out.println("컨베이어에 이미 제품이 적재되어 있습니다.");
						continue;
					}
					int[] pos = io.getPositionByASC();
					if (pos.length > 1) {
						cd.updateOnProductByIdWithPos(pos[0], pos[1], pos[2], pos[3]);
						System.out.println();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	// DB 연결
	public void connectDB() {
		db = DBManager.getInstance();
	}
	
	public void test() {
		leftPanel.setVisible(true);
	}
	
	
}
