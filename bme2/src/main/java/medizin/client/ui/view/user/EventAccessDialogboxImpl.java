package medizin.client.ui.view.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.user.EventAccessDialogbox.Delegate;
import medizin.client.ui.view.user.EventAccessDialogbox.Presenter;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;


import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EventAccessDialogboxImpl extends DialogBox implements EventAccessDialogbox {

	private static EventAccessDialogboxImplUiBinder uiBinder = GWT
			.create(EventAccessDialogboxImplUiBinder.class);

	interface EventAccessDialogboxImplUiBinder extends
			UiBinder<Widget, EventAccessDialogboxImpl> {
	}

	private Presenter presenter;
	
	@UiField
	Button closeButton;
	
	@UiHandler ("closeButton")
	public void onCloseButtonClick(ClickEvent event) {
            hide();
            
          }

	public BmeConstants constants = GWT.create(BmeConstants.class);
	
	public EventAccessDialogboxImpl() {
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		tableEvent = new CellTable<QuestionEventProxy>(McAppConstant.TABLE_PAGE_SIZE,tableResources);

		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		setWidget(uiBinder.createAndBindUi(this));
	    setGlassEnabled(true);
	    setAnimationEnabled(true);
	    setTitle(constants.eventAccess());
	    setText(constants.eventAccess());
	    
	    init();
	    
	}

	  private List<AbstractEditableCell<?, ?>> editableCells;
	  
	 protected Set<String> paths = new HashSet<String>();

	    public void init() {
	    	
	    	
	    	
	    	editableCells = new ArrayList<AbstractEditableCell<?, ?>>();

//	        paths.add("id");
//	        tableEvent.addColumn(new TextColumn<QuestionEventProxy>() {
	//
//	            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {
	//
//	                public String render(java.lang.Long obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
	//
//	            @Override
//	            public String getValue(QuestionEventProxy object) {
//	                return renderer.render(object.getId());
//	            }
//	        }, "Id");
//	        paths.add("version");
//	        tableEvent.addColumn(new TextColumn<QuestionEventProxy>() {
	//
//	            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
	//
//	                public String render(java.lang.Integer obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
	//
//	            @Override
//	            public String getValue(QuestionEventProxy object) {
//	                return renderer.render(object.getVersion());
//	            }
//	        }, "Version");
//	        paths.add("eventName");
//	        tableEvent.addColumn(new TextColumn<QuestionEventProxy>() {
	//
//	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
	//
//	                public String render(java.lang.String obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
	//
//	            @Override
//	            public String getValue(QuestionEventProxy object) {
//	                return renderer.render(object.getEventName());
//	            }
//	        }, "Event Name");
//	        paths.add("institution");
//	        tableEvent.addColumn(new TextColumn<QuestionEventProxy>() {
	//
//	            Renderer<medizin.client.managed.request.InstitutionProxy> renderer = medizin.client.ui.view.roo.InstitutionProxyRenderer.instance();
	//
//	            @Override
//	            public String getValue(QuestionEventProxy object) {
//	                return renderer.render(object.getInstitution());
//	            }
//	        }, "Institution");
	    	
	

	        paths.add("eventName");
	        tableEvent.addColumn(new TextColumn<QuestionEventProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(QuestionEventProxy object) {
	                return renderer.render(object.getEventName());
	            }
	        }, constants.eventName());
	        
	    	addColumn(new ActionCell<QuestionEventProxy>(
	    	        constants.read(), new ActionCell.Delegate<QuestionEventProxy>() {
	    	            public void execute(QuestionEventProxy questionEvent) {
	    	              Log.debug("You clicked " + questionEvent.getEventName());
	    	              delegate.addClicked(medizin.shared.AccessRights.AccRead, questionEvent);
	    	            }
	    	          }), constants.read(), new GetValue<QuestionEventProxy>() {
	    	        public QuestionEventProxy getValue(QuestionEventProxy contact) {
	    	          return contact;
	    	        }
	    	      }, null);
	    	
	    	addColumn(new ActionCell<QuestionEventProxy>(
	    			constants.write(), new ActionCell.Delegate<QuestionEventProxy>() {
	    	            public void execute(QuestionEventProxy questionEvent) {
	    	              Log.debug("You clicked " + questionEvent.getEventName());
	    	              delegate.addClicked(medizin.shared.AccessRights.AccWrite, questionEvent);
	    	            }
	    	          }), constants.write(), new GetValue<QuestionEventProxy>() {
	    	        public QuestionEventProxy getValue(QuestionEventProxy contact) {
	    	          return contact;
	    	        }
	    	      }, null);
	    	addColumn(new ActionCell<QuestionEventProxy>(
	    			constants.addQuestions(), new ActionCell.Delegate<QuestionEventProxy>() {
	    	            public void execute(QuestionEventProxy questionEvent) {
	    	              Log.debug("You clicked " + questionEvent.getEventName());
	    	              delegate.addClicked(medizin.shared.AccessRights.AccAddQuestions, questionEvent);
	    	            }
	    	          }), constants.addQuestions(), new GetValue<QuestionEventProxy>() {
	    	        public QuestionEventProxy getValue(QuestionEventProxy contact) {
	    	          return contact;
	    	        }
	    	      }, null);
	        
	        /*searchInstitution.addChangeHandler(new ChangeHandler(){

				@Override
				public void onChange(ChangeEvent event) {
					int selectedIndex = searchInstitution.getSelectedIndex();
					if (selectedIndex >= 0){
						delegate.filterInstitutionChanged(searchInstitution.getValue(selectedIndex));
					}
					
					
				}
	        	
	        });*/
	    	
	    	searchInstitution.addHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
					if (searchInstitution.getSelected() != null)
						delegate.filterInstitutionChanged(searchInstitution.getSelected().getId());
					else
						delegate.filterInstitutionChanged(null);
				}
			});
	    	
	        searchEvent.addKeyUpHandler(new KeyUpHandler(){

				@Override
				public void onKeyUp(KeyUpEvent event) {
					delegate.filterEventChanged(searchEvent.getText());
					
				}


				});
	        
	    	tableEvent.addColumnStyleName(0, "questionTextColumn");
	    	tableEvent.addColumnStyleName(1, "accessRightColumn");
	    	tableEvent.addColumnStyleName(2, "accessRightColumn");
	    	tableEvent.addColumnStyleName(3, "accessRightColumn");
	        
	    }
	@Override
	public void setPresenter(Presenter presenter) {
	this.presenter = presenter;
		
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
	      final GetValue<C> getter, FieldUpdater<QuestionEventProxy, C> fieldUpdater) {
	    Column<QuestionEventProxy, C> column = new Column<QuestionEventProxy, C>(cell) {
	      @Override
	      public C getValue(QuestionEventProxy object) {
	        return getter.getValue(object);
	      }
	    };
	    column.setFieldUpdater(fieldUpdater);
	    if (cell instanceof AbstractEditableCell<?, ?>) {
	      editableCells.add((AbstractEditableCell<?, ?>) cell);
	    }
	    tableEvent.addColumn(column, headerText);
	  }

	  /**
	   * Get a cell value from a record.
	   *
	   * @param <C> the cell type
	   */
	  private static interface GetValue<C> {
	    C getValue(QuestionEventProxy contact);
	  }
	
	  
	  @UiField(provided = true)
		CellTable<QuestionEventProxy> tableEvent;

		@UiField(provided = true)
		public SimplePager pager;

		
		
	/*@UiField
	CellTable<QuestionEventProxy> tableEvent;*/
	/*@UiField
	ListBox searchInstitution;*/
		
	@UiField
	DefaultSuggestBox<InstitutionProxy, EventHandlingValueHolderItem<InstitutionProxy>> searchInstitution;
	
	@UiField
    TextBox searchEvent;
	
	
	
	

	private Delegate delegate;

	public DefaultSuggestBox<InstitutionProxy, EventHandlingValueHolderItem<InstitutionProxy>> getSearchInstitution() {
		return searchInstitution;
	}



	@Override
	public CellTable<QuestionEventProxy> getTable() {
		
		return tableEvent;
	}

	@Override
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate= delegate;
		
	}

	@Override
	public void display() {
		center();
		show();
		
	}

}
