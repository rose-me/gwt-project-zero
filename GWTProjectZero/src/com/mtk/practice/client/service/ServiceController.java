package com.mtk.practice.client.service;

import java.sql.SQLException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mtk.practice.shared.InvoiceData;
import com.mtk.practice.shared.InvoiceDataset;
import com.mtk.practice.shared.Result;

@RemoteServiceRelativePath("practiceservice")
public interface ServiceController extends RemoteService {

	InvoiceDataset getInvoiceDataset(String gParameters, long cusSK,
			 String searchText, int m_ResultSize, int totalPage);
	
	InvoiceData readInvoiceDataGrid(long key);
	
	Result saveInvoice(InvoiceData mInvData);
	
	Result deleteInvoice(long mSK);
	
	
}
