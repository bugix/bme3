package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.Answer;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.Answer")
@Component
public class AnswerLocator extends Locator<Answer, Long> {

    public Answer create(Class<? extends medizin.server.domain.Answer> clazz) {
        return new Answer();
    }

    public Answer find(Class<? extends medizin.server.domain.Answer> clazz, Long id) {
        return Answer.findAnswer(id);
    }

    public Class<medizin.server.domain.Answer> getDomainType() {
        return Answer.class;
    }

    public Long getId(Answer answer) {
        return answer.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(Answer answer) {
        return answer.getVersion();
    }
}
