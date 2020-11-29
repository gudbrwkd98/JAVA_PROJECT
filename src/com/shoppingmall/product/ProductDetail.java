package com.shoppingmall.product;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.ScrollPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import com.shoppingmall.cart.Cart;
import com.shoppingmall.cart.CartVO;
import com.shoppingmall.main.Page;
import com.shoppingmall.main.ShopMain;
import com.shoppingmall.product.ProductVO;

public class ProductDetail extends Page{
	
	public JPanel p_content; //상세 내용을 담게될 패널
	public JPanel p_can; //그림이 오게될 영역
	JPanel p_option;//옵션 선택영역
	JLabel la_product_name; //상품명 라벨
	JLabel la_brand ; //브랜드 라벨
	JLabel la_price;//상품가격
	JTextArea la_detail;//상품 디테일
	JScrollPane scroll;
	
	JLabel ch_c;
	JLabel ch_s;
	Choice ch_color; //색상옵션
	Choice ch_size; //사이즈 옵션
	
	JButton bt_buy; //구매하기
	JButton bt_cart; //장바구니

	private ProductVO vo;
	private Image img;
	
	
	public ProductDetail(ShopMain shopMain) {
		super(shopMain);
		this.vo = vo;
		this.img = img;
		
		//생성
		p_content = new JPanel(); 
		p_can = new JPanel() {
			@Override
			public void paint(Graphics g) {
				//g.setColor(Color.red);
				//g.fillRect(0, 0, ShopMain.WIDTH-40, ShopMain.HEIGHT-100);
				g.drawImage(img, 125,150, p_can);
			}
		}; 
		p_option = new JPanel();
		la_brand = new JLabel();
		la_product_name = new JLabel(); 
		la_price = new JLabel();
		la_detail = new JTextArea();
		la_detail.setEditable(false);
		scroll = new JScrollPane(la_detail);
		
		
		ch_c = new JLabel("색상");
		ch_s = new JLabel("사이즈");
		
		
		ch_color = new Choice();
		ch_size = new Choice(); 

		
		ch_size.add("------[필수] SIZE------");

		
		 bt_buy = new JButton("구매");
		 bt_cart = new JButton("장바구니");
		 
		 p_content.setLayout(new GridLayout(1,2));
		 //스타일
		 p_content.setPreferredSize(new Dimension(ShopMain.WIDTH-100,ShopMain.HEIGHT-300));
		 p_content.setBackground(Color.WHITE);
		 p_content.setBorder(new LineBorder(new Color(15,76,129), 2));
		 p_option.setBackground(Color.WHITE); 
		 Dimension d = new Dimension(ShopMain.WIDTH/3,30);
		 la_brand.setPreferredSize(d);
		 la_product_name.setPreferredSize(d);
		 la_price.setPreferredSize(d);
		 scroll.setPreferredSize(new Dimension(ShopMain.WIDTH/3,100));
		 la_detail.setLineWrap(true);
		 la_detail.setWrapStyleWord(true);
		 ch_size.setPreferredSize(new Dimension(ShopMain.WIDTH/3,30));
		 bt_buy.setPreferredSize(new  Dimension(200,30));
		 bt_cart.setPreferredSize(new  Dimension(200,30));
		 bt_buy.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		 bt_buy.setBackground(new Color(15,76,129));
		 bt_buy.setForeground(Color.WHITE);		
		 
		 bt_cart.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		 bt_cart.setBackground(new Color(15,76,129));
		 bt_cart.setForeground(Color.WHITE);
		 
		 //조립
		 p_option.add(la_product_name);
		 p_option.add(la_brand);
		 p_option.add(la_price);
		 p_option.add(scroll);
		 p_option.add(ch_size);
		 p_option.add(bt_buy);
		 p_option.add(bt_cart);
		 
		 p_content.add(p_can);
		 p_content.add(p_option);
		 
		 this.add(p_content);
		 
		 bt_buy.addActionListener((e)->{
			 if(ch_size.getSelectedIndex() == 0) {
				 JOptionPane.showMessageDialog(this, "사이즈를 선택해주세요!");
			 }else {
				 registCart();
				 ch_size.select(0);
			 getShopMain().showPage(ShopMain.Cart);
			 }
		});
		 
		 bt_cart.addActionListener((e)->{
			 
			 if(ch_color.getSelectedIndex() == 0 || ch_size.getSelectedIndex() == 0) {
				 JOptionPane.showMessageDialog(this, "사이즈를 선택해주세요!");
			 }else {
				 registCart();
				 ch_size.select(0);
				 int ans = JOptionPane.showConfirmDialog(ProductDetail.this, "장바구니로 이동하시게습니까?");
				 if(ans == JOptionPane.OK_OPTION) {
				 getShopMain().showPage(ShopMain.Cart);
				 }
			 }
			 

		 });
		 

		 
	}
	//상세페이지가 보여질떄 데이터를 채워넣는 메서드 (생성자에서 하면 제한이 많다)
	public void init(ProductVO vo,Image img) {
		this.vo = vo; //멤버변수에 현재 보고있는 상품 vo 를 주입..
		la_brand.setText("브랜드 : " +vo.getBrand()); //브랜드 채워넣기
		la_product_name.setText("상품명 : " +vo.getProduct_name());
		la_price.setText("Price : " + Integer.toString( vo.getPrice()) + "  원");
		la_detail.setText(vo.getDetail());
		ch_size.removeAll();
		ch_size.add("------[필수] SIZE------");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select t.name,s.name as subname from subcategory s,topcategory t where s.subcategory_id = "+vo.getSubcategory_id() +" and s.topcategory_id = t.topcategory_id";
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String name = rs.getString("name");
				if(name.equals("상의") || name.equals("하의")) {
				ch_size.add("S");
				ch_size.add("M");
				ch_size.add("L");
				}else if (name.equals("액세서리")){
					if(rs.getString("subname").equals("반지")) {
						for (int i = 7; i <= 18; i++) {
							ch_size.add(Integer.toString(i) + "호수");
						}
					}else if(rs.getString("subname").equals("목걸이")) {
						for (int i = 18; i <= 30; i++) {
							if(i%2 == 0 ) {
							ch_size.add(Integer.toString(i) + "inch");
							}
						}
					}else if(rs.getString("subname").equals("귀걸이") || rs.getString("subname").equals("팔찌")){
						ch_size.add("S");
						ch_size.add("M");
						ch_size.add("L");
					}
				}else if(name.equals("신발")) {
					for (int i = 240; i < 290; i++) {
						if(i%5 == 0 ) {
							ch_size.add(Integer.toString(i) + "mm");
						}
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getShopMain().getDbManager().close(pstmt,rs);
		}
		
		
		
		this.img = img;
		this.img = this.img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		
	}
	//장바구니에 등록(메모리상에 올려놓으면서 보여주기)
	public void registCart() {
		Cart cartPage = (Cart) getShopMain().getPage()[ShopMain.Cart];//장바구니 페이지에 접근
		CartVO cartVO = new CartVO();
		cartVO.setProduct_id(vo.getProduct_id()); //현재보고있는 상품을 이용하여 cartVO에 채우기
		cartVO.setBrand(vo.getBrand());
		cartVO.setProduct_name(vo.getProduct_name());
		cartVO.setPrice(vo.getPrice());
		cartVO.setFilename(vo.getFilename());
		//선택한 컬러
		//cartVO.setColor(ch_color.getSelectedItem());
		
		//선택한 사이즈
		cartVO.setSize(ch_size.getSelectedItem());		
		cartVO.setEa(1); //장바구니에 담을때는 기본이 1개임 ;
		
		
		
		cartPage.addCart(cartVO);//장바구니에 상품1건추가하기
		 cartPage.getCartList();//장바구니 목록 구성하기
	}
	
	
	public ProductVO getVo() {
		return vo;
	}
	public void setVo(ProductVO vo) {
		this.vo = vo;
	}
	public Image getImg() {
		return img;
	}
	public void setImg(Image img) {
		this.img = img;
	}
	
}
