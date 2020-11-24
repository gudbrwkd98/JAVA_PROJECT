package com.swingmall.home;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;


import com.swingmall.main.Page;
import com.swingmall.main.ShopMain;
import com.swingmall.product.ProductDetail;
import com.swingmall.product.ProductVO;

public class Home extends Page{
	JPanel p_content; //상품리스트를 담게될 패널,추후 상세보기로 전환시 이패널자체를 들어내버릴꺼임..
	ArrayList<ProductItem> itemList; //생성된 상품 아이템들을 담게될 리스트 ~ (왜? productItem 클래스 내에서 이벤트를 구현하면 너무 많은 정보를 넘겨야 하므로 또한 페이지도 아니면서 너무 많은 정보를 가져야 하므로 효율성 저하)
	int i = 0;
	Border blackline;
	public Home(ShopMain shopMain) {
		super(shopMain);
		
		 blackline = BorderFactory.createLineBorder(Color.black);
 
	        
		p_content = new JPanel();
		p_content.setPreferredSize(new Dimension(ShopMain.WIDTH-40,ShopMain.HEIGHT-150));
		p_content.setBackground(new Color(15,76,129));
		p_content.setBorder(blackline);
		add(p_content);
		//add(detail = new ProductDetail());
		getProductList();
		p_content.updateUI();
		
		//생성된 아이템들에 대해서 마우스 리스너 연결하기...
		for(ProductItem item :itemList) {
			item.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					
					ProductDetail productDetail = (ProductDetail) getShopMain().getPage()[ShopMain.PRODUCT_DETAIL];
					productDetail.init(item.vo,item.img);
					productDetail.p_can.repaint();
					getShopMain().setSize(1200,800+i);
					i++;
					//보여주고 싶은 페이지 
					shopMain.showPage(ShopMain.PRODUCT_DETAIL);
					
				}
			});
 
		}
		
		
		
	}
	
	//모든 상품 가져오기
	public void getProductList() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql= "select * from product";
		
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			itemList = new ArrayList<ProductItem>();
			while(rs.next()) {//레코드가있는만큼
				//vo하나를 생성한후 rs 의 데이터를 vo 옮긴다!!
			 ProductVO vo = new ProductVO();
			 vo.setProduct_id(rs.getInt("product_id"));
			 vo.setProduct_name(rs.getString("product_name"));
			 vo.setBrand(rs.getString("brand"));
			 vo.setPrice(rs.getInt("price"));
			 vo.setFilename(rs.getString("filename"));
			 vo.setDetail(rs.getString("detail"));
			 
			 itemList.add(getCreateItem(vo));//완성된 VO 를 이용하여 createItem()호출
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getShopMain().getDbManager().close(pstmt,rs);
		}
		
		
	}
	
	
	//상품 아이템 카드 생성하기
	public ProductItem getCreateItem(ProductVO vo) {
		 
		ProductItem item = new ProductItem(this,vo,250, 280);
		p_content.add(item);
		return item; //생성후 반환까지 하자
	}
	

	
}
