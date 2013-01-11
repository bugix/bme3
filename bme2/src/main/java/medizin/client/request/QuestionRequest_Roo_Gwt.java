// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Question")
@ServiceName("medizin.server.domain.Question")
public interface QuestionRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countQuestions();

    abstract Request<java.util.List<medizin.client.proxy.QuestionProxy>> findAllQuestions();

    abstract Request<java.util.List<medizin.client.proxy.QuestionProxy>> findQuestionEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.QuestionProxy> findQuestion(Long id);

    abstract InstanceRequest<medizin.client.proxy.QuestionProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.QuestionProxy, java.lang.Void> remove();
}
