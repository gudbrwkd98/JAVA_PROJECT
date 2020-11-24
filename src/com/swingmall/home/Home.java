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
	JPanel p_content; //��ǰ����Ʈ�� ��Ե� �г�,���� �󼼺���� ��ȯ�� ���г���ü�� ����������..
	ArrayList<ProductItem> itemList; //������ ��ǰ �����۵��� ��Ե� ����Ʈ ~ (��? productItem Ŭ���� ������ �̺�Ʈ�� �����ϸ� �ʹ� ���� ������ �Ѱܾ� �ϹǷ� ���� �������� �ƴϸ鼭 �ʹ� ���� ������ ������ �ϹǷ� ȿ���� ����)
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
		
		//������ �����۵鿡 ���ؼ� ���콺 ������ �����ϱ�...
		for(ProductItem item :itemList) {
			item.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					
					ProductDetail productDetail = (ProductDetail) getShopMain().getPage()[ShopMain.PRODUCT_DETAIL];
					productDetail.init(item.vo,item.img);
					productDetail.p_can.repaint();
					getShopMain().setSize(1200,800+i);
					i++;
					//�����ְ� ���� ������ 
					shopMain.showPage(ShopMain.PRODUCT_DETAIL);
					
				}
			});
 
		}
		
		
		
	}
	
	//��� ��ǰ ��������
	public void getProductList() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql= "select * from product";
		
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			itemList = new ArrayList<ProductItem>();
			while(rs.next()) {//���ڵ尡�ִ¸�ŭ
				//vo�ϳ��� �������� rs �� �����͸� vo �ű��!!
			 ProductVO vo = new ProductVO();
			 vo.setProduct_id(rs.getInt("product_id"));
			 vo.setProduct_name(rs.getString("product_name"));
			 vo.setBrand(rs.getString("brand"));
			 vo.setPrice(rs.getInt("price"));
			 vo.setFilename(rs.getString("filename"));
			 vo.setDetail(rs.getString("detail"));
			 
			 itemList.add(getCreateItem(vo));//�ϼ��� VO �� �̿��Ͽ� createItem()ȣ��
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getShopMain().getDbManager().close(pstmt,rs);
		}
		
		
	}
	
	
	//��ǰ ������ ī�� �����ϱ�
	public ProductItem getCreateItem(ProductVO vo) {
		 
		ProductItem item = new ProductItem(this,vo,250, 280);
		p_content.add(item);
		return item; //������ ��ȯ���� ����
	}
	

	
}
