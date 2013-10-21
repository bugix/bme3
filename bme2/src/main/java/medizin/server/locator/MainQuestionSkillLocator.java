package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.MainQuestionSkill;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.MainQuestionSkill")
@Component
public class MainQuestionSkillLocator extends Locator<MainQuestionSkill, Long> {

    public MainQuestionSkill create(Class<? extends medizin.server.domain.MainQuestionSkill> clazz) {
        return new MainQuestionSkill();
    }

    public MainQuestionSkill find(Class<? extends medizin.server.domain.MainQuestionSkill> clazz, Long id) {
        return MainQuestionSkill.findMainQuestionSkill(id);
    }

    public Class<medizin.server.domain.MainQuestionSkill> getDomainType() {
        return MainQuestionSkill.class;
    }

    public Long getId(MainQuestionSkill mainQuestionSkill) {
        return mainQuestionSkill.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(MainQuestionSkill mainQuestionSkill) {
        return mainQuestionSkill.getVersion();
    }
}
