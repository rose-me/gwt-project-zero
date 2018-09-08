package com.mtk.practice.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Result implements Serializable {
	public Result()
	{
		
	}
	
	private boolean state = false;
	private String msgCode ="";
	private String msgDesc= "" ;
	private ArrayList<Long> longResult = new ArrayList<Long>();
	private int num = 0;
	
	public int getNum() {
		return num;
	}

	public void setNum(int n) {
		this.num = n;
	}
	
	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getMsgDesc() {
		return msgDesc;
	}

	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}
	
	public ArrayList<Long> getLongResult() {
		return longResult;
	}

	public void setLongResult(ArrayList<Long> longResult) {
		this.longResult = longResult;
	}
}
