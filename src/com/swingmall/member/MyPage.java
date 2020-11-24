package com.swingmall.member;

import java.awt.Color;
import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.swingmall.main.Page;
import com.swingmall.main.ShopMain;

public class MyPage extends Page{
	JPanel p_content;
	JPanel p_check;
	JTextField t_mid;
	JPasswordField t_pass;
	JTextField t_name;
	JTextField t_phone;
	JTextField t_email;
	JPasswordField t_confirmPass;
	JLabel l_mid,l_pass,l_name,l_phone,l_email,l_confirmPass;
	JButton bt_update;
	JButton bt_confirmPass;
	public MyPage(ShopMain shopMain) {
		super(shopMain);

		p_content = new JPanel();
		p_check = new JPanel();
		
		t_confirmPass = new JPasswordField();
		l_confirmPass = new JLabel("��й�ȣ Ȯ��");
		
		bt_confirmPass = new JButton("Ȯ��");
		
		p_check.add(l_confirmPass);
		p_check.add(t_confirmPass);
		p_check.add(bt_confirmPass);
		
		t_mid = new JTextField() ;
		t_pass = new JPasswordField();
		t_name = new JTextField();
		t_phone = new JTextField();
		t_email = new JTextField();
	
 
		l_mid = new JLabel("���̵� :") ;
		l_pass = new JLabel("��й�ȣ : ");
		l_name = new JLabel("�̸� : ");
		l_phone = new JLabel("��ȭ��ȣ : ");
		l_email = new JLabel("�̸��� : ");
		
		bt_update = new JButton("���� ����");

		// ��Ÿ��
		Dimension d = new Dimension(280, 25);
		p_content.setPreferredSize(new Dimension(380, 400));
		p_content.setBackground(Color.WHITE);
		t_mid.setPreferredSize(d);
		t_mid.setEditable(false);
		t_pass.setPreferredSize(d);
		t_name.setPreferredSize(d);
		t_phone.setPreferredSize(d);
		t_email.setPreferredSize(d);
		t_confirmPass.setPreferredSize(d);
		Dimension d2 = new Dimension(80, 25);
		l_mid.setPreferredSize(d2);
		l_pass.setPreferredSize(d2);
		l_name.setPreferredSize(d2);
		l_phone.setPreferredSize(d2);
		l_email.setPreferredSize(d2);
		
		bt_update.setPreferredSize(new Dimension(200, 30));
		bt_confirmPass.setPreferredSize(new Dimension(100, 30));
		
		
		
		p_content.add(l_mid);
		p_content.add(t_mid);
		
		p_content.add(l_pass);
		p_content.add(t_pass);
		
		p_content.add(l_name);
		p_content.add(t_name);
		
		p_content.add(l_phone);
		p_content.add(t_phone); 


		p_content.add(l_email);
		p_content.add(t_email);
	
		
		p_content.add(bt_update);

		bt_update.addActionListener((e)->{
	 
			ShopMember vo = new ShopMember();
			vo.setPass(new String(t_pass.getPassword()));	
			vo.setName(t_name.getText());	
			vo.setPhone(t_phone.getText());	
			vo.setEmail(t_email.getText());	
	
			if (updateInfo(vo) == 0) {
				JOptionPane.showMessageDialog(MyPage.this, "����");
			} else {
				JOptionPane.showMessageDialog(MyPage.this, "����");
				}
 
			
		});
		
		bt_confirmPass.addActionListener((e)->{
			if(checkPass() == true) {
					this.remove(p_check);
					this.add(p_content);
					this.updateUI();
					
			}else {
					JOptionPane.showMessageDialog(MyPage.this, "��й�ȣ�� Ʋ���ϴ�.");
				}
		});
		
		this.add(p_check);
 
 
 
	}
	
	// ȸ�����翩�� üũ
	public boolean checkId(String mid) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from shop_member where mid = ?";
		boolean flag = false;
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1, mid);
			rs = pstmt.executeQuery();
			flag = rs.next(); //���ڵ尡 �����ϸ� true �ƴϸ� false ����

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getShopMain().getDbManager().close(pstmt, rs);
		}

		return flag;

	}
	
	public boolean checkPass(){
		Login login =  (Login) getShopMain().getPage()[ShopMain.LOGIN];//�α��� �������� ����
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from shop_member where member_id = ? AND pass = ?";
		boolean flag = false;
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, login.getVo().getMember_id());
			pstmt.setString(2, new String(t_confirmPass.getPassword())); 
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				t_mid.setText(rs.getString("mid"));   
				t_pass.setText(rs.getString("pass"));  
				t_name.setText(rs.getString("name"));  
				t_phone.setText(rs.getString("phone"));  
				t_email.setText(rs.getString("email"));  
				flag = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			getShopMain().getDbManager().close(pstmt,rs);
		}
		
		return flag;
	}
	
	public void getUserInfo() {
		Login login =  (Login) getShopMain().getPage()[ShopMain.LOGIN];//�α��� �������� ����
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from shop_member where member_id = ?";
		
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, login.getVo().getMember_id());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				t_mid.setText(rs.getString("mid"));   
				t_pass.setText(rs.getString("pass"));  
				t_name.setText(rs.getString("name"));  
				t_phone.setText(rs.getString("phone"));  
				t_email.setText(rs.getString("email"));  
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			getShopMain().getDbManager().close(pstmt,rs);
		}
		
	}
	
	
 
	public int updateInfo(ShopMember shopMember) {
		Login login =  (Login) getShopMain().getPage()[ShopMain.LOGIN];//�α��� �������� ����
		// ������� �ڵ��ʱ�ȭ�ʹ� �޸� �޼����� ���������� �����Ϸ��� �ʱ�ȭ ���������Ƿ� �ݵ�� �����ڰ� �ʱ�ȭ�ؾ��Ѵ�..
		PreparedStatement pstmt = null;
		String sql = "update shop_member set pass=?,name=?,phone=?,email=? where member_id = ?  ";
		int result = 0;
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1, shopMember.getPass());
			pstmt.setString(2, shopMember.getName());
			pstmt.setString(3, shopMember.getPhone());
			pstmt.setString(4, shopMember.getEmail());
			pstmt.setInt(5,login.getVo().getMember_id());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getShopMain().getDbManager().close(pstmt);
		}

		return result;

	}

	
}
