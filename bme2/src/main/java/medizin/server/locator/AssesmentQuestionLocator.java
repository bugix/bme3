package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.AssesmentQuestion;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.AssesmentQuestion")
@Component
public class AssesmentQuestionLocator extends Locator<AssesmentQuestion, Long> {

    public AssesmentQuestion create(Class<? extends medizin.server.domain.AssesmentQuestion> clazz) {
        return new AssesmentQuestion();
    }

    public AssesmentQuestion find(Class<? extends medizin.server.domain.AssesmentQuestion> clazz, Long id) {
        return AssesmentQuestion.findAssesmentQuestion(id);
    }

    public Class<medizin.server.domain.AssesmentQuestion> getDomainType() {
        return AssesmentQuestion.class;
    }

    public Long getId(AssesmentQuestion assesmentQuestion) {
        return assesmentQuestion.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(AssesmentQuestion assesmentQuestion) {
        return assesmentQuestion.getVersion();
    }
}
