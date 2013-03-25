package medizin.client.request;

import java.util.List;

import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.widget.Sorting;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.QuestionType")
@ServiceName("medizin.server.domain.QuestionType")
public interface QuestionTypeRequest extends QuestionTypeRequest_Roo_Gwt {
	
	abstract Request<Long> countAllQuestionType(String searchValue);
	abstract Request<List<QuestionTypeProxy>> findAllQuestionType(Integer start,Integer end,String sortBy,Sorting sortOrder,String searchValue);
	
	Request<List<QuestionTypeProxy>> findAllQuestionTypesByAssesment(Long assesmentId);
	abstract Request<Long> countQuestionsForQuestionType(Long questionTypeId);
	abstract Request<List<QuestionTypeProxy>> findAllQuestionTypesForInstituteInSession();
	
	//    Request<java.lang.Long> countQuestionEventsByInstitutionNonRoo(java.lang.Long institutionId);
//    
//    Request<List<QuestionEventProxy>> findQuestionEventsByInstitutionNonRoo(java.lang.Long institutionId, int firstResult, int maxResults);
//
//    abstract Request<java.util.List<medizin.client.managed.request.QuestionEventProxy>> findAllQuestionEvents();
//
//    abstract Request<java.util.List<medizin.client.managed.request.QuestionEventProxy>> findQuestionEventEntries(int firstResult, int maxResults);
//
//    abstract Request<medizin.client.managed.request.QuestionEventProxy> findQuestionEvent(Long id);
//
//    abstract InstanceRequest<medizin.client.managed.request.QuestionEventProxy, java.lang.Void> remove();
//
//    abstract InstanceRequest<medizin.client.managed.request.QuestionEventProxy, java.lang.Void> persist();
//
//	Request<List<QuestionEventProxy>> findQuestionEventsByPersonNonRoo(Long id,
//			int start, int length);
//
//	Request<java.lang.Long>  countQuestionEventsByPersonNonRoo(Long id);
//
//	Request<java.lang.Long>  countQuestionEventsByInstitutionOrEvent(
//			Long institutionId, String eventNameFilter);
//	
//	Request<List<QuestionEventProxy>>   findQuestionEventsByInstitutionOrEvent(
//			Long institutionId, String eventNameFilter, int firstResult, int maxResults);
}
