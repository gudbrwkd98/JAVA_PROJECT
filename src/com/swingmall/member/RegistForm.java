package com.swingmall.member;

import java.awt.Color;
import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.swingmall.main.Page;
import com.swingmall.main.ShopMain;

public class RegistForm extends Page {
	JPanel p_content;
	JTextField t_mid;
	JPasswordField t_pass;
	JTextField t_name;
	JTextField t_phone;
	JTextField t_email;
	JButton bt_regist;
	

	public RegistForm(ShopMain shopMain) {
		super(shopMain);

		p_content = new JPanel();

		t_mid = new JTextField("아이디");
		t_pass = new JPasswordField("비밀번호");
		t_name = new JTextField("이름");
		t_phone = new JTextField("전화번호");
		t_email = new JTextField("이메일");
		bt_regist = new JButton("등록하기");

		// 스타일
		Dimension d = new Dimension(280, 25);
		p_content.setPreferredSize(new Dimension(400, 200));
		p_content.setBackground(Color.WHITE);
		t_mid.setPreferredSize(d);
		t_pass.setPreferredSize(d);
		t_name.setPreferredSize(d);
		t_phone.setPreferredSize(d);
		t_email.setPreferredSize(d);
		bt_regist.setPreferredSize(new Dimension(200, 30));

		p_content.add(t_mid);
		p_content.add(t_pass);
		p_content.add(t_name);
		p_content.add(t_phone);
		p_content.add(t_email);
		p_content.add(bt_regist);

		this.add(p_content);

		bt_regist.addActionListener((e) -> {
			if(checkId(t_mid.getText())){
				JOptionPane.showMessageDialog(RegistForm.this, "중복된 아이디 입니다");		
			}else {
			ShopMember vo = new ShopMember();
			vo.setMid(t_mid.getText());	
			vo.setPass(new String(t_pass.getPassword()));	
			vo.setName(t_name.getText());	
			vo.setPhone(t_phone.getText());	
			vo.setEmail(t_email.getText());	
			if (regist(vo) == 0) {
				JOptionPane.showMessageDialog(RegistForm.this, "실패");
			} else {
				JOptionPane.showMessageDialog(RegistForm.this, "성공");
			}
		}
			

		});

	}

	// 회원존재여부 체크
	public boolean checkId(String mid) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from shop_member where mid = ?";
		boolean flag = false;
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1, mid);
			rs = pstmt.executeQuery();
			flag = rs.next(); //레코드가 존재하면 true 아니면 false 대입

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getShopMain().getDbManager().close(pstmt, rs);
		}

		return flag;

	}

	// 회원등록
	public int regist(ShopMember shopMember) {
		// 멤버변수 자동초기화와는 달리 메서드의 지역변수는 컴파일러가 초기화 하지않으므로 반드시 개발자가 초기화해야한다..
		PreparedStatement pstmt = null;
		String sql = "insert into shop_member (member_id,mid,pass,name,phone,email) values (seq_shop_member.nextval,?,?,?,?,?) ";
		int result = 0;
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1, shopMember.getMid());
			pstmt.setString(2, shopMember.getPass());
			pstmt.setString(3, shopMember.getName());
			pstmt.setString(4, shopMember.getPhone());
			pstmt.setString(5, shopMember.getEmail());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getShopMain().getDbManager().close(pstmt);
		}

		return result;

	}

}
