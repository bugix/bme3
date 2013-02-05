package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.QuestionResource;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.QuestionResource")
@Component
public class QuestionResourceLocator extends Locator<QuestionResource, Long> {

    public QuestionResource create(Class<? extends medizin.server.domain.QuestionResource> clazz) {
        return new QuestionResource();
    }

    public QuestionResource find(Class<? extends medizin.server.domain.QuestionResource> clazz, Long id) {
        return QuestionResource.findQuestionResource(id);
    }

    public Class<medizin.server.domain.QuestionResource> getDomainType() {
        return QuestionResource.class;
    }

    public Long getId(QuestionResource questionResource) {
        return questionResource.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(QuestionResource questionResource) {
        return questionResource.getVersion();
    }
}
