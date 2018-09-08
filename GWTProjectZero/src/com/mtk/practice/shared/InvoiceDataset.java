package com.mtk.practice.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class InvoiceDataset implements Serializable {
	
	private String m_searchText;
	private int m_totalCount;
	private int m_currentPage;
	private int m_pageSize;
	private double m_summaryamt;
	private double m_summaryperamt;//hpm.
	private ArrayList<InvoiceData> m_arlData;
	//private ArrayList<TotalAmtOpportunityData> m_arlAmtData;
	private String msgDesc= "";
	
	
	public String getMsgDesc() {return msgDesc;}
	public void setMsgDesc(String msgDesc) {this.msgDesc = msgDesc;}
	public  String getSearchText(){return   this.m_searchText;}
	public  void setSearchText(String value){this.m_searchText = value;}
	public  int getTotalCount(){return   this.m_totalCount;}
	public  void setTotalCount(int value){this.m_totalCount = value;}
	public  int getCurrentPage(){return   this.m_currentPage;}
	public  void setCurrentPage(int value){this.m_currentPage = value;}
	public  int getPageSize(){return   this.m_pageSize;}
	public  void setPageSize(int value){this.m_pageSize = value;}
	public  ArrayList<InvoiceData> getDataList(){return   this.m_arlData;}
	public  void setDataList(ArrayList<InvoiceData> value){this.m_arlData = value;}
	
	/*public double getM_summaryamt() {return m_summaryamt;}
	public void setM_summaryamt(double m_summaryamt) {this.m_summaryamt = m_summaryamt;}
	public double getM_summaryPerAmt() {return m_summaryperamt;}//hpm.
	public void setM_summaryPerAmt(double m_summaryperamt) {this.m_summaryperamt = m_summaryperamt;}//hpm.
	public  ArrayList<TotalAmtOpportunityData> getAmtDataList(){return   this.m_arlAmtData;}
	public void setAmtDataList(ArrayList<TotalAmtOpportunityData> m_arlAmtData) {
		this.m_arlAmtData = m_arlAmtData;
	}*/
	
}
