// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Appliance")
@ServiceName("medizin.server.domain.Appliance")
public interface ApplianceRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countAppliances();

    abstract Request<java.util.List<medizin.client.proxy.ApplianceProxy>> findAllAppliances();

    abstract Request<java.util.List<medizin.client.proxy.ApplianceProxy>> findApplianceEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.ApplianceProxy> findAppliance(Long id);

    abstract InstanceRequest<medizin.client.proxy.ApplianceProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.ApplianceProxy, java.lang.Void> remove();
}
