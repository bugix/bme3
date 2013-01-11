// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Mc")
@ServiceName("medizin.server.domain.Mc")
public interface McRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countMcs();

    abstract Request<java.util.List<medizin.client.proxy.McProxy>> findAllMcs();

    abstract Request<java.util.List<medizin.client.proxy.McProxy>> findMcEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.McProxy> findMc(Long id);

    abstract InstanceRequest<medizin.client.proxy.McProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.McProxy, java.lang.Void> remove();
}
