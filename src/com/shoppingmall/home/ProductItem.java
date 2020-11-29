package com.shoppingmall.home;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.shoppingmall.product.ProductDetail;
import com.shoppingmall.product.ProductVO;

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
		
		NumberFormat myFormat = NumberFormat.getInstance(); 
		myFormat.setGroupingUsed(true);
		
		la_brand = new JLabel("  "+vo.getBrand());
		la_brand.setPreferredSize(new Dimension(width,(height/2)/4));
		la_product_name = new JLabel("  "+vo.getProduct_name());
		la_product_name.setPreferredSize(new Dimension(width,(height/2)/4));
		la_price = new JLabel("   Price :" + myFormat.format(vo.getPrice()) + " ¿ø");
		la_price.setPreferredSize(new Dimension(width,(height/2)/4));
		
		//½ºÅ¸ÀÏ Àû¿ë
		setPreferredSize(new Dimension(width,height));
		p_can.setPreferredSize(new Dimension(width-20,height/2));
		blackline = BorderFactory.createLineBorder(Color.black);
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, Color.BLACK));
		this.setBackground(Color.WHITE);
		la_brand.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 16));
		la_product_name.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 20));
		la_price.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		
		
		
		add(p_can);
		add(la_product_name);
		add(la_brand);
		add(la_price);
		

		
	}
}
