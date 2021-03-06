package medizin.client.ui.dnd3.ui;

import medizin.client.proxy.AssesmentQuestionProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

/**
 * Draggable question.
 * @author masterthesis
 *
 */
public class QuestionViewImpl extends VerticalPanel implements QuestionView {
	private static QuestionViewImplUiBinder uiBinder = GWT.create(QuestionViewImplUiBinder.class);

	interface QuestionViewImplUiBinder extends
			UiBinder<Widget, QuestionViewImpl> {
	}



	public QuestionViewImpl() {
		//initWidget(uiBinder.createAndBindUi(this));
		this.questionView = this;
	}

	private AssesmentQuestionProxy questionProxy;
	private VerticalPanel answerVerticalPanel = new VerticalPanel();
	private Integer orderAversion;
	//private Label questionTextLbl = new Label();
	private HorizontalPanel questionTextLbl = new HorizontalPanel();

	private QuestionViewImpl questionView;
	
	
	
	
	//Getters n setters
	
	
	public HorizontalPanel getQuestionTextLbl() {
		return questionTextLbl;
	}

	/*public void setQuestionTextLbl(HorizontalPanel questionTextLbl) {
		this.questionTextLbl = questionTextLbl;
	}*/
	
	@Override
	public AssesmentQuestionProxy getQuestionProxy() {
		return questionProxy;
	}
	@Override
	public void setQuestionProxy(AssesmentQuestionProxy questionProxy) {
		this.questionProxy = questionProxy;		
	}
	
	
	private Delegate delegate;
	private TextBox percentTxt;
	private TextBox pointsTxt;
	private CheckBox eleminateQuestion;
	
	
	@Override
	public VerticalPanel getAnswerVerticalPanel() {
		return answerVerticalPanel;
	}

	@Override
	public void setAnswerVerticalPanel(VerticalPanel answerVerticalPanel) {
		this.answerVerticalPanel = answerVerticalPanel;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	
	@Override
	public void setOrderAversion(Integer orderAversion) {
		this.orderAversion = orderAversion;
		
		
	}
	@Override
	public Integer getOrderAversion() {
		
		return orderAversion;
	}
	/**
	 * Sets up question header and container for answers.
	 */
	public void init(){
		
		this.setOrderAversion(questionProxy.getOrderAversion());
		this.addStyleName("questionVert");

	    
	  
		//set up labels
		questionTextLbl.setVerticalAlignment(ALIGN_MIDDLE);
		questionTextLbl.add(new HTML(questionProxy.getQuestion().getQuestionText()));
		questionTextLbl.setHeight("auto");
		//questionTextLbl.setWidth("860px");
		questionTextLbl.setWidth("720px");
		
	   	final Label twistieOpenQuestion = new Label();
		twistieOpenQuestion.addStyleName("ui-icon ui-icon-triangle-1-e");
		
		final Label twistieCloseQuestion = new Label();
		twistieCloseQuestion.addStyleName("ui-icon ui-icon-triangle-1-s");
		
		HorizontalPanel headerPanel = new HorizontalPanel();
		
		headerPanel.add(twistieOpenQuestion);
		headerPanel.setCellVerticalAlignment(twistieOpenQuestion, HasVerticalAlignment.ALIGN_MIDDLE);
		
		headerPanel.add(twistieCloseQuestion);
		headerPanel.setCellVerticalAlignment(twistieCloseQuestion, HasVerticalAlignment.ALIGN_MIDDLE);		
		twistieCloseQuestion.setVisible(false);
		
		headerPanel.add(questionTextLbl);
		headerPanel.setCellVerticalAlignment(questionTextLbl, HasVerticalAlignment.ALIGN_MIDDLE);
		headerPanel.add(getInputPanel());
		headerPanel.setStyleName("questionHeader");
		headerPanel.setHeight("auto");
		
	    this.add(headerPanel);
	    this.add(answerVerticalPanel);

		twistieOpenQuestion.addClickHandler(new ClickHandler() {

		/**
		 * Twisties for opening and closing questions
		 */
		public	void onClick(ClickEvent event) {
				Log.debug("Twistie Frage anzeigen geklickt!");
				twistieOpenQuestion.setVisible(false);
				twistieCloseQuestion.setVisible(true);								
				delegate.openAssesmentQuestionContainer(questionProxy, questionView);				
				showQuestions();
			}

		});
		
		twistieCloseQuestion.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Log.debug("Twistie Frage schliessen geklickt!");
				twistieOpenQuestion.setVisible(true);
				twistieCloseQuestion.setVisible(false);
				hideQuestions();				
			}

		});
		
		
		
		
		
		
	}//End init

	private HorizontalPanel getInputPanel() {
		HorizontalPanel panel = new HorizontalPanel();
		
		if(questionProxy.getQuestion().getQuestionType().getSumTrueAnswer() != null && questionProxy.getQuestion().getQuestionType().getSumTrueAnswer() != 1) {
			Label percentLbl = new Label("percent");
			panel.add(percentLbl);
			panel.setCellVerticalAlignment(percentLbl, HasVerticalAlignment.ALIGN_MIDDLE);
			panel.setCellHorizontalAlignment(percentLbl, HasHorizontalAlignment.ALIGN_RIGHT);
			percentTxt = new TextBox();
			percentTxt.setWidth("40px");
			percentTxt.setHeight("20px");
			if(questionProxy.getPoints() != null) {
				percentTxt.setText(questionProxy.getPercent());	
			}
			panel.add(percentTxt);
			panel.setCellVerticalAlignment(percentTxt, HasVerticalAlignment.ALIGN_MIDDLE);
			panel.setCellHorizontalAlignment(percentTxt, HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			questionTextLbl.setWidth("825px");
		}
		Label pointsLbl = new Label("points");
		panel.add(pointsLbl);
		pointsTxt = new TextBox();
		pointsTxt.setWidth("40px");
		pointsTxt.setHeight("20px");
		if(questionProxy.getPoints() != null) {
			pointsTxt.setText(questionProxy.getPoints());	
		}
		panel.add(pointsTxt);
		eleminateQuestion = new CheckBox("eleminate question");
		if(questionProxy.getEliminateQuestion() != null) {
			eleminateQuestion.setValue(questionProxy.getEliminateQuestion());
		}
		panel.add(eleminateQuestion);
		panel.setSpacing(4);
		panel.setCellVerticalAlignment(pointsLbl, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setCellVerticalAlignment(pointsTxt, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setCellVerticalAlignment(eleminateQuestion, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setCellHorizontalAlignment(pointsLbl, HasHorizontalAlignment.ALIGN_RIGHT);
		panel.setCellHorizontalAlignment(pointsTxt, HasHorizontalAlignment.ALIGN_RIGHT);
		panel.setCellHorizontalAlignment(eleminateQuestion, HasHorizontalAlignment.ALIGN_RIGHT);
		return panel;
	}

	public void showQuestions(){
		Integer widgetCount = this.getWidgetCount();
			for (int i = 1; i < widgetCount; i++){
			this.getWidget(i).setVisible(true);
		}
	}//End showQuestions
	
	public void hideQuestions(){
	Integer widgetCount = this.getWidgetCount();
		for (int i = 1; i < widgetCount; i++){
			this.getWidget(i).setVisible(false);
		}
	}//End hideQuestions
	
	
	public String getPercentValue() {
		return percentTxt == null? null :percentTxt.getText();
	}
	
	public String getPointsValue() {
		return pointsTxt.getText();
	}
	
	public Boolean getEleminateQuestionValue() {
		return eleminateQuestion.getValue();
	}
}
