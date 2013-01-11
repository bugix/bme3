// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.StaticToAssesment")
@ServiceName("medizin.server.domain.StaticToAssesment")
public interface StaticToAssesmentRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countStaticToAssesments();

    abstract Request<java.util.List<medizin.client.proxy.StaticToAssesmentProxy>> findAllStaticToAssesments();

    abstract Request<java.util.List<medizin.client.proxy.StaticToAssesmentProxy>> findStaticToAssesmentEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.StaticToAssesmentProxy> findStaticToAssesment(Long id);

    abstract InstanceRequest<medizin.client.proxy.StaticToAssesmentProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.StaticToAssesmentProxy, java.lang.Void> remove();
}
