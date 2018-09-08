package com.mtk.practice.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class MenuView extends Composite {
	private HorizontalPanel hPanel = new HorizontalPanel();
	
	
	public MenuView() {
		initWidget(this.hPanel);
		
		this.hPanel.getElement().setId("idtopbar");
		
		Label lblPerson = new Label("BY MAY THU KYAW");		
		lblPerson.getElement().setId("idlblperson");
		this.hPanel.add(lblPerson);	
		
		Label lblTitle = new Label("PROJECT PRACTICE");		
		lblTitle.getElement().setId("idlbltitle");
		this.hPanel.add(lblTitle);
		
			
		
		
	}
}
