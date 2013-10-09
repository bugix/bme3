// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Set;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Skill", locator = "medizin.server.locator.SkillLocator")
@RooGwtProxy(value = "medizin.server.domain.Skill", readOnly = { "version", "id" })
public interface SkillProxy extends EntityProxy {

    abstract Long getId();

    abstract Integer getShortcut();

    abstract void setShortcut(Integer shortcut);

    abstract String getDescription();

    abstract void setDescription(String description);

    abstract TopicProxy getTopic();

    abstract void setTopic(TopicProxy topic);

    abstract SkillLevelProxy getSkillLevel();

    abstract void setSkillLevel(SkillLevelProxy skillLevel);

    abstract Set<medizin.client.proxy.SkillHasApplianceProxy> getSkillHasAppliances();

    abstract void setSkillHasAppliances(Set<medizin.client.proxy.SkillHasApplianceProxy> skillHasAppliances);

    abstract Integer getVersion();
}
