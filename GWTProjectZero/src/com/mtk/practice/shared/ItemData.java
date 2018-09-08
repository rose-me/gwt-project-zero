package com.mtk.practice.shared;

import java.io.Serializable;

public class ItemData implements Serializable {
	public ItemData() {
		super();
		clearProperties();
	}
	
	private long syskey;	
	private String createddate;
	private String modifieddate;
	private int RecordStatus;
	private long parentid;
	private String t1;
	private String t2;
	private String t3;
	private String t4;
	private String t5;
	private long n1;
	private double n2;
	private double n3;
	private long n4;
	private long n5;
	
	public void clearProperties(){
		 this.syskey=0;		 
		 this.createddate="";
		 this.modifieddate="";
		 this.RecordStatus=0;
		 this.parentid=0;
		 this.t1="";
		 this.t2="";
		 this.t3="";
		 this.t4="";
		 this.t5="";		 
		 this.n1=0;
		 this.n2=0.0;
		 this.n3=0.0;
		 this.n4=0;
		 this.n5=0;		 
	}
	
	
	public void setSyskey(long args ){
		this.syskey=args;
	}
	public long getSyskey() {
		return this.syskey;
	}	
	
	public void setCreateddate(String args ){
		this.createddate=args;
	}
	public String getCreateddate() {
		return this.createddate;
	}	
	
	public void setModifieddate(String args ){
		this.modifieddate=args;
	}
	public String getModifieddate() {
		return this.modifieddate;
	}	
	
	public void setRecordstatus(int args ){
		this.RecordStatus=args;
	}
	public int getRecordstatus() {
		return this.RecordStatus;
	}
	
	public void setParentid(long args) {
		this.parentid = args;
	}
	public long getParentid() {
		return this.parentid;
	}
	
	public void setT1(String args ){
		this.t1=args;
	}
	public String getT1() {
		return this.t1;
	}	
	
	public void setT2(String args ){
		this.t2=args;
	}
	public String getT2() {
		return this.t2;
	}
	
	
	public void setT3(String args ){
		this.t3=args;
	}
	public String getT3() {
		return this.t3;
	}
	
	public void setT4(String args ){
		this.t4=args;
	}
	public String getT4() {
		return this.t4;
	}
	
	public void setT5(String args ){
		this.t5=args;
	}
	public String getT5() {
		return this.t5;
	}		
	
	public void setN1(long args ){
		this.n1=args;
	}
	public long getN1() {
		return this.n1;
	}
	
	public void setN2(double args ){
		this.n2=args;
	}
	public double getN2() {
		return this.n2;
	}	
	
	public void setN3(double args ){
		this.n3=args;
	}
	public double getN3() {
		return this.n3;
	}
	
	public void setN4( long args ){
		this.n4=args;
	}
	public long getN4() {
		return this.n4;
	}
	
	public void setN5( long args ){
		this.n5=args;
	}
	public long getN5(){
		return this.n5;
	}
}
