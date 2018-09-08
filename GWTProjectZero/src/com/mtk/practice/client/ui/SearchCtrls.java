package com.mtk.practice.client.ui;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public class SearchCtrls {
	SuggestBox txtsearch;
	MultiWordSuggestOracle suggestion;
	Button butSearch;
	Button butAddItem;
	
	HorizontalPanel searchPnl;	
	
	public SearchCtrls(){		
		
		suggestion = new MultiWordSuggestOracle();
		txtsearch = new SuggestBox(suggestion);
		txtsearch.setSize("245px","28px");
		txtsearch.setStyleName("search-suggestbox");		
		
		butSearch=new Button("");
		butSearch.setStyleName("searchButton");
		butSearch.setHTML("<div width='80px'><Label width='50px' >Show</Label></div>");	

		butAddItem=new Button("");
		butAddItem.setStyleName("searchButton");
		butAddItem.setHTML("<div width='130px'><img src = 'images/plus-icon.png' width='13px' height='13px' align='left'/><Label width='20px' ></Label><Label width='50px' >Add Invoice</Label></div>");		
		
		searchPnl = new HorizontalPanel();		
		searchPnl.clear();	
	    searchPnl.add(txtsearch);//0		
		searchPnl.add(new Label());//1
		searchPnl.setCellWidth(searchPnl.getWidget(1), "5px");
		searchPnl.add(butSearch);//2
		searchPnl.add(new Label());//3
		searchPnl.setCellWidth(searchPnl.getWidget(3), "350px");			
		searchPnl.add(butAddItem);//4
		searchPnl.setCellVerticalAlignment(searchPnl.getWidget(4), HasVerticalAlignment.ALIGN_MIDDLE);
		
		
	}//constructor
	
	public HorizontalPanel getCombinedControls(){
		return searchPnl;
	}//display combined controls panel
	
	public void addSearchBoxKeyUpHandler(KeyUpHandler handler){
		txtsearch.addKeyUpHandler(handler);
	}	
	
	public void addSearchButtonClickHandler(ClickHandler handler){
		butSearch.addClickHandler(handler);
	}
	
	public void addCreateInvoiceButtonClickHandler(ClickHandler handler){
		butAddItem.addClickHandler(handler);
	}
	
	public String getSearchText(){
		return txtsearch.getText();
	}	
	public void setSearchText(String value){
		txtsearch.setText(value);
	}	
	public void setSearchBoxFocus(){
		txtsearch.setFocus(true);
	}	
}
