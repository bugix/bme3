package medizin.server.utils.solutionkey;

import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.QuestionEvent;
import medizin.server.domain.QuestionType;
import medizin.server.domain.QuestionTypeCountPerExam;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class SubscoresGenerator {
	private final ByteArrayOutputStream os;
	private final PrintWriter out;
	private final String fileName = "Basel Subscores.txt";
	private final Long assessmentId;
	private final Function<QuestionType, Long> function = new Function<QuestionType, Long>() {

		@Override
		public Long apply(final QuestionType input) {
			return input.getId();
		}	
	};
	
	public SubscoresGenerator(Long assessmentId) {
		this.assessmentId = assessmentId;
		os = new ByteArrayOutputStream();
		out = new PrintWriter(os);
	}
	
	public byte[] getBytes() {
		return os.toByteArray();
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void generate() {
		
		boolean isVersionA = true;
		boolean printAllQuestions = false;
		int aIndex = 1;
		int kprimIndex = 1;
		
		List<QuestionTypeCountPerExam> countPerExams = QuestionTypeCountPerExam.findQuestionTypesCountSortedByAssesmentNonRoo(assessmentId);
		Set<QuestionType> questionTypes = Sets.newHashSet();
		for (QuestionTypeCountPerExam questionTypeCountPerExam : countPerExams) {
			questionTypes.addAll(questionTypeCountPerExam.getQuestionTypesAssigned());	
		}
			
		List<Long> questionTypeIds = Lists.newArrayList(FluentIterable.from(questionTypes).transform(function).iterator());
		List<QuestionEvent> questionEvents =  QuestionEvent.findAllQuestionEventsByQuestionTypeAndAssesmentID(assessmentId, questionTypeIds);
		
		for (QuestionEvent questionEvent : questionEvents) {
			out.println(questionEvent.getEventName());
			out.println(questionEvent.getEventName());
			List<AssesmentQuestion> assesmentQuestions = AssesmentQuestion.findAssesmentQuestionsByQuestionEventAssIdQuestType(questionEvent.getId(), assessmentId, questionTypeIds,isVersionA,printAllQuestions);
			StringBuilder aBuilder = new  StringBuilder("A,");
			StringBuilder kBuilder = new  StringBuilder("K,");
			StringBuilder rBuilder = new  StringBuilder("R,");
			for (AssesmentQuestion assesmentQuestion : assesmentQuestions) {
				QuestionType questionType = assesmentQuestion.getQuestion().getQuestionType();
				if(questionType.getSumTrueAnswer() == 1) {
					// single choice (A+ or A-)
					aBuilder.append(String.format("%03d", aIndex)).append(",");
					aIndex++;
				} else {
					// Multiple choice (k prim)
					kBuilder.append(String.format("%03d", kprimIndex)).append(",");
					kprimIndex++;
				}
			}
			out.println(aBuilder.toString());
			out.println(kBuilder.toString());
			out.println(rBuilder.toString());
		}
		
		
		out.flush();
		out.close();
	}
}
