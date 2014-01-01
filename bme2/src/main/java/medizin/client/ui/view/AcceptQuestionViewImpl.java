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
import medizin.client.proxy.QuestionProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.roo.McProxyRenderer;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class AcceptQuestionViewImpl extends Composite implements AcceptQuestionView, RecordChangeHandler,AcceptAnswerView.Delegate  {

	private static AcceptQuestionViewImplUiBinder uiBinder = GWT
			.create(AcceptQuestionViewImplUiBinder.class);

	interface AcceptQuestionViewImplUiBinder extends
			UiBinder<Widget, AcceptQuestionViewImpl> {
	}

	private Presenter presenter;
	private Map<String, String> columnName=new HashMap<String, String>();
	private List<String> columnNameorder = new ArrayList<String>();
	private List<String> path = new ArrayList<String>();
	private String columnHeader;
	public int x;
	public int y;
	private Sorting sortorder = Sorting.ASC;
	private String sortname = "name";
	private Delegate delegate;

	public AcceptQuestionViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new AdvanceCellTable<QuestionProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);
		
		splitLayoutPanel =new SplitLayoutPanel(){
            @Override
            public void onResize() {
               super.onResize();
               	Double newWidth =splitLayoutPanel.getWidgetSize(scrollPanel);
               	Cookies.setCookie(McAppConstant.ACCEPT_QUESTION_VIEW_WIDTH, String.valueOf(newWidth));
            }
        };  
        
		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources, true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		DOM.setElementAttribute(this.getElement(), "style", "position: absolute; left: 5px; top: 0px; right: 0px; bottom: 0px; overflow: auto;");
		
		init();
		//setting widget width from cookie.
        setWidgetWidth();
		
	}

	private void setWidgetWidth() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.ACCEPT_QUESTION_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	splitLayoutPanel.setWidgetSize(scrollPanel,Double.valueOf(widgetWidthFromCookie));	
        }
	}
	BmeConstants constants = GWT.create(BmeConstants.class);

	@UiField(provided=true)
	SplitLayoutPanel splitLayoutPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	SimplePanel detailsPanel;
	
	public SimplePanel getDetailsPanel() {
		return detailsPanel;
	}
	
	public void setDetailsPanel(SimplePanel detailsPanel) {
		this.detailsPanel = detailsPanel;
	}

	private void init() {
		columnName.put(constants.id(), "id");
		columnNameorder.add(constants.id());
	    paths.add("id");
	        table.addColumn(new TextColumn<QuestionProxy>() {

	            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {

	                public String render(java.lang.Long obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(QuestionProxy object) {
	                return renderer.render(object == null ? null : object.getId());
	            }
	        }, constants.id(),true);
	       
		     	
	       columnName.put(constants.question(), "keywords");
		   columnNameorder.add(constants.question()); 	
	        paths.add("keywords");
	        paths.add("comment");
	        paths.add("previosVersion");
	     	table.addColumn(new Column<QuestionProxy, QuestionProxy>(new QuestionTextCell()) {
	    	      @Override
	    	      public QuestionProxy getValue(QuestionProxy object) {
	    	        return object;
	    	      }
	    	    },  constants.question(),true);

	     	columnName.put(constants.acceptQuestion(), "rewiewer");
			columnNameorder.add(constants.acceptQuestion()); 
	     	paths.add("rewiewer");
	     	paths.add("autor");
	     	paths.add("questEvent");
	     	paths.add("mcs");
	     	paths.add("questionType");
	     	table.addColumn(new Column<QuestionProxy, QuestionProxy>(new SimpleAttributeCell()) {
	    	      @Override
	    	      public QuestionProxy getValue(QuestionProxy object) {
	    	        return object;
	    	      }
	    	},  constants.acceptQuestion(),true);
		     	

		     	
		
		    	/*addColumn(new ActionCell<QuestionProxy>( McAppConstant.DECLINE_ICON, new ActionCell.Delegate<QuestionProxy>() {
		    	            public void execute(QuestionProxy questionProxy) {
		    	            	DeclineEmailPopup popup = new DeclineEmailPopup(questionProxy);
		    	            	popup.setDelegate(delegate);
//		    	            	Log.debug("hinterPupup");
		    	             // delegate.rejectClicked(questionProxy, "verweigert");
		    	            }
		    	          }), "Ablehnen", new GetValue<QuestionProxy>() {
		    	        public QuestionProxy getValue(QuestionProxy questionProxy) {
		    	          return questionProxy;
		    	        }
		    	      }, null);
		    	
		    	addColumn(new ActionCell<QuestionProxy>( McAppConstant.ACCEPT_ICON, new ActionCell.Delegate<QuestionProxy>() {
    	            public void execute(QuestionProxy questionProxy) {
    	              delegate.acceptClicked(questionProxy);
    	            }
    	          }), "Annehmen", new GetValue<QuestionProxy>() {
    	        public QuestionProxy getValue(QuestionProxy questionProxy) {
    	          return questionProxy;
    	        }
    	      }, null);*/
		    	
		    	table.addColumnStyleName(0, "iconColumn");
		    	table.addColumnStyleName(1, "questionTextColumnAccept");
		    	table.addColumnStyleName(3, "deleteColumn");	
		    	table.addColumnStyleName(4, "deleteColumn");
		    	table.getRowContainer().setClassName("tableBody");
		    	
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
				

			if(columnHeader==constants.id()){
				
				 table.addColumn(new TextColumn<QuestionProxy>() {

					 	{
							this.setSortable(true);
						}
			            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {

			                public String render(java.lang.Long obj) {
			                    return obj == null ? "" : String.valueOf(obj);
			                }
			            };

			            @Override
			            public String getValue(QuestionProxy object) {
			                return renderer.render(object == null ? null : object.getId());
			            }
			        }, constants.id(),false);
				
			}else if(columnHeader==constants.question()) {
				
				table.addColumn(new Column<QuestionProxy, QuestionProxy>(new QuestionTextCell()) {
		    	      @Override
		    	      public QuestionProxy getValue(QuestionProxy object) {
		    	        return object;
		    	      }
		    	    },  constants.question(),true);
		  }
			else if(columnHeader==constants.acceptQuestion()){
				
				table.addColumn(new Column<QuestionProxy, QuestionProxy>(new SimpleAttributeCell()) {
		    	      @Override
		    	      public QuestionProxy getValue(QuestionProxy object) {
		    	        return object;
		    	      }
		    	},  constants.acceptQuestion(),true);
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
		
					Column<QuestionProxy, String> col = (Column<QuestionProxy, String>) event.getColumn();
					
					
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
								delegate.columnClickedForSorting(sortname,sortorder);
						}
					}
				}
			});
		}


	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}
	
	@Override
	public CellTable<QuestionProxy> getTable() {

		return table;
	}
	@Override
	public String[] getPaths() {

        return paths.toArray(new String[paths.size()]);
	
}

	public List<String> getPath(){
		return new ArrayList<String>(this.paths);
	}
	

	  
	  private static class SimpleAttributeCell extends AbstractCell<QuestionProxy> {

		  BmeConstants constants = GWT.create(BmeConstants.class);


			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					QuestionProxy value, SafeHtmlBuilder sb) {
			      // Always do a null check on the value. Cell widgets can pass null to cells
			      // if the underlying data contains a null, or if the data arrives out of order.
			      if (value == null) {
			        return;
			      }
			      sb.appendHtmlConstant("<table>");
			      
			      if(value.getRewiewer()!=null){
			    	  sb.appendHtmlConstant(getTableRow(constants.reviewer(), value.getRewiewer().getPrename() + " " + value.getRewiewer().getName()));
			      }
			      if(value.getAutor()!=null){
			    	  sb.appendHtmlConstant(getTableRow(constants.auther(),value.getAutor().getPrename() + " " + value.getAutor().getName()));
			      }
			      if(value.getQuestEvent()!=null){
			    	  sb.appendHtmlConstant(getTableRow(constants.questionEvent(),value.getQuestEvent().getEventName()));
			      }
			      if(value.getMcs()!=null){
			    	  sb.appendHtmlConstant(getTableRow(constants.mcs(), medizin.client.ui.view.roo.CollectionRenderer.of(McProxyRenderer.instance()).render(value.getMcs())));
			      }
			      if( value.getQuestionType()!=null){
			    	  sb.appendHtmlConstant(getTableRow(constants.questionType(), value.getQuestionType().getShortName()));
			      }
			      sb.appendHtmlConstant("</table>");
			}

			private String getTableRow(String title, String value) {
				
				return "<tr style=\"vertical-align: top;\"><td><strong>"+ title +"&nbsp;</strong></td><td>" + value + "</td></tr>";
			}
		  }
    @UiField(provided = true)
    AdvanceCellTable<QuestionProxy> table;
	
    @UiField(provided = true)
	public MySimplePager pager;
    
	protected Set<String> paths = new HashSet<String>();

	/*private DeclineEmailPopupDelagate delegate;*/

	
	
	  /**
	   * Add a column with a header.
	   *
	   * @param <C> the cell type
	   * @param cell the cell used to render the column
	   * @param headerText the header string
	   * @param getter the value getter for the cell
	   */
	  private <C> void addColumn(Cell<C> cell, String headerText,
	      final GetValue<C> getter, FieldUpdater<QuestionProxy, C> fieldUpdater) {
	    Column<QuestionProxy, C> column = new Column<QuestionProxy, C>(cell) {
	      @Override
	      public C getValue(QuestionProxy object) {
	        return getter.getValue(object);
	      }
	    };
	    column.setFieldUpdater(fieldUpdater);
	  
	    table.addColumn(column, headerText);
	  }
	  
	  /**
	   * Get a cell value from a record.
	   *
	   * @param <C> the cell type
	   */
	  private static interface GetValue<C> {
	    C getValue(QuestionProxy contact);
	  }
	  
	  
	  private static class QuestionTextCell extends AbstractCell<QuestionProxy> {
		  
		  BmeConstants constants = GWT.create(BmeConstants.class);

			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					QuestionProxy value, SafeHtmlBuilder sb) {
			      // Always do a null check on the value. Cell widgets can pass null to cells
			      // if the underlying data contains a null, or if the data arrives out of order.
			      if (value == null) {
			        return;
			      }
			      sb.appendHtmlConstant("<table>");
			      
			      if(value.getQuestionText() != null){
			    	  sb.appendHtmlConstant(getQueTextTableRow(constants.questionText(), value.getQuestionText()));
			    	 // sb.appendHtmlConstant("<tr><td><strong>Reviewer: </strong></td><td>" + value.getRewiewer().getPrename() + " " + value.getRewiewer().getName() + " " + "</td></tr>");
			    	  
			      }
			      if(value.getPreviousVersion() != null){
			    	  sb.appendHtmlConstant(getTableRow("Text der vorgeriegen Version:", value.getPreviousVersion().getQuestionText()));
			    	 // sb.appendHtmlConstant("<tr><td><strong>Autor: </strong></td><td>" + value.getAutor().getPrename() + " " + value.getAutor().getName() + " " + "</td></tr>");
			      }
			      if(value.getComment() != null){
			    	  sb.appendHtmlConstant(getTableRow(constants.comment(), value.getComment()));
			    	 // sb.appendHtmlConstant("<tr><td><strong>Autor: </strong></td><td>" + value.getAutor().getPrename() + " " + value.getAutor().getName() + " " + "</td></tr>");
			      }
			      if(value.getKeywords() != null){
			    	  sb.appendHtmlConstant(getTableRow(constants.keyword(), medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.KeywordProxyRenderer.instance()).render(value.getKeywords())));
			    	 // sb.appendHtmlConstant("<tr><td><strong>Autor: </strong></td><td>" + value.getAutor().getPrename() + " " + value.getAutor().getName() + " " + "</td></tr>");
			      }
			      
			      sb.appendHtmlConstant("</table>");
				
			}
			
			private String getTableRow(String title, String value) {
				
				return "<tr style=\"vertical-align: top;\"><td><strong>"+ title +"&nbsp;</strong></td></tr><tr><td>" + value + "</td></tr>";
			}
			
			private String getQueTextTableRow(String title, String value) {
				String text = new HTML(value).getText();
				return "<tr style=\"vertical-align: top;\"><td><strong>"+ title +"&nbsp;</strong></td></tr><tr><td><div title='"+ text +"'>" + getFirstSomeChar(text) + "</div></td></tr>";
			}
			
			private String getFirstSomeChar(String text) {
				if(text.length() > 100) {
					return text.substring(0,100) + "...";
				}
				return text;
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

	public List<String> getColumnNameorder() {
		return columnNameorder;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
		
	}
	  
	



}
