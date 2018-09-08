package com.mtk.practice.client;

public class Message {
	static boolean codesvr = true; 
	public static String getSuffix(){return codesvr?"&gwt.codesvr=127.0.0.1:9997":"";};
	
	public static String jsni(String s){  return jsni1(s);};
	public static String serverTime(){return serverTime1();};
	
	public static native String jsni1(String a) /*-{ return parent.parent.jsni(a);}-*/;			
	public static native String serverTime1() /*-{ return parent.parent.serverTime();}-*/;
}
