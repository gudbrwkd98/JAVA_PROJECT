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
	
	
	ArrayList<String> topCategory=new ArrayList<String>(); //상위카테고리
	ArrayList<ArrayList> subCategory=new ArrayList<ArrayList>();//하위카테고리
	ProductModel model;
	RegistForm registForm;
	public Product(AdminMain adminMain) {
		super(adminMain);
		getTopList();

		
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
		
		//등록폼 생성
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
		
		//스타일
		s1.setPreferredSize(new Dimension(200,adminMain.HEIGHT-150));
		p_west.setBackground(Color.white);
		s2.setPreferredSize(new Dimension(adminMain.WIDTH-300,adminMain.HEIGHT-200));
		//조립
		setLayout(new BorderLayout());
		
		p_west.add(s1);
		p_center.add(s2);
		p_center.add(bt_regist);
		
		add(p_west,BorderLayout.WEST);
		addRemoveContent(registForm, p_center);
		//tree 는 이벤트가 별도로 지원
		tree.addTreeSelectionListener((e)->{
			DefaultMutableTreeNode selectedNode= (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if(selectedNode.toString().equals("상품 목록")) {
				getProduct(null);
			}else{
			getProduct(selectedNode.toString());
			}
		});
		
		bt_regist.addActionListener((e)->{
			addRemoveContent(p_center, registForm);
		});
 
	}
		//상위카테고리 가져오기
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
		
	//하위 카테고리 가져오기
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
	
	//상품 가져오기
	public void getProduct(String name) {
		
		PreparedStatement pstmt = null;
		ResultSet rs  = null ;
		String sql =  null;
		
		//name 값이 넘어오면 조건에 맞게 쿼리 수행
		if(name == null) {//name 값이 안넘어올시 모든 상품 가져오기
			sql = "select * from product";
		}else {
			sql = "select * from product p inner join subcategory s on p.subcategory_id = s.subcategory_id inner join topcategory t on s.topcategory_id = t.topcategory_id where t.name = '"+name+"' OR s.name = '"+name+"'";
		}
		
		try {
			pstmt = getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			//메타 정보를 이용하여 ProductModel 의 colum arrayList 를 채우자 
	
			ResultSetMetaData meta=rs.getMetaData();
			int columnCount = meta.getColumnCount();//총 컬럼 수
			
			ArrayList<String> columnNames = new ArrayList<String>();
			for(int i=1;i<=columnCount;i++) {
				String colName = meta.getColumnName(i);
				columnNames.add(colName);
			} 
			
			
			
			//rs의 레코드를 productModel 의 record ArrayList에 채우자..
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
				
				productList.add(vo); //방금생성하고 하나의 레코드가 채워진 vo를 arrayList에 추가하자
				
			}
			
			model = new ProductModel();
			model.column = columnNames; //컬럼정보 대입
			model.record = productList; //레코드 정보 대입
			table.setModel(model); //테이블에 방금 생성한 모델 적용!
			table.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getAdminMain().getDbManager().close(pstmt, rs);
		}

	}
	//보여질 컨텐트와 가려질 컨텐트를 제어하는 메서드
	public void addRemoveContent(Component removeObj,Component addObj) {
		this.remove(removeObj);
		this.add(addObj);
		((JPanel)addObj).updateUI();
		
	}
	
 


}
