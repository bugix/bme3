package medizin.client.ui.view.assignquestion;

import medizin.client.proxy.QuestionSumPerPersonProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class QuestionTypeCountViewImpl extends Composite implements  QuestionTypeCountView{

	private Delegate delegate;
	
	private static QuestionTypeCountViewImplUiBinder uiBinder = GWT
			.create(QuestionTypeCountViewImplUiBinder.class);

	interface QuestionTypeCountViewImplUiBinder extends
			UiBinder<Widget, QuestionTypeCountViewImpl> {
	}
	
	private Integer totalQuestionAllowed;
	
	private Integer totalQuestionAllocated;
	
	public Integer getTotalQuestionAllocated() {
		return totalQuestionAllocated;
	}

	public void setTotalQuestionAllocated(Integer totalQuestionAllocated) {
		this.totalQuestionAllocated = totalQuestionAllocated;
	}

	public Integer getTotalQuestionAllowed() {
		return totalQuestionAllowed;
	}

	public void setTotalQuestionAllowed(Integer totalQuestionAllowed) {
		this.totalQuestionAllowed = totalQuestionAllowed;
	}

	private BmeConstants constants=GWT.create(BmeConstants.class);
	
	private QuestionSumPerPersonProxy questionSumPerPersonProxy;
	
	public QuestionSumPerPersonProxy getQuestionSumPerPersonProxy() {
		return questionSumPerPersonProxy;
	}

	public void setQuestionSumPerPersonProxy(
			QuestionSumPerPersonProxy questionSumPerPersonProxy) {
		this.questionSumPerPersonProxy = questionSumPerPersonProxy;
	}

	private QuestionTypeCountPerExamProxy questionTypeCountPerExamProxy;

	
	public QuestionTypeCountPerExamProxy getQuestionTypeCountPerExamProxy() {
		return questionTypeCountPerExamProxy;
	}

	public void setQuestionTypeCountPerExamProxy(
			QuestionTypeCountPerExamProxy questionTypeCountPerExamProxy) {
		this.questionTypeCountPerExamProxy = questionTypeCountPerExamProxy;
	}

	@UiField
	Label questionTypeLbl;
	
	public Label getQuestionTypeLbl() {
		return questionTypeLbl;
	}

	public Label getBlockingCounter() {
		return blockingCounter;
	}

	@UiField
	Label blockingCounter;
	
	public QuestionTypeCountViewImpl()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
		
	}

}
