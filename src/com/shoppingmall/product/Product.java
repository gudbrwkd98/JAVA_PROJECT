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
		
		
		ArrayList<String> topCategory=new ArrayList<String>(); //����ī�װ�
		ArrayList<ArrayList> subCategory=new ArrayList<ArrayList>();//����ī�װ�
		
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
				subCategory.add(subList); //������ ������ ����
			}
			
			DefaultMutableTreeNode top = new DefaultMutableTreeNode("��ǰ ���");
			
			for(int i=0;i<topCategory.size();i++) {
				String name= topCategory.get(i);
				top.add(getCreatedNode(name, subCategory.get(i)));
			}
			
			//����
			p_west = new JPanel();
			p_center = new JPanel();
			tree = new JTree(top); //��带 ���� ����
			table  = new JTable();
			s1 = new JScrollPane(tree);
			s2 = new JScrollPane(table);
			bt_regist = new JButton("����ϱ�");
			p_content.setBackground(new Color(15,76,129));
 

			
			//getProduct(null);
			
			//��Ÿ��
			s1.setPreferredSize(new Dimension(200,shopMain.HEIGHT-150));
			p_west.setBackground(Color.white);
			s2.setPreferredSize(new Dimension(shopMain.WIDTH-300,shopMain.HEIGHT-200));
			
			//����
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
			
			
			//tree �� �̺�Ʈ�� ������ ����
			tree.addTreeSelectionListener((e)->{
				DefaultMutableTreeNode selectedNode= (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(selectedNode.toString().equals("��ǰ ���")) {
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
			//����ī�װ� ��������
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
			
		//���� ī�װ� ��������
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
		
		//Ʈ����Ʈ �����ϱ� 
		public DefaultMutableTreeNode getCreatedNode(String parentName, ArrayList childName) {
			//�θ��� �����ϱ�
			DefaultMutableTreeNode parent = new DefaultMutableTreeNode(parentName);
			
			//�Ѱܹ��� �Ű������� ArrayList ��ŭ �ݺ��Ͽ� �θ��忡 �ڽĳ�� ����!!
			for (int i = 0; i < childName.size(); i++) {
				parent.add(new DefaultMutableTreeNode(childName.get(i)));
			}
			
			return parent;
		}
		

		
		//��� ��ǰ ��������
		public void getProductList(String name) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = null;
			totalH = 0 ;
			p_content.removeAll();
			//name ���� �Ѿ���� ���ǿ� �°� ���� ����
			if(name == null) {//name ���� �ȳѾ�ý� ��� ��ǰ ��������
				sql = "select * from product order by product_id desc";
			}else {
				sql = "select * from product p inner join subcategory s on p.subcategory_id = s.subcategory_id inner join topcategory t on s.topcategory_id = t.topcategory_id where t.name = '"+name+"' OR s.name = '"+name+"' order by product_id desc";
			}
			
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
				 vo.setSubcategory_id(rs.getInt("subcategory_id"));
				 itemList.add(getCreateItem(vo));//�ϼ��� VO �� �̿��Ͽ� createItem()ȣ��
				 totalH += 70;
				}
				p_content.setPreferredSize(new Dimension( ShopMain.WIDTH-500,totalH));
				//������ �����۵鿡 ���ؼ� ���콺 ������ �����ϱ�...
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
							
							//�����ְ� ���� ������ 
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
		
		//��ǰ������
		
		public ProductItem getCreateItem(ProductVO vo) {
			
			ProductItem item = new ProductItem(this, vo, 200, 250);
			p_content.add(item);
			return item;
		}
		
		
		
		//������ ����Ʈ�� ������ ����Ʈ�� �����ϴ� �޼���
		public void addRemoveContent(Component removeObj,Component addObj) {
			this.remove(removeObj);
			this.add(addObj);
			((JPanel)addObj).updateUI();
			
		}
		
	 


	}



