package medizin.client.ui.view.question;

import medizin.client.ui.view.assesment.AssesmentDetailsView;
import medizin.client.ui.view.assesment.QuestionSumPerPersonViewImpl;
import medizin.client.ui.view.assesment.QuestionTypeCountViewImpl;
import medizin.client.ui.view.assesment.AssesmentDetailsView.Delegate;
import medizin.client.ui.view.assesment.AssesmentDetailsView.Presenter;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class QuestionDetailsViewImpl extends Composite implements QuestionDetailsView  {

	private static AssesmentDetailsViewImplUiBinder uiBinder = GWT
			.create(AssesmentDetailsViewImplUiBinder.class);

	interface AssesmentDetailsViewImplUiBinder extends
			UiBinder<Widget, QuestionDetailsViewImpl> {
	}
	


	private Presenter presenter;

	
    @UiField
    HasClickHandlers edit;

    @UiField
    HasClickHandlers delete;

    private Delegate delegate;
	
//    @UiField
//    SpanElement id;
//
//    @UiField
//    SpanElement version;

    @UiField
    SpanElement dateAdded;

    @UiField
    SpanElement dateChanged;

    @UiField
    SpanElement rewiewer;

    @UiField
    SpanElement autor;

    @UiField
    SpanElement questionText;

    @UiField
    SpanElement picturePath;

    @UiField
    SpanElement questionVersion;

    @UiField
    SpanElement isAcceptedRewiever;

    @UiField
    SpanElement isAcceptedAdmin;

    @UiField
    SpanElement isActive;

    @UiField
    SpanElement previousVersion;

    @UiField
    SpanElement keywords;

    @UiField
    SpanElement questEvent;

    @UiField
    SpanElement comment;

    @UiField
    SpanElement questionType;

    @UiField
    SpanElement mcs;

//    @UiField
//    SpanElement answers;

    QuestionProxy proxy;

    @UiField
    AnswerListViewImpl answerListViewImpl;


    
//    @UiField
//    EventAccessViewImpl eventAccessView;
//    
//    @UiField
//    QuestionAccessViewImpl questionAccessView;
    
    
//	@Override
//    public EventAccessViewImpl getEventAccessView(){
//    	return eventAccessView;
//    }


    @Override
    public AnswerListViewImpl getAnswerListViewImpl() {
		return answerListViewImpl;
	}

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

    @Override
    public void setValue(QuestionProxy proxy) {
        this.proxy = proxy;
//        id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
//        version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
        dateAdded.setInnerText(proxy.getDateAdded() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(proxy.getDateAdded()));
        dateChanged.setInnerText(proxy.getDateChanged() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(proxy.getDateChanged()));
        rewiewer.setInnerText(proxy.getRewiewer() == null ? "" : medizin.client.ui.view.roo.PersonProxyRenderer.instance().render(proxy.getRewiewer()));
        autor.setInnerText(proxy.getAutor() == null ? "" : medizin.client.ui.view.roo.PersonProxyRenderer.instance().render(proxy.getAutor()));
        questionText.setInnerHTML(proxy.getQuestionText() == null ? "" : String.valueOf(proxy.getQuestionText()));
        
        if(proxy.getPicturePath() == null)
        	Document.get().getElementById("picturePath").removeFromParent();
        else 
        	picturePath.setInnerText(String.valueOf(proxy.getPicturePath()));
        
        if(proxy.getQuestionVersion()<2)
        	Document.get().getElementById("previousVersion").removeFromParent();
        else{
        	
        	 previousVersion.setInnerText(proxy.getPreviousVersion() == null ? "" : medizin.client.ui.view.roo.QuestionProxyRenderer.instance().render(proxy.getPreviousVersion()));
        }
        
        questionVersion.setInnerText(String.valueOf(proxy.getQuestionVersion()));
        
        isAcceptedRewiever.setClassName(proxy.getIsAcceptedRewiever() == true ? "ui-icon ui-icon-check" : "ui-icon ui-icon-closethick");
        isAcceptedAdmin.setClassName(proxy.getIsAcceptedAdmin() == true ? "ui-icon ui-icon-check" : "ui-icon ui-icon-closethick");
        isActive.setInnerText(proxy.getIsActive() == true ? "ja" : "nein");
       
        keywords.setInnerText(proxy.getKeywords() == null ? "" : medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.KeywordProxyRenderer.instance()).render(proxy.getKeywords()));
        questEvent.setInnerText(proxy.getQuestEvent() == null ? "" : medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance().render(proxy.getQuestEvent()));
        comment.setInnerText(proxy.getComment() == null ? "" : medizin.client.ui.view.roo.CommentProxyRenderer.instance().render(proxy.getComment()));
        questionType.setInnerText(proxy.getQuestionType() == null ? "" : medizin.client.ui.view.roo.QuestionTypeProxyRenderer.instance().render(proxy.getQuestionType()));
        mcs.setInnerText(proxy.getMcs() == null ? "" : medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.McProxyRenderer.instance()).render(proxy.getMcs()));
//        answers.setInnerText(proxy.getAnswers() == null ? "" : medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.AnswerProxyRenderer.instance()).render(proxy.getAnswers()));
  
    }

	public QuestionDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

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

	




}
