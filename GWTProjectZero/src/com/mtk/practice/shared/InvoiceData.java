package com.mtk.practice.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class InvoiceData implements Serializable {
	
	public InvoiceData() {
		super();
		clearProperties();
	}
	
	private long syskey;
	private long autokey;
	private String createddate;
	private String modifieddate;
	private int RecordStatus;
	private String t1;
	private String t2;
	private String t3;
	private String t4;
	private String t5;
	private double n1;
	private long n2;
	private double n3;
	private double n4;
	private long n5;
	
	//referece data
	private ItemData i_data;
	private ArrayList<ItemData> i_arlData;
	private long i_count;//#of items in a invoice
	
	
	public void clearProperties(){
		 this.syskey=0;
		 this.autokey=0;
		 this.createddate="";
		 this.modifieddate="";
		 this.RecordStatus=0;
		 this.t1="";
		 this.t2="";
		 this.t3="";
		 this.t4="";
		 this.t5="";		 
		 this.n1=0.0;
		 this.n2=0;
		 this.n3=0.0;
		 this.n4=0.0;
		 this.n5=0;	
		 
		 this.i_arlData = new ArrayList<ItemData>();
		 i_count = 0;
		 
	}
	
	
	public void setSyskey(long args ){
		this.syskey=args;
	}
	public long getSyskey() {
		return this.syskey;
	}
	
	public void setAutokey(long args ){
		this.autokey=args;
	}
	public long getAutokey() {
		return this.autokey;
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
	
	public void setN1(double args ){
		this.n1=args;
	}
	public double getN1() {
		return this.n1;
	}
	
	public void setN2(long args ){
		this.n2=args;
	}
	public long getN2() {
		return this.n2;
	}	
	
	public void setN3(double args ){
		this.n3=args;
	}
	public double getN3() {
		return this.n3;
	}
	
	public void setN4( double args ){
		this.n4=args;
	}
	public double getN4() {
		return this.n4;
	}
	
	public void setN5( long args ){//ItemCount
		this.n5=args;
	}
	public long getN5(){
		return this.n5;
	}
	
	
	//reference data
	public  ArrayList<ItemData> getItemDataList(){
		return   this.i_arlData;
	}
	public  void setItemDataList(ArrayList<ItemData> value){
		this.i_arlData = value;
	}
	
	public void setItemCount(long args ){
		this.i_count=args;
	}
	public long getItemCount() {
		return this.i_count;
	}
		
	
	
	/*private String religion ;
	public void setReligionname(String args){
		this.religion=args;
	}
	public String getReligionname(){
		return this.religion;
	}	
	 * private ArrayList<SubjectData> subjectList;
	public ArrayList<SubjectData> getActSubjectList() {
		return subjectList;
	}
	public void setActSubjectList(ArrayList<SubjectData> subjList) {
		this.subjectList = subjList;
	}
	
	private SubjectData subjData;
	public SubjectData getSubjectData() {
		return subjData;
	}
	public void setSubjectData(SubjectData subjData) {
		this.subjData = subjData;
	}*/	
	
	/*private boolean errStudName;
	public boolean isErrName() {
		return errStudName;
	}
	public void setErrStudName(boolean errStudName) {
		this.errStudName = errStudName;
	}
	private boolean errNRC;
	public boolean isErrNRC() {
		return errNRC;
	}
	public void setErrNRC(boolean errNRC) {
		this.errNRC = errNRC;
	}
	private boolean errDOB;
	public boolean isErrDOB() {
		return errDOB;
	}
	public void setErrDOB(boolean errDOB) {
		this.errDOB = errDOB;
	}
	private boolean errFName;
	public boolean isErrFName() {
		return errFName;
	}
	public void setErrFName(boolean errFName) {
		this.errFName = errFName;
	}
	private boolean errReligion;
	public boolean isErrReligion() {
		return errReligion;
	}
	public void setErrReligion(boolean errReligion) {
		this.errReligion = errReligion;
	}
	private boolean errYear;
	public boolean isErrYear() {
		return errYear;
	}
	public void setErrYear(boolean errYear) {
		this.errYear = errYear;
	}
	private boolean errSubject;
	public boolean isErrSubject() {
		return errSubject;
	}
	public void setErrSubject(boolean errSubject) {
		this.errSubject = errSubject;
	}
	private boolean errMark;
	public boolean isErrMark() {
		return errMark;
	}
	public void setErrMark(boolean errMark) {
		this.errMark = errMark;
	}*/
	
}
