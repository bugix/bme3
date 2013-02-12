package medizin.client.ui.view.user;

import java.util.HashSet;
import java.util.Set;

import medizin.client.proxy.InstitutionProxy;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.cell.client.AbstractEditableCell;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class InstituteAccessDialogBoxImpl extends DialogBox implements InstituteAccessDialogBox {

	private static InstituteAccessDialogBoxImplUiBinder uiBinder = GWT
			.create(InstituteAccessDialogBoxImplUiBinder.class);

	interface InstituteAccessDialogBoxImplUiBinder extends
			UiBinder<Widget, InstituteAccessDialogBoxImpl> {
	}

	public InstituteAccessDialogBoxImpl() {
		setWidget(uiBinder.createAndBindUi(this));
	    setGlassEnabled(true);
	    setAnimationEnabled(true);
	    setTitle("Zufriff auf Themenblock hinzufügen");
	    setText("Zufriff auf Themenblock hinzufügen");
	    
	    init();
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
    	              delegate.addClicked(medizin.client.shared.AccessRights.AccRead, questionEvent);
    	            }
    	          }), constants.accessRights(), new GetValue<InstitutionProxy>() {
    	        public InstitutionProxy getValue(InstitutionProxy contact) {
    	          return contact;
    	        }
    	      }, null);
      
    	tableEvent.addColumnStyleName(0, "questionTextColumn");
    	tableEvent.addColumnStyleName(1, "accessRightColumn");
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
		
		@UiField
		CellTable<InstitutionProxy> tableEvent;
		
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
		Button closeButton;
		
		@UiHandler ("closeButton")
		public void onCloseButtonClick(ClickEvent event) {
	            hide();
	    }
}
