package medizin.client.ui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.events.RecordChangeEvent;
import medizin.client.events.RecordChangeHandler;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.QuickSearchBox;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.TextPopupViewImpl;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class InstitutionViewImpl extends Composite implements InstitutionView, RecordChangeHandler  {

	private static InstitutionViewImplUiBinder uiBinder = GWT.create(InstitutionViewImplUiBinder.class);

	interface InstitutionViewImplUiBinder extends UiBinder<Widget, InstitutionViewImpl> {}
	
    private Delegate delegate;

    public BmeConstants constants = GWT.create(BmeConstants.class);
    
    @UiField(provided=true)
    SplitLayoutPanel splitLayoutPanel;
    
    @UiField
    ScrollPanel scrollPanel;
    
    @UiField
    TextBox institutionName;
    
	@UiField
    IconButton addInstitution;
	
	@UiField
	SimplePanel slidingPanel;
	
	@UiField(provided=true)
	AdvanceCellTable<InstitutionProxy> table;
	
	@UiField(provided = true)
	MySimplePager pager;
	
	@UiField (provided = true)
	QuickSearchBox searchBox;
	
	private Map<String, String> columnName=new HashMap<String, String>();
	private List<String> columnNameorder = new ArrayList<String>();
	private List<String> path = new ArrayList<String>();
	private String columnHeader;
	public int x;
	public int y;
	private Sorting sortorder = Sorting.ASC;
	private String sortname = "name";
	private boolean flag;
    /*@UiField
    CellTable<InstitutionProxy> table;
    */
    protected Set<String> paths = new HashSet<String>();

	private int left = 0;

	private int top = 0;
	
	@UiHandler("addInstitution")
	void addInstitutionClicked(ClickEvent event) {
		delegate.newClicked(institutionName.getText());
		institutionName.setText("");
	}

	/*private Presenter presenter;

	private String name;*/

	public InstitutionViewImpl(Map<String, Widget> reciverMap, Boolean flag) {
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new AdvanceCellTable<InstitutionProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);
			
		splitLayoutPanel =new SplitLayoutPanel(){
            @Override
            public void onResize() {
               super.onResize();
               	Double newWidth =splitLayoutPanel.getWidgetSize(scrollPanel);
               	Cookies.setCookie(McAppConstant.INSTITUTION_VIEW_WIDTH, String.valueOf(newWidth));
            }
        };  
        
		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources, true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				table.setVisibleRange(0, McAppConstant.TABLE_PAGE_SIZE);
				delegate.performSearch(searchBox.getValue());
			}
		});
		
		
		initWidget(uiBinder.createAndBindUi(this));
		
		reciverMap.put("institutionName", institutionName);
		
		DOM.setElementAttribute(this.getElement(), "style", "position: absolute; left: 5px; top: 0px; right: 0px; bottom: 0px; overflow: auto;");
		
		if (!flag)
		{
			addInstitution.removeFromParent();
			institutionName.removeFromParent();
			
			pager.getElement().getStyle().setFloat(Float.RIGHT);
		}
		else
		{
			pager.getElement().getStyle().setPaddingLeft(50, Unit.PX);
		}
		
		init(flag);
		//setting widget width from cookie.
        setWidgetWidth();

	}

	private void setWidgetWidth() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.INSTITUTION_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	splitLayoutPanel.setWidgetSize(scrollPanel,Double.valueOf(widgetWidthFromCookie));	
        }
	}

	/*@Override
	public void setName(String helloName) {
		this.name = name;
		
	}*/



	/*@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}*/
	
    public void init(Boolean flag) {
    	editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
    	this.flag=flag;

      /*  paths.add("id");
        table.addColumn(new TextColumn<InstitutionProxy>() {

            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {

                public String render(java.lang.Long obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(InstitutionProxy object) {
                return renderer.render(object.getId());
            }
        }, "Id");
        paths.add("version");
        table.addColumn(new TextColumn<InstitutionProxy>() {

            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {

                public String render(java.lang.Integer obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(InstitutionProxy object) {
                return renderer.render(object.getVersion());
            }
        }, "Version");*/
    	

    	table.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				left = event.getClientX();
				top = event.getClientY();
			}
		}, ClickEvent.getType());

    	columnName.put(constants.institutionLbl(), "institutionName");
		columnNameorder.add(constants.institutionLbl());
        paths.add("institutionName");
        table.addColumn(new TextColumn<InstitutionProxy>() {

            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(InstitutionProxy object) {
                return renderer.render(object == null ? null : object.getInstitutionName());
            }
        }, constants.institutionLbl(),true);
        
        if (flag)
        {
        	addColumn(new ActionCell<InstitutionProxy>(
            		McAppConstant.EDIT_ICON, new ActionCell.Delegate<InstitutionProxy>() {
    					public void execute(final InstitutionProxy institution) {
    						final TextPopupViewImpl popupView = new TextPopupViewImpl();
    						popupView.setText(institution.getInstitutionName());
    						popupView.addSaveClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									delegate.editClicked(institution, popupView.getText());
									popupView.hide();
								}
							});
    						popupView.addCancelClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									popupView.hide();
								}
							});
    						popupView.setPopupPosition((left-200), (top-50));
    						popupView.show();
    					}	
    				}), "", new GetValue<InstitutionProxy>() {
    			public InstitutionProxy getValue(InstitutionProxy institution) {
    				return institution;
    			}
    		}, null);
        	
        	addColumn(new ActionCell<InstitutionProxy>(
            		McAppConstant.DELETE_ICON, new ActionCell.Delegate<InstitutionProxy>() {
    					public void execute(final InstitutionProxy institution) {
    						//Window.alert("You clicked " + institution.getInstitutionName());
    						ConfirmationDialogBox.showYesNoDialogBox(constants.warning(), constants.deleteInstitutionConfirmation(), new ConfirmDialogBoxYesNoButtonEventHandler() {
    							
    							@Override
    							public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
    								delegate.deleteClicked(institution);
    							}
    							
    							@Override
    							public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
    															
    							}
    						});
    						
    						
    					}	
    				}), "", new GetValue<InstitutionProxy>() {
    			public InstitutionProxy getValue(InstitutionProxy institution) {
    				return institution;
    			}
    		}, null);
        	
        	table.addColumnStyleName(1, "iconColumn");
        	table.addColumnStyleName(2, "iconColumn");
        }
        
        
       /* 
    	addColumn(new ActionCell<InstitutionProxy>(
    			McAppConstant.DECLINE_ICON, new ActionCell.Delegate<InstitutionProxy>() {
    	            public void execute(InstitutionProxy institution) {
    	              //Window.alert("You clicked " + institution.getInstitutionName());
    	              delegate.deleteClicked(institution);
    	            }
    	          }), "", new GetValue<InstitutionProxy>() {
    	        public InstitutionProxy getValue(InstitutionProxy contact) {
    	          return contact;
    	        }
    	      }, null);*/
    	
    	
    	
    	table.addColumnStyleName(0, "questionTextColumn");
    	
    	path=getPath();
		addMouseDownHandler();
		addColumnSortHandler();
    }
    
    public void addColumnOnMouseout()
	{
		Set<String> selectedItems = table.getPopup().getMultiSelectionModel().getSelectedSet();

		
		int j = table.getColumnCount();
		while (j > 0) {
			
			table.removeColumn(0);
			j--;
		}

		path.clear();

		Iterator<String> i;
		if (selectedItems.size() == 0) {

			i = table.getPopup().getDefaultValue().iterator();

		} else {
			i = selectedItems.iterator();
		}

		Iterator<String> i1=getColumnNameorder().iterator();

		while (i1.hasNext()) {
		
			
			String colValue=i1.next();

			if(selectedItems.contains(colValue) || table.getInitList().contains(colValue))
			{
				
				if(table.getInitList().contains(colValue))
				{
					table.getInitList().remove(colValue);
				}
			columnHeader = colValue;
			String colName=(String)columnName.get(columnHeader);
			path.add(colName.toString());
				

			if(columnHeader==constants.institutionLbl()){
				
				table.addColumn(new TextColumn<InstitutionProxy>() {

					{
						this.setSortable(true);
						this.setCellStyleNames("tableCellWidth");
					}
		            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

		                public String render(java.lang.String obj) {
		                    return obj == null ? "" : String.valueOf(obj);
		                }
		            };

		            @Override
		            public String getValue(InstitutionProxy object) {
		                return renderer.render(object == null ? "" : object.getInstitutionName());
		            }
		        }, constants.institutionLbl(),false);
				
				if (flag)
		        {
		        	addColumn(new ActionCell<InstitutionProxy>(
		            		McAppConstant.EDIT_ICON, new ActionCell.Delegate<InstitutionProxy>() {
		    					public void execute(final InstitutionProxy institution) {
		    						final TextPopupViewImpl popupView = new TextPopupViewImpl();
		    						popupView.setText(institution.getInstitutionName());
		    						popupView.addSaveClickHandler(new ClickHandler() {
										
										@Override
										public void onClick(ClickEvent event) {
											delegate.editClicked(institution, popupView.getText());
											popupView.hide();
										}
									});
		    						popupView.addCancelClickHandler(new ClickHandler() {
										
										@Override
										public void onClick(ClickEvent event) {
											popupView.hide();
										}
									});
		    						popupView.setPopupPosition((left-200), (top-50));
		    						popupView.show();
		    					}	
		    				}), "", new GetValue<InstitutionProxy>() {
		    			public InstitutionProxy getValue(InstitutionProxy institution) {
		    				return institution;
		    			}
		    		}, null);
		        	
		        	addColumn(new ActionCell<InstitutionProxy>(
		            		McAppConstant.DELETE_ICON, new ActionCell.Delegate<InstitutionProxy>() {
		    					public void execute(final InstitutionProxy institution) {
		    						//Window.alert("You clicked " + institution.getInstitutionName());
		    						ConfirmationDialogBox.showYesNoDialogBox(constants.warning(), constants.deleteInstitutionConfirmation(), new ConfirmDialogBoxYesNoButtonEventHandler() {
		    							
		    							@Override
		    							public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
		    								delegate.deleteClicked(institution);
		    							}
		    							
		    							@Override
		    							public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
		    															
		    							}
		    						});
		    						
		    						
		    					}	
		    				}), "", new GetValue<InstitutionProxy>() {
		    			public InstitutionProxy getValue(InstitutionProxy institution) {
		    				return institution;
		    			}
		    		}, null);
		        	
		        	table.addColumnStyleName(1, "iconColumn");
		        	table.addColumnStyleName(2, "iconColumn");
		        }
			}
		}
	}
		table.addLastColumn();
}
    private void addMouseDownHandler() {
		table.addHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				Log.info("mouse down");

				x = event.getClientX();
				y = event.getClientY();

				/*if(table.getRowCount()>0)
				{
				Log.info(table.getRowElement(0).getAbsoluteTop() + "--"+ event.getClientY());

				
				if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT&& event.getClientY() < table.getRowElement(0).getAbsoluteTop()) {
					
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();

					Log.info("right event");
				}
				}
				else
				{
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();
					
				}*/

			}
		}, MouseDownEvent.getType());
		
		table.getPopup().addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				table.getPopup().hide();
				addColumnOnMouseout();
				
			}
		}, MouseOutEvent.getType());
	}
    
    public void addColumnSortHandler(){
    	
		table.addColumnSortHandler(new ColumnSortEvent.Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
	
				Column<InstitutionProxy, String> col = (Column<InstitutionProxy, String>) event.getColumn();
				
				
				int index = table.getColumnIndex(col);
				
				Log.info("call for sort " + path.size() + "--index--" + index+ "cc=" + table.getColumnCount());
				if (index == (table.getColumnCount() - 1)) {
					
					table.getPopup().setPopupPosition(x-10, y);
					table.getPopup().show();
	
				} else {
					
					if(table.getRowCount() > 0){
						Log.info("call for sort " + path.size() + "--index--"+ index);
						sortname = path.get(index);
						Log.info("sort column name is " + sortname);
						sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
						Log.info("Call Init Search from addColumnSortHandler");
						delegate.columnClickedForSorting(sortname,sortorder,searchBox.getValue());
					}
				}
			}
		});
	}
    
	@Override
	public CellTable<InstitutionProxy> getTable() {
		return table;
	}
	
	@Override
	public String[] getPaths() {
        return paths.toArray(new String[paths.size()]);
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	  /**
	   * Add a column with a header.
	   *
	   * @param <C> the cell type
	   * @param cell the cell used to render the column
	   * @param headerText the header string
	   * @param getter the value getter for the cell
	   */
	
	
	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<InstitutionProxy, C> fieldUpdater) {
		Column<InstitutionProxy, C> column = new Column<InstitutionProxy, C>(cell) {
			@Override
			public C getValue(InstitutionProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		table.addColumn(column);
	}
	
	 /* private <C> void addColumn(Cell<C> cell, String headerText,
	      final GetValue<C> getter, FieldUpdater<InstitutionProxy, C> fieldUpdater) {
	    Column<InstitutionProxy, C> column = new Column<InstitutionProxy, C>(cell) {
	      @Override
	      public C getValue(InstitutionProxy object) {
	        return getter.getValue(object);
	      }
	    };
	    column.setFieldUpdater(fieldUpdater);
	    if (cell instanceof AbstractEditableCell<?, ?>) {
	      editableCells.add((AbstractEditableCell<?, ?>) cell);
	    }
	    table.addColumn(column, headerText);
	  }*/

	  /**
	   * Get a cell value from a record.
	   *
	   * @param <C> the cell type
	   */
	  private static interface GetValue<C> {
	    C getValue(InstitutionProxy contact);
	  }
	  
	  private List<AbstractEditableCell<?, ?>> editableCells;

	@Override
	public SimplePanel getDetailsPanel() {
		
		return slidingPanel;
	}


	@Override
	public void onRecordChange(RecordChangeEvent event) {
		int pagesize = 0;

		if (event.getRecordValue() == "ALL") {
			pagesize = table.getRowCount();
			McAppConstant.TABLE_PAGE_SIZE = pagesize;
		} else if (event.getRecordValue().matches("\\d+")) {
			pagesize = Integer.parseInt(event.getRecordValue());
		}

		table.setPageSize(pagesize);
	}

	private List<String> getPath(){
		return new ArrayList<String>(this.paths);
	}
	
	private List<String> getColumnNameorder() {
		return columnNameorder;
	}
}
