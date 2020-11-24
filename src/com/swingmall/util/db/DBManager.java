package com.swingmall.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBManager {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "user1104";
	private String password = "user1104";
	
	public Connection connect() {
		Connection con = null;	
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return con;
	}
	
	//쿼리문수행과 관련된 자원을 닫아주는 메서드 
	public void close(PreparedStatement pstmt) {
		if(pstmt !=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//쿼리문수행과 관련된 자원을 닫아주는 메서드 
	public void close(PreparedStatement pstmt,ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(pstmt !=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//닫고 싶은 connection 을 받아와서 처리
	public void disConnect(Connection con) {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
