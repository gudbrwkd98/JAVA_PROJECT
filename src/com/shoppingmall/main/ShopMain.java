package com.shoppingmall.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.shoppingmall.admin.order.OrderDetail;
import com.shoppingmall.board.Board;
import com.shoppingmall.cart.Cart;
import com.shoppingmall.home.Home;
import com.shoppingmall.member.Login;
import com.shoppingmall.member.MyPage;
import com.shoppingmall.member.RegistForm;
import com.shoppingmall.member.ShopMember;
import com.shoppingmall.product.Product;
import com.shoppingmall.product.ProductDetail;
import com.shoppingmall.util.db.DBManager;

public class ShopMain extends JFrame {
	// ��� ����
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 900;

	// �ֻ��� ��������
	public static final int HOME = 0;
	public static final int PRODUCT = 1;
	public static final int BOARD = 2;
	public static final int MYPAGE = 3;
	public static final int LOGIN = 4;

	// ����������
	public static final int PRODUCT_DETAIL = 5; // ��ǰ ������ ��ǰ ����������
	public static final int MEMBER_REGIST = 6; // ȸ������ �α��� ����������
	public static final int Cart = 7;

	JPanel user_container; // ������,����� ȭ���� �����������ִ� �����̳�
	JPanel p_content;// ���⿡ ��� �������� �̸� �پ�����������
	// ���� showPage�޼���� ������ ���� ����..

	JPanel p_navi; // ������Ʈ�� �� �޴��� ������ �����̳� �г�
	String[] navi_title = { "Home", "Product", "QnA", "MyPage", "Login" };
	public JButton[] navi = new JButton[navi_title.length]; // [][][][][] �迭����

	// ������ ����
	Page[] page = new Page[8]; // �ֻ��� ��������
	ProductDetail productDetail; // �� ������

	JLabel la_footer; // ������ �ϴ��� ī�Ƕ���Ʈ ����
	private DBManager dbManager;
	private Connection con;

	// �α��� �������� ���θ� �˼��ִ� ����
	private boolean hasSession = false;

	public ShopMain() {
		dbManager = new DBManager();
		user_container = new JPanel();
		p_content = new JPanel();
		p_navi = new JPanel(); // ��ư���� ���� �г�
		la_footer = new JLabel("copyright@2020 shoppingMall Java", SwingConstants.CENTER);
		Border emptyBorder = BorderFactory.createEmptyBorder();

		con = dbManager.connect();
		if (con == null) {
			JOptionPane.showMessageDialog(this, "���ӺҰ�");
			System.exit(0);
		} else {
			this.setTitle("ShoppMallApp");
		}

		// ���γ׺���̼� ����
		for (int i = 0; i < navi.length; i++) {
			navi[i] = new JButton(navi_title[i]);
			navi[i].setFont(new Font("���� ����", Font.PLAIN, 15));
			navi[i].setPreferredSize(new Dimension(150, 50));
			navi[i].setBackground(new Color(15,76,129));
			navi[i].setForeground(Color.WHITE);
			navi[i].setBorder(emptyBorder);
			p_navi.add(navi[i]);
		}

		// ������ ����
		page[HOME] = new Home(this);
		page[PRODUCT] = new Product(this);
		page[BOARD] = new Board(this);
		page[MYPAGE] = new MyPage(this);
		page[LOGIN] = new Login(this);
		page[PRODUCT_DETAIL] = new ProductDetail(this); // ���������� ����
		page[MEMBER_REGIST] = new RegistForm(this); // ���������� ����
		page[Cart] = new Cart(this);

		// ��Ÿ�� ����
		user_container.setPreferredSize(new Dimension(WIDTH, HEIGHT - 50));
		user_container.setBackground(Color.white);
		la_footer.setPreferredSize(new Dimension(WIDTH, 50));
		la_footer.setFont(new Font("Arial Black", Font.BOLD, 15));
		p_navi.setBackground(new Color(15,76,129));

		// ����
		user_container.setLayout(new BorderLayout());
		user_container.add(p_navi, BorderLayout.NORTH);

		// ������ ���̱�
		for (int i = 0; i < page.length; i++) {
			p_content.add(page[i]);
		}

		user_container.add(p_content);

		this.add(user_container);
		this.add(la_footer, BorderLayout.SOUTH);

		showPage(LOGIN);

		setSize(1200, 900);
		setVisible(true);
		setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dbManager.disConnect(con);
				System.exit(0);
			}
		});

		// �׺���̼ǹ�ư�� ������ ����
		for (int i = 0; i < navi.length; i++) {
			navi[i].addActionListener((e) -> {
				Object obj = e.getSource();
				if (obj == navi[0]) {
					showPage(0);
				} else if (obj == navi[1]) {
					showPage(1);
				} else if (obj == navi[2]) {
					showPage(2);
				} else if (obj == navi[3]) {
					// Mypage�� ������ �����༭�� �ȵǰ� �α����� ������Ը� ���������
					if (hasSession == false) {
						JOptionPane.showMessageDialog(ShopMain.this, "�α����� ���ּ���");
						showPage(4);
					} else {
						showPage(3);
					}
				} else if (obj == navi[4]) {
					// �α��� / �α׾ƿ� ����
					if (hasSession) {
						int ans = JOptionPane.showConfirmDialog(this, "�α׾ƿ� �Ͻðڽ��ϱ�?");
						if (ans == JOptionPane.OK_OPTION) {// ������������
							Login loginPage = (Login) page[ShopMain.LOGIN];
							MyPage mypage = (MyPage) page[ShopMain.MYPAGE];
							mypage.setPage();
							if (mypage.getOrder() != null) {
								mypage.getOrder().setPage();
								if (mypage.getOrder().getOrderDetail() != null) {
									mypage.getOrder().getOrderDetail().setPage();
								}
							}

							loginPage.logout();
						}
					} else {
						showPage(4);
					}

				}
			});
		}

	}

	// ������ �������� �Ⱥ����� ������ �����ϴ� �޼���
	public void showPage(int showIndex) { // �Ű������� �����ְ� ���������� �ѹ� �ޱ�
		for (int i = 0; i < page.length; i++) { // ����������� �������
			if (i == showIndex) {
				page[i].setVisible(true); // ���̰� �� ������
			} else {
				page[i].setVisible(false); // �Ⱥ��̰� �� ������
			}

		}
	}

	public Page[] getPage() {
		return page;
	}

	public DBManager getDbManager() {
		return dbManager;
	}

	public Connection getCon() {
		return con;
	}

	public boolean isHasSession() {
		return hasSession;
	}

	public void setHasSession(boolean hasSession) {
		this.hasSession = hasSession;
	}

	// ������ ����Ʈ�� ������ ����Ʈ�� �����ϴ� �޼���
	public void addRemoveContent(Component removeObj, Component addObj) {
		this.remove(removeObj);
		this.add(addObj);
		((JPanel) addObj).updateUI();

	}

	public static void main(String[] args) {
		new ShopMain();
	}

}