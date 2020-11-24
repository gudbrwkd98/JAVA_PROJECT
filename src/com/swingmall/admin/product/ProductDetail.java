package com.swingmall.admin.product;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.swingmall.admin.AdminMain;

public class ProductDetail extends JPanel{
	JPanel p_container;//그리드 적용 예정(7, 2)

	String[] title= {"상위카테고리","하위카테고리","상품명","브랜드","가격","이미지","상세설명"};
	JLabel[] la_title = new JLabel[title.length];
	
	Choice ch_top;//최상위 카테고리
	Choice ch_sub;//하위 카테고리
	JTextField t_product_name;//상품명
	JTextField t_brand;//브랜드
	JTextField t_price;//가격
	JTextField t_filename;//이미지
	JTextArea t_detail;//상세설명
	JScrollPane s1; //상세설명에 부착할 스크롤
	JButton bt_update,bt_list;
	Product product;
	ProductVO vo;
	
	public ProductDetail(Product product) {
		this.product = product;
		p_container = new JPanel();
		for(int i=0;i<title.length;i++) {
			la_title[i] = new JLabel(title[i], SwingConstants.RIGHT);
		}
		ch_top = new Choice();
		ch_sub = new Choice();
		t_product_name = new JTextField();
		t_brand = new JTextField();
		t_price = new JTextField();
		t_filename = new JTextField();
		t_detail = new JTextArea();
		s1 = new JScrollPane(t_detail);
		bt_update = new JButton("수정");
		bt_list = new JButton("목록");
		
		//최상위 카테고리 채우기
		for (int i = 0; i < product.topCategory.size(); i++) {
			ch_top.add(product.topCategory.get(i));
		}
		
 
	
		
		//스타일 적용 
		Dimension d = new Dimension(320,25);
		
		p_container.setBackground(Color.WHITE);
		p_container.setPreferredSize(new Dimension(AdminMain.WIDTH-500, AdminMain.HEIGHT-400));
		for(int i=0;i<title.length;i++) {
			la_title[i].setPreferredSize(d);
		}
		ch_top.setPreferredSize(d);
		ch_sub.setPreferredSize(d);
		t_product_name.setPreferredSize(d);
		t_brand.setPreferredSize(d);
		t_price.setPreferredSize(d);
		t_filename.setPreferredSize(d);
		t_detail.setPreferredSize(new Dimension(320, 300));
		bt_update.setPreferredSize(new Dimension(300, 40));
		bt_list.setPreferredSize(new Dimension(300,40));
		
		//조립
		p_container.add(la_title[0]);
		p_container.add(ch_top);
		p_container.add(la_title[1]);
		p_container.add(ch_sub);
		p_container.add(la_title[2]);
		p_container.add(t_product_name);
		p_container.add(la_title[3]);
		p_container.add(t_brand);
		p_container.add(la_title[4]);
		p_container.add(t_price);
		p_container.add(la_title[5]);
		p_container.add(t_filename);
		p_container.add(la_title[6]);
		p_container.add(s1);
		
		this.add(p_container);//현재 패널에 폼컨테이너 부착
		this.add(bt_update);//현재 패널에 버튼 부착 //버튼크기조절을 하기위해선 패널을 하나 만들어 버튼2개를 넣는다
		this.add(bt_list);
		
		bt_update.addActionListener((e)->{
			if(update()==0) {
				JOptionPane.showMessageDialog(ProductDetail.this, "실패");
			}else {
				JOptionPane.showMessageDialog(ProductDetail.this, "성공");
				product.getProduct(null);//목록을 갱신시키위한 메서드 호출 테이블을 다시 가져온다..
				
				
			}
		});
		
		bt_list.addActionListener((e)->{
			this.product.addRemoveContent(this, this.product.p_center);
		 
		});
		
		ch_top.addItemListener((e)->{
			int index = ch_top.getSelectedIndex();
			getSubcategory(index);
		});
		
	 
 
		
		
		
		
	}
	
	//지금 유저가 선택한 최상위 카테고리에 소속된 하위카테고리만 가져오기!
	public void getSubcategory(int index) {
		ArrayList<String> list = (ArrayList<String>) product.subCategory.get(index);
		ch_sub.removeAll();
		for(String item: list) {
			ch_sub.add(item);
		}
	}
	
	//유저가 선택한 아이템으로부터 subcategory의 pk 를 가져오기
	public int getSubId(String name) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int subcategory_id = 0;
		String sql = "select * from subcategory where name = ?";
		
		try {
			pstmt = product.getAdminMain().getCon().prepareStatement(sql);
			pstmt.setString(1, name); //매개변수로 전달된 아이템을 바인드 변수에 대입
			rs = pstmt.executeQuery();
			if(rs.next()) {
				subcategory_id = rs.getInt("subcategory_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			product.getAdminMain().getDbManager().close(pstmt,rs);
		}
		
		return subcategory_id;
		
		
	}
	
	//한건 가져오기
	public void getDetail(int product_id) {
		String sql = "select * from product where product_id = " + product_id;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = product.getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				vo = new ProductVO();
				vo.setProduct_id(rs.getInt("product_id"));
				vo.setProduct_name(rs.getString("product_name"));
				vo.setBrand(rs.getString("brand"));
				vo.setPrice(rs.getInt("price"));
				vo.setDetail(rs.getString("detail"));
				vo.setFilename(rs.getString("filename"));
				vo.setSubcategory_id(rs.getInt("subcategory_id"));
				
				 t_product_name.setText(vo.getProduct_name());;
				 t_brand.setText(vo.getBrand());;
				 t_price.setText(Integer.toString(vo.getPrice()));;
				 t_filename.setText(vo.getFilename());
				 t_detail.setText(vo.getDetail());;
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			product.getAdminMain().getDbManager().close(pstmt);
		}
		
		
	}
	
	public int setCategory(int row) {
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		
		String subcategory_id = Integer.toString(row);
		
		String sql="select * from topcategory where topcategory_id=(";
		sql+="select topcategory_id  from subcategory where subcategory_id ="+subcategory_id;		
		sql+=")";
		
		try {
			pstmt = product.getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
			ch_top.select(rs.getString("name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			
			
		}	
		return ch_top.getSelectedIndex();
	}
	
	public void setSubCategory(int row) {
		String subcategory_id = Integer.toString(row);
		String sql="select * from subcategory where subcategory_id ="+subcategory_id;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null; 
		
		try {
			pstmt=product.getAdminMain().getCon().prepareStatement(sql);//쿼리수행 객체 생성, 쿼리문 준비
			rs=pstmt.executeQuery();
 
			if(rs.next()) {
				ch_sub.select(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	public int update() {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "update  product set subcategory_id=?, product_name=?, brand=?, price=?, filename=?, detail=? where product_id = ?";
		try {
			pstmt = product.getAdminMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, getSubId(ch_sub.getSelectedItem()));
			pstmt.setString(2, t_product_name.getText());
			pstmt.setString(3, t_brand.getText());
			pstmt.setInt(4, Integer.parseInt(t_price.getText())); 
			pstmt.setString(5, t_filename.getText());
			pstmt.setString(6, t_detail.getText());
			pstmt.setInt(7, vo.getProduct_id());
			
			result = pstmt.executeUpdate();
			if(result == 0) {
				System.out.println("실패");
			}else {
				System.out.println("성공");
			}
			
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			product.getAdminMain().getDbManager().close(pstmt);
		}
		return result;
		
	}
}
