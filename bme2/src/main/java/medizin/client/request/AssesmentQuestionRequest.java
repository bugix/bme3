package medizin.client.request;

import java.util.List;

import medizin.client.proxy.AssesmentQuestionProxy;

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

	abstract Request<List<AssesmentQuestionProxy>> findAssesmentQuestionsByMc(Long id);
	abstract Request<List<AssesmentQuestionProxy>> findAssesmentQuestionsByMcProposal(Long id);
	abstract Request<List<AssesmentQuestionProxy>> findAssesmentQuestionsByAssesment(Long id);
	abstract Request<AssesmentQuestionProxy> copyAssesmentQuestion(Long assementQuestionId, Long assementId);

	abstract Request<AssesmentQuestionProxy> findAssesmentQuestionByAssesmentAndQuestion(Long questionId,
			Long assesmentId);

	abstract Request<List<AssesmentQuestionProxy>>  findAssementQuestionForAssementBook(Long id);
	
	abstract Request <List<AssesmentQuestionProxy>> findAssesmentQuestionsByQuestionEventAssIdQuestType(java.lang.Long questEventId, java.lang.Long assesmentId, List<Long> questionTypesId);
	
//	QuestionTypeCountPerExamRequestNonRoo questionTypeCountPerExamRequest();
//	QuestionSumPerPersonRequestNonRoo questionSumPerPersonRequest();
// AssesmentRequestNonRoo assesmentRequest();
//	AssesmentQuestionRequestNonRoo assesmentQuestionRequest();


}
