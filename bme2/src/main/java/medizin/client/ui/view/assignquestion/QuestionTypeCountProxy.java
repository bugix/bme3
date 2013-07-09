package medizin.client.ui.view.assignquestion;

import medizin.client.proxy.QuestionSumPerPersonProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;


public class QuestionTypeCountProxy {

	private QuestionTypeCountPerExamProxy questionTypeCountPerExamProxy;
	
	public QuestionTypeCountPerExamProxy getQuestionTypeCountPerExamProxy() {
		return questionTypeCountPerExamProxy;
	}

	public void setQuestionTypeCountPerExamProxy(
			QuestionTypeCountPerExamProxy questionTypeCountPerExamProxy) {
		this.questionTypeCountPerExamProxy = questionTypeCountPerExamProxy;
	}

	private QuestionSumPerPersonProxy questionSumPerPersonProxy;
	
	private Integer count;

	public QuestionSumPerPersonProxy getQuestionSumPerPersonProxy() {
		return questionSumPerPersonProxy;
	}

	public void setQuestionSumPerPersonProxy(
			QuestionSumPerPersonProxy questionSumPerPersonProxy) {
		this.questionSumPerPersonProxy = questionSumPerPersonProxy;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	private Integer totalQuestionAllowed;
	
	public Integer getTotalQuestionAllowed() {
		return totalQuestionAllowed;
	}

	public void setTotalQuestionAllowed(Integer totalQuestionAllowed) {
		this.totalQuestionAllowed = totalQuestionAllowed;
	}

	public Integer getTotalQuestionAllocated() {
		return totalQuestionAllocated;
	}

	public void setTotalQuestionAllocated(Integer totalQuestionAllocated) {
		this.totalQuestionAllocated = totalQuestionAllocated;
	}

	private Integer totalQuestionAllocated;
	
}
