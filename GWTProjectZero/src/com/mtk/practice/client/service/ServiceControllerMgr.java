package com.mtk.practice.client.service;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.mtk.practice.shared.InvoiceData;
import com.mtk.practice.shared.InvoiceDataset;
import com.mtk.practice.shared.Result;

public class ServiceControllerMgr {
	
	private static final ServiceControllerAsync controllerAsync = GWT.create(ServiceController.class);
	
	public static void getInvoiceDataset(String gParameters, long cusSK, String searchText, 
			int m_ResultSize, int totalPage, AsyncCallback<InvoiceDataset> getcallbackInvoiceDataset) {
		
		controllerAsync.getInvoiceDataset(gParameters, cusSK, searchText,
				m_ResultSize, totalPage, getcallbackInvoiceDataset);

	}
	
	public static void readInvoiceDataGrid(long key,
			AsyncCallback<InvoiceData> m_callback) {
		controllerAsync.readInvoiceDataGrid(key, m_callback);

	}
	
	public static void saveInvoice(InvoiceData mInvData,
			AsyncCallback<Result> saveCallback) {
		controllerAsync.saveInvoice(mInvData, saveCallback);

	}
	
	public static void deleteInvoice(long mSK,
			AsyncCallback<Result> deletecallback) {
		controllerAsync.deleteInvoice(mSK, deletecallback);
	}
	
}
