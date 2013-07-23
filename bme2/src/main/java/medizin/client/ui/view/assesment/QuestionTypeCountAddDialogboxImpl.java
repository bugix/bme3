package medizin.client.ui.view.assesment;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.view.renderer.EnumRenderer;
import medizin.client.ui.view.roo.QuestionTypeSetEditor;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.shared.BlockingTypes;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

public class QuestionTypeCountAddDialogboxImpl extends DialogBox implements QuestionTypeCountAddDialogbox, Editor<QuestionTypeCountPerExamProxy> {

	private static EventAccessDialogboxImplUiBinder uiBinder = GWT
			.create(EventAccessDialogboxImplUiBinder.class);

	interface EventAccessDialogboxImplUiBinder extends
			UiBinder<Widget, QuestionTypeCountAddDialogboxImpl> {
	}
	
	private BmeConstants constants=GWT.create(BmeConstants.class);
	
	private Presenter presenter;
	
	@UiField
	Button closeButton;
	
	@UiField
	DivElement blockingTypeLbl;
	
	@UiField(provided=true)
	ValueListBox<BlockingTypes> blockingType=new ValueListBox<BlockingTypes>(new EnumRenderer<BlockingTypes>());
	
	@UiHandler ("closeButton")
	public void onCloseButtonClick(ClickEvent event) {
            hide();
            
          }

	interface EditorDriver extends RequestFactoryEditorDriver<QuestionTypeCountPerExamProxy, QuestionTypeCountAddDialogboxImpl> {}
	// private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
	
    @Override
    public RequestFactoryEditorDriver<QuestionTypeCountPerExamProxy,QuestionTypeCountAddDialogboxImpl> createEditorDriver() {
        RequestFactoryEditorDriver<QuestionTypeCountPerExamProxy, QuestionTypeCountAddDialogboxImpl> driver = GWT.create(EditorDriver.class);
        driver.initialize(this);
        return driver;
    }
	

	public QuestionTypeCountAddDialogboxImpl() {
		setWidget(uiBinder.createAndBindUi(this));
	    setGlassEnabled(true);
	    setAnimationEnabled(true);
	    setTitle("Anzahl Fragentypen pro Prüfung hinzufügen");
	    setText("Anzahl Fragentypen pro Prüfung hinzufügen");
	    
		super.getCaption().asWidget().addStyleName("confirmbox");
		this.getElement().getStyle().setZIndex(3);
	    
	    init();
	    blockingTypeLbl.setInnerText(constants.blockingType());	   
	    List<BlockingTypes> b=BlockingTypes.getValues();
	    blockingType.setValue(b.get(0));
	    blockingType.setAcceptableValues(b);
	    blockingType.setValue(b.get(0));
	    //blockingType.setValue(BlockingTypes.NON_BLOCLING);
	    
	}

	  private List<AbstractEditableCell<?, ?>> editableCells;
	  
	 protected Set<String> paths = new HashSet<String>();

	    public void init() {
	    	
	 
	        
	    }
	@Override
	public void setPresenter(Presenter presenter) {
	this.presenter = presenter;
		
	}
	
	
	private Delegate delegate;

	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate= delegate;
		
	}

	@Override
	public void display() {
		center();
		show();
		
	}

    @UiField
    QuestionTypeSetEditor questionTypesAssigned;


    @UiField
    IntegerBox questionTypeCount;

    @Override
    public void setQuestionTypesAssignedPickerValues(Collection<QuestionTypeProxy> values) {
        questionTypesAssigned.setAcceptableValues(values);
    }
    @UiField
    Button save;
    
    @UiHandler("save")
    void onSave(ClickEvent event) {
    	
    	if(blockingType.getValue()==null)
    	{
    		ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.blockingTypeNotEmpty());
    		
    		return;
    	}
    	
    	if(questionTypesAssigned.getDisplayedList().size()==0)
    	{
    		ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.questionTypeCountSetNotNull());
    		return;
    	}
        delegate.addClicked();
        hide();
    }



}
