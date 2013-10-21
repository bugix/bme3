// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.MinorQuestionSkill")
@ServiceName("medizin.server.domain.MinorQuestionSkill")
public interface MinorQuestionSkillRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countMinorQuestionSkills();

    abstract Request<java.util.List<medizin.client.proxy.MinorQuestionSkillProxy>> findAllMinorQuestionSkills();

    abstract Request<java.util.List<medizin.client.proxy.MinorQuestionSkillProxy>> findMinorQuestionSkillEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.MinorQuestionSkillProxy> findMinorQuestionSkill(Long id);

    abstract InstanceRequest<medizin.client.proxy.MinorQuestionSkillProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.MinorQuestionSkillProxy, java.lang.Void> remove();
}
