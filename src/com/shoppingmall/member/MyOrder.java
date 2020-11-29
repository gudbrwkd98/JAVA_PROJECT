package com.shoppingmall.member;

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

import com.shoppingmall.admin.order.Order;
import com.shoppingmall.admin.order.OrderDetail;
import com.shoppingmall.admin.order.OrderModel;
import com.shoppingmall.admin.order.OrderVO;
import com.shoppingmall.main.ShopMain;

public class MyOrder extends JPanel{
	JPanel p_center;
	JTable table;
	JScrollPane scroll;
	MyOrderModel model;
	MyPage mypage;
	JButton bt_back;
	private MyOrderDetail order;

	public MyOrder(MyPage mypage) {
		this.mypage = mypage;
		
		p_center = new JPanel();
		table = new JTable();
		scroll  = new JScrollPane(table);
		bt_back = new JButton("마이 페이지");
	 
		//스타일
		table.getTableHeader().setFont(new Font("맑은 고딕",Font.BOLD,12));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().	setBackground(new Color(15, 76, 129));
		table.getTableHeader().setForeground(new Color(255,255,255));
		table.setRowHeight(25);
		scroll.setPreferredSize(new Dimension(ShopMain.WIDTH-180,ShopMain.HEIGHT-200));
		setLayout(new BorderLayout());
		bt_back.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_back.setBackground(new Color(15,76,129));
		bt_back.setForeground(Color.WHITE);
		
		p_center.add(scroll);
		p_center.add(bt_back);
		add(p_center);
		getOrder(Integer.toString(mypage.memberVO.getMember_id()));
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				 order = new MyOrderDetail(MyOrder.this,(String) table.getValueAt(table.getSelectedRow(), 0),(String) table.getValueAt(table.getSelectedRow(), 2),(String) table.getValueAt(table.getSelectedRow(), 4));
				
				addRemoveContent(p_center,order);
			}
		});
		
		bt_back.addActionListener((e)->{
			mypage.addRemoveContent(this.p_center, mypage.p_content);
			this.p_center.setVisible(false);
			mypage.p_content.setVisible(true);
		});
		
	}
	
		public void setPage() {
			mypage.addRemoveContent(this.p_center, mypage.p_content);
			this.p_center.setVisible(false);
			mypage.p_content.setVisible(true);
		}
	
	//주문목록 가져오기 
		public void getOrder(String member_id) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = "select r.receipt_id,r.paid_p,r.member_id,m.name,r.order_date,r.status from receipt r, shop_member m where r.member_id = ? AND  r.member_id = m.member_id ORDER BY r.order_date desc";
			
			try {
				pstmt =  mypage.getShopMain().getCon().prepareStatement(sql);
				pstmt.setString(1, member_id);
				rs = pstmt.executeQuery();
				

				
				//rs의 레코드를 orderModel의 record ArrayList에 채우기
				ArrayList<MyOrderVO> orderList = new ArrayList<MyOrderVO>();
				while(rs.next()) {
					MyOrderVO vo = new MyOrderVO();
					vo.setReceipt_id(rs.getInt("receipt_id"));
					vo.setPaid_p(rs.getInt("paid_p"));
					vo.setMember_id(rs.getInt("member_id"));
					vo.setName(rs.getString("name"));
					vo.setOrder_date(rs.getString("order_date"));
					vo.setStatus(rs.getString("status"));
					
					orderList.add(vo);
				}
				
				model = new MyOrderModel();
				model.record = orderList;
				table.setModel(model);
				table.updateUI();
				 
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				mypage.getShopMain().getDbManager().close(pstmt,rs);
			}
			
			
			
		}
		
		public MyOrderDetail getOrderDetail() {
			return this.order;
		}
		
		public static boolean isNull(MyOrderDetail order) {
			return order != null;
		}
		


		public void setOrderDetail(MyOrderDetail order) {
			this.order = order;
		}

		
		//보여질 컨텐트와 가려질 컨텐트를 제어하는 메서드
		public void addRemoveContent(Component removeObj,Component addObj) {
			this.remove(removeObj);
			this.add(addObj);
			((JPanel)addObj).updateUI();
			
		}
	
}
