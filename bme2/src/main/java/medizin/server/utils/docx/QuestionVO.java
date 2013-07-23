package medizin.server.utils.docx;

import java.util.List;

import medizin.server.domain.AnswerToAssQuestion;
import medizin.server.domain.AssesmentQuestion;

import com.google.common.collect.Lists;

public class QuestionVO {
	private final AssesmentQuestion assesmentQuestion;
	private final List<AnswerToAssQuestion> answerToAssQuestions;

	public QuestionVO(AssesmentQuestion assesmentQuestion, List<AnswerToAssQuestion> answerToAssQuestions) {
		this.assesmentQuestion = assesmentQuestion;
		if (answerToAssQuestions == null) {
			this.answerToAssQuestions = Lists.newArrayList();
		} else {
			this.answerToAssQuestions = answerToAssQuestions;
		}

	}

	public AssesmentQuestion getAssesmentQuestion() {
		return assesmentQuestion;
	}

	public List<AnswerToAssQuestion> getAnswerToAssQuestions() {
		return answerToAssQuestions;
	}
}