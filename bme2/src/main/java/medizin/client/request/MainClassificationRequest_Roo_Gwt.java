// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.MainClassification")
@ServiceName("medizin.server.domain.MainClassification")
public interface MainClassificationRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countMainClassifications();

    abstract Request<java.util.List<medizin.client.proxy.MainClassificationProxy>> findAllMainClassifications();

    abstract Request<java.util.List<medizin.client.proxy.MainClassificationProxy>> findMainClassificationEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.MainClassificationProxy> findMainClassification(Long id);

    abstract InstanceRequest<medizin.client.proxy.MainClassificationProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.MainClassificationProxy, java.lang.Void> remove();
}
