package com.mtk.practice.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.mtk.practice.client.ClientUtility;

public class PageDataCtrls {
	
	int m_resultSize = 10;
	int m_totalrecord = 0;	
	FlexTable tablepagecontainer;
	int m_totalpage = 0;
	
	public PageDataCtrls(int p_currentpage, int p_pagesize, int p_totalrecord){		
		try{							
			m_totalrecord = p_totalrecord;
			m_resultSize = p_pagesize;
			m_totalpage = (int) Math.ceil((double) p_totalrecord/ p_pagesize);
			tablepagecontainer = new FlexTable();
			tablepagecontainer.clear();
			tablepagecontainer.insertRow(0);
			
			for(int i = 0; i < m_totalpage; i++) {
				long num =0;
				Label lblPgNo = new Label();
				lblPgNo.setText(ClientUtility.formatIntegerNumber(num+1));
				lblPgNo.setSize("8px", "12px");
				lblPgNo.setStyleName("paginate-label");
				tablepagecontainer.setWidget(0, i, lblPgNo);
				
				lblPgNo.addClickHandler(new ClickHandler () {

					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						
					}
					
				} );
					
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public FlexTable getPageDataControl(){
		return tablepagecontainer;
	}
	
	
}
