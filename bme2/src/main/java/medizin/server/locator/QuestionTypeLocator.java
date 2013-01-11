package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.QuestionType;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.QuestionType")
@Component
public class QuestionTypeLocator extends Locator<QuestionType, Long> {

    public QuestionType create(Class<? extends medizin.server.domain.QuestionType> clazz) {
        return new QuestionType();
    }

    public QuestionType find(Class<? extends medizin.server.domain.QuestionType> clazz, Long id) {
        return QuestionType.findQuestionType(id);
    }

    public Class<medizin.server.domain.QuestionType> getDomainType() {
        return QuestionType.class;
    }

    public Long getId(QuestionType questionType) {
        return questionType.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(QuestionType questionType) {
        return questionType.getVersion();
    }
}
