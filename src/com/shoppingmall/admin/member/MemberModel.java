package com.shoppingmall.admin.member;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;


public class MemberModel extends AbstractTableModel{
	
	ArrayList<MemberVO> record = new ArrayList<MemberVO>();
	
	String[] column = {"Member_id","회원 아이디","이름","접근권한"};
	
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
		// TODO Auto-generated method stub
		return column[col];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		MemberVO vo = record.get(row);
		String obj = null;
		if(col == 0) {
				obj  = Integer.toString(vo.getMember_id()); 
		}else if (col == 1 ) {
				obj = vo.getMid(); 
				
		}else if (col ==2) {
			obj = vo.getName();
		}else if (col == 3) {
			String lvl = "";
			if(vo.getLvl() == 1) {
				lvl = "회원";
			}else {
				lvl = "관리자";
			}
			
			obj = lvl;
		} 
		return obj;
	}
}
