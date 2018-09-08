package com.mtk.practice.shared.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnAdmin {
	  
	public static Connection getConnection() {		
		Connection conn = null;
		conn = new ConnProvider().getConn();
		
		return conn;
	}
	
	
}
