package medizin.client.request;

import java.util.List;

import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionEventProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.QuestionEvent")
@ServiceName("medizin.server.domain.QuestionEvent")
public interface QuestionEventRequest extends QuestionEventRequest_Roo_Gwt {
	
Request<java.lang.Long> countQuestionEventsByInstitutionNonRoo(java.lang.Long institutionId);
    
    Request<List<QuestionEventProxy>> findQuestionEventsByInstitutionNonRoo(java.lang.Long institutionId, int firstResult, int maxResults);
    
    Request<List<QuestionEventProxy>> findAllQuestionEventsByQuestionTypeAndAssesmentID(java.lang.Long assesmentId, List<Long> questionTypesId);

    abstract Request<java.util.List<medizin.client.proxy.QuestionEventProxy>> findAllQuestionEvents();

    abstract Request<java.util.List<medizin.client.proxy.QuestionEventProxy>> findQuestionEventEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.QuestionEventProxy> findQuestionEvent(Long id);

    abstract InstanceRequest<medizin.client.proxy.QuestionEventProxy, java.lang.Void> remove();

    abstract InstanceRequest<medizin.client.proxy.QuestionEventProxy, java.lang.Void> persist();

	Request<List<QuestionEventProxy>> findQuestionEventsByPersonNonRoo(Long id,
			int start, int length);

	Request<java.lang.Long>  countQuestionEventsByPersonNonRoo(Long id);

	Request<java.lang.Long>  countQuestionEventsByInstitutionOrEvent(
			Long institutionId, String eventNameFilter);
	
	Request<List<QuestionEventProxy>>   findQuestionEventsByInstitutionOrEvent(
			Long institutionId, String eventNameFilter, int firstResult, int maxResults);
	
	Request<List<QuestionEventProxy>> findQuestionEventByInstitution(InstitutionProxy institution);
	
	Request<List<QuestionEventProxy>> findQuestionEventByInstitutionAndAccRights(Boolean isAdmin, Long personId, Long instId);
	
	abstract Request<List<QuestionEventProxy>> findAllQuestionEventByLoggedPerson();
}
