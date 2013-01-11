// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Person")
@ServiceName("medizin.server.domain.Person")
public interface PersonRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countPeople();

    abstract Request<java.util.List<medizin.client.proxy.PersonProxy>> findAllPeople();

    abstract Request<java.util.List<medizin.client.proxy.PersonProxy>> findPersonEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.PersonProxy> findPerson(Long id);

    abstract InstanceRequest<medizin.client.proxy.PersonProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.PersonProxy, java.lang.Void> remove();
}
