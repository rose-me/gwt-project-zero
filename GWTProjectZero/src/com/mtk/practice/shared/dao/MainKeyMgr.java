package com.mtk.practice.shared.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainKeyMgr {
	
	public static long getMainKey(Connection aConnection)
		    throws SQLException {

	    int mkey = 0;
	    String query = "";
	    boolean result = false;
	    try {
	    	aConnection.setAutoCommit(false);
	    	
	    	try {
				while(mkey == 0) {
					mkey=0;
					//query = insertSqlString(aConnection);
					query = "Insert into [MainKey](syskey) values (?)";								
					PreparedStatement stmt = aConnection.prepareStatement(query);
					stmt.setLong(1, mkey);
					if(stmt.executeUpdate() > 0) {
						result = true;
					}
					int autokey = readAutoID(mkey, "MainKey", aConnection);
					
					Statement updstmt = aConnection.createStatement();
					updstmt.execute("update mainkey set syskey=" + autokey + " where syskey=" + mkey);
					mkey = autokey;
					
				}
			}
			catch (SQLException e)
		    {
		      e.printStackTrace();
		    }
	    } catch (SQLException e)
	    {
	        e.printStackTrace();
	      }		
		
	    return mkey;
	}
	
	public static int readAutoID(long akey, String aTableName, Connection aConnection) {
		int autokey = 0;
		String query = "select id,syskey from " + aTableName + " where syskey=" + akey;
		try 
		{
			PreparedStatement stmt = aConnection.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				autokey = rs.getInt("id");
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException localSQLException) {}
		
		return autokey;
	}
	
	public static String insertSqlString(Connection aConnection)
	  {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Insert into [");
	    sb.append("MainKey");
	    sb.append("] (");
	    for (int i = 0; i < 1; i++)
	    {
	      if (i != 0) {
	        sb.append("syskey");
	      }
	      sb.append("");
	    }
	    sb.append(") values (");
	    for (int i = 0; i < 1; i++)
	    {
	      if (i != 0) {
	        sb.append(", ");
	      }
	      sb.append("?");
	    }
	    sb.append(")");
	    return sb.toString();
	  }
		
}
