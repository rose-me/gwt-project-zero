package com.mtk.practice.shared.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mtk.practice.shared.InvoiceData;
import com.mtk.practice.shared.ItemData;
import com.mtk.practice.shared.Result;

public class ItemDao {
	public static ArrayList<ItemData> readItemDataByParentid(long aPk,
			Connection aConnection) throws SQLException {
		
		ArrayList<ItemData> ret = new ArrayList<ItemData>();
		String sql = "select * from ppjun001 where parentid=" + aPk 
						+ " and recordstatus=1";
		Statement st = aConnection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()) {
			ItemData aData = new ItemData();
			aData.setSyskey(rs.getLong(1));
			aData.setT1(rs.getString(6));
			aData.setN1(rs.getLong(11));
			aData.setN2(rs.getDouble(12));			
			aData.setN3(rs.getDouble(13));
			ret.add(aData);			
		}		
		
		return ret;
	}
	
	public static Result deleteByParentid(long p_key, Connection aConnection)
			throws SQLException {
		Result res = new Result();
		res.setState(false);
		String sql = " delete from ppjun001 where parentid=" + p_key;
		PreparedStatement stmt = aConnection.prepareStatement(sql);
		try {
			stmt.executeUpdate();
			res.setState(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public  static   Result deleteActiviytItem(long p_key, Connection aConnection ) throws SQLException {     
		Result res= new Result();
		res.setState(false);
		String sql = "UPDATE PPJUN001 SET RecordStatus = 4  where  parentid=" + p_key;
		PreparedStatement stmt = aConnection.prepareStatement(sql); 
		try {
		 	stmt.executeUpdate();
			res.setState(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return   res;
	}
	
	public static Result insert(ItemData aObj, Connection aConnection)
			throws SQLException {
		Result res = new Result();
		res.setState(false);
		if (!isCodeExist(aObj.getSyskey(), aConnection)) {
			String query = insertSqlString(aConnection);
			PreparedStatement stmt = aConnection.prepareStatement(query);			
			//setQueryValues(stmt, aObj, aConnection);
			setQueryValues(stmt, aObj);
			try {
				stmt.executeUpdate();
				res.setState(true);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			res.setMsgCode("Code already exists");
		}
		return res;
	}
	public static boolean isCodeExist(long mKey, Connection aConnection) throws SQLException {		
		
		String sqlString = "Select syskey from ppjun001 where syskey=" + mKey + " and recordstatus= 1";
		ItemData mData = new ItemData();
		ArrayList<ItemData> mList = new ArrayList<ItemData>();				
		
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
	
	public static String insertSqlString(Connection aConnection)
	  {
		String[] arr_field = new String[]{"syskey","createddate","modifieddate","recordstatus","parentid",
				"t1","t2","t3","t4","t5","n1","n2","n3","n4","n5"};
	    StringBuilder sb = new StringBuilder();
	    sb.append("Insert into [");
	    sb.append("PPJUN001");
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
	 
	 public static void setQueryValues(PreparedStatement stmt, ItemData obj)
			    throws SQLException {
		 
		stmt.setLong(1, obj.getSyskey());
		stmt.setLong(5, obj.getParentid());
		stmt.setLong(11, obj.getN1());		
		stmt.setLong(14, obj.getN4());
		stmt.setLong(15, obj.getN5());
		
		stmt.setInt(4, obj.getRecordstatus());
		
		stmt.setDouble(12, obj.getN2());
		stmt.setDouble(13, obj.getN3());		
		
		stmt.setString(2, obj.getCreateddate());
		stmt.setString(3, obj.getModifieddate());
		stmt.setString(6, obj.getT1());
		stmt.setString(7, obj.getT2());
		stmt.setString(8, obj.getT3());
		stmt.setString(9, obj.getT4());
		stmt.setString(10, obj.getT5());			 
	 }
	 
	 
}
