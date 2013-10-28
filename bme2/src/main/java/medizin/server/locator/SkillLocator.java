package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.Skill;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.Skill")
@Component
public class SkillLocator extends Locator<Skill, Long> {

    public Skill create(Class<? extends medizin.server.domain.Skill> clazz) {
        return new Skill();
    }

    public Skill find(Class<? extends medizin.server.domain.Skill> clazz, Long id) {
        return Skill.findSkill(id);
    }

    public Class<medizin.server.domain.Skill> getDomainType() {
        return Skill.class;
    }

    public Long getId(Skill skill) {
        return skill.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(Skill skill) {
        return skill.getVersion();
    }
}
