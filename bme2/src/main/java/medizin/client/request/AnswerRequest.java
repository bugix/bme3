package medizin.client.request;

import java.util.List;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.QuestionProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.Answer")
@ServiceName("medizin.server.domain.Answer")
public interface AnswerRequest extends AnswerRequest_Roo_Gwt {
	
	Request<List<AnswerProxy>> findAnswersEntriesByQuestion(Long id, int start, int max);
	Request<Long> contAnswersByQuestion(Long id);
	InstanceRequest<AnswerProxy, Void> eliminateAnswer();
	Request<List<AnswerProxy>>  findAnswersByQuestion(Long id);
	Request<Long> countAnswersNonAcceptedAdminByQuestion(Long id);
	Request<List<AnswerProxy>> findAnswersEntriesNonAcceptedAdminByQuestion(
			Long questionId, Integer start, Integer length);
	Request<List<String>> findAllAnswersPoints(Long id, Long currentAnswerId);

	Request<Boolean> acceptMatrixAnswer(QuestionProxy questionProxy, Boolean isAdmin, Boolean isInstitutionalAdmin);
	
	Request<Long> countAnswerForAcceptQuestion(Long questionId);
	
	Request<List<AnswerProxy>> findAnswerForAcceptQuestion(Long questionId, Integer start, Integer length);
	
	Request<List<Long>> maxDifferenceBetweenAnswerForQuestion(Long answerId, Long questionId);
	
	Request<Boolean> forceAcceptMatrixAnswer(QuestionProxy questionProxy, Boolean isAdmin, Boolean isInstitutionalAdmin);
	
	Request<Long> countAnswersForForceActiveByQuestion(Long questionId);
	
	Request<List<AnswerProxy>> findAnswersForForceActiveByQuestion(Long questionId, Integer start, Integer length);
	
	Request<Long> countAnswerByLoggedUser(boolean isAdminOrInstitutionalAdmin, Long loggedUserId);
	
	Request<Void> acceptAllAnswerClicked(Long questionId);
	
	Request<Void> acceptAllForceActiveAnswer(Long questionId);
}
