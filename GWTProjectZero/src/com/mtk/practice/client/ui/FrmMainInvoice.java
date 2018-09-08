package com.mtk.practice.client.ui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.mtk.practice.client.ClientUtility;
import com.mtk.practice.client.MainView;
import com.mtk.practice.client.Message;
import com.mtk.practice.client.service.ServiceControllerMgr;
import com.mtk.practice.shared.InvoiceData;
import com.mtk.practice.shared.ItemData;
import com.mtk.practice.shared.Result;

public class FrmMainInvoice extends Composite {

	private static FrmMainInvoiceUiBinder uiBinder = GWT.create(FrmMainInvoiceUiBinder.class);

	interface FrmMainInvoiceUiBinder extends UiBinder<Widget, FrmMainInvoice> {
	}
	
	//Uibinder
	@UiField
	VerticalPanel uiVPanel;
	@UiField
	HorizontalPanel uiHPanelTitle;
	@UiField
	FlowPanel uiInvoice;
	@UiField
	ScrollPanel uiActivityScroll;
	
	@UiField
	Label lblTitle;	
	@UiField
	TextBox txtInv;
	@UiField
	TextBox txtSubTotal;
	@UiField
	TextBox txtTaxpercentage;
	@UiField
	TextBox txtTaxvalue;
	@UiField
	TextBox txtTotal;
	@UiField
	Button butActAddItem;
	@UiField
	Button butSave;
	
	boolean isFrom;
	long invSK = 0;
	long noOfItem = 0;
	PopupPanel mpop = new PopupPanel(false);
	AsyncCallback<Result> m_callbackLink;
	InvoiceData i_Data = new InvoiceData();	
	ItemData junData = new ItemData();
	ArrayList<ItemData> itemDataList = new ArrayList<ItemData>();
	TableDataCtrls itemGrid = new TableDataCtrls(true);
	private MainView parentview;//parent view
	
	public FrmMainInvoice(long mSK, boolean isLink, PopupPanel pop, MainView container, AsyncCallback<Result> p_callbackLink) {
		initWidget(uiBinder.createAndBindUi(this));	
		parentview = container;
		mpop = pop;
		isFrom = isLink;	//true for lbldesc(item name)click and false for add invoice click	
		m_callbackLink = p_callbackLink;
		invSK = mSK;
		//butActAddItem = new Button("");
		butActAddItem.setStyleName("searchButton");
		butActAddItem.setHTML("<div width='150px'><img src = 'images/plus-icon.png' width='13px' height='13px' "
				+ "align='left' /><Label width='20px' ></Label><Label width='50px' >Add Item</Label></div>");
		mpop.setStyleName("popup");
		mpop.setWidth("100%");
		mpop.setHeight("650px");		
		uiHPanelTitle.setStyleName("popup-title");
		lblTitle.setSize("340px", "20px");
		lblTitle.setStyleName("delete-label");
		lblTitle.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);		
		
		uiVPanel.add(uiHPanelTitle);
		uiVPanel.add(uiInvoice);		
		
		if(isFrom) { //datagrid lbldesc click
			lblTitle.setText("Update Invoice");
			butSave.setText("Update");
			ServiceControllerMgr.readInvoiceDataGrid(invSK, getcallbackInvoice());	
			
		} else { //datagrid add item click
			lblTitle.setText("New Invoice");
			butSave.setText("Create");
			i_Data = new InvoiceData();
			settingDefault();
			showActivityItemGrid();			
		}		
		
		mpop.setGlassEnabled(true);
		mpop.setPopupPosition(350, 120);
		mpop.show();
		
	}
	
	AsyncCallback<InvoiceData> getcallbackInvoice() {
		return new AsyncCallback<InvoiceData>() {
			public void onFailure(Throwable caught) {
				Window.alert("ServerError");
			}
			public void onSuccess(InvoiceData result) {
				if (result != null) {
					invSK = result.getSyskey();						
					updateScreen(result);
					updateScreenJunItem(itemDataList);
					
				}else{
					uiActivityScroll.clear();	
				}
			} 			
		};
	}

	private void updateScreen(InvoiceData result) {
		i_Data = result;
		invSK = result.getSyskey();		
		itemDataList = result.getItemDataList();
		txtInv.setText(result.getT1());
		txtSubTotal.setText(ClientUtility.formatNumber(result.getN1()));
		txtTaxpercentage.setText(ClientUtility.formatIntegerNumber(result.getN2()));
		txtTaxvalue.setText(ClientUtility.formatNumber(result.getN3()));
		txtTotal.setText(ClientUtility.formatNumber(result.getN4()));		
		
	}
	
	private void updateScreenJunItem(ArrayList<ItemData> junDataList) {		
		if (!junDataList.equals(null)) {
			bindActivityGridHeader();
			for (int i = 0; i < junDataList.size(); i++) {
				if (i >= 5) {
					uiActivityScroll.setHeight("200px");
				}
				
				rebindActivityItemGrid(junDataList.get(i));				
			}
		} else {
			uiActivityScroll.clear();
		}
		butSave.setEnabled(true);
	}
	
	private void rebindActivityItemGrid(ItemData result) {
		int colindex = 0;
		itemGrid.insertRow();
		
		final TextBox txtAItem = new TextBox();
		txtAItem.setWidth("200px");
		txtAItem.setAlignment(TextAlignment.LEFT);
		txtAItem.setStyleName("rp-txtbox");
		txtAItem.setText(result.getT1());		
		itemGrid.setWidget(colindex++, txtAItem);//item name
		
		final TextBox txtANoItem = new TextBox();
		txtANoItem.setWidth("100px");
		txtANoItem.setAlignment(TextAlignment.RIGHT);
		txtANoItem.setStyleName("rp-txtbox");
		txtANoItem.setText(result.getN1() + "");
		checkNumberTextbox1(txtANoItem);
		itemGrid.setWidget(colindex++, txtANoItem);//#of items
		
		final TextBox txtAPrice = new TextBox();
		txtAPrice.setWidth("150px");
		txtAPrice.setAlignment(TextAlignment.RIGHT);
		txtAPrice.setStyleName("rp-txtbox");
		txtAPrice.setText(result.getN2() + "");	
		checkNumberTextbox(txtAPrice);
		itemGrid.setWidget(colindex++, txtAPrice);//price
		
		final TextBox txtATotal = new TextBox();
		txtATotal.setWidth("160px");
		txtATotal.setAlignment(TextAlignment.RIGHT);
		txtATotal.setStyleName("rp-txtbox");
		txtATotal.setText(result.getN3() + "");	
		checkNumberTextbox(txtATotal);
		txtATotal.setTitle(itemGrid.getTableDataControl().getRowCount()+"");
		itemGrid.setWidget(colindex++, txtATotal);//total
		
		final Image imgItemDelete = new Image("images/delete4.png");
		imgItemDelete.setWidth("16px");
		imgItemDelete.setHeight("16px");
		imgItemDelete.setStyleName("cursor-pointer");
		imgItemDelete.setTitle(String.valueOf(itemGrid.getListRowCount()));
		itemGrid.setWidget(colindex++, imgItemDelete);
		
		imgItemDelete.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteGridRow(Integer.parseInt(imgItemDelete.getTitle()));
			}
		});
		
		txtAItem.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {				
				if (event.getNativeKeyCode() == 13) {					
					txtANoItem.setFocus(true);
					txtANoItem.selectAll();
				}

			}
		});
		
		txtANoItem.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {				
				if (event.getNativeKeyCode() == 13) {					
					txtAPrice.setFocus(true);
					txtAPrice.selectAll();
				}

			}
		});
		
		txtAPrice.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13
						|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					double cPerTotal = 0.00;
					String price = txtAPrice.getText() + "";
					txtAPrice.setText(ClientUtility.formatNumber(Double
							.parseDouble(price.replace(",", ""))));
					cPerTotal = calculatePerTotal(
							Double.parseDouble(txtANoItem.getText().replace(",",
									"")),
							Double.parseDouble(txtAPrice.getText().replace(",",
									"")));
					txtATotal.setText(ClientUtility.formatNumber(cPerTotal));
					txtATotal.setFocus(true);
				}
			}
		});

		txtATotal.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13
						|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					if(Integer.parseInt(txtATotal.getTitle()) < itemGrid.getTableDataControl().getRowCount()){
						FlexTable eqflx = itemGrid.getTableDataControl();
						TextBox txt = (TextBox) eqflx.getWidget(Integer.parseInt(txtATotal.getTitle())+1, 0);//set focus item column
						txt.setFocus(true);
						txt.selectAll();
						
					} else {								
						txtSubTotal.setFocus(true);						
					}						
				}
			}
		});			
		
		//uiActivityScroll.add(itemGrid.getTableDataControl());
		if (itemGrid.getTableDataControl().getRowCount() >= 5) {
			uiActivityScroll.setHeight("200px");
		}
	}
	
	public void settingDefault() {
		setFocus(txtInv, true);
		txtTaxpercentage.setMaxLength(2);
		checkNumberTextbox(txtSubTotal);
		checkNumberTextbox(txtTaxvalue);
		checkNumberTextbox(txtTotal);
		checkNumberTextbox1(txtTaxpercentage);		
	}
	
	public void showActivityItemGrid() {		
		bindActivityGridHeader();				
		bindDefaultActivityGrid();		
		if (itemGrid.getTableDataControl().getOffsetHeight() > 200) {
			uiActivityScroll.setHeight("200px");
		} else {
			uiActivityScroll.setHeight("");
		}		
	}
	
	public static void setFocus(final FocusWidget wid, final boolean status) {
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				wid.setFocus(status);
			}
		});
	}
	
	public void checkNumberTextbox(final TextBox pTextbox) {
		if (pTextbox.getText().equals("")) {
			pTextbox.setText("0.00");
		} else {
			pTextbox.setText(ClientUtility.formatNumber(Double
					.parseDouble(pTextbox.getText().replace(",", ""))));
		}

		pTextbox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (!Character.isDigit(event.getCharCode())
						&& (event.getCharCode() != '.' && (event.getCharCode() != '-'))
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_DELETE)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_HOME)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_END)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_LEFT)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_RIGHT)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_ENTER))
					((TextBox) event.getSource()).cancelKey();
			}
		});

		pTextbox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13
						|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					if (pTextbox.getText().equals(""))
						pTextbox.setText("0.00");
					else
						pTextbox.setText(ClientUtility.formatNumber(Double
								.parseDouble(pTextbox.getText()
										.replace(",", ""))));
				}
			}

		});

	}

	public void checkNumberTextbox1(final TextBox pTextbox) {
		pTextbox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (!Character.isDigit(event.getCharCode())
						&& (event.getCharCode() != '.' && (event.getCharCode() != '-'))
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_DELETE)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_HOME)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_END)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_LEFT)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_RIGHT && (event
								.getNativeEvent().getKeyCode() != KeyCodes.KEY_ENTER)))
					((TextBox) event.getSource()).cancelKey();
			}
		});
		pTextbox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == 13
						|| event.getNativeKeyCode() == KeyCodes.KEY_TAB)
					if (pTextbox.getText().equals(""))
						pTextbox.setText("0");
			}
		});
	}
	
	private void bindActivityGridHeader() {
		int colindex = 0;		
		itemGrid = new TableDataCtrls(true);
		itemGrid.getTableDataControl().setSize("300px", "");///setsize itemgrid
		itemGrid.setHeader(colindex++, "Item Name", "100px");
		itemGrid.setHeader(colindex++, "#of Items", "50px");
		itemGrid.setHeader(colindex++, "Price", "130px");
		itemGrid.setHeader(colindex++, "Total", "120px");
		itemGrid.setHeader(colindex++, "", "50px");
		
		uiActivityScroll.add(itemGrid.getTableDataControl());
	}	
	
	private void bindDefaultActivityGrid() {
		for (int i = 0; i < 2; i++) {
			int colindex = 0;
			uiActivityScroll.clear();			
			itemGrid.insertRow();
			
			final TextBox txtAItem = new TextBox();
			txtAItem.setWidth("200px");
			txtAItem.setAlignment(TextAlignment.LEFT);
			txtAItem.setStyleName("rp-txtbox");
			txtAItem.setText("");		
			itemGrid.setWidget(colindex++, txtAItem);//item name
			
			final TextBox txtANoItem = new TextBox();
			txtANoItem.setWidth("100px");
			txtANoItem.setAlignment(TextAlignment.RIGHT);
			txtANoItem.setStyleName("rp-txtbox");
			txtANoItem.setText("0");
			checkNumberTextbox1(txtANoItem);
			itemGrid.setWidget(colindex++, txtANoItem);//#of items
			
			final TextBox txtAPrice = new TextBox();
			txtAPrice.setWidth("150px");
			txtAPrice.setAlignment(TextAlignment.RIGHT);
			txtAPrice.setStyleName("rp-txtbox");
			txtAPrice.setText("0");	
			checkNumberTextbox(txtAPrice);
			itemGrid.setWidget(colindex++, txtAPrice);//price
			
			final TextBox txtATotal = new TextBox();
			txtATotal.setWidth("160px");
			txtATotal.setAlignment(TextAlignment.RIGHT);
			txtATotal.setStyleName("rp-txtbox");
			txtATotal.setText("0");	
			checkNumberTextbox(txtATotal);
			txtATotal.setTitle(itemGrid.getTableDataControl().getRowCount()+"");
			itemGrid.setWidget(colindex++, txtATotal);//total
			
			final Image imgItemDelete = new Image("images/delete4.png");
			imgItemDelete.setWidth("16px");
			imgItemDelete.setHeight("16px");
			imgItemDelete.setStyleName("cursor-pointer");
			imgItemDelete.setTitle(String.valueOf(itemGrid.getListRowCount()));
			itemGrid.setWidget(colindex++, imgItemDelete);
			
			imgItemDelete.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					deleteGridRow(Integer.parseInt(imgItemDelete.getTitle()));
				}
			});
			
			txtAItem.addKeyDownHandler(new KeyDownHandler() {
				public void onKeyDown(KeyDownEvent event) {				
					if (event.getNativeKeyCode() == 13) {					
						txtANoItem.setFocus(true);
						txtANoItem.selectAll();
					}

				}
			});
			
			txtANoItem.addKeyDownHandler(new KeyDownHandler() {
				public void onKeyDown(KeyDownEvent event) {				
					if (event.getNativeKeyCode() == 13) {
						//noOfItem = Long.parseLong(txtANoItem.getText());
						txtAPrice.setFocus(true);
						txtAPrice.selectAll();
					}

				}
			});
			
			txtAPrice.addKeyDownHandler(new KeyDownHandler() {
				public void onKeyDown(KeyDownEvent event) {
					if (event.getNativeKeyCode() == 13
							|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
						double cPerTotal = 0.00;
						String price = txtAPrice.getText() + "";
						txtAPrice.setText(ClientUtility.formatNumber(Double
								.parseDouble(price.replace(",", ""))));
						cPerTotal = calculatePerTotal(
								Double.parseDouble(txtANoItem.getText().replace(",",
										"")),
								Double.parseDouble(txtAPrice.getText().replace(",",
										"")));
						txtATotal.setText(ClientUtility.formatNumber(cPerTotal));
						txtATotal.setFocus(true);
					}
				}
			});

			txtATotal.addKeyDownHandler(new KeyDownHandler() {
				public void onKeyDown(KeyDownEvent event) {
					if (event.getNativeKeyCode() == 13
							|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
						if(Integer.parseInt(txtATotal.getTitle()) < itemGrid.getTableDataControl().getRowCount()){
							FlexTable eqflx = itemGrid.getTableDataControl();
							TextBox txt = (TextBox) eqflx.getWidget(Integer.parseInt(txtATotal.getTitle())+1, 0);//set focus item column
							txt.setFocus(true);
							txt.selectAll();
							
						} else {								
							txtSubTotal.setFocus(true);						
						}						
					}
				}
			});			
			
			uiActivityScroll.add(itemGrid.getTableDataControl());
			if (itemGrid.getTableDataControl().getRowCount() >= 5) {
				uiActivityScroll.setHeight("200px");
			}
		}//end for		
		
	}
	
	private void bindActivityGrid() {		
		int colindex = 0;
		//uiActivityScroll.clear();			
		itemGrid.insertRow();
		
		final TextBox txtAItem = new TextBox();
		txtAItem.setWidth("200px");
		txtAItem.setAlignment(TextAlignment.LEFT);
		txtAItem.setStyleName("rp-txtbox");
		txtAItem.setText("");		
		itemGrid.setWidget(colindex++, txtAItem);//item name
		
		final TextBox txtANoItem = new TextBox();
		txtANoItem.setWidth("100px");
		txtANoItem.setAlignment(TextAlignment.RIGHT);
		txtANoItem.setStyleName("rp-txtbox");
		txtANoItem.setText("0");
		checkNumberTextbox1(txtANoItem);
		itemGrid.setWidget(colindex++, txtANoItem);//#of items
		
		final TextBox txtAPrice = new TextBox();
		txtAPrice.setWidth("150px");
		txtAPrice.setAlignment(TextAlignment.RIGHT);
		txtAPrice.setStyleName("rp-txtbox");
		txtAPrice.setText("0");	
		checkNumberTextbox(txtAPrice);
		itemGrid.setWidget(colindex++, txtAPrice);//price
		
		final TextBox txtATotal = new TextBox();
		txtATotal.setWidth("160px");
		txtATotal.setAlignment(TextAlignment.RIGHT);
		txtATotal.setStyleName("rp-txtbox");
		txtATotal.setText("");	
		checkNumberTextbox(txtATotal);
		txtATotal.setTitle(itemGrid.getTableDataControl().getRowCount()+"");
		itemGrid.setWidget(colindex++, txtATotal);//total
		
		final Image imgItemDelete = new Image("images/delete4.png");
		imgItemDelete.setWidth("16px");
		imgItemDelete.setHeight("16px");
		imgItemDelete.setStyleName("cursor-pointer");
		imgItemDelete.setTitle(String.valueOf(itemGrid.getListRowCount()));
		itemGrid.setWidget(colindex++, imgItemDelete);
		
		imgItemDelete.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteGridRow(Integer.parseInt(imgItemDelete.getTitle()));
			}
		});
		
		txtAItem.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {				
				if (event.getNativeKeyCode() == 13) {					
					txtANoItem.setFocus(true);
					txtANoItem.selectAll();
				}
	
			}
		});
		
		txtANoItem.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {				
				if (event.getNativeKeyCode() == 13) {					
					txtAPrice.setFocus(true);
					txtAPrice.selectAll();
				}
	
			}
		});
		
		txtAPrice.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13
						|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					double cPerTotal = 0.00;
					String price = txtAPrice.getText() + "";
					txtAPrice.setText(ClientUtility.formatNumber(Double
							.parseDouble(price.replace(",", ""))));
					cPerTotal = calculatePerTotal(
							Double.parseDouble(txtANoItem.getText().replace(",",
									"")),
							Double.parseDouble(txtAPrice.getText().replace(",",
									"")));
					txtATotal.setText(ClientUtility.formatNumber(cPerTotal));
					txtATotal.setFocus(true);
				}
			}
		});
	
		txtATotal.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13
						|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					if(Integer.parseInt(txtATotal.getTitle()) < itemGrid.getTableDataControl().getRowCount()){
						FlexTable eqflx = itemGrid.getTableDataControl();
						for (int j = 0; j < eqflx.getRowCount(); j++) {
							TextBox txt = new TextBox();
							txt = (TextBox) eqflx.getWidget(j+1, 0);
							txt.setFocus(true);
							txt.selectAll();
						}
					}else{
						txtTotal.setFocus(true);
					}
				}
			}
		});			
		
		uiActivityScroll.add(itemGrid.getTableDataControl());
		if (itemGrid.getTableDataControl().getRowCount() >= 5) {
			uiActivityScroll.setHeight("200px");
		}
		
		
	}
	
	private void deleteGridRow(final int row) {
		final PopupPanel popup = new PopupPanel();
		popup.setStyleName("popup");

		VerticalPanel vPanel = new VerticalPanel();
		HorizontalPanel horTitle = new HorizontalPanel();
		HorizontalPanel horText = new HorizontalPanel();
		HorizontalPanel horBut = new HorizontalPanel();
		HorizontalPanel hor = new HorizontalPanel();

		horTitle.setWidth("350px");
		horText.setWidth("350px");
		hor.setWidth("350px");

		horTitle.setHeight("30px");
		horText.setHeight("25px");
		hor.setHeight("25px");

		horText.setStyleName("horBorder");

		Label lblTitle = new Label("Delete This Item?");
		lblTitle.setStyleName("crm-delete");
		lblTitle.setWidth("350px");

		Label lblText = new Label(
				"Are you sure you want to delete? This can't be undone.");

		Button butOK = new Button("Delete");
		butOK.setStyleName("delete-Button");

		Button butCancel = new Button("Cancel");
		butCancel.setStyleName("delete-Button");

		Label lblSpace = new Label("");
		lblSpace.setWidth("5px");

		horTitle.add(lblTitle);
		horTitle.getWidget(0).getElement()
				.setAttribute("style", "padding: 5px 0px 0px 10px");
		horText.add(lblText);
		horText.getWidget(0).getElement()
				.setAttribute("style", "padding: 0px 0px 0px 10px");
		horBut.add(butOK);
		horBut.add(lblSpace);
		horBut.add(butCancel);
		hor.add(horBut);
		hor.getWidget(0).getElement()
				.setAttribute("style", "padding: 0px 10px 0px 10px");
		hor.setCellHorizontalAlignment(horBut,
				HasHorizontalAlignment.ALIGN_RIGHT);

		vPanel.add(horTitle);
		vPanel.add(horText);
		vPanel.add(hor);

		popup.add(vPanel);

		popup.setHeight("100px");
		popup.setWidth("350px");
		popup.setVisible(true);
		popup.setPopupPosition(300, 100);
		popup.setGlassEnabled(true);
		popup.show();

		butOK.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				itemGrid.deleteRow(row);
				reInsertActivityGrid(itemGrid);
				popup.hide();
			}
		});

		butCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				popup.hide();
			}
		});
	}
	
	private void reInsertActivityGrid(TableDataCtrls pGrid) {
		FlexTable flx = pGrid.getTableDataControl();
		bindActivityGridHeader();
		if (flx.getRowCount() > 0) {
			for (int i = 0; i < flx.getRowCount(); i++) {
				int colindex = 0;
				itemGrid.insertRow();				

				final TextBox txtAItem  = (TextBox) flx.getWidget(i, 0);
				itemGrid.setWidget(colindex++, txtAItem ); // Description.

				final TextBox txtANoItem  = (TextBox) flx.getWidget(i, 1);
				itemGrid.setWidget(colindex++, txtANoItem ); // Activity Type.				

				final TextBox txtAPrice  = (TextBox) flx.getWidget(i, 2);
				itemGrid.setWidget(colindex++, txtAPrice ); // Date.	
				
				final TextBox txtATotal  = (TextBox) flx.getWidget(i, 3);
				itemGrid.setWidget(colindex++, txtATotal );

				final Image imgItemDelete = new Image("image/delete3.png");
				imgItemDelete.setWidth("14px");
				imgItemDelete.setHeight("14px");
				imgItemDelete.setStyleName("cursor-pointer");
				imgItemDelete.setTitle(String.valueOf(itemGrid.getListRowCount()));//rowcount start 1
				itemGrid.setWidget(colindex++, imgItemDelete);
				
				imgItemDelete.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						deleteGridRow(Integer.parseInt(imgItemDelete.getTitle()));
					}
				});
				
				txtAItem.addKeyDownHandler(new KeyDownHandler() {
					public void onKeyDown(KeyDownEvent event) {				
						if (event.getNativeKeyCode() == 13) {					
							txtANoItem.setFocus(true);
							txtANoItem.selectAll();
						}

					}
				});
				
				txtANoItem.addKeyDownHandler(new KeyDownHandler() {
					public void onKeyDown(KeyDownEvent event) {				
						if (event.getNativeKeyCode() == 13) {					
							txtAPrice.setFocus(true);
							txtAPrice.selectAll();
						}

					}
				});
				
				txtAPrice.addKeyDownHandler(new KeyDownHandler() {
					public void onKeyDown(KeyDownEvent event) {
						if (event.getNativeKeyCode() == 13
								|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
							double cPerTotal = 0.00;
							String price = txtAPrice.getText() + "";
							txtAPrice.setText(ClientUtility.formatNumber(Double
									.parseDouble(price.replace(",", ""))));
							cPerTotal = calculatePerTotal(
									Double.parseDouble(txtANoItem.getText().replace(",",
											"")),
									Double.parseDouble(txtAPrice.getText().replace(",",
											"")));
							txtATotal.setText(ClientUtility.formatNumber(cPerTotal));
							txtATotal.setFocus(true);
						}
					}
				});

				txtATotal.addKeyDownHandler(new KeyDownHandler() {
					public void onKeyDown(KeyDownEvent event) {
						if (event.getNativeKeyCode() == 13
								|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
							if(Integer.parseInt(txtATotal.getTitle()) < itemGrid.getTableDataControl().getRowCount()){
								FlexTable eqflx = itemGrid.getTableDataControl();
								for (int j = 0; j < eqflx.getRowCount(); j++) {
									TextBox txt = new TextBox();
									txt = (TextBox) eqflx.getWidget(j+1, 0);
									txt.setFocus(true);
									txt.selectAll();
								}
							}else{
								txtTotal.setFocus(true);
							}
						}
					}
				});
				
				uiActivityScroll.add(itemGrid.getTableDataControl());
				if (itemGrid.getTableDataControl().getRowCount() >= 5) {
					uiActivityScroll.setHeight("200px");
				}
			}
		} else
			itemGrid.getTableDataControl().clear();
	}	
	
	private double calculatePerTotal(double qty, double qamt) {
		double tamt = 0.00;
		tamt = qty * qamt;
		return tamt;
	}
	
	@UiHandler("butSave")
	void onButSaveClick(ClickEvent event) {
		if(isValid()) {
			noOfItem = getCountItem();
			updateObjectJunItemList();
			updateObject(i_Data);
			ServiceControllerMgr.saveInvoice(i_Data, savecallbackMaster());
		}
		/*updateObjectJunItemList();
		updateObject(i_Data);*/
		/*if(isValid()) {
			updateObjectJunItemList();
			updateObject(i_Data);*/
			//ServiceControllerMgr.saveInvoice(i_Data, savecallbackMaster());
			/*if(!isFrom) {//click add new invoice
				ServiceControllerMgr.saveInvoice(i_Data, savecallbackMaster());				
			} 
			else { //click link from FrmInvoice
				ServiceControllerMgr.saveInvoice(i_Data, m_callbackLink);
			}*/
					
		//}
	}	

	AsyncCallback<Result> savecallbackMaster() {
		return new AsyncCallback<Result>() {
			public void onFailure(Throwable caught) {
				//Message.jsni("New Invoice");
				Window.alert("New Invoice");
			}
			public void onSuccess(Result result) {
				if (result.getState()) {
					//Message.jsni("normal|Saved Successfully.");
					Window.alert("normal|Saved Successfully.");
					invSK = result.getLongResult().get(0);	
					parentview.openInvoiceDatagrid();
					mpop.show();
					/*mpop.hide();
					mpop.clear();*/					

				} else {
					/*if (!result.getMsgCode().equals(""))
						Message.jsni("warn|" + result.getMsgCode());
					if (!result.getMsgDesc().equals(""))
						Message.jsni("warn|" + result.getMsgDesc());*/
					Window.alert("warn| Code Already Exists.");
				}

			}
		};
	}
	
	private boolean isValid() {
		if (txtInv.getText().equals("")) {
			//Message.jsni("warn|Please provide the Invoice Name.");
			Window.alert("warn|Please provide the Invoice Name.");
			return false;
		}		
		return true;
	}	
	
	private long getCountItem() {
		noOfItem = 0;
		FlexTable eqflx = itemGrid.getTableDataControl();
		
		for(int i=0; i< itemGrid.getTableDataControl().getRowCount(); i++) {
			TextBox txt = (TextBox) eqflx.getWidget(i, 1);
			noOfItem += Long.parseLong(txt.getText());
		}
		return noOfItem;
	}
	
	private void updateObjectJunItemList() { // ItemData.
		try {
			itemDataList.clear();
			FlexTable eqflx = itemGrid.getTableDataControl();
			if (eqflx.getRowCount() > 0) {
				System.out.println("rowcount="+eqflx.getRowCount());
				for (int j = 0; j < eqflx.getRowCount(); j++) {
					junData = new ItemData();
					junData.setParentid(invSK);
					
					TextBox txt = new TextBox();
					txt = (TextBox) eqflx.getWidget(j, 0);
					junData.setT1(txt.getText());//item name
					
					TextBox txt2 = new TextBox();
					txt2 = (TextBox) eqflx.getWidget(j, 1);
					junData.setN1(Long.parseLong(txt2.getText()));//#of items
					
					TextBox txt3 = new TextBox();
					txt3 = (TextBox) eqflx.getWidget(j, 2);
					junData.setN2(Double.parseDouble(txt3.getText().replace(",",
							"")));
					System.out.println("n2="+txt3.getText().replace(",",
							""));//out
					TextBox txt4 = new TextBox();
					txt4 = (TextBox) eqflx.getWidget(j, 3);
					junData.setN3(Double.parseDouble(txt4.getText().replace(",",
							"")));	
					System.out.println("n3="+txt4.getText().replace(",",
							""));//out
					System.out.println("n3="+junData.getN3());//out
					
					itemDataList.add(junData);
				}

			}
		} catch (Exception e) {
			// Window.alert(e.toString());
		}		
		
		i_Data.setItemDataList(itemDataList);
	}
	
	private void updateObject(InvoiceData result) {
		result.setSyskey(invSK);
		String sDate = ClientUtility.getDateFormatString(new Date());
		
		if (invSK == 0) {
			result.setCreateddate(sDate);
		}
		result.setModifieddate(sDate);
		result.setRecordstatus(1);
		result.setT1(txtInv.getText());
		result.setN1(Double.parseDouble(txtSubTotal.getText().replace(",", "")));
		result.setN2(Long.parseLong(txtTaxpercentage.getText().replace(",", "")));
		result.setN3(Double.parseDouble(txtTaxvalue.getText().replace(",", "")));
		result.setN4(Double.parseDouble(txtTotal.getText().replace(",", "")));
		result.setN5(noOfItem);
	}
	
	@UiHandler("imageClose")
	void onImageClick(ClickEvent event) {
		if (mpop != null)
			mpop.hide();
	}
	
	int actrow = 0;
	@UiHandler("butActAddItem")
	void onButActivityAddClick(ClickEvent event) {
		if (actrow != 0) {//when click edit button			
			//do nothing
			
		} else {
			if (itemGrid.getTableDataControl().getRowCount() == 0) {
				bindActivityGridHeader();
			}
			bindActivityGrid();
			if (itemGrid.getTableDataControl().getOffsetHeight() > 200) {
				uiActivityScroll.setHeight("200px");
			} else {
				uiActivityScroll.setHeight("");
			}
		}
	}
	
	
	double totalamt = 0.00;
	long Amount = 0;
	@UiHandler("txtSubTotal")
	void onTxtTotalKeyDown(KeyDownEvent event) {
		if (event.getNativeKeyCode() == 13) {
			Window.alert("here1");
			totalamt = 0.00;
			FlexTable eqflx = itemGrid.getTableDataControl();
			if (eqflx.getRowCount() > 0) {
				for (int j = 0; j < eqflx.getRowCount(); j++) {
					TextBox txt = new TextBox();
					txt = (TextBox) eqflx.getWidget(j, 3);
					totalamt += Double.parseDouble(txt.getText().replace(",",
							""));
				}
				txtSubTotal.setText(totalamt + "");
				txtSubTotal.setText(ClientUtility.formatNumber(Double
						.parseDouble(txtSubTotal.getText().replace(",", ""))));
				txtTaxpercentage.setFocus(true);
				txtTaxpercentage.selectAll();
			}
		}
	}
	
	@UiHandler("txtSubTotal")
	void onTxtTotalKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == 13
				|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
			Window.alert("here2");
			totalamt = 0.00;
			FlexTable eqflx = itemGrid.getTableDataControl();
			if (eqflx.getRowCount() > 0) {
				for (int j = 0; j < eqflx.getRowCount(); j++) {
					TextBox txt = new TextBox();
					txt = (TextBox) eqflx.getWidget(j, 3);
					totalamt += Double.parseDouble(txt.getText().replace(",",
							""));
				}
				txtSubTotal.setText(totalamt + "");
				txtSubTotal.setText(ClientUtility.formatNumber(Double
						.parseDouble(txtSubTotal.getText().replace(",", ""))));
				txtTaxpercentage.setFocus(true);
			}
		}
	}

	@UiHandler("txtTaxpercentage")
	void onTxtTaxpercentageKeyDown(KeyDownEvent event) {
		if (event.getNativeKeyCode() == 13
				|| event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
			double taxper = Double.parseDouble(txtTaxpercentage.getText()
					.replace(",", ""));
			if (taxper > 100) {
				taxper = 100;
				txtTaxpercentage.setText(String.valueOf(taxper));
			}
			totalamt = Double.parseDouble(txtSubTotal.getText().replace(",", ""));
			double tax = (totalamt * taxper) / 100;
			String taxval = tax + "";
			double net = totalamt + tax;
			String netval = net + "";
			txtTaxvalue.setText(ClientUtility.formatNumber(Double
					.parseDouble(taxval.replace(",", ""))));
			txtTotal.setText(ClientUtility.formatNumber(Double.parseDouble(netval
					.replace(",", ""))));
			txtTotal.setFocus(true);
		}
	}

}
