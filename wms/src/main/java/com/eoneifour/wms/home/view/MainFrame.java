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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
import com.eoneifour.wms.inboundrate.view.AllInboundRatePage;
import com.eoneifour.wms.inboundrate.view.StackerInboundRatePage;
import com.eoneifour.wms.iobound.repository.InBoundOrderDAO;
import com.eoneifour.wms.iobound.view.InboundOrderPage;
import com.eoneifour.wms.iobound.view.LookupProductPage;
import com.eoneifour.wms.iobound.view.OutBoundOrderPage;
import com.eoneifour.wms.iohistory.view.InboundHistoryPage;
import com.eoneifour.wms.iohistory.view.OutBoundHistoryPage;
import com.eoneifour.wms.monitoring.repository.ConveyorDAO;
import com.eoneifour.wms.monitoring.view.MonitoringPopup;

public class MainFrame extends AbstractMainFrame {
	public HomePage homePage;
	public AdminLoginPage adminLoginPage;
	public AdminRegistPage adminRegistPage;
	public AdminEditPage adminEditPage;
	public AdminDeletePage adminDeletePage;
	public InboundOrderPage inboundOrderPage;
	public OutBoundOrderPage outBoundOrderPage;
	public LookupProductPage lookupProductPage;
	public AllInboundRatePage allInboundRatePage;
	public StackerInboundRatePage stackerInboundRatePage;
	public RackInboundStatusPage rackInboundStatusPage;
	public InboundHistoryPage inboundHistoryPage;
	public OutBoundHistoryPage outBoundHistoryPage;

	public JPanel leftPane;
	public Admin admin;
	public JLabel adminInfoLabel;
	public JLabel dbStatusLabel;
	public DBManager db;
	public JPanel menuPanel;

	public ConveyorDAO cd;

	public InBoundOrderDAO io;
	public Map<String, JPanel> subMenuMap = new HashMap<>();

	private List<JButton> mainMenuButtons; // @혜원
	private List<JButton> subMenuButtons; // @혜원

	public MainFrame() {
		super("WMS 메인(관리자)");
		initPages();
		createSubMenu();
		createMainMenu();

		this.leftPanel = createLeftPanel();
		add(this.leftPanel, BorderLayout.WEST);

		cd = new ConveyorDAO();
		io = new InBoundOrderDAO();
		connectDB();
		updateDBstatus(dbStatusLabel);
		autoLoadingConveyor();
		showContent("ADMIN_LOGIN");

		new Timer(5000, e -> updateDBstatus(dbStatusLabel)).start();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				DBManager.getInstance().release(DBManager.getInstance().getConnection());
				System.exit(0);
			}
		});

	}

	public void logout() {
		this.admin = null;
		setAdminInfo(null);
		showContent("ADMIN_LOGIN");
	}

	private void initPages() {
		homePage = new HomePage(this);
		adminLoginPage = new AdminLoginPage(this);
		adminRegistPage = new AdminRegistPage(this);
		adminEditPage = new AdminEditPage(this);
		adminDeletePage = new AdminDeletePage(this);
		inboundOrderPage = new InboundOrderPage(this);
		outBoundOrderPage = new OutBoundOrderPage(this);
		lookupProductPage = new LookupProductPage(this);
		allInboundRatePage = new AllInboundRatePage(this);
		stackerInboundRatePage = new StackerInboundRatePage(this);
		rackInboundStatusPage = new RackInboundStatusPage(this);
		inboundHistoryPage = new InboundHistoryPage(this);
		outBoundHistoryPage = new OutBoundHistoryPage(this);

		contentCardPanel.add(homePage, "HOME");
		contentCardPanel.add(adminLoginPage, "ADMIN_LOGIN");
		contentCardPanel.add(adminRegistPage, "ADMIN_REGISTER");
		contentCardPanel.add(adminDeletePage, "ADMIN_DELETE");
		contentCardPanel.add(adminEditPage, "ADMIN_EDIT");
		contentCardPanel.add(inboundOrderPage, "INBOUND_ORDER");
		contentCardPanel.add(outBoundOrderPage, "OUTBOUND_ORDER");
		contentCardPanel.add(lookupProductPage, "PRODUCT_LOOKUP");
		contentCardPanel.add(allInboundRatePage, "ALL_INBOUND_RATE");
		contentCardPanel.add(stackerInboundRatePage, "STACKER_INBOUND_RATE");
		contentCardPanel.add(rackInboundStatusPage, "RACK_INBOUND_STATUS");
		contentCardPanel.add(inboundHistoryPage, "INBOUND_HISTORY");
		contentCardPanel.add(outBoundHistoryPage, "OUTBOUND_HISTORY");

		contentCardPanel.revalidate();
		contentCardPanel.repaint();
	}

	@Override
	public JPanel createTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(creatInfoBar(), BorderLayout.NORTH);
		return topPanel;
	}

	private JPanel creatInfoBar() {
		JPanel infoBar = new JPanel(new BorderLayout());
		infoBar.setPreferredSize(new Dimension(1280, 30));
		infoBar.setBackground(new Color(33, 33, 33));

		JButton homeBtn = new JButton("HOME");
		ButtonUtil.styleHeaderButton(homeBtn);
		homeBtn.setContentAreaFilled(false);
		homeBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		homeBtn.addActionListener(e -> showContent("HOME"));

		dbStatusLabel = new JLabel("DB 접속중");
		dbStatusLabel.setFont(new Font("Noto Sans CJK KR", Font.PLAIN, 15));
		dbStatusLabel.setForeground(Color.YELLOW);

		JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		leftWrapper.setOpaque(false);
		leftWrapper.add(homeBtn);
		leftWrapper.add(dbStatusLabel);
		infoBar.add(leftWrapper, BorderLayout.WEST);

		adminInfoLabel = new JLabel();
		adminInfoLabel.setForeground(Color.WHITE);
		adminInfoLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JPopupMenu popup = new JPopupMenu();
				JMenuItem logoutItem = new JMenuItem("로그아웃");
				logoutItem.addActionListener(ev -> logout());
				popup.add(logoutItem);
				popup.show(adminInfoLabel, e.getX(), e.getY());
			}
		});

		JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		rightWrapper.setOpaque(false);
		rightWrapper.add(adminInfoLabel);
		infoBar.add(rightWrapper, BorderLayout.EAST);

		return infoBar;
	}

	@Override
	public JPanel createLeftPanel() {
		mainMenuButtons = new ArrayList<>(); // @혜원
		subMenuButtons = new ArrayList<>(); // @혜원
		JPanel menuBar = createMainMenu();

		leftPanel = new JPanel(new BorderLayout());
		leftPanel.setOpaque(false);
		leftPanel.setEnabled(false);
		leftPanel.setPreferredSize(new Dimension(140, 400));
		leftPanel.add(createMainMenu(), BorderLayout.CENTER);
		return leftPanel;
	}

	private JPanel createMainMenu() {
		menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
		menuPanel.setOpaque(true);
		menuPanel.setBackground(new Color(33, 33, 33));

		for (int i = 0; i < Config.MENUNAME.length; i++) {
			int index = i;
			JButton button = new JButton(Config.MENUNAME[index]);
			ButtonUtil.styleMenuButton(button);
			mainMenuButtons.add(button); // 리스트에 버튼 추가 @혜원
			button.addActionListener(e -> {
				showTopMenu(Config.MENUKYES[index]);
				ButtonUtil.applyMenuActiveStyle(mainMenuButtons, button); // 활성화 @혜원
			});
			button.setAlignmentX(JButton.CENTER_ALIGNMENT);
			button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // 폭 자동 확장

			button.setEnabled(admin != null);
			button.setForeground(Color.WHITE);

			menuButtons.add(button);
			menuPanel.add(Box.createVerticalStrut(20));
			button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
			menuPanel.add(button);

		}
		menuPanel.add(Box.createVerticalGlue()); // 아래 공간 채우기

		if (!mainMenuButtons.isEmpty()) {
			ButtonUtil.applyMenuActiveStyle(mainMenuButtons, mainMenuButtons.get(0)); // "HOME" 활성화 @혜원
		}

		return menuPanel;
	}

	private void createSubMenu() {
		for (int i = 0; i < Config.PAGENAME.length; i++) {
			String groupKey = Config.MENUKYES[i];

			JPanel groupPanel = new JPanel();
			groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.X_AXIS)); // 가로 방향 박스 레이아웃
			groupPanel.setPreferredSize(new Dimension(120, 40));
			groupPanel.setBackground(Color.LIGHT_GRAY);
			groupPanel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10)); // 상하 패딩 줄임

			for (int j = 0; j < Config.PAGENAME[i].length; j++) {
				JButton button = new JButton(Config.PAGENAME[i][j]);
				ButtonUtil.styleMenuButton(button);
				subMenuButtons.add(button); // @ 혜원
				final String PAGEKEY = Config.PAGEKEYS[i][j];
				button.addActionListener(e -> {
					if (admin == null) {
						JOptionPane.showMessageDialog(this, "로그인 후 이용 가능합니다.");
						return;
					}
					showContent(PAGEKEY);
				});

				button.addActionListener(e -> {
					showContent(PAGEKEY);
					ButtonUtil.applyMenuActiveStyle(subMenuButtons, button); // @ 혜원
				});
				button.setAlignmentX(JButton.CENTER_ALIGNMENT);
				groupPanel.add(button);

				/***
				 * 팝업으로 띄울시 이곳에다가 추가
				 */

				if (PAGEKEY.equals("MONITORING")) {
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
		if (!subMenuButtons.isEmpty()) { // @혜원
			ButtonUtil.applyMenuActiveStyle(subMenuButtons, subMenuButtons.get(0));
		}

	}

	public void showTopMenu(String key) {
		CardLayout layout = (CardLayout) menuCardPanel.getLayout();
		layout.show(menuCardPanel, key);
	}

	private void updateDBstatus(JLabel dbStatus) {
		boolean isConnected = DBManager.getInstance().isConnected();
		dbStatus.setText(isConnected ? "📶 DB 연결됨" : "🚫 DB 연결 끊김");
		dbStatus.setForeground(isConnected ? Color.GREEN : Color.RED);
	}

	public void setAdminInfo(String name) {
		adminInfoLabel.setText(name);
	}

	public void autoLoadingConveyor() {
		new Thread(() -> {
			try {
				while (true) {
					Thread.sleep(5000);
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

	// 메뉴에 들어가는 버튼들 공통 로직
	private JButton createMenuButton(String title, String pageKey, List<JButton> menuButtons) {
		JButton btn = new JButton(title);
		ButtonUtil.styleMenuButton(btn);
		btn.addActionListener(e -> {
			showTopMenu(pageKey);
			ButtonUtil.applyMenuActiveStyle(menuButtons, btn);
			btn.setForeground(Color.WHITE);
		});
		menuButtons.add(btn);
		return btn;
	}

	public void activateAllSubMenus() {
		for (Map.Entry<String, JPanel> entry : subMenuMap.entrySet()) {
			if (entry.getValue().getParent() == null) {
				menuCardPanel.add(entry.getValue(), entry.getKey());
			}
		}
		menuPanel.setBackground(new Color(33, 33, 33));
		menuCardPanel.repaint();
		menuCardPanel.revalidate();
	}

	public void connectDB() {
		db = DBManager.getInstance();
	}
}
