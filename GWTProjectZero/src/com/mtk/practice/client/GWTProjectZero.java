package com.mtk.practice.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class GWTProjectZero implements EntryPoint {
	
	/**
	 * This is the entry point method.
	 */	
	
	public void onModuleLoad() {
		MainView mainView = new MainView();
		RootPanel.get().add(mainView);		
  }	
}
