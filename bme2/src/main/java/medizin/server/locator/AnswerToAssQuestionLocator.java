package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.AnswerToAssQuestion;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.AnswerToAssQuestion")
@Component
public class AnswerToAssQuestionLocator extends Locator<AnswerToAssQuestion, Long> {

    public AnswerToAssQuestion create(Class<? extends medizin.server.domain.AnswerToAssQuestion> clazz) {
        return new AnswerToAssQuestion();
    }

    public AnswerToAssQuestion find(Class<? extends medizin.server.domain.AnswerToAssQuestion> clazz, Long id) {
        return AnswerToAssQuestion.findAnswerToAssQuestion(id);
    }

    public Class<medizin.server.domain.AnswerToAssQuestion> getDomainType() {
        return AnswerToAssQuestion.class;
    }

    public Long getId(AnswerToAssQuestion answerToAssQuestion) {
        return answerToAssQuestion.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(AnswerToAssQuestion answerToAssQuestion) {
        return answerToAssQuestion.getVersion();
    }
}
