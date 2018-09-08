package com.mtk.practice.server.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mtk.practice.shared.InvoiceData;
import com.mtk.practice.shared.InvoiceDataset;
import com.mtk.practice.shared.ItemData;
import com.mtk.practice.shared.Result;
import com.mtk.practice.shared.dao.DoubleValues;
import com.mtk.practice.shared.dao.IntValues;
import com.mtk.practice.shared.dao.LongValues;

public class InvoiceDao {
	
	public static InvoiceDataset getDataInvoice(String gparameters,
			long invSK,  String p_searchtext,
			int p_PageSize, int p_CurrentPage, Connection aConnection)
			throws SQLException {
		if (aConnection.toString().startsWith("net.sourceforge.jtds.jdbc.")) {
			System.out.println("0");
	      }
	      if (aConnection.toString().startsWith("sun.jdbc.odbc.")) {
	    	  System.out.println("1");
	      }
	      if (aConnection.toString().startsWith("com.microsoft.sqlserver.jdbc")){
	    	  System.out.println("2");
	      } else {
	    	  System.out.println("3");
	      }
	     /* if (aConnection.toString().startsWith("sun.jdbc.odbc.")) {
	    	  System.out.println("2");
	      }*/
		
		InvoiceDataset l_InvoiceDataset = new InvoiceDataset();
		ArrayList<InvoiceData> ret = new ArrayList<InvoiceData>();
		String mSrchStr = " ";
		String aSql = "";String aJoin = "";String aFilter = "";String aOrder = "";
		/*if (invSK != 0) {
			aFilter = "  and  PP001.syskey= " + invSK;
		}*/
		//aOrder = " group by inv.syskey, inv.t1, inv.n4 ";
		aSql = "select inv.syskey, inv.t1, inv.n5 as tot ,inv.n4 from pp001 as inv "
				+ " where inv.recordstatus <> 4";
		aJoin = " inv " + " where inv.recordstatus <> 4 ";
		/*aSql = "select inv.syskey, inv.t1, sum(item.n1) as tot,inv.n4 from pp001 as ";
		aJoin = " inv " +  " inner join ppjun001 as item on inv.syskey=item.parentid"
				+ " where inv.recordstatus <> 4 and item.recordstatus <> 4 ";*/
		mSrchStr = getSearchStringForInvoice(p_searchtext, invSK, aConnection)
					+ aOrder;		
		aSql = aSql + mSrchStr + " order by inv.t1 desc";
		
		l_InvoiceDataset.setTotalCount((getTotalCountbyInvlist("PP001", aJoin + mSrchStr, aConnection)));
		Statement st = aConnection.createStatement();
		ResultSet rs = st.executeQuery(aSql);
		while(rs.next()){
			InvoiceData l_data = new InvoiceData();
			l_data.setSyskey(rs.getLong(1));
			l_data.setT1(rs.getString(2));//inv name
			l_data.setN5(rs.getLong("tot")); //#of items
			l_data.setN4(rs.getDouble(4));	//total		
			ret.add(l_data);
		}
		l_InvoiceDataset.setDataList(ret);
		l_InvoiceDataset.setCurrentPage(p_CurrentPage);//1
		l_InvoiceDataset.setPageSize(p_PageSize);//10
		
		
		return l_InvoiceDataset;
	}
	
	public static InvoiceData readInvoiceDatagrid(long aPk,
			Connection aConnection) throws SQLException {
		InvoiceData ret = null;
		String sql = "select * from pp001 where syskey=" + aPk
						+ " and recordstatus=1";
		Statement st = aConnection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if(rs.next()) {
			ret = new InvoiceData();
			ret.setSyskey(rs.getLong(1));
			ret.setT1(rs.getString(6));
			ret.setN1(rs.getDouble(11));
			ret.setN2(rs.getLong(12));
			ret.setN3(rs.getDouble(13));
			ret.setN4(rs.getDouble(14));
			ret.setN5(rs.getLong(15));//noofitems
		}
		
		return ret;
	}
	
	public static String getSearchStringForInvoice(String p_searchtext,
						long psyskey, Connection aConnection)
			throws SQLException {
		
		String sql = "";
		String amt = "";

		if (!p_searchtext.equals("")) {	
			p_searchtext = p_searchtext.replaceAll("\\s+","");//removes all whitespaces and non-visible characters (e.g., tab, \n)
			
			if (p_searchtext.substring(0,1).matches("[0-9]")) {
				if (p_searchtext.contains(",")) {
					amt = p_searchtext.replace(",", "");
					sql += " and (inv.n4 like '" + amt + "%'"
							+ " or inv.n5 like '" + amt + "%' )";
				}
				else if(p_searchtext.contains(".")) {					
					sql += " and (inv.n4 like '" + p_searchtext + "%'"
							+ " or inv.n5 like '" + p_searchtext + "%' )";
				}
				else {
					sql += " and (inv.n5 like '" + p_searchtext + "%' )";//#no of items
				}	
				
			}
			else {
				sql += " and (replace(inv.T1,' ','') like '%"						
						+ p_searchtext.replace(" ", "") + "%' )";
			}
		}		
		return sql;
	}
	
	public static int getTotalCountbyInvlist(String tableName, String sql,
			Connection aConn) throws SQLException {
		int tot = 0;
		//sql = " select count(*) as tot from " + tableName + sql;
		sql = " select count(tot) as tot from ( select inv.syskey as tot from "
				+ tableName + sql + ") as tbl";
		PreparedStatement psmt = aConn.prepareStatement(sql);
		ResultSet rs = psmt.executeQuery();
		if (rs.next())
			tot = rs.getInt("tot");	
		else
			tot = 0;
		
		return tot;
	}
	
	public static Result insert(InvoiceData aObj, Connection aConnection)
			throws SQLException {
		String filter = "";
		Result res = new Result();
		res.setState(false);
		if (!isCodeExist(aObj.getSyskey(), aConnection) && !isNameExist(aObj.getT1(), aConnection)) {
			String query = insertSqlString(aConnection);
			PreparedStatement stmt = aConnection.prepareStatement(query);			
			//setQueryValues(stmt, aObj, aConnection);
			setQueryValues(stmt, aObj);
			
			try {
				stmt.executeUpdate();
				res.setState(true);
				res.getLongResult().add(aObj.getSyskey());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if(isCodeExist(aObj.getSyskey(), aConnection)){
				res.setNum(1);
				res.setMsgCode("Code already exists!");
				res.setState(false);
			} else {
				res.setNum(2);
				res.setMsgCode("Item already exists!");
				res.setState(false); 
			}
		}
		return res;
	}
	
	public static Result update(InvoiceData aObj, Connection aConnection)
			throws SQLException {
		Result res = new Result();
		res.setState(false);
		try {
			String filter = " where syskey=?";
			String query = updateSqlString(filter, aConnection);
			PreparedStatement stmt = aConnection.prepareStatement(query);	
			setUpdateQueryValues(stmt, aObj);					
			stmt.executeUpdate();
			res.setState(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static boolean isCodeExist(long mKey,
			Connection aConnection) throws SQLException {		
		
		String sqlString = "Select syskey from pp001 where syskey=" + mKey + " and recordstatus= 1";
		InvoiceData mData = new InvoiceData();
		ArrayList<InvoiceData> mList = new ArrayList<InvoiceData>();
		//mData.setSyskey(mKey);		
		
		Statement stmt = aConnection.createStatement();
		ResultSet rs = stmt.executeQuery(sqlString);
		while(rs.next()) {
			mData.setSyskey(rs.getLong("syskey")); 
			mList.add(mData);
	      }
		if (mList.size() > 0) {
			return false;
		} else {
			return false;
		}	
		
	}
	public static boolean isNameExist(String mName,
			Connection aConnection) throws SQLException {		
	
		String sqlString = "Select t1 from pp001 where replace(t1,' ','') like '%" + 
							mName.replace(" ","") + " %' and recordstatus= 1";
		InvoiceData mData = new InvoiceData();
		ArrayList<InvoiceData> mList = new ArrayList<InvoiceData>();			
		
		Statement stmt = aConnection.createStatement();
		ResultSet rs = stmt.executeQuery(sqlString);
		while(rs.next()) {
			mData.setT1(rs.getString("t1")); 
			mList.add(mData);
	      }
		if (mList.size() > 0) {
			return false;
		} else {
			return false;
		}	
		
	}	
	 
	 public static String insertSqlString(Connection aConnection)
	  {
		String[] arr_field = new String[]{"syskey","createddate","modifieddate","recordstatus",
							"t1","t2","t3","t4","t5","n1","n2","n3","n4","n5"};
	    StringBuilder sb = new StringBuilder();
	    sb.append("Insert into [");
	    sb.append("PP001");
	    sb.append("] (");
	    for (int i = 0; i < arr_field.length; i++)
	    {
	      if (i != 0) {
	    	  sb.append(", ");
	      }
	      sb.append(arr_field[i]);
	    }
	    sb.append(") values (");
	    for (int i = 0; i < arr_field.length; i++)
	    {
	      if (i != 0) {
	        sb.append(", ");
	      }
	      sb.append("?");
	    }
	    sb.append(")");
	    return sb.toString();
	  }
	 
	 public static String updateSqlString(String afilter, Connection aConnection)
	  {
		 //Cause of error, 
		 //com.microsoft.sqlserver.jdbc.SQLServerException: The value is not set for the parameter number 2.
		 /*String[] arr_field = new String[]{"syskey","createddate","modifieddate","recordstatus",  
					"t1","t2","t3","t4","t5","n1","n2","n3","n4","n5"};
			remove syskey fields from update
			*/
		 String[] arr_field = new String[]{"syskey","createddate","modifieddate","recordstatus",  
					"t1","t2","t3","t4","t5","n1","n2","n3","n4","n5"};
	    StringBuilder sb = new StringBuilder();
	    sb.append("Update [");
	    sb.append("PP001");
	    sb.append("] set ");
	    for (int i = 0; i < arr_field.length; i++)
	    {
	    	System.out.println("arrlength="+arr_field.length);
	      if (i != 0) {
	    	  sb.append(", ");
	      }
	      sb.append(arr_field[i]);
	      sb.append("=? ");
	    }
	    /*sb.append(" values (");
	    
	    for (int i = 0; i < arr_field.length; i++)
	    {
	      if (i != 0) {
	        sb.append(", ");
	      }
	      sb.append("?");
	    }
	    sb.append(" ) ");*/
	    
	   
	    /*if (aConnection.toString().startsWith("net.sourceforge.jtds.jdbc.")) {
	        return aFilter;
	      }
	      if (aConnection.toString().startsWith("sun.jdbc.odbc.")) {
	        return aFilter;
	      }*/
	    /*if(afilter != null) {
	    	sb.append("where syskey=?");	    	
	    }
	    //sb.append(afilter);
*/	    
	    sb.append(afilter);
	    return sb.toString();
	  }
	 
	 public static void setQueryValues(PreparedStatement stmt, InvoiceData obj)
			    throws SQLException {
		 
		stmt.setLong(1, obj.getSyskey());
		stmt.setLong(11, obj.getN2());		
		stmt.setLong(14, obj.getN5());
		
		stmt.setInt(4, obj.getRecordstatus());
		
		stmt.setDouble(10, obj.getN1());
		stmt.setDouble(12, obj.getN3());
		stmt.setDouble(13, obj.getN4());
		
		stmt.setString(2, obj.getCreateddate());
		stmt.setString(3, obj.getModifieddate());
		stmt.setString(5, obj.getT1());
		stmt.setString(6, obj.getT2());
		stmt.setString(7, obj.getT3());
		stmt.setString(8, obj.getT4());
		stmt.setString(9, obj.getT5());			
		
	 }
	 
	 public static void setUpdateQueryValues(PreparedStatement stmt, InvoiceData obj)
			    throws SQLException {		
		 try {
			 	stmt.setLong(1, obj.getSyskey());		
			 	stmt.setString(2, obj.getCreateddate());
				stmt.setString(3, obj.getModifieddate());
				stmt.setInt(4, obj.getRecordstatus());
				stmt.setString(5, obj.getT1());
				stmt.setString(6, obj.getT2());
				stmt.setString(7, obj.getT3());
				stmt.setString(8, obj.getT4());
				stmt.setString(9, obj.getT5());		
				stmt.setDouble(10, obj.getN1());
				stmt.setLong(11, obj.getN2());
				stmt.setDouble(12, obj.getN3());
				stmt.setDouble(13, obj.getN4());
				stmt.setLong(14, obj.getN5());
				stmt.setLong(15, obj.getSyskey());					
				
			} catch (Exception e) {
				e.printStackTrace();
			}		
	 }
	 
	 public static Result deleteInvoice(long p_key, Connection aConnection)
				throws SQLException {
			Result res = new Result();
			res.setState(false);
			String sql = "UPDATE PP001 SET RecordStatus = 4  where  syskey="
					+ p_key;
			PreparedStatement stmt = aConnection.prepareStatement(sql);
			try {
				stmt.executeUpdate();
				res.setState(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}
	 
	/* public static void setQueryValues(PreparedStatement stmt, InvoiceData obj, Connection conn)
			    throws SQLException {
		 Statement st = conn.createStatement();
		 String query = "select * from pp001 where recordstatus <>4";
		 boolean result = false;
		 String tblInv = "";
		 String tblName = "";
		 int colNumber = 0;
		 ResultSet rs = null;
		 
		 //get database table col count
		 rs = st.executeQuery(query);
		 ResultSetMetaData rsmd = rs.getMetaData();
		 colNumber = rsmd.getColumnCount();		
		 
		 //get database table name
		 DatabaseMetaData dbmeta = conn.getMetaData();	
		 rs = dbmeta.getTables(null, null, null, new String[]{"TABLE"});
		 while(rs.next()) {
			 System.out.println("tblname="+rs.getString("TABLE_NAME"));//System.out.println(rs.getString(3));column 3 is table name
			 tblInv = "pp001";
			 tblName = rs.getString("TABLE_NAME");
			 if(tblInv.equalsIgnoreCase(tblName)) {
				 result = true;
				 break;
			 }
		 }		 
		
		 //get database table columns attributes		 
		 ArrayList<LongValues> lVals_arlist = new ArrayList<LongValues>();
		 ArrayList<IntValues> iVals_arlist = new ArrayList<IntValues>();
		 ArrayList<DoubleValues> dVals_arlist = new ArrayList<DoubleValues>();
		 rs = null;
		 rs = dbmeta.getColumns(null, null, tblName, null);
		 while(rs.next())
		 {
		     //String columnName = rs.getString("COLUMN_NAME");
		     String datatype = rs.getString("DATA_TYPE");
		     String lastWord = "";
		     if(datatype.contains(" ") || datatype.contains(".")) {
		    	 lastWord = datatype.substring(datatype.lastIndexOf(" ")+1);
		     }
		     for(int i=0; i<colNumber; i++) {
	    	 	 if(lastWord.toLowerCase()== "long") {	    	 		 
	    	 		lVals_arlist.add(new LongValues(obj.getAutokey()));	//incompatible this here 		
	    	 		
		    	 }
		    	 else if(lastWord.toLowerCase()== "string") {
		    		 
		    	 }
		    	 else if(lastWord.toLowerCase()== "double") {
		    		 
		    	 }
		    	 else {
		    		 
		    	 }
		     }		     
		     
		 }
	 }	*/	 
		 
	
}
