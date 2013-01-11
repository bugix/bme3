// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.QuestionType")
@ServiceName("medizin.server.domain.QuestionType")
public interface QuestionTypeRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countQuestionTypes();

    abstract Request<java.util.List<medizin.client.proxy.QuestionTypeProxy>> findAllQuestionTypes();

    abstract Request<java.util.List<medizin.client.proxy.QuestionTypeProxy>> findQuestionTypeEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.QuestionTypeProxy> findQuestionType(Long id);

    abstract InstanceRequest<medizin.client.proxy.QuestionTypeProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.QuestionTypeProxy, java.lang.Void> remove();
}
