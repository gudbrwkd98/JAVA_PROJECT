package com.shoppingmall.member;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

import com.shoppingmall.admin.order.Order;
import com.shoppingmall.main.Page;
import com.shoppingmall.main.ShopMain;

public class MyPage extends Page{
	JPanel p_content;
	JPanel p_check;
	JTextField t_mid;
	JPasswordField t_pass;
	JTextField t_name;
	JTextField t_phone;
	JTextField t_email;
	JTextField t_address;
	JPasswordField t_confirmPass;
	JLabel l_mid,l_pass,l_name,l_phone,l_email,l_confirmPass;
	JButton bt_update;
	JButton bt_confirmPass;
	ShopMember memberVO; 
	JLabel lblNewLabel_6;
	private MyOrder order;


	private boolean flag = false;


	public MyPage(ShopMain shopMain) {
		super(shopMain);

		p_content = new JPanel();
		p_check = new JPanel();
		p_check.setPreferredSize(new Dimension(getShopMain().WIDTH-100,getShopMain().HEIGHT-100 ));
		p_check.setBackground(Color.white);
		p_check.setLayout(null);
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(15,76,129)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(288, 63, 524, 423);
		panel.setLayout(null);
		p_check.add(panel);
		
		
		t_confirmPass = new JPasswordField();
		t_confirmPass.setBounds(200, 148, 266, 41);
		t_confirmPass.setBackground(new Color(242,242,242));
		panel.add(t_confirmPass);
		t_confirmPass.setColumns(10);
		
		JLabel l_confirmPass = new JLabel("비밀번호 : ");
		l_confirmPass.setBounds(100, 148, 84, 41);
		panel.add(l_confirmPass);
		
		JButton bt_confirmPass = new JButton("인증확인");
		bt_confirmPass.setBounds(200, 217, 266, 35);
		bt_confirmPass.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_confirmPass.setBackground(new Color(15,76,129));
		bt_confirmPass.setForeground(Color.WHITE);
		panel.add(bt_confirmPass);
		
		JLabel la_cpass = new JLabel("비밀번호 확인",SwingConstants.CENTER);
		la_cpass.setBounds(150, 50, 285, 57);
		la_cpass.setFont(new Font("맑은 고딕", Font.PLAIN, 22));
		panel.add(la_cpass);
		
		p_content = new JPanel();
		p_content.setPreferredSize(new Dimension(getShopMain().WIDTH-80,getShopMain().HEIGHT-200 ));
		p_content.setBackground(Color.WHITE);
		p_content.setBorder(new LineBorder(new Color(15,76,129), 2));
		p_content.setBounds(400, 120, 450, 450);
		p_content.setLayout(null);
		
		
		t_mid = new JTextField();
		t_mid.setBounds(482, 123, 213, 34);
		p_content.add(t_mid);
		t_mid.setColumns(10);
		
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
		
		t_address = new JTextField();
		t_address.setBounds(482, 423, 213, 34);
		p_content.add(t_address);
		t_address.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("아이디 : ",SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNewLabel.setBounds(330, 120, 140, 25);
		p_content.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("비밀번호 : ",SwingConstants.RIGHT);
		lblNewLabel_1.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(330, 180, 140, 25);
		p_content.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("이름 : ",SwingConstants.RIGHT);
		lblNewLabel_2.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(330, 240, 140, 25);
		p_content.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("핸드폰 번호 : ",SwingConstants.RIGHT);
		lblNewLabel_3.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(330, 300, 140, 25);
		p_content.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("이메일 : ",SwingConstants.RIGHT);
		lblNewLabel_4.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(330, 360, 140, 23);
		p_content.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("주소 : ",SwingConstants.RIGHT);
		lblNewLabel_5.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(330, 420, 140, 23);
		p_content.add(lblNewLabel_5);
		
		JButton bt_update = new JButton("정보수정");
		bt_update.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_update.setBackground(new Color(15,76,129));
		bt_update.setForeground(Color.WHITE);
		bt_update.setBounds(550, 550, 122, 34);
		p_content.add(bt_update);
		
		JButton bt_orderlist = new JButton("주문기록");
		bt_orderlist.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_orderlist.setBackground(new Color(15,76,129));
		bt_orderlist.setForeground(Color.WHITE);
		bt_orderlist.setBounds(350, 550, 122, 34);
		p_content.add(bt_orderlist);
		
		lblNewLabel_6 = new JLabel("회원 정보 수정 / 주문 기록",SwingConstants.CENTER);
		lblNewLabel_6.setBounds(380, 50, 285, 57);
		lblNewLabel_6.setFont(new Font("맑은 고딕", Font.PLAIN, 22));
		



		bt_update.addActionListener((e)->{
	 
			ShopMember vo = new ShopMember();
			vo.setPass(new String(t_pass.getPassword()));	
			vo.setName(t_name.getText());	
			vo.setPhone(t_phone.getText());	
			vo.setEmail(t_email.getText());	
			vo.setAddress(t_address.getText());
	
			if (updateInfo(vo) == 0) {
				JOptionPane.showMessageDialog(MyPage.this, "실패");

			} else {
				JOptionPane.showMessageDialog(MyPage.this, "성공");
				setFlag(false);
				this.remove(p_content);
				this.remove(lblNewLabel_6);
				this.add(p_check);
				this.updateUI();
				}
 
			
		});
		
		bt_confirmPass.addActionListener((e)->{
			if(checkPass() == true) {
					t_confirmPass.setText("");
					this.remove(p_check);
					this.add(lblNewLabel_6);
					this.add(p_content);
					this.updateUI();
					
			}else {
					JOptionPane.showMessageDialog(MyPage.this, "비밀번호가 틀립니다.");
				}
		});
		
		bt_orderlist.addActionListener((e)->{
			order = new MyOrder(this);
			
			addRemoveContent(p_content,order);
 
		});
		
		 
 
			this.add(p_check);
	 
 
 
 
	}
	
	public void setPage() {
		setFlag(false);
		this.remove(p_content);
		this.remove(lblNewLabel_6);
		this.add(p_check);
		this.updateUI();
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
	
	public boolean checkPass(){
		Login login =  (Login) getShopMain().getPage()[ShopMain.LOGIN];//로그인 페이지에 접근
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from shop_member where member_id = ? AND pass = ?";
		flag = false;
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, login.getVo().getMember_id());
			pstmt.setString(2, new String(t_confirmPass.getPassword())); 
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				memberVO = new ShopMember();
				
				memberVO.setMid(rs.getString("mid"));
				memberVO.setPass(rs.getString("pass"));
				memberVO.setName(rs.getString("name"));
				memberVO.setPhone(rs.getString("phone"));
				memberVO.setEmail(rs.getString("email"));
				memberVO.setMember_id(rs.getInt("member_id"));
				memberVO.setAddress(rs.getString("address"));
				
				t_mid.setText(memberVO.getMid());   
				t_pass.setText(memberVO.getPass());  
				t_name.setText(memberVO.getName());  
				t_phone.setText(memberVO.getPass());  
				t_email.setText(memberVO.getEmail());  
				t_address.setText(memberVO.getAddress());
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
		Login login =  (Login) getShopMain().getPage()[ShopMain.LOGIN];//로그인 페이지에 접근
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
		Login login =  (Login) getShopMain().getPage()[ShopMain.LOGIN];//로그인 페이지에 접근
		// 멤버변수 자동초기화와는 달리 메서드의 지역변수는 컴파일러가 초기화 하지않으므로 반드시 개발자가 초기화해야한다..
		PreparedStatement pstmt = null;
		String sql = "update shop_member set pass=?,name=?,phone=?,email=?,address = ?where member_id = ?  ";
		int result = 0;
		try {
			pstmt = getShopMain().getCon().prepareStatement(sql);
			pstmt.setString(1, shopMember.getPass());
			pstmt.setString(2, shopMember.getName());
			pstmt.setString(3, shopMember.getPhone());
			pstmt.setString(4, shopMember.getEmail());
			pstmt.setString(5, shopMember.getAddress());
			pstmt.setInt(6,login.getVo().getMember_id());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getShopMain().getDbManager().close(pstmt);
		}

		return result;

	}
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public MyOrder getOrder() {
		return order;
	}

	public void setOrder(MyOrder order) {
		this.order = order;
	}
	
	
	// 보여질 컨텐트와 가려질 컨텐트를 제어하는 메서드
	public void addRemoveContent(Component removeObj, Component addObj) {
		this.remove(removeObj);
		this.add(addObj);
		((JPanel) addObj).updateUI();

	}
	
}
