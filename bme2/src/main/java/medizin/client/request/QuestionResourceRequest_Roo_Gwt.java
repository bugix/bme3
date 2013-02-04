// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.QuestionResource")
@ServiceName("medizin.server.domain.QuestionResource")
public interface QuestionResourceRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countQuestionResources();

    abstract Request<java.util.List<medizin.client.proxy.QuestionResourceProxy>> findAllQuestionResources();

    abstract Request<java.util.List<medizin.client.proxy.QuestionResourceProxy>> findQuestionResourceEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.QuestionResourceProxy> findQuestionResource(Long id);

    abstract InstanceRequest<medizin.client.proxy.QuestionResourceProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.QuestionResourceProxy, java.lang.Void> remove();
}
