// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.UserAccessRights")
@ServiceName("medizin.server.domain.UserAccessRights")
public interface UserAccessRightsRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countUserAccessRightses();

    abstract Request<java.util.List<medizin.client.proxy.UserAccessRightsProxy>> findAllUserAccessRightses();

    abstract Request<java.util.List<medizin.client.proxy.UserAccessRightsProxy>> findUserAccessRightsEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.UserAccessRightsProxy> findUserAccessRights(Long id);

    abstract InstanceRequest<medizin.client.proxy.UserAccessRightsProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.UserAccessRightsProxy, java.lang.Void> remove();
}
