// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.StaticContent")
@ServiceName("medizin.server.domain.StaticContent")
public interface StaticContentRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countStaticContents();

    abstract Request<java.util.List<medizin.client.proxy.StaticContentProxy>> findAllStaticContents();

    abstract Request<java.util.List<medizin.client.proxy.StaticContentProxy>> findStaticContentEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.StaticContentProxy> findStaticContent(Long id);

    abstract InstanceRequest<medizin.client.proxy.StaticContentProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.StaticContentProxy, java.lang.Void> remove();
}
