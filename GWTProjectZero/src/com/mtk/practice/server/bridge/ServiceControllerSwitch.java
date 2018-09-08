package com.mtk.practice.server.bridge;

import java.sql.SQLException;

import com.mtk.practice.server.mgr.InvoiceMgr;
import com.mtk.practice.shared.InvoiceData;
import com.mtk.practice.shared.InvoiceDataset;
import com.mtk.practice.shared.Result;

public class ServiceControllerSwitch {
	public static InvoiceDataset getInvoiceDataset(
			String gParameters, long invSK, String searchText, int m_ResultSize, int totalPage) {
		return InvoiceMgr.getInvoiceDataset(gParameters, invSK,
				 searchText, m_ResultSize, totalPage);
	}
	
	public static InvoiceData readInvoiceDatagrid(long key) {
		return InvoiceMgr.readInvoiceDatagrid(key);
	}
	
	public static Result saveInvoice(InvoiceData mData) {
		Result res = new Result();
		try {
			res = InvoiceMgr.save(mData);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static Result deleteInvoice(long mSK) {
		Result res = new Result();
		res = InvoiceMgr.deleteInvoice(mSK);
		return res;
	}
	
}
