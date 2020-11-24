package com.swingmall.cart;

import java.awt.Color;
import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.swingmall.main.Page;
import com.swingmall.main.ShopMain;
import com.swingmall.member.Login;
import com.swingmall.member.ShopMember;

public class Cart extends Page{
	JPanel bt_container;  //버튼을 묶어줄 컨테이너
	JButton bt_pay; //결제단계로 가기
	JButton bt_del; //장바구니 비우기
	
	//장바구니 역할을 컬렉션 프레임웍 객체를 선언 
	HashMap<Integer,CartVO> cartList;
	JPanel p_content ; //cart에 직접 붙이지 말고 아이템들을 붙일 컨테이너를 준비한다 
	int total;
	JScrollPane scrollPane;
	JPanel contentPane;
	int totalH;
	public Cart(ShopMain shopMain) {
		super(shopMain);
		
		cartList = new HashMap<Integer,CartVO>();
		
		bt_container = new JPanel();
		bt_pay = new JButton("결제하기");
		bt_del = new JButton("장바구니 비우기");
		
		//스타일
		bt_container.setPreferredSize(new Dimension(ShopMain.WIDTH,100));
		bt_container.setBackground(Color.cyan);
		
		getCartList();
		
		bt_container.add(bt_pay);
		bt_container.add(bt_del);
		add(bt_container);
		
		bt_pay.addActionListener((e)->{
			if(shopMain.isHasSession() == false) {
				JOptionPane.showMessageDialog(Cart.this,"로그인을 해주세요");
			}else{
				if(JOptionPane.showConfirmDialog(Cart.this, "결제 하시겠습니까?") == JOptionPane.OK_OPTION) { 
					if(orderProduct() == 0) {
						JOptionPane.showMessageDialog(this, "장바구니를 채워주세요");
					}else {
						JOptionPane.showMessageDialog(this, "총 결제 금액은 : " + total + " 원 입니다 \n결제가 완료 되었습니다");
					}
				}else {
					
				}
			}
		});
		
		bt_del.addActionListener((e)->{
			if(JOptionPane.showConfirmDialog(Cart.this, "장바구니를 비우시겠습니까?") == JOptionPane.OK_OPTION) {
			cartList.clear();
			getCartList();
			}else {
				
			}
		});
		
	}
	//장바구니에 넣기
	public void addCart(CartVO vo) { //상품1건을 장바구니에 추가하기
		cartList.put(vo.getProduct_id(), vo);  //key와 값을 저장
	}
	//장바구니삭제하기
	public void removeCart(int product_id) {//상품1건을 삭제하기
		cartList.remove(product_id);
	}
	
	public void updateCart(CartVO vo) {//상품1건의 수량 변경
//		cartList.get(product_id).setEa(Integer.parseInt(item.t_ea.getText()));
		// 해시맵에 들어있는 객체 중 해당 객체를 찾아내어, vo교체!!!
		CartVO obj=cartList.get(vo.getProduct_id());//검색!!
		obj=vo;//기존 해시맵이 가지고 잇던 vo를 찾아내어 주소 변경 
	}
	
	//장바구니 비우기
	public void removeAll() { //모든 상품 비우기
		
	}
	
	
	
	//장바구니 목록 출력하기
	public void getCartList() {
		Set<Integer> set = cartList.keySet(); //키들을 set으로 반환받는다..즉 맵은 한번에 일렬로 늘어서는 것이 아니라,  set으로 먼저
		//key를 가져와야 함
		if(p_content != null) {
			this.remove(p_content); //제거
			this.remove(contentPane);
			this.remove(scrollPane);
			this.revalidate();
			this.updateUI();
			this.repaint();
		}
		//동적으로 새로 생성
		p_content  = new  JPanel();
		p_content.setPreferredSize(new Dimension(ShopMain.WIDTH-350,600));
		p_content.setBackground(Color.WHITE);
		total = 0 ;
		totalH = 0 ;
		this.removeAll();
		Iterator<Integer> it = set.iterator();
		
		while(it.hasNext()) {//요소가 있는 동안..
		int key=it.next();//요소를 추출
		CartVO vo=cartList.get(key);
		//디자인을 표현하는 CartItem에 CartVO의 정보를 채워넣자!!
		CartItem item = new CartItem(vo);
		item.bt_del.addActionListener((e)->{
			if(JOptionPane.showConfirmDialog(Cart.this, "삭제하시겠습니까?") == JOptionPane.OK_OPTION) {
			removeCart(vo.getProduct_id());
			getCartList();
			}
		});
		
		item.bt_update.addActionListener((e)->{
			if(JOptionPane.showConfirmDialog(Cart.this, "장바구니를 수정하시겠어요??") == JOptionPane.OK_OPTION) {
				int ea = Integer.parseInt(item.t_ea.getText()); //수정한 갯수구하기!! 
				vo.setEa(ea);//변경된 갯수를 vo에 반영한 후에 전달..
				updateCart(vo);
				getCartList();
			}
		});
		p_content.add(item);
		total += Integer.parseInt(item.la_price.getText());
		totalH += 150;
	 
		}
		p_content.setPreferredSize(new Dimension(ShopMain.WIDTH-350,totalH));
        scrollPane = new JScrollPane(p_content);

        scrollPane.setBounds(0, 0, ShopMain.WIDTH-350,500);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(ShopMain.WIDTH-350,500));
        contentPane.add(scrollPane);

        
		this.add(contentPane);
		this.updateUI();
		
	}
	
	
	//결제하기
	public int orderProduct() {
		Login login =  (Login) getShopMain().getPage()[ShopMain.LOGIN];//로그인 페이지에 접근
		PreparedStatement pstmt = null;
		String sql = "";
		
		
		int result = 0;

		
		sql = "insert into receipt(receipt_id,paid_p,member_id) values (seq_receipt.nextval,?,?)";
		
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, total);
			pstmt.setInt(2, login.getVo().getMember_id());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Set<Integer> set = cartList.keySet();
		
		Iterator<Integer> it = set.iterator();
		
		while(it.hasNext()) {
			int key = it.next();
			CartVO vo = cartList.get(key);
			
			sql = "insert into product_order( order_id,product_id,num_product,receipt_id) values (seq_product_order.nextval,?,?,seq_receipt.CURRVAL)";
			
			try {
				pstmt = getShopMain().getCon().prepareStatement(sql);
				pstmt.setInt(1, vo.getProduct_id());
				pstmt.setInt(2, vo.getEa());
				result = pstmt.executeUpdate();
				
 
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				getShopMain().getDbManager().close(pstmt);
			}
		}
		

		
		return result;
		
		
	}
	
}
