package medizin.client.ui.view.assignquestion;


import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.roo.CollectionRenderer;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AssesmentQuestionViewImpl extends Composite implements AssesmentQuestionView {

	private Delegate delegate;
	private AssesmentQuestionProxy assesmentQuestion;
	
	public BmeConstants constants = GWT.create(BmeConstants.class);
	
	private static QuestionViewImplUiBinder uiBinder = GWT
	.create(QuestionViewImplUiBinder.class);

	interface QuestionViewImplUiBinder extends
		UiBinder<Widget, AssesmentQuestionViewImpl> {
	}

	@UiField
	Label header;
	
	@UiField
	VerticalPanel answers;
	@UiField
	Label twistieOpen;
	@UiField
	Label twistieClose;

	@UiField
	Label deleteFromAssesment;
	@UiField
	Label addToAssesment;
	
	//visible only to admin / institutional admin for proposed question only
	@UiField
	Button forceAcceptButton;
	
	public Button getForceAcceptButton() {
		return forceAcceptButton;
	}

	@UiField
	TableElement questionTable;
	
	  @UiField
	    SpanElement rewiewer;

	    @UiField
	    SpanElement autor;

	    @UiField
	    SpanElement keywords;

	    @UiField
	    SpanElement questEvent;

	    @UiField
	    SpanElement comment;

	    @UiField
	    SpanElement questionType;
	    
	    @UiField
	    HTMLPanel detailsTablePanel;
	    
	
	boolean answersVisible=false;
	boolean answersLoaded=false;
	private boolean issInAssement;
	
	@UiHandler(value = { "twistieOpen", "twistieClose" })
	void twistieClicked(ClickEvent event) {
		if(answersVisible==false){
			open();
		}
		else {
			close();
		}
		
		
		
	}
	
	
	
	private void close() {
		twistieOpen.setVisible(true);
		answers.setVisible(false);
		twistieClose.setVisible(false);	
		answersVisible=false;	
		detailsTablePanel.setVisible(false);
		questionTable.setClassName("questionTable-close");

	}

	private void open() {
		if(answersLoaded==false){
			delegate.twistieOpenAssQuestionClicked(this, issInAssement);
			answersLoaded=true;
		}
		twistieOpen.setVisible(false);
		answers.setVisible(true);
		twistieClose.setVisible(true);
		answersVisible=true;
		detailsTablePanel.setVisible(true);
		questionTable.setClassName("questionTable-open");
	}

	public AssesmentQuestionViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		twistieClose.setVisible(false);
		answers.setVisible(false);
		questionTable.setClassName("questionTable-close");
		forceAcceptButton.setText(constants.forceAccept());
		
		
	}
	
	@UiHandler("forceAcceptButton")
	public void forceAcceptButtonClicked(ClickEvent event)
	{
		delegate.forceAccept(this);
	}
	
	@UiHandler("header")
	public void headerClicked(ClickEvent event)
	{
		if(answersVisible==false){
			open();
		}
		else {
			close();
		}
		
	}
	
	@Override
	public void setProxy(AssesmentQuestionProxy assesmentQuestion, boolean delOrAdd) {
		this.issInAssement=delOrAdd;
		this.assesmentQuestion = assesmentQuestion;
		header.setText(assesmentQuestion.getQuestion().getQuestionText());
		if (delOrAdd){
			deleteFromAssesment.setVisible(false);
			addToAssesment.setVisible(true); 
		}
		else {
			deleteFromAssesment.setVisible(true); 
			addToAssesment.setVisible(false);
		}
		QuestionProxy question = assesmentQuestion.getQuestion();
	     rewiewer.setInnerText(question.getRewiewer() == null ? "" : medizin.client.ui.view.roo.PersonProxyRenderer.instance().render(question.getRewiewer()));
	     autor.setInnerText(question.getAutor() == null ? "" : medizin.client.ui.view.roo.PersonProxyRenderer.instance().render(question.getAutor()));
	     keywords.setInnerText(question.getKeywords() == null ? "" : CollectionRenderer.of(medizin.client.ui.view.roo.KeywordProxyRenderer.instance()).render(question.getKeywords()));
	     questEvent.setInnerText(question.getQuestEvent() == null ? "" : medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance().render(question.getQuestEvent()));
	     comment.setInnerText(question.getComment() == null ? "" : medizin.client.ui.view.roo.CommentProxyRenderer.instance().render(question.getComment()));
	     questionType.setInnerText(question.getQuestionType() == null ? "" : medizin.client.ui.view.roo.QuestionTypeProxyRenderer.instance().render(question.getQuestionType()));
	      
		
		
	}

	@Override
	public AssesmentQuestionProxy getProxy() {
		
		return assesmentQuestion;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
		
	}

	@Override
	public void addAnswer(AnswerView answer) {
		//answers.add(new HTML(answer.getProxy().getAnswerText()));
		answers.add(answer);
		
	}

	@Override
	public void removeAnswer(AnswerView answer) {
		answers.remove(answer);
		
	}

	@Override
	public Widget getDragControler() {
		return header;
	}

	@Override
	public void setOpen() {
		open();
		
	}

}
