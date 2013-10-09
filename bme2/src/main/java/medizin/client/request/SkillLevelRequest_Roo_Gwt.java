// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.SkillLevel")
@ServiceName("medizin.server.domain.SkillLevel")
public interface SkillLevelRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countSkillLevels();

    abstract Request<java.util.List<medizin.client.proxy.SkillLevelProxy>> findAllSkillLevels();

    abstract Request<java.util.List<medizin.client.proxy.SkillLevelProxy>> findSkillLevelEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.SkillLevelProxy> findSkillLevel(Long id);

    abstract InstanceRequest<medizin.client.proxy.SkillLevelProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.SkillLevelProxy, java.lang.Void> remove();
}
