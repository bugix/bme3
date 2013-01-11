package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.QuestionAccess;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.QuestionAccess")
@Component
public class QuestionAccessLocator extends Locator<QuestionAccess, Long> {

    public QuestionAccess create(Class<? extends medizin.server.domain.QuestionAccess> clazz) {
        return new QuestionAccess();
    }

    public QuestionAccess find(Class<? extends medizin.server.domain.QuestionAccess> clazz, Long id) {
        return QuestionAccess.findQuestionAccess(id);
    }

    public Class<medizin.server.domain.QuestionAccess> getDomainType() {
        return QuestionAccess.class;
    }

    public Long getId(QuestionAccess questionAccess) {
        return questionAccess.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(QuestionAccess questionAccess) {
        return questionAccess.getVersion();
    }
}
