package com.shoppingmall.member;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.shoppingmall.admin.member.MemberVO;
import com.shoppingmall.cart.Cart;
import com.shoppingmall.cart.CartItem;
import com.shoppingmall.cart.CartVO;
import com.shoppingmall.main.ShopMain;

public class MyOrderDetail extends JPanel {
	JPanel p_member; //member ������ ���� �����̳� 
	JPanel p_content; // cart�� ���� ������ ���� �����۵��� ���� �����̳ʸ� �غ��Ѵ�
	int total;
	JScrollPane scrollPane;
	JPanel contentPane;
	int totalH;
	MyOrder order;
	JPanel p_button;
	JButton bt_back;
	JButton bt_save;
	JLabel l_name,l_phone,l_email,l_address,l_status;

	
	
	public MyOrderDetail(MyOrder order, String i,String member_id,String status) {
		this.order = order;
		
		getProductList(i);

		p_button = new JPanel();
		p_member = new JPanel();
		
		l_name = new JLabel();
		l_phone = new JLabel();
		l_email = new JLabel();
		l_address = new JLabel();
		l_status = new JLabel();
		
 

		
		
		//��Ÿ��
		bt_back = new JButton("�������");
		bt_back.setFont(new Font("���� ���", Font.PLAIN, 12));
		bt_back.setBackground(new Color(15, 76, 129));
		bt_back.setForeground(Color.WHITE);
		
		bt_save = new JButton("�����ϱ�");
		bt_save.setFont(new Font("���� ���", Font.PLAIN, 12));
		bt_save.setBackground(new Color(15, 76, 129));
		bt_save.setForeground(Color.WHITE);
		this.setPreferredSize(new Dimension(ShopMain.WIDTH,ShopMain.HEIGHT-200));
		
		
		p_member.setLayout(new FlowLayout());
		p_member.setPreferredSize(new Dimension(400,400));
		
		l_name.setPreferredSize(new Dimension(400,25));
		l_phone.setPreferredSize(new Dimension(400,25));
		l_email.setPreferredSize(new Dimension(400,25));
		l_address.setPreferredSize(new Dimension(400,25));
		l_status.setPreferredSize(new Dimension(400,25));
		
		
		p_member.add(l_name);
		p_member.add(l_phone);
		p_member.add(l_email);
		p_member.add(l_address);
		p_member.add(l_status);
		p_member.add(bt_back);


		getMemberInfo(member_id,i);
		l_status.setText("��� ��Ȳ  : "  + status);
		
		bt_back.addActionListener((e) -> {
			order.addRemoveContent(this, order.p_center);
		});
		
 

	}
	
	public void setPage() {
		order.addRemoveContent(this, order.p_center);
	}
	
	public void getMemberInfo(String member_id, String i) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select s.name,s.email,s.phone,r.address from shop_member s,receipt r  where s.member_id = " +  member_id +  " and r.receipt_id = " + i + "  and r.member_id = s.member_id";
		
		try {
			pstmt = order.mypage.getShopMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
//				MemberVO vo = new MemberVO();
//				vo.setName(rs.getString("name"));
//				vo.setAddress(rs.getString("address"));
//				vo.setEmail(rs.getString("email"));
//				vo.setPhone(rs.getString("phone"));
				
				l_name.setText("�̸� : " +rs.getString("name"));
				l_email.setText("�̸��� : " +rs.getString("email"));
				l_phone.setText("��ȭ��ȣ : " + rs.getString("phone"));
				l_address.setText("�ּ� : " + rs.getString("address"));
			}
			this.add(p_member);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			order.mypage.getShopMain().getDbManager().close(pstmt,rs);
		}
		
		
		
		
		
	}

	public void getProductList(String i) {
		if (p_content != null) {
			this.remove(p_content); // ����
			this.remove(contentPane);
			this.remove(scrollPane);
			this.revalidate();
			this.updateUI();
			this.repaint();
		}
		// �������� ���� ����
		p_content = new JPanel();
		p_content.setPreferredSize(new Dimension(ShopMain.WIDTH - 100, 600));
		p_content.setBackground(Color.WHITE);
		total = 0;
		totalH = 0;
		this.removeAll();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select p.product_id, p.subcategory_id,p.product_name,p.brand,p.price, p.filename,p.detail,o.product_size as product_size,o.num_product from product_order o , receipt r,product p where o.receipt_id = "
				+ i + " and o.receipt_id = r.receipt_id and o.product_id = p.product_id";

		try {
			pstmt = order.mypage.getShopMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MyOrderDetailVO vo = new MyOrderDetailVO();

				vo.setProduct_id(rs.getInt("product_id"));
				vo.setProduct_name(rs.getString("product_name"));
				vo.setBrand(rs.getString("brand"));
				vo.setPrice(rs.getInt("price"));
				vo.setFilename(rs.getString("filename"));
				vo.setDetail(rs.getString("detail"));
				vo.setNumProduct(rs.getInt("num_product"));
				vo.setSize(rs.getString("product_size"));

				// �������� ǥ���ϴ� CartItem�� CartVO�� ������ ä������!!
				MyOrderItem item = new MyOrderItem(vo);

				p_content.add(item);
				// total += Integer.parseInt(item.la_price.getText());
				totalH += 150;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			order.mypage.getShopMain().getDbManager().close(pstmt, rs);
		}

		p_content.setPreferredSize(new Dimension(ShopMain.WIDTH - 100, totalH));
		scrollPane = new JScrollPane(p_content);

		scrollPane.setBounds(0, 0, ShopMain.WIDTH - 100, 500);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane = new JPanel(null);
		contentPane.setPreferredSize(new Dimension(ShopMain.WIDTH - 100, 500));
		contentPane.add(scrollPane);

		this.add(contentPane);

		this.updateUI();

	}
	
	
}
