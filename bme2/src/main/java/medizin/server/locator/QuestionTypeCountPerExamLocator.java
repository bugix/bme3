package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.QuestionTypeCountPerExam;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.QuestionTypeCountPerExam")
@Component
public class QuestionTypeCountPerExamLocator extends Locator<QuestionTypeCountPerExam, Long> {

    public QuestionTypeCountPerExam create(Class<? extends medizin.server.domain.QuestionTypeCountPerExam> clazz) {
        return new QuestionTypeCountPerExam();
    }

    public QuestionTypeCountPerExam find(Class<? extends medizin.server.domain.QuestionTypeCountPerExam> clazz, Long id) {
        return QuestionTypeCountPerExam.findQuestionTypeCountPerExam(id);
    }

    public Class<medizin.server.domain.QuestionTypeCountPerExam> getDomainType() {
        return QuestionTypeCountPerExam.class;
    }

    public Long getId(QuestionTypeCountPerExam questionTypeCountPerExam) {
        return questionTypeCountPerExam.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(QuestionTypeCountPerExam questionTypeCountPerExam) {
        return questionTypeCountPerExam.getVersion();
    }
}
