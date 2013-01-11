package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.QuestionSumPerPerson;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.QuestionSumPerPerson")
@Component
public class QuestionSumPerPersonLocator extends Locator<QuestionSumPerPerson, Long> {

    public QuestionSumPerPerson create(Class<? extends medizin.server.domain.QuestionSumPerPerson> clazz) {
        return new QuestionSumPerPerson();
    }

    public QuestionSumPerPerson find(Class<? extends medizin.server.domain.QuestionSumPerPerson> clazz, Long id) {
        return QuestionSumPerPerson.findQuestionSumPerPerson(id);
    }

    public Class<medizin.server.domain.QuestionSumPerPerson> getDomainType() {
        return QuestionSumPerPerson.class;
    }

    public Long getId(QuestionSumPerPerson questionSumPerPerson) {
        return questionSumPerPerson.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(QuestionSumPerPerson questionSumPerPerson) {
        return questionSumPerPerson.getVersion();
    }
}
