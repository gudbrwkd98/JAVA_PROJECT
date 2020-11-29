package com.shoppingmall.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import com.shoppingmall.member.Login;

public class AddBoard extends JPanel {
	JPanel p_content, p_t, p_i;
	JTextField t_title;
	JTextArea content;
	JScrollPane scroll;
	JLabel l_title, l_content;
	JButton bt_regist;
	JButton bt_list;
	Board board;

	public AddBoard(Board board) {
		this.board = board;
		p_content = new JPanel();
		p_t = new JPanel();
		p_i = new JPanel();
		t_title = new JTextField();
		content = new JTextArea();
		scroll = new JScrollPane(content);
		l_title = new JLabel("제목 ");
		l_content = new JLabel("내용");
		bt_regist = new JButton("글등록");
		bt_list = new JButton("목록보기");

		// 스타일
		p_t.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 100, 100));
		p_i.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 100, 500));
		l_title.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500, 50));
		t_title.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500, 25));
		l_content.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500, 50));
		scroll.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 500, 400));
		p_content.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 100, board.getShopMain().HEIGHT - 200));
		this.setPreferredSize(new Dimension(board.getShopMain().WIDTH - 100, board.getShopMain().HEIGHT - 200));
		bt_regist.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_regist.setBackground(new Color(15,76,129));
		bt_regist.setForeground(Color.WHITE);
		bt_list.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_list.setBackground(new Color(15,76,129));
		bt_list.setForeground(Color.WHITE);
		
		
		// 조립
		p_t.add(l_title);
		p_t.add(t_title);
		p_i.add(l_content);
		p_i.add(scroll);
		p_content.add(p_t);
		p_content.add(p_i);
		p_content.add(bt_regist);
		p_content.add(bt_list);

		this.add(p_content);

		bt_regist.addActionListener((e) -> {
			if (t_title.getText().isEmpty() || content.getText().isEmpty()) {
				JOptionPane.showMessageDialog(AddBoard.this, "빈칸을 채워주세요");
			} else {
				if (writeBoard() != 0) {
					JOptionPane.showMessageDialog(AddBoard.this, "글을 등록하였습니다");
					board.addRemoveContent(this, board.p_content);
					board.getBoardList();
				}
			}
		});

		bt_list.addActionListener((e) -> {
			board.addRemoveContent(this, board.p_content);
			board.getBoardList();
		});

	}

	public int writeBoard() {
		PreparedStatement pstmt = null;

		Login login = (Login) board.getShopMain().getPage()[board.getShopMain().LOGIN];
		int board_number = getLastRow();
		int result = 0;
		String sql = "insert into shop_board (board_id,board_number,title,member_id,content,hit) values (seq_shop_board.nextval,?,?,?,?,?)";

		try {
			pstmt = board.getShopMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, board_number + 1);
			pstmt.setString(2, t_title.getText());
			pstmt.setInt(3, login.getVo().getMember_id());
			pstmt.setString(4, content.getText());
			pstmt.setInt(5, 0);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			board.getShopMain().getDbManager().close(pstmt);
		}

		return result;

	}

	public int getLastRow() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT LAST_VALUE(board_number) OVER() AS board_number FROM shop_board ORDER BY board_number ASC";
		int board_number = 0;
		try {
			pstmt = board.getShopMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				board_number = rs.getInt("board_number");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			board.getShopMain().getDbManager().close(pstmt);
		}

		return board_number;
	}
}
