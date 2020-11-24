package com.swingmall.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.swingmall.board.QnA;
import com.swingmall.home.Home;
import com.swingmall.member.Login;
import com.swingmall.member.MyPage;
import com.swingmall.member.RegistForm;
import com.swingmall.member.ShopMember;
import com.swingmall.product.Product;
import com.swingmall.product.ProductDetail;
import com.swingmall.cart.Cart;
import com.swingmall.util.db.DBManager;

public class ShopMain extends JFrame {
	// 상수 선언
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 900;

	// 최상위 페이지들
	public static final int HOME = 0;
	public static final int PRODUCT = 1;
	public static final int QNA = 2;
	public static final int MYPAGE = 3;
	public static final int LOGIN = 4;

	// 하위페이지
	public static final int PRODUCT_DETAIL = 5; //상품 디테일 상품 하위페이지
	public static final int MEMBER_REGIST = 6; //회원가입  로그인 하위페이지
	public static final int Cart = 7;

	JPanel user_container; // 관리자,사용자 화면을 구분지을수있는 컨테이너
	JPanel p_content;// 여기에 모든 페이지가 미리 붙어져있을것임
	// 추후 showPage메서드로 보일지 여부 설정..

	JPanel p_navi; // 웹사이트의 주 메뉴를 포함할 컨테이너 패널
	String[] navi_title = { "Home", "Product", "QnA", "MyPage", "Login" };
	public JButton[] navi = new JButton[navi_title.length]; // [][][][][] 배열생성

	// 페이지 구성
	Page[] page = new Page[8]; // 최상위 페이지들
	ProductDetail productDetail; // 상세 페이지

	JLabel la_footer; // 윈도우 하단의 카피라이트 영역
	private DBManager dbManager;
	private Connection con;
	
	//로그인 상태인지 여부를 알수있는 변수
	private boolean hasSession = false;
 



	public ShopMain() {
		dbManager = new DBManager();
		user_container = new JPanel();
		p_content = new JPanel();
		p_navi = new JPanel(); // 버튼들을 담을 패널
		la_footer = new JLabel("SwingMall All rights reserved", SwingConstants.CENTER);

		
		con = dbManager.connect();
		if (con == null) {
			JOptionPane.showMessageDialog(this, "접속불가");
			System.exit(0);
		} else {
			this.setTitle("SwingMall에 오신걸 환영합니다");
		}

		// 메인네비게이션 생성
		for (int i = 0; i < navi.length; i++) {
			navi[i] = new JButton(navi_title[i]);
			p_navi.add(navi[i]);
		}

		// 페이지 구성
		page[HOME] = new Home(this);
		page[PRODUCT] = new Product(this);
		page[QNA] = new QnA(this);
		page[MYPAGE] = new MyPage(this);
		page[LOGIN] = new Login(this);
		page[PRODUCT_DETAIL] = new ProductDetail(this); // 하위페이지 구성
		page[MEMBER_REGIST] = new RegistForm(this); // 하위페이지 구성
		page[Cart] = new Cart(this);

		
		// 스타일 적용
		user_container.setPreferredSize(new Dimension(WIDTH, HEIGHT - 50));
		user_container.setBackground(Color.white);
		la_footer.setPreferredSize(new Dimension(WIDTH, 50));
		la_footer.setFont(new Font("Arial Black", Font.BOLD, 19));

		// 조립
		user_container.setLayout(new BorderLayout());
		user_container.add(p_navi, BorderLayout.NORTH);

		// 페이지 붙이기
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

		// 네비게이션버튼과 리스너 연결
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
					//Mypage는 무조건 보여줘서는 안되고 로그인한 사람에게만 보여줘야함 
					if(hasSession == false) {
						JOptionPane.showMessageDialog(ShopMain.this,"로그인을 해주세요");
					}else{
					showPage(3);
					}
				} else if (obj == navi[4]) {
					//로그인 / 로그아웃 구분
					if(hasSession) {
					int ans = JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?");
					if(ans == JOptionPane.OK_OPTION) {//예를누른것임
						Login loginPage = (Login)page[ShopMain.LOGIN];
						loginPage.logout();
					}
					}else {
						showPage(4);
					}
					
				}
			});
		}

	}

	// 보여질 페이지와 안보여질 페이지 설정하는 메서드
	public void showPage(int showIndex) { // 매개변수로 보여주고 싶은페이지 넘버 받기
		for (int i = 0; i < page.length; i++) { // 모든페이지를 대상으로
			if (i == showIndex) {
				page[i].setVisible(true); // 보이게 할 페이지
			} else {
				page[i].setVisible(false); // 안보이게 할 페이지
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
	

 

	// 보여질 컨텐트와 가려질 컨텐트를 제어하는 메서드
	public void addRemoveContent(Component removeObj, Component addObj) {
		this.remove(removeObj);
		this.add(addObj);
		((JPanel) addObj).updateUI();

	}
	
	

	public static void main(String[] args) {
		new ShopMain();
	}

}
