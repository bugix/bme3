// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Institution")
@ServiceName("medizin.server.domain.Institution")
public interface InstitutionRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countInstitutions();

    abstract Request<java.util.List<medizin.client.proxy.InstitutionProxy>> findAllInstitutions();

    abstract Request<java.util.List<medizin.client.proxy.InstitutionProxy>> findInstitutionEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.InstitutionProxy> findInstitution(Long id);

    abstract InstanceRequest<medizin.client.proxy.InstitutionProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.InstitutionProxy, java.lang.Void> remove();
}
