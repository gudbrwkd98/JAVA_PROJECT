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
	JPanel bt_container;  //��ư�� ������ �����̳�
	JButton bt_pay; //�����ܰ�� ����
	JButton bt_del; //��ٱ��� ����
	
	//��ٱ��� ������ �÷��� �����ӿ� ��ü�� ���� 
	HashMap<Integer,CartVO> cartList;
	JPanel p_content ; //cart�� ���� ������ ���� �����۵��� ���� �����̳ʸ� �غ��Ѵ� 
	int total;
	JScrollPane scrollPane;
	JPanel contentPane;
	int totalH;
	public Cart(ShopMain shopMain) {
		super(shopMain);
		
		cartList = new HashMap<Integer,CartVO>();
		
		bt_container = new JPanel();
		bt_pay = new JButton("�����ϱ�");
		bt_del = new JButton("��ٱ��� ����");
		
		//��Ÿ��
		bt_container.setPreferredSize(new Dimension(ShopMain.WIDTH,100));
		bt_container.setBackground(Color.cyan);
		
		getCartList();
		
		bt_container.add(bt_pay);
		bt_container.add(bt_del);
		add(bt_container);
		
		bt_pay.addActionListener((e)->{
			if(shopMain.isHasSession() == false) {
				JOptionPane.showMessageDialog(Cart.this,"�α����� ���ּ���");
			}else{
				if(JOptionPane.showConfirmDialog(Cart.this, "���� �Ͻðڽ��ϱ�?") == JOptionPane.OK_OPTION) { 
					if(orderProduct() == 0) {
						JOptionPane.showMessageDialog(this, "��ٱ��ϸ� ä���ּ���");
					}else {
						JOptionPane.showMessageDialog(this, "�� ���� �ݾ��� : " + total + " �� �Դϴ� \n������ �Ϸ� �Ǿ����ϴ�");
					}
				}else {
					
				}
			}
		});
		
		bt_del.addActionListener((e)->{
			if(JOptionPane.showConfirmDialog(Cart.this, "��ٱ��ϸ� ���ðڽ��ϱ�?") == JOptionPane.OK_OPTION) {
			cartList.clear();
			getCartList();
			}else {
				
			}
		});
		
	}
	//��ٱ��Ͽ� �ֱ�
	public void addCart(CartVO vo) { //��ǰ1���� ��ٱ��Ͽ� �߰��ϱ�
		cartList.put(vo.getProduct_id(), vo);  //key�� ���� ����
	}
	//��ٱ��ϻ����ϱ�
	public void removeCart(int product_id) {//��ǰ1���� �����ϱ�
		cartList.remove(product_id);
	}
	
	public void updateCart(CartVO vo) {//��ǰ1���� ���� ����
//		cartList.get(product_id).setEa(Integer.parseInt(item.t_ea.getText()));
		// �ؽøʿ� ����ִ� ��ü �� �ش� ��ü�� ã�Ƴ���, vo��ü!!!
		CartVO obj=cartList.get(vo.getProduct_id());//�˻�!!
		obj=vo;//���� �ؽø��� ������ �մ� vo�� ã�Ƴ��� �ּ� ���� 
	}
	
	//��ٱ��� ����
	public void removeAll() { //��� ��ǰ ����
		
	}
	
	
	
	//��ٱ��� ��� ����ϱ�
	public void getCartList() {
		Set<Integer> set = cartList.keySet(); //Ű���� set���� ��ȯ�޴´�..�� ���� �ѹ��� �Ϸķ� �þ�� ���� �ƴ϶�,  set���� ����
		//key�� �����;� ��
		if(p_content != null) {
			this.remove(p_content); //����
			this.remove(contentPane);
			this.remove(scrollPane);
			this.revalidate();
			this.updateUI();
			this.repaint();
		}
		//�������� ���� ����
		p_content  = new  JPanel();
		p_content.setPreferredSize(new Dimension(ShopMain.WIDTH-350,600));
		p_content.setBackground(Color.WHITE);
		total = 0 ;
		totalH = 0 ;
		this.removeAll();
		Iterator<Integer> it = set.iterator();
		
		while(it.hasNext()) {//��Ұ� �ִ� ����..
		int key=it.next();//��Ҹ� ����
		CartVO vo=cartList.get(key);
		//�������� ǥ���ϴ� CartItem�� CartVO�� ������ ä������!!
		CartItem item = new CartItem(vo);
		item.bt_del.addActionListener((e)->{
			if(JOptionPane.showConfirmDialog(Cart.this, "�����Ͻðڽ��ϱ�?") == JOptionPane.OK_OPTION) {
			removeCart(vo.getProduct_id());
			getCartList();
			}
		});
		
		item.bt_update.addActionListener((e)->{
			if(JOptionPane.showConfirmDialog(Cart.this, "��ٱ��ϸ� �����Ͻðھ��??") == JOptionPane.OK_OPTION) {
				int ea = Integer.parseInt(item.t_ea.getText()); //������ �������ϱ�!! 
				vo.setEa(ea);//����� ������ vo�� �ݿ��� �Ŀ� ����..
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
	
	
	//�����ϱ�
	public int orderProduct() {
		Login login =  (Login) getShopMain().getPage()[ShopMain.LOGIN];//�α��� �������� ����
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
