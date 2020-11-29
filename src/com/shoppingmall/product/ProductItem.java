package com.shoppingmall.product;

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
import javax.swing.SwingConstants;

import com.shoppingmall.product.ProductDetail;

public class ProductItem extends JPanel{
	JPanel p_can;
	JLabel la_brand;
	JLabel la_product_name;
	JLabel la_price;
	Image img;
	ProductVO vo;
	Product product;
	ProductDetail productDetail;
	public ProductItem(Product product,ProductVO vo ,int width , int height) {
		this.product = product;
		this.vo = vo;
		
		try {
			URL url = new URL(vo.getFilename());
			img = ImageIO.read(url);
			img = img.getScaledInstance(width, height/2+5, Image.SCALE_SMOOTH);
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

		la_brand = new JLabel(vo.getBrand(),SwingConstants.CENTER);
		la_brand.setPreferredSize(new Dimension(width,(height/2)/6));
		la_product_name = new JLabel(vo.getProduct_name(),SwingConstants.CENTER);
		la_product_name.setPreferredSize(new Dimension(width,(height/2)/3));
		la_price = new JLabel("°¡°Ý : " + myFormat.format(vo.getPrice())+ " ¿ø",SwingConstants.CENTER);
		la_price.setPreferredSize(new Dimension(width,(height/2)/6));
		la_brand.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		la_product_name.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 15));
		la_price.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 11));
		la_price.setForeground(Color.gray);
		
		//½ºÅ¸ÀÏ Àû¿ë
		setPreferredSize(new Dimension(width,height));
		p_can.setPreferredSize(new Dimension(width-5,height/2+5));
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, Color	.BLACK));
		add(p_can);
		add(la_product_name);
		add(la_brand);
		add(la_price);
		this.setSize(1200, 900);

		
	}
}
