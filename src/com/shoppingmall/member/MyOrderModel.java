package com.shoppingmall.member;

import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.shoppingmall.admin.order.OrderVO;


public class MyOrderModel extends AbstractTableModel{
	ArrayList<MyOrderVO> record = new ArrayList<MyOrderVO>();
	
	String[] column = {"Receipt_id","주문 가격","Member_id","이름","배송 현황","주문날짜"};
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return record.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return column.length;
	}
	
	@Override
	public String getColumnName(int col) {
		return column[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		NumberFormat myFormat = NumberFormat.getInstance(); 
		myFormat.setGroupingUsed(true);
		MyOrderVO vo = record.get(row);
		String obj = null;
		if(col == 0) {
				obj  = Integer.toString(vo.getReceipt_id()); 
		}else if (col == 1 ) {
				obj = myFormat.format(vo.getPaid_p());
		}else if (col ==2) {
			obj = Integer.toString(vo.getMember_id()); 
		}else if (col == 3) {
			obj = vo.getName();
		}else if(col == 4) {
			obj = vo.getStatus();
		}else if (col == 5) {
			obj = vo.getOrder_date(); 
		}
		return obj;
	}
 
}
