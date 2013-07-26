package medizin.client.ui.view.assignquestion;

import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.QuestionTextViewDialogBoxImpl;
import medizin.client.ui.view.roo.CollectionRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.util.ClientUtility;
import medizin.client.util.MathJaxs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionViewImpl extends Composite implements QuestionView {

	private Delegate delegate;
	private QuestionProxy question;
	

	private static QuestionViewImplUiBinder uiBinder = GWT
	.create(QuestionViewImplUiBinder.class);

	interface QuestionViewImplUiBinder extends
		UiBinder<Widget, QuestionViewImpl> {
	}

	@UiField
	HTML header;
	
	@UiField
	VerticalPanel answers;
	@UiField
	Label twistieOpen;
	@UiField
	Label twistieClose;
	@UiField
	Label addToAssesment;
	
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
		TableElement questionTable;
		
	@UiField
	IconButton viewHtmlText;
	
	boolean answersVisible=false;
	boolean answersLoaded=false;
	
	@UiHandler(value = { "twistieOpen", "twistieClose" })
	void twistieClicked(ClickEvent event) {
		if(answersVisible==false){
			open();
		}
		else {
			close();
		}
		
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
	
	@UiHandler("viewHtmlText")
	public void viewHtmlTextClicked(ClickEvent event)
	{
		QuestionTextViewDialogBoxImpl dialogBox = new QuestionTextViewDialogBoxImpl();
		
		if (question != null)
		{
			MathJaxs.delayRenderLatexResult(RootPanel.getBodyElement());
			dialogBox.setHtmlText(question.getQuestionText());
			dialogBox.show();
			dialogBox.setWidth(dialogBox.getOffsetWidth() + "px");
			dialogBox.setLeftTopPosition((viewHtmlText.getElement().getAbsoluteLeft()-dialogBox.getOffsetWidth()+30), (viewHtmlText.getAbsoluteTop()+20));
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
			delegate.twistieOpenQuestionClicked(this);
			answersLoaded=true;
		}
		twistieOpen.setVisible(false);
		answers.setVisible(true);
		twistieClose.setVisible(true);
		answersVisible=true;
		detailsTablePanel.setVisible(true);
		questionTable.setClassName("questionTable-open");
	}
	public QuestionViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		twistieClose.setVisible(false);
		answers.setVisible(false);
		questionTable.setClassName("questionTable-close");
	}

	@Override
	public void setProxy(QuestionProxy question) {
		this.question = question;
		header.setHTML(new HTML(ClientUtility.removeMathJax(question.getQuestionText())).getText());
		
        rewiewer.setInnerText(question.getRewiewer() == null ? "" : medizin.client.ui.view.roo.PersonProxyRenderer.instance().render(question.getRewiewer()));
        autor.setInnerText(question.getAutor() == null ? "" : medizin.client.ui.view.roo.PersonProxyRenderer.instance().render(question.getAutor()));
        keywords.setInnerText(question.getKeywords() == null ? "" : CollectionRenderer.of(medizin.client.ui.view.roo.KeywordProxyRenderer.instance()).render(question.getKeywords()));
        questEvent.setInnerText(question.getQuestEvent() == null ? "" : medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance().render(question.getQuestEvent()));
        comment.setInnerText(question.getComment() == null ? "" : medizin.client.ui.view.roo.CommentProxyRenderer.instance().render(question.getComment()));
        questionType.setInnerText(question.getQuestionType() == null ? "" : medizin.client.ui.view.roo.QuestionTypeProxyRenderer.instance().render(question.getQuestionType()));
        
		
	}

	@Override
	public QuestionProxy getProxy() {
		
		return question;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
		
	}

	@Override
	public void addAnswer(AnswerView answer) {
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
	public VerticalPanel getAnswerPanel() {
		return answers;
		
	}
	
	@UiHandler("addToAssesment")
	public void addToAssesmentClicked(ClickEvent event)
	{
		delegate.addNewQuestionToAssesment(this);
	}

}
