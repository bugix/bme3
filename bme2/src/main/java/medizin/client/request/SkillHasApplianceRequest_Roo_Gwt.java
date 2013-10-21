// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.SkillHasAppliance")
@ServiceName("medizin.server.domain.SkillHasAppliance")
public interface SkillHasApplianceRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countSkillHasAppliances();

    abstract Request<java.util.List<medizin.client.proxy.SkillHasApplianceProxy>> findAllSkillHasAppliances();

    abstract Request<java.util.List<medizin.client.proxy.SkillHasApplianceProxy>> findSkillHasApplianceEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.SkillHasApplianceProxy> findSkillHasAppliance(Long id);

    abstract InstanceRequest<medizin.client.proxy.SkillHasApplianceProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.SkillHasApplianceProxy, java.lang.Void> remove();
}
