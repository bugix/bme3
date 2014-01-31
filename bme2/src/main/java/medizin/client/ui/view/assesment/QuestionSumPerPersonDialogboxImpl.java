package medizin.client.ui.view.assesment;

import java.util.List;

import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionSumPerPersonProxy;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.process.ApplicationLoadingView;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;


public class QuestionSumPerPersonDialogboxImpl extends DialogBox implements QuestionSumPerPersonDialogbox/*, Editor<QuestionSumPerPersonProxy>*/ {

	private static EventAccessDialogboxImplUiBinder uiBinder = GWT.create(EventAccessDialogboxImplUiBinder.class);

	interface EventAccessDialogboxImplUiBinder extends UiBinder<Widget, QuestionSumPerPersonDialogboxImpl> {}

	public BmeConstants constants = GWT.create(BmeConstants.class);
	
	private Presenter presenter;
	
	@UiField
	Button closeButton;
	
	@UiField
	DivElement responsiblePersonlbl;
	
	@UiField 
	DivElement questionEventlbl;
	
	@UiField
	DivElement questionSumlbl;
	 
	@UiField/*(provided = true)*/
	//ValueListBox<PersonProxy> responsiblePerson = new ValueListBox<PersonProxy>(medizin.client.ui.view.roo.PersonProxyRenderer.instance(), new EntityProxyKeyProvider<medizin.client.proxy.PersonProxy>());
	DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> responsiblePerson;
	
    @UiField/*(provided = true)*/
    //ValueListBox<QuestionEventProxy> questionEvent = new ValueListBox<QuestionEventProxy>(medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance(), new EntityProxyKeyProvider<medizin.client.proxy.QuestionEventProxy>());
    DefaultSuggestBox<QuestionEventProxy, EventHandlingValueHolderItem<QuestionEventProxy>> questionEvent;

    @UiField
    IntegerBox questionSum;
    
    @UiField
    Button save;

    @UiField
	ApplicationLoadingView loadingPopup;
    
    private Delegate delegate;
		
	@UiHandler ("closeButton")
	public void onCloseButtonClick(ClickEvent event) {
		hide();   
    }
	@UiHandler("save")
    void onSave(ClickEvent event) {
		
		if(validateFields()) {
			delegate.addQuestionSumPerPersonClicked(this);	
		}
        
        //hide();
    }
	
	/*interface EditorDriver extends RequestFactoryEditorDriver<QuestionSumPerPersonProxy, QuestionSumPerPersonDialogboxImpl> {}
	// private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
	
    @Override
    public RequestFactoryEditorDriver<QuestionSumPerPersonProxy,QuestionSumPerPersonDialogboxImpl> createEditorDriver() {
        RequestFactoryEditorDriver<QuestionSumPerPersonProxy, QuestionSumPerPersonDialogboxImpl> driver = GWT.create(EditorDriver.class);
        driver.initialize(this);
        return driver;
    }*/
	

	public QuestionSumPerPersonDialogboxImpl() {
		setWidget(uiBinder.createAndBindUi(this));
	    setGlassEnabled(true);
	    setAnimationEnabled(true);
	    super.getCaption().asWidget().addStyleName("confirmbox");
		this.getElement().getStyle().setZIndex(2);
	    setTitle(constants.questionSumPersonTitle());
	    setText(constants.questionSumPersonText());
	    responsiblePersonlbl.setInnerText(constants.responsiblePerson());
	    questionEventlbl.setInnerText(constants.questionEvent());
	    questionSumlbl.setInnerText(constants.questionSum());
	    save.setText(constants.save());
	    closeButton.setText(constants.close());
	}


	@Override
	public void setPresenter(Presenter presenter) {
	this.presenter = presenter;
		
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
	
   /* @Override
    public void setQuestionEventPickerValues(Collection<QuestionEventProxy> values) {
        questionEvent.setAcceptableValues(values);
    }
    @Override
    public void setResponsiblePersonPickerValues(Collection<PersonProxy> values) {
        responsiblePerson.setAcceptableValues(values);
    }*/
    
	@Override
    public void setResponsiblePersonValues(List<PersonProxy> values) {
    	DefaultSuggestOracle<PersonProxy> suggestOracle = (DefaultSuggestOracle<PersonProxy>) responsiblePerson.getSuggestOracle();
		suggestOracle.setPossiblilities(values);
		responsiblePerson.setSuggestOracle(suggestOracle);
		responsiblePerson.setRenderer(medizin.client.ui.view.roo.PersonProxyRenderer.instance());
    }

	@Override
    public void setQuestionEventValues(List<QuestionEventProxy> values) {
    	DefaultSuggestOracle<QuestionEventProxy> suggestOracle = (DefaultSuggestOracle<QuestionEventProxy>) questionEvent.getSuggestOracle();
		suggestOracle.setPossiblilities(values);
		questionEvent.setSuggestOracle(suggestOracle);
		questionEvent.setRenderer(medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance());	
    }
	
	@Override
	public void setValueInProxy(QuestionSumPerPersonProxy questionSumPerPersonProxy) {
		questionSumPerPersonProxy.setQuestionSum(questionSum.getValue());
		questionSumPerPersonProxy.setResponsiblePerson(responsiblePerson.getSelected());
		questionSumPerPersonProxy.setQuestionEvent(questionEvent.getSelected());
	}
	
	private boolean validateFields() {
	
		if(responsiblePerson.getSelected() == null) {
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.responsiblePersonNotEmpty());
			return false;
		}
		
		if(questionEvent.getSelected() == null) {
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.questionEventNotEmpty());
			return false;
		}
		
		if(questionSum.getText() == null || questionSum.getText().isEmpty()) {
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.questionSumNotEmpty());
			return false;
		}
		return true;
	}
	
	@Override
	public ApplicationLoadingView getLoadingPopup() {
			return loadingPopup;
		}	
}
