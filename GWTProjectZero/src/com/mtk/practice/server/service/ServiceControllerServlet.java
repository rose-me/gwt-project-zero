package com.mtk.practice.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mtk.practice.client.service.ServiceController;
import com.mtk.practice.server.bridge.ServiceControllerSwitch;
import com.mtk.practice.shared.InvoiceData;
import com.mtk.practice.shared.InvoiceDataset;
import com.mtk.practice.shared.Result;

public class ServiceControllerServlet extends RemoteServiceServlet implements ServiceController {

	public InvoiceDataset getInvoiceDataset(String gParameters, long cusSK,
			String searchText, int m_ResultSize, int totalPage) {
		return ServiceControllerSwitch.getInvoiceDataset(gParameters, cusSK, searchText,
									m_ResultSize, totalPage);
	}

	public InvoiceData readInvoiceDataGrid(long key) {		
		return ServiceControllerSwitch.readInvoiceDatagrid(key);		
	}
	
	public Result saveInvoice(InvoiceData mInvData) {
		return ServiceControllerSwitch.saveInvoice(mInvData);
	}

	public Result deleteInvoice(long mSK) {
		return ServiceControllerSwitch.deleteInvoice(mSK);
	}

}
