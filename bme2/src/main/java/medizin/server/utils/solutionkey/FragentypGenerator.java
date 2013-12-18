package medizin.server.utils.solutionkey;

import java.io.PrintWriter;
import java.util.List;

import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.QuestionType;

import org.apache.commons.io.output.ByteArrayOutputStream;

public class FragentypGenerator{
	private final ByteArrayOutputStream os;
	private final PrintWriter out;
	private final String fileName = "Basel Fragentyp.txt";
	private final Long assessmentId;
	
	public FragentypGenerator(Long assessmentId) {
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
		List<AssesmentQuestion> assesmentQuestions = AssesmentQuestion.findAssementQuestionForAssementBookByQuestionTypeAndQuestionEvent(this.assessmentId);
		int i =1;
		for (AssesmentQuestion assesmentQuestion : assesmentQuestions) {
			QuestionType questionType = assesmentQuestion.getQuestion().getQuestionType();
			if(questionType.getSumTrueAnswer() == 1) {
				// single choice (A+ or A-)
				out.println('A');
			} else {
				// Multiple choice (k prim)
				out.println('K');
			}
		
			i++;
		}
		
		if(i < 150) {
			for (; i <= 150; i++) {
				out.println("-");
			}
		}
		out.flush();
		out.close();
	}
}
