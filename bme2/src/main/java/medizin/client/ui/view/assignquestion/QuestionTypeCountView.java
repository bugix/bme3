package medizin.client.ui.view.assignquestion;

import medizin.client.proxy.QuestionSumPerPersonProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface QuestionTypeCountView extends IsWidget{
	
	interface Delegate {
		
	}
	
	void setDelegate(Delegate delegate);

	public QuestionTypeCountPerExamProxy getQuestionTypeCountPerExamProxy();

	public void setQuestionTypeCountPerExamProxy(
			QuestionTypeCountPerExamProxy questionTypeCountPerExamProxy) ;
	
	public Label getQuestionTypeLbl();

	public Label getBlockingCounter();
	
	public QuestionSumPerPersonProxy getQuestionSumPerPersonProxy();

	public void setQuestionSumPerPersonProxy(
			QuestionSumPerPersonProxy questionSumPerPersonProxy);
}
