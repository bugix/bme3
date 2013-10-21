// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.MainQuestionSkill")
@ServiceName("medizin.server.domain.MainQuestionSkill")
public interface MainQuestionSkillRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countMainQuestionSkills();

    abstract Request<java.util.List<medizin.client.proxy.MainQuestionSkillProxy>> findAllMainQuestionSkills();

    abstract Request<java.util.List<medizin.client.proxy.MainQuestionSkillProxy>> findMainQuestionSkillEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.MainQuestionSkillProxy> findMainQuestionSkill(Long id);

    abstract InstanceRequest<medizin.client.proxy.MainQuestionSkillProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.MainQuestionSkillProxy, java.lang.Void> remove();
}
