// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Skill")
@ServiceName("medizin.server.domain.Skill")
public interface SkillRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countSkills();

    abstract Request<java.util.List<medizin.client.proxy.SkillProxy>> findAllSkills();

    abstract Request<java.util.List<medizin.client.proxy.SkillProxy>> findSkillEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.SkillProxy> findSkill(Long id);

    abstract InstanceRequest<medizin.client.proxy.SkillProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.SkillProxy, java.lang.Void> remove();
}
