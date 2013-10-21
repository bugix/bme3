package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.MinorQuestionSkill;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.MinorQuestionSkill")
@Component
public class MinorQuestionSkillLocator extends Locator<MinorQuestionSkill, Long> {

    public MinorQuestionSkill create(Class<? extends medizin.server.domain.MinorQuestionSkill> clazz) {
        return new MinorQuestionSkill();
    }

    public MinorQuestionSkill find(Class<? extends medizin.server.domain.MinorQuestionSkill> clazz, Long id) {
        return MinorQuestionSkill.findMinorQuestionSkill(id);
    }

    public Class<medizin.server.domain.MinorQuestionSkill> getDomainType() {
        return MinorQuestionSkill.class;
    }

    public Long getId(MinorQuestionSkill minorQuestionSkill) {
        return minorQuestionSkill.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(MinorQuestionSkill minorQuestionSkill) {
        return minorQuestionSkill.getVersion();
    }
}
