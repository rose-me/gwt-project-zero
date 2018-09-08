package com.mtk.practice.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.server.testing.Parent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mtk.practice.client.ClientUtility;
import com.mtk.practice.client.MainView;
import com.mtk.practice.client.Message;
import com.mtk.practice.client.service.ServiceControllerMgr;
import com.mtk.practice.shared.InvoiceData;
import com.mtk.practice.shared.InvoiceDataset;
import com.mtk.practice.shared.Result;

public class InvoiceDataGrid {

	AbsolutePanel m_progressPnl = new AbsolutePanel();
	FlowPanel m_listDataPnl = new FlowPanel();
	ScrollPanel m_scroll = new ScrollPanel();
	AbsolutePanel m_paginateControlPnl = new AbsolutePanel();
	VerticalPanel m_searhControlPnl = new VerticalPanel();
	VerticalPanel m_listPnl = new VerticalPanel();
	HorizontalPanel m_hrListPnl = new HorizontalPanel();
	PopupPanel mPopup = new PopupPanel(false);
	
	SearchCtrls m_searchCtrl;
	AsyncCallback<PopupPanel> m_callback;	
	
	String m_type="";	
	boolean mflag;
	String gparameters="";
	String fromdate = "";
	String todate = "";
	String opportype = "";
	long invSK = 0;
	int m_ResultSize = 10;
	boolean frmpopup;
	boolean isLink = false;
	
	private MainView main;
	public InvoiceDataGrid(MainView main){
		this.main = main;//get parent view
		m_progressPnl.setSize("600px", "300px");		
		m_searchCtrl = new SearchCtrls();//build search controls
		createSearchControl(m_listDataPnl, m_scroll, m_paginateControlPnl,
				m_progressPnl);
		
		
			goGetThem(m_listDataPnl, m_scroll, m_paginateControlPnl,
					m_progressPnl, invSK);			
		//show
		/*HorizontalPanel hrBlank=new HorizontalPanel();
		hrBlank.setWidth("1024px");
		hrBlank.setHeight("50px");
		hrBlank.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		g.setWidget(0, 0, hrBlank);*/
		Grid g = new Grid(1, 1);		
		g.setWidget(0, 0, m_searhControlPnl);
		m_listPnl.add(new HorizontalPanel());
		m_listPnl.getWidget(0).setHeight("30px");
		m_listPnl.add(g);		
		m_listPnl.add(m_scroll);
		m_listPnl.add(m_progressPnl);
		m_listPnl.add(m_paginateControlPnl);//set page number		
		
	}
	public InvoiceDataGrid(boolean pFlag, PopupPanel pop, AsyncCallback<PopupPanel> p_callback) {		
		mPopup = pop;
		mPopup.hide();
		m_callback = p_callback;
		m_progressPnl.setSize("600px", "300px");		
		m_searchCtrl = new SearchCtrls();//build search controls
		createSearchControl(m_listDataPnl, m_scroll, m_paginateControlPnl,
				m_progressPnl);
		
		if (pFlag) {
			goGetThem(m_listDataPnl, m_scroll, m_paginateControlPnl,
					m_progressPnl, invSK);			
		}//show
		/*HorizontalPanel hrBlank=new HorizontalPanel();
		hrBlank.setWidth("1024px");
		hrBlank.setHeight("50px");
		hrBlank.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		g.setWidget(0, 0, hrBlank);*/
		Grid g = new Grid(1, 1);		
		g.setWidget(0, 0, m_searhControlPnl);
		m_listPnl.add(new HorizontalPanel());
		m_listPnl.getWidget(0).setHeight("30px");
		m_listPnl.add(g);		
		m_listPnl.add(m_scroll);
		m_listPnl.add(m_progressPnl);
		//m_listPnl.add(m_paginateControlPnl);//set page number		
		
	}//constructor
	
	private void createSearchControl(final FlowPanel p_uiList,
			final ScrollPanel p_uiListScroll, final AbsolutePanel p_uiPaginate,
			final AbsolutePanel p_uiWait) {
		m_searhControlPnl.add(m_searchCtrl.getCombinedControls());
		m_searchCtrl.addSearchButtonClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				goGetThem(p_uiList, p_uiListScroll, p_uiPaginate, p_uiWait, invSK);
			}
		});	
		
		m_searchCtrl.addSearchBoxKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == 13) {
					goGetThem(p_uiList, p_uiListScroll, p_uiPaginate, p_uiWait, invSK);
				}
			}
		});
		
		m_searchCtrl.addCreateInvoiceButtonClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				callInvoiceForm(0, false);				
			}			
		});

	}
	
	private void callInvoiceForm(long key, boolean flag) {
		isLink = flag;//when click lbldesc(grid item name)
		invSK = key;
		mPopup.clear();
		mPopup.setWidth("1025px");
		mPopup.setHeight("650px");
		mPopup.setStyleName("rp-background1");
		AbsolutePanel abp = new AbsolutePanel();
		abp.setStyleName("AbsoultePanel-Margin");
		final FrmMainInvoice frmMain = new FrmMainInvoice(invSK, isLink, mPopup, main, getcallbackInvoiceLink());
		abp.add(frmMain);
		mPopup.add(abp);
		mPopup.setGlassEnabled(true);
		mPopup.setPopupPosition(250, 120);
		mPopup.show();			
	}
	
	AsyncCallback<Result> getcallbackInvoiceLink() {
		return new AsyncCallback<Result>() {
			public void onFailure(Throwable caught) {
				Window.alert("ServerError");
			}
			public void onSuccess(Result result) {
				if (result.getState()) {					
					//Message.jsni("normal|Saved Successfully.");
					//mPopup.hide();
					Window.alert("normal|Saved Successfully.");
					invSK = result.getLongResult().get(0);
					mPopup.show();
					//main.openInvoiceDatagrid();
					
					
				} else {
					Window.alert("warn| Code Already Exists.");
					/*if(result.getNum() == 1)
						Message.jsni("warn|Code Already Exists.");
					else
						Message.jsni("warn|Item Already Exists.");*/
						
				}
			}
		};
	}
	
	private void goGetThem(FlowPanel p_uiList, ScrollPanel p_uiListScroll,
			AbsolutePanel p_uiPaginate, AbsolutePanel p_progressPnl, long SK) {
		p_progressPnl.setVisible(true);
		p_uiPaginate.clear();
		p_uiList.clear();
		String l_searchText = m_searchCtrl.getSearchText();
		
		ServiceControllerMgr.getInvoiceDataset("",SK,
				l_searchText,m_ResultSize,1,getcallbackInvoiceDataset(p_uiList, p_uiListScroll,
						p_uiPaginate, p_progressPnl));
		
	}//show invoice dataset(searchctrls+grid)
	
	AsyncCallback<InvoiceDataset> getcallbackInvoiceDataset(
			final FlowPanel p_uiList, final ScrollPanel p_uiListScroll,
			final AbsolutePanel p_uiPaginate, final AbsolutePanel p_uiWait) {
		return new AsyncCallback<InvoiceDataset>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(InvoiceDataset result) {
				p_uiWait.setVisible(false);
				p_uiListScroll.setVisible(true);			
				p_uiList.clear();
				p_uiListScroll.clear();				
				
 				showList(result, p_uiList);
 				p_uiListScroll.add(p_uiList);
 				
 				if(result.getDataList().size()>0)
					createPagePanel(result.getCurrentPage(), result.getTotalCount(),result.getPageSize(), 
							p_uiList, p_uiListScroll,p_uiPaginate, p_uiWait); 

			}
		};
	}
	
	private void createPagePanel(final int p_currentPage, int p_TotalCount,
			int p_PageSize, final FlowPanel p_uiList,
			final ScrollPanel p_uiListScroll, final AbsolutePanel p_uiPaginate,
			final AbsolutePanel p_uiWait) {
		p_uiPaginate.clear();
		HorizontalPanel hpanel = new HorizontalPanel();
		final int totalPage = (int) Math.ceil((double) p_TotalCount/ p_PageSize);
		final PageDataCtrls l_paginatecontrol = new PageDataCtrls(p_currentPage,p_PageSize, p_TotalCount);		
		
		hpanel.add(l_paginatecontrol.getPageDataControl());
		hpanel.setCellHorizontalAlignment(l_paginatecontrol.getPageDataControl(),
				HasHorizontalAlignment.ALIGN_RIGHT);
		p_uiPaginate.add(hpanel);
		
	}
	
	private void showList(InvoiceDataset p_result, FlowPanel p_uiList) {
		
		if (p_result != null && p_result.getDataList().size() > 0) {
			
			TableDataCtrls l_tableDataControl = new TableDataCtrls();
			int col = 0;
			
			l_tableDataControl.setHeader(col++, "Invoice Name", "180px");
			l_tableDataControl.setHeader(col++, "#of Items", "100px");
			l_tableDataControl.setHeader(col++, "Total", "150px");
			l_tableDataControl.setHeader(col++, "", "200px");
			
			for (int i = 0; i < p_result.getDataList().size(); i++) {
				int index = 0;
				InvoiceData l_Data = p_result.getDataList().get(i);
				final long key = p_result.getDataList().get(i).getSyskey();
				
				l_tableDataControl.insertRow();				
				Label lblInvName = new Label();
				lblInvName.setText(l_Data.getT1());
				lblInvName.setStyleName("link-label");
				l_tableDataControl.setWidget(index++, lblInvName);//
				l_tableDataControl.setText(index++, ClientUtility.formatIntegerNumber(l_Data.getN5()));
				l_tableDataControl.setText(index++, ClientUtility.formatNumber(l_Data.getN4()));
				
				//multiWidget in one cell
				Widget w1 = new Label();
				Widget w2 = new Label();
				Widget w3 = new Label();
				Label lblPDF = (Label) w1;
				lblPDF.setText("PDF");
				lblPDF.setStyleName("link-label");
				
							
				Label lblInvRemove = (Label) w2;
				lblInvRemove.setText("Remove");
				lblInvRemove.setStyleName("link-label");
				//lblPDF.setStyleName("noborder");
				Label lblSpace = (Label) w3;
				lblSpace.setText("");
				lblSpace.setWidth("10px");
				
				final HorizontalPanel hpanel = new HorizontalPanel();				
				hpanel.add(lblPDF);//0
				hpanel.add(lblSpace);//1
				hpanel.add(lblInvRemove);//2
				hpanel.setBorderWidth(0);
				/*hpanel.getWidget(0).setStyleName("noborder");
				hpanel.getWidget(2).setStyleName("noborder");	*/		
			    l_tableDataControl.setWidget(index++, hpanel);
			   
			    
			    lblInvName.addClickHandler(new ClickHandler() {//item name click
					public void onClick(ClickEvent event) {
						callInvoiceForm(key, true);						
						//ServiceControllerMgr.readInvoiceDataGrid(key, getCallbackPopup());						
					}					
				});
			    
			    lblPDF.addClickHandler(new ClickHandler() { //PDF link click
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						
					}					
					
				});					
				
			    lblInvRemove.addClickHandler(new ClickHandler() { //remove link click
					public void onClick(ClickEvent event) {	
						
						callDelConfirm(key);						
					}					
				});					
				
			}//end for			
				
			p_uiList.add(l_tableDataControl.getTableDataControl());
			l_tableDataControl.getTableDataControl().getElement().setId("list");
			//l_tableDataControl.callSorting("list", 0);
		} else {
			p_uiList.add(new Label("There are no invoices matching your filter selections."));
		}
	}	
	
	public void callDelConfirm(long key) {
		invSK = key;
		final PopupPanel popup = new PopupPanel();
		popup.setStyleName("popup");

		VerticalPanel vPanel = new VerticalPanel();
		HorizontalPanel horTitle = new HorizontalPanel();
		HorizontalPanel horText = new HorizontalPanel();
		HorizontalPanel horBut = new HorizontalPanel();
		HorizontalPanel hor = new HorizontalPanel();

		horTitle.setWidth("350px");
		horText.setWidth("350px");
		hor.setWidth("350px");

		horTitle.setHeight("30px");
		horText.setHeight("25px");
		hor.setHeight("25px");

		horText.setStyleName("horBorder");

		Label lblTitle = new Label("Delete This Invoice?");
		lblTitle.setStyleName("delete-label");
		lblTitle.setWidth("350px");

		Label lblText = new Label(
				"Are you sure you want to delete? This can't be undone.");

		Button butOK = new Button("Delete");
		butOK.setStyleName("delete-Button");

		Button butCancel = new Button("Cancel");
		butCancel.setStyleName("delete-Button");

		Label lblSpace = new Label("");
		lblSpace.setWidth("5px");

		horTitle.add(lblTitle);
		horTitle.getWidget(0).getElement()
				.setAttribute("style", "padding: 5px 0px 0px 10px");
		horText.add(lblText);
		horText.getWidget(0).getElement()
				.setAttribute("style", "padding: 0px 0px 0px 10px");
		horBut.add(butOK);
		horBut.add(lblSpace);
		horBut.add(butCancel);
		hor.add(horBut);
		hor.getWidget(0).getElement()
				.setAttribute("style", "padding: 0px 10px 0px 10px");
		hor.setCellHorizontalAlignment(horBut,
				HasHorizontalAlignment.ALIGN_RIGHT);

		vPanel.add(horTitle);
		vPanel.add(horText);
		vPanel.add(hor);

		popup.add(vPanel);

		popup.setHeight("100px");
		popup.setWidth("350px");
		popup.setVisible(true);
		popup.setPopupPosition(300, 180);
		popup.setGlassEnabled(true);
		popup.show();

		butOK.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				ServiceControllerMgr.deleteInvoice(invSK,
						deletecallback());
				popup.hide();
			}
		});

		butCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				popup.hide();
			}
		});
	}
	
	private AsyncCallback<Result> deletecallback() {
		return new AsyncCallback<Result>() {
			public void onFailure(Throwable caught) {

			}
			public void onSuccess(Result result) {
				//mPopup.hide();
				if (result.getState()) {						
					Window.alert("normal|Deleted Successfully.");
					goGetThem(m_listDataPnl, m_scroll, m_paginateControlPnl,
							m_progressPnl, invSK);	
					//main = new MainView();
					//main.openInvoiceDatagrid();
				} else {
					Window.alert("error|" + result.getMsgDesc());
				}
			}
		};
	}
	
	public VerticalPanel getListPopup() { //return (serctrls + dataset)
		return m_listPnl;
	}	

}
