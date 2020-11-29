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
	
	public JPanel p_content; //�� ������ ��Ե� �г�
	public JPanel p_can; //�׸��� ���Ե� ����
	JPanel p_option;//�ɼ� ���ÿ���
	JLabel la_product_name; //��ǰ�� ��
	JLabel la_brand ; //�귣�� ��
	JLabel la_price;//��ǰ����
	JTextArea la_detail;//��ǰ ������
	JScrollPane scroll;
	
	JLabel ch_c;
	JLabel ch_s;
	Choice ch_color; //����ɼ�
	Choice ch_size; //������ �ɼ�
	
	JButton bt_buy; //�����ϱ�
	JButton bt_cart; //��ٱ���

	private ProductVO vo;
	private Image img;
	
	
	public ProductDetail(ShopMain shopMain) {
		super(shopMain);
		this.vo = vo;
		this.img = img;
		
		//����
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
		
		
		ch_c = new JLabel("����");
		ch_s = new JLabel("������");
		
		
		ch_color = new Choice();
		ch_size = new Choice(); 

		
		ch_size.add("------[�ʼ�] SIZE------");

		
		 bt_buy = new JButton("����");
		 bt_cart = new JButton("��ٱ���");
		 
		 p_content.setLayout(new GridLayout(1,2));
		 //��Ÿ��
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
		 bt_buy.setFont(new Font("���� ���", Font.PLAIN, 12));
		 bt_buy.setBackground(new Color(15,76,129));
		 bt_buy.setForeground(Color.WHITE);		
		 
		 bt_cart.setFont(new Font("���� ���", Font.PLAIN, 12));
		 bt_cart.setBackground(new Color(15,76,129));
		 bt_cart.setForeground(Color.WHITE);
		 
		 //����
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
				 JOptionPane.showMessageDialog(this, "����� �������ּ���!");
			 }else {
				 registCart();
				 ch_size.select(0);
			 getShopMain().showPage(ShopMain.Cart);
			 }
		});
		 
		 bt_cart.addActionListener((e)->{
			 
			 if(ch_color.getSelectedIndex() == 0 || ch_size.getSelectedIndex() == 0) {
				 JOptionPane.showMessageDialog(this, "����� �������ּ���!");
			 }else {
				 registCart();
				 ch_size.select(0);
				 int ans = JOptionPane.showConfirmDialog(ProductDetail.this, "��ٱ��Ϸ� �̵��ϽðԽ��ϱ�?");
				 if(ans == JOptionPane.OK_OPTION) {
				 getShopMain().showPage(ShopMain.Cart);
				 }
			 }
			 

		 });
		 

		 
	}
	//���������� �������� �����͸� ä���ִ� �޼��� (�����ڿ��� �ϸ� ������ ����)
	public void init(ProductVO vo,Image img) {
		this.vo = vo; //��������� ���� �����ִ� ��ǰ vo �� ����..
		la_brand.setText("�귣�� : " +vo.getBrand()); //�귣�� ä���ֱ�
		la_product_name.setText("��ǰ�� : " +vo.getProduct_name());
		la_price.setText("Price : " + Integer.toString( vo.getPrice()) + "  ��");
		la_detail.setText(vo.getDetail());
		ch_size.removeAll();
		ch_size.add("------[�ʼ�] SIZE------");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select t.name,s.name as subname from subcategory s,topcategory t where s.subcategory_id = "+vo.getSubcategory_id() +" and s.topcategory_id = t.topcategory_id";
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String name = rs.getString("name");
				if(name.equals("����") || name.equals("����")) {
				ch_size.add("S");
				ch_size.add("M");
				ch_size.add("L");
				}else if (name.equals("�׼�����")){
					if(rs.getString("subname").equals("����")) {
						for (int i = 7; i <= 18; i++) {
							ch_size.add(Integer.toString(i) + "ȣ��");
						}
					}else if(rs.getString("subname").equals("�����")) {
						for (int i = 18; i <= 30; i++) {
							if(i%2 == 0 ) {
							ch_size.add(Integer.toString(i) + "inch");
							}
						}
					}else if(rs.getString("subname").equals("�Ͱ���") || rs.getString("subname").equals("����")){
						ch_size.add("S");
						ch_size.add("M");
						ch_size.add("L");
					}
				}else if(name.equals("�Ź�")) {
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
	//��ٱ��Ͽ� ���(�޸𸮻� �÷������鼭 �����ֱ�)
	public void registCart() {
		Cart cartPage = (Cart) getShopMain().getPage()[ShopMain.Cart];//��ٱ��� �������� ����
		CartVO cartVO = new CartVO();
		cartVO.setProduct_id(vo.getProduct_id()); //���纸���ִ� ��ǰ�� �̿��Ͽ� cartVO�� ä���
		cartVO.setBrand(vo.getBrand());
		cartVO.setProduct_name(vo.getProduct_name());
		cartVO.setPrice(vo.getPrice());
		cartVO.setFilename(vo.getFilename());
		//������ �÷�
		//cartVO.setColor(ch_color.getSelectedItem());
		
		//������ ������
		cartVO.setSize(ch_size.getSelectedItem());		
		cartVO.setEa(1); //��ٱ��Ͽ� �������� �⺻�� 1���� ;
		
		
		
		cartPage.addCart(cartVO);//��ٱ��Ͽ� ��ǰ1���߰��ϱ�
		 cartPage.getCartList();//��ٱ��� ��� �����ϱ�
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
