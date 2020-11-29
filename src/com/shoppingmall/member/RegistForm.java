package com.shoppingmall.member;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.shoppingmall.main.Page;
import com.shoppingmall.main.ShopMain;

public class RegistForm extends Page {
	JPanel p_content;
	JTextField t_mid;
	JPasswordField t_pass;
	JTextField t_name;
	JTextField t_phone;
	JTextField t_email;
	JTextField t_address;
	JButton bt_regist;

	public RegistForm(ShopMain shopMain) {
		super(shopMain);
		setBackground(new Color(255, 255, 255));
		setSize(1200, 800);
		setLayout(null);

		p_content = new JPanel();
		p_content.setBackground(Color.white);
		p_content.setBorder(new LineBorder(new Color(15, 76, 129), 2));
		p_content.setBounds(400, 120, 450, 550);
		add(p_content);
		p_content.setLayout(null);

		t_mid = new JTextField();
		t_mid.setBounds(175, 53, 213, 34);
		p_content.add(t_mid);
		t_mid.setColumns(10);

		t_pass = new JPasswordField();
		t_pass.setBounds(175, 119, 213, 36);
		p_content.add(t_pass);
		t_pass.setColumns(10);

		t_name = new JTextField();
		t_name.setBounds(175, 179, 213, 34);
		p_content.add(t_name);
		t_name.setColumns(10);

		t_phone = new JTextField("010");
		t_phone.setBounds(175, 235, 213, 34);
		p_content.add(t_phone);
		t_phone.setColumns(10);
		t_phone.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (t_phone.getText().length() >= 11) { // limit to 3 characters
					e.consume();
				}
			}
		});

		t_email = new JTextField();
		t_email.setBounds(175, 293, 213, 34);
		p_content.add(t_email);
		t_email.setColumns(10);

		t_address = new JTextField();
		t_address.setBounds(175, 358, 213, 34);
		p_content.add(t_address);
		t_address.setColumns(10);

		JLabel lblNewLabel = new JLabel("아이디 : ", SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNewLabel.setBounds(23, 56, 140, 25);
		p_content.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("비밀번호 : ", SwingConstants.RIGHT);
		lblNewLabel_1.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(23, 123, 140, 25);
		p_content.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("이름 : ", SwingConstants.RIGHT);
		lblNewLabel_2.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(23, 182, 140, 25);
		p_content.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("핸드폰 번호 : ", SwingConstants.RIGHT);
		lblNewLabel_3.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(23, 238, 140, 25);
		p_content.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("이메일 : ", SwingConstants.RIGHT);
		lblNewLabel_4.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(23, 297, 140, 23);
		p_content.add(lblNewLabel_4);

		JLabel address = new JLabel("주소 : ", SwingConstants.RIGHT);
		address.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		address.setBounds(23, 361, 140, 23);
		p_content.add(address);

		JButton bt_regist = new JButton("가입하기");
		bt_regist.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_regist.setBackground(new Color(15, 76, 129));
		bt_regist.setForeground(Color.WHITE);
		bt_regist.setBounds(266, 450, 122, 34);
		p_content.add(bt_regist);

		JButton btnNewButton_1 = new JButton("로그인 창으로");
		btnNewButton_1.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		btnNewButton_1.setBackground(new Color(15, 76, 129));
		btnNewButton_1.setForeground(Color.WHITE);
		btnNewButton_1.setBounds(82, 450, 122, 34);
		p_content.add(btnNewButton_1);

		JLabel lblNewLabel_6 = new JLabel("회원가입", SwingConstants.CENTER);
		lblNewLabel_6.setBounds(484, 53, 285, 57);
		add(lblNewLabel_6);
		lblNewLabel_6.setFont(new Font("맑은 고딕", Font.PLAIN, 22));

		bt_regist.addActionListener((e) -> {

			if (t_mid.getText().isEmpty() || t_pass.getPassword().length == 0 || t_name.getText().isEmpty()
					|| t_phone.getText().isEmpty() || t_email.getText().isEmpty() || t_address.getText().isEmpty()) {
				JOptionPane.showMessageDialog(RegistForm.this, "빈칸을 채워주세요");
			} else {

				if (!t_email.getText().contains("@")) {
					JOptionPane.showMessageDialog(null, "이메일을 제대로 입력해주세요");
				} else {
					if (checkId(t_mid.getText())) {
						JOptionPane.showMessageDialog(RegistForm.this, "중복된 아이디 입니다");
					} else if (checkEmail(t_email.getText())) {
						JOptionPane.showMessageDialog(RegistForm.this, "중복된 이메일 입니다");
					} else {
						ShopMember vo = new ShopMember();
						vo.setMid(t_mid.getText());
						vo.setPass(new String(t_pass.getPassword()));
						vo.setName(t_name.getText());
						vo.setPhone(t_phone.getText());
						vo.setEmail(t_email.getText());
						vo.setAddress(t_address.getText());
						if (regist(vo) == 0) {
							JOptionPane.showMessageDialog(RegistForm.this, "실패");
						} else {
							JOptionPane.showMessageDialog(RegistForm.this, "성공");
							t_mid.setText("");
							t_pass.setEchoChar('*');
							t_name.setText("");
							t_phone.setText("");
							t_email.setText("");
							t_address.setText("");
							getShopMain().showPage(shopMain.LOGIN);
						}
					}
				}
			}
		});
		
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

		btnNewButton_1.addActionListener((e) -> {
			getShopMain().showPage(shopMain.LOGIN);
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
			flag = rs.next(); // 레코드가 존재하면 true 아니면 false 대입

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getShopMain().getDbManager().close(pstmt, rs);
		}

		return flag;

	}

	// 회원이메일존재여부 체크
	public boolean checkEmail(String email) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from shop_member where email = ?";
		boolean flag = false;
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			flag = rs.next(); // 레코드가 존재하면 true 아니면 false 대입

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
		String sql = "insert into shop_member (member_id,mid,pass,name,phone,email,address) values (seq_shop_member.nextval,?,?,?,?,?,?) ";
		int result = 0;
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1, shopMember.getMid());
			pstmt.setString(2, shopMember.getPass());
			pstmt.setString(3, shopMember.getName());
			pstmt.setString(4, shopMember.getPhone());
			pstmt.setString(5, shopMember.getEmail());
			pstmt.setString(6, shopMember.getAddress());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getShopMain().getDbManager().close(pstmt);
		}

		return result;

	}

}
