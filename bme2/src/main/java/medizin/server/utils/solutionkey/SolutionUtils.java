package medizin.server.utils.solutionkey;

import java.util.List;

import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.QuestionEvent;
import medizin.server.domain.QuestionType;
import medizin.server.domain.QuestionTypeCountPerExam;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;


public final class SolutionUtils {

	private final static Function<QuestionType, Long> QUESTIONTYPE_TO_ID = new Function<QuestionType, Long>() {

		@Override
		public Long apply(final QuestionType input) {
			return input.getId();
		}
	};
	
	public static List<AssesmentQuestion> getAssessmentQuestions(Long assessmentId) {
		List<AssesmentQuestion> assesmentQuestions = Lists.newArrayList();
		List<QuestionTypeCountPerExam> typeCountPerExams = QuestionTypeCountPerExam.findQuestionTypesCountSortedByAssesmentNonRoo(assessmentId);
		for (QuestionTypeCountPerExam questionTypeCountPerExam : typeCountPerExams) {
			List<Long> questiontypesIds = FluentIterable.from(questionTypeCountPerExam.getQuestionTypesAssigned()).transform(QUESTIONTYPE_TO_ID).toImmutableList();
			List<QuestionEvent> questionEvents = QuestionEvent.findAllQuestionEventsByQuestionTypeAndAssesmentID(assessmentId, questiontypesIds);
			for (QuestionEvent questionEvent : questionEvents) {
				List<AssesmentQuestion> list = AssesmentQuestion.findAssesmentQuestionsByQuestionEventAssIdQuestType(questionEvent.getId(), assessmentId, questiontypesIds, true, false);
				if(list != null) {
					assesmentQuestions.addAll(list);
				}
			}
		}
		return assesmentQuestions;
	}
}
