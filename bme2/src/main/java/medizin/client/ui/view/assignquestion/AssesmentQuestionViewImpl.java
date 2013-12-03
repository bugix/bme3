package medizin.client.ui.view.assignquestion;


import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.QuestionTextViewDialogBoxImpl;
import medizin.client.ui.view.roo.CollectionRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.util.ClientUtility;
import medizin.client.util.MathJaxs;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
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

	/*@UiField
	Label header;
*/	
	@UiField
	HTML htmlHeader;
	
	@UiField
	VerticalPanel answers;
	@UiField
	Label twistieOpen;
	@UiField
	Label twistieClose;

	@UiField
	Label deleteFromAssesment;
	public Label getDeleteFromAssesment() {
		return deleteFromAssesment;
	}



	public void setDeleteFromAssesment(Label deleteFromAssesment) {
		this.deleteFromAssesment = deleteFromAssesment;
	}

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
	
	  public TableElement getQuestionTable() {
		return questionTable;
	}

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
	    
	 @UiField
	 IconButton viewHtmlText;	    
	
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
		questionTable.addClassName("questionTable-close");
		questionTable.removeClassName("questionTable-open");
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
		questionTable.addClassName("questionTable-open");
		questionTable.removeClassName("questionTable-close");
	}

	public AssesmentQuestionViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		twistieClose.setVisible(false);
		answers.setVisible(false);
		questionTable.setClassName("questionTable-close");
		forceAcceptButton.setText(constants.forceAccept());
		
		viewHtmlText.getElement().getStyle().setMarginRight(5, Unit.PX);
	}
	
	@UiHandler("forceAcceptButton")
	public void forceAcceptButtonClicked(ClickEvent event)
	{
		delegate.forceAccept(this);
	}
	
	@UiHandler("htmlHeader")
	public void headerClicked(ClickEvent event)
	{
		if(answersVisible==false){
			open();
		}
		else {
			close();
		}
		
	}
	
	@UiHandler("viewHtmlText")
	public void viewHtmlTextClicked(ClickEvent event)
	{
		QuestionTextViewDialogBoxImpl dialogBox = new QuestionTextViewDialogBoxImpl();
		
		if (assesmentQuestion != null && assesmentQuestion.getQuestion() != null)
		{
			MathJaxs.delayRenderLatexResult(RootPanel.getBodyElement());
			dialogBox.setHtmlText(assesmentQuestion.getQuestion().getQuestionText());
			dialogBox.show();
			dialogBox.setWidth(dialogBox.getOffsetWidth() + "px");
			dialogBox.setLeftTopPosition((viewHtmlText.getElement().getAbsoluteLeft()-dialogBox.getOffsetWidth()+30), (viewHtmlText.getAbsoluteTop()+20));
		}		
	}
	
	@Override
	public void setProxy(AssesmentQuestionProxy assesmentQuestion, boolean delOrAdd) {
		this.issInAssement=delOrAdd;
		this.assesmentQuestion = assesmentQuestion;
		htmlHeader.setHTML(new HTML(ClientUtility.removeMathJax(assesmentQuestion.getQuestion().getQuestionText())).getText());
		
		if (delOrAdd){
			deleteFromAssesment.removeFromParent();
			//deleteFromAssesment.setVisible(false);
			addToAssesment.setVisible(true); 
		}
		else {
			deleteFromAssesment.setVisible(true); 
			//addToAssesment.setVisible(false);
			addToAssesment.removeFromParent();
		}
		QuestionProxy question = assesmentQuestion.getQuestion();
	     rewiewer.setInnerText(question.getRewiewer() == null ? "" : medizin.client.ui.view.roo.PersonProxyRenderer.instance().render(question.getRewiewer()));
	     autor.setInnerText(question.getAutor() == null ? "" : medizin.client.ui.view.roo.PersonProxyRenderer.instance().render(question.getAutor()));
	     keywords.setInnerText(question.getKeywords() == null ? "" : CollectionRenderer.of(medizin.client.ui.view.roo.KeywordProxyRenderer.instance()).render(question.getKeywords()));
	     questEvent.setInnerText(question.getQuestEvent() == null ? "" : medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance().render(question.getQuestEvent()));
	     comment.setInnerText(question.getComment() == null ? "" : question.getComment());
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
		return htmlHeader;
	}

	@Override
	public void setOpen() {
		open();
		
	}
	
	@UiHandler("addToAssesment")
	public void addToAssesmentButtonClicked(ClickEvent event)
	{
		delegate.addToAssesmentButtonClicked(this);
	}
	
	@UiHandler("deleteFromAssesment")
	public void deleteAssesmentQuestion(ClickEvent event)
	{
		delegate.deleteAssesmentQuestion(this);
	}

}
