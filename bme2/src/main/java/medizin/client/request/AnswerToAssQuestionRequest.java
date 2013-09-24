package medizin.client.request;

import java.util.List;

import medizin.client.proxy.AnswerToAssQuestionProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.AnswerToAssQuestion")
@ServiceName("medizin.server.domain.AnswerToAssQuestion")
public interface AnswerToAssQuestionRequest extends AnswerToAssQuestionRequest_Roo_Gwt {
	
	abstract Request<List<AnswerToAssQuestionProxy>> findAnswerToAssQuestionByAnswer(java.lang.Long answerId);
	
	abstract Request<java.lang.Long> countAnswerToAssQuestions();

    abstract Request<java.util.List<medizin.client.proxy.AnswerToAssQuestionProxy>> findAllAnswerToAssQuestions();

    abstract Request<java.util.List<medizin.client.proxy.AnswerToAssQuestionProxy>> findAnswerToAssQuestionEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.AnswerToAssQuestionProxy> findAnswerToAssQuestion(Long id);

    abstract InstanceRequest<medizin.client.proxy.AnswerToAssQuestionProxy, java.lang.Void> remove();

    abstract InstanceRequest<medizin.client.proxy.AnswerToAssQuestionProxy, java.lang.Void> persist();

    abstract Request<List<AnswerToAssQuestionProxy>> findAnswerToAssQuestionByAssesmentQuestion(Long id);

	abstract Request<Long> countAnswerToAssQuestionByAnswer(Long answerId);

	abstract Request<Long> countAnswerToAssQuestionByMatrixValidity(Long questionId);
}
