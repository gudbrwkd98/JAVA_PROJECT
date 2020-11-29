package com.shoppingmall.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.shoppingmall.member.Login;

public class BoardDetail extends JPanel{
	JPanel p_content, p_t, p_i,p_w;
	JTextField t_title;
	JTextArea content;
	JTextArea answer_content;
	JScrollPane scroll,answer_scroll;
	JLabel l_title, l_content,l_writer,l_answer,l_answerC;
	JButton bt_list;
	JButton bt_edit;
	JButton bt_delete;
	Board board;
	String value = "";
	String p_sba_id;

	public BoardDetail(Board board,String value) {
		Login login = (Login)board.getShopMain().getPage()[board.getShopMain().LOGIN];
		this.value = value;
		this.board = board;
		p_content = new JPanel();
		p_t = new JPanel();
		p_i = new JPanel();
		p_w = new JPanel();
		t_title = new JTextField();
		content = new JTextArea();
		scroll = new JScrollPane(content);
		answer_content = new JTextArea();
		answer_scroll = new JScrollPane(answer_content);
		
		l_title = new JLabel("제목 ");
		l_content = new JLabel("내용");
		l_writer = new JLabel("작성자",SwingConstants.LEFT);
		l_answer = new JLabel("답변자 : ");
		l_answerC = new JLabel("답변 : ");
		bt_list = new JButton("목록보기");
		bt_edit = new JButton("수정하기");
		bt_delete = new JButton("삭제하기");
		t_title.setEditable(false);
		content.setEditable(false);
		answer_content.setEditable(false);

		// 스타일
		p_t.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 100, 100));
		p_i.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 100, 500));
		p_w.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 100, 30));
		l_title.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500, 50));
		t_title.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500, 25));
		l_writer.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500, 25));
		l_answer.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500,20));
		l_answerC.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500,50));
		answer_scroll.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500,150));
		content.setLineWrap(true);
		content.setWrapStyleWord(true);
		answer_content.setLineWrap(true);
		answer_content.setWrapStyleWord(true);
		l_content.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500, 50));
		scroll.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500, 200));
		p_content.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 100, board.getShopMain().HEIGHT - 200));
		bt_edit.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_edit.setBackground(new Color(15,76,129));
		bt_edit.setForeground(Color.WHITE);
		bt_list.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_list.setBackground(new Color(15,76,129));
		bt_list.setForeground(Color.WHITE);
		bt_delete.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_delete.setBackground(new Color(15,76,129));
		bt_delete.setForeground(Color.WHITE);
		this.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 100, board.getShopMain().HEIGHT - 200));

		// 조립
		p_t.add(l_title);
		p_t.add(t_title);
		p_w.add(l_writer);
		p_i.add(l_content);
		p_i.add(scroll);
		p_i.add(l_answer);
		p_i.add(l_answerC);
		p_i.add(answer_scroll);
		p_content.add(p_t);
		p_content.add(p_w);
		p_content.add(p_i);
		p_content.add(bt_list);

		this.add(p_content);

		if(login.getVo() != null) {
			if(login.getVo().getMember_id() == getMember_id(value)) {
				t_title.setEditable(true);
				content.setEditable(true);
				p_content.add(bt_edit);
				p_content.add(bt_delete);
			}
		}

		
		bt_list.addActionListener((e)->{
			board.addRemoveContent(this,board.p_content);
			board.getBoardList();
		});
		
		
		bt_edit.addActionListener((e)->{
			 if(JOptionPane.showConfirmDialog(BoardDetail.this, "수정 하시겠습니까?") == JOptionPane.OK_OPTION) {
			if(updateBoard(value) != 0) {
				JOptionPane.showMessageDialog(BoardDetail.this, "글을 수정하였습니다");
				board.addRemoveContent(this,board.p_content);
				board.getBoardList();
			}
			 }
		});
		
		bt_delete.addActionListener((e)->{
			 if(JOptionPane.showConfirmDialog(BoardDetail.this, "삭제 하시겠습니까?") == JOptionPane.OK_OPTION) {
			String id = getAnswer();
			if(deleteBoard() != 0) {
				deteleAnswer(id);
				JOptionPane.showMessageDialog(BoardDetail.this, "삭제되었습니다");
				board.addRemoveContent(this, board.p_content);
				board.getBoardList();
			}
			 }
		});

	}
	
	public void getDetail(String value) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "";
		if(getAnswer() == null) {
			sql = "select * from shop_board s , shop_member m where s.board_id = " + value + " and s.member_id = m.member_id";
			try {
				pstmt = board.getShopMain().getCon().prepareStatement(sql);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					t_title.setText(rs.getString("title"));
					l_writer.setText("작성자 : " + rs.getString("name"));
					content.setText(rs.getString("content"));
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				board.getShopMain().getDbManager().close(pstmt, rs);
			}
		}else {
		sql = "select s.title,m.name,s.content,a.answer_member_id,a.answer_content from shop_board s, shop_member m,shop_board_answer a where s.board_id = " + value + " and s.member_id = m.member_id AND s.shop_board_answer_id  =  a.shop_board_answer_id " ;
		
		try {
			pstmt = board.getShopMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				t_title.setText(rs.getString("title"));
				l_writer.setText("작성자 : " + rs.getString("name"));
				content.setText(rs.getString("content"));
				l_answer.setText("답변자 : " + getAnswerName(rs.getString("answer_member_id")));
				answer_content.setText(rs.getString("answer_content"));
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			board.getShopMain().getDbManager().close(pstmt,rs);
		}
		}
	}
	
	public String getAnswer() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select shop_board_answer_id from shop_board where board_id = ? ";
		String sba_id = null;
		try {
			pstmt = board.getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1, value);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				sba_id = rs.getString("shop_board_answer_id");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			board.getShopMain().getDbManager().close(pstmt, rs);
		}
		
		return sba_id;
		
	}
	
	public void updateHit(String value) {
		PreparedStatement pstmt = null;
		String sql = "update shop_board set hit = hit+1 where board_id = " + value;
		
		try {
			pstmt = board.getShopMain().getCon().prepareStatement(sql);
			int result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			board.getShopMain().getDbManager().close(pstmt);
		}
		
	}
	
	public int getMember_id(String board_id) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select m.member_id from shop_board s , shop_member m where s.board_id = " + board_id + " and s.member_id = m.member_id ";
		int member_id = 0;
		try {
			pstmt = board.getShopMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				member_id = rs.getInt("member_id");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			board.getShopMain().getDbManager().close(pstmt);
		}
		
		return member_id;
	}
	
	public int updateBoard(String board_id) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		String sql = "update shop_board set title = ? ,content = ? where board_id = ? ";
		
		try {
			pstmt = board.getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1, t_title.getText());
			pstmt.setString(2, content.getText());
			pstmt.setString(3, board_id);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			board.getShopMain().getDbManager().close(pstmt);
		}
		
		return result;
		
	}
	
	public String getAnswerName(String member_id) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String mid = "";
		String sql = "select * from  shop_board_answer a, shop_member m where a.answer_member_id  = " +member_id + "and a.answer_member_id = m.member_id";
		
		try {
			pstmt = board.getShopMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				mid = rs.getString("name");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			board.getShopMain().getDbManager().close(pstmt);
		}
		
		return mid;
	
	}
	
	public int deleteBoard() {
		PreparedStatement pstmt = null;
		String sql = "delete from shop_board where board_id = ?";
		int result = 0 ; 
		try {
			pstmt = board.getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1, value);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			board.getShopMain().getDbManager().close(pstmt);
		}
		
		return result;
		
		
	}
	
	public int deteleAnswer(String id) {
		PreparedStatement pstmt = null;
		String sql = "delete from shop_board_answer where shop_board_answer_id = ?";
		int result = 0 ; 
		try {
			pstmt = board.getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1, id);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			board.getShopMain().getDbManager().close(pstmt);
		}
		
		return result;
	}
	
}
