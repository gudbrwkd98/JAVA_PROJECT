package com.swingmall.home;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.swingmall.product.ProductVO;
import com.swingmall.product.ProductDetail;

public class ProductItem extends JPanel{
	JPanel p_can;
	JLabel la_brand;
	JLabel la_product_name;
	JLabel la_price;
	Image img;
	ProductVO vo;
	Home home;
	ProductDetail productDetail;
	Border blackline;
	public ProductItem(Home home ,ProductVO vo ,int width , int height) {
		this.home = home;
		this.vo = vo;
		
		try {
			URL url = new URL(vo.getFilename());
			img = ImageIO.read(url);
			img = img.getScaledInstance(width, height/2, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		p_can = new JPanel() {
			public void paint(Graphics g) {
//				g.setColor(Color.BLUE);
//				g.fillRect(0, 0, width, height/2);
				g.drawImage(img, 0,0, this);
			}
		};
		
		la_brand = new JLabel("  "+vo.getBrand());
		la_brand.setPreferredSize(new Dimension(width,(height/2)/4));
		la_product_name = new JLabel("  "+vo.getProduct_name());
		la_product_name.setPreferredSize(new Dimension(width,(height/2)/4));
		la_price = new JLabel(" 가격 :" + vo.getPrice());
		la_price.setPreferredSize(new Dimension(width,(height/2)/4));
		
		//스타일 적용
		setPreferredSize(new Dimension(width,height));
		p_can.setPreferredSize(new Dimension(width-20,height/2));
		blackline = BorderFactory.createLineBorder(Color.black);
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, Color.BLACK));
		
		add(p_can);
		add(la_brand);
		add(la_product_name);
		add(la_price);
		

		
	}
}
