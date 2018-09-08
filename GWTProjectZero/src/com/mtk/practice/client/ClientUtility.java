package com.mtk.practice.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

public class ClientUtility {

	public static String formatIntegerNumber(Long p){
		String ret = "";
		ret = NumberFormat.getFormat("#,##0").format(p);
		return ret;
	}
	public static String formatNumber(double p){
		String ret = "";
		ret = NumberFormat.getFormat("#,##0.00").format(p);
		return ret;
	}
	
	public static String getDateFormatString(java.util.Date date) {
		String l_date = "";
		DateTimeFormat l_dateformat = DateTimeFormat.getFormat("yyyyMMdd");//change by gwt date format		
		l_date = l_dateformat.format(date);		
		return l_date;
	}
}
