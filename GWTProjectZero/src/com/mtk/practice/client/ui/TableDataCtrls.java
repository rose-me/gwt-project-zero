package com.mtk.practice.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

public class TableDataCtrls {
	
	FlexTable tableControl = new FlexTable();
	Element table = tableControl.getElement();
	Element thead = DOM.createTHead();
	Element tr = DOM.createTR();
	Element tableAct = tableControl.getElement();
	Element theadAct = DOM.createTHead();
	Element trAct = DOM.createTR();
	int rowIndex = 1;
	HTMLTable.RowFormatter t_rowFormatter = tableControl.getRowFormatter();
	HTMLTable.CellFormatter t_cellFormatter = tableControl.getCellFormatter();
	
	
	public TableDataCtrls(){		
		//tableControl.setStyleName("tableActivityFrmSorter", false);
		tableControl.getElement().setId("mytable");
		tableControl.addStyleName("tablesorter");
		table.appendChild(thead);
		thead.appendChild(tr);
		
	}
	public TableDataCtrls(boolean flag){
		//tableControl.setStyleName("tablesorter", false);
		tableControl.getElement().setId("mytable");
		tableControl.addStyleName("tableActivityFrmSorter");
		table.appendChild(thead);
		thead.appendChild(tr);
		
	}
	
	public FlexTable getTableDataControl(){
		return tableControl;
	}
	
	public void setHeader(int columnIndex, String columnCaption, String columnWidth){
		Element col1 = DOM.createTH();
		col1.setInnerText(columnCaption);
		col1.setAttribute("width", columnWidth);
		col1.setAttribute("word-wrap", "break-word");
		tr.appendChild(col1);
	}
	
	public void insertRow(){
		rowIndex = tableControl.getRowCount();
		tableControl.insertRow(rowIndex);
		t_rowFormatter.setVerticalAlign(rowIndex, HasVerticalAlignment.ALIGN_TOP);
	}
	
	public void deleteRow(int index){
		tableControl.removeRow(index);
	}

	public int rowCount(){
		rowIndex = tableControl.getRowCount();
		return rowIndex;
	}
	
	public int getListRowCount(){
		return tableControl.getRowCount()-1;
	}
	
	public void setWidget(int columnIndex, Widget w){		
		tableControl.setWidget(tableControl.getRowCount() - 1, columnIndex, w);
	}
	
	public void setText(int columnIndex, String s){		
		tableControl.setText(tableControl.getRowCount() - 1, columnIndex, s);
		t_cellFormatter.setVerticalAlignment(tableControl.getRowCount() - 1, columnIndex, HasVerticalAlignment.ALIGN_TOP);
	}
	
	public void setWidget(int columnIndex, Widget w, String stylename){		
		t_cellFormatter.addStyleName(tableControl.getRowCount() - 1, columnIndex, stylename);
		tableControl.setWidget(tableControl.getRowCount() - 1, columnIndex, w);		
	}
	
	/*public void callSorting(final String id, final int columnIndex){
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {					
			@Override
			public void execute() {
				sortTable(id, columnIndex);	
			}
		});
	}
	
	public native void sortTable(String id, int columnIndex)-{
		$wnd.$("#" + id).colResizable({disable:true});
		$wnd.sorting(id, columnIndex);
	}-;*/
	
	/*public void setMultiWidget() {
		//1
		Grid g = new Grid(1,3);
		g.setWidget(0, 0, hrcustype);
		g.setWidget(0, 1, m_searhControlPnl);
		g.setWidget(0, 2, hrexcelexport);
		m_list.add(g);//verpanel
		
		//2
		final FlowPanel fpanel = new FlowPanel();
		fpanel.add(new Label(""));
		fpanel.add(new Label(""));
		
		final FlexTable ftable = new FlexTable();
		ftable.setWidget(0, 0, fpanel);
		
		//3
		 Widget w1 = new Label("");
		 Widget w2 = new Label("");
		 final FlowPanel fpanel = new FlowPanel();
		 fpanel.add(w1);
		 fpanel.add(w2);
		 final FlexTable ftable = new FlexTable();
		 ftable.setWidget(0,0,fpanel);
	}*/
	
	
}
