// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.SkillHasAppliance", locator = "medizin.server.locator.SkillHasApplianceLocator")
@RooGwtProxy(value = "medizin.server.domain.SkillHasAppliance", readOnly = { "version", "id" })
public interface SkillHasApplianceProxy extends EntityProxy {

    abstract Long getId();

    abstract SkillProxy getSkill();

    abstract void setSkill(SkillProxy skill);

    abstract ApplianceProxy getAppliance();

    abstract void setAppliance(ApplianceProxy appliance);

    abstract Integer getVersion();
}
