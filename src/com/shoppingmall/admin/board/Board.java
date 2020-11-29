package com.shoppingmall.admin.board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.shoppingmall.admin.AdminMain;
import com.shoppingmall.admin.Page;


public class Board extends Page{
	JTable table;
	JScrollPane scroll;
	BoardModel model;
	JTextField t_search;
	JButton bt_search;
	JPanel p_content;
	JPanel p_table;
	JPanel p_search;
	
	public Board(AdminMain adminMain) {
		super(adminMain);
		p_content = new JPanel();
		p_search = new JPanel();
		p_table = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);
		t_search = new JTextField("제목 검색 창");
		bt_search = new JButton("검색");
		
		p_content.setLayout(new BorderLayout());
		p_content.setPreferredSize(new Dimension(adminMain.WIDTH-100,adminMain.HEIGHT-200));
		t_search.setPreferredSize(new Dimension(500,25));
		p_search.setPreferredSize(new Dimension(adminMain.WIDTH-100,50));
		scroll.setPreferredSize(new Dimension(adminMain.WIDTH-100,adminMain.HEIGHT-300));
		table.getTableHeader().setFont(new Font("맑은 고딕",Font.BOLD,12));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().	setBackground(new Color(15, 76, 129));
		table.getTableHeader().setForeground(new Color(255,255,255));
		table.setRowHeight(25);
		
		p_search.add(t_search);
		p_search.add(bt_search);
		
		
		p_table.add(scroll);
		
		p_content.add(p_search,BorderLayout.NORTH);
		p_content.add(p_table);
		
		
		t_search.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				t_search.setText("");
			}
		});
		
		this.add(p_content);
		
		 getBoardList();
		
//		bt_add.addActionListener((e)->{
//			addBoard = new AddBoard(this);
//			addRemoveContent(p_content,addBoard);
//
//	 	});
		
		bt_search.addActionListener((e)->{
			getSearchResult(t_search.getText());
			table.updateUI();//테이블 갱신
		});
		
		table.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				BoardDetail boardDetail = new BoardDetail(Board.this,(String)table.getValueAt(table.getSelectedRow(), 0),(String)table.getValueAt(table.getSelectedRow(), 4));
				addRemoveContent(p_content, boardDetail);
				boardDetail.getDetail((String)table.getValueAt(table.getSelectedRow(), 0));
				
			}
			
		});
		
		
		
	}
	
	public  void getBoardList() {
		PreparedStatement pstmt = null;
		ResultSet rs = null ;
		String sql = "select * from shop_board s , shop_member m where s.member_id = m.member_id ORDER BY s.regDate DESC";
		
		try {
			pstmt = getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			ArrayList<BoardVO> boardList = new ArrayList<BoardVO>();
			
			while(rs.next()) {
				BoardVO vo = new BoardVO();
				vo.setBoard_id(rs.getInt("board_id"));
				vo.setBoard_number(rs.getInt("board_number"));
				vo.setTitle(rs.getString("title"));
				vo.setContent(rs.getString("content"));
				vo.setMember_id(rs.getInt("member_id"));
				vo.setRegdate(rs.getString("regdate"));
				vo.setHit(rs.getInt("hit"));
				vo.setName(rs.getString("name"));
				vo.setSatus(rs.getString("status"));
				boardList.add(vo);
			}
			
			model = new BoardModel();
			model.record = boardList;
			table.setModel(model);
			table.updateUI();
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getAdminMain().getDbManager().close(pstmt,rs);
		}
	
		
		
		
	}
	
	
	public  void getSearchResult(String keyword) {
		PreparedStatement pstmt = null;
		ResultSet rs = null ;
		String sql = "select * from shop_board s , shop_member m where s.member_id = m.member_id and title LIKE '%" +keyword+ "%' ORDER BY s.board_number DESC";
		
		try {
			pstmt = getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			ArrayList<BoardVO> boardList = new ArrayList<BoardVO>();
			
			while(rs.next()) {
				BoardVO vo = new BoardVO();
				vo.setBoard_id(rs.getInt("board_id"));
				vo.setBoard_number(rs.getInt("board_number"));
				vo.setTitle(rs.getString("title"));
				vo.setContent(rs.getString("content"));
				vo.setMember_id(rs.getInt("member_id"));
				vo.setRegdate(rs.getString("regdate"));
				vo.setHit(rs.getInt("hit"));
				vo.setName(rs.getString("name"));
				vo.setSatus(rs.getString("status"));
				boardList.add(vo);
			}
			
			model = new BoardModel();
			model.record = boardList;
			table.setModel(model);
			table.updateUI();
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getAdminMain().getDbManager().close(pstmt,rs);
		}
	
		
		
		
	}
	
	

	
	
	// 보여질 컨텐트와 가려질 컨텐트를 제어하는 메서드
	public void addRemoveContent(Component removeObj, Component addObj) {
		this.remove(removeObj);
		this.add(addObj);
		((JPanel) addObj).updateUI();

	}
	
	}


