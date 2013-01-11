package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.QuestionEvent;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.QuestionEvent")
@Component
public class QuestionEventLocator extends Locator<QuestionEvent, Long> {

    public QuestionEvent create(Class<? extends medizin.server.domain.QuestionEvent> clazz) {
        return new QuestionEvent();
    }

    public QuestionEvent find(Class<? extends medizin.server.domain.QuestionEvent> clazz, Long id) {
        return QuestionEvent.findQuestionEvent(id);
    }

    public Class<medizin.server.domain.QuestionEvent> getDomainType() {
        return QuestionEvent.class;
    }

    public Long getId(QuestionEvent questionEvent) {
        return questionEvent.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(QuestionEvent questionEvent) {
        return questionEvent.getVersion();
    }
}
