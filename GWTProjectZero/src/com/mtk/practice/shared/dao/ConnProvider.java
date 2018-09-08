package com.mtk.practice.shared.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnProvider {
	/*String DRIVER="com.mysql.jdbc.Driver";
	String CONNECTION_URL="jdbc:mysql://localhost:3306/learning";
	String USERNAME="root";
	String PASSWORD="root";*/
	
	/*String DRIVER="com.microsoft.sqlserver.jdbc.SQLSERVERDRIVER";
	String CONNECTION_URL="jdbc:sqlserver://DELL\\SQLSERVER2014:1433;databaseName=ProjectPractice;integratedSecurity=true";
	String USERNAME="sa";
	String PASSWORD="ns123";*/
	
      public static String servername = "";
	  public static String port = "";
	  public static String instance = "";
	  public static String dbname = "";
	  public static String dbUsr = "";
	  public static String dbPwd = "";
	  public static String connType = "";
	  public static String DRIVER = "";
	  public static String URL = "";
	  public boolean authtype;
	  
	public ConnProvider() {
		//DriverManager.getConnection("jdbc:sqlserver://DELL\\SQLSERVER2014:1433;databaseName=ProjectPractice;integratedSecurity=false", "sa", "ns123");
		authtype = false;
		servername = "localhost";
		port = "1433";
		instance = "DELL\\SQLSERVER2014";
		dbname = "ProjectPractice";
		dbUsr = "sa";
		dbPwd = "ns123";
		DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		/*URL = "jdbc:sqlserver://" + servername + ":" + port + "/" + dbname 
				+ ";Instance=" + instance + ";";*/
		URL = "jdbc:sqlserver://" + instance + ":" + port + ";databaseName=" + dbname 
				+ ";integratedSecurity=" + authtype ;
		
	}
	
	public Connection getConn() {
		try {
			Class.forName(DRIVER).newInstance();
			return DriverManager.getConnection(URL, dbUsr, dbPwd);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return null;
		
	}
	
}
