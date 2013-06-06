package medizin.client.ui.view.assesment;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.TabPanelHelper;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class AssesmentDetailsViewImpl extends Composite implements AssesmentDetailsView  {

	private static AssesmentDetailsViewImplUiBinder uiBinder = GWT
			.create(AssesmentDetailsViewImplUiBinder.class);

	interface AssesmentDetailsViewImplUiBinder extends
			UiBinder<Widget, AssesmentDetailsViewImpl> {
	}
	
	public BmeConstants constants = GWT.create(BmeConstants.class);

	private Presenter presenter;

	
	@UiField
	TabPanel assessmentDetailPanel;
	
    @UiField
    IconButton edit;

    @UiField
    IconButton delete;

    private Delegate delegate;
	

    @UiField
    SpanElement name;
    
    @UiField
    SpanElement exam;
    
    @UiField
    SpanElement dateOfAssesment;

    @UiField
    SpanElement dateOpen;

    @UiField
    SpanElement dateClosed;

    @UiField
    SpanElement place;

    @UiField
    SpanElement logo;
    
    @UiField
    SpanElement lblbeforeClosing;
    
    @UiField
    SpanElement rememberBeforeClosing;

    @UiField
    SpanElement isClosed;

    @UiField
    SpanElement assesmentVersion;

    @UiField
    SpanElement mc;

    @UiField
    SpanElement repeFor;

    @UiField
    SpanElement percentSameQuestion;

    AssesmentProxy proxy;

    @UiField
	QuestionTypeCountViewImpl questionTypeCountViewImpl;


    @UiField
    StudentViewImpl studentViewImpl;
//    @UiField
//    EventAccessViewImpl eventAccessView;
//    
//    @UiField
//    QuestionAccessViewImpl questionAccessView;
    
    
//	@Override
//    public EventAccessViewImpl getEventAccessView(){
//    	return eventAccessView;
//    }


    @UiField
    DisclosurePanel studentPanel;
    
    @UiHandler("delete")
    public void onDeleteClicked(ClickEvent e) {
        delegate.deleteClicked();
    }

    @UiHandler("edit")
    public void onEditClicked(ClickEvent e) {
        delegate.editClicked();
    }

//    @UiField
//    SpanElement displayRenderer;

    public void setValue(AssesmentProxy proxy) {
        this.proxy = proxy;
        
        assessmentDetailPanel.selectTab(0);
        assessmentDetailPanel.getTabBar().setTabText(0, constants.manageAssessment());
		TabPanelHelper.moveTabBarToBottom(assessmentDetailPanel);
        
        name.setInnerText(proxy.getName() == null ? "" : String.valueOf(proxy.getName()));
        dateOfAssesment.setInnerText(proxy.getDateOfAssesment() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(proxy.getDateOfAssesment()));
        dateOpen.setInnerText(proxy.getDateOpen() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(proxy.getDateOpen()));
        dateClosed.setInnerText(proxy.getDateClosed() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(proxy.getDateClosed()));
        place.setInnerText(proxy.getPlace() == null ? "" : String.valueOf(proxy.getPlace()));
        logo.setInnerText(proxy.getLogo() == null ? "" : String.valueOf(proxy.getLogo()));
        rememberBeforeClosing.setInnerText(proxy.getRememberBeforeClosing() == null ? "" : String.valueOf(proxy.getRememberBeforeClosing()));
       
        isClosed.setInnerText(proxy.getIsClosed() == null ? "" : (proxy.getIsClosed()? "ja":"nein"));
        assesmentVersion.setInnerText(proxy.getAssesmentVersion() == null ? "" : String.valueOf(proxy.getAssesmentVersion()));
        mc.setInnerText(proxy.getMc() == null ? "" : medizin.client.ui.view.roo.McProxyRenderer.instance().render(proxy.getMc()));
        repeFor.setInnerText(proxy.getRepeFor() == null ? "" : medizin.client.ui.view.roo.AssesmentProxyRenderer.instance().render(proxy.getRepeFor()));
        percentSameQuestion.setInnerText(proxy.getPercentSameQuestion() == null ? "" : String.valueOf(proxy.getPercentSameQuestion()));
      //  displayRenderer.setInnerText(medizin.client.ui.view.roo.AssesmentProxyRenderer.instance().render(proxy));
       
       
    }
    
    @UiField
    SpanElement mclbl;
    
    @UiField
    SpanElement openFromLbl;
    
    @UiField
    SpanElement closedFromLbl;
    
    @UiField
    SpanElement venueLbl;
    
    @UiField
    SpanElement completedLbl;
    
    @UiField
    SpanElement logoForTitlePageLbl;
    
    @UiField
    SpanElement repeForLbl;
    
    @UiField
    SpanElement percentSameQuestionLbl;
    
	public AssesmentDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		edit.setText(constants.assessmentProcess());
		delete.setText(constants.assessmentDelete());
		exam.setInnerText(constants.exam());
		mclbl.setInnerText(constants.mc());
		openFromLbl.setInnerText(constants.openFrom());
		closedFromLbl.setInnerText(constants.closedFrom());
		venueLbl.setInnerText(constants.venue());
		completedLbl.setInnerText(constants.completed());
		logoForTitlePageLbl.setInnerText(constants.logoForTitlePage());
		repeForLbl.setInnerText(constants.repeFor());
		percentSameQuestionLbl.setInnerText(constants.percentSameQuestion());
		lblbeforeClosing.setInnerText(constants.rememberExaminerBeforeClosing());
		studentPanel.getHeaderTextAccessor().setText(constants.studentMgt());
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

	@Override
	public QuestionTypeCountViewImpl getQuestionTypeCountViewImpl() {
		// TODO Auto-generated method stub
		return questionTypeCountViewImpl;
	}
	
	@UiField
	QuestionSumPerPersonViewImpl questionSumPerPersonViewImpl;
	
	@Override
	public QuestionSumPerPersonViewImpl getQuestionSumPerPersonViewImpl(){
		return questionSumPerPersonViewImpl;
	}

	public StudentViewImpl getStudentViewImpl() {
		return studentViewImpl;
	}

	public void setStudentViewImpl(StudentViewImpl studentViewImpl) {
		this.studentViewImpl = studentViewImpl;
	}
}
