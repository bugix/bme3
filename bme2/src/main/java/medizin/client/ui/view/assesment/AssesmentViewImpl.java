package medizin.client.ui.view.assesment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.events.RecordChangeEvent;
import medizin.client.events.RecordChangeHandler;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.client.DateTimeFormatRenderer;
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
import com.google.gwt.user.client.ui.Widget;

public class AssesmentViewImpl extends Composite implements AssesmentView, RecordChangeHandler  {

	private static AssesmentViewImplUiBinder uiBinder = GWT.create(AssesmentViewImplUiBinder.class);
	
	interface AssesmentViewImplUiBinder extends UiBinder<Widget, AssesmentViewImpl> {
	}
	
	public BmeConstants constants = GWT.create(BmeConstants.class);
	
	@UiField(provided=true)
	SplitLayoutPanel splitLayoutPanel;

	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	ScrollPanel scrollDetailPanel;
	
	private Map<String, String> columnName=new HashMap<String, String>();
	private List<String> columnNameorder = new ArrayList<String>();
	private List<String> path = new ArrayList<String>();
	private String columnHeader;
	public int x;
	public int y;
	private Sorting sortorder = Sorting.ASC;
	private String sortname = "name";
	
	public AssesmentViewImpl() {
		
		CellTable.Resources tableResources = GWT
				.create(MyCellTableResources.class);
		table = new AdvanceCellTable<AssesmentProxy>(McAppConstant.TABLE_PAGE_SIZE,
				tableResources);

		MySimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources,
				true, McAppConstant.TABLE_JUMP_SIZE, true);

		splitLayoutPanel =new SplitLayoutPanel(){
            @Override
            public void onResize() {
               super.onResize();
               	Double newWidth =splitLayoutPanel.getWidgetSize(scrollPanel);
               	Cookies.setCookie(McAppConstant.ASSESMENT_VIEW_WIDTH, String.valueOf(newWidth));
            }
        };  
        
		initWidget(uiBinder.createAndBindUi(this));
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		newButton.setText(constants.createNewTest());
		init();

		//setting widget width from cookie.
        setWidgetWidth();
	}

	private void setWidgetWidth() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.ASSESMENT_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	splitLayoutPanel.setWidgetSize(scrollPanel,Double.valueOf(widgetWidthFromCookie));	
        }
	}

	@UiField
	IconButton newButton;
	
	@UiHandler(value = { "newButton" })
	public void newButtonClicked(ClickEvent e){
		delegate.newClicked();
	}


    @UiField(provided = true)
    AdvanceCellTable<AssesmentProxy> table;
	
    
    @UiField(provided = true)
	public MySimplePager pager;
    
	protected Set<String> paths = new HashSet<String>();

	private Delegate delegate;
	

		
		   public void init() {
//		        paths.add("id");
//		        table.addColumn(new TextColumn<AssesmentProxy>() {
//
//		            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {
//
//		                public String render(java.lang.Long obj) {
//		                    return obj == null ? "" : String.valueOf(obj);
//		                }
//		            };
//
//		            @Override
//		            public String getValue(AssesmentProxy object) {
//		                return renderer.render(object.getId());
//		            }
//		        }, "Id");
//		        paths.add("version");
//		        table.addColumn(new TextColumn<AssesmentProxy>() {
//
//		            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//
//		                public String render(java.lang.Integer obj) {
//		                    return obj == null ? "" : String.valueOf(obj);
//		                }
//		            };
//
//		            @Override
//		            public String getValue(AssesmentProxy object) {
//		                return renderer.render(object.getVersion());
//		            }
//		        }, "Version");
			   columnName.put(constants.name(), "name");
			   columnNameorder.add(constants.name());
			   paths.add("name");
			   table.addColumn(new Column<AssesmentProxy, AssesmentProxy>(new AssesmentTextCell()) {
		    	      @Override
		    	      public AssesmentProxy getValue(AssesmentProxy object) {
		    	        return object;
		    	      }
		    	    }, constants.name(),true);
			   
			   
		      
//		        table.addColumn(new TextColumn<AssesmentProxy>() {
//
//		            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//		                public String render(java.lang.String obj) {
//		                    return obj == null ? "" : String.valueOf(obj);
//		                }
//		            };
//
//		            @Override
//		            public String getValue(AssesmentProxy object) {
//		                return renderer.render(object.getName());
//		            }
//		        }, "Name");
			   columnName.put("Prüfung am", "dateOfAssesment");
			   columnNameorder.add("Prüfung am");
		        paths.add("dateOfAssesment");
		        table.addColumn(new TextColumn<AssesmentProxy>() {

		            Renderer<java.util.Date> renderer = new DateTimeFormatRenderer(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));

		            @Override
		            public String getValue(AssesmentProxy object) {
		                return renderer.render(object == null ? null : object.getDateOfAssesment());
		            }
		        }, "Prüfung am",true);
//		        paths.add("dateOpen");
//		        table.addColumn(new TextColumn<AssesmentProxy>() {
//
//		            Renderer<java.util.Date> renderer = new DateTimeFormatRenderer(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));
//
//		            @Override
//		            public String getValue(AssesmentProxy object) {
//		                return renderer.render(object.getDateOpen());
//		            }
//		        }, "Date Open");
//		        paths.add("dateClosed");
//		        table.addColumn(new TextColumn<AssesmentProxy>() {
//
//		            Renderer<java.util.Date> renderer = new DateTimeFormatRenderer(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));
//
//		            @Override
//		            public String getValue(AssesmentProxy object) {
//		                return renderer.render(object.getDateClosed());
//		            }
//		        }, "Date Closed");
//		        paths.add("place");
//		        table.addColumn(new TextColumn<AssesmentProxy>() {
//
//		            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//		                public String render(java.lang.String obj) {
//		                    return obj == null ? "" : String.valueOf(obj);
//		                }
//		            };
//
//		            @Override
//		            public String getValue(AssesmentProxy object) {
//		                return renderer.render(object.getPlace());
//		            }
//		        }, "Place");
//		        paths.add("logo");
//		        table.addColumn(new TextColumn<AssesmentProxy>() {
//
//		            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//		                public String render(java.lang.String obj) {
//		                    return obj == null ? "" : String.valueOf(obj);
//		                }
//		            };
//
//		            @Override
//		            public String getValue(AssesmentProxy object) {
//		                return renderer.render(object.getLogo());
//		            }
//		        }, "Logo");
//		        paths.add("isClosed");
//		        table.addColumn(new TextColumn<AssesmentProxy>() {
//
//		            Renderer<java.lang.Boolean> renderer = new AbstractRenderer<java.lang.Boolean>() {
//
//		                public String render(java.lang.Boolean obj) {
//		                    return obj == null ? "" : String.valueOf(obj);
//		                }
//		            };
//
//		            @Override
//		            public String getValue(AssesmentProxy object) {
//		                return renderer.render(object.getIsClosed());
//		            }
//		        }, "Is Closed");
//		        paths.add("assesmentVersion");
//		        table.addColumn(new TextColumn<AssesmentProxy>() {
//
//		            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//
//		                public String render(java.lang.Integer obj) {
//		                    return obj == null ? "" : String.valueOf(obj);
//		                }
//		            };
//
//		            @Override
//		            public String getValue(AssesmentProxy object) {
//		                return renderer.render(object.getAssesmentVersion());
//		            }
//		        }, "Assesment Version");
		        columnName.put("Mc", "mc");
				columnNameorder.add("Mc");
		        paths.add("mc");
		        table.addColumn(new TextColumn<AssesmentProxy>() {

		            Renderer<medizin.client.proxy.McProxy> renderer = medizin.client.ui.view.roo.McProxyRenderer.instance();

		            @Override
		            public String getValue(AssesmentProxy object) {
		                return renderer.render(object == null ? null : object.getMc());
		            }
		        }, "Mc",true);
		        
		        columnName.put("Repe", "repeFor");
				columnNameorder.add("Repe");
		        paths.add("repeFor");
		        table.addColumn(new TextColumn<AssesmentProxy>() {

		            Renderer<medizin.client.proxy.AssesmentProxy> renderer = medizin.client.ui.view.roo.AssesmentProxyRenderer.instance();

		            @Override
		            public String getValue(AssesmentProxy object) {
		                return object == null ? null : object.getRepeFor() == null? "" : "ja";
		            }
		        }, "Repe",true);
//		        paths.add("percentSameQuestion");
//		        table.addColumn(new TextColumn<AssesmentProxy>() {
//
//		            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//
//		                public String render(java.lang.Integer obj) {
//		                    return obj == null ? "" : String.valueOf(obj);
//		                }
//		            };
//
//		            @Override
//		            public String getValue(AssesmentProxy object) {
//		                return renderer.render(object.getPercentSameQuestion());
//		            }
//		        }, "Percent Same Question");
		        
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
						

					if(columnHeader==constants.name()){
						table.addColumn(new Column<AssesmentProxy, AssesmentProxy>(new AssesmentTextCell()) {
							{
								this.setSortable(true);
							}
				    	      @Override
				    	      public AssesmentProxy getValue(AssesmentProxy object) {
				    	        return object;
				    	      }
				    	    }, constants.name(),false);
					}else if(columnHeader=="Prüfung am"){
						
						table.addColumn(new TextColumn<AssesmentProxy>() {

							{
								this.setSortable(true);
							}
							
				            Renderer<java.util.Date> renderer = new DateTimeFormatRenderer(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));

				            @Override
				            public String getValue(AssesmentProxy object) {
				                return renderer.render(object == null ? null : object.getDateOfAssesment());
				            }
				        }, "Prüfung am",false);
					}else if(columnHeader=="Repe"){
							table.addColumn(new TextColumn<AssesmentProxy>() {
	
								{
									this.setSortable(true);
								}
								
					            Renderer<medizin.client.proxy.AssesmentProxy> renderer = medizin.client.ui.view.roo.AssesmentProxyRenderer.instance();
	
					            @Override
					            public String getValue(AssesmentProxy object) {
					                return object == null ? null : object.getRepeFor() == null? "" : "ja";
					            }
					        }, "Repe",false);
					}
						else if(columnHeader=="Mc"){
							table.addColumn(new TextColumn<AssesmentProxy>() {

								{
									this.setSortable(true);
								}
					            Renderer<medizin.client.proxy.McProxy> renderer = medizin.client.ui.view.roo.McProxyRenderer.instance();

					            @Override
					            public String getValue(AssesmentProxy object) {
					                return renderer.render(object == null ? null : object.getMc());
					            }
					        }, "Mc",false);
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
			
						Column<AssesmentProxy, String> col = (Column<AssesmentProxy, String>) event.getColumn();
						
						
						int index = table.getColumnIndex(col);
						
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
		public CellTable<AssesmentProxy> getTable() {

			return table;
		}
		@Override
		public String[] getPaths() {

	        return paths.toArray(new String[paths.size()]);
		
	}


		@Override
		public void setDelegate(Delegate delegate) {
			this.delegate= delegate;
			
			
		}


		@UiField
		SimplePanel detailsPanel;
		@Override
		public SimplePanel getDetailsPanel() {

			return detailsPanel;
		}
		
		  private static class AssesmentTextCell extends AbstractCell<AssesmentProxy> {


				@Override
				public void render(com.google.gwt.cell.client.Cell.Context context,
						AssesmentProxy value, SafeHtmlBuilder sb) {
				      // Always do a null check on the value. Cell widgets can pass null to cells
				      // if the underlying data contains a null, or if the data arrives out of order.
				      if (value == null) {
				        return;
				      }
				      String beginn = "<div style=\"";
				      String end = "</div>";
				      if(value.getIsClosed())
				      {
				    	  beginn += "color:grey; ";		    	  
				      }
				      else if(value.getDateOpen().before(new Date()) && value.getDateClosed().after(new Date())){
				    	  beginn += "color:green; ";			    	  
				      }
				      if(!value.getDateOfAssesment().after(new Date())){
				    	 
				    	  beginn += "text-decoration: line-through; ";		    	  
				      }
				      
	 
				      beginn += "\">";
				      sb.appendHtmlConstant(beginn);
				      sb.appendHtmlConstant(value.getName());
				      sb.appendHtmlConstant(end);
					
				}
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
		
		@Override
		public ScrollPanel getScrollDetailPanel() {
			return scrollDetailPanel;
		}
}
