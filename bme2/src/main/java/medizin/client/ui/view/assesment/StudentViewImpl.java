package medizin.client.ui.view.assesment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.StudentToAssesmentProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.QuestionTextViewDialogBoxImpl;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.process.ApplicationLoadingPopupView;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StudentViewImpl extends Composite implements StudentView {

	private static StudentViewImplUiBinder uiBinder = GWT
			.create(StudentViewImplUiBinder.class);

	interface StudentViewImplUiBinder extends UiBinder<Widget, StudentViewImpl> {
	}
	
	private final BmeConstants constants = GWT.create(BmeConstants.class);
	
	private Delegate delegate;

	private StudentViewImpl StudentSubDetailsViewImpl;
	
	@UiField (provided = true)
	SimplePager pager;

	@UiField (provided = true)
	CellTable<StudentToAssesmentProxy> table;
	
	@UiField
	public Hidden hidden;
	
	@UiField
	FileUpload fileUpload;
	
	@UiField
	FormPanel uploadFormPanel;
	
	@UiField Button importfile;	
	
	@UiField Label fileLabel;
	
	@UiField IconButton fileInfo;

	private List<AbstractEditableCell<?, ?>> editableCells;
	
	protected Set<String> paths = new HashSet<String>();

	public Hidden getHidden() {
		return hidden;
	}

	public void setHidden(Hidden hidden) {
		this.hidden = hidden;
	}

	private Presenter presenter;	
	
	@UiHandler("importfile")
	void importFileClicked(ClickEvent event) {
		uploadFormPanel.submit();
	}
	
	public StudentViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<StudentToAssesmentProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		 uploadFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		 uploadFormPanel.setMethod(FormPanel.METHOD_POST);
		 uploadFormPanel.setAction(GWT.getHostPageBaseURL()+"CsvFileUploadServlet");
		 
		 hidden.setName("hidden");
		 //hidden.setValue(String.valueOf(osceProxy.getId()));
		 importfile.setText(constants.importStudBtn());	 
		 fileLabel.setText(constants.uploadCsvMsg());
		 
		 uploadFormPanel.addSubmitHandler(new FormPanel.SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {			
				
				String fileName = fileUpload.getFilename();

				if(fileName.length() == 0)
	            {
	                event.cancel();
	            }
	            else if(!fileName.endsWith("csv"))
	            {
	                event.cancel();
	            }
	            else
	            {
	            	ApplicationLoadingPopupView.showApplicationLoadingPopup(true);
	            }
			}
		});
		
		uploadFormPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if (event.getResults().contains("false"))
	        	{
					ApplicationLoadingPopupView.showApplicationLoadingPopup(false);
	        		ConfirmationDialogBox.showOkDialogBox(constants.error(), "Upload proper format CSV File");
	        	}
	        	else if (event.getResults().contains("true"))
	        	{
	        		delegate.importClicked();
	        		ApplicationLoadingPopupView.showApplicationLoadingPopup(false);		        		
	        	}
			}
		});
		 
		
		init();
	}
	
	public void init()
	{
		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
		
		paths.add("name");
        table.addColumn(new TextColumn<StudentToAssesmentProxy>() {
            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(StudentToAssesmentProxy object) {
               return renderer.render(object.getStudent() == null ? "" : object.getStudent().getName());
            }
        }, constants.name());
        
        paths.add("prename");
        table.addColumn(new TextColumn<StudentToAssesmentProxy>() {
            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(StudentToAssesmentProxy object) {
               return renderer.render(object.getStudent() == null ? "" : object.getStudent().getPreName());
            }
        }, constants.prename());
        
        
        addColumn(new ValidityTextCell(new ActionCell.Delegate<StudentToAssesmentProxy>() 
            	{
	            public void execute(StudentToAssesmentProxy proxy) {
	            	delegate.deactivateClicked(proxy);
	            }
	          }), "", new GetValue<StudentToAssesmentProxy>() {
	        	public StudentToAssesmentProxy getValue(StudentToAssesmentProxy studentToAssesmentProxy) {
	        		return studentToAssesmentProxy;
	        	}
        }, null);
        
       /* table.addColumn(new Column<StudentToAssesmentProxy, StudentToAssesmentProxy>(new ValidityTextCell()) {
  	      @Override
  	      public StudentToAssesmentProxy getValue(StudentToAssesmentProxy object) {
  	        return object;
  	      }
  	    }, "");*/
	}

	@Override
	public CellTable<StudentToAssesmentProxy> getTable() {
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

	@Override
	public void setPresenter(Presenter systemStartActivity) {
		this.presenter = systemStartActivity;
	}

	 private <C> void addColumn(Cell<C> cell, String headerText, final GetValue<C> getter, FieldUpdater<StudentToAssesmentProxy, C> fieldUpdater) {
	    Column<StudentToAssesmentProxy, C> column = new Column<StudentToAssesmentProxy, C>(cell) {
	      @Override
	      public C getValue(StudentToAssesmentProxy object) {
	        return getter.getValue(object);
	      }
	    };
	    column.setFieldUpdater(fieldUpdater);
	    if (cell instanceof AbstractEditableCell<?, ?>) {
	      editableCells.add((AbstractEditableCell<?, ?>) cell);
	    }
	    table.addColumn(column, headerText);
	 }

		 
	private static interface GetValue<C> {
	    C getValue(StudentToAssesmentProxy contact);
	}
		  
	private static class ValidityTextCell extends ActionCell<StudentToAssesmentProxy> {

		
		public ValidityTextCell(com.google.gwt.cell.client.ActionCell.Delegate<StudentToAssesmentProxy> delegate) {
			super("", delegate);			
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				StudentToAssesmentProxy value, SafeHtmlBuilder sb) {
		      if (value == null) {
		    	  return;
		      }
		      SafeHtml validityIcon;
		      
		      if(value.getIsEnrolled())
		    	  validityIcon = McAppConstant.RIGHT_ICON;
		      else
		    	  validityIcon = McAppConstant.WRONG_ICON;
		      
		      sb.appendHtmlConstant(validityIcon.asString());
		      
			
		}
	}
	
	@UiHandler("fileInfo")
	public void fileInfoClicked(ClickEvent event)
	{
		QuestionTextViewDialogBoxImpl dialogBox = new QuestionTextViewDialogBoxImpl();
		//dialogBox.questionTextHorizontalPanel.add(new Label(constants.studentImportCsvInfo()));
		dialogBox.setHtmlText(constants.studentImportCsvInfo());
		dialogBox.setWidth("500px");
		dialogBox.setPopupPosition(event.getRelativeElement().getAbsoluteLeft()-250, event.getRelativeElement().getAbsoluteTop()+25);
		dialogBox.show();		
	}
}
