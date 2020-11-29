package com.shoppingmall.admin.member;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
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

public class Member extends Page{

	JPanel p_center;
	JPanel p_search;
	Choice c_kind;
	JTextField t_search;
	JButton bt_search;
	JTable table;
	JScrollPane scroll;
	JButton bt_add;
	
	MemberModel model;
	
	
	public Member(AdminMain adminMain) {
		super(adminMain);
		
		p_center = new JPanel();
		p_search = new JPanel();
		t_search = new JTextField();
		bt_search = new JButton("검색");
		c_kind = new Choice();
		c_kind.add("아이디");
		c_kind.add("이름");
	
		
		table = new JTable();
		scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(adminMain.WIDTH-300,adminMain.HEIGHT-300));
		
		p_search.setPreferredSize(new Dimension(adminMain.WIDTH-300,50));
		t_search.setPreferredSize(new Dimension(500,25));
		c_kind.setPreferredSize(new Dimension(200,25));
		table.getTableHeader().setFont(new Font("맑은 고딕",Font.BOLD,12));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().	setBackground(new Color(15, 76, 129));
		table.getTableHeader().setForeground(new Color(255,255,255));
		table.setRowHeight(25);
		setLayout(new BorderLayout());
	
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				MemberDetail memberDetail = new MemberDetail(Member.this,(String) table.getValueAt(table.getSelectedRow(), 0));
				addRemoveContent(p_center, memberDetail);
			}
		});
		
		bt_search.addActionListener((e)->{
			getSeacrhMemberList(c_kind.getSelectedIndex(),t_search.getText());
		});
		
		p_search.add(c_kind);
		p_search.add(t_search);
		p_search.add(bt_search);
		
		getMemberList();
		p_center.add(p_search);
		p_center.add(scroll);
		add(p_center);
		
		
	}
	
	public void getMemberList() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from shop_member order by member_id desc";
		
		try {
			pstmt = getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			

			
			ArrayList<MemberVO> memberList = new ArrayList<MemberVO>();
			while(rs.next()) {
				MemberVO vo = new MemberVO();
				vo.setMember_id(rs.getInt("member_id"));
				vo.setMid(rs.getString("mid"));
				vo.setPass(rs.getString("pass"));
				vo.setName(rs.getString("name"));
				vo.setPhone(rs.getString("phone"));
				vo.setEmail(rs.getString("email"));
				vo.setLvl(rs.getInt("lvl"));
				memberList.add(vo);
			}
			
			model = new MemberModel();
			model.record = memberList;
			table.setModel(model);
			table.updateUI();
			
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			getAdminMain().getDbManager().close(pstmt,rs);
		}
		
		
		
	}
	
	public void getSeacrhMemberList(int category,String keyword) {
		String cate = "";
		if(category == 0) {
			cate = "mid";
		}else if(category == 1) {
			cate = "name";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from shop_member where " + cate + " like '%" + keyword +"%' order by member_id desc";
		System.out.println(sql);
		try {
			pstmt = getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			

			
			ArrayList<MemberVO> memberList = new ArrayList<MemberVO>();
			while(rs.next()) {
				MemberVO vo = new MemberVO();
				vo.setMember_id(rs.getInt("member_id"));
				vo.setMid(rs.getString("mid"));
				vo.setPass(rs.getString("pass"));
				vo.setName(rs.getString("name"));
				vo.setPhone(rs.getString("phone"));
				vo.setEmail(rs.getString("email"));
				vo.setLvl(rs.getInt("lvl"));
				memberList.add(vo);
			}
			
			model = new MemberModel();
			model.record = memberList;
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
