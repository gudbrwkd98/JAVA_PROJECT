package com.shoppingmall.admin.order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.shoppingmall.admin.AdminMain;
import com.shoppingmall.admin.Page;
import com.shoppingmall.main.ShopMain;

public class Order extends Page{

	JPanel p_center;
	JPanel p_search;
	JTable table;
	JScrollPane scroll;
	JTextField t_search;
	JButton bt_search;
	OrderModel model;
	
	public Order(AdminMain adminMain) {
		super(adminMain);

		p_center = new JPanel();
		p_search = new JPanel();
		t_search = new JTextField();
		bt_search = new JButton("검색");

		table = new JTable();
		scroll  = new JScrollPane(table);
		table.getTableHeader().setFont(new Font("맑은 고딕",Font.BOLD,12));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().	setBackground(new Color(15, 76, 129));
		table.getTableHeader().setForeground(new Color(255,255,255));
		table.setRowHeight(25);
		//스타일
		p_search.setPreferredSize(new Dimension(adminMain.WIDTH-300,50));
		t_search.setPreferredSize(new Dimension(500,25));
		scroll.setPreferredSize(new Dimension(adminMain.WIDTH-300,adminMain.HEIGHT-300));
		setLayout(new BorderLayout());
		
		p_search.add(t_search);
		p_search.add(bt_search);
		p_center.add(p_search);
		p_center.add(scroll);
		add(p_center);
		getOrder();
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				OrderDetail order = new OrderDetail(Order.this,(String) table.getValueAt(table.getSelectedRow(), 0),(String) table.getValueAt(table.getSelectedRow(), 2),(String) table.getValueAt(table.getSelectedRow(), 4));
				
				addRemoveContent(p_center,order);
			}
		});
		
		
		bt_search.addActionListener((e)->{
			getSeacrhOrder(t_search.getText());
		});
		
		

		
	}
	
	
	//주문목록 가져오기 
	public void getOrder() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select r.receipt_id,r.paid_p,r.member_id,m.name,r.order_date,r.status from receipt r, shop_member m where r.member_id = m.member_id ORDER BY r.order_date DESC";
		
		try {
			pstmt = getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			

			
			//rs의 레코드를 orderModel의 record ArrayList에 채우기
			ArrayList<OrderVO> orderList = new ArrayList<OrderVO>();
			while(rs.next()) {
				OrderVO vo = new OrderVO();
				vo.setReceipt_id(rs.getInt("receipt_id"));
				vo.setPaid_p(rs.getInt("paid_p"));
				vo.setMember_id(rs.getInt("member_id"));
				vo.setName(rs.getString("name"));
				vo.setOrder_date(rs.getString("order_date"));
				vo.setStatus(rs.getString("status"));
				
				orderList.add(vo);
			}
			
			model = new OrderModel();
			model.record = orderList;
			table.setModel(model);
			table.updateUI();
			 
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			getAdminMain().getDbManager().close(pstmt,rs);
		}
		
		
		
	}
	
	
	public void getSeacrhOrder(String keyword) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select r.receipt_id,r.paid_p,r.member_id,m.name,r.order_date,r.status from receipt r, shop_member m where m.name LIKE '%"+keyword+"%'  AND r.member_id = m.member_id ORDER BY r.order_date DESC";
		
		try {
			pstmt = getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			

			
			//rs의 레코드를 orderModel의 record ArrayList에 채우기
			ArrayList<OrderVO> orderList = new ArrayList<OrderVO>();
			while(rs.next()) {
				OrderVO vo = new OrderVO();
				vo.setReceipt_id(rs.getInt("receipt_id"));
				vo.setPaid_p(rs.getInt("paid_p"));
				vo.setMember_id(rs.getInt("member_id"));
				vo.setName(rs.getString("name"));
				vo.setOrder_date(rs.getString("order_date"));
				vo.setStatus(rs.getString("status"));
				
				orderList.add(vo);
			}
			
			model = new OrderModel();
			model.record = orderList;
			table.setModel(model);
			table.updateUI();
			 
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			getAdminMain().getDbManager().close(pstmt,rs);
		}
		
		
		
	}
	
	//보여질 컨텐트와 가려질 컨텐트를 제어하는 메서드
	public void addRemoveContent(Component removeObj,Component addObj) {
		this.remove(removeObj);
		this.add(addObj);
		((JPanel)addObj).updateUI();
		
	}
	
	
}
