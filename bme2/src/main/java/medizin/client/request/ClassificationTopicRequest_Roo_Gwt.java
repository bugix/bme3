// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.ClassificationTopic")
@ServiceName("medizin.server.domain.ClassificationTopic")
public interface ClassificationTopicRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countClassificationTopics();

    abstract Request<java.util.List<medizin.client.proxy.ClassificationTopicProxy>> findAllClassificationTopics();

    abstract Request<java.util.List<medizin.client.proxy.ClassificationTopicProxy>> findClassificationTopicEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.ClassificationTopicProxy> findClassificationTopic(Long id);

    abstract InstanceRequest<medizin.client.proxy.ClassificationTopicProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.ClassificationTopicProxy, java.lang.Void> remove();
}
