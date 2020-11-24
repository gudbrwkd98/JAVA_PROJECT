package com.swingmall.product;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ProductModel extends AbstractTableModel{

	ArrayList<ProductVO> record = new ArrayList<ProductVO>(); //생성하지않으면 getRowCount() 메서드에서  nullPointerException 이 발생
	

	//컬럼정보를 위한 arrayList 선언
	ArrayList<String> column = new ArrayList<String>();
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return record.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return column.size();
	}
	
	@Override
	public String getColumnName(int col) {
		// TODO Auto-generated method stub
		return column.get(col);
	}

	@Override
	public Object getValueAt(int row, int col) {
		ProductVO vo = record.get(row);
		String obj = null;
		if(col == 0) {
				obj  = Integer.toString(vo.getProduct_id()); 
		}else if (col == 1 ) {
				obj = Integer.toString(vo.getSubcategory_id()); 
		}else if (col ==2) {
			obj = vo.getProduct_name();
		}else if (col == 3) {
			obj = vo.getBrand();
		}else if(col == 4) {
			obj =  Integer.toString(vo.getPrice()); 
		}else if(col == 5) {
			obj = vo.getFilename();
		}else if (col == 6) {
			obj = vo.getDetail();
		}
		return obj;
	}

}
