package com.shoppingmall.member;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.shoppingmall.main.ShopMain;

import common.image.ImageUtil;

public class MyOrderItem extends JPanel{
	JPanel p_can; //상품 이미지 
	JPanel p_info; //라벨들이 위치할 그리드 패널 (3,1)
	JLabel la_brand,la_product_name, la_price;
	JLabel la_ea;//수량 
	JTextField t_ea;
	Image  image; //이미지 가져오기
	
	public MyOrderItem(MyOrderDetailVO vo) {
		this.image = ImageUtil.getCustomSize( ImageUtil.getImageFromURL(vo.getFilename()), 100, 85);
		p_can = new JPanel() {
			public void paint(Graphics g) {
//				g.setColor(Color.BLUE);
//				g.fillRect(0, 0, 100, 100);
				g.drawImage(image, 0,0, p_can);
			}
		};
		
		NumberFormat myFormat = NumberFormat.getInstance(); 
		myFormat.setGroupingUsed(true);
		
		p_info = new JPanel();
		la_brand = new JLabel(vo.getBrand() + "/" + vo.getProduct_name() + "/" + myFormat.format(vo.getPrice()*vo.getNumProduct()) + "원 /" + vo.getSize() ,SwingConstants.CENTER);
		la_product_name = new JLabel(vo.getProduct_name(),SwingConstants.LEFT);
		la_price = new JLabel(Integer.toString(vo.getPrice()*vo.getNumProduct()),SwingConstants.LEFT);
		la_ea = new JLabel(" 수량");
		t_ea = new JTextField(Integer.toString(vo.getNumProduct()),4);
		t_ea.setEditable(false);
		
		
		//스타일
		this.setPreferredSize(new Dimension(ShopMain.WIDTH-400, 115));
		this.setBackground(Color.white);
		p_can.setPreferredSize(new Dimension(100, 85));
		p_info.setPreferredSize(new Dimension(400, 100));
		p_info.setBackground(Color.WHITE);
		
		la_ea.setPreferredSize(new Dimension(40, 25));
		la_brand.setPreferredSize(new Dimension(120, 250));
		la_brand.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		la_product_name.setPreferredSize(new Dimension(40, 50));
		la_price.setPreferredSize(new Dimension(40, 50));
		t_ea.setPreferredSize(new Dimension(50, 25));


		
		//조립 
		p_info.setLayout(new GridLayout(1,1));
		this.add(p_can);
		p_info.add(la_brand);
		//p_info.add(la_product_name);
		//p_info.add(la_price);
		this.add(p_info);
		this.add(la_ea);
		this.add(t_ea);
	
		
		p_can.repaint();
		

		
		
		
	}
	
}
