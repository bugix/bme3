package medizin.client.ui.view.assesment;

import java.util.Collection;
import java.util.Map;

import medizin.client.place.PlaceUserDetails;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.request.PersonRequest;
import medizin.client.ui.view.roo.QuestionAccessSetEditor;
import medizin.client.ui.widget.IconButton;
import medizin.shared.i18n.BmeConstants;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider;

public class AssesmentEditViewImpl extends Composite implements AssesmentEditView, Editor<AssesmentProxy>  {

	private static AssesmentEditViewImplUiBinder uiBinder = GWT
			.create(AssesmentEditViewImplUiBinder.class);

	interface AssesmentEditViewImplUiBinder extends
			UiBinder<Widget, AssesmentEditViewImpl> {
	}
	
	interface EditorDriver extends RequestFactoryEditorDriver<AssesmentProxy, AssesmentEditViewImpl> {}
	// private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
	
    @Override
    public RequestFactoryEditorDriver<AssesmentProxy,AssesmentEditViewImpl> createEditorDriver() {
        RequestFactoryEditorDriver<AssesmentProxy, AssesmentEditViewImpl> driver = GWT.create(EditorDriver.class);
        driver.initialize(this);
        return driver;
    }
    
    public BmeConstants constants = GWT.create(BmeConstants.class);
    
	private Presenter presenter;

	
    @UiField
    IconButton cancel;

    @UiField
    IconButton save;

    private Delegate delegate;
	
    @UiField
    TextBox name;
    
    @UiField
    TextBox logo;

    @UiField
    DateBox dateOfAssesment;

    @UiField
    DateBox dateOpen;

    @UiField
    DateBox dateClosed;

    @UiField
    TextBox place;

    @UiField
    DivElement lblbeforeClosing;
    
    @UiField
    IntegerBox rememberBeforeClosing;

    @UiField(provided = true)
    CheckBox isClosed = new CheckBox() {

        public void setValue(Boolean value) {
            super.setValue(value == null ? Boolean.FALSE : value);
        }
    };



    @UiField(provided = true)
    ValueListBox<McProxy> mc = new ValueListBox<McProxy>(medizin.client.ui.view.roo.McProxyRenderer.instance(), new EntityProxyKeyProvider<medizin.client.proxy.McProxy>());

    @UiField(provided = true)
    ValueListBox<AssesmentProxy> repeFor = new ValueListBox<AssesmentProxy>(medizin.client.ui.view.roo.AssesmentProxyRenderer.instance(), new EntityProxyKeyProvider<medizin.client.proxy.AssesmentProxy>());

    @UiField
    IntegerBox percentSameQuestion;
    
    @Override
    public void setRepeForPickerValues(Collection<AssesmentProxy> values) {
        repeFor.setAcceptableValues(values);
    }

    @Override
    public void setMcPickerValues(Collection<McProxy> values) {
        mc.setAcceptableValues(values);
    }
    
	private AssesmentProxy proxy;

	private McAppRequestFactory requests;


    @UiHandler("cancel")
    void onCancel(ClickEvent event) {
        delegate.cancelClicked();
    }

    @UiHandler("save")
    void onSave(ClickEvent event) {

    	
        delegate.saveClicked();
    }


//    @UiField
//    SpanElement displayRenderer;

    
    @UiField
    DivElement lblQuestionShortName;
    
    @UiField
    DivElement lbldateOfAssesment;
    
    @UiField
    DivElement lbldateOpen;
    
    
    @UiField
    DivElement lbldateClosed;
    
    @UiField
    DivElement lblplace;
    
    
    @UiField 
    DivElement lblisClosed;
    
    @UiField
    DivElement lblmc;
    
    @UiField
    DivElement lblrepeFor;
    
    @UiField
    DivElement lblpercentSameQuestion;
    
    @UiField
    DivElement lbllogo;
    
	public AssesmentEditViewImpl(Map<String, Widget> reciverMap) {
		initWidget(uiBinder.createAndBindUi(this));
		assessmentTabPanel.selectTab(0);
		assessmentTabPanel.getTabBar().setTabText(0, constants.manageAssessment());
		save.setText(constants.saveTest());
		cancel.setText(constants.cancelEdit());
		//lblQuestionShortName.setText(constants.nameOfTest());
		lbldateOfAssesment.setInnerText(constants.willBeHeldOn());
		lbldateOpen.setInnerText(constants.openFrom());
		lbldateClosed.setInnerText(constants.closedFrom());
		lblplace.setInnerText(constants.willTakePlaceIn());
		lblisClosed.setInnerText(constants.isClosed());
		lblmc.setInnerText(constants.mCisOneOf());
		lblrepeFor.setInnerText(constants.repeFor());
		lblpercentSameQuestion.setInnerText(constants.percentSameQuestion());
		lbllogo.setInnerText(constants.logo());
		lblbeforeClosing.setInnerText(constants.rememberExaminerBeforeClosing());
		
		reciverMap.put("name", name);
		reciverMap.put("dateOfAssesment", dateOfAssesment);
		reciverMap.put("dateOpen", dateOpen);
		reciverMap.put("dateClosed", dateClosed);
		reciverMap.put("place", place);
		reciverMap.put("logo", logo);
		reciverMap.put("rememberBeforeClosing", rememberBeforeClosing);
		
	}


	@Override
	public void setName(String helloName) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	@UiField
	SpanElement title;

	@UiField
	TabPanel assessmentTabPanel;
	
	private boolean edit;
	
	@Override
	public void setEditTitle(boolean edit) {
		this.edit = edit;
		if (edit) {
			title.setInnerText(constants.edit());
			// questionTypePanel.getTabBar().setTabText(0, "Edit Question");
			// editTitle.getStyle().clearDisplay();
			// createTitle.getStyle().setDisplay(Display.NONE);
		} else {
			title.setInnerText(constants.create());
			// questionTypePanel.getTabBar().setTabText(0, "New Question");
			// editTitle.getStyle().setDisplay(Display.NONE);
			// createTitle.getStyle().clearDisplay();
		}

	}

}
