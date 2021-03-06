package medizin.client.request;

import java.util.List;

import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.ui.widget.Sorting;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.AssesmentQuestion")
@ServiceName("medizin.server.domain.AssesmentQuestion")
public interface AssesmentQuestionRequest extends AssesmentQuestionRequest_Roo_Gwt {
	
//	   abstract Request<java.lang.Long> countAssesmentQuestions();
//
//	    abstract Request<java.util.List<medizin.client.managed.request.AssesmentQuestionProxy>> findAllAssesmentQuestions();
//
//	    abstract Request<java.util.List<medizin.client.managed.request.AssesmentQuestionProxy>> findAssesmentQuestionEntries(int firstResult, int maxResults);
//
//	    abstract Request<medizin.client.managed.request.AssesmentQuestionProxy> findAssesmentQuestion(Long id);
//
//	    abstract InstanceRequest<medizin.client.managed.request.AssesmentQuestionProxy, java.lang.Void> remove();
//
//	    abstract InstanceRequest<medizin.client.managed.request.AssesmentQuestionProxy, java.lang.Void> persist();
//	    
	abstract Request<java.util.List<medizin.client.proxy.AssesmentQuestionProxy>> findAssesmentQuestionsByQuestionEvent(Long id, Long assesmentId);

	abstract Request<List<AssesmentQuestionProxy>> findAssesmentQuestionsByMc(Long assesmentId,Long id, List<String> criteriaStringList, String questionId, String questionType, String questionName,PersonProxy author);
	abstract Request<List<PersonProxy>> findAuthorListByAssesment(AssesmentProxy assesment);
	abstract Request<List<AssesmentQuestionProxy>> findAssesmentQuestionsByMcProposal(Long id, List<String> encodedStringList, String questionId, String questionType, String questionName);
	abstract Request<List<AssesmentQuestionProxy>> findAssesmentQuestionsByAssesment(Long id,PersonProxy author);
	abstract Request<AssesmentQuestionProxy> copyAssesmentQuestion(Long assementQuestionId, Long assementId,PersonProxy selectedAuthor);

	abstract Request<AssesmentQuestionProxy> findAssesmentQuestionByAssesmentAndQuestion(Long questionId,
			Long assesmentId);

	abstract Request<List<AssesmentQuestionProxy>>  findAssementQuestionForAssementBook(Long id);
	
	abstract Request <List<AssesmentQuestionProxy>> findAssesmentQuestionsByQuestionEventAssIdQuestType(java.lang.Long questEventId, java.lang.Long assesmentId, List<Long> questionTypesId,boolean isVersionA,boolean printAllQuestions);
	
	abstract Request<String> loadTemplate();
//	QuestionTypeCountPerExamRequestNonRoo questionTypeCountPerExamRequest();
//	QuestionSumPerPersonRequestNonRoo questionSumPerPersonRequest();
// AssesmentRequestNonRoo assesmentRequest();
//	AssesmentQuestionRequestNonRoo assesmentQuestionRequest();

	abstract Request<Boolean> sendMail(List<PersonProxy> list, String messageContent,
			String mailSubject, AssesmentProxy activeTab);

	abstract Request<Void> shuffleQuestionsAnswers(Long assessmentID, Boolean disallowSorting);
	
	abstract Request<String> loadSystemOverviewTemplate();

	abstract Request<List<AssesmentQuestionProxy>> findAssessmentQuestionByAssesmentAndAuthor(Long assessmentId, Long authorId);

	abstract Request<Integer> countAllAssesmentQuestionByQuestion(Long questionId);

	abstract Request<List<AssesmentQuestionProxy>> findAllAssesmentQuestionByQuestion(Long questionId, int start, int length);
	
	abstract Request<List<AssesmentQuestionProxy>> findQuestionsByAssesmentRepeFor(Long assessmentId,List<Long> availableQuestionTypeList);
	
	abstract Request<List<Long>> findQuestionsByAssesment(Long assessmentId);
	
	abstract Request<AssesmentQuestionProxy> findPastAssesmentOfQuestion(Long assesmentId,Long questionId);
	
	abstract Request<Integer> countAssessmentQuestionByAssessment(Long assessmentId, List<String> criteriaStringList);
	
	abstract Request<List<AssesmentQuestionProxy>> findAssessmentQuestionByAssessmentForAdmin(Long assessmentId, String sortname,Sorting sortorder,List<String> criteriaStringList, int start, int length);
}
