package medizin.client.ui.view.user;

import java.util.HashSet;
import java.util.Set;

import medizin.client.proxy.UserAccessRightsProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.client.ui.widget.process.ApplicationLoadingView;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class QuestionAccessViewImpl extends Composite implements QuestionAccessView {

	private static QuestionAccessViewImplUiBinder uiBinder = GWT
			.create(QuestionAccessViewImplUiBinder.class);

	interface QuestionAccessViewImplUiBinder extends
			UiBinder<Widget, QuestionAccessViewImpl> {
	}

	public QuestionAccessViewImpl() {
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		tableEvent = new CellTable<UserAccessRightsProxy>(McAppConstant.TABLE_PAGE_SIZE,tableResources);

		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources,true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	private BmeConstants constants = GWT.create(BmeConstants.class);

    private Delegate delegate;
    

	@UiField
    IconButton newQuestionAccess;

	@UiField
	ApplicationLoadingView loadingPopup;
	
	@UiHandler("newQuestionAccess")
	void addEventClicked(ClickEvent event) {
		delegate.addNewQuestionAccessClicked();
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
	CellTable<UserAccessRightsProxy> tableEvent;

	@UiField(provided = true)
	public MySimplePager pager;
	
   /* @UiField
    CellTable<QuestionAccessProxy> tableEvent;*/
    
    protected Set<String> paths = new HashSet<String>();

    public void init() {
    	
    	Log.debug("Im EventViewImpl.init() ");
    	

//        paths.add("id");
//        tableEvent.addColumn(new TextColumn<QuestionAccessProxy>() {
//
//            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {
//
//                public String render(java.lang.Long obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(QuestionAccessProxy object) {
//                return renderer.render(object.getId());
//            }
//        }, "Id");
//        paths.add("version");
//        tableEvent.addColumn(new TextColumn<QuestionAccessProxy>() {
//
//            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//
//                public String render(java.lang.Integer obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(QuestionAccessProxy object) {
//                return renderer.render(object.getVersion());
//            }
//        }, "Version");
//        paths.add("eventName");
//        tableEvent.addColumn(new TextColumn<QuestionAccessProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(QuestionAccessProxy object) {
//                return renderer.render(object.getEventName());
//            }
//        }, "Event Name");
//        paths.add("institution");
//        tableEvent.addColumn(new TextColumn<QuestionAccessProxy>() {
//
//            Renderer<medizin.client.managed.request.InstitutionProxy> renderer = medizin.client.ui.view.roo.InstitutionProxyRenderer.instance();
//
//            @Override
//            public String getValue(QuestionAccessProxy object) {
//                return renderer.render(object.getInstitution());
//            }
//        }, "Institution");
    	


        paths.add("questionText");
        tableEvent.addColumn(new TextColumn<UserAccessRightsProxy>() {

            Renderer<UserAccessRightsProxy> renderer = new AbstractRenderer<UserAccessRightsProxy>() {

                public String render(UserAccessRightsProxy obj) {
                	if(obj != null) {
                		return obj.getQuestion().getQuestionText();
                	}
                    return "";
                }
            };

            @Override
            public String getValue(UserAccessRightsProxy object) {
                return renderer.render(object);
            }
        }, constants.question());
        paths.add("accRights");
        tableEvent.addColumn(new TextColumn<UserAccessRightsProxy>() {

            Renderer<UserAccessRightsProxy> renderer = new AbstractRenderer<UserAccessRightsProxy>() {

                public String render(UserAccessRightsProxy obj) {
                    return obj == null ? "" : String.valueOf(obj.getAccRights());
                }
            };

            @Override
            public String getValue(UserAccessRightsProxy object) {
                return renderer.render(object);
            }
        }, constants.privilege());
        
    	addColumn(new ActionCell<UserAccessRightsProxy>(
    	        McAppConstant.DELETE_ICON, new ActionCell.Delegate<UserAccessRightsProxy>() {
    	            public void execute(UserAccessRightsProxy question) {
    	              Log.debug("You clicked " + question.getQuestion().getQuestionText());
    	              delegate.deleteQuestionAccessClicked(question);
    	            }
    	          }), constants.delete(), new GetValue<UserAccessRightsProxy>() {
    	        public UserAccessRightsProxy getValue(UserAccessRightsProxy contact) {
    	          return contact;
    	        }
    	      }, null);
    	
    	tableEvent.addColumnStyleName(0, "questionTextColumn");
    	tableEvent.addColumnStyleName(1, "accessRightColumn");
    	tableEvent.addColumnStyleName(2, "iconColumn");
    }
	@Override
	public CellTable<UserAccessRightsProxy> getTable() {

		return tableEvent;
	}
	@Override
	public String[] getPaths() {
		Log.debug("im EventViewImpl.getPaths");

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
	      final GetValue<C> getter, FieldUpdater<UserAccessRightsProxy, C> fieldUpdater) {
	    Column<UserAccessRightsProxy, C> column = new Column<UserAccessRightsProxy, C>(cell) {
	      @Override
	      public C getValue(UserAccessRightsProxy object) {
	        return getter.getValue(object);
	      }
	    };
	    column.setFieldUpdater(fieldUpdater);
//	    if (cell instanceof AbstractEditableCell<?, ?>) {
//	      editableCells.add((AbstractEditableCell<?, ?>) cell);
//	    }
	    tableEvent.addColumn(column, headerText);
	  }

	  /**
	   * Get a cell value from a record.
	   *
	   * @param <C> the cell type
	   */
	  private static interface GetValue<C> {
	    C getValue(UserAccessRightsProxy contact);
	  }

	@Override
	public ApplicationLoadingView getLoadingPopup() {
		return loadingPopup;
	}
	  
}
