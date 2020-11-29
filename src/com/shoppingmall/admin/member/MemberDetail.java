package com.shoppingmall.admin.member;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;



public class MemberDetail extends JPanel{
	JPanel p_content;
	JTextField t_mid;
	JPasswordField t_pass;
	JTextField t_name;
	JTextField t_phone;
	JTextField t_email;
	JButton bt_update;
	JButton bt_menu;
	Member member;
	JTextField t_address;
	JButton bt_delete;
	String[] lvl = {"È¸¿ø","°ü¸®ÀÚ"};
	JComboBox comboBox ;
	
	public MemberDetail(Member member,String value) {
		this.setLayout(new BorderLayout());
		this.member = member;
		p_content = new JPanel();
		p_content.setPreferredSize(new Dimension(member.getAdminMain().WIDTH-100,member.getAdminMain().HEIGHT-200 ));
		p_content.setBackground(Color.WHITE);
		p_content.setBorder(new LineBorder(new Color(15,76,129), 2));
		p_content.setBounds(400, 120, 450, 450);
		add(p_content);
		p_content.setLayout(null);
		
		
		t_mid = new JTextField();
		t_mid.setBounds(482, 123, 213, 34);
		p_content.add(t_mid);
		t_mid.setColumns(10);
		t_mid.setEditable(false);
		
		t_pass = new JPasswordField();
		t_pass.setBounds(482, 183, 213, 36);
		p_content.add(t_pass);
		t_pass.setColumns(10);
		
		t_name = new JTextField();
		t_name.setBounds(482, 243, 213, 34);
		p_content.add(t_name);
		t_name.setColumns(10);
		
		t_phone = new JTextField("010");
		t_phone.setBounds(482, 303, 213, 34);
		p_content.add(t_phone);
		t_phone.setColumns(10);
		
		t_email = new JTextField();
		t_email.setBounds(482, 363, 213, 34);
		p_content.add(t_email);
		t_email.setColumns(10);
		
		
		comboBox = new JComboBox(lvl);
		comboBox.setBounds(480, 423, 213, 25);
		p_content.add(comboBox);
		
		t_address = new JTextField();
		t_address.setBounds(482, 483, 213, 34);
		p_content.add(t_address);
		t_address.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("¾ÆÀÌµð : ",SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		lblNewLabel.setBounds(330, 120, 140, 25);
		p_content.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("ºñ¹Ð¹øÈ£ : ",SwingConstants.RIGHT);
		lblNewLabel_1.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(330, 180, 140, 25);
		p_content.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("ÀÌ¸§ : ",SwingConstants.RIGHT);
		lblNewLabel_2.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(330, 240, 140, 25);
		p_content.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("ÇÚµåÆù ¹øÈ£ : ",SwingConstants.RIGHT);
		lblNewLabel_3.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(330, 300, 140, 25);
		p_content.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("ÀÌ¸ÞÀÏ : ",SwingConstants.RIGHT);
		lblNewLabel_4.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(330, 360, 140, 23);
		p_content.add(lblNewLabel_4);
		
		
		JLabel lblNewLabel_5 = new JLabel("Á¢±Ù±ÇÇÑ : ",SwingConstants.RIGHT);
		lblNewLabel_5.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(330, 420, 140, 25);
		p_content.add(lblNewLabel_5);
		
		JLabel address = new JLabel("ÁÖ¼Ò : ",SwingConstants.RIGHT);
		address.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		address.setBounds(330, 485, 140, 23);
		p_content.add(address);
		
		JButton bt_regist = new JButton("Á¤º¸¼öÁ¤");
		bt_regist.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		bt_regist.setBackground(new Color(15,76,129));
		bt_regist.setForeground(Color.WHITE);
		bt_regist.setBounds(575, 550, 122, 34);
		p_content.add(bt_regist);
		
		JButton btnNewButton_1 = new JButton("¸ñ·ÏÀ¸·Î");
		btnNewButton_1.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		btnNewButton_1.setBackground(new Color(15,76,129));
		btnNewButton_1.setForeground(Color.WHITE);
		btnNewButton_1.setBounds(425, 550, 122, 34);
		p_content.add(btnNewButton_1);
		
		
		bt_delete = new JButton("»èÁ¦ÇÏ±â");
		bt_delete.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		bt_delete.setBackground(new Color(15,76,129));
		bt_delete.setForeground(Color.WHITE);
		bt_delete.setBounds(650, 550, 122, 34);
		//p_content.add(bt_delete);
		
		JLabel lblNewLabel_6 = new JLabel("È¸¿ø Á¤º¸ ¼öÁ¤",SwingConstants.CENTER);
		lblNewLabel_6.setBounds(380, 50, 285, 57);
		add(lblNewLabel_6);
		lblNewLabel_6.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 22));

		t_phone.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String value = t_phone.getText();
				int l = value.length();
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
					t_phone.setEditable(true);

				} else {
					t_phone.setEditable(false);

				}
			}
		});

		this.add(p_content);
		
		getDetail(value);
		
		bt_regist.addActionListener((e)->{
			if(update(value) != 0) {
				JOptionPane.showMessageDialog(MemberDetail.this, "Á¤º¸¸¦ ¼öÁ¤ÇÏ¿´½À´Ï´Ù");
			member.getMemberList();
			member.addRemoveContent(this, member.p_center);
			}
		});
		
		btnNewButton_1.addActionListener((e)->{
			member.addRemoveContent(this, member.p_center);
		});
		
//		bt_delete.addActionListener((e)->{
//			if(deleteUser(value) != 0) {
//				JOptionPane.showMessageDialog(MemberDetail.this, "À¯Àú¸¦ »èÁ¦ÇÏ¿´½À´Ï´Ù");
//			member.getMemberList();
//			member.addRemoveContent(this, member.p_center);
//			}
//		});
 

	}
	
	
	public void getDetail(String value) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from shop_member where member_id = " + value ;
		
		try {
			pstmt = member.getAdminMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
			t_mid.setText(rs.getString("mid"));
			t_pass.setText(rs.getString("pass"));
			t_name.setText(rs.getString("name"));
			t_phone.setText(rs.getString("phone"));
			t_email.setText(rs.getString("email"));
			t_address.setText(rs.getString("address"));
			comboBox.setSelectedIndex(rs.getInt("lvl")-1);
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			member.getAdminMain().getDbManager().close(pstmt,rs);
		}
		
	
	}
	
	
	public int update(String value) {
		PreparedStatement pstmt=null;
		String sql = "update shop_member set pass = ? ,name =?,phone =?,email=?,address = ?where member_id  = ? ";
		int result = 0;
		try {
			pstmt = member.getAdminMain().getCon().prepareStatement(sql);
			pstmt.setString(1, new String(t_pass.getPassword()));
			pstmt.setString(2, t_name.getText());
			pstmt.setInt(3,Integer.parseInt(t_phone.getText()));
			pstmt.setString(4, t_email.getText());
			pstmt.setString(5, t_address.getText());
			pstmt.setInt(6,Integer.parseInt(value));
		
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			member.getAdminMain().getDbManager().close(pstmt);
		}
		
		return result ;
		
		
	}
	
	public int deleteUser(String value) {
		PreparedStatement pstmt = null;
		String sql = "delete from shop_member where member_id = ?";
		int result = 0;
		try {
			pstmt = member.getAdminMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(value));
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			member.getAdminMain().getDbManager().close(pstmt);
		}
		
		return result;
	}
	
 
	
 
	
	
	

}
