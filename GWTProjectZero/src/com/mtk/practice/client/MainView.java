package com.mtk.practice.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mtk.practice.client.ui.InvoiceDataGrid;

public class MainView extends Composite {
	private VerticalPanel vPanel = new VerticalPanel();
	private VerticalPanel contentPanel;
	private InvoiceDataGrid grid;//1
	
	public MainView() {
		initWidget(this.vPanel);
		
		MenuView menu = new MenuView();
		this.vPanel.add(menu);
		
		this.contentPanel = new VerticalPanel();
		this.contentPanel.clear();
		//this.contentPanel.add(new FrmInvoice());
		this.grid = new InvoiceDataGrid(this);
		this.contentPanel.add(grid.getListPopup());//2
		this.vPanel.add(contentPanel);
	}
	
	public void openInvoiceDatagrid() {
		this.vPanel.clear();
		this.grid = new InvoiceDataGrid(this);
		this.vPanel.add(grid.getListPopup());
	}
	
}
