package com.shoppingmall.product;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;

import com.shoppingmall.main.Page;
import com.shoppingmall.main.ShopMain;
import com.shoppingmall.product.ProductItem;

public class Product extends Page{

		JPanel p_west ;
		JPanel p_center;
		JTree tree;
		JTable table;
		JScrollPane s1,s2;
		JButton bt_regist;
		
		
		ArrayList<String> topCategory=new ArrayList<String>(); //상위카테고리
		ArrayList<ArrayList> subCategory=new ArrayList<ArrayList>();//하위카테고리
		
		JPanel p_content;
		ArrayList<ProductItem> itemList;
		
		ProductModel model;
		
		JScrollPane scrollPane;
		JPanel contentPane;
		
		int totalH;
		int i = 0;
		public Product(ShopMain shopMain) {
			super(shopMain); 
			getTopList();
			p_content = new JPanel();
		
			
			for (int i = 0; i < topCategory.size(); i++) {
				String name = topCategory.get(i);
				ArrayList subList = (ArrayList)getSubList(name);
				subCategory.add(subList); //이차원 구조로 생성
			}
			
			DefaultMutableTreeNode top = new DefaultMutableTreeNode("상품 목록");
			
			for(int i=0;i<topCategory.size();i++) {
				String name= topCategory.get(i);
				top.add(getCreatedNode(name, subCategory.get(i)));
			}
			
			//생성
			p_west = new JPanel();
			p_center = new JPanel();
			tree = new JTree(top); //노드를 넣을 예정
			table  = new JTable();
			s1 = new JScrollPane(tree);
			s2 = new JScrollPane(table);
			bt_regist = new JButton("등록하기");
			p_content.setBackground(new Color(15,76,129));
 

			
			//getProduct(null);
			
			//스타일
			s1.setPreferredSize(new Dimension(200,shopMain.HEIGHT-150));
			p_west.setBackground(Color.white);
			s2.setPreferredSize(new Dimension(shopMain.WIDTH-300,shopMain.HEIGHT-200));
			
			//조립
			setLayout(new BorderLayout());
			
			p_west.add(s1);
			p_center.add(s2);
			p_center.add(bt_regist);
			
	        scrollPane = new JScrollPane(p_content);

	        scrollPane.setBounds(0, 0, ShopMain.WIDTH-300,shopMain.HEIGHT-150);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        contentPane = new JPanel(null);
	        contentPane.setPreferredSize(new Dimension(ShopMain.WIDTH,shopMain.HEIGHT));
	        contentPane.add(scrollPane);
			
			add(p_west,BorderLayout.WEST);
			add(contentPane);
			getProductList(null);
			
			
			//tree 는 이벤트가 별도로 지원
			tree.addTreeSelectionListener((e)->{
				DefaultMutableTreeNode selectedNode= (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(selectedNode.toString().equals("상품 목록")) {
					getProductList(null);
					p_content.updateUI();
					p_content.revalidate();
				}else{
					getProductList(selectedNode.toString());
					p_content.updateUI();
					p_content.revalidate();
				}
			});
			p_content.updateUI();
			p_content.revalidate();
	


		}
			//상위카테고리 가져오기
		public void getTopList() {
			String sql = "select * from topcategory";	
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				pstmt = getShopMain().getCon().prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					topCategory.add(rs.getString("name"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				getShopMain().getDbManager().close(pstmt, rs);
			}
		}
			
		//하위 카테고리 가져오기
		public ArrayList getSubList(String name) {
			String sql = "select * from subcategory where topcategory_id = (select topcategory_id from topcategory where name = ?)";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ArrayList subList=new ArrayList();
			try {
			 
				pstmt = getShopMain().getCon().prepareStatement(sql);
				pstmt.setString(1, name);
				
				rs = pstmt.executeQuery();
				while(rs.next()) {
					subList.add(rs.getString("name"));
				}
			 
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
					getShopMain().getDbManager().close(pstmt, rs);
				
			}
			return subList;
		}
		
		//트리노트 생성하기 
		public DefaultMutableTreeNode getCreatedNode(String parentName, ArrayList childName) {
			//부모노드 생성하기
			DefaultMutableTreeNode parent = new DefaultMutableTreeNode(parentName);
			
			//넘겨받은 매개변수인 ArrayList 만큼 반복하여 부모노드에 자식노드 부착!!
			for (int i = 0; i < childName.size(); i++) {
				parent.add(new DefaultMutableTreeNode(childName.get(i)));
			}
			
			return parent;
		}
		

		
		//모든 상품 가져오기
		public void getProductList(String name) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = null;
			totalH = 0 ;
			p_content.removeAll();
			//name 값이 넘어오면 조건에 맞게 쿼리 수행
			if(name == null) {//name 값이 안넘어올시 모든 상품 가져오기
				sql = "select * from product order by product_id desc";
			}else {
				sql = "select * from product p inner join subcategory s on p.subcategory_id = s.subcategory_id inner join topcategory t on s.topcategory_id = t.topcategory_id where t.name = '"+name+"' OR s.name = '"+name+"' order by product_id desc";
			}
			
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
				 vo.setSubcategory_id(rs.getInt("subcategory_id"));
				 itemList.add(getCreateItem(vo));//완성된 VO 를 이용하여 createItem()호출
				 totalH += 70;
				}
				p_content.setPreferredSize(new Dimension( ShopMain.WIDTH-500,totalH));
				//생성된 아이템들에 대해서 마우스 리스너 연결하기...
				for(ProductItem item :itemList) {
					item.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							
							ProductDetail productDetail = (ProductDetail) getShopMain().getPage()[ShopMain.PRODUCT_DETAIL];
							productDetail.init(item.vo,item.img);
							productDetail.p_can.repaint();
							productDetail.p_can.revalidate();
							productDetail.p_can.updateUI();
							productDetail.repaint();
							
							getShopMain().setSize(1200,900+i);
							i++;
							
							//보여주고 싶은 페이지 
							getShopMain().showPage(ShopMain.PRODUCT_DETAIL);
							
						}
					});
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				getShopMain().getDbManager().close(pstmt,rs);
			}
			
			
		}
		
		//상품아이템
		
		public ProductItem getCreateItem(ProductVO vo) {
			
			ProductItem item = new ProductItem(this, vo, 200, 250);
			p_content.add(item);
			return item;
		}
		
		
		
		//보여질 컨텐트와 가려질 컨텐트를 제어하는 메서드
		public void addRemoveContent(Component removeObj,Component addObj) {
			this.remove(removeObj);
			this.add(addObj);
			((JPanel)addObj).updateUI();
			
		}
		
	 


	}



