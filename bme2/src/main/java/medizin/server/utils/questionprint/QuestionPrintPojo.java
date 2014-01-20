package medizin.server.utils.questionprint;

import java.util.List;

import medizin.server.domain.Answer;
import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.Question;

public class QuestionPrintPojo {
	
	private Question question;
	private String questionText;
	private String mcs;
	private String keywords;
	private String createdDate;
	private String modifiedDate;
	private List<AssesmentQuestion> assessmentQueList;
	private List<String> xAnswerList;
	private List<String> yAnswerList;
	private List<String> validityList;
	private List<Answer> answerList;
	private List<List<String>> matrixAnswerList;
	
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String getMcs() {
		return mcs;
	}
	public void setMcs(String mcs) {
		this.mcs = mcs;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public List<AssesmentQuestion> getAssessmentQueList() {
		return assessmentQueList;
	}
	public void setAssessmentQueList(List<AssesmentQuestion> assessmentQueList) {
		this.assessmentQueList = assessmentQueList;
	}
	public List<String> getxAnswerList() {
		return xAnswerList;
	}
	public void setxAnswerList(List<String> xAnswerList) {
		this.xAnswerList = xAnswerList;
	}
	public List<String> getyAnswerList() {
		return yAnswerList;
	}
	public void setyAnswerList(List<String> yAnswerList) {
		this.yAnswerList = yAnswerList;
	}
	public List<String> getValidityList() {
		return validityList;
	}
	public void setValidityList(List<String> validityList) {
		this.validityList = validityList;
	}
	public List<Answer> getAnswerList() {
		return answerList;
	}
	public void setAnswerList(List<Answer> answerList) {
		this.answerList = answerList;
	}
	public List<List<String>> getMatrixAnswerList() {
		return matrixAnswerList;
	}
	public void setMatrixAnswerList(List<List<String>> matrixAnswerList) {
		this.matrixAnswerList = matrixAnswerList;
	}
	
}
