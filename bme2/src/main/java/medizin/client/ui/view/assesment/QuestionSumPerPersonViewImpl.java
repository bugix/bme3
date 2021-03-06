package medizin.client.ui.view.assesment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.QuestionSumPerPersonProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class QuestionSumPerPersonViewImpl extends Composite implements  QuestionSumPerPersonView {

	private static QuestionSumPerPersonViewImplUiBinder uiBinder = GWT
			.create(QuestionSumPerPersonViewImplUiBinder.class);

	interface QuestionSumPerPersonViewImplUiBinder extends
			UiBinder<Widget, QuestionSumPerPersonViewImpl> {
	}

	public BmeConstants constants = GWT.create(BmeConstants.class);
	
	public QuestionSumPerPersonViewImpl() {
		
		CellTable.Resources tableResources = GWT
				.create(MyCellTableResources.class);
		tableQuestionSumPerPerson = new CellTable<QuestionSumPerPersonProxy>(5,
				tableResources);

		MySimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources,
				true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		newQuestionSumPerPerson.setText(constants.newQuestionSumsPerPersonRecord());
		init();
	}


    private Delegate delegate;
    

	@UiField
    Button newQuestionSumPerPerson;

	
	@UiHandler("newQuestionSumPerPerson")
	void addEventClicked(ClickEvent event) {
		Log.info("event");
		delegate.addNewQuestionSumPerPersonClicked();
	}
	


	private Presenter presenter;

	private String name;




	@Override
	public void setName(String helloName) {
		this.name = name;
		
	}


	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}
	
    @UiField(provided = true)
    CellTable<QuestionSumPerPersonProxy> tableQuestionSumPerPerson;
    
	@UiField(provided = true)
	public MySimplePager pager;
    
    
    protected Set<String> paths = new HashSet<String>();

    public void init() {
    	
    	Log.debug("Im EventViewImpl.init() ");
    	
    	editableCells = new ArrayList<AbstractEditableCell<?, ?>>();

    	
        paths.add("responsiblePerson");
        tableQuestionSumPerPerson.addColumn(new TextColumn<QuestionSumPerPersonProxy>() {

            Renderer<medizin.client.proxy.PersonProxy> renderer = medizin.client.ui.view.roo.PersonProxyRenderer.instance();

            @Override
            public String getValue(QuestionSumPerPersonProxy object) {
            	
                return renderer.render(object == null ? null : object.getResponsiblePerson());
            }
        }, "Verantwortlicher");
        paths.add("questionEvent");
        tableQuestionSumPerPerson.addColumn(new TextColumn<QuestionSumPerPersonProxy>() {

            Renderer<medizin.client.proxy.QuestionEventProxy> renderer = medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance();

            @Override
            public String getValue(QuestionSumPerPersonProxy object) {
                return renderer.render(object == null ? null : object.getQuestionEvent());
            }
        }, "Themenbereich");
  
        paths.add("questionSum");
        tableQuestionSumPerPerson.addColumn(new TextColumn<QuestionSumPerPersonProxy>() {

            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {

                public String render(java.lang.Integer obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(QuestionSumPerPersonProxy object) {
                return renderer.render(object == null ? null : object.getQuestionSum());
            }
        }, new Header<String>(new IconHeader()){

			@Override
			public String getValue() {
				
				return "copy";
			}});
        paths.add("sort_order");
        tableQuestionSumPerPerson.addColumn(new TextColumn<QuestionSumPerPersonProxy>() {

            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {

                public String render(java.lang.Integer obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(QuestionSumPerPersonProxy object) {
                return renderer.render(object == null ? null : object.getSort_order());
            }
        }, new Header<String>(new IconHeader()){

			@Override
			public String getValue() {
				
				return "carat-2-n-s";
			}});
        
    	addColumn(new ActionCell<QuestionSumPerPersonProxy>(
    			McAppConstant.DELETE_ICON, new ActionCell.Delegate<QuestionSumPerPersonProxy>() {
    	            public void execute(QuestionSumPerPersonProxy questionTypeCount) {
    	              delegate.deleteQuestionSumPerPersonClicked(questionTypeCount);
    	            }
    	          }), "", new GetValue<QuestionSumPerPersonProxy>() {
    	        public QuestionSumPerPersonProxy getValue(QuestionSumPerPersonProxy contact) {
    	          return contact;
    	        }
    	      }, null);
    	addColumn(new ActionCell<QuestionSumPerPersonProxy>(
    			McAppConstant.UP_ICON, new ActionCell.Delegate<QuestionSumPerPersonProxy>() {
    	            public void execute(QuestionSumPerPersonProxy questionTypeCount) {
    	              delegate.moveQuestionSumPerPersonUp(questionTypeCount);
    	            }
    	          }), "", new GetValue<QuestionSumPerPersonProxy>() {
    	        public QuestionSumPerPersonProxy getValue(QuestionSumPerPersonProxy contact) {
    	          return contact;
    	        }
    	      }, null);
    	addColumn(new ActionCell<QuestionSumPerPersonProxy>(
    			McAppConstant.DOWN_ICON, new ActionCell.Delegate<QuestionSumPerPersonProxy>() {
    	            public void execute(QuestionSumPerPersonProxy questionTypeCount) {
    	              delegate.moveQuestionSumPerPersonDown(questionTypeCount);
    	            }
    	          }), "", new GetValue<QuestionSumPerPersonProxy>() {
    	        public QuestionSumPerPersonProxy getValue(QuestionSumPerPersonProxy contact) {
    	          return contact;
    	        }
    	      }, null);
    	
    	tableQuestionSumPerPerson.addColumnStyleName(0, "textColumnHalf");
    	tableQuestionSumPerPerson.addColumnStyleName(1, "textColumnHalf");
    	tableQuestionSumPerPerson.addColumnStyleName(2, "iconColumn");	
    	tableQuestionSumPerPerson.addColumnStyleName(3, "iconColumn");
    	tableQuestionSumPerPerson.addColumnStyleName(4, "iconColumn");
    	tableQuestionSumPerPerson.addColumnStyleName(5, "iconColumn");
    	tableQuestionSumPerPerson.addColumnStyleName(6, "iconColumn");

    	tableQuestionSumPerPerson.getColumn(4).setCellStyleNames("cellStyleWithTransparentButton");
    	tableQuestionSumPerPerson.getColumn(5).setCellStyleNames("cellStyleWithTransparentButton");
    	tableQuestionSumPerPerson.getColumn(6).setCellStyleNames("cellStyleWithTransparentButton");
    	
    }
	@Override
	public CellTable<QuestionSumPerPersonProxy> getTable() {

		return tableQuestionSumPerPerson;
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
	      final GetValue<C> getter, FieldUpdater<QuestionSumPerPersonProxy, C> fieldUpdater) {
	    Column<QuestionSumPerPersonProxy, C> column = new Column<QuestionSumPerPersonProxy, C>(cell) {
	      @Override
	      public C getValue(QuestionSumPerPersonProxy object) {
	        return getter.getValue(object);
	      }
	    };
	    column.setFieldUpdater(fieldUpdater);
	    
	    if (cell instanceof AbstractEditableCell<?, ?>) {
	      editableCells.add((AbstractEditableCell<?, ?>) cell);
	    }
	    tableQuestionSumPerPerson.addColumn(column, headerText);
	    
	  }

	  /**
	   * Get a cell value from a record.
	   *
	   * @param <C> the cell type
	   */
	  private static interface GetValue<C> {
	    C getValue(QuestionSumPerPersonProxy contact);
	  }
	  
	  private List<AbstractEditableCell<?, ?>> editableCells;

	  private static class IconHeader extends AbstractCell<String> {


			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					String value, SafeHtmlBuilder sb) {
			      // Always do a null check on the value. Cell widgets can pass null to cells
			      // if the underlying data contains a null, or if the data arrives out of order.
			      
			    	  sb.appendHtmlConstant("<span class=\"ui-icon ui-icon-"+value+"\"></span>");
			
			      
				
			}
		  }

}
