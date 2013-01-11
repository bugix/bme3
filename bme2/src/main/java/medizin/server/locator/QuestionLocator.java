package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.Question;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.Question")
@Component
public class QuestionLocator extends Locator<Question, Long> {

    public Question create(Class<? extends medizin.server.domain.Question> clazz) {
        return new Question();
    }

    public Question find(Class<? extends medizin.server.domain.Question> clazz, Long id) {
        return Question.findQuestion(id);
    }

    public Class<medizin.server.domain.Question> getDomainType() {
        return Question.class;
    }

    public Long getId(Question question) {
        return question.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(Question question) {
        return question.getVersion();
    }
}
