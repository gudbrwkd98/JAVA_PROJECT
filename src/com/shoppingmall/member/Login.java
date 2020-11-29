package com.shoppingmall.member;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

import com.shoppingmall.admin.AdminMain;
import com.shoppingmall.main.Page;
import com.shoppingmall.main.ShopMain;

public class Login extends Page{
 
	JTextField t_id;
	JPasswordField t_pass;
	JButton bt_login;
	JButton bt_regist;


	private ShopMember vo;

	public Login(ShopMain shopMain) {
		super(shopMain);
 
		setLayout(null);
		this.setBackground(Color.white);
		t_id = new JTextField();
		t_id.setText("���̵�");
		t_id.setBounds(450, 205, 268, 50);
		t_id.setForeground(Color.BLACK);
		t_id.setBackground(new Color(242,242,242));
		add(t_id);
		t_id.setColumns(10);
		t_id.addMouseListener(new MouseAdapter() {
	 
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				t_id.setText("");
			}
		});
		
		t_pass = new JPasswordField();
		t_pass.setText("000000");
		t_pass.setBounds(450, 283, 268, 50);
		t_pass.setForeground(Color.BLACK);
		t_pass.setBackground(new Color(242,242,242));
		add(t_pass);
		t_pass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				t_pass.setText("");
			}
		});
		
		
		JLabel lblNewLabel = new JLabel("LOGIN");
		lblNewLabel.setFont(new Font("���� ���", Font.BOLD, 40));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(450, 106, 268, 73);
		add(lblNewLabel);
		
		JButton bt_login = new JButton("LOGIN");
		bt_login.setFont(new Font("���� ���", Font.PLAIN, 12));
		bt_login.setBackground(new Color(15,76,129));
		bt_login.setForeground(Color.WHITE);
		bt_login.setBounds(450, 381, 268, 50);
		add(bt_login);
		
		JButton bt_regist = new JButton("ȸ������");
		bt_regist.setFont(new Font("���� ���", Font.PLAIN, 12));
		bt_regist.setBackground(new Color(15,76,129));
		bt_regist.setForeground(Color.WHITE);
		bt_regist.setBounds(450, 449, 268, 50);
		add(bt_regist);
		
	 
		
		
		
		bt_regist.addActionListener((e)->{
			getShopMain().showPage(ShopMain.MEMBER_REGIST);
		});
		
		bt_login.addActionListener((e)->{
			ShopMember vo = new ShopMember();
			vo.setMid(t_id.getText());
			vo.setPass(new String(t_pass.getPassword()));
			validcheck(vo);
		});
		
		t_id.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				 if(e.getKeyCode()==KeyEvent.VK_ENTER) {
						ShopMember vo = new ShopMember();
						vo.setMid(t_id.getText());
						vo.setPass(new String(t_pass.getPassword()));
						validcheck(vo);
				 }
			}
		});
		
		t_pass.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				 if(e.getKeyCode()==KeyEvent.VK_ENTER) {
						ShopMember vo = new ShopMember();
						vo.setMid(t_id.getText());
						vo.setPass(new String(t_pass.getPassword()));
						validcheck(vo);
				 }
			}
		});
		
	}
	//��ȿ�� üũ (�Է��������� �ʵ尡 �ִ��� ���ο� ���� �ǵ��..)
	public void validcheck(ShopMember shopMember) {
		if(shopMember.getMid().length() < 1) {//���ڿ��� ���̰� 0 �̶��..
			JOptionPane.showMessageDialog(this, "���̵� �Է��ϼ���");	
			  //������� �޼��� ������ ���´�()
		}else if(shopMember.getPass().length() < 1) {
			JOptionPane.showMessageDialog(this, "��й�ȣ��  �Է��ϼ���");	
			 //������� �޼��� ������ ���´�()
		}else {
			 vo = login(shopMember);
			if(vo==null) {
				JOptionPane.showMessageDialog(this, "�α��� ������ �ùٸ��� �ʽ��ϴ�");
			}else{
				JOptionPane.showMessageDialog(this, "�α��� ����");
				if(vo.getLevel() == 1) {
				//home ������ �����ֱ�!!
				getShopMain().showPage(getShopMain().HOME);
				//��ư�� ���� �α׾ƿ����� ��ȣ��
				getShopMain().navi[4].setText("LOG OUT");
				getShopMain().navi[4].setBackground(new Color(15,76,129));
				getShopMain().navi[4].setForeground(Color.WHITE);
				getShopMain().setHasSession(true);//�α��� �������� �˼��ִ� ��
				
				}else {
					
					AdminMain a = new AdminMain();
					a.setMember_id(vo.getMember_id());
				}
				
				t_id.setText("");
				t_pass.setText("");
			}
		}
	}
	
	
	//ȸ�� �α��� ó�� �޼��� ���� �α��� ������ Home�� ������ ������
	public ShopMember login(ShopMember shopMember) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		vo = null; //�α��� ������ ȸ���� ��� ������ ��Ե� VO

		String sql = "select * from shop_member where mid = ? and pass = ?";
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1,shopMember.getMid());
			pstmt.setString(2,shopMember.getPass());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				vo = new ShopMember();
				vo.setMember_id(rs.getInt("member_id"));	
				vo.setMid(rs.getString("mid"));	
				vo.setPass(rs.getString("pass"));	
				vo.setName(rs.getString("name"));	
				vo.setPhone(rs.getString("phone"));	
				vo.setEmail(rs.getString("email"));	
				vo.setAddress(rs.getString("address"));
				vo.setLevel(Integer.parseInt(rs.getString("lvl")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			getShopMain().getDbManager().close(pstmt,rs);
		}
		
		
		return vo;
		
	}
	
	public ShopMember getVo() {
		return vo;
	}
	public void setVo(ShopMember vo) {
		this.vo = vo;
	}
	
	//�α׾ƿ�ó��
	public void logout() {
		getShopMain().navi[4].setText("login");
		getShopMain().navi[4].setBackground(new Color(15,76,129));
		getShopMain().navi[4].setForeground(Color.white);
		getShopMain().setHasSession(false);
		getShopMain().showPage(ShopMain.HOME);
	}
	
}
