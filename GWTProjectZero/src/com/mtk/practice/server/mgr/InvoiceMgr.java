package com.mtk.practice.server.mgr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mtk.practice.server.dao.InvoiceDao;
import com.mtk.practice.shared.InvoiceData;
import com.mtk.practice.shared.InvoiceDataset;
import com.mtk.practice.shared.ItemData;
import com.mtk.practice.shared.Result;
import com.mtk.practice.shared.dao.ConnAdmin;
import com.mtk.practice.shared.dao.ItemDao;
import com.mtk.practice.shared.dao.MainKeyMgr;

public class InvoiceMgr {
	
	public static InvoiceDataset getInvoiceDataset(
			String gParameters, long invSK, String p_searchText, int p_pageSize, int p_currentPage) {
		Connection l_Conn = ConnAdmin.getConnection();
		
		InvoiceDataset p_InvoiceDataset = new InvoiceDataset();
		try {
			p_InvoiceDataset = InvoiceDao.getDataInvoice(gParameters, invSK, p_searchText, 
					p_pageSize, p_currentPage, l_Conn);						
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return p_InvoiceDataset;

	}
	
	public static InvoiceData readInvoiceDatagrid(long p_Key) {
		InvoiceData mData = new InvoiceData();
		ArrayList<ItemData> l_List = new ArrayList<ItemData>();		
		
		Connection l_Conn = ConnAdmin.getConnection();

		try {
			mData = InvoiceDao.readInvoiceDatagrid(p_Key, l_Conn);			
			
			if (mData != null)
				l_List = ItemDao.readItemDataByParentid(p_Key, l_Conn);
			mData.setItemDataList(l_List);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mData;
	}
	
	public static Result deleteInvoice(long p_key) {
		Result res = new Result();
		res.setState(false);
		Connection l_Conn = ConnAdmin.getConnection();
		try {
			l_Conn.setAutoCommit(false);	
			res = InvoiceDao.deleteInvoice(p_key, l_Conn);
			if(res.getState()) {
				res = ItemDao.deleteActiviytItem(p_key, l_Conn);
			}
			
			if (res.getState()) {
				res = new Result();								
				res.setState(true);				
				l_Conn.commit();
				
			} else
				l_Conn.rollback();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
	            if (l_Conn != null && !l_Conn.isClosed()) {
	            	l_Conn.close();
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
		}
		return res;
	}
	
	public static Result save(InvoiceData l_Invoice) throws SQLException {
		Result res = new Result();
		res.setState(false);
		Connection l_Conn = ConnAdmin.getConnection();	
		//System.out.println("driver="+l_Conn.toString());
		System.out.println("Database Name: " + l_Conn.getMetaData().getDatabaseProductName());
		/*Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection l_Conn = DriverManager.getConnection("jdbc:sqlserver://DELL\\SQLSERVER2014:1433;databaseName=ProjectPractice;integratedSecurity=false", "sa", "ns123");*/
		long invKey = 0;
		
		try {			
			l_Conn.setAutoCommit(false);			

			if (l_Invoice.getSyskey() == 0) {//createnew				
				invKey = MainKeyMgr.getMainKey(l_Conn);
				l_Invoice.setSyskey(invKey);				
				res = InvoiceDao.insert(l_Invoice, l_Conn);
				invKey = res.getLongResult().get(0);

			} else {//update
				invKey = l_Invoice.getSyskey();				
				res = InvoiceDao.update(l_Invoice, l_Conn);
			}		

			long itemSK = 0;
			if (res.getState()) {
				if (l_Invoice.getItemDataList().size() > 0) {
					ItemDao.deleteByParentid(invKey, l_Conn);//delete previous activity items before adding
					for (ItemData aData : l_Invoice.getItemDataList()) {
						itemSK = MainKeyMgr.getMainKey(l_Conn);
						aData.setSyskey(itemSK);
						aData.setCreateddate(l_Invoice.getCreateddate());
						aData.setModifieddate(l_Invoice.getModifieddate());						
						aData.setRecordstatus(l_Invoice.getRecordstatus());						
						aData.setParentid(l_Invoice.getSyskey());
						
						res = ItemDao.insert(aData, l_Conn);
					}
				}
			}			

			if (res.getState()) {
				res = new Result();
				res.getLongResult().add(invKey);				
				res.setState(true);				
				l_Conn.commit();
				
			} else
				l_Conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		} /*finally {
			if (!l_Conn.isClosed() && l_Conn != null) {
				l_Conn.close();
			}
		}*/
		finally {
			try {
	            if(l_Conn != null && !l_Conn.isClosed()) {
	            	l_Conn.close();
	            }           
	            l_Conn.setAutoCommit(true);
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
		}
		return res;
	}	
	
}
