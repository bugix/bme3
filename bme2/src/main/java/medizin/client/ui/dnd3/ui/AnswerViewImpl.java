package medizin.client.ui.dnd3.ui;


import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.AnswerToAssQuestionProxy;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
/**
 * Implements View for answers in the drag and drop-UI. 
 */

public class AnswerViewImpl extends HorizontalPanel implements AnswerView {

	   //Label lblAnswerText = new Label();
		
	HorizontalPanel lblAnswerText = new HorizontalPanel();
	Label lblIcon = new Label();

	private AnswerProxy answer;
	private AnswerToAssQuestionProxy answerToAssQueston;
	   
	@Override
	public AnswerToAssQuestionProxy getAnswerToAssQueston() {
		return answerToAssQueston;
	}
	@Override
	public void setAnswerToAssQueston(AnswerToAssQuestionProxy answerToAssQueston) {
		this.answerToAssQueston = answerToAssQueston;
	}

	public AnswerProxy getAnswer() {
		return answer;
	}

	public void setAnswer(AnswerProxy answer) {
		this.answer = answer;
	}

	/**
	 * Class constructor
	 */	
	public AnswerViewImpl() {


	}

	/**
	 * Getters and Setters
	 */
	@Override
	public void setProxy(AnswerProxy answer) {
		this.answer = answer;
		
		 init();
		
	}


	@Override
	public Widget asWidget() {
		
		return super.asWidget();
	}
	

	
	public void answerDropped(EntityProxyId<?> answerId) {
		delegate.answerDropped(answerId);
		
	}
	private Delegate delegate;

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public void setAnswerText(String answerText){
		//lblAnswerText.setText(answerText);
		lblAnswerText.add(new HTML(answerText));
	}
	@Override
	public HorizontalPanel getLblAnswerText(){
		return lblAnswerText;
	}

	public void init(){
		this.add(lblIcon);
		this.setStyleName("answerDND");	
		this.add(lblAnswerText);
		//lblAnswerText.setText(answer.getAnswerText());
		lblAnswerText.add(new HTML(answer.getAnswerText()));
		lblAnswerText.getElement().getStyle().setMarginLeft(5, Unit.PX);
		if (lblIcon.getElement().getParentElement() != null) {
			lblIcon.getElement().getParentElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
		}
		lblAnswerText.setWidth("820px");
		/**
		 * Set icon for Answer via css.
		 */
		switch (answer.getValidity()) {
		case Wahr: ; lblIcon.setStyleName("ui-icon ui-icon-check");	
				     break;
				
		case Falsch: lblIcon.setStyleName("ui-icon ui-icon-minus");
				     break;
				     
		/*case Weil:	lblIcon.setStyleName("ui-icon ui-icon-arrowthick-1-e");
					break;*/
				     
		default:	 lblIcon.setStyleName("ui-icon ui-icon-help");
				     break;
		}
		

	}


}//End class
