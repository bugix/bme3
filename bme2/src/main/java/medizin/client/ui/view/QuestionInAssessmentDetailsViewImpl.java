package medizin.client.ui.view;

import medizin.client.proxy.QuestionProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionInAssessmentDetailsViewImpl extends Composite implements QuestionInAssessmentDetailsView{

	private static QuestionInAssessmentDetailsViewImplUiBinder uiBinder = GWT
			.create(QuestionInAssessmentDetailsViewImplUiBinder.class);

	interface QuestionInAssessmentDetailsViewImplUiBinder extends
			UiBinder<Widget, QuestionInAssessmentDetailsViewImpl> {
	}
	
	@UiField
	VerticalPanel answerVerticalPanel;
	private Delegate delegate;
	private QuestionProxy proxy;

	public QuestionInAssessmentDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}

	@Override
	public void setValue(QuestionProxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public VerticalPanel getAnswerVerticalPanel() {
		return answerVerticalPanel;
	}



}
