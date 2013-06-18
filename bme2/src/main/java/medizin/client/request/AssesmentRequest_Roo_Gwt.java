// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Assesment")
@ServiceName("medizin.server.domain.Assesment")
public interface AssesmentRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countAssesments();

    abstract Request<java.util.List<medizin.client.proxy.AssesmentProxy>> findAllAssesments();

    abstract Request<java.util.List<medizin.client.proxy.AssesmentProxy>> findAssesmentByInsitute(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.AssesmentProxy> findAssesment(Long id);

    abstract InstanceRequest<medizin.client.proxy.AssesmentProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.AssesmentProxy, java.lang.Void> remove();
}
