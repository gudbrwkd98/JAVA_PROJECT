package com.swingmall.product;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.swingmall.product.ProductVO;
import com.swingmall.cart.Cart;
import com.swingmall.cart.CartVO;
import com.swingmall.main.Page;
import com.swingmall.main.ShopMain;

public class ProductDetail extends Page{
	
	public JPanel p_content; //�� ������ ��Ե� �г�
	public JPanel p_can; //�׸��� ���Ե� ����
	JPanel p_option;//�ɼ� ���ÿ���
	JLabel la_product_name; //��ǰ�� ��
	JLabel la_brand ; //�귣�� ��
	JLabel la_price;//��ǰ����
	
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
		
		ch_c = new JLabel("����");
		ch_s = new JLabel("������");
		
		
		ch_color = new Choice();
		ch_size = new Choice(); 
		
		//����ä���
		ch_color.add("------[�ʼ�] COLOR------");
		ch_color.add("red");
		ch_color.add("black");
		ch_color.add("white");
		
		
		ch_size.add("------[�ʼ�] SIZE------");
		ch_size.add("S");
		ch_size.add("M");
		ch_size.add("L");
		
		 bt_buy = new JButton("����");
		 bt_cart = new JButton("��ٱ���");
		 
		 p_content.setLayout(new GridLayout(1,2));
		 //��Ÿ��
		 p_content.setPreferredSize(new Dimension(ShopMain.WIDTH-100,ShopMain.HEIGHT-100));
		 p_option.setBackground(Color.pink); 
		 Dimension d = new Dimension(ShopMain.WIDTH/3,30);
		 la_brand.setPreferredSize(d);
		 la_product_name.setPreferredSize(d);
		 la_price.setPreferredSize(d);
		 ch_color.setPreferredSize(new Dimension(ShopMain.WIDTH/6,30));
		 ch_size.setPreferredSize(new Dimension(ShopMain.WIDTH/6,30));
		 bt_buy.setPreferredSize(new  Dimension(200,30));
		 bt_cart.setPreferredSize(new  Dimension(200,30));
		 //����
		 p_option.add(la_product_name);
		 p_option.add(la_brand);
		 p_option.add(la_price);
		 p_option.add(ch_color);
		 p_option.add(ch_size);
		 p_option.add(bt_buy);
		 p_option.add(bt_cart);
		 
		 p_content.add(p_can);
		 p_content.add(p_option);
		 
		 this.add(p_content);
		 
		 bt_buy.addActionListener((e)->{
			 if(ch_color.getSelectedIndex() == 0 || ch_size.getSelectedIndex() == 0) {
				 JOptionPane.showMessageDialog(this, "���� �� ����� �������ּ���!");
			 }else {
				 registCart();
			 getShopMain().showPage(ShopMain.Cart);
			 }
		});
		 
		 bt_cart.addActionListener((e)->{
			 
			 if(ch_color.getSelectedIndex() == 0 || ch_size.getSelectedIndex() == 0) {
				 JOptionPane.showMessageDialog(this, "���� �� ����� �������ּ���!");
			 }else {
				 registCart();
				 
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
		la_price.setText(Integer.toString(vo.getPrice()) + "  won");
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
		//������ ������
		cartVO.setColor(ch_color.getSelectedItem());
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
