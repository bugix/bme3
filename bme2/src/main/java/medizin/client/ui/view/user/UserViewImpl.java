package medizin.client.ui.view.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.events.RecordChangeEvent;
import medizin.client.events.RecordChangeHandler;
import medizin.client.proxy.PersonProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.QuickSearchBox;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserViewImpl extends Composite implements UserView, RecordChangeHandler  {

	private static UserViewImplUiBinder uiBinder = GWT
			.create(UserViewImplUiBinder.class);

	interface UserViewImplUiBinder extends
			UiBinder<Widget, UserViewImpl> {
	}
	
	@UiField(provided=true)
	SplitLayoutPanel splitLayoutPanel;

	@UiField (provided = true)
	QuickSearchBox searchBox;
	
	@UiField
	HTMLPanel mainPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	private Presenter presenter;

	Map<String, String> columnName=new HashMap<String, String>();
	List<String> columnNameorder = new ArrayList<String>();
	public List<String> path = new ArrayList<String>();
	public String columnHeader;
	private int x;
	private int y;
	public Sorting sortorder = Sorting.ASC;
	public String sortname = "name";
	
	public UserViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new AdvanceCellTable<PersonProxy>(McAppConstant.TABLE_PAGE_SIZE,tableResources);

		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources,true, McAppConstant.TABLE_JUMP_SIZE, true);

		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				table.setVisibleRange(0, McAppConstant.TABLE_PAGE_SIZE);
				delegate.performSearch(searchBox.getValue());
			}
		});
		
		splitLayoutPanel =new SplitLayoutPanel(){
            @Override
            public void onResize() {
               super.onResize();
               	Double newWidth =splitLayoutPanel.getWidgetSize(scrollPanel);
               	Cookies.setCookie(McAppConstant.USER_VIEW_WIDTH, String.valueOf(newWidth));
            }
        };  
        
		initWidget(uiBinder.createAndBindUi(this));
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		init();
		//setting widget width from cookie.
        setWidgetWidth();
		
	}

	private void setWidgetWidth() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.USER_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	splitLayoutPanel.setWidgetSize(scrollPanel,Double.valueOf(widgetWidthFromCookie));	
        }
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}
	
	@UiField(provided = true)
	AdvanceCellTable<PersonProxy> table;

	@UiField(provided = true)
	public MySimplePager pager;

	
	  /* @UiField
	    CellTable<PersonProxy> table;*/
	  
	   @UiField
	   IconButton addUser;
	   
	   public BmeConstants constants = GWT.create(BmeConstants.class);
	   
	   @UiHandler ("addUser")
	   public void onNewClicked(ClickEvent e) {
        delegate.newClicked();
    }

	    protected List<String> paths = new ArrayList<String>();
		private Delegate delegate;

	    public void init() {
//	        paths.add("id");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {
//
//	                public String render(java.lang.Long obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getId());
//	            }
//	        }, "Id");
//	        paths.add("version");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//
//	                public String render(java.lang.Integer obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getVersion());
//	            }
//	        }, "Version");
	    	columnName.put(constants.name(), "name");
			columnNameorder.add(constants.name());
	        paths.add("name");
	        table.addColumn(new TextColumn<PersonProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(PersonProxy object) {
	                return renderer.render(object == null ? null : object.getName());
	            }
	        }, constants.name(),true);

	        
	        columnName.put(constants.prename(), "prename");
			columnNameorder.add(constants.prename());
	        paths.add("prename");
	        table.addColumn(new TextColumn<PersonProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(PersonProxy object) {
	                return renderer.render(object == null ? null : object.getPrename());
	            }
	        }, constants.prename(),true);
	        
	        columnName.put(constants.email(), "email");
			columnNameorder.add(constants.email());
	        paths.add("email");
	        table.addColumn(new TextColumn<PersonProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(PersonProxy object) {
	                return renderer.render(object == null ? null : object.getEmail());
	            }
	        }, constants.email(),true);
	        
	        columnName.put(constants.alternativeEmail(), "alternativEmail");
			columnNameorder.add(constants.alternativeEmail());
	        paths.add("alternativEmail");
	        table.addColumn(new TextColumn<PersonProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(PersonProxy object) {
	                return renderer.render(object.getAlternativEmail());
	            }
	        }, constants.alternativeEmail(),false);
	        
	        columnName.put(constants.phoneNo(), "phoneNumber");
			columnNameorder.add(constants.phoneNo());
	        paths.add("phoneNumber");
	        table.addColumn(new TextColumn<PersonProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(PersonProxy object) {
	                return renderer.render(object.getPhoneNumber());
	            }
	        }, constants.phoneNo(),false);
	        // Adding to show column filer popup
	        //table.addLastColumn();
	        
//	        paths.add("isAdmin");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.lang.Boolean> renderer = new AbstractRenderer<java.lang.Boolean>() {
//
//	                public String render(java.lang.Boolean obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getIsAdmin());
//	            }
//	        }, "Is Admin");
//	        paths.add("isAccepted");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.lang.Boolean> renderer = new AbstractRenderer<java.lang.Boolean>() {
//
//	                public String render(java.lang.Boolean obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getIsAccepted());
//	            }
//	        }, "Is Accepted");
//	        paths.add("questionAccesses");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.util.Set> renderer = medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.QuestionAccessProxyRenderer.instance());
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getQuestionAccesses());
//	            }
//	        }, "Question Accesses");
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

			Iterator<String> i1=getColumnSortSet().iterator();

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
					
					
				

				table.addColumn(new TextColumn<PersonProxy>() {

					{
						this.setSortable(true);
					}

					Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

						public String render(java.lang.String obj) {
							return obj == null ? "" : String.valueOf(obj);
						}
					};

					String tempColumnHeader = columnHeader;

					@Override
					public String getValue(PersonProxy object) {

						if(object!=null){
	
							if (tempColumnHeader == constants.name()) {
								
								return renderer.render(object.getName()!=null?object.getName():"");
							} else if (tempColumnHeader == constants.prename()) {
								
								return renderer.render(object.getPrename()!=null?object.getPrename():"");
							} else if (tempColumnHeader == constants.email()) {
								
								return renderer.render(object.getEmail()!=null?object.getEmail():"");
							} else if (tempColumnHeader == constants.alternativeEmail()) {
								
								return renderer.render(object.getAlternativEmail()!=null?object.getAlternativEmail():"");
							} else if (tempColumnHeader == constants.phoneNo()) {
								
								return renderer.render(object.getPhoneNumber()!=null?object.getPhoneNumber():"");
							} 
							else {
								return "";
							}
							}else{
							return "";
						}
					}
				}, columnHeader, false);
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
						
						table.getPopup().setPopupPosition(x-10, y);
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
		
					Column<PersonProxy, String> col = (Column<PersonProxy, String>) event.getColumn();
					
					
					final int index = table.getColumnIndex(col);
					
					Log.info("call for sort " + path.size() + "--index--" + index+ "cc=" + table.getColumnCount());
					if (index == (table.getColumnCount() - 1)) {
						table.getPopup().setPopupPosition(x-10, y);
						table.getPopup().show();
					} else {
							if(table.getRowCount() > 0 ){
								Log.info("call for sort " + path.size() + "--index--"+ index);
								sortname = path.get(index);
								Log.info("sort column name is " + sortname);
								sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
								Log.info("Call Init Search from addColumnSortHandler");
								delegate.columnClickedForSorting(sortname,sortorder);
						}
					}
				}
			});
		}
		@Override
		public AdvanceCellTable<PersonProxy> getTable() {
			
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
		@UiField
		SimplePanel slidingPanel;
		@Override
		public SimplePanel getDetailsPanel() {
			
			return slidingPanel;
		}


		@Override
		public SplitLayoutPanel getSplitLayoutPanel() {
			
			return splitLayoutPanel;
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

		public Map<String, String> getColumnName() {
			return this.columnName;
		}

	

		@Override
		public List<String> getColumnSortSet() {
			return this.columnNameorder;
		}

		public List<String> getPath(){
			return this.paths;
		}
}
