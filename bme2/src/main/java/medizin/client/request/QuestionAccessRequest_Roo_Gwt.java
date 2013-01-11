// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.QuestionAccess")
@ServiceName("medizin.server.domain.QuestionAccess")
public interface QuestionAccessRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countQuestionAccesses();

    abstract Request<java.util.List<medizin.client.proxy.QuestionAccessProxy>> findAllQuestionAccesses();

    abstract Request<java.util.List<medizin.client.proxy.QuestionAccessProxy>> findQuestionAccessEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.QuestionAccessProxy> findQuestionAccess(Long id);

    abstract InstanceRequest<medizin.client.proxy.QuestionAccessProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.QuestionAccessProxy, java.lang.Void> remove();
}
