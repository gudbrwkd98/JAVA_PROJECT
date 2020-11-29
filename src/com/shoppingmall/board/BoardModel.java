package com.shoppingmall.board;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class BoardModel extends AbstractTableModel{

	ArrayList<BoardVO> record = new ArrayList<BoardVO>();
		String [] column = {"게시판 번호","작성자","제목","작성일","답변 상황","조회수"};
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
		BoardVO vo = record.get(row);
		String obj = null;
		
		if(col == 0) {
			obj = Integer.toString(vo.getBoard_id());
		}else if(col == 1) {
			obj = vo.getName();
		}else if(col == 2) {
			obj = vo.getTitle();
		}else if(col == 3) {
			obj = vo.getRegdate();
		}else if(col == 4) {
			obj = vo.getSatus();
		}else if(col == 5) {
			obj =  Integer.toString(vo.getHit());
		}
		
		return obj;
	}

}
