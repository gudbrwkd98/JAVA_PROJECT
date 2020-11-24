package com.swingmall.admin.product;

import java.awt.BorderLayout;
import java.awt.Color;
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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.swingmall.admin.AdminMain;
import com.swingmall.admin.Page;
import com.swingmall.admin.board.Board;

public class Product extends Page{
	JPanel p_west ;
	JPanel p_center;
	JTree tree;
	JTable table;
	JScrollPane s1,s2;
	JButton bt_regist;
	
	
	ArrayList<String> topCategory=new ArrayList<String>(); //����ī�װ�
	ArrayList<ArrayList> subCategory=new ArrayList<ArrayList>();//����ī�װ�
	ProductModel model;
	RegistForm registForm;
	public Product(AdminMain adminMain) {
		super(adminMain);
		getTopList();

		
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
		
		//����� ����
		registForm = new RegistForm(this);
		
		getProduct(null);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				ProductDetail productDetail = new ProductDetail(Product.this);
				productDetail.getDetail(Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)));
				int result = productDetail.setCategory(Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 1)));
				productDetail.getSubcategory(result);
				productDetail.setSubCategory(productDetail.vo.getSubcategory_id());
				addRemoveContent(p_center, productDetail);
			}
		});
		
		//��Ÿ��
		s1.setPreferredSize(new Dimension(200,adminMain.HEIGHT-150));
		p_west.setBackground(Color.white);
		s2.setPreferredSize(new Dimension(adminMain.WIDTH-300,adminMain.HEIGHT-200));
		//����
		setLayout(new BorderLayout());
		
		p_west.add(s1);
		p_center.add(s2);
		p_center.add(bt_regist);
		
		add(p_west,BorderLayout.WEST);
		addRemoveContent(registForm, p_center);
		//tree �� �̺�Ʈ�� ������ ����
		tree.addTreeSelectionListener((e)->{
			DefaultMutableTreeNode selectedNode= (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if(selectedNode.toString().equals("��ǰ ���")) {
				getProduct(null);
			}else{
			getProduct(selectedNode.toString());
			}
		});
		
		bt_regist.addActionListener((e)->{
			addRemoveContent(p_center, registForm);
		});
 
	}
		//����ī�װ� ��������
	public void getTopList() {
		String sql = "select * from topcategory";	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				topCategory.add(rs.getString("name"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getAdminMain().getDbManager().close(pstmt, rs);
		}
	}
		
	//���� ī�װ� ��������
	public ArrayList getSubList(String name) {
		String sql = "select * from subcategory where topcategory_id = (select topcategory_id from topcategory where name = ?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList subList=new ArrayList();
		try {
		 
			pstmt = getAdminMain().getCon().prepareStatement(sql);
			pstmt.setString(1, name);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				subList.add(rs.getString("name"));
			}
		 
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
				getAdminMain().getDbManager().close(pstmt, rs);
			
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
	
	//��ǰ ��������
	public void getProduct(String name) {
		
		PreparedStatement pstmt = null;
		ResultSet rs  = null ;
		String sql =  null;
		
		//name ���� �Ѿ���� ���ǿ� �°� ���� ����
		if(name == null) {//name ���� �ȳѾ�ý� ��� ��ǰ ��������
			sql = "select * from product";
		}else {
			sql = "select * from product p inner join subcategory s on p.subcategory_id = s.subcategory_id inner join topcategory t on s.topcategory_id = t.topcategory_id where t.name = '"+name+"' OR s.name = '"+name+"'";
		}
		
		try {
			pstmt = getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			//��Ÿ ������ �̿��Ͽ� ProductModel �� colum arrayList �� ä���� 
	
			ResultSetMetaData meta=rs.getMetaData();
			int columnCount = meta.getColumnCount();//�� �÷� ��
			
			ArrayList<String> columnNames = new ArrayList<String>();
			for(int i=1;i<=columnCount;i++) {
				String colName = meta.getColumnName(i);
				columnNames.add(colName);
			} 
			
			
			
			//rs�� ���ڵ带 productModel �� record ArrayList�� ä����..
			ArrayList<ProductVO> productList = new ArrayList<ProductVO>();
			while(rs.next()) {
				ProductVO vo = new ProductVO();
				vo.setProduct_id(rs.getInt("product_id"));
				vo.setSubcategory_id(rs.getInt("subcategory_id"));
				vo.setProduct_name(rs.getString("product_name"));
				vo.setBrand(rs.getString("brand"));
				vo.setPrice(rs.getInt("price"));
				vo.setFilename(rs.getString("filename"));
				vo.setDetail(rs.getString("detail"));
				
				productList.add(vo); //��ݻ����ϰ� �ϳ��� ���ڵ尡 ä���� vo�� arrayList�� �߰�����
				
			}
			
			model = new ProductModel();
			model.column = columnNames; //�÷����� ����
			model.record = productList; //���ڵ� ���� ����
			table.setModel(model); //���̺� ��� ������ �� ����!
			table.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getAdminMain().getDbManager().close(pstmt, rs);
		}

	}
	//������ ����Ʈ�� ������ ����Ʈ�� �����ϴ� �޼���
	public void addRemoveContent(Component removeObj,Component addObj) {
		this.remove(removeObj);
		this.add(addObj);
		((JPanel)addObj).updateUI();
		
	}
	
 


}
