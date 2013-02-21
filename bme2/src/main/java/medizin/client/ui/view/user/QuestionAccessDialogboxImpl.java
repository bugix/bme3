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
import medizin.client.proxy.QuestionProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class QuestionAccessDialogboxImpl extends DialogBox implements QuestionAccessDialogbox {

	private static EventAccessDialogboxImplUiBinder uiBinder = GWT
			.create(EventAccessDialogboxImplUiBinder.class);

	interface EventAccessDialogboxImplUiBinder extends
			UiBinder<Widget, QuestionAccessDialogboxImpl> {
	}

	private Presenter presenter;
	
	@UiField
	Button closeButton;
	
	@UiHandler ("closeButton")
	public void onCloseButtonClick(ClickEvent event) {
            hide();
            
          }

	

	public QuestionAccessDialogboxImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		tableEvent = new CellTable<QuestionProxy>(McAppConstant.TABLE_PAGE_SIZE,tableResources);

		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		
		setWidget(uiBinder.createAndBindUi(this));
	    setGlassEnabled(true);
	    setAnimationEnabled(true);
	    setTitle("Zufriff auf Themenblock hinzufügen");
	    setText("Zufriff auf Themenblock hinzufügen");
	    
	    init();
	    
	}

	  private List<AbstractEditableCell<?, ?>> editableCells;
	  
	 protected Set<String> paths = new HashSet<String>();

	    public void init() {
	    	
	    	
	    	
	    	editableCells = new ArrayList<AbstractEditableCell<?, ?>>();

//	        paths.add("id");
//	        tableEvent.addColumn(new TextColumn<QuestionProxy>() {
	//
//	            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {
	//
//	                public String render(java.lang.Long obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
	//
//	            @Override
//	            public String getValue(QuestionProxy object) {
//	                return renderer.render(object.getId());
//	            }
//	        }, "Id");
//	        paths.add("version");
//	        tableEvent.addColumn(new TextColumn<QuestionProxy>() {
	//
//	            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
	//
//	                public String render(java.lang.Integer obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
	//
//	            @Override
//	            public String getValue(QuestionProxy object) {
//	                return renderer.render(object.getVersion());
//	            }
//	        }, "Version");
//	        paths.add("eventName");
//	        tableEvent.addColumn(new TextColumn<QuestionProxy>() {
	//
//	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
	//
//	                public String render(java.lang.String obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
	//
//	            @Override
//	            public String getValue(QuestionProxy object) {
//	                return renderer.render(object.getEventName());
//	            }
//	        }, "Event Name");
//	        paths.add("institution");
//	        tableEvent.addColumn(new TextColumn<QuestionProxy>() {
	//
//	            Renderer<medizin.client.managed.request.InstitutionProxy> renderer = medizin.client.ui.view.roo.InstitutionProxyRenderer.instance();
	//
//	            @Override
//	            public String getValue(QuestionProxy object) {
//	                return renderer.render(object.getInstitution());
//	            }
//	        }, "Institution");
	    	
	        paths.add("eventName");
	        tableEvent.addColumn(new TextColumn<QuestionProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(QuestionProxy object) {
	                return renderer.render(object.getQuestionText());
	            }
	        }, "Name des Themenbereichs");
	        
	    	addColumn(new ActionCell<QuestionProxy>(
	    	        "Lesen", new ActionCell.Delegate<QuestionProxy>() {
	    	            public void execute(QuestionProxy question) {
	    	              Log.debug("You clicked " + question.getQuestionText());
	    	              delegate.addClicked(medizin.client.shared.AccessRights.AccRead, question);
	    	            }
	    	          }), "Lesend", new GetValue<QuestionProxy>() {
	    	        public QuestionProxy getValue(QuestionProxy contact) {
	    	          return contact;
	    	        }
	    	      }, null);
	    	
	    	addColumn(new ActionCell<QuestionProxy>(
	    	        "Schreiben", new ActionCell.Delegate<QuestionProxy>() {
	    	            public void execute(QuestionProxy question) {
	    	              Log.debug("You clicked " + question.getQuestionText());
	    	              delegate.addClicked(medizin.client.shared.AccessRights.AccWrite, question);
	    	            }
	    	          }), "Schreiben", new GetValue<QuestionProxy>() {
	    	        public QuestionProxy getValue(QuestionProxy contact) {
	    	          return contact;
	    	        }
	    	      }, null);
	    	addColumn(new ActionCell<QuestionProxy>(
	    	        "Antw. hinz.", new ActionCell.Delegate<QuestionProxy>() {
	    	            public void execute(QuestionProxy question) {
	    	              Log.debug("You clicked " + question.getQuestionText());
	    	              delegate.addClicked(medizin.client.shared.AccessRights.AccAddAnswers, question);
	    	            }
	    	          }), "Anworten hinzufügen", new GetValue<QuestionProxy>() {
	    	        public QuestionProxy getValue(QuestionProxy contact) {
	    	          return contact;
	    	        }
	    	      }, null);

	
	        
	        /*searchInstitution.addChangeHandler(new ChangeHandler(){

				@Override
				public void onChange(ChangeEvent event) {
					int selectedIndex = searchInstitution.getSelectedIndex();
					if (selectedIndex >= 0){
						delegate.filterInstitutionQuestionChanged(searchInstitution.getValue(selectedIndex));
					}
					
					
				}
	        	
	        });
	        
	        searchEvent.addChangeHandler(new ChangeHandler(){

				@Override
				public void onChange(ChangeEvent event) {
					int selectedIndex = searchEvent.getSelectedIndex();
					if (selectedIndex >= 0){
						delegate.filterEventQuestionChanged(searchEvent.getValue(selectedIndex));
					}
					
					
				}
	        	
	        });*/
	    	
	    	searchInstitution.addHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
					if(searchInstitution.getSelected() != null)
						delegate.filterInstitutionQuestionChanged(searchInstitution.getSelected().getId());
					else
						delegate.filterInstitutionQuestionChanged(null);
				}
			});
	    	
	    	searchEvent.addHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
					if (searchEvent.getSelected() != null)
						delegate.filterEventQuestionChanged(searchEvent.getSelected().getId());
					else
						delegate.filterEventQuestionChanged(null);
				}
			});
	    	
	        searchQuestion.addKeyUpHandler(new KeyUpHandler(){

				@Override
				public void onKeyUp(KeyUpEvent event) {
					delegate.filterQuestionChanged(searchQuestion.getText());
					
				}


				});
	        
	        checkQuestiontext.setValue(true);
	        
	        checkQuestiontext.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					delegate.filterSearchTextChanged(checkQuestiontext.getValue());
					
				}
	        	
	        });
	        
	        checkKeywords.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					delegate.filterSearchKeywordChanged(checkKeywords.getValue());
					
				}
	        	
	        });
	        
	        
	        
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
	      final GetValue<C> getter, FieldUpdater<QuestionProxy, C> fieldUpdater) {
	    Column<QuestionProxy, C> column = new Column<QuestionProxy, C>(cell) {
	      @Override
	      public C getValue(QuestionProxy object) {
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
	    C getValue(QuestionProxy contact);
	  }
	
	  
	  @UiField(provided = true)
			CellTable<QuestionProxy> tableEvent;

			@UiField(provided = true)
			public SimplePager pager;

			
			
	/*@UiField
	CellTable<QuestionProxy> tableEvent;*/

   
	/*@UiField
	ListBox searchInstitution;
	@UiField
	ListBox searchEvent;*/
	
	@UiField
	DefaultSuggestBox<InstitutionProxy, EventHandlingValueHolderItem<InstitutionProxy>> searchInstitution;

	@UiField
	DefaultSuggestBox<QuestionEventProxy, EventHandlingValueHolderItem<QuestionEventProxy>> searchEvent;
	
	@UiField
    TextBox searchQuestion;
	@UiField
	CheckBox checkQuestiontext;
	@UiField
	CheckBox checkKeywords;	
	
	@Override
	public DefaultSuggestBox<InstitutionProxy, EventHandlingValueHolderItem<InstitutionProxy>> getSearchInstitution(){
		return searchInstitution;
	}
	
	@Override
	public DefaultSuggestBox<QuestionEventProxy, EventHandlingValueHolderItem<QuestionEventProxy>> getSearchEvent(){
		return searchEvent;
	}

	private Delegate delegate;

	@Override
	public CellTable<QuestionProxy> getTable() {
		
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
		setWidth("800px");
		center();
		show();
		
	}

}
