// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Topic")
@ServiceName("medizin.server.domain.Topic")
public interface TopicRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countTopics();

    abstract Request<java.util.List<medizin.client.proxy.TopicProxy>> findAllTopics();

    abstract Request<java.util.List<medizin.client.proxy.TopicProxy>> findTopicEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.TopicProxy> findTopic(Long id);

    abstract InstanceRequest<medizin.client.proxy.TopicProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.TopicProxy, java.lang.Void> remove();
}
