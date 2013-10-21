package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.SkillHasAppliance;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.SkillHasAppliance")
@Component
public class SkillHasApplianceLocator extends Locator<SkillHasAppliance, Long> {

    public SkillHasAppliance create(Class<? extends medizin.server.domain.SkillHasAppliance> clazz) {
        return new SkillHasAppliance();
    }

    public SkillHasAppliance find(Class<? extends medizin.server.domain.SkillHasAppliance> clazz, Long id) {
        return SkillHasAppliance.findSkillHasAppliance(id);
    }

    public Class<medizin.server.domain.SkillHasAppliance> getDomainType() {
        return SkillHasAppliance.class;
    }

    public Long getId(SkillHasAppliance skillHasAppliance) {
        return skillHasAppliance.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(SkillHasAppliance skillHasAppliance) {
        return skillHasAppliance.getVersion();
    }
}
