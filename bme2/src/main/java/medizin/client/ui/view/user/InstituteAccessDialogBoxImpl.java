package medizin.client.ui.view.user;

import java.util.HashSet;
import java.util.Set;

import medizin.client.proxy.InstitutionProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.client.ui.widget.process.ApplicationLoadingView;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class InstituteAccessDialogBoxImpl extends DialogBox implements InstituteAccessDialogBox {

	private static InstituteAccessDialogBoxImplUiBinder uiBinder = GWT
			.create(InstituteAccessDialogBoxImplUiBinder.class);

	interface InstituteAccessDialogBoxImplUiBinder extends
			UiBinder<Widget, InstituteAccessDialogBoxImpl> {
	}

	@UiField
	ApplicationLoadingView loadingPopup;
	
	public InstituteAccessDialogBoxImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		tableEvent = new CellTable<InstitutionProxy>(McAppConstant.TABLE_PAGE_SIZE,tableResources);

		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources,true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		setWidget(uiBinder.createAndBindUi(this));
	    setGlassEnabled(true);
	    setAnimationEnabled(true);
	    setTitle(constants.institutionAccess());
	    setText(constants.institutionAccess());
	    
	   /* tableEvent.removeColumnStyleName(1, "cellTableCell");
	    
	    tableEvent.addColumnStyleName(1,"cellStyle" );
	  
	    tableEvent.removeColumnStyleName(0, "cellTableCell");
	    
	    tableEvent.addColumnStyleName(0,"cellStyle" );
	   */
	    
	    init();
	    
	    //tableEvent.getColumn(1).setCellStyleNames("cellStyle");
	    
	    closeButton.setText(constants.close());
	}

	public BmeConstants constants = GWT.create(BmeConstants.class);
	
	public void init()
	{
		paths.add("eventName");
  		tableEvent.addColumn(new TextColumn<InstitutionProxy>() {

            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(InstitutionProxy object) {
                return renderer.render(object.getInstitutionName());
            }
        }, constants.institution());
        
    	addColumn(new ActionCell<InstitutionProxy>(
    	        constants.access(), new ActionCell.Delegate<InstitutionProxy>() {
    	            public void execute(InstitutionProxy questionEvent) {
    	              delegate.addClicked(medizin.shared.AccessRights.AccRead, questionEvent);
    	            }
    	          }), constants.accessRights(), new GetValue<InstitutionProxy>() {
    	        public InstitutionProxy getValue(InstitutionProxy contact) {
    	          return contact;
    	        }
    	      }, null);
      
    	tableEvent.addColumnStyleName(0, "questionTextColumn");
//    	tableEvent.addColumnStyleName(1, "accessRightColumn");
    	
    	tableEvent.addColumnStyleName(1,"tableColumn");
    	
    	
    	searchInstitute.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				delegate.filterInstituteChanged(searchInstitute.getText());
			}
		});
	}

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
		     // editableCells.add((AbstractEditableCell<?, ?>) cell);
		    }
		    tableEvent.addColumn(column, headerText);
		  }

		  /**
		   * Get a cell value from a record.
		   *
		   * @param <C> the cell type
		   */
		  private static interface GetValue<C> {
		    C getValue(InstitutionProxy contact);
		  }
		
		/*@UiField
		CellTable<InstitutionProxy> tableEvent;*/
		
		@UiField(provided = true)
		CellTable<InstitutionProxy> tableEvent;

		@UiField(provided = true)
		public MySimplePager pager;
		
		private Delegate delegate;
		
		protected Set<String> paths = new HashSet<String>();

		@Override
		public CellTable<InstitutionProxy> getTable() {
			
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

		@Override
		public void setPresenter(Presenter activityInstitution) {
			// TODO Auto-generated method stub
			
		}
		
		@UiField
		IconButton closeButton;
		
		@UiField
		TextBox searchInstitute;
		
		@UiHandler ("closeButton")
		public void onCloseButtonClick(ClickEvent event) {
	            hide();
	    }
		
		@Override
		public ApplicationLoadingView getLoadingPopup() {
				return loadingPopup;
			}
		
}
