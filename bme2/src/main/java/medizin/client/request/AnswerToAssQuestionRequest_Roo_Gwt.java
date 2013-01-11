// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.AnswerToAssQuestion")
@ServiceName("medizin.server.domain.AnswerToAssQuestion")
public interface AnswerToAssQuestionRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countAnswerToAssQuestions();

    abstract Request<java.util.List<medizin.client.proxy.AnswerToAssQuestionProxy>> findAllAnswerToAssQuestions();

    abstract Request<java.util.List<medizin.client.proxy.AnswerToAssQuestionProxy>> findAnswerToAssQuestionEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.AnswerToAssQuestionProxy> findAnswerToAssQuestion(Long id);

    abstract InstanceRequest<medizin.client.proxy.AnswerToAssQuestionProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.AnswerToAssQuestionProxy, java.lang.Void> remove();
}
