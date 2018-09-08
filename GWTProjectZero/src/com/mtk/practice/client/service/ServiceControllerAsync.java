package com.mtk.practice.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mtk.practice.shared.InvoiceData;
import com.mtk.practice.shared.InvoiceDataset;
import com.mtk.practice.shared.Result;

public interface ServiceControllerAsync {

	void getInvoiceDataset(String gParameters, long cusSK, String searchText, int m_ResultSize,
			int totalPage, AsyncCallback<InvoiceDataset> getcallbackInvoiceDataset);
	
	void readInvoiceDataGrid(long key,
			AsyncCallback<InvoiceData> m_callback);
	
	void saveInvoice(InvoiceData mInvData,
			AsyncCallback<Result> saveCallback);
	
	void deleteInvoice(long mSK, AsyncCallback<Result> deletecallback);
}
