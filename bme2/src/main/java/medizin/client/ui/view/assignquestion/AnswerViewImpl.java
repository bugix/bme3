package medizin.client.ui.view.assignquestion;

import medizin.client.ui.view.assignquestion.QuestionViewImpl.QuestionViewImplUiBinder;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.AnswerToAssQuestionProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.shared.QuestionTypes;
import medizin.shared.Validity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnswerViewImpl extends Composite implements AnswerView {

	private Delegate delegate;
	private AnswerProxy answer;
	
	private static AnswerViewImplUiBinder uiBinder = GWT
	.create(AnswerViewImplUiBinder.class);

	interface AnswerViewImplUiBinder extends
		UiBinder<Widget, AnswerViewImpl> {
	}
	
	
	
	public AnswerViewImpl(){
		initWidget(uiBinder.createAndBindUi(this));
		
		answerViewClickEvent();
	}

	@UiField
	   HTML lblAnswerText;
	@UiField
	   Label lblIcon;
//	   Validity validity ;
	
	@UiField
	   Label lblCheck;
	@UiField
	   Label lblDel;
	
	@UiField
	HTMLPanel answerPanel;
	
	private boolean checked=false;
	private boolean isInAssesment;
	
	@Override
	public boolean getChecked(){
		return checked;
	}
	
	/*@UiHandler(value = { "lblCheck", "lblDel", "lblIcon" , "lblAnswerText"})
	void lblCheckClicked(ClickEvent event) {
		toggleAnswerSelection();

	}*/
	
	public void toggleAnswerSelection()
	{
		if(isInAssesment){
			if(checked==false){
				checked = true;
				lblCheck.setVisible(false);
				lblDel.setVisible(true);
				DOM.setElementAttribute(answerPanel.getElement(), "style", "background-color: #AAD1C3; border: 1px solid #87B2A0; cursor: pointer;");
			}
			else{
				checked = false;
				lblCheck.setVisible(true);
				lblDel.setVisible(false);		
				DOM.setElementAttribute(answerPanel.getElement(), "style", "background-color: #E89EA2; border: 1px solid #CF7074; cursor: pointer;");
			}
		}
	}
	

	@Override
	public void setProxy(QuestionProxy questionProxy, AnswerProxy answer, boolean addCheck) {
		this.answer = answer;
	

		 init(addCheck, questionProxy);
		
	}
	

	@Override
	public AnswerProxy getProxy() {
		
		return answer;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
		
	}
	
	public void init(boolean addCheck, QuestionProxy questionProxy){
		this.isInAssesment= addCheck;
		//this.add(lblIcon);
		//this.setStyleName("answerDND");	
		//this.add(lblAnswerText);
		
		if (questionProxy != null && questionProxy.getQuestionType() != null && QuestionTypes.Imgkey.equals(questionProxy.getQuestionType().getQuestionType()))
			lblAnswerText.setHTML(answer.getAdditionalKeywords());
		else
			lblAnswerText.setHTML(answer.getAnswerText());
		if(addCheck) {
			lblCheck.setVisible(true);
			DOM.setElementAttribute(answerPanel.getElement(), "style", "cursor: pointer;");
			
		}
		else {
			lblCheck.setVisible(false);
			DOM.setElementAttribute(answerPanel.getElement(), "style", "background-color: #AAD1C3; border: 1px solid #87B2A0; ");
		}
	
		//lblAnswerText.setWidth("100%");
		
		switch (answer.getValidity()) {
		case Wahr: ; lblIcon.addStyleName("ui-icon-check");	
				     break;
				
		case Falsch: lblIcon.addStyleName("ui-icon-close");
				     break;
				     
		/*case Weil:	lblIcon.addStyleName("ui-icon-refresh");
					break;*/
				     
		default:	 lblIcon.addStyleName("ui-icon-help");
				     break;
		}
		

	}

	@Override
	public void setProxy(AssesmentQuestionProxy assesmentQuestionProxy, AnswerToAssQuestionProxy answerToAssQuestionProxy, boolean addCheck) {
		this.answer=answerToAssQuestionProxy.getAnswers();
		if (assesmentQuestionProxy != null)
		{
			init(addCheck, assesmentQuestionProxy.getQuestion());
		}
	}

	private void answerViewClickEvent() {
		sinkEvents(Event.ONCLICK);
		addHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				toggleAnswerSelection();
			}
		}, ClickEvent.getType());
	}

}
